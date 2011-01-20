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
import org.onebusaway.berry.api.ObaInteger;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * @author Paul Watts (paulcwatts@gmail.com)
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 */
public final class ObaTripElement implements ObaTrip, JSONReceivable {
    public static final ObaTripElement   EMPTY_OBJECT = new ObaTripElement();
    public static final ObaTripElement[] EMPTY_ARRAY  = new ObaTripElement[] {};

    private String                       id;
    private String                       tripShortName;
    private String                       shapeId;
    private String                       directionId;
    private String                       serviceId;
    private String                       tripHeadsign;
    private String                       timeZone;
    private String                       routeId;

    public ObaTripElement() {
        id = "";
        tripShortName = "";
        shapeId = "";
        directionId = "";
        serviceId = "";
        tripHeadsign = "";
        timeZone = "";
        routeId = "";
    }

    public void fromJSON(JSONObject json) throws JSONException {
        id = json.getString("id");
        tripShortName = json.getString("tripShortName");
        shapeId = json.getString("shapeId");
        directionId = json.getString("directionId");
        serviceId = json.getString("serviceId");
        tripHeadsign = json.getString("tripHeadsign");
        timeZone = json.getString("timeZone");
        routeId = json.getString("routeId");
    }

    //@Override
    public String getId() {
        return id;
    }

    //@Override
    public String getShortName() {
        return tripShortName;
    }

    //@Override
    public String getShapeId() {
        return shapeId;
    }

    //@Override
    public int getDirectionId() {
        return ObaInteger.getInteger(directionId, DIRECTION_OUTBOUND);
    }

    //@Override
    public String getServiceId() {
        return serviceId;
    }

    //@Override
    public String getHeadsign() {
        return tripHeadsign;
    }

    //@Override
    public String getTimezone() {
        return timeZone;
    }

    //@Override
    public String getRouteId() {
        return routeId;
    }
}
