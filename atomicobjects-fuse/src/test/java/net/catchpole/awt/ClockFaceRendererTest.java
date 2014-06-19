package net.catchpole.awt;

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

import net.catchpole.image.RenderedImageWriter;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ClockFaceRendererTest {
    @Test
    public void testClockFaceRenderer() throws IOException {
        ClockFaceRenderer clockFaceRenderer = new ClockFaceRenderer(200, new Color(200, 200, 50), true);
        BufferedImage bufferedImage = clockFaceRenderer.transform(new Date());

        RenderedImageWriter renderedImageWriter = new RenderedImageWriter();
        FileOutputStream fos = new FileOutputStream(new File(getReportDir(), this.getClass().getSimpleName() + '.' + renderedImageWriter.getFormatName()));
        try {
            renderedImageWriter.write(bufferedImage, fos);
        } finally {
            fos.close();
        }
    }

    public static File getReportDir() {
        File dir = new File("./target/surefire-reports");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.exists()) {
            return new File(".");
        }
        return dir;
    }
}
