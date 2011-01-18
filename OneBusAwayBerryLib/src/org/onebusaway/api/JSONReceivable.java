package org.onebusaway.api;

import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public interface JSONReceivable
{
    /**
     * Think of this as a public constructor; if this fails then the instance should be discarded.
     * It should probably be renamed to something like "constructor_CallerShouldDiscardObjectIfExceptionThrown".
     * I am not adding a JSONObject parameter to class constructors, because I need to be able to call 
     * class.newInstance() which requires a public default (no parameters) constructor.    
     * The only class that calls this should be ObaRequest and/or ObaResponse.
     * Thus both of these callers should catch any thrown Exception and discard the object. 
     */
    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException;
}
