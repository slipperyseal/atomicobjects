package net.catchpole.classpath;

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
import java.util.Iterator;
import java.util.Map;

public class ClasspathClasses implements Classes {
    private final Map<String, Class> map = new HashMap<String, Class>();

    public ClasspathClasses(String[] names) throws ClassNotFoundException {
        for (String name : names) {
            addClass(Class.forName(name));
        }
    }

    public ClasspathClasses(Class[] classes) throws ClassNotFoundException {
        for (Class clazz : classes) {
            addClass(clazz);
        }
    }

    public Class getClass(String name) throws ClassNotFoundException {
        return map.get(name);
    }

    public void addClass(Class clazz) {
        map.put(clazz.getName(), clazz);
    }

    public void removeClass(Class clazz) {
        map.remove(clazz.getName());
    }

    public Iterator<Class> iterator() {
        return map.values().iterator();
    }
}
