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
import java.io.Reader;

import net.rim.device.api.io.FileNotFoundException;

import org.onebusaway.api.JSONReceivable;
import org.onebusaway.api.ObaApi;
import org.onebusaway.api.ObaHelp;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.net.Uri;
import org.onebusaway.rim.settings.ObaSettings;

/**
 * The base class for Oba requests.
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public class RequestBase
{

    protected final Uri mUri;

    protected RequestBase(Uri uri)
    {
        mUri = uri;
    }

    private static String getServer()
    {
        return ObaSettings.getSettings().getApiServerName();
    }

    public static class BuilderBase
    {

        private static final String   API_KEY   = "v1_BktoDJ2gJlu6nLM6LsT9H8IUbWc=cGF1bGN3YXR0c0BnbWFpbC5jb20=";
        protected static final String BASE_PATH = "/api/where";

        protected final Uri.Builder   mBuilder;
        private String                mApiKey   = API_KEY;

        protected BuilderBase(String path)
        {
            mBuilder = new Uri.Builder();
            mBuilder.scheme("http");
            mBuilder.authority(getServer());
            mBuilder.path(path);
            mBuilder.appendQueryParameter("version", "2");
            ObaApi.setAppInfo(mBuilder);
        }

        protected BuilderBase(String path, boolean noVersion)
        {
            mBuilder = new Uri.Builder();
            mBuilder.scheme("http");
            mBuilder.authority(getServer());
            mBuilder.path(path);
            ObaApi.setAppInfo(mBuilder);
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
            mBuilder.appendQueryParameter("key", mApiKey);
            return mBuilder.build();
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
            mBuilder.authority(server);
        }

        /**
         * Allows the caller to assign a different API key for a specific request.
         *
         * Because this is implemented in the base class, it can't return 'this'
         * to use the standard builder pattern. Oh well, it's only for test.
         */
        public void setApiKey(String key)
        {
            mApiKey = key;
        }
    }

    private ObaResponse createFromError(Class cls, int code, Throwable err)
    {
        if (cls == null || cls.isInstance(ObaResponse.class))
        {
            // TODO:(pv) Should this instead return an error object below?
            throw new IllegalArgumentException();
        }

        // This is not very efficient, but it's an error case and it's easier
        // than instantiating one ourselves.
        //final String jsonErr =  ObaApi.getGson().toJson(error);
        //final String json = String.format("{code: %d,version:\"2\",text:%s}", code, jsonErr);

        // Hopefully this never returns null.
        return null;//ObaApi.getGson().fromJson(json, cls);
    }

    protected ObaResponse call(Class clsData)
    {
        if (clsData == null || clsData.isInstance(JSONReceivable.class))
        {
            // TODO:(pv) Should this instead return an error object below?
            throw new IllegalArgumentException();
        }

        try
        {
            String response = ObaHelp.getUri(mUri);

            // Example response for ObaCurrentTimeRequest
            // {"text":"OK","data":{"time":1294877371929,"readableTime":"2011-01-12T16:09:31-08:00"},"code":200,"version":1}

            ObaResponse obaResponse = new ObaResponse(response, clsData);
            return obaResponse;

        }
        catch (FileNotFoundException e)
        {
            return createFromError(clsData, ObaApi.OBA_NOT_FOUND, e);
        }
        catch (IOException e)
        {
            return createFromError(clsData, ObaApi.OBA_IO_EXCEPTION, e);
        }
        catch (JSONException e)
        {
            return createFromError(clsData, ObaApi.OBA_INTERNAL_ERROR, e);
        }
        catch (InstantiationException e)
        {
            return createFromError(clsData, ObaApi.OBA_INTERNAL_ERROR, e);
        }
        catch (IllegalAccessException e)
        {
            return createFromError(clsData, ObaApi.OBA_INTERNAL_ERROR, e);
        }
    }
}
