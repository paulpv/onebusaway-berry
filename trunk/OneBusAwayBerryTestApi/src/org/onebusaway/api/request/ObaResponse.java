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

import org.onebusaway.api.JSONReceivable;
import org.onebusaway.api.ObaApi;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Base class for response objects.
 * @author Paul Watts (paulcwatts@gmail.com) ORIGINAL
 * @author Paul Peavyhouse (pv@swooby.com) JME
 */
public class ObaResponse
{
    private String         version;
    private int            code;
    private String         text;
    private JSONReceivable data;

    public ObaResponse(JSONObject json, JSONReceivable data) throws JSONException
    {
        try
        {
            this.version = json.getString("version");
            this.code = json.getInt("code");
            this.text = json.getString("text");
            this.data = data;
            if (data != null)
            {
                JSONObject jsonData = json.getJSONObject("data");
                if (jsonData != null)
                {
                    this.data.fromJSON(jsonData);
                }
            }
        }
        catch (JSONException e)
        {
            reset();
            throw e;
        }
    }

    public void reset()
    {
        version = ObaApi.VERSION1;
        code = 0;
        text = "ERROR";
        data = null;
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
    
    public JSONReceivable getData()
    {
        return data;
    }
}
