package org.folio.marccat.util;

import org.folio.marccat.business.common.SubfieldCodeComparator;
import org.folio.marccat.model.Subfield;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import java.io.Serializable;
import java.util.*;


/**
 * The Class StringText.
 */
public class StringText implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -486513419723833977L;

  /** The subfield list. */
  private List subfieldList = new ArrayList();

  /**
   * Instantiates a new string text.
   */
  public StringText() {
  }

  /**
   * Instantiates a new string text.
   *
   * @param a the a
   */
  public StringText(final Subfield a) {
    this();
    addSubfield(a);
  }

  /**
   * Instantiates a new string text.
   *
   * @param stringTextString the string text string
   */
  public StringText(final String stringTextString) {
    parse(stringTextString);
  }

  /**
   * Instantiates a new string text.
   *
   * @param codes the codes
   * @param values the values
   */
  public StringText(final List<String> codes, final List<String> values) {
    for (int i = 0; i < Math.min(codes.size(), values.size()); i++) {
      this.subfieldList.add(
        new Subfield(codes.get(i), values.get(i)));
    }
  }

  /**
   * String to set of subfield codes.
   *
   * @param str the str
   * @return the sets the
   */
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

  /**
   * Parses the model xml element content.
   *
   * @param xmlElement the xml element
   * @return the string text
   */
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
      } else {
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
   * Parses the.
   *
   * @param stringTextString the string text string
   */
  public void parse(final String stringTextString) {
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

  /**
   * Gets the subfield.
   *
   * @param klm the klm
   * @return the subfield
   */
  public Subfield getSubfield(int klm) {
    return (Subfield) this.subfieldList.get(klm);
  }

  /**
   * Gets the number of subfields.
   *
   * @return the number of subfields
   */
  public int getNumberOfSubfields() {
    return this.subfieldList.size();
  }

  /**
   * Adds the subfield.
   *
   * @param subfield the subfield
   */
  public void addSubfield(Subfield subfield) {
    this.subfieldList.add(subfield);
  }

  /**
   * Adds the subfield.
   *
   * @param klm the klm
   * @param subfield the subfield
   * @return the string text
   */
  public StringText addSubfield(int klm, Subfield subfield) {
    this.subfieldList.add(klm, subfield);
    return this;
  }

  /**
   * Equals.
   *
   * @param anObject the an object
   * @return true, if successful
   */
  public boolean equals(Object anObject) {
    if (anObject instanceof StringText) {
      return this.subfieldList.equals(
        ((StringText) anObject).subfieldList);
    }
    return false;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    Iterator subfields = this.subfieldList.iterator();
    StringBuilder builder = new StringBuilder();
    while (subfields.hasNext()) {
      builder.append(subfields.next());
    }
    return builder.toString();
  }

  /**
   * To display string.
   *
   * @return the string
   */
  public String toDisplayString() {
    String returnString = "";

    Iterator iter = subfieldList.iterator();
    while (iter.hasNext()) {
      final Subfield aStringTextSubField = (Subfield) iter.next();
      returnString = returnString.concat(aStringTextSubField.getContent());
      if (iter.hasNext()) {
        returnString = returnString.concat(" ");
      }
    }
    return returnString;
  }

  /**
   * Gets the marc display string.
   *
   * @return the marc display string
   */
  public String getMarcDisplayString() {
    return getMarcDisplayString("$");
  }

  /**
   * Gets the marc display string.
   *
   * @param subfieldCodeSubstitution the subfield code substitution
   * @return the marc display string
   */
  public String getMarcDisplayString(final String subfieldCodeSubstitution) {
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

  /**
   * Contains subfield.
   *
   * @param subfield the subfield
   * @return true, if successful
   */
  public boolean containsSubfield(Subfield subfield) {
    return this.subfieldList.contains(subfield);
  }

  /**
   * Gets the subfield list.
   *
   * @return the subfield list
   */
  public List getSubfieldList() {
    return Collections.unmodifiableList(subfieldList);
  }


  /**
   * Adds the.
   *
   * @param text the text
   * @return the string text
   */
  public StringText add(StringText text) {
    if (text != null) {
      for (int i = 0; i < text.getNumberOfSubfields(); i++) {
        addSubfield(text.getSubfield(i));
      }
    }
    return this;
  }

  /**
   * Gets the subfields.
   *
   * @param include the include
   * @param exclude the exclude
   * @return the subfields
   */
  public StringText getSubfields(String include, String exclude) {
    return getSubfields(
      stringToSetOfSubfieldCodes(include),
      stringToSetOfSubfieldCodes(exclude));
  }

  /**
   * Gets the subfields.
   *
   * @param include the include
   * @param exclude the exclude
   * @return the subfields
   */
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

  /**
   * Order subfields.
   *
   * @param order the order
   * @return the string text
   */
  public StringText orderSubfields(String order) {
    StringText text = new StringText();
    for (int i = 0; i < order.length(); i++) {
      text.add(getSubfieldsWithCodes(order.substring(i, i + 1)));
    }
    return text;
  }

  /**
   * Gets the subfields with codes.
   *
   * @param codes the codes
   * @return the subfields with codes
   */
  public StringText getSubfieldsWithCodes(String codes) {
    return getSubfields(codes, null);
  }

  /**
   * Gets the subfields without codes.
   *
   * @param codes the codes
   * @return the subfields without codes
   */
  public StringText getSubfieldsWithoutCodes(String codes) {
    return getSubfields(null, codes);
  }

  /**
   * Generate model xml element content.
   *
   * @param xmlDocument the xml document
   * @return the element
   */
  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("stringText");
      List subfieldList = getSubfieldList();
      Iterator subfieldIterator = subfieldList.iterator();
      while (subfieldIterator.hasNext()) {
        Subfield subfield = (Subfield) subfieldIterator.next();
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

  /**
   * Generate marc xml element content.
   *
   * @param datafield the datafield
   * @param xmlDocument the xml document
   * @param cclQuery the ccl query
   */
  public void generateMarcXmlElementContent(Element datafield, Document xmlDocument, String cclQuery) {
    if (xmlDocument != null) {
      List subfieldList = getSubfieldList();
      Iterator subfieldIterator = subfieldList.iterator();
      while (subfieldIterator.hasNext()) {
        Subfield subfield = (Subfield) subfieldIterator.next();
        Element subfieldElement =
          xmlDocument.createElement("subfield");
        datafield.appendChild(subfieldElement);
        subfieldElement.setAttribute(
          "code",
          subfield.getCode());

        Text text =
          xmlDocument.createTextNode(subfield.getContent());
        subfieldElement.appendChild(text);

      }
    }
  }


  /**
   * Checks if is empty.
   *
   * @return true if no subfields are present or all the subfields are empty
   */
  public boolean isEmpty() {
    if (getNumberOfSubfields() == 0) return true;
    Iterator it = getSubfieldList().iterator();
    while (it.hasNext()) {
      Subfield s = (Subfield) it.next();
      if (!s.isEmpty()) return false;
    }
    return true;
  }

  /**
   * Add punctuation before the given code unless the given excluded punc is already present.
   *
   * @param code the code
   * @param punc the punc
   * @param excludedPunc the excluded punc
   */
  public void addPrecedingPunctuation(String code, String punc, String excludedPunc) {
    int subfieldIndex = 0;
    for (Object o : getSubfieldList()) {
      Subfield s = (Subfield) o;
      if (s.getCode().equals(code)) {
        if (subfieldIndex > 0) {
          Subfield before = getSubfield(subfieldIndex - 1);
          if (!before.getContent().endsWith(excludedPunc)) {
            before.setContent(before.getContent() + punc);
          }
        }
      }
      subfieldIndex++;
    }
  }

  /**
   * Remove the given punctuation if preceding the given subfield code.
   *
   * @param code the code
   * @param punc the punc
   */
  public void removePrecedingPunctuation(String code, String punc) {
    int subfieldIndex = 0;
    for (Object o : getSubfieldList()) {
      Subfield s = (Subfield) o;
      if (s.getCode().equals(code)) {
        if (subfieldIndex > 0) {
          Subfield before = getSubfield(subfieldIndex - 1);
          String bc = before.getContent();
          if (bc.endsWith(punc)) {
            before.setContent(bc.substring(0, bc.length() - punc.length() + 1));
          }
        }
      }
      subfieldIndex++;
    }
  }

  /**
   * Ensures that the given subfield codes end in the given punctuation.
   *
   * @param codes the codes
   * @param otherTerminals the other terminals
   * @param terminalChar the terminal char
   * @return true if given subfield is found
   */
  public boolean addTerminalPunctuation(String codes, String otherTerminals, String terminalChar) {
    int subfieldIndex = 0;
    boolean result = false;
    for (Object o : getSubfieldList()) {
      Subfield s = (Subfield) o;
      if (codes.contains(s.getCode())) {
        result = true;
        if (!otherTerminals.contains(""+s.getContent().charAt(s.getContentLength() - 1))){
          s.setContent(s.getContent() + terminalChar);
        }
      }
      subfieldIndex++;
    }
    return result;
  }
}
