package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"name", "defaultValue", "dropdownSelect"})

public class FixedFieldElement {
  @JsonProperty("name")
  private String name;

  @JsonProperty("defaultValue")
  private String dafaultValue;

  @JsonProperty("dropdownSelect")
  private List<?> dropdownSelect = new ArrayList<Pair>();

  public FixedFieldElement(String name, List dropdownSelect) {
    this.name = name;
    this.dropdownSelect = dropdownSelect;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDafaultValue() {
    return dafaultValue;
  }

  public void setDafaultValue(String dafaultValue) {
    this.dafaultValue = dafaultValue;
  }

  public List getDropdownSelect() {
    return dropdownSelect;
  }

  public void setDropdownSelect(List dropdownSelect) {
    this.dropdownSelect = dropdownSelect;
  }


}
