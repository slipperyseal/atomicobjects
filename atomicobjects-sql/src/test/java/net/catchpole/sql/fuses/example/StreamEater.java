package net.catchpole.sql.fuses.example;

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

import net.catchpole.fuse.annotation.Fuse;
import net.catchpole.fuse.annotation.Input;

import java.io.IOException;
import java.io.InputStream;

@Fuse
public class StreamEater {
    private final String value;

    @Input
    public StreamEater(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = is.read()) != -1) {
            sb.append((char) c);
        }
        value = sb.toString();
    }

    public String toString() {
        return '\"' + value + '\"';
    }
}
