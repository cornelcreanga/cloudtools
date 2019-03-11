package com.com.ccreanga.cloudutils.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Util {

    public static void configureLog(Logger log ){

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                String message = record.getMessage();
                if (message.endsWith("\\"))
                    return message.substring(0,message.length()-1);
                return record.getMessage() + "\n";
            }
        });
        consoleHandler.setLevel(Level.INFO);
        log.setUseParentHandlers(false);
        log.addHandler(consoleHandler);
        log.setLevel(Level.INFO);
    }

}
