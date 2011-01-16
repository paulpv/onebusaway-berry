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
    public static final GeoPoint makeGeoPoint(double lat, double lon) {
        return new GeoPoint((int)(lat*1E6), (int)(lon*1E6));
    }

    // TODO:(pv) Is there a better location for these? ObaHelp? ObaRequest? ObaResponse?
    
    public static String[] EMPTY_ARRAY_STRING = new String[0];
    
    public static boolean isInstanceObaReceivable(Class cls)
    {
        return (cls != null && cls.getClass().isInstance(ObaReceivable.class));
        //return (cls != null && cls.isInstance(ObaReceivable.class));
    }

    public static Object newObjectFromJson(JSONObject json, String key, Class clsItem) throws JSONException, InstantiationException, IllegalAccessException
    {
        if (!isInstanceObaReceivable(clsItem))
        {
            throw new IllegalArgumentException("getObjectFromJson: clsItem must implement JSONReceivable");
        }
        
        JSONObject jsonItem = json.getJSONObject(key);
        ObaReceivable item = (ObaReceivable) clsItem.newInstance(); 
        item.fromJSON(jsonItem);
        return item;
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
    public static void copyJSONArrayToObaReceivableArray(JSONArray src, Object[] dest, Class clsItem) throws JSONException, InstantiationException, IllegalAccessException
    {
        if (src == null || dest == null || clsItem == null)
        {
            throw new IllegalArgumentException("src, dest, and clsItem must not be null");
        }
        
        if (src != null && dest != null && src.length() == dest.length)
        {
            throw new IllegalArgumentException("getObjectFromJson: src.length() and dest.length must equal.");
        }
        
        if (!isInstanceObaReceivable(clsItem))
        {
            throw new IllegalArgumentException("getObjectFromJson: clsItem must implement JSONReceivable");
        }

        JSONObject jsonObject;
        ObaReceivable item;
        for (int i = 0; i < dest.length; i++)
        {
            jsonObject = src.getJSONObject(i);
            item = (ObaReceivable) clsItem.newInstance(); 
            item.fromJSON(jsonObject);
            dest[i] = item;
        }
    }
}
