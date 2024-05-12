package com.kanatti.antlr;

import org.antlr.v4.runtime.tree.TerminalNode;

// Tutorial - https://www.baeldung.com/java-antlr

public class CheckMethodCase extends Java8BaseListener {
    @Override
    public void enterMethodDeclarator(Java8Parser.MethodDeclaratorContext ctx) {
        TerminalNode node = ctx.Identifier();
        String methodName = node.getText();

        System.out.println("Method name is - " + methodName);
    }
}