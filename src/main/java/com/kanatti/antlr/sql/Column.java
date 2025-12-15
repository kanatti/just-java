package com.kanatti.antlr.sql;

import lombok.Data;

@Data
public class Column {
    private final String tableName;  // Can be null
    private final String columnName;

    public Column(String columnName) {
        this(null, columnName);
    }

    public Column(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        if (tableName != null) {
            return tableName + "." + columnName;
        }
        return columnName;
    }
}
