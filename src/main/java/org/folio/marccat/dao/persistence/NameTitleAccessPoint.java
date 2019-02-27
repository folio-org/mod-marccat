package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.NameTitleComponent;
import org.folio.marccat.business.cataloguing.common.OrderedTag;
import org.folio.marccat.config.log.Global;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAONameTitleAccessPoint;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

/**
 * Persistent class to manage name-title heading.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class NameTitleAccessPoint extends NameTitleComponent implements OrderedTag {

  /**
   * The Constant daoNameTitleAccessPoint.
   */
  private static final DAONameTitleAccessPoint daoNameTitleAccessPoint = new DAONameTitleAccessPoint();

  /**
   * The descriptor.
   */
  private NME_TTL_HDG descriptor = new NME_TTL_HDG();

  /**
   * The institution.
   */
  private String institution;

  /**
   * The secondary function code.
   */
  private int secondaryFunctionCode;

  /**
   * The series issn heading number.
   */
  private String seriesIssnHeadingNumber;

  /**
   * The volume number description.
   */
  private String volumeNumberDescription;

  /**
   * The sequence number.
   */
  private Integer sequenceNumber;

  /**
   * Instantiates a new name title access point.
   */
  public NameTitleAccessPoint() {
    super();
  }

  /**
   * Instantiates a new name title access point.
   *
   * @param itemNbr the item nbr
   */
  public NameTitleAccessPoint(final int itemNbr) {
    super(itemNbr);
  }

  /**
   * Gets the stringText associated to name-title access point.
   *
   * @return stringText.
   */
  public StringText getAccessPointStringText() {
    final StringText text = new StringText(volumeNumberDescription);
    text.parse(institution);
    return text;
  }

  /**
   * Sets stringText to name-title access point.
   *
   * @param stringText -- the stringText to set.
   */
  public void setAccessPointStringText(final StringText stringText) {
    volumeNumberDescription = stringText.getSubfieldsWithCodes(Global.TITLE_VOLUME_SUBFIELD_CODE).toString();
    institution = stringText.getSubfieldsWithCodes(Global.NAME_TITLE_INSTITUTION_SUBFIELD_CODE).toString();
  }

  /**
   * Gets correlation values of name-title descriptor.
   *
   * @return correlationValues.
   */
  public CorrelationValues getCorrelationValues() {
    CorrelationValues v = getDescriptor().getCorrelationValues();
    v = v.change(3, v.getValue(2));
    v = v.change(2, v.getValue(1));
    v = v.change(1, getFunctionCode());
    return v;
  }

  /**
   * Sets correlation values of name-title descriptor.
   *
   * @param v -- the correlation values to set.
   */
  public void setCorrelationValues(final CorrelationValues v) {
    setFunctionCode(v.getValue(1));
    CorrelationValues v2 = new CorrelationValues(v.getValue(2), v.getValue(3), CorrelationValues.UNDEFINED);
    getDescriptor().setCorrelationValues(v2);
  }

  /**
   * Gets descriptor associated to name-title access point.
   *
   * @return descriptor.
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Sets descriptor.
   *
   * @param descriptor -- the descriptor associated to title access point.
   */
  public void setDescriptor(final Descriptor descriptor) {
    this.descriptor = (NME_TTL_HDG) descriptor;
  }

  /**
   * Gets institution associated to name-title access point.
   *
   * @return institution.
   */
  public String getInstitution() {
    return institution;
  }

  /**
   * Sets institution param to name-title access point.
   *
   * @param institution -- the institution param to set.
   */
  public void setInstitution(final String institution) {
    this.institution = institution;
  }

  /**
   * Gets the secondary function code related to title.
   *
   * @return secondaryFunctionCode.
   */
  public int getSecondaryFunctionCode() {
    return secondaryFunctionCode;
  }

  /**
   * Sets secondary function code to name-title access point.
   *
   * @param s -- secondary function code associated.
   */
  public void setSecondaryFunctionCode(final int s) {
    secondaryFunctionCode = s;
  }

  /**
   * Gets series issn heading number associated to name-title access point.
   *
   * @return seriesIssnHeadingNumber.
   */
  public String getSeriesIssnHeadingNumber() {
    return seriesIssnHeadingNumber;
  }

  /**
   * Sets issn heading number to title access point.
   *
   * @param issnHeadingNumber -- the issn number to set.
   */
  public void setSeriesIssnHeadingNumber(final String issnHeadingNumber) {
    seriesIssnHeadingNumber = issnHeadingNumber;
  }

  /**
   * Gets volume number description associated to name-title access point.
   *
   * @return volumeNumberDescription.
   */
  public String getVolumeNumberDescription() {
    return volumeNumberDescription;
  }

  /**
   * Gets volume number description associated to title access point.
   *
   * @param volume the new volume number description
   * @return volumeNumberDescription.
   */
  public void setVolumeNumberDescription(final String volume) {
    volumeNumberDescription = volume;
  }

  /**
   * Sets descriptor stringText.
   *
   * @param stringText -- the stringText to set.
   */
  public void setDescriptorStringText(StringText stringText) {
    getDescriptor().setStringText(stringText.getSubfieldsWithoutCodes(Global.NAME_TITLE_VARIANT_CODES).toString());
  }

  /**
   * Gets the dao.
   *
   * @return the associated dao.
   */
  public AbstractDAO getDAO() {
    return daoNameTitleAccessPoint;
  }

  /**
   * Gets the variant codes.
   *
   * @return the variant codes
   */
  @Override
  public String getVariantCodes() {
    return Global.NAME_TITLE_VARIANT_CODES;
  }

  /**
   * Gets the sequence number associated to name-title access point.
   *
   * @return sequenceNumber.
   */
  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * Sets sequence number to title access point.
   *
   * @param sequenceNumber -- the sequence number to set.
   */
  public void setSequenceNumber(final Integer sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
    super.setSequenceNumber(sequenceNumber);
  }


  /**
   * Adds the punctuation.
   *
   * @return the string text
   * @throws Exception the exception
   */
  @Override
  public StringText addPunctuation() throws Exception {
    final StringText result = new StringText(getStringText().toString());
    try {
      final CorrelationKey marc = getMarcEncoding();

      if (Global.NAMES_D.contains(marc.getMarcTag())) {
        result.addPrecedingPunctuation("d", ",", ",");
      }
      if (Global.NAMES_E.contains(marc.getMarcTag())) {
        result.addPrecedingPunctuation("e", ",", ",");
      }
      result.removePrecedingPunctuation("4", ",");
      if (Global.NAMES_X.contains(marc.getMarcTag())) {
        result.addPrecedingPunctuation("x", ",", ",");
      }
      if (Global.NAMES_V.contains(marc.getMarcTag())) {
        result.addPrecedingPunctuation("v", " :", null);
      }
      if (Global.NAMES_245.contains(marc.getMarcTag())) {
        result.addTerminalPunctuation("245", Global.TERMINAL_PUNCTUATION, ".");
      }
      boolean found = result.addTerminalPunctuation("4", Global.OTHER_TERMINAL_PUNCTUATION, ".");
      if (!found) {
        if (Global.NAMES.contains(marc.getMarcTag())) {
          Subfield last = result.getSubfield(result.getNumberOfSubfields() - 1);
          if (!Global.TERMINAL_PUNCTUATION.contains("" + last.getContent().charAt(last.getContentLength() - 1))) {
            last.setContent(last.getContent() + ".");
          }
        } else {
          result.addPrecedingPunctuation("t", Global.TERMINAL_PUNCTUATION, ".");
        }
      }
      return result;
    } catch (Exception e) {
      return result;
    }
  }


}
