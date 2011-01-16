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
package org.onebusaway.api.request;

import org.onebusaway.api.GeoPoint;
import org.onebusaway.api.ObaApi;
import org.onebusaway.api.elements.ObaReferences;
import org.onebusaway.api.elements.ObaReferencesElement;
import org.onebusaway.api.elements.ObaRoute;
import org.onebusaway.api.elements.ObaStop;
import org.onebusaway.api.elements.ObaStopElement;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Response object for ObaStopRequest requests.
 * @author Paul Watts (paulcwatts@gmail.com) ORIGINAL
 * @author Paul Peavyhouse (pv@swooby.com) BB JME
 */
public final class ObaStopResponse implements ObaStop {
    
    private ObaReferencesElement references;
    private ObaStopElement entry;

    public ObaStopResponse() {
        references = ObaReferencesElement.EMPTY_OBJECT;
        entry = ObaStopElement.EMPTY_OBJECT;
    }

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException
    {
        references = (ObaReferencesElement) ObaApi.newObjectFromJson(json, "reference", ObaReferencesElement.class);
        entry = (ObaStopElement) ObaApi.newObjectFromJson(json, "entry", ObaStopElement.class);
    }
    
    public String getId() {
        return entry.getId();
    }

    public String getStopCode() {
        return entry.getStopCode();
    }

    public String getName() {
        return entry.getName();
    }

    public GeoPoint getLocation() {
        return entry.getLocation();
    }

    public double getLatitude() {
        return entry.getLatitude();
    }

    public double getLongitude() {
        return entry.getLatitude();
    }

    public String getDirection() {
        return entry.getDirection();
    }

    public int getLocationType() {
        return entry.getLocationType();
    }

    public String[] getRouteIds() {
        return entry.getRouteIds();
    }

    /**
     * Returns the list of dereferenced routes.
     */
    public ObaRoute[] getRoutes() {
        return references.getRoutes(entry.getRouteIds());
    }

    protected ObaReferences getRefs() {
        return references;
    }
}
