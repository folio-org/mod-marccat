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
@JsonPropertyOrder({ "controlTypes" })
public class ControlTypeCollection {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("controlTypes")
    @Valid
    @NotNull
    private List<ControlType> controlTypes = new ArrayList<ControlType>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The controlTypes
     */
    @JsonProperty("controlTypes")
    public List<ControlType> getControlTypes() {
        return controlTypes;
    }

    /**
     * 
     * (Required)
     * 
     * @param controlTypes
     *     The controlTypes
     */
    @JsonProperty("controlTypes")
    public void setControlTypes(List<ControlType> controlTypes) {
        this.controlTypes = controlTypes;
    }

    public ControlTypeCollection withControlTypes(List<ControlType> controlTypes) {
        this.controlTypes = controlTypes;
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

    public ControlTypeCollection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }
}
