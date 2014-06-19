package net.catchpole.web.log;

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

import java.util.Date;

public class WebLogEntryTest {
    @Test
    public void testWebLogEntry() {
        WebLogEntry web = new WebLogEntry("localhost", "user", "authUser", new Date(1000000), "GET /page.html HTTP/1.1",
                200, 1024, "http://refer/", "agent");
        TestCase.assertEquals("localhost user authUser [01/Jan/1970:10:16:40] \"GET /page.html HTTP/1.1\" 200 1024 \"http://refer/\" \"agent\"",
                removeTimeZoneOffset(web.toString()));

        // test with nulls
        web = new WebLogEntry("localhost", null, null, new Date(1000000), "GET /page.html HTTP/1.1", 200, 1024, null, null);
        TestCase.assertEquals("localhost - - [01/Jan/1970:10:16:40] \"GET /page.html HTTP/1.1\" 200 1024 - -",
                removeTimeZoneOffset(web.toString()));
    }

    /**
     * Time zone will be different depending on where you are,
     * so we remove the offset, so its not part of the test.
     * <p>turn [01/Jan/1970:10:16:40 +1000]
     * <p>into [01/Jan/1970:10:16:40]
     */
    private static String removeTimeZoneOffset(String line) {
        return line.substring(0, line.indexOf(' ', line.indexOf('['))) + line.substring(line.indexOf(']'));
    }
}
