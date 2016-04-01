import jdk.internal.org.xml.sax.XMLReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class CurrencyDataGetter  {

    private final Map<String, BigDecimal> conversionRates = new HashMap<String, BigDecimal>();

    private void fetchWebData() throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        Element documentElement = document.getDocumentElement();
        NodeList sList = documentElement.getElementsByTagName("Cube");
        for (int i = 0; i < sList.getLength(); i++) {

            Node currencyNode = sList.item(i).getAttributes().getNamedItem("currency");
            Node rateNode = sList.item(i).getAttributes().getNamedItem("rate");

            if (rateNode != null && currencyNode != null) {
                conversionRates.put(currencyNode.getTextContent(),
                                    new BigDecimal(rateNode.getTextContent()));
            }
        }
    }

    private void fetchResourceData() {

    }

    public Map<String, BigDecimal> getConversionRates () {

    }

}
