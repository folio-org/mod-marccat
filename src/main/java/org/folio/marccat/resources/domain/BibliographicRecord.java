package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * BibliograpgicRecord
 * <p>
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"id", "group", "canadianContentIndicator", "verificationLevel", "leader", "fields", "recordView"})
public class BibliographicRecord extends Record {

}
