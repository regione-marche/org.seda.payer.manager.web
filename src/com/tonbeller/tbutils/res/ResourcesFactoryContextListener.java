package com.tonbeller.tbutils.res;

import java.io.Serializable;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.jstl.core.Config;

import com.seda.commons.logger.CustomLoggerManager;
import com.seda.commons.logger.LoggerWrapper;


public class ResourcesFactoryContextListener implements ServletContextListener, Serializable {

	private static LoggerWrapper logger = CustomLoggerManager.get(ResourcesFactoryContextListener.class);

	  public void contextInitialized(ServletContextEvent e) {
	    try {
	      Locale fixedLocale = ResourcesFactory.instance().getFixedLocale();
	      // if appLocale is set, make locale available to JSTL tags
	      if (fixedLocale != null) {
	        logger.info("setting application locale to " + fixedLocale);
	        ServletContext sc = e.getServletContext();
	        Config.set(sc, Config.FMT_LOCALE, fixedLocale);
	      }
	    } catch (Exception ex) {
	      logger.error("Initialize Factory", ex);
	    }
	  }

	  public void contextDestroyed(ServletContextEvent e) {
	  }
}
