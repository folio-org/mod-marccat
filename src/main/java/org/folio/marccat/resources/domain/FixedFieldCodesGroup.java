package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * FixedFieldCodesGroup
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"headerTypeCode", "results"})
/*


 */
public class FixedFieldCodesGroup {

  @JsonProperty("headerTypeCode")
  private Integer headerTypeCode;

  @JsonProperty("results")
  @Valid
  private Map<String, FixedFieldElement> results = new LinkedHashMap<String, FixedFieldElement>();

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

  /**
   * @return The headerTypeCode
   */
  @JsonProperty("headerTypeCode")
  public Integer getHeaderTypeCode() {
    return headerTypeCode;
  }

  /**
   * @param category The headerTypeCode
   */
  @JsonProperty("headerTypeCode")
  public void setHeaderTypeCode(Integer headerTypeCode) {
    this.headerTypeCode = headerTypeCode;
  }

  public FixedFieldCodesGroup withHeaderTypeCode(Integer headerTypeCode) {
    this.headerTypeCode = headerTypeCode;
    return this;
  }


}
