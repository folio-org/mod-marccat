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
@JsonPropertyOrder({ "recordStatusTypes", "recordTypes", "bibliographicLevels", "controlTypes", "characterEncodingSchemas", "encodingLevels", "descriptiveCatalogForms", "linkedRecordCodes", "dateTypes", "bookIllustrationCodes1", "bookIllustrationCodes2", "bookIllustrationCodes3", "bookIllustrationCodes4", "targetAudienceCodes", "formOfItemCodes", "natureOfContents1", "natureOfContents2", "natureOfContents3", "natureOfContents4", "governmentPublicationCodes", "conferencePublicationCodes", "bookFestschrifts", "bookIndexAvailabilityCodes", "bookLiteraryFormTypeCodes", "bookBiographyCodes", "musicFormOfCompositions", "musicParts", "musicFormats", "modifiedRecordTypes", "catalogSources" })
public class FixedFieldCodesGroup {

    @JsonProperty("recordStatusTypes")
    @Valid
    private List<RecordStatusType> recordStatusTypes = new ArrayList<RecordStatusType>();

    @JsonProperty("recordTypes")
    @Valid
    private List<RecordType> recordTypes = new ArrayList<RecordType>();

    @JsonProperty("bibliographicLevels")
    @Valid
    private List<BibliographicLevel> bibliographicLevels = new ArrayList<BibliographicLevel>();

    @JsonProperty("controlTypes")
    @Valid
    private List<ControlType> controlTypes = new ArrayList<ControlType>();

    @JsonProperty("characterEncodingSchemas")
    @Valid
    private List<CharacterEncodingSchema> characterEncodingSchemas = new ArrayList<CharacterEncodingSchema>();

    @JsonProperty("encodingLevels")
    @Valid
    private List<EncodingLevel> encodingLevels = new ArrayList<EncodingLevel>();

    @JsonProperty("descriptiveCatalogForms")
    @Valid
    private List<DescriptiveCatalogForm> descriptiveCatalogForms = new ArrayList<DescriptiveCatalogForm>();

    @JsonProperty("linkedRecordCodes")
    @Valid
    private List<LinkedRecordCode> linkedRecordCodes = new ArrayList<LinkedRecordCode>();

    @JsonProperty("dateTypes")
    @Valid
    private List<DateType> dateTypes = new ArrayList<DateType>();

    @JsonProperty("bookIllustrationCodes1")
    @Valid
    private List<BookIllustrationCodes1> bookIllustrationCodes1 = new ArrayList<BookIllustrationCodes1>();

    @JsonProperty("bookIllustrationCodes2")
    @Valid
    private List<BookIllustrationCodes2> bookIllustrationCodes2 = new ArrayList<BookIllustrationCodes2>();

    @JsonProperty("bookIllustrationCodes3")
    @Valid
    private List<BookIllustrationCodes3> bookIllustrationCodes3 = new ArrayList<BookIllustrationCodes3>();

    @JsonProperty("bookIllustrationCodes4")
    @Valid
    private List<BookIllustrationCodes4> bookIllustrationCodes4 = new ArrayList<BookIllustrationCodes4>();

    @JsonProperty("targetAudienceCodes")
    @Valid
    private List<TargetAudienceCode> targetAudienceCodes = new ArrayList<TargetAudienceCode>();

    @JsonProperty("formOfItemCodes")
    @Valid
    private List<FormOfItemCode> formOfItemCodes = new ArrayList<FormOfItemCode>();

    @JsonProperty("natureOfContents1")
    @Valid
    private List<NatureOfContents1> natureOfContents1 = new ArrayList<NatureOfContents1>();

    @JsonProperty("natureOfContents2")
    @Valid
    private List<NatureOfContents2> natureOfContents2 = new ArrayList<NatureOfContents2>();

    @JsonProperty("natureOfContents3")
    @Valid
    private List<NatureOfContents3> natureOfContents3 = new ArrayList<NatureOfContents3>();

    @JsonProperty("natureOfContents4")
    @Valid
    private List<NatureOfContents4> natureOfContents4 = new ArrayList<NatureOfContents4>();

