package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.F;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.domain.ConversionFieldUtils;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.FieldTemplate;
import org.folio.cataloging.resources.domain.FixedField;
import org.folio.cataloging.resources.domain.VariableField;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.GeneralInformation;
import org.folio.cataloging.shared.PhysicalInformation;
import org.folio.cataloging.shared.Validation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * FieldTemplate Restful API.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@CrossOrigin("http://localhost:3000")
@Api(value = "modcat-api", description = "Field template resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class FieldTemplateAPI extends BaseResource {

  @ApiOperation(value = "Returns all field template associated with the given data.")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested field template."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
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
        !isFixedField(code)
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
                                   final Map <String, String> serviceConfiguration) {


    FixedField fixedField = null;
    if (isFixedField(code) && checkParameters(code, headerTypeCode, leader)) {
      fixedField = new FixedField();
      fixedField.setCode(code);
      fixedField.setCategoryCode(Global.INT_CATEGORY);
      fixedField.setHeaderTypeCode(headerTypeCode);

      valueField = F.isNotNullOrEmpty(valueField) ? valueField : null;

      GeneralInformation generalInformation = null;

      if (code.equals(Global.LEADER_TAG_NUMBER)) {
        final String description = storageService.getHeadingTypeDescription(headerTypeCode, lang, Global.INT_CATEGORY);
        fixedField.setDescription(description);
        fixedField.setDisplayValue(ofNullable(valueField).orElse(getLeaderValue()));
        setLeaderValues(fixedField);

      } else if (code.equals(Global.MATERIAL_TAG_CODE)) {
        generalInformation = new GeneralInformation();
        generalInformation.setDefaultValues(serviceConfiguration);
        final Map <String, Object> mapRecordTypeMaterial = storageService.getMaterialTypeInfosByLeaderValues(leader.charAt(6), leader.charAt(7), code);
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
        final Map <String, Object> mapRecordTypeMaterial = storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode, code);
        generalInformation.setMaterialTypeCode((String) mapRecordTypeMaterial.get(Global.MATERIAL_TYPE_CODE_LABEL));
        generalInformation.setFormOfMaterial((String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL));
        generalInformation.setMaterialDescription008Indicator("0");

      } else if (code.equals(Global.PHYSICAL_DESCRIPTION_TAG_CODE)) {
        final String categoryOfMaterial = ofNullable(Global.PHYSICAL_TYPES_MAP.get(headerTypeCode)).orElse(Global.UNSPECIFIED);
        fixedField.setHeaderTypeCode((categoryOfMaterial.equals(Global.UNSPECIFIED)) ? Global.PHYSICAL_UNSPECIFIED_HEADER_TYPE : headerTypeCode);
        fixedField.setDescription(storageService.getHeadingTypeDescription(fixedField.getHeaderTypeCode(), lang, Global.INT_CATEGORY));
        fixedField.setDisplayValue(valueField);
        fixedField.setCategoryOfMaterial(categoryOfMaterial);
        setPhysicalInformationValues(fixedField, valueField);

      } else if (code.equals(Global.DATETIME_TRANSACION_TAG_CODE)) {
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
          setMaterialValues(fixedField, generalInformation);
        }
      }
    }

    return fixedField;
  }

  /**
   * Inject physical values for drop-down list selected related to 007 field.
   *
   * @param fixedField the fixedField to populate.
   * @param valueField the string value of field.
   */
  private void setPhysicalInformationValues(final FixedField fixedField, String valueField) {

    final PhysicalInformation pi = new PhysicalInformation();

    if (!F.isNotNullOrEmpty(valueField)) {
      valueField = pi.getValueString(fixedField.getCategoryOfMaterial());
    }

    fixedField.setDisplayValue(valueField);
    ConversionFieldUtils.setPhysicalInformationValuesInFixedField(fixedField);

  }

  /**
   * Checks the input parameters depending on field code.
   *
   * @param code           the current field/tag code
   * @param headerTypeCode the header type code
   * @param leader         the leader specified for template
   * @return true if parameters are valid, false otherwise
   */
  private boolean checkParameters(final String code, final int headerTypeCode, final String leader) {
    return (code.equals(Global.MATERIAL_TAG_CODE) && (leader != null && !leader.isEmpty())) ||
      (!code.equals(Global.MATERIAL_TAG_CODE) && headerTypeCode != 0);
  }

  /**
   * Inject leader values for drop-down list selected.
   *
   * @param fixedField the fixedField to populate.
   */
  private void setLeaderValues(final FixedField fixedField) {

    final String leaderValue = fixedField.getDisplayValue().length() != Global.LEADER_LENGTH
      ? getLeaderValue()
      : fixedField.getDisplayValue();

    fixedField.setDisplayValue(leaderValue);
    ConversionFieldUtils.setLeaderValuesInFixedField(fixedField);
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
    if (!isFixedField(code)) {
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

  /**
   * Check if is a fixedField or not.
   *
   * @param code the tag number code.
   * @return true if is fixedfield, false otherwise.
   */
  private boolean isFixedField(final String code) {
    return Global.FIXED_FIELDS.contains(code);
  }

  /**
   * Inject material or other material values for drop-down list selected.
   *
   * @param fixedField the fixedField to populate.
   * @param gi         the general information used to create fixed field.
   */
  private void setMaterialValues(final FixedField fixedField, final GeneralInformation gi) {
    String displayValue = null;

    if ("1".equals(gi.getMaterialDescription008Indicator())) {
      displayValue = (fixedField.getDisplayValue().length() != Global.MATERIAL_FIELD_LENGTH
        ? gi.getValueString()
        : fixedField.getDisplayValue());
    } else { //006
      displayValue = fixedField.getDisplayValue().length() != Global.OTHER_MATERIAL_FIELD_LENGTH
        ? gi.getValueString()
        : fixedField.getDisplayValue();
    }

    fixedField.setDisplayValue(displayValue);
    ConversionFieldUtils.setMaterialValuesInFixedField(fixedField, gi.getFormOfMaterial());
  }

  /**
   * Sets default leader value.
   *
   * @return a leader value.
   */
  private String getLeaderValue() {
    return new StringBuilder(Global.FIXED_LEADER_LENGTH)
      .append(Global.RECORD_STATUS_CODE)
      .append(Global.RECORD_TYPE_CODE)
      .append(Global.BIBLIOGRAPHIC_LEVEL_CODE)
      .append(Global.CONTROL_TYPE_CODE)
      .append(Global.CHARACTER_CODING_SCHEME_CODE)
      .append(Global.FIXED_LEADER_BASE_ADDRESS)
      .append(Global.ENCODING_LEVEL)
      .append(Global.DESCRIPTIVE_CATALOGUING_CODE)
      .append(Global.LINKED_RECORD_CODE)
      .append(Global.FIXED_LEADER_PORTION)
      .toString();
  }
}
