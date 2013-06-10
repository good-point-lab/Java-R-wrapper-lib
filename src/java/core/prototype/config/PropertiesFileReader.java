package core.prototype.config;

import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/*
 * Reads properties files
 */
@Component
public class PropertiesFileReader {

    private static final String DEFAULT_PROPERTIES_FILE_ON_CLASSPATH = "general.properties";
    @Autowired
    private ApplicationContext applicationContext;
    // Database type.  Set in the properties file or at runtime.
    @Value("#{propertiesReader['db.type']?:''}")
    private String dbType;
    // JDBC parameters
    @Value("#{propertiesReader['jdbc.driverClassName']?:'com.mysql.jdbc.Driver'}")
    private String driverClassName;
    @Value("#{propertiesReader['jdbc.url']?:'jdbc:mysql://localhost:3306/some_db'}")
    private String url;
    @Value("#{propertiesReader['jdbc.username']?:''}")
    private String userName;
    @Value("#{propertiesReader['jdbc.password']?:''}")
    private String password;
    @Value("#{propertiesReader['max.returned.rows']?:'0'}")
    private int maxRows;
    // Jakarta's Commons DBCP pooled data source
    @Value("#{propertiesReader['dbcp.maxActive']?:'8'}")
    private int dbcpMaxActive;
    @Value("#{propertiesReader['dbcp.dbcp.maxIdle']?:'8'}")
    private int dbcpMaxIdle;
    @Value("#{propertiesReader['dbcp.maxWait']?:'-1'}")
    private int dbcpMaxWait;
    // Hibernate Configuration
    @Value("#{propertiesReader['hibernate.show_sql']?:'false'}")
    private String hibernateShowSql;

    @Qualifier("propertiesReader")
    @Bean(name = "propertiesReader")
    public Properties propertiesReader() {
        Properties properties = null;
        PropertiesFactoryBean pfb = new PropertiesFactoryBean();
        String file = DEFAULT_PROPERTIES_FILE_ON_CLASSPATH;
        ClassPathResource resources = new ClassPathResource(file);
        pfb.setLocation(resources);
        try {
            pfb.afterPropertiesSet();
            properties = (Properties) pfb.getObject();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return properties;
    }

    @Bean(name = "fileSystemPropertiesReader")
    @Scope("prototype")
    Properties fileSystemPropertiesReader(String filePath) {
        Properties properties = null;
        PropertiesFactoryBean pfb = new PropertiesFactoryBean();
        Resource[] resources = new FileSystemResource[]{new FileSystemResource(filePath)};
        pfb.setLocations(resources);
        pfb.setIgnoreResourceNotFound(true);
        try {
            pfb.afterPropertiesSet();
            properties = (Properties) pfb.getObject();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return properties;
    }

    @Bean(name = "classPathPropertiesReader")
    @Scope("prototype")
    Properties classPathPropertiesReader(String fileName) {
        Properties properties = null;
        PropertiesFactoryBean pfb = new PropertiesFactoryBean();
        ClassPathResource resources = new ClassPathResource(fileName);
        pfb.setLocation(resources);
        pfb.setIgnoreResourceNotFound(true);
        try {
            pfb.afterPropertiesSet();
            properties = (Properties) pfb.getObject();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return properties;
    }

    public String getDbType() {
        return dbType;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getDbcpMaxActive() {
        return dbcpMaxActive;
    }

    public int getDbcpMaxIdle() {
        return dbcpMaxIdle;
    }

    public int getDbcpMaxWait() {
        return dbcpMaxWait;
    }

    public String getHibernateShowSql() {
        return hibernateShowSql;
    }

    public int getMaxRows() {
        return maxRows;
    }
}
