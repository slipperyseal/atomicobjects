package net.catchpole.sql.fuses;

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

import net.catchpole.fuse.Fuse;
import net.catchpole.fuse.criteria.Criteria;
import net.catchpole.lang.Throw;
import net.catchpole.sql.meta.Column;
import net.catchpole.sql.meta.DatabaseMetaTools;
import net.catchpole.sql.meta.Table;
import net.catchpole.sql.query.Excavation;
import net.catchpole.sql.query.Prospect;

import java.sql.ResultSet;

public class TableFuse implements Fuse {
    private final Table table;
    private final Column parameterColumn;
    private final Column resultColumn;
    private final Criteria inputCriteria;
    private final Criteria outputCriteria;
    private final Prospect prospect;

    public TableFuse(Table table, Column whereColumn, Column resultColumn) throws Exception {
        this.table = table;
        this.parameterColumn = whereColumn;
        this.resultColumn = resultColumn;
        this.inputCriteria = new Criteria(whereColumn.getName().toLowerCase(),
                Class.forName(DatabaseMetaTools.getJavaMappingType(whereColumn)));
        this.outputCriteria = new Criteria(Class.forName(DatabaseMetaTools.getJavaMappingType(resultColumn)));
        this.prospect = new Prospect(table);
        this.prospect.addWhereCriteria(whereColumn);
    }

    public Criteria getOutputCriteria() {
        return outputCriteria;
    }

    public Criteria getInputCriteria() {
        return inputCriteria;
    }

    public Object[] involk(Object[] criteria) {
        try {
            Excavation excavation = new Excavation(prospect);
            excavation.setCriteria(criteria);
            ResultSet resultSet = excavation.getResultSet();
            try {
                if (resultSet.next()) {
                    return new Object[] { resultSet.getObject(resultColumn.getColumnName()) };
                } else {
                    throw new IllegalArgumentException("No results for " + criteria);
                }
            } finally {
                resultSet.close();
            }
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + parameterColumn + " > " + resultColumn;
    }
}