    @JsonProperty("governmentPublicationCodes")
    @Valid
    private List<GovernmentPublicationCode> governmentPublicationCodes = new ArrayList<GovernmentPublicationCode>();

    @JsonProperty("conferencePublicationCodes")
    @Valid
    private List<ConferencePublicationCode> conferencePublicationCodes = new ArrayList<ConferencePublicationCode>();

    @JsonProperty("bookFestschrifts")
    @Valid
    private List<BookFestschrift> bookFestschrifts = new ArrayList<BookFestschrift>();

    @JsonProperty("bookIndexAvailabilityCodes")
    @Valid
    private List<BookIndexAvailabilityCode> bookIndexAvailabilityCodes = new ArrayList<BookIndexAvailabilityCode>();

    @JsonProperty("bookLiteraryFormTypeCodes")
    @Valid
    private List<BookLiteraryFormTypeCode> bookLiteraryFormTypeCodes = new ArrayList<BookLiteraryFormTypeCode>();

    @JsonProperty("bookBiographyCodes")
    @Valid
    private List<BookBiographyCode> bookBiographyCodes = new ArrayList<BookBiographyCode>();

    @JsonProperty("musicFormOfCompositions")
    @Valid
    private List<MusicFormOfCompositionCode> musicFormOfCompositionCodes = new ArrayList<MusicFormOfCompositionCode>();

    @JsonProperty("musicParts")
    @Valid
    private List<MusicPartCode> musicPartCodes = new ArrayList<MusicPartCode>();

    @JsonProperty("musicFormats")
    @Valid
    private List<MusicFormatCode> musicFormatCodes = new ArrayList<MusicFormatCode>();

    @JsonProperty("modifiedRecordTypes")
    @Valid
    private List<ModifiedRecordType> modifiedRecordTypes = new ArrayList<ModifiedRecordType>();

    @JsonProperty("catalogSources")
    @Valid
    private List<CatalogSource> catalogSources = new ArrayList<CatalogSource>();

    /**
     * 
     * @return
     *     The recordStatusTypes
     */
    @JsonProperty("recordStatusTypes")
    public List<RecordStatusType> getRecordStatusTypes() {
        return recordStatusTypes;
    }

    /**
     * 
     * @param recordStatusTypes
     *     The recordStatusTypes
     */
    @JsonProperty("recordStatusTypes")
    public void setRecordStatusTypes(List<RecordStatusType> recordStatusTypes) {
        this.recordStatusTypes = recordStatusTypes;
    }

    public FixedFieldCodesGroup withRecordStatusTypes(List<RecordStatusType> recordStatusTypes) {
        this.recordStatusTypes = recordStatusTypes;
        return this;
    }

    /**
     * 
     * @return
     *     The recordTypes
     */
    @JsonProperty("recordTypes")
    public List<RecordType> getRecordTypes() {
        return recordTypes;
    }

    /**
     * 
     * @param recordTypes
     *     The recordTypes
     */
    @JsonProperty("recordTypes")
    public void setRecordTypes(List<RecordType> recordTypes) {
        this.recordTypes = recordTypes;
    }

    public FixedFieldCodesGroup withRecordTypes(List<RecordType> recordTypes) {
        this.recordTypes = recordTypes;
        return this;
    }

    /**
     * 
     * @return
     *     The bibliographicLevels
     */
    @JsonProperty("bibliographicLevels")
    public List<BibliographicLevel> getBibliographicLevels() {
        return bibliographicLevels;
    }

    /**
     * 
     * @param bibliographicLevels
     *     The bibliographicLevels
     */
    @JsonProperty("bibliographicLevels")
    public void setBibliographicLevels(List<BibliographicLevel> bibliographicLevels) {
        this.bibliographicLevels = bibliographicLevels;
    }

    public FixedFieldCodesGroup withBibliographicLevels(List<BibliographicLevel> bibliographicLevels) {
        this.bibliographicLevels = bibliographicLevels;
        return this;
    }

