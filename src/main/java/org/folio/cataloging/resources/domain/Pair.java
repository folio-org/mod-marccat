package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "value", "label" })
public class Pair {

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

    @Override
    public String toString(){
        return "Code: " + this.code + " Description: " + this.description;
    }
}
