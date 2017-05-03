package ru.atom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class WorkWithProperties {
    private static final Logger log = LogManager.getLogger(Ticker.class);
    private static Properties properties = new Properties();

    static {
        try {
            properties.load(WorkWithProperties
                    .class
                    .getClassLoader()
                    .getResourceAsStream("settings.properties")
            );
        } catch (IOException e) {
            log.error("Error when load properties: " + e.getMessage());
        }
    }

    public static Properties getProperties() {
        return properties;
    }

}