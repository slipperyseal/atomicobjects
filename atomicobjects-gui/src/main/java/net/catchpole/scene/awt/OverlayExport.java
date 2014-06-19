package net.catchpole.scene.awt;

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

import java.awt.*;
import java.awt.image.BufferedImage;

public class OverlayExport {
    public BufferedImage export(AwtOverlay awtOverlay) {
        Graphics sizingGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR_PRE).getGraphics();
        try {
            awtOverlay.calculateSize(sizingGraphics);
        } finally {
            sizingGraphics.dispose();
        }

        BufferedImage bufferedImage = new BufferedImage(awtOverlay.getWidth() + (awtOverlay.getBorder() * 2),
                awtOverlay.getHeight() + (awtOverlay.getBorder() * 2),
                BufferedImage.TYPE_4BYTE_ABGR_PRE);

        Graphics graphics = bufferedImage.getGraphics();
        try {
            awtOverlay.render(graphics, awtOverlay.getBorder(), awtOverlay.getBorder());
        } finally {
            graphics.dispose();
        }
        return bufferedImage;
    }
}
