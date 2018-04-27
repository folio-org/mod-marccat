package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.domain.GeneralInformation;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Fixed-Field Codes Groups RESTful APIs.
 *
 * @author nbianchini
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Fixed field code group resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class FixedFieldCodesGroupAPI extends BaseResource {

    @ApiOperation(value = "Returns all mandatory groups.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested mandatory groups"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/fixed-fields-code-groups")
    public FixedFieldCodesGroup getFixedFieldCodesGroups(
            @RequestParam final String code,
            @RequestParam final int headerTypeCode,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
                final FixedFieldCodesGroup fixedFieldCodesGroup = new FixedFieldCodesGroup();
            return ofNullable(code)
                    .map(tag -> {
                        if (tag.equals(Global.LEADER_TAG_NUMBER)){
                            injectLeaderCodes(fixedFieldCodesGroup, storageService, lang);
                        } else if (tag.equals(Global.MATERIAL_TAG_CODE) || tag.equals(Global.OTHER_MATERIAL_TAG_CODE)){
                            injectMaterialCodes(fixedFieldCodesGroup, storageService, lang, headerTypeCode, tag);
                        } else if (tag.equals(Global.PHYSICAL_DESCRIPTION_TAG_CODE)){
                            // TODO
                        } else {
                            logger.error(String.format(MessageCatalog._00017_CODES_GROUPS_NOT_AVAILABLE, code));
                            return null;
                        }
                        return fixedFieldCodesGroup;
                    }).orElse(null);
        }, tenant, configurator);
    }


    /**
     * Adapter that converts existing stringValue object in catalog source Okapi resource.
     */
    private Function<Avp<String>, CatalogSource> toCatalogSource = source -> {
        final CatalogSource catalogSource = new CatalogSource();
        catalogSource.setCode(source.getValue());
        catalogSource.setDescription(source.getLabel());
        return catalogSource;
    };

    /**
     * Adapter that converts existing stringValue object in date type Okapi resource.
     */
    private Function<Avp<String>, DateType> toDateType = source -> {
        final DateType dateType = new DateType();
        dateType.setCode(source.getValue());
        dateType.setDescription(source.getLabel());
        return dateType;
    };

    /**
     * Adapter that converts existing stringValue object in record status type Okapi resource.
     */
    private Function<Avp<String>, RecordStatusType> toRecordStatusType = source -> {
        final RecordStatusType recordStatusType = new RecordStatusType();
        recordStatusType.setCode(source.getValue());
        recordStatusType.setDescription(source.getLabel());
        return recordStatusType;
    };

    /**
     * Adapter that converts existing stringValue object in Record type Okapi resource.
     */
    private Function<Avp<String>, RecordType> toRecordType = source -> {
        final RecordType recordType = new RecordType();
        recordType.setCode(source.getValue());
        recordType.setDescription(source.getLabel());
        return recordType;
    };

    /**
     * Adapter that converts existing stringValue object in bibliographic level Okapi resource.
     */
    private Function<Avp<String>, BibliographicLevel> toBibliographicLevel = source -> {
        final BibliographicLevel bibliographicLevel = new BibliographicLevel();
        bibliographicLevel.setCode(source.getValue());
        bibliographicLevel.setDescription(source.getLabel());
        return bibliographicLevel;
    };

    /**
     * Adapter that converts existing stringValue object in encoding level Okapi resource.
     */
    private Function<Avp<String>, EncodingLevel> toEncodingLevel = source -> {
        final EncodingLevel encodingLevel = new EncodingLevel();
        encodingLevel.setCode(source.getValue());
        encodingLevel.setDescription(source.getLabel());
        return encodingLevel;
    };

    /**
     * Adapter that converts existing stringValue object in descriptive catalog form Okapi resource.
     */
    private Function<Avp<String>, DescriptiveCatalogForm> toDescriptiveCatalogForm = source -> {
        final DescriptiveCatalogForm descriptiveCatalogForm = new DescriptiveCatalogForm();
        descriptiveCatalogForm.setCode(source.getValue());
        descriptiveCatalogForm.setDescription(source.getLabel());
        return descriptiveCatalogForm;
    };

    /**
     * Adapter that converts existing stringValue object in linked record code Okapi resource.
     */
    private Function<Avp<String>, LinkedRecordCode> toLinkedRecord = source -> {
        final LinkedRecordCode linkedRecordCode = new LinkedRecordCode();
        linkedRecordCode.setCode(source.getValue());
        linkedRecordCode.setDescription(source.getLabel());
        return linkedRecordCode;
    };

    /**
     * Adapter that converts existing stringValue object in character encoding schema Okapi resource.
     */
    private Function<Avp<String>, CharacterEncodingSchema> toCharacterEncodingSchema = source -> {
        final CharacterEncodingSchema characterEncodingSchema = new CharacterEncodingSchema();
        characterEncodingSchema.setCode(source.getValue());
        characterEncodingSchema.setDescription(source.getLabel());
        return characterEncodingSchema;
    };

    /**
     * Adapter that converts existing stringValue object in control type Okapi resource.
     */
    private Function<Avp<String>, ControlType> toControlType = source -> {
        final ControlType controlType = new ControlType();
        controlType.setCode(source.getValue());
        controlType.setDescription(source.getLabel());
        return controlType;
    };

    /**
     * Adapter that converts existing stringValue object in Book Literary Form type Okapi resource.
     */
    private Function<Avp<String>, BookLiteraryFormTypeCode> toBookLiteraryForm = source -> {
        final BookLiteraryFormTypeCode bookLiteraryForm = new BookLiteraryFormTypeCode();
        bookLiteraryForm.setCode(source.getValue());
        bookLiteraryForm.setDescription(source.getLabel());
        return bookLiteraryForm;
    };

    /**
     * Adapter that converts existing stringValue object in Music Format type Okapi resource.
     */
    private Function<Avp<String>, MusicFormatCode> toMusicFormat = source -> {
        final MusicFormatCode musicFormat = new MusicFormatCode();
        musicFormat.setCode(source.getValue());
        musicFormat.setDescription(source.getLabel());
        return musicFormat;
    };

    /**
     * Adapter that converts existing stringValue object in Music Part type Okapi resource.
     */
    private Function<Avp<String>, MusicPartCode> toMusicPart = source -> {
        final MusicPartCode musicPart = new MusicPartCode();
        musicPart.setCode(source.getValue());
        musicPart.setDescription(source.getLabel());
        return musicPart;
    };

    /**
     * Adapter that converts existing stringValue object in Music Form of Composition type Okapi resource.
     */
    private Function<Avp<String>, MusicFormOfCompositionCode> toMusicFormOfComposition = source -> {
        final MusicFormOfCompositionCode musicFormOfComposition = new MusicFormOfCompositionCode();
        musicFormOfComposition.setCode(source.getValue());
        musicFormOfComposition.setDescription(source.getLabel());
        return musicFormOfComposition;
    };

    /**
     * Adapter that converts existing stringValue object in festschrift type Okapi resource.
     */
    private Function<Avp<String>, BookFestschrift> toFestschrift = source -> {
        final BookFestschrift festschrift = new BookFestschrift();
        festschrift.setCode(source.getValue());
        festschrift.setDescription(source.getLabel());
        return festschrift;
    };

    /**
     * Adapter that converts existing stringValue object in target audience type Okapi resource.
     */
    private Function<Avp<String>, TargetAudienceCode> toTargetAudience = source -> {
        final TargetAudienceCode targetAudience = new TargetAudienceCode();
        targetAudience.setCode(source.getValue());
        targetAudience.setDescription(source.getLabel());
        return targetAudience;
    };

    /**
     * Adapter that converts existing stringValue object in format of item code Okapi resource.
     */
    private Function<Avp<String>, FormOfItemCode> toFormOfItem = source -> {
        final FormOfItemCode formOfItemCode = new FormOfItemCode();
        formOfItemCode.setCode(source.getValue());
        formOfItemCode.setDescription(source.getLabel());
        return formOfItemCode;
    };

    /**
     * Adapter that converts existing stringValue object in modified record type Okapi resource.
     */
    private Function<Avp<String>, ModifiedRecordType> toModifiedRecordType = source -> {
        final ModifiedRecordType modifiedRecordType = new ModifiedRecordType();
        modifiedRecordType.setCode(source.getValue());
        modifiedRecordType.setDescription(source.getLabel());
        return modifiedRecordType;
    };

    /**
     * Adapter that converts existing stringValue object in book illustration code Okapi resource.
     */
    private Function<Avp<String>, BookIllustrationCode> toBookIllustration = source -> {
        final BookIllustrationCode bookIllustrationCode = new BookIllustrationCode();
        bookIllustrationCode.setCode(source.getValue());
        bookIllustrationCode.setDescription(source.getLabel());
        return bookIllustrationCode;
    };

    /**
     * Adapter that converts existing stringValue object in nature of content code Okapi resource.
     */
    private Function<Avp<String>, NatureOfContent> toNatureOfContent = source -> {
        final NatureOfContent natureOfContent = new NatureOfContent();
        natureOfContent.setCode(source.getValue());
        natureOfContent.setDescription(source.getLabel());
        return natureOfContent;
    };

    /**
     * Inject codes groups for 008 and 006 tags.
     *
     * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
     * @param storageService the storage service.
     * @param lang the lang associated with the current request.
     * @param headerTypeCode the header type code selected.
     * @param tag the tag code.
     */
    private void injectMaterialCodes(final FixedFieldCodesGroup fixedFieldCodesGroup,
                                     final StorageService storageService,
                                     final String lang,
                                     final int headerTypeCode,
                                     final String tag) {
        ofNullable(storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode, tag))
                .map(mapRecordTypeMaterial -> {
                    final GeneralInformation gi = new GeneralInformation();
                    gi.setFormOfMaterial((String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL));
                    if (gi.isBook()){
                        final List<BookIllustrationCode> bookIllustrations = storageService.getBookIllustrations(lang).stream().map(toBookIllustration).collect(toList());
                        fixedFieldCodesGroup.setBookIllustrationCodes1(bookIllustrations);
                        fixedFieldCodesGroup.setBookIllustrationCodes2(bookIllustrations);
                        fixedFieldCodesGroup.setBookIllustrationCodes3(bookIllustrations);
                        fixedFieldCodesGroup.setBookIllustrationCodes4(bookIllustrations);

                        fixedFieldCodesGroup.setTargetAudienceCodes(storageService.getTargetAudiences(lang).stream().map(toTargetAudience).collect(toList()));
                        fixedFieldCodesGroup.setFormOfItemCodes(storageService.getFormOfItems(lang).stream().map(toFormOfItem).collect(toList()));

                        final List<NatureOfContent> natureOfContents = storageService.getNatureOfContents(lang).stream().map(toNatureOfContent).collect(toList());
                        fixedFieldCodesGroup.setNatureOfContents1(natureOfContents);
                        fixedFieldCodesGroup.setNatureOfContents2(natureOfContents);
                        fixedFieldCodesGroup.setNatureOfContents3(natureOfContents);
                        fixedFieldCodesGroup.setNatureOfContents4(natureOfContents);

                        fixedFieldCodesGroup.setBookFestschrifts(storageService.getFestschrifts(lang).stream().map(toFestschrift).collect(toList()));
                        fixedFieldCodesGroup.setBookLiteraryFormTypeCodes(storageService.getLiteraryForms(lang).stream().map(toBookLiteraryForm).collect(toList()));
                    }else if (gi.isMusic()){
                        fixedFieldCodesGroup.setMusicFormatCodes(storageService.getMusicFormats(lang).stream().map(toMusicFormat).collect(toList()));
                        fixedFieldCodesGroup.setMusicFormOfCompositionCodes(storageService.getMusicFormsOfComposition(lang).stream().map(toMusicFormOfComposition).collect(toList()));
                        fixedFieldCodesGroup.setMusicPartCodes(storageService.getMusicParts(lang).stream().map(toMusicPart).collect(toList()));
                    }
                    if (tag.equals(Global.MATERIAL_TAG_CODE)) {
                        injectDefaultMaterialType(fixedFieldCodesGroup, storageService, lang);
                    }

                    return fixedFieldCodesGroup;
                }).orElseGet(() -> {
                    logger.error(MessageCatalog._00019_HEADER_TYPE_ID_WRONG, tag);
                    return null;
                });
    }

    /**
     * Inject codes groups common to all types of 008 tag.
     *
     * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
     * @param storageService the storage service.
     * @param lang the lang associated with the current request.
     */
    private void injectDefaultMaterialType(final FixedFieldCodesGroup fixedFieldCodesGroup, final StorageService storageService, final String lang) {
        fixedFieldCodesGroup.setDateTypes(
                storageService.getDateTypes(lang)
                        .stream()
                        .map(toDateType)
                        .collect(toList()));

        fixedFieldCodesGroup.setModifiedRecordTypes(
                storageService.getModifiedRecordTypes(lang)
                        .stream()
                        .map(toModifiedRecordType)
                        .collect(toList()));

        fixedFieldCodesGroup.setCatalogSources(
                storageService.getCatalogSources(lang)
                        .stream()
                        .map(toCatalogSource)
                        .collect(toList()));

    }

    /**
     * Inject codes group for leader tag.
     *
     * @param fixedFieldCodesGroup the fixedFieldCodesGroup to populate.
     * @param storageService the storage service.
     * @param lang the lang associated with the current request.
     */
    private void injectLeaderCodes(final FixedFieldCodesGroup fixedFieldCodesGroup, final StorageService storageService, final String lang) {
        fixedFieldCodesGroup.setRecordStatusTypes(
                storageService.getRecordStatusTypes(lang)
                        .stream()
                        .map(toRecordStatusType).collect(toList()));
        fixedFieldCodesGroup.setRecordTypes(
                storageService.getRecordTypes(lang)
                        .stream()
                        .map(toRecordType).collect(toList()));
        fixedFieldCodesGroup.setBibliographicLevels(
                storageService.getBibliographicLevels(lang)
                        .stream()
                        .map(toBibliographicLevel).collect(toList()));
        fixedFieldCodesGroup.setControlTypes(
                storageService.getControlTypes(lang)
                    .stream()
                    .map(toControlType).collect(toList()));
        fixedFieldCodesGroup.setCharacterEncodingSchemas(
                storageService.getCharacterEncodingSchemas(lang)
                    .stream()
                    .map(toCharacterEncodingSchema).collect(toList()));
        fixedFieldCodesGroup.setEncodingLevels(
                storageService.getEncodingLevels(lang)
                    .stream()
                    .map(toEncodingLevel).collect(toList()));
        fixedFieldCodesGroup.setDescriptiveCatalogForms(
                storageService.getDescriptiveCatalogForms(lang)
                    .stream()
                    .map(toDescriptiveCatalogForm).collect(toList()));
        fixedFieldCodesGroup.setLinkedRecordCodes(
                storageService.getMultipartResourceLevels(lang)
                    .stream()
                    .map(toLinkedRecord).collect(toList()));

    }
}
