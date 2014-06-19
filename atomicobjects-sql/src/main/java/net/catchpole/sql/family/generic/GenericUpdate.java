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
import net.catchpole.sql.query.Prospect;
import net.catchpole.trace.Core;

import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 */
public class GenericUpdate implements StatementBuilder {
    public String createStatement(Prospect prospect) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamCodeWriter codeWriter = new OutputStreamCodeWriter(baos);
        codeWriter.println("update");
        codeWriter.indent();
        codeWriter.println(prospect.getTable().getTableName());
        codeWriter.outdent();
        codeWriter.println("set");
        codeWriter.indent();

        List<Column> columns = prospect.getSelectColumns();

        Wire commaWire = new Wire();
        for (Column column : columns) {
            if (!column.isPrimaryKey()) {
                if (commaWire.trip()) {
                    codeWriter.println(',');
                }
                codeWriter.print(column.getColumnName());
                codeWriter.print(" = ?");
            }
        }
        codeWriter.println();
        codeWriter.outdent();
        codeWriter.println("where");
        codeWriter.indent();

        Wire andWire = new Wire();
        for (Column column : columns) {
            if (column.isPrimaryKey()) {
                if (andWire.trip()) {
                    codeWriter.println(" and");
                }
                codeWriter.print(column.getColumnName());
                codeWriter.print(" = ?");
            }
        }
        codeWriter.println();
        codeWriter.close();
        return baos.toString();
    }

    public void setObjects(PreparedStatement preparedStatement, Prospect prospect, Row row) throws SQLException {
        List<Column> columns = prospect.getSelectColumns();

        int index = 1;
        int col = 0;
        for (Column column : columns) {
            if (!column.isPrimaryKey()) {
                Core.getTrace().info(index, col, row.get(col).toString(), row.get(col).getClass().getName());
                preparedStatement.setObject(index++, row.get(col));
            }
            col++;
        }
        col = 0;
        for (Column column : columns) {
            if (column.isPrimaryKey()) {
                Core.getTrace().info(index, col, row.get(col).toString(), row.get(col).getClass().getName());
                preparedStatement.setObject(index++, row.get(col));
            }
            col++;
        }
    }
}
