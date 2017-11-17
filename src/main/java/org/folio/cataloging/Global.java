package org.folio.cataloging;

import net.sf.hibernate.cfg.Configuration;

/**
 * Global constants.
 * With "Global" we mean a constant that 
 * 
 * <ul>
 * 		<li>is supposed to be shared at least between 2 modules.</li>
 * 		<li>needs to be used within this "shared" module</li>
 * </ul>
 *
 * @since 1.0
 */
public abstract class Global  {
	public static String SUBFIELD_DELIMITER = "\u001f";
	public static String SWITCH_DATABASE = "switch.database";
	public static String SCHEMA_CUSTOMER_KEY = "CUSTOM_KEY";
	public static String SCHEMA_SUITE_KEY = "SUITE_KEY";

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