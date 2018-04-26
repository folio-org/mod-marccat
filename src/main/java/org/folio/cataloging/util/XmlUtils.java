/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2004
 * 
 * XmlUtilis.java
 */
package org.folio.cataloging.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.librivision.AbstractRecord;
import org.folio.cataloging.business.librivision.XslTransformerConfigurationException;
import org.folio.cataloging.business.librivision.XslTransformerException;
import org.folio.cataloging.exception.ModCatalogingException;
import org.folio.cataloging.model.Subfield;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Vector;

/**
 * @author wimc
 * @version $Revision: 1.2 $, $Date: 2004/12/07 09:30:54 $
 * @see
 * @since 1.0
 */
public final class XmlUtils {

	private static final Log logger = LogFactory.getLog(XmlUtils.class);

	private XmlUtils() {
	}

	public static String documentToString(Document document) {
		return new String(documentToStringBuffer(document));
	}

	public static StringBuffer documentToStringBuffer(Document document) {
		StringBuffer stringBuffer = new StringBuffer("");
		if (document != null) {
			try {
				TransformerFactory transformerFactory =
					TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				//stringBuffer.append(nodeListToStringBuffer(document.getChildNodes()));
				DOMSource source = new DOMSource(document);
				StringWriter stringWriter = new StringWriter();
				StreamResult streamResult = new StreamResult(stringWriter);
				try {
					transformer.transform(source, streamResult);
					stringBuffer.append(stringWriter.getBuffer());
				} catch (TransformerException transformerException) {
					logger.error(transformerException.getMessage());
					//throw new XslTransformerException(transformerException);
				}
			} catch (TransformerConfigurationException transformerConfigurationException) {
				logger.error(transformerConfigurationException.getMessage());
				//throw new XslTransformerConfigurationException(
				//		transformerConfigurationException);
			}
		}
		return stringBuffer;
	}
	
	/**
	 * Suddivide la stringa di ricerca in tante ricerche più semplici (in base agli operatori 'and' e 'or')
	 * Elimina operatori 'not', 'near' '(' e ')'
	 * Elimina gli indici di ricerca  
	 * @param cclQuery
	 * @return array di termini ricercati 
	 */
	public static String[] getHighlightedTerms(String cclQuery){
		Vector nodes = new Vector();
		String[] nodesAND = cclQuery.split(" and ");
		for (int i=0;i<nodesAND.length;i++) {
			String[] nodesOR = nodesAND[i].split(" or ");
			for (int j=0;j<nodesOR.length;j++) {
				nodesOR[j] = nodesOR[j].replaceAll("not ", " ");
				nodesOR[j] = nodesOR[j].replaceAll(" near ", " ");
				nodesOR[j] = nodesOR[j].replace('(',' ');
				nodesOR[j] = nodesOR[j].replace(')',' ');
				nodesOR[j] = nodesOR[j].replaceAll("\"", " ");
				nodesOR[j] = nodesOR[j].replaceAll("'", " ");
				nodesOR[j] = nodesOR[j].trim();
				if(nodesOR[j].indexOf(" ") != -1)
					nodesOR[j] = nodesOR[j].substring(nodesOR[j].indexOf(" ")).trim();
				
				nodes.addElement(nodesOR[j]);
			}
		}
		return (String[])nodes.toArray(new String[0]);
	}
	
	/**
	 * Cerca il pattern nel testo, se non viene trovato si prova a cercare parola per parola (nel caso in cui eventuali delimitatori aggiunti disturbino la ricerca)
	 * @param pattern
	 * @param text
	 * @return una coppia di indici (inizio e fine) del punto dove si trova il pattern cercato
	 */
	public static int[] indexesToBeHighlighted(String pattern, String text){
		int[] coppiaIndici = new int[2];
		
		int indiceInizio = 0;
		int indiceFine = 0;
		String delimitatori = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
		if ((indiceInizio= text.toLowerCase().indexOf(pattern.toLowerCase()))!=-1){ // se il pattern è presente nel testo
			char caratterePrima = ' ';
			char carattereDopo = ' ';
			
			if (indiceInizio!=0)
				caratterePrima = text.charAt(indiceInizio-1);
			if (indiceInizio+pattern.length()<text.length())
				carattereDopo = text.charAt(indiceInizio+pattern.length());
			
			if (((delimitatori.indexOf(caratterePrima))==-1) || ((delimitatori.indexOf(""+carattereDopo))==-1)) // se il pattern non è delimitato da un carattere speciale (è parte di una parola)
				indiceInizio = -1;
			
			indiceFine = indiceInizio+pattern.length();
		} else {
			String testoRipulito = text.replaceAll("\u00FE","");
			testoRipulito = testoRipulito.replaceAll("\u00FF","");
			
			if (testoRipulito.toLowerCase().indexOf(pattern.toLowerCase())!=-1){
				String[] elencoParole = pattern.split(" ");

				
				indiceInizio = text.indexOf(elencoParole[0]);
				indiceFine = text.indexOf(elencoParole[elencoParole.length-1])+elencoParole[elencoParole.length-1].length();
			}	
		}
		coppiaIndici[0]=indiceInizio;
		coppiaIndici[1]=indiceFine;
		
		return coppiaIndici;
	}
	
