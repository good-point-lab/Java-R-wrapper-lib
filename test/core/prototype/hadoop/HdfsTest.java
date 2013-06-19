/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.prototype.hadoop;

import core.prototype.config.Application;
import core.prototype.config.ConfigUtils;
import core.prototype.hadoop.hdfs.HdfsDelegateApi;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author agreysukh
 */
public class HdfsTest {

    Application app;
    HdfsDelegateApi hf;
    
    @Test
    public void roperty () {       
       String file =  hf.readFile("/user/hduser/test1-output/part-00000");
       System.out.println(file);
    }

    public HdfsTest() {
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
        hf = app.getHdfs();
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
