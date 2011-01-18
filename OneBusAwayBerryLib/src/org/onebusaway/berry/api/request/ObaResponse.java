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
package org.onebusaway.berry.api.request;

import org.onebusaway.berry.api.JSONReceivable;
import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Base class for response objects.
 * @author Paul Watts (paulcwatts@gmail.com) ORIGINAL
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 */
public abstract class ObaResponse implements JSONReceivable
{
    private String version;
    private int    code;
    private String text;

    public ObaResponse()
    {
        version = ObaApi.VERSION1;
        code = 0;
        text = "ERROR";
    }

    protected void fromError(int obaErrorCode, Throwable err)
    {
        this.version = ObaApi.VERSION2;
        this.code = obaErrorCode;
        this.text = (err == null) ? "UNKNOWN ERROR" : err.toString();
    }

    /**
     * Parses the jsonString and populates the required fields "version", "code", "text", and "data".
     * Returns the response's data field as a JSONObject so that the caller/subclass can retrieve other JSON objects.
     *  
     * @param jsonString with fields "version", "code", "text", "data".
     * @return jsonString's "data" field as a JSONObject  
     * @throws JSONException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public JSONObject fromJSON(String jsonString) throws JSONException, InstantiationException, IllegalAccessException
    {
        JSONObject json = new JSONObject(jsonString);

        this.version = json.getString("version");
        this.code = json.getInt("code");
        this.text = json.getString("text");
        JSONObject jsonData = json.getJSONObject("data");
        fromJSON(jsonData);

        return jsonData;
    }

    /**
     * @return The version of this response.
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * @return The status code (one of the ObaApi.OBA_ constants)
     */
    public int getCode()
    {
        return code;
    }

    /**
     * @return The status text.
     */
    public String getText()
    {
        return text;
    }
}
