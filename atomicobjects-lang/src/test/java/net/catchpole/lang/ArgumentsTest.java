package net.catchpole.lang;

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
import org.junit.Test;

public class ArgumentsTest {
    @Test
    public void testArguments() {
        Arguments arguments = new Arguments(new String[]{"-a", "-b", "1", "2", "-c"});
        String[] args = arguments.getArgumentArray("-b");
        TestCase.assertNotNull(args);
        TestCase.assertEquals(2, args.length);
        TestCase.assertEquals("1", args[0]);
        TestCase.assertEquals("2", args[1]);

        args = arguments.getArgumentArray("-d");
        TestCase.assertNotNull(args);
        TestCase.assertEquals(0, args.length);

        TestCase.assertEquals("Arguments: -a -b 1 2 -c", arguments.toString());
    }
}
