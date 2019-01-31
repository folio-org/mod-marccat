package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.resources.domain.FieldTemplate;
import org.folio.marccat.resources.domain.VariableField;
import org.folio.marccat.shared.*;
import org.springframework.web.bind.annotation.*;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.MarccatHelper.doGet;

/**
 * FieldTemplate Restful API.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class FieldTemplateAPI extends BaseResource implements CatalogingInformation{


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
          : ofNullable(CatalogingInformation.getFixedField(storageService, headerType, code, leader, valueField, lang, configuration))
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
