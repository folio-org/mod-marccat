package librisuite.bean.cataloguing.bibliographic.codelist;

import librisuite.bean.LibrisuiteBean;

public class CodeListsBean extends LibrisuiteBean 
{
	private static AuthorityDisplayFormat authorityDisplayFormat =	new AuthorityDisplayFormat();
	private static AuthorityEncodingLevel authorityEncodingLevel =	new AuthorityEncodingLevel();
	private static AuthorityRecordStatus authorityRecordStatus =	new AuthorityRecordStatus();
	private static AuthorityStructure authorityStructure =	new AuthorityStructure();
	private static AuthorityTagCategory authorityTagCategory =	new AuthorityTagCategory();
	private static BibHeader bibHeader = new BibHeader();
	private static BibliographicDisplayFormat bibliographicDisplayFormat =	new BibliographicDisplayFormat();
	
	private static ReferenceThsType refThsTyp = new ReferenceThsType();
	public static ReferenceThsType getReferenceThsType() {
		return refThsTyp;
	}
	
	private static ReferenceAttrType refAttrTyp = new ReferenceAttrType();
	public static ReferenceAttrType getReferenceAttrType() {
		return refAttrTyp;
	}
	private static BibliographicTagCategory bibliographicTagCategory = new BibliographicTagCategory();
	
	private static BibliographicNoteGroup bibliographicNoteGroup =	new BibliographicNoteGroup();
	public static BibliographicNoteGroup getBibliographicNoteGroup() {
		return bibliographicNoteGroup;
	}

	private static AuthorityNoteGroup  authorityNoteGroup = new AuthorityNoteGroup();
     public static AuthorityNoteGroup getAuthorityNoteGroup() {
    	 return authorityNoteGroup;
    }

	private static BilingualUsage bilingualUsage = new BilingualUsage();
	private static BookBiography bookBiography = new BookBiography();
	private static BookFestschrift bookFestschrift = new BookFestschrift();
	private static BookIllustration bookIllustration = new BookIllustration();
	private static BookIndexAvailability bookIndexAvailability = new BookIndexAvailability();
	private static BookLiteraryForm bookLiteraryForm = new BookLiteraryForm();
	private static BookMaterialType bookMaterialType = new BookMaterialType();
	private static CartographicFormat cartographicFormat = new CartographicFormat();
	private static CartographicIndexAvailability cartographicIndexAvailability = new CartographicIndexAvailability();
	private static CartographicMaterial cartographicMaterial = new CartographicMaterial();
	private static CartographicProjection cartographicProjection = new CartographicProjection();
	private static CartographicRelief cartographicRelief = new CartographicRelief();
	private static CataloguingRules cataloguingRules = new CataloguingRules();
	private static CataloguingSource cataloguingSource = new CataloguingSource();
	private static CataloguingSourceCode CataloguingSourceCode = new CataloguingSourceCode();
	private static CharacterCodingScheme characterCodingScheme = new CharacterCodingScheme();
	private static Collection collection = new Collection();
	private static ComputerFileAntecedentSource computerFileAntecedentSource = new ComputerFileAntecedentSource();
	private static ComputerFileColour computerFileColour = new ComputerFileColour();
	private static ComputerFileDimensions computerFileDimensions = new ComputerFileDimensions();

