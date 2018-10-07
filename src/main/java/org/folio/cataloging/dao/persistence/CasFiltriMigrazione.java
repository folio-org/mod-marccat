package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

public class CasFiltriMigrazione implements Serializable {
  private String idFonte;
  private String filtro;
  private String ragione;

  public String getFiltro() {
    return filtro;
  }

  public void setFiltro(String filtro) {
    this.filtro = filtro;
  }

  public String getIdFonte() {
    return idFonte;
  }

  public void setIdFonte(String idFonte) {
    this.idFonte = idFonte;
  }

  public String getRagione() {
    return ragione;
  }

  public void setRagione(String ragione) {
    this.ragione = ragione;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof CasFiltriMigrazione) {
      CasFiltriMigrazione cfm = (CasFiltriMigrazione) arg0;
      return cfm.getIdFonte ( ).equals (getIdFonte ( )) &&
        cfm.getFiltro ( ).equals (getFiltro ( ));
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getIdFonte ( ).hashCode ( ) +
      getFiltro ( ).hashCode ( );
  }

}
