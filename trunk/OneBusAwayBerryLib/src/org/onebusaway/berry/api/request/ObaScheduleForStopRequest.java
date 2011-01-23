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

import org.onebusaway.berry.api.Context;
import org.onebusaway.berry.api.ObaCallable;
import org.onebusaway.berry.api.Time;
import org.onebusaway.berry.net.Uri;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Retrieve the full schedule for a stop on a particular day
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_ScheduleForStop}
 *
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaScheduleForStopRequest extends RequestBase {
    protected ObaScheduleForStopRequest(Uri uri) {
        super(uri);
    }

    public static class Builder extends RequestBase.BuilderBase {
        public Builder(Context context, String stopId) {
            super(context, getPathWithId("/schedule-for-stop/", stopId));
        }

        /**
         * Sets the requested date. Defaults to the current date.
         * @param date The requested date.
         * @return This object.
         */
        public Builder setDate(Time date) {
            mBuilder.appendQueryParameter("date", date.format("%Y-%m-%d"));
            return this;
        }

        public ObaScheduleForStopRequest build() {
            return new ObaScheduleForStopRequest(buildUri());
        }
    }

    protected ObaResponse createResponse(JSONObject json) throws JSONException {
        return new ObaScheduleForStopResponse(json);
    }

    protected ObaResponse createResponseFromError(int obaErrorCode, Throwable err) {
        return new ObaScheduleForStopResponse(obaErrorCode, err);
    }

    /*
    @Override
    public ObaScheduleForStopResponse call() {
        return call(ObaScheduleForStopResponse.class);
    }

    @Override
    public String toString() {
        return "ObaScheduleForStopRequest [mUri=" + mUri + "]";
    }
    */
}
