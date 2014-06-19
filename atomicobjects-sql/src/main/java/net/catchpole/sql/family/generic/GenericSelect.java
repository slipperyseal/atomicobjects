package net.catchpole.sql.family.generic;

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

import net.catchpole.compiler.writer.OutputStreamCodeWriter;
import net.catchpole.lang.Wire;
import net.catchpole.sql.data.Row;
import net.catchpole.sql.family.StatementBuilder;
import net.catchpole.sql.meta.Column;
import net.catchpole.sql.meta.Table;
import net.catchpole.sql.query.Prospect;

import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 */
public class GenericSelect implements StatementBuilder {
    public String createStatement(Prospect prospect) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamCodeWriter codeWriter = new OutputStreamCodeWriter(baos);
        
        codeWriter.println("select");
        codeWriter.indent();
        Wire wire = new Wire();
        for (Column column : prospect.getSelectColumns()) {
            if (wire.trip()) {
                codeWriter.println(',');
            }
            codeWriter.print(column.getTable().getTableName());
            codeWriter.print('.');
            codeWriter.print(column.getColumnName());
        }
        codeWriter.println();
        codeWriter.outdent();
        codeWriter.println("from");
        codeWriter.indent();
        codeWriter.println(prospect.getTable().getTableName());
        codeWriter.outdent();
        
        // inner join
        for (Column column : prospect.getSelectColumns()) {
            if (!column.getTableName().equals(prospect.getTable().getTableName())) {
                Table foreignTable = column.getTable();
                List<Column> keys = foreignTable.getPrimaryKeyColumns();
                if (keys.size() != 1) {
                    throw new IllegalArgumentException("currently only supporting single primary key for inner join");
                }
                Column foreignColumn = keys.get(0);

                codeWriter.println("inner join");
                codeWriter.indent();
                codeWriter.println(foreignTable.getTableName());
                codeWriter.indent();
                codeWriter.print("on ");
                codeWriter.print(prospect.getTable().getTableName());
                codeWriter.print('.');
                codeWriter.print(column.getTableName());
                codeWriter.print('=');
                codeWriter.print(foreignTable.getTableName());
                codeWriter.print('.');
                codeWriter.println(foreignColumn.getColumnName());
                codeWriter.outdent();
                codeWriter.outdent();
            }
        }

        List<Column> criteriaColumns = prospect.getCriteria();
        int size = prospect.getCriteria().size();
        if (size > 0) {
            codeWriter.println("where");
            codeWriter.indent();
            Wire andWire = new Wire();
            for (int x = 0; x < size; x++) {
                if (andWire.trip()) {
                    codeWriter.print(" and ");
                }
                codeWriter.print(criteriaColumns.get(x).getColumnName());
                codeWriter.println(" = ?");
            }
            codeWriter.outdent();
        }

        codeWriter.println("order by");
        codeWriter.indent();
        List<Column> orderColumns = prospect.getOrderColumns();
        if (orderColumns.size() != 0) {
            Wire commaWire = new Wire();
            for (Column orderColumn : orderColumns) {
                if (commaWire.trip()) {
                    codeWriter.print(',');
                }
                codeWriter.print(orderColumn.getTable().getTableName());
                codeWriter.print('.');
                codeWriter.print(orderColumn.getColumnName());
            }
        } else {
            // default to order by primary keys
            Wire commaWire = new Wire();
            for (Column column : prospect.getTable().getColumns()) {
                if (column.isPrimaryKey()) {
                    if (commaWire.trip()) {
                        codeWriter.print(',');
                    }
                    codeWriter.print(column.getTable().getTableName());
                    codeWriter.print('.');
                    codeWriter.print(column.getColumnName());
                }
            }
        }
        codeWriter.outdent();
        codeWriter.close();
        return baos.toString();
    }

    public void setObjects(PreparedStatement preparedStatement, Prospect prospect, Row row) throws SQLException {
    }
}
