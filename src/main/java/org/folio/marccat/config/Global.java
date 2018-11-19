package org.folio.marccat.config;

import net.sf.hibernate.cfg.Configuration;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Global constants.
 * With "Global" we mean a constant that
 *
 * <ul>
 * <li>is supposed to be shared at least between 2 modules.</li>
 * <li>needs to be used within this "shared" module</li>
 * </ul>
 *
 * @author paulm
 * @author agazzarini
 * @author natasciab
 * @since 1.0
 */
// TODO: clean up (lot of unused fields)
public abstract class Global {
  public static final String OKAPI_TENANT_HEADER_NAME = "x-okapi-tenant";
  public static final String EMPTY_STRING = "";
  public final static String SUBFIELD_DELIMITER_FOR_VIEW = "\\$";
  public static String SUBFIELD_DELIMITER = "\u001f";
  public static Configuration HCONFIGURATION = new Configuration();

  static {
    HCONFIGURATION.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
    HCONFIGURATION.setProperty("dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
    HCONFIGURATION.setProperty("show_sql", System.getProperty("show.sql", "false"));
    try {
      HCONFIGURATION.configure("/hibernate.cfg.xml");
    } catch (final Throwable failure) {
      throw new ExceptionInInitializerError(failure);
    }
  }


}
