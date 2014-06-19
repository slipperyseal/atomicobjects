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

import net.catchpole.lang.Throw;
import net.catchpole.model.Model;
import net.catchpole.model.iterator.ModelIterator;
import net.catchpole.sql.Database;
import net.catchpole.trace.Core;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Table implements Serializable, Model {
    private Database database;
    private DatabaseMetaData databaseMetaData;
    private List<Column> columns;
    private List<Column> primaryKeyColumns;
    private List<Column> nonPrimaryKeyColumns;
    private String catalog;
    private String schema;
    private String tableName;
    private String type;
    private String remarks;
    private String typesCatalog;
    private String typesSchema;
    private String typeName;
    private String selfReferencingColumnName;
    private String selfReferencingGeneration;

    public Table(Database database, ResultSet resultSet, DatabaseMetaData databaseMetaData) throws SQLException {
        this.database = database;
        this.databaseMetaData = databaseMetaData;
        this.catalog = DatabaseMetaTools.columnString(resultSet, 1);
        this.schema = DatabaseMetaTools.columnString(resultSet, 2);
        this.tableName = DatabaseMetaTools.columnString(resultSet, 3);
        this.type = DatabaseMetaTools.columnString(resultSet, 4);
        this.remarks = DatabaseMetaTools.columnString(resultSet, 5);
        this.typesCatalog = DatabaseMetaTools.columnString(resultSet, 6);
        this.typesSchema = DatabaseMetaTools.columnString(resultSet, 7);
        this.typeName = DatabaseMetaTools.columnString(resultSet, 8);
        this.selfReferencingColumnName = DatabaseMetaTools.columnString(resultSet, 9);
        this.selfReferencingGeneration = DatabaseMetaTools.columnString(resultSet, 10);
    }

    public Database getDatabase() {
        return this.database;
    }

    public List<Column> getColumns() {
        if (columns == null) {
            retrieveColumns();
        }
        return columns;
    }

    public List<Column> getPrimaryKeyColumns() {
        if (columns == null) {
            retrieveColumns();
        }
        return primaryKeyColumns;
    }

    public List<Column> getNonPrimaryKeyColumns() {
        if (columns == null) {
            retrieveColumns();
        }
        return nonPrimaryKeyColumns;
    }

    public Column getColumn(String name) {
        if (columns == null) {
            retrieveColumns();
        }
        for (Column column : columns) {
            if (column.getColumnName().equalsIgnoreCase(name)) {
                return column;
            }
        }
        return null;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getTypesCatalog() {
        return typesCatalog;
    }

    public String getTypesSchema() {
        return typesSchema;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getSelfReferencingColumnName() {
        return selfReferencingColumnName;
    }

    public String getSelfReferencingGeneration() {
        return selfReferencingGeneration;
    }

    public void retrieveColumns() {
        this.columns = new ArrayList<Column>();
        this.primaryKeyColumns = new ArrayList<Column>();
        this.nonPrimaryKeyColumns = new ArrayList<Column>();

        try {
            Map<String, String> foreignKeys = null;
            try {
                foreignKeys = getForeignKeyColumns(databaseMetaData);
            } catch (Exception e) {
                foreignKeys = new HashMap<String, String>();
                Core.getTrace().info(e);
            }
            List<String> primaryKeys = getPrimaryKeyColumns(databaseMetaData);

            ResultSet resultSet = databaseMetaData.getColumns(this.typesCatalog, this.schema, this.tableName, "%");
            try {
                while (resultSet.next()) {
                    Column column = new Column(this, resultSet);

                    // ignore pseudo tables
                    if (!DatabaseMetaTools.isPsudoColumn(column)) {
                        column.setPrimaryKey(primaryKeys.contains(column.getColumnName()));
                        if (foreignKeys.containsKey(column.getColumnName())) {
                            column.setForeignKeyTable(foreignKeys.get(column.getColumnName()));
                        }
                        columns.add(column);
                        if (column.isPrimaryKey()) {
                            primaryKeyColumns.add(column);
                        } else {
                            nonPrimaryKeyColumns.add(column);
                        }

                    }
                }
            } finally {
                resultSet.close();
            }
        } catch (SQLException sqle) {
            throw Throw.unchecked(sqle);
        }
    }

    private List<String> getPrimaryKeyColumns(DatabaseMetaData databaseMetaData) throws SQLException {
        List<String> primaryKeys = new ArrayList<String>();
        ResultSet resultSet = databaseMetaData.getPrimaryKeys(this.catalog, this.schema, this.tableName);
        try {
            while (resultSet.next()) {
                String columnName = resultSet.getString(4);
                if (columnName.indexOf('$') == -1) {
                    primaryKeys.add(columnName);
                }
            }
        } finally {
            resultSet.close();
        }
        return primaryKeys;
    }

    private Map<String, String> getForeignKeyColumns(DatabaseMetaData databaseMetaData) throws SQLException {
        Map<String, String> foreignKeys = new HashMap<String, String>();
        ResultSet resultSet = databaseMetaData.getImportedKeys(this.catalog, this.schema, this.tableName);
        try {
            while (resultSet.next()) {
                String columnName = resultSet.getString(8);

                // 8 = this column, 3 = table, 4 = foreign column
//                for (int x=1;x<10;x++) {
//                    Core.getTrace().info(this.getTableName() + " " + x + ' ' + resultSet.getString(x));
//                }
                if (columnName.indexOf('$') == -1) {
                    foreignKeys.put(columnName, resultSet.getString(3));
                }
            }
        } finally {
            resultSet.close();
        }
        return foreignKeys;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + tableName + '/' + catalog + '/' + schema;
    }

    // model

    public String getName() {
        return tableName;
    }

    public Class getType() {
        return Table.class;
    }

    public Iterator getValues() {
        return null;
    }

    public Iterator<Model> iterator() {
        return new ModelIterator(getColumns().iterator());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (catalog != null ? !catalog.equals(table.catalog) : table.catalog != null) return false;
        if (schema != null ? !schema.equals(table.schema) : table.schema != null) return false;
        if (selfReferencingColumnName != null ? !selfReferencingColumnName.equals(table.selfReferencingColumnName) : table.selfReferencingColumnName != null)
            return false;
        if (selfReferencingGeneration != null ? !selfReferencingGeneration.equals(table.selfReferencingGeneration) : table.selfReferencingGeneration != null)
            return false;
        if (tableName != null ? !tableName.equals(table.tableName) : table.tableName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = catalog != null ? catalog.hashCode() : 0;
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (selfReferencingColumnName != null ? selfReferencingColumnName.hashCode() : 0);
        result = 31 * result + (selfReferencingGeneration != null ? selfReferencingGeneration.hashCode() : 0);
        return result;
    }
}
