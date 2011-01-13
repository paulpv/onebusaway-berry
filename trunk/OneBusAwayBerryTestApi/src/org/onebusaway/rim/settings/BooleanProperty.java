package org.onebusaway.rim.settings;

public final class BooleanProperty extends Property
{
    private final boolean defaultValue;
    private boolean       value;

    public BooleanProperty(ObaSettings settings, String propName)
    {
        this(settings, propName, false);
    }

    public BooleanProperty(ObaSettings settings, String propName, boolean defaultValue)
    {
        super(settings, propName);
        this.defaultValue = defaultValue;
        setValue(defaultValue);
    }

    public boolean getDefaultValue()
    {
        return defaultValue;
    }

    public boolean isDefaultValue()
    {
        return value == defaultValue;
    }

    public boolean getValue()
    {
        return value;
    }

    public void setValue(boolean value)
    {
        this.value = value;
    }

    public boolean valuesEqual(Property property)
    {
        if (property == null || property.getClass() != BooleanProperty.class)
        {
            return false;
        }
        BooleanProperty prop = (BooleanProperty) property;
        return value == prop.value;
    }

    public void copyValueFrom(Property property)
    {
        // can assume property is of the right type
        BooleanProperty prop = (BooleanProperty) property;
        setValue(prop.getValue());
    }

    public void setStringValue(String text)
    {
        setValue("Y".equals(text));
    }

    public String getStringValue()
    {
        return value ? "Y" : "N";
    }

    public int getValueHashCode()
    {
        return value ? 1 : 0;
    }
}
