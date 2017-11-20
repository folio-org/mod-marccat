/*
 * Created on 18-mei-2004
 *
 */
package org.folio.cataloging.business.controller;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.folio.cataloging.bean.cataloguing.heading.HeadingBean;
import org.folio.cataloging.bean.searching.SearchTypeBean;
import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.authorisation.Permission;
import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.dao.persistence.T_ITM_DSPLY;
/**
 * This Class holds methods to set and get objects from a HttpSession MIKE:
 * added mades view
 * 
 * @author janick
 */
public final class SessionUtils {

	// TODO: ANDREA: REMOVED Globals.LOCALE_KEY
	private static String CURRENT_LOCALE_NAME = Locale.ITALIAN.toString();

	private static String USER_PROFILE_NAME = "userProfile";

	// pm 2011
	private static String CURRENT_SEARCHING_VIEW = "currentSearchingView";
	
	private static final String HEADING_BEAN_SESSION_NAME = "HeadingBean";

	/**
	 * 
	 */
	private SessionUtils() {
		super();
	}

	/**
	 * Binds a Locale to an HttpSession
	 * 
	 * @param session
	 *            The HttpSession to which the Locale will be bound
	 * @param locale
	 *            The Locale that will be bound to the HttpSession
	 * @throws IllegalArgumentException
	 *             If session or locale is null
	 */
	public static void setCurrentLocale(HttpSession session, Locale locale) {
		if (session == null)
			throw new IllegalArgumentException(
					"Cannot set an attribute on a null session");
		if (locale == null)
			throw new IllegalArgumentException("the value of the "
					+ CURRENT_LOCALE_NAME + " attribute should not be null");
		session.setAttribute(CURRENT_LOCALE_NAME, locale);
	}

	/**
	 * Gets the Locale bound to an HttpSession
	 * 
	 * @param session
	 *            An HttpSession
	 * @return The Locale bound to the session or null if no current Locale is
	 *         bound to the session
	 * @throws IllegalArgumentException
	 *             If session is null
	 */
	public static Locale getCurrentLocale(HttpSession session) {
		if (session == null)
			throw new IllegalArgumentException(
					"Cannot get an attribute of a null session");
		return (Locale) session.getAttribute(CURRENT_LOCALE_NAME);
	}

	/**
	 * Gets the Locale for an HttpServletRequest
	 * 
	 * @param request
	 *            An HttpServletRequest
	 * @return The Locale bound to the session or null if no current Locale is
	 *         bound to the session
	 * @throws IllegalArgumentException
	 *             If session is null
	 */
	public static Locale getCurrentLocale(HttpServletRequest request) {
		return getCurrentLocale(request.getSession(false));
	}

	/**
	 * Binds a UserProfile to an HttpSession
	 * 
	 * @param session
	 *            The HttpSession to which the UserProfile will be bound
	 * @param userProfile
	 *            The UserProfile that will be bound to the HttpSession
	 * @throws IllegalArgumentException
	 *             If session or userProfile is null
	 */
	public static void setUserProfile(HttpSession session,
			UserProfile userProfile) {
		if (session == null)
			throw new IllegalArgumentException(
					"Cannot set an attribute on a null session");
		if (userProfile == null)
			throw new IllegalArgumentException("the value of the "
					+ USER_PROFILE_NAME + " attribute should not be null");
		session.setAttribute(USER_PROFILE_NAME, userProfile);
	}

	/**
	 * Gets the UserProfile bound to an HttpSession
	 * 
	 * @param session
	 *            An HttpSession
	 * @return The UserProfile bound to the session or null if no UserProfile is
	 *         bound to the session
	 * @throws IllegalArgumentException
	 *             If session is null
	 */
	public static UserProfile getUserProfile(HttpSession session) {
		if (session == null)
			throw new IllegalArgumentException(
					"Cannot get an attribute of a null session");
		return (UserProfile) session.getAttribute(USER_PROFILE_NAME);
	}

	/**
	 * Gets the UserProfile bound to an HttpSession
	 * @return The UserProfile bound to the session or null if no UserProfile is
	 *         bound to the session
	 * @throws IllegalArgumentException
	 *             If session is null
	 */
	public static UserProfile getUserProfile(HttpServletRequest request) {
		return getUserProfile(request.getSession(false));
	}

