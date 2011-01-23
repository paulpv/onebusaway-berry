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
package org.onebusaway.berry.api.elements;

import net.rim.device.api.ui.Color;

import org.onebusaway.berry.api.ObaInteger;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Object defining a Route element.
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_RouteElementV2}
 *
 * @author Paul Watts (paulcwatts@gmail.com)
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 */
public final class ObaRouteElement implements ObaRoute {
    public static final ObaRouteElement   EMPTY_OBJECT = new ObaRouteElement();
    public static final ObaRouteElement[] EMPTY_ARRAY  = new ObaRouteElement[] {};

    private final String                  id;
    private final String                  shortName;
    private final String                  longName;
    private final String                  description;
    private final int                     type;
    private final String                  url;
    private final String                  color;
    private final String                  textColor;
    private final String                  agencyId;

    public ObaRouteElement() {
        id = "";
        shortName = "";
        longName = "";
        description = "";
        type = TYPE_BUS;
        url = "";
        color = "";
        textColor = "";
        agencyId = "";
    }

    public ObaRouteElement(JSONObject json) throws JSONException {
        id = json.getString("id");
        shortName = json.getString("shortName");
        longName = json.getString("longName");
        description = json.getString("description");
        type = json.getInt("type");
        url = json.getString("url");
        color = json.getString("color");
        textColor = json.getString("textColor");
        agencyId = json.getString("agencyId");
    }

    //@Override
    public String getId() {
        return id;
    }

    //@Override
    public String getShortName() {
        return shortName;
    }

    //@Override
    public String getLongName() {
        return longName;
    }

    //@Override
    public String getDescription() {
        return description;
    }

    //@Override
    public int getType() {
        return type;
    }

    //@Override
    public String getUrl() {
        return url;
    }

    //@Override
    public int getColor() {
        return 0xFF000000 | ObaInteger.getInteger(color, Color.WHITE);
    }

    //@Override
    public int getTextColor() {
        return 0xFF000000 | ObaInteger.getInteger(textColor, Color.BLACK);
    }

    //@Override
    public String getAgencyId() {
        return agencyId;
    }

    //@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    //@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ObaRouteElement))
            return false;
        ObaRouteElement other = (ObaRouteElement) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    //@Override
    public String toString() {
        return "ObaRouteElement [id=" + id + "]";
    }
}
