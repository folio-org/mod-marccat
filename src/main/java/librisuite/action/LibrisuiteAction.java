package librisuite.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import librisuite.business.cataloguing.copy.DAOCopy;
import librisuite.business.codetable.DAOCodeTable;
import librisuite.business.common.DataAccessException;
import librisuite.business.exception.LibrisuiteException;
import librisuite.business.exception.RecordInUseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.libricore.librisuite.controller.SessionUtils;

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