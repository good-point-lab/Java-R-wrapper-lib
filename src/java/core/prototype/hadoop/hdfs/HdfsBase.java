package core.prototype.hadoop.hdfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsBase implements HdfsDelegateApi {

    private FileSystem fs;
    private String fsName;

    public HdfsBase(FileSystem fs) {
        Configuration conf = fs.getConf();
        this.fsName = conf.getRaw("fs.default.name");
        this.fs = fs;
    }

    @Override
    public void deleteDirectory(String dirPath) {
        try {
            Path path = new Path(fsName + dirPath);
            this.fs.delete(path, true);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Override
    public String readFile(String filePath) {
        StringBuilder bf = new StringBuilder();
        FSDataInputStream st = null;
        try {
            Configuration conf = this.fs.getConf();
            String fsName = conf.getRaw("fs.default.name");
            Path path = new Path(fsName + filePath);
            st = this.fs.open(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(st));
            String line;
            line = br.readLine();
            while (line != null) {
                line = br.readLine();
                bf.append(line);
                bf.append("\r\n");
            }
            st.close();
            this.fs.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                
                if (this.fs != null) {
                    this.fs.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return bf.toString();
    }
}
