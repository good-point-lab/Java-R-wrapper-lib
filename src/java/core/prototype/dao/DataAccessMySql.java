package core.prototype.dao;

import javax.sql.DataSource;
import org.hibernate.SessionFactory;

public class DataAccessMySql extends DataAccessBase {

    public DataAccessMySql(DataSource dataSource, SessionFactory sessionFactory, int maxRows) {
        super.dataSource = dataSource;
        super.sessionFactory = sessionFactory;
        super.maxRows = maxRows;
    }
}