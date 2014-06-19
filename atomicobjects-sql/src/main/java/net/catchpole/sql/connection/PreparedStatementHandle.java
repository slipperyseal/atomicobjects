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
import java.sql.PreparedStatement;

public class PreparedStatementHandle implements Comparable<PreparedStatementHandle> {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private String sql;

    public PreparedStatementHandle(Connection connection, PreparedStatement preparedStatement, String sql) {
        this.connection = connection;
        this.preparedStatement = preparedStatement;
        this.sql = sql;
    }

    public boolean equals(Object object) {
        if (!(object instanceof PreparedStatementHandle)) {
            return false;
        }
        PreparedStatementHandle other = (PreparedStatementHandle) object;
        return (other.sql.equals(this.sql));
    }


    public int compareTo(PreparedStatementHandle preparedStatementHandle) {
        return this.sql.compareTo(preparedStatementHandle.sql);
    }
}
