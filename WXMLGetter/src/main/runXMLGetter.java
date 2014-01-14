package main;

import log.WBotFormatter;
import thread.XMLGetter;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by wades on 1/14/14.
 */
public class runXMLGetter {

    private final static Logger LOGGER = Logger.getLogger("wbotlogger");

    public static void main(String[] args) {
        initLogger();
        LOGGER.info("Starting getter...");
        new XMLGetter("http://docs.oasis-open.org/wsn/b-2.xsd").start();
    }

    private static void initLogger() {
        Logger l = Logger.getLogger("wbotlogger");
        l.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new WBotFormatter());
        l.addHandler(consoleHandler);
    }
}
