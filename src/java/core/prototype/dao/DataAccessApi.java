package core.prototype.dao;

import core.prototype.dao.entities.RIrisDataSet;
import core.prototype.domain.DataPage;
import java.util.List;

public interface DataAccessApi {

    public int getTableRowCount();

    public <T> DataPage getTablePage(DataPage page, Class<T> clazz);

    public void saveIrisDataset(final List<RIrisDataSet> data);

    public void deleteIrisDataset();
    
}
