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

import net.catchpole.image.Fingerprint;
import net.catchpole.image.ImageScaler;
import net.catchpole.image.ImageUtils;
import net.catchpole.image.RenderedImageWriter;
import net.catchpole.io.Files;
import net.catchpole.lang.Arguments;
import net.catchpole.lang.Serializer;
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ImageCompare {
    private static final Trace trace = Core.getTrace();
    private static final int THUMBSIZE = 32;
    private final ImageScaler imageScaler = new ImageScaler(new Dimension(THUMBSIZE, THUMBSIZE));
    private int deletes;

    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments(args);
        if (arguments.hasArgument("-load")) {
            trace.info(new ImageCompare(
                    new File(arguments.getArgument("-load", null)),
                    arguments.getArgument("-file", null),
                    arguments.hasArgument("-deleteonerror")));
        }

        if (arguments.hasArgument("-poster")) {
            trace.info(new ImageCompare(arguments.getArgument("-poster", null)));
        }

        if (arguments.hasArgument("-clean")) {
            trace.info(new ImageCompare(
                    arguments.getArgument("-basefile", null),
                    arguments.getArgument("-cleanfile", null)));
        }

        if (arguments.hasArgument("-dupe")) {
            trace.info(new ImageCompare(
                    new File(arguments.getArgument("-dupe", null)),
                    arguments.hasArgument("-delete")));
        }
    }

    public ImageCompare(File baseFile, boolean delete) throws Exception {
        deleteDuplicates(baseFile, delete);
    }

    public ImageCompare(File baseDir, String store, boolean deleteonerror) throws IOException {
        createFingerprintsFile(baseDir, new File(store), deleteonerror);
    }

    public ImageCompare(String baseStore, String cleanStore) throws Exception {
        Map<Fingerprint, Fingerprint> baseMap = deleteDuplicates(new File(baseStore), true);
        Map<Fingerprint, Fingerprint> cleanMap = deleteDuplicates(new File(cleanStore), true);

        for (Fingerprint clean : cleanMap.keySet()) {
            Fingerprint base = baseMap.get(clean);
            if (base != null) {
                trace.info("Match: " + base.getOriginalPath() + ' ' + clean.getOriginalPath());
                File cleanFile = new File(clean.getOriginalPath());
                File baseFile = new File(base.getOriginalPath());
                if (cleanFile.length() != baseFile.length()) {
                    trace.info("Size mismatch: " + baseFile + '=' + baseFile.length() + ' ' + cleanFile + '=' + cleanFile.length());
                }
                //new File(clean.getOriginalPath()).delete();
                //deletes++;
            }
        }

    }

    /**
     * Make thumb image.
     */
    public ImageCompare(String baseStore) throws Exception {
        makePoster(sortByHue(loadFingerprintsFile(new File(baseStore))), baseStore);
    }

    public void makePoster(List<Fingerprint> list, String name) throws Exception {
        int total = list.size();
        int side = (int) (Math.sqrt((double) total) + 1);

        trace.info("Total images: " + total);
        trace.info("Image: " + side + ' ' + '(' + side * THUMBSIZE + ')');

        BufferedImage bufferedImage = new BufferedImage(side * THUMBSIZE, side * THUMBSIZE, BufferedImage.TYPE_INT_RGB);

        int y = 0;
        int x = 0;
        for (Fingerprint fingerprint : list) {
            int dx = x * THUMBSIZE;
            int dy = y * THUMBSIZE;

            int[] thumbnail = fingerprint.getThumbnail();
            int thumbWidth = fingerprint.getThumbWidth();
            int thumbHeight = thumbnail.length / thumbWidth;

            int z = 0;
            for (int yy = 0; yy < thumbHeight; yy++) {
                for (int xx = 0; xx < thumbWidth; xx++) {
                    bufferedImage.setRGB(dx + xx, dy + yy, thumbnail[z++]);
                }
            }

            if (++x >= side) {
                x = 0;
                y++;
            }
        }

        FileOutputStream fos = new FileOutputStream(name + ".png");
        try {
            new RenderedImageWriter().write(bufferedImage, fos);
        } finally {
            fos.close();
        }
    }

    private void createFingerprintsFile(File dir, File outFile, boolean deleteonerror) throws IOException {
        OutputStream os = new DeflaterOutputStream(new FileOutputStream(outFile), new Deflater(Deflater.BEST_COMPRESSION));
        try {
            DataOutputStream dos = new DataOutputStream(os);
            List<File> fileList = Files.getRecursiveFileList(dir);

            Worker[] workers = new Worker[4];
            for (int x = 0; x < workers.length; x++) {
                workers[x] = new Worker();
            }
            Iterator<File> i = fileList.iterator();

            while (i.hasNext()) {
                for (Worker worker : workers) {
                    if (i.hasNext()) {
                        worker.begin(i.next());
                    }
                }

                for (Worker worker : workers) {
                    if (worker.hasWork()) {
                        Fingerprint fingerprint = worker.end();
                        if (fingerprint != null) {
                            byte[] bytes = new Serializer().serialize(fingerprint);
                            dos.writeInt(bytes.length);
                            dos.write(bytes);
                            trace.info(fingerprint);
                        }
                    }
                }
            }
        } finally {
            os.close();
        }
    }

    class Worker implements Runnable {
        Thread thread;
        File file;
        Fingerprint fingerprint;
        boolean hasWork;

        public Worker() {
        }

        public void run() {
            fingerprint = makeFingerprint(file, false);
        }

        public void begin(File file) {
            this.hasWork = true;
            this.file = file;
            this.thread = new Thread(this);
            this.thread.start();
        }

        public Fingerprint end() {
            this.hasWork = false;
            try {
                this.thread.join();
                Fingerprint f = fingerprint;
                fingerprint = null;
                return f;
            } catch (InterruptedException e) {
            }
            return null;
        }

        public boolean hasWork() {
            return hasWork;
        }
    }

    private Fingerprint makeFingerprint(File file, boolean deleteonerror) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageUtils.loadImage(file);
        } catch (Exception e) {
            trace.info(file.toString() + ' ' + e.getClass() + ' ' + e.getMessage());
            if (deleteonerror) {
                file.delete();
                deletes++;
                trace.info(file.toString() + " deleted");
            }
            return null;
        }
        return new Fingerprint(imageScaler, bufferedImage, file.getAbsolutePath());
    }

    private Map<Fingerprint, Fingerprint> deleteDuplicates(File file, boolean delete) throws Exception {
        List<Fingerprint> list = new ArrayList<Fingerprint>();
        Map<Fingerprint, Fingerprint> map = new HashMap<Fingerprint, Fingerprint>();
        DataInputStream dis = new DataInputStream(new InflaterInputStream(new FileInputStream(file)));
        try {
            try {
                for (; ;) {
                    int len = dis.readInt();
                    byte[] data = new byte[len];
                    dis.readFully(data);
                    Fingerprint fingerprint = (Fingerprint) new Serializer().deserialize(data);

                    Fingerprint dupe = map.get(fingerprint);
                    if (dupe != null) {
                        trace.info("Duplicate: " + dupe.getOriginalPath() + ' ' + fingerprint.getOriginalPath());
                        list.add(fingerprint);
                        if (delete) {
                            new File(fingerprint.getOriginalPath()).delete();
                            deletes++;
                        }
                    } else {
                        map.put(fingerprint, fingerprint);
                    }
                }
            } catch (EOFException e) {
                // thats what we want
            }
        } finally {
            dis.close();
        }
        if (list.size() > 0) {
            makePoster(list, "duplicates");
        }
        return map;
    }

    private List<Fingerprint> loadFingerprintsFile(File file) throws Exception {
        ArrayList<Fingerprint> array = new ArrayList<Fingerprint>();
        DataInputStream dis = new DataInputStream(new InflaterInputStream(new FileInputStream(file)));
        try {
            try {
                for (; ;) {
                    int len = dis.readInt();
                    byte[] data = new byte[len];
                    dis.readFully(data);
                    array.add((Fingerprint) new Serializer().deserialize(data));
                }
            } catch (EOFException e) {
                // thats what we want
            }
        } finally {
            dis.close();
        }
        return array;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + this.deletes + " deletes";
    }

    private List<Fingerprint> sortByHue(List<Fingerprint> list) {
        Fingerprint[] array = list.toArray(new Fingerprint[list.size()]);
        Arrays.sort(array);
        return new ArrayList<Fingerprint>(Arrays.asList(array));
    }
}
