package com.less.tplayer.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.less.plugin.Animal;
import com.less.plugin.BuildConfig;
import com.less.tplayer.R;
import com.less.tplayer.util.DynamicClassLoader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "less";
    private String fileName = "dynamic.jar-1.0.jar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        writeToApp();
    }

    public void handle(View view) {
        try {
            String outPath = Environment.getExternalStorageDirectory() + File.separator + fileName;
            // 注意这个输出dex的路径需要在自己的目录里
            File dexOutputDir = getDir("dex", 0);

            DynamicClassLoader classLoader = new DynamicClassLoader(outPath, dexOutputDir.getAbsolutePath());
            Class<?> clazz = classLoader.loadClass("com.less.plugin.Dog");
            Animal animal = (Animal) clazz.newInstance();

            animal.say(new Animal.Callback() {
                @Override
                public void done(String message) {
                    Log.i(TAG, " ===> " + message);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToApp() {
        String outPath = Environment.getExternalStorageDirectory() + File.separator + fileName;

        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            inputStream = getResources().getAssets().open(fileName);
            bufferedInputStream = new BufferedInputStream(inputStream);
            fileOutputStream = new FileOutputStream(new File(outPath));
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            byte[] buffer = new byte[1024];
            int hasRead = -1;
            while ((hasRead = bufferedInputStream.read(buffer) ) != -1) {
                bufferedOutputStream.write(buffer,0,hasRead);
                bufferedOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedInputStream.close();
                fileOutputStream.close();
                bufferedInputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "写入成功");
        }
    }
}
