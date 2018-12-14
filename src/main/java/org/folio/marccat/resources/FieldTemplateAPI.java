package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.FieldTemplate;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.resources.domain.VariableField;
import org.folio.marccat.shared.*;
import org.folio.marccat.util.F;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * FieldTemplate Restful API.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class FieldTemplateAPI extends BaseResource {


  @GetMapping("/field-template")
  public FieldTemplate getFieldTemplate(
    @RequestParam final int categoryCode,
    @RequestParam final String ind1,
    @RequestParam final String ind2,
    @RequestParam final String code,
    @RequestParam final Integer headerType,
    @RequestParam final String leader,
    @RequestParam final String valueField,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) ->
        !CatalogingInformation.isFixedField(code)
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
            return fieldTemplate;
          }).orElseGet(() -> {
            logger.error(MessageCatalog._00016_FIELD_PARAMETER_INVALID, categoryCode, code);
            return new FieldTemplate();
          })
          : ofNullable(getFixedField(storageService, headerType, code, leader, valueField, lang, configuration))
          .map(fixedField -> {
            final FieldTemplate fieldT = new FieldTemplate();
            fieldT.setFixedField(fixedField);
            return fieldT;
          }).orElseGet(() -> {
            logger.error(MessageCatalog._00016_FIELD_PARAMETER_INVALID, categoryCode, code);
            return new FieldTemplate();
          })
      , tenant, configurator, "bibliographic", "material");

  }

  /**
   * Gets the fixed-field associated to header type code.
   * <p>
   * In case of 008 field, leader will be used to get related values:
   * if header type code selected doesn't match with leader value,
   * a default value string will'be returned, based on leader value.
   * Example: recordType (leader/05) = 'a' bibliographicLevel (leader/06) = 'm'
   * 008 --> 008 - Book
   *
   * @param storageService the storage service.
   * @param headerTypeCode the header type code selected from drop-down tag list.
   * @param code           the tag number code.
   * @param leader         the leader of record.
   * @param valueField     the display value field (null or blank a default value will'be set).
   * @param lang           the lang associated with the current request.
   * @return a fixed-field containing all selected values.
   */
  private FixedField getFixedField(final StorageService storageService,
                                   final int headerTypeCode,
                                   final String code,
                                   final String leader,
                                   String valueField,
                                   final String lang,
                                   final Map<String, String> serviceConfiguration) {


    FixedField fixedField = null;
    if (CatalogingInformation.isFixedField(code) && CatalogingInformation.checkParameters(code, headerTypeCode, leader)) {
      fixedField = new FixedField();
      fixedField.setCode(code);
      fixedField.setCategoryCode(Global.INT_CATEGORY);
      fixedField.setHeaderTypeCode(headerTypeCode);

      valueField = F.isNotNullOrEmpty(valueField) ? valueField : null;

      GeneralInformation generalInformation = null;

      if (code.equals(Global.LEADER_TAG_NUMBER)) {
        final String description = storageService.getHeadingTypeDescription(headerTypeCode, lang, Global.INT_CATEGORY);
        fixedField.setDescription(description);
        fixedField.setDisplayValue(ofNullable(valueField).orElse(CatalogingInformation.getLeaderValue()));
        CatalogingInformation.setLeaderValues(fixedField);

      } else if (code.equals(Global.MATERIAL_TAG_CODE)) {
        generalInformation = new GeneralInformation();
        generalInformation.setDefaultValues(serviceConfiguration);
        final Map<String, Object> mapRecordTypeMaterial = storageService.getMaterialTypeInfosByLeaderValues(leader.charAt(6), leader.charAt(7), code);
        final int headerTypeCalculated = (int) mapRecordTypeMaterial.get(Global.HEADER_TYPE_LABEL);

        generalInformation.setFormOfMaterial((String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL));
        generalInformation.setHeaderType(headerTypeCalculated);
        generalInformation.setMaterialDescription008Indicator("1");

        //header type code doesn't match with leader value
        if (headerTypeCode != headerTypeCalculated) {
          valueField = null;
        }

      } else if (code.equals(Global.OTHER_MATERIAL_TAG_CODE)) {
        generalInformation = new GeneralInformation();
        generalInformation.setDefaultValues(serviceConfiguration);

        generalInformation.setHeaderType(headerTypeCode);
        final Map<String, Object> mapRecordTypeMaterial = storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode, code);
        generalInformation.setMaterialTypeCode((String) mapRecordTypeMaterial.get(Global.MATERIAL_TYPE_CODE_LABEL));
        generalInformation.setFormOfMaterial((String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL));
        generalInformation.setMaterialDescription008Indicator("0");

      } else if (code.equals(Global.PHYSICAL_DESCRIPTION_TAG_CODE)) {
        final String categoryOfMaterial = ofNullable(Global.PHYSICAL_TYPES_MAP.get(headerTypeCode)).orElse(Global.UNSPECIFIED);
        fixedField.setHeaderTypeCode((categoryOfMaterial.equals(Global.UNSPECIFIED)) ? Global.PHYSICAL_UNSPECIFIED_HEADER_TYPE : headerTypeCode);
        fixedField.setDescription(storageService.getHeadingTypeDescription(fixedField.getHeaderTypeCode(), lang, Global.INT_CATEGORY));
        fixedField.setDisplayValue(valueField);
        fixedField.setCategoryOfMaterial(categoryOfMaterial);
        CatalogingInformation.setPhysicalInformationValues(fixedField, valueField);

      } else if (code.equals(Global.DATETIME_TRANSACTION_TAG_CODE)) {
        fixedField.setDescription(storageService.getHeadingTypeDescription(
          headerTypeCode, lang, Global.INT_CATEGORY));
        fixedField.setDisplayValue(F.getFormattedToday("yyyyMMddHHmmss."));
      }

      if (code.equals(Global.MATERIAL_TAG_CODE) || code.equals(Global.OTHER_MATERIAL_TAG_CODE)) {
        if (generalInformation != null) {
          if (valueField == null) {
            if ("1".equals(generalInformation.getMaterialDescription008Indicator())) {
              generalInformation.setEnteredOnFileDateYYMMDD(F.getFormattedToday("yyMMdd"));
            }
            valueField = generalInformation.getValueString();
          }

          fixedField.setHeaderTypeCode(generalInformation.getHeaderType());
          fixedField.setDescription(storageService.getHeadingTypeDescription(generalInformation.getHeaderType(), lang, Global.INT_CATEGORY));
          fixedField.setDisplayValue(valueField);
          CatalogingInformation.setMaterialValues(fixedField, generalInformation);
        }
      }
    }

    return fixedField;
  }



  /**
   * Sets variable field with selected drop-down list (correlation entity)
   * and sub-fields (validation entity).
   *
   * @param categoryCode the field category code.
   * @param ind1         the first indicator of tag field.
   * @param ind2         the second indicator of tag field.
   * @param code         the tag number code.
   * @param correlations the selected drop-down list.
   * @param description  the field description
   * @param validation   the sub-fields valid for this tag/field
   * @return a VariableField entity.
   */
  private VariableField getVariableField(final int categoryCode,
                                         final String ind1,
                                         final String ind2,
                                         final String code,
                                         final CorrelationValues correlations,
                                         final String description, final Validation validation) {

    final VariableField variableField = new VariableField();
    if (!CatalogingInformation.isFixedField(code)) {
      variableField.setHeadingTypeCode(Integer.toString(correlations.getValue(1)));
      variableField.setItemTypeCode(Integer.toString(correlations.getValue(2)));
      variableField.setFunctionCode(Integer.toString(correlations.getValue(3)));
      variableField.setCategoryCode(categoryCode);
      variableField.setCode(code);
      variableField.setInd1(ind1);
      variableField.setInd2(ind2);
      variableField.setDescription(description);

      variableField.setSubfields(
        stream(validation.getMarcValidSubfieldStringCode().split(""))
          .collect(toList()));
      variableField.setDefaultSubfieldCode(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));

    }
    return variableField;
  }

}
