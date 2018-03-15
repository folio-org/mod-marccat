package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.F;
import org.folio.cataloging.Global;
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
        doGet((storageService, configuration, future) -> {
            try {
                final FieldCollection container = new FieldCollection();
                container.setFields(
                        asList(createRequiredLeaderField(storageService, lang),
                                createControlNumberField(storageService, lang),
                                createRequiredMaterialDescriptionField(configuration, storageService, lang),
                                createCatalogingSourceField(configuration, storageService, lang)));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext, "bibliographic", "material");
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
                        Global.INT_CATEGORY,
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
     * @param storageService the storage service.
     * @param lang the lang associated with the current request.
     * @return a new 001 {@link Field} entity populated with default values.
     */
    private Field createControlNumberField(final StorageService storageService, final String lang)
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
     * @param storageService the storage service.
     * @param lang the lang associated with the current request.
     * @param code1 the first correlation or header type code selected.
     * @return string description.
     */
    private String getDescriptionFixedField(final StorageService storageService, final String lang, final int code1)  {
        return storageService.getHeadingTypeDescription( (short)code1, lang, T_BIB_HDR.class);
    }

    /**
     * Create a leader with default values.
     *
     * @param storageService the storage service.
     * @param lang the lang associated with the current request.
     * @return a new leader {@link Field} entity populated with default values.
     */
    private Field createRequiredLeaderField(final StorageService storageService, final String lang) {

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
     * @param lang the lang associated with the current request.
     * @return the 008 default tag definition.
     */
    private Field createRequiredMaterialDescriptionField(final Map<String, String> configuration, final StorageService storageService, final String lang) {

        final String description = getDescriptionFixedField(storageService, lang, Global.MATERIAL_DESCRIPTION_HEADER_TYPE);

        final GeneralInformation generalInformation = new GeneralInformation();
        generalInformation.setMaterialDescription008Indicator("1");
        generalInformation.setFormOfMaterial(Global.bookformOfMaterial); //book
        generalInformation.setDefaultValues(configuration);
        generalInformation.setEnteredOnFileDateYYMMDD(F.getFormattedDate("yyMMdd"));

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
     * Sets the default leader value.
     *
     * @return the default leader value.
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
                .append(Global.fixedLeaderPortion)
                .toString();
    }

    @Override
    public void postCatalogingFieldsBibliographicMandatory(String lang, Field entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
        throw new IllegalArgumentException();
    }
}