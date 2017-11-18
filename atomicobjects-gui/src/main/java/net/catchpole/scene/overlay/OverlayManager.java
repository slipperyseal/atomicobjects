package net.catchpole.scene.overlay;

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
import net.catchpole.scene.Resizable;
import net.catchpole.scene.spacial.Coordinate2D;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OverlayManager implements Resizable, Iterable<Overlay> {
    private final int border = 25;
    private final List<Overlay> overlays = new ArrayList<Overlay>();
    private int width;
    private int height;

    public OverlayManager() {
    }

    public Iterator<Overlay> iterator() {
        return overlays.iterator();
    }

    public void addOverlay(Overlay overlay) {
        overlays.add(overlay);
    }

    public void resize(Milieu milieu, int width, int height) {
        this.width = width;
        this.height = height;

        float x = border;
        for (Overlay overlay : overlays) {
            Coordinate2D coordinate2D = overlay.getCoordinate2D();
            coordinate2D.setX(x);
            coordinate2D.setY(height - border - overlay.getHeight());
            x += overlay.getWidth() + border;
        }
    }

    public void render(Milieu milieu) {
        GL2 gl2 = milieu.getGL().getGL2();

        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glPushMatrix();
        gl2.glLoadIdentity();
        milieu.getGLU().gluOrtho2D(0, width, 0, height);
        gl2.glScalef(1, -1, 1);
        gl2.glTranslatef(0, -height, 0);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glPushMatrix();
        gl2.glLoadIdentity();

        gl2.glEnable(GL.GL_BLEND);
        gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        for (Overlay overlay : overlays) {
            overlay.render(milieu);
        }

        gl2.glPopMatrix();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glPopMatrix();
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
    }
}
