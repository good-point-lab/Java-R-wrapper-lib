package core.prototype.rengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.BindException;
import java.net.ServerSocket;
import org.rosuda.REngine.Rserve.RConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rserve {

    private static final Logger logger = LoggerFactory.getLogger(Rserve.class);
    private final static String DEFAULT_RSERVE_HOST = "127.0.0.1";
    private final static int MIN_RSERVE_VERSION = 103;
    private String host;
    private int port;
    private String login;
    private String password;
    private boolean isLocal;
    private String rHomeDir;

    public Rserve() {
    }

    public Rserve(RServeParameters parameters) {
        this();
        this.host = parameters.getHost();
        this.port = parameters.getPort();
        this.login = parameters.getLogin();
        this.password = parameters.getPassword();
        this.rHomeDir = parameters.getHomeDirectory();
        this.isLocal = parameters.isIsLocal();
    }

    /* 
     * Get a new connection
     */
    public RConnection connect() {
        return connect(this.host, this.port, this.login, this.password);
    }

    private RConnection connect(String host, int port, String login, String password) {
        RConnection connection = null;
        int attempts = 5;
        int delay = 1000; // in ms
        while (attempts > 0) {
            try {
                if (host == null || (host != null && host.equals(DEFAULT_RSERVE_HOST))) {
                    if (port > 0) {
                        connection = new RConnection(DEFAULT_RSERVE_HOST, port);
                    } else {
                        connection = new RConnection();
                    }
                    if (connection.needLogin()) {
                        connection.login(login, password);
                    }
                } else {
                    if (port > 0) {
                        connection = new RConnection(host, port);
                    } else {
                        connection = new RConnection(host);
                    }
                    if (connection.needLogin()) {
                        connection.login(login, password);
                    }
                }
                return connection;
            } catch (Exception e) {
                if (connection != null) {
                    connection.close();
                }
                logger.error("Try failed with: " + e.getMessage());
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ix) {
                logger.error(ix.getMessage());
            }
            attempts--;
        }
        return connection;
    }

    public boolean isRServerRunning() {
        RConnection c = null;
        try {
            c = new RConnection();
            return true;
        } catch (Exception e) {
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return false;
    }

    /*
     * Start or stop R Server (if it is insalled locally)
     */
    public void startUpRServer() {
        if (!this.isLocalHost()) {
            throw new UnsupportedOperationException("Not authorized to stop a remote R daemon: " + formatConfigurationSummary());
        }
        if (!isRHomeDirExists(this.rHomeDir)) {
            throw new IllegalArgumentException("R home directory does not set correctly.\n");
        }

        String execPath = this.rHomeDir + File.separator + "bin" + File.separator + "R" + (System.getProperty("os.name").contains("Win") ? ".exe" : "");
        boolean isServerInstalled = Rserve.isRServerInstalled(execPath);

        if (!isServerInstalled) {
            String notice = "Please install Rserve manually in your R environment using \"install.packages('Rserve')\" command.";
            logger.error(notice);
            return;
        } else {
            logger.info("Local R server is installed");
        }

        logger.info("Starting R process... "
                + "RURL_START"
                + (this.login != null ? (this.login + ":"
                + this.password + "@") : "")
                + (this.host == null ? DEFAULT_RSERVE_HOST : this.host)
                + (this.port > 0 ? ":" + this.port : ""));

        StringBuilder RserveArgs = new StringBuilder("--no-save --slave");
        if (this.port > 0) {
            RserveArgs.append(" --RS-port ").append(this.port);
        }

        String cmd = this.rHomeDir + File.separator + "bin" + File.separator + "" + File.separator + "R"
                + (System.getProperty("os.name").contains("Win") ? ".exe" : ""); 

        boolean started = launchRServerProcess(cmd, "--no-save --slave", RserveArgs.toString(), false);
        if (started) {
            logger.info("Launch is ok, R server is ready");
        } else {
            logger.error("Start is failed");
        }
    }

    public void stopRServer() {
        logger.info("Stopping R daemon... " + formatConfigurationSummary());
        if (!this.isLocalHost()) {
            throw new UnsupportedOperationException("Not authorized to stop a remote R daemon: " + formatConfigurationSummary());
        }
        try {
            RConnection cn = new RConnection();
            if (cn == null || (cn != null && !cn.isConnected())) {
                logger.info("R daemon already stoped.");
                return;
            }
            cn.shutdown();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        logger.info("R daemon stoped.");
    }

    public boolean launchRServer(String cmd) {
        return launchRServerProcess(cmd, "--no-save --slave", "--no-save --slave", false);
    }

    public boolean launchRServerProcess(String cmd, String rargs, String rsrvargs, boolean debug) {
        logger.info("Waiting for Rserver to start ...");
        boolean startRserve = executeSystemCommand(
                "library("
                + "Rserve);Rserve("
                + (debug ? "TRUE" : "FALSE") + ",args='"
                + rsrvargs
                + "')",
                cmd, rargs, null, null);
        if (startRserve) {
            logger.info("Rserver startup done, let us try to connect ...");
        } else {
            logger.info("Failed to start Rserve process.");
            return false;
        }

        int port;
        if (rsrvargs.contains("--RS-port")) {
            String rsport = rsrvargs.split("--RS-port")[1].trim().split(" ")[0];
            port = Integer.parseInt(rsport);
        } else {
            port = -1;
        }
        RConnection c = null;
        try {
            c = connect("localhost", port, null, null);
            if (c != null) {
                if (c.getServerVersion() < MIN_RSERVE_VERSION) {
                    logger.error("Rserver " + " version is too old.");
                } else {
                    logger.info("Test connection is ok, so Rserver is launched. ");
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return false;
    }

    public boolean isLocalHost() {
        return this.isLocal;
    }

    private String formatConfigurationSummary() {
        return "RURL_START"
                + (this.login != null ? (this.login + ":"
                + this.password + "@") : "")
                + (this.host == null ? DEFAULT_RSERVE_HOST : host)
                + (this.port > 0 ? ":" + this.port : "");
    }


    /* 
     * R Server environment
     */
    public final String getRHomeDir() {
        return this.rHomeDir;
    }

    public boolean isRHomeDirExists(String rHomeDir) {
        this.rHomeDir = rHomeDir;
        if (this.rHomeDir == null) {
            return false;
        }
        return new File(this.rHomeDir).exists();
    }

    public static boolean isRServerInstalled(String Rcmd) {
        StringBuffer result = new StringBuffer();
        boolean done = executeSystemCommand("i=installed.packages();is.element(set=i,el='Rserve')", Rcmd, "--vanilla -q", result, result);
        if (!done) {
            return false;
        }
        logger.info("\n" + result.toString() + "\n");
        if (result.toString().contains("TRUE")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPortAvailable(int p) {
        try {
            ServerSocket test = new ServerSocket(p);
            test.close();
        } catch (BindException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /*
     * Process manipulation
     */
    public static boolean executeSystemCommand(String todo, String Rcmd, String rargs, StringBuffer out, StringBuffer err) {
        try {
            Process p;
            boolean isWindows = false;
            String osname = System.getProperty("os.name");
            String command;
            if (osname != null && osname.length() >= 7 && osname.substring(0, 7).equals("Windows")) {
                isWindows = true;
                command = "\"" + Rcmd + "\" -e \"" + todo + "\" " + rargs;
                p = Runtime.getRuntime().exec(command);
            } else {
                command = "echo \"" + todo + "\"|" + Rcmd + " " + rargs;
                p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            }
            logger.info("Executing " + command);
            AsynchronousStreamReader error = new AsynchronousStreamReader(p.getErrorStream(), (err != null));
            error.start();
            AsynchronousStreamReader output = new AsynchronousStreamReader(p.getInputStream(), (out != null));
            output.start();
            if (err != null) {
                error.join();
            }
            if (out != null) {
                output.join();
            }
            if (!isWindows) {
                p.waitFor();
            }
            if (out != null) {
                out.append(output.getOutput());
            }
            if (err != null) {
                err.append(error.getOutput());
            }
        } catch (Exception x) {
            logger.error(x.getMessage());
            return false;
        }
        return true;
    }
}
/*
 * See 'When Runtime.exec() won't' at http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=1
 */

class AsynchronousStreamReader extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(AsynchronousStreamReader.class);
    InputStream is;
    boolean capture;
    StringBuffer out = new StringBuffer();

    AsynchronousStreamReader(InputStream is, boolean capture) {
        this.is = is;
        this.capture = capture;
    }

    public String getOutput() {
        return out.toString();
    }

    @Override
    public void run() {
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (capture) {
                    out.append("\n").append(line);
                } else {
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
            }
        }

    }
}

class WindowsReqistryReader {

    public static String readRegistry(String location, String key) {
        try {
            String cmd = "reg query " + location + " /v " + key;
            Process process = Runtime.getRuntime().exec(cmd);
            StreamReader reader = new StreamReader(process.getInputStream());

            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult();
            int i = output.indexOf("REG_SZ");
            if (i == -1) {
                return null;
            }
            StringBuilder sw = new StringBuilder();
            i += 6; // skip REG_SZ
            // skip spaces or tabs
            for (;;) {
                if (i > output.length()) {
                    break;
                }
                char c = output.charAt(i);
                if (c != ' ' && c != '\t') {
                    break;
                }
                ++i;
            }
            // take everything until end of line
            for (;;) {
                if (i > output.length()) {
                    break;
                }
                char c = output.charAt(i);
                if (c == '\r' || c == '\n') {
                    break;
                }
                sw.append(c);
                ++i;
            }
            return sw.toString();
        } catch (Exception e) {
            return null;
        }

    }
}

class StreamReader extends Thread {

    private InputStream is;
    private StringWriter sw = new StringWriter();

    public StreamReader(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {
        try {
            int c;
            while ((c = is.read()) != -1) {
                sw.write(c);
            }
        } catch (IOException e) {
        }
    }

    public String getResult() {
        return sw.toString();
    }
}
