package org.onebusaway.berry.api;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import net.rim.device.api.i18n.SimpleDateFormat;

/**
 * Some ideas came from the the following codes:
 *  org.apache.catalina.util.Strftime.java
 *  android.text.format.Time.java
 * 
 * @author pv@swooby.com
 *
 */
public class Time extends Date {

    Calendar calendar = Calendar.getInstance();

    public Time() {
        super();
    }

    /**
     * Year [1970-...] 
     * Month [0-11]
     * Day of month [1-31]
     */
    public Time(int year, int month, int dayOfMonth) {
        super();
        setYear(year, false);
        setMonth(month, false);
        setDayOfMonth(dayOfMonth, true);
    }

    /**
     * Year [1970-...] 
     * Updates/computes the resulting internal value
     */
    public void setYear(int year) {
        setYear(year, true);
    }

    /**
     * Year [1970-...] 
     * Optionally updates/computes the resulting internal value
     */
    public void setYear(int year, boolean update) {
        calendar.set(Calendar.YEAR, year);
        if (update) {
            setTime(calendar.getTime().getTime());
        }
    }

    /**
     * Month [0-11]
     * Updates/computes the resulting internal value
     */
    public void setMonth(int month) {
        setMonth(month, true);
    }

    /**
     * Month [0-11]
     * Optionally updates/computes the resulting internal value
     */
    public void setMonth(int month, boolean update) {
        calendar.set(Calendar.MONTH, month);
        if (update) {
            setTime(calendar.getTime().getTime());
        }
    }

    /**
     * Day of month [1-31]
     * Updates/computes the resulting internal value
     */
    public void setDayOfMonth(int dayOfMonth) {
        setDayOfMonth(dayOfMonth, true);
    }

    /**
     * Day of month [1-31]
     * Optionally updates/computes the resulting internal value
     */
    public void setDayOfMonth(int dayOfMonth, boolean update) {
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (update) {
            setTime(calendar.getTime().getTime());
        }
    }

    public String format(String origFormat) {
        return new SimpleDateFormat(convertDateFormat(origFormat)).format(this);
    }

    protected static final Hashtable translate;

    /**
     * Initialize our pattern translation
     */
    static {
        translate = new Hashtable();
        translate.put("a", "EEE");
        translate.put("A", "EEEE");
        translate.put("b", "MMM");
        translate.put("B", "MMMM");
        translate.put("c", "EEE MMM d HH:mm:ss yyyy");

        //There's no way to specify the century in SimpleDateFormat.  We don't want to hard-code
        //20 since this could be wrong for the pre-2000 files.
        //translate.put("C", "20");
        translate.put("d", "dd");
        translate.put("D", "MM/dd/yy");
        translate.put("e", "dd"); //will show as '03' instead of ' 3'
        translate.put("F", "yyyy-MM-dd");
        translate.put("g", "yy");
        translate.put("G", "yyyy");
        translate.put("H", "HH");
        translate.put("h", "MMM");
        translate.put("I", "hh");
        translate.put("j", "DDD");
        translate.put("k", "HH"); //will show as '07' instead of ' 7'
        translate.put("l", "hh"); //will show as '07' instead of ' 7'
        translate.put("m", "MM");
        translate.put("M", "mm");
        translate.put("n", "\n");
        translate.put("p", "a");
        translate.put("P", "a"); //will show as pm instead of PM
        translate.put("r", "hh:mm:ss a");
        translate.put("R", "HH:mm");
        //There's no way to specify this with SimpleDateFormat
        //translate.put("s","seconds since ecpoch");
        translate.put("S", "ss");
        translate.put("t", "\t");
        translate.put("T", "HH:mm:ss");
        //There's no way to specify this with SimpleDateFormat
        //translate.put("u","day of week ( 1-7 )");

        //There's no way to specify this with SimpleDateFormat
        //translate.put("U","week in year with first sunday as first day...");

        translate.put("V", "ww"); //I'm not sure this is always exactly the same

        //There's no way to specify this with SimpleDateFormat
        //translate.put("W","week in year with first monday as first day...");

        //There's no way to specify this with SimpleDateFormat
        //translate.put("w","E");
        translate.put("X", "HH:mm:ss");
        translate.put("x", "MM/dd/yy");
        translate.put("y", "yy");
        translate.put("Y", "yyyy");
        translate.put("Z", "z");
        translate.put("z", "Z");
        translate.put("%", "%");
    }

    /**
     * Search the provided pattern and get the C standard
     * Date/Time formatting rules and convert them to the
     * Java equivalent.
     *
     * @param pattern The pattern to search
     * @return The modified pattern
     */
    protected String convertDateFormat(String pattern) {
        boolean inside = false;
        boolean mark = false;
        boolean modifiedCommand = false;

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);

            if (c == '%' && !mark) {
                mark = true;
            }
            else {
                if (mark) {
                    if (modifiedCommand) {
                        //don't do anything--we just wanted to skip a char
                        modifiedCommand = false;
                        mark = false;
                    }
                    else {
                        inside = translateCommand(buf, pattern, i, inside);
                        //It's a modifier code
                        if (c == 'O' || c == 'E') {
                            modifiedCommand = true;
                        }
                        else {
                            mark = false;
                        }
                    }
                }
                else {
                    if (!inside && c != ' ') {
                        //We start a literal, which we need to quote
                        buf.append("'");
                        inside = true;
                    }

                    buf.append(c);
                }
            }
        }

        if (buf.length() > 0) {
            char lastChar = buf.charAt(buf.length() - 1);

            if (lastChar != '\'' && inside) {
                buf.append('\'');
            }
        }
        return buf.toString();
    }

    protected String quote(String str, boolean insideQuotes) {
        String retVal = str;
        if (!insideQuotes) {
            retVal = '\'' + retVal + '\'';
        }
        return retVal;
    }

    /**
     * try to get the Java Date/Time formating associated with
     * the C standard provided
     *
     * @param c The C equivalent to translate
     * @return The Java formatting rule to use
     */
    protected boolean translateCommand(StringBuffer buf, String pattern, int index, boolean oldInside) {
        char firstChar = pattern.charAt(index);
        boolean newInside = oldInside;

        //O and E are modifiers, they mean to present an alternative representation of the next char
        //we just handle the next char as if the O or E wasn't there
        if (firstChar == 'O' || firstChar == 'E') {
            if (index + 1 < pattern.length()) {
                newInside = translateCommand(buf, pattern, index + 1, oldInside);
            }
            else {
                buf.append(quote("%" + firstChar, oldInside));
            }
        }
        else {
            String command = (String) translate.get(String.valueOf(firstChar));

            //If we don't find a format, treat it as a literal--That's what apache does
            if (command == null) {
                buf.append(quote("%" + firstChar, oldInside));
            }
            else {
                //If we were inside quotes, close the quotes
                if (oldInside) {
                    buf.append('\'');
                }
                buf.append(command);
                newInside = false;
            }
        }
        return newInside;
    }
}
