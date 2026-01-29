package org.example.mapper;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

public class JacksonConfig {

    private static final JsonMapper INSTANCE = JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
            .enable(SerializationFeature.WRAP_ROOT_VALUE)
            .enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
            .build();

    public static JsonMapper get() {
        return INSTANCE;
    }
}
