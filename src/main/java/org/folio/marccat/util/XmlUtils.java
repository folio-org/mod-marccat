package org.folio.marccat.util;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.model.Subfield;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Vector;

/**
 * XML Utils.
 *
 * @author wimc
 * author cchiama
 * @since 1.0
 */
public final class XmlUtils {

  private final static ThreadLocal<TransformerFactory> FACTORIES = ThreadLocal.withInitial(TransformerFactory::newInstance);

  private static final Log LOGGER = new Log(XmlUtils.class);

  /**
   * Returns a string representation of the given XML document.
   *
   * @param document the XML Document.
   * @return a string representation of the given XML document.
   */
  public static String documentToString(final Document document) {
    final StringBuilder builder = new StringBuilder();

    if (document != null) {
      try {
        final Transformer transformer = FACTORIES.get().newTransformer();
        final DOMSource source = new DOMSource(document);
        final StringWriter writer = new StringWriter();

        transformer.transform(source, new StreamResult(writer));

        builder.append(writer.getBuffer());
      } catch (final TransformerException transformerException) {
        LOGGER.error(Message.MOD_MARCCAT_00024_XSLT_FAILURE, transformerException);
      }
    }
    return builder.toString();
  }

  /**
   * Suddivide la stringa di ricerca in tante ricerche più semplici (in base agli operatori 'and' e 'or')
   * Elimina operatori 'not', 'near' '(' e ')'
   * Elimina gli indici di ricerca
   *
   * @param cclQuery
   * @return array di termini ricercati
   */
  public static String[] getHighlightedTerms(String cclQuery) {
    Vector nodes = new Vector();
    String[] nodesAND = cclQuery.split(" and ");
    for (int i = 0; i < nodesAND.length; i++) {
      String[] nodesOR = nodesAND[i].split(" or ");
      for (int j = 0; j < nodesOR.length; j++) {
        nodesOR[j] = nodesOR[j].replaceAll("not ", " ");
        nodesOR[j] = nodesOR[j].replaceAll(" near ", " ");
        nodesOR[j] = nodesOR[j].replace('(', ' ');
        nodesOR[j] = nodesOR[j].replace(')', ' ');
        nodesOR[j] = nodesOR[j].replaceAll("\"", " ");
        nodesOR[j] = nodesOR[j].replaceAll("'", " ");
        nodesOR[j] = nodesOR[j].trim();
        if (nodesOR[j].contains(" "))
          nodesOR[j] = nodesOR[j].substring(nodesOR[j].indexOf(" ")).trim();

        nodes.addElement(nodesOR[j]);
      }
    }
    return (String[]) nodes.toArray(new String[0]);
  }

  /**
   * Cerca il pattern nel testo, se non viene trovato si prova a cercare parola per parola (nel caso in cui eventuali delimitatori aggiunti disturbino la ricerca)
   *
   * @param pattern
   * @param text
   * @return una coppia di indici (inizio e fine) del punto dove si trova il pattern cercato
   */
  public static int[] indexesToBeHighlighted(String pattern, String text) {
    int[] coppiaIndici = new int[2];

    int indiceInizio = 0;
    int indiceFine = 0;
    String delimitatori = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    if ((indiceInizio = text.toLowerCase().indexOf(pattern.toLowerCase())) != -1) { // se il pattern è presente nel testo
      char caratterePrima = ' ';
      char carattereDopo = ' ';

      if (indiceInizio != 0)
        caratterePrima = text.charAt(indiceInizio - 1);
      if (indiceInizio + pattern.length() < text.length())
        carattereDopo = text.charAt(indiceInizio + pattern.length());

      if (((delimitatori.indexOf(caratterePrima)) == -1) || ((delimitatori.indexOf("" + carattereDopo)) == -1)) // se il pattern non è delimitato da un carattere speciale (è parte di una parola)
        indiceInizio = -1;

      indiceFine = indiceInizio + pattern.length();
    } else {
      String testoRipulito = text.replaceAll("\u00FE", "");
      testoRipulito = testoRipulito.replaceAll("\u00FF", "");

      if (testoRipulito.toLowerCase().indexOf(pattern.toLowerCase()) != -1) {
        String[] elencoParole = pattern.split(" ");


        indiceInizio = text.indexOf(elencoParole[0]);
        indiceFine = text.indexOf(elencoParole[elencoParole.length - 1]) + elencoParole[elencoParole.length - 1].length();
      }
    }
    coppiaIndici[0] = indiceInizio;
    coppiaIndici[1] = indiceFine;

    return coppiaIndici;
  }

  /**
   * Aggiunge i delimitatori del pattern da evidenziare nel testo
   *
   * @param begin
   * @param end
   * @param text
   * @return il testo con i delimitatori del singolo pattern cercato
   */
  public static String addDelimiters(int begin, int end, String text) {
    String delimApertura = "\u00FE";
    String delimChiusura = "\u00FF";

    String primaParte = text.substring(0, end);
    String secondaParte = text.substring(end);
    text = primaParte + delimChiusura + secondaParte;

    primaParte = text.substring(0, begin);
    secondaParte = text.substring(begin);
    text = primaParte + delimApertura + secondaParte;

    return cleanUpDelimiters(text);
  }

  /**
   * Pulisce il testo dai delimitatori 'incrociati', cioè i casi in cui due delimitatori di apertura o di chiusura compaiano di seguito
   *
   * @param text
   * @return il testo ripulito
   */
  private static String cleanUpDelimiters(final String text) {
    boolean open = false;
    String cleanedText = "";
    for (int i = 0; i < text.length(); i++) {
      switch (text.charAt(i)) {
        case '\u00FE':
          if (!open) {
            cleanedText += "\u00FE";
            open = true;
          }
          break;
        case '\u00FF':
          if (open) {
            cleanedText += "\u00FF";
          } else {
            int indiceChiusura = cleanedText.lastIndexOf("\u00FF");
            cleanedText = cleanedText.substring(0, indiceChiusura) + cleanedText.substring(indiceChiusura + 1) + "\u00FF";
          }
          open = false;
          break;
        default:
          cleanedText += text.charAt(i);
      }
    }

    return cleanedText;
  }

  /**
   * Inizia la ricerca nella stringa per inserire i delimitatori di tutti i pattern.
   *
   * @param cclQuery
   * @param subfield
   * @return la stringa con tutti i delimitatori
   */
  public static String parseString(final String cclQuery, final Subfield subfield) {
    final String[] termsToBeHighlighted = getHighlightedTerms(cclQuery);
    String text = subfield.getContent();
    for (int i = 0; i < termsToBeHighlighted.length; i++) {
      String termineCorrente = termsToBeHighlighted[i];
      int[] indiciDelimitatore = indexesToBeHighlighted(termineCorrente, text);

      if (indiciDelimitatore[0] != -1)
        text = addDelimiters(indiciDelimitatore[0], indiciDelimitatore[1], text);
    }
    return text;
  }
}
