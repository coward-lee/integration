package org.lee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

public class JsonConverter<T>  implements AttributeConverter<T, String> {
    private static final ObjectMapper objectMapper = JsonUtil.getObjectMapper();

    @Override
    public String convertToDatabaseColumn(T attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    public T convertToEntityAttribute(String attribute) {

        if (attribute == null) {
            return null;
        }
        try {
            Class<T> actualTypeArgument1 = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            ParameterizedType genericSuperclass = (ParameterizedType)getClass().getGenericSuperclass();

            return (T) objectMapper.readValue(attribute,actualTypeArgument1);
        } catch (IOException ex) {
            throw new IllegalArgumentException("string: " + attribute + ", json string convert to object error: " + ex);
        }
    }

    public static void main(String[] args) {
        DemoP demoP = new DemoC("xxx","xxxxxxx");
        DemoC<String> demoP1 = (DemoC<String>) demoP;
        System.out.println(demoP1);
    }
}

class DemoP{
    String name;

    public DemoP(String name) {
        this.name = name;
    }
}
class DemoC<T> extends DemoP{
    T type;

    public DemoC(String name, T type) {
        super(name);
        this.type = type;
    }

    @Override
    public String toString() {
        return "DemoC{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}