package net.catchpole;

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
import net.catchpole.io.file.FileModel;
import net.catchpole.model.Model;
import net.catchpole.model.ModelSource;
import net.catchpole.model.ValueModel;
import net.catchpole.sql.Database;
import net.catchpole.trace.Core;

import java.io.File;
import java.sql.DriverManager;

public class MasterModelSource implements ModelSource {
    private final ValueModel valueModel = new ValueModel("root", null);

    public MasterModelSource() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            Database database = new Database(DriverManager.getConnection("jdbc:hsqldb:mem:Airline", "sa", ""), "%");
            database.executeStatement(new String(Files.loadResource(Database.class, "AirlinesTest.sql")));
            database.reloadTables();
            valueModel.addChild(database);
        } catch (Throwable e) {
            Core.getTrace().warning(e);
            // drop back to something less exciting
            valueModel.addChild(new FileModel(new File("./src/")));
        }
    }

    public Model get() {
        return valueModel;
    }
}
