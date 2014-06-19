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

import net.catchpole.sql.meta.Column;
import net.catchpole.sql.meta.Table;
import net.catchpole.trace.Core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A set of criteria, tables and columns to operate on which is used to output appropriate SQL
 * selects, updates and inserts.
 */
public class Prospect {
    private final Table table;
    private final List<Column> columns = new ArrayList<Column>();
    private final List<Column> criteria = new ArrayList<Column>();
    private final List<Column> orderColumns = new ArrayList<Column>();
    private PreparedStatement selectPs;
    private PreparedStatement updatePs;

    public Prospect(Table table) {
        this.table = table;
    }

    public PreparedStatement getSelectPreparedStatement() throws SQLException {
        if (this.selectPs == null) {
            String statement = table.getDatabase().getFamily().getSelectBuilder().createStatement(this);
            Core.getTrace().info(statement);
            this.selectPs = table.getDatabase().getConnection().prepareStatement(statement);
        }
        return this.selectPs;
    }

    public PreparedStatement getUpdatePreparedStatement() throws SQLException {
        if (this.updatePs == null) {
            String statement = table.getDatabase().getFamily().getUpdateBuilder().createStatement(this);
            Core.getTrace().info(statement);
            this.updatePs = table.getDatabase().getConnection().prepareStatement(statement);
        }
        return this.updatePs;
    }

    public void addSelectColumn(Column column) {
        this.columns.add(column);
    }

    public void addWhereCriteria(Column column) {
        this.criteria.add(column);
    }

    public List<Column> getOrderColumns() {
        return orderColumns;
    }

    public void addOrderColumn(Column orderColumn) {
        this.orderColumns.add(orderColumn);
    }

    public Table getTable() {
        return table;
    }

    /**
     * Returns the columns defined for a selection, or all the columns for the table if no
     * select columns have been set.
     */
    public List<Column> getSelectColumns() {
        return columns.size() == 0 ? table.getColumns() : columns;
    }

    public List<Column> getCriteria() {
        return criteria;
    }
}
