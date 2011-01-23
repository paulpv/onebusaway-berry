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

import org.onebusaway.berry.api.elements.ObaReferences;
import org.onebusaway.berry.api.elements.ObaReferencesElement;
import org.onebusaway.berry.api.elements.ObaRouteSchedule;
import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.api.elements.ObaStopSchedule;
import org.onebusaway.berry.api.elements.ObaStopScheduleElement;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Response object for ObaScheduleForStopRequest requests.
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaScheduleForStopResponse extends ObaResponseWithRefs implements ObaStopSchedule {

    private static final class Data {
        private static final Data            EMPTY_OBJECT = new Data();

        private final ObaReferencesElement   references;
        private final ObaStopScheduleElement entry;

        private Data() {
            references = ObaReferencesElement.EMPTY_OBJECT;
            entry = ObaStopScheduleElement.EMPTY_OBJECT;
        }

        public Data(JSONObject json) throws JSONException {
            references = new ObaReferencesElement(json.getJSONObject("references"));
            entry = new ObaStopScheduleElement(json.getJSONObject("entry"));
        }
    }

    private final Data data;

    private ObaScheduleForStopResponse() {
        super();
        data = Data.EMPTY_OBJECT;
    }

    public ObaScheduleForStopResponse(int obaErrorCode, Throwable err) {
        super(obaErrorCode, err);
        data = Data.EMPTY_OBJECT;
    }

    public ObaScheduleForStopResponse(JSONObject json) throws JSONException {
        super(json);
        data = new Data(json.getJSONObject("data"));
    }

    //@Override
    public ObaStop getStop() {
        return data.entry.getStop();
    }

    //@Override
    public String getTimeZone() {
        return data.entry.getTimeZone();
    }

    //@Override
    public long getDate() {
        return data.entry.getDate();
    }

    //@Override
    public CalendarDay[] getCalendarDays() {
        return data.entry.getCalendarDays();
    }

    //@Override
    public ObaRouteSchedule[] getRouteSchedules() {
        return data.entry.getRouteSchedules();
    }

    //@Override
    protected ObaReferences getRefs() {
        return data.references;
    }
}
