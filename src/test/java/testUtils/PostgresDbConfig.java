package testUtils;


import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.val;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
public class PostgresDbConfig {
    private static String ENV_TESTCONTAINER_PGSQL_PORT = "TESTCONTAINER_PGSQL_PORT";

    @Container
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:11.10");

    static {
        startDatabaseAndInitialize();
    }

    public static void startDatabaseAndInitialize() {
        if (useFixedPort()) { // for local tests
            if (postgres != null) {
                postgres.withExposedPorts(PostgreSQLContainer.POSTGRESQL_PORT)
                        .withCreateContainerCmdModifier(createContainerCmd -> {
                            ((CreateContainerCmd) createContainerCmd).getHostConfig()
                                    .withPortBindings(
                                            new PortBinding(Ports.Binding.bindPort(getFixedPort()),
                                                    new ExposedPort(PostgreSQLContainer.POSTGRESQL_PORT))
                                    );
                        })
                        .start();
            }
        } else { // for paralell tests in jenkins use dynamic ports
            postgres.start();
        }
        return;
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            val url = (postgres == null ? "jdbc:postgresql://localhost:5432/test?loggerLevel=OFF" : postgres.getJdbcUrl());
            val user = (postgres == null ? "test" : postgres.getUsername());
            val password = (postgres == null ? "test" : postgres.getPassword());
            System.out.println("POSTGRES URL: " + url);
            System.out.println("POSTGRES USERNAME: " + user);
            System.out.println("POSTGRES PASSWORD: " + password);
            TestPropertyValues.of(
                    "spring.datasource.url=" + url,
                    "spring.datasource.username=" + user,
                    "spring.datasource.password=" + password
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    private static boolean useFixedPort() {
        return System.getenv(ENV_TESTCONTAINER_PGSQL_PORT) != null;
    }

    private static int getFixedPort() {
        int port = Integer.parseInt(System.getenv(ENV_TESTCONTAINER_PGSQL_PORT));
        return port;
    }
}