    /**
     * 
     * @return
     *     The controlTypes
     */
    @JsonProperty("controlTypes")
    public List<ControlType> getControlTypes() {
        return controlTypes;
    }

    /**
     * 
     * @param controlTypes
     *     The controlTypes
     */
    @JsonProperty("controlTypes")
    public void setControlTypes(List<ControlType> controlTypes) {
        this.controlTypes = controlTypes;
    }

    public FixedFieldCodesGroup withControlTypes(List<ControlType> controlTypes) {
        this.controlTypes = controlTypes;
        return this;
    }

    /**
     * 
     * @return
     *     The characterEncodingSchemas
     */
    @JsonProperty("characterEncodingSchemas")
    public List<CharacterEncodingSchema> getCharacterEncodingSchemas() {
        return characterEncodingSchemas;
    }

    /**
     * 
     * @param characterEncodingSchemas
     *     The characterEncodingSchemas
     */
    @JsonProperty("characterEncodingSchemas")
    public void setCharacterEncodingSchemas(List<CharacterEncodingSchema> characterEncodingSchemas) {
        this.characterEncodingSchemas = characterEncodingSchemas;
    }

    public FixedFieldCodesGroup withCharacterEncodingSchemas(List<CharacterEncodingSchema> characterEncodingSchemas) {
        this.characterEncodingSchemas = characterEncodingSchemas;
        return this;
    }

    /**
     * 
     * @return
     *     The encodingLevels
     */
    @JsonProperty("encodingLevels")
    public List<EncodingLevel> getEncodingLevels() {
        return encodingLevels;
    }

    /**
     * 
     * @param encodingLevels
     *     The encodingLevels
     */
    @JsonProperty("encodingLevels")
    public void setEncodingLevels(List<EncodingLevel> encodingLevels) {
        this.encodingLevels = encodingLevels;
    }

    public FixedFieldCodesGroup withEncodingLevels(List<EncodingLevel> encodingLevels) {
        this.encodingLevels = encodingLevels;
        return this;
    }

    /**
     * 
     * @return
     *     The descriptiveCatalogForms
     */
    @JsonProperty("descriptiveCatalogForms")
    public List<DescriptiveCatalogForm> getDescriptiveCatalogForms() {
        return descriptiveCatalogForms;
    }

    /**
     * 
     * @param descriptiveCatalogForms
     *     The descriptiveCatalogForms
     */
    @JsonProperty("descriptiveCatalogForms")
    public void setDescriptiveCatalogForms(List<DescriptiveCatalogForm> descriptiveCatalogForms) {
        this.descriptiveCatalogForms = descriptiveCatalogForms;
    }

    public FixedFieldCodesGroup withDescriptiveCatalogForms(List<DescriptiveCatalogForm> descriptiveCatalogForms) {
        this.descriptiveCatalogForms = descriptiveCatalogForms;
        return this;
    }

    /**
     * 
     * @return
     *     The linkedRecordCodes
     */
    @JsonProperty("linkedRecordCodes")
    public List<LinkedRecordCode> getLinkedRecordCodes() {
        return linkedRecordCodes;
    }

    /**
     * 
     * @param linkedRecordCodes
     *     The linkedRecordCodes
     */
    @JsonProperty("linkedRecordCodes")
    public void setLinkedRecordCodes(List<LinkedRecordCode> linkedRecordCodes) {
        this.linkedRecordCodes = linkedRecordCodes;
    }

    public FixedFieldCodesGroup withLinkedRecordCodes(List<LinkedRecordCode> linkedRecordCodes) {
        this.linkedRecordCodes = linkedRecordCodes;
        return this;
    }

    /**
     * 
     * @return
     *     The dateTypes
     */
    @JsonProperty("dateTypes")
    public List<DateType> getDateTypes() {
        return dateTypes;
    }

    /**
     * 
     * @param dateTypes
     *     The dateTypes
     */
    @JsonProperty("dateTypes")
    public void setDateTypes(List<DateType> dateTypes) {
        this.dateTypes = dateTypes;
    }

    public FixedFieldCodesGroup withDateTypes(List<DateType> dateTypes) {
        this.dateTypes = dateTypes;
        return this;
    }

