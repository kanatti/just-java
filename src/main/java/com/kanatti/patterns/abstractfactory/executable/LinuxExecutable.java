package com.kanatti.patterns.abstractfactory.executable;

public class LinuxExecutable extends Executable {
    private String code;

    public LinuxExecutable(String code) {
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
