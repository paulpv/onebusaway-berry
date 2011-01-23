package org.onebusaway.berry.api;

import java.io.IOException;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;

public class URL {
    private final String url;

    public URL(String url) {
        this.url = url;
    }

    public Connection openConnection() throws IOException {
        return Connector.open(url);
    }

    public String toString() {
        return url;
    }
}