    /**
     * 
     * @return
     *     The bookIllustrationCodes1
     */
    @JsonProperty("bookIllustrationCodes1")
    public List<BookIllustrationCodes1> getBookIllustrationCodes1() {
        return bookIllustrationCodes1;
    }

    /**
     * 
     * @param bookIllustrationCodes1
     *     The bookIllustrationCodes1
     */
    @JsonProperty("bookIllustrationCodes1")
    public void setBookIllustrationCodes1(List<BookIllustrationCodes1> bookIllustrationCodes1) {
        this.bookIllustrationCodes1 = bookIllustrationCodes1;
    }

    public FixedFieldCodesGroup withBookIllustrationCodes1(List<BookIllustrationCodes1> bookIllustrationCodes1) {
        this.bookIllustrationCodes1 = bookIllustrationCodes1;
        return this;
    }

    /**
     * 
     * @return
     *     The bookIllustrationCodes2
     */
    @JsonProperty("bookIllustrationCodes2")
    public List<BookIllustrationCodes2> getBookIllustrationCodes2() {
        return bookIllustrationCodes2;
    }

    /**
     * 
     * @param bookIllustrationCodes2
     *     The bookIllustrationCodes2
     */
    @JsonProperty("bookIllustrationCodes2")
    public void setBookIllustrationCodes2(List<BookIllustrationCodes2> bookIllustrationCodes2) {
        this.bookIllustrationCodes2 = bookIllustrationCodes2;
    }

    public FixedFieldCodesGroup withBookIllustrationCodes2(List<BookIllustrationCodes2> bookIllustrationCodes2) {
        this.bookIllustrationCodes2 = bookIllustrationCodes2;
        return this;
    }

    /**
     * 
     * @return
     *     The bookIllustrationCodes3
     */
    @JsonProperty("bookIllustrationCodes3")
    public List<BookIllustrationCodes3> getBookIllustrationCodes3() {
        return bookIllustrationCodes3;
    }

    /**
     * 
     * @param bookIllustrationCodes3
     *     The bookIllustrationCodes3
     */
    @JsonProperty("bookIllustrationCodes3")
    public void setBookIllustrationCodes3(List<BookIllustrationCodes3> bookIllustrationCodes3) {
        this.bookIllustrationCodes3 = bookIllustrationCodes3;
    }

    public FixedFieldCodesGroup withBookIllustrationCodes3(List<BookIllustrationCodes3> bookIllustrationCodes3) {
        this.bookIllustrationCodes3 = bookIllustrationCodes3;
        return this;
    }

    /**
     * 
     * @return
     *     The bookIllustrationCodes4
     */
    @JsonProperty("bookIllustrationCodes4")
    public List<BookIllustrationCodes4> getBookIllustrationCodes4() {
        return bookIllustrationCodes4;
    }

    /**
     * 
     * @param bookIllustrationCodes4
     *     The bookIllustrationCodes4
     */
    @JsonProperty("bookIllustrationCodes4")
    public void setBookIllustrationCodes4(List<BookIllustrationCodes4> bookIllustrationCodes4) {
        this.bookIllustrationCodes4 = bookIllustrationCodes4;
    }

    public FixedFieldCodesGroup withBookIllustrationCodes4(List<BookIllustrationCodes4> bookIllustrationCodes4) {
        this.bookIllustrationCodes4 = bookIllustrationCodes4;
        return this;
    }

    /**
     * 
     * @return
     *     The targetAudienceCodes
     */
    @JsonProperty("targetAudienceCodes")
    public List<TargetAudienceCode> getTargetAudienceCodes() {
        return targetAudienceCodes;
    }

    /**
     * 
     * @param targetAudienceCodes
     *     The targetAudienceCodes
     */
    @JsonProperty("targetAudienceCodes")
    public void setTargetAudienceCodes(List<TargetAudienceCode> targetAudienceCodes) {
        this.targetAudienceCodes = targetAudienceCodes;
    }