	/**
	 * delegate method for UserProfile.getCataloguingView()
	 * 
	 */
	public static int getCataloguingView(HttpServletRequest request) {
		return getUserProfile(request.getSession(false)).getCataloguingView();
	}

	/**
	 * delegate method for SearchTypeBean.getCatalog()
	 * 
	 */
	public static Catalog getCatalog(HttpServletRequest request) {
		return SearchTypeBean.getInstance(request).getCatalog();
	}

	/**
	 * returns the currently active view to be used for searching
	 */
	public static int getSearchingView(HttpServletRequest request) {
		int result = SearchTypeBean.getInstance(request).getSearchingView();
		if (result > 0) {
			return getUserProfile(request.getSession(false))
					.getCataloguingView();
		} else {
			return result;
		}
	}

	
	
	/**
	 * pm 2011
	 * returns the currently active view to be used for searching
	 */
	public static int getSearchingView(HttpSession session) {
		Integer result = (Integer)session.getAttribute(CURRENT_SEARCHING_VIEW);
		if (result == null) {
			return getUserProfile(session)
					.getSearchingView();
		} else {
			return result.intValue();
		}
	}
	
	
	
	
	
	/**
	 * pm 2011
	 *  stores the current searching view in the session cache
	 * @param session
	 * @param view
	 */
	public static void setSearchingView(HttpSession session, int view) {
		session.setAttribute(CURRENT_SEARCHING_VIEW, new Integer(view));
	}
	
	
	/**
	 * delegate method for UserProfile.getDefaultRecordDisplay()
	 * 
	 */
	public static T_ITM_DSPLY getDefaultRecordDisplay(HttpServletRequest request) {
		return getUserProfile(request.getSession(false))
				.getDefaultRecordDisplay();
	}

	/**
	 * delegate method for UserProfile.getBranchLibrary()
	 * 
	 */
	public static int getUsersBranchLibrary(HttpServletRequest request) {
		return getUserProfile(request.getSession(false)).getBranchLibrary();
	}

	/**
	 * delegate method for UserProfile.getMainLibrary()
	 * 
	 */
	public static int getUsersMainLibrary(HttpServletRequest request) {
		return getUserProfile(request.getSession(false)).getMainLibrary();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public static void checkPermission(HttpServletRequest request,
			Permission aPermission) throws AuthorisationException {
		getUserProfile(request.getSession(false)).checkPermission(aPermission);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public static void checkPermission(HttpServletRequest request,
			Permission[] somePermissions) throws AuthorisationException {
		getUserProfile(request.getSession(false)).checkPermission(
				somePermissions);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public static void checkPermission(HttpServletRequest request,
			String permissionName) throws AuthorisationException {
		getUserProfile(request.getSession(false)).checkPermission(
				permissionName);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public static void checkPermission(HttpServletRequest request,
			String[] someNames) throws AuthorisationException {
		getUserProfile(request.getSession(false)).checkPermission(someNames);
	}

	/**
	 * @return a three-letter abbreviation for this locale's language
	 */
	public static String get3CharLocale(HttpSession session) {
		Locale l = getCurrentLocale(session);
		return l.getISO3Language();
	}

	/**
	 * MIKE: added for Mades
	 * @param request
	 * @return
	 */
	public static int getCataloguingViewForHeadings(HttpServletRequest request) {
		return getCataloguingView(request);
	}

	public static Map updateMap(HttpServletRequest request, Map properties)  {
		// Iterator of parameter names
		Enumeration names = request.getParameterNames();

		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();

			// Populate parameters, except "standard" struts attributes
			// such as 'org.apache.struts.action.CANCEL'
			if (!(name.startsWith("org.apache.struts."))) {
				properties.put(name, request.getParameter(name));
			}
		}
		return properties;
	}
	/**
	 * MIKE: normally if you create or edit a NameTitle if you wish to create 
	 * its name or title the HeadingBean is replaced in session without any control.
	 * This method help to fix the problem
	 */
	public static void popHeadingBean(HttpSession session) {
		HeadingBean headingBean = (HeadingBean)session.getAttribute(HEADING_BEAN_SESSION_NAME);
		if (headingBean != null && headingBean.hasPreviousHeadingBean()) {
			session.setAttribute(HEADING_BEAN_SESSION_NAME, headingBean.getPreviousHeadingBean());
			headingBean.removePreviousHeadingBean();
		} else {
			session.removeAttribute(HEADING_BEAN_SESSION_NAME);
		}
	}
}