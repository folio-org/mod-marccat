package com.libricore.librisuite.common;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mwolff.struts.action.BackBaseAction;

/**
 * This class is struts dependent
 * @author michelem
 */
public class BackUtils {
	private static final Log logger = LogFactory.getLog(BackUtils.class);

	/**
	 * Recostruct the url with protocol, host, port etc...
	 * @param fullPath
	 * @param context
	 * @param module
	 * @param backPath
	 * @return
	 * @throws MalformedURLException
	 */
	private static String computeBackPath(String fullPath, String context, String module, String backPath) throws MalformedURLException {
		URL url = new URL(fullPath);
		int len = url.getProtocol().length() + 1;
		StringBuffer result = new StringBuffer(len);
		result.append(url.getProtocol());
		    result.append(":");
		    if (url.getAuthority() != null && url.getAuthority().length() > 0) {
		        result.append("//");
		        result.append(url.getAuthority());
		    }
 		result.append(context);
		result.append(module);
		result.append(backPath);
		return result.toString();
	}

	/**
	 * Used to push the return action. 
	 * This method uses org.mwolff.struts.action.BackBaseAction class
	 * @param mapping
	 * @param request
	 * @param forward
	 */
	public static void registerBackPath(ActionMapping mapping, HttpServletRequest request, String forward) {
		ActionForward backTo = mapping.findForward(forward);
		if(backTo!=null){
			try {
				String fullPath = request.getRequestURL().toString();
				String context = request.getContextPath();
				String module = "";
				if(!backTo.getContextRelative()){
					module = mapping.getModuleConfig().getPrefix();
				}
				String backPath = backTo.getPath();
				String result = computeBackPath(fullPath, context, module, backPath);
				BackBaseAction.push(request,result.toString());
				logger.debug("registered for back:"+result.toString());
			} catch (MalformedURLException e) {
				logger.error("back not registered", e);
			}
		}
	}

	/**
	 * Used to scan action's forwards and intercept the first that match the URL parameters 
	 * @param mapping the current action mapping
	 * @param request the http request
	 * @return selectedDestination the value associated to the matched parameter to use as forward
	 * 
	 */
	public static String findAndRemember(ActionMapping mapping, HttpServletRequest request) {
		String forwards[] = mapping.findForwards();
		String selectedDestination = null;
		for (int i = 0; i < forwards.length; i++) {
			String forward = forwards[i];
			if (request.getParameter(forward) != null) {
				// MIKE: if there is a parameter sets with a REGISTRY_ACTIVATOR_KEY value 
				// then push the back action in the BACKBUFFER
				registerBackPath(mapping, request, forward);
				selectedDestination = request.getParameter(forward);
				break; // MIKE: only one is enough!
			}
		}
		logger.info("Destination: "+selectedDestination);
		return selectedDestination;
	}

	/**
	 * Entry method to save action
	 * @param mapping
	 * @param request
	 * @return
	 */
	public static ActionForward rememberAction(ActionMapping mapping, HttpServletRequest request) {
		String selectedDestination = findAndRemember(mapping, request);
		ActionForward toForward = mapping.findForward(selectedDestination);
		if(	selectedDestination==null 
			|| "true".equalsIgnoreCase(selectedDestination.trim()) 
			|| toForward==null) {
			toForward = mapping.findForward(mapping.getParameter());
			if(toForward==null) throw new IllegalArgumentException(
					(mapping.getParameter()==null?
							"parameter is mandatory for RememberAction "
							+ mapping.getPath()
							+ " when no parameters match;"
						:
							"Check for a duplicated parameter '"
							+ "' in the form request "
							+ "(key already used as form field name)"
					)
				);
		}
		return toForward;
	}

}
