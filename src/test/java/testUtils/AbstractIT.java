package testUtils;

import com.pokedex.entity.Db;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.hibernate.transform.Transformers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgresDbConfig.Initializer.class})
@TestPropertySource(properties = {"spring.liquibase.enabled=true"})
public abstract class AbstractIT {

    @Autowired
    protected EntityManager entityManager;
    @Autowired
    protected TransactionTemplate transactionTemplate;
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected TestRestTemplate rest;
    @LocalServerPort
    protected int port;
    @Getter
    protected URL base;
    @Autowired
    protected Db db;

    protected volatile boolean startedUp = false;

    @BeforeEach
    @SneakyThrows
    protected void beforeEach(TestInfo testInfo) {
        if (!startedUp) {
            startedUp = true;
        }
        System.out.println("---- STARTING TEST " + testInfo.getDisplayName() + " ----");
    }

    public <T> List<T> executeSQL(String sql, Class<T> resultClass, Object... paramKeyValueList) {
        verifyKeyValueList(paramKeyValueList);

        Query q = entityManager.createNativeQuery(sql)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(resultClass));

        for (int i = 0; i < paramKeyValueList.length; i += 2) {
            q.setParameter(paramKeyValueList[i].toString(), paramKeyValueList[i + 1]);
        }
        return q.getResultList();
    }

    public void verifyKeyValueList(Object... paramKeyValueList) {
        if (paramKeyValueList.length % 2 != 0) {
            throw new IllegalArgumentException("Missing key or value, <Key,Value> list is expected!");
        }
        val nonStringKeyIndices = IntStream.range(0, paramKeyValueList.length)
                .filter(n -> n % 2 == 0 && !(paramKeyValueList[n] instanceof String))
                .boxed().collect(Collectors.toList());
        if (nonStringKeyIndices.size() > 0) {
            String msg = "<Key,Value> list is expected, where Keys have to be of type String but on indeces:"
                    + nonStringKeyIndices + " different types have been found!";
            throw new IllegalArgumentException(msg);
        }
    }

    public String getJdbcConnectionInfo() {
        val emInfo = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
        val ds = ((HikariDataSource) emInfo.getDataSource());
        return ds.getJdbcUrl() + " User: " + ds.getUsername() + " Password: " + ds.getPassword();
    }

}
