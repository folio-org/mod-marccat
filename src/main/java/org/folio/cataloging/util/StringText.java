package org.folio.cataloging.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.business.common.SubfieldCodeComparator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

@SuppressWarnings("unchecked")
public class StringText implements Serializable 
{
	private static final long serialVersionUID = -486513419723833977L;
	
	private List subfieldList = new ArrayList();

	public StringText() {
	}

	public StringText(Subfield a) {
		this();
		addSubfield(a);
	}

	public StringText(String stringTextString) {
		parse(stringTextString);
	}

	public StringText(List codes, List values) {
		for (int i = 0; i < Math.min(codes.size(), values.size()); i++) {
			this.subfieldList.add(
				new Subfield((String) codes.get(i), (String) values.get(i)));
		}
	}

	public void parse(String stringTextString) {
		if (stringTextString != null) {
			StringTokenizer tokenizer =
				new StringTokenizer(
					stringTextString,
					Subfield.SUBFIELD_DELIMITER);
			while (tokenizer.hasMoreTokens()) {
				this.subfieldList.add(new Subfield(tokenizer.nextToken()));
			}
		}
	}

	public Subfield getSubfield(int klm) {
		return (Subfield) this.subfieldList.get(klm);
	}

	public int getNumberOfSubfields() {
		return this.subfieldList.size();
	}

	public void addSubfield(Subfield subfield) {
		this.subfieldList.add(subfield);
	}

	public void moveSubfieldUp(int i) {
		if (i > 0) {
			subfieldList.add(i - 1, subfieldList.remove(i));
		}
	}

	public void moveSubfieldDown(int i) {
		if (i < subfieldList.size() - 1) {
			subfieldList.add(i + 1, subfieldList.remove(i));
		}
	}

	public StringText addSubfield(int klm, Subfield subfield) {
		this.subfieldList.add(klm, subfield);
		return this;
	}

	public StringText removeSubfield(int klm) {
		this.subfieldList.remove(klm);
		return this;
	}

	public StringText setSubfield(int klm, Subfield subfield) {
		this.subfieldList.set(klm, subfield);
		return this;
	}

	public boolean equals(Object anObject) {
		if (anObject instanceof StringText) {
			return this.subfieldList.equals(
				((StringText) anObject).subfieldList);
		}
		return false;
	}

	public String toString() {
		Iterator subfields = this.subfieldList.iterator();
		StringBuffer buffer = new StringBuffer();

		while (subfields.hasNext()) {
			buffer.append(subfields.next());
		}
		return buffer.toString();
	}

	public String toDisplayString() {
		String returnString = new String();

		Iterator iter = subfieldList.iterator();
		while (iter.hasNext()) {
			Subfield aStringTextSubField = (Subfield) iter.next();
			returnString =
				returnString.concat(aStringTextSubField.getContent());
			if (iter.hasNext()) {
				returnString = returnString.concat(" ");
			}
		}
		return returnString;
	}
	
	public String getDisplayText() {
		return toDisplayString();
	}


	public String getMarcDisplayString() {
		return getMarcDisplayString("$");
	}

	public String getMarcDisplayString(String subfieldCodeSubstitution) {
		StringBuffer result = new StringBuffer();

		Iterator iter = subfieldList.iterator();
		while (iter.hasNext()) {
			Subfield aStringTextSubField = (Subfield) iter.next();
			result.append(subfieldCodeSubstitution);
			result.append(aStringTextSubField.getCode());
			result.append(aStringTextSubField.getContent());
		}
		return result.toString();
	}

	public boolean containsSubfield(Subfield subfield) {
		return this.subfieldList.contains(subfield);
	}

	public List getSubfieldList() {
		return Collections.unmodifiableList(subfieldList);
	}

	public Set getUsedSubfieldCodes() {
		Set set = new TreeSet();
		Iterator iterator = subfieldList.iterator();
		while (iterator.hasNext()) {
			set.add(((Subfield) iterator.next()).getCode());
		}
		return set;
	}

	public StringText add(StringText text) {
		if (text != null) {
			for (int i = 0; i < text.getNumberOfSubfields(); i++) {
				addSubfield(text.getSubfield(i));
			}
		}
		return this;
	}

	public StringText getSubfields(String include, String exclude) {
		return getSubfields(
			stringToSetOfSubfieldCodes(include),
			stringToSetOfSubfieldCodes(exclude));
	}

	private StringText getSubfields(Set include, Set exclude) {
		StringText text = new StringText();
		for (int i = 0; i < getNumberOfSubfields(); i++) {
			Subfield s = getSubfield(i);
			if (((include == null) || (include.contains(s.getCode())))
				&& ((exclude == null) || (!exclude.contains(s.getCode())))) {
				text.addSubfield((Subfield) s.clone());
			}
		}
		return text;
	}
	
	/* Order the stringText subfields given a string of subfields	 */
	public StringText orderSubfields(String order){
		StringText text = new StringText();
		for (int i=0; i < order.length(); i++){			
			text.add(getSubfieldsWithCodes(order.substring(i,i+1)));
		}
		return text;
	}

	public StringText getSubfieldsWithCodes(String codes) {
		return getSubfields(codes, null);
	}

	public StringText getSubfieldsWithoutCodes(String codes) {
		return getSubfields(null, codes);
	}

