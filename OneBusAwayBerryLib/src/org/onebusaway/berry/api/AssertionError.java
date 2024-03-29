package org.onebusaway.berry.api;

/**
 * Functional mock of Java's/Android's AssertionError class.
 * Used only in class Uri which was copied from Android code base.
 * 
 * @author pv
 *
 */
public class AssertionError extends Error {
    public AssertionError() {
        super();
    }

    public AssertionError(Exception e) {
        super(e.toString());
    }
}
