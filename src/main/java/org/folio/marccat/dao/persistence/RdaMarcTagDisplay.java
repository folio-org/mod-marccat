package org.folio.marccat.dao.persistence;

import java.io.Serializable;

public class RdaMarcTagDisplay extends LabelTagDisplay implements Serializable {
  private static final long serialVersionUID = 4091846105600659469L;

  private String marcTagNumberText;


  @Override
  public String getMarcTagNumberText() {
    return marcTagNumberText;
  }

  @Override
  public void setMarcTagNumberText(String marcTagNumberText) {
    this.marcTagNumberText = marcTagNumberText;
  }


}
