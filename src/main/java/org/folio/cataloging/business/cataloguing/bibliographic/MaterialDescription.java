package org.folio.cataloging.business.cataloguing.bibliographic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.common.ItemEntity;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.dao.DAORecordTypeMaterial;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.RecordTypeMaterial;
import org.folio.cataloging.dao.persistence.T_BIB_HDR;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.folio.cataloging.F.*;

@SuppressWarnings("unchecked")
public class MaterialDescription extends FixedFieldUsingItemEntity implements PersistentObjectWithView 
{
	private static final long serialVersionUID = 232911118088181003L;
	
	static private Log logger = LogFactory.getLog(MaterialDescription.class);
	public static final String DEFAULT_DATE = "    ";
	private int materialDescriptionKeyNumber;
	private Character materialTypeCode = new Character('a');
	private char materialDescription008Indicator = '0';
	private String bookIllustrationCode = "    ";
	private Character targetAudienceCode = new Character(' ');
	private Character formOfItemCode = new Character(' ');
	private String natureOfContentsCode = "    ";
	private Character governmentPublicationCode = new Character(' ');
	private Character conferencePublicationCode = new Character(' ');
	private Character bookFestschrift = new Character(' ');
	private Character bookIndexAvailabilityCode = new Character(' ');
	private Character bookLiteraryFormTypeCode = new Character(' ');
	private Character bookBiographyCode = new Character(' ');
	private Character bookMainEntryCode = new Character(' ');
	private String cartographicReliefCode = "      ";
	private String cartographicProjectionCode = "  ";
	private String cartographicMeridianCode = "  ";
	private Character cartographicNarrativeTextCode = new Character(' ');
	private Character cartographicIndexAvailabilityCode = new Character(' ');
	private String cartographicFormatCode = " ";
	private Character computerTargetAudienceCode = new Character(' ');
	private Character computerFileTypeCode = new Character(' ');
	
	/* Bug 4119 */
	private Character computerFileFormCode = new Character(' ');

	private String visualRunningTime = "000";
	private Character visualTargetAudienceCode = new Character(' ');
	private String visualAccompanyingMaterialCode = " ";
	private Character visualMaterialTypeCode = new Character(' ');
	private Character visualTechniqueCode = new Character(' ');
	private Character serialFrequencyCode = new Character(' ');
	private Character serialRegularityCode = new Character(' ');
	private Character serialISDSCenterCode = new Character(' ');
	private Character serialTypeCode = new Character(' ');
	private Character serialFormOriginalItemCode = new Character(' ');
	private Character serialCumulativeIndexCode = new Character(' ');
	private Character serialOriginalAlphabetOfTitleCode = new Character(' ');
	private Character serialSuccessiveLatestCode = new Character(' ');
	private Character serialTitlePageExistenceCode = new Character(' ');
	private Character serialIndexAvailabilityCode = new Character(' ');
	private String musicFormOfCompositionCode = " ";
	private Character musicFormatCode = new Character(' ');
	private String musicTextualMaterialCode = " ";
	private String musicLiteraryTextCode = " ";
	
	/* Bug 4161 */
	private Character musicPartsCode = new Character(' ');
	private Character musicTranspositionArrangementCode = new Character(' ');	
	
	private Character cartographicMaterial = new Character(' ');
	private Character visualOriginalHolding = new Character(' ');
	private String formOfMaterial = " ";
	private UserViewHelper userViewHelper = new UserViewHelper();
	private String marcCountryCodeCustom;
	private String languageCodeCustom;

	public Character getMusicPartsCode() {
		return musicPartsCode;
	}

	public void setMusicPartsCode(Character musicPartsCode) {
		this.musicPartsCode = musicPartsCode;
	}

	public Character getMusicTranspositionArrangementCode() {
		return musicTranspositionArrangementCode;
	}

	public void setMusicTranspositionArrangementCode(
			Character musicTranspositionArrangementCode) {
		this.musicTranspositionArrangementCode = musicTranspositionArrangementCode;
	}

