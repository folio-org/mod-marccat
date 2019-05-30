package org.folio.marccat.resources;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealtCheckAPI extends BaseResource<JSONObject> {

  /**
   * Gets the count of the record.
   */
  @GetMapping("/checkOkapi")
  public JSONObject checkOkapi() {
   return new JSONObject(new String[]{"say","hello"});
  }


}
