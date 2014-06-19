package net.catchpole.sql.mapping;

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

import net.catchpole.model.ValueModel;
import net.catchpole.sql.Database;
import net.catchpole.sql.meta.Column;
import net.catchpole.sql.meta.DatabaseMetaTools;
import net.catchpole.sql.meta.Table;
import net.catchpole.sql.query.Prospect;

public class SchemaExport {
    private final ValueModel valueModel;
    private final Prospect prospect;

    public SchemaExport(Prospect prospect) {
        this(prospect.getTable().getDatabase(), prospect);
    }

    public SchemaExport(Database database) {
        this(database, null);
    }

    private SchemaExport(Database database, Prospect prospect) {
        this.prospect = prospect;

        this.valueModel = new ValueModel(database.getName(), null);
        for (Table table : database.getTables()) {
            if (includeTable(table)) {
                addTable(valueModel, table);
            }
        }
    }

    public ValueModel getValueModel() {
        return valueModel;
    }

    private ValueModel addTable(ValueModel addTag, Table table) {
        ValueModel complexType = new ValueModel("complexType", null);
        complexType.setAttribute("name", table.getTableName().toLowerCase());
        addTag.addChild(complexType);
        ValueModel sequence = new ValueModel("sequence", null);
        complexType.addChild(sequence);
        addColumns(sequence, table);
        return complexType;
    }

    private ValueModel addColumns(ValueModel addTag, Table table) {
        for (Column column : table.getColumns()) {
            if (includeColumn(column)) {
                ValueModel element = new ValueModel("element", null);
                element.setAttribute("name", column.getColumnName().toLowerCase());
                String foreignKeyTable = column.getForeignKeyTable();
                if (foreignKeyTable != null) {
                    element.setAttribute("type", foreignKeyTable.toLowerCase());
                } else {
                    String type = DatabaseMetaTools.getJavaMappingType(column);
                    if (type.equals(String.class.getName())) {
                        type = "string";
                    }
                    element.setAttribute("type", "xsd:"+type);
                }
                if (column.isNullable()) {
                    element.setAttribute("nillable", "true");
                }
                addTag.addChild(element);
            }
        }
        return addTag;
    }

    private boolean includeColumn(Column column) {
        if (prospect == null) {
            return true;
        }

        for (Column testColumn : prospect.getSelectColumns()) {
            if (column.equals(testColumn)) {
                return true;
            }
        }

        return false;
    }

    private boolean includeTable(Table table) {
        if (prospect == null) {
            return true;
        }

        for (Column testColumn : prospect.getSelectColumns()) {
            if (table.equals(testColumn.getTable())) {
                return true;
            }
        }

        return false;
    }
}
