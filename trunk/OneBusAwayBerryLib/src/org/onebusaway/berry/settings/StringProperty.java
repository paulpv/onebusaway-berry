package org.onebusaway.berry.settings;

public final class StringProperty extends Property
{
    private final String defaultValue;
    private String       value;

    public StringProperty(ObaSettings settings, String propertyName)
    {
        this(settings, propertyName, "");
    }

    public StringProperty(ObaSettings settings, String propertyName, String defaultValue)
    {
        super(settings, propertyName);
        this.defaultValue = getNonNullValue(defaultValue);
        setValue(defaultValue);
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public boolean isDefaultValue()
    {
        return defaultValue.equals(value);
    }

    public String getValue()
    {
        return value;
    }

    private static String getNonNullValue(String value)
    {
        return value == null ? "" : value;
    }

    public void setValue(String value)
    {
        this.value = getNonNullValue(value);
    }

    public boolean valuesEqual(Property property)
    {
        if (property == null || property.getClass() != StringProperty.class)
        {
            return false;
        }
        StringProperty prop = (StringProperty) property;
        return value.equals(prop.value);
    }

    public void copyValueFrom(Property property)
    {
        // can assume property is of the right type
        StringProperty prop = (StringProperty) property;
        setValue(prop.getValue());
    }

    public void setStringValue(String text)
    {
        setValue(text);
    }

    public String getStringValue()
    {
        return value;
    }

    public int getValueHashCode()
    {
        return value.hashCode();
    }
}
