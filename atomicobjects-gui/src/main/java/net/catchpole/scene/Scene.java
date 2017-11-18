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

import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import net.catchpole.lang.Throw;
import net.catchpole.lang.Wire;
import net.catchpole.scene.handler.MouseHandler;
import net.catchpole.scene.handler.WindowHandler;
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;

public final class Scene implements GLEventListener, WindowListener {
    private final Trace trace = Core.getTrace(Scene.class);
    private final Renderer renderer;
    private GLWindow window;
    private Wire stop = new Wire(false);
    private Wire resize = new Wire();

    static final AnimatorBase anim = new FPSAnimator(25);
    static {
        anim.start();
    }
    private SceneMilieu milieu;

    public Scene(String title, Renderer renderer) {
        this.renderer = renderer;
        GLWindow w = GLWindow.create(config());
        w.addGLEventListener(this);
        w.addWindowListener(this);
        this.window = w;
        //w.setSharedContext(sharedDrawable.getContext());

        anim.add(w);

        try {
            //this.display = NewtFactory.createDisplay(null);
            this.window.setPosition(0, 0);
            this.window.setSize(1024,800);
                    ///window.getScreen().getWidth(), window.getScreen().getHeight());
            this.window.addWindowListener(new WindowHandler(resize, stop));
            this.window.addMouseListener(new MouseHandler(this.window, renderer));
            this.window.setTitle(title);
            this.window.setVisible(true);
        } catch (Throwable t) {
            dispose();
            Throw.unchecked(t);
        }
    }

    static GLCapabilitiesImmutable config() {


        return new GLCapabilities(

                //GLProfile.getMinimum(true)
                GLProfile.getDefault()
                //GLProfile.getMaximum(true)


        );
    }

//    public void dispatch() {
//        try {
//            renderFrame(true);
//            while (!stop.isTripped()) {
//                Thread.yield();
//                renderFrame(false);
//            }
//        } finally {
//            dispose();
//        }
//    }


    public synchronized void dispose() {
        if (this.window != null) {
            try {
                this.window.setVisible(false);
                this.window.destroy();
                this.window = null;
            } catch (Exception e) {
                this.trace.error(e);
            }
        }

    }

    @Override
    public void windowResized(WindowEvent e) {

    }

    @Override
    public void windowMoved(WindowEvent e) {

    }

    @Override
    public void windowDestroyNotify(WindowEvent e) {

    }

    @Override
    public void windowDestroyed(WindowEvent e) {

    }

    @Override
    public void windowGainedFocus(WindowEvent e) {

    }

    @Override
    public void windowLostFocus(WindowEvent e) {

    }

    @Override
    public void windowRepaint(WindowUpdateEvent e) {

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        try {
            milieu = new SceneMilieu(drawable.getGL());

            renderer.init(milieu);
        } catch (Throwable t) {
            t.printStackTrace();
            Throw.unchecked(t);
        }
        resize.trip();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {


            GL2 gl = drawable.getGL().getGL2();

            if (resize.reset()) {
                gl.glViewport(0, 0, this.window.getWidth(), this.window.getHeight());
                this.renderer.resize(milieu, window.getWidth(), window.getHeight());
            }

            try {
                this.renderer.render(milieu);
            } catch (Throwable t) {
                t.printStackTrace();
                Throw.unchecked(t);
            }
            //this.window.swapBuffers();

            if (stop.isTripped()) {
                this.renderer.destory(milieu);
            }


    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }
}
