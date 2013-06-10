package core.prototype.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/*
 * In-memory store of the DBs configuration parameters.
 */
@Component
public class DaoProfilesConfig {

    @Bean(name = "daoProfiles")
    public Map<String, Map<String, String>> daoProfiles() {
        Map<String, Map<String, String>> profiles = new HashMap<String, Map<String, String>>();

        /* Oracle Family */
        /* Oracle Common */
        Map<String, String> profile = new HashMap<String, String>();
        profile.put("daoImplementation", "daoOracle");
        profile.put("dataSourceProvider", "dbcpDataSource");
        profile.put("hibernateDialect", "org.hibernate.dialect.OracleDialect");
        profile.put("preferredTestQuery", "");
        profiles.put("oracle", profile);
        /* Oracle 9i */
        profile = new HashMap<String, String>();
        profiles.put("oracle_9i", profile);
        /* Oracle 10g */
        profile = new HashMap<String, String>();
        profiles.put("oracle_10g", profile);

        /* MySQL Family */
        /* MySQL common */
        profile = new HashMap<String, String>();
        profile.put("daoImplementation", "daoMySql");
        profile.put("dataSourceProvider", "dbcpDataSource");
        profile.put("ormSessionFactory", "hibernateSessionFactory");
        profile.put("daoImplementationDynamic", "daoMySqlDynamic");
        profile.put("dataSourceProviderDynamic", "dbcpDataSourceDynamic");
        profile.put("ormSessionFactoryDynamic", "hibernateSessionFactoryDynamic");
        profile.put("hibernateDialect", "org.hibernate.dialect.MySQLDialect");
        profile.put("preferredTestQuery", "SELECT 1");
        profiles.put("mysql", profile);
        /* My SQL with MyISAM */
        profile = new HashMap<String, String>();
        profiles.put("mysql_With_MyISAM", profile);
        /* My SQL with InnoDB */
        profile = new HashMap<String, String>();
        profiles.put("mysql_With_InnoDB", profile);

        /* MS SQL Server */
        profile = new HashMap<String, String>();
        profile.put("daoImplementation", "daoSqlServer");
        profile.put("dataSourceProvider", "dbcpDataSource");
        profile.put("dataSourceProviderDynamic", "dbcpDataSourceDynamic");
        profile.put("hibernateDialect",
                "org.hibernate.dialect.SQLServerDialect");
        profile.put("preferredTestQuery", "SELECT CURRENT_TIMESTAMP");
        profiles.put("sql_server", profile);

        /* Other DB products; not in use, but enumerated as an example */
        /* Informix */
        profile = new HashMap<String, String>();
        profiles.put("informix", profile);

        /* IBM DB2 */
        profile = new HashMap<String, String>();
        profiles.put("db2", profile);

        /* PostgreSql */
        profile = new HashMap<String, String>();
        profiles.put("postgresql", profile);

        return profiles;
    }
}
