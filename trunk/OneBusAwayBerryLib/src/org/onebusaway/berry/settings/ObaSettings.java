package org.onebusaway.berry.settings;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class ObaSettings {
    private static ObaSettings obaSettings = null;

    public static ObaSettings getSettings() {
        if (obaSettings == null) {
            obaSettings = new ObaSettings();
            obaSettings.load();
        }
        return obaSettings;
    }

    private static final String   RECORD_STORE_NAME = "OneBusAway_Settings_v1";

    private final Hashtable       propMap           = new Hashtable();
    private final Vector          propList          = new Vector();
    private static RecordStore    recordStore;

    // to add new property:
    // 1) add variable of appropriate Property subclass
    // 2) add settings getter/setter
    private final BooleanProperty permissionsOk     = new BooleanProperty(this, "permissionsOk", false);
    private final StringProperty  apiServerName     = new StringProperty(this, "apiServerName", "api.onebusaway.org");

    //
    // BEGIN: settings getters/setters
    //

    public boolean getPermissionsOk() {
        return permissionsOk.getValue();
    }

    public void setPermissionsOk(boolean permissionsOk) {
        this.permissionsOk.setValue(permissionsOk);
    }

    public String getApiServerName() {
        return apiServerName.getValue();
    }

    public void setApiServerName(String apiServerName) {
        this.apiServerName.setValue(apiServerName);
    }

    //
    // END: settings getters/setters
    //

    public void clearSettings() {
        try {
            RecordStore.deleteRecordStore(RECORD_STORE_NAME);
        }
        catch (RecordStoreException rse) {
        }
    }

    public void load() {
        int numRecords;

        // statically synchronized
        synchronized (RecordStore.class) {
            try {
                recordStore = RecordStore.openRecordStore(RECORD_STORE_NAME, true);
                numRecords = recordStore.getNumRecords();
            }
            catch (RecordStoreException rse) {
                // shouldn't happen
                return;
            }
        }

        if (numRecords > 0) {
            String data = "";

            try {
                int recSize = recordStore.getRecordSize(1);
                if (recSize > 0) {
                    byte[] recData = new byte[recSize];
                    recData = recordStore.getRecord(1);
                    if (recData != null) {
                        data = new String(recData, 0, recData.length);
                    }
                }
            }
            catch (Exception e)//RecordStoreException rse)
            {
                // ignore
                data = "";
            }

            while (data.length() > 0) {
                // get property name
                int nextIndex = data.indexOf('\n');
                String kvp;

                if (nextIndex >= 0) {
                    kvp = data.substring(0, nextIndex);
                    data = data.substring(nextIndex + 1);
                }
                else {
                    kvp = data;
                    data = "";
                }

                // key won't contain =, so it doesn't matter if value contains embedded =
                // it will matter if value contains embedded newlines, but that should be impossible
                int equalsIndex = kvp.indexOf('=');
                String key;
                String value;
                if (equalsIndex >= 0) {
                    key = kvp.substring(0, equalsIndex);
                    value = kvp.substring(equalsIndex + 1);
                }
                else {
                    // shouldn't happen
                    key = kvp;
                    value = "";
                }

                Property prop = (Property) propMap.get(key);
                if (prop != null) {
                    prop.setStringValue(value);
                }
            }
        }

        try {
            recordStore.closeRecordStore();
        }
        catch (RecordStoreNotOpenException rsnoe) {
        }
        catch (RecordStoreException rse) {
        }
    }

    // Add a new record to the record store
    public synchronized void saveRecord() {
        // statically synchronized
        synchronized (RecordStore.class) {
            try {
                recordStore = RecordStore.openRecordStore(RECORD_STORE_NAME, true);
            }
            catch (RecordStoreException rse) {
            }
        }

        // store the data
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < propList.size(); ++i) {
            Property prop = (Property) propList.elementAt(i);
            if (!prop.isDefaultValue()) {
                sb.append(prop.getName());
                sb.append('=');
                sb.append(prop.getStringValue());
                sb.append('\n');
            }
        }

        String record = sb.toString();
        byte[] b = record.getBytes();

        try {
            // statically synchronized
            synchronized (RecordStore.class) {
                if (recordStore.getNumRecords() > 0) {
                    recordStore.setRecord(1, b, 0, b.length);
                }
                else {
                    recordStore.addRecord(b, 0, b.length);
                }
            }
        }
        catch (RecordStoreException rse) {
        }

        try {
            recordStore.closeRecordStore();
        }
        catch (RecordStoreNotOpenException rsnoe) {
        }
        catch (RecordStoreException rse) {
        }
    }

    public int hashCode() {
        int result = 0;
        for (int i = 0; i < propList.size(); ++i) {
            Property prop = (Property) propList.elementAt(i);
            result = 31 * result + prop.getValueHashCode();
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        ObaSettings other = (ObaSettings) obj;

        for (int i = 0; i < propList.size(); ++i) {
            Property myProp = (Property) propList.elementAt(i);
            Property otherProp = (Property) other.propMap.get(myProp.getName());
            if (!myProp.valuesEqual(otherProp)) {
                return false;
            }
        }
        return true;
    }

    public ObaSettings copy() {
        ObaSettings settings = new ObaSettings();

        Property thisProp;
        String propName;
        Property otherProp;

        for (int i = 0; i < propList.size(); ++i) {
            thisProp = (Property) propList.elementAt(i);
            propName = thisProp.getName();
            otherProp = (Property) settings.propMap.get(propName);
            otherProp.copyValueFrom(thisProp);
        }

        return settings;
    }

    protected void registerProperty(String propName, Property property) {
        Object oldValue = propMap.put(propName, property);
        if (oldValue != null) {
            throw new IllegalArgumentException("Duplicate property: " + propName);
        }
        propList.addElement(property);
    }
}
