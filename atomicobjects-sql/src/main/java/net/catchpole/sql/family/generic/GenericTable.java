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
import net.catchpole.sql.family.TableStatementBuilder;
import net.catchpole.sql.meta.ColumnType;
import net.catchpole.sql.meta.TableType;

import java.io.ByteArrayOutputStream;

public class GenericTable implements TableStatementBuilder {
    public String createDropStatement(TableType tableType) {
        return "drop table " + tableType.getName();
    }

    public String createCreateStatement(TableType tableType) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamCodeWriter codeWriter = new OutputStreamCodeWriter(baos);

        codeWriter.println("create table ");
        codeWriter.indent();
        codeWriter.print(tableType.getName());
        codeWriter.println(" (");
        codeWriter.indent();
        Wire wire = new Wire();
        for (ColumnType column : tableType.getColumnTypes()) {
            if (wire.trip()) {
                codeWriter.println(',');
            }

            codeWriter.print(column.getName());
            codeWriter.print(' ');
            codeWriter.print(column.getType());
            int size = column.getSize();
            if (size != -1) {
                codeWriter.print('(');
                codeWriter.print(size);
                codeWriter.print(')');
            }
            if (column.isNullable()) {
                codeWriter.print(" NOT NULL");
            }
        }
        codeWriter.println();
        codeWriter.outdent();
        codeWriter.println("PRIMARY KEY");
        codeWriter.indent();

        codeWriter.print('(');
        Wire commaWire = new Wire();
        for (ColumnType column : tableType.getColumnTypes()) {
            if (column.isPrimaryKey()) {
                if (commaWire.trip()) {
                    codeWriter.println(',');
                }
                codeWriter.print(column.getName());
            }
        }
        codeWriter.println(")");
        codeWriter.outdent();
        codeWriter.println(")");
        codeWriter.close();
        return baos.toString();
    }
}
