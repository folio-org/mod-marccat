package com.libricore.librisuite.common;

import java.util.Iterator;

import librisuite.business.common.Defaults;
import net.sf.hibernate.FlushMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.mapping.Column;
import net.sf.hibernate.mapping.Component;
import net.sf.hibernate.mapping.PersistentClass;
import net.sf.hibernate.mapping.Property;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atc.weloan.shared.Global;

public class HibernateSessionProvider
{
  private static String dialect = Defaults.getString("hibernate.dialect");

  private static String datasource = Defaults.getString("hibernate.connection.datasource");

  private static Log logger = LogFactory.getLog(HibernateSessionProvider.class);
  private static final ThreadLocal sessionHolder = new ThreadLocal();
  private SessionFactory sessionFactory = null;

  private Configuration configuration = null;

  private static HibernateSessionProvider uniqueInstance = null;
  private static HibernateException except = null;

  static {
    try {
      uniqueInstance = new HibernateSessionProvider();
    } catch (HibernateException e) {
      except = e;
    }
  }    

  private HibernateSessionProvider()
    throws HibernateException
  {
    this.configuration = new Configuration();
    
    
    
    this.configuration.setProperty("hibernate.dialect", dialect);
    this.configuration.setProperty("dialect", dialect);
    this.configuration.setProperty("hibernate.connection.datasource", datasource);
    this.configuration.setProperty("show_sql", "true");  
    
    String switch_database = System.getProperty(Global.SWITCH_DATABASE);
	if(switch_database != null && switch_database.equals("true"))
		this.configuration.configure("/hibernate-casalini.cfg.xml");
	else
		this.configuration.configure("/hibernate.cfg.xml");

    logger.debug("Hibernate Configuration parameters set");
    this.sessionFactory = this.configuration.buildSessionFactory();
    logger.debug("Hibernate SessionFactory created from Configuration");  
  }

  
  public static HibernateSessionProvider getInstance() throws HibernateException {
    if (except != null) throw except;
    return uniqueInstance;
  }

  public void closeSession() throws HibernateException {
    Session session = (Session)sessionHolder.get();
    if (session != null) {
      sessionHolder.set(null);
      session.close();
      logger.info("Hibernate Session Closed");
    } else {
      logger.info("No Hibernate Session to close");
    }
  }

  public Session currentSession() throws HibernateException {
    Session session = (Session)sessionHolder.get();

    if (session == null) {
      session = this.sessionFactory.openSession();

      session.setFlushMode(FlushMode.COMMIT);
      sessionHolder.set(session);
      logger.info("New Hibernate Session Opened");
    }
    return session;
  }

  public Class getHibernateClassName(String tableName) {
    Iterator iterator = this.configuration.getClassMappings();
    PersistentClass pc = null;

    while (iterator.hasNext())
    {
      pc = (PersistentClass)iterator.next();
      if (pc.getTable().getName().equals(tableName)) {
        return pc.getMappedClass();
      }
    }
    return null;
  }
	/**
	 * Get the name of the database table that is mapped to this class
	 * in the hibernate configuration
	 * @param c
	 * @return
	 */
	public String getHibernateTableName(Class c){
		String result = null;
		PersistentClass pc = configuration.getClassMapping(c);
		if (pc != null) {
			result = pc.getTable().getName();
		}
		return result;
	}
	/**
	 * Get the name of the database column that is mapped to the
	 * given class/property in the hibernate configuration
	 * @param c
	 * @param property
	 * @return
	 * @throws HibernateException
	 */
	public String getHibernateColumnName(Class c, String property) {
		String result = null;
		PersistentClass pc = configuration.getClassMapping(c);
		Property p = null;
		
		if (pc != null) {
			try {
				p = pc.getProperty(property);
			}
			catch (MappingException e) {
				logger.debug("not in properties, checking id");
				Iterator iter = ((Component)pc.getIdentifier()).getPropertyIterator();
				while (iter.hasNext()) {
					p = (Property)iter.next();
					logger.debug("checking " + p.getName());
					if (p.getName().equals(property)) {
						break;
					}
					else {
						p = null;
					}
				}
			}
			if (p != null) {
				result = ((Column)p.getColumnIterator().next()).getName();
			}
		}
		logger.debug("getHibernateColumnName(" + c + "," + property + ") = " + result);
		return result;
	}

  
}