package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.Global;
import org.folio.marccat.resources.domain.HeadingType;
import org.folio.marccat.resources.domain.HeadingTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.MarccatHelper.doGet;

/**
 * Header type code RESTful APIs.
 *
 * @author cchiama
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class HeaderTypeAPI extends BaseResource {

  private Function<Avp<String>, HeadingType> toHeadingType = source -> {
    final HeadingType headingType = new HeadingType();
    headingType.setCode(Integer.parseInt(source.getValue()));
    headingType.setDescription(source.getLabel());
    return headingType;
  };

  private HeadingTypeCollection mapToHeading(List<Avp<String>> list, final String code) {

    HeadingTypeCollection headingTypeCollection = new HeadingTypeCollection();
    headingTypeCollection.setHeadingTypes(list.stream()
      .filter(element -> element.getLabel().startsWith(code))
      .map(toHeadingType).collect(toList()));
    return headingTypeCollection;
  }

  @GetMapping("/header-types")
  public HeadingTypeCollection getHeadingTypes(
    @RequestParam final String code,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {

      final int category = Global.HEADER_CATEGORY;
      if (ofNullable(storageService.getFirstCorrelation(lang, category)).isPresent())
        return mapToHeading(storageService.getFirstCorrelation(lang, category), code);

      return null;
    }, tenant, configurator);
  }
}
