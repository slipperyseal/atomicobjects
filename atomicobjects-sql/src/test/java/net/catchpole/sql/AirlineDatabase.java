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

import net.catchpole.io.Files;
import net.catchpole.lang.Throw;

import java.io.File;
import java.sql.DriverManager;

public class AirlineDatabase {
    private static int count = 0;

    public static Database create() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            Database database = new Database(DriverManager.getConnection("jdbc:hsqldb:mem:Airline-" + count++, "sa", ""), "%");
            database.executeStatement(new String(Files.loadFile(new File("../atomicobjects-demo/src/main/resources/net/catchpole/sql/AirlinesTest.sql"))));
            database.reloadTables();
            return database;
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }
}
