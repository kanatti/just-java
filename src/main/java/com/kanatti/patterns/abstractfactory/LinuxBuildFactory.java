package com.kanatti.patterns.abstractfactory;

import com.kanatti.patterns.abstractfactory.executable.Executable;
import com.kanatti.patterns.abstractfactory.executable.LinuxExecutable;
import com.kanatti.patterns.abstractfactory.filesystem.FileSystem;
import com.kanatti.patterns.abstractfactory.filesystem.UnixFS;

public class LinuxBuildFactory extends BuildFactory {

    @Override
    public FileSystem getFileSystem() {
        return new UnixFS();
    }

    @Override
    public Executable createExecutable(String code) {
        return new LinuxExecutable(code);
    }
    
}
