package net.catchpole.image;

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

import net.catchpole.lang.Transformation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageScaler implements Transformation<BufferedImage, BufferedImage> {
    private final Dimension fit;
    private final boolean scaleUp;

    public ImageScaler(Dimension fit) {
        this.fit = fit;
        this.scaleUp = false;
    }

    public ImageScaler(Dimension fit, boolean scaleUp) {
        this.fit = fit;
        this.scaleUp = scaleUp;
    }

    public Dimension getFitDimension() {
        return this.fit;
    }

    public BufferedImage transform(BufferedImage image) {
        Dimension dimension = ImageUtils.fitWithin(new Dimension(image.getWidth(), image.getHeight()), fit, scaleUp);

        BufferedImage bi = new BufferedImage(dimension.width, dimension.height, image.getType());
        Graphics g = bi.createGraphics();
        if (g instanceof Graphics2D) {
            Graphics2D g2 = ((Graphics2D) g);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.drawImage(image, 0, 0, dimension.width, dimension.height, null);
        return bi;
    }


    public BufferedImage scale(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, image.getType());
        Graphics g = bi.createGraphics();
        if (g instanceof Graphics2D) {
            Graphics2D g2 = ((Graphics2D) g);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        g.drawImage(image, 0, 0, width, height, null);
        return bi;
    }
}
