package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * FixedFieldCodesGroup
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "itemRecordStatusCode", "itemRecordTypeCode", "itemBibliographicLevelCode", "itemControlTypeCode", "characterEncodingSchemas", "encodingLevel", "descriptiveCatalogForms",
        "linkedRecordCode", "dateTypes", "bookIllustrationCode1", "bookIllustrationCode2", "bookIllustrationCode3", "bookIllustrationCode4", "targetAudienceCode",
        "formOfItemCode", "natureOfContent1", "natureOfContent2", "natureOfContent3", "natureOfContent4", "governmentPublicationCode", "conferencePublicationCode",
        "bookFestschrift", "bookIndexAvailabilityCode", "bookLiteraryFormTypeCode", "bookBiographyCode", "musicFormOfCompositions", "musicParts", "musicFormats",
        "musicTextualMaterialCodes", "musicLiteraryTextCodes", "musicTranspositionArrangementCode",
        "serialFrequencyCode", "serialRegularityCode", "serialTypeOfContinuingResourceCodes", "serialFormOriginalCodes", "serialNatureOfWorkCodes", "serialOriginAlphabetCodes", "serialEntryConvCodes",
        "mapReliefCodes", "mapProjectionCodes", "mapTypeCartographicMaterialCodes", "mapIndexCodes", "mapSpecialFormatCharacteristicCodes",
        "visualTargetAudienceCodes", "visualTypeMaterialCodes", "visualTechnique",
        "computerTargetAudienceCodes", "computerFormOfItemCodes", "computerTypeMaterialCodes",
        "modifiedRecordTypes", "catalogSources",
        "categoryOfMaterial", "specificMaterialDesignationCode", "colourCode", "physicalMediumCode", "mediumForSoundCode", "secondarySupportMaterialCode",
        "typeOfReproductionCode", "polarityCode", "dimensionCodes", "primarySupportMaterialCode", "configurationCode", "generationCode", "baseOfFilmCode",
        "productionDetailsCode", "includesSoundCodes", "fileFormatsCode", "qualityAssuranceTargetCode", "antecedentSourceCode", "levelOfCompressionCode",
        "reformattingQualityCode", "classOfBrailleWritingCodes", "levelOfContractionCode", "brailleMusicFormatCodes", "specificPhysicalCharacteristicsCode",
        "baseOfEmulsionCode", "soundOnMediumOrSeparateCode", "presentationFormatCode", "productionElementsCode", "refinedCategoriesOfColourCode",
        "kindOfColourStockCode", "deteriorationStageCode", "completenessCode", "altitudeOfSensorCode", "attitudeOfSensorCode", "cloudCoverCode",
        "platformConstructionTypeCode", "platformUseCode", "sensorTypeCode", "remoteSensingDataTypeCodes", "speedCode", "grooveWidthCode", "tapeWidthCode",
        "tapeConfigurationCode", "discTypeCode", "sndMaterialTypeCode", "cuttingTypeCode", "specialPlaybackCharacteristicsCode", "storageTechniqueCode",
        "videoRecordingFormatCodes", "emulsionOnFilmCode", "reductionRatioRangeCode"})
/*


 */
public class FixedFieldCodesGroup {

    @JsonProperty("itemRecordStatusCode")
    @Valid
    private List<Pair> recordStatusTypes = new ArrayList<Pair>();

    @JsonProperty("itemRecordTypeCode")
    @Valid
    private List<Pair> recordTypes = new ArrayList<Pair>();

    @JsonProperty("itemBibliographicLevelCode")
    @Valid
    private List<Pair> bibliographicLevels = new ArrayList<Pair>();

    @JsonProperty("itemControlTypeCode")
    @Valid
    private List<Pair> controlTypes = new ArrayList<Pair>();

    @JsonProperty("characterEncodingSchemas")
    @Valid
    private List<Pair> characterEncodingSchemas = new ArrayList<Pair>();

    @JsonProperty("encodingLevel")
    @Valid
    private List<Pair> encodingLevels = new ArrayList<Pair>();

    @JsonProperty("descriptiveCatalogForms")
    @Valid
    private List<Pair> descriptiveCatalogForms = new ArrayList<Pair>();

    @JsonProperty("linkedRecordCode")
    @Valid
    private List<Pair> linkedRecordCodes = new ArrayList<Pair>();

    @JsonProperty("dateTypes")
    @Valid
    private List<Pair> dateTypes = new ArrayList<Pair>();

    @JsonProperty("bookIllustrationCode1")
    @Valid
    private List<Pair> bookIllustrationCodes1 = new ArrayList<Pair>();

    @JsonProperty("bookIllustrationCode2")
    @Valid
    private List<Pair> bookIllustrationCodes2 = new ArrayList<Pair>();

    @JsonProperty("bookIllustrationCode3")
    @Valid
    private List<Pair> bookIllustrationCodes3 = new ArrayList<Pair>();

    @JsonProperty("bookIllustrationCode4")
    @Valid
    private List<Pair> bookIllustrationCodes4 = new ArrayList<Pair>();

    @JsonProperty("targetAudienceCode")
    @Valid
    private List<Pair> targetAudienceCodes = new ArrayList<Pair>();

    @JsonProperty("formOfItemCode")
    @Valid
    private List<Pair> formOfItemCodes = new ArrayList<Pair>();

    @JsonProperty("natureOfContent1")
    @Valid
    private List<Pair> natureOfContents1 = new ArrayList<Pair>();

    @JsonProperty("natureOfContent2")
    @Valid
    private List<Pair> natureOfContents2 = new ArrayList<Pair>();

    @JsonProperty("natureOfContent3")
    @Valid
    private List<Pair> natureOfContents3 = new ArrayList<Pair>();

    @JsonProperty("natureOfContent4")
    @Valid
    private List<Pair> natureOfContents4 = new ArrayList<Pair>();

    @JsonProperty("governmentPublicationCode")
    @Valid
    private List<Pair> governmentPublicationCodes = new ArrayList<Pair>();

    @JsonProperty("conferencePublicationCode")
    @Valid
    private List<Pair> conferencePublicationCodes = new ArrayList<Pair>();

    @JsonProperty("bookFestschrift")
    @Valid
    private List<Pair> bookFestschrifts = new ArrayList<Pair>();

    @JsonProperty("bookIndexAvailabilityCode")
    @Valid
    private List<Pair> bookIndexAvailabilityCodes = new ArrayList<Pair>();

    @JsonProperty("bookLiteraryFormTypeCode")
    @Valid
    private List<Pair> bookLiteraryFormTypeCodes = new ArrayList<Pair>();

    @JsonProperty("bookBiographyCode")
    @Valid
    private List<Pair> bookBiographyCodes = new ArrayList<Pair>();

    @JsonProperty("musicFormOfCompositions")
    @Valid
    private List<Pair> musicFormOfCompositionCodes = new ArrayList<Pair>();

    @JsonProperty("musicParts")
    @Valid
    private List<Pair> musicPartCodes = new ArrayList<Pair>();

    @JsonProperty("musicFormats")
    @Valid
    private List<Pair> musicFormatCodes = new ArrayList<Pair>();

    @JsonProperty("musicTextualMaterialCodes")
    @Valid
    private List<Pair> musicTextualMaterialCodes = new ArrayList<Pair>();

    @JsonProperty("musicLiteraryTextCodes")
    @Valid
    private List<Pair> musicLiteraryTextCodes = new ArrayList<Pair>();

    @JsonProperty("musicTranspositionArrangementCode")
    @Valid
    private List<Pair> musicTranspositionArrangementCodes = new ArrayList<Pair>();

    @JsonProperty("serialFrequencyCode")
    @Valid
    private List<Pair> serialFrequencyCodes = new ArrayList<Pair>();

    @JsonProperty("serialRegularityCode")
    @Valid
    private List<Pair> serialRegularityCodes = new ArrayList<Pair>();

    @JsonProperty("serialTypeOfContinuingResourceCodes")
    @Valid
    private List<Pair> serialTypeOfContinuingResourceCodes = new ArrayList<Pair>();

    @JsonProperty("serialFormOriginalCodes")
    @Valid
    private List<Pair> serialFormOriginalCodes = new ArrayList<Pair>();

    @JsonProperty("serialNatureOfWorkCodes")
    @Valid
    private List<Pair> serialNatureOfWorkCodes = new ArrayList<Pair>();

    @JsonProperty("serialOriginAlphabetCodes")
    @Valid
    private List<Pair> serialOriginAlphabetCodes = new ArrayList<Pair>();

    @JsonProperty("serialEntryConvCodes")
    @Valid
    private List<Pair> serialEntryConvCodes = new ArrayList<Pair>();

