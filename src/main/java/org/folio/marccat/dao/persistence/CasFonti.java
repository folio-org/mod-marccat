package org.folio.marccat.dao.persistence;

import java.io.Serializable;

public class CasFonti implements Serializable {
  private String idFonte;
  private String nomeFonte;

  public String getIdFonte() {
    return idFonte;
  }

  public void setIdFonte(String idFonte) {
    this.idFonte = idFonte;
  }

  public String getNomeFonte() {
    return nomeFonte;
  }

  public void setNomeFonte(String nomeFonte) {
    this.nomeFonte = nomeFonte;
  }

}
