package core.prototype.config;

import core.prototype.dao.DaoParameters;
import core.prototype.dao.DataAccessApi;
import core.prototype.domain.DomainApi;
import core.prototype.rengine.REngineApi;
import core.prototype.rengine.RServeParameters;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    private static final Application SINGLETON = new Application();
    private AnnotationConfigApplicationContext applicationContext;
    private String propertiesFileRelativePath;
    private String workDirAbsolutePath;
    private String propertiesFileAbsolutePath;

    public void setWorkDirAbsolutePath(String workDirAbsolutePath) {
        this.workDirAbsolutePath = workDirAbsolutePath;
    }

    public void setPropertiesFileRelativePath(String propertiesFileRelativePath) {
        this.propertiesFileRelativePath = propertiesFileRelativePath;
    }

    public String getPropertiesFileAbsolutePath() {
        return propertiesFileAbsolutePath;
    }

    public AnnotationConfigApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Application getInstance() {
        return SINGLETON;
    }

    public void start() {
        this.propertiesFileAbsolutePath =
                ConfigUtils.appendPaths(this.workDirAbsolutePath, this.propertiesFileRelativePath);
        this.applicationContext = new AnnotationConfigApplicationContext();
        this.applicationContext.register(ApplicationConfig.class);
        this.applicationContext.refresh();
        this.applicationContext.registerShutdownHook();
    }

    public void stop() {
        if (this.applicationContext != null && this.applicationContext.isRunning()) {
            this.getREngine().stopRServer();
            this.applicationContext.close();
        }
    }

    /*
     * Domain
     */
    public DomainApi getDomain() {
        return (DomainApi) (this.applicationContext.getBean("domain"));
    }

    /*
     * R Engine Serevice
     */
    public REngineApi getREngine() {
        Properties props = this.getProperties(this.propertiesFileAbsolutePath);
        RServeParameters parameters = new RServeParameters();
        parameters.setIsLocal(true);
        String scriptsDirAbsPath = ConfigUtils.appendPaths(this.workDirAbsolutePath, (String) props.get("r.scripts.dir"));
        String tempDirAbsPath = ConfigUtils.appendPaths(this.workDirAbsolutePath, (String) props.get("r.temp.dir"));

        String rServerHost = (String) props.get("r.serve.host");
        String isRServerLocal = (String) props.get("r.serve.local");
        String rHomeDir = (String) props.get("r.serve.home.dir");
        ConfigUtils.createDirectory(tempDirAbsPath);
        parameters.setLocalRScriptsDirPath(scriptsDirAbsPath);
        parameters.setLocatTempDirPath(tempDirAbsPath);
        parameters.setIsLocal(isRServerLocal.equalsIgnoreCase("yes") || isRServerLocal.equalsIgnoreCase("true"));
        parameters.setHomeDirectory(rHomeDir);
        parameters.setHost(rServerHost);
        REngineApi r =
                (REngineApi) (this.applicationContext.getBean("rEngine", new Object[]{parameters}));
        return r;
    }


    /*
     * Data Access Module
     */
    public DataAccessApi getDao() {
        DaoParameters parameters = getDaoParameters(this.propertiesFileAbsolutePath);
        DataAccessApi dao = getDao(parameters);
        return dao;
    }

    public DataAccessApi getDao(DaoParameters parameters) {
        DataAccessApi dao =
                (DataAccessApi) (this.applicationContext.getBean("daoDynamic", new Object[]{parameters}));
        return dao;
    }

    public DataAccessApi getDaoStatic() {
        DataAccessApi dao =
                (DataAccessApi) (this.applicationContext.getBean("daoStatic"));
        return dao;
    }

    /*
     * Data Source for Data Access Module
     */
    public DataSource getDataSource() {
        DaoParameters parameters = getDaoParameters(this.propertiesFileAbsolutePath);
        DataSource ds = getDataSource(parameters);
        return ds;
    }

    public DataSource getDataSource(DaoParameters params) {
        String dbType = params.getDbType();
        Map<String, String> profile = params.getDaoProfiles().get(dbType);
        String dsName = profile.get("dataSourceProviderDynamic");
        DataSource ds = (DataSource) this.applicationContext.getBean(dsName, new Object[]{params});
        return ds;
    }

    /*
     * Database Configuration
     */
    public DaoParameters getDaoParameters() {
        DaoParameters parameters = new DaoParameters();
        PropertiesFileReader dbProperties = this.getProperties();
        Map<String, Map<String, String>> dbProfiles = getDbProfiles();
        parameters.setDbType(dbProperties.getDbType());
        parameters.setDriverClassName(dbProperties.getDriverClassName());
        parameters.setPassword(dbProperties.getPassword());
        parameters.setUrl(dbProperties.getUrl());
        parameters.setUserName(dbProperties.getUserName());
        parameters.setDaoProfiles(dbProfiles);
        return parameters;
    }

    public DaoParameters getDaoParameters(String filePath) {
        DaoParameters parameters = new DaoParameters();
        Properties props = this.getProperties(filePath);
        Map<String, Map<String, String>> dbProfiles = getDbProfiles();
        parameters.setDbType((String) props.get("db.type"));
        parameters.setDriverClassName((String) props.get("jdbc.driverClassName"));
        parameters.setPassword((String) props.get("jdbc.password"));
        parameters.setUrl((String) props.get("jdbc.url"));
        parameters.setUserName((String) props.get("jdbc.username"));
        String maxRowsProperty = (String) props.get("max.returned.rows");
        parameters.setMaxRows(Integer.parseInt(maxRowsProperty));
        parameters.setDaoProfiles(dbProfiles);
        return parameters;
    }

    /*
     * Properties Readers
     */
    /*
     * Properties red at load (configuration) time.
     */
    public PropertiesFileReader getProperties() {
        PropertiesFileReader props =
                (PropertiesFileReader) (this.applicationContext.getBean("propertiesFileReader"));
        return props;
    }

    public Properties getProperties(String filePath) {
        Properties props =
                (Properties) (this.applicationContext.getBean("fileSystemPropertiesReader", new Object[]{filePath}));
        return props;
    }

    public Map<String, Map<String, String>> getDbProfiles() {
        Map<String, Map<String, String>> profiles =
                (Map<String, Map<String, String>>) (this.applicationContext.getBean("daoProfiles"));
        return profiles;
    }


    /* 
     * An example of bean autowiring 
     */
    public String useAutowiredExampleBean(String arg) {
        BeanWithAutowiredDependencyExample bean = autowire(new BeanWithAutowiredDependencyExample(arg));
        return bean.printAutowiredProperty();
    }

    public <T> T autowire(T existingBean) {
        this.applicationContext.getAutowireCapableBeanFactory().autowireBean(existingBean);
        return existingBean;
    }
}

/*
 * References:
 * 1. Autowiring-prototypes http://i-proving.com/2010/06/23/Autowiring-prototypes/
 * 
**/