package org.folio.cataloging.bean;

import org.folio.cataloging.log.Log;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

/**
 * @author Wim Crols
 * @since 1.0
 */
public abstract class LocalisedBean extends LibrisuiteBean {

	// TODO _MIKE: the map should be synchronized because it is shared over threads
	private static Hashtable/*<Class, Hashtable>*/ tables = new Hashtable();
	private static final Log logger = new Log(LocalisedBean.class);
	
	private Class clazz = null;
	
	public LocalisedBean() {
		super();
	}
	
	public LocalisedBean(Class clazz) {
		super();
		this.clazz = clazz;
	}


	private Hashtable getUniqueTable(){
		Class key = clazz;
		if(!tables.containsKey(key)) {
			tables.put(key, new Hashtable());
		}
		return (Hashtable) tables.get(key);
	}
	
	private boolean containsLocale(Locale locale) {
		return getUniqueTable().containsKey(locale);
	}

	public Object getObject(Locale locale) {
		
		Hashtable hashtable = getUniqueTable();
		if (containsLocale(locale)) {
			return hashtable.get(locale);
		} else {
			Object object = loadObject(locale);
			return setObject(locale, object);
		}
	}

	private Object setObject(Locale locale, Object object) {
		getUniqueTable().put(locale, object);
		return object;
	}

	public abstract Object loadObject(Locale locale);
	
	/**
	 * This method is called when the table is changed at runtime to inform
	 * all EditBeans to discard statics preloaded values
	 * @author michelem
	 * @return
	 */
	public void onChange(Class changedClass){
		if(!tables.containsKey(changedClass)) {
			return;
		}
		Hashtable ht = (Hashtable) tables.get(changedClass);
		Enumeration locals = ht.keys();
		tables.remove(changedClass);
		while (locals.hasMoreElements()) {
			Locale locale = (Locale) locals.nextElement();
			loadObject(locale);
		}
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
	public Class getClazz() {
		return clazz;
	}

 public Object getObject(Locale locale, boolean alphabetic) {
		
		Hashtable hashtable = getUniqueTable();
		if (containsLocale(locale)) {
			logger.debug("getting table from cache");
			return hashtable.get(locale);
		} else {
			logger.debug("getting table from db");
			Object object = loadObject(locale, alphabetic);
			return setObject(locale, object);
		}
	}

  public Object loadObject(Locale locale, boolean alphabetic) {
		return loadObject(locale);
	}
}
