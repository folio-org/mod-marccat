package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.common.SortFormException;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.business.descriptor.SortformUtils;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.ClassificationDescriptorDAO;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hibernate class for table CLSTN.
 *
 * @author paulm
 * @author carment
 */
public class CLSTN extends Descriptor {

  private String textEnd = "\u0003";
  /**
   * The dewey edition number.
   */
  private Integer deweyEditionNumber;

  /**
   * The type code.
   */
  private int typeCode;

  /**
   * Instantiates a new clstn.
   */
  public CLSTN() {
    super();
  }


  /* (non-Javadoc)
   * @see Descriptor#getReferenceClass(java.lang.Class)
   */
  public Class getReferenceClass(Class targetClazz) {
    return null;
  }


  /**
   * Gets the dao.
   *
   * @return the dao
   */
  public AbstractDAO getDAO() {
    return new ClassificationDescriptorDAO();
  }


  /* (non-Javadoc)
   * @see Descriptor#getAccessPointClass()
   */
  public Class getAccessPointClass() {
    return ClassificationAccessPoint.class;
  }


  public String getDefaultBrowseKey() {
    return "23P5";
  }


  public String getNextNumberKeyFieldCode() {
    return "LN";
  }


  public CorrelationValues getCorrelationValues() {
    return new CorrelationValues(
      getTypeCode(),
      CorrelationValues.UNDEFINED,
      CorrelationValues.UNDEFINED);
  }

  public void setCorrelationValues(CorrelationValues v) {
    setTypeCode(v.getValue(1));
  }


  public SortFormParameters getSortFormParameters() {
    return new SortFormParameters(400, getTypeCode(), 0, 0, 0);
  }

  public int getCategory() {
    return 20;
  }


  /**
   * Gets the dewey edition number.
   *
   * @return the dewey edition number
   */
  public Integer getDeweyEditionNumber() {
    return deweyEditionNumber;
  }

  /**
   * Sets the dewey edition number.
   *
   * @param short1 the new dewey edition number
   */
  public void setDeweyEditionNumber(Integer short1) {
    deweyEditionNumber = short1;
  }

  /**
   * Gets the type code.
   *
   * @return the type code
   */
  public int getTypeCode() {
    return typeCode;
  }

  /**
   * Sets the type code.
   *
   * @param s the new type code
   */
  public void setTypeCode(int s) {
    typeCode = s;
  }

  /* (non-Javadoc)
   * @see Descriptor#getHeadingNumberSearchIndexKey()
   */
  public String getHeadingNumberSearchIndexKey() {
    return "233P";
  }

  /* (non-Javadoc)
   * @see Descriptor#getLockingEntityType()
   */
  public String getLockingEntityType() {
    return "LN";
  }

  @Override
  public void setStringText(String string) {
    if (ClassificationType.isDewey(getTypeCode())) {
      StringText st = new StringText(string);
      StringText sub2 = st.getSubfieldsWithCodes("2");
      if (!sub2.isEmpty()) {
        try {
          setDeweyEditionNumber(Integer.valueOf(sub2.getSubfield(0).getContent()));
        } catch (NumberFormatException e) {
          // ignore non numeric $2
        }
        st = st.getSubfieldsWithoutCodes("2");
      }
      super.setStringText(st.toString());
    } else {
      super.setStringText(string);
    }
  }

  @Override
  public void calculateAndSetSortForm() {
    if (ClassificationType.isLC((short) getTypeCode())) {
      setSortForm(calculateLcSortForm());
    } else if (ClassificationType.isDewey((short) getTypeCode())) {
      setSortForm(calculateDeweySortForm());
    } else if (ClassificationType.isNLM((short) getTypeCode())) {
      setSortForm(calculateLcSortForm());
    } else if (ClassificationType.isNAL((short) getTypeCode())) {
      setSortForm(calculateNalSortForm());
    } else {
      setSortForm(calculateDefaultClassSortForm());
    }
  }

  private String calculateDefaultClassSortForm() {
    String result = new StringText(getStringText()).toDisplayString().toUpperCase();
    result = SortformUtils.get().stripAccents(result);
    result = result.replace("\"", " ");
    result = result.replace("'", " ");
    result = replacePeriodsForClassNumbers(result);
    result = SortformUtils.get().stripMultipleBlanks(result);
    return result;
  }

