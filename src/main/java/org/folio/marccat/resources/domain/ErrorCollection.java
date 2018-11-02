package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ErrorCollection {

  @JsonProperty("errors")
  @Valid
  @NotNull
  private List <Error> errors = new ArrayList <>();

  /**
   * (Required)
   *
   * @return The errors
   */
  @JsonProperty("errors")
  public List <Error> getErrors() {
    return errors;
  }

  /**
   * (Required)
   *
   * @param errors The errors
   */
  @JsonProperty("errors")
  public void setErrors(List <Error> errors) {
    this.errors = errors;
  }

}
