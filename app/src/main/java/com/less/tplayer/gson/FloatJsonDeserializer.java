package com.less.tplayer.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.less.tplayer.util.LogUtils;

import java.lang.reflect.Type;

/**
 * Created by deeper on 2017/11/18.
 */

public class FloatJsonDeserializer implements JsonDeserializer<Float> {
    @Override
    public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json.getAsFloat();
        } catch (Exception e) {
            LogUtils.d("FloatJsonDeserializer-deserialize-error:" + (json != null ? json.toString() : ""));
            return 0f;
        }
    }
}
