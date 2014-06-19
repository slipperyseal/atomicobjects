package net.catchpole.sql.connection;

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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
    private ConnectionSource connectionSource;
    private int maxConnections;
    private int totalConnections;
    private List<Connection> freeConnections = new ArrayList<Connection>();
    private List<Connection> lockedConnections = new ArrayList<Connection>();

    public ConnectionManager(ConnectionSource connectionSource, int maxConnections) {
        this.connectionSource = connectionSource;
        this.maxConnections = maxConnections;
    }

    public Connection leaseConnection() throws SQLException {
        Connection connection = null;
        synchronized (freeConnections) {
            if (freeConnections.size() != 0) {
                connection = freeConnections.remove(0);
            }
        }
        if (connection == null) {
            connection = connectionSource.getConnection();
            totalConnections++;
        }
        synchronized (lockedConnections) {
            lockedConnections.add(connection);
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {
        synchronized (lockedConnections) {
            lockedConnections.remove(connection);
        }
        synchronized (freeConnections) {
            freeConnections.add(connection);
        }
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + connectionSource + ' ' + totalConnections + ':' + maxConnections;
    }
}
