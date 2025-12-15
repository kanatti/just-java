package com.kanatti.antlr.sql;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ComparisonCondition extends Condition {
    private final Column column;
    private final String operator;
    private final Value value;

    public ComparisonCondition(Column column, String operator, Value value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String toString() {
        return column + " " + operator + " " + value;
    }
}
