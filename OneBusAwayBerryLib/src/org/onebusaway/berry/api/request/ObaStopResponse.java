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

import org.onebusaway.berry.api.ObaListObaRoute;
import org.onebusaway.berry.api.elements.ObaReferences;
import org.onebusaway.berry.api.elements.ObaReferencesElement;
import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.api.elements.ObaStopElement;
import org.onebusaway.berry.map.GeoPoint;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Response object for ObaStopRequest requests.
 * @author Paul Watts (paulcwatts@gmail.com) ORIGINAL
 * @author Paul Peavyhouse (pv@swooby.com) BB JME
 */
public final class ObaStopResponse extends ObaResponseWithRefs implements ObaStop {

    private static final class Data {
        private static final Data          EMPTY_OBJECT = new Data();

        private final ObaReferencesElement references;
        private final ObaStopElement       entry;

        public Data() {
            references = ObaReferencesElement.EMPTY_OBJECT;
            entry = ObaStopElement.EMPTY_OBJECT;
        }

        public Data(JSONObject json) throws JSONException {
            references = new ObaReferencesElement(json.getJSONObject("references"));
            entry = new ObaStopElement(json.getJSONObject("entry"));
        }
    }

    private final Data data;

    private ObaStopResponse() {
        super();
        data = Data.EMPTY_OBJECT;
    }

    public ObaStopResponse(int obaErrorCode, Throwable err) {
        super(obaErrorCode, err);
        data = Data.EMPTY_OBJECT;
    }

    public ObaStopResponse(JSONObject json) throws JSONException {
        super(json);
        data = new Data(json.getJSONObject("data"));
    }

    //@Override
    public String getId() {
        return data.entry.getId();
    }

    //@Override
    public String getStopCode() {
        return data.entry.getStopCode();
    }

    //@Override
    public String getName() {
        return data.entry.getName();
    }

    //@Override
    public GeoPoint getLocation() {
        return data.entry.getLocation();
    }

    //@Override
    public double getLatitude() {
        return data.entry.getLatitude();
    }

    //@Override
    public double getLongitude() {
        return data.entry.getLatitude();
    }

    //@Override
    public String getDirection() {
        return data.entry.getDirection();
    }

    //@Override
    public int getLocationType() {
        return data.entry.getLocationType();
    }

    //@Override
    public String[] getRouteIds() {
        return data.entry.getRouteIds();
    }

    /**
     * Returns the list of dereferenced routes.
     */
    public ObaListObaRoute getRoutes() {
        return data.references.getRoutes(data.entry.getRouteIds());
    }

    //@Override
    protected ObaReferences getRefs() {
        return data.references;
    }
}
