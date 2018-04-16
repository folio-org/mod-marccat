package org.folio.rest.jaxrs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "headingTypes" })
public class HeadingTypeCollection {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("headingTypes")
    @Valid
    @NotNull
    private List<HeadingType> headingTypes = new ArrayList<HeadingType>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The headingTypes
     */
    @JsonProperty("headingTypes")
    public List<HeadingType> getHeadingTypes() {
        return headingTypes;
    }

    /**
     * 
     * (Required)
     * 
     * @param headingTypes
     *     The headingTypes
     */
    @JsonProperty("headingTypes")
    public void setHeadingTypes(List<HeadingType> headingTypes) {
        this.headingTypes = headingTypes;
    }

    public HeadingTypeCollection withHeadingTypes(List<HeadingType> headingTypes) {
        this.headingTypes = headingTypes;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public HeadingTypeCollection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }
}
