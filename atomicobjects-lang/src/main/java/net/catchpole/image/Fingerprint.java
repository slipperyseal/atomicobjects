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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class Fingerprint implements Externalizable, Comparable<Fingerprint> {
    private static final long serialVersionUID = 42L;
    private String originalPath;
    private int width;
    private int height;
    private int thumbWidth;
    private int[] thumbnail;
    private int hashcode;
    private float hue;

    public Fingerprint() {
    }

    public Fingerprint(ImageScaler imageScaler, BufferedImage image, String originalPath) {
        this.originalPath = originalPath;
        this.width = image.getWidth();
        this.height = image.getHeight();

        BufferedImage thumb = imageScaler.transform(image);
        this.thumbWidth = thumb.getWidth();
        this.thumbnail = getThumbnail(thumb);
        calculateHashcode();
    }

    public float getHue() {
        return hue;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public int[] getThumbnail() {
        return thumbnail;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    private static int[] getThumbnail(BufferedImage thumb) {
        int thumbWidth = thumb.getWidth();
        int thumbHeight = thumb.getHeight();
        int[] thumbnail = new int[thumbHeight * thumbWidth];

        int z = 0;
        for (int y = 0; y < thumbHeight; y++) {
            for (int x = 0; x < thumbWidth; x++) {
                thumbnail[z++] = thumb.getRGB(x, y);
            }
        }
        return thumbnail;
    }

    public int hashCode() {
        return hashcode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Fingerprint)) {
            return false;
        }
        Fingerprint check = (Fingerprint) obj;
        return (check.width == this.width &&
                check.height == this.height &&
                Arrays.equals(check.thumbnail, this.thumbnail)
        );
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + width + 'x' + height + ' ' +
                '[' + this.thumbWidth + 'x' + this.thumbnail.length / this.thumbWidth + ']' + ' ' +
                originalPath;
    }

    private void calculateHashcode() {
        int hash = (width * height);
        for (int x : thumbnail) {
            hash += 0xffff + x;
        }
        this.hashcode = hash;

        hue = getAverageHue();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.originalPath);
        out.writeInt(this.width);
        out.writeInt(this.height);
        out.writeInt(this.thumbWidth);
        out.writeInt(this.thumbnail.length);
        for (int value : this.thumbnail) {
            out.writeInt(value);
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.originalPath = in.readUTF();
        this.width = in.readInt();
        this.height = in.readInt();
        this.thumbWidth = in.readInt();
        this.thumbnail = new int[in.readInt()];
        for (int x = 0; x < thumbnail.length; x++) {
            this.thumbnail[x] = in.readInt();
        }
        calculateHashcode();
    }

    private static final char[] chars = {'#', '$', '&', '@', '=', '-', '.', ' '};

    public String getAsciiThumbnail() {
        StringBuilder sb = new StringBuilder();
        int h = thumbnail.length / thumbWidth;
        int z = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < thumbWidth; x++) {
                int v = thumbnail[z++];
                v = ((v & 7) + ((v & 56) >> 3) + ((v & 448) >> 6)) / 4;
                if (v > 7) {
                    v = 7;
                }
                sb.append(chars[v]);
            }
            sb.append('\r');
            sb.append('\n');
        }
        return sb.toString();
    }

    private float getAverageHue() {
        float[] hsb = new float[3];
        float hue = 0;
        int div = 0;
        for (int value : thumbnail) {
            Color.RGBtoHSB(
                    ((value) & 0xff),
                    ((value >> 8) & 0xff),
                    ((value >> 16) & 0xff), hsb);
            // only count hue of saturation and brightness are high
            if (hsb[1] > .5 && hsb[2] > .5) {
                hue += hsb[0];
                div++;
            }
        }
        return (div == 0 ? hue : hue / div);
    }

    public int compareTo(Fingerprint o) {
        if (this.equals(o)) {
            return 0;
        }

        return (int) ((this.hue - ((Fingerprint) o).hue) * 1000000);
    }
}
