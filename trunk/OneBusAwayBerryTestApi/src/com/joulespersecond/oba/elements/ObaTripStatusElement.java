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
package com.joulespersecond.oba.elements;

import com.joulespersecond.oba.GeoPoint;

public final class ObaTripStatusElement implements ObaTripStatus {
    protected static final ObaTripStatusElement EMPTY_OBJECT = new ObaTripStatusElement();

    private final long serviceDate;
    private final boolean predicted;
    private final long scheduleDeviation;
    private final String vehicleId;
    private final String closestStop;
    private final long closestStopTimeOffset;
    private final Position position;
    private final String activeTripId;
    private final Double distanceAlongTrip;
    private final Double scheduledDistanceAlongTrip;
    private final Double totalDistanceAlongTrip;
    private final Double orientation;
    private final String nextStop;
    private final long nextStopTimeOffset;
    private final String phase;
    private final String status;
    private final Long lastUpdateTime;
    private final Position lastKnownLocation;
    private final Double lastKnownOrientation;

    ObaTripStatusElement() {
        serviceDate = 0;
        predicted = false;
        scheduleDeviation = 0;
        vehicleId = "";
        closestStop = "";
        closestStopTimeOffset = 0;
        position = null;
        activeTripId = null;
        distanceAlongTrip = null;
        scheduledDistanceAlongTrip = null;
        totalDistanceAlongTrip = null;
        orientation = null;
        nextStop = null;
        nextStopTimeOffset = 0;
        phase = null;
        status = null;
        lastUpdateTime = null;
        lastKnownLocation = null;
        lastKnownOrientation = null;
    }

    public long getServiceDate() {
        return serviceDate;
    }

    public boolean isPredicted() {
        return predicted;
    }

    public long getScheduleDeviation() {
        return scheduleDeviation;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getClosestStop() {
        return closestStop;
    }

    public long getClosestStopTimeOffset() {
        return closestStopTimeOffset;
    }

    public GeoPoint getPosition() {
        return (position != null) ? position.getPoint() : null;
    }

    public String getActiveTripId() {
        return activeTripId;
    }

    public Double getDistanceAlongTrip() {
        return distanceAlongTrip;
    }

    public Double getScheduledDistanceAlongTrip() {
        return scheduledDistanceAlongTrip;
    }

    public Double getTotalDistanceAlongTrip() {
        return totalDistanceAlongTrip;
    }

    public Double getOrientation() {
        return orientation;
    }

    public String getNextStop() {
        return nextStop;
    }

    public Long getNextStopTimeOffset() {
        return new Long(nextStopTimeOffset);
    }

    public String getPhase() {
        return phase;
    }

    public String getStatus() {
        return status;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime.longValue();
    }

    public GeoPoint getLastKnownLocation() {
        return (lastKnownLocation != null) ? lastKnownLocation.getPoint() : null;
    }

    public Double getLastKnownOrientation() {
        return lastKnownOrientation;
    }
}
