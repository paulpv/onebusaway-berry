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
package org.onebusaway.berry.test.api.request.test;

import j2meunit.framework.Test;
import j2meunit.framework.TestSuite;

import org.onebusaway.berry.api.Time;
import org.onebusaway.berry.api.elements.ObaRouteSchedule;
import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.api.elements.ObaStopSchedule;
import org.onebusaway.berry.api.request.ObaScheduleForStopRequest;
import org.onebusaway.berry.api.request.ObaScheduleForStopResponse;


public class ScheduleForStopTest extends ObaTestCase {
    
    public ScheduleForStopTest() {
        super();
    }

    public ScheduleForStopTest(String testName) {
        super(testName);
    }
    
    public Test suite() {
        TestSuite suite = new TestSuite("ScheduleForStopTest");

        // TODO:(pv) The response of these seems to be malformed compared to expected; verify if the same in Android.
        /*
        suite.addTest(new ScheduleForStopTest("testKCMStop")
        {
            public void runTest() {
                testKCMStop();
            }
        });
        suite.addTest(new ScheduleForStopTest("testDate")
        {
            public void runTest() {
                testDate();
            }
        });
        */
        
        return suite;
    }
    
    public void testKCMStop() {
        // TODO:(pv) The response seems to be malformed compared to expected; verify if the same in Android.
        ObaScheduleForStopResponse response =
            (ObaScheduleForStopResponse) new ObaScheduleForStopRequest.Builder(getContext(), "1_75403")
            .build()
            .call();
        // This is just to ensure we can call it, but since we don't
        // know the day we can't really assume very much.
        assertOK(response);
        final ObaStop stop = response.getStop();
        assertEquals("1_75403", stop.getId());
        final ObaStopSchedule.CalendarDay[] days = response.getCalendarDays();
        assertTrue(days.length > 0);
        final ObaRouteSchedule[] schedules = response.getRouteSchedules();
        assertTrue(schedules.length > 0);
        final ObaRouteSchedule.Direction[] dirs = schedules[0].getDirectionSchedules();
        assertTrue(dirs.length > 0);
    }

    public void testDate() {
        // TODO:(pv) The response seems to be malformed compared to expected; verify if the same in Android. 
        Time time = new Time();
        time.setYear(2010);
        time.setMonth(7);
        time.setDayOfMonth(23);
        ObaScheduleForStopResponse response =
            (ObaScheduleForStopResponse) new ObaScheduleForStopRequest.Builder(getContext(), "1_75403")
            .setDate(time)
            .build()
            .call();
        assertOK(response);
        final ObaStop stop = response.getStop();
        assertEquals("1_75403", stop.getId());
        final ObaRouteSchedule[] schedules = response.getRouteSchedules();
        assertTrue(schedules.length > 0);
        assertEquals("29_810", schedules[0].getRouteId());
        final ObaRouteSchedule.Direction[] dirs = schedules[0].getDirectionSchedules();
        assertTrue(dirs.length > 0);
        assertEquals("McCollum Park Park &Ride", dirs[0].getTripHeadsign());
        final ObaRouteSchedule.Time[] times = dirs[0].getStopTimes();
        assertTrue(times.length > 0);
        assertEquals("29_10JUN-Weekday-810s-0", times[0].getTripId());
    }
}
