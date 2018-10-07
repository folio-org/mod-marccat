package org.folio.cataloging.dao.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * @author Hibernate CodeGenerator
 */
public class CasRecordCache implements Serializable {

  /**
   * identifier field
   */
  private CasRecordCachePK comp_id;

  /**
   * nullable persistent field
   */
  private Object recordBin;

  /**
   * full constructor
   */
  public CasRecordCache(CasRecordCachePK comp_id, Object recordBin) {
    this.comp_id = comp_id;
    this.recordBin = recordBin;
  }

  /**
   * default constructor
   */
  public CasRecordCache() {
  }

  /**
   * minimal constructor
   */
  public CasRecordCache(CasRecordCachePK comp_id) {
    this.comp_id = comp_id;
  }

  public CasRecordCachePK getComp_id() {
    return this.comp_id;
  }

  public void setComp_id(CasRecordCachePK comp_id) {
    this.comp_id = comp_id;
  }

  public Object getRecordBin() {
    return this.recordBin;
  }

  public void setRecordBin(Object recordBin) {
    this.recordBin = recordBin;
  }

  public String toString() {
    return new ToStringBuilder (this)
      .append ("comp_id", getComp_id ( ))
      .toString ( );
  }

  public boolean equals(Object other) {
    if ((this == other)) return true;
    if (!(other instanceof CasRecordCache)) return false;
    CasRecordCache castOther = (CasRecordCache) other;
    return new EqualsBuilder ( )
      .append (this.getComp_id ( ), castOther.getComp_id ( ))
      .isEquals ( );
  }

  public int hashCode() {
    return new HashCodeBuilder ( )
      .append (getComp_id ( ))
      .toHashCode ( );
  }

}
