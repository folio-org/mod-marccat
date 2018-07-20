package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "headings" })
public class HeadingCollection {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("headings")
    @Valid
    @NotNull
    private List<Heading> headings = new ArrayList<Heading>();

   
    /**
     * 
     * (Required)
     * 
     * @return
     * The headings
     */
    @JsonProperty("headings")
    public List<Heading> getHeadings() {
        return headings;
    }

    /**
     * 
     * (Required)
     * 
     * @param indexes
     * The indexes
     */
    @JsonProperty("headings")
    public void setHeadings(List<Heading> headings) {
        this.headings = headings;
    }

    public HeadingCollection withHeadings(List<Heading> headings) {
        this.headings = headings;
        return this;
    }

 
}