	/* Bug 4119 */
	private static ComputerFileForm computerFileForm = new ComputerFileForm();
	private static ComputerFileFormat computerFileFormats = new ComputerFileFormat();
	private static ComputerFileLevelOfCompression computerFileLevelOfCompression = new ComputerFileLevelOfCompression();
	private static ComputerFileQualityAssuranceTarget computerFileQualityAssuranceTarget = new ComputerFileQualityAssuranceTarget();
	private static ComputerFileReformattingQuality computerFileReformattingQuality = new ComputerFileReformattingQuality();
	private static ComputerFileSpecificMaterialDesignation computerFileSpecificMaterialDesignation = new ComputerFileSpecificMaterialDesignation();
	private static ComputerFileType computerFileType = new ComputerFileType();
	private static ComputerTargetAudience computerTargetAudience = new ComputerTargetAudience();
	private static Entity entity = new Entity();
	private static ConferencePublication conferencePublication = new ConferencePublication();
	private static ControlType controlType = new ControlType();
	private static CrossReferenceType crossReferenceType = new CrossReferenceType();
	private static DescriptiveCataloguing descriptiveCataloguing = new DescriptiveCataloguing();
	private static DualReference dualReference = new DualReference();
	private static LinkDisplay linkDisplay = new LinkDisplay();
	private static ReplacementComplexity replacementComplexity = new ReplacementComplexity();
	private static EarlierRules earlierRules = new EarlierRules();
	private static EncodingLevel encodingLevel = new EncodingLevel();
	private static FormOfItem formOfItem = new FormOfItem();
	private static GlobeColour globeColour = new GlobeColour();
	private static GlobePhysicalMedium globePhysicalMedium = new GlobePhysicalMedium();
	private static GlobeReproductionType globeReproductionType = new GlobeReproductionType();
	private static GlobeSpecificMaterialDesignation globeSpecificMaterialDesignation = new GlobeSpecificMaterialDesignation();
	private static GovernmentAgency governmentAgency = new GovernmentAgency();
	private static GovernmentPublication governmentPublication = new GovernmentPublication();
	private static HeadingStatus headingStatus = new HeadingStatus();
	private static IncludesSound includesSound = new IncludesSound();
	private static ItemBibliographicLevel itemBibliographicLevel = new ItemBibliographicLevel();
	private static ItemDateType itemDateType = new ItemDateType();
	private static ItemRecordType itemRecordType = new ItemRecordType();
	private static KitSpecificMaterialDesignation kitSpecificMaterialDesignation = new KitSpecificMaterialDesignation();
	private static Language language = new Language();
	private static LinkedRecord linkedRecord = new LinkedRecord();
	private static MainAddedEntryIndicator mainAddedEntryIndicator = new MainAddedEntryIndicator();
	private static MapColour mapColour = new MapColour();
	private static MapMaterialType mapMaterialType = new MapMaterialType();
	private static MapPhysicalMedium mapPhysicalMedium = new MapPhysicalMedium();
	private static MapPolarity mapPolarity = new MapPolarity();
	private static MapProductionDetails mapProductionDetails = new MapProductionDetails();
	private static MapReproductionType mapReproductionType = new MapReproductionType();
	private static MapSpecificMaterialDesignation mapSpecificMaterialDesignation = new MapSpecificMaterialDesignation();
	private static MarcCountry marcCountry = new MarcCountry();
	private static MaterialType materialType = new MaterialType();
	private static MicroformBaseOfFilm microformBaseOfFilm = new MicroformBaseOfFilm();
	private static MicroformColour microformColour = new MicroformColour();
	private static MicroformDimensions microformDimensions = new MicroformDimensions();
	private static MicroformEmulsionOnFilm microformEmulsionOnFilm = new MicroformEmulsionOnFilm();
	private static MicroformGeneration microformGeneration = new MicroformGeneration();
	private static MicroformPolarity microformPolarity = new MicroformPolarity();
	private static MicroformReductionRatioRange microformReductionRatioRange = new MicroformReductionRatioRange();
	private static MicroformSpecificMaterialDesignation microformSpecificMaterialDesignation = new MicroformSpecificMaterialDesignation();
	private static ModifiedRecord modifiedRecord = new ModifiedRecord();
	private static MotionPictureBaseOfFilm motionPictureBaseOfFilm = new MotionPictureBaseOfFilm();
	private static MotionPictureColour motionPictureColour = new MotionPictureColour();
	private static MotionPictureCompleteness motionPictureCompleteness = new MotionPictureCompleteness();
	private static MotionPictureConfiguration motionPictureConfiguration = new MotionPictureConfiguration();
	private static MotionPictureDeteriorationStage motionPictureDeteriorationStage = new MotionPictureDeteriorationStage();
	private static MotionPictureDimensions motionPictureDimensions = new MotionPictureDimensions();
	private static MotionPictureGeneration motionPictureGeneration = new MotionPictureGeneration();
	private static MotionPictureKindOfColourStock motionPictureKindOfColourStock = new MotionPictureKindOfColourStock();
	private static MotionPictureMediumForSound motionPictureMediumForSound = new MotionPictureMediumForSound();
	private static MotionPicturePolarity motionPicturePolarity = new MotionPicturePolarity();

	private static MotionPicturePresentationFormat motionPicturePresentationFormat =
		new MotionPicturePresentationFormat();

	private static MotionPictureProductionElements motionPictureProductionElements =
		new MotionPictureProductionElements();

	private static MotionPictureRefinedCategoriesOfColour motionPictureRefinedCategoriesOfColour =
		new MotionPictureRefinedCategoriesOfColour();

	private static MotionPictureSpecificMaterialDesignation motionPictureSpecificMaterialDesignation =
		new MotionPictureSpecificMaterialDesignation();

	private static MusicFormat musicFormat = new MusicFormat();

	private static MusicFormOfComposition musicFormOfComposition =
		new MusicFormOfComposition();

	private static MusicLiteraryText musicLiteraryText =
		new MusicLiteraryText();

	private static MusicMaterialType musicMaterialType =
		new MusicMaterialType();

	private static MusicTextualMaterial musicTextualMaterial =
		new MusicTextualMaterial();
	
	/* Bug 4161 */
	private static MusicParts musicParts = new MusicParts();
	private static MusicTranspositionArrangement musicTranspositionArrangement = new MusicTranspositionArrangement();

	private static NatureOfContent natureOfContent = new NatureOfContent();

