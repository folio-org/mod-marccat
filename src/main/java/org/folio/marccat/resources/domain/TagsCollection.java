package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"tags"})
public class TagsCollection {

  /**
   * (Required)
   */
  @JsonProperty("tags")
  @Valid
  @NotNull
  private Map<String, String> tags = new HashMap<>();

  @JsonProperty("tags")
  public Map<String, String> getTags() {
    return tags;
  }

  @JsonProperty("tags")
  public void setTags(Map<String, String> tags) {
    this.tags = tags;
  }
}
