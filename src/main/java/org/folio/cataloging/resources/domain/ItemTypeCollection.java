package org.folio.cataloging.resources.domain;

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
@JsonPropertyOrder({ "itemTypes" })
public class ItemTypeCollection {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("itemTypes")
    @Valid
    @NotNull
    private List<ItemType> itemTypes = new ArrayList<ItemType>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The itemTypes
     */
    @JsonProperty("itemTypes")
    public List<ItemType> getItemTypes() {
        return itemTypes;
    }

    /**
     * 
     * (Required)
     * 
     * @param itemTypes
     *     The itemTypes
     */
    @JsonProperty("itemTypes")
    public void setItemTypes(List<ItemType> itemTypes) {
        this.itemTypes = itemTypes;
    }

    public ItemTypeCollection withItemTypes(List<ItemType> itemTypes) {
        this.itemTypes = itemTypes;
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

    public ItemTypeCollection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }
}
