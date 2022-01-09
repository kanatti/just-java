package com.kanatti.patterns.abstractfactory.executable;

public class WindowsExecutable extends Executable {
    private String code;

    public WindowsExecutable(String code) {
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
