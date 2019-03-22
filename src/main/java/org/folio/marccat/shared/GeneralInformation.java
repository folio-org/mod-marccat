package org.folio.marccat.shared;

import org.apache.commons.lang.StringUtils;
import org.folio.marccat.config.log.Global;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

/**
 * Class related to:
 *
 * <ul>
 * <li>008 - Fixed-Length Data Elements</li>
 * </li>006 - Fixed-Length Data Elements-Additional Material Characteristics</li>
 * </ul>
 * It contains only methods of logic to create tag 008 and 006 default values.
 *
 * @author nbianchini
 * @since 1.0
 */
public class GeneralInformation {
  private int headerType;
  private String enteredOnFileDateYYMMDD;
  private String itemDateTypeCode;
  private String itemDateFirstPublication;
  private String itemDateLastPublication;
  private String recordModifiedCode;
  private String recordCataloguingSourceCode;
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
  private String serialEntryConventionCode;
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

  private String marcCountryCode;
  private String languageCode;

  public GeneralInformation() {}

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

  /**
   * Sets the configuration values to manage defaults.
   *
   * @param configuration the map that contains configuration values to set.
   */
  public void setDefaultValues(final Map<String, String> configuration) {
	final String audienceMaterial = "material.targetAudienceCode";
    setRecordModifiedCode(" ");
    setRecordCataloguingSourceCode(configuration.get("bibliographicItem.recordCataloguingSourceCode"));
    setItemDateTypeCode(configuration.get("bibliographicItem.itemDateTypeCode"));
    setLanguageCode(configuration.get("bibliographicItem.languageCode"));
    setItemDateFirstPublication(Global.ITEM_DATE_FIRST_PUBLICATION);
    setItemDateLastPublication(Global.ITEM_DATE_LAST_PUBLICATION);
    setMarcCountryCode(configuration.get("bibliographicItem.marcCountryCode"));

    setBookIllustrationCode(configuration.get("material.bookIllustrationCode"));
    setTargetAudienceCode(configuration.get(audienceMaterial));
    setFormOfItemCode(configuration.get("material.formOfItemCode"));
    setNatureOfContentsCode(configuration.get("material.natureOfContentsCode"));
    setConferencePublicationCode(configuration.get("material.conferencePublicationCode"));
    setBookFestschrift(configuration.get("material.bookFestschrift"));
    setBookIndexAvailabilityCode(configuration.get("material.bookIndexAvailabilityCode"));
    setBookLiteraryFormTypeCode(configuration.get("material.bookLiteraryFormTypeCode"));
    setBookBiographyCode(configuration.get("material.bookBiographyCode"));
    setGovernmentPublicationCode(configuration.get("material.governmentPublicationCode"));
    setComputerTargetAudienceCode(configuration.get(audienceMaterial));
    setComputerFileTypeCode(configuration.get("material.computerFileTypeCode"));
    setComputerFileFormCode(configuration.get("material.computerFileFormCode"));
    setCartographicIndexAvailabilityCode("0");
    setCartographicReliefCode(configuration.get("material.cartographicReliefCode"));
    setCartographicProjectionCode(configuration.get("material.cartographicProjectionCode"));
    setCartographicMaterial(configuration.get("material.cartographicMaterial"));
    setCartographicFormatCode(configuration.get("material.cartographicFormatCode"));
    setMusicFormOfCompositionCode(configuration.get("material.musicFormOfCompositionCode"));
    setMusicFormatCode(configuration.get("material.musicFormatCode"));
    setMusicTextualMaterialCode(configuration.get("material.musicTextualMaterialCode"));
    setMusicLiteraryTextCode(configuration.get("material.musicLiteraryTextCode"));
    setMusicPartsCode(configuration.get("material.musicPartsCode"));
    setMusicTranspositionArrangementCode(configuration.get("material.musicTranspositionArrangementCode"));
    setSerialFrequencyCode(configuration.get("material.serialFrequencyCode"));
    setSerialRegularityCode(configuration.get("material.serialRegularityCode"));
    setSerialTypeCode(configuration.get("material.serialTypeCode"));
    setSerialFormOriginalItemCode(configuration.get("material.formOfItemCode"));
    setSerialOriginalAlphabetOfTitleCode(configuration.get("material.serialOriginalAlphabetOfTitleCode"));
    setSerialEntryConventionCode(configuration.get("material.serialSuccessiveLatestCode"));
    setVisualRunningTime(configuration.get("material.visualRunningTime"));
    setVisualTargetAudienceCode(configuration.get(audienceMaterial));
    setVisualMaterialTypeCode(configuration.get("material.visualMaterialTypeCode"));
    setVisualTechniqueCode(configuration.get("material.visualTechniqueCode"));
  }