	private static NonprojectedGraphicColour nonprojectedGraphicColour =
		new NonprojectedGraphicColour();

	private static NonprojectedGraphicPrimarySupportMaterial nonprojectedGraphicPrimarySupportMaterial =
		new NonprojectedGraphicPrimarySupportMaterial();

	private static NonprojectedGraphicSecondarySupportMaterial nonprojectedGraphicSecondarySupportMaterial =
		new NonprojectedGraphicSecondarySupportMaterial();

	private static NonprojectedGraphicSpecificMaterialDesignation nonprojectedGraphicSpecificMaterialDesignation =
		new NonprojectedGraphicSpecificMaterialDesignation();

	private static NonUniqueName nonUniqueName =
		new NonUniqueName();

	private static NotatedMusicSpecificMaterialDesignation notatedMusicSpecificMaterialDesignation =
		new NotatedMusicSpecificMaterialDesignation();

	private static NoteGeneration noteGeneration = new NoteGeneration();

	private static PrintConstant printConstant = new PrintConstant();

	private static ProjectedGraphicBaseOfEmulsion projectedGraphicBaseOfEmulsion =
		new ProjectedGraphicBaseOfEmulsion();

	private static ProjectedGraphicColour projectedGraphicColour =
		new ProjectedGraphicColour();

	private static ProjectedGraphicDimensions projectedGraphicDimensions =
		new ProjectedGraphicDimensions();

	private static ProjectedGraphicMediumForSound projectedGraphicMediumForSound =
		new ProjectedGraphicMediumForSound();

	private static ProjectedGraphicSecondarySupportMaterial projectedGraphicSecondarySupportMaterial =
		new ProjectedGraphicSecondarySupportMaterial();

	private static ProjectedGraphicSpecificMaterialDesignation projectedGraphicSpecificMaterialDesignation =
		new ProjectedGraphicSpecificMaterialDesignation();

	private static RecordModification recordModification =
		new RecordModification();

	private static RecordRevision recordRevision =
		new RecordRevision();

	private static RecordStatus recordStatus = new RecordStatus();

	private static RecordType recordType =
		new RecordType();

	private static ReferenceHeadingType referenceHeadingType =
		new ReferenceHeadingType();

	private static ReferenceStatus referenceStatus =
		new ReferenceStatus();

	private static RelationReciprocal relationReciprocal =
		new RelationReciprocal();

	private static RemoteSensingImageAltitudeOfSensor remoteSensingImageAltitudeOfSensor =
		new RemoteSensingImageAltitudeOfSensor();

	private static RemoteSensingImageAttitudeOfSensor remoteSensingImageAttitudeOfSensor =
		new RemoteSensingImageAttitudeOfSensor();

	private static RemoteSensingImageCloudCover remoteSensingImageCloudCover =
		new RemoteSensingImageCloudCover();

	private static RemoteSensingImageDataType remoteSensingImageDataType =
		new RemoteSensingImageDataType();

	private static RemoteSensingImagePlatformConstructionType remoteSensingImagePlatformConstructionType =
		new RemoteSensingImagePlatformConstructionType();

	private static RemoteSensingImagePlatformUse remoteSensingImagePlatformUse =
		new RemoteSensingImagePlatformUse();

	private static RemoteSensingImageSensorType remoteSensingImageSensorType =
		new RemoteSensingImageSensorType();

	private static RemoteSensingImageSpecificMaterialDesignation remoteSensingImageSpecificMaterialDesignation =
		new RemoteSensingImageSpecificMaterialDesignation();

	private static RomanizationScheme romanizationScheme =
		new RomanizationScheme();

	private static SerialFormOriginalItem serialFormOriginalItem =
		new SerialFormOriginalItem();

	private static SerialFrequency serialFrequency = new SerialFrequency();

	private static SerialOriginalAlphabetOfTitle serialOriginalAlphabetOfTitle =
		new SerialOriginalAlphabetOfTitle();

	private static SerialRegularity serialRegularity = new SerialRegularity();

	private static SerialSuccessiveLatest serialSuccessiveLatest =
		new SerialSuccessiveLatest();

	private static SerialType serialType = new SerialType();

	private static SeriesEntryIndicator seriesEntryIndicator =
		new SeriesEntryIndicator();

	private static SeriesNumbering seriesNumbering =
		new SeriesNumbering();

	private static SeriesType seriesType =
		new SeriesType();

	private static SoundConfiguration soundConfiguration =
		new SoundConfiguration();

	private static SoundCuttingType soundCuttingType = new SoundCuttingType();

	private static SoundDimensions soundDimensions = new SoundDimensions();

	private static SoundDiscType soundDiscType = new SoundDiscType();

	private static SoundGrooveWidth soundGrooveWidth = new SoundGrooveWidth();

	private static SoundMaterialType soundMaterialType =
		new SoundMaterialType();

