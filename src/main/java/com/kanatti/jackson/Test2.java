package com.kanatti.jackson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kanatti.jackson.filter2.Filter;
import com.kanatti.jackson.filter2.FilterDeserializer;

public class Test2 {
    public static void main(String[] args) {
        String json = loadResource("filter2.json");

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Filter.class, new FilterDeserializer());
        objectMapper.registerModule(module);

        TypeReference<List<Filter>> typeRef = new TypeReference<List<Filter>>() {
        };

        try {
            List<Filter> filters = objectMapper.readValue(json, typeRef);
            System.out.println(filters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load sample.json from resources
    private static String loadResource(String name) {
        try {
            return new String(Test2.class.getClassLoader().getResourceAsStream(name).readAllBytes(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
