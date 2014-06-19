package net.catchpole.model.handler;

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

import java.util.HashMap;
import java.util.Map;

public class TypeHandler<H> {
    private final Map<Class, H> map = new HashMap<Class, H>();
    private H defaultHandler;
    //todo: add assignable from support

    public TypeHandler() {
    }

    public void setDefaultHandler(H defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public void addHandler(Class type, H handler) {
        map.put(type, handler);
    }

    public H getHandler(Class type) {
        H handler = map.get(type);
        return handler == null ? defaultHandler : handler;
    }
}
