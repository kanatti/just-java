package com.kanatti.patterns.abstractfactory;

import com.kanatti.patterns.abstractfactory.executable.Executable;
import com.kanatti.patterns.abstractfactory.executable.WindowsExecutable;
import com.kanatti.patterns.abstractfactory.filesystem.FileSystem;
import com.kanatti.patterns.abstractfactory.filesystem.WindowFS;

public class WindowsBuildFactory extends BuildFactory {

    @Override
    public FileSystem getFileSystem() {
        return new WindowFS();
    }

    @Override
    public Executable createExecutable(String code) {
        return new WindowsExecutable(code);
    }

}