    @JsonProperty("mapReliefCodes")
    @Valid
    private List<Pair> mapReliefCodes = new ArrayList<Pair>();

    @JsonProperty("mapProjectionCodes")
    @Valid
    private List<Pair> mapProjectionCodes = new ArrayList<Pair>();

    @JsonProperty("mapTypeCartographicMaterialCodes")
    @Valid
    private List<Pair> mapTypeCartographicMaterialCodes = new ArrayList<Pair>();

    @JsonProperty("mapIndexCodes")
    @Valid
    private List<Pair> mapIndexCodes = new ArrayList<Pair>();

    @JsonProperty("mapSpecialFormatCharacteristicCodes")
    @Valid
    private List<Pair> mapSpecialFormatCharacteristicCodes = new ArrayList<Pair>();

    @JsonProperty("visualTargetAudienceCodes")
    @Valid
    private List<Pair> visualTargetAudienceCodes = new ArrayList<Pair>();

    @JsonProperty("visualTypeMaterialCodes")
    @Valid
    private List<Pair> visualTypeMaterialCodes = new ArrayList<Pair>();

    @JsonProperty("visualTechnique")
    @Valid
    private List<Pair> visualTechnique = new ArrayList<Pair>();

    @JsonProperty("computerTargetAudienceCodes")
    @Valid
    private List<Pair> computerTargetAudienceCodes = new ArrayList<Pair>();

    @JsonProperty("computerFormOfItemCodes")
    @Valid
    private List<Pair> computerFormOfItemCodes = new ArrayList<Pair>();

    @JsonProperty("computerTypeMaterialCodes")
    @Valid
    private List<Pair> computerTypeMaterialCodes = new ArrayList<Pair>();

    @JsonProperty("modifiedRecordTypes")
    @Valid
    private List<Pair> modifiedRecordTypes = new ArrayList<Pair>();

    @JsonProperty("catalogSources")
    @Valid
    private List<Pair> catalogSources = new ArrayList<Pair>();

    @JsonProperty("categoryOfMaterial")
    @Valid
    private List<Pair> categoryOfMaterialCodes = new ArrayList<Pair>();

    @JsonProperty("specificMaterialDesignationCode")
    @Valid
    private List<Pair> specificMaterialDesignationCodes = new ArrayList<Pair>();

    @JsonProperty("colourCode")
    @Valid
    private List<Pair> colorCodes = new ArrayList<Pair>();

    @JsonProperty("physicalMediumCode")
    @Valid
    private List<Pair> physicalMediumCodes = new ArrayList<Pair>();

    @JsonProperty("mediumForSoundCode")
    @Valid
    private List<Pair> mediumForSoundCodes = new ArrayList<Pair>();

    @JsonProperty("secondarySupportMaterialCode")
    @Valid
    private List<Pair> secondarySupportMaterialCodes = new ArrayList<Pair>();

    @JsonProperty("typeOfReproductionCode")
    @Valid
    private List<Pair> typeOfReproductionCodes = new ArrayList<Pair>();

    @JsonProperty("polarityCode")
    @Valid
    private List<Pair> polarityCodes = new ArrayList<Pair>();

    @JsonProperty("dimensionCodes")
    @Valid
    private List<Pair> dimensionCodes = new ArrayList<Pair>();

    @JsonProperty("primarySupportMaterialCode")
    @Valid
    private List<Pair> primarySupportMaterialCodes = new ArrayList<Pair>();

    @JsonProperty("configurationCode")
    @Valid
    private List<Pair> configurationCodes = new ArrayList<Pair>();

    @JsonProperty("generationCode")
    @Valid
    private List<Pair> generationCodes = new ArrayList<Pair>();

    @JsonProperty("baseOfFilmCode")
    @Valid
    private List<Pair> baseOfFilmCodes = new ArrayList<Pair>();

    @JsonProperty("productionDetailsCode")
    @Valid
    private List<Pair> productionDetailsCodes = new ArrayList<Pair>();

    @JsonProperty("includesSoundCodes")
    @Valid
    private List<Pair> includesSoundCodes = new ArrayList<Pair>();

    @JsonProperty("fileFormatsCode")
    @Valid
    private List<Pair> fileFormatsCodes = new ArrayList<Pair>();

    @JsonProperty("qualityAssuranceTargetCode")
    @Valid
    private List<Pair> qualityAssuranceTargetCodes = new ArrayList<Pair>();

    @JsonProperty("antecedentSourceCode")
    @Valid
    private List<Pair> antecedentSourceCodes = new ArrayList<Pair>();

    @JsonProperty("levelOfCompressionCode")
    @Valid
    private List<Pair> levelOfCompressionCodes = new ArrayList<Pair>();

    @JsonProperty("reformattingQualityCode")
    @Valid
    private List<Pair> reformattingQualityCodes = new ArrayList<Pair>();

    @JsonProperty("classOfBrailleWritingCodes")
    @Valid
    private List<Pair> classOfBrailleWritingCodes = new ArrayList<Pair>();

    @JsonProperty("levelOfContractionCode")
    @Valid
    private List<Pair> levelOfContractionCodes = new ArrayList<Pair>();

    @JsonProperty("brailleMusicFormatCodes")
    @Valid
    private List<Pair> brailleMusicFormatCodes = new ArrayList<Pair>();

    @JsonProperty("specificPhysicalCharacteristicsCode")
    @Valid
    private List<Pair> specificPhysicalCharacteristicsCodes = new ArrayList<Pair>();

    @JsonProperty("baseOfEmulsionCode")
    @Valid
    private List<Pair> baseOfEmulsionCodes = new ArrayList<Pair>();

    @JsonProperty("soundOnMediumOrSeparateCode")
    @Valid
    private List<Pair> soundOnMediumOrSeparateCodes = new ArrayList<Pair>();

    @JsonProperty("presentationFormatCode")
    @Valid
    private List<Pair> presentationFormatCodes = new ArrayList<Pair>();

    @JsonProperty("productionElementsCode")
    @Valid
    private List<Pair> productionElementsCodes = new ArrayList<Pair>();

    @JsonProperty("refinedCategoriesOfColourCode")
    @Valid
    private List<Pair> refinedCategoriesOfColourCodes = new ArrayList<Pair>();

    @JsonProperty("kindOfColourStockCode")
    @Valid
    private List<Pair> kindOfColourStockCodes = new ArrayList<Pair>();

    @JsonProperty("deteriorationStageCode")
    @Valid
    private List<Pair> deteriorationStageCodes = new ArrayList<Pair>();

    @JsonProperty("completenessCode")
    @Valid
    private List<Pair> completenessCodes = new ArrayList<Pair>();

    @JsonProperty("altitudeOfSensorCode")
    @Valid
    private List<Pair> altitudeOfSensorCodes = new ArrayList<Pair>();

    @JsonProperty("attitudeOfSensorCode")
    @Valid
    private List<Pair> attitudeOfSensorCodes = new ArrayList<Pair>();

    @JsonProperty("cloudCoverCode")
    @Valid
    private List<Pair> cloudCoverCodes = new ArrayList<Pair>();

    @JsonProperty("platformConstructionTypeCode")
    @Valid
    private List<Pair> platformConstructionTypeCodes = new ArrayList<Pair>();

    @JsonProperty("platformUseCode")
    @Valid
    private List<Pair> platformUseCodes = new ArrayList<Pair>();

    @JsonProperty("sensorTypeCode")
    @Valid
    private List<Pair> sensorTypeCodes = new ArrayList<Pair>();

    @JsonProperty("remoteSensingDataTypeCodes")
    @Valid
    private List<Pair> remoteSensingDataTypeCodes = new ArrayList<Pair>();

    @JsonProperty("speedCode")
    @Valid
    private List<Pair> speedCodes = new ArrayList<Pair>();

    @JsonProperty("grooveWidthCode")
    @Valid
    private List<Pair> grooveWidthCodes = new ArrayList<Pair>();

    @JsonProperty("tapeWidthCode")
    @Valid
    private List<Pair> tapeWidthCodes = new ArrayList<Pair>();

    @JsonProperty("tapeConfigurationCode")
    @Valid
    private List<Pair> tapeConfigurationCodes = new ArrayList<Pair>();

    @JsonProperty("discTypeCode")
    @Valid
    private List<Pair> discTypeCodes = new ArrayList<Pair>();

    @JsonProperty("sndMaterialTypeCode")
    @Valid
    private List<Pair> sndMaterialTypeCodes = new ArrayList<Pair>();

    @JsonProperty("cuttingTypeCode")
    @Valid
    private List<Pair> cuttingTypeCodes = new ArrayList<Pair>();

    @JsonProperty("specialPlaybackCharacteristicsCode")
    @Valid
    private List<Pair> specialPlaybackCharacteristicsCodes = new ArrayList<Pair>();