    public FixedFieldCodesGroup withTargetAudienceCodes(List<TargetAudienceCode> targetAudienceCodes) {
        this.targetAudienceCodes = targetAudienceCodes;
        return this;
    }

    /**
     * 
     * @return
     *     The formOfItemCodes
     */
    @JsonProperty("formOfItemCodes")
    public List<FormOfItemCode> getFormOfItemCodes() {
        return formOfItemCodes;
    }

    /**
     * 
     * @param formOfItemCodes
     *     The formOfItemCodes
     */
    @JsonProperty("formOfItemCodes")
    public void setFormOfItemCodes(List<FormOfItemCode> formOfItemCodes) {
        this.formOfItemCodes = formOfItemCodes;
    }

    public FixedFieldCodesGroup withFormOfItemCodes(List<FormOfItemCode> formOfItemCodes) {
        this.formOfItemCodes = formOfItemCodes;
        return this;
    }

    /**
     * 
     * @return
     *     The natureOfContents1
     */
    @JsonProperty("natureOfContents1")
    public List<NatureOfContents1> getNatureOfContents1() {
        return natureOfContents1;
    }

    /**
     * 
     * @param natureOfContents1
     *     The natureOfContents1
     */
    @JsonProperty("natureOfContents1")
    public void setNatureOfContents1(List<NatureOfContents1> natureOfContents1) {
        this.natureOfContents1 = natureOfContents1;
    }

    public FixedFieldCodesGroup withNatureOfContents1(List<NatureOfContents1> natureOfContents1) {
        this.natureOfContents1 = natureOfContents1;
        return this;
    }

    /**
     * 
     * @return
     *     The natureOfContents2
     */
    @JsonProperty("natureOfContents2")
    public List<NatureOfContents2> getNatureOfContents2() {
        return natureOfContents2;
    }

    /**
     * 
     * @param natureOfContents2
     *     The natureOfContents2
     */
    @JsonProperty("natureOfContents2")
    public void setNatureOfContents2(List<NatureOfContents2> natureOfContents2) {
        this.natureOfContents2 = natureOfContents2;
    }

    public FixedFieldCodesGroup withNatureOfContents2(List<NatureOfContents2> natureOfContents2) {
        this.natureOfContents2 = natureOfContents2;
        return this;
    }

    /**
     * 
     * @return
     *     The natureOfContents3
     */
    @JsonProperty("natureOfContents3")
    public List<NatureOfContents3> getNatureOfContents3() {
        return natureOfContents3;
    }

    /**
     * 
     * @param natureOfContents3
     *     The natureOfContents3
     */
    @JsonProperty("natureOfContents3")
    public void setNatureOfContents3(List<NatureOfContents3> natureOfContents3) {
        this.natureOfContents3 = natureOfContents3;
    }

    public FixedFieldCodesGroup withNatureOfContents3(List<NatureOfContents3> natureOfContents3) {
        this.natureOfContents3 = natureOfContents3;
        return this;
    }

    /**
     * 
     * @return
     *     The natureOfContents4
     */
    @JsonProperty("natureOfContents4")
    public List<NatureOfContents4> getNatureOfContents4() {
        return natureOfContents4;
    }

    /**
     * 
     * @param natureOfContents4
     *     The natureOfContents4
     */
    @JsonProperty("natureOfContents4")
    public void setNatureOfContents4(List<NatureOfContents4> natureOfContents4) {
        this.natureOfContents4 = natureOfContents4;
    }

    public FixedFieldCodesGroup withNatureOfContents4(List<NatureOfContents4> natureOfContents4) {
        this.natureOfContents4 = natureOfContents4;
        return this;
    }

    /**
     * 
     * @return
     *     The governmentPublicationCodes
     */
    @JsonProperty("governmentPublicationCodes")
    public List<GovernmentPublicationCode> getGovernmentPublicationCodes() {
        return governmentPublicationCodes;
    }

    /**
     * 
     * @param governmentPublicationCodes
     *     The governmentPublicationCodes
     */
    @JsonProperty("governmentPublicationCodes")
    public void setGovernmentPublicationCodes(List<GovernmentPublicationCode> governmentPublicationCodes) {
        this.governmentPublicationCodes = governmentPublicationCodes;
    }

