package com.less.tplayer.util;

import dalvik.system.DexClassLoader;

/**
 *
 * @author deeper
 * @date 2017/10/30
 */

public class DynamicClassLoader extends DexClassLoader {

    public DynamicClassLoader(String dexPath,String dexOutputDir) {
        super(dexPath,dexOutputDir,null,DynamicClassLoader.class.getClassLoader());
    }
    public DynamicClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }
}
