package org.folio.cataloging.log;

import org.apache.log4j.Logger;

public class Log {
    private final Logger logger;

    public Log(final Class clazz) {
        logger = Logger.getLogger(clazz);
    }

    public void info(final String message, final Object ... values) {
        logger.info(values == null ? message : String.format(message, values));
    }

    public void debug(final String message, final Object ... values) {
        logger.debug(values == null ? message : String.format(message, values));
    }
}
