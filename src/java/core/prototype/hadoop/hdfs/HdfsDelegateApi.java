package core.prototype.hadoop.hdfs;

public interface HdfsDelegateApi {

    public String readFile(String filePath);

    public void deleteDirectory(String dirPath);
}
