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
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public final class ObaCurrentTimeResponse extends ObaResponse implements JSONReceivable
{
    private long   time;
    private String readableTime;

    public ObaCurrentTimeResponse()
    {
        time = 0;
        readableTime = "";
    }

    public void fromJSON(JSONObject json) throws JSONException
    {
        time = json.getLong("time");
        readableTime = json.getString("readableTime");
    }

    /**
     * @return The time as milliseconds past the epoch.
     */
    public long getTime()
    {
        return time;
    }

    /**
     * @return The time in ISO8601 format.
     */
    public String getReadableTime()
    {
        return readableTime;
    }
}