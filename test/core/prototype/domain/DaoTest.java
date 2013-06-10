package core.prototype.domain;

import core.prototype.config.Application;
import core.prototype.config.ConfigUtils;
import core.prototype.config.PropertiesFileReader;
import core.prototype.dao.entities.RIrisDataSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class DaoTest {

    private static Application app;

    @Ignore
    public void connection() throws SQLException {

        app = Application.getInstance();
        DataSource ds = app.getDataSource();
        Connection con = ds.getConnection();
        System.out.println("Connection object = " + con.toString());
        PropertiesFileReader prop = app.getProperties();
        System.out.println("driver class " + prop.getDriverClassName());
    }

    @Ignore
    public void dao() throws SQLException {
        app = Application.getInstance();
        int cnt1 = app.getDaoStatic().getTableRowCount();
        System.out.println("Rows (static config DAO) " + cnt1);
        int cnt2 = app.getDao().getTableRowCount();
        System.out.println("Rows (run time config DAO) " + cnt2);

        DataPage page = new DataPage();
        page.setSortColumn("id");
        page.setSortDirection("asc");
        page.setRowsPerPage(20);
        page.setStartRow(0);
        page = app.getDao().getTablePage(page, RIrisDataSet.class);
        List<RIrisDataSet> rows = page.getRows();
        for (Object row : rows) {
            long id = ((RIrisDataSet) row).getId();
            String entry = ((RIrisDataSet) row).getSpecies();
            System.out.println(id + "  " + entry);
        }
    }

    @Test
    public void loadIris() {
        app.getDomain().loadIrisDatasetToDb();
    }

    public DaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        app = Application.getInstance();
        String userDir = System.getProperty("user.dir");
        String workDirAbsPath = ConfigUtils.appendPaths(userDir, "web");
        String configDirRelativePath = "/WEB-INF/config/";
        String logConfigFile = ConfigUtils.appendPaths(
                ConfigUtils.appendPaths(workDirAbsPath, configDirRelativePath), "logback.xml");

        String propertiesFileRelativePath = ConfigUtils.appendPaths(configDirRelativePath, "general.properties");
        ConfigUtils.initLogging(logConfigFile);
        app = Application.getInstance();
        app.setWorkDirAbsolutePath(workDirAbsPath);
        app.setPropertiesFileRelativePath(propertiesFileRelativePath);
        app.start();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
