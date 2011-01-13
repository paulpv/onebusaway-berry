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

import org.onebusaway.api.JSONReceivable;
import org.onebusaway.api.ObaApi;
import org.onebusaway.api.ObaHelp;
import org.onebusaway.api.TextUtils;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;
import org.onebusaway.net.Uri;
import org.onebusaway.rim.settings.ObaSettings;

/**
 * The base class for Oba requests.
 * @author Paul Watts (paulcwatts@gmail.com) ORIGINAL
 * @author Paul Peavyhouse (pv@swooby.com) JME
 */
public class RequestBase
{
    private static final String   API_KEY     = "v1_BktoDJ2gJlu6nLM6LsT9H8IUbWc=cGF1bGN3YXR0c0BnbWFpbC5jb20=";
    private static final String   API_VERSION = "2";
    protected static final String BASE_PATH   = "/api/where";

    protected final Class         clsData;
    protected final Uri           uri;

    public static boolean isDataClassJsonReceivable(Class clsData)
    {
        return (clsData != null && clsData.getClass().isInstance(JSONReceivable.class));
    }

    protected RequestBase(Class clsData, Uri uri)
    {
        if (!isDataClassJsonReceivable(clsData))
        {
            throw new IllegalArgumentException("clsData must implement JSONReceivable");
        }

        this.clsData = clsData;
        this.uri = uri;
    }

    public String toString()
    {
        return TextUtils.getShortClassName(this) + " { clsData=" + TextUtils.getShortClassName(clsData) + ", uri=\"" + uri
                        + "\" }";
    }

    private static String getServer()
    {
        return ObaSettings.getSettings().getApiServerName();
    }

    public static class BuilderBase
    {
        protected final Uri.Builder builder;
        private String              apiKey = API_KEY;

        protected BuilderBase(String path)
        {
            this(path, false);
        }

        protected BuilderBase(String path, boolean noVersion)
        {
            builder = new Uri.Builder();
            builder.scheme("http");
            builder.authority(getServer());
            builder.path(path);
            if (!noVersion)
            {
                builder.appendQueryParameter("version", API_VERSION);
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

    private ObaResponse createResponseWithDataFromJson(String jsonString) throws JSONException, InstantiationException,
                    IllegalAccessException
    {
        JSONObject json = new JSONObject(jsonString);
        JSONReceivable obaResponse = (JSONReceivable) clsData.newInstance();
        return new ObaResponse(json, obaResponse);
    }

    private ObaResponse createResponseWithNoDataDueToError(int code, Throwable err)
    {
        try
        {
            final String failSafeString = "{code:" + code + ",version:\"2\",text:\"" + err.toString() + "\"}";
            JSONObject json = new JSONObject(failSafeString);
            return new ObaResponse(json, null);
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            // Hopefully this never returns null.
            return null;
        }
    }

    public ObaResponse call()
    {
        try
        {
            String response = ObaHelp.getUri(uri);

            // Example responses:
            // ObaCurrentTimeRequest {"text":"OK","data":{"time":1294877371929,"readableTime":"2011-01-12T16:09:31-08:00"},"code":200,"version":1}

            return createResponseWithDataFromJson(response);
        }
        catch (FileNotFoundException e)
        {
            return createResponseWithNoDataDueToError(ObaApi.OBA_NOT_FOUND, e);
        }
        catch (IOException e)
        {
            return createResponseWithNoDataDueToError(ObaApi.OBA_IO_EXCEPTION, e);
        }
        catch (JSONException e)
        {
            return createResponseWithNoDataDueToError(ObaApi.OBA_INTERNAL_ERROR, e);
        }
        catch (InstantiationException e)
        {
            return createResponseWithNoDataDueToError(ObaApi.OBA_INTERNAL_ERROR, e);
        }
        catch (IllegalAccessException e)
        {
            return createResponseWithNoDataDueToError(ObaApi.OBA_INTERNAL_ERROR, e);
        }
    }
}
