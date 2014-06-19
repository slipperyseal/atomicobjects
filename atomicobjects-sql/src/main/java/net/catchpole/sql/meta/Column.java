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

import net.catchpole.model.Model;
import net.catchpole.model.iterator.ModelIterator;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class Column implements Serializable, Model {
    private Table table;
    private String catalog;
    private String schema;
    private String tableName;
    private String columnName;
    private int dataType;
    private String typeName;
    private int columnSize;
    private int bufferLength;
    private String decimalDigits;
    private int radix;
    private String nullAllowed;
    private String remarks;
    private String defaultValue;
    private String sqlDataType;
    private boolean sqlDateTimeSub;
    private String charOctetLength;
    private String ordinalPosition;
    private boolean nullable;
    private String scopeCatalog;
    private String scopeSchema;
    private String scopeTable;
    private String sourceDataType;
    private boolean primaryKey;
    private String foreignKeyTable;
    private Class mappingType;

    public Column(Table table, ResultSet resultSet) throws SQLException {
        this.table = table;
        this.catalog = DatabaseMetaTools.columnString(resultSet, 1);
        this.schema = DatabaseMetaTools.columnString(resultSet, 2);
        this.tableName = DatabaseMetaTools.columnString(resultSet, 3);
        this.columnName = DatabaseMetaTools.columnString(resultSet, 4);
        this.dataType = DatabaseMetaTools.columnInteger(resultSet, 5);
        this.typeName = DatabaseMetaTools.columnString(resultSet, 6);
        this.columnSize = DatabaseMetaTools.columnInteger(resultSet, 7);
        this.bufferLength = DatabaseMetaTools.columnInteger(resultSet, 8);
        this.decimalDigits = DatabaseMetaTools.columnString(resultSet, 9);
        this.radix = DatabaseMetaTools.columnInteger(resultSet, 10);
        this.nullAllowed = DatabaseMetaTools.columnString(resultSet, 11);
        this.remarks = DatabaseMetaTools.columnString(resultSet, 12);
        this.defaultValue = DatabaseMetaTools.columnString(resultSet, 13);
        this.sqlDataType = DatabaseMetaTools.columnString(resultSet, 14);
        this.sqlDateTimeSub = DatabaseMetaTools.columnBoolean(resultSet, 15);
        this.charOctetLength = DatabaseMetaTools.columnString(resultSet, 16);
        this.ordinalPosition = DatabaseMetaTools.columnString(resultSet, 17);
        this.nullable = DatabaseMetaTools.columnBoolean(resultSet, 18);
        this.scopeCatalog = DatabaseMetaTools.columnString(resultSet, 19);
        this.scopeSchema = DatabaseMetaTools.columnString(resultSet, 20);
        this.scopeTable = DatabaseMetaTools.columnString(resultSet, 21);
        this.sourceDataType = DatabaseMetaTools.columnString(resultSet, 22);
        try {
            this.mappingType = Class.forName(DatabaseMetaTools.getJavaMappingType(this, true));
        } catch (Exception e) {
            this.mappingType = Object.class;
        }
    }

    public Table getTable() {
        return table;
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

    public String getColumnName() {
        return columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public int getBufferLength() {
        return bufferLength;
    }

    public String getDecimalDigits() {
        return decimalDigits;
    }

    public int getRadix() {
        return radix;
    }

    public String getNullAllowed() {
        return nullAllowed;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getSqlDataType() {
        return sqlDataType;
    }

    public boolean isSqlDateTimeSub() {
        return sqlDateTimeSub;
    }

    public String getCharOctetLength() {
        return charOctetLength;
    }

    public String getOrdinalPosition() {
        return ordinalPosition;
    }

    public boolean isNullable() {
        return nullable;
    }

    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public String getScopeSchema() {
        return scopeSchema;
    }

    public String getScopeTable() {
        return scopeTable;
    }

    public String getSourceDataType() {
        return sourceDataType;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getForeignKeyTable() {
        return foreignKeyTable;
    }

    public void setForeignKeyTable(String foreignKeyTable) {
        this.foreignKeyTable = foreignKeyTable;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' +
                tableName + '/' + columnName + ' ' +
                catalog + ':' + schema +
                (primaryKey ? " PK" : "") +
                (foreignKeyTable != null ? " FK: " + foreignKeyTable : "") +
                (nullable ? " NULLABLE" : "") + ' ' +
                //DatabaseMetaTools.getJavaMappingType(this) + '/' +
                columnSize;
    }

    // model

    public String getName() {
        return columnName;
    }

    public Class getType() {
        return mappingType;
    }

    public Iterator getValues() {
        return null;
    }

    public Iterator<Model> iterator() {
        return new ModelIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (catalog != null ? !catalog.equals(column.catalog) : column.catalog != null) return false;
        if (columnName != null ? !columnName.equals(column.columnName) : column.columnName != null) return false;
        if (schema != null ? !schema.equals(column.schema) : column.schema != null) return false;
        if (tableName != null ? !tableName.equals(column.tableName) : column.tableName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = catalog != null ? catalog.hashCode() : 0;
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
        return result;
    }
}
