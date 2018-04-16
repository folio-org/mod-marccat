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
@JsonPropertyOrder({ "descriptiveCatalogForms" })
public class DescriptiveCatalogFormCollection {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("descriptiveCatalogForms")
    @Valid
    @NotNull
    private List<DescriptiveCatalogForm> descriptiveCatalogForms = new ArrayList<DescriptiveCatalogForm>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The descriptiveCatalogForms
     */
    @JsonProperty("descriptiveCatalogForms")
    public List<DescriptiveCatalogForm> getDescriptiveCatalogForms() {
        return descriptiveCatalogForms;
    }

    /**
     * 
     * (Required)
     * 
     * @param descriptiveCatalogForms
     *     The descriptiveCatalogForms
     */
    @JsonProperty("descriptiveCatalogForms")
    public void setDescriptiveCatalogForms(List<DescriptiveCatalogForm> descriptiveCatalogForms) {
        this.descriptiveCatalogForms = descriptiveCatalogForms;
    }

    public DescriptiveCatalogFormCollection withDescriptiveCatalogForms(List<DescriptiveCatalogForm> descriptiveCatalogForms) {
        this.descriptiveCatalogForms = descriptiveCatalogForms;
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

    public DescriptiveCatalogFormCollection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }
}
