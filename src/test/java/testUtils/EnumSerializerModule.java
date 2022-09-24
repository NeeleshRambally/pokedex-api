package testUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import java.io.IOException;

public class EnumSerializerModule extends SimpleModule {
    private static final long serialVersionUID = 7345731193905952551L;

    @Override
    public void setSerializers(SimpleSerializers s) {
        super.setSerializers(s);
        s.addSerializer(Enum.class, new JsonSerializer<Enum>() {
            @Override
            public void serialize(Enum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.name().toUpperCase());
            }
        });
    }

    @Override
    public SimpleModule setDeserializerModifier(BeanDeserializerModifier mod) {
        return super.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<Enum> modifyEnumDeserializer(DeserializationConfig config,
                                                                 final JavaType type,
                                                                 BeanDescription beanDesc,
                                                                 final JsonDeserializer<?> deserializer) {
                return new JsonDeserializer<Enum>() {
                    @Override
                    public Enum deserialize(com.fasterxml.jackson.core.JsonParser jp, DeserializationContext ctxt) throws IOException {
                        Class<? extends Enum> rawClass = (Class<Enum<?>>) type.getRawClass();
                        return Enum.valueOf(rawClass, jp.getValueAsString().toUpperCase());
                    }
                };
            }
        });

    }

}
