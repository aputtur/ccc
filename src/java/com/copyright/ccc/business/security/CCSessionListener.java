package com.copyright.ccc.business.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.business.services.SystemStatus;
import com.copyright.ccc.business.services.rlnk.RlnkRightsServices;
import com.copyright.ccc.web.WebConstants;

/**
 * Listens for C.com session start and destroy events.
 * As of 9/26, the only action this Listener takes is to
 * terminate to RL session when the c.com session is destroyed.
 * 
 * @author jarbo
 *
 */
public class CCSessionListener implements HttpSessionListener {
    protected static final Logger _logger = Logger.getLogger( CCSessionListener.class );

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener
	 */
	public void sessionCreated(HttpSessionEvent event) {
    	event.getSession().setAttribute(WebConstants.SessionKeys.SESSION_IS_NEWLY_CREATED, Boolean.TRUE);
		if (_logger.isDebugEnabled()) {
			_logger.debug("new c.com session started with id " + event.getSession().getId());
		}
	}

	/**
	 * Attempts to terminate the related Rightlink session if there is one.
	 * If an exception is raised, it will be logged and the method call will
	 * fail silently.
	 * <p>
	 * This method will check to see if RL is down prior to making
	 * the call.
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		if (SystemStatus.isRightslinkUp()) {
			Object rlSessionId = event.getSession().getAttribute(WebConstants.SessionKeys.RIGHTSLINK_SESSION_ID);
			try {
				if (rlSessionId!=null) {
					boolean result = RlnkRightsServices.removeSession( (String) rlSessionId );
					if (!result) {
						_logger.error("attempt to remove the associated RL Session returned false " + rlSessionId);
					}
				}
			} catch (RuntimeException t) {
				_logger.error("error while calling RL to remove RL session " + rlSessionId);
				_logger.error(ExceptionUtils.getFullStackTrace(t));
			}
		}
	}
}
