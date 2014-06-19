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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class RenderedImageWriter {
    private final static String defaultFormatName = "PNG";

    private final String formatName;
    private final Float quality;

    static {
        // we dont like disk caches
        ImageIO.setUseCache(false);
    }

    public RenderedImageWriter() {
        this.formatName = defaultFormatName;
        this.quality = null;
    }

    public RenderedImageWriter(String formatName, Float quality) {
        this.formatName = formatName;
        this.quality = quality;
    }

    public byte[] write(RenderedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        write(image, baos);
        return baos.toByteArray();
    }

    public void write(RenderedImage image, OutputStream os) throws IOException {
        final ImageWriter writer = getImageWriter();
        final ImageWriteParam iwp = writer.getDefaultWriteParam();
        if (quality != null) {
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(quality);
        }
        writer.setOutput(new MemoryCacheImageOutputStream(os));
        writer.write(null, new IIOImage(image, null, null), iwp);
        writer.dispose();
    }

    public ImageWriter getImageWriter() {
        final Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName(formatName);
        if (!iterator.hasNext()) {
            throw new RuntimeException(ImageIO.class.getClass().getName() + ' ' + formatName);
        }
        return iterator.next();
    }


    public String getFormatName() {
        return formatName;
    }

    public Float getQuality() {
        return quality;
    }
}
