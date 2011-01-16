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

import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Object that defines an Agency element
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_AgencyElementV2}
 *
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaAgencyElement implements ObaAgency {//, JSONReceivable {
    
    public static final ObaAgencyElement EMPTY_OBJECT = new ObaAgencyElement();
    public static final ObaAgencyElement[] EMPTY_ARRAY = new ObaAgencyElement[] {};

    private String id;
    private String name;
    private String url;
    private String timezone;
    private String lang;
    private String phone;
    private String disclaimer;

    public ObaAgencyElement() {
        id = "";
        name = "";
        url = "";
        timezone = "";
        lang = "";
        phone = "";
        disclaimer = "";
    }
    
    public void fromJSON(JSONObject json) throws JSONException
    {
        id = json.getString("id");
        name = json.getString("name");
        url = json.getString("url");
        timezone = json.getString("timezone");
        lang = json.getString("lang");
        phone = json.getString("phone");
        disclaimer = json.getString("disclaimer");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getLang() {
        return lang;
    }

    public String getPhone() {
        return phone;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ObaAgencyElement))
            return false;
        ObaAgencyElement other = (ObaAgencyElement)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String toString() {
        return "ObaAgencyElement [id=" + id + ", name=" + name + "]";
    }
}
