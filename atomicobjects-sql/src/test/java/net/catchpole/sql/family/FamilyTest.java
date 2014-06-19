package net.catchpole.sql.family;

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
import net.catchpole.sql.meta.ColumnType;
import net.catchpole.sql.meta.TableType;
import net.catchpole.trace.Core;
import org.junit.Test;

public class FamilyTest {
    private static final Database database = AirlineDatabase.create();

    @Test
    public void testFamily() {
        FamilyLocator familyLocator = new FamilyLocator();
        Family family = familyLocator.findFamily(database);

        TableStatementBuilder tableStatementBuilder = family.getTableStatementBuilder();

        TableType tableType = new TableType("MrTable");
        tableType.addColumnType(new ColumnType("one", "int", 6, false, true));
        tableType.addColumnType(new ColumnType("two", "varchar", 100, true, false));
        tableType.addColumnType(new ColumnType("three", "int", 10, false, false));

        Core.getTrace().info(tableStatementBuilder.createCreateStatement(tableType));
    }
}
