/*
 * (c) LibriCore
 * 
 * Created on Aug 13, 2004
 * 
 * Defaults.java
 */
package librisuite.business.common;

import java.io.Serializable;
import java.util.Hashtable;

import librisuite.business.codetable.DAOGlobalVariable;

/**
 * provides access to default values from S_SYS_GLBL_VRBL
 * @author carmen
 * @version $Revision: 1.0 $, $Date: 2012/30/07 10:01:06 $
 * @since 1.0
 */
public class ConfigHandler implements Serializable{

	private DAOGlobalVariable daoGV;
	private Hashtable<String, String> allGlobalVariable;
	
	/** singleton field */
	private static ConfigHandler instance = null;

	private ConfigHandler() {
		daoGV = new DAOGlobalVariable();
		setAllGlobalVariable(daoGV.getAllGlobalVariable());
	 }

	/**
	 * singleton method
	 */
    public static ConfigHandler getInstance(){
		if(instance == null) {
			instance = new ConfigHandler();
		}
     	return instance;
    }
    
    public String getValue(String name){
		String value= (String)getAllGlobalVariable().get(name);
		return value;
	}
	

	public Hashtable<String, String> getAllGlobalVariable() {
		return allGlobalVariable;
	}    

	public void setAllGlobalVariable(Hashtable<String, String> allGlobalVariable) {
		this.allGlobalVariable = allGlobalVariable;
	}
    
	/**
	 * find the default values first in the S_SYS_GLBL_VRBL
	 * then in in defaultValues.properties.
	 * The value in S_SYS_GLBL_VRBL is always updated
	 * @param name is property for S_SYS_GLBL_VRBL
	 * @param defaultName is property for efaultValues.properties.
	 * @return value
	 * @throws DataAccessException 
	 */
	public String findValue(String name,String defaultName){
		String value;
		if(allGlobalVariable.containsKey(name))
		  value=allGlobalVariable.get(name);
		else
		  value = Defaults.getString(defaultName);
		return value;
	}
	
	/**
	 * Natascia: bug 2700
	 * Checks if the name is in S_SYS_GLBL_VRBL
	 * @param name the name of parameter to check.
	 * @return true if exist.
	 */
	public boolean isParamOfGlobalVariable(String name)
	{		
		return allGlobalVariable.containsKey(name);
	}
	
}
