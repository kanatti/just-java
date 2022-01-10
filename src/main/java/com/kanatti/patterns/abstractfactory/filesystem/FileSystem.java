package com.kanatti.patterns.abstractfactory.filesystem;

public abstract class FileSystem {
    public abstract String read(String filename);

    public abstract void write(String filename, byte[] bytes);
}
