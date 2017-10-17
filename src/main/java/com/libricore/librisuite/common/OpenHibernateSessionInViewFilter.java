/*
 * Created on Oct 13, 2004
 *
 */
package com.libricore.librisuite.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.hibernate.HibernateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class comment
 * @author janick
 */
public class OpenHibernateSessionInViewFilter implements Filter {
	private static Log logger = LogFactory.getLog(OpenHibernateSessionInViewFilter.class);
	private HibernateSessionProvider provider = null;


	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		try {
			provider = HibernateSessionProvider.getInstance();
		} catch (HibernateException ex) {
			logger.error("Filter Init Failed: P.S. Check if there are some hibernate configuration files missing or wrong", ex);
			throw new ServletException(ex);
		} catch (NoClassDefFoundError e) {
			logger.error("Filter Init Failed: P.S. Possible default missing for class 'not loaded'. See defaultValues.properties", e);
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(
		ServletRequest request,
		ServletResponse response,
		FilterChain chain)
		throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} finally {
			try {
				provider.closeSession();
			} catch (HibernateException ex) {
				logger.error("Could not close hibernate session", ex);
				throw new ServletException(ex);
			}
		}

	}
	


	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// nothing
	}

}
