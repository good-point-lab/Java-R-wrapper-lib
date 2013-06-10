package core.prototype.domain;

import core.prototype.config.Application;
import core.prototype.config.ConfigUtils;
import core.prototype.rengine.REngineApi;
import core.prototype.utils.ObjectUtils;
import core.prototype.utils.TableFormatter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class REngineTest {

    Application app;
    REngineApi re;

    @Ignore
    public void version() {
        String version = re.getRVersion();
        System.out.println("R version " + version);
    }

    @Test
    public void plotIris() {
        String imageName = "iris-data-plot.jpg";
        String scriptName = "iris-data-plot.R";
        String absPathToImageDir = re.getLocalTempDirPath();
        String absPathToScriptDir = re.getLocalRScriptsDirPath();
        String absScriptPath = ConfigUtils.getAbsoluteFilePath(absPathToScriptDir, scriptName);
        String absImagePath = ConfigUtils.getAbsoluteFilePath(absPathToImageDir, imageName);
        Map<String, Object> vars = new HashMap();
        vars.put("fn", absScriptPath);
        vars.put("plotType", "h");
        vars.put("imagePath", absImagePath);
        byte[] imageData = re.executeScriptForGraphics(vars);
        String encoded = new sun.misc.BASE64Encoder().encode(imageData);
        System.out.println("Encoded grafics bit stream \n" + encoded);
    }

    @Ignore
    public void plotFrame() {
        String imageName = "frame-data-plot.jpg";
        String scriptName = "frame-data-plot.R";
        String absPathToImageDir = re.getLocalTempDirPath();
        String absPathToScriptDir = re.getLocalRScriptsDirPath();
        String absScriptPath = ConfigUtils.getAbsoluteFilePath(absPathToScriptDir, scriptName);
        String absImagePath = ConfigUtils.getAbsoluteFilePath(absPathToImageDir, imageName);
        Map<String, Object> frame = new MockDataFrame().getFrame();
        Map<String, Object> vars = new HashMap();
        vars.put("frame", frame);
        vars.put("fn", absScriptPath);
        vars.put("plotType", "p");
        vars.put("imagePath", absImagePath);
        byte[] imageData = re.executeScriptForGraphics(vars);
        String encoded = new sun.misc.BASE64Encoder().encode(imageData);
        System.out.println("Encoded grafics bit stream \n" + encoded);
    }

    @Ignore
    public void frame() {
        Map<String, Object> map = re.getIrisDataset();
        Object[][] tabO = ObjectUtils.convertFrameMapToArray(map);
        String[][] tabS = ObjectUtils.formatArrayToStringsWithRowNumbers(tabO);
        PrintStream out = System.out;
        final TableFormatter printer = new TableFormatter(out);
        printer.print(tabS);
    }

    public REngineTest() {
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
        re = app.getREngine();
    }

    @After
    public void tearDown() {
    }
}
