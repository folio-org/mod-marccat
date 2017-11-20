/*
 * (c) LibriCore
 * 
 * Created on Jun 21, 2006
 * 
 * SearchEngineFactory.java
 */
package org.folio.cataloging.business.searching;

import org.folio.cataloging.business.librivision.LibrivisionSearchEngine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Returns instances of SearchEngine implementations
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/07/11 08:01:05 $
 * @since 1.0
 */
public class SearchEngineFactory {
	private static Log logger = LogFactory.getLog(SearchEngineFactory.class);
	public static SearchEngine getInstance(Class clazz) {
		try {
			return (SearchEngine) clazz.newInstance();
		} catch (Exception e) {
			logger.error(
				"Could not instantiate a "
					+ clazz.getName()
					+ " search engine.  Using LibriVision.");
			return new LibrivisionSearchEngine();
		}
	}
}
