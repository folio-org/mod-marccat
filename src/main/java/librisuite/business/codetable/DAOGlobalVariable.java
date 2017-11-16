package librisuite.business.codetable;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.hibernate.S_SYS_GLBL_VRBL;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * Provides access to S_SYS_GLBL_VRBL
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class DAOGlobalVariable extends HibernateUtil implements Serializable {
    
    private Log logger = LogFactory.getLog(DAOGlobalVariable.class);
    
	//TODO null exception if variable doesn't exist
	public String getValueByName(String name) throws DataAccessException 
	{
		String valueByName = null;
		S_SYS_GLBL_VRBL ss =((S_SYS_GLBL_VRBL) get(S_SYS_GLBL_VRBL.class, name));
		if(ss!=null){
		  valueByName= ss.getValue();
		}
		return valueByName;
	}
	
	public void edit(final S_SYS_GLBL_VRBL globalVrbl) throws DataAccessException 
	{
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException 
			{
				s.update(globalVrbl);
			}
		}
		.execute();
	}
	
	public void setValueByName(String name,String value) throws DataAccessException 
	{	  
		S_SYS_GLBL_VRBL sysGlobal = (S_SYS_GLBL_VRBL) get(S_SYS_GLBL_VRBL.class, name);
		sysGlobal.setValue(value);
		edit(sysGlobal);
	}
	
	public Hashtable<String, String> getAllGlobalVariable()
	{
		List<S_SYS_GLBL_VRBL> listAllKeys = null;
		Hashtable<String, String> hash = new Hashtable <String, String>();
		try {
			listAllKeys = find("from S_SYS_GLBL_VRBL");
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		Iterator<S_SYS_GLBL_VRBL> iter = listAllKeys.iterator();
		while (iter.hasNext()) {
			S_SYS_GLBL_VRBL rawGlobalVar = (S_SYS_GLBL_VRBL) iter.next();
			hash.put(rawGlobalVar.getName(),rawGlobalVar.getValue());
		}
		return hash;
	}
}