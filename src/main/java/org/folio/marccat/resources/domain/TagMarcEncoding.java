package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"tagCode", "ind1", "ind2", "code1", "code2", "code3", "marcCategory"})
public class TagMarcEncoding {

  @JsonProperty("tagCode")
  private String tagCode;

  @JsonProperty("ind1")
  private String ind1;

  @JsonProperty("ind2")
  private String ind2;

  @JsonProperty("code1")
  private Integer code1;

  @JsonProperty("code2")
  private Integer code2;

  @JsonProperty("code3")
  private Integer code3;

  @JsonProperty("marcCategory")
  private Integer marcCategory;

  /**
   * tagCode
   * <p>
   *
   * @return the tagCode
   */
  @JsonProperty("tagCode")
  public String getTagCode() {
    return tagCode;
  }

  /**
   * tagCode
   * <p>
   *
   * @param tagCode The tagCode
   */
  @JsonProperty("tagCode")
  public void setTagCode(String tagCode) {
    this.tagCode = tagCode;
  }

  /**
   * ind1
   * <p>
   *
   * @return the ind1
   */
  @JsonProperty("ind1")
  public String getInd1() {
    return ind1;
  }

  /**
   * ind1
   * <p>
   *
   * @param ind1 The ind1
   */
  @JsonProperty("ind1")
  public void setInd1(String ind1) {
    this.ind1 = ind1;
  }

  /**
   * ind2
   * <p>
   *
   * @return the ind2
   */
  @JsonProperty("ind2")
  public String getInd2() {
    return ind2;
  }

  /**
   * ind2
   * <p>
   *
   * @param ind2 The ind2
   */
  @JsonProperty("ind2")
  public void setInd2(String ind2) {
    this.ind2 = ind2;
  }

  /**
   * code1
   * <p>
   *
   * @return the code1
   */
  @JsonProperty("code1")
  public Integer getCode1() {
    return code1;
  }

  /**
   * code1
   * <p>
   *
   * @param code1 The code1
   */
  @JsonProperty("code1")
  public void setCode1(Integer code1) {
    this.code1 = code1;
  }

  /**
   * code2
   * <p>
   *
   * @return the code2
   */
  @JsonProperty("code2")
  public Integer getCode2() {
    return code2;
  }

  /**
   * code2
   * <p>
   *
   * @param code2 The code2
   */
  @JsonProperty("code2")
  public void setCode2(Integer code2) {
    this.code2 = code2;
  }

  /**
   * code3
   * <p>
   *
   * @return the code3
   */
  @JsonProperty("code3")
  public Integer getCode3() {
    return code3;
  }

  /**
   * code3
   * <p>
   *
   * @param code3 The code3
   */
  @JsonProperty("code3")
  public void setCode3(Integer code3) {
    this.code3 = code3;
  }

  /**
   * marcCategory
   * <p>
   *
   * @return the marcCategory
   */
  @JsonProperty("marcCategory")
  public Integer getMarcCategory() {
    return marcCategory;
  }

  /**
   * marcCategory
   * <p>
   *
   * @param marcCategory The marcCategory
   */
  @JsonProperty("marcCategory")
  public void setMarcCategory(Integer marcCategory) {
    this.marcCategory = marcCategory;
  }
}
