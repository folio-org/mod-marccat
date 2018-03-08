package org.folio.cataloging.domain;

/**
 * Class related to 008 - Fixed-Length Data Elements
 * Contains only methods of logic to create tag 008
 */

import org.apache.commons.lang.StringUtils;
import org.folio.cataloging.Global;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

public class GeneralInformation
{

	private short headerType;
	private String enteredOnFileDateYYMMDD;
	private char itemDateTypeCode;
	private String itemDateFirstPublication;
	private String itemDateLastPublication;
	private char recordModifiedCode;
	private char recordCataloguingSourceCode;
	private char materialTypeCode;
	private char materialDescription008Indicator;
	private String bookIllustrationCode;
	private char targetAudienceCode;
	private char formOfItemCode;
	private String natureOfContentsCode;
	private char governmentPublicationCode;
	private char conferencePublicationCode;
	private char bookFestschrift;
	private char bookIndexAvailabilityCode;
	private char bookLiteraryFormTypeCode;

	private char bookBiographyCode;
	private char bookMainEntryCode;
	private String cartographicReliefCode;
	private String cartographicProjectionCode;
	private String cartographicMeridianCode;
	private char cartographicNarrativeTextCode;
	private char cartographicIndexAvailabilityCode;
	private String cartographicFormatCode;
	private char computerTargetAudienceCode;
	private char computerFileTypeCode;
	private char computerFileFormCode;

	private String visualRunningTime;
	private char visualTargetAudienceCode;
	private String visualAccompanyingMaterialCode;
	private char visualMaterialTypeCode;
	private char visualTechniqueCode ;
	private char serialFrequencyCode;
	private char serialRegularityCode;
	private char serialISDSCenterCode;
	private char serialTypeCode;
	private char serialFormOriginalItemCode;
	private char serialCumulativeIndexCode;
	private char serialOriginalAlphabetOfTitleCode;
	private char serialSuccessiveLatestCode;
	private char serialTitlePageExistenceCode;
	private char serialIndexAvailabilityCode;
	private String musicFormOfCompositionCode;
	private char musicFormatCode;
	private String musicTextualMaterialCode;
	private String musicLiteraryTextCode;
	
	private char musicPartsCode;
	private char musicTranspositionArrangementCode;

	private char cartographicMaterial;
	private char visualOriginalHolding;
	private String formOfMaterial;
	
	private String marcCountryCode;
	private String languageCode;

	public char getMusicPartsCode() {
		return musicPartsCode;
	}

	public void setMusicPartsCode(final char musicPartsCode) {
		this.musicPartsCode = musicPartsCode;
	}

	public char getMusicTranspositionArrangementCode() {
		return musicTranspositionArrangementCode;
	}

	public void setMusicTranspositionArrangementCode(final char musicTranspositionArrangementCode) {
		this.musicTranspositionArrangementCode = musicTranspositionArrangementCode;
	}

	public char getComputerFileFormCode() {
		return computerFileFormCode;
	}

	public void setComputerFileFormCode(final char computerFileFormCode) {
		this.computerFileFormCode = computerFileFormCode;
	}

	public String getMarcCountryCode() {
		return marcCountryCode;
	}

	public void setMarcCountryCode(final String marcCountryCode) { this.marcCountryCode = marcCountryCode; }

	public char getItemDateTypeCode() {
		return itemDateTypeCode;
	}

	public void setItemDateTypeCode(final char itemDateTypeCode) {
		this.itemDateTypeCode = itemDateTypeCode;
	}
	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(final String languageCode) {
		this.languageCode = languageCode;
	}

	public GeneralInformation() {
	}

	/*
	 * Squeeze all non-blank chars to the left side of the string and
	 * retain the original length by padding with blanks on the right
	 */
	private String leftJustify(final String s) {
		return ofNullable(stream(s.split("")).filter(character -> !" ".equals(character)).collect(joining()))
				.map(result -> {
					return StringUtils.leftPad(result, s.length()-result.length(), ' ');
				}).orElse(null);
	}

