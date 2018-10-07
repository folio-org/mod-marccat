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
@JsonPropertyOrder({"noteTypes"})
public class NoteTypeCollection {

  /**
   * (Required)
   */
  @JsonProperty("noteTypes")
  @Valid
  @NotNull
  private List <NoteType> noteTypes = new ArrayList <NoteType> ( );

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object> ( );

  /**
   * (Required)
   *
   * @return The noteTypes
   */
  @JsonProperty("noteTypes")
  public List <NoteType> getNoteTypes() {
    return noteTypes;
  }

  /**
   * (Required)
   *
   * @param noteTypes The noteTypes
   */
  @JsonProperty("noteTypes")
  public void setNoteTypes(List <NoteType> noteTypes) {
    this.noteTypes = noteTypes;
  }

  public NoteTypeCollection withNoteTypes(List <NoteType> noteTypes) {
    this.noteTypes = noteTypes;
    return this;
  }

  @JsonAnyGetter
  public Map <String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put (name, value);
  }

  public NoteTypeCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put (name, value);
    return this;
  }
}
