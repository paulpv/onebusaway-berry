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


public final class ObaStopScheduleElement implements ObaStopSchedule {
    public static final ObaStopScheduleElement EMPTY_OBJECT = new ObaStopScheduleElement();

    private final ObaStopElement stop;
    private final String timeZone;
    private final long date;
    private final CalendarDay[] stopCalendarDays;
    private final ObaRouteSchedule[] stopRouteSchedules;

    ObaStopScheduleElement() {
        stop = ObaStopElement.EMPTY_OBJECT;
        timeZone = "";
        date = 0;
        stopCalendarDays = CalendarDay.EMPTY_ARRAY;
        stopRouteSchedules = ObaRouteSchedule.EMPTY_ARRAY;
    }

    public ObaStop getStop() {
        return stop;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public long getDate() {
        return date;
    }

    public CalendarDay[] getCalendarDays() {
        return stopCalendarDays;
    }

    public ObaRouteSchedule[] getRouteSchedules() {
        return stopRouteSchedules;
    }
}