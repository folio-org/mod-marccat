package org.folio.cataloging.action;

import org.folio.cataloging.dao.DAOCodeTable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class LibrisuiteAction
{
	private static Log logger = LogFactory.getLog(LibrisuiteAction.class);
	private static DAOCodeTable daoCodeTable = new DAOCodeTable();
	private static String SUFFIX_BREADCRUMB_TAB = "_tab";


	public static void traceException(Throwable cause) 
	{
		if(cause!=null){
			logger.error("caused by", cause);
			if(!cause.equals(cause.getCause())){
				traceException(cause.getCause());
			}
		}
	}


	public static DAOCodeTable getDaoCodeTable() {
		return daoCodeTable;
	}
}