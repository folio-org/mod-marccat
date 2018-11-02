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
@JsonPropertyOrder({"subscriptions"})
public class SubscriptionCollection {

  /**
   * (Required)
   */
  @JsonProperty("subscriptions")
  @Valid
  @NotNull
  private List <Subscription> subscriptions = new ArrayList <Subscription>();

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object>();

  /**
   * (Required)
   *
   * @return The subscriptions
   */
  @JsonProperty("subscriptions")
  public List <Subscription> getSubscriptions() {
    return subscriptions;
  }

  /**
   * (Required)
   *
   * @param subscriptions The subscriptions
   */
  @JsonProperty("subscriptions")
  public void setSubscriptions(List <Subscription> subscriptions) {
    this.subscriptions = subscriptions;
  }

  public SubscriptionCollection withSubscriptions(List <Subscription> subscriptions) {
    this.subscriptions = subscriptions;
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

  public SubscriptionCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }
}
