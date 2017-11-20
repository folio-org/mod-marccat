/*
 * Created on Nov 25, 2004
 */
package org.folio.cataloging.business.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Class comment
 * @author janick
 */
public class PropertyBasedFactoryBuilder {
	
	private static final char RANGE_INDICATOR = '-';
	private static final String NUMBER_DELIMITER = ",";
	private static final String PACKAGE_STRING_KEY = "package";

	/**
	 * @param fileName
	 * @param factory
	 */
	public void load(String fileName, AbstractMapBackedFactory factory) {
		factory.clear();
		try {
			readProperties(getClass().getResourceAsStream(fileName), factory);
		} catch (IOException e) {
			throw new RuntimeException("Error reading properties from stream for file " + fileName, e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Error finding a class for a key in " + fileName, e);
		}
	}


	private void readProperties(InputStream stream, AbstractMapBackedFactory factory) throws IOException, ClassNotFoundException {
		Properties p = new Properties();
		p.load(stream);
		addAllNumberToClassMappings(p, factory);
	}

	private void addAllNumberToClassMappings(Properties p, AbstractMapBackedFactory factory) throws ClassNotFoundException {
		String packageString = p.getProperty(PACKAGE_STRING_KEY);
		Enumeration e = p.propertyNames();
		while (e.hasMoreElements()) {
			String next = (String) e.nextElement();
			if (!(PACKAGE_STRING_KEY.equals(next))) {
				mapNumbers(getNumberList(p.getProperty(next)), findClass(packageString,next), factory);
			}
		}
	}

	private void mapNumbers(List list, Class clazz, AbstractMapBackedFactory factory) {
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			factory.put((Integer)iterator.next(), clazz);
		}
	}

	private String getFullClassName(String packageString, String next) {
		if ((packageString == null) || ("".equals(packageString))) {
			return next;
		}else {
			return packageString + "." + next;
		}
	}

	private List getNumberList(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value,NUMBER_DELIMITER);
		List list = new ArrayList();
		while (tokenizer.hasMoreTokens()) {
			addValueOrRangeToList(tokenizer.nextToken(),list);
		}
		return list;
	}


	private void addValueOrRangeToList(String string, List list) {
		int dashIndex = string.indexOf(RANGE_INDICATOR);
		if (dashIndex == -1) {
			list.add(new Integer(string));
		} else {
			addRangeToList(string.substring(0,dashIndex), string.substring(dashIndex+1), list);
		}
	}


	private void addRangeToList(String start, String stop, List list) {
		for (int i = Integer.parseInt(start);
			i <= Integer.parseInt(stop);
			i++) {
			list.add(new Integer(i));
		}
	}


	private Class findClass(String packageString, String next) throws ClassNotFoundException {
		return Class.forName(getFullClassName(packageString,next));
	}
	
}
