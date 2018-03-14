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
import org.folio.rest.jaxrs.model.FieldTemplate;
import org.folio.rest.jaxrs.model.FixedField;
import org.folio.rest.jaxrs.model.VariableField;
import org.folio.rest.jaxrs.resource.CatalogingFieldTemplateResource;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;


/**
 * TODO: One line comment (optional) long description + new line empty + author + since 1.0
 *
 * FieldTemplate Restful API
 *
 * @author natasciab
 * @since 1.0
 *
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

        doGet((storageService, future) -> {
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
                                                        Global.firstCorrelationHeadingClassMap.get(categoryCode)),
                                                validation));
                                fieldTemplate.setCode(code);
                                return fieldTemplate;
                            }).orElse(null)
                    :   ofNullable(getFixedField(storageService, headerType, code, leader, valueField, vertxContext, lang))
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
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    /**
     * Gets the fixed-field associated to header type code.
     *
     * In case of 008 field, leader will be used to get related values:
     * if header type code selected doesn't match with leader value,
     * a default value string will'be returned, based on leader value.
     *
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
                                     final String lang) {


        FixedField fixedField = new FixedField();
        fixedField.setCode(code);
        fixedField.setCategoryCode(Global.INT_CATEGORY);
        fixedField.setHeaderTypeCode(headerTypeCode);

        if (isFixedField(code)){

            valueField = ofNullable(valueField).filter(s -> !s.isEmpty()).orElse(null);

            final GeneralInformation generalInformation = new GeneralInformation();
            if (code.equals(Global.LEADER_TAG_NUMBER)){

                fixedField.setDescription(storageService.getHeadingTypeDescription(headerTypeCode, lang, T_BIB_HDR.class));
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
                fixedField.setDescription(storageService.getHeadingTypeDescription(headerTypeCode, lang, T_BIB_HDR.class));
                fixedField.setDisplayValue(F.getFormattedDate("yyyyMMddHHmmss."));
            }

            if (code.equals(Global.MATERIAL_TAG_CODE) || code.equals(Global.OTHER_MATERIAL_TAG_CODE)) {

                if (valueField == null){
                    if (generalInformation.getMaterialDescription008Indicator().equals("1")) {
                        generalInformation.setEnteredOnFileDateYYMMDD(F.getFormattedDate("yyMMdd"));
                    }
                    final Map<String, String> configuration = getConfigurationValues(vertxContext, generalInformation);
                    generalInformation.setDefaultValues(configuration);
                    valueField = generalInformation.getValueString();
                }

                fixedField.setHeaderTypeCode(generalInformation.getHeaderType());
                fixedField.setDescription(storageService.getHeadingTypeDescription(generalInformation.getHeaderType(), lang, T_BIB_HDR.class));
                fixedField.setDisplayValue(valueField);
                setMaterialValues(fixedField, generalInformation);
            }
        }

        return fixedField;
    }

    /**
     * Inject leader values for drop-down selected.
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
     * and sub-fields (validation entity)
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
     * @param code the tag number code.
     * @return true if is fixedfield, false otherwise.
     */
    private boolean isFixedField(final String code) {
        return Global.FIXED_FIELDS.contains(code);
    }

    //TODO  change all!
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
            fixedField.setMaterialTypeCode(String.valueOf(displayValue.charAt(0)));
        }

        /*if (gi.isBook()) {
            gi.setBookIllustrationCode(getBook_illustrationOne().toString()
                    + getBook_illustrationTwo().toString()
                    + getBook_illustrationThree().toString()
                    + getBook_illustrationFour().toString());
            gi.setTargetAudienceCode(getTargetAudienceCode());
            gi.setFormOfItemCode(getFormOfItemCode());
            gi.setNatureOfContentsCode(getNatureOfContentOne().toString()
                    + getNatureOfContentTwo().toString()
                    + getNatureOfContentThree().toString()
                    + getNatureOfContentFour().toString());
            gi.setGovernmentPublicationCode(getGovernmentPublicationCode());
            gi.setConferencePublicationCode(getConferencePublicationCode());
            gi.setBookFestschrift(getBookFestschrift());
            gi.setBookIndexAvailabilityCode(getBookIndexAvailabilityCode());
            gi.setBookLiteraryFormTypeCode(getBookLiteraryFormTypeCode());
            gi.setBookBiographyCode(getBookBiographyCode());

        } else if (gi.isSerial()) {
            gi.setSerialFrequencyCode(getSerialFrequencyCode());
            gi.setSerialRegularityCode(getSerialRegularityCode());
            gi.setSerialTypeCode(getSerialTypeCode());
            gi.setSerialFormOriginalItemCode(getSerialFormOriginalItemCode());
            gi.setFormOfItemCode(getFormOfItemCode());

            gi.setNatureOfContentsCode(getNatureOfContentOne().toString()
                    + getNatureOfContentTwo().toString()
                    + getNatureOfContentThree().toString()
                    + getNatureOfContentFour().toString());

            gi.setGovernmentPublicationCode(getGovernmentPublicationCode());
            gi.setConferencePublicationCode(getConferencePublicationCode());
            gi.setSerialOriginalAlphabetOfTitleCode(getSerialOriginalAlphabetOfTitleCode());
            gi.setSerialSuccessiveLatestCode(getSerialSuccessiveLatestCode());

        } else if (gi.isComputerFile()) {
            gi.setComputerTargetAudienceCode(getComputerTargetAudienceCode());

            gi.setComputerFileFormCode(getComputerFileFormCode());
            gi.setComputerFileTypeCode(getComputerFileTypeCode());
            gi.setGovernmentPublicationCode(getGovernmentPublicationCode());

        } else if (gi.isMap()) {
            gi.setCartographicReliefCode(getCartographicReliefCode1().toString()
                    + getCartographicReliefCode2().toString()
                    + getCartographicReliefCode3().toString()
                    + getCartographicReliefCode4().toString());
            gi.setCartographicProjectionCode(getCartographicProjectionCode());
            gi.setCartographicMaterial(getCartographicMaterial());
            gi.setGovernmentPublicationCode(getGovernmentPublicationCode());
            gi.setFormOfItemCode(getFormOfItemCode());
            gi.setCartographicIndexAvailabilityCode(getCartographicIndexAvailabilityCode());
            gi.setCartographicFormatCode(getCartographicFormatCode1().toString() + getCartographicFormatCode2().toString());

        } else if (gi.isMixedMaterial()) {
            gi.setFormOfItemCode(getFormOfItemCode());

        } else if (gi.isMusic()) {
            gi.setMusicFormOfCompositionCode(getMusicFormOfCompositionCode());
            gi.setMusicFormatCode(getMusicFormatCode());
            gi.setTargetAudienceCode(getTargetAudienceCode());
            gi.setFormOfItemCode(getFormOfItemCode());
            gi.setMusicTextualMaterialCode(getMusicTextualMaterialCode1().toString()
                    + getMusicTextualMaterialCode2().toString()
                    + getMusicTextualMaterialCode3().toString()
                    + getMusicTextualMaterialCode4().toString()
                    + getMusicTextualMaterialCode5().toString()
                    + getMusicTextualMaterialCode6().toString());
            gi.setMusicLiteraryTextCode(getMusicLiteraryTextCode1().toString() + getMusicLiteraryTextCode2().toString());

            gi.setMusicPartsCode(getMusicPartsCode());
            gi.setMusicTranspositionArrangementCode(getMusicTranspositionArrangementCode());

        } else if (gi.isVisualMaterial()) {
            gi.setVisualRunningTime(getVisualRunningTime());
            gi.setVisualTargetAudienceCode(getVisualTargetAudienceCode());
            gi.setGovernmentPublicationCode(getGovernmentPublicationCode());
            gi.setFormOfItemCode(getFormOfItemCode());
            gi.setVisualMaterialTypeCode(getVisualMaterialTypeCode());
            gi.setVisualTechniqueCode(getVisualTechniqueCode());
        }*/
    }

    @Override
    public void deleteCatalogingFieldTemplate(String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void putCatalogingFieldTemplate(String lang, FieldTemplate entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }


    // TODO: Asynch management and configure all types of material
    /**
     * Reads parameters from configuration module
     * @param vertxContext the vertx context.
     * @return configuration map values.
     */
    private Map<String, String> getConfigurationValues(final Context vertxContext, final GeneralInformation generalInformation){

        final Map<String, String> configuration = new HashMap<>();

        if (generalInformation.isBook()) {
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
        } //else altri casi


        return configuration;
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

}