    public FixedFieldCodesGroup withGovernmentPublicationCodes(List<GovernmentPublicationCode> governmentPublicationCodes) {
        this.governmentPublicationCodes = governmentPublicationCodes;
        return this;
    }

    /**
     * 
     * @return
     *     The conferencePublicationCodes
     */
    @JsonProperty("conferencePublicationCodes")
    public List<ConferencePublicationCode> getConferencePublicationCodes() {
        return conferencePublicationCodes;
    }

    /**
     * 
     * @param conferencePublicationCodes
     *     The conferencePublicationCodes
     */
    @JsonProperty("conferencePublicationCodes")
    public void setConferencePublicationCodes(List<ConferencePublicationCode> conferencePublicationCodes) {
        this.conferencePublicationCodes = conferencePublicationCodes;
    }

    public FixedFieldCodesGroup withConferencePublicationCodes(List<ConferencePublicationCode> conferencePublicationCodes) {
        this.conferencePublicationCodes = conferencePublicationCodes;
        return this;
    }

    /**
     * 
     * @return
     *     The bookFestschrifts
     */
    @JsonProperty("bookFestschrifts")
    public List<BookFestschrift> getBookFestschrifts() {
        return bookFestschrifts;
    }

    /**
     * 
     * @param bookFestschrifts
     *     The bookFestschrifts
     */
    @JsonProperty("bookFestschrifts")
    public void setBookFestschrifts(List<BookFestschrift> bookFestschrifts) {
        this.bookFestschrifts = bookFestschrifts;
    }

    public FixedFieldCodesGroup withBookFestschrifts(List<BookFestschrift> bookFestschrifts) {
        this.bookFestschrifts = bookFestschrifts;
        return this;
    }

    /**
     * 
     * @return
     *     The bookIndexAvailabilityCodes
     */
    @JsonProperty("bookIndexAvailabilityCodes")
    public List<BookIndexAvailabilityCode> getBookIndexAvailabilityCodes() {
        return bookIndexAvailabilityCodes;
    }

    /**
     * 
     * @param bookIndexAvailabilityCodes
     *     The bookIndexAvailabilityCodes
     */
    @JsonProperty("bookIndexAvailabilityCodes")
    public void setBookIndexAvailabilityCodes(List<BookIndexAvailabilityCode> bookIndexAvailabilityCodes) {
        this.bookIndexAvailabilityCodes = bookIndexAvailabilityCodes;
    }

    public FixedFieldCodesGroup withBookIndexAvailabilityCodes(List<BookIndexAvailabilityCode> bookIndexAvailabilityCodes) {
        this.bookIndexAvailabilityCodes = bookIndexAvailabilityCodes;
        return this;
    }

    /**
     * 
     * @return
     *     The bookLiteraryFormTypeCodes
     */
    @JsonProperty("bookLiteraryFormTypeCodes")
    public List<BookLiteraryFormTypeCode> getBookLiteraryFormTypeCodes() {
        return bookLiteraryFormTypeCodes;
    }

    /**
     * 
     * @param bookLiteraryFormTypeCodes
     *     The bookLiteraryFormTypeCodes
     */
    @JsonProperty("bookLiteraryFormTypeCodes")
    public void setBookLiteraryFormTypeCodes(List<BookLiteraryFormTypeCode> bookLiteraryFormTypeCodes) {
        this.bookLiteraryFormTypeCodes = bookLiteraryFormTypeCodes;
    }

    public FixedFieldCodesGroup withBookLiteraryFormTypeCodes(List<BookLiteraryFormTypeCode> bookLiteraryFormTypeCodes) {
        this.bookLiteraryFormTypeCodes = bookLiteraryFormTypeCodes;
        return this;
    }

    /**
     * 
     * @return
     *     The bookBiographyCodes
     */
    @JsonProperty("bookBiographyCodes")
    public List<BookBiographyCode> getBookBiographyCodes() {
        return bookBiographyCodes;
    }