	private static SoundSpecialPlaybackCharacteristics soundSpecialPlaybackCharacteristics =
		new SoundSpecialPlaybackCharacteristics();

	private static SoundSpecificMaterialDesignation soundSpecificMaterialDesignation =
		new SoundSpecificMaterialDesignation();

	private static SoundSpeed soundSpeed = new SoundSpeed();

	private static SoundStorageTechnique soundStorageTechnique =
		new SoundStorageTechnique();

	private static SoundTapeConfiguration soundTapeConfiguration =
		new SoundTapeConfiguration();

	private static SoundTapeWidth soundTapeWidth = new SoundTapeWidth();

	private static SubDivisionType subDivisionType =
		new SubDivisionType();

	private static SubjectDescriptor subjectDescriptor =
		new SubjectDescriptor();

	private static SubjectEntryIndicator subjectEntryIndicator =
		new SubjectEntryIndicator();

	private static SubjectSystem subjectSystem =
		new SubjectSystem();

	private static TactileMaterialBrailleMusicFormat tactileMaterialBrailleMusicFormat =
		new TactileMaterialBrailleMusicFormat();

	private static TactileMaterialClassOfBrailleWriting tactileMaterialClassOfBrailleWriting =
		new TactileMaterialClassOfBrailleWriting();

	private static TactileMaterialLevelOfContraction tactileMaterialLevelOfContraction =
		new TactileMaterialLevelOfContraction();

	private static TactileMaterialSpecificMaterialDesignation tactileMaterialSpecificMaterialDesignation =
		new TactileMaterialSpecificMaterialDesignation();

	private static TactileMaterialSpecificPhysicalCharacteristics tactileMaterialSpecificPhysicalCharacteristics =
		new TactileMaterialSpecificPhysicalCharacteristics();

	private static TargetAudience targetAudience = new TargetAudience();

	private static TextSpecificMaterialDesignation textSpecificMaterialDesignation =
		new TextSpecificMaterialDesignation();

	private static UnspecifiedSpecificMaterialDesignation unspecifiedSpecificMaterialDesignation =
		new UnspecifiedSpecificMaterialDesignation();

	private static VerificationLevel verificationLevel =
		new VerificationLevel();

	private static VideoRecordingColour videoRecordingColour =
		new VideoRecordingColour();

	private static VideoRecordingConfiguration videoRecordingConfiguration =
		new VideoRecordingConfiguration();

	private static VideoRecordingDimensions videoRecordingDimensions =
		new VideoRecordingDimensions();

	private static VideoRecordingFormat videoRecordingFormat =
		new VideoRecordingFormat();

	private static VideoRecordingMediumForSound videoRecordingMediumForSound =
		new VideoRecordingMediumForSound();

	private static VideoRecordingSpecificMaterialDesignation videoRecordingSpecificMaterialDesignation =
		new VideoRecordingSpecificMaterialDesignation();

	private static VisualMaterialType visualMaterialType =
		new VisualMaterialType();

	private static VisualTargetAudience visualTargetAudience =
		new VisualTargetAudience();

	private static VisualTechnique visualTechnique = new VisualTechnique();

	private static VisualType visualType = new VisualType();

	private static MarcHelperLabel marcHelperLabel =
		new MarcHelperLabel();
	
	public static AuthorityDisplayFormat getAuthorityDisplayFormat() {
		return authorityDisplayFormat;
	}

	public static AuthorityEncodingLevel getAuthorityEncodingLevel() {
		return authorityEncodingLevel;
	}

	public static AuthorityRecordStatus getAuthorityRecordStatus() {
		return authorityRecordStatus;
	}

	public static AuthorityStructure getAuthorityStructure() {
		return authorityStructure;
	}

	public static AuthorityTagCategory getAuthorityTagCategory() {
		return authorityTagCategory;
	}

	public static BibHeader getBibHeader() {
		return bibHeader;
	}

	public static BibliographicDisplayFormat getBibliographicDisplayFormat() {
		return bibliographicDisplayFormat;
	}

	public static BibliographicTagCategory getBibliographicTagCategory() {
		return bibliographicTagCategory;
	}

	public static BilingualUsage getBilingualUsage() {
		return bilingualUsage;
	}

	public static BookBiography getBookBiography() {
		return bookBiography;
	}

	public static BookFestschrift getBookFestschrift() {
		return bookFestschrift;
	}

	public static BookIllustration getBookIllustration() {
		return bookIllustration;
	}

	public static BookIndexAvailability getBookIndexAvailability() {
		return bookIndexAvailability;
	}

	public static BookLiteraryForm getBookLiteraryForm() {
		return bookLiteraryForm;
	}

	public static BookMaterialType getBookMaterialType() {
		return bookMaterialType;
	}

	public static CartographicFormat getCartographicFormat() {
		return cartographicFormat;
	}

