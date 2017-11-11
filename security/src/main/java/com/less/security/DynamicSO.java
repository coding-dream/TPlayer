package com.less.security;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;

/**
 * Created by deeper on 2017/11/10.
 */

public class DynamicSO {
    private static final String TAG = DynamicSO.class.getSimpleName();

    public static void loadExSo(Context context,String soName, String soFilesDir){
        File soFile = choose(soFilesDir,soName);

        String destFileName = context.getDir("myso", Context.MODE_PRIVATE).getAbsolutePath()  + File.separator + soName;
        File destFile = new File(destFileName);
        if (soFile != null) {
            Log.e(TAG, "最终选择加载的so路径: " + soFile.getAbsolutePath());
            Log.e(TAG, "写入so的路径: " + destFileName);
            boolean flag = FileUtil.copyFile(soFile, destFile);
            if (flag) {
                System.load(destFileName);
            }
        }

    }

    /**
     * 在网络或者本地下载过的so文件夹: 选择适合当前设备的so文件
     *
     * @param soFilesDir so文件的目录, 如apk文件解压后的 Amusic/libs/ 目录 : 包含[arm64-v8a,arm64-v7a等]
     * @param soName so库的文件名, 如 libmusic.so
     * @return 最终匹配合适的so文件
     */
    private static File choose(String soFilesDir,String soName) {
        if (Build.VERSION.SDK_INT >= 21) {
            String [] abis = Build.SUPPORTED_ABIS;
            for (String abi : abis) {
                Log.e(TAG, "SUPPORTED_ABIS =============> " + abi);
            }
            for (String abi : abis) {
                File file = new File(soFilesDir,abi + File.separator + soName);
                if (file.exists()) {
                    return file;
                }
            }
        } else {
            File file = new File(soFilesDir, Build.CPU_ABI + File.separator + soName);
            if (file.exists()) {
                return file;
            } else {
                // 没有找到和Build.CPU_ABI 匹配的值,那么就委屈设备使用armeabi算了.
                File finnalFile = new File(soFilesDir, "armeabi" + File.separator + soName);
                if (finnalFile.exists()) {
                    return finnalFile;
                }
            }
        }
        return null;
    }
}
