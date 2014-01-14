package thread;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

/**
 * Created by wades on 1/14/14.
 */
public class XMLGetter extends Thread{

    private final static int QUEUE_LENGTH = Integer.MAX_VALUE/100000;
    private final static String XML_IMPORT_NAME = "import";
    private final static String XML_IMPORT_URL_NAME = "schemaLocation";
    private final static Logger LOGGER = Logger.getLogger("wbotlogger");
    private BlockingQueue<URL> queue;
    private int xmlFileCounter;


    public XMLGetter(String url) {
        LOGGER.info(String.format("Queue size: %s", QUEUE_LENGTH));
        xmlFileCounter = 0;
        queue = new ArrayBlockingQueue<URL>(QUEUE_LENGTH);
        try {
            queue.put(new URL(url));
        } catch (Exception e){
            LOGGER.warning(String.format("Failed to create URL from: %s", url));
        }
    }

    @Override
    public void run() {
        super.run();
        while (! queue.isEmpty()){
            try {
                lookForImports(parse(queue.take()));
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.warning(String.format("Exception!"));
            }
            xmlFileCounter++;
        }
        LOGGER.warning(String.format("Queue is empty, i'am done! Total xml files: %s", xmlFileCounter));
    }


    private Document parse(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        return document;
    }


    private void lookForImports(Document document) throws DocumentException, MalformedURLException, InterruptedException {
        Element root = document.getRootElement();
        for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            if (element.getName().equals(XML_IMPORT_NAME)){
                LOGGER.info(String.format("Name: %s, url: %s", element.getName(), element.attribute(XML_IMPORT_URL_NAME).getValue()));
                queue.put(new URL(element.attribute(XML_IMPORT_URL_NAME).getValue()));
            }
        }
    }
}
