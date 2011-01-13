package org.onebusaway.api;

import org.onebusaway.json.me.JSONException;

public interface JSONSendable
{
    String toJSON() throws JSONException;
}