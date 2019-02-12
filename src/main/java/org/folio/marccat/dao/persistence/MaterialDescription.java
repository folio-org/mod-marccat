package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.lang.StringUtils;
import org.folio.marccat.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.common.UserViewHelper;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.CorrelationValues;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

public class MaterialDescription extends FixedFieldUsingItemEntity implements PersistentObjectWithView {
  public static final String DEFAULT_DATE = "    ";
  private static final long serialVersionUID = 232911118088181003L;
  private int materialDescriptionKeyNumber;
  private String materialTypeCode;
  private String materialDescription008Indicator;
  private String bookIllustrationCode;
  private String targetAudienceCode;
  private String formOfItemCode;
  private String natureOfContentsCode;
  private String governmentPublicationCode;
  private String conferencePublicationCode;
  private String bookFestschrift;
  private String bookIndexAvailabilityCode;
  private String bookLiteraryFormTypeCode;
  private String bookBiographyCode;
  private String bookMainEntryCode;
  private String cartographicReliefCode;
  private String cartographicProjectionCode;
  private String cartographicMeridianCode;
  private String cartographicNarrativeTextCode;
  private String cartographicIndexAvailabilityCode;
  private String cartographicFormatCode;
  private String computerTargetAudienceCode;
  private String computerFileTypeCode;

  private String computerFileFormCode;

  private String visualRunningTime;
  private String visualTargetAudienceCode;
  private String visualAccompanyingMaterialCode;
  private String visualMaterialTypeCode;
  private String visualTechniqueCode;
  private String serialFrequencyCode;
  private String serialRegularityCode;
  private String serialISDSCenterCode;
  private String serialTypeCode;
  private String serialFormOriginalItemCode;
  private String serialCumulativeIndexCode;
  private String serialOriginalAlphabetOfTitleCode;
  private String serialSuccessiveLatestCode;
  private String serialTitlePageExistenceCode;
  private String serialIndexAvailabilityCode;
  private String musicFormOfCompositionCode;
  private String musicFormatCode;
  private String musicTextualMaterialCode;
  private String musicLiteraryTextCode;

  private String musicPartsCode;
  private String musicTranspositionArrangementCode;

  private String cartographicMaterial;
  private String visualOriginalHolding;
  private String formOfMaterial;
  private UserViewHelper userViewHelper = new UserViewHelper();
  private String marcCountryCodeCustom;
  private String languageCodeCustom;

  public MaterialDescription() {
    super();
    setPersistenceState(new PersistenceState());
  }

  public String getMusicPartsCode() {
    return musicPartsCode;
  }

  public void setMusicPartsCode(final String musicPartsCode) {
    this.musicPartsCode = musicPartsCode;
  }

  public String getMusicTranspositionArrangementCode() {
    return musicTranspositionArrangementCode;
  }

  public void setMusicTranspositionArrangementCode(final String musicTranspositionArrangementCode) {
    this.musicTranspositionArrangementCode = musicTranspositionArrangementCode;
  }

  public String getComputerFileFormCode() {
    return computerFileFormCode;
  }

  public void setComputerFileFormCode(final String computerFileFormCode) {
    this.computerFileFormCode = computerFileFormCode;
  }

  public String getMarcCountryCodeCustom() {
    return marcCountryCodeCustom;
  }

  public void setMarcCountryCodeCustom(String marcCountryCodeCustom) {
    this.marcCountryCodeCustom = marcCountryCodeCustom;
  }

  public String getLanguageCodeCustom() {
    return languageCodeCustom;
  }

  public void setLanguageCodeCustom(String languageCodeCustom) {
    this.languageCodeCustom = languageCodeCustom;
  }