  private String calculateDeweySortForm() {
    String result = new StringText(getStringText()).toDisplayString();
    result = SortformUtils.get().stripAccents(result);
    result = SortformUtils.get().replaceDeweyPunctuation(result, "");
    String prefix = null;
    Pattern prefixPattern = Pattern.compile("^(JC|J|C)\\s*(.*)");
    Matcher m = prefixPattern.matcher(result);
    if (m.find()) {
      prefix = m.group(1);
      result = m.group(2);
    }
    result = result.replaceAll("[A-Z]", "");
    Integer edition = getDeweyEditionNumber();
    if (edition == null) {
      edition = 0;
    }
    result = result + " Z" + String.format("%02d", edition);
    if (prefix != null) {
      result = result + " " + prefix;
    }
    return result;
  }

  private String calculateLcSortForm() {
    StringText st = new StringText(getStringText());
    for (Object obj : st.getSubfieldList()) {
      Subfield sub = (Subfield) obj;
      if (sub.getCode().equals("b")) {
        sub.setContent(textEnd + sub.getContent());
        break;
      }
    }
    String result = st.toDisplayString();
    result = SortformUtils.get().replacePunctuationMark1(result, " ");
    result = SortformUtils.get().stripMultipleBlanks(result);
    String[] parts = result.split(textEnd);
    if (parts[0].endsWith("*")) {
      parts[0] = parts[0].substring(0, parts[0].length() - 2) + ".";
    }
    StringBuilder sb = new StringBuilder();
    Pattern lc = Pattern.compile("([A-Z]{0,3})(\\s*)([0-9]{0,4})\\.?");
    Matcher m = lc.matcher(parts[0]);
    if (m.find()) {
      if (m.group(1 /* alpha */) != null) {
        sb.append((m.group(1) + "^^^^"), 0, 3);
        sb.append(m.group(2));
      }
      if (m.group(3 /* digits */) != null && !m.group(3).isEmpty()) {
        sb.append(("0000" + m.group(3)).substring(m.group(3).length()));
      }
      if (m.group(0).endsWith(".")) {
        sb.append(".");
      }
      sb.append(parts[0].substring(m.end()));
    } else {
      sb.append(parts[0]);
    }
    if (parts.length > 1) {
      sb.append(parts[1]);
    }
    result = sb.toString();
    if (!st.getSubfield(0).getCode().equals("a")) {
      result = " " + result;
    }
    result = replacePeriodsForClassNumbers(result);
    result = result.replace('^', ' ');
    if (result.length() > 3) {
      result = result.substring(0, 2) + result.substring(2).replaceAll("\\s{2,}", " ");
    }
    result = result.replaceAll("\\s*$", "");
    return result;
  }

  private String calculateNalSortForm() {
    StringText st = new StringText(getStringText());
    for (Object obj : st.getSubfieldList()) {
      Subfield sub = (Subfield) obj;
      if (sub.getCode().equals("b")) {
        sub.setContent(textEnd + sub.getContent());
        break;
      }
    }
    String result = st.toDisplayString();
    result = SortformUtils.get().replacePunctuationMark1(result, " ");
    result = SortformUtils.get().stripMultipleBlanks(result);
    String[] parts = result.split(textEnd);
    if (parts[0].endsWith("*")) {
      parts[0] = parts[0].substring(0, parts[0].length() - 2) + ".";
    }
    StringBuilder sb = new StringBuilder();
    Pattern nal = Pattern.compile("([A-Z]{0,5})(\\s*)([0-9]{0,4})\\.?");
    Matcher m = nal.matcher(parts[0]);
    if (m.find()) {
      if (m.group(1 /* alpha */) != null) {
        sb.append((m.group(1) + "^^^^^^"), 0, 5);
        sb.append(m.group(2));
      }
      if (m.group(3 /* digits */) != null && !m.group(3).isEmpty()) {
        sb.append(("0000" + m.group(3)).substring(m.group(3).length()));
      }
      if (m.group(0).endsWith(".")) {
        sb.append(".");
      }
      sb.append(parts[0].substring(m.end()));
    } else {
      sb.append(parts[0]);
    }
    if (parts.length > 1) {
      sb.append(parts[1]);
    }
    result = sb.toString();
    if (!st.getSubfield(0).getCode().equals("a")) {
      result = " " + result;
    }
    result = replacePeriodsForClassNumbers(result);
    result = result.replace('^', ' ');
    if (result.length() > 3) {
      result = result.substring(0, 4) + result.substring(4).replaceAll("\\s{2,}", " ");
    }
    result = result.replaceAll("\\s*$", "");
    return result;
  }

  private String replacePeriodsForClassNumbers(String s) {
    s = s.replaceAll("([A-Z])\\.(?=[A-Z])", "$1");
    s = s.replaceAll("([0-9])\\.(?=[0-9])", "$1");
    s = s.replaceAll("\\.$", "");
    s = s.replaceAll("\\.", " ");
    return s;
  }
}
