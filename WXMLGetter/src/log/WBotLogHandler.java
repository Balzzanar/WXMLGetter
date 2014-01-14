package log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class WBotLogHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        System.out.println("TJOOOOSAN");
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }

    public WBotLogHandler() {
        super();
        setFormatter(new WBotFormatter());
    }

}