package com.odan.common.application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
 
public class ApplicationContextListener 
               implements ServletContextListener{
 
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("ServletContextListener destroyed");
	}
 
        //Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("ServletContextListener started");	
        Application.setServletContext(sce.getServletContext());
	}
}