	public void setDefaultValuesForBook(final Map<String, String> configuration){
		setMaterialDescription008Indicator('1');
		setItemDateFirstPublication(Global.itemDateFirstPublication);
		setItemDateLastPublication(Global.itemDateLastPublication);
		setMarcCountryCode(configuration.get("bibliographicItem.marcCountryCode"));
		setBookIllustrationCode(configuration.get("material.bookIllustrationCode"));
		setTargetAudienceCode(configuration.get("material.targetAudienceCode").charAt(0));
		setFormOfItemCode(configuration.get("material.formOfItemCode").charAt(0));
		setNatureOfContentsCode(configuration.get("material.natureOfContentsCode"));
		setGovernmentPublicationCode(configuration.get("material.governmentPublicationCode").charAt(0));
		setConferencePublicationCode(configuration.get("material.conferencePublicationCode").charAt(0));
		setBookFestschrift(configuration.get("material.bookFestschrift").charAt(0));
		setBookIndexAvailabilityCode(configuration.get("material.bookIndexAvailabilityCode").charAt(0));
		setBookLiteraryFormTypeCode(configuration.get("material.bookLiteraryFormTypeCode").charAt(0));
		setBookBiographyCode(configuration.get("material.bookBiographyCode").charAt(0));
	}

	/**
	 * Sets the form-specific values to their default
	 * 
	 * @since 1.0
	 */
	public void setDefaultValues(final Map<String, String> configuration)
	{
		setRecordCataloguingSourceCode(configuration.get("bibliographicItem.recordCataloguingSourceCode").charAt(0));
		setMarcCountryCode(configuration.get("bibliographicItem.marcCountryCode"));

		setBookIllustrationCode(configuration.get("material.bookIllustrationCode"));
		setTargetAudienceCode(configuration.get("material.targetAudienceCode").charAt(0));
		setFormOfItemCode(configuration.get("material.formOfItemCode").charAt(0));
		setNatureOfContentsCode(configuration.get("material.natureOfContentsCode"));
		setGovernmentPublicationCode(configuration.get("material.governmentPublicationCode").charAt(0));
		setConferencePublicationCode(configuration.get("material.conferencePublicationCode").charAt(0));
		setBookFestschrift(configuration.get("material.bookFestschrift").charAt(0));
		setBookIndexAvailabilityCode(configuration.get("material.bookIndexAvailabilityCode").charAt(0));
		setBookLiteraryFormTypeCode(configuration.get("material.bookLiteraryFormTypeCode").charAt(0));
		setBookBiographyCode(configuration.get("material.bookBiographyCode").charAt(0));

		setCartographicReliefCode(configuration.get("material.cartographicReliefCode"));
		setCartographicProjectionCode(configuration.get("material.cartographicProjectionCode"));
		setCartographicMaterial(configuration.get("material.cartographicMaterial").charAt(0));
		setCartographicFormatCode(configuration.get("material.cartographicFormatCode"));

		setComputerTargetAudienceCode(configuration.get("material.targetAudienceCode").charAt(0));
		setComputerFileTypeCode(configuration.get("material.computerFileTypeCode").charAt(0));
		setComputerFileFormCode(configuration.get("material.computerFileFormCode").charAt(0));

		setVisualRunningTime(configuration.get("material.visualRunningTime"));
		setVisualTargetAudienceCode(configuration.get("material.targetAudienceCode").charAt(0));
		setVisualMaterialTypeCode(configuration.get("material.visualMaterialTypeCode").charAt(0));
		setVisualTechniqueCode(configuration.get("material.visualTechniqueCode").charAt(0));

		setSerialFrequencyCode(configuration.get("material.serialFrequencyCode").charAt(0));
		setSerialRegularityCode(configuration.get("material.serialRegularityCode").charAt(0));
		setSerialTypeCode(configuration.get("material.serialTypeCode").charAt(0));
		setSerialFormOriginalItemCode(configuration.get("material.formOfItemCode").charAt(0));
		setSerialOriginalAlphabetOfTitleCode(configuration.get("material.serialOriginalAlphabetOfTitleCode").charAt(0));
		setSerialSuccessiveLatestCode(configuration.get("material.serialSuccessiveLatestCode").charAt(0));

		setMusicFormOfCompositionCode(configuration.get("material.musicFormOfCompositionCode"));
		setMusicFormatCode(configuration.get("material.musicFormatCode").charAt(0));
		setMusicTextualMaterialCode(configuration.get("material.musicTextualMaterialCode"));
		setMusicLiteraryTextCode(configuration.get("material.musicLiteraryTextCode"));
		setMusicPartsCode(configuration.get("material.musicPartsCode").charAt(0));
		setMusicTranspositionArrangementCode(configuration.get("material.musicTranspositionArrangementCode").charAt(0));
	}

