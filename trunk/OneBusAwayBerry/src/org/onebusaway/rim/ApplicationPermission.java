package org.onebusaway.rim;

import java.util.Hashtable;

class ApplicationPermission
{
    int     id;
    boolean required;
    String  reason;

    public ApplicationPermission(int id, boolean required, String reason)
    {
        this.id = id;
        this.required = required;
        this.reason = reason;
    }

    public int getId()
    {
        return id;
    }

    public boolean getRequired()
    {
        return required;
    }

    public String getReason()
    {
        return reason;
    }
}

class ApplicationPermissionsTable extends Hashtable
{
    public ApplicationPermission put(int key, ApplicationPermission value)
    {
        return (ApplicationPermission) put(new Integer(key), value);
    }

    public ApplicationPermission put(ApplicationPermission wmcApplicationPermission)
    {
        return put(wmcApplicationPermission.getId(), wmcApplicationPermission);
    }

    public Object put(Object key, Object value)
    {
        if (!(key instanceof Integer))
        {
            throw new IllegalArgumentException("key must be of type Integer");
        }
        if (!(value instanceof ApplicationPermission))
        {
            throw new IllegalArgumentException("key must be of type ApplicationPermission");
        }
        return super.put(key, value);
    }

    public ApplicationPermission get(int key)
    {
        return (ApplicationPermission) get(new Integer(key));
    }

    public Object get(Object key)
    {
        if (!(key instanceof Integer))
        {
            throw new IllegalArgumentException("key must be of type Integer");
        }
        return super.get(key);
    }
}
