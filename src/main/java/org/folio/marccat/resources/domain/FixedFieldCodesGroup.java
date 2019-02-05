package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * FixedFieldCodesGroup
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"results"})
/*


 */
public class FixedFieldCodesGroup {

  @JsonProperty("results")
  @Valid
  private Map<String, FixedFieldElement> results = new HashMap<String, FixedFieldElement>();

  /**
   * @return Results
   */
  @JsonProperty("results")
  public Map<String, FixedFieldElement> getResults() {
    return results;
  }

  /**
   * @param results
   */
  @JsonProperty("results")
  public void setResults(Map<String, FixedFieldElement> results) {
    this.results = results;
  }

  /**
   * @param results
   */
  @JsonProperty("results")
  public void addResults(FixedFieldElement fixedFieldElement) {
    this.results.put(fixedFieldElement.getName(), fixedFieldElement);
  }

}
