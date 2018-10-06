package org.folio.cataloging.shared;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @author paulm
 * @since 1.0
 */
public abstract class Validation {

  private char marcTagObsoleteIndicator;
  private char marcTagRepeatableIndicator;
  private char marcTagEditableIndicator;
  private char marcTagDefaultSubfieldCode;
  private String marcValidSubfieldStringCode;
  private String repeatableSubfieldStringCode;
  private char skipInFlngCode;

  public char getMarcTagDefaultSubfieldCode() {
    return marcTagDefaultSubfieldCode;
  }

  public void setMarcTagDefaultSubfieldCode(char c) {
    marcTagDefaultSubfieldCode = c;
  }

  public char getMarcTagEditableIndicator() {
    return marcTagEditableIndicator;
  }

  public void setMarcTagEditableIndicator(char c) {
    marcTagEditableIndicator = c;
  }

  public char getMarcTagObsoleteIndicator() {
    return marcTagObsoleteIndicator;
  }

  public void setMarcTagObsoleteIndicator(char c) {
    marcTagObsoleteIndicator = c;
  }

  public char getMarcTagRepeatableIndicator() {
    return marcTagRepeatableIndicator;
  }

  public void setMarcTagRepeatableIndicator(char c) {
    marcTagRepeatableIndicator = c;
  }

  public boolean isMarcTagRepeatable() {
    return marcTagRepeatableIndicator == '1';
  }

  public String getMarcValidSubfieldStringCode() {
    return marcValidSubfieldStringCode;
  }

  public void setMarcValidSubfieldStringCode(String string) {
    marcValidSubfieldStringCode = string;
  }

  public String getRepeatableSubfieldStringCode() {
    return repeatableSubfieldStringCode;
  }

  public void setRepeatableSubfieldStringCode(String string) {
    repeatableSubfieldStringCode = string;
  }

  public char getSkipInFlngCode() {
    return skipInFlngCode;
  }

  public void setSkipInFlngCode(char c) {
    skipInFlngCode = c;
  }

  public List <String> getValidSubfieldCodes() {
    return stream (marcValidSubfieldStringCode.split ("")).collect (toList ( ));
  }

  public List <String> getRepeatableSubfieldCodes() {
    return stream (repeatableSubfieldStringCode.split ("")).collect (toList ( ));
  }

  abstract public ValidationKey getKey();
}
