package net.catchpole.scene.gl;

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

import net.catchpole.lang.Maths;
import net.catchpole.lang.Throw;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BlitImage {
    private ByteBuffer pixelBuffer;
    private int width;
    private int height;

    public BlitImage(BufferedImage image) {
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        this.pixelBuffer = toRGBA(getPixels(image));
    }

    public ByteBuffer getByteBuffer() {
        return pixelBuffer;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void draw(GL2 gl2, int x, int y) {
        gl2.glRasterPos2i(x, y + height);
        gl2.glDrawPixels(width, height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, pixelBuffer);
    }

    private int[] getPixels(Image image) {
        int[] pixelsARGB = null;
        if (image != null) {
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            pixelsARGB = new int[width * height];
            PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixelsARGB, 0, width);
            try {
                pg.grabPixels();
            } catch (Exception e) {
                throw Throw.unchecked(e);
            }
        }
        return pixelsARGB;
    }

    private ByteBuffer toRGBA(int[] jpixels) {
        return allocBytes(fromARGBtoRGBAflipVertical(jpixels));
    }

    private static byte[] fromARGBtoRGBA(int[] pixels) {
        byte[] bytes = new byte[pixels.length * 4];
        int x = 0;
        for (int i : pixels) {
            bytes[x++] = (byte) (i >> 16);
            bytes[x++] = (byte) (i >> 8);
            bytes[x++] = (byte) i;
            bytes[x++] = (byte) (i >> 24);
        }
        return bytes;
    }

    private byte[] fromARGBtoRGBAflipVertical(int[] pixels) {
        byte[] bytes = new byte[pixels.length * 4];
        int x = 0;
        for (int h=0;h<this.height;h++) {
            int start = (this.height-1-h)*this.width;
            int stop = start + this.width;
            for (int w=start;w<stop;w++) {
                int i = pixels[w];
                bytes[x++] = (byte) (i >> 16);
                bytes[x++] = (byte) (i >> 8);
                bytes[x++] = (byte) i;
                bytes[x++] = (byte) (i >> 24);
            }
        }
        return bytes;
    }

    private static ByteBuffer allocBytes(byte[] bytearray) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytearray.length).order(ByteOrder.nativeOrder());
        byteBuffer.put(bytearray).flip();
        return byteBuffer;
    }

    private BufferedImage toPowerOf2(BufferedImage bufferedImage) {
        int width = Maths.fitPowerOf2(bufferedImage.getWidth());
        int height = Maths.fitPowerOf2(bufferedImage.getHeight());

        if (width == bufferedImage.getWidth() && height == bufferedImage.getHeight()) {
            return bufferedImage;
        }

        AffineTransform affineTransform = AffineTransform.getScaleInstance((double) width / bufferedImage.getWidth(),
                (double) height / bufferedImage.getHeight());
        BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = newBufferedImage.createGraphics();
        graphics2D.drawRenderedImage(bufferedImage, affineTransform);
        return newBufferedImage;
    }

    private BufferedImage flipVertical(BufferedImage bufferedImage) {
        AffineTransform affineTransform = AffineTransform.getScaleInstance(1, -1);
        affineTransform.translate(0, 0 - bufferedImage.getHeight(null));
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return affineTransformOp.filter(bufferedImage, null);
    }
}
