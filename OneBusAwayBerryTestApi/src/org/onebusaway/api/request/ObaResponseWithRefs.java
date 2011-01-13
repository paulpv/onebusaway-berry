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

import java.util.Vector;

import net.rim.device.api.collection.List;

import org.onebusaway.api.JSONReceivable;
import org.onebusaway.api.elements.ObaAgency;
import org.onebusaway.api.elements.ObaReferences;
import org.onebusaway.api.elements.ObaRoute;
import org.onebusaway.api.elements.ObaSituation;
import org.onebusaway.api.elements.ObaStop;
import org.onebusaway.api.elements.ObaTrip;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public abstract class ObaResponseWithRefs extends ObaResponse implements ObaReferences {

    public ObaResponseWithRefs(JSONObject json, JSONReceivable data) throws JSONException, InstantiationException,
                    IllegalAccessException
    {
        super(json, data);
    }

    public ObaStop getStop(String id) {
        return getRefs().getStop(id);
    }

    public Vector getStops(String[] ids) {
        return getRefs().getStops(ids);
    }

    public ObaRoute getRoute(String id) {
        return getRefs().getRoute(id);
    }

    public Vector getRoutes(String[] ids) {
        return getRefs().getRoutes(ids);
    }

    public ObaTrip getTrip(String id) {
        return getRefs().getTrip(id);
    }

    public Vector getTrips(String[] ids) {
        return getRefs().getTrips(ids);
    }

    public ObaAgency getAgency(String id) {
        return getRefs().getAgency(id);
    }

    public Vector getAgencies(String[] ids) {
        return getRefs().getAgencies(ids);
    }

    public ObaSituation getSituation(String id) {
        return getRefs().getSituation(id);
    }

    public Vector getSituations(String[] ids) {
        return getRefs().getSituations(ids);
    }

    abstract protected ObaReferences getRefs();
}
