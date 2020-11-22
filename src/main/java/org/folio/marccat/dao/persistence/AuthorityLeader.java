package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;

/**
 * @author elena
 *
 */
public class AuthorityLeader extends Leader {
  public AuthorityLeader() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 9);
  }

  @Override
  public String getDisplayString() {
    String result = "00000";
    result = result + getRecordStatusCode() + "z   2200000" + getEncodingLevel() + "  4500";
    return result;
  }

  private AUT getAutItm() {
    return (AUT) getItemEntity();
  }

  public char getEncodingLevel() {
    return getAutItm().getEncodingLevel();
  }

  public void setEncodingLevel(char c) {
    getAutItm().setEncodingLevel(c);
  }

  public char getRecordStatusCode() {
    return getAutItm().getRecordStatusCode();
  }

  public void setRecordStatusCode(char c) {
    getAutItm().setRecordStatusCode(c);
  }

}
