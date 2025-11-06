package edu.unimagdalena.paymentservice.config;

import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.core.MessageProperties;

public class SimpleNameClassMapper implements ClassMapper {

    private static final String LOCAL_DTO_PACKAGE = "edu.unimagdalena.paymentservice.messaging";

    @Override
    public void fromClass(Class<?> clazz, MessageProperties props) {

        props.getHeaders().put("__TypeId__", clazz.getSimpleName());
    }

    @Override
    public Class<?> toClass(MessageProperties props) {
        Object typeId = props.getHeaders().get("__TypeId__");
        if (typeId == null) {
            return Object.class;
        }

        String simpleName = typeId.toString();

        if (simpleName.contains(".")) {
            simpleName = simpleName.substring(simpleName.lastIndexOf('.') + 1);
        }

        try {
            return Class.forName(LOCAL_DTO_PACKAGE + "." + simpleName);
        } catch (ClassNotFoundException e) {
            return Object.class;
        }
    }
}

