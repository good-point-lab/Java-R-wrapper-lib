package core.prototype.domain;

public interface DomainApi {
    
    String displayConfigParameters();

    public byte[] plotRIrisDataset(String imageName, String scriptName);
    
    public byte[] plotRIrisDatasetDataInDb(String imageName, String scriptName, String plotType);

    public byte[] plotMockDataset(String imageName, String scriptName);

    public void loadIrisDatasetToDb();
}