    @JsonProperty("storageTechniqueCode")
    @Valid
    private List<Pair> storageTechniqueCodes = new ArrayList<Pair>();

    @JsonProperty("videoRecordingFormatCodes")
    @Valid
    private List<Pair> videoRecordingFormatCodes = new ArrayList<Pair>();

    @JsonProperty("emulsionOnFilmCode")
    @Valid
    private List<Pair> emulsionOnFilmCodes = new ArrayList<Pair>();

    @JsonProperty("reductionRatioRangeCode")
    @Valid
    private List<Pair> reductionRatioRangeCodes = new ArrayList<Pair>();

    /**
     *
     * @return videoRecordingFormatCodes
     */
    @JsonProperty("videoRecordingFormatCodes")
    public List<Pair> getVideoRecordingFormatCodes() {
        return videoRecordingFormatCodes;
    }

    /**
     *
     * @return emulsionOnFilmCodes
     */
    @JsonProperty("emulsionOnFilmCode")
    public List<Pair> getEmulsionOnFilmCodes() {
        return emulsionOnFilmCodes;
    }

    /**
     *
     * @param emulsionOnFilmCodes
     */
    @JsonProperty("emulsionOnFilmCode")
    public void setEmulsionOnFilmCodes(List<Pair> emulsionOnFilmCodes) {
        this.emulsionOnFilmCodes = emulsionOnFilmCodes;
    }

    /**
     *
     * @return reductionRatioRangeCodes
     */
    @JsonProperty("reductionRatioRangeCode")
    public List<Pair> getReductionRatioRangeCodes() {
        return reductionRatioRangeCodes;
    }

    /**
     *
     * @param reductionRatioRangeCodes
     */
    @JsonProperty("reductionRatioRangeCode")
    public void setReductionRatioRangeCodes(List<Pair> reductionRatioRangeCodes) {
        this.reductionRatioRangeCodes = reductionRatioRangeCodes;
    }

    /**
     *
     * @param videoRecordingFormatCodes
     */
    @JsonProperty("videoRecordingFormatCodes")
    public void setVideoRecordingFormatCodes(List<Pair> videoRecordingFormatCodes) {
        this.videoRecordingFormatCodes = videoRecordingFormatCodes;
    }

    /**
     *
     * @return storageTechniqueCodes
     */
    @JsonProperty("storageTechniqueCode")
    public List<Pair> getStorageTechniqueCodes() {
        return storageTechniqueCodes;
    }

    /**
     *
     * @param storageTechniqueCodes
     */
    @JsonProperty("storageTechniqueCode")
    public void setStorageTechniqueCodes(List<Pair> storageTechniqueCodes) {
        this.storageTechniqueCodes = storageTechniqueCodes;
    }

    /**
     *
     * @return specialPlaybackCharacteristicsCodes
     */
    @JsonProperty("specialPlaybackCharacteristicsCode")
    public List<Pair> getSpecialPlaybackCharacteristicsCodes() {
        return specialPlaybackCharacteristicsCodes;
    }

    /**
     *
     * @param specialPlaybackCharacteristicsCodes
     */
    @JsonProperty("specialPlaybackCharacteristicsCode")
    public void setSpecialPlaybackCharacteristicsCodes(List<Pair> specialPlaybackCharacteristicsCodes) {
        this.specialPlaybackCharacteristicsCodes = specialPlaybackCharacteristicsCodes;
    }

    /**
     *
     * @return cuttingTypeCodes
     */
    @JsonProperty("cuttingTypeCode")
    public List<Pair> getCuttingTypeCodes() {
        return cuttingTypeCodes;
    }

    /**
     *
     * @param cuttingTypeCodes
     */
    @JsonProperty("cuttingTypeCode")
    public void setCuttingTypeCodes(List<Pair> cuttingTypeCodes) {
        this.cuttingTypeCodes = cuttingTypeCodes;
    }

    /**
     *
     * @return sndMaterialTypeCodes
     */
    @JsonProperty("sndMaterialTypeCode")
    public List<Pair> getSndMaterialTypeCodes() {
        return sndMaterialTypeCodes;
    }

    /**
     *
     * @param sndMaterialTypeCodes
     */
    @JsonProperty("sndMaterialTypeCode")
    public void setSndMaterialTypeCodes(List<Pair> sndMaterialTypeCodes) {
        this.sndMaterialTypeCodes = sndMaterialTypeCodes;
    }

    /**
     *
     * @return tapeConfigurationCodes
     */
    @JsonProperty("tapeConfigurationCode")
    public List<Pair> getTapeConfigurationCodes() {
        return tapeConfigurationCodes;
    }

    /**
     *
     * @param tapeConfigurationCodes
     */
    @JsonProperty("tapeConfigurationCode")
    public void setTapeConfigurationCodes(List<Pair> tapeConfigurationCodes) {
        this.tapeConfigurationCodes = tapeConfigurationCodes;
    }

    /**
     *
     * @return discTypeCodes
     */
    @JsonProperty("discTypeCode")
    public List<Pair> getDiscTypeCodes() {
        return discTypeCodes;
    }

    /**
     *
     * @param discTypeCodes
     */
    @JsonProperty("discTypeCode")
    public void setDiscTypeCodes(List<Pair> discTypeCodes) {
        this.discTypeCodes = discTypeCodes;
    }

    /**
     *
     * @return tapeWidthCodes
     */
    @JsonProperty("tapeWidthCode")
    public List<Pair> getTapeWidthCodes() {
        return tapeWidthCodes;
    }

    /**
     *
     * @param tapeWidthCodes
     */
    @JsonProperty("tapeWidthCode")
    public void setTapeWidthCodes(List<Pair> tapeWidthCodes) {
        this.tapeWidthCodes = tapeWidthCodes;
    }

    /**
     *
     * @return grooveWidthCodes
     */
    @JsonProperty("grooveWidthCode")
    public List<Pair> getGrooveWidthCodes() {
        return grooveWidthCodes;
    }

    /**
     *
     * @param grooveWidthCodes
     */
    @JsonProperty("grooveWidthCode")
    public void setGrooveWidthCodes(List<Pair> grooveWidthCodes) {
        this.grooveWidthCodes = grooveWidthCodes;
    }

    /**
     *
     * @return speedCodes
     */
    @JsonProperty("speedCode")
    public List<Pair> getSpeedCodes() {
        return speedCodes;
    }

    /**
     *
     * @param speedCodes
     */
    @JsonProperty("speedCode")
    public void setSpeedCodes(List<Pair> speedCodes) {
        this.speedCodes = speedCodes;
    }

    /**
     *
     * @return remoteSensingDataTypeCodes
     */
    @JsonProperty("remoteSensingDataTypeCodes")
    public List<Pair> getRemoteSensingDataTypeCodes() {
        return remoteSensingDataTypeCodes;
    }

    /**
     *
     * @param remoteSensingDataTypeCodes
     */
    @JsonProperty("remoteSensingDataTypeCodes")
    public void setRemoteSensingDataTypeCodes(List<Pair> remoteSensingDataTypeCodes) {
        this.remoteSensingDataTypeCodes = remoteSensingDataTypeCodes;
    }

    /**
     *
     * @return sensorTypeCodes
     */
    @JsonProperty("sensorTypeCode")
    public List<Pair> getSensorTypeCodes() {
        return sensorTypeCodes;
    }

    /**
     *
     * @param sensorTypeCodes
     */
    @JsonProperty("sensorTypeCode")
    public void setSensorTypeCodes(List<Pair> sensorTypeCodes) {
        this.sensorTypeCodes = sensorTypeCodes;
    }

    /**
     *
     * @return platformConstructionTypeCodes
     */
    @JsonProperty("platformConstructionTypeCode")
    public List<Pair> getPlatformConstructionTypeCodes() {
        return platformConstructionTypeCodes;
    }

    /**
     *
     * @param platformConstructionTypeCodes
     */
    @JsonProperty("platformConstructionTypeCode")
    public void setPlatformConstructionTypeCodes(List<Pair> platformConstructionTypeCodes) {
        this.platformConstructionTypeCodes = platformConstructionTypeCodes;
    }

    /**
     *
     * @return platformUseCodes
     */
    @JsonProperty("platformUseCode")
    public List<Pair> getPlatformUseCodes() {
        return platformUseCodes;
    }

    /**
     *
     * @param platformUseCodes
     */
    @JsonProperty("platformUseCode")
    public void setPlatformUseCodes(List<Pair> platformUseCodes) {
        this.platformUseCodes = platformUseCodes;
    }

