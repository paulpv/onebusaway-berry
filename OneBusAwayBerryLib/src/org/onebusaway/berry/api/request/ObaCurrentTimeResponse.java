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

import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public final class ObaCurrentTimeResponse extends ObaResponse {
    private static final class Data {
        private static final Data EMPTY_OBJECT = new Data();

        private final long        time;
        private final String      readableTime;

        private Data() {
            time = 0;
            readableTime = "";
        }

        private Data(JSONObject json) throws JSONException {
            time = json.getLong("time");
            readableTime = json.getString("readableTime");
        }
    }

    private final Data data;

    private ObaCurrentTimeResponse() {
        super();
        data = Data.EMPTY_OBJECT;
    }

    public ObaCurrentTimeResponse(int obaErrorCode, Throwable err) {
        super(obaErrorCode, err);
        data = Data.EMPTY_OBJECT;
    }

    public ObaCurrentTimeResponse(JSONObject json) throws JSONException {
        super(json);
        data = new Data(json.getJSONObject("data"));
    }

    /**
     * @return The time as milliseconds past the epoch.
     */
    public long getTime() {
        return data.time;
    }

    /**
     * @return The time in ISO8601 format.
     */
    public String getReadableTime() {
        return data.readableTime;
    }
}
