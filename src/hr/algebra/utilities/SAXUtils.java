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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author dnlbe
 */
public class SAXUtils {

    private static final String FILENAME_CARS = "playerSetting.xml";

    public static PlayerSetting loadPlayerSettings() {
        PlayerSetting playerSetting = new PlayerSetting("", "");
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(FILENAME_CARS), new PlayerSettingsHandler(playerSetting));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(SAXUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return playerSetting;
    }

    private static class PlayerSettingsHandler extends DefaultHandler {

        private final PlayerSetting playerSetting;

        private PlayerSettingsHandler(PlayerSetting playerSetting) {
            this.playerSetting = playerSetting;
        }

        private Optional<PlayerSettingTag> tag;
        private PlayerSetting car;

        @Override
        public void startDocument() throws SAXException {
            tag = Optional.empty();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            tag = PlayerSettingTag.from(qName);
            if (tag.isPresent()) {
                switch (tag.get()) {
                    case PLAYERSETTING:
                        //playerSetting = new PlayerSetting();
                        break;
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String value = new String(ch, start, length);
            if (tag.isPresent()) {
                switch (tag.get()) {
                    case PLAYER1COLOR:
                        // we do not check whether is null - it should brake if xml not valid!
                        playerSetting.player1Color = value;
                        break;
                    case PLAYER2COLOR:
                        // we do not check whether is null - it should brake if xml not valid!
                        playerSetting.player2Color = value;
                        break;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            tag = Optional.empty();
        }
    }

    private enum PlayerSettingTag {

        PLAYERSETTING("playerSetting"),
        PLAYER1COLOR("player1Color"),
        PLAYER2COLOR("player2Color");

        private final String name;

        private PlayerSettingTag(String name) {
            this.name = name;
        }

        private static Optional<PlayerSettingTag> from(String name) {
            for (PlayerSettingTag value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }

}
