package com.less.tplayer.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/31.
 */

public class ReadState {
    private ReadStateHelper helper;

    ReadState(ReadStateHelper helper) {
        this.helper = helper;
    }

    /**
     * 添加已读状态
     *
     * @param key 一般为资讯等Id
     */
    public void put(String key) {
        helper.put(key);
    }

    /**
     * 获取是否为已读
     *
     * @param key 一般为咨询等Id
     * @return true 已读
     */
    public boolean already(String key) {
        return helper.already(key);
    }
}

class ReadStateHelper {
    private final static Map<String, ReadStateHelper> helperCache = new HashMap<>();
    private final File file;// 当前json 配置文件
    private final Map<String, Long> cache = new HashMap<>();
    private final int maxPoolSize;// 当前json(cache)能够存储的最大key:value数量

    private ReadStateHelper(File file, int maxPoolSize) {
        if (file == null || !file.exists() || !file.isFile() || !file.canRead() || !file.canWrite()) {
            throw new NullPointerException("file not null.");
        }
        this.maxPoolSize = maxPoolSize;
        this.file = file;
        read();
    }

    public static ReadStateHelper create(Context context, String fileName, int maxPoolSize) {
        fileName = fileName + ".json";
        if (helperCache.containsKey(fileName)) {
            return helperCache.get(fileName);
        }
        File file = new File(context.getDir("read_state", Context.MODE_PRIVATE), fileName);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new RuntimeException("can't mkdirs by:" + parent.toString());
            }
            try {
                if (!file.createNewFile())
                    throw new IOException("can't createNewFile by:" + file.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        ReadStateHelper helper = new ReadStateHelper(file, maxPoolSize);
        helperCache.put(fileName, helper);// key值是文件名， 博客：blog 或 新闻：news 等配置的文件名。
        return helper;
    }

    /**
     * 添加已读状态
     *
     * @param key 一般为资讯等Id
     */
    public void put(String key) {
        if (TextUtils.isEmpty(key) || cache.containsKey(key))
            return;
        if (cache.size() >= maxPoolSize) {
            clearCache();// 清除一部分旧数据
        }
        cache.put(key, System.currentTimeMillis());// cache(Map) 中存储的内容为 key (key), value(currentTimeMillis)
        save();
    }

    /**
     * 获取是否为已读
     *
     * @param key 一般为资讯等Id
     * @return True 已读
     */
    public boolean already(String key) {
        return !TextUtils.isEmpty(key) && cache.containsKey(key);
    }

    /**
     * 清理一次当前缓存
     */
    public void clearCache() {
        if (cache.size() == 0)
            return;
        List<Map.Entry<String, Long>> info = new ArrayList<>(cache.entrySet());
        // 把cache中的key按照value(currentTimeMillis)排序
        Collections.sort(info, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                long et = (o1.getValue() - o2.getValue());
                return et > 0 ? 1 : (et == 0 ? 0 : -1);
            }
        });

        // 默认清除 70% 的旧 数据
        int deleteSize = (int) (info.size() * 0.7f);
        if (deleteSize <= 0)
            return;
        for (Map.Entry<String, Long> stringLongEntry : info) {
            // Remove
            cache.remove(stringLongEntry.getKey());
            if (--deleteSize <= 0)
                break;
        }
    }
    // 文件读操作(读取json文件到 当前cache中)
    private void read() {
        Reader reader = null;
        try {
            Map<String, Long> data = new Gson().fromJson(reader = new FileReader(file),
                    new TypeToken<Map<String, Long>>() {
                    }.getType());
            if (data != null && data.size() > 0)
                cache.putAll(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(reader);
        }
    }
    // 文件写操作(cache<Map> 写入到json文件)
    private void save() {
        Writer writer = null;
        try {
            writer = new FileWriter(file);
            new Gson().toJson(cache, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(writer);
        }
    }
}