	public String getValueString()
	{
		final StringBuilder sb = new StringBuilder();
		if (getMaterialDescription008Indicator() == '1') {
			sb.append(getEnteredOnFileDateYYMMDD())
				.append(getItemDateTypeCode())
				.append(getItemDateFirstPublication())
				.append(getItemDateLastPublication())
				.append(getMarcCountryCode());
		} else {
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

		if (getMaterialDescription008Indicator() == '1') {
			sb.append( getLanguageCode())
				.append(getRecordModifiedCode())
					.append(getRecordCataloguingSourceCode());
		}

		return sb.toString();
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
	 * return the displayString segment for book material
	 * @author paulm
	 * @since 1.0
	 */
	public String bookDisplayString() 
	{
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
	 * return the displayString segment for map material
	 * @author paulm
	 * @since 1.0
	 */
	public String mapDisplayString() {
		return getCartographicReliefCode()
			+ getCartographicProjectionCode()
			+ " " //undefined
		+ getCartographicMaterial() + "  " // two undefined
		+ getGovernmentPublicationCode() + getFormOfItemCode() + " "
		// undefined
		+ getCartographicIndexAvailabilityCode() + " " //undefined position
		+ getCartographicFormatCode();
	}

	/**
	 * return the displayString segment for computer file material
	 * @author paulm
	 * @since 1.0
	 */
	public String computerFileDisplayString() 
	{
		StringBuilder builder = new StringBuilder(); 
		builder.append("    ")								/*18-21 - Undefined             */
			   .append(getComputerTargetAudienceCode())		/*22    - Target audience       */
			   .append(getComputerFileFormCode())			/*23    - Form of item          */
			   .append("  ")								/*24-25 - Undefined             */ 
			   .append(getComputerFileTypeCode())			/*26    - Type of computer file */ 
			   .append(" ")									/*27    - Undefined             */
			   .append(getGovernmentPublicationCode())		/*28    - Government publication*/ 
			   .append("      ");							/*29-34 - Undefined             */
		
		return builder.toString();
	}

	/**
	 * return the displayString segment for mixed material material
	 * @author paulm
	 * @since 1.0
	 */
	public String mixedMaterialDisplayString() 
	{
		// five undefined + 11 undefined positions
		return "     " + getFormOfItemCode() + "           ";
	}

	/**
	 * return the displayString segment for music material
	 * @author paulm
	 * @since 1.0
	 */
	public String musicDisplayString() 
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getMusicFormOfCompositionCode()) 		/* 18-19 - Form of composition */
				.append(getMusicFormatCode())					/* 20 - Format of music */
				.append(getMusicPartsCode())					/* 21 - Music parts  */
				.append(getTargetAudienceCode())				/* 22 - Target audience */
				.append(getFormOfItemCode())					/* 23 - Form of item */
				.append(getMusicTextualMaterialCode())			/* 24-29 - Accompanying matter */
				.append(getMusicLiteraryTextCode())				/* 30-31 - Literary text for sound recordings */
				.append(" ")									/* 32 - Undefined */
				.append(getMusicTranspositionArrangementCode())	/* 33 - Transposition and arrangement */
				.append(" ");									/* 34 - Undefined  */
		
		return builder.toString();
	}

	/**
	 * return the displayString segment for visual material
	 * @author paulm
	 * @since 1.0
	 */
	public String visualMaterialDisplayString() {
		return getVisualRunningTime() + " " // undefined position
		+getVisualTargetAudienceCode() + "     " // five undefined
		+getGovernmentPublicationCode() + getFormOfItemCode() + "   "
		// three undefined
		+getVisualMaterialTypeCode() + getVisualTechniqueCode();
	}

	/**
	 * return the displayString segment for serial (continuing resources) material
	 * @author paulm
	 * @since 1.0
	 */
	public String serialDisplayString() 
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("")
			   .append(getSerialFrequencyCode())				/* 18 - Frequency */
			   .append(getSerialRegularityCode())				/* 19 - Regularity */
			   .append(" ")										/* 20 - Undefined */
			   .append(getSerialTypeCode())						/* 21 - Type of continuing resource */
			   .append(getSerialFormOriginalItemCode())			/* 22 - Form of original item */
			   .append(getFormOfItemCode())						/* 23 - Form of item */
			   .append(getNatureOfContentsCode())				/* 24 - Nature of entire work 25-27 - Nature of contents */
			   .append(getGovernmentPublicationCode())			/* 28 - Government publication */
			   .append(getConferencePublicationCode())			/* 29 - Conference publication */
			   .append("   ")									/* 30-32 - Undefined  */
			   .append(getSerialOriginalAlphabetOfTitleCode())  /* 33 - Original alphabet or script of title */
			   .append(getSerialSuccessiveLatestCode());		/* 34 - Entry convention */
		
		return builder.toString();
	}

