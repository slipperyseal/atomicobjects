package net.catchpole.sql.meta;

//   Copyright 2014 catchpole.net
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class DatabaseMetaTools {
    public static void printResultSet(ResultSet resultSet, PrintStream out) throws SQLException {
        int cols = resultSet.getMetaData().getColumnCount();
        for (int x = 0; x < cols; x++) {
            out.print(' ');
            out.print(x + 1);
            out.print(':');
            out.print(resultSet.getObject(x + 1));
        }
        out.println();
    }

    public static Object columnObject(ResultSet resultSet, int column) throws SQLException {
        return resultSet.getMetaData().getColumnCount() >= column ? resultSet.getObject(column) : null;
    }

    public static String columnString(ResultSet resultSet, int column) throws SQLException {
        return resultSet.getMetaData().getColumnCount() >= column ? resultSet.getString(column) : null;
    }

    public static boolean columnBoolean(ResultSet resultSet, int column) throws SQLException {
        return "YES".equalsIgnoreCase(DatabaseMetaTools.columnString(resultSet, column));
    }

    public static int columnInteger(ResultSet resultSet, int column) throws SQLException {
        String value = DatabaseMetaTools.columnString(resultSet, column);
        if (value != null && value.length() > 0) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    public static boolean isPsudoTable(Table table) {
        return isPsudoTable(table.getTableName());
    }

    public static boolean isPsudoTable(String tableName) {
        return tableName.indexOf('$') != -1;
    }

    public static boolean isPsudoColumn(Column column) {
        return isPsudoColumn(column.getColumnName());
    }

    public static boolean isPsudoColumn(String columnName) {
        return columnName.indexOf('$') != -1;
    }

    public static List<String> getResultColumnNames(ResultSet resultSet) throws SQLException {
        List<String> columnNames = new ArrayList<String>();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int width = resultSetMetaData.getColumnCount();
        for (int x=0;x<width;x++) {
            columnNames.add(resultSetMetaData.getColumnName(x+1));
        }
        return columnNames;
    }

    /**
     * Returns the name of Java types appropriate for mapping ResultSet data for a Column.
     * This returns the optimal type based on the columns nullability,
     * width and using primative types
     * if possible for non-nullable fields.
     * Integer columns 9 decimal width or less use Integer, 18 or less use Long and those longer use BigDecimal
     *
     * @return The String name of a Java type which can be "java.lang.Object" if the type is unknown or unmappable.
     */
    public static String getJavaMappingType(Column column) {
        return getJavaMappingType(column, column.isNullable());
    }

    public static String getJavaMappingType(Column column, boolean nullable) {
        switch (column.getDataType()) {
            case Types.ARRAY:
            case Types.BINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
            case Types.VARBINARY:
                return "byte[]";

            case Types.BIT:
            case Types.BOOLEAN:
                return nullable ? "java.lang.Boolean" : "boolean";

            case Types.CHAR:
            case -15://Types.NCHAR: // JDK 1.6
                if (column.getColumnSize() == 1) {
                    return nullable ? "java.lang.Character" : "char";
                }
                // continue to String types

            case Types.CLOB:
            case -16://Types.LONGNVARCHAR: // JDK 1.6
            case Types.LONGVARCHAR:
            case Types.VARCHAR:
            case -9://Types.NVARCHAR: // JDK 1.6
            case 2011://Types.NCLOB: // JDK 1.6
                return "java.lang.String";

            case Types.BIGINT:
            case Types.DECIMAL:
            case Types.INTEGER:
            case Types.NUMERIC:
            case Types.TINYINT:
            case Types.SMALLINT:
                int size = column.getColumnSize();
                if (size <= 9) {
                    // 9 9s fills 30 bits + 1 sign bit
                    return nullable ? "java.lang.Integer" : "int";
                } else if (size <= 18) {
                    // 18 9s fills 60 bits + 1 sign bit
                    return nullable ? "java.lang.Long" : "long";
                } else {
                    return "java.math.BigInteger";
                }

            case Types.FLOAT:
            case Types.DOUBLE:
                return nullable ? "java.lang.Double" : "double";

            case Types.REAL:
                return nullable ? "java.lang.Float" : "float";

            case Types.DATE:
                return "java.sql.Date";
            case Types.TIME:
                return "java.sql.Time";
            case Types.TIMESTAMP:
                return "java.sql.Timestamp";

            case Types.JAVA_OBJECT:
            default:
                return "java.lang.Object";

//            case Types.DISTINCT:
//            case Types.DATALINK:
//            case Types.NULL:
//            case Types.OTHER:
//            case Types.REF:
//            case Types.ROWID:
//            case Types.SQLXML:
//            case Types.STRUCT:
        }
    }


}
