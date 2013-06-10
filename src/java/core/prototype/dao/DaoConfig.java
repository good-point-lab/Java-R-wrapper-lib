package core.prototype.dao;

import core.prototype.config.PropertiesFileReader;
import core.prototype.dao.entities.RIrisDataSet;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

@Configuration
public class DaoConfig {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    @Resource(name = "daoProfiles")
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<String, Map<String, String>> daoProfiles;
    @Autowired
    private PropertiesFileReader dbProperties;

    /*
     * Static (load time) configuration
     */
    @Autowired
    @Bean(name = "daoStatic")
    public DataAccessApi daoStatic() {
        String dbType = this.dbProperties.getDbType();
        Map<String, String> profile = this.daoProfiles.get(dbType);
        String daoBeanName = profile.get("daoImplementation");
        DataAccessApi dao = (DataAccessApi) this.applicationContext.getBean(daoBeanName);
        return dao;
    }

    @Bean(name = {"daoMySql"})
    DataAccessApi daoMySql() {
        DataSource ds = (DataSource) this.applicationContext.getBean("dataSource");
        SessionFactory sf = (SessionFactory) this.applicationContext.getBean("ormSessionFactory");
        int maxRows = this.dbProperties.getMaxRows();
        DataAccessApi dao = new DataAccessMySql(ds, sf, maxRows);
        return dao;
    }

    @Bean(name = "ormSessionFactory")
    SessionFactory ormSessionFactory() {
        String dbType = this.dbProperties.getDbType();
        Map<String, String> profile = this.daoProfiles.get(dbType);
        String factoryBeanName = profile.get("ormSessionFactory");
        SessionFactory sf = (SessionFactory) this.applicationContext.getBean(factoryBeanName);
        return sf;
    }

    @Bean(name = "dataSource")
    DataSource dataSource() {
        String dbType = this.dbProperties.getDbType();
        Map<String, String> profile = this.daoProfiles.get(dbType);
        String dsBeanName = profile.get("dataSourceProvider");
        DataSource ds = (DataSource) this.applicationContext.getBean(dsBeanName);
        return ds;
    }

    @Bean(name = "dbcpDataSource")
    @Qualifier("dbcpDataSource ")
    DataSource dbcpDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(this.dbProperties.getDriverClassName());
        ds.setUrl(this.dbProperties.getUrl());
        ds.setUsername(this.dbProperties.getUserName());
        ds.setPassword(this.dbProperties.getPassword());
        return ds;
    }

    @Bean(name = "hibernateSessionFactory")
    @Qualifier("hibernateSessionFactory")
    SessionFactory hibernateSessionFactory() {
        LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
        String dbType = this.dbProperties.getDbType();
        Map<String, String> profile = this.daoProfiles.get(dbType);
        String dsBeanName = profile.get("dataSourceProvider");
        DataSource ds = (DataSource) this.applicationContext.getBean(dsBeanName);
        sfb.setDataSource(ds);
        sfb.setAnnotatedClasses(annotatedClasses);
        Properties hp = sfb.getHibernateProperties();
        String dialect = profile.get("hibernateDialect");
        hp.setProperty("hibernate.dialect", dialect);
        hp.setProperty("hibernate.show_sql", "true");

        sfb.setHibernateProperties(hp);
        try {
            sfb.afterPropertiesSet();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return sfb.getObject();
    }

    @Bean(name = "hibernateSessionFactoryDirect", destroyMethod = "close")
    SessionFactory getHibernateSessionFactoryDirect() {
        org.hibernate.cfg.Configuration conf =
                (org.hibernate.cfg.Configuration) this.applicationContext.getBean("annotationConfiguration");
        Properties properties = conf.getProperties();
        ServiceRegistryBuilder builder = new ServiceRegistryBuilder();
        ServiceRegistry serviceRegistry = builder.applySettings(properties).buildServiceRegistry();
        SessionFactory sessionFactory = conf.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    @Bean(name = "annotationConfiguration")
    org.hibernate.cfg.Configuration annotationConfiguration() {
        org.hibernate.cfg.Configuration cfg =
                new org.hibernate.cfg.Configuration();
        String dbType = this.dbProperties.getDbType();
        Map<String, String> profile = this.daoProfiles.get(dbType);
        cfg.setProperty("hibernate.dialect", profile.get(
                "hibernateDialect"));
        cfg.setProperty("hibernate.connection.driver_class",
                this.dbProperties.getDriverClassName());
        cfg.setProperty("hibernate.connection.url", this.dbProperties.getUrl());
        cfg.setProperty("hibernate.connection.username", this.dbProperties.getUserName());
        cfg.setProperty("hibernate.connection.password", this.dbProperties.getPassword());
        cfg.setProperty("hibernate.show_sql", "true");
        // set more properties if needed, e.g.
        cfg.setProperty("hibernate.c3p0.max_size", "20");

        for (Class<?> aClass : annotatedClasses) {
            cfg.addAnnotatedClass(aClass);
        }
        return cfg;
    }


    /* 
     * Dynamic (run time) configuration
     */
    @Bean(name = "daoDynamic")
    @Scope("prototype")
    public DataAccessApi daoDynamic(DaoParameters params) {
        String dbType = params.getDbType();
        Map<String, String> profile = params.getDaoProfiles().get(dbType);
        String daoBeanName = profile.get("daoImplementationDynamic");
        DataAccessApi dao = (DataAccessApi) this.applicationContext.getBean(daoBeanName, new Object[]{params});
        return dao;
    }

    @Bean(name = {"daoMySqlDynamic"})
    @Scope("prototype")
    DataAccessApi daoMySqlDynamic(DaoParameters params) {
        String dbType = params.getDbType();
        Map<String, String> profile = params.getDaoProfiles().get(dbType);
        String dsName = profile.get("dataSourceProviderDynamic");
        DataSource ds = (DataSource) this.applicationContext.getBean(dsName, new Object[]{params});
        String ormProvider = profile.get("ormSessionFactoryDynamic");
        SessionFactory sf = (SessionFactory) this.applicationContext.getBean(ormProvider, new Object[]{params});
        int maxRows = params.getMaxRows();
        DataAccessApi dao = new DataAccessMySql(ds, sf, maxRows);
        return dao;
    }

    @Bean(name = "dbcpDataSourceDynamic", destroyMethod = "close")
    @Scope("prototype")
    DataSource dbcpDataSourceDynamic(DaoParameters params) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(params.getDriverClassName());
        ds.setUrl(params.getUrl());
        ds.setUsername(params.getUserName());
        ds.setPassword(params.getPassword());
        return ds;
    }

    @Bean(name = "hibernateSessionFactoryDynamic")
    @Scope("prototype")
    SessionFactory hibernateSessionFactoryDynamic(DaoParameters params) {
        String dbType = params.getDbType();
        Map<String, String> profile = params.getDaoProfiles().get(dbType);
        LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
        String dsName = profile.get("dataSourceProviderDynamic");
        DataSource ds = (DataSource) this.applicationContext.getBean(dsName, new Object[]{params});
        sfb.setDataSource(ds);
        sfb.setAnnotatedClasses(annotatedClasses);
        Properties hp = sfb.getHibernateProperties();
        String dialect = profile.get("hibernateDialect");
        hp.setProperty("hibernate.dialect", dialect);
        hp.setProperty("hibernate.show_sql", "true");
        sfb.setHibernateProperties(hp);
        try {
            sfb.afterPropertiesSet();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return sfb.getObject();
    }
    private static final Class[] annotatedClasses;

    static {
        annotatedClasses =
                new Class[]{
            RIrisDataSet.class
        // annotated domain classes should be listed here
        };
    }
}
