package net.catchpole.trace;

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

public interface Trace {
    public Trace drill();

    public Trace drill(Object object);

    public void demote();

    public void promote();

    public void info(Object object);

    public void info(Object... list);

    public void warning(Object object);

    public void warning(Object... list);

    public void error(Object object);

    public void error(Object... list);

    public void critical(Object object);

    public void critical(Object... list);
}
