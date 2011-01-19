package org.onebusaway.berry.logging;

import java.io.PrintStream;
import java.util.Hashtable;

import net.rim.device.api.system.EventLogger;

import org.onebusaway.berry.util.ObaString;

public class ObaLogger
{
    public static final int      TRACE       = EventLogger.ALWAYS_LOG;
    public static final int      FATAL       = EventLogger.SEVERE_ERROR;
    public static final int      ERROR       = EventLogger.ERROR;
    public static final int      WARN        = EventLogger.WARNING;
    public static final int      INFO        = EventLogger.INFORMATION;
    public static final int      DEBUG       = EventLogger.DEBUG_INFO;

    public static final String[] LEVEL_NAMES = new String[]
                                             {
        "TRACE", "FATAL", "ERROR", "WARN", "INFO", "DEBUG",
                                             };

    // GUID is "WAVE Thin Client" as a long
    //private long                GUID      = 0xd4b6b5eeea339daL;

    private final String        name;
    private final PrintStream   out;
    private int                 level;
    private final boolean       printStackTrace;

    private static Hashtable    threadIds = new Hashtable();

    //private final AppMain app;

    public ObaLogger(String name, PrintStream out, int level, boolean printStackTrace)
    {
        //app = AppMain.get();

        this.name = name;
        this.out = out;
        this.level = level;
        this.printStackTrace = printStackTrace;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public boolean isLevelEnabled(int level)
    {
        return this.level >= level;
    }

    public void log(int level, String msg)
    {
        log(level, msg, null);
    }

    public void log(int level, Throwable e)
    {
        log(level, null, e);
    }

    public void log(int level, String msg, Throwable e)
    {
        if (!isLevelEnabled(level))// || !app.getIsGlobalLoggingEnabled())
        {
            return;
        }

        if (msg == null)
        {
            msg = (e != null) ? e.toString() : "";
        }

        msg = formatMessage(level, name, msg, e);

        // Print to System.out immediately
        if (out != null)
        {
            out.println(msg);
            if (printStackTrace)
            {
                e.printStackTrace();
            }
        }
    }

    private synchronized static String getCurrentThreadId()
    {
        String currentThreadName = Thread.currentThread().getName();
        //System.out.println("currentThreadName=" + currentThreadName);
        String currentThreadId = (String) threadIds.get(currentThreadName);
        if (currentThreadId == null)
        {
            currentThreadId = "T" + threadIds.size();
            threadIds.put(currentThreadName, currentThreadId);
        }
        //System.out.println("currentThreadId=" + currentThreadId);
        return currentThreadId;
    }

    public static String formatMessage(int level, String name, String msg)
    {
        return formatMessage(level, name, msg, null, false);
    }

    public static String formatMessage(int level, String name, String msg, Throwable e)
    {
        return formatMessage(level, name, msg, e, false);
    }

    public static String formatMessage(int level, String name, String msg, Throwable e, boolean printStackTrace)
    {
        // For brevity reasons, we only need to output seconds and milliseconds
        long milliseconds = System.currentTimeMillis() % (60 * 1000);

        msg =
            ObaString.formatNumber(milliseconds / 1000, 2) + "." + ObaString.formatNumber(milliseconds % 1000, 3) + " "
                            + LEVEL_NAMES[level] + " " + getCurrentThreadId() + " " + name + " " + msg;
        if (e != null)
        {
            msg += ": " + e.toString();
        }

        return msg;
    }

    public void debug(String msg, Throwable e)
    {
        log(DEBUG, msg, e);
    }

    public void debug(String msg)
    {
        log(DEBUG, msg);
    }

    public void debug(Throwable e)
    {
        log(DEBUG, e);
    }

    public void error(String msg, Throwable e)
    {
        log(ERROR, msg, e);
    }

    public void error(String msg)
    {
        log(ERROR, msg);
    }

    public void error(Throwable e)
    {
        log(ERROR, e);
    }

    public void fatal(String msg, Throwable e)
    {
        log(FATAL, msg, e);
    }

    public void fatal(String msg)
    {
        log(FATAL, msg);
    }

    public void fatal(Throwable e)
    {
        log(FATAL, e);
    }

    public void info(String msg, Throwable e)
    {
        log(INFO, msg, e);
    }

    public void info(String msg)
    {
        log(INFO, msg);
    }

    public void info(Throwable e)
    {
        log(INFO, e);
    }

    public void trace(String msg, Throwable e)
    {
        log(TRACE, msg, e);
    }

    public void trace(String msg)
    {
        log(TRACE, msg);
    }

    public void trace(Throwable e)
    {
        log(TRACE, e);
    }

    public void warn(String msg, Throwable e)
    {
        log(WARN, msg, e);
    }

    public void warn(String msg)
    {
        log(WARN, msg);
    }

    public void warn(Throwable e)
    {
        log(WARN, e);
    }
}
