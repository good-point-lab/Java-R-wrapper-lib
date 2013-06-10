package core.prototype.rengine;

public class RServeParameters {

    private String host;
    private int port;
    private String login;
    private String password;
    private String workingDirectory;
    private boolean isLocal;
    private String locatTempDirPath;
    private String localRScriptsDirPath;
    private String homeDirectory; // for local installation

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public boolean isIsLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocatTempDirPath() {
        return locatTempDirPath;
    }

    public void setLocatTempDirPath(String locatTempDirPath) {
        this.locatTempDirPath = locatTempDirPath;
    }

    public String getLocalRScriptsDirPath() {
        return localRScriptsDirPath;
    }

    public void setLocalRScriptsDirPath(String localRScriptsDirPath) {
        this.localRScriptsDirPath = localRScriptsDirPath;
    }
}
