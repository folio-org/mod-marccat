/*
 * (c) LibriCore
 *
 * Created on Nov 17, 2004
 *
 * Policy.java
 */
package org.folio.cataloging.business.authorisation;

import java.util.HashSet;
import java.util.Set;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/11/19 16:48:32 $
 * @since 1.0
 */
public class Policy {
  static private Policy theInstance;
  private Set permissions;

  private Policy() {
    setPermissions(new HashSet());
    getPermissions().add(new Permission("basicCataloguing"));
    getPermissions().add(new Permission("advancedCataloguing"));
    getPermissions().add(new Permission("editNotes"));
    getPermissions().add(new Permission("editCopies"));
    getPermissions().add(new Permission("editClassificationNumbers"));
    getPermissions().add(new Permission("editHeaders"));
    getPermissions().add(new Permission("editTitles"));
    getPermissions().add(new Permission("editNames"));
    getPermissions().add(new Permission("editSummaryHoldings"));
    getPermissions().add(new Permission("editControlNumbers"));
    getPermissions().add(new Permission("editNameTitles"));
    getPermissions().add(new Permission("editRelationships"));
    getPermissions().add(new Permission("editSubjects"));
  }

  static public Policy getInstance() {
    if (theInstance == null) {
      theInstance = new Policy();
    }
    return theInstance;
  }

  /**
   * @since 1.0
   */
  public Set getPermissions() {
    return permissions;
  }

  /**
   * @since 1.0
   */
  public void setPermissions(Set set) {
    permissions = set;
  }

}
