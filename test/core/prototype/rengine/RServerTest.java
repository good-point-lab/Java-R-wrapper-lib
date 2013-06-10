package core.prototype.rengine;

import core.prototype.config.ConfigUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class RServerTest {

    private Rserve server;

    @Test
    public void version() throws RserveException {
        RServeParameters parameters = new RServeParameters();
        parameters.setIsLocal(true);
        parameters.setHomeDirectory("/Library/Frameworks/R.framework/Versions/3.0/Resources");
        server = new Rserve(parameters);
        server.startUpRServer();
        RConnection con = server.connect();
        if (con != null) {
            con.close();
        }
    }

    public RServerTest() {
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
        ConfigUtils.initLogging(logConfigFile);
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
