package org.onebusaway.api;

import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public interface JSONReceivable
{
    void reset();
    void fromJSON(JSONObject jsonObject) throws JSONException;
}
