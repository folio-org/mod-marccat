package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.F;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.dao.persistence.T_BIB_HDR;
import org.folio.cataloging.domain.GeneralInformation;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.Validation;
import org.folio.rest.jaxrs.model.Field;
import org.folio.rest.jaxrs.model.FieldCollection;
import org.folio.rest.jaxrs.model.FixedField;
import org.folio.rest.jaxrs.model.VariableField;
import org.folio.rest.jaxrs.resource.CatalogingFieldsResource;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * FieldsAPI RestFul service to manage fields (tag).
 *
 * @author nbianchini
 * @author agazzarini
 * @since 1.0
 */
public class FieldsAPI implements CatalogingFieldsResource {

    protected final Log logger = new Log(FieldsAPI.class);

    /**
     * Gets the mandatory fields to create a new field.
     */
    @Override
    public void getCatalogingFieldsBibliographicMandatory(final String lang,
                                                          final Map<String, String> okapiHeaders,
                                                          final Handler<AsyncResult<Response>> asyncResultHandler,
                                                          final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final Map<String, String> configuration = getConfigurationValues(vertxContext);
                final FieldCollection container = new FieldCollection();
                container.setFields(
                        asList(createRequiredLeaderField(configuration, storageService, lang),
                                createControlNumberField(configuration, storageService, lang),
                                createRequiredMaterialDescriptionField(configuration, storageService, lang),
                                createCatalogingSourceField(configuration, storageService, lang)));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    /**
     * Creates a cataloging source field (tag 040) using default values.
     *
     * @param configuration the configuration parameters
     * @param storageService the storage service.
     * @param lang the lang code.
     * @return a new 040 {@link Field} entity populated with default values.
     */
    private Field createCatalogingSourceField(final Map<String, String> configuration, final StorageService storageService, final String lang) {
        final CorrelationValues correlationValues =
                new CorrelationValues(
                        Global.CATALOGING_SOURCE_HEADER_TYPE,
                        Global.CORRELATION_UNDEFINED,
                        Global.CORRELATION_UNDEFINED);

        final String description = getDescriptionFixedField(storageService, lang, Global.CATALOGING_SOURCE_HEADER_TYPE);
        final Validation validation =
                storageService.getSubfieldsByCorrelations(
                        Integer.toString(Global.INT_CATEGORY),
                        correlationValues.getValue(1),
                        correlationValues.getValue(2),
                        correlationValues.getValue(3));

        final VariableField catalogingSourceField = new VariableField();

        catalogingSourceField.setDescription(description);
        catalogingSourceField.setCategoryCode(Global.INT_CATEGORY);
        catalogingSourceField.setCode(Global.CATALOGING_SOURCE_TAG_NUMBER);
        catalogingSourceField.setHeadingTypeCode(Integer.toString(Global.CATALOGING_SOURCE_HEADER_TYPE));
        catalogingSourceField.setSubfields(stream(validation.getMarcValidSubfieldStringCode().split("")).collect(toList()));
        catalogingSourceField.setDefaultSubfieldCode(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));

        catalogingSourceField.setValue(configuration.get("bibliographicItem.cataloguingSourceStringText"));

        final Field field = new Field();
        field.setVariableField(catalogingSourceField);
        field.setCode(Global.CATALOGING_SOURCE_TAG_NUMBER);

        return field;
    }

    /**
     * create default control field value
     *
     * @param configuration the configuration parameters
     * @param storageService
     * @param lang
     * @return
     */
    private Field createControlNumberField(final Map<String, String> configuration, final StorageService storageService, final String lang)
    {
        final String description = getDescriptionFixedField(storageService, lang, Global.CONTROL_NUMBER_HEADER_TYPE);
        final FixedField controlNumberFixedField = new FixedField();
        controlNumberFixedField.setCategoryCode(Global.INT_CATEGORY);
        controlNumberFixedField.setCode(Global.CONTROL_NUMBER_TAG_NUMBER);
        controlNumberFixedField.setDisplayValue(Global.DECIMAL_FORMAT_AN.format(0));
        controlNumberFixedField.setDescription(description);
        controlNumberFixedField.setHeaderTypeCode(Global.CONTROL_NUMBER_HEADER_TYPE);

        final Field field = new Field();
        field.setFixedField(controlNumberFixedField);
        field.setCode(Global.CONTROL_NUMBER_TAG_NUMBER);

        return field;

    }

