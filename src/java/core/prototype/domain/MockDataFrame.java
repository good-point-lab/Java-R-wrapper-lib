package core.prototype.domain;

import java.util.HashMap;
import java.util.Map;

public class MockDataFrame {

   public Map<String, Object> getFrame() {
        Map<String, Object> frame = new HashMap();
        frame.put("x", new double[]{2.2, 3.0, 4.3, 6.9, 5.1});
        frame.put("y", new double[]{12.2, 23.0, 1.1, 7.7, 13.3});
        return frame;
    }
}
