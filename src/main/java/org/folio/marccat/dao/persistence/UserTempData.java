package org.folio.cataloging.dao.persistence;

public class UserTempData {

  private int tmp_id;
  private String pass;
  private String dataKey;
  private String dataValue;

  public UserTempData() {
    super();
  }

  public UserTempData(int tmp_id, String pass, String dataKey, String dataValue) {
    super();
    this.tmp_id = tmp_id;
    this.pass = pass;
    this.dataKey = dataKey;
    this.dataValue = dataValue;
  }

  public UserTempData(String pass, String dataKey, String dataValue) {
    super();
    this.pass = pass;
    this.dataKey = dataKey;
    this.dataValue = dataValue;
  }

  public int getTmp_id() {
    return tmp_id;
  }

  public void setTmp_id(int tmp_id) {
    this.tmp_id = tmp_id;
  }

  public String getPass() {
    return pass;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }

  public String getDataKey() {
    return dataKey;
  }

  public void setDataKey(String dataKey) {
    this.dataKey = dataKey;
  }

  public String getDataValue() {
    return dataValue;
  }

  public void setDataValue(String dataValue) {
    this.dataValue = dataValue;
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + tmp_id;
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final UserTempData other = (UserTempData) obj;
    if (tmp_id != other.tmp_id)
      return false;
    return true;
  }

}
