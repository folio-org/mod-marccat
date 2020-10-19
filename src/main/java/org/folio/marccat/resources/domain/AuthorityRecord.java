package org.folio.marccat.resources.domain;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author elena
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "group", "canadianContentIndicator", "verificationLevel", "leader", "fields", "recordView" })
public class AuthorityRecord extends Record {

}