  /**
   * Squeeze all non-blank Strings to the left side of the string and retain the original length by padding with blanks on the right.
   *
   * @param s -- the input string.
   */
  private String leftJustify(final String s) {
    return ofNullable(s).map(v -> stream(s.split("")).filter(character -> !" ".equals(character)).collect(joining()))
      .map(result -> StringUtils.leftPad(result, s.length() - result.length(), ' '))
      .orElse("    ");
  }

  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setMaterialDescriptionKeyNumber(dao.getNextNumber("X0", session));
  }


  public String getDisplayString() {
    final StringBuilder sb = new StringBuilder();
    if (getMaterialDescription008Indicator().equals("1")) {
      sb.append(getEnteredOnFileDateYYMMDD())
        .append(getItemDateTypeCode())
        .append(getItemDateFirstPublication())
        .append(getItemDateLastPublication())
        .append(getMarcCountryCode());
    } else { //case tag 006
      sb.append(getMaterialTypeCode());
    }

    if (isBook()) {
      sb.append(bookDisplayString());
    } else if (isComputerFile()) {
      sb.append(computerFileDisplayString());
    } else if (isMap()) {
      sb.append(mapDisplayString());
    } else if (isMixedMaterial()) {
      sb.append(mixedMaterialDisplayString());
    } else if (isMusic()) {
      sb.append(musicDisplayString());
    } else if (isSerial()) {
      sb.append(serialDisplayString());
    } else if (isVisualMaterial()) {
      sb.append(visualMaterialDisplayString());
    }

    if (getMaterialDescription008Indicator().equals("1")) {
      sb.append(getLanguageCode())
        .append(getRecordModifiedCode())
        .append(getRecordCataloguingSourceCode());
    }

    return sb.toString();
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof MaterialDescription))
      return false;
    MaterialDescription other = (MaterialDescription) obj;
    if (other.materialDescriptionKeyNumber
      == this.materialDescriptionKeyNumber
      && other.getItemNumber() == this.getItemNumber()
      && other.getUserViewString().equals(this.getUserViewString())) {
      /*
       * For models, the key number is not set and so equality depends on
       * a) whether both tags are 008's and if not then
       * b) on correlation settings
       */
//      if (other.materialDescriptionKeyNumber > 0) {
//        return true;
//      } else {
//        if (this.getMaterialDescription008Indicator().equals("1") &&
//          other.getMaterialDescription008Indicator().equals("1")) {
//          return true;
//        }
//        return this.getHeaderType() == other.getHeaderType();
//      }
//    } else {
//      return true;
//    }
//
    }
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return materialDescriptionKeyNumber;
  }

  public boolean isBook() {
    return getFormOfMaterial().equals("bk");
  }

  public boolean isMap() {
    return getFormOfMaterial().equals("cm");
  }

  public boolean isComputerFile() {
    return getFormOfMaterial().equals("cf");
  }

  public boolean isMixedMaterial() {
    return getFormOfMaterial().equals("mm");
  }

  public boolean isMusic() {
    return getFormOfMaterial().equals("msr");
  }

  public boolean isSerial() {
    return getFormOfMaterial().equals("se");
  }

  public boolean isVisualMaterial() {
    return getFormOfMaterial().equals("vm");
  }

  /**
   * @return the displayString segment for book material.
   */
  public String bookDisplayString() {
    return getBookIllustrationCode()
      + getTargetAudienceCode()
      + getFormOfItemCode()
      + getNatureOfContentsCode()
      + getGovernmentPublicationCode()
      + getConferencePublicationCode()
      + getBookFestschrift()
      + getBookIndexAvailabilityCode()
      + " " //undefined position (formerly main entry indicator)
      + getBookLiteraryFormTypeCode() + getBookBiographyCode();
  }

  /**
   * @return the displayString segment for map material.
   */
  public String mapDisplayString() {
    return getCartographicReliefCode()
      + getCartographicProjectionCode()
      + " " //undefined
      + getCartographicMaterial() + "  " // two undefined
      + getGovernmentPublicationCode() + getFormOfItemCode() + " " // undefined
      + getCartographicIndexAvailabilityCode() + " " //undefined position
      + getCartographicFormatCode();
  }

  /**
   * @return the displayString segment for computer file material
   */
  public String computerFileDisplayString() {
    StringBuilder builder = new StringBuilder();
    builder.append("    ")                /*18-21 - Undefined             */
      .append(getComputerTargetAudienceCode())    /*22    - Target audience       */
      .append(getComputerFileFormCode())      /*23    - Form of item          */
      .append("  ")                /*24-25 - Undefined             */
      .append(getComputerFileTypeCode())      /*26    - Type of computer file */
      .append(" ")                  /*27    - Undefined             */
      .append(getGovernmentPublicationCode())    /*28    - Government publication*/
      .append("      ");              /*29-34 - Undefined             */

    return builder.toString();
  }

  /**
   * @return the displayString segment for mixed material material
   */
  public String mixedMaterialDisplayString() {
    // five undefined + 11 undefined positions
    return "     " + getFormOfItemCode() + "           ";
  }

  /**
   * @return the displayString segment for music material
   */
  public String musicDisplayString() {
    StringBuilder builder = new StringBuilder();
    builder.append(getMusicFormOfCompositionCode())    /* 18-19 - Form of composition */
      .append(getMusicFormatCode())          /* 20 - Format of music */
      .append(getMusicPartsCode())          /* 21 - Music parts  */
      .append(getTargetAudienceCode())        /* 22 - Target audience */
      .append(getFormOfItemCode())          /* 23 - Form of item */
      .append(getMusicTextualMaterialCode())      /* 24-29 - Accompanying matter */
      .append(getMusicLiteraryTextCode())        /* 30-31 - Literary text for sound recordings */
      .append(" ")                  /* 32 - Undefined */
      .append(getMusicTranspositionArrangementCode())  /* 33 - Transposition and arrangement */
      .append(" ");                  /* 34 - Undefined  */

    return builder.toString();
  }

  /**
   * @return the displayString segment for visual material
   */
  public String visualMaterialDisplayString() {
    return getVisualRunningTime() + " " // undefined position
      + getVisualTargetAudienceCode() + "     " // five undefined
      + getGovernmentPublicationCode() + getFormOfItemCode() + "   " // three undefined
      + getVisualMaterialTypeCode() + getVisualTechniqueCode();
  }

  /**
   * @return the displayString segment for serial (continuing resources) material
   */
  public String serialDisplayString() {
    StringBuilder builder = new StringBuilder();
    builder
      .append(getSerialFrequencyCode())        /* 18 - Frequency */
      .append(getSerialRegularityCode())        /* 19 - Regularity */
      .append(" ")                    /* 20 - Undefined */
      .append(getSerialTypeCode())            /* 21 - Type of continuing resource */
      .append(getSerialFormOriginalItemCode())      /* 22 - Form of original item */
      .append(getFormOfItemCode())            /* 23 - Form of item */
      .append(getNatureOfContentsCode())        /* 24 - Nature of entire work 25-27 - Nature of contents */
      .append(getGovernmentPublicationCode())      /* 28 - Government publication */
      .append(getConferencePublicationCode())      /* 29 - Conference publication */
      .append("   ")                  /* 30-32 - Undefined  */
      .append(getSerialOriginalAlphabetOfTitleCode())  /* 33 - Original alphabet or script of title */
      .append(getSerialSuccessiveLatestCode());    /* 34 - Entry convention */

    return builder.toString();
  }

  public int getMaterialDescriptionKeyNumber() {
    return materialDescriptionKeyNumber;
  }

  /**
   * GETTERS AND SETTERS *
   **/

  public void setMaterialDescriptionKeyNumber(int integer) {
    materialDescriptionKeyNumber = integer;
  }

  public String getBookBiographyCode() {
    return bookBiographyCode;
  }

  public void setBookBiographyCode(final String value) {
    bookBiographyCode = value;
  }

  public String getBookFestschrift() {
    return bookFestschrift;
  }

  public void setBookFestschrift(final String value) {
    bookFestschrift = value;
  }

  public String getBookIllustrationCode() {
    return bookIllustrationCode;
  }

  public void setBookIllustrationCode(final String value) {
    bookIllustrationCode = leftJustify(value);
  }

  public char[] getBookIllustrationChar() {
    if (getBookIllustrationCode() == null) {
      return null;
    } else {
      return getBookIllustrationCode().toCharArray();
    }
  }

  public String getBookIndexAvailabilityCode() {
    return bookIndexAvailabilityCode;
  }

  public void setBookIndexAvailabilityCode(final String value) {
    bookIndexAvailabilityCode = value;
  }

  public String getBookLiteraryFormTypeCode() {
    return bookLiteraryFormTypeCode;
  }

  public void setBookLiteraryFormTypeCode(final String value) {
    bookLiteraryFormTypeCode = value;
  }

  public String getBookMainEntryCode() {
    return bookMainEntryCode;
  }

  public void setBookMainEntryCode(final String value) {
    bookMainEntryCode = value;
  }

  public String getCartographicFormatCode() {
    return cartographicFormatCode;
  }

  public void setCartographicFormatCode(final String s) {
    cartographicFormatCode = s;
  }

  public char[] getCartographicFormatChar() {
    if (cartographicFormatCode == null) {
      return null;
    } else {
      return cartographicFormatCode.toCharArray();
    }
  }

  public String getCartographicIndexAvailabilityCode() {
    return cartographicIndexAvailabilityCode;
  }

  public void setCartographicIndexAvailabilityCode(final String value) {
    cartographicIndexAvailabilityCode = value;
  }

  public String getCartographicMaterial() {
    return cartographicMaterial;
  }

  public void setCartographicMaterial(final String c) {
    cartographicMaterial = c;
  }

  public String getCartographicMeridianCode() {
    return cartographicMeridianCode;
  }

  public void setCartographicMeridianCode(final String string) {
    cartographicMeridianCode = string;
  }

  public String getCartographicNarrativeTextCode() {
    return cartographicNarrativeTextCode;
  }

  public void setCartographicNarrativeTextCode(final String value) {
    cartographicNarrativeTextCode = value;
  }

  public String getCartographicProjectionCode() {
    return cartographicProjectionCode;
  }

  public void setCartographicProjectionCode(final String string) {
    cartographicProjectionCode = string;
  }

  public String getCartographicReliefCode() {
    return cartographicReliefCode;
  }

  public void setCartographicReliefCode(final String s) {
    cartographicReliefCode = s;
  }

  public char[] getCartographicReliefChar() {
    if (cartographicReliefCode == null) {
      return null;
    } else {
      return cartographicReliefCode.toCharArray();
    }
  }

  public String getComputerFileTypeCode() {
    return computerFileTypeCode;
  }

  public void setComputerFileTypeCode(final String value) {
    computerFileTypeCode = value;
  }

  public String getComputerTargetAudienceCode() {
    return computerTargetAudienceCode;
  }

  public void setComputerTargetAudienceCode(final String value) {
    computerTargetAudienceCode = value;
  }

  public String getConferencePublicationCode() {
    return conferencePublicationCode;
  }

  public void setConferencePublicationCode(final String value) {
    conferencePublicationCode = value;
  }

  public String getFormOfItemCode() {
    return formOfItemCode;
  }

  public void setFormOfItemCode(final String value) {
    formOfItemCode = value;
  }

  public String getGovernmentPublicationCode() {
    return governmentPublicationCode;
  }

  public void setGovernmentPublicationCode(final String value) {
    governmentPublicationCode = value;
  }

  public String getMaterialDescription008Indicator() {
    return materialDescription008Indicator;
  }

  public void setMaterialDescription008Indicator(final String c) {
    materialDescription008Indicator = c;
  }

  public String getMaterialTypeCode() {
    return materialTypeCode;
  }

  public void setMaterialTypeCode(final String value) {
    materialTypeCode = value;
  }

  public String getMusicFormatCode() {
    return musicFormatCode;
  }

  public void setMusicFormatCode(final String value) {
    musicFormatCode = value;
  }

  public String getMusicFormOfCompositionCode() {
    return musicFormOfCompositionCode;
  }

  public void setMusicFormOfCompositionCode(final String string) {
    musicFormOfCompositionCode = string;
  }

  public String getMusicLiteraryTextCode() {
    return musicLiteraryTextCode;
  }

  public void setMusicLiteraryTextCode(final String string) {
    musicLiteraryTextCode = string;
  }

  public char[] getMusicLiteraryTextChar() {
    if (musicLiteraryTextCode == null) {
      return null;
    } else {
      return musicLiteraryTextCode.toCharArray();
    }
  }

  public String getMusicTextualMaterialCode() {
    return musicTextualMaterialCode;
  }

  public void setMusicTextualMaterialCode(final String string) {
    musicTextualMaterialCode = string;
  }

  public char[] getMusicTextualMaterialChar() {
    if (musicTextualMaterialCode == null) {
      return null;
    } else {
      return musicTextualMaterialCode.toCharArray();
    }
  }

  public String getNatureOfContentsCode() {
    return natureOfContentsCode;
  }

  public void setNatureOfContentsCode(final String string) {
    natureOfContentsCode = leftJustify(string);
  }

  public char[] getNatureOfContentsChar() {
    if (natureOfContentsCode == null) {
      return null;
    } else {
      return natureOfContentsCode.toCharArray();
    }
  }

  public String getSerialCumulativeIndexCode() {
    return serialCumulativeIndexCode;
  }

  public void setSerialCumulativeIndexCode(final String value) {
    serialCumulativeIndexCode = value;
  }

  public String getSerialFormOriginalItemCode() {
    return serialFormOriginalItemCode;
  }

  public void setSerialFormOriginalItemCode(final String value) {
    serialFormOriginalItemCode = value;
  }

  public String getSerialFrequencyCode() {
    return serialFrequencyCode;
  }

  public void setSerialFrequencyCode(final String value) {
    serialFrequencyCode = value;
  }

  public String getSerialIndexAvailabilityCode() {
    return serialIndexAvailabilityCode;
  }

  public void setSerialIndexAvailabilityCode(final String value) {
    serialIndexAvailabilityCode = value;
  }

  public String getSerialISDSCenterCode() {
    return serialISDSCenterCode;
  }

  public void setSerialISDSCenterCode(final String value) {
    serialISDSCenterCode = value;
  }

  public String getSerialRegularityCode() {
    return serialRegularityCode;
  }

  public void setSerialRegularityCode(final String value) {
    serialRegularityCode = value;
  }

  public String getSerialSuccessiveLatestCode() {
    return serialSuccessiveLatestCode;
  }

  public void setSerialSuccessiveLatestCode(final String value) {
    serialSuccessiveLatestCode = value;
  }

  public String getSerialOriginalAlphabetOfTitleCode() {
    return serialOriginalAlphabetOfTitleCode;
  }

  public void setSerialOriginalAlphabetOfTitleCode(final String value) {
    serialOriginalAlphabetOfTitleCode = value;
  }

  public String getSerialTitlePageExistenceCode() {
    return serialTitlePageExistenceCode;
  }

  public void setSerialTitlePageExistenceCode(final String value) {
    serialTitlePageExistenceCode = value;
  }

  public String getSerialTypeCode() {
    return serialTypeCode;
  }

  public void setSerialTypeCode(final String value) {
    serialTypeCode = value;
  }

  public String getTargetAudienceCode() {
    return targetAudienceCode;
  }

  public void setTargetAudienceCode(final String value) {
    targetAudienceCode = value;
  }

  public String getVisualAccompanyingMaterialCode() {
    return visualAccompanyingMaterialCode;
  }

  public void setVisualAccompanyingMaterialCode(final String string) {
    visualAccompanyingMaterialCode = string;
  }

  public String getVisualMaterialTypeCode() {
    return visualMaterialTypeCode;
  }

  public void setVisualMaterialTypeCode(final String value) {
    visualMaterialTypeCode = value;
  }

  public String getVisualOriginalHolding() {
    return visualOriginalHolding;
  }

  public void setVisualOriginalHolding(final String value) {
    visualOriginalHolding = value;
  }

  public String getVisualRunningTime() {
    return visualRunningTime;
  }

  public void setVisualRunningTime(final String string) {
    visualRunningTime = string;
  }

  public String getVisualTargetAudienceCode() {
    return visualTargetAudienceCode;
  }

  public void setVisualTargetAudienceCode(final String value) {
    visualTargetAudienceCode = value;
  }

  public String getVisualTechniqueCode() {
    return visualTechniqueCode;
  }

  public void setVisualTechniqueCode(final String value) {
    visualTechniqueCode = value;
  }


  /**
   * @since 1.0
   */
  public String getFormOfMaterial() {
    return formOfMaterial;
  }

  /**
   * @since 1.0
   */
  public void setFormOfMaterial(final String string) {
    formOfMaterial = string;
  }

  /* (non-Javadoc)
   * @see FixedField#setBibItm(BIB_ITM)
   */
  public void setItemEntity(final ItemEntity item) {
    super.setItemEntity(item);
  }

  /* (non-Javadoc)
   * @see FixedField#setBibHeader(short)
   */
  public void setHeaderType(final int s) {
    super.setHeaderType(s);
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
   */
  public boolean isAbleToBeDeleted() {
    return !getMaterialDescription008Indicator().equals("1");
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return v.isValueDefined(1)
      && ((v.getValue(1) < 16)
      || (v.getValue(1) > 37)
      || ((v.getValue(1) > 22) && (v.getValue(1) < 31)));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    setHeaderType(v.getValue(1));
    super.setCorrelationValues(v);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#getDAO()
   */
  public AbstractDAO getDAO() {
    return getPersistenceState().getDAO();
  }

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
		/*if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			if (getMaterialDescription008Indicator() == '1') {
				content.setAttribute("enteredOnFileDateYYYYMMDD", getEnteredOnFileDateYYYYMMDD());
				content.setAttribute("itemDateTypeCode","" + getItemDateTypeCode());
				content.setAttribute("ITEM_DATE_FIRST_PUBLICATION", getItemDateFirstPublication());
				content.setAttribute("ITEM_DATE_LAST_PUBLICATION",	getItemDateLastPublication());
				content.setAttribute("marcCountryCode", getMarcCountryCode());
				content.setAttribute("LANGUAGE_CODE", getLanguageCode());
				content.setAttribute("recordModifiedCode","" + getRecordModifiedCode());
				content.setAttribute("recordCataloguingSourceCode","" + getRecordCataloguingSourceCode());
			}
			content.setAttribute("MATERIAL_TYPE_CODE", asNullableString(getMaterialTypeCode()));
			content.setAttribute("materialDescription008Indicator","" + getMaterialDescription008Indicator());
			content.setAttribute("bookIllustrationCode",getBookIllustrationCode());
			content.setAttribute("targetAudienceCode", asNullableString(getTargetAudienceCode()));
			content.setAttribute("FORM_OF_ITEM_CODE", asNullableString(getFormOfItemCode()));
			content.setAttribute("natureOfContentsCode",getNatureOfContentsCode());
			content.setAttribute("governmentPublicationCode", asNullableString(getGovernmentPublicationCode()));
			content.setAttribute("conferencePublicationCode", asNullableString(getConferencePublicationCode()));
			content.setAttribute("bookFestschrift", asNullableString(getBookFestschrift()));
			content.setAttribute("bookIndexAvailabilityCode", asNullableString(getBookIndexAvailabilityCode()));
			content.setAttribute("bookLiteraryFormTypeCode", asNullableString(getBookLiteraryFormTypeCode()));
			content.setAttribute("bookBiographyCode", asNullableString(getBookBiographyCode()));
			content.setAttribute("bookMainEntryCode", asNullableString(getBookMainEntryCode()));
			content.setAttribute("cartographicReliefCode",getCartographicReliefCode());
			content.setAttribute("cartographicProjectionCode",getCartographicProjectionCode());
			content.setAttribute("cartographicMeridianCode",getCartographicMeridianCode());
			content.setAttribute("cartographicNarrativeTextCode", asNullableString(getCartographicNarrativeTextCode()));
			content.setAttribute("cartographicIndexAvailabilityCode", asNullableString(getCartographicIndexAvailabilityCode()));
			content.setAttribute("cartographicFormatCode",getCartographicFormatCode());
			content.setAttribute("computerTargetAudienceCode", asNullableString(getComputerTargetAudienceCode()));
			content.setAttribute("computerFileTypeCode", asNullableString(getComputerFileTypeCode()));
			content.setAttribute("computerFileFormCode", asNullableString(getComputerFileFormCode()));
			content.setAttribute("visualRunningTime", getVisualRunningTime());
			content.setAttribute("visualTargetAudienceCode", asNullableString(getVisualTargetAudienceCode()));
			content.setAttribute("visualAccompanyingMaterialCode",getVisualAccompanyingMaterialCode());
			content.setAttribute("visualMaterialTypeCode", asNullableString(getVisualMaterialTypeCode()));
			content.setAttribute("visualTechniqueCode", asNullableString(getVisualTechniqueCode()));
			content.setAttribute("visualTechniqueCode",	asNullableString(getVisualTechniqueCode()));
			content.setAttribute("serialFrequencyCode", asNullableString(getSerialFrequencyCode()));
			content.setAttribute("serialRegularityCode", asNullableString(getSerialRegularityCode()));
			content.setAttribute("serialISDSCenterCode", asNullableString(getSerialISDSCenterCode()));
			content.setAttribute("serialTypeCode", asNullableString(getSerialTypeCode()));
			content.setAttribute("serialFormOriginalItemCode", asNullableString(getSerialFormOriginalItemCode()));
			content.setAttribute("serialCumulativeIndexCode", asNullableString(getSerialCumulativeIndexCode()));
			content.setAttribute("serialOriginalAlphabetOfTitleCode", asNullableString(getSerialOriginalAlphabetOfTitleCode()));
			content.setAttribute("serialSuccessiveLatestCode", asNullableString(getSerialSuccessiveLatestCode()));
			content.setAttribute("serialTitlePageExistenceCode", asNullableString(getSerialTitlePageExistenceCode()));
			content.setAttribute("serialIndexAvailabilityCode", asNullableString(getSerialIndexAvailabilityCode()));
			content.setAttribute("musicFormOfCompositionCode",getMusicFormOfCompositionCode());
			content.setAttribute("musicFormatCode", asNullableString(getMusicFormatCode()));
			content.setAttribute("musicTextualMaterialCode",getMusicTextualMaterialCode());
			content.setAttribute("musicLiteraryTextCode",getMusicLiteraryTextCode());
			content.setAttribute("musicPartsCode", asNullableString(getMusicPartsCode()));
			content.setAttribute("musicTranspositionArrangementCode", asNullableString(getMusicTranspositionArrangementCode()));
			content.setAttribute("cartographicMaterial", asNullableString(getCartographicMaterial()));
			content.setAttribute("visualOriginalHolding", asNullableString(getVisualOriginalHolding()));
			content.setAttribute("formOfMaterial", getFormOfMaterial());
		}*/
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
		/*Element content = (Element) xmlElement.getChildNodes().item(0);
		setMaterialTypeCode(characterFromXML(content.getAttribute("MATERIAL_TYPE_CODE")));
		setMaterialDescription008Indicator(content.getAttribute("materialDescription008Indicator").charAt(0));
		setBookIllustrationCode(stringFromXML(content.getAttribute("bookIllustrationCode")));
		setTargetAudienceCode(characterFromXML(content.getAttribute("targetAudienceCode")));
		setFormOfItemCode(characterFromXML(content.getAttribute("FORM_OF_ITEM_CODE")));
		setNatureOfContentsCode(stringFromXML(content.getAttribute("natureOfContentsCode")));
		setGovernmentPublicationCode(characterFromXML(content.getAttribute("governmentPublicationCode")));
		setConferencePublicationCode(characterFromXML(content.getAttribute("conferencePublicationCode")));
		setBookFestschrift(characterFromXML(content.getAttribute("bookFestschrift")));
		setBookIndexAvailabilityCode(characterFromXML(content.getAttribute("bookIndexAvailabilityCode")));
		setBookLiteraryFormTypeCode(characterFromXML(content.getAttribute("bookLiteraryFormTypeCode")));
		setBookBiographyCode(characterFromXML(content.getAttribute("bookBiographyCode")));
		setBookMainEntryCode(characterFromXML(content.getAttribute("bookMainEntryCode")));
		setCartographicReliefCode(stringFromXML(content.getAttribute("cartographicReliefCode")));
		setCartographicProjectionCode(stringFromXML(content.getAttribute("cartographicProjectionCode")));
		setCartographicMeridianCode(stringFromXML(content.getAttribute("cartographicMeridianCode")));
		setCartographicNarrativeTextCode(characterFromXML(content.getAttribute("cartographicNarrativeTextCode")));
		setCartographicIndexAvailabilityCode(characterFromXML(content.getAttribute("cartographicIndexAvailabilityCode")));
		setCartographicFormatCode(stringFromXML(content.getAttribute("cartographicFormatCode")));
		setComputerTargetAudienceCode(characterFromXML(content.getAttribute("computerTargetAudienceCode")));
		setComputerFileTypeCode(characterFromXML(content.getAttribute("computerFileTypeCode")));

		setComputerFileFormCode(characterFromXML(content.getAttribute("computerFileFormCode")));

		setVisualRunningTime(stringFromXML(content.getAttribute("visualRunningTime")));
		setVisualTargetAudienceCode(characterFromXML(content.getAttribute("visualTargetAudienceCode")));
		setVisualAccompanyingMaterialCode(stringFromXML(content.getAttribute("visualAccompanyingMaterialCode")));
		setVisualMaterialTypeCode(characterFromXML(content.getAttribute("visualMaterialTypeCode")));
		setVisualTechniqueCode(characterFromXML(content.getAttribute("visualTechniqueCode")));
		setSerialFrequencyCode(characterFromXML(content.getAttribute("serialFrequencyCode")));
		setSerialRegularityCode(characterFromXML(content.getAttribute("serialRegularityCode")));
		setSerialISDSCenterCode(characterFromXML(content.getAttribute("serialISDSCenterCode")));
		setSerialFormOriginalItemCode(characterFromXML(content.getAttribute("serialFormOriginalItemCode")));
		setSerialFormOriginalItemCode(characterFromXML(content.getAttribute("serialFormOriginalItemCode")));
		setSerialOriginalAlphabetOfTitleCode(characterFromXML(content.getAttribute("serialOriginalAlphabetOfTitleCode")));
		setSerialOriginalAlphabetOfTitleCode(characterFromXML(content.getAttribute("serialOriginalAlphabetOfTitleCode")));
		setSerialSuccessiveLatestCode(characterFromXML(content.getAttribute("serialSuccessiveLatestCode")));
		setSerialTitlePageExistenceCode(characterFromXML(content.getAttribute("serialTitlePageExistenceCode")));
		setSerialIndexAvailabilityCode(characterFromXML(content.getAttribute("serialIndexAvailabilityCode")));
		setMusicFormOfCompositionCode(stringFromXML(content.getAttribute("musicFormOfCompositionCode")));
		setMusicFormatCode(characterFromXML(content.getAttribute("musicFormatCode")));
		setMusicTextualMaterialCode(stringFromXML(content.getAttribute("musicTextualMaterialCode")));
		setMusicLiteraryTextCode(stringFromXML(content.getAttribute("musicLiteraryTextCode")));


		setMusicPartsCode(characterFromXML(content.getAttribute("musicPartsCode")));
		setMusicTranspositionArrangementCode(characterFromXML(content.getAttribute("musicTranspositionArrangementCode")));

		setCartographicMaterial(characterFromXML(content.getAttribute("cartographicMaterial")));
		setVisualOriginalHolding(characterFromXML(content.getAttribute("visualOriginalHolding")));
		setFormOfMaterial(stringFromXML(content.getAttribute("formOfMaterial")));
		if (getMaterialDescription008Indicator() == '1') {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			Date date =
				formatter.parse(content.getAttribute("enteredOnFileDateYYYYMMDD"),new ParsePosition(0));
			setEnteredOnFileDate(date);
			setItemDateTypeCode(content.getAttribute("itemDateTypeCode").charAt(0));
			setItemDateFirstPublication(content.getAttribute("ITEM_DATE_FIRST_PUBLICATION"));
			setItemDateLastPublication(content.getAttribute("ITEM_DATE_LAST_PUBLICATION"));
			setMarcCountryCode(content.getAttribute("marcCountryCode"));
			setLanguageCode(content.getAttribute("LANGUAGE_CODE"));
			setRecordModifiedCode(content.getAttribute("recordModifiedCode").charAt(0));
			setRecordCataloguingSourceCode(content.getAttribute("recordCataloguingSourceCode").charAt(0));
		}*/
  }

  public String getUserViewString() {
    return userViewHelper.getUserViewString();
  }

  public void setUserViewString(final String string) {
    userViewHelper.setUserViewString(string);
  }

  public int getBibItemNumber() {
    return getItemNumber();
  }

  public void setBibItemNumber(int i) {
    setItemNumber(i);
  }

  private BIB_ITM getBibItm() {
    return (BIB_ITM) getItemEntity();
  }

  public Date getEnteredOnFileDate() {
    return getBibItm().getEnteredOnFileDate();
  }

  public void setEnteredOnFileDate(Date date) {
    getBibItm().setEnteredOnFileDate(date);
  }

  public String getEnteredOnFileDateYYMMDD() {
    return getBibItm().getEnteredOnFileDateYYMMDD();
  }

  public String getEnteredOnFileDateYYYYMMDD() {
    return getBibItm().getEnteredOnFileDateYYYYMMDD();
  }

  public String getItemDateFirstPublication() {
    return getBibItm().getItemDateFirstPublication();
  }

  public void setItemDateFirstPublication(String string) {
    if (string == null || string.trim().length() == 0) {
      string = DEFAULT_DATE;
    }
    getBibItm().setItemDateFirstPublication(string);
  }

  public String getItemDateLastPublication() {
    return getBibItm().getItemDateLastPublication();
  }

  public void setItemDateLastPublication(String string) {
    if (string == null || string.trim().length() == 0) {
      string = DEFAULT_DATE;
    }
    getBibItm().setItemDateLastPublication(string);
  }

  public String getItemDateFirstPublicationDisplay() {
    return getBibItm().getItemDateFirstPublication().trim();
  }

  public String getItemDateLastPublicationDisplay() {
    return getBibItm().getItemDateLastPublication().trim();
  }

  public char getItemDateTypeCode() {
    return getBibItm().getItemDateTypeCode();
  }

  public void setItemDateTypeCode(final char c) {
    getBibItm().setItemDateTypeCode(c);
  }

  public String getMarcCountryCode() {
    return getBibItm().getMarcCountryCode();
  }

  public void setMarcCountryCode(final String string) {
    getBibItm().setMarcCountryCode(string);
  }

  public String getLanguageCode() {
    return getBibItm().getLanguageCode();
  }

  public void setLanguageCode(final String string) {
    getBibItm().setLanguageCode(string);
  }

  public char getRecordCataloguingSourceCode() {
    return getBibItm().getRecordCataloguingSourceCode();
  }

  public void setRecordCataloguingSourceCode(final char c) {
    getBibItm().setRecordCataloguingSourceCode(c);
  }

  public char getRecordModifiedCode() {
    return getBibItm().getRecordModifiedCode();
  }

  public void setRecordModifiedCode(final char c) {
    getBibItm().setRecordModifiedCode(c);
  }

  public char getItemBibliographicLevelCode() {
    return getBibItm().getItemBibliographicLevelCode();
  }

  public void setItemBibliographicLevelCode(final char c) {
    getBibItm().setItemBibliographicLevelCode(c);
  }

  public char getItemRecordTypeCode() {
    return getBibItm().getItemRecordTypeCode();
  }

  public void setItemRecordTypeCode(final char c) {
    getBibItm().setItemRecordTypeCode(c);
  }

  @Deprecated
  public List getFirstCorrelationList() throws DataAccessException {
		/*if(getMaterialDescription008Indicator() == '1')
		  return daoCodeTable.getListFromTag008(T_BIB_HDR.class,"008",false);
		else
		  return daoCodeTable.getListFromWithoutTag008(T_BIB_HDR.class,"008",false);*/

    return null;
  }

  //@paulm, us_bbl_loading
  @Override
  public void setContentFromMarcString(String str) {
    if (getMaterialDescription008Indicator().equals("1")) {
      DateFormat df = new SimpleDateFormat("yyMMdd");
      try {
        getBibItm().setEnteredOnFileDate(df.parse(str.substring(0, 6)));
      } catch (ParseException e) {
        // date not set if parse error
      }
      setItemDateTypeCode(str.charAt(6));
      setItemDateFirstPublication(str.substring(7, 11));
      setItemDateLastPublication(str.substring(11, 15));
      setMarcCountryCode(str.substring(15, 18));
      setLanguageCode(str.substring(35, 38));
      setRecordModifiedCode(str.charAt(38));
      setRecordCataloguingSourceCode(str.charAt(39));
      str = str.substring(17, 35);
    } else {
      setMaterialTypeCode(String.valueOf(str.charAt(0)));
    }

    if (isBook()) {
      setBookIllustrationCode(str.substring(1, 5));
      setTargetAudienceCode(String.valueOf(str.charAt(5)));
      setFormOfItemCode(String.valueOf(str.charAt(6)));
      setNatureOfContentsCode(str.substring(7, 11));
      setGovernmentPublicationCode(String.valueOf(str.charAt(11)));
      setConferencePublicationCode(String.valueOf(str.charAt(12)));
      setBookFestschrift(String.valueOf(str.charAt(13)));
      setBookIndexAvailabilityCode(String.valueOf(str.charAt(14)));
      setBookLiteraryFormTypeCode(String.valueOf(str.charAt(16)));
      setBookBiographyCode(String.valueOf(str.charAt(17)));
    } else if (isComputerFile()) {
      setComputerTargetAudienceCode(String.valueOf(str.charAt(5)));
      setComputerFileFormCode(String.valueOf(str.charAt(6)));
      setComputerFileTypeCode(String.valueOf(str.charAt(9)));
      setGovernmentPublicationCode(String.valueOf(str.charAt(11)));
    } else if (isMap()) {
      setCartographicReliefCode(str.substring(1, 5));
      setCartographicProjectionCode(str.substring(5, 7));
      setCartographicMaterial(String.valueOf(str.charAt(8)));
      setGovernmentPublicationCode(String.valueOf(str.charAt(11)));
      setFormOfItemCode(String.valueOf(str.charAt(12)));
      setCartographicIndexAvailabilityCode(String.valueOf(str.charAt(14)));
      setCartographicFormatCode(str.substring(16, 18));
    } else if (isMixedMaterial()) {
      setFormOfItemCode(String.valueOf(str.charAt(6)));
    } else if (isMusic()) {
      setMusicFormOfCompositionCode(str.substring(1, 3));
      setMusicFormatCode(String.valueOf(str.charAt(3)));
      setMusicPartsCode(String.valueOf(str.charAt(4)));
      setTargetAudienceCode(String.valueOf(str.charAt(5)));
      setFormOfItemCode(String.valueOf(str.charAt(6)));
      setMusicTextualMaterialCode(str.substring(7, 13));
      setMusicLiteraryTextCode(str.substring(13, 15));
      setMusicTranspositionArrangementCode(String.valueOf(str.charAt(16)));
    } else if (isSerial()) {
      setSerialFrequencyCode(String.valueOf(str.charAt(1)));
      setSerialRegularityCode(String.valueOf(str.charAt(2)));
      setSerialTypeCode(String.valueOf(str.charAt(4)));
      setSerialFormOriginalItemCode(String.valueOf(str.charAt(5)));
      setFormOfItemCode(String.valueOf(str.charAt(6)));
      setNatureOfContentsCode(str.substring(7, 11));
      setGovernmentPublicationCode(String.valueOf(str.charAt(11)));
      setConferencePublicationCode(String.valueOf(str.charAt(12)));
      setSerialOriginalAlphabetOfTitleCode(String.valueOf(str.charAt(16)));
      setSerialSuccessiveLatestCode(String.valueOf(str.charAt(17)));
    } else if (isVisualMaterial()) {
      setVisualRunningTime(str.substring(1, 4));
      setVisualTargetAudienceCode(String.valueOf(str.charAt(5)));
      setGovernmentPublicationCode(String.valueOf(str.charAt(11)));
      setFormOfItemCode(String.valueOf(str.charAt(12)));
      setVisualMaterialTypeCode(String.valueOf(str.charAt(16)));
      setVisualTechniqueCode(String.valueOf(str.charAt(17)));
    }
  }
}