    /**
     *
     * @return cloudCoverCodes
     */
    @JsonProperty("cloudCoverCode")
    public List<Pair> getCloudCoverCodes() {
        return cloudCoverCodes;
    }

    /**
     *
     * @param cloudCoverCodes
     */
    @JsonProperty("cloudCoverCode")
    public void setCloudCoverCodes(List<Pair> cloudCoverCodes) {
        this.cloudCoverCodes = cloudCoverCodes;
    }

    /**
     *
     * @return attitudeOfSensorCodes
     */
    @JsonProperty("attitudeOfSensorCode")
    public List<Pair> getAttitudeOfSensorCodes() {
        return attitudeOfSensorCodes;
    }

    /**
     *
     * @param attitudeOfSensorCodes
     */
    @JsonProperty("attitudeOfSensorCode")
    public void setAttitudeOfSensorCodes(List<Pair> attitudeOfSensorCodes) {
        this.attitudeOfSensorCodes = attitudeOfSensorCodes;
    }

    /**
     *
     * @return altitudeOfSensorCodes
     */
    @JsonProperty("altitudeOfSensorCode")
    public List<Pair> getAltitudeOfSensorCodes() {
        return altitudeOfSensorCodes;
    }

    /**
     *
     * @param altitudeOfSensorCodes
     */
    @JsonProperty("altitudeOfSensorCode")
    public void setAltitudeOfSensorCodes(List<Pair> altitudeOfSensorCodes) {
        this.altitudeOfSensorCodes = altitudeOfSensorCodes;
    }

    /**
     *
     * @return completenessCodes
     */
    @JsonProperty("completenessCode")
    public List<Pair> getCompletenessCodes() {
        return completenessCodes;
    }

    /**
     *
     * @param completenessCodes
     */
    @JsonProperty("completenessCode")
    public void setCompletenessCodes(List<Pair> completenessCodes) {
        this.completenessCodes = completenessCodes;
    }

    /**
     *
     * @return deteriorationStageCodes
     */
    @JsonProperty("deteriorationStageCode")
    public List<Pair> getDeteriorationStageCodes() {
        return deteriorationStageCodes;
    }

    /**
     *
     * @param deteriorationStageCodes
     */
    @JsonProperty("deteriorationStageCode")
    public void setDeteriorationStageCodes(List<Pair> deteriorationStageCodes) {
        this.deteriorationStageCodes = deteriorationStageCodes;
    }

    /**
     *
     * @return refinedCategoriesOfColourCodes
     */
    @JsonProperty("refinedCategoriesOfColourCode")
    public List<Pair> getRefinedCategoriesOfColourCodes() {
        return refinedCategoriesOfColourCodes;
    }

    /**
     *
     * @param refinedCategoriesOfColourCodes
     */
    @JsonProperty("refinedCategoriesOfColourCode")
    public void setRefinedCategoriesOfColourCodes(List<Pair> refinedCategoriesOfColourCodes) {
        this.refinedCategoriesOfColourCodes = refinedCategoriesOfColourCodes;
    }

    /**
     *
     * @return kindOfColourStockCodes
     */
    @JsonProperty("kindOfColourStockCode")
    public List<Pair> getKindOfColourStockCodes() {
        return kindOfColourStockCodes;
    }

    /**
     *
     * @param kindOfColourStockCodes
     */
    @JsonProperty("kindOfColourStockCode")
    public void setKindOfColourStockCodes(List<Pair> kindOfColourStockCodes) {
        this.kindOfColourStockCodes = kindOfColourStockCodes;
    }

    /**
     *
     * @return productionElementsCodes
     */
    @JsonProperty("productionElementsCode")
    public List<Pair> getProductionElementsCodes() {
        return productionElementsCodes;
    }

    /**
     *
     * @param productionElementsCodes
     */
    @JsonProperty("productionElementsCode")
    public void setProductionElementsCodes(List<Pair> productionElementsCodes) {
        this.productionElementsCodes = productionElementsCodes;
    }

    /**
     *
     * @return presentationFormatCodes
     */
    @JsonProperty("presentationFormatCode")
    public List<Pair> getPresentationFormatCodes() {
        return presentationFormatCodes;
    }

    /**
     *
     * @param presentationFormatCodes
     */
    @JsonProperty("presentationFormatCode")
    public void setPresentationFormatCodes(List<Pair> presentationFormatCodes) {
        this.presentationFormatCodes = presentationFormatCodes;
    }

    /**
     *
     * @return soundOnMediumOrSeparateCodes
     */
    @JsonProperty("soundOnMediumOrSeparateCode")
    public List<Pair> getSoundOnMediumOrSeparateCodes() {
        return soundOnMediumOrSeparateCodes;
    }

    /**
     *
     * @param soundOnMediumOrSeparateCodes
     */
    @JsonProperty("soundOnMediumOrSeparateCode")
    public void setSoundOnMediumOrSeparateCodes(List<Pair> soundOnMediumOrSeparateCodes) {
        this.soundOnMediumOrSeparateCodes = soundOnMediumOrSeparateCodes;
    }

    /**
     *
     * @return baseOfEmulsionCodes
     */
    @JsonProperty("baseOfEmulsionCode")
    public List<Pair> getBaseOfEmulsionCodes() {
        return baseOfEmulsionCodes;
    }

    /**
     *
     * @param baseOfEmulsionCodes
     */
    @JsonProperty("baseOfEmulsionCode")
    public void setBaseOfEmulsionCodes(List<Pair> baseOfEmulsionCodes) {
        this.baseOfEmulsionCodes = baseOfEmulsionCodes;
    }

    /**
     *
     * @return specificPhysicalCharacteristicsCodes
     */
    @JsonProperty("specificPhysicalCharacteristicsCode")
    public List<Pair> getSpecificPhysicalCharacteristicsCodes() {
        return specificPhysicalCharacteristicsCodes;
    }

    /**
     *
     * @param specificPhysicalCharacteristicsCodes
     */
    @JsonProperty("specificPhysicalCharacteristicsCode")
    public void setSpecificPhysicalCharacteristicsCodes(List<Pair> specificPhysicalCharacteristicsCodes) {
        this.specificPhysicalCharacteristicsCodes = specificPhysicalCharacteristicsCodes;
    }

    /**
     *
     * @return levelOfContractionCodes
     */
    @JsonProperty("levelOfContractionCode")
    public List<Pair> getLevelOfContractionCodes() {
        return levelOfContractionCodes;
    }

    /**
     *
     * @param levelOfContractionCodes
     */
    @JsonProperty("levelOfContractionCode")
    public void setLevelOfContractionCodes(List<Pair> levelOfContractionCodes) {
        this.levelOfContractionCodes = levelOfContractionCodes;
    }

    /**
     *
     * @return brailleMusicFormatCodes
     */
    @JsonProperty("brailleMusicFormatCodes")
    public List<Pair> getBrailleMusicFormatCodes() {
        return brailleMusicFormatCodes;
    }

    /**
     *
     * @param brailleMusicFormatCodes
     */
    @JsonProperty("brailleMusicFormatCodes")
    public void setBrailleMusicFormatCodes(List<Pair> brailleMusicFormatCodes) {
        this.brailleMusicFormatCodes = brailleMusicFormatCodes;
    }

    /**
     *
     * @return classOfBrailleWritingCodes
     */
    @JsonProperty("classOfBrailleWritingCodes")
    public List<Pair> getClassOfBrailleWritingCodes() {
        return classOfBrailleWritingCodes;
    }

    /**
     *
     * @param classOfBrailleWritingCodes
     */
    @JsonProperty("classOfBrailleWritingCodes")
    public void setClassOfBrailleWritingCodes(List<Pair> classOfBrailleWritingCodes) {
        this.classOfBrailleWritingCodes = classOfBrailleWritingCodes;
    }

    /**
     *
     * @return reformattingQualityCodes
     */
    @JsonProperty("reformattingQualityCode")
    public List<Pair> getReformattingQualityCodes() {
        return reformattingQualityCodes;
    }

    /**
     *
     * @param reformattingQualityCodes
     */
    @JsonProperty("reformattingQualityCode")
    public void setReformattingQualityCodes(List<Pair> reformattingQualityCodes) {
        this.reformattingQualityCodes = reformattingQualityCodes;
    }

    /**
     *
     * @return levelOfCompressionCodes
     */
    @JsonProperty("levelOfCompressionCode")
    public List<Pair> getLevelOfCompressionCodes() {
        return levelOfCompressionCodes;
    }

    /**
     *
     * @param levelOfCompressionCodes
     */
    @JsonProperty("levelOfCompressionCode")
    public void setLevelOfCompressionCodes(List<Pair> levelOfCompressionCodes) {
        this.levelOfCompressionCodes = levelOfCompressionCodes;
    }

