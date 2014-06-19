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
import net.catchpole.scene.gl.GLTools;
import net.catchpole.scene.spacial.Position;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

public class Cube implements Effigy {
    private static final byte[] indices = {
            0, 3, 1, 2, 0, 1, 6, 5, 4, 5, 7, 4, 8, 11, 9, 10, 8, 9,
            15, 12, 13, 12, 14, 13, 16, 19, 17, 18, 16, 17, 23, 20, 21, 20, 22, 21
    };

    private static final byte[] normals = {
            0, 0, 127, 0, 0, 127, 0, 0, 127, 0, 0, 127,
            0, 0, -128, 0, 0, -128, 0, 0, -128, 0, 0, -128,
            0, -128, 0, 0, -128, 0, 0, -128, 0, 0, -128, 0,
            0, 127, 0, 0, 127, 0, 0, 127, 0, 0, 127, 0,
            127, 0, 0, 127, 0, 0, 127, 0, 0, 127, 0, 0,
            -128, 0, 0, -128, 0, 0, -128, 0, 0, -128, 0, 0
    };

    private static final short[] vertices = {
            -8, 8, 8, 8, -8, 8, 8, 8, 8, -8, -8, 8,
            -8, 8, -8, 8, -8, -8, 8, 8, -8, -8, -8, -8,
            -8, -8, 8, 8, -8, -8, 8, -8, 8, -8, -8, -8,
            -8, 8, 8, 8, 8, -8, 8, 8, 8, -8, 8, -8,
            8, -8, 8, 8, 8, -8, 8, 8, 8, 8, -8, -8,
            -8, -8, 8, -8, 8, -8, -8, 8, 8, -8, -8, -8
    };

    private final GLTools tools = new GLTools();
    private final ShortBuffer cubeVertices = tools.allocShorts(vertices);
    private final ByteBuffer cubeNormals = tools.allocBytes(normals);
    private final ByteBuffer cubeIndices = tools.allocBytes(indices);
    private final ByteBuffer cubeColors;

    public Cube() {
        byte[] colors = new byte[96];
        Random random = new Random(1); // constant seed will give predictable colours
        for (int x = 0; x < colors.length; x++) {
            colors[x] = (byte) random.nextInt(256);
        }
        this.cubeColors = tools.allocBytes(colors);
    }

    public Cube(Color color) {
        byte[] colors = new byte[96];
        for (int x = 0; x < colors.length / 3; x++) {
            colors[(x * 3)] = (byte) color.getRed();
            colors[(x * 3) + 1] = (byte) color.getGreen();
            colors[(x * 3) + 2] = (byte) color.getBlue();
        }
        this.cubeColors = tools.allocBytes(colors);
    }

    public void globalInit(Milieu milieu) {
    }

    public void init(Milieu milieu) {
    }

    public void render(Milieu milieu, Position position) {
        GL2 gl2 = milieu.getGL().getGL2();
        gl2.glLoadIdentity();
        gl2.glTranslatef(position.getX(), position.getY(), position.getZ());
        gl2.glRotatef(position.getRotationY(), 0, 1, 0);
        gl2.glRotatef(position.getRotationX(), 1, 0, 0);
        gl2.glRotatef(position.getRotationY(), 0, 0, 1);
        gl2.glVertexPointer(3, GL.GL_SHORT, 0, cubeVertices);
        gl2.glColorPointer(4, GL.GL_UNSIGNED_BYTE, 0, cubeColors);
        gl2.glNormalPointer(GL.GL_BYTE, 0, cubeNormals);
        gl2.glDrawElements(GL.GL_TRIANGLES, 6 * 6, GL.GL_UNSIGNED_BYTE, cubeIndices);
    }

    public void destory(Milieu milieu) {
        GL2 gl2 = milieu.getGL().getGL2();
        gl2.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl2.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl2.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl2.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        this.cubeVertices.clear();
        this.cubeColors.clear();
        this.cubeNormals.clear();
        this.cubeIndices.clear();
    }
}
