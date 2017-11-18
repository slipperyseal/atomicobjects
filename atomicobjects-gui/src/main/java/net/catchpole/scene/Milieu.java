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


import com.jogamp.opengl.GL;

import com.jogamp.opengl.glu.GLU;

/**
 */
public interface Milieu {
    /**
     * The JOGL GL instance to render against.
     * @return GL
     */
    public GL getGL();
    
    public GLU getGLU();

    /**
     * Returns a time value on which to base rendering positions and transitions.
     * It may or may not be based on the current time.
     * It is supplied via this interface so that non-real time rendering can occur.
     * The time value may not be real time.
     * The time may be equal to or greater than the time returned on the last render.
     * @return time
     */
    public long getRenderTime();
}