    /**
     *
     * @return antecedentSourceCodes
     */
    @JsonProperty("antecedentSourceCode")
    public List<Pair> getAntecedentSourceCodes() {
        return antecedentSourceCodes;
    }

    /**
     *
     * @param antecedentSourceCodes
     */
    @JsonProperty("antecedentSourceCode")
    public void setAntecedentSourceCodes(List<Pair> antecedentSourceCodes) {
        this.antecedentSourceCodes = antecedentSourceCodes;
    }

    /**
     *
     * @return qualityAssuranceTargetCodes
     */
    @JsonProperty("qualityAssuranceTargetCode")
    public List<Pair> getQualityAssuranceTargetCodes() {
        return qualityAssuranceTargetCodes;
    }

    /**
     *
     * @param qualityAssuranceTargetCodes
     */
    @JsonProperty("qualityAssuranceTargetCode")
    public void setQualityAssuranceTargetCodes(List<Pair> qualityAssuranceTargetCodes) {
        this.qualityAssuranceTargetCodes = qualityAssuranceTargetCodes;
    }

    /**
     *
     * @return includesSoundCodes
     */
    @JsonProperty("includesSoundCodes")
    public List<Pair> getIncludesSoundCodes() {
        return includesSoundCodes;
    }

    /**
     *
     * @param includesSoundCodes
     */
    @JsonProperty("includesSoundCodes")
    public void setIncludesSoundCodes(List<Pair> includesSoundCodes) {
        this.includesSoundCodes = includesSoundCodes;
    }


    /**
     *
     * @return fileFormatsCodes
     */
    @JsonProperty("fileFormatsCode")
    public List<Pair> getFileFormatsCodes() {
        return fileFormatsCodes;
    }

    /**
     *
     * @param fileFormatsCodes
     */
    @JsonProperty("fileFormatsCode")
    public void setFileFormatsCodes(List<Pair> fileFormatsCodes) {
        this.fileFormatsCodes = fileFormatsCodes;
    }

    /**
     *
     * @return productionDetailsCodes
     */
    @JsonProperty("productionDetailsCode")
    public List<Pair> getProductionDetailsCodes() {
        return productionDetailsCodes;
    }

    /**
     *
     * @param productionDetailsCodes
     */
    @JsonProperty("productionDetailsCode")
    public void setProductionDetailsCodes(List<Pair> productionDetailsCodes) {
        this.productionDetailsCodes = productionDetailsCodes;
    }

    /**
     *
     * @return baseOfFilmCodes
     */
    @JsonProperty("baseOfFilmCode")
    public List<Pair> getBaseOfFilmCodes() {
        return baseOfFilmCodes;
    }

    /**
     *
     * @param baseOfFilmCodes
     */
    @JsonProperty("baseOfFilmCode")
    public void setBaseOfFilmCodes(List<Pair> baseOfFilmCodes) {
        this.baseOfFilmCodes = baseOfFilmCodes;
    }

    /**
     *
     * @return generationCodes
     */
    @JsonProperty("generationCode")
    public List<Pair> getGenerationCodes() {
        return generationCodes;
    }

    /**
     *
     * @param generationCodes
     */
    @JsonProperty("generationCode")
    public void setGenerationCodes(List<Pair> generationCodes) {
        this.generationCodes = generationCodes;
    }

    /**
     *
     * @return configurationCodes
     */
    @JsonProperty("configurationCode")
    public List<Pair> getConfigurationCodes() {
        return configurationCodes;
    }

    /**
     *
     * @param configurationCodes
     */
    @JsonProperty("configurationCode")
    public void setConfigurationCodes(List<Pair> configurationCodes) {
        this.configurationCodes = configurationCodes;
    }

    /**
     *
     * @return primarySupportMaterialCodes
     */
    @JsonProperty("primarySupportMaterialCode")
    public List<Pair> getPrimarySupportMaterialCodes() {
        return primarySupportMaterialCodes;
    }

    /**
     *
     * @param primarySupportMaterialCodes
     */
    @JsonProperty("primarySupportMaterialCode")
    public void setPrimarySupportMaterialCodes(List<Pair> primarySupportMaterialCodes) {
        this.primarySupportMaterialCodes = primarySupportMaterialCodes;
    }

    /**
     *
     * @return polarityCodes
     */
    @JsonProperty("polarityCode")
    public List<Pair> getPolarityCodes() {
        return polarityCodes;
    }

    /**
     *
     * @param polarityCodes
     */
    @JsonProperty("polarityCode")
    public void setPolarityCodes(List<Pair> polarityCodes) {
        this.polarityCodes = polarityCodes;
    }


    /**
     *
     * @return dimensionCodes
     */
    @JsonProperty("dimensionCodes")
    public List<Pair> getDimensionCodes() {
        return dimensionCodes;
    }

    /**
     *
     * @param dimensionCodes
     */
    @JsonProperty("dimensionCodes")
    public void setDimensionCodes(List<Pair> dimensionCodes) {
        this.dimensionCodes = dimensionCodes;
    }

    /**
     *
     * @return typeOfReproductionCodes
     */
    @JsonProperty("typeOfReproductionCode")
    public List<Pair> getTypeOfReproductionCodes() {
        return typeOfReproductionCodes;
    }

    /**
     *
     * @param typeOfReproductionCodes
     */
    @JsonProperty("typeOfReproductionCode")
    public void setTypeOfReproductionCodes(List<Pair> typeOfReproductionCodes) {
        this.typeOfReproductionCodes = typeOfReproductionCodes;
    }

    /**
     *
     * @return secondarySupportMaterialCodes
     */
    @JsonProperty("secondarySupportMaterialCode")
    public List<Pair> getSecondarySupportMaterialCodes() {
        return secondarySupportMaterialCodes;
    }

    /**
     *
     * @param secondarySupportMaterialCodes
     */
    @JsonProperty("secondarySupportMaterialCode")
    public void setSecondarySupportMaterialCodes(List<Pair> secondarySupportMaterialCodes) {
        this.secondarySupportMaterialCodes = secondarySupportMaterialCodes;
    }

    /**
     *
     * @return mediumForSoundCodes
     */
    @JsonProperty("mediumForSoundCode")
    public List<Pair> getMediumForSoundCodes() {
        return mediumForSoundCodes;
    }

    /**
     *
     * @param mediumForSoundCodes
     */
    @JsonProperty("mediumForSoundCode")
    public void setMediumForSoundCodes(List<Pair> mediumForSoundCodes) {
        this.mediumForSoundCodes = mediumForSoundCodes;
    }

    /**
     *
     * @return physicalMediumCodes
     */
    @JsonProperty("physicalMediumCode")
    public List<Pair> getPhysicalMediumCodes() {
        return physicalMediumCodes;
    }

    /**
     *
     * @param physicalMediumCodes
     */
    @JsonProperty("physicalMediumCode")
    public void setPhysicalMediumCodes(List<Pair> physicalMediumCodes) {
        this.physicalMediumCodes = physicalMediumCodes;
    }

    /**
     *
     * @return colorCodes
     */
    @JsonProperty("colourCode")
    public List<Pair> getColorCodes() {
        return colorCodes;
    }

    /**
     *
     * @param colorCodes
     */
    @JsonProperty("colourCode")
    public void setColorCodes(List<Pair> colorCodes) {
        this.colorCodes = colorCodes;
    }

    /**
     *
     * @return categoryOfMaterialCodes
     */
    @JsonProperty("categoryOfMaterial")
    public List<Pair> getCategoryOfMaterialCodes() {
        return categoryOfMaterialCodes;
    }

    /**
     *
     * @param categoryOfMaterialCodes
     */
    @JsonProperty("categoryOfMaterial")
    public void setCategoryOfMaterialCodes(List<Pair> categoryOfMaterialCodes) {
        this.categoryOfMaterialCodes = categoryOfMaterialCodes;
    }


    /**
     *
     * @return specificMaterialDesignationCodes
     */
    @JsonProperty("specificMaterialDesignationCode")
    public List<Pair> getSpecificMaterialDesignationCodes() {
        return specificMaterialDesignationCodes;
    }

    /**
     *
     * @param specificMaterialDesignationCodes
     */
    @JsonProperty("specificMaterialDesignationCode")
    public void setSpecificMaterialDesignationCodes(List<Pair> specificMaterialDesignationCodes) {
        this.specificMaterialDesignationCodes = specificMaterialDesignationCodes;
    }

    /**
     * 
     * @return
     *     The recordStatusTypes
     */
    @JsonProperty("itemRecordStatusCode")
    public List<Pair> getRecordStatusTypes() {
        return recordStatusTypes;
    }

