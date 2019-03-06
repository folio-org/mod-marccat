package org.folio.marccat.resources.shared;

import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.log.Global;
import org.folio.marccat.resources.domain.*;
import org.folio.marccat.shared.MapHeading;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class MappingUtils {

  public static Function<MapHeading, HeadingDecorator> toHeading = source -> {
    final HeadingDecorator heading = new HeadingDecorator();
    heading.setHeadingNumber(source.getHeadingNumber());
    heading.setStringText(source.getStringText());
    heading.setCountAuthorities(source.getCountAuthorities());
    heading.setCountDocuments(source.getCountDocuments());
    heading.setCountTitleNameDocuments(source.getCountTitleNameDocuments());
    heading.setAccessPointlanguage(source.getAccessPointlanguage());
    heading.setCrossReferences(source.getCrossReferences());
    return heading;
  };

  /**
   * Adapter that converts existing stringValue object in nature of content code Okapi resource.
   */
  public static Function<Avp<String>, Pair> toPairItem = source -> {
    final Pair pairItem = new Pair();
    pairItem.setCode(source.getValue());
    pairItem.setDescription(source.getLabel());
    return pairItem;
  };

  public static Function<Avp<String>, HeadingType> toHeadingType = source -> {
    final HeadingType headingType = new HeadingType();
    headingType.setCode(Integer.parseInt(source.getValue()));
    headingType.setDescription(source.getLabel());
    return headingType;
  };
  public static Function<Avp<Integer>, RecordTemplate> toRecordTemplate = avp -> {
    final RecordTemplate template = new RecordTemplate();
    template.setId(avp.getValue());
    template.setName(avp.getLabel());
    return template;
  };

  public static HeadingTypeCollection mapToHeading(List<Avp<String>> list, final String code) {
    HeadingTypeCollection headingTypeCollection = new HeadingTypeCollection();
    headingTypeCollection.setHeadingTypes(list.stream()
      .filter(element -> element.getLabel().startsWith(code))
      .map(toHeadingType).collect(toList()));
    return headingTypeCollection;
  }

  @SuppressWarnings("unchecked")
  public static ResultLoader setMapToResult(final Map<String, Object> source) {
    final ResultLoader resultLoader = new ResultLoader();
    resultLoader.setFilename((String) source.get(Global.LOADING_FILE_FILENAME));
    resultLoader.setAdded((int) source.get(Global.LOADING_FILE_ADDED));
    resultLoader.setRejected((int) source.get(Global.LOADING_FILE_REJECTED));
    resultLoader.setErrorCount((int) source.get(Global.LOADING_FILE_ERRORS));
    resultLoader.setIds((List<Integer>) source.get(Global.LOADING_FILE_IDS));
    return resultLoader;
  }
}
