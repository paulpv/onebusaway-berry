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

import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * @author Paul Watts (paulcwatts@gmail.com)
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 */
public final class ObaRouteSchedule {
    public static final ObaRouteSchedule   EMPTY_OBJECT = new ObaRouteSchedule();
    public static final ObaRouteSchedule[] EMPTY_ARRAY  = new ObaRouteSchedule[] {};

    public static final class Time {
        private static final Time[] EMPTY_ARRAY = new Time[] {};

        private final String        tripId;
        private final String        serviceId;
        private final String        stopHeadsign;
        private final long          arrivalTime;
        private final long          departureTime;

        Time() {
            tripId = "";
            serviceId = "";
            stopHeadsign = "";
            arrivalTime = 0;
            departureTime = 0;
        }

        public Time(JSONObject json) throws JSONException {
            tripId = json.getString("tripId");
            serviceId = json.getString("serviceId");
            stopHeadsign = json.getString("stopHeadsign");
            arrivalTime = json.getLong("arrivalTime");
            departureTime = json.getLong("departureTime");
        }

        /**
         * @return The ID for the trip of the scheduled transit vehicle.
         */
        public String getTripId() {
            return tripId;
        }

        /**
         * @return The service ID for the scheduled trip.
         */
        public String getServiceId() {
            return serviceId;
        }

        /**
         * @return The headsign for the trip at the stop.
         */
        public String getStopHeadsign() {
            return stopHeadsign;
        }

        /**
         * @return The time in milliseconds since the epoch that the transit vehicle
         * will arrive.
         */
        public long getArrivalTime() {
            return arrivalTime;
        }

        /**
         * @return The time in milliseconds since the epoch that the transit vehicle
         * will depart.
         */
        public long getDepartureTime() {
            return departureTime;
        }
    }

    public static final class Direction {
        private static final Direction[] EMPTY_ARRAY = new Direction[] {};

        private final String             tripHeadsign;
        private final Time[]             scheduleStopTimes;

        Direction() {
            tripHeadsign = "";
            scheduleStopTimes = Time.EMPTY_ARRAY;
        }

        public Direction(JSONObject json) throws JSONException {
            this.tripHeadsign = json.getString("tripHeadsign");
            JSONArray jsonScheduleStopTimes = json.getJSONArray("scheduleStopTimes");
            this.scheduleStopTimes = new Time[jsonScheduleStopTimes.length()];
            for (int i = 0; i < this.scheduleStopTimes.length; i++) {
                this.scheduleStopTimes[i] = new Time(jsonScheduleStopTimes.getJSONObject(i));
            }
        }

        /**
         * @return The direction of travel, indicated by the trip's headsign.
         */
        public String getTripHeadsign() {
            return tripHeadsign;
        }

        /**
         * @return A list of stop times for that direction.
         */
        public Time[] getStopTimes() {
            return scheduleStopTimes;
        }
    }

    private final String      routeId;
    private final Direction[] stopRouteDirectionSchedules;

    private ObaRouteSchedule() {
        routeId = "";
        stopRouteDirectionSchedules = Direction.EMPTY_ARRAY;
    }

    public ObaRouteSchedule(JSONObject json) throws JSONException {
        this.routeId = json.getString("routeId");
        JSONArray jsonStopRouteDirectionSchedules = json.getJSONArray("stopRouteDirectionSchedules");
        this.stopRouteDirectionSchedules = new Direction[jsonStopRouteDirectionSchedules.length()];
        for (int i = 0; i < this.stopRouteDirectionSchedules.length; i++) {
            this.stopRouteDirectionSchedules[i] = new Direction(jsonStopRouteDirectionSchedules.getJSONObject(i));
        }
    }

    /**
     * @return The route ID for this schedule.
     */
    public String getRouteId() {
        return routeId;
    }

    /**
     * @return The stop times for this route, split by direction along the route.
     * The trip headsign indicates the direction of travel.
     */
    public Direction[] getDirectionSchedules() {
        return stopRouteDirectionSchedules;
    }
}