    /**
     * 
     * @param recordStatusTypes
     *     The recordStatusTypes
     */
    @JsonProperty("itemRecordStatusCode")
    public void setRecordStatusTypes(List<Pair> recordStatusTypes) {
        this.recordStatusTypes = recordStatusTypes;
    }


    /**
     * 
     * @return
     *     The recordTypes
     */
    @JsonProperty("itemRecordTypeCode")
    public List<Pair> getRecordTypes() {
        return recordTypes;
    }

    /**
     * 
     * @param recordTypes
     *     The recordTypes
     */
    @JsonProperty("itemRecordTypeCode")
    public void setRecordTypes(List<Pair> recordTypes) {
        this.recordTypes = recordTypes;
    }


    /**
     * 
     * @return
     *     The bibliographicLevels
     */
    @JsonProperty("itemBibliographicLevelCode")
    public List<Pair> getBibliographicLevels() {
        return bibliographicLevels;
    }

    /**
     * 
     * @param bibliographicLevels
     *     The bibliographicLevels
     */
    @JsonProperty("itemBibliographicLevelCode")
    public void setBibliographicLevels(List<Pair> bibliographicLevels) {
        this.bibliographicLevels = bibliographicLevels;
    }

    /**
     * 
     * @return
     *     The controlTypes
     */
    @JsonProperty("itemControlTypeCode")
    public List<Pair> getControlTypes() {
        return controlTypes;
    }

    /**
     * 
     * @param controlTypes
     *     The controlTypes
     */
    @JsonProperty("itemControlTypeCode")
    public void setControlTypes(List<Pair> controlTypes) {
        this.controlTypes = controlTypes;
    }


    /**
     * 
     * @return
     *     The characterEncodingSchemas
     */
    @JsonProperty("characterEncodingSchemas")
    public List<Pair> getCharacterEncodingSchemas() {
        return characterEncodingSchemas;
    }

    /**
     * 
     * @param characterEncodingSchemas
     *     The characterEncodingSchemas
     */
    @JsonProperty("characterEncodingSchemas")
    public void setCharacterEncodingSchemas(List<Pair> characterEncodingSchemas) {
        this.characterEncodingSchemas = characterEncodingSchemas;
    }


    /**
     * 
     * @return
     *     The encodingLevels
     */
    @JsonProperty("encodingLevel")
    public List<Pair> getEncodingLevels() {
        return encodingLevels;
    }

    /**
     * 
     * @param encodingLevels
     *     The encodingLevels
     */
    @JsonProperty("encodingLevel")
    public void setEncodingLevels(List<Pair> encodingLevels) {
        this.encodingLevels = encodingLevels;
    }


    /**
     * 
     * @return
     *     The descriptiveCatalogForms
     */
    @JsonProperty("descriptiveCatalogForms")
    public List<Pair> getDescriptiveCatalogForms() {
        return descriptiveCatalogForms;
    }

    /**
     * 
     * @param descriptiveCatalogForms
     *     The descriptiveCatalogForms
     */
    @JsonProperty("descriptiveCatalogForms")
    public void setDescriptiveCatalogForms(List<Pair> descriptiveCatalogForms) {
        this.descriptiveCatalogForms = descriptiveCatalogForms;
    }

    /**
     * 
     * @return
     *     The linkedRecordCodes
     */
    @JsonProperty("linkedRecordCode")
    public List<Pair> getLinkedRecordCodes() {
        return linkedRecordCodes;
    }

    /**
     * 
     * @param linkedRecordCodes
     *     The linkedRecordCodes
     */
    @JsonProperty("linkedRecordCode")
    public void setLinkedRecordCodes(List<Pair> linkedRecordCodes) {
        this.linkedRecordCodes = linkedRecordCodes;
    }


    /**
     * 
     * @return
     *     The dateTypes
     */
    @JsonProperty("dateTypes")
    public List<Pair> getDateTypes() {
        return dateTypes;
    }

    /**
     * 
     * @param dateTypes
     *     The dateTypes
     */
    @JsonProperty("dateTypes")
    public void setDateTypes(List<Pair> dateTypes) {
        this.dateTypes = dateTypes;
    }

    /**
     * 
     * @return
     *     The bookIllustrationCodes1
     */
    @JsonProperty("bookIllustrationCode1")
    public List<Pair> getBookIllustrationCodes1() {
        return bookIllustrationCodes1;
    }

    /**
     * 
     * @param bookIllustrationCodes1
     *     The bookIllustrationCodes1
     */
    @JsonProperty("bookIllustrationCode1")
    public void setBookIllustrationCodes1(List<Pair> bookIllustrationCodes1) {
        this.bookIllustrationCodes1 = bookIllustrationCodes1;
    }

    /**
     * 
     * @return
     *     The bookIllustrationCodes2
     */
    @JsonProperty("bookIllustrationCode2")
    public List<Pair> getBookIllustrationCodes2() {
        return bookIllustrationCodes2;
    }

    /**
     * 
     * @param bookIllustrationCodes2
     *     The bookIllustrationCodes2
     */
    @JsonProperty("bookIllustrationCode2")
    public void setBookIllustrationCodes2(List<Pair> bookIllustrationCodes2) {
        this.bookIllustrationCodes2 = bookIllustrationCodes2;
    }


    /**
     * 
     * @return
     *     The bookIllustrationCodes3
     */
    @JsonProperty("bookIllustrationCode3")
    public List<Pair> getBookIllustrationCodes3() {
        return bookIllustrationCodes3;
    }

    /**
     * 
     * @param bookIllustrationCodes3
     *     The bookIllustrationCodes3
     */
    @JsonProperty("bookIllustrationCode3")
    public void setBookIllustrationCodes3(List<Pair> bookIllustrationCodes3) {
        this.bookIllustrationCodes3 = bookIllustrationCodes3;
    }


    /**
     * 
     * @return
     *     The bookIllustrationCodes4
     */
    @JsonProperty("bookIllustrationCode4")
    public List<Pair> getBookIllustrationCodes4() {
        return bookIllustrationCodes4;
    }

    /**
     * 
     * @param bookIllustrationCodes4
     *     The bookIllustrationCodes4
     */
    @JsonProperty("bookIllustrationCode4")
    public void setBookIllustrationCodes4(List<Pair> bookIllustrationCodes4) {
        this.bookIllustrationCodes4 = bookIllustrationCodes4;
    }


    /**
     * 
     * @return
     *     The targetAudienceCodes
     */
    @JsonProperty("targetAudienceCode")
    public List<Pair> getTargetAudienceCodes() {
        return targetAudienceCodes;
    }

    /**
     * 
     * @param targetAudienceCodes
     *     The targetAudienceCodes
     */
    @JsonProperty("targetAudienceCode")
    public void setTargetAudienceCodes(List<Pair> targetAudienceCodes) {
        this.targetAudienceCodes = targetAudienceCodes;
    }


    /**
     * 
     * @return
     *     The formOfItemCodes
     */
    @JsonProperty("formOfItemCode")
    public List<Pair> getFormOfItemCodes() {
        return formOfItemCodes;
    }

    /**
     * 
     * @param formOfItemCodes
     *     The formOfItemCodes
     */
    @JsonProperty("formOfItemCode")
    public void setFormOfItemCodes(List<Pair> formOfItemCodes) {
        this.formOfItemCodes = formOfItemCodes;
    }


    /**
     * 
     * @return
     *     The natureOfContents1
     */
    @JsonProperty("natureOfContent1")
    public List<Pair> getNatureOfContents1() {
        return natureOfContents1;
    }

    /**
     * 
     * @param natureOfContents1
     *     The natureOfContents1
     */
    @JsonProperty("natureOfContent1")
    public void setNatureOfContents1(List<Pair> natureOfContents1) {
        this.natureOfContents1 = natureOfContents1;
    }


    /**
     * 
     * @return
     *     The natureOfContents2
     */
    @JsonProperty("natureOfContent2")
    public List<Pair> getNatureOfContents2() {
        return natureOfContents2;
    }

    /**
     * 
     * @param natureOfContents2
     *     The natureOfContents2
     */
    @JsonProperty("natureOfContent2")
    public void setNatureOfContents2(List<Pair> natureOfContents2) {
        this.natureOfContents2 = natureOfContents2;
    }


    /**
     * 
     * @return
     *     The natureOfContents3
     */
    @JsonProperty("natureOfContent3")
    public List<Pair> getNatureOfContents3() {
        return natureOfContents3;
    }

    /**
     * 
     * @param natureOfContents3
     *     The natureOfContents3
     */
    @JsonProperty("natureOfContent3")
    public void setNatureOfContents3(List<Pair> natureOfContents3) {
        this.natureOfContents3 = natureOfContents3;
    }

