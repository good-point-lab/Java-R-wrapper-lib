package core.prototype.hadoop;

import core.prototype.config.Application;
import core.prototype.config.ConfigUtils;
import core.prototype.hadoop.hbase.HbaseDelegateApi;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HbaseConnectionTest {

    Application app;
    HbaseDelegateApi hb;

    @Test
    public void connect() {
        String desc = hb.getDescriptor("table1");
        System.out.println(desc);
        String list = hb.listTables();
        System.out.println(list);
    }

    public HbaseConnectionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

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
        hb = app.getHbase();
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
