package main;

import log.WBotFormatter;
import thread.XMLGetter;

import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by wades on 1/14/14.
 */
public class runXMLGetter {

    private final static String ARG_INFO = "-i";
    private final static String ARG_DOWNLOAD = "-d";
    private static boolean download;
    private static URL urlToFirstXml;

    private final static Logger LOGGER = Logger.getLogger("wbotlogger");

    public static void main(String[] args) {
        validateArgs(args);
        initLogger();
        LOGGER.info("Starting getter...");
        new XMLGetter(urlToFirstXml, download).start();
    }

    private static void validateArgs(String[] args) {
        download = false;
        if (args.length > 0){
            if (args[0].equals(ARG_INFO)){
                showInfo();
                System.exit(0);
            }
            try {
                urlToFirstXml = new URL(args[0]);
            } catch (Exception e){
                System.out.println(String.format("Invalid url: %s", args[1]));
                System.exit(0);
            }
            if (args.length > 1 && args[1].equals(ARG_DOWNLOAD)){
                download = true;
            }
        } else {
            System.out.println("Missing argument, try -i");
            System.exit(0);
        }
    }

    private static void showInfo() {
        System.out.println("WXMLGetter.jar [url/-i] [-d]");
        System.out.println("[-i] Info on how to use WXMLGetter.");
        System.out.println("[url] Url to the first xml file.");
        System.out.println("[-d] Flag, if set will download the files.");
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
