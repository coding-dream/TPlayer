package com.less.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by deeper on 2017/11/10.
 */

public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    public static boolean copyFile(File srcFile, File destFile) {
        boolean flag = false;
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            fileInputStream = new FileInputStream(srcFile);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            fileOutputStream = new FileOutputStream(destFile);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            byte[] bytes = new byte[1024];
            int len = -1;
            while ( (len = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, len);
            }
            bufferedOutputStream.flush();

            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }finally {
            try {
                bufferedOutputStream.close();
                bufferedInputStream.close();
                fileOutputStream.close();
                fileInputStream.close();
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }
        }
        return flag;
    }
}
