package com.kanatti.jackson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanatti.jackson.filter.Filter;

public class Test {
    public static void main(String[] args) {
        String json = loadResource("filter.json");

        ObjectMapper objectMapper = new ObjectMapper();

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
            return new String(Test.class.getClassLoader().getResourceAsStream(name).readAllBytes(),
                StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
