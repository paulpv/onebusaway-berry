/*
 * Copyright (C) 2010 Paul Watts (paulcwatts@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.berry.api;

import org.onebusaway.berry.map.GeoPoint;
import org.onebusaway.berry.net.Uri;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * 
 * @author Paul Watts (paulcwatts@gmail.com)
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 *
 */
public final class ObaApi {
    //private static final String TAG = "ObaApi";

    // Uninstantiatable
    private ObaApi() {
        throw new AssertionError();
    }

    public static final int    OBA_OK             = 200;
    public static final int    OBA_BAD_REQUEST    = 400;
    public static final int    OBA_UNAUTHORIZED   = 401;
    public static final int    OBA_NOT_FOUND      = 404;
    public static final int    OBA_INTERNAL_ERROR = 500;
    public static final int    OBA_OUT_OF_MEMORY  = 666;
    public static final int    OBA_IO_EXCEPTION   = 700;

    public static final String VERSION1           = "1";
    public static final String VERSION2           = "2";

    private static int         mAppVer            = 0;
    private static String      mAppUid            = null;

    public static int getVersion() {
        return mAppVer;
    }

    public static void setAppInfo(int version, String uuid) {
        mAppVer = version;
        mAppUid = uuid;
    }

    public static void setAppInfo(Uri.Builder builder) {
        if (mAppVer != 0) {
            builder.appendQueryParameter("app_ver", String.valueOf(mAppVer));
        }
        if (mAppUid != null) {
            builder.appendQueryParameter("app_uid", mAppUid);
        }
    }

    /**
     * Converts a latitude/longitude to a GeoPoint.
     * @param lat The latitude.
     * @param lon The longitude.
     * @return A GeoPoint representing this latitude/longitude.
     */
    public static final GeoPoint makeGeoPoint(double lat, double lon) {
        return new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
    }

    // TODO:(pv) Is there a better location for these? ObaHelp? ObaRequest? ObaResponse?

    /**
     * No-Op method used as a placeholder for better compatibility w/ Android code.
     */
    public static Context getContext() {
        return null;
    }

    public static String[] EMPTY_ARRAY_STRING = new String[] {};

    /*
    public static boolean isInstanceJSONReceivable(Class cls)
    {
        return (cls != null && cls.getClass().isInstance(JSONReceivable.class));
    }
    */

    /**
     * Used by both Elements and Responses, so best in the root api package,
     */
    public static JSONReceivable fromJSON(JSONObject json, String key, JSONReceivable jsonReceivable) throws JSONException,
                    InstantiationException, IllegalAccessException {
        JSONObject jsonItem = json.getJSONObject(key);
        jsonReceivable.fromJSON(jsonItem);
        return jsonReceivable;
    }

    public static JSONReceivable optJSON(JSONObject json, String key, JSONReceivable jsonReceivable, JSONReceivable defaultValue)
                    throws JSONException, InstantiationException, IllegalAccessException {
        JSONObject jsonItem;
        try {
            jsonItem = json.getJSONObject(key);
        }
        catch (JSONException e) {
            return defaultValue;
        }
        jsonReceivable.fromJSON(jsonItem);
        return jsonReceivable;
    }

    /**
     * Copies the src JSONArray to an already allocated dest array, allocating objects of the clsItem
     *
     * src.length() and dest.length must equal!
     * 
     * @param src
     * @param dest
     * @param clsItem
     * @throws JSONException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static JSONReceivable[] fromJSON(JSONArray src, JSONReceivable[] dest, Class clsItem) throws JSONException,
                    InstantiationException, IllegalAccessException {
        if (src == null || dest == null || clsItem == null) {
            throw new IllegalArgumentException("copyTo: src, dest, and clsItem must not be null");
        }

        if (src != null && dest != null && src.length() != dest.length) {
            throw new IllegalArgumentException("copyTo: src.length() and dest.length must equal");
        }

        /*
        if (!isInstanceJSONReceivable(clsItem))
        {
            throw new IllegalArgumentException("copyTo: clsItem must be of type JSONReceivable.class");
        }
        */

        JSONObject jsonObject;
        JSONReceivable item;
        for (int i = 0; i < dest.length; i++) {
            jsonObject = src.getJSONObject(i);
            item = (JSONReceivable) clsItem.newInstance();
            item.fromJSON(jsonObject);
            dest[i] = item;
        }

        return dest;
    }

    public static JSONReceivable[] optJSON(JSONArray src, JSONReceivable[] dest, Class clsItem, JSONReceivable[] defaultValue)
                    throws InstantiationException, IllegalAccessException {
        try {
            return fromJSON(src, dest, clsItem);
        }
        catch (JSONException e) {
            return defaultValue;
        }
    }

    public static String[] fromJSON(JSONArray src, String[] dest) throws JSONException {
        if (src == null || dest == null) {
            throw new IllegalArgumentException("copyTo: src, and dest must not be null");
        }

        if (src != null && dest != null && src.length() != dest.length) {
            throw new IllegalArgumentException("copyTo: src.length() and dest.length must equal");
        }

        for (int i = 0; i < dest.length; i++) {
            dest[i] = src.getString(i);
        }

        return dest;
    }

    public static String[] optJSON(JSONArray src, String[] dest, String[] defaultValue)
                    throws InstantiationException, IllegalAccessException {
        try {
            return fromJSON(src, dest);
        }
        catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * Get an optional Boolean value associated with a key,
     * or null if there is no such key or if the value is not a number.
     * If the value is a string, an attempt will be made to evaluate it as a number.
     * 
     * @param json
     * @param key
     * @return
     */
    public static Boolean optBoolean(JSONObject json, String key) {
        boolean value = json.optBoolean(key);
        return (!value) ? null : Boolean.TRUE;
    }

    /**
     * Get an optional Integer value associated with a key,
     * or null if there is no such key or if the value is not a number.
     * If the value is a string, an attempt will be made to evaluate it as a number.
     * 
     * @param json
     * @param key
     * @return
     */
    public static Integer optInteger(JSONObject json, String key) {
        int value = json.optInt(key);
        return (value == 0) ? null : new Integer(value);
    }

    /**
     * Get an optional Double value associated with a key,
     * or null if there is no such key or if the value is not a number.
     * If the value is a string, an attempt will be made to evaluate it as a number.
     * 
     * @param json
     * @param key
     * @return
     */
    public static Double optDouble(JSONObject json, String key) {
        double value = json.optDouble(key);
        return (value == Double.NaN) ? null : new Double(value);
    }
}
