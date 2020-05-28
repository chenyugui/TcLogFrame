package com.taichuan.code.utils;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gui on 2017/8/8.
 * Json转化工具
 */
@SuppressLint("LogNotTimber")
public class JsonUtil {
    private static final String TAG = "JsonUtil";

    public static <T> List<T> jsonToArray(String jsonString, Class<T> cls) throws JSONException {
        JSONArray array = new JSONArray(jsonString);
        int size = array.length();
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            if (cls == String.class) {
                list.add((T) object.toString());
            } else {
                list.add(GsonUtil.getGson().fromJson(object.toString(), cls));
            }
        }
        return list;
    }

}
