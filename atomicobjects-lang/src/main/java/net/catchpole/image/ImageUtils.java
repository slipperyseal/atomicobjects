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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static Dimension fitWithin(Dimension original, Dimension fit) {
        return fitWithin(original, fit, true);
    }

    /**
     * Returns a Dimension which is is the result of one dimension being scaled to fit within
     * another, without changing the orignal aspect ratio.
     * <p/>
     * <p>For example a dimension 800 by 300, to fit inside a dimension 50 by 90 would be scaled to 50 by 18.
     *
     * @param original
     * @param fit
     * @param scaleUp  Scale up the original if it fits within 'fit'
     */
    public static Dimension fitWithin(Dimension original, Dimension fit, boolean scaleUp) {
        final double scaleX = ((double) fit.width) / ((double) original.width);
        final double scaleY = ((double) fit.height) / ((double) original.height);

        if (!scaleUp && original.width <= fit.width && original.height <= fit.height) {
            return new Dimension(original);
        }

        if (scaleX > scaleY) {
            return new Dimension((int) (((double) original.width) * scaleY), (int) (((double) original.height) * scaleY));
        } else {
            return new Dimension((int) (((double) original.width) * scaleX), (int) (((double) original.height) * scaleX));
        }
    }

    public static Insets centerOver(Dimension base, Dimension overlay) {
        final double baseCenterX = ((double) base.width) / 2.0;
        final double baseCenterY = ((double) base.height) / 2.0;
        final double overlayCenterX = ((double) overlay.width) / 2.0;
        final double overlayCenterY = ((double) overlay.height) / 2.0;
        final int top = (int) (baseCenterY - overlayCenterY);
        final int left = (int) (baseCenterX - overlayCenterX);
        // must add integer overlay size to top and left to prevent rounding errors changing the overlay size
        return new Insets(top, left, top + overlay.width, left + overlay.height);
    }

    /**
     * Converts a 24 bit RGB value to a 15 bit RGB value.
     * eg.
     * 111100001111000011110000 becomes 111101111011110
     */
    public static short rgbToShort(int rgb) {
        return (short) (
                ((rgb & 0xf8) >> 3) |
                        ((rgb & 0xf800) >> 6) |
                        ((rgb & 0xf80000) >> 9));
    }

    public static int shortToRGB(short rgbShort) {
        return ((rgbShort << 3) & 0xf8) |
                ((rgbShort << 6) & 0xf800) |
                ((rgbShort << 9) & 0xf80000);
    }

    public static BufferedImage loadImage(File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        try {
            final BufferedImage bufferedImage = ImageIO.read(fis);
            if (bufferedImage == null) {
                throw new IOException("ImageIO.read returned null for " + file);
            }
            return bufferedImage;
        } finally {
            fis.close();
        }
    }

    public static void saveImageJpg(BufferedImage bufferedImage, File file, float quality) throws IOException {
        final FileOutputStream fos = new FileOutputStream(file);
        try {
            new RenderedImageWriter("JPG", quality).write(bufferedImage, fos);
        } finally {
            fos.close();
        }
    }
}
