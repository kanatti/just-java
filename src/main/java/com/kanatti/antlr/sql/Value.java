package com.kanatti.antlr.sql;

import lombok.Data;

@Data
public class Value {
    public enum Type {
        STRING, NUMBER, IDENTIFIER
    }

    private final Type type;
    private final String value;

    public Value(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Value string(String value) {
        // Remove quotes
        return new Value(Type.STRING, value.substring(1, value.length() - 1));
    }

    public static Value number(String value) {
        return new Value(Type.NUMBER, value);
    }

    public static Value identifier(String value) {
        return new Value(Type.IDENTIFIER, value);
    }

    @Override
    public String toString() {
        if (type == Type.STRING) {
            return "'" + value + "'";
        }
        return value;
    }
}
