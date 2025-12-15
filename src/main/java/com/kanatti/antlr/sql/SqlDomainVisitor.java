package com.kanatti.antlr.sql;

import com.kanatti.antlr.SimpleSqlBaseVisitor;
import com.kanatti.antlr.SimpleSqlParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor that transforms ANTLR parse tree into domain objects.
 * This demonstrates the visitor pattern for tree walking.
 */
public class SqlDomainVisitor extends SimpleSqlBaseVisitor<Object> {

    /**
     * Visit the root query node and return a SelectStatement
     */
    @Override
    public SelectStatement visitQuery(SimpleSqlParser.QueryContext ctx) {
        return (SelectStatement) visit(ctx.selectStatement());
    }

    /**
     * Visit a SELECT statement and build the SelectStatement domain object
     */
    @Override
    public SelectStatement visitSelectStatement(SimpleSqlParser.SelectStatementContext ctx) {
        // Determine if SELECT * or specific columns
        boolean selectAll = ctx.columnList() instanceof SimpleSqlParser.SelectAllContext;

        // Get columns (empty list if SELECT *)
        List<Column> columns = new ArrayList<>();
        if (!selectAll) {
            SimpleSqlParser.SelectColumnsContext columnListCtx =
                (SimpleSqlParser.SelectColumnsContext) ctx.columnList();
            for (SimpleSqlParser.ColumnContext columnCtx : columnListCtx.column()) {
                columns.add((Column) visit(columnCtx));
            }
        }

        // Get table name
        String tableName = ctx.tableName().IDENTIFIER().getText();

        // Get WHERE condition (if present)
        Condition whereCondition = null;
        if (ctx.whereClause() != null) {
            whereCondition = (Condition) visit(ctx.whereClause());
        }

        return new SelectStatement(columns, selectAll, tableName, whereCondition);
    }

    /**
     * Visit a column reference
     */
    @Override
    public Column visitColumn(SimpleSqlParser.ColumnContext ctx) {
        if (ctx.IDENTIFIER().size() == 2) {
            // table.column format
            return new Column(
                ctx.IDENTIFIER(0).getText(),
                ctx.IDENTIFIER(1).getText()
            );
        } else {
            // just column format
            return new Column(ctx.IDENTIFIER(0).getText());
        }
    }

    /**
     * Visit WHERE clause
     */
    @Override
    public Condition visitWhereClause(SimpleSqlParser.WhereClauseContext ctx) {
        return (Condition) visit(ctx.condition());
    }

    /**
     * Visit AND condition
     */
    @Override
    public Condition visitAndCondition(SimpleSqlParser.AndConditionContext ctx) {
        Condition left = (Condition) visit(ctx.condition(0));
        Condition right = (Condition) visit(ctx.condition(1));
        return new AndCondition(left, right);
    }

    /**
     * Visit OR condition
     */
    @Override
    public Condition visitOrCondition(SimpleSqlParser.OrConditionContext ctx) {
        Condition left = (Condition) visit(ctx.condition(0));
        Condition right = (Condition) visit(ctx.condition(1));
        return new OrCondition(left, right);
    }

    /**
     * Visit parenthesized condition
     */
    @Override
    public Condition visitParenCondition(SimpleSqlParser.ParenConditionContext ctx) {
        // Just unwrap the parentheses and visit the inner condition
        return (Condition) visit(ctx.condition());
    }

    /**
     * Visit comparison condition (column operator value)
     */
    @Override
    public Condition visitComparisonCondition(SimpleSqlParser.ComparisonConditionContext ctx) {
        Column column = (Column) visit(ctx.column());
        String operator = ctx.operator().getText();
        Value value = (Value) visit(ctx.value());

        return new ComparisonCondition(column, operator, value);
    }

    /**
     * Visit string literal value
     */
    @Override
    public Value visitStringValue(SimpleSqlParser.StringValueContext ctx) {
        return Value.string(ctx.STRING_LITERAL().getText());
    }

    /**
     * Visit number literal value
     */
    @Override
    public Value visitNumberValue(SimpleSqlParser.NumberValueContext ctx) {
        return Value.number(ctx.NUMBER_LITERAL().getText());
    }

    /**
     * Visit identifier value
     */
    @Override
    public Value visitIdentifierValue(SimpleSqlParser.IdentifierValueContext ctx) {
        return Value.identifier(ctx.IDENTIFIER().getText());
    }
}
