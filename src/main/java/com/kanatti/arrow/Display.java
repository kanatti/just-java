package com.kanatti.arrow;

import java.util.function.Supplier;

/**
 * Display utility for Arrow exercises - provides tree-like output
 */
public class Display {
    private int indent = 0;

    public void section(String title, Runnable block) {
        System.out.println("  ".repeat(indent) + title + ":");
        indent++;
        block.run();
        indent--;
    }

    public <T> T show(String expr, Supplier<T> supplier) {
        T result = supplier.get();
        System.out.println("  ".repeat(indent) + expr + " = " + result);
        return result;
    }

    public <T> T show(String expr, T value) {
        System.out.println("  ".repeat(indent) + expr + " = " + value);
        return value;
    }

    public static String toBinary(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }
}
