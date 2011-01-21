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
import org.onebusaway.berry.api.elements.ObaAgencyWithCoverage;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public final class ObaAgenciesWithCoverageResponse extends ObaResponseWithRefs {
    
    private ObaAgencyWithCoverage[] list = ObaAgencyWithCoverage.EMPTY_ARRAY;
    private boolean limitExceeded = false;

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
        JSONArray jsonList = json.getJSONArray("list");
        list = (ObaAgencyWithCoverage[]) ObaApi.fromJSON(jsonList, new ObaAgencyWithCoverage[jsonList.length()], ObaAgencyWithCoverage.class);
        limitExceeded = json.getBoolean("limitExceeded");
    }
    
    public ObaAgencyWithCoverage[] getAgencies() {
        return list;
    }

    /**
     * @return Whether the request exceeded the maximum response size.
     */
    public boolean getLimitExceeded() {
        return limitExceeded;
    }

    /*
    @Override
    protected ObaReferences getRefs() {
        return data.references;
    }
    */
}