	/** * GETTERS AND SETTERS * **/

	public char getBookBiographyCode() {
		return bookBiographyCode;
	}
	public char getBookFestschrift() {
		return bookFestschrift;
	}

	public String getBookIllustrationCode() {
		return bookIllustrationCode;
	}

	public char getBookIndexAvailabilityCode() {
		return bookIndexAvailabilityCode;
	}

	public char getBookLiteraryFormTypeCode() {
		return bookLiteraryFormTypeCode;
	}

	public char getBookMainEntryCode() {
		return bookMainEntryCode;
	}

	public String getCartographicFormatCode() {
		return cartographicFormatCode;
	}

	public char getCartographicIndexAvailabilityCode() {
		return cartographicIndexAvailabilityCode;
	}

	public char getCartographicMaterial() {
		return cartographicMaterial;
	}

	public String getCartographicMeridianCode() {
		return cartographicMeridianCode;
	}

	public char getCartographicNarrativeTextCode() {
		return cartographicNarrativeTextCode;
	}

	public String getCartographicProjectionCode() {
		return cartographicProjectionCode;
	}

	public String getCartographicReliefCode() {
		return cartographicReliefCode;
	}

	public char getComputerFileTypeCode() {
		return computerFileTypeCode;
	}

	public char getComputerTargetAudienceCode() {
		return computerTargetAudienceCode;
	}

	public char getConferencePublicationCode() {
		return conferencePublicationCode;
	}

	public char getFormOfItemCode() {
		return formOfItemCode;
	}

	public char getGovernmentPublicationCode() {
		return governmentPublicationCode;
	}

	public char getMaterialDescription008Indicator() {
		return materialDescription008Indicator;
	}

	public char getRecordCataloguingSourceCode() {
		return recordCataloguingSourceCode;
	}

	public void setRecordCataloguingSourceCode(char recordCataloguingSourceCode) {
		this.recordCataloguingSourceCode = recordCataloguingSourceCode;
	}

	public char getMaterialTypeCode() {
		return materialTypeCode;
	}

	public char getMusicFormatCode() {
		return musicFormatCode;
	}

	public String getMusicFormOfCompositionCode() {
		return musicFormOfCompositionCode;
	}

	public String getMusicLiteraryTextCode() {
		return musicLiteraryTextCode;
	}

	public String getMusicTextualMaterialCode() {
		return musicTextualMaterialCode;
	}

	public String getNatureOfContentsCode() {
		return natureOfContentsCode;
	}

	public char getSerialCumulativeIndexCode() {
		return serialCumulativeIndexCode;
	}

	public char getSerialFormOriginalItemCode() {
		return serialFormOriginalItemCode;
	}

	public char getSerialFrequencyCode() {
		return serialFrequencyCode;
	}

	public char getSerialIndexAvailabilityCode() {
		return serialIndexAvailabilityCode;
	}

	public char getSerialISDSCenterCode() {
		return serialISDSCenterCode;
	}

	public char getSerialRegularityCode() {
		return serialRegularityCode;
	}

	public char getSerialSuccessiveLatestCode() {
		return serialSuccessiveLatestCode;
	}

	public char getSerialOriginalAlphabetOfTitleCode() {
		return serialOriginalAlphabetOfTitleCode;
	}

	public char getSerialTitlePageExistenceCode() {
		return serialTitlePageExistenceCode;
	}

	public char getSerialTypeCode() {
		return serialTypeCode;
	}

	public char getTargetAudienceCode() {
		return targetAudienceCode;
	}

	public String getVisualAccompanyingMaterialCode() {
		return visualAccompanyingMaterialCode;
	}

	public char getVisualMaterialTypeCode() {
		return visualMaterialTypeCode;
	}

	public char getVisualOriginalHolding() {
		return visualOriginalHolding;
	}

	public String getVisualRunningTime() {
		return visualRunningTime;
	}

	public char getVisualTargetAudienceCode() {
		return visualTargetAudienceCode;
	}

	public char getVisualTechniqueCode() {
		return visualTechniqueCode;
	}

	public void setBookIllustrationCode(String string) {
		bookIllustrationCode = leftJustify(string);
	}

