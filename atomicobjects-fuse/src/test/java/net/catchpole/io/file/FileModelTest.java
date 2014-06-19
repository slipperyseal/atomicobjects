package net.catchpole.io.file;

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

import net.catchpole.model.Model;
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;
import org.junit.Test;

import java.io.File;

public class FileModelTest {
    private Trace trace = Core.getTrace();

    @Test
    public void testFileModel() {

        recurse(new FileModel(new File("./src/test/net")), trace);
    }

    public void recurse(Model model, Trace trace) {
        trace.info(model.getName());

        Trace subTrace = trace.drill();
        for (Model subModel : model) {
            recurse(subModel, subTrace);
        }
    }
}
