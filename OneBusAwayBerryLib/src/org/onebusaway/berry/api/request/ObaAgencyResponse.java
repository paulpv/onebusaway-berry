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
public final class ObaAgencyResponse extends ObaResponse implements ObaAgency {
    private static final class Data {
        private static final Data      EMPTY_OBJECT = new Data();

        private final ObaAgencyElement entry;

        protected Data() {
            entry = ObaAgencyElement.EMPTY_OBJECT;
        }

        public Data(JSONObject json) throws JSONException {
            entry = new ObaAgencyElement(json.getJSONObject("entry"));
        }
    }

    private final Data data;

    private ObaAgencyResponse() {
        super();
        data = Data.EMPTY_OBJECT;
    }

    public ObaAgencyResponse(int obaErrorCode, Throwable err) {
        super(obaErrorCode, err);
        data = Data.EMPTY_OBJECT;
    }

    public ObaAgencyResponse(JSONObject json) throws JSONException {
        super(json);
        data = new Data(json.getJSONObject("data"));
    }

    //@Override
    public String getId() {
        return data.entry.getId();
    }

    //@Override
    public String getName() {
        return data.entry.getName();
    }

    //@Override
    public String getUrl() {
        return data.entry.getUrl();
    }

    //@Override
    public String getTimezone() {
        return data.entry.getTimezone();
    }

    //@Override
    public String getLang() {
        return data.entry.getLang();
    }

    //@Override
    public String getPhone() {
        return data.entry.getPhone();
    }

    //@Override
    public String getDisclaimer() {
        return data.entry.getDisclaimer();
    }
}
