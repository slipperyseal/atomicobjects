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
import net.catchpole.sql.data.Row;
import net.catchpole.sql.meta.Column;
import net.catchpole.sql.query.Excavation;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ExcavationExport {
    private final Excavation excavation;
    private final ValueModel valueModel;

    public ExcavationExport(Excavation excavation) throws SQLException {
        this.excavation = excavation;
        this.valueModel = new ValueModel(excavation.getProspect().getTable().getDatabase().getName(), null);

        for (Row row : excavation.getRowSet()) {
            Set<String> visitedTables = new HashSet<String>();
            visitedTables.add(excavation.getProspect().getTable().getName());
            // add primary select table
            ValueModel nestModel = addTable(valueModel, excavation.getProspect().getTable().getName(), row);

            // add all other tables within it
            for (Column tableColumn : excavation.getProspect().getSelectColumns()) {
                if (visitedTables.add(tableColumn.getTable().getName())) {
                    addTable(nestModel, tableColumn.getTable().getName(), row);
                }
            }
        }
    }

    public ValueModel getValueModel() {
        return valueModel;
    }

    private ValueModel addTable(ValueModel addTag, String tableName, Row row) {
        ValueModel tableModel = new ValueModel(tableName.toLowerCase(), null);
        addTag.addChild(tableModel);
        int index=0;
        for (Column column : excavation.getProspect().getSelectColumns()) {
            if (column.getTable().getName().equals(tableName)) {
                ValueModel columnModel = new ValueModel(column.getName().toLowerCase(), row.get(index));
                tableModel.addChild(columnModel);
            }
            index++;
        }
        return tableModel;
    }
}
