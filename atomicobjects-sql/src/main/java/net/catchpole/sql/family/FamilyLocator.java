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

import net.catchpole.sql.Database;
import net.catchpole.sql.family.generic.GenericFamily;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class FamilyLocator {
    private List<Family> familyList = new ArrayList<Family>();

    public FamilyLocator() {
        familyList.add(new GenericFamily());
    }

    public Family findFamily(Database database) {
        for (Family family : familyList) {
            if (family.supportsDatabase(database.getName())) {
                return family;
            }
        }
        throw new RuntimeException("Unsupported database: " + database.getName());
    }
}
