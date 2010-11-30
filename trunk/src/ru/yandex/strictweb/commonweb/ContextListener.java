package ru.yandex.strictweb.commonweb;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
	
	public static ServletContext context;
	public static String rootPath = "";
	
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		rootPath = context.getRealPath("");
	}

	public void contextDestroyed(ServletContextEvent event) {
	}	
}
