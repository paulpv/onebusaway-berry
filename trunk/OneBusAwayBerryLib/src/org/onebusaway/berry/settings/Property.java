package org.onebusaway.berry.settings;

public abstract class Property
{
    private final String propName;

    protected Property(ObaSettings settings, String propName)
    {
        this.propName = propName;
        settings.registerProperty(propName, this);
    }

    public final String getName()
    {
        return this.propName;
    }

    public abstract boolean isDefaultValue();

    public abstract boolean valuesEqual(Property property);

    public abstract int getValueHashCode();

    public abstract String getStringValue();

    public abstract void setStringValue(String text);

    public abstract void copyValueFrom(Property property);
}
