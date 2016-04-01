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

public class CurrencyDataGetter  {

    private final Map<String, BigDecimal> conversionRates = new HashMap<String, BigDecimal>();

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

    private void fetchData() {
        try {
            fetchWebData();
        } catch (Exception e) {
            try {
                fetchResourceData();
            } catch (IOException e1) {
                throw new RuntimeException("Could not read currency data!");
            }
        }
    }

    public Map<String, BigDecimal> getConversionRates() {
        fetchData();
        return conversionRates;
    }
}
