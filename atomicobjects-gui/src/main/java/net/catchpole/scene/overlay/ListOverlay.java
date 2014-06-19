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

import net.catchpole.model.Model;
import net.catchpole.scene.Milieu;
import net.catchpole.scene.awt.IterableAwtOverlay;
import net.catchpole.scene.awt.OverlayExport;
import net.catchpole.scene.gl.BlitImage;
import net.catchpole.scene.spacial.Coordinate2D;

import java.awt.*;

public class ListOverlay implements Overlay {
    private final Coordinate2D coordinate2D = new Coordinate2D(0, 0);
    private final BlitImage blitImage;

    public ListOverlay(Model model) {
        IterableAwtOverlay iterableOverlay = new IterableAwtOverlay(new Color(200, 200, 0));
        add(iterableOverlay, model, 0);
        this.blitImage = new BlitImage(new OverlayExport().export(iterableOverlay));
    }

    private void add(IterableAwtOverlay iterableOverlay, Model model, int indent) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < indent; x++) {
            sb.append("  ");
        }
        iterableOverlay.addItem(sb + model.toString());
        for (Model sub : model) {
            add(iterableOverlay, sub, indent + 1);
        }
    }

    public Coordinate2D getCoordinate2D() {
        return coordinate2D;
    }

    public void render(Milieu milieu) {
        blitImage.draw(milieu.getGL().getGL2(), (int)coordinate2D.getX(), (int)coordinate2D.getY());
    }

    public int getWidth() {
        return blitImage.getWidth();
    }

    public int getHeight() {
        return blitImage.getHeight();
    }
}
