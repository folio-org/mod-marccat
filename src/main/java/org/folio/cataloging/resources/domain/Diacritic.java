package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

/**
 * Diacritic Schema
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "value", "label", "character", "characterSet", "unicode" })
public class Diacritic {

    @JsonProperty("value")
    private Long code;

    @JsonProperty("label")
    private String description;

    @JsonProperty("character")
    private String character;

    @JsonProperty("characterSet")
    private String characterSet;

    @JsonProperty("unicode")
    private String unicode;


    /**
     * 
     * @return
     *     The code
     */
    @JsonProperty("value")
    public Long getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    @JsonProperty("value")
    public void setCode(Long code) {
        this.code = code;
    }

    public Diacritic withCode(Long code) {
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

    public Diacritic withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     *
     * @return
     *     The character
     */
    @JsonProperty("character")
    public String getCharacter() {
        return character;
    }

    /**
     *
     * @param character
     *     The character
     */
    @JsonProperty("character")
    public void setCharacter(String character) {
        this.character = character;
    }

    public Diacritic withCharacter(String character) {
        this.character = character;
        return this;
    }

    /**
     *
     * @return
     *     The characterSet
     */
    @JsonProperty("characterSet")
    public String getCharacterSet() {
        return characterSet;
    }

    /**
     *
     * @param characterSet
     *     The characterSet
     */
    @JsonProperty("characterSet")
    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public Diacritic withCharacterSet(String characterSet) {
        this.characterSet = characterSet;
        return this;
    }

    /**
     *
     * @return
     *     The unicode
     */
    @JsonProperty("unicode")
    public String getUnicode() {
        return unicode;
    }

    /**
     *
     * @param unicode
     *     The unicode
     */
    @JsonProperty("unicode")
    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public Diacritic withUnicode(String unicode) {
        this.unicode = characterSet;
        return this;
    }
}
