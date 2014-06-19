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

import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 */
public class GenericInsert implements StatementBuilder {
    public String createStatement(Prospect prospect) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamCodeWriter codeWriter = new OutputStreamCodeWriter(baos);

        codeWriter.println("insert into ");
        codeWriter.indent();
        codeWriter.println(prospect.getTable().getTableName());

        // values
        List<Column> columns = prospect.getSelectColumns();

        codeWriter.indent();
        codeWriter.print('(');

        Wire commaWire = new Wire();
        for (Column column : columns) {
            if (commaWire.trip()) {
                codeWriter.println(',');
            }
            codeWriter.print(column.getColumnName());
        }
        codeWriter.println(')');
        codeWriter.outdent();
        codeWriter.println("values");
        codeWriter.indent();

        codeWriter.print('(');
        Wire wire = new Wire();
        int cols = columns.size();
        for (int x = 0; x < cols; x++) {
            if (wire.trip()) {
                codeWriter.print(',');
            }
            codeWriter.print('?');
        }
        codeWriter.println(')');

        codeWriter.close();
        return baos.toString();
    }

    public void setObjects(PreparedStatement preparedStatement, Prospect prospect, Row row) throws SQLException {
    }
}