    /**
     * Gets description fixed field related to type of selected tag/field.
     *
     * @param storageService
     * @param lang the language used here as filter criterion.
     * @param code1 the first correlation or header type code selected.
     * @return string description.
     */
    private String getDescriptionFixedField(final StorageService storageService, final String lang, final int code1)  {
        return storageService.getHeadingTypeDescription( (short)code1, lang, T_BIB_HDR.class);
    }

    /**
     * Create default leader
     * @param configuration the configuration parameters
     * @param storageService
     * @param lang
     * @return
     */
    private Field createRequiredLeaderField(final Map<String, String> configuration, final StorageService storageService, final String lang) {

        final String description = getDescriptionFixedField(storageService, lang, Global.LEADER_HEADER_TYPE);
        final FixedField leader = new FixedField();
        leader.setCategoryCode(Global.INT_CATEGORY);
        leader.setHeaderTypeCode(Global.LEADER_HEADER_TYPE);
        leader.setCode(Global.LEADER_TAG_NUMBER);
        leader.setDescription(description);
        leader.setDisplayValue(getLeaderValue());

        final Field field = new Field();
        field.setFixedField(leader);
        field.setCode(Global.LEADER_TAG_NUMBER);

        return field;
    }

    /**
     * Creates default 008 field.
     *
     * @param configuration the configuration parameters
     * @param storageService the storage service.
     * @param lang tlang associated with the current request.
     * @return the 008 default tag definition.
     */
    private Field createRequiredMaterialDescriptionField(final Map<String, String> configuration, final StorageService storageService, final String lang) {

        final String description = getDescriptionFixedField(storageService, lang, Global.MATERIAL_DESCRIPTION_HEADER_TYPE);

        final GeneralInformation generalInformation = new GeneralInformation();
        generalInformation.setMaterialDescription008Indicator("1");
        generalInformation.setFormOfMaterial(Global.bookformOfMaterial); //book
        generalInformation.setDefaultValues(configuration);
        generalInformation.setEnteredOnFileDateYYMMDD(F.getFormattedDateYYMMDD());

        final FixedField materialDescription = new FixedField();
        materialDescription.setCategoryCode(Global.INT_CATEGORY);
        materialDescription.setHeaderTypeCode(Global.MATERIAL_DESCRIPTION_HEADER_TYPE);
        materialDescription.setCode(Global.MATERIAL_DESCRIPTION_TAG_NUMBER);
        materialDescription.setDescription(description);
        materialDescription.setDisplayValue(generalInformation.getValueString());

        final Field field = new Field();
        field.setFixedField(materialDescription);
        field.setCode(Global.MATERIAL_DESCRIPTION_TAG_NUMBER);

        return field;
    }

    /**
     * sets default leader value
     * @return
     */
    private String getLeaderValue() {
        return new StringBuilder(Global.fixedLeaderLength)
                .append(Global.recordStatusCode)
                .append(Global.recordTypeCode)
                .append(Global.bibliographicLevelCode)
                .append(Global.controlTypeCode)
                .append(Global.characterCodingSchemeCode)
                .append(Global.fixedLeaderBaseAddress)
                .append(Global.encodingLevel)
                .append(Global.descriptiveCataloguingCode)
                .append(Global.linkedRecordCode)
                .append(Global.fixedLeaderPortion).toString();
    }

