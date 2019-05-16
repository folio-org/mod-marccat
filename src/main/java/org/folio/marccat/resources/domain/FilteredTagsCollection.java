package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"tag", "tags"})
public class FilteredTagsCollection {

  @JsonProperty("tag")
  @Valid
  @NotNull
  private FilteredTag tag = new FilteredTag();

  @JsonProperty("tag")
  public FilteredTag getTag() {
    return tag;
  }

  @JsonProperty("tag")
  public void setFilteredTag(FilteredTag tag) {
    this.tag = tag;
  }
  /**
   * (Required)
   */
  @JsonProperty("tags")
  @Valid
  @NotNull
  private List<String> tags = new ArrayList <>();

  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  @JsonProperty("tags")
  public void setTags(List<String> tags) {
  this.tags = tags;
  }



}
