package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.domain.GeneralInformation;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.*;
import org.folio.rest.jaxrs.resource.CatalogingFixedFieldResource;

import javax.ws.rs.core.Response;
import java.util.Map;
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
public class FixedFieldCodesGroupsAPI implements CatalogingFixedFieldResource {
    protected final Log logger = new Log(FixedFieldCodesGroupsAPI.class);

    @Override
    public void getCatalogingFixedFieldCodesGroups(final String code,
                                                   final int headerTypeCode,
                                                   final String lang,
                                                   final Map<String, String> okapiHeaders,
                                                   final Handler<AsyncResult<Response>> asyncResultHandler,
                                                   final Context vertxContext) throws Exception {

        doGet((storageService, configuration, future) -> {
            try {
                final FixedFieldCodesGroup fixedFieldCodesGroup = new FixedFieldCodesGroup();
                return ofNullable(code)
                        .map(tag -> {
                            if (tag.equals(Global.LEADER_TAG_NUMBER)){
                                injectLeaderCodes(fixedFieldCodesGroup, storageService, lang);
                            } else if (tag.equals(Global.MATERIAL_TAG_CODE) || tag.equals(Global.OTHER_MATERIAL_TAG_CODE)){
                                injectMaterialCodes(fixedFieldCodesGroup, storageService, lang, headerTypeCode, tag);
                            } else if (tag.equals(Global.PHYSICAL_DESCRIPTION_TAG_CODE)){ //TODO
                                //
                            } else {
                                logger.error(String.format(MessageCatalog._00017_CODES_GROUPS_NOT_AVAILABLE, code));
                                return null;
                            }
                            return fixedFieldCodesGroup;
                        }).orElse(null);

            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

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
                        fixedFieldCodesGroup.setTargetAudiences(storageService.getTargetAudiences(lang).stream().map(toTargetAudience).collect(toList()));
                        fixedFieldCodesGroup.setFestschrifts(storageService.getFestschrifts(lang).stream().map(toFestschrift).collect(toList()));
                        fixedFieldCodesGroup.setBookLiteraryForms(storageService.getLiteraryForms(lang).stream().map(toBookLiteraryForm).collect(toList()));
                    }else if (gi.isMusic()){
                        fixedFieldCodesGroup.setMusicFormats(storageService.getMusicFormats(lang).stream().map(toMusicFormat).collect(toList()));
                        fixedFieldCodesGroup.setMusicFormOfCompositions(storageService.getMusicFormsOfComposition(lang).stream().map(toMusicFormOfComposition).collect(toList()));
                        fixedFieldCodesGroup.setMusicParts(storageService.getMusicParts(lang).stream().map(toMusicPart).collect(toList()));
                    }
                    if (tag.equals(Global.MATERIAL_TAG_CODE)) {
                        injectDefaultMaterialType(fixedFieldCodesGroup, storageService, lang);
                    }

                    return fixedFieldCodesGroup;
                }).orElseGet(() -> {
                    logger.error(MessageCatalog._00018_HEADER_TYPE_ID_WRONG, tag);
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
                        .map(toRecordStatusType)
                        .collect(toList()));
        fixedFieldCodesGroup.setRecordTypes(
                storageService.getRecordTypes(lang)
                        .stream()
                        .map(toRecordType)
                        .collect(toList()));
        fixedFieldCodesGroup.setBibliographicLevels(
                storageService.getBibliographicLevels(lang)
                        .stream()
                        .map(toBibliographicLevel)
                        .collect(toList()));
    }

    /**
     * Adapter that converts existing stringValue object in Book Literary Form type Okapi resource.
     */
    private Function<Avp<String>, BookLiteraryForm> toBookLiteraryForm = source -> {
        final BookLiteraryForm bookLiteraryForm = new BookLiteraryForm();
        bookLiteraryForm.setCode(source.getValue());
        bookLiteraryForm.setDescription(source.getLabel());
        return bookLiteraryForm;
    };

    /**
     * Adapter that converts existing stringValue object in Music Format type Okapi resource.
     */
    private Function<Avp<String>, MusicFormat> toMusicFormat = source -> {
        final MusicFormat musicFormat = new MusicFormat();
        musicFormat.setCode(source.getValue());
        musicFormat.setDescription(source.getLabel());
        return musicFormat;
    };

    /**
     * Adapter that converts existing stringValue object in Music Part type Okapi resource.
     */
    private Function<Avp<String>, MusicPart> toMusicPart = source -> {
        final MusicPart musicPart = new MusicPart();
        musicPart.setCode(source.getValue());
        musicPart.setDescription(source.getLabel());
        return musicPart;
    };

    /**
     * Adapter that converts existing stringValue object in Music Form of Composition type Okapi resource.
     */
    private Function<Avp<String>, MusicFormOfComposition> toMusicFormOfComposition = source -> {
        final MusicFormOfComposition musicFormOfComposition = new MusicFormOfComposition();
        musicFormOfComposition.setCode(source.getValue());
        musicFormOfComposition.setDescription(source.getLabel());
        return musicFormOfComposition;
    };

    /**
     * Adapter that converts existing stringValue object in festschrift type Okapi resource.
     */
    private Function<Avp<String>, Festschrift> toFestschrift = source -> {
        final Festschrift festschrift = new Festschrift();
        festschrift.setCode(source.getValue());
        festschrift.setDescription(source.getLabel());
        return festschrift;
    };

    /**
     * Adapter that converts existing stringValue object in target audience type Okapi resource.
     */
    private Function<Avp<String>, TargetAudience> toTargetAudience = source -> {
        final TargetAudience targetAudience = new TargetAudience();
        targetAudience.setCode(source.getValue());
        targetAudience.setDescription(source.getLabel());
        return targetAudience;
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
     * Check if is a fixed-field or not.
     *
     * @param code the tag number code.
     * @return true if is fixed-field, false otherwise.
     */
    private boolean isFixedField(final String code) {
        return Global.FIXED_FIELDS.contains(code);
    }

    @Override
    public void deleteCatalogingFixedFieldCodesGroups(final String lang,
                                                      final Map<String, String> okapiHeaders,
                                                      final Handler<AsyncResult<Response>> asyncResultHandler,
                                                      final Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void putCatalogingFixedFieldCodesGroups(final String lang,
                                                   final FixedFieldCodesGroup entity,
                                                   final Map<String, String> okapiHeaders,
                                                   final Handler<AsyncResult<Response>> asyncResultHandler,
                                                   final Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
