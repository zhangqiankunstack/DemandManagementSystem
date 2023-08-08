package com.rengu.util;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetToListConverter {
    public static <T> List<T> convertToList(ResultSet resultSet, Class<T> clazz) throws SQLException {
        List<T> resultList = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            try {
                T object = clazz.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Field field = getField(clazz, columnName);
                    if (field != null) {
                        Object value = resultSet.getObject(i);
                        field.setAccessible(true);
                        field.set(object, value);
                    }
                }
                resultList.add(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
