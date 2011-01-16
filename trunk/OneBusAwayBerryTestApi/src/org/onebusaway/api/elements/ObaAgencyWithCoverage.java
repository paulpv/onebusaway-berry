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
package org.onebusaway.api.elements;

import org.onebusaway.api.GeoPoint;
import org.onebusaway.api.ObaApi;
import org.onebusaway.api.ObaReceivable;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public final class ObaAgencyWithCoverage implements ObaElement, ObaReceivable
{
    public static final ObaAgencyWithCoverage[] EMPTY_ARRAY = new ObaAgencyWithCoverage[] {};

    private String                              agencyId;
    private double                              lat;
    private double                              lon;
    private double                              latSpan;
    private double                              lonSpan;

    public ObaAgencyWithCoverage()
    {
        agencyId = "";
        lat = 0;
        lon = 0;
        latSpan = 0;
        lonSpan = 0;
    }

    public void fromJSON(JSONObject json) throws JSONException
    {
        agencyId = json.getString("agencyId");
        lat = json.getDouble("lat");
        lon = json.getDouble("lon");
        latSpan = json.getDouble("latSpan");
        lonSpan = json.getDouble("lonSpan");
    }

    public String getId()
    {
        return agencyId;
    }

    /**
     * @return The center point of the agency's coverage area.
     */
    public GeoPoint getPoint()
    {
        return ObaApi.makeGeoPoint(lat, lon);
    }

    /**
     * @return The latitude height of the coverage bounding box.
     */
    public double getLatitudeSpan()
    {
        return latSpan;
    }

    /**
     * @return The longitude hight of the coverage bounding box.
     */
    public double getLongitudeSpan()
    {
        return lonSpan;
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((agencyId == null) ? 0 : agencyId.hashCode());
        return result;
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ObaAgencyWithCoverage))
            return false;
        ObaAgencyWithCoverage other = (ObaAgencyWithCoverage) obj;
        if (agencyId == null)
        {
            if (other.agencyId != null)
                return false;
        }
        else if (!agencyId.equals(other.agencyId))
            return false;
        return true;
    }

    public String toString()
    {
        return "ObaAgencyWithCoverage [agencyId=" + agencyId + "]";
    }
}