	public static CartographicIndexAvailability getCartographicIndexAvailability() {
		return cartographicIndexAvailability;
	}

	public static CartographicMaterial getCartographicMaterial() {
		return cartographicMaterial;
	}

	public static CartographicProjection getCartographicProjection() {
		return cartographicProjection;
	}

	public static CartographicRelief getCartographicRelief() {
		return cartographicRelief;
	}

	public static CataloguingRules getCataloguingRules() {
		return cataloguingRules;
	}

	public static CataloguingSource getCataloguingSource() {
		return cataloguingSource;
	}

	public static CataloguingSourceCode getCataloguingSourceCode() {
		return CataloguingSourceCode;
	}

	public static CharacterCodingScheme getCharacterCodingScheme() {
		return characterCodingScheme;
	}

	public static Collection getCollection() {
		return collection;
	}

	public static ComputerFileAntecedentSource getComputerFileAntecedentSource() {
		return computerFileAntecedentSource;
	}

	public static ComputerFileColour getComputerFileColour() {
		return computerFileColour;
	}

	public static ComputerFileDimensions getComputerFileDimensions() {
		return computerFileDimensions;
	}

	public static ComputerFileFormat getComputerFileFormats() {
		return computerFileFormats;
	}
	
	/* Bug 4119 */
	public static ComputerFileForm getComputerFileForm() {
		return computerFileForm;
	}

	public static ComputerFileLevelOfCompression getComputerFileLevelOfCompression() {
		return computerFileLevelOfCompression;
	}

	public static ComputerFileQualityAssuranceTarget getComputerFileQualityAssuranceTarget() {
		return computerFileQualityAssuranceTarget;
	}

	public static ComputerFileReformattingQuality getComputerFileReformattingQuality() {
		return computerFileReformattingQuality;
	}

	public static ComputerFileSpecificMaterialDesignation getComputerFileSpecificMaterialDesignation() {
		return computerFileSpecificMaterialDesignation;
	}

	public static ComputerFileType getComputerFileType() {
		return computerFileType;
	}

	public static ComputerTargetAudience getComputerTargetAudience() {
		return computerTargetAudience;
	}

	public static ConferencePublication getConferencePublication() {
		return conferencePublication;
	}

	public static ControlType getControlType() {
		return controlType;
	}

	public static CrossReferenceType getCrossReferenceType() {
		return crossReferenceType;
	}

	public static DescriptiveCataloguing getDescriptiveCataloguing() {
		return descriptiveCataloguing;
	}

	public static DualReference getDualReference() {
		return dualReference;
	}

	public static LinkDisplay getLinkDisplay() {
		return linkDisplay;
	}

	public static ReplacementComplexity getReplacementComplexity() {
		return replacementComplexity;
	}

	public static EarlierRules getEarlierRules() {
		return earlierRules;
	}

	public static EncodingLevel getEncodingLevel() {
		return encodingLevel;
	}

	public static FormOfItem getFormOfItem() {
		return formOfItem;
	}

	public static GlobeColour getGlobeColour() {
		return globeColour;
	}

	public static GlobePhysicalMedium getGlobePhysicalMedium() {
		return globePhysicalMedium;
	}

	public static GlobeReproductionType getGlobeReproductionType() {
		return globeReproductionType;
	}

	public static GlobeSpecificMaterialDesignation getGlobeSpecificMaterialDesignation() {
		return globeSpecificMaterialDesignation;
	}

	public static GovernmentAgency getGovernmentAgency() {
		return governmentAgency;
	}

	public static GovernmentPublication getGovernmentPublication() {
		return governmentPublication;
	}

	public static HeadingStatus getHeadingStatus() {
		return headingStatus;
	}

	public static IncludesSound getIncludesSound() {
		return includesSound;
	}

	public static ItemBibliographicLevel getItemBibliographicLevel() {
		return itemBibliographicLevel;
	}

	public static ItemDateType getItemDateType() {
		return itemDateType;
	}

	public static ItemRecordType getItemRecordType() {
		return itemRecordType;
	}

	public static KitSpecificMaterialDesignation getKitSpecificMaterialDesignation() {
		return kitSpecificMaterialDesignation;
	}

	public static Language getLanguage() {
		return language;
	}

	public static LinkedRecord getLinkedRecord() {
		return linkedRecord;
	}

	public static MainAddedEntryIndicator getMainAddedEntryIndicator() {
		return mainAddedEntryIndicator;
	}

	public static MapColour getMapColour() {
		return mapColour;
	}

	public static MapMaterialType getMapMaterialType() {
		return mapMaterialType;
	}

	public static MapPhysicalMedium getMapPhysicalMedium() {
		return mapPhysicalMedium;
	}

	public static MapPolarity getMapPolarity() {
		return mapPolarity;
	}

	public static MapProductionDetails getMapProductionDetails() {
		return mapProductionDetails;
	}

