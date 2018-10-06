/*
 * (c) LibriCore
 *
 * Created on Jul 2, 2004
 *
 * DAOUserAccount.java
 */
package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.USR_ACNT;

import java.util.List;

/**
 * Provides data access to USR_ACNT.
 *
 * @author paulm
 * @since 1.0
 */

public class DAOUserAccount extends HibernateUtil {
  public static final String DEFAULT_LDAP_AUTH_BROKER = "2";
  /**
   * add following line in defaultValues.properties
   * <p>
   * padding.user=false [if you use Oracle 9.2 or later] (mandatory)
   * <p>
   * padding.user=true [if you use Oracle 9.0 or previous] (optional)
   * <p>
   * Oracle 9.1 not tested
   */
  private static String padding = Defaults.getString ("padding.user");
  private static String broker = Defaults.getString ("authentication.broker");
  private static String ldapPrefix = Defaults.getString ("username.ldap.prefix");


  private String toStringUserAccount(String name) {
    String stringName = name;
    if (DEFAULT_LDAP_AUTH_BROKER.equals (broker)) {
      if (Character.isDigit (name.charAt (0))) {
        stringName = ldapPrefix + name;
      }
    }
    return stringName;
  }

  // TODO pass Context and Session to this method (when needed)
  // TODO the null reference needs to be replaced with the VertContext
  // TODO The return type of this method is a future, not a boolean
  private String padUserAccount(final String name) {
    //Defaults.getBoolean("padding.user").handle(result -> {

    //})
    if (padding == null || "true".equalsIgnoreCase (padding.trim ( ))) {
      return name.concat ("            ").substring (0, 12);
    } else return name;
  }

  public USR_ACNT load(final String userAccount) throws DataAccessException {
    final Session session = currentSession ( );
    return (USR_ACNT) get (session, USR_ACNT.class, padUserAccount (toStringUserAccount (userAccount)));
  }

  public List getModuleAuthorisations(String userAccount)
    throws DataAccessException {
    Session s = currentSession ( );
    List result = null;
    try {
      result =
        s.find (
          "from ModuleAuthorisation ma where ma.userAccount = ?",
          padUserAccount (toStringUserAccount (userAccount)),
          Hibernate.STRING);

    } catch (HibernateException e) {
      logAndWrap (e);
    }

    return result;
  }

  public List getTagAuthorisations(String userAccount)
    throws DataAccessException {
    Session s = currentSession ( );
    List result = null;
    try {
      result =
        s.find (
          "from TagAuthorisation ta where ta.userAccount = ?",
          /*
           * Note that userAccount in this table is VARCHAR and therefore
           * should not be padded to 12
           */
          toStringUserAccount (userAccount),
          Hibernate.STRING);
    } catch (HibernateException e) {
      logAndWrap (e);
    }
    return result;
  }
}
