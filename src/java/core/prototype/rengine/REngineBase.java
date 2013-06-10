package core.prototype.rengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPFactor;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RFactor;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class REngineBase implements REngineApi {

    private static final Logger logger = LoggerFactory.getLogger(REngineBase.class);
    private Rserve server;
    private String localTempDirPath;
    private String localRScriptsDirPath;

    public REngineBase(Rserve server) {
        this.server = server;
    }

    public REngineBase(Rserve server, String localRScriptsDirPath, String localTempDirPath) {
        this(server);
        this.localRScriptsDirPath = localRScriptsDirPath;
        this.localTempDirPath = localTempDirPath;
    }

    @Override
    public String getLocalTempDirPath() {
        return localTempDirPath;
    }

    @Override
    public String getLocalRScriptsDirPath() {
        return localRScriptsDirPath;
    }

    @Override
    public String getRVersion() {
        String version = this.executeForString("R.version.string");
        return version;
    }

    @Override
    public String getRServerInstallPath() {
        return this.server.getRHomeDir();
    }

    @Override
    public Map<String, Object> getIrisDataset() {
        return this.executeForDataFrame("{data(iris); iris}");
    }

    @Override
    public void valiadateIrisDataset(final Map<String, Object> vars) {
        this.executeForString("identical(df, iris)", vars);
    }

    @Override
    public byte[] executeScriptForGraphics(final Map<String, Object> vars) {

        class RGraphics implements RConnectionCallback {

            private byte[] result;

            @Override
            public Object executeInR(RConnection c) throws REXPMismatchException, REngineException {
                String filePath = (String) vars.get("imagePath");
                assign(c, vars);
                c.eval("try(source(fn), silent=TRUE)");
                REXP exp = c.parseAndEval("r=readBin('" + filePath + "','raw',1024*1024); unlink('" + filePath + "'); r");
                this.result = exp.asBytes();
                return null;
            }

            public byte[] getResult() {
                return this.result;
            }
        }

        RGraphics ri = new RGraphics();
        this.execute(ri);
        return ri.getResult();
    }

    @Override
    public String executeForString(final String command) {
        return this.executeForString(command, null);
    }

    @Override
    public String executeForString(final String command, final Map<String, Object> vars) {
        class RCommand implements RConnectionCallback {

            private String result;

            @Override
            public String executeInR(RConnection c) throws REXPMismatchException, REngineException {
                assign(c, vars);
                REXP exp = c.eval(command);
                this.result = exp.asString();
                return null;
            }

            public String getResult() {
                return this.result;
            }
        }
        RCommand cmd = new RCommand();
        this.execute(cmd);
        return cmd.getResult();
    }

    @Override
    public Map<String, Object> executeForDataFrame(final String command) {
        return this.executeForDataFrame(command, null);
    }

    @Override
    public Map<String, Object> executeForDataFrame(final String command, final Map<String, Object> vars) {
        class RCommand implements RConnectionCallback {

            private Map<String, Object> result;

            @Override
            public Map<String, Object> executeInR(RConnection c) throws REXPMismatchException, REngineException {
                assign(c, vars);
                REXP exp = c.parseAndEval(command);
                if (!exp.isList()) {
                    return null;
                }
                RList list = exp.asList();
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < list.size(); i++) {
                    REXP rexp = list.at(i);
                    String key;
                    if (list.isNamed()) {
                        key = list.keys()[i];
                    } else {
                        key = Integer.toString(i);
                    }
                    map.put(key, convert(rexp));
                }
                this.result = map;
                return null;
            }

            public Map<String, Object> getResult() {
                return this.result;
            }
        }
        RCommand cmd = new RCommand();
        this.execute(cmd);
        return cmd.getResult();
    }

    private <T> T execute(RConnectionCallback<T> action) {
        RConnection c = null;
        T result = null;
        try {
            c = this.server.connect();
            if(c == null) {
                throw new RuntimeException("Can not get connection to Rserve");
            }
            result = action.executeInR(c);
        } catch (REXPMismatchException ex) {
            throw new RuntimeException(ex.getMessage());
        } catch (REngineException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return result;
    }

    private void assign(RConnection c, Map<String, Object> vars) throws RserveException, REngineException, REXPMismatchException {
        if (vars == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : vars.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            assign(c, name, value);
        }
    }

    private void assign(RConnection c, String name, Object value) throws RserveException, REngineException, REXPMismatchException {
        if (value instanceof String) {
            c.assign(name, (String) value);
        } else if (value instanceof Integer) {
            c.assign(name, new int[]{(Integer) value});
        } else if (value instanceof Double) {
            c.assign(name, new double[]{(Double) value});
        } else if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            map.size();
            RList list = new RList();
            int index = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                if (entry.getValue() instanceof String[]) {
                    list.put(key, new REXPFactor(new RFactor((String[]) entry.getValue())));
                } else if (entry.getValue() instanceof double[]) {
                    list.put(key, new REXPDouble((double[]) entry.getValue()));
                } else {
                    throw new IllegalArgumentException("R does not supported type of the variable.");
                }
                list.setKeyAt(index++, key);
            }
            REXP frame = REXP.createDataFrame(list);
            c.assign(name, frame);
        } else if (value instanceof List) {
            RList list = new RList((List) value);
            c.assign(name, new REXPList((RList) list));
        } else {
            throw new IllegalArgumentException("R does not supported type of the variable.");
        }
    }

    private Object convert(REXP exp) throws REXPMismatchException {
        Object rv;
        if (exp.isFactor()) {
            rv = exp.asFactor().asStrings();
        } else if (exp.isNumeric()) {
            rv = exp.asDoubles();
        } else if (exp.isInteger()) {
            rv = exp.asIntegers();
        } else {
            rv = null;
        }
        return rv;
    }

    @Override
    public void stopRServer() {
        this.server.stopRServer();
    }
}

interface RConnectionCallback<T> {

    T executeInR(RConnection c) throws REXPMismatchException, REngineException;
}
/*
 * References:
 * 1. org.rosuda.REngine API docs  http://www.rforge.net/org/docs/org/rosuda/REngine/package-summary.html
 * 2. Usage examples http://stackoverflow.com/questions/1203662/how-to-connect-to-r-with-java-using-eclipse
 */
