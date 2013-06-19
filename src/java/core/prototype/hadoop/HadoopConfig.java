package core.prototype.hadoop;

import core.prototype.config.ConfigUtils;
import core.prototype.hadoop.hbase.HbaseBase;
import core.prototype.hadoop.hbase.HbaseDelegateApi;
import core.prototype.hadoop.hdfs.HdfsBase;
import core.prototype.hadoop.hdfs.HdfsDelegateApi;
import core.prototype.hadoop.mapreduce.MapreduceDelegateApi;
import core.prototype.hadoop.mapreduce.WordCount;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.mapred.JobConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class HadoopConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = "hBase")
    @Scope("prototype")
    public HbaseDelegateApi hBase(HadoopParameters params) {
        HbaseDelegateApi hbApi = null;
        try {
            System.setProperty("java.net.preferIPv4Stack", params.isIsIp4() ? "true" : "false");
            System.setProperty("HADOOP_USER_NAME", params.getUserName());
            org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();         
            conf.set("hbase.master", params.getHbaseMaster());
            conf.set("hbase.zookeeper.quorum", params.getHbaseZookeeperQuorum());
            HBaseAdmin admin = new HBaseAdmin(conf);         
            HTablePool pool = new HTablePool(conf, params.getHbasePoolSize());
            hbApi = new HbaseBase(admin, pool);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return hbApi;
    }

    @Bean(name = "hDfs")
    @Scope("prototype")
    public HdfsDelegateApi hDfs(HadoopParameters params) {
        HdfsDelegateApi hbApi = null;
        try {          
            System.setProperty("java.net.preferIPv4Stack", params.isIsIp4() ? "true" : "false");
            System.setProperty("HADOOP_USER_NAME", params.getUserName());
            org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();         
            conf.set("fs.default.name", params.getFsDefaultName());
            FileSystem fs = FileSystem.get(conf);
            hbApi = new HdfsBase(fs);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return hbApi;
    }

    @Bean(name = "mrWordcount")
    @Scope("prototype")
    public MapreduceDelegateApi mrWordcount(HadoopParameters params) {       
        System.setProperty("HADOOP_USER_NAME", params.getUserName());
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();      
        conf.set("mapred.job.tracker", params.getMapredJobTracker());
        conf.set("fs.default.name", params.getFsDefaultName());
        String mapredJarRelativePath = params.getMapredJarPath();
        String mapredJarPath =
                ConfigUtils.appendPaths(params.getLocalWorkDir(), mapredJarRelativePath);
        conf.set("mapred.jar", mapredJarPath);
        JobConf jconf = new JobConf(conf);
        MapreduceDelegateApi mrwc = new WordCount(jconf);
        return mrwc;
    }
}
