/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utilities;

import hr.algebra.model.PlayerSetting;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author dnlbe
 */
public class DOMUtils {

    private static final String FILENAME_PLAYERSETTING = "playerSetting.xml";

    public static void savePlayerSettings(PlayerSetting playerSetting) {

        try {
            Document document = DOMUtils.createDocument("playerSetting");
            document.getDocumentElement().appendChild(createPlayerSettingElement(playerSetting, document));
            saveDocument(document, FILENAME_PLAYERSETTING);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static Element createPlayerSettingElement(PlayerSetting p, Document document) throws DOMException {
        Element car = document.createElement("playerSetting");
        car.appendChild(createElement(document, "player1Color", p.player1Color));
        car.appendChild(createElement(document, "player2Color", p.player2Color));
        return car;
    }

    private static Document createDocument(String element) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        DocumentType documentType = domImplementation.createDocumentType("DOCTYPE", null, "employees.dtd");
        return domImplementation.createDocument(null, element, documentType);
    }

    private static Attr createAttribute(Document document, String name, String value) {
        Attr attr = document.createAttribute(name);
        attr.setValue(value);
        return attr;
    }

    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    private static void saveDocument(Document document, String fileName) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        //transformer.transform(new DOMSource(document), new StreamResult(System.out));
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
    }
     
    private static PlayerSetting processPlayerSettingNode(Element playerSetting) {
        return new PlayerSetting(
                playerSetting.getElementsByTagName("player1Color").item(0).getTextContent(),
                playerSetting.getElementsByTagName("player2Color").item(0).getTextContent());
    }

    public static PlayerSetting loadPlayerSettings() {
        PlayerSetting playerSetting = new PlayerSetting("", "");
        try {
            Document document = createDocument(new File(FILENAME_PLAYERSETTING));
            NodeList nodes = document.getElementsByTagName("playerSetting");
            for (int i = 0; i < nodes.getLength(); i++) {
                // dangerous class cast exception
                playerSetting = processPlayerSettingNode((Element) nodes.item(i));
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return playerSetting;
    }

    private static Document createDocument(File file) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        return document;
    }
}
