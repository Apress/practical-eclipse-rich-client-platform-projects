/*******************************************************************************
 * Copyright (c) 2006 Vladimir Silva and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vladimir Silva - initial API and implementation
 *******************************************************************************/
package ch08.opengl.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ch08.opengl.util.SimpleHTTPClient;

/**
 * Yahoo geoSearch client
 * 
 * For example :
 * http://local.yahooapis.com/MapsService/V1/geocode?appid=YD-
 * f7BUYpg_JX25g8v.EmGtMxpfMhpX2XIz17DeSzXV&location=durham
 * 
 * Returns
 * 
 * <pre>
 * &lt;ResultSet xsi:schemaLocation=&quot;urn:yahoo:maps http://api.local.yahoo.com/MapsService/V1/GeocodeResponse.xsd&quot;&gt;
 * &lt;Result precision=&quot;city&quot;&gt;
 * 	&lt;Latitude&gt;35.949417&lt;/Latitude&gt;
 * 	&lt;Longitude&gt;-93.980476&lt;/Longitude&gt;
 * 	&lt;Address/&gt;
 * 	&lt;City&gt;DURHAM&lt;/City&gt;
 * 	&lt;State&gt;AR&lt;/State&gt;
 * 	&lt;Zip/&gt;
 * 	&lt;Country&gt;US&lt;/Country&gt;
 * &lt;/Result&gt;
 * &lt;Result precision=&quot;city&quot;&gt;
 * 	&lt;Latitude&gt;39.64608&lt;/Latitude&gt;
 * 	&lt;Longitude&gt;-121.799248&lt;/Longitude&gt;
 * 	&lt;Address/&gt;
 * 	&lt;City&gt;DURHAM&lt;/City&gt;
 * 	&lt;State&gt;CA&lt;/State&gt;
 * 	&lt;Zip/&gt;
 * 	&lt;Country&gt;US&lt;/Country&gt;
 * &lt;/Result&gt;
 * &lt;/ResultSet&gt; *
 * </pre>
 * 
 * @author Owner
 * 
 */
public class YGeoSearch {

    private String location;

    /**
     * Yahoo search result object
     * 
     * @author Owner
     * 
     */
    public static class YResult {
        public double latitude, longitude;
        public String address, city, state, zip, country;
        public String warning;

        public String debug() {
            return "Y! lat= " + latitude + " lon=" + longitude + " city="
                    + city + " st=" + state + " zip=" + zip + " c="
                    + country;
        }

        @Override
        public String toString() {
            return (warning != null ? warning + ", " : "")
                    + (address != null ? address + ", " : "")
                    + (city != null ? city + ", " : "")
                    + (state != null ? state + ", " : "")
                    + (zip != null ? zip + ", " : "")
                    + (country != null ? country : "");
        }
    }

    /**
     * Search for a place using Yahoo
     * 
     * @param location
     */
    public YGeoSearch(String location) {
        if (location == null)
            throw new IllegalArgumentException("Invalid location");

        this.location = location.replaceAll(" ", "+");
    }

    /**
     * Get search locations
     * 
     * @return array of {@link YResult} objects
     * @throws Exception
     */
    public YResult[] getLocations() throws Exception {

        final String url = "http://local.yahooapis.com/MapsService/V1/geocode"
           + "?appid=YD-f7BUYpg_JX25g8v.EmGtMxpfMhpX2XIz17DeSzXV&location="
           + location;

        SimpleHTTPClient client = new SimpleHTTPClient(new URL(url));
        final String xml = client.doGet();

        if (client.getStatus() == HttpURLConnection.HTTP_OK) {
            client.close();
            return parseYahooXml(xml);
        }

        // handle error
        throw new IOException("HTTP request failed " + client.getStatus()
                + " " + url);
    }

    /**
     * Parse Y! results XML
     * @param xml
     * @throws Exception
     */
    private YResult[] parseYahooXml(String xml) throws Exception {
        Document doc = parse(new ByteArrayInputStream(xml.getBytes()));

        // KML Doc
        NodeList results = doc.getElementsByTagName("Result");

        YResult[] Yresults = new YResult[results.getLength()];

        for (int i = 0; i < results.getLength(); i++) {
            final Element e = (Element) results.item(i);

            YResult Yres = new YResult();

            Yres.warning = getAttributeValue(e, "warning");
            Yres.address = getNodeValue(e, "Address");
            Yres.latitude = Double
                    .parseDouble(getNodeValue(e, "Latitude"));
            Yres.longitude = Double.parseDouble(getNodeValue(e,
                    "Longitude"));
            Yres.city = getNodeValue(e, "City");
            Yres.state = getNodeValue(e, "State");
            Yres.country = getNodeValue(e, "Country");
            Yres.zip = getNodeValue(e, "Zip");

            Yresults[i] = Yres;
        }
        return Yresults;
    }

    /*
     * XML Document Utilities
     */
    public static Document parse(InputStream is) throws SAXException,
            IOException, ParserConfigurationException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory
                .newDocumentBuilder();
        return docBuilder.parse(is);
    }

    /**
     * Extract the value of a XML element
     * @param e Document element
     * @param name Element name
     * @return Value
     */
    static public String getNodeValue(Element e, String name) {
        NodeList nl = e.getElementsByTagName(name);

        try {
            return (nl != null && nl.getLength() > 0) ? nl.item(0)
                    .getFirstChild().getNodeValue().trim() : null;

        } catch (NullPointerException ex) {
            return null;
        }
    }

    static public String getTextContent(Element e, String name) {
        NodeList nl = e.getElementsByTagName(name);

        try {
            return (nl != null && nl.getLength() > 0) ? nl.item(0)
                    .getTextContent().trim() : null;

        } catch (NullPointerException ex) {
            return null;
        }
    }

    /**
     * Get an attribute value
     * @return Attribute value or null
     */
    static public String getAttributeValue(Element e, String name) {
        Node n = e.getAttributes().getNamedItem(name);
        return (n != null) ? n.getNodeValue() : null;

    }

}