	public void setCartographicFormatCode(String s) {
		cartographicFormatCode = s;
	}

	public void setCartographicMaterial(char c) {
		cartographicMaterial = c;
	}

	public void setCartographicMeridianCode(String string) {
		cartographicMeridianCode = string;
	}

	public void setCartographicProjectionCode(String string) {
		cartographicProjectionCode = string;
	}

	public void setCartographicReliefCode(String s) {
		cartographicReliefCode = s;
	}

	public void setMaterialDescription008Indicator(char c) {
		materialDescription008Indicator = c;
	}

	public void setMusicFormOfCompositionCode(String string) {
		musicFormOfCompositionCode = string;
	}

	public void setMusicLiteraryTextCode(String string) {
		musicLiteraryTextCode = string;
	}

	public void setMusicTextualMaterialCode(String string) {
		musicTextualMaterialCode = string;
	}

	public void setNatureOfContentsCode(String string) {
		natureOfContentsCode = leftJustify(string);
	}

	public void setVisualAccompanyingMaterialCode(String string) {
		visualAccompanyingMaterialCode = string;
	}

	public void setVisualRunningTime(String string) {
		visualRunningTime = string;
	}


	//TODO move to service API: change tag in record
	/*public void setFormOfMaterialFromBibItem() {
		*
		 * formOfMaterial is an artificial (non-persistent) stringValue for material description
		 * that must be derived from other mtrl_dsc and bib_itm data (as coded below).
		 *
		DAORecordTypeMaterial dao = new DAORecordTypeMaterial();
		RecordTypeMaterial rtm;
		rtm =
			dao.get008HeaderCode(
				getItemRecordTypeCode(),
				getItemBibliographicLevelCode());
		setFormOfMaterial(rtm.getAmicusMaterialTypeCode());
	}*/