	/**
	 * Aggiunge i delimitatori del pattern da evidenziare nel testo
	 * @param begin
	 * @param end
	 * @param text
	 * @return il testo con i delimitatori del singolo pattern cercato
	 */
	public static String addDelimiters(int begin, int end, String text){
		String delimApertura = "\u00FE";
		String delimChiusura = "\u00FF";
		
		String primaParte = text.substring(0,end);
		String secondaParte = text.substring(end);		
		text = primaParte+delimChiusura+secondaParte;
		
		primaParte = text.substring(0,begin);
		secondaParte = text.substring(begin);		
		text = primaParte+delimApertura+secondaParte;
		
		return cleanUpDelimiters(text); 
	}
	
	/**
	 * Pulisce il testo dai delimitatori 'incrociati', cioè i casi in cui due delimitatori di apertura o di chiusura compaiano di seguito 
	 * @param text
	 * @return il testo ripulito
	 */
	private static String cleanUpDelimiters (String text){
		boolean aperto = false;
		String cleanedText = "";
		for (int i=0;i<text.length();i++)
			switch (text.charAt(i)) {
				case '\u00FE':
					if (!aperto){
						cleanedText += "\u00FE";
						aperto = true;
					}
					break;
				case '\u00FF':
					if (aperto) {
						cleanedText += "\u00FF";						
					} else {
						int indiceChiusura = cleanedText.lastIndexOf("\u00FF");
						cleanedText = cleanedText.substring(0,indiceChiusura) + cleanedText.substring(indiceChiusura+1)+"\u00FF";
					}
					aperto = false;
					break;
				default:
					cleanedText += text.charAt(i);
			} 
				
		
		return cleanedText;
	}
	
	/**
	 * Inizia la ricerca nella stringa per inserire i delimitatori di tutti i pattern
	 * @param cclQuery
	 * @param subfield
	 * @return la stringa con tutti i delimitatori
	 */
	public static String parseString(String cclQuery, Subfield subfield) {
		String[] termsToBeHighlighted = getHighlightedTerms(cclQuery);
		String text = subfield.getContent();
		for (int i=0;i<termsToBeHighlighted.length;i++){
			String termineCorrente = termsToBeHighlighted[i];	
			int[] indiciDelimitatore = indexesToBeHighlighted(termineCorrente, text);
			
			if (indiciDelimitatore[0]!=-1)
				text = addDelimiters(indiciDelimitatore[0], indiciDelimitatore[1], text);						
		}
		return text;
	}

	public static byte[] transformDocument(Document document, String stylesheet)
			throws ModCatalogingException {

		try {
			// load the transformer using JAXP
			TransformerFactory factory = TransformerFactory.newInstance();
			URL styleURL = AbstractRecord.class.getResource("/xslt/"
					+ stylesheet);
			logger.debug("transform sheet is:" + styleURL);
			Transformer transformer = factory.newTransformer(new StreamSource(
					styleURL.openStream()));
			// now lets style the given document
			DOMSource source = new DOMSource(document);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(buffer);
			transformer.transform(source, result);

			// return the transformed document
			return buffer.toByteArray();
		} catch (TransformerConfigurationException transformerConfigurationException) {
			logger.error(transformerConfigurationException.getMessage());
			throw new XslTransformerConfigurationException(
					transformerConfigurationException);
		} catch (TransformerException transformerException) {
			logger.error(transformerException.getMessage());
			throw new XslTransformerException(transformerException);
		} 
//		catch (FileNotFoundException e) {
//			logger.error(e);
//			throw new ModCatalogingException(e);
//		}
 catch (IOException e) {
		logger.error(e);
		throw new ModCatalogingException(e);
		}

	}

}
