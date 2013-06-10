package core.prototype.domain;

import core.prototype.config.Application;
import core.prototype.config.ConfigUtils;
import core.prototype.dao.entities.RIrisDataSet;
import core.prototype.utils.ObjectUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DomainBase implements DomainApi {

    @Override
    public String displayConfigParameters() {
        StringBuilder buf = new StringBuilder();
        Application app = Application.getInstance();
        String path = app.getPropertiesFileAbsolutePath();
        Properties props = app.getProperties(path);
        buf.append((String) props.get("db.type"));
        buf.append("<br>");
        buf.append(app.getDbProfiles().get("mysql").get("hibernateDialect"));
        buf.append("<br>");
        buf.append(app.getREngine().getRServerInstallPath());
        return buf.toString();     
    }

    @Override
    public void loadIrisDatasetToDb() {
        Application app = Application.getInstance();
        Map<String, Object> irisDsMap = app.getREngine().getIrisDataset();
        List<RIrisDataSet> iriDsList = converIrisDataset(irisDsMap);
        app.getDao().deleteIrisDataset();
        app.getDao().saveIrisDataset(iriDsList);
    }

    @Override
    public byte[] plotRIrisDataset(String imageName, String scriptName) {
        Application app = Application.getInstance();
        int size = app.getDao().getTableRowCount();
        DataPage page = new DataPage();
        page.setRowsPerPage(size);
        page.setStartRow(0);
        page = app.getDao().getTablePage(page, RIrisDataSet.class);
        List<RIrisDataSet> rows = page.getRows();
        Map<String, Object> frame = convertToFrame(rows);
        String absPathToImageDir = app.getREngine().getLocalTempDirPath();
        String absPathToScriptDir = app.getREngine().getLocalRScriptsDirPath();
        String absScriptPath = ConfigUtils.getAbsoluteFilePath(absPathToScriptDir, scriptName);
        String absImagePath = ConfigUtils.getAbsoluteFilePath(absPathToImageDir, imageName);
        Map<String, Object> vars = new HashMap();
        vars.put("frame", frame);
        vars.put("fn", absScriptPath);
        vars.put("plotType", "p");
        vars.put("imagePath", absImagePath);
        byte[] imageData = app.getREngine().executeScriptForGraphics(vars);
        return imageData;
    }

    @Override
    public byte[] plotRIrisDatasetDataInDb(String imageName, String scriptName, String plotType) {
        Application app = Application.getInstance();
        String absPathToImageDir = app.getREngine().getLocalTempDirPath();
        String absPathToScriptDir = app.getREngine().getLocalRScriptsDirPath();
        String absScriptPath = ConfigUtils.getAbsoluteFilePath(absPathToScriptDir, scriptName);
        String absImagePath = ConfigUtils.getAbsoluteFilePath(absPathToImageDir, imageName);
        Map<String, Object> vars = new HashMap();
        vars.put("fn", absScriptPath);
        vars.put("plotType", plotType);
        vars.put("imagePath", absImagePath);
        byte[] imageData = app.getREngine().executeScriptForGraphics(vars);
        return imageData;
    }

    @Override
    public byte[] plotMockDataset(String imageName, String scriptName) {
        Application app = Application.getInstance();
        String absPathToImageDir = app.getREngine().getLocalTempDirPath();
        String absPathToScriptDir = app.getREngine().getLocalRScriptsDirPath();
        String absScriptPath = ConfigUtils.getAbsoluteFilePath(absPathToScriptDir, scriptName);
        String absImagePath = ConfigUtils.getAbsoluteFilePath(absPathToImageDir, imageName);
        Map<String, Object> frame = new MockDataFrame().getFrame();
        Map<String, Object> vars = new HashMap();
        vars.put("frame", frame);
        vars.put("fn", absScriptPath);
        vars.put("plotType", "p");
        vars.put("imagePath", absImagePath);
        byte[] imageData = app.getREngine().executeScriptForGraphics(vars);
        return imageData;
    }

    private Map<String, Object> convertToFrame(List<RIrisDataSet> rows) {
        Map<String, Object> frame = new HashMap();
        int size = rows.size();
        double[] sl = new double[size];
        double[] sw = new double[size];
        double[] pl = new double[size];
        double[] pw = new double[size];
        String[] sp = new String[size];
        int i = 0;
        for (RIrisDataSet row : rows) {
            sl[i] = row.getSepalLength();
            sw[i] = row.getSepalWidth();
            pl[i] = row.getPetalLength();
            pw[i] = row.getPetalWidth();
            sp[i++] = row.getSpecies();
        }
        frame.put("Sepal.Length", sl);
        frame.put("Petal.Length", pl);
        frame.put("Sepal.Width", sw);
        frame.put("Petal.Width", pw);
        frame.put("Species", sp);
        return frame;
    }

    private List<RIrisDataSet> converIrisDataset(Map<String, Object> map) {
        Object[][] array = ObjectUtils.convertFrameMapToArray(map);
        int rows = array.length;
        List<RIrisDataSet> list = new ArrayList<RIrisDataSet>();
        // First row contains column names -- start from the second row
        for (int row = 1; row < rows; row++) {
            RIrisDataSet bean = new RIrisDataSet();
            bean.setPetalLength(((Double) array[row][0]).doubleValue());
            bean.setPetalWidth(((Double) array[row][1]).doubleValue());
            bean.setSepalLength(((Double) array[row][2]).doubleValue());
            bean.setSepalWidth(((Double) array[row][3]).doubleValue());
            bean.setSpecies((String) array[row][4]);
            list.add(bean);
        }
        return list;
    }
}