	public Character getComputerFileFormCode() {
		return computerFileFormCode;
	}

	public void setComputerFileFormCode(Character computerFileFormCode) {
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

	public MaterialDescription() {
		super();
		setPersistenceState(new PersistenceState());
		setToDefault();
	}

	/*
	 * Squeeze all non-blank characters to the left side of the string and
	 * retain the original length by padding with blanks on the right
	 */
	private String leftJustify(String s) {
		if (s == null) {
			return null;
		}
		String result = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				result = result + s.charAt(i);
			}
		}
		while (result.length() < s.length()) {
			result = result + " ";
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setMaterialDescriptionKeyNumber(dao.getNextNumber("X0"));
	}

	/**
	 * Sets the form-specific values to their default
	 * 
	 * @since 1.0
	 */
	public void setToDefault() 
	{
		setBookIllustrationCode(Defaults.getString("material.bookIllustrationCode"));
		setTargetAudienceCode(Defaults.getCharacter("material.targetAudienceCode"));
		setFormOfItemCode(Defaults.getCharacter("material.formOfItemCode"));
		setNatureOfContentsCode(Defaults.getString("material.natureOfContentsCode"));		
		setGovernmentPublicationCode(Defaults.getCharacter("material.governmentPublicationCode"));
		setConferencePublicationCode(Defaults.getCharacter("material.conferencePublicationCode"));
		setBookFestschrift(Defaults.getCharacter("material.bookFestschrift"));
		setBookIndexAvailabilityCode(Defaults.getCharacter("material.bookIndexAvailabilityCode"));
		setBookLiteraryFormTypeCode(Defaults.getCharacter("material.bookLiteraryFormTypeCode"));
		setBookBiographyCode(Defaults.getCharacter("material.bookBiographyCode"));
		setCartographicReliefCode(Defaults.getString("material.cartographicReliefCode"));
		setCartographicProjectionCode(Defaults.getString("material.cartographicProjectionCode"));
		setCartographicMaterial(Defaults.getCharacter("material.cartographicMaterial"));
		setCartographicFormatCode(Defaults.getString("material.cartographicFormatCode"));
		setComputerTargetAudienceCode(Defaults.getCharacter("material.targetAudienceCode"));
		setComputerFileTypeCode(Defaults.getCharacter("material.computerFileTypeCode"));
		/* Bug 4119 */
		setComputerFileFormCode(Defaults.getCharacter("material.computerFileFormCode"));
		
		setVisualRunningTime(Defaults.getString("material.visualRunningTime"));
		setVisualTargetAudienceCode(Defaults.getCharacter("material.targetAudienceCode"));
		setVisualMaterialTypeCode(Defaults.getCharacter("material.visualMaterialTypeCode"));
		setVisualTechniqueCode(Defaults.getCharacter("material.visualTechniqueCode"));
		setSerialFrequencyCode(Defaults.getCharacter("material.serialFrequencyCode"));
		setSerialRegularityCode(Defaults.getCharacter("material.serialRegularityCode"));
		setSerialTypeCode(Defaults.getCharacter("material.serialTypeCode"));
		setSerialFormOriginalItemCode(Defaults.getCharacter("material.formOfItemCode"));
		setSerialOriginalAlphabetOfTitleCode(Defaults.getCharacter("material.serialOriginalAlphabetOfTitleCode"));
		setSerialSuccessiveLatestCode(Defaults.getCharacter("material.serialSuccessiveLatestCode"));
		setMusicFormOfCompositionCode(Defaults.getString("material.musicFormOfCompositionCode"));
		setMusicFormatCode(Defaults.getCharacter("material.musicFormatCode"));
		setMusicTextualMaterialCode(Defaults.getString("material.musicTextualMaterialCode"));
		setMusicLiteraryTextCode(Defaults.getString("material.musicLiteraryTextCode"));
		/* 4161 */
		setMusicPartsCode(Defaults.getCharacter("material.musicPartsCode"));
		setMusicTranspositionArrangementCode(Defaults.getCharacter("material.musicTranspositionArrangementCode"));
	}

