package com.kanatti.jackson.filter2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class FilterDeserializer extends StdDeserializer<Filter> {
    public FilterDeserializer() { 
        this(null); 
    } 

    public FilterDeserializer(Class<?> vc) { 
        super(vc); 
    }

    @Override
    public Filter deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.has("OR")) {
            JsonNode orNode = node.get("OR");
            List<Filter> filters = deserializeFilters(jsonParser, orNode);
            return new OrFilter(filters);
        } else if (node.has("AND")) {
            JsonNode andNode = node.get("AND");
            List<Filter> filters = deserializeFilters(jsonParser, andNode);
            return new AndFilter(filters);
        }

        return jsonParser.getCodec().treeToValue(node, OnFilter.class);
    }

    private List<Filter> deserializeFilters(JsonParser jsonParser, JsonNode node) throws IOException {
        List<Filter> filters = new ArrayList<>();
        for (JsonNode filterNode : node) {
            Filter filter = jsonParser.getCodec().treeToValue(filterNode, Filter.class);
            filters.add(filter);
        }
        return filters;
    }
    
}
