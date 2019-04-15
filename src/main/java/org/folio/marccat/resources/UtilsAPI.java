package org.folio.marccat.resources;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.Maps;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.resources.domain.HeadingDecoratorCollection;
import org.folio.marccat.resources.domain.TagsCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.github.javaparser.printer.concretesyntaxmodel.CsmElement.indent;
import static org.folio.marccat.config.constants.Global.TAGS;
import static org.json.JSONObject.wrap;

/**
 * Utility RESTful APIs.
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class UtilsAPI extends BaseResource{


  @ResponseStatus
  @GetMapping("/marcTags")
  public TagsCollection getAllMARCTag(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    final TagsCollection container = new TagsCollection();
    container.setTags(TAGS);
    return container;
  }


}
