package net.catchpole.scene;

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

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class SceneMilieu implements Milieu {
    private final GL gl;
    private final GLU glu = new GLU();
    private final long time = System.currentTimeMillis();

    public SceneMilieu(GL gl) {
        this.gl = gl;
    }

    public GL getGL() {
        return gl;
    }

    public GLU getGLU() {
        return glu;
    }

    public long getRenderTime() {
        return time;
    }
}
