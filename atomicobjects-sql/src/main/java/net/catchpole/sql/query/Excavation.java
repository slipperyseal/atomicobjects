package net.catchpole.sql.query;

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

import net.catchpole.sql.data.Row;
import net.catchpole.sql.data.RowSet;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Excavation {
    private final Prospect prospect;
    private RowSet rowSet;
    private Object[] criteria;

    public Excavation(Prospect prospect) throws SQLException {
        this.prospect = prospect;
    }

    public Prospect getProspect() {
        return prospect;
    }

    public void setCriteria(Object... criteria) {
        this.criteria = criteria;
    }

    public ResultSet getResultSet() throws SQLException {
        setCriteria();
        return prospect.getSelectPreparedStatement().executeQuery();
    }

    public RowSet getRowSet() throws SQLException {
        if (rowSet == null) {
            setCriteria();
            ResultSet rs = prospect.getSelectPreparedStatement().executeQuery();
            try {
                this.rowSet = new RowSet(rs);
            } finally {
                rs.close();
            }
        }
        return rowSet;
    }

    private void setCriteria() throws SQLException {
        if (criteria != null) {
            for (int x = 0; x < criteria.length; x++) {
                prospect.getSelectPreparedStatement().setObject(x + 1, criteria[x]);
            }
        }
    }

    public void update() throws SQLException {
        for (Row row : rowSet) {
            if (row.hasChanged()) {
                prospect.getTable().getDatabase().getFamily().getUpdateBuilder().setObjects(
                        prospect.getUpdatePreparedStatement(), prospect, row);
                prospect.getUpdatePreparedStatement().executeUpdate();
            }
        }
    }
}
