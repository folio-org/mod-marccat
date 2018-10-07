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
@JsonPropertyOrder({"characterEncodingSchemas"})
public class CharacterEncodingSchemaCollection {

  /**
   * (Required)
   */
  @JsonProperty("characterEncodingSchemas")
  @Valid
  @NotNull
  private List <CharacterEncodingSchema> characterEncodingSchemas = new ArrayList <CharacterEncodingSchema> ( );

  @JsonIgnore
  private Map <String, Object> additionalProperties = new HashMap <String, Object> ( );

  /**
   * (Required)
   *
   * @return The characterEncodingSchemas
   */
  @JsonProperty("characterEncodingSchemas")
  public List <CharacterEncodingSchema> getCharacterEncodingSchemas() {
    return characterEncodingSchemas;
  }

  /**
   * (Required)
   *
   * @param characterEncodingSchemas The characterEncodingSchemas
   */
  @JsonProperty("characterEncodingSchemas")
  public void setCharacterEncodingSchemas(List <CharacterEncodingSchema> characterEncodingSchemas) {
    this.characterEncodingSchemas = characterEncodingSchemas;
  }

  public CharacterEncodingSchemaCollection withCharacterEncodingSchemas(List <CharacterEncodingSchema> characterEncodingSchemas) {
    this.characterEncodingSchemas = characterEncodingSchemas;
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

  public CharacterEncodingSchemaCollection withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put (name, value);
    return this;
  }
}