    /**
     * Reads parameters from configuration module
     * @param vertxContext
     * @return
     */
    private Map<String, String> getConfigurationValues(final Context vertxContext){

        final Map<String, String> configuration = new HashMap<>();

        Defaults.getString("bibliographicItem.recordCataloguingSourceCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("bibliographicItem.recordCataloguingSourceCode", asyncHandlerResult.result());
            } else {
                configuration.put("bibliographicItem.recordCataloguingSourceCode", Global.recordCataloguingSourceCode);
            }
        });

        Defaults.getString("bibliographicItem.languageCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("bibliographicItem.languageCode", asyncHandlerResult.result());
            } else {
                configuration.put("bibliographicItem.languageCode", Global.languageCode);
            }
        });

        Defaults.getString("material.bookIllustrationCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("material.bookIllustrationCode", asyncHandlerResult.result());
            } else {
                configuration.put("material.bookIllustrationCode", Global.bookIllustrationCode);
            }
        });

        Defaults.getString("material.targetAudienceCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("material.targetAudienceCode", String.valueOf(asyncHandlerResult.result()));
            } else {
                configuration.put("material.targetAudienceCode", String.valueOf(Global.targetAudienceCode));
            }
        });

        Defaults.getString("material.formOfItemCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("material.formOfItemCode", String.valueOf(asyncHandlerResult.result()));
            } else {
                configuration.put("material.formOfItemCode", String.valueOf(Global.formOfItemCode));
            }
        });

        Defaults.getString("material.natureOfContentsCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("material.natureOfContentsCode", asyncHandlerResult.result());
            } else {
                configuration.put("material.natureOfContentsCode", Global.natureOfContentsCode);
            }
        });

        Defaults.getString("material.governmentPublicationCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("material.governmentPublicationCode", String.valueOf(asyncHandlerResult.result()));
            } else {
                configuration.put("material.governmentPublicationCode", String.valueOf(Global.governmentPublicationCode));
            }
        });

        Defaults.getString("material.conferencePublicationCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("material.conferencePublicationCode", String.valueOf(asyncHandlerResult.result()));
            } else {
                configuration.put("material.conferencePublicationCode", String.valueOf(Global.conferencePublicationCode));
            }
        });

        Defaults.getString("material.bookFestschrift", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("material.bookFestschrift", String.valueOf(asyncHandlerResult.result()));
            } else {
                configuration.put("material.bookFestschrift", String.valueOf(Global.bookFestschrift));
            }
        });

        Defaults.getString("material.bookIndexAvailabilityCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("material.bookIndexAvailabilityCode", String.valueOf(asyncHandlerResult.result()));
            } else {
                configuration.put("material.bookIndexAvailabilityCode", String.valueOf(Global.bookIndexAvailabilityCode));
            }
        });

        Defaults.getString("material.bookLiteraryFormTypeCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("material.bookLiteraryFormTypeCode", String.valueOf(asyncHandlerResult.result()));
            } else {
                configuration.put("material.bookLiteraryFormTypeCode", String.valueOf(Global.bookLiteraryFormTypeCode));
            }
        });

        Defaults.getString("material.bookBiographyCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("material.bookBiographyCode", String.valueOf(asyncHandlerResult.result()));
            } else {
                configuration.put("material.bookBiographyCode", String.valueOf(Global.bookBiographyCode));
            }
        });

        Defaults.getString("bibliographicItem.itemDateTypeCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("bibliographicItem.itemDateTypeCode", String.valueOf(asyncHandlerResult.result()));
            } else {
                configuration.put("bibliographicItem.itemDateTypeCode", String.valueOf(Global.itemDateTypeCode));
            }
        });

        Defaults.getString("bibliographicItem.marcCountryCode", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("bibliographicItem.marcCountryCode", asyncHandlerResult.result());
            } else {
                configuration.put("bibliographicItem.marcCountryCode", Global.undefinedMarcCountryCode);
            }
        });
        Defaults.getString("bibliographicItem.cataloguingSourceStringText", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("bibliographicItem.cataloguingSourceStringText", asyncHandlerResult.result());
            } else {
                configuration.put("bibliographicItem.cataloguingSourceStringText", Global.cataloguingSourceStringText);
            }
        });
        Defaults.getString("bibliographicItem.languageOfCataloguing", vertxContext).setHandler(asyncHandlerResult -> {
            if (asyncHandlerResult.succeeded()) {
                configuration.put("bibliographicItem.languageOfCataloguing", asyncHandlerResult.result());
            } else {
                configuration.put("bibliographicItem.languageOfCataloguing", Global.languageOfCataloguing);
            }
        });


        return configuration;
    }


    @Override
    public void postCatalogingFieldsBibliographicMandatory(String lang, Field entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
