package net.catchpole.build;

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

import junit.framework.TestCase;
import net.catchpole.trace.Core;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DependencyFinderTest {
    //@Test
    public void testDependencies() throws IOException {
        DependencyFinder dependencyFinder = new DependencyFinder(new File("src/main/java"), "net.catchpole", true);

        List<String> list = dependencyFinder.checkLocalized("net.catchpole");
        if (list != null) {
            for (String string : list) {
                Core.getTrace().info(string);
            }
            for (String string : list) {
                // fail if anything referenced outside its package
                TestCase.fail(string);
            }
        }
    }
}
