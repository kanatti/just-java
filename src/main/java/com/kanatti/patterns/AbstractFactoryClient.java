package com.kanatti.patterns;

import com.kanatti.patterns.abstractfactory.BuildFactory;
import com.kanatti.patterns.abstractfactory.BuildTypes;
import com.kanatti.patterns.abstractfactory.executable.Executable;
import com.kanatti.patterns.abstractfactory.filesystem.FileSystem;

public class AbstractFactoryClient {
    /**
     * BuildFactory provides factory of different flavors MAC, WINDOWS, LINUX.
     * Factory provides the necessary abstractions to create an executable without
     * worrying about the underlying Arch.
     */
    public static void main(String[] args) {
        BuildFactory buildFactory = BuildFactory.getFactory(BuildTypes.LINUX);
        build(buildFactory);
    }

    static void build(BuildFactory buildFactory) {
        FileSystem fs = buildFactory.getFileSystem();
        String code = fs.read("HelloWorld.java");
        Executable executable = buildFactory.createExecutable(code);
        fs.write("HelloWorld.out", executable.getBytes());
    }
}
