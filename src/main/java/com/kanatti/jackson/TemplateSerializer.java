package com.kanatti.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemplateSerializer {
  private static final ObjectMapper mapper = new ObjectMapper();

  @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
  @JsonSubTypes({
      @JsonSubTypes.Type(Element.class),
      @JsonSubTypes.Type(Text.class),
  })
  public static abstract class Node {
    public abstract String toString();
  }

  // @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
  // @JsonSubTypes({
  //     @JsonSubTypes.Type(value = Paragraph.class, name = "p"),
  //     @JsonSubTypes.Type(value = Image.class, name = "img"),
  // })
  public static class Element extends Node {
    public String type;
    public List<Node> children;

    @Override
    public String toString() {
      return type.toString() + ":" + children.toString();
    }
  }

  public static class Text extends Node {
    public String text;

    @Override
    public String toString() {
      return text;
    }
  }

  public static class Paragraph extends Element {
    @Override
    public String toString() {
      return "<p>" + children.toString() + "</p>";
    }
  }

  public static class Image extends Element {
    public String src;

    @Override
    public String toString() {
      return String.format("<img src=\"%s\" />", src);
    }
  }

  public static class NodeDeserializer extends StdDeserializer<Node> {
    public NodeDeserializer() {
      super(Node.class);
    }

    @Override
    public Node deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws JsonProcessingException, IOException {
      ObjectCodec codec = jsonParser.getCodec();
      JsonNode node = codec.readTree(jsonParser);

      JsonNode typeNode = node.get("type");
      String type = typeNode.asText();

      JsonNode childrenNode = node.get("children");

      switch (type) {
        case "p":
          Paragraph paragraph = new Paragraph();
          paragraph.type = type;
          paragraph.children = mapper.readValue(childrenNode.traverse(codec), new TypeReference<List<Node>>() {
          });
          return paragraph;
        case "img":
          Image image = new Image();
          image.type = type;
          image.children = new ArrayList<>();
          return image;
        default:
          return null;
      }
    }
  }

  public static class NodeModule extends com.fasterxml.jackson.databind.module.SimpleModule {
    public NodeModule() {
      super();
      addDeserializer(Node.class, new NodeDeserializer());
    }
  }

  public List<Node> serialize(String template) throws JsonProcessingException {
    // type reference
    TypeReference<List<Node>> typeRef = new TypeReference<List<Node>>() {
    };
    mapper.registerModule(new NodeModule());
    final List<Node> nodes = mapper.readValue(template, typeRef);
    return nodes;
  }
}