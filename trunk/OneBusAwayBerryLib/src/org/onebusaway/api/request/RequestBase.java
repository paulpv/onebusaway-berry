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
package org.onebusaway.api.request;

import java.io.IOException;

import net.rim.device.api.io.FileNotFoundException;

import org.onebusaway.api.Context;
import org.onebusaway.api.ObaApi;
import org.onebusaway.api.ObaHelp;
import org.onebusaway.api.TextUtils;
import org.onebusaway.net.Uri;
import org.onebusaway.rim.settings.ObaSettings;

/**
 * The base class for Oba requests.
 * @author Paul Watts (paulcwatts@gmail.com) ORIGINAL
 * @author Paul Peavyhouse (pv@swooby.com) JME
 */
public class RequestBase
{
    public static boolean isInstanceObaResponse(Class cls)
    {
        return (cls != null && cls.getClass().isInstance(ObaResponse.class));
    }

    protected final Class clsResponse;
    protected final Uri   uri;

    protected RequestBase(Class clsResponse, Uri uri)
    {
        if (!isInstanceObaResponse(clsResponse))
        {
            throw new IllegalArgumentException("clsResponse must be of type ObaResponse.class");
        }

        this.clsResponse = clsResponse;
        this.uri = uri;
    }

    public String toString()
    {
        return TextUtils.getShortClassName(this) + //
                        " { clsResponse=" + TextUtils.getShortClassName(clsResponse) + //
                        ", uri=\"" + uri + "\" }";
    }

    private static String getServer(Context context)
    {
        return ObaSettings.getSettings().getApiServerName();
    }

    public static class BuilderBase
    {
        private static final String   API_KEY_ANDROID    = "v1_BktoDJ2gJlu6nLM6LsT9H8IUbWc=cGF1bGN3YXR0c0BnbWFpbC5jb20=";
        private static final String   API_KEY_BLACKBERRY = "84050538-4d94-4f19-b05e-5221c86eda95";
        protected static final String BASE_PATH          = "/api/where";

        protected final Uri.Builder   builder;
        private String                apiKey             = API_KEY_BLACKBERRY;

        protected BuilderBase(Context context, String path)
        {
            this(context, path, false);
        }

        protected BuilderBase(Context context, String path, boolean noVersion)
        {
            builder = new Uri.Builder();
            builder.scheme("http");
            builder.authority(getServer(context));
            builder.path(path);
            if (!noVersion)
            {
                builder.appendQueryParameter("version", ObaApi.VERSION2);
            }
            ObaApi.setAppInfo(builder);
        }

        protected static String getPathWithId(String pathElement, String id)
        {
            StringBuffer builder = new StringBuffer(BASE_PATH);
            builder.append(pathElement);
            builder.append(id);
            builder.append(".json");
            return builder.toString();
        }

        protected Uri buildUri()
        {
            builder.appendQueryParameter("key", apiKey);
            return builder.build();
        }

        /**
         * Allows the caller to assign a different server for a specific request.
         * Useful for unit-testing against specific servers (for instance, soak-api
         * when some new APIs haven't been released to production).
         *
         * Because this is implemented in the base class, it can't return 'this'
         * to use the standard builder pattern. Oh well, it's only for test.
         */
        public void setServer(String server)
        {
            builder.authority(server);
        }

        /**
         * Allows the caller to assign a different API key for a specific request.
         *
         * Because this is implemented in the base class, it can't return 'this'
         * to use the standard builder pattern. Oh well, it's only for test.
         */
        public void setApiKey(String key)
        {
            apiKey = key;
        }
    }

    public ObaResponse call()
    {
        ObaResponse obaResponse = null;

        try
        {
            // MUST NEVER FAIL: Must have a public default constructor!
            obaResponse = (ObaResponse) clsResponse.newInstance();

            // Example requests:
            // Initial startup out of service area:
            // http://api.onebusaway.org/api/where/stops-for-location.json?version=2&app_ver=11&app_uid=5284047f4ffb4e04824a2fd1d1f0cd62&lat=36.149777&lon=-95.993398&latSpan=0.0&lonSpan=360.0&key=v1_BktoDJ2gJlu6nLM6LsT9H8IUbWc%3DcGF1bGN3YXR0c0BnbWFpbC5jb20%3D
            // Initial request for Seattle:
            // http://api.onebusaway.org/api/where/stops-for-location.json?version=2&app_ver=11&app_uid=5284047f4ffb4e04824a2fd1d1f0cd62&lat=47.60599&lon=-122.33178&latSpan=0.012441&lonSpan=0.013732&key=v1_BktoDJ2gJlu6nLM6LsT9H8IUbWc%3DcGF1bGN3YXR0c0BnbWFpbC5jb20%3D

            String jsonStringResponse = ObaHelp.getUri(uri);

            // Example responses:
            // ObaCurrentTimeRequest {"text":"OK","data":{"time":1294877371929,"readableTime":"2011-01-12T16:09:31-08:00"},"code":200,"version":1}

            obaResponse.fromJSON(jsonStringResponse);
        }
        catch (FileNotFoundException e)
        {
            obaResponse.fromError(ObaApi.OBA_NOT_FOUND, e);
        }
        catch (IOException e)
        {
            obaResponse.fromError(ObaApi.OBA_IO_EXCEPTION, e);
        }
        catch (Exception e)
        {
            obaResponse.fromError(ObaApi.OBA_INTERNAL_ERROR, e);
        }

        return obaResponse;
    }
}
