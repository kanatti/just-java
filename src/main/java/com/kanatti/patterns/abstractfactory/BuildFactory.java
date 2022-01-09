package com.kanatti.patterns.abstractfactory;

import com.kanatti.patterns.abstractfactory.executable.Executable;
import com.kanatti.patterns.abstractfactory.filesystem.FileSystem;

public abstract class BuildFactory {
    private static final BuildFactory LINUX_BUILD_FACTORY = new LinuxBuildFactory();
    private static final BuildFactory WINDOWS_BUILD_FACTORY = new WindowsBuildFactory();
    private static final BuildFactory MAC_BUILD_FACTORY = new MacBuildFactory();

    public static BuildFactory getFactory(BuildTypes type) {
        BuildFactory buildFactory = null;
        switch (type) {
            case LINUX:
                buildFactory = LINUX_BUILD_FACTORY;
                break;
            case WINDOWS:
                buildFactory = WINDOWS_BUILD_FACTORY;
                break;
            case MAC:
                buildFactory = MAC_BUILD_FACTORY;
                break;
        }
        return buildFactory;
    }

    public abstract FileSystem getFileSystem();
    
    public abstract Executable createExecutable(String code); 
}
