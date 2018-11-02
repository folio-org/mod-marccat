package org.folio.marccat.model;

public class ConfigurationBrowseColumn {
  private String user;
  private String ntColumn;
  private String refColumn;
  private String autorityColumn;
  private String levelColumn;
  private String docColumn;
  private String indColumn;
  private String accColumn;
  private String defBibModel;
  private String defAuthModel;


  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getNtColumn() {
    return ntColumn;
  }

  public void setNtColumn(String ntColumn) {
    this.ntColumn = ntColumn;
  }

  public String getRefColumn() {
    return refColumn;
  }

  public void setRefColumn(String refColumn) {
    this.refColumn = refColumn;
  }

  public String getAutorityColumn() {
    return autorityColumn;
  }

  public void setAutorityColumn(String autorityColumn) {
    this.autorityColumn = autorityColumn;
  }

  public String getLevelColumn() {
    return levelColumn;
  }

  public void setLevelColumn(String levelColumn) {
    this.levelColumn = levelColumn;
  }

  public String getDocColumn() {
    return docColumn;
  }

  public void setDocColumn(String docColumn) {
    this.docColumn = docColumn;
  }

  public String getIndColumn() {
    return indColumn;
  }

  public void setIndColumn(String indColumn) {
    this.indColumn = indColumn;
  }

  public String getAccColumn() {
    return accColumn;
  }

  public void setAccColumn(String accColumn) {
    this.accColumn = accColumn;
  }

  public String getDefBibModel() {
    return defBibModel;
  }

  public void setDefBibModel(String defBibModel) {
    this.defBibModel = defBibModel;
  }

  public String getDefAuthModel() {
    return defAuthModel;
  }

  public void setDefAuthModel(String defAuthModel) {
    this.defAuthModel = defAuthModel;
  }
}
