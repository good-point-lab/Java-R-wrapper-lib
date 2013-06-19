
package core.prototype.hadoop;

import core.prototype.config.Application;
import core.prototype.config.ConfigUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class WordCountTest {

    Application app;

    @Test
    public void countWords() {
        String input = "/user/hduser/test1";
        String output = "/user/hduser/test1-output";
        app.getHdfs().deleteDirectory(output);
        app.getMapreduceWordCount().runJob(input, output);
        String file = app.getHdfs().readFile("/user/hduser/test1-output/part-00000");
        System.out.println(file);
    }

    public WordCountTest() {
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
