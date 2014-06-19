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

import junit.framework.TestCase;
import org.junit.Test;

import java.awt.*;

public class ImageUtilsTest {
    //@Test
    public void testFitWithin() {
        Dimension d = ImageUtils.fitWithin(new Dimension(2560, 1920), new Dimension(1280, 1280), false);
        TestCase.assertEquals(2560, d.width);
        TestCase.assertEquals(1920, d.height);

        d = ImageUtils.fitWithin(new Dimension(80, 30), new Dimension(50, 90));
        TestCase.assertEquals(50, d.width);
        TestCase.assertEquals(18, d.height);
        d = ImageUtils.fitWithin(new Dimension(800, 300), new Dimension(50, 90));
        TestCase.assertEquals(50, d.width);
        TestCase.assertEquals(18, d.height);

        d = ImageUtils.fitWithin(new Dimension(80, 30), new Dimension(90, 50));
        TestCase.assertEquals(90, d.width);
        TestCase.assertEquals(33, d.height);
        d = ImageUtils.fitWithin(new Dimension(800, 300), new Dimension(90, 50));
        TestCase.assertEquals(90, d.width);
        TestCase.assertEquals(33, d.height);

        // scale up small values
        d = ImageUtils.fitWithin(new Dimension(8, 3), new Dimension(90, 50));
        TestCase.assertEquals(90, d.width);
        TestCase.assertEquals(33, d.height);
        d = ImageUtils.fitWithin(new Dimension(8, 3), new Dimension(50, 90));
        TestCase.assertEquals(50, d.width);
        TestCase.assertEquals(18, d.height);

        // dont scale up small values
        d = ImageUtils.fitWithin(new Dimension(8, 3), new Dimension(90, 50), false);
        TestCase.assertEquals(8, d.width);
        TestCase.assertEquals(3, d.height);
        Insets i = ImageUtils.centerOver(new Dimension(90, 50), d);
        TestCase.assertEquals(23, i.top);
        TestCase.assertEquals(41, i.left);
        TestCase.assertEquals(31, i.bottom);
        TestCase.assertEquals(44, i.right);

        // scale up small values
        d = ImageUtils.fitWithin(new Dimension(8, 3), new Dimension(90, 50));
        TestCase.assertEquals(90, d.width);
        TestCase.assertEquals(33, d.height);
        d = ImageUtils.fitWithin(new Dimension(8, 3), new Dimension(50, 90));
        TestCase.assertEquals(50, d.width);
        TestCase.assertEquals(18, d.height);
        i = ImageUtils.centerOver(new Dimension(50, 90), d);
        TestCase.assertEquals(36, i.top);
        TestCase.assertEquals(0, i.left);
        TestCase.assertEquals(86, i.bottom);
        TestCase.assertEquals(18, i.right);

        // dont scale up small values
        d = ImageUtils.fitWithin(new Dimension(8, 3), new Dimension(50, 90), false);
        TestCase.assertEquals(8, d.width);
        TestCase.assertEquals(3, d.height);
        i = ImageUtils.centerOver(new Dimension(50, 90), d);
        TestCase.assertEquals(43, i.top);
        TestCase.assertEquals(21, i.left);
        TestCase.assertEquals(51, i.bottom);
        TestCase.assertEquals(24, i.right);
    }

    @Test
    public void testRbgToShort() {
        TestCase.assertEquals(0x7BDE, ImageUtils.rgbToShort(0xf0f0f0));
    }
}
