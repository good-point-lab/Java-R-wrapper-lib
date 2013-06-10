package core.prototype.web;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import core.prototype.config.Application;
import core.prototype.config.ConfigUtils;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Initializer implements WebApplicationInitializer {

    private static final String relativePathToConfigDir = "/WEB-INF/config/";

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {

        String contextRootPath = servletContext.getRealPath("");
        String logConfigFile = ConfigUtils.appendPaths(
                ConfigUtils.appendPaths(contextRootPath, relativePathToConfigDir), "logback.xml");
        ConfigUtils.initLogging(logConfigFile);
        String propertiesFileRelativePath = ConfigUtils.appendPaths(relativePathToConfigDir, "general.properties");

        Application app = Application.getInstance();
        app.setWorkDirAbsolutePath(contextRootPath);
        app.setPropertiesFileRelativePath(propertiesFileRelativePath);
        app.start();

        AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        root.register(WebConfig.class);
        servletContext.addListener(new ContextLoaderListener(root));
        servletContext.addListener(new AppliacationShutdownHandler());
        root.registerShutdownHook();

        AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
        mvcContext.register(MvcConfig.class);

        // Sprint MVC
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "dispatcher", new DispatcherServlet(mvcContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // REST with JERSEY
        dispatcher = servletContext.addServlet(
                "JAX-RS REST Servlet", new ServletContainer());
        dispatcher.setLoadOnStartup(2);
        dispatcher.addMapping("/services/*");


    }
}

class AppliacationShutdownHandler
        implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Application app = Application.getInstance();
        app.stop();
    }
}