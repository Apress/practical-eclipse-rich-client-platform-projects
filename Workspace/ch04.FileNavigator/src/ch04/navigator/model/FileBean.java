package ch04.navigator.model;

import java.io.File;

public class FileBean {
    File file;

    public FileBean(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return file.getName();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean hasChildren() {
        return file.list() != null;
    }

    public FileBean[] getChildren() {
        File[] files = file.listFiles();

        FileBean[] fileBeans = new FileBean[files.length];

        for (int i = 0; i < files.length; i++) {
            fileBeans[i] = new FileBean(files[i]);
        }
        return fileBeans;
    }
}
