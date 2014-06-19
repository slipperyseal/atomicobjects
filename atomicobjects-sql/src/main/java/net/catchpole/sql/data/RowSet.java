package net.catchpole.sql.data;

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

import net.catchpole.sql.meta.DatabaseMetaTools;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RowSet implements Iterable<Row>, Serializable {
    private final List<Row> rows = new ArrayList<Row>();
    private List<String> columnNames;
    private final int width;

    public RowSet(ResultSet rs) throws SQLException {
        this(rs.getMetaData().getColumnCount());
        addAllRows(rs);
    }

    public RowSet(int width) {
        this.width = width;
    }

    public void addAllRows(ResultSet resultSet) throws SQLException {
        this.columnNames = DatabaseMetaTools.getResultColumnNames(resultSet);
        //resultSet.beforeFirst();
        while (resultSet.next()) {
            addRow(new Row(resultSet, this.width));
        }
    }

    public void addRow(Row row) {
        this.rows.add(row);
    }

    public Iterator<Row> iterator() {
        return rows.iterator();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append('\r');
        sb.append('\n');
        for (String columnName : columnNames) {
            sb.append(columnName);
            sb.append(',');
            sb.append(' ');
        }
        sb.append('\r');
        sb.append('\n');
        for (Row row : rows) {
            if (row != null) {
                sb.append(row);
                sb.append('\r');
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
