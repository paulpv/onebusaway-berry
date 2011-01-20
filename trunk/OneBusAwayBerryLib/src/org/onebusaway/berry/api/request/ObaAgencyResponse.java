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

import org.onebusaway.berry.api.JSONReceivable;
import org.onebusaway.berry.api.elements.ObaAgency;
import org.onebusaway.berry.api.elements.ObaAgencyElement;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Response for an ObaAgencyRequest request.
 *
 * @author Paul Watts (paulcwatts@gmail.com) ORIGINAL
 * @author Paul Peavyhouse (pv@swooby.com) JME
 */
public final class ObaAgencyResponse extends ObaResponse implements ObaAgency, JSONReceivable {
    private ObaAgencyElement entry;

    public ObaAgencyResponse() {
        entry = ObaAgencyElement.EMPTY_OBJECT;
    }

    public void fromJSON(JSONObject json) throws JSONException {
        JSONObject jsonEntry = json.getJSONObject("entry");
        entry = new ObaAgencyElement();
        entry.fromJSON(jsonEntry);
    }

    //@Override
    public String getId() {
        return entry.getId();
    }

    //@Override
    public String getName() {
        return entry.getName();
    }

    //@Override
    public String getUrl() {
        return entry.getUrl();
    }

    //@Override
    public String getTimezone() {
        return entry.getTimezone();
    }

    //@Override
    public String getLang() {
        return entry.getLang();
    }

    //@Override
    public String getPhone() {
        return entry.getPhone();
    }

    //@Override
    public String getDisclaimer() {
        return entry.getDisclaimer();
    }
}