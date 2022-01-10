package com.kanatti.patterns.abstractfactory.filesystem;

public class WindowFS extends FileSystem {

    @Override
    public String read(String filename) {
        System.out.printf("Reading from %s on Windows\n", filename);
        return "Code from " + filename;
    }

    @Override
    public void write(String filename, byte[] bytes) {
        System.out.printf("Writing to %s on Windows\n", filename);
    }

}
