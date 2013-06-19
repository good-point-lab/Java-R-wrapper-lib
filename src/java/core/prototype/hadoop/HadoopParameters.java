package core.prototype.hadoop;

public class HadoopParameters {

    private boolean isIp4;
    private String userName;
    private String fsDefaultName;
    private String hbaseMaster;
    private String hbaseZookeeperQuorum;
    private int hbasePoolSize;
    private String mapredJobTracker;
    private String mapredJarPath;
    private String localWorkDir;

    public boolean isIsIp4() {
        return isIp4;
    }

    public void setIsIp4(boolean isIp4) {
        this.isIp4 = isIp4;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFsDefaultName() {
        return fsDefaultName;
    }

    public void setFsDefaultName(String fsDefaultName) {
        this.fsDefaultName = fsDefaultName;
    }

    public String getHbaseMaster() {
        return hbaseMaster;
    }

    public void setHbaseMaster(String hbaseMaster) {
        this.hbaseMaster = hbaseMaster;
    }

    public String getHbaseZookeeperQuorum() {
        return hbaseZookeeperQuorum;
    }

    public void setHbaseZookeeperQuorum(String hbaseZookeeperQuorum) {
        this.hbaseZookeeperQuorum = hbaseZookeeperQuorum;
    }

    public int getHbasePoolSize() {
        return hbasePoolSize;
    }

    public void setHbasePoolSize(int hbasePoolSize) {
        this.hbasePoolSize = hbasePoolSize;
    }

    public String getMapredJobTracker() {
        return mapredJobTracker;
    }

    public void setMapredJobTracker(String mapredJobTracker) {
        this.mapredJobTracker = mapredJobTracker;
    }

    public String getMapredJarPath() {
        return mapredJarPath;
    }

    public void setMapredJarPath(String mapredJarPath) {
        this.mapredJarPath = mapredJarPath;
    }

    public String getLocalWorkDir() {
        return localWorkDir;
    }

    public void setLocalWorkDir(String localWorkDir) {
        this.localWorkDir = localWorkDir;
    }
}
