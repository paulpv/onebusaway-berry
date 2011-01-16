package org.onebusaway.api;

import org.onebusaway.json.me.JSONException;

public interface ObaSendable
{
    String toJSON() throws JSONException;
}