    /**
     * 
     * @return
     *     The natureOfContents4
     */
    @JsonProperty("natureOfContent4")
    public List<Pair> getNatureOfContents4() {
        return natureOfContents4;
    }

    /**
     * 
     * @param natureOfContents4
     *     The natureOfContents4
     */
    @JsonProperty("natureOfContent4")
    public void setNatureOfContents4(List<Pair> natureOfContents4) {
        this.natureOfContents4 = natureOfContents4;
    }


    /**
     * 
     * @return
     *     The governmentPublicationCodes
     */
    @JsonProperty("governmentPublicationCode")
    public List<Pair> getGovernmentPublicationCodes() {
        return governmentPublicationCodes;
    }

    /**
     * 
     * @param governmentPublicationCodes
     *     The governmentPublicationCodes
     */
    @JsonProperty("governmentPublicationCode")
    public void setGovernmentPublicationCodes(List<Pair> governmentPublicationCodes) {
        this.governmentPublicationCodes = governmentPublicationCodes;
    }


    /**
     * 
     * @return
     *     The conferencePublicationCodes
     */
    @JsonProperty("conferencePublicationCode")
    public List<Pair> getConferencePublicationCodes() {
        return conferencePublicationCodes;
    }

    /**
     * 
     * @param conferencePublicationCodes
     *     The conferencePublicationCodes
     */
    @JsonProperty("conferencePublicationCode")
    public void setConferencePublicationCodes(List<Pair> conferencePublicationCodes) {
        this.conferencePublicationCodes = conferencePublicationCodes;
    }


    /**
     * 
     * @return
     *     The bookFestschrifts
     */
    @JsonProperty("bookFestschrift")
    public List<Pair> getBookFestschrifts() {
        return bookFestschrifts;
    }

    /**
     * 
     * @param bookFestschrifts
     *     The bookFestschrifts
     */
    @JsonProperty("bookFestschrift")
    public void setBookFestschrifts(List<Pair> bookFestschrifts) {
        this.bookFestschrifts = bookFestschrifts;
    }



    /**
     * 
     * @return
     *     The bookIndexAvailabilityCodes
     */
    @JsonProperty("bookIndexAvailabilityCode")
    public List<Pair> getBookIndexAvailabilityCodes() {
        return bookIndexAvailabilityCodes;
    }

    /**
     * 
     * @param bookIndexAvailabilityCodes
     *     The bookIndexAvailabilityCodes
     */
    @JsonProperty("bookIndexAvailabilityCode")
    public void setBookIndexAvailabilityCodes(List<Pair> bookIndexAvailabilityCodes) {
        this.bookIndexAvailabilityCodes = bookIndexAvailabilityCodes;
    }

    /**
     * 
     * @return
     *     The bookLiteraryFormTypeCodes
     */
    @JsonProperty("bookLiteraryFormTypeCode")
    public List<Pair> getBookLiteraryFormTypeCodes() {
        return bookLiteraryFormTypeCodes;
    }

    /**
     * 
     * @param bookLiteraryFormTypeCodes
     *     The bookLiteraryFormTypeCodes
     */
    @JsonProperty("bookLiteraryFormTypeCode")
    public void setBookLiteraryFormTypeCodes(List<Pair> bookLiteraryFormTypeCodes) {
        this.bookLiteraryFormTypeCodes = bookLiteraryFormTypeCodes;
    }

    /**
     * 
     * @return
     *     The bookBiographyCodes
     */
    @JsonProperty("bookBiographyCode")
    public List<Pair> getBookBiographyCodes() {
        return bookBiographyCodes;
    }

    /**
     * 
     * @param bookBiographyCodes
     *     The bookBiographyCodes
     */
    @JsonProperty("bookBiographyCode")
    public void setBookBiographyCodes(List<Pair> bookBiographyCodes) {
        this.bookBiographyCodes = bookBiographyCodes;
    }

    /**
     * 
     * @return
     *     The modifiedRecordTypes
     */
    @JsonProperty("modifiedRecordTypes")
    public List<Pair> getModifiedRecordTypes() {
        return modifiedRecordTypes;
    }

    /**
     * 
     * @param modifiedRecordTypes
     *     The modifiedRecordTypes
     */
    @JsonProperty("modifiedRecordTypes")
    public void setModifiedRecordTypes(List<Pair> modifiedRecordTypes) {
        this.modifiedRecordTypes = modifiedRecordTypes;
    }


    /**
     * 
     * @return
     *     The catalogSources
     */
    @JsonProperty("catalogSources")
    public List<Pair> getCatalogSources() {
        return catalogSources;
    }

    /**
     * 
     * @param catalogSources
     *     The catalogSources
     */
    @JsonProperty("catalogSources")
    public void setCatalogSources(List<Pair> catalogSources) {
        this.catalogSources = catalogSources;
    }


    /**
     *
     * @return
     *     The musicFormOfCompositions
     */
    @JsonProperty("musicFormOfCompositions")
    public List<Pair> getMusicFormOfCompositionCodes() {
        return musicFormOfCompositionCodes;
    }

    /**
     *
     * @param musicFormOfCompositions
     *     The musicFormOfCompositions
     */
    @JsonProperty("musicFormOfCompositions")
    public void setMusicFormOfCompositionCodes(List<Pair> musicFormOfCompositionCodes) {
        this.musicFormOfCompositionCodes = musicFormOfCompositionCodes;
    }

    /**
     *
     * @return
     *     The musicParts
     */
    @JsonProperty("musicParts")
    public List<Pair> geMusicPartCodes() {
        return musicPartCodes;
    }

    /**
     *
     * @param musicPartCodes
     *     The musicParts
     */
    @JsonProperty("musicParts")
    public void setMusicPartCodes (List<Pair> musicPartCodes) {
        this.musicPartCodes = musicPartCodes;
    }


    /**
     *
     * @return
     *     The musicFormats
     */
    @JsonProperty("musicFormats")
    public List<Pair> getMusicFormatCodes() {
        return musicFormatCodes;
    }

    /**
     *
     * @param musicFormatCodes
     *     The musicFormats
     */
    @JsonProperty("musicFormats")
    public void setMusicFormatCodes(List<Pair> musicFormatCodes) {
        this.musicFormatCodes = musicFormatCodes;
    }

    /**
     *
     * @return
     *     The musicTextualMaterialCodes
     */
    @JsonProperty("musicTextualMaterialCodes")
    public List<Pair> getMusicTextualMaterialCodes() {
        return musicTextualMaterialCodes;
    }

    /**
     *
     * @param musicTextualMaterialCodes
     *     The musicTextualMaterialCodes
     */
    @JsonProperty("musicTextualMaterialCodes")
    public void setMusicTextualMaterialCodes(List<Pair> musicTextualMaterialCodes) {
        this.musicTextualMaterialCodes = musicTextualMaterialCodes;
    }

    /**
     *
     * @return
     *       The musicTranspositionArrangementCodes
     */
    @JsonProperty("musicTranspositionArrangementCode")
    public List<Pair> getMusicTranspositionArrangementCodes() {
        return musicTranspositionArrangementCodes;
    }

    /**
     *
     * @param musicTranspositionArrangementCodes
     *      The musicTranspositionArrangementCodes
     */
    @JsonProperty("musicTranspositionArrangementCode")
    public void setMusicTranspositionArrangementCodes(List<Pair> musicTranspositionArrangementCodes) {
        this.musicTranspositionArrangementCodes = musicTranspositionArrangementCodes;
    }

    /**
     *
     * @return
     *      The musicLiteraryTextCodes
     *
     */
    @JsonProperty("musicLiteraryTextCodes")
    public List<Pair> getMusicLiteraryTextCodes() {
        return musicLiteraryTextCodes;
    }

    /**
     *
     * @param musicLiteraryTextCodes
     *      The musicLiteraryTextCodes
     */
    @JsonProperty("musicLiteraryTextCodes")
    public void setMusicLiteraryTextCodes(List<Pair> musicLiteraryTextCodes) {
        this.musicLiteraryTextCodes = musicLiteraryTextCodes;
    }

    /**
     *
     * @return serialFrequencyCodes
     */
    @JsonProperty("serialFrequencyCode")
    public List<Pair> getSerialFrequencyCodes() {
        return serialFrequencyCodes;
    }

    /**
     *
     * @param serialFrequencyCodes
     */
    @JsonProperty("serialFrequencyCode")
    public void setSerialFrequencyCodes(List<Pair> serialFrequencyCodes) {
        this.serialFrequencyCodes = serialFrequencyCodes;
    }

    /**
     *
     * @return serialRegularityCodes
     */
    @JsonProperty("serialRegularityCode")
    public List<Pair> getSerialRegularityCodes() {
        return serialRegularityCodes;
    }

