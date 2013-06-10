package core.prototype.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectUtils {

    public static <T> T[] convertListToArray(List<T> list) {

        T[] array = (T[]) Array.newInstance(list.get(0)
                .getClass(), list.size());
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;

    }

    public static <T> List<T> convertArrayToList(T[] arary) {
        return Arrays.asList(arary);
    }

    public static Object[][] convertFrameMapToArray(Map<String, Object> map) {
        int cols = map.size();
        int rows = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof double[]) {
                rows = ((double[]) value).length;
            } else if (value instanceof String[]) {
                rows = ((String[]) value).length;
            } else if (value instanceof int[]) {
                rows = ((int[]) value).length;
            } else {
                throw new IllegalArgumentException("Unknown type");
            }
            break;
        }
        Object[][] array = new Object[rows + 1][cols];
        Set<String> colNames = map.keySet();
        int col = 0;
        for (String name : colNames) {
            array[0][col++] = name;
        }

        col = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof double[]) {
                for (int row = 0; row < rows; row++) {
                    array[row + 1][col] = ((double[]) value)[row];
                }
                col++;
            } else if (value instanceof String[]) {
                for (int row = 0; row < rows; row++) {
                    array[row + 1][col] = ((String[]) value)[row];
                }
                col++;
            } else if (value instanceof int[]) {
                for (int row = 0; row < rows; row++) {
                    array[row + 1][col] = ((int[]) value)[row];
                }
                col++;
            } else {
                throw new IllegalArgumentException("Unknown type");
            }
        }
        return array;
    }

    public static String[][] formatArrayToStrings(Object[][] array) {
        int rows = array.length;
        int cols = array[0].length;
        String[][] rv = new String[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                rv[row][col] = array[row][col].toString();
            }
        }
        return rv;
    }

    public static String[][] formatArrayToStringsWithRowNumbers(Object[][] array) {
        int rows = array.length;
        int cols = array[0].length;
        String[][] rv = new String[rows][cols + 1];
        for (int row = 0; row < rows; row++) {
            if (row == 0) {
                rv[row][0] = "#";
            } else {
                rv[row][0] = Integer.toString(row);
            }

            for (int col = 0; col < cols; col++) {
                rv[row][col + 1] = array[row][col].toString();
            }
        }
        return rv;
    }
}
