package net.catchpole.sql;

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

import net.catchpole.lang.KeyedSource;
import net.catchpole.model.Model;
import net.catchpole.model.iterator.ModelIterator;
import net.catchpole.sql.family.Family;
import net.catchpole.sql.family.FamilyLocator;
import net.catchpole.sql.meta.DatabaseMetaTools;
import net.catchpole.sql.meta.Table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Database implements KeyedSource<String, Table>, Model {
    private static final FamilyLocator FAMILY_LOCATOR = new FamilyLocator();
    private final String productName;
    private final Family family;
    private final List<Table> tables = new ArrayList<Table>();
    private final DatabaseMetaData databaseMetaData;
    private final Connection connection;
    private final String schemaPattern;

    public Database(Connection connection, String schemaPattern) throws SQLException {
        this.connection = connection;
        this.databaseMetaData = connection.getMetaData();
        this.schemaPattern = schemaPattern;

        this.productName = databaseMetaData.getDatabaseProductName();
        this.family = FAMILY_LOCATOR.findFamily(this);

        reloadTables();
    }

    public Database(String jndiName) {
        throw new RuntimeException();
    }

    public void reloadTables() throws SQLException {
        ResultSet resultSet = databaseMetaData.getTables(null, schemaPattern, "%", new String[]{"TABLE"});

        try {
            tables.clear();
            while (resultSet.next()) {
                Table table = new Table(this, resultSet, databaseMetaData);

                // ignore pseudo tables
                if (!DatabaseMetaTools.isPsudoTable(table)) {
                    tables.add(table);
                }
            }
        } finally {
            resultSet.close();
        }
    }

    public List<Table> getTables() {
        return tables;
    }

    public Table get(String tableName) {
        return getTable(tableName);
    }

    public Table getTable(String name) {
        for (Table table : tables) {
            if (table.getTableName().equalsIgnoreCase(name)) {
                return table;
            }
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean executeStatement(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            return statement.execute(sql);
        } finally {
            statement.close();
        }
    }

    public DatabaseMetaData getDatabaseMetaData() {
        return databaseMetaData;
    }

    public Family getFamily() {
        return family;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + productName + ' ' + tables.size() + " tables";
    }

    // model

    public String getName() {
        return productName;
    }

    public Class getType() {
        return Database.class;
    }

    public Iterator getValues() {
        return null;
    }

    public Iterator<Model> iterator() {
        return new ModelIterator(tables.iterator());
    }
}

