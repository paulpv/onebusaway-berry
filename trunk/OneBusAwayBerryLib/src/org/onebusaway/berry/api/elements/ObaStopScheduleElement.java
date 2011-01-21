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
package org.onebusaway.berry.api.elements;

import org.onebusaway.berry.api.JSONReceivable;
import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * @author Paul Watts (paulcwatts@gmail.com)
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 */
public final class ObaStopScheduleElement implements ObaStopSchedule, JSONReceivable {
    public static final ObaStopScheduleElement EMPTY_OBJECT = new ObaStopScheduleElement();

    private ObaStopElement               stop = ObaStopElement.EMPTY_OBJECT; 
    private String                       timeZone = "";
    private long                         date = 0;
    private CalendarDay[]                stopCalendarDays = CalendarDay.EMPTY_ARRAY;
    private ObaRouteSchedule[]           stopRouteSchedules = ObaRouteSchedule.EMPTY_ARRAY;

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
        stop = (ObaStopElement) ObaApi.fromJSON(json, "stop", new ObaStopElement());
        timeZone = json.getString("timeZone");
        date = json.getLong("date");
        JSONArray jsonStopCalendarDays = json.getJSONArray("stopCalendarDays");
        stopCalendarDays = (CalendarDay[]) ObaApi.fromJSON(jsonStopCalendarDays, new CalendarDay[jsonStopCalendarDays.length()], CalendarDay.class);
        JSONArray jsonStopRouteSchedules = json.getJSONArray("stopRouteSchedules");
        stopRouteSchedules = (ObaRouteSchedule[]) ObaApi.fromJSON(jsonStopRouteSchedules, new ObaRouteSchedule[jsonStopRouteSchedules.length()], ObaRouteSchedule.class);
    }
    
    //@Override
    public ObaStop getStop() {
        return stop;
    }

    //@Override
    public String getTimeZone() {
        return timeZone;
    }

    //@Override
    public long getDate() {
        return date;
    }

    //@Override
    public CalendarDay[] getCalendarDays() {
        return stopCalendarDays;
    }

    //@Override
    public ObaRouteSchedule[] getRouteSchedules() {
        return stopRouteSchedules;
    }
}
