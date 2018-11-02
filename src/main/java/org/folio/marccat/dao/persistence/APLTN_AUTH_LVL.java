package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * @author michelem
 * @version $Revision: 1.1 $, $Date: 2007/03/28 13:10:03 $
 * @since 1.0
 */
public class APLTN_AUTH_LVL implements Serializable {
  private String apltnListKeyNme;
  private long apltnAuthLvlKeyNbr;
  private String apltnAuthLvlEngDsc;
  private String apltnAuthLvlFrDsc;

  /**
   * @return Returns the apltnAuthLvlEngDsc.
   */
  public String getApltnAuthLvlEngDsc() {
    return apltnAuthLvlEngDsc;
  }

  /**
   * @param apltnAuthLvlEngDsc The apltnAuthLvlEngDsc to set.
   */
  public void setApltnAuthLvlEngDsc(String apltnAuthLvlEngDsc) {
    this.apltnAuthLvlEngDsc = apltnAuthLvlEngDsc;
  }

  /**
   * @return Returns the apltnAuthLvlFrDsc.
   */
  public String getApltnAuthLvlFrDsc() {
    return apltnAuthLvlFrDsc;
  }

  /**
   * @param apltnAuthLvlFrDsc The apltnAuthLvlFrDsc to set.
   */
  public void setApltnAuthLvlFrDsc(String apltnAuthLvlFrDsc) {
    this.apltnAuthLvlFrDsc = apltnAuthLvlFrDsc;
  }

  /**
   * @return Returns the apltnAuthLvlKeyNbr.
   */
  public long getApltnAuthLvlKeyNbr() {
    return apltnAuthLvlKeyNbr;
  }

  /**
   * @param apltnAuthLvlKeyNbr The apltnAuthLvlKeyNbr to set.
   */
  public void setApltnAuthLvlKeyNbr(long apltnAuthLvlKeyNbr) {
    this.apltnAuthLvlKeyNbr = apltnAuthLvlKeyNbr;
  }

  /**
   * @return Returns the apltnListKeyNme.
   */
  public String getApltnListKeyNme() {
    return apltnListKeyNme;
  }

  /**
   * @param apltnListKeyNme The apltnListKeyNme to set.
   */
  public void setApltnListKeyNme(String apltnListKeyNme) {
    this.apltnListKeyNme = apltnListKeyNme;
  }
}