  /**
   * Makes field string value with default values.
   *
   * @return a string representing the field string value.
   */
  public String getValueString() {
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


  /**
   * Checks if is a book type.
   *
   * @return true if is a book type false otherwise.
   */
  public boolean isBook() {
    return "bk".equals(getFormOfMaterial());
  }

  /**
   * Checks if is a map type.
   *
   * @return true if is a map type false otherwise.
   */
  public boolean isMap() {
    return "cm".equals(getFormOfMaterial());
  }

  /**
   * Checks if is a computer file type.
   *
   * @return true if is a computer file type false otherwise.
   */
  public boolean isComputerFile() {
    return "cf".equals(getFormOfMaterial());
  }

  /**
   * Checks if is a mixed material type.
   *
   * @return true if is a mixed material type false otherwise.
   */
  public boolean isMixedMaterial() {
    return "mm".equals(getFormOfMaterial());
  }

  /**
   * Checks if is a music type.
   *
   * @return true if is a music type false otherwise.
   */
  public boolean isMusic() {
    return "msr".equals(getFormOfMaterial());
  }

  /**
   * Checks if is a serial type.
   *
   * @return true if is a serial type false otherwise.
   */
  public boolean isSerial() {
    return "se".equals(getFormOfMaterial());
  }

  /**
   * Checks if is a visual material type.
   *
   * @return true if is a visual material type false otherwise.
   */
  public boolean isVisualMaterial() {
    return "vm".equals(getFormOfMaterial());
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
    builder.append("")
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
      .append(getSerialEntryConventionCode());    /* 34 - Entry convention */

    return builder.toString();
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

  public String getMarcCountryCode() {
    return marcCountryCode;
  }

  public void setMarcCountryCode(final String marcCountryCode) {
    this.marcCountryCode = marcCountryCode;
  }

  public String getItemDateTypeCode() {
    return itemDateTypeCode;
  }

  public void setItemDateTypeCode(final String itemDateTypeCode) {
    this.itemDateTypeCode = itemDateTypeCode;
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(final String languageCode) {
    this.languageCode = languageCode;
  }

  public String getBookBiographyCode() {
    return bookBiographyCode;
  }

  public void setBookBiographyCode(String bookBiographyCode) {
    this.bookBiographyCode = bookBiographyCode;
  }

  public String getBookFestschrift() {
    return bookFestschrift;
  }

  public void setBookFestschrift(String bookFestschrift) {
    this.bookFestschrift = bookFestschrift;
  }

  public String getBookIllustrationCode() {
    return bookIllustrationCode;
  }

  public void setBookIllustrationCode(String string) {
    bookIllustrationCode = leftJustify(string);
  }

  public String getBookIndexAvailabilityCode() {
    return bookIndexAvailabilityCode;
  }

  public void setBookIndexAvailabilityCode(String bookIndexAvailabilityCode) {
    this.bookIndexAvailabilityCode = bookIndexAvailabilityCode;
  }

  public String getBookLiteraryFormTypeCode() {
    return bookLiteraryFormTypeCode;
  }

  public void setBookLiteraryFormTypeCode(String bookLiteraryFormTypeCode) {
    this.bookLiteraryFormTypeCode = bookLiteraryFormTypeCode;
  }

  public String getBookMainEntryCode() {
    return bookMainEntryCode;
  }

  public void setBookMainEntryCode(String bookMainEntryCode) {
    this.bookMainEntryCode = bookMainEntryCode;
  }

  public String getCartographicFormatCode() {
    return cartographicFormatCode;
  }

  public void setCartographicFormatCode(String s) {
    cartographicFormatCode = s;
  }

  public String getCartographicIndexAvailabilityCode() {
    return cartographicIndexAvailabilityCode;
  }

  public void setCartographicIndexAvailabilityCode(String cartographicIndexAvailabilityCode) {
    this.cartographicIndexAvailabilityCode = cartographicIndexAvailabilityCode;
  }

  public String getCartographicMaterial() {
    return cartographicMaterial;
  }

  public void setCartographicMaterial(String c) {
    cartographicMaterial = c;
  }

  public String getCartographicMeridianCode() {
    return cartographicMeridianCode;
  }

  public void setCartographicMeridianCode(String string) {
    cartographicMeridianCode = string;
  }

  public String getCartographicNarrativeTextCode() {
    return cartographicNarrativeTextCode;
  }

  public void setCartographicNarrativeTextCode(String cartographicNarrativeTextCode) {
    this.cartographicNarrativeTextCode = cartographicNarrativeTextCode;
  }

  public String getCartographicProjectionCode() {
    return cartographicProjectionCode;
  }

  public void setCartographicProjectionCode(String string) {
    cartographicProjectionCode = string;
  }

  public String getCartographicReliefCode() {
    return cartographicReliefCode;
  }

  public void setCartographicReliefCode(String s) {
    cartographicReliefCode = s;
  }

  public String getComputerFileTypeCode() {
    return computerFileTypeCode;
  }

  public void setComputerFileTypeCode(String computerFileTypeCode) {
    this.computerFileTypeCode = computerFileTypeCode;
  }

  public String getComputerTargetAudienceCode() {
    return computerTargetAudienceCode;
  }

  public void setComputerTargetAudienceCode(String computerTargetAudienceCode) {
    this.computerTargetAudienceCode = computerTargetAudienceCode;
  }

  public String getConferencePublicationCode() {
    return conferencePublicationCode;
  }

  public void setConferencePublicationCode(String conferencePublicationCode) {
    this.conferencePublicationCode = conferencePublicationCode;
  }

  public String getFormOfItemCode() {
    return formOfItemCode;
  }

  public void setFormOfItemCode(String formOfItemCode) {
    this.formOfItemCode = formOfItemCode;
  }

  public String getGovernmentPublicationCode() {
    return governmentPublicationCode;
  }

  public void setGovernmentPublicationCode(String governmentPublicationCode) {
    this.governmentPublicationCode = governmentPublicationCode;
  }

  public String getMaterialDescription008Indicator() {
    return materialDescription008Indicator;
  }

  public void setMaterialDescription008Indicator(String c) {
    materialDescription008Indicator = c;
  }

  public String getRecordCataloguingSourceCode() {
    return recordCataloguingSourceCode;
  }

  public void setRecordCataloguingSourceCode(String recordCataloguingSourceCode) {
    this.recordCataloguingSourceCode = recordCataloguingSourceCode;
  }

  public String getMaterialTypeCode() {
    return materialTypeCode;
  }

  public void setMaterialTypeCode(String materialTypeCode) {
    this.materialTypeCode = materialTypeCode;
  }

  public String getMusicFormatCode() {
    return musicFormatCode;
  }

  public void setMusicFormatCode(String musicFormatCode) {
    this.musicFormatCode = musicFormatCode;
  }

  public String getMusicFormOfCompositionCode() {
    return musicFormOfCompositionCode;
  }

  public void setMusicFormOfCompositionCode(String string) {
    musicFormOfCompositionCode = string;
  }

  public String getMusicLiteraryTextCode() {
    return musicLiteraryTextCode;
  }

  public void setMusicLiteraryTextCode(String string) {
    musicLiteraryTextCode = string;
  }

  public String getMusicTextualMaterialCode() {
    return musicTextualMaterialCode;
  }

  public void setMusicTextualMaterialCode(String string) {
    musicTextualMaterialCode = string;
  }

  public String getNatureOfContentsCode() {
    return natureOfContentsCode;
  }

  public void setNatureOfContentsCode(String string) {
    natureOfContentsCode = leftJustify(string);
  }

  public String getSerialCumulativeIndexCode() {
    return serialCumulativeIndexCode;
  }

  public void setSerialCumulativeIndexCode(String serialCumulativeIndexCode) {
    this.serialCumulativeIndexCode = serialCumulativeIndexCode;
  }

  public String getSerialFormOriginalItemCode() {
    return serialFormOriginalItemCode;
  }

  public void setSerialFormOriginalItemCode(String serialFormOriginalItemCode) {
    this.serialFormOriginalItemCode = serialFormOriginalItemCode;
  }

  public String getSerialFrequencyCode() {
    return serialFrequencyCode;
  }

  public void setSerialFrequencyCode(String serialFrequencyCode) {
    this.serialFrequencyCode = serialFrequencyCode;
  }

  public String getSerialIndexAvailabilityCode() {
    return serialIndexAvailabilityCode;
  }

  public void setSerialIndexAvailabilityCode(String serialIndexAvailabilityCode) {
    this.serialIndexAvailabilityCode = serialIndexAvailabilityCode;
  }

  public String getSerialISDSCenterCode() {
    return serialISDSCenterCode;
  }

  public void setSerialISDSCenterCode(String serialISDSCenterCode) {
    this.serialISDSCenterCode = serialISDSCenterCode;
  }

  public String getSerialRegularityCode() {
    return serialRegularityCode;
  }

  public void setSerialRegularityCode(String serialRegularityCode) {
    this.serialRegularityCode = serialRegularityCode;
  }

  public String getSerialEntryConventionCode() {
    return serialEntryConventionCode;
  }

  public void setSerialEntryConventionCode(String serialEntryConventionCode) {
    this.serialEntryConventionCode = serialEntryConventionCode;
  }

  public String getSerialOriginalAlphabetOfTitleCode() {
    return serialOriginalAlphabetOfTitleCode;
  }

  public void setSerialOriginalAlphabetOfTitleCode(String serialOriginalAlphabetOfTitleCode) {
    this.serialOriginalAlphabetOfTitleCode = serialOriginalAlphabetOfTitleCode;
  }

  public String getSerialTitlePageExistenceCode() {
    return serialTitlePageExistenceCode;
  }

  public void setSerialTitlePageExistenceCode(String serialTitlePageExistenceCode) {
    this.serialTitlePageExistenceCode = serialTitlePageExistenceCode;
  }

  public String getSerialTypeCode() {
    return serialTypeCode;
  }

  public void setSerialTypeCode(String serialTypeCode) {
    this.serialTypeCode = serialTypeCode;
  }

  public String getTargetAudienceCode() {
    return targetAudienceCode;
  }

  public void setTargetAudienceCode(String targetAudienceCode) {
    this.targetAudienceCode = targetAudienceCode;
  }

  public String getVisualAccompanyingMaterialCode() {
    return visualAccompanyingMaterialCode;
  }

  public void setVisualAccompanyingMaterialCode(String string) {
    visualAccompanyingMaterialCode = string;
  }

  public String getVisualMaterialTypeCode() {
    return visualMaterialTypeCode;
  }

  public void setVisualMaterialTypeCode(String visualMaterialTypeCode) {
    this.visualMaterialTypeCode = visualMaterialTypeCode;
  }

  public String getVisualOriginalHolding() {
    return visualOriginalHolding;
  }

  public void setVisualOriginalHolding(String visualOriginalHolding) {
    this.visualOriginalHolding = visualOriginalHolding;
  }

  public String getVisualRunningTime() {
    return visualRunningTime;
  }

  public void setVisualRunningTime(String string) {
    visualRunningTime = string;
  }

  public String getVisualTargetAudienceCode() {
    return visualTargetAudienceCode;
  }

  public void setVisualTargetAudienceCode(String visualTargetAudienceCode) {
    this.visualTargetAudienceCode = visualTargetAudienceCode;
  }

  public String getVisualTechniqueCode() {
    return visualTechniqueCode;
  }

  public void setVisualTechniqueCode(String visualTechniqueCode) {
    this.visualTechniqueCode = visualTechniqueCode;
  }

  public String getFormOfMaterial() {
    return formOfMaterial;
  }

  public void setFormOfMaterial(String string) {
    formOfMaterial = string;
  }

  public int getHeaderType() {
    return headerType;
  }

  public void setHeaderType(int s) {
    this.headerType = s;
  }

  public String getEnteredOnFileDateYYMMDD() {
    return enteredOnFileDateYYMMDD;
  }

  public void setEnteredOnFileDateYYMMDD(String enteredOnFileDateYYMMDD) {
    this.enteredOnFileDateYYMMDD = enteredOnFileDateYYMMDD;
  }

  public String getItemDateFirstPublication() {
    return itemDateFirstPublication;
  }

  public void setItemDateFirstPublication(String dateFirstPublication) {
    itemDateFirstPublication = dateFirstPublication;
  }

  public String getItemDateLastPublication() {
    return itemDateLastPublication;
  }

  public void setItemDateLastPublication(String dateLastPublication) {
    itemDateLastPublication = dateLastPublication;
  }

  public String getRecordModifiedCode() {
    return recordModifiedCode;
  }

  public void setRecordModifiedCode(String recordModifiedCode) {
    this.recordModifiedCode = recordModifiedCode;
  }
}
