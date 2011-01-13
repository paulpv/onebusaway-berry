package org.onebusaway.api;

import org.onebusaway.json.me.JSONException;

public interface JSONReceivable
{
    void reset();
    void fromJSON(String jsonString) throws JSONException;
}
