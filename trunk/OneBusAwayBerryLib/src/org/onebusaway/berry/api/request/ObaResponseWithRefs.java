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

import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.berry.api.ObaListObaAgency;
import org.onebusaway.berry.api.ObaListObaRoute;
import org.onebusaway.berry.api.ObaListObaSituation;
import org.onebusaway.berry.api.ObaListObaStop;
import org.onebusaway.berry.api.ObaListObaTrip;
import org.onebusaway.berry.api.elements.ObaAgency;
import org.onebusaway.berry.api.elements.ObaReferences;
import org.onebusaway.berry.api.elements.ObaReferencesElement;
import org.onebusaway.berry.api.elements.ObaRoute;
import org.onebusaway.berry.api.elements.ObaSituation;
import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.api.elements.ObaTrip;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public abstract class ObaResponseWithRefs extends ObaResponse implements ObaReferences {
    protected ObaReferencesElement references;

    public ObaResponseWithRefs() {
        references = ObaReferencesElement.EMPTY_OBJECT;
    }

    public JSONObject fromJSON(String jsonString) throws JSONException, InstantiationException, IllegalAccessException {
        JSONObject jsonData = super.fromJSON(jsonString);

        references = (ObaReferencesElement) ObaApi.fromJSON(jsonData, "references", new ObaReferencesElement());

        return jsonData;
    }

    //@Override
    public ObaStop getStop(String id) {
        return references.getStop(id);
    }

    //@Override
    public ObaListObaStop getStops(String[] ids) {
        return references.getStops(ids);
    }

    //@Override
    public ObaRoute getRoute(String id) {
        return references.getRoute(id);
    }

    //@Override
    public ObaListObaRoute getRoutes(String[] ids) {
        return references.getRoutes(ids);
    }

    //@Override
    public ObaTrip getTrip(String id) {
        return references.getTrip(id);
    }

    //@Override
    public ObaListObaTrip getTrips(String[] ids) {
        return references.getTrips(ids);
    }

    //@Override
    public ObaAgency getAgency(String id) {
        return references.getAgency(id);
    }

    //@Override
    public ObaListObaAgency getAgencies(String[] ids) {
        return references.getAgencies(ids);
    }

    //@Override
    public ObaSituation getSituation(String id) {
        return references.getSituation(id);
    }

    //@Override
    public ObaListObaSituation getSituations(String[] ids) {
        return references.getSituations(ids);
    }
}
