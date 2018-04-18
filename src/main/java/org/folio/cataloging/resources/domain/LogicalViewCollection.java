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
@JsonPropertyOrder({ "views" })
public class LogicalViewCollection {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("views")
    @Valid
    @NotNull
    private List<View> views = new ArrayList<View>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The views
     */
    @JsonProperty("views")
    public List<View> getViews() {
        return views;
    }

    /**
     * 
     * (Required)
     * 
     * @param views
     *     The views
     */
    @JsonProperty("views")
    public void setViews(List<View> views) {
        this.views = views;
    }

    public LogicalViewCollection withViews(List<View> views) {
        this.views = views;
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

    public LogicalViewCollection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }
}
