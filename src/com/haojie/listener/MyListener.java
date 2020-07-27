package com.haojie.listener;

import com.haojie.config.Config;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.beans.PropertyVetoException;

@WebListener()
public class MyListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    // Public constructor is required by servlet spec
    public MyListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------

    /**
     * To initiate the global datasource (connection pool) variable here after the context has initialized
     *
     * @param sce
     */
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
        ComboPooledDataSource comboPooledDataSource = null;
        try {

            //Set the properties of datasource.
            comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setDriverClass(Config.driverClassName);
            comboPooledDataSource.setUser(Config.username);
            comboPooledDataSource.setPassword(Config.password);
            comboPooledDataSource.setJdbcUrl(Config.jdbcURL);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        //Attach the datasource variable to the servlet context.
        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("dataSource", comboPooledDataSource);


    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
        sce.getServletContext().setAttribute("dataSource", null);
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attribute
         is replaced in a session.
      */
    }
}
