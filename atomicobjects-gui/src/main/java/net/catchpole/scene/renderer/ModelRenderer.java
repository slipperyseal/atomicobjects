package net.catchpole.scene.renderer;

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

import net.catchpole.model.Model;
import net.catchpole.model.ValueModel;
import net.catchpole.scene.Milieu;
import net.catchpole.scene.Renderer;
import net.catchpole.scene.effigy.Effigy;
import net.catchpole.scene.effigy.EffigySource;
import net.catchpole.scene.effigy.ModelEffigySource;
import net.catchpole.scene.overlay.ImageOverlay;
import net.catchpole.scene.overlay.ListOverlay;
import net.catchpole.scene.overlay.OverlayManager;
import net.catchpole.scene.spacial.Coordinate3D;
import net.catchpole.scene.spacial.Position;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class ModelRenderer implements Renderer {
    private static final float[] lightPosition = {-45.f, 60.f, 70.0f, 0.f};
    private static final float[] lightAmbient = {0.110f, 0.110f, 0.110f, 1.f};
    private static final float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.f};
    private static final float[] materialSpec = {1.0f, 1.0f, 1.0f, 0.0f};
    private static final float[] zeroVec4 = {0.0f, 0.0f, 0.0f, 0.f};

    private final OverlayManager overlayManager;
    private final Model rootModel;
    private final EffigySource effigySource = new ModelEffigySource();
    private final Coordinate3D zoom = new Coordinate3D(0f, 0f, -100f);

    public ModelRenderer(Model model) {
        this.rootModel = model;
        this.overlayManager = new OverlayManager();
    }

    public OverlayManager getOverlayManager() {
        return overlayManager;
    }

    public void init(Milieu milieu) {
        this.overlayManager.addOverlay(new ListOverlay(rootModel));
        this.overlayManager.addOverlay(new ImageOverlay("./test.jpg"));
    }

    public void resize(Milieu milieu, int width, int height) {
        overlayManager.resize(milieu, width, height);

        GL2 gl2 = milieu.getGL().getGL2();

        float aspect = height != 0 ? (float) width / (float) height : 1.0f;

        gl2.glViewport(0, 0, width, height);
        gl2.glScissor(0, 0, width, height);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, zeroVec4, 0);
        gl2.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, materialSpec, 0);

        gl2.glEnable(GL2.GL_NORMALIZE);
        gl2.glEnable(GL2.GL_LIGHTING);
        gl2.glEnable(GL2.GL_LIGHT0);
        gl2.glEnable(GL2.GL_COLOR_MATERIAL);
        gl2.glEnable(GL.GL_CULL_FACE);

        gl2.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_FASTEST);

        gl2.glShadeModel(GL2.GL_SMOOTH);
        gl2.glDisable(GL.GL_DITHER);

        gl2.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl2.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl2.glEnableClientState(GL2.GL_NORMAL_ARRAY);
        gl2.glEnableClientState(GL2.GL_COLOR_ARRAY);

        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        milieu.getGLU().gluPerspective(50.0f, aspect, 0.5f, 2900.0f);
        gl2.glCullFace(GL.GL_BACK);
    }

    public Coordinate3D getZoom() {
        return zoom;
    }

    public void render(Milieu milieu) {
        GL2 gl2 = milieu.getGL().getGL2();

        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);

        // single entry ValueModel.  render loop will not render the root node
        ValueModel valueModel = new ValueModel(null, null);
        valueModel.addChild(rootModel);
        render(valueModel, milieu, zoom, 0);

        overlayManager.render(milieu);
    }

    private void render(Model model, Milieu milieu, Coordinate3D source, int depth) {
        float spin1 = (((milieu.getRenderTime() & 0xffff) * (1.0f / (float) 0xffff))) + (depth * 0.2f) * 360f;
        float spin2 = (((milieu.getRenderTime() & 0xfff) * (1.0f / (float) 0xfff))) * 360f;

        int count = 0;
        for (Model subModel : model) {
            count++;
        }

        int index = 0;
        for (Model subModel : model) {
            int objectSpacing = (subModel.iterator().hasNext() ? 150 : 100);
            float rotation = (((float)Math.PI * 2) / ((float) count) * index++) +
                    (1.0f * depth); // offset to stop overlap

            Coordinate3D target = new Coordinate3D(
                    source.getX() + (objectSpacing * (float)Math.sin(rotation)),
                    source.getY() + (objectSpacing * (float)Math.cos(rotation)),
                    source.getZ());

            Effigy effigy = effigySource.getIdiograph(subModel.getType());
            effigy.render(milieu, new Position(target, spin1, spin2));

            render(subModel, milieu, target, depth + 1);
        }
    }

    public void destory(Milieu milieu) {
        effigySource.destroy(milieu);
    }
}
