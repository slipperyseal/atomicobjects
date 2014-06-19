package net.catchpole.scene;

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

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.opengl.GLWindow;
import net.catchpole.lang.Disposable;
import net.catchpole.lang.Throw;
import net.catchpole.lang.Wire;
import net.catchpole.scene.handler.MouseHandler;
import net.catchpole.scene.handler.WindowHandler;
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLProfile;

public final class Scene implements Disposable {
    private final Trace trace = Core.getTrace(Scene.class);
    private final Renderer renderer;
    private Display display;
    private GLWindow window;
    private Wire stop = new Wire(false);
    private Wire resize = new Wire();

    public Scene(String title, Renderer renderer) {
        this.renderer = renderer;

        try {
            this.display = NewtFactory.createDisplay(null);
            this.window = GLWindow.create(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
            this.window.setPosition(0, 0);
            this.window.setSize(window.getScreen().getWidth(), window.getScreen().getHeight());
            this.window.addWindowListener(new WindowHandler(resize, stop));
            this.window.addMouseListener(new MouseHandler(this.window, renderer));
            this.window.setTitle(title);
            this.window.setVisible(true);
        } catch (Throwable t) {
            dispose();
            Throw.unchecked(t);
        }
    }

    public void dispatch() {
        try {
            renderFrame(true);
            while (!stop.isTripped()) {
                Thread.yield();
                renderFrame(false);
            }
        } finally {
            dispose();
        }
    }

    private void renderFrame(boolean init) {
        //this.display.pumpMessages();
        this.display.dispatchMessages();

        this.window.lockSurface();
        try {
            GLContext glContext = window.getContext();
            if (glContext == null) {
                throw new IllegalStateException("unable to get context");
            }
            try {
                if (GLContext.CONTEXT_NOT_CURRENT == glContext.makeCurrent()) {
                    throw new IllegalStateException("Context error");
                }

                Milieu milieu = new SceneMilieu(glContext.getGL());

                if (init) {
                    try {
                        renderer.init(milieu);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        Throw.unchecked(t);
                    }
                    resize.trip();
                }

                if (resize.reset()) {
                    milieu.getGL().getGL2().glViewport(0, 0, this.window.getWidth(), this.window.getHeight());
                    this.renderer.resize(milieu, window.getWidth(), window.getHeight());
                }

                try {
                    this.renderer.render(milieu);
                } catch (Throwable t) {
                    t.printStackTrace();
                    Throw.unchecked(t);
                }
                this.window.swapBuffers();

                if (stop.isTripped()) {
                    this.renderer.destory(milieu);
                }
            } finally {
                glContext.release();
            }
        } finally {
            this.window.unlockSurface();
        }
    }

    public void dispose() {
        if (this.window != null) {
            try {
                this.window.setVisible(false);
                this.window.destroy();
                this.window = null;
            } catch (Exception e) {
                this.trace.error(e);
            }
        }
        if (display != null) {
            try {
                this.display.destroy();
                this.display = null;
            } catch (Exception e) {
                this.trace.error(e);
            }
        }
    }
}
