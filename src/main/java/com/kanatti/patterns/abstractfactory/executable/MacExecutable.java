package com.kanatti.patterns.abstractfactory.executable;

public class MacExecutable extends Executable {
    private String code;

    public MacExecutable(String code) {
        this.code = code;
    }

    @Override
    public byte[] getBytes() {
        return code.getBytes();
    }

    @Override
    public String getStringRepr() {
        return code;
    }

}
