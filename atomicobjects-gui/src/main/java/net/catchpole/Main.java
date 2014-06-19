package net.catchpole;

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

import net.catchpole.fuse.Junction;
import net.catchpole.fuse.JunctionBox;
import net.catchpole.lang.Arguments;
import net.catchpole.lang.Throw;
import net.catchpole.model.Model;
import net.catchpole.model.ModelSource;
import net.catchpole.scene.Scene;
import net.catchpole.scene.renderer.ModelRenderer;

public class Main {
    private final String host;
    private final Junction junction = new JunctionBox();

    public Main(String[] args) throws Exception {
        Arguments arguments = new Arguments(args);

        this.host = arguments.getArgument("-host", "localhost");

        Runtime.getRuntime().addShutdownHook(new ShutdownHook());

        if (!arguments.hasArgument("-nogui")) {
            System.setProperty("java.awt.headless", "true");

            Model model = arguments.hasArgument("-model") ?
                    getModel(arguments.getArgument("-model")) :
                    new MasterModelSource().get();

            new Scene("Catchpole", new ModelRenderer(model)).dispatch();
            System.exit(0);
        }

        synchronized (this) {
            this.wait();
        }
    }

    public static void main(String[] args) {
        try {
            new Main(args);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    class ShutdownHook extends Thread {
        public void run() {
            junction.dispose();
        }
    }

    private Model getModel(String className) {
        try {
            Class clazz = Class.forName(className);
            return ((ModelSource) clazz.newInstance()).get();
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }
}
