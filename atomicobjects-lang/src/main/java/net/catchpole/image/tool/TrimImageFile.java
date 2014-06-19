package net.catchpole.image.tool;

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

import net.catchpole.io.CountingInputStream;
import net.catchpole.io.Files;
import net.catchpole.trace.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Removes excess binary from the end of an image file by loading the image
 * and then truncating it to the last byte that was read.
 * <p/>
 * <p>This class was written for a hard drive recovery after images were
 * detected and recovered to a fixed file length.
 * <p/>
 * <p>It must be warned that some image readers may read past the end of the logical
 * file, using a buffered reader, or even less than the length of the logical
 * file if all of it's properties were not accessed.
 */
public class TrimImageFile {
    private final File file;
    private final long total;
    private String imageString;

    public static void main(String[] args) throws IOException {
        trim(new File(args[0]));
    }

    public static void trim(File dir) throws IOException {
        final File[] files = dir.listFiles();

        for (int x = 0; x < files.length; x++) {
            final File file = files[x];
            try {
                if (file.getName().toLowerCase().endsWith(".jpg") ||
                        file.getName().toLowerCase().endsWith(".jpeg") ||
                        file.getName().toLowerCase().endsWith(".gif")) {
                    Core.getTrace().info(new TrimImageFile(file));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public TrimImageFile(File file) throws IOException {
        this.file = file;

        final FileInputStream fis = new FileInputStream(file);
        final CountingInputStream cis = new CountingInputStream(fis);

        final BufferedImage bi = ImageIO.read(cis);
        this.total = cis.getTotalBytesRead();

        if (bi != null) {
            this.imageString = bi.toString();
            Files.trimFile(file, this.total);
        }
    }

    public String toString() {
        return file.toString() + ' ' + total + ' ' + imageString;
    }
}
