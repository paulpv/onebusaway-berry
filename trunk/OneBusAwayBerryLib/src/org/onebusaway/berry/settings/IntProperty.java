package org.onebusaway.berry.settings;

public final class IntProperty extends Property
{
    private final int defaultValue;
    private int       value;

    public IntProperty(ObaSettings settings, String propName)
    {
        this(settings, propName, 0);
    }

    public IntProperty(ObaSettings settings, String propName, int defaultValue)
    {
        super(settings, propName);
        this.defaultValue = defaultValue;
        setValue(defaultValue);
    }

    public int getDefaultValue()
    {
        return defaultValue;
    }

    public boolean isDefaultValue()
    {
        return value == defaultValue;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public boolean valuesEqual(Property property)
    {
        if (property == null || property.getClass() != IntProperty.class)
        {
            return false;
        }
        IntProperty prop = (IntProperty) property;
        return value == prop.value;
    }

    public void copyValueFrom(Property property)
    {
        // can assume property is of the right type
        IntProperty prop = (IntProperty) property;
        setValue(prop.getValue());
    }

    public String getStringValue()
    {
        return Integer.toString(value);
    }

    public int getValueHashCode()
    {
        return value;
    }

    public void setStringValue(String text)
    {
        setValue(Integer.parseInt(text));
    }
}