    /**
     * 
     * @param bookBiographyCodes
     *     The bookBiographyCodes
     */
    @JsonProperty("bookBiographyCodes")
    public void setBookBiographyCodes(List<BookBiographyCode> bookBiographyCodes) {
        this.bookBiographyCodes = bookBiographyCodes;
    }

    public FixedFieldCodesGroup withBookBiographyCodes(List<BookBiographyCode> bookBiographyCodes) {
        this.bookBiographyCodes = bookBiographyCodes;
        return this;
    }

    /**
     * 
     * @return
     *     The modifiedRecordTypes
     */
    @JsonProperty("modifiedRecordTypes")
    public List<ModifiedRecordType> getModifiedRecordTypes() {
        return modifiedRecordTypes;
    }

    /**
     * 
     * @param modifiedRecordTypes
     *     The modifiedRecordTypes
     */
    @JsonProperty("modifiedRecordTypes")
    public void setModifiedRecordTypes(List<ModifiedRecordType> modifiedRecordTypes) {
        this.modifiedRecordTypes = modifiedRecordTypes;
    }

    public FixedFieldCodesGroup withModifiedRecordTypes(List<ModifiedRecordType> modifiedRecordTypes) {
        this.modifiedRecordTypes = modifiedRecordTypes;
        return this;
    }

    /**
     * 
     * @return
     *     The catalogSources
     */
    @JsonProperty("catalogSources")
    public List<CatalogSource> getCatalogSources() {
        return catalogSources;
    }

    /**
     * 
     * @param catalogSources
     *     The catalogSources
     */
    @JsonProperty("catalogSources")
    public void setCatalogSources(List<CatalogSource> catalogSources) {
        this.catalogSources = catalogSources;
    }

    public FixedFieldCodesGroup withCatalogSources(List<CatalogSource> catalogSources) {
        this.catalogSources = catalogSources;
        return this;
    }

    /**
     *
     * @return
     *     The musicFormOfCompositions
     */
    @JsonProperty("musicFormOfCompositions")
    public List<MusicFormOfCompositionCode> getMusicFormOfCompositionCodes() {
        return musicFormOfCompositionCodes;
    }

    /**
     *
     * @param musicFormOfCompositions
     *     The musicFormOfCompositions
     */
    @JsonProperty("musicFormOfCompositions")
    public void setMusicFormOfCompositionCodes(List<MusicFormOfCompositionCode> musicFormOfCompositionCodes) {
        this.musicFormOfCompositionCodes = musicFormOfCompositionCodes;
    }

    public FixedFieldCodesGroup withMusicFormOfCompositions(List<MusicFormOfCompositionCode> musicFormOfCompositionCodes) {
        this.musicFormOfCompositionCodes = musicFormOfCompositionCodes;
        return this;
    }

    /**
     *
     * @return
     *     The musicParts
     */
    @JsonProperty("musicParts")
    public List<MusicPartCode> getMusicMusicPartCodes() {
        return musicPartCodes;
    }

    /**
     *
     * @param musicPartCodes
     *     The musicParts
     */
    @JsonProperty("musicParts")
    public void setMusicPartCodes (List<MusicPartCode> musicPartCodes) {
        this.musicPartCodes = musicPartCodes;
    }

    public FixedFieldCodesGroup withMusicParts(List<MusicPartCode> musicPartCodes) {
        this.musicPartCodes = musicPartCodes;
        return this;
    }


    /**
     *
     * @return
     *     The musicFormats
     */
    @JsonProperty("musicFormats")
    public List<MusicFormatCode> getMusicFormatCodes() {
        return musicFormatCodes;
    }

    /**
     *
     * @param musicFormatCodes
     *     The musicFormats
     */
    @JsonProperty("musicFormats")
    public void setMusicFormatCodes(List<MusicFormatCode> musicFormatCodes) {
        this.musicFormatCodes = musicFormatCodes;
    }

    public FixedFieldCodesGroup withMusicFormats(List<MusicFormatCode> musicFormatCodes) {
        this.musicFormatCodes = musicFormatCodes;
        return this;
    }
}
