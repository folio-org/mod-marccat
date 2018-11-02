/*
 * (c) LibriCore
 *
 * Created on Nov 17, 2004
 *
 * Permission.java
 */
package org.folio.marccat.business.authorisation;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/11/19 16:48:32 $
 * @since 1.0
 */
public class Permission {
  public static final String TO_EDIT_CODE_TABLE_ITEM = "editCodeTableItem";
  private String name;

  public Permission(String name) {
    setName(name);
  }

  /**
   * @since 1.0
   */
  public String getName() {
    return name;
  }

  /**
   * @since 1.0
   */
  public void setName(String string) {
    name = string;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof Permission) {
      return ((Permission) arg0).getName().equals(getName());
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getName().hashCode();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return new ToStringBuilder(this).append("name", name).toString();
  }

}
