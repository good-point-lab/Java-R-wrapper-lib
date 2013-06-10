package core.prototype.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import java.io.File;
import java.net.URI;
import org.slf4j.LoggerFactory;

public final class ConfigUtils {

    public static void initLogging(String logConfigFile) {
        LoggerContext lctx = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(lctx);
        lctx.reset();
        try {
            File f = new File(logConfigFile);
            jc.doConfigure(f);
        } catch (JoranException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String appendPaths(String iPath1, String iPath2) {
        File file1 = new File(iPath1);
        File file2 = new File(file1, iPath2);
        return file2.getPath();
    }

    /*
     * Some details at http://stackoverflow.com/questions/1131273/java-file-touri-tourl-on-windows-file
     */
    public static String getAbsoluteFilePath(String realPath, String fileName) {
        File file = new File(realPath, fileName);
        URI uri = file.toURI();
        String path = uri.getPath();      
        if (System.getProperty("os.name").contains("Win")) {
            path = path.substring(1);
        }       
        return path;    
    }

    public static void deleteFile(String realFilePath, String fileName) {
        File file = new File(realFilePath, fileName);
        file.delete();
    }

    public static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean success = dir.mkdir();
            if (!success) {
                throw new RuntimeException("Can not craete temporary directory");
            }
        }
    }
}