	public static MapReproductionType getMapReproductionType() {
		return mapReproductionType;
	}

	public static MapSpecificMaterialDesignation getMapSpecificMaterialDesignation() {
		return mapSpecificMaterialDesignation;
	}

	public static MarcCountry getMarcCountry() {
		return marcCountry;
	}

	public static MaterialType getMaterialType() {
		return materialType;
	}

	public static MicroformBaseOfFilm getMicroformBaseOfFilm() {
		return microformBaseOfFilm;
	}

	public static MicroformColour getMicroformColour() {
		return microformColour;
	}

	public static MicroformDimensions getMicroformDimensions() {
		return microformDimensions;
	}

	public static MicroformEmulsionOnFilm getMicroformEmulsionOnFilm() {
		return microformEmulsionOnFilm;
	}

	public static MicroformGeneration getMicroformGeneration() {
		return microformGeneration;
	}

	public static MicroformPolarity getMicroformPolarity() {
		return microformPolarity;
	}

	public static MicroformReductionRatioRange getMicroformReductionRatioRange() {
		return microformReductionRatioRange;
	}

	public static MicroformSpecificMaterialDesignation getMicroformSpecificMaterialDesignation() {
		return microformSpecificMaterialDesignation;
	}

	public static ModifiedRecord getModifiedRecord() {
		return modifiedRecord;
	}

	public static MotionPictureBaseOfFilm getMotionPictureBaseOfFilm() {
		return motionPictureBaseOfFilm;
	}

	public static MotionPictureColour getMotionPictureColour() {
		return motionPictureColour;
	}

	public static MotionPictureCompleteness getMotionPictureCompleteness() {
		return motionPictureCompleteness;
	}

	public static MotionPictureConfiguration getMotionPictureConfiguration() {
		return motionPictureConfiguration;
	}

	public static MotionPictureDeteriorationStage getMotionPictureDeteriorationStage() {
		return motionPictureDeteriorationStage;
	}

	public static MotionPictureDimensions getMotionPictureDimensions() {
		return motionPictureDimensions;
	}

	public static MotionPictureGeneration getMotionPictureGeneration() {
		return motionPictureGeneration;
	}

	public static MotionPictureKindOfColourStock getMotionPictureKindOfColourStock() {
		return motionPictureKindOfColourStock;
	}

	public static MotionPictureMediumForSound getMotionPictureMediumForSound() {
		return motionPictureMediumForSound;
	}

	public static MotionPicturePolarity getMotionPicturePolarity() {
		return motionPicturePolarity;
	}

	public static MotionPicturePresentationFormat getMotionPicturePresentationFormat() {
		return motionPicturePresentationFormat;
	}

	public static MotionPictureProductionElements getMotionPictureProductionElements() {
		return motionPictureProductionElements;
	}

	public static MotionPictureRefinedCategoriesOfColour getMotionPictureRefinedCategoriesOfColour() {
		return motionPictureRefinedCategoriesOfColour;
	}

	public static MotionPictureSpecificMaterialDesignation getMotionPictureSpecificMaterialDesignation() {
		return motionPictureSpecificMaterialDesignation;
	}

	public static MusicFormat getMusicFormat() {
		return musicFormat;
	}

	public static MusicFormOfComposition getMusicFormOfComposition() {
		return musicFormOfComposition;
	}

	public static MusicLiteraryText getMusicLiteraryText() {
		return musicLiteraryText;
	}

	public static MusicMaterialType getMusicMaterialType() {
		return musicMaterialType;
	}

	public static MusicTextualMaterial getMusicTextualMaterial() {
		return musicTextualMaterial;
	}
	
	/* Bug 4161 inizio */
	public static MusicParts getMusicParts(){
		return musicParts;
	}
	public static MusicTranspositionArrangement getMusicTranspositionArrangement(){
		return musicTranspositionArrangement;
	}
	/* Bug 4161 fine */

	public static NatureOfContent getNatureOfContent() {
		return natureOfContent;
	}

	public static NonprojectedGraphicColour getNonprojectedGraphicColour() {
		return nonprojectedGraphicColour;
	}

	public static NonprojectedGraphicPrimarySupportMaterial getNonprojectedGraphicPrimarySupportMaterial() {
		return nonprojectedGraphicPrimarySupportMaterial;
	}

	public static NonprojectedGraphicSecondarySupportMaterial getNonprojectedGraphicSecondarySupportMaterial() {
		return nonprojectedGraphicSecondarySupportMaterial;
	}

	public static NonprojectedGraphicSpecificMaterialDesignation getNonprojectedGraphicSpecificMaterialDesignation() {
		return nonprojectedGraphicSpecificMaterialDesignation;
	}

	public static NonUniqueName getNonUniqueName() {
		return nonUniqueName;
	}

	public static NotatedMusicSpecificMaterialDesignation getNotatedMusicSpecificMaterialDesignation() {
		return notatedMusicSpecificMaterialDesignation;
	}

