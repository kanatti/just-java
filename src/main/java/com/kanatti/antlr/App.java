package com.kanatti.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


public class App {
    public static void main(String[] args) {
        String javaClassContent = "public class SampleClass { void DoSomething(){} }";
        Java8Lexer lexer = new Java8Lexer(CharStreams.fromString(javaClassContent));
    
        Java8Parser parser = new Java8Parser(new CommonTokenStream(lexer));
        ParseTree ast = parser.compilationUnit();
    
        ParseTreeWalker walker = new ParseTreeWalker();
    
        CheckMethodCase checkMethodCase = new CheckMethodCase();
    
        walker.walk(checkMethodCase, ast);
    }
}
