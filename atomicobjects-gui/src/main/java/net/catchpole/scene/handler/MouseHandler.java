package net.catchpole.scene.handler;

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

import com.jogamp.newt.Window;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import net.catchpole.scene.Renderer;
import net.catchpole.scene.overlay.Overlay;
import net.catchpole.scene.spacial.Coordinate2D;
import net.catchpole.scene.spacial.Coordinate3D;

public class MouseHandler implements MouseListener {
    private final Window window;
    private final Renderer renderer;
    private Coordinate2D move;
    private Coordinate2D last;

    public MouseHandler(Window window, Renderer renderer) {
        this.window = window;
        this.renderer = renderer;
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        window.setFullscreen(!window.isFullscreen());
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void mousePressed(MouseEvent mouseEvent) {
        last = new Coordinate2D(mouseEvent.getX(), mouseEvent.getY());
        for (Overlay overlay : renderer.getOverlayManager()) {
            Coordinate2D coordinate2D = overlay.getCoordinate2D();
            if (mouseEvent.getX() >= coordinate2D.getX() &&
                    mouseEvent.getY() >= coordinate2D.getY() &&
                    mouseEvent.getX() <= (coordinate2D.getX() + overlay.getWidth()) &&
                    mouseEvent.getY() <= (coordinate2D.getY() + overlay.getHeight())) {

                move = coordinate2D;
                return;
            }

        }
        move = renderer.getZoom();
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseMoved(MouseEvent mouseEvent) {
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (move != null && last != null) {
            Coordinate2D current = new Coordinate2D(mouseEvent.getX(), mouseEvent.getY());
            Coordinate2D diff = new Coordinate2D(last);
            diff.subtract(current);
            move.subtract(diff);
            last = current;
        }
    }

    public void mouseWheelMoved(MouseEvent mouseEvent) {
        Coordinate3D zoom = renderer.getZoom();
        zoom.setZ(zoom.getZ() - 1);
    }
}
