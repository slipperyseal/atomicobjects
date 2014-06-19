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

import net.catchpole.sql.AirlineDatabase;
import net.catchpole.sql.Database;
import net.catchpole.sql.meta.Table;
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;
import org.junit.Test;

public class TableFuseTest {
    private static final Database database = AirlineDatabase.create();
    private Trace trace = Core.getTrace();

    @Test
    public void testTableFuse() throws Exception {
        Table table = database.getTable("schedule");

        TableFuse tableFuse = new TableFuse(table, table.getColumn("call_sign"), table.getColumn("arrive_time"));
        trace.info(tableFuse.involk(new Object[]{"VH123"}));
    }
}