	public static Set stringToSetOfSubfieldCodes(String str) {
		if (str == null) {
			return null;
		}
		Set set = new TreeSet(new SubfieldCodeComparator());
		if (str.startsWith(" ")) {
			/*
			 * for historic reasons, this column is not nullable -- the convention
			 * has been to put blank to signify that there are no repeatable subfields
			 */
			return set;
		}
		for (int i = 0; i < str.length(); i++) {
			set.add(String.valueOf(str.charAt(i)));
		}
		return set;
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("stringText");
			List subfieldList = getSubfieldList();
			Iterator subfieldIterator = subfieldList.iterator();
			while (subfieldIterator.hasNext()) {
				Subfield subfield = (Subfield) subfieldIterator.next();
				//content_sub = lv_www_html_escape (subfield.content);
				Element subfieldElement =
					xmlDocument.createElement("subfield");
				content.appendChild(subfieldElement);
				subfieldElement.setAttribute(
					"code",
					subfield.getCode());
				Text text =
					xmlDocument.createTextNode(subfield.getContent());
				subfieldElement.appendChild(text);
			}
		}
		return content;
	}

	public void generateMarcXmlElementContent(Element datafield, Document xmlDocument, String cclQuery) {
		if (xmlDocument != null) {
			List subfieldList = getSubfieldList();
			Iterator subfieldIterator = subfieldList.iterator();
			while (subfieldIterator.hasNext()) {
				Subfield subfield = (Subfield) subfieldIterator.next();
				//content_sub = lv_www_html_escape (subfield.content);
				Element subfieldElement =
					xmlDocument.createElement("subfield");
				datafield.appendChild(subfieldElement);
				subfieldElement.setAttribute(
					"code",
					subfield.getCode());
				
				if(cclQuery !=null && cclQuery.length()>2){
					// NIC gestione testo evidenziato 	
					highlightElements(cclQuery, xmlDocument, subfield, subfieldElement);
				} else {
					Text text =
						xmlDocument.createTextNode(subfield.getContent());
					subfieldElement.appendChild(text);
				}
			}
		}
	}

	private void highlightElements(String cclQuery, Document xmlDocument, Subfield subfield, Element subfieldElement) {
		String text = XmlUtils.parseString(cclQuery, subfield); // inserisce delimitatori \u00FE e \u00FF nel testo originale
		
		boolean evidenziato = false; // questo flag indica se il testo in esame deve essere evidenziato o meno
		while (text.length()>0){ // scorre l'intero testo con i delimitatori
			if (!evidenziato){ // se il testo non deve essere evidenziato
				int indice = text.indexOf("\u00FE"); // cerca il delimitatore di apertura
				Text testoNonEvidenziato;
				if (indice == -1){ // se il pattern cercato non viene trovato (non ci sono delimitatori)...
					testoNonEvidenziato = xmlDocument.createTextNode(text); // ... viene scritto l'intero testo non evidenziato nel tag xml
					text = ""; 
				} else { // se il pattern viene trovato nel testo...
					testoNonEvidenziato = xmlDocument.createTextNode(text.substring(0,indice)); // ... viene scritto il testo non evidenziato...
					evidenziato = true; // ... viene settato a true il flag (indica che il testo seguente deve essere evidenziato) ...
					text = text.substring(indice); // ... e infine viene tagliato il testo appena scritto
				}
				subfieldElement.appendChild(testoNonEvidenziato);
			} else { // se il testo deve essere evidenziato
				int indice = text.indexOf("\u00FF"); // cerca il delimitatore di chiusura
				Element highlight = xmlDocument.createElement("highlight"); // crea un tag highlight
				
				Text testoEvidenziato;
				if (indice == -1){	// se non c'è il delimitatore di chiusura ...	
					testoEvidenziato = xmlDocument.createTextNode(text.substring(1));
					highlight.appendChild(testoEvidenziato);
//							highlight.setNodeValue(text.substring(1)); //... viene scritto l'intero testo  evidenziato nel tag highlight
					text = "";
				} else { // se il delimitatore viene trovato nel testo...
					testoEvidenziato = xmlDocument.createTextNode(text.substring(1,indice));
					highlight.appendChild(testoEvidenziato);
					
//							highlight.setNodeValue(text.substring(1,indice)); // ... viene scritto il testo non evidenziato...
					evidenziato = false; // ... viene settato a false il flag (indica che il testo seguente non deve essere evidenziato) ...
					text = text.substring(indice+1); // ... e infine viene tagliato il testo appena scritto
				}	
				subfieldElement.appendChild(highlight); // viene aggiunto il tag highlight col suo contenuto
			}					
		}
	}

	public static StringText parseModelXmlElementContent(Element xmlElement) {
		String subfieldContent;
		Element content = (Element) xmlElement.getChildNodes().item(0);
		StringText stringText = new StringText();
		NodeList subfieldList =
			content.getElementsByTagName("subfield");
		for (int subfieldIndex = 0;
			subfieldIndex < subfieldList.getLength();
			subfieldIndex++) {
			Element subfieldElement =
				(Element) subfieldList.item(subfieldIndex);
			if (subfieldElement.getChildNodes().item(0) == null) {
				subfieldContent = "";
			}
			else {
				subfieldContent = subfieldElement.getChildNodes().item(0).getNodeValue();
			}
			Subfield subfield =
				new Subfield(
					subfieldElement.getAttribute("code"),
					subfieldContent);
			stringText.addSubfield(subfield);
		}
		return stringText;
	}

	/**
	 * MIKE
	 * @return true if no subfields are present or all the subfields are empty
	 */
	public boolean isEmpty(){
		if(getNumberOfSubfields()==0) return true;
		Iterator it = getSubfieldList().iterator();
		while(it.hasNext()){
			Subfield s = (Subfield)it.next();
			if(!s.isEmpty()) return false;
		}
		return true;
	}
}
