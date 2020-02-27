package org.folio.marccat.shared;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.resources.shared.ConversionFieldUtils;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.util.F;

import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.folio.marccat.resources.shared.FixedFieldUtils.isFixedField;
import static org.folio.marccat.resources.shared.RecordUtils.getLeaderValue;

public interface CatalogingInformation {

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
  static FixedField getFixedField(final StorageService storageService,
                                  final int headerTypeCode,
                                  final String code,
                                  final String leader,
                                  String valueField,
                                  final String lang,
                                  final Map<String, String> serviceConfiguration) {


    FixedField fixedField = null;
    if (isFixedField(code) && checkParameters(code, headerTypeCode, leader)) {
      fixedField = new FixedField();
      fixedField.setCode(code);
      fixedField.setCategoryCode(Global.INT_CATEGORY);
      fixedField.setHeaderTypeCode(headerTypeCode);

      valueField = F.isNotNullOrEmpty(valueField) ? valueField : null;

      GeneralInformation generalInformation = null;

      switch (code) {
        case Global.LEADER_TAG_NUMBER:
          final String description = storageService.getHeadingTypeDescription(headerTypeCode, lang, Global.INT_CATEGORY);
          fixedField.setDescription(description);
          fixedField.setDisplayValue(ofNullable(valueField).orElse(getLeaderValue()));
          setLeaderValues(fixedField);
          break;
        case Global.MATERIAL_TAG_CODE:
          generalInformation = new GeneralInformation();
          generalInformation.setDefaultValues(serviceConfiguration);
          final Map<String, Object> mapRecordTypeMaterialLeader = storageService.getMaterialTypeInfosByLeaderValues(leader.charAt(6), leader.charAt(7), code);
          final int headerTypeCalculated = (int) mapRecordTypeMaterialLeader.get(Global.HEADER_TYPE_LABEL);

          generalInformation.setFormOfMaterial((String) mapRecordTypeMaterialLeader.get(Global.FORM_OF_MATERIAL_LABEL));
          generalInformation.setHeaderType(headerTypeCalculated);
          generalInformation.setMaterialDescription008Indicator("1");

          //header type code doesn't match with leader value
          if (headerTypeCode != headerTypeCalculated) {
            valueField = null;
          }
          break;

        case Global.OTHER_MATERIAL_TAG_CODE:
          generalInformation = new GeneralInformation();
          generalInformation.setDefaultValues(serviceConfiguration);

          generalInformation.setHeaderType(headerTypeCode);
          final Map<String, Object> mapRecordTypeMaterialHeader = storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode, code);
          generalInformation.setMaterialTypeCode((String) mapRecordTypeMaterialHeader.get(Global.MATERIAL_TYPE_CODE_LABEL));
          generalInformation.setFormOfMaterial((String) mapRecordTypeMaterialHeader.get(Global.FORM_OF_MATERIAL_LABEL));
          generalInformation.setMaterialDescription008Indicator("0");
          break;
        case Global.PHYSICAL_DESCRIPTION_TAG_CODE:
          final String categoryOfMaterial = ofNullable(Global.PHYSICAL_TYPES_MAP.get(headerTypeCode)).orElse(Global.UNSPECIFIED);
          fixedField.setHeaderTypeCode((categoryOfMaterial.equals(Global.UNSPECIFIED)) ? Global.PHYSICAL_UNSPECIFIED_HEADER_TYPE : headerTypeCode);
          fixedField.setDescription(storageService.getHeadingTypeDescription(fixedField.getHeaderTypeCode(), lang, Global.INT_CATEGORY));
          fixedField.setDisplayValue(valueField);
          fixedField.setCategoryOfMaterial(categoryOfMaterial);
          setPhysicalInformationValues(fixedField, valueField);
          break;

        case Global.DATETIME_TRANSACTION_TAG_CODE:
          fixedField.setDescription(storageService.getHeadingTypeDescription(
            headerTypeCode, lang, Global.INT_CATEGORY));
          fixedField.setDisplayValue(F.getFormattedToday("yyyyMMddHHmmss."));
          break;

        default:
      }

      if ((code.equals(Global.MATERIAL_TAG_CODE) || code.equals(Global.OTHER_MATERIAL_TAG_CODE)) && generalInformation != null) {
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

    return fixedField;
  }


  /**
   * Checks the input parameters depending on field code.
   *
   * @param code           the current field/tag code
   * @param headerTypeCode the header type code
   * @param leader         the leader specified for template
   * @return true if parameters are valid, false otherwise
   */
  static boolean checkParameters(final String code, final int headerTypeCode, final String leader) {
    return (code.equals(Global.MATERIAL_TAG_CODE) && (leader != null && !leader.isEmpty())) ||
      (!code.equals(Global.MATERIAL_TAG_CODE) && headerTypeCode != 0);
  }

  /**
   * Inject leader values for drop-down list selected.
   *
   * @param fixedField the fixedField to populate.
   */
  static void setLeaderValues(final FixedField fixedField) {

    final String leaderValue = fixedField.getDisplayValue().length() != Global.LEADER_LENGTH
      ? getLeaderValue()
      : fixedField.getDisplayValue();

    fixedField.setDisplayValue(leaderValue);
    ConversionFieldUtils.setLeaderValuesInFixedField(fixedField);
  }

  /**
   * Inject physical values for drop-down list selected related to 007 field.
   *
   * @param fixedField the fixedField to populate.
   * @param valueField the string value of field.
   */
  static void setPhysicalInformationValues(final FixedField fixedField, String valueField) {

    final PhysicalInformation pi = new PhysicalInformation();

    if (!F.isNotNullOrEmpty(valueField)) {
      valueField = pi.getValueString(fixedField.getCategoryOfMaterial());
    }

    fixedField.setDisplayValue(valueField);
    ConversionFieldUtils.setPhysicalInformationValuesInFixedField(fixedField);

  }


  /**
   * Inject material or other material values for drop-down list selected.
   *
   * @param fixedField the fixedField to populate.
   * @param gi         the general information used to create fixed field.
   */
  static void setMaterialValues(final FixedField fixedField, final GeneralInformation gi) {
    String displayValue;

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


}
