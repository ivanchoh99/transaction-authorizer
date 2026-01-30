package org.example.mapper;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

public class JacksonMapper {

    private static final JsonMapper INSTANCE = JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
            .build();

    public static JsonMapper get() {
        return INSTANCE;
    }
}