    /**
     *
     * @param serialRegularityCodes
     */
    @JsonProperty("serialRegularityCode")
    public void setSerialRegularityCodes(List<Pair> serialRegularityCodes) {
        this.serialRegularityCodes = serialRegularityCodes;
    }

    /**
     *
     * @return serialTypeOfContinuingResourceCodes
     */
    @JsonProperty("serialTypeOfContinuingResourceCodes")
    public List<Pair> getSerialTypeOfContinuingResourceCodes() {
        return serialTypeOfContinuingResourceCodes;
    }

    /**
     *
     * @param serialTypeOfContinuingResourceCodes
     */
    @JsonProperty("serialTypeOfContinuingResourceCodes")
    public void setSerialTypeOfContinuingResourceCodes(List<Pair> serialTypeOfContinuingResourceCodes) {
        this.serialTypeOfContinuingResourceCodes = serialTypeOfContinuingResourceCodes;
    }

    /**
     *
     * @return serialFormOriginalCodes
     */
    @JsonProperty("serialFormOriginalCodes")
    public List<Pair> getSerialFormOriginalCodes() {
        return serialFormOriginalCodes;
    }

    /**
     *
     * @param serialFormOriginalCodes
     */
    @JsonProperty("serialFormOriginalCodes")
    public void setSerialFormOriginalCodes(List<Pair> serialFormOriginalCodes) {
        this.serialFormOriginalCodes = serialFormOriginalCodes;
    }

    /**
     *
     * @return serialNatureOfWorkCodes
     */
    @JsonProperty("serialNatureOfWorkCodes")
    public List<Pair> getSerialNatureOfWorkCodes() {
        return serialNatureOfWorkCodes;
    }

    /**
     *
     * @param serialNatureOfWorkCodes
     */
    @JsonProperty("serialNatureOfWorkCodes")
    public void setSerialNatureOfWorkCodes(List<Pair> serialNatureOfWorkCodes) {
        this.serialNatureOfWorkCodes = serialNatureOfWorkCodes;
    }

    /**
     *
     * @return serialOriginAlphabetCodes
     */
    @JsonProperty("serialOriginAlphabetCodes")
    public List<Pair> getSerialOriginAlphabetCodes() {
        return serialOriginAlphabetCodes;
    }

    /**
     *
     * @param serialOriginAlphabetCodes
     */
    @JsonProperty("serialOriginAlphabetCodes")
    public void setSerialOriginAlphabetCodes(List<Pair> serialOriginAlphabetCodes) {
        this.serialOriginAlphabetCodes = serialOriginAlphabetCodes;
    }

    /**
     *
     * @return serialEntryConvCodes
     */
    @JsonProperty("serialEntryConvCodes")
    public List<Pair> getSerialEntryConvCodes() {
        return serialEntryConvCodes;
    }

    /**
     *
     * @param serialEntryConvCodes
     */
    @JsonProperty("serialEntryConvCodes")
    public void setSerialEntryConvCodes(List<Pair> serialEntryConvCodes) {
        this.serialEntryConvCodes = serialEntryConvCodes;
    }

    /**
     *
     * @return mapReliefCodes
     */
    @JsonProperty("mapReliefCodes")
    public List<Pair> getMapReliefCodes() {
        return mapReliefCodes;
    }

    /**
     *
     * @param mapReliefCodes
     */
    @JsonProperty("mapReliefCodes")
    public void setMapReliefCodes(List<Pair> mapReliefCodes) {
        this.mapReliefCodes = mapReliefCodes;
    }

    /**
     *
     * @return mapProjectionCodes
     */
    @JsonProperty("mapProjectionCodes")
    public List<Pair> getMapProjectionCodes() {
        return mapProjectionCodes;
    }

    /**
     *
     * @param mapProjectionCodes
     */
    @JsonProperty("mapProjectionCodes")
    public void setMapProjectionCodes(List<Pair> mapProjectionCodes) {
        this.mapProjectionCodes = mapProjectionCodes;
    }

    /**
     *
     * @return mapTypeCartographicMaterialCodes
     */
    @JsonProperty("mapTypeCartographicMaterialCodes")
    public List<Pair> getMapTypeCartographicMaterialCodes() {
        return mapTypeCartographicMaterialCodes;
    }

    /**
     *
     * @param mapTypeCartographicMaterialCodes
     */
    @JsonProperty("mapTypeCartographicMaterialCodes")
    public void setMapTypeCartographicMaterialCodes(List<Pair> mapTypeCartographicMaterialCodes) {
        this.mapTypeCartographicMaterialCodes = mapTypeCartographicMaterialCodes;
    }

    /**
     *
     * @return mapIndexCodes
     */
    @JsonProperty("mapIndexCodes")
    public List<Pair> getMapIndexCodes() {
        return mapIndexCodes;
    }

    /**
     *
     * @param mapIndexCodes
     */
    @JsonProperty("mapIndexCodes")
    public void setMapIndexCodes(List<Pair> mapIndexCodes) {
        this.mapIndexCodes = mapIndexCodes;
    }

    /**
     *
     * @return mapSpecialFormatCharacteristicCodes
     */
    @JsonProperty("mapSpecialFormatCharacteristicCodes")
    public List<Pair> getMapSpecialFormatCharacteristicCodes() {
        return mapSpecialFormatCharacteristicCodes;
    }

    /**
     *
     * @param mapSpecialFormatCharacteristicCodes
     */
    @JsonProperty("mapSpecialFormatCharacteristicCodes")
    public void setMapSpecialFormatCharacteristicCodes(List<Pair> mapSpecialFormatCharacteristicCodes) {
        this.mapSpecialFormatCharacteristicCodes = mapSpecialFormatCharacteristicCodes;
    }

    /**
     *
     * @return visualTargetAudienceCodes
     */
    @JsonProperty("visualTargetAudienceCodes")
    public List<Pair> getVisualTargetAudienceCodes() {
        return visualTargetAudienceCodes;
    }

    /**
     *
     * @param visualTargetAudienceCodes
     */
    @JsonProperty("visualTargetAudienceCodes")
    public void setVisualTargetAudienceCodes(List<Pair> visualTargetAudienceCodes) {
        this.visualTargetAudienceCodes = visualTargetAudienceCodes;
    }

    /**
     *
     * @return visualTypeMaterialCodes
     */
    @JsonProperty("visualTypeMaterialCodes")
    public List<Pair> getVisualTypeMaterialCodes() {
        return visualTypeMaterialCodes;
    }

    /**
     *
     * @param visualTypeMaterialCodes
     */
    @JsonProperty("visualTypeMaterialCodes")
    public void setVisualTypeMaterialCodes(List<Pair> visualTypeMaterialCodes) {
        this.visualTypeMaterialCodes = visualTypeMaterialCodes;
    }

    /**
     *
     * @return visualTechnique
     */
    @JsonProperty("visualTechnique")
    public List<Pair> getVisualTechnique() {
        return visualTechnique;
    }

    /**
     *
     * @param visualTechnique
     */
    @JsonProperty("visualTechnique")
    public void setVisualTechnique(List<Pair> visualTechnique) {
        this.visualTechnique = visualTechnique;
    }

    /**
     *
     * @return computerTargetAudienceCodes
     */
    @JsonProperty("computerTargetAudienceCodes")
    public List<Pair> getComputerTargetAudienceCodes() {
        return computerTargetAudienceCodes;
    }

    /**
     *
     * @param computerTargetAudienceCodes
     */
    @JsonProperty("computerTargetAudienceCodes")
    public void setComputerTargetAudienceCodes(List<Pair> computerTargetAudienceCodes) {
        this.computerTargetAudienceCodes = computerTargetAudienceCodes;
    }

    /**
     *
     * @return computerFormOfItemCodes
     */
    @JsonProperty("computerFormOfItemCodes")
    public List<Pair> getComputerFormOfItemCodes() {
        return computerFormOfItemCodes;
    }

    /**
     *
     * @param computerFormOfItemCodes
     */
    @JsonProperty("computerFormOfItemCodes")
    public void setComputerFormOfItemCodes(List<Pair> computerFormOfItemCodes) {
        this.computerFormOfItemCodes = computerFormOfItemCodes;
    }

    /**
     *
     * @return computerTypeMaterialCodes
     */
    @JsonProperty("computerTypeMaterialCodes")
    public List<Pair> getComputerTypeMaterialCodes() {
        return computerTypeMaterialCodes;
    }

    /**
     *
     * @param computerTypeMaterialCodes
     */
    @JsonProperty("computerTypeMaterialCodes")
    public void setComputerTypeMaterialCodes(List<Pair> computerTypeMaterialCodes) {
        this.computerTypeMaterialCodes = computerTypeMaterialCodes;
    }
}