	public String getDisplayString() 
	{
		String str = new String("");
		if (getMaterialDescription008Indicator() == '1') {
			str = str 
				+ getEnteredOnFileDateYYMMDD()
				+ getItemDateTypeCode()
				+ getItemDateFirstPublication()
				+ getItemDateLastPublication()
				+ getMarcCountryCode();
		} else {
			str = str + getMaterialTypeCode();
		}

		//		TODO this may beg subclassing -- Janick thinks so too					
		if (isBook()) {
			str = str + bookDisplayString();
		} else if (isComputerFile()) {
			str = str + computerFileDisplayString();
		} else if (isMap()) {
			str = str + mapDisplayString();
		} else if (isMixedMaterial()) {
			str = str + mixedMaterialDisplayString();
		} else if (isMusic()) {
			str = str + musicDisplayString();
		} else if (isSerial()) {
			str = str + serialDisplayString();
		} else if (isVisualMaterial()) {
			str = str + visualMaterialDisplayString();
		}

		if (getMaterialDescription008Indicator() == '1') {
			str = str
				+ getLanguageCode()
				+ getRecordModifiedCode()
				+ getRecordCataloguingSourceCode();
		}
		logger.debug("displayString: " + str);
		return str;
	}

	public boolean equals(Object obj) 
	{
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
			if (other.materialDescriptionKeyNumber > 0) {
				return true;
			} else {
				if (this.getMaterialDescription008Indicator() == '1' &&
					other.getMaterialDescription008Indicator() == '1') {
					return true;
				}
				return this.getHeaderType() == other.getHeaderType();
			}
		} else {
			return false;
		}
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
	 * return the displayString segment for book material
	 * @author paulm
	 * @version $Revision: 1.17 $, $Date: 2006/07/26 14:31:51 $
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
	 * @version $Revision: 1.17 $, $Date: 2006/07/26 14:31:51 $
	 * @since 1.0
	 */
	public String mapDisplayString() {
		return getCartographicReliefCode()
			+ getCartographicProjectionCode()
			+ " "
		//undefined
		+getCartographicMaterial() + "  " // two undefined
		+getGovernmentPublicationCode() + getFormOfItemCode() + " "
		// undefined
		+getCartographicIndexAvailabilityCode() + " " //undefined position
		+getCartographicFormatCode();
	}

	/**
	 * return the displayString segment for computer file material
	 * @author paulm
	 * @version $Revision: 1.17 $, $Date: 2006/07/26 14:31:51 $
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
	 * @version $Revision: 1.17 $, $Date: 2006/07/26 14:31:51 $
	 * @since 1.0
	 */
	public String mixedMaterialDisplayString() 
	{
		return "     " 							// five undefined 
		+getFormOfItemCode() + "           "; 	// 11 undefined positions
	}

