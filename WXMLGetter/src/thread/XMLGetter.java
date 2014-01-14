package thread;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import shell.ExecuteShellCommand;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private List<URL> urlToXmlFiles;
    private boolean dwn;


    public XMLGetter(URL url, boolean dwn) {
        this.dwn = dwn;
        urlToXmlFiles = new ArrayList<URL>();
        LOGGER.info(String.format("Queue size: %s", QUEUE_LENGTH));
        queue = new ArrayBlockingQueue<URL>(QUEUE_LENGTH);
        try {
            queue.put(url);
        } catch (Exception e){
            LOGGER.warning(String.format("Failed to add URL to queue: %s", url));
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
        }
        LOGGER.info(String.format("Queue is empty, done collecting urls! Total: %s", urlToXmlFiles.size()));
        if (dwn){
            getAllXmlFiles();
        }
    }

    private void getAllXmlFiles() {
        LOGGER.info(String.format("Will start downloading files..."));
        for (URL url : urlToXmlFiles){
            StringBuilder sb = new StringBuilder();
            sb.append("wget ").append(url.toString());
            ExecuteShellCommand ex = new ExecuteShellCommand(sb.toString());
            LOGGER.info(String.format("Download done! %s", ex.getResult()));
        }
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
                URL u = new URL(element.attribute(XML_IMPORT_URL_NAME).getValue());
                queue.put(u);
                urlToXmlFiles.add(u);
            }
        }
    }
}
