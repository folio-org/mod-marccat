/*
 * (c) LibriCore
 * 
 * Created on Apr 14, 2004
 */
package com.libricore.librisuite.common;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.InvalidXPathException;
import org.dom4j.io.SAXReader;

/**
 * @author Wim Crols
 * @version $Revision: 1.5 $, $Date: 2004/07/01 12:28:54 $
 * @since 1.0
 */
public class L10nString {
    private Hashtable l10nStringHashtable = new Hashtable();

    public String getDefaultL10nStringEntry() {
        L10nCode l10nCode = new L10nCode(".@_default_");
        return this.getL10nStringEntry(l10nCode);
    }

    public String getOtherL10nStringEntry() {
        L10nCode l10nCode = new L10nCode(".@_other_");
        return this.getL10nStringEntry(l10nCode);
    }

    public String getL10nStringEntry(Locale aLocale) {
        L10nCode l10nCode = new L10nCode(aLocale);
        return this.getL10nStringEntry(l10nCode);
    }

    public String getL10nStringEntry(L10nCode l10nCode) {
        String returnString = null;

        if (l10nCode == null) {
            l10nCode = new L10nCode(".@_default_");
        }

        returnString =
            (String) this.l10nStringHashtable.get(l10nCode);
        while ((returnString == null)
            && (l10nCode.strip() != null)) {
            l10nCode = l10nCode.strip();
            returnString =
                (String) this.l10nStringHashtable.get(l10nCode);
        }

        return returnString;
    }

    public void setDefaultL10nStringEntry(String l10nStringEntry) {
        L10nCode l10nCode = new L10nCode(".@_default_");
        this.setL10nStringEntry(l10nCode, l10nStringEntry);
        System.err.println(
            "default insert = "
                + l10nCode.getL10nCode()
                + "/"
                + l10nStringEntry);
    }

    public void setOtherL10nStringEntry(String l10nStringEntry) {
        L10nCode l10nCode = new L10nCode(".@_other_");
        this.setL10nStringEntry(l10nCode, l10nStringEntry);
    }

    public void setL10nStringEntry(
        L10nCode l10nCode,
        String l10nStringEntry) {
        if (l10nCode == null) {
            l10nCode = new L10nCode(".@_default_");
        }

        if (l10nStringEntry != null) {
            this.l10nStringHashtable.put(l10nCode, l10nStringEntry);
            while (l10nCode.strip() != null) {
                l10nCode = l10nCode.strip();
                if (!this
                    .l10nStringHashtable
                    .containsKey(l10nCode)) {
                    this.l10nStringHashtable.put(
                        l10nCode,
                        l10nStringEntry);
                }
            }
        }
    }

    public void loadResource(String aResource, String key) {
        try {
            SAXReader reader = new SAXReader();
            Document document =
                reader.read(L10nString.class.getResource(aResource));

            List defaultNode =
                document.selectNodes(
                    "//resources/resource[@key = '"
                        + key
                        + "']/value[@language='en' AND @country='UK' AND @variant='default']");
            Iterator iterDefault = defaultNode.iterator();
            if (iterDefault.hasNext()) {
                Element aElement = (Element) iterDefault.next();
                String defaultString = aElement.getText();
                this.setL10nStringEntry(
                    new L10nCode(new Locale("en", "UK", "default")),
                    defaultString);
            }

            List allNodes =
                document.selectNodes(
                    "//resources/resource[@key = '"
                        + key
                        + "']/value");
            for (Iterator iter = allNodes.iterator();
                iter.hasNext();
                ) {
                Element aElement = (Element) iter.next();
                String languageString =
                    aElement.attribute("language").getText();
                String countryString =
                    aElement.attribute("country").getText();
                String variantString =
                    aElement.attribute("variant").getText();
                String aString = aElement.getText();
                this.setL10nStringEntry(
                    new L10nCode(
                        new Locale(
                            languageString,
                            countryString,
                            variantString)),
                    aString);
            }

            if (this.getDefaultL10nStringEntry() == null) {
                this.setDefaultL10nStringEntry("");
            }

            if (this.getOtherL10nStringEntry() == null) {
                this.setOtherL10nStringEntry("");
            }
        } catch (DocumentException aDocumentException) {
            System.err.println(
                "L10nString::loadResource "
                    + aDocumentException.getMessage());
        } catch (InvalidXPathException aInvalidXPathException) {
            System.err.println(
                "L10nString::loadResource "
                    + aInvalidXPathException.getMessage());
        }
    }

    public void loadXmlValues(Element valuesElement) {
        if (valuesElement != null) {
            for (Iterator valueIterator =
                valuesElement.elementIterator("value");
                valueIterator.hasNext();
                ) {
                Element valueElement = (Element) valueIterator.next();
                String languageString =
                    valueElement.attribute("language").getText();
                String countryString =
                    valueElement.attribute("country").getText();
                String variantString =
                    valueElement.attribute("variant").getText();
                String valueString = valueElement.getText();
                if ((languageString.equals("en"))
                    && (countryString.equals("UK"))
                    && (variantString.equals("default"))) {
                    this.setL10nStringEntry(
                        new L10nCode(
                            new Locale(
                                languageString,
                                countryString,
                                variantString)),
                        valueString);
                }
            }

            for (Iterator valueIterator =
                valuesElement.elementIterator("value");
                valueIterator.hasNext();
                ) {
                Element valueElement = (Element) valueIterator.next();
                String languageString =
                    valueElement.attribute("language").getText();
                String countryString =
                    valueElement.attribute("country").getText();
                String variantString =
                    valueElement.attribute("variant").getText();
                String valueString = valueElement.getText();
                this.setL10nStringEntry(
                    new L10nCode(
                        new Locale(
                            languageString,
                            countryString,
                            variantString)),
                    valueString);
            }

        }

        if (this.getDefaultL10nStringEntry() == null) {
            this.setDefaultL10nStringEntry("");
        }

        if (this.getOtherL10nStringEntry() == null) {
            this.setOtherL10nStringEntry("");
        }
    }
}
