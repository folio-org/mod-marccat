package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"currencies"})
public class CurrencyCollection {

  /**
   * (Required)
   */
  @JsonProperty("currencies")
  @Valid
  @NotNull
  private List <Currency> currencies = new ArrayList <Currency>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The currencies
   */
  @JsonProperty("currencies")
  public List <Currency> getCurrencies() {
    return currencies;
  }

  /**
   * (Required)
   *
   * @param currencies The currencies
   */
  @JsonProperty("currencies")
  public void setCurrencies(List <Currency> currencies) {
    this.currencies = currencies;
  }

  public CurrencyCollection withCurrencies(List <Currency> currencies) {
    this.currencies = currencies;
    return this;
  }

  @JsonAnyGetter
  public Map <String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  public CurrencyCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
