package core.prototype.hadoop.hbase;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;

public class HbaseBase implements HbaseDelegateApi {

    private HTablePool pool;
    private HBaseAdmin admin;

    public HbaseBase(HBaseAdmin admin, HTablePool pool) {
        this.admin = admin;
        this.pool = pool;
    }

    @Override
    public String listTables() {
        class ListTablesAction implements HbaseAdminCallback {

            private String result;

            @Override
            public Object executeInAdmin(HBaseAdmin admin) throws Exception {
                HTableDescriptor[] ds = admin.listTables();
                result = ds.toString();
                return null;
            }

            public String getResult() {
                return this.result;
            }
        }
        ListTablesAction ac = new ListTablesAction();
        this.adminAction(ac);
        return ac.getResult();
    }

    @Override
    public String getDescriptor(final String tableName) {
        class TableDescriptorQuery implements HbaseTableCallback {

            private String result;

            @Override
            public String executeInTable(HTableInterface table) throws Exception {
                HTableDescriptor htd = table.getTableDescriptor();
                result = htd.getName().toString();
                return null;
            }

            public String getResult() {
                return this.result;
            }
        }
        TableDescriptorQuery q = new TableDescriptorQuery();
        this.tableQuery(q, tableName);
        return q.getResult();
    }

    private <T> T tableQuery(HbaseTableCallback<T> action, final String tableName) {
        HTableInterface table = null;
        T result = null;
        try {
            table = this.pool.getTable(tableName);
            result = action.executeInTable(table);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (table != null) {
                    table.close();
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return result;
    }

    private <T> T adminAction(HbaseAdminCallback<T> action) {
        T result = null;
        try {
            result = action.executeInAdmin(this.admin);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
        }
        return result;
    }
}

interface HbaseTableCallback<T> {

    T executeInTable(HTableInterface table) throws Exception;
}

interface HbaseAdminCallback<T> {

    T executeInAdmin(HBaseAdmin admin) throws Exception;
}
