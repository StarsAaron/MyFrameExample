package com.fastbuildlibrary.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Json操作相关工具
 */
public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();
    private static final boolean DEBUG = true;

    public static Map<String, Object> parse(String jsonStr) {

        Map<String, Object> result = null;

        if (jsonStr != null) {
            try {

                JSONObject jsonObject = new JSONObject(jsonStr);
                result = parseJSONObject(jsonObject);

            } catch (JSONException e) {
                if (DEBUG) Log.e(TAG, "", e);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseJSONObject(JSONObject jsonObject) throws JSONException {

        Map<String, Object> valueObject = null;

        if (jsonObject != null) {

            valueObject = new HashMap<String, Object>();
            Iterator<String> keyIter = jsonObject.keys();

            while (keyIter.hasNext()) {
                String keyStr = keyIter.next();
                Object itemObject = jsonObject.opt(keyStr);
                if (itemObject != null) {
                    valueObject.put(keyStr, parseValue(itemObject));
                }
            }
        }

        return valueObject;
    }

    private static Object parseValue(Object inputObject) throws JSONException {

        Object outputObject = null;

        if (inputObject != null) {

            if (inputObject instanceof JSONArray) {
                outputObject = parseJSONArray((JSONArray) inputObject);
            } else if (inputObject instanceof JSONObject) {
                outputObject = parseJSONObject((JSONObject) inputObject);
            } else if (inputObject instanceof String
                    || inputObject instanceof Boolean
                    || inputObject instanceof Integer) {
                outputObject = inputObject;
            }

        }

        return outputObject;
    }

    private static List<Object> parseJSONArray(JSONArray jsonArray) throws JSONException {

        List<Object> valueList = null;

        if (null != jsonArray) {

            valueList = new ArrayList<Object>();

            for (int i = 0; i < jsonArray.length(); i++) {
                Object itemObject = jsonArray.get(i);
                if (null != itemObject) {
                    valueList.add(parseValue(itemObject));
                }
            }
        }

        return valueList;
    }


    private JsonUtils() {/*Do not new me*/}

}