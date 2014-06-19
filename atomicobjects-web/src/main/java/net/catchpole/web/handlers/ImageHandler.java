package net.catchpole.web.handlers;

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
import net.catchpole.io.Arrays;
import net.catchpole.web.paths.PathHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageHandler implements PathHandler {
    private final File imageDir;
    private final File scaleDir;
    private Map<String,Integer> fitMap = new HashMap<String,Integer>();

    public ImageHandler(File imageDir, File scaleDir) {
        this.imageDir = imageDir;
        this.scaleDir = scaleDir;
        fitMap.put("s", 100);
        fitMap.put("m", 300);
        fitMap.put("l", 600);
    }

    public void handle(String[] path, HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (path.length != 3) {
            throw new IOException();
        }

        File imageFile = getImage(path[1],path[2]);

        res.setContentType("image/jpeg");

        FileInputStream fileInputStream = new FileInputStream(imageFile);
        try {
            Arrays.spool(fileInputStream, res.getOutputStream());
        } finally {
            fileInputStream.close();
        }
    }

    public long getLastModified(String[] path, HttpServletRequest req) throws IOException {
        if (path.length != 3) {
            throw new IOException();
        }
        File imageFile = getImage(path[1],path[2]);
        return (imageFile.lastModified() / 1000) * 1000;
    }

    private synchronized File getImage(String type, String name) throws IOException {
        Integer size = fitMap.get(type);
        if (size == null) {
            throw new IllegalArgumentException("unknown type");
        }
        File imageFile = new File(scaleDir, type + '.' + name);
        if (!imageFile.exists()) {
            scale(new File(imageDir, name), imageFile, size);
        }
        return imageFile;
    }

    private void scale(File fromFile, File toFile, int size) throws IOException {
        ImageScaler imageScaler = new ImageScaler(new Dimension(size, size), true);
        ImageUtils.saveImageJpg(imageScaler.transform(ImageUtils.loadImage(fromFile)), toFile, .8f);
    }
}
