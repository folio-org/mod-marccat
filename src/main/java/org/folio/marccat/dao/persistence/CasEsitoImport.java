package org.folio.marccat.dao.persistence;

import java.io.Serializable;

public class CasEsitoImport implements Serializable {
  private String transactionId;
  private String idFonte;
  private String idEsito;

  public String getIdEsito() {
    return idEsito;
  }

  public void setIdEsito(String idEsito) {
    this.idEsito = idEsito;
  }

  public String getIdFonte() {
    return idFonte;
  }

  public void setIdFonte(String idFonte) {
    this.idFonte = idFonte;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

}
