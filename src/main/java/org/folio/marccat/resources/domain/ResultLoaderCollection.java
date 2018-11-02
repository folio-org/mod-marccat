package org.folio.cataloging.resources.domain;

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
@JsonPropertyOrder({"resultLoaders"})
public class ResultLoaderCollection {

  @JsonProperty("resultLoaders")
  @Valid
  @NotNull
  private List <ResultLoader> resultLoaders = new ArrayList <ResultLoader>();

  /**
   * (Required)
   *
   * @return The resultLoaders
   */
  @JsonProperty("resultLoaders")
  public List <ResultLoader> getResultLoaders() {
    return resultLoaders;
  }

  /**
   * (Required)
   *
   * @param resultLoaders The resultLoaders
   */
  @JsonProperty("resultLoaders")
  public void setResultLoaders(List <ResultLoader> resultLoaders) {
    this.resultLoaders = resultLoaders;
  }


}
