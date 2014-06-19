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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class TableType implements Serializable {
    private String name;
    private List<ColumnType> columnTypes = new ArrayList<ColumnType>();

    public TableType(String name) {
        this.name = name;
    }

    public void addColumnType(ColumnType columnType) {
        columnTypes.add(columnType);
    }

    public List<ColumnType> getColumnTypes() {
        return columnTypes;
    }

    public String getName() {
        return name;
    }
}
