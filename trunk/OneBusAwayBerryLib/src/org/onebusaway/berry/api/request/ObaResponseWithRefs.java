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

import java.util.Vector;

import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.berry.api.elements.ObaAgency;
import org.onebusaway.berry.api.elements.ObaReferences;
import org.onebusaway.berry.api.elements.ObaReferencesElement;
import org.onebusaway.berry.api.elements.ObaRoute;
import org.onebusaway.berry.api.elements.ObaSituation;
import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.api.elements.ObaTrip;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public abstract class ObaResponseWithRefs extends ObaResponse implements ObaReferences
{
    protected ObaReferencesElement references;

    public ObaResponseWithRefs()
    {
        references = ObaReferencesElement.EMPTY_OBJECT;
    }

    public JSONObject fromJSON(String jsonString) throws JSONException, InstantiationException, IllegalAccessException
    {
        JSONObject jsonData = super.fromJSON(jsonString);
        
        references = new ObaReferencesElement();
        ObaApi.fromJSON(jsonData, "references", references);

        return jsonData;
    }

    public ObaStop getStop(String id)
    {
        return references.getStop(id);
    }

    public Vector getStops(String[] ids)
    {
        return references.getStops(ids);
    }

    public ObaRoute getRoute(String id)
    {
        return references.getRoute(id);
    }

    public Vector getRoutes(String[] ids)
    {
        return references.getRoutes(ids);
    }

    public ObaTrip getTrip(String id)
    {
        return references.getTrip(id);
    }

    public Vector getTrips(String[] ids)
    {
        return references.getTrips(ids);
    }

    public ObaAgency getAgency(String id)
    {
        return references.getAgency(id);
    }

    public Vector getAgencies(String[] ids)
    {
        return references.getAgencies(ids);
    }

    public ObaSituation getSituation(String id)
    {
        return references.getSituation(id);
    }

    public Vector getSituations(String[] ids)
    {
        return references.getSituations(ids);
    }
}
