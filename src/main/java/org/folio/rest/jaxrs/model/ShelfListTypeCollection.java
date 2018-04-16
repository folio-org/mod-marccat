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
@JsonPropertyOrder({ "shelfListTypes" })
public class ShelfListTypeCollection {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("shelfListTypes")
    @Valid
    @NotNull
    private List<ShelfListType> shelfListTypes = new ArrayList<ShelfListType>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The shelfListTypes
     */
    @JsonProperty("shelfListTypes")
    public List<ShelfListType> getShelfListTypes() {
        return shelfListTypes;
    }

    /**
     * 
     * (Required)
     * 
     * @param shelfListTypes
     *     The shelfListTypes
     */
    @JsonProperty("shelfListTypes")
    public void setShelfListTypes(List<ShelfListType> shelfListTypes) {
        this.shelfListTypes = shelfListTypes;
    }

    public ShelfListTypeCollection withShelfListTypes(List<ShelfListType> shelfListTypes) {
        this.shelfListTypes = shelfListTypes;
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

    public ShelfListTypeCollection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }
}
