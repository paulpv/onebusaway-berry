package org.onebusaway.berry.logging;

import java.io.PrintStream;

import org.onebusaway.berry.util.ObaString;

public class ObaLoggerFactory {
    private PrintStream out;
    private int         defaultLevel;
    private boolean     printStackTrace;

    public ObaLoggerFactory(PrintStream out, int defaultLevel, boolean printStackTrace) {
        this.out = out;
        this.defaultLevel = defaultLevel;
        this.printStackTrace = printStackTrace;
    }

    public ObaLogger createLogger(Object o) {
        return createLogger(o.getClass());
    }

    public ObaLogger createLogger(Class c) {
        return createLogger(ObaString.getShortClassName(c));
    }

    public ObaLogger createLogger(String name) {
        ObaLogger logger = new ObaLogger(name, out, defaultLevel, printStackTrace);
        return logger;
    }
}
