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

import net.catchpole.image.ImageScaler;
import net.catchpole.image.ImageUtils;
import net.catchpole.io.Files;
import net.catchpole.lang.Arguments;
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Jpegify {
    private final Trace trace = Core.getTrace();

    private final File fileDir;
    private final boolean recurse;
    private final boolean existingonly;
    private final float quality;
    private final int maxdimension;
    private final long maxfilesize;
    private final boolean join;
    private final boolean replace;
    private int skips;
    private int converts;

    public static void main(String[] args) throws Exception {
        new Jpegify(args);
    }

    public Jpegify(String[] args) throws Exception {
        final Arguments arguments = new Arguments(args);

        this.fileDir = new File(arguments.getArgument("-dir"));
        this.recurse = arguments.hasArgument("-recurse");
        this.existingonly = arguments.hasArgument("-existingonly");
        this.quality = Float.parseFloat(arguments.getArgument("-quality", "0.9"));
        this.maxfilesize = Long.parseLong(arguments.getArgument("-maxfilesize", "0"));
        this.maxdimension = Integer.parseInt(arguments.getArgument("-maxdimension", "0"));
        this.join = arguments.hasArgument("-join");
        this.replace = arguments.hasArgument("-replace");

        if (!fileDir.exists()) {
            throw new IOException("Directory does not exist: " + fileDir);
        }
        final File[] files;
        if (recurse) {
            List<File> list = Files.getRecursiveFileList(fileDir);
            files = list.toArray(new File[list.size()]);
        } else {
            files = fileDir.listFiles();
        }
        Arrays.sort(files);

        if (join) {
            for (int x = 0; x < files.length; x += 2) {
                join(files[x], files[x + 1], new File(fileDir, "_" + files[x].getName()));
            }
        } else {
            for (File file : files) {
                try {
                    attempt(file);
                } catch (Exception e) {
                    trace.info(e.getClass().toString() + ' ' + e.getMessage());
                }
            }
        }

        trace.info("Skips", skips, "Conversions", converts);
    }

    public void attempt(File file) throws Exception {
        File newFile = file;
        boolean isJpg = file.getName().toLowerCase().endsWith(".jpg");

        // ignore non-jpg if existing file is not jpg
        if (existingonly && !isJpg) {
            trace.info("Not jpg", file);
            return;
        }

        // try to convert non-jpg to jpg
        if (!existingonly && !isJpg) {
            newFile = new File(file.getAbsoluteFile() + ".jpg");
        } else if (replace) {
            newFile = file;
        }

        if (file.length() > maxfilesize) {
            if (maxdimension != 0) {
                trace.info("Size change", file, newFile, quality);
                resize(file, newFile, quality, maxdimension);
            } else {
                trace.info("Quality change", file, newFile, quality);
                convert(file, newFile, quality);
            }
            return;

        }
        trace.info("No change", file);
        skips++;
    }

    public void convert(File file, File newFile, float quality) throws Exception {
        long lastModified = file.lastModified();
        ImageUtils.saveImageJpg(ImageUtils.loadImage(file), newFile, quality);
        newFile.setLastModified(lastModified);
        converts++;
    }

    public void join(File topFile, File bottomFile, File targetFile) throws IOException {
        final BufferedImage topImage = ImageUtils.loadImage(topFile);
        final BufferedImage bottomImage = ImageUtils.loadImage(bottomFile);

        final BufferedImage wholeImage = new BufferedImage(topImage.getWidth(),
                topImage.getHeight() + bottomImage.getHeight(),
                topImage.getType());

        final Graphics g = wholeImage.getGraphics();
        try {
            g.drawImage(topImage, 0, 0, null);
            g.drawImage(bottomImage, 0, topImage.getHeight(), null);
        } finally {
            g.dispose();
        }

        ImageUtils.saveImageJpg(wholeImage, targetFile, quality);
        converts++;
    }

    public void resize(File file, File targetFile, float quality, int maxdimension) throws IOException {
        final BufferedImage image = ImageUtils.loadImage(file);

        if (image.getWidth() <= maxdimension && image.getHeight() <= maxdimension) {
            trace.info("Skipping by max dimension", image.getWidth(), file);
            return;
        }

        ImageScaler imageScaler = new ImageScaler(new Dimension(maxdimension,maxdimension));

        long lastModified = file.lastModified();
        BufferedImage newImage = imageScaler.transform(image);
        ImageUtils.saveImageJpg(newImage, targetFile, quality);

        targetFile.setLastModified(lastModified);
        converts++;
    }
}
