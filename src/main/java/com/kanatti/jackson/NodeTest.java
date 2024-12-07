package com.kanatti.jackson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.kanatti.jackson.TemplateSerializer.Node;

public class NodeTest {
    public static void main(String[] args) {
        String json = loadResource("sample.json");

        TemplateSerializer templateSerializer = new TemplateSerializer();

        try {
            List<Node> nodes = templateSerializer.serialize(json);
            System.out.println(nodes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load sample.json from resources
    private static String loadResource(String name) {
        try {
            return new String(NodeTest.class.getClassLoader().getResourceAsStream(name).readAllBytes(),
                StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
