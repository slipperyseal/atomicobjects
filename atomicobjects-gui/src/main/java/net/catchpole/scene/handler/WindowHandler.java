package net.catchpole.scene.handler;

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

import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;
import net.catchpole.lang.Wire;


public class WindowHandler implements WindowListener {
    private final Wire resize;
    private final Wire stop;

    public WindowHandler(Wire resize, Wire stop) {
        this.resize = resize;
        this.stop = stop;
    }

    public void windowResized(WindowEvent e) {
        resize.trip();
    }

    public void windowMoved(WindowEvent e) {
    }

    public void windowDestroyNotify(WindowEvent e) {
        stop.trip();
    }

    public void windowGainedFocus(WindowEvent e) {
    }

    public void windowLostFocus(WindowEvent e) {
    }

    public void windowDestroyed(WindowEvent windowEvent) {
    }

    public void windowRepaint(WindowUpdateEvent windowUpdateEvent) {
    }
}
