package core.prototype.rengine;

import java.util.Map;

public interface REngineApi {

    public String getRVersion();

    public String getRServerInstallPath();

    public String executeForString(final String rCommand);

    public String executeForString(final String command, final Map<String, Object> vars);

    public Map<String, Object> executeForDataFrame(String command);

    public Map<String, Object> executeForDataFrame(final String command, final Map<String, Object> vars);

    public Map<String, Object> getIrisDataset();

    public void valiadateIrisDataset(final Map<String, Object> vars);

    public byte[] executeScriptForGraphics(final Map<String, Object> vars);

    public String getLocalTempDirPath();

    public String getLocalRScriptsDirPath();

    public void stopRServer();
}