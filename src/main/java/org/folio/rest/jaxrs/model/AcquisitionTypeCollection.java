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
@JsonPropertyOrder({ "acquisitionTypes" })
public class AcquisitionTypeCollection {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("acquisitionTypes")
    @Valid
    @NotNull
    private List<AcquisitionType> acquisitionTypes = new ArrayList<AcquisitionType>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The acquisitionTypes
     */
    @JsonProperty("acquisitionTypes")
    public List<AcquisitionType> getAcquisitionTypes() {
        return acquisitionTypes;
    }

    /**
     * 
     * (Required)
     * 
     * @param acquisitionTypes
     *     The acquisitionTypes
     */
    @JsonProperty("acquisitionTypes")
    public void setAcquisitionTypes(List<AcquisitionType> acquisitionTypes) {
        this.acquisitionTypes = acquisitionTypes;
    }

    public AcquisitionTypeCollection withAcquisitionTypes(List<AcquisitionType> acquisitionTypes) {
        this.acquisitionTypes = acquisitionTypes;
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

    public AcquisitionTypeCollection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }
}
