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
package org.onebusaway.api;

import javax.microedition.location.Coordinates;

import org.onebusaway.api.request.ObaResponse;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;
import org.onebusaway.net.Uri;


public class ObaApi
{
    private static final String TAG = "ObaApi";
    
    // Uninstantiatable
    private ObaApi() { }

    public static final int OBA_OK = 200;
    public static final int OBA_BAD_REQUEST = 400;
    public static final int OBA_UNAUTHORIZED = 401;
    public static final int OBA_NOT_FOUND = 404;
    public static final int OBA_INTERNAL_ERROR = 500;
    public static final int OBA_OUT_OF_MEMORY = 666;
    public static final int OBA_IO_EXCEPTION = 700;

    public static final String VERSION1 = "1";
    public static final String VERSION2 = "2";

    private static int mAppVer = 0;
    private static String mAppUid = null;
    
    public static int getVersion()
    {
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
    public static final Coordinates makeGeoPoint(double lat, double lon) {
        return new Coordinates(lat, lon, 0);
    }

    // TODO:(pv) Is there a better location for these? ObaHelp? ObaRequest? ObaResponse?

    /**
     * No-Op method used as a placeholder for compatibility w/ Android code.
     */
    public static Context getContext()
    {
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
    public static void fromJSON(JSONObject json, String key, JSONReceivable jsonReceivable) throws JSONException, InstantiationException, IllegalAccessException
    {
        JSONObject jsonItem = json.getJSONObject(key);
        jsonReceivable.fromJSON(jsonItem);
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
    public static void copyTo(JSONArray src, JSONReceivable[] dest, Class clsItem) throws JSONException, InstantiationException, IllegalAccessException
    {
        if (src == null || dest == null || clsItem == null)
        {
            throw new IllegalArgumentException("copyTo: src, dest, and clsItem must not be null");
        }
        
        if (src != null && dest != null && src.length() != dest.length)
        {
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
        for (int i = 0; i < dest.length; i++)
        {
            jsonObject = src.getJSONObject(i);
            item = (JSONReceivable) clsItem.newInstance();
            item.fromJSON(jsonObject);
            dest[i] = item;
        }
    }

    public static void copyTo(JSONArray src, String[] dest) throws JSONException
    {
        if (src == null || dest == null)
        {
            throw new IllegalArgumentException("copyTo: src, and dest must not be null");
        }
        
        if (src != null && dest != null && src.length() != dest.length)
        {
            throw new IllegalArgumentException("copyTo: src.length() and dest.length must equal");
        }

        for (int i = 0; i < dest.length; i++)
        {
            dest[i] = src.getString(i);
        }
    }

    /*
    public static void copyTo(JSONArray src, int[] dest) throws JSONException
    {
        if (src == null || dest == null)
        {
            throw new IllegalArgumentException("copyTo: src, and dest must not be null");
        }
        
        if (src != null && dest != null && src.length() != dest.length)
        {
            throw new IllegalArgumentException("copyTo: src.length() and dest.length must equal");
        }

        for (int i = 0; i < dest.length; i++)
        {
            dest[i] = src.getInt(i);
        }
    }
    */
}