	public static NoteGeneration getNoteGeneration() {
		return noteGeneration;
	}

	public static PrintConstant getPrintConstant() {
		return printConstant;
	}

	public static ProjectedGraphicBaseOfEmulsion getProjectedGraphicBaseOfEmulsion() {
		return projectedGraphicBaseOfEmulsion;
	}

	public static ProjectedGraphicColour getProjectedGraphicColour() {
		return projectedGraphicColour;
	}

	public static ProjectedGraphicDimensions getProjectedGraphicDimensions() {
		return projectedGraphicDimensions;
	}

	public static ProjectedGraphicMediumForSound getProjectedGraphicMediumForSound() {
		return projectedGraphicMediumForSound;
	}

	public static ProjectedGraphicSecondarySupportMaterial getProjectedGraphicSecondarySupportMaterial() {
		return projectedGraphicSecondarySupportMaterial;
	}

	public static ProjectedGraphicSpecificMaterialDesignation getProjectedGraphicSpecificMaterialDesignation() {
		return projectedGraphicSpecificMaterialDesignation;
	}

	public static RecordModification getRecordModification() {
		return recordModification;
	}

	public static RecordRevision getRecordRevision() {
		return recordRevision;
	}

	public static RecordStatus getRecordStatus() {
		return recordStatus;
	}

	public static RecordType getRecordType() {
		return recordType;
	}

	public static ReferenceHeadingType getReferenceHeadingType() {
		return referenceHeadingType;
	}

	public static ReferenceStatus getReferenceStatus() {
		return referenceStatus;
	}

	public static RelationReciprocal getRelationReciprocal() {
		return relationReciprocal;
	}

	public static RemoteSensingImageAltitudeOfSensor getRemoteSensingImageAltitudeOfSensor() {
		return remoteSensingImageAltitudeOfSensor;
	}

	public static RemoteSensingImageAttitudeOfSensor getRemoteSensingImageAttitudeOfSensor() {
		return remoteSensingImageAttitudeOfSensor;
	}

	public static RemoteSensingImageCloudCover getRemoteSensingImageCloudCover() {
		return remoteSensingImageCloudCover;
	}

	public static RemoteSensingImageDataType getRemoteSensingImageDataType() {
		return remoteSensingImageDataType;
	}

	public static RemoteSensingImagePlatformConstructionType getRemoteSensingImagePlatformConstructionType() {
		return remoteSensingImagePlatformConstructionType;
	}

	public static RemoteSensingImagePlatformUse getRemoteSensingImagePlatformUse() {
		return remoteSensingImagePlatformUse;
	}

	public static RemoteSensingImageSensorType getRemoteSensingImageSensorType() {
		return remoteSensingImageSensorType;
	}

	public static RemoteSensingImageSpecificMaterialDesignation getRemoteSensingImageSpecificMaterialDesignation() {
		return remoteSensingImageSpecificMaterialDesignation;
	}

	public static RomanizationScheme getRomanizationScheme() {
		return romanizationScheme;
	}

	public static SerialFormOriginalItem getSerialFormOriginalItem() {
		return serialFormOriginalItem;
	}

	public static SerialFrequency getSerialFrequency() {
		return serialFrequency;
	}

	public static SerialOriginalAlphabetOfTitle getSerialOriginalAlphabetOfTitle() {
		return serialOriginalAlphabetOfTitle;
	}

	public static SerialRegularity getSerialRegularity() {
		return serialRegularity;
	}

	public static SerialSuccessiveLatest getSerialSuccessiveLatest() {
		return serialSuccessiveLatest;
	}

	public static SerialType getSerialType() {
		return serialType;
	}

	public static SeriesEntryIndicator getSeriesEntryIndicator() {
		return seriesEntryIndicator;
	}

	public static SeriesNumbering getSeriesNumbering() {
		return seriesNumbering;
	}

	public static SeriesType getSeriesType() {
		return seriesType;
	}

	public static SoundConfiguration getSoundConfiguration() {
		return soundConfiguration;
	}

	public static SoundCuttingType getSoundCuttingType() {
		return soundCuttingType;
	}

	public static SoundDimensions getSoundDimensions() {
		return soundDimensions;
	}

	public static SoundDiscType getSoundDiscType() {
		return soundDiscType;
	}

	public static SoundGrooveWidth getSoundGrooveWidth() {
		return soundGrooveWidth;
	}

	public static SoundMaterialType getSoundMaterialType() {
		return soundMaterialType;
	}

	public static SoundSpecialPlaybackCharacteristics getSoundSpecialPlaybackCharacteristics() {
		return soundSpecialPlaybackCharacteristics;
	}

	public static SoundSpecificMaterialDesignation getSoundSpecificMaterialDesignation() {
		return soundSpecificMaterialDesignation;
	}

