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

import net.catchpole.image.ImageUtils;
import net.catchpole.lang.Throw;
import net.catchpole.scene.Milieu;
import net.catchpole.scene.gl.BlitImage;
import net.catchpole.scene.spacial.Coordinate2D;

import java.io.File;

public class ImageOverlay implements Overlay {
    private final Coordinate2D coordinate2D = new Coordinate2D(0, 0);
    private BlitImage blitImage;

    public ImageOverlay(String filename) {
        try {
            blitImage = new BlitImage(ImageUtils.loadImage(new File(filename)));
        } catch (Exception e) {
            throw Throw.unchecked(e);
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
