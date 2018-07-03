
package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;


/**
 * Control Type Schema
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "value", "label" })
public class ControlType {

    @JsonProperty("value")
    private String code;
    @JsonProperty("label")
    private String description;

    /**
     * 
     * @return
     *     The code
     */
    @JsonProperty("value")
    public String getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    @JsonProperty("value")
    public void setCode(String code) {
        this.code = code;
    }

    public ControlType withCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("label")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("label")
    public void setDescription(String description) {
        this.description = description;
    }

    public ControlType withDescription(String description) {
        this.description = description;
        return this;
    }

}
