package core.prototype.dao;

import java.util.Map;

public class DaoParameters {

    private String dbType;
    private String driverClassName;
    private String url;
    private String userName;
    private String password;
    private Map<String, Map<String, String>> daoProfiles;
    private int maxRows;

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Map<String, String>> getDaoProfiles() {
        return daoProfiles;
    }

    public void setDaoProfiles(Map<String, Map<String, String>> daoProfiles) {
        this.daoProfiles = daoProfiles;
    }
}
