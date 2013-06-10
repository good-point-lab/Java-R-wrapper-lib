package core.prototype.dao;

import core.prototype.dao.entities.RIrisDataSet;
import core.prototype.domain.DataPage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class DataAccessBase implements DataAccessApi {

    private final Logger logger = LoggerFactory.getLogger(DataAccessBase.class);
    protected JdbcTemplate jdbcTemplate;
    protected NamedParameterJdbcTemplate npJdbcTemplate;
    protected TransactionTemplate transactionTemplate;
    protected DataSource dataSource;
    protected SessionFactory sessionFactory;
    protected int maxRows;

    protected JdbcTemplate getJdbcTemplate() {
        if (this.jdbcTemplate == null) {
            this.jdbcTemplate = new JdbcTemplate(this.dataSource);
            this.jdbcTemplate.setMaxRows(this.maxRows);
        }
        return this.jdbcTemplate;
    }

    protected NamedParameterJdbcTemplate getNpJdbcTemplate() {
        if (this.npJdbcTemplate == null) {
            this.npJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
            ((JdbcTemplate) this.npJdbcTemplate.getJdbcOperations()).setMaxRows(this.maxRows);
        }
        return this.npJdbcTemplate;
    }

    protected TransactionTemplate getTransactionTemplate() {
        if (this.transactionTemplate == null) {
            PlatformTransactionManager transactionManager = new DataSourceTransactionManager(this.dataSource);
            this.transactionTemplate = new TransactionTemplate(transactionManager);
        }
        return this.transactionTemplate;
    }

    protected class RowMapperInstanse implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    //  API Methods
    @Override
    public int getTableRowCount() {
        return getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM DATA_SET", Integer.class);
    }

    @Override
    public <T> DataPage getTablePage(DataPage page, Class<T> clazz) {
        return getPageWithHibernateORM(page, clazz);
    }

    private <T> DataPage getPageWithHibernateORM(DataPage page, Class<T> clazz) {
        int firstResult = page.getStartRow();
        int maxResults = page.getRowsPerPage();
        String sortedColumn = page.getSortColumn();
        String sortDirection = page.getSortDirection();
        List<T> list = null;
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(clazz);
        criteria.setFirstResult(firstResult);
        criteria.setMaxResults(maxResults);
        if (sortDirection != null && sortDirection.equals("asc")) {
            criteria.addOrder(Order.asc(sortedColumn));
        } else if (sortDirection != null && sortDirection.equals("desc")) {
            criteria.addOrder(Order.desc(sortedColumn));
        }
        criteria.add(Restrictions.like("species", "%"));
        try {
            list = criteria.list();
        } catch (HibernateException e) {
            throw SessionFactoryUtils.convertHibernateAccessException(e);
        } finally {
            SessionFactoryUtils.closeSession(session);
        }
        page.setRows(list);
        return page;
    }

    @Override
    public void saveIrisDataset(final List<RIrisDataSet> data) {
        final String sqlInsert =
                "INSERT INTO DATA_SET (SEPAL_LENGTH, SEPAL_WIDTH, PETAL_LENGTH, PETAL_WIDTH, SPECIES) VALUES(:sepalLength, :sepalWidth, :petalLength, :petalWidth, :species)";
        getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    for (RIrisDataSet obj : data) {
                        SqlParameterSource par = new BeanPropertySqlParameterSource(obj);
                        getNpJdbcTemplate().update(sqlInsert, par);
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    status.setRollbackOnly();
                }
            }
        });
    }

    @Override
    public void deleteIrisDataset() {
        final String sqlDelete = "DELETE FROM DATA_SET";
        getJdbcTemplate().update(sqlDelete);
    }
}
