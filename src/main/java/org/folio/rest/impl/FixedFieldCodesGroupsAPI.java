package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.*;
import org.folio.rest.jaxrs.resource.CatalogingFixedFieldResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

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
                if (isFixedField(code)){
                    final FixedFieldCodesGroup fixedFieldCodesGroup = new FixedFieldCodesGroup();
                    if (code.equals(Global.LEADER_TAG_NUMBER)){
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

                    if (code.equals(Global.MATERIAL_TAG_CODE)){

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
                }
                return null;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext, "bibliographic", "material");

    }

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
     * Check if is a fixedField or not.
     *
     * @param code the tag number code.
     * @return true if is fixed-field, false otherwise.
     */
    private boolean isFixedField(final String code) {
        return Global.FIXED_FIELDS.contains(code);
    }

    @Override
    public void deleteCatalogingFixedFieldCodesGroups(String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void putCatalogingFixedFieldCodesGroups(String lang, FixedFieldCodesGroup entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
