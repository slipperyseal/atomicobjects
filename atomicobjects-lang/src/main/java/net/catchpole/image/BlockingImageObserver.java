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
import java.awt.image.ImageObserver;

/**
 * An Image Observer which will block until the image is drawn.
 */
// TODO:  not tested very well
public class BlockingImageObserver implements ImageObserver {
    private boolean finished = false;

    public BlockingImageObserver() {
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        if ((infoflags & ALLBITS) == 0) {
            return true;
        }

        finished = true;
        this.notifyAll();
        return false;
    }

    public void waitForUpdate() {
        while (!finished) {
            try {
                this.wait();
            } catch (InterruptedException ie) {
            }
        }
    }
}