	/**
	 * 
	 * @since 1.0
	 */
	public String getFormOfMaterial() {
		return formOfMaterial;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setFormOfMaterial(String string) {
		formOfMaterial = string;
	}

	/* (non-Javadoc)
	 * @see FixedField#setBibItm(BIB_ITM)
	 */

	//TODO: maybe the method needs in recordTemplate
	/*public void setItemEntity(ItemEntity item) {

		 * we override this item to establish the new values for bibHeader 
		 * whenever the bibItm data changes (including new instances)
		 * bib_header is an artificial (non-persistent) stringValue for material description
		 * that must be derived from other mtrl_dsc and bib_itm data (as coded below).

		super.setItemEntity(item);
		if (getMaterialDescription008Indicator() == '1') {
			DAORecordTypeMaterial dao = new DAORecordTypeMaterial();
			RecordTypeMaterial rtm;
			rtm =
				dao.get008HeaderCode(
					getItemRecordTypeCode(),
					getItemBibliographicLevelCode());
			if(rtm!=null){
			setHeaderType(rtm.getBibHeader008());
			setFormOfMaterial(rtm.getAmicusMaterialTypeCode());
			}
		} else {

			 * although the 006 values are not affected by bib_item values, we
			 * need to establish the correct bib_header stringValue for loaded items

			DAORecordTypeMaterial dao = new DAORecordTypeMaterial();
			RecordTypeMaterial rtm;
			rtm = dao.get006HeaderCode(getMaterialTypeCode());
			if(rtm!=null){
			setHeaderType(rtm.getBibHeader006());
			}

		}
	}*/


	public short getHeaderType() {
		return headerType;
	}

	public void setHeaderType(short s) {
		this.headerType = s;
	}

	public String getEnteredOnFileDateYYMMDD() {
		return enteredOnFileDateYYMMDD;
	}

	public void setEnteredOnFileDateYYMMDD(String enteredOnFileDateYYMMDD) {
		this.enteredOnFileDateYYMMDD = enteredOnFileDateYYMMDD;
	}

	public String getItemDateFirstPublication() {return itemDateFirstPublication;}
	public String getItemDateLastPublication() {
		return itemDateLastPublication;
	}

	public void setItemDateFirstPublication(String dateFirstPublication)
	{
		itemDateFirstPublication = dateFirstPublication;
	}

	public char getRecordModifiedCode() {
		return recordModifiedCode;
	}

	public void setRecordModifiedCode(char recordModifiedCode) {
		this.recordModifiedCode = recordModifiedCode;
	}

	public void setItemDateLastPublication(String dateLastPublication)
	{
		itemDateLastPublication = dateLastPublication;
	}

	public void setMaterialTypeCode(char materialTypeCode) {
		this.materialTypeCode = materialTypeCode;
	}

	public void setTargetAudienceCode(char targetAudienceCode) {
		this.targetAudienceCode = targetAudienceCode;
	}

	public void setFormOfItemCode(char formOfItemCode) {
		this.formOfItemCode = formOfItemCode;
	}

	public void setGovernmentPublicationCode(char governmentPublicationCode) {
		this.governmentPublicationCode = governmentPublicationCode;
	}

	public void setConferencePublicationCode(char conferencePublicationCode) {
		this.conferencePublicationCode = conferencePublicationCode;
	}

	public void setBookFestschrift(char bookFestschrift) {
		this.bookFestschrift = bookFestschrift;
	}

	public void setBookIndexAvailabilityCode(char bookIndexAvailabilityCode) {
		this.bookIndexAvailabilityCode = bookIndexAvailabilityCode;
	}

	public void setBookLiteraryFormTypeCode(char bookLiteraryFormTypeCode) {
		this.bookLiteraryFormTypeCode = bookLiteraryFormTypeCode;
	}

	public void setBookBiographyCode(char bookBiographyCode) {
		this.bookBiographyCode = bookBiographyCode;
	}

	public void setBookMainEntryCode(char bookMainEntryCode) {
		this.bookMainEntryCode = bookMainEntryCode;
	}

	public void setCartographicNarrativeTextCode(char cartographicNarrativeTextCode) {
		this.cartographicNarrativeTextCode = cartographicNarrativeTextCode;
	}

	public void setCartographicIndexAvailabilityCode(char cartographicIndexAvailabilityCode) {
		this.cartographicIndexAvailabilityCode = cartographicIndexAvailabilityCode;
	}

	public void setComputerTargetAudienceCode(char computerTargetAudienceCode) {
		this.computerTargetAudienceCode = computerTargetAudienceCode;
	}

	public void setComputerFileTypeCode(char computerFileTypeCode) {
		this.computerFileTypeCode = computerFileTypeCode;
	}

	public void setVisualTargetAudienceCode(char visualTargetAudienceCode) {
		this.visualTargetAudienceCode = visualTargetAudienceCode;
	}

	public void setVisualMaterialTypeCode(char visualMaterialTypeCode) {
		this.visualMaterialTypeCode = visualMaterialTypeCode;
	}

	public void setVisualTechniqueCode(char visualTechniqueCode) {
		this.visualTechniqueCode = visualTechniqueCode;
	}

	public void setSerialFrequencyCode(char serialFrequencyCode) {
		this.serialFrequencyCode = serialFrequencyCode;
	}

	public void setSerialRegularityCode(char serialRegularityCode) {
		this.serialRegularityCode = serialRegularityCode;
	}

	public void setSerialISDSCenterCode(char serialISDSCenterCode) {
		this.serialISDSCenterCode = serialISDSCenterCode;
	}

	public void setSerialTypeCode(char serialTypeCode) {
		this.serialTypeCode = serialTypeCode;
	}

	public void setSerialFormOriginalItemCode(char serialFormOriginalItemCode) {
		this.serialFormOriginalItemCode = serialFormOriginalItemCode;
	}

	public void setSerialCumulativeIndexCode(char serialCumulativeIndexCode) {
		this.serialCumulativeIndexCode = serialCumulativeIndexCode;
	}

	public void setSerialOriginalAlphabetOfTitleCode(char serialOriginalAlphabetOfTitleCode) {
		this.serialOriginalAlphabetOfTitleCode = serialOriginalAlphabetOfTitleCode;
	}

	public void setSerialSuccessiveLatestCode(char serialSuccessiveLatestCode) {
		this.serialSuccessiveLatestCode = serialSuccessiveLatestCode;
	}

	public void setSerialTitlePageExistenceCode(char serialTitlePageExistenceCode) {
		this.serialTitlePageExistenceCode = serialTitlePageExistenceCode;
	}

	public void setSerialIndexAvailabilityCode(char serialIndexAvailabilityCode) {
		this.serialIndexAvailabilityCode = serialIndexAvailabilityCode;
	}

	public void setMusicFormatCode(char musicFormatCode) {
		this.musicFormatCode = musicFormatCode;
	}

	public void setVisualOriginalHolding(char visualOriginalHolding) {
		this.visualOriginalHolding = visualOriginalHolding;
	}

}