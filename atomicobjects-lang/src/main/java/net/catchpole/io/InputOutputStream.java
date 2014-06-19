package net.catchpole.io;

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

import net.catchpole.lang.Disposable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An interface which defines that an object can return Input and Output streams or throw an IOException if
 * a stream is not available at the time of the call.  It is not defined when and if both or either of the streams
 * should be available as this is determined by the implementation.
 * Repeated calls to getOutputStream should invalidate (write over) previous data.  Repeated calls to getInputStream
 * should return new streams set to the start of the data.  It is likely, but not defined by this interface,
 * that implementations support mutiple InputStreams reading the same data.
 */
public interface InputOutputStream extends InputStreamSource, OutputStreamSource, Disposable {
    public InputStream getInputStream() throws IOException;

    public OutputStream getOutputStream() throws IOException;

    public void dispose();
}
