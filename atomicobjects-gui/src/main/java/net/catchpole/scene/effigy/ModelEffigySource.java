package net.catchpole.scene.effigy;

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

import net.catchpole.scene.Milieu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ModelEffigySource implements EffigySource {
    private Map<Class, Effigy> map = new HashMap<Class, Effigy>();

    public Effigy getIdiograph(Class type) {
        Effigy effigy = map.get(type);
        if (effigy == null) {
//            if (type == null) {
            effigy = new Cube();
//            } else {
//                switch (type.getSimpleName().hashCode() & 3) {
//                    case 0:
//                        component = new Cube(Color.RED);
//                        break;
//                    case 1:
//                        component = new Cube(Color.YELLOW);
//                        break;
//                    case 2:
//                        component = new Cube(Color.ORANGE);
//                        break;
//                    case 3:
//                        component = new Cube(Color.MAGENTA);
//                        break;
//                }
//            }
            map.put(type, effigy);
        }
        return effigy;
    }

    public void destroy(Milieu milieu) {
        for (Iterator<Effigy> iterator = map.values().iterator(); iterator.hasNext();) {
            iterator.next().destory(milieu);
            iterator.remove();
        }
    }
}