	/**
	 * return the displayString segment for music material
	 * @author paulm
	 * @version $Revision: 1.17 $, $Date: 2006/07/26 14:31:51 $
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
	 * @version $Revision: 1.17 $, $Date: 2006/07/26 14:31:51 $
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
	 * @version $Revision: 1.17 $, $Date: 2006/07/26 14:31:51 $
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

	public void setMaterialDescriptionKeyNumber(int integer) {
		materialDescriptionKeyNumber = integer;
	}
	public int getMaterialDescriptionKeyNumber() {
		return materialDescriptionKeyNumber;
	}
	public Character getBookBiographyCode() {
		return bookBiographyCode;
	}

	public Character getBookFestschrift() {
		return bookFestschrift;
	}

	public String getBookIllustrationCode() {
		return bookIllustrationCode;
	}

	public char[] getBookIllustrationChar() {
		if (getBookIllustrationCode() == null) {
			return null;
		} else {
			return getBookIllustrationCode().toCharArray();
		}
	}

	public Character getBookIndexAvailabilityCode() {
		return bookIndexAvailabilityCode;
	}

	public Character getBookLiteraryFormTypeCode() {
		return bookLiteraryFormTypeCode;
	}

	public Character getBookMainEntryCode() {
		return bookMainEntryCode;
	}

	public String getCartographicFormatCode() {
		return cartographicFormatCode;
	}

	public char[] getCartographicFormatChar() {
		if (cartographicFormatCode == null) {
			return null;
		} else {
			return cartographicFormatCode.toCharArray();
		}
	}

	public Character getCartographicIndexAvailabilityCode() {
		return cartographicIndexAvailabilityCode;
	}

	public Character getCartographicMaterial() {
		return cartographicMaterial;
	}

	public String getCartographicMeridianCode() {
		return cartographicMeridianCode;
	}

	public Character getCartographicNarrativeTextCode() {
		return cartographicNarrativeTextCode;
	}

	public String getCartographicProjectionCode() {
		return cartographicProjectionCode;
	}

	public String getCartographicReliefCode() {
		return cartographicReliefCode;
	}

	public char[] getCartographicReliefChar() {
		if (cartographicReliefCode == null) {
			return null;
		} else {
			return cartographicReliefCode.toCharArray();
		}
	}

	public Character getComputerFileTypeCode() {
		return computerFileTypeCode;
	}

	public Character getComputerTargetAudienceCode() {
		return computerTargetAudienceCode;
	}

	public Character getConferencePublicationCode() {
		return conferencePublicationCode;
	}

	public Character getFormOfItemCode() {
		return formOfItemCode;
	}

	public Character getGovernmentPublicationCode() {
		return governmentPublicationCode;
	}

	public char getMaterialDescription008Indicator() {
		return materialDescription008Indicator;
	}

	public Character getMaterialTypeCode() {
		return materialTypeCode;
	}

	public Character getMusicFormatCode() {
		return musicFormatCode;
	}

	public String getMusicFormOfCompositionCode() {
		return musicFormOfCompositionCode;
	}

	public String getMusicLiteraryTextCode() {
		return musicLiteraryTextCode;
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

	public char[] getNatureOfContentsChar() {
		if (natureOfContentsCode == null) {
			return null;
		} else {
			return natureOfContentsCode.toCharArray();
		}
	}
	
	public Character getSerialCumulativeIndexCode() {
		return serialCumulativeIndexCode;
	}

	public Character getSerialFormOriginalItemCode() {
		return serialFormOriginalItemCode;
	}

	public Character getSerialFrequencyCode() {
		return serialFrequencyCode;
	}

	public Character getSerialIndexAvailabilityCode() {
		return serialIndexAvailabilityCode;
	}

	public Character getSerialISDSCenterCode() {
		return serialISDSCenterCode;
	}

	public Character getSerialRegularityCode() {
		return serialRegularityCode;
	}

	public Character getSerialSuccessiveLatestCode() {
		return serialSuccessiveLatestCode;
	}

	public Character getSerialOriginalAlphabetOfTitleCode() {
		return serialOriginalAlphabetOfTitleCode;
	}

	public Character getSerialTitlePageExistenceCode() {
		return serialTitlePageExistenceCode;
	}

	public Character getSerialTypeCode() {
		return serialTypeCode;
	}

	public Character getTargetAudienceCode() {
		return targetAudienceCode;
	}

	public String getVisualAccompanyingMaterialCode() {
		return visualAccompanyingMaterialCode;
	}

	public Character getVisualMaterialTypeCode() {
		return visualMaterialTypeCode;
	}

	public Character getVisualOriginalHolding() {
		return visualOriginalHolding;
	}

	public String getVisualRunningTime() {
		return visualRunningTime;
	}

	public Character getVisualTargetAudienceCode() {
		return visualTargetAudienceCode;
	}

	public Character getVisualTechniqueCode() {
		return visualTechniqueCode;
	}

	public void setBookBiographyCode(Character character) {
		bookBiographyCode = character;
	}

	public void setBookFestschrift(Character character) {
		bookFestschrift = character;
	}

	public void setBookIllustrationCode(String string) {
		bookIllustrationCode = leftJustify(string);
	}

	public void setBookIndexAvailabilityCode(Character character) {
		bookIndexAvailabilityCode = character;
	}

	public void setBookLiteraryFormTypeCode(Character character) {
		bookLiteraryFormTypeCode = character;
	}

	public void setBookMainEntryCode(Character character) {
		bookMainEntryCode = character;
	}

	public void setCartographicFormatCode(String s) {
		cartographicFormatCode = s;
	}

	public void setCartographicIndexAvailabilityCode(Character character) {
		cartographicIndexAvailabilityCode = character;
	}

	public void setCartographicMaterial(Character c) {
		cartographicMaterial = c;
	}

	public void setCartographicMeridianCode(String string) {
		cartographicMeridianCode = string;
	}

	public void setCartographicNarrativeTextCode(Character character) {
		cartographicNarrativeTextCode = character;
	}

	public void setCartographicProjectionCode(String string) {
		cartographicProjectionCode = string;
	}

	public void setCartographicReliefCode(String s) {
		cartographicReliefCode = s;
	}

	public void setComputerFileTypeCode(Character character) {
		computerFileTypeCode = character;
	}

	public void setComputerTargetAudienceCode(Character character) {
		computerTargetAudienceCode = character;
	}

	public void setConferencePublicationCode(Character character) {
		conferencePublicationCode = character;
	}

	public void setFormOfItemCode(Character character) {
		formOfItemCode = character;
	}

	public void setGovernmentPublicationCode(Character character) {
		governmentPublicationCode = character;
	}

	public void setMaterialDescription008Indicator(char c) {
		materialDescription008Indicator = c;
	}

	public void setMaterialTypeCode(Character character) {
		materialTypeCode = character;
	}

	public void setMusicFormatCode(Character character) {
		musicFormatCode = character;
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

	public void setSerialCumulativeIndexCode(Character character) {
		serialCumulativeIndexCode = character;
	}

	public void setSerialFormOriginalItemCode(Character character) {
		serialFormOriginalItemCode = character;
	}

	public void setSerialFrequencyCode(Character character) {
		serialFrequencyCode = character;
	}

	public void setSerialIndexAvailabilityCode(Character character) {
		serialIndexAvailabilityCode = character;
	}

	public void setSerialISDSCenterCode(Character character) {
		serialISDSCenterCode = character;
	}

	public void setSerialRegularityCode(Character character) {
		serialRegularityCode = character;
	}

	public void setSerialSuccessiveLatestCode(Character character) {
		serialSuccessiveLatestCode = character;
	}

	public void setSerialOriginalAlphabetOfTitleCode(Character character) {
		serialOriginalAlphabetOfTitleCode = character;
	}

	public void setSerialTitlePageExistenceCode(Character character) {
		serialTitlePageExistenceCode = character;
	}

	public void setSerialTypeCode(Character character) {
		serialTypeCode = character;
	}

	public void setTargetAudienceCode(Character character) {
		targetAudienceCode = character;
	}

	public void setVisualAccompanyingMaterialCode(String string) {
		visualAccompanyingMaterialCode = string;
	}

	public void setVisualMaterialTypeCode(Character character) {
		visualMaterialTypeCode = character;
	}

	public void setVisualOriginalHolding(Character character) {
		visualOriginalHolding = character;
	}

	public void setVisualRunningTime(String string) {
		visualRunningTime = string;
	}

	public void setVisualTargetAudienceCode(Character character) {
		visualTargetAudienceCode = character;
	}

	public void setVisualTechniqueCode(Character character) {
		visualTechniqueCode = character;
	}

	public void setFormOfMaterialFromBibItem() {
		/*
		 * formOfMaterial is an artificial (non-persistent) value for material description
		 * that must be derived from other mtrl_dsc and bib_itm data (as coded below).
		 */
		DAORecordTypeMaterial dao = new DAORecordTypeMaterial();
		RecordTypeMaterial rtm;
		rtm =
			dao.get008HeaderCode(
				getItemRecordTypeCode(),
				getItemBibliographicLevelCode());
		setFormOfMaterial(rtm.getAmicusMaterialTypeCode());
	}

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
	public void setItemEntity(ItemEntity item) {
		/*
		 * we override this item to establish the new values for bibHeader 
		 * whenever the bibItm data changes (including new instances)
		 * bib_header is an artificial (non-persistent) value for material description
		 * that must be derived from other mtrl_dsc and bib_itm data (as coded below).
		 */
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
			/*
			 * although the 006 values are not affected by bib_item values, we
			 * need to establish the correct bib_header value for loaded items
			 */
			DAORecordTypeMaterial dao = new DAORecordTypeMaterial();
			RecordTypeMaterial rtm;
			rtm = dao.get006HeaderCode(getMaterialTypeCode());
			if(rtm!=null){
			setHeaderType(rtm.getBibHeader006());
			}

		}
	}

	/* (non-Javadoc)
	 * @see FixedField#setBibHeader(short)
	 */
	public void setHeaderType(short s) {
		//TODO put these hard coded values from T_BIB_HEADER into a table
		//TODO this code allows the user to select an 008 type from the header that is not
		// consistent with the values in the Leader -- shouldn't allow this		
		if (getHeaderType() != s) {
			super.setHeaderType(s);
			if (getHeaderType() >= 16 && getHeaderType() <= 22) {
				setMaterialDescription008Indicator('0');
				DAORecordTypeMaterial dao = new DAORecordTypeMaterial();
				RecordTypeMaterial rtm = dao.getDefaultTypeBy006Header(s);
				setMaterialTypeCode(rtm.getRecordTypeCodeCharacter());
				setFormOfMaterial(rtm.getAmicusMaterialTypeCode());
			} else {
				setMaterialDescription008Indicator('1');
				setMaterialTypeCode(null);
				setFormOfMaterialFromBibItem();
			}
		}
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
	 */
	public boolean isAbleToBeDeleted() {
        return getMaterialDescription008Indicator() != '1';
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
	public HibernateUtil getDAO() {
		return getPersistenceState().getDAO();
	}

	public Element generateModelXmlElementContent(Document xmlDocument) 
	{
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			if (getMaterialDescription008Indicator() == '1') {
				content.setAttribute("enteredOnFileDateYYYYMMDD", getEnteredOnFileDateYYYYMMDD());
				content.setAttribute("itemDateTypeCode","" + getItemDateTypeCode());
				content.setAttribute("itemDateFirstPublication", getItemDateFirstPublication());
				content.setAttribute("itemDateLastPublication",	getItemDateLastPublication());
				content.setAttribute("marcCountryCode", getMarcCountryCode());
				content.setAttribute("languageCode", getLanguageCode());
				content.setAttribute("recordModifiedCode","" + getRecordModifiedCode());
				content.setAttribute("recordCataloguingSourceCode","" + getRecordCataloguingSourceCode());
			}
			content.setAttribute("materialTypeCode", asNullableString(getMaterialTypeCode()));
			content.setAttribute("materialDescription008Indicator","" + getMaterialDescription008Indicator());
			content.setAttribute("bookIllustrationCode",getBookIllustrationCode());
			content.setAttribute("targetAudienceCode", asNullableString(getTargetAudienceCode()));
			content.setAttribute("formOfItemCode", asNullableString(getFormOfItemCode()));
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
			
			/* Bug 4119 */
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
			
			/* Bug 4161 */
			content.setAttribute("musicPartsCode", asNullableString(getMusicPartsCode()));
			content.setAttribute("musicTranspositionArrangementCode", asNullableString(getMusicTranspositionArrangementCode()));
			
			content.setAttribute("cartographicMaterial", asNullableString(getCartographicMaterial()));
			content.setAttribute("visualOriginalHolding", asNullableString(getVisualOriginalHolding()));
			content.setAttribute("formOfMaterial", getFormOfMaterial());			
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) 
	{
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setMaterialTypeCode(characterFromXML(content.getAttribute("materialTypeCode")));
		setMaterialDescription008Indicator(content.getAttribute("materialDescription008Indicator").charAt(0));
		setBookIllustrationCode(stringFromXML(content.getAttribute("bookIllustrationCode")));
		setTargetAudienceCode(characterFromXML(content.getAttribute("targetAudienceCode")));
		setFormOfItemCode(characterFromXML(content.getAttribute("formOfItemCode")));
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
		
		/* Bug 4119 */
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
		
		/* Bug 4161 */
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
			setItemDateFirstPublication(content.getAttribute("itemDateFirstPublication"));
			setItemDateLastPublication(content.getAttribute("itemDateLastPublication"));
			setMarcCountryCode(content.getAttribute("marcCountryCode"));
			setLanguageCode(content.getAttribute("languageCode"));
			setRecordModifiedCode(content.getAttribute("recordModifiedCode").charAt(0));
			setRecordCataloguingSourceCode(content.getAttribute("recordCataloguingSourceCode").charAt(0));
		}		
	}

	public String getUserViewString() {
		return userViewHelper.getUserViewString();
	}

	public void setUserViewString(String string) {
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

	public String getEnteredOnFileDateYYMMDD() {
		return getBibItm().getEnteredOnFileDateYYMMDD();
	}

	public String getEnteredOnFileDateYYYYMMDD() {
		return getBibItm().getEnteredOnFileDateYYYYMMDD();
	}

	public String getItemDateFirstPublication() {
		return getBibItm().getItemDateFirstPublication();
	}

	/*modifica barbara 16/05/2007 - tag 008 data fissa*/
	public String getItemDateLastPublication() {
		return getBibItm().getItemDateLastPublication();
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

	public String getMarcCountryCode() {
		return getBibItm().getMarcCountryCode();
	}

	public void setEnteredOnFileDate(Date date) {
		getBibItm().setEnteredOnFileDate(date);
	}

	public void setItemDateFirstPublication(String string) 
	{
		if(string==null || string.trim().length()==0) {
			string = DEFAULT_DATE;
		}
		getBibItm().setItemDateFirstPublication(string);
	}

	public void setItemDateLastPublication(String string) 
	{
		if(string==null || string.trim().length()==0) {
			string = DEFAULT_DATE;
		}
		getBibItm().setItemDateLastPublication(string);
	}

	public void setItemDateTypeCode(char c) {
		getBibItm().setItemDateTypeCode(c);
	}

	public void setMarcCountryCode(String string) {
		getBibItm().setMarcCountryCode(string);
	}

	public String getLanguageCode() {
		return getBibItm().getLanguageCode();
	}

	public char getRecordCataloguingSourceCode() {
		return getBibItm().getRecordCataloguingSourceCode();
	}

	public char getRecordModifiedCode() {
		return getBibItm().getRecordModifiedCode();
	}

	public void setLanguageCode(String string) {
		getBibItm().setLanguageCode(string);
	}

	public void setRecordCataloguingSourceCode(char c) {
		getBibItm().setRecordCataloguingSourceCode(c);
	}

	public void setRecordModifiedCode(char c) {
		getBibItm().setRecordModifiedCode(c);
	}

	public char getItemBibliographicLevelCode() {
		return getBibItm().getItemBibliographicLevelCode();
	}

	public char getItemRecordTypeCode() {
		return getBibItm().getItemRecordTypeCode();
	}

	public void setItemBibliographicLevelCode(char c) {
		getBibItm().setItemBibliographicLevelCode(c);
	}

	public void setItemRecordTypeCode(char c) {
		getBibItm().setItemRecordTypeCode(c);
	}

	public List getFirstCorrelationList() throws DataAccessException 
	{
		if(getMaterialDescription008Indicator() == '1')
		  return daoCodeTable.getListFromTag008(T_BIB_HDR.class,"008",false);
		else 
		  return daoCodeTable.getListFromWithoutTag008(T_BIB_HDR.class,"008",false);
	}
}