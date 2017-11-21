package com.less.tplayer.http;

import com.less.tplayer.http.bean.FileInput;
import com.less.tplayer.http.callback.HttpCallback;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author deeper
 * @date 2017/11/21
 */

public interface IHttpMethod {
    /**
     * get 请求
     * @param url
     * @param params
     * @param httpCallback
     */
    void get(String url, Map<String,String> params,HttpCallback httpCallback);

    /**
     * post表单, 不附带文件
     */
    void postForm(String url,Map<String,String> params,HttpCallback httpCallback);

    /**
     * post文件流,仅上传文件,没有key.
     *
     * @param file
     * @param httpCallback
     */
    void postOnlyFile(File file,HttpCallback httpCallback);

    /**
     * post Json{application/json; charset=utf-8} 格式的json数据
     *
     * @param url
     * @param json
     * @param httpCallback
     */
    void postJsonNoFile(String url,String json,HttpCallback httpCallback);

    /**
     * post multipart形式的Form表单
     *
     * @param
     */
    void postMultiForm(String url, Map<String,String> params, List<FileInput> fileInputs,HttpCallback httpCallback);
}
