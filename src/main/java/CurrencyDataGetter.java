import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class CurrencyDataGetter {

    /**
     * Data structure that contains a currency and its corresponding rate to euro.
     */
    private final Map<String, BigDecimal> conversionRates = new HashMap<String, BigDecimal>();

    /**
     * Gets the current currency rates for EU Central Bank site.
     * Reads a document in XML file and then parses the necessary data into a map.
     *
     * @throws Exception if something unexpected happens. There are so many things that could blow up.
     */
    private void fetchWebData() throws Exception {
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

    /**
     * Gets conversion rates from a resource file (resources/currencyRates.txt).
     * Store read data in map similar to {@link #fetchWebData()}.
     *
     * @return true if data reading was successful, false otherwise.
     * @throws IOException
     */
    private boolean fetchResourceData() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("currencyRates.txt").getFile());
        if (!file.exists()) {
            return false;
        }

        FileInputStream is = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(" ");
            conversionRates.put(parts[0], new BigDecimal(parts[1]));
        }
        is.close();
        br.close();
        return true;
    }

    /**
     *
     * @return
     */
    public Map<String, BigDecimal> getWebCurrencyData() {
        try {
            fetchWebData();
        } catch (Exception e) {
            throw new RuntimeException("Something broke whilist reading data from web");
        }
        return conversionRates;
    }

    public Map<String, BigDecimal> getFileCurrencyData() {
        try {
            fetchResourceData();
        } catch (Exception e) {
            throw new RuntimeException("Something broke whilist reading data from resource file.");
        }
        return conversionRates;
    }

}
