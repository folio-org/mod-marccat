package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.Field;
import org.folio.marccat.resources.domain.FieldCollection;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.resources.domain.VariableField;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.shared.GeneralInformation;
import org.folio.marccat.shared.Validation;
import org.folio.marccat.util.F;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.MarccatHelper.doGet;

/**
 * FieldsAPI RestFul service to manage fields (tag).
 *
 * @author nbianchini
 * @author agazzarini
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class FieldAPI extends BaseResource {

  /**
   * Gets the mandatory fields to create a new template / record.
   */
  @GetMapping("/bibliographic/fields/mandatory")
  public FieldCollection getMandatoryFields(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
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
    }, tenant, configurator, "bibliographic", "material");
  }

  /**
   * Creates a cataloging source field (tag 040) using default values.
   *
   * @param configuration  the configuration parameters
   * @param storageService the storage service.
   * @param lang           the lang code.
   * @return a new 040 {@link Field} entity populated with default values.
   */
  private Field createCatalogingSourceField(
    final Map<String, String> configuration,
    final StorageService storageService,
    final String lang) {
    final CorrelationValues correlationValues =
      new CorrelationValues(
        Global.CATALOGING_SOURCE_HEADER_TYPE,
        Global.CORRELATION_UNDEFINED,
        Global.CORRELATION_UNDEFINED);

    final String description = storageService.getHeadingTypeDescription(Global.CATALOGING_SOURCE_HEADER_TYPE, lang, Global.INT_CATEGORY);
    final Validation validation =
      storageService.getSubfieldsByCorrelations(
        Global.INT_CATEGORY,
        correlationValues.getValue(1),
        correlationValues.getValue(2),
        correlationValues.getValue(3));

    final VariableField catalogingSourceField = new VariableField();

    catalogingSourceField.setCode(Global.CATALOGING_SOURCE_TAG_CODE);
    catalogingSourceField.setHeadingTypeCode(Integer.toString(Global.CATALOGING_SOURCE_HEADER_TYPE));
    catalogingSourceField.setSubfields(stream(validation.getMarcValidSubfieldStringCode().split("")).collect(toList()));
    catalogingSourceField.setDefaultSubfieldCode(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));
    catalogingSourceField.setValue(configuration.get("bibliographicItem.cataloguingSourceStringText"));
    catalogingSourceField.setDescription(description);
    catalogingSourceField.setCategoryCode(Global.INT_CATEGORY);

    final Field field = new Field();
    field.setCode(Global.CATALOGING_SOURCE_TAG_CODE);
    field.setMandatory(true);
    field.setVariableField(catalogingSourceField);

    return field;
  }

  /**
   * Creates default control field value.
   *
   * @param storageService the storage service.
   * @param lang           the lang associated with the current request.
   * @return a new 001 {@link Field} entity populated with default values.
   */
  private Field createControlNumberField(final StorageService storageService, final String lang) {
    final String description = storageService.getHeadingTypeDescription(Global.CONTROL_NUMBER_HEADER_TYPE, lang, Global.INT_CATEGORY);
    final FixedField controlNumberFixedField = new FixedField();
    controlNumberFixedField.setCode(Global.CONTROL_NUMBER_TAG_CODE);
    controlNumberFixedField.setDisplayValue(Global.DECIMAL_FORMAT_AN.format(0));
    controlNumberFixedField.setHeaderTypeCode(Global.CONTROL_NUMBER_HEADER_TYPE);
    controlNumberFixedField.setDescription(description);
    controlNumberFixedField.setCategoryCode(Global.INT_CATEGORY);

    final Field field = new Field();
    field.setCode(Global.CONTROL_NUMBER_TAG_CODE);
    field.setMandatory(true);
    field.setFixedField(controlNumberFixedField);

    return field;

  }

  /**
   * Creates a leader with default values.
   *
   * @param storageService the storage service.
   * @param lang           the lang associated with the current request.
   * @return a new leader {@link Field} entity populated with default values.
   */
  private Field createRequiredLeaderField(final StorageService storageService, final String lang) {
    final String description = storageService.getHeadingTypeDescription(Global.LEADER_HEADER_TYPE, lang, Global.INT_CATEGORY);
    final FixedField leader = new FixedField();
    leader.setHeaderTypeCode(Global.LEADER_HEADER_TYPE);
    leader.setCode(Global.LEADER_TAG_NUMBER);
    leader.setDisplayValue(getLeaderValue());
    leader.setDescription(description);
    leader.setCategoryCode(Global.INT_CATEGORY);

    final Field field = new Field();
    field.setCode(Global.LEADER_TAG_NUMBER);
    field.setMandatory(true);
    field.setFixedField(leader);
    return field;
  }

  /**
   * Creates default 008 field.
   *
   * @param configuration  the configuration parameters.
   * @param storageService the storage service.
   * @param lang           the lang associated with the current request.
   * @return the 008 default tag definition.
   */
  private Field createRequiredMaterialDescriptionField(final Map<String, String> configuration, final StorageService storageService, final String lang) {

    final String description = storageService.getHeadingTypeDescription(Global.MATERIAL_DESCRIPTION_HEADER_TYPE, lang, Global.INT_CATEGORY);
    final GeneralInformation generalInformation = new GeneralInformation();
    generalInformation.setMaterialDescription008Indicator("1");
    generalInformation.setFormOfMaterial(Global.BOOKFORM_OF_MATERIAL);
    generalInformation.setDefaultValues(configuration);
    generalInformation.setEnteredOnFileDateYYMMDD(F.getFormattedToday("yyMMdd"));

    final FixedField materialDescription = new FixedField();
    materialDescription.setHeaderTypeCode(Global.MATERIAL_DESCRIPTION_HEADER_TYPE);
    materialDescription.setCode(Global.MATERIAL_TAG_CODE);
    materialDescription.setDisplayValue(generalInformation.getValueString());
    materialDescription.setDescription(description);
    materialDescription.setCategoryCode(Global.INT_CATEGORY);

    final Field field = new Field();
    field.setCode(Global.MATERIAL_TAG_CODE);
    field.setMandatory(true);
    field.setFixedField(materialDescription);

    return field;
  }

  /**
   * Sets the default leader value.
   *
   * @return the default leader value.
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
