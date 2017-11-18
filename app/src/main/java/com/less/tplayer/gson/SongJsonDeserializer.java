package com.less.tplayer.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.less.tplayer.bean.Song;

import java.lang.reflect.Type;

/**
 * Created by deeper on 2017/11/18.
 */

public class SongJsonDeserializer implements JsonDeserializer<Song> {
    @Override
    public Song deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
