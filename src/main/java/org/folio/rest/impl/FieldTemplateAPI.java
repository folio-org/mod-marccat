package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.F;
import org.folio.cataloging.Global;
import org.folio.cataloging.domain.GeneralInformation;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.Validation;
import org.folio.rest.jaxrs.model.FieldTemplate;
import org.folio.rest.jaxrs.model.FixedField;
import org.folio.rest.jaxrs.model.VariableField;
import org.folio.rest.jaxrs.resource.CatalogingFieldTemplateResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 *
 * FieldTemplate Restful API
 *
 * @author natasciab
 * @since 1.0
 */
public class FieldTemplateAPI implements CatalogingFieldTemplateResource {

    protected final Log logger = new Log(FieldTemplateAPI.class);

    @Override
    public void getCatalogingFieldTemplate(final int categoryCode,
                                           final String ind1, final String ind2,
                                           final String code, final Integer headerType,
                                           final String leader,
                                           final String valueField,
                                           final String lang,
                                           final Map<String, String> okapiHeaders,
                                           final Handler<AsyncResult<Response>> asyncResultHandler,
                                           final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                return !isFixedField(code)
                    ? ofNullable(storageService.getCorrelationVariableField(categoryCode, ind1, ind2, code))
                            .map(correlationValues -> {
                                final Validation validation = storageService.getSubfieldsByCorrelations(
                                        categoryCode,
                                        correlationValues.getValue(1),
                                        correlationValues.getValue(2),
                                        correlationValues.getValue(3));

                                final FieldTemplate fieldTemplate = new FieldTemplate();
                                fieldTemplate.setVariableField(
                                        getVariableField(
                                                categoryCode,
                                                ind1,
                                                ind2,
                                                code,
                                                correlationValues,
                                                storageService.getHeadingTypeDescription(
                                                        correlationValues.getValue(1),
                                                        lang,
                                                        categoryCode),
                                                validation));
                                fieldTemplate.setCode(code);
                                return fieldTemplate;
                            }).orElse(null)
                    :   ofNullable(getFixedField(storageService, headerType, code, leader, valueField, vertxContext, lang, configuration))
                            .map(fixedField -> {
                                final FieldTemplate fieldT = new FieldTemplate();
                                fieldT.setFixedField(fixedField);
                                fieldT.setCode(code);
                                return fieldT;
                            }).orElse(null) ;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext, "bibliographic", "material");

    }

    /**
     * Gets the fixed-field associated to header type code.
     *
     * In case of 008 field, leader will be used to get related values:
     * if header type code selected doesn't match with leader value,
     * a default value string will'be returned, based on leader value.
     * Example: recordType (leader/05) = 'a' bibliographicLevel (leader/06) = 'm'
     *          008 --> 008 - Book
     *
     * @param storageService the storage service.
     * @param headerTypeCode the header type code selected from drop-down tag list.
     * @param code the tag number code.
     * @param leader the leader of record.
     * @param valueField the display value field (null or blank a default value will'be set).
     * @param lang the lang associated with the current request.
     * @param vertxContext the vertx context.
     * @return a fixed-field containing all selected values.
     */
    private FixedField getFixedField(final StorageService storageService,
                                     final int headerTypeCode,
                                     final String code,
                                     final String leader,
                                     String valueField,
                                     final Context vertxContext,
                                     final String lang,
                                     final Map<String, String> serviceConfiguration) {


        FixedField fixedField = new FixedField();
        fixedField.setCode(code);
        fixedField.setCategoryCode(Global.INT_CATEGORY);
        fixedField.setHeaderTypeCode(headerTypeCode);

        if (isFixedField(code)){

            valueField = ofNullable(valueField).filter(s -> !s.isEmpty()).orElse(null);

            final GeneralInformation generalInformation = new GeneralInformation();
            if (code.equals(Global.LEADER_TAG_NUMBER)){
                final String description = storageService.getHeadingTypeDescription(headerTypeCode, lang, Global.INT_CATEGORY);
                fixedField.setDescription(description);
                fixedField.setDisplayValue(ofNullable(valueField).orElse(getLeaderValue()));
                setLeaderValues(fixedField);

            }else if (code.equals(Global.MATERIAL_TAG_CODE)) {

                final Map<String, Object> mapRecordTypeMaterial = storageService.getMaterialTypeInfosByLeaderValues(leader.charAt(6), leader.charAt(7), code);
                final int headerTypeCalculated = (int) mapRecordTypeMaterial.get(Global.HEADER_TYPE_LABEL);

                generalInformation.setFormOfMaterial((String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL));
                generalInformation.setHeaderType(headerTypeCalculated);
                generalInformation.setMaterialDescription008Indicator("1");

                //header type code doesn't match with leader value
                if (headerTypeCode != headerTypeCalculated) { valueField = null; }

            } else if (code.equals(Global.OTHER_MATERIAL_TAG_CODE)) {
                generalInformation.setHeaderType(headerTypeCode);
                final Map<String, Object> mapRecordTypeMaterial = storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode, code);
                generalInformation.setFormOfMaterial((String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL));
                generalInformation.setMaterialTypeCode((String) mapRecordTypeMaterial.get(Global.MATERIAL_TYPE_CODE_LABEL));
                generalInformation.setMaterialDescription008Indicator("0");

            } else if (code.equals(Global.PHYSICAL_DESCRIPTION_TAG_CODE)){
                //use PhysicalDescription or what?
            } else if (code.equals(Global.DATETIME_TRANSACION_TAG_CODE)){
                fixedField.setDescription(storageService.getHeadingTypeDescription(
                        headerTypeCode, lang, Global.INT_CATEGORY));
                fixedField.setDisplayValue(F.getFormattedDate("yyyyMMddHHmmss."));
            }

            if (code.equals(Global.MATERIAL_TAG_CODE) || code.equals(Global.OTHER_MATERIAL_TAG_CODE)) {
                if (valueField == null){
                    if ("1".equals(generalInformation.getMaterialDescription008Indicator())) {
                        generalInformation.setEnteredOnFileDateYYMMDD(F.getFormattedDate("yyMMdd"));
                    }
                    generalInformation.setDefaultValues(serviceConfiguration);
                    valueField = generalInformation.getValueString();
                }

                fixedField.setHeaderTypeCode(generalInformation.getHeaderType());
                fixedField.setDescription(storageService.getHeadingTypeDescription(generalInformation.getHeaderType(), lang, Global.INT_CATEGORY));
                fixedField.setDisplayValue(valueField);
                setMaterialValues(fixedField, generalInformation);
            }
        }

        return fixedField;
    }

    /**
     * Inject leader values for drop-down list selected.
     *
     * @param fixedField the fixedField to populate.
     */
    private void setLeaderValues(final FixedField fixedField) {

        final String leaderValue = (fixedField.getDisplayValue().length() != 24 ? getLeaderValue() : fixedField.getDisplayValue());

        fixedField.setItemRecordStatusCode(String.valueOf(leaderValue.charAt(5)));
        fixedField.setItemRecordTypeCode(String.valueOf(leaderValue.charAt(6)));
        fixedField.setItemBibliographicLevelCode(String.valueOf(leaderValue.charAt(7)));
        fixedField.setItemControlTypeCode(String.valueOf(leaderValue.charAt(8)));
        fixedField.setCharacterCodingSchemeCode(String.valueOf(leaderValue.charAt(9)));
        fixedField.setEncodingLevel(String.valueOf(leaderValue.charAt(17)));
        fixedField.setDescriptiveCataloguingCode(String.valueOf(leaderValue.charAt(18)));
        fixedField.setLinkedRecordCode(String.valueOf(leaderValue.charAt(19)));
    }

    /**
     * Sets variable field with selected drop-down list (correlation entity)
     * and sub-fields (validation entity).
     *
     * @param categoryCode the field category code.
     * @param ind1 the first indicator of tag field.
     * @param ind2 the second indicator of tag field.
     * @param code the tag number code.
     * @param correlations the selected drop-down list.
     * @param description the field description
     * @param validation the sub-fields valid for this tag/field
     * @return a VariableField entity.
     */
    private VariableField getVariableField(final int categoryCode,
                             final String ind1,
                             final String ind2,
                             final String code,
                             final CorrelationValues correlations,
                             final String description, final Validation validation) {

        final VariableField variableField = new VariableField();
        if (!isFixedField(code))
        {
            variableField.setHeadingTypeCode(Integer.toString(correlations.getValue(1)));
            variableField.setItemTypeCode(Integer.toString(correlations.getValue(2)));
            variableField.setFunctionCode(Integer.toString(correlations.getValue(3)));
            variableField.setCategoryCode(categoryCode);
            variableField.setCode(code);
            variableField.setInd1(ind1);
            variableField.setInd2(ind2);
            variableField.setDescription(description);

            variableField.setSubfields(stream(validation.getMarcValidSubfieldStringCode().split(""))
                                .collect(Collectors.toList()));
            variableField.setDefaultSubfieldCode(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));

        }
        return variableField;
    }

    /**
     * Check if is a fixedField or not.
     *
     * @param code the tag number code.
     * @return true if is fixedfield, false otherwise.
     */
    private boolean isFixedField(final String code) {
        return Global.FIXED_FIELDS.contains(code);
    }

    //TODO  add 007 tag
    /**
     * Inject material or other material values for drop-down list selected.
     *
     * @param fixedField the fixedField to populate.
     * @param gi the general information used to create fixed field.
     */
    private void setMaterialValues(FixedField fixedField, final GeneralInformation gi)
    {
        String displayValue = fixedField.getDisplayValue();

        int startPosition = 1;
        if (gi.getMaterialDescription008Indicator().equals("1")) {
            displayValue = ((displayValue.length() != 40) ? gi.getValueString() : fixedField.getDisplayValue());
            startPosition = 18;
            fixedField.setDateEnteredOnFile(displayValue.substring(0, 6));
            fixedField.setDateTypeCode(String.valueOf(displayValue.charAt(6)));
            fixedField.setDateFirstPublication(displayValue.substring(7, 11));
            fixedField.setDateLastPublication(displayValue.substring(11, 15));
            fixedField.setPlaceOfPublication(displayValue.substring(15, 18));
            fixedField.setLanguageCode(displayValue.substring(35, 38));
            fixedField.setRecordModifiedCode(String.valueOf(displayValue.charAt(38)));
            fixedField.setRecordCataloguingSourceCode(String.valueOf(displayValue.charAt(39)));
        } else { //006
            displayValue = ((displayValue.length() != 17) ? gi.getValueString() : fixedField.getDisplayValue());
            fixedField.setMaterialTypeCode(String.valueOf(displayValue.charAt(0)));
        }

        if (gi.isBook()) {
            fixedField.setBookIllustrationCode1(String.valueOf(displayValue.charAt(startPosition)));
            fixedField.setBookIllustrationCode2(String.valueOf(displayValue.charAt(startPosition+1)));
            fixedField.setBookIllustrationCode2(String.valueOf(displayValue.charAt(startPosition+2)));
            fixedField.setBookIllustrationCode2(String.valueOf(displayValue.charAt(startPosition+3)));
            fixedField.setTargetAudienceCode(String.valueOf(displayValue.charAt(startPosition+4)));
            fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition+5)));
            fixedField.setNatureOfContent1(String.valueOf(displayValue.charAt(startPosition+6)));
            fixedField.setNatureOfContent2(String.valueOf(displayValue.charAt(startPosition+7)));
            fixedField.setNatureOfContent3(String.valueOf(displayValue.charAt(startPosition+8)));
            fixedField.setNatureOfContent4(String.valueOf(displayValue.charAt(startPosition+9)));
            fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition+10)));
            fixedField.setConferencePublicationCode(String.valueOf(displayValue.charAt(startPosition+11)));
            fixedField.setBookFestschrift(String.valueOf(displayValue.charAt(startPosition+12)));
            fixedField.setBookIndexAvailabilityCode(String.valueOf(displayValue.charAt(startPosition+13)));
            fixedField.setBookLiteraryFormTypeCode(String.valueOf(displayValue.charAt(startPosition+15)));
            fixedField.setBookBiographyCode(String.valueOf(displayValue.charAt(startPosition+16)));
            fixedField.setMaterialType(FixedField.MaterialType.BOOK);
        } else if (gi.isSerial()) {
            fixedField.setSerialFrequencyCode(String.valueOf(displayValue.charAt(startPosition)));
            fixedField.setSerialRegularityCode(String.valueOf(displayValue.charAt(startPosition+1)));
            fixedField.setSerialTypeCode(String.valueOf(displayValue.charAt(startPosition+2)));
            fixedField.setSerialFormOriginalItemCode(String.valueOf(displayValue.charAt(startPosition+4)));
            fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition+5)));
            fixedField.setNatureOfContent1(String.valueOf(displayValue.charAt(startPosition+6)));
            fixedField.setNatureOfContent2(String.valueOf(displayValue.charAt(startPosition+7)));
            fixedField.setNatureOfContent3(String.valueOf(displayValue.charAt(startPosition+8)));
            fixedField.setNatureOfContent4(String.valueOf(displayValue.charAt(startPosition+9)));
            fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition+10)));
            fixedField.setConferencePublicationCode(String.valueOf(displayValue.charAt(startPosition+11)));
            fixedField.setSerialOriginalAlphabetOfTitleCode(String.valueOf(displayValue.charAt(startPosition+15)));
            fixedField.setSerialEntryConventionCode(String.valueOf(displayValue.charAt(startPosition+16)));
            fixedField.setMaterialType(FixedField.MaterialType.CONTINUING_RESOURCE);
        } else if (gi.isComputerFile()) {
            fixedField.setTargetAudienceCode(String.valueOf(displayValue.charAt(startPosition+4)));
            fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition+5)));
            fixedField.setComputerFileTypeCode(String.valueOf(displayValue.charAt(startPosition+8)));
            fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition+10)));
            fixedField.setMaterialType(FixedField.MaterialType.COMPUTER_FILE);
        } else if (gi.isMap()) {
            fixedField.setCartographicReliefCode1(String.valueOf(displayValue.charAt(startPosition)));
            fixedField.setCartographicReliefCode2(String.valueOf(displayValue.charAt(startPosition+1)));
            fixedField.setCartographicReliefCode3(String.valueOf(displayValue.charAt(startPosition+2)));
            fixedField.setCartographicReliefCode4(String.valueOf(displayValue.charAt(startPosition+3)));
            fixedField.setCartographicProjectionCode(displayValue.substring(startPosition+4, startPosition+6));
            fixedField.setCartographicMaterial(String.valueOf(displayValue.charAt(startPosition+7)));
            fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition+10)));
            fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition+11)));
            fixedField.setCartographicIndexAvailabilityCode(String.valueOf(displayValue.charAt(startPosition+13)));
            fixedField.setCartographicFormatCode1(String.valueOf(displayValue.charAt(startPosition+15)));
            fixedField.setCartographicFormatCode2(String.valueOf(displayValue.charAt(startPosition+16)));
            fixedField.setMaterialType(FixedField.MaterialType.MAP);
        } else if (gi.isMixedMaterial()) {
            fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition+5)));
            fixedField.setMaterialType(FixedField.MaterialType.MIXED_MATERIAL);
        } else if (gi.isMusic()) {
            fixedField.setMusicFormOfCompositionCode(displayValue.substring(startPosition, startPosition+2));
            fixedField.setMusicFormatCode(String.valueOf(displayValue.charAt(startPosition+2)));
            fixedField.setMusicPartsCode(String.valueOf(displayValue.charAt(startPosition+3)));
            fixedField.setTargetAudienceCode(String.valueOf(displayValue.charAt(startPosition+4)));
            fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition+5)));
            fixedField.setMusicTextualMaterialCode1(String.valueOf(displayValue.charAt(startPosition+6)));
            fixedField.setMusicTextualMaterialCode2(String.valueOf(displayValue.charAt(startPosition+7)));
            fixedField.setMusicTextualMaterialCode3(String.valueOf(displayValue.charAt(startPosition+8)));
            fixedField.setMusicTextualMaterialCode4(String.valueOf(displayValue.charAt(startPosition+9)));
            fixedField.setMusicTextualMaterialCode5(String.valueOf(displayValue.charAt(startPosition+10)));
            fixedField.setMusicTextualMaterialCode6(String.valueOf(displayValue.charAt(startPosition+11)));
            fixedField.setMusicLiteraryTextCode1(String.valueOf(displayValue.charAt(startPosition+12)));
            fixedField.setMusicLiteraryTextCode2(String.valueOf(displayValue.charAt(startPosition+13)));
            fixedField.setMusicTranspositionArrangementCode(String.valueOf(displayValue.charAt(startPosition+15)));
            fixedField.setMaterialType(FixedField.MaterialType.MUSIC);
        } else if (gi.isVisualMaterial()) {
            fixedField.setVisualRunningTime(displayValue.substring(startPosition, startPosition+3));
            fixedField.setTargetAudienceCode(String.valueOf(displayValue.charAt(startPosition+4)));
            fixedField.setGovernmentPublicationCode(String.valueOf(displayValue.charAt(startPosition+10)));
            fixedField.setFormOfItemCode(String.valueOf(displayValue.charAt(startPosition+11)));
            fixedField.setVisualMaterialTypeCode(String.valueOf(displayValue.charAt(startPosition+15)));
            fixedField.setVisualTechniqueCode(String.valueOf(displayValue.charAt(startPosition+16)));
            fixedField.setMaterialType(FixedField.MaterialType.VISUAL_MATERIAL);
        }
    }

    @Override
    public void deleteCatalogingFieldTemplate(String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void putCatalogingFieldTemplate(String lang, FieldTemplate entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    /**
     * Sets default leader value.
     *
     * @return a leader value.
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

}
