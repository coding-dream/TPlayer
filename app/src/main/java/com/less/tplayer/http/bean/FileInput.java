package com.less.tplayer.http.bean;

import java.io.File;

/**
 * Created by deeper on 2017/11/21.
 */

public class FileInput {
    public String key;
    public String filename;
    public File file;

    public FileInput(String name, String filename, File file) {
        this.key = name;
        this.filename = filename;
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileInput{" +
                "key='" + key + '\'' +
                ", filename='" + filename + '\'' +
                ", file=" + file +
                '}';
    }
}
