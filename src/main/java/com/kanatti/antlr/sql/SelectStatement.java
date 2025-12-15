package com.kanatti.antlr.sql;

import lombok.Data;
import java.util.List;

@Data
public class SelectStatement {
    private final List<Column> columns;
    private final boolean selectAll;
    private final String tableName;
    private final Condition whereCondition;

    public SelectStatement(List<Column> columns, boolean selectAll, String tableName, Condition whereCondition) {
        this.columns = columns;
        this.selectAll = selectAll;
        this.tableName = tableName;
        this.whereCondition = whereCondition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        if (selectAll) {
            sb.append("*");
        } else {
            sb.append(String.join(", ", columns.stream().map(Column::toString).toList()));
        }
        sb.append(" FROM ").append(tableName);
        if (whereCondition != null) {
            sb.append(" WHERE ").append(whereCondition);
        }
        return sb.toString();
    }
}
