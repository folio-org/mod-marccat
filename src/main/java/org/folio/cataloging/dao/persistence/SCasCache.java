package org.folio.cataloging.dao.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * Local Cache Casalini
 */
public class SCasCache implements Serializable {

  /**
   * identifier field
   */
  private Long bibItmNbr;

  /**
   * nullable persistent field
   */
  private String livGestScheda;

  /**
   * full constructor
   */
  public SCasCache(Long bibItmNbr, String livGestScheda) {
    this.bibItmNbr = bibItmNbr;
    this.livGestScheda = livGestScheda;
  }

  /**
   * default constructor
   */
  public SCasCache() {
  }

  /**
   * minimal constructor
   */
  public SCasCache(Long bibItmNbr) {
    this.bibItmNbr = bibItmNbr;
  }

  public Long getBibItmNbr() {
    return this.bibItmNbr;
  }

  public void setBibItmNbr(Long bibItmNbr) {
    this.bibItmNbr = bibItmNbr;
  }

  public String getLivGestScheda() {
    return this.livGestScheda;
  }

  public void setLivGestScheda(String livGestScheda) {
    this.livGestScheda = livGestScheda;
  }

  public String toString() {
    return new ToStringBuilder(this)
      .append("bibItmNbr", getBibItmNbr())
      .toString();
  }

}