	public static SoundSpeed getSoundSpeed() {
		return soundSpeed;
	}

	public static SoundStorageTechnique getSoundStorageTechnique() {
		return soundStorageTechnique;
	}

	public static SoundTapeConfiguration getSoundTapeConfiguration() {
		return soundTapeConfiguration;
	}

	public static SoundTapeWidth getSoundTapeWidth() {
		return soundTapeWidth;
	}

	public static SubDivisionType getSubDivisionType() {
		return subDivisionType;
	}

	public static SubjectDescriptor getSubjectDescriptor() {
		return subjectDescriptor;
	}

	public static SubjectEntryIndicator getSubjectEntryIndicator() {
		return subjectEntryIndicator;
	}

	public static SubjectSystem getSubjectSystem() {
		return subjectSystem;
	}

	public static TactileMaterialBrailleMusicFormat getTactileMaterialBrailleMusicFormat() {
		return tactileMaterialBrailleMusicFormat;
	}

	public static TactileMaterialClassOfBrailleWriting getTactileMaterialClassOfBrailleWriting() {
		return tactileMaterialClassOfBrailleWriting;
	}

	public static TactileMaterialLevelOfContraction getTactileMaterialLevelOfContraction() {
		return tactileMaterialLevelOfContraction;
	}

	public static TactileMaterialSpecificMaterialDesignation getTactileMaterialSpecificMaterialDesignation() {
		return tactileMaterialSpecificMaterialDesignation;
	}

	public static TactileMaterialSpecificPhysicalCharacteristics getTactileMaterialSpecificPhysicalCharacteristics() {
		return tactileMaterialSpecificPhysicalCharacteristics;
	}

	public static TargetAudience getTargetAudience() {
		return targetAudience;
	}

	public static TextSpecificMaterialDesignation getTextSpecificMaterialDesignation() {
		return textSpecificMaterialDesignation;
	}

	public static UnspecifiedSpecificMaterialDesignation getUnspecifiedSpecificMaterialDesignation() {
		return unspecifiedSpecificMaterialDesignation;
	}

	public static VerificationLevel getVerificationLevel() {
		return verificationLevel;
	}

	public static VideoRecordingColour getVideoRecordingColour() {
		return videoRecordingColour;
	}

	public static VideoRecordingConfiguration getVideoRecordingConfiguration() {
		return videoRecordingConfiguration;
	}

	public static VideoRecordingDimensions getVideoRecordingDimensions() {
		return videoRecordingDimensions;
	}

	public static VideoRecordingFormat getVideoRecordingFormat() {
		return videoRecordingFormat;
	}

	public static VideoRecordingMediumForSound getVideoRecordingMediumForSound() {
		return videoRecordingMediumForSound;
	}
	
	public static VideoRecordingSpecificMaterialDesignation getVideoRecordingSpecificMaterialDesignation() {
		return videoRecordingSpecificMaterialDesignation;
	}

	public static VisualMaterialType getVisualMaterialType() {
		return visualMaterialType;
	}

	public static VisualTargetAudience getVisualTargetAudience() {
		return visualTargetAudience;
	}

	public static VisualTechnique getVisualTechnique() {
		return visualTechnique;
	}

	public static VisualType getVisualType() {
		return visualType;
	}

	public static MarcHelperLabel getMarcHelperLabel() {
		return marcHelperLabel;
	}
	private static SubjectTermTyp subjectTermTyp =
		new SubjectTermTyp();
	
	public static SubjectTermTyp getSubjectTermType() {
		return subjectTermTyp;
	}
	
	private static DigitalTyp digitalTyp = new DigitalTyp();
	
	public static DigitalTyp getDigitalTyp() {
		return digitalTyp;
	}
	
	private static TagGroupSubjectType tagGroupSubjectType= new TagGroupSubjectType();
	
	public static TagGroupSubjectType getTagGroupSubjectType() {
		return tagGroupSubjectType;
	}
   private static GeneralMaterialDesignation generalMaterialDesignation= new GeneralMaterialDesignation();
	
	public static GeneralMaterialDesignation getGeneralMaterialDesignation() {
		return generalMaterialDesignation;
	}
	
	/*20100729: Aggiunta tabella di decodifica T_FORMAT_REC come tendina */ 
	private static FormatRecordType formatRecordType = new FormatRecordType();
	
	public static FormatRecordType getFormatRecordType() {
		return formatRecordType;
	}
	
	/* Bug 4306 */
	private static RelatorTermType relatorTermType  = new RelatorTermType();
	
	public static RelatorTermType getRelatorTermType() {
		return relatorTermType;
	}
	
	public static DatabaseViewList getDatabaseViewList() {
		return databaseViewList;
	}
		
	private static DatabaseViewList databaseViewList = new DatabaseViewList(); //pm 2011

	public static Entity getEntity() {
		return entity;
	}
}