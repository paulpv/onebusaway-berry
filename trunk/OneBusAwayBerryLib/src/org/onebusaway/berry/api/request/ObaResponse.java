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

import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Base class for response objects.
 * @author Paul Watts (paulcwatts@gmail.com) ORIGINAL
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 */
public abstract class ObaResponse {
    private final String version;
    private final int    code;
    private final String text;

    protected ObaResponse() {
        version = ObaApi.VERSION1;
        code = 0;
        text = "ERROR";
    }

    protected ObaResponse(int obaErrorCode, Throwable err) {
        version = ObaApi.VERSION2;
        code = obaErrorCode;
        text = (err == null) ? "UNKNOWN ERROR" : err.toString();
    }

    public ObaResponse(JSONObject json) throws JSONException {
        version = json.getString("version");
        code = json.getInt("code");
        text = json.getString("text");
    }

    /**
     * @return The version of this response.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return The status code (one of the ObaApi.OBA_ constants)
     */
    public int getCode() {
        return code;
    }

    /**
     * @return The status text.
     */
    public String getText() {
        return text;
    }
}
