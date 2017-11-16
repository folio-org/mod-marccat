/*
 * (c) LibriCore
 * 
 * Created on Jul 2, 2004
 * 
 * DAOUserAccount.java
 */
package librisuite.business.authentication;

import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.hibernate.USR_ACNT;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * provides data a ccess to USR_ACNT
 * @author paulm
 * @version MIKE: Modified for Oracle 9.2 and later
 * @since 1.0
 */

public class DAOUserAccount extends HibernateUtil {
	/**
	 * add following line in defaultValues.properties
	 * 
	 * padding.user=false [if you use Oracle 9.2 or later] (mandatory)
	 * 
	 * padding.user=true [if you use Oracle 9.0 or previous] (optional)
	 * 
	 * Oracle 9.1 not tested
	 */
	private static String padding =Defaults.getString("padding.user");
	public static final String DEFAULT_LDAP_AUTH_BROKER = "2";
	private static String broker =Defaults.getString("authentication.broker");
	private static String ldapPrefix =Defaults.getString("username.ldap.prefix");
	
	
	private String toStringUserAccount(String name) {
		String stringName = name;
		if(DEFAULT_LDAP_AUTH_BROKER.equals(broker)){
			if(Character.isDigit(name.charAt(0)) ){
				stringName = ldapPrefix + name;
			}
		} 
		return stringName;
	}
	
	private String padUserAccount(String name) {
		if(padding==null || "true".equalsIgnoreCase(padding.trim())){
			return name.concat("            ").substring(0, 12);
		}
		else return name;
	}

	public USR_ACNT load(String userAccount) throws DataAccessException {
		return (USR_ACNT) get(USR_ACNT.class, padUserAccount(toStringUserAccount(userAccount)));
	}

	public List getModuleAuthorisations(String userAccount)
		throws DataAccessException {
		Session s = currentSession();
		List result = null;
		try {
			result =
				s.find(
					"from ModuleAuthorisation ma where ma.userAccount = ?",
					padUserAccount(toStringUserAccount(userAccount)),
					Hibernate.STRING);
			
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		
		return result;
	}

	public List getTagAuthorisations(String userAccount)
		throws DataAccessException {
		Session s = currentSession();
		List result = null;
		try {
			result =
				s.find(
					"from TagAuthorisation ta where ta.userAccount = ?",
					/*
					 * Note that userAccount in this table is VARCHAR and therefore
					 * should not be padded to 12 
					 */
					toStringUserAccount(userAccount),
					Hibernate.STRING);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
}
