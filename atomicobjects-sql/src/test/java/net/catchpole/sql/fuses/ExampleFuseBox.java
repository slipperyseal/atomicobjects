package net.catchpole.sql.fuses;

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

import net.catchpole.awt.ClockFaceRenderer;
import net.catchpole.fuse.Fuse;
import net.catchpole.fuse.FuseBox;
import net.catchpole.fuse.signature.ByteArrayInputStreamSignature;
import net.catchpole.fuse.signature.RandomAccessArraySignature;
import net.catchpole.fuse.signature.RenderedImageWriterSignature;
import net.catchpole.fuse.signature.StringSignature;
import net.catchpole.lang.Source;
import net.catchpole.lang.Throw;
import net.catchpole.sql.AirlineDatabase;
import net.catchpole.sql.fuses.example.StreamEater;
import net.catchpole.sql.fuses.example.Stringer;
import net.catchpole.sql.meta.Table;
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;

import java.awt.*;

public class ExampleFuseBox implements Source<FuseBox> {
    private Trace trace = Core.getTrace();
    private FuseBox fuseBox = new FuseBox();

    public ExampleFuseBox() {
        try {
            Table table = AirlineDatabase.create().getTable("schedule");
            fuseBox.addFuse(new TableFuse(table, table.getColumn("call_sign"), table.getColumn("arrive_time")));

            fuseBox.addClass(ByteArrayInputStreamSignature.class);
            fuseBox.addClass(StringSignature.class);
            fuseBox.addClass(Stringer.class);
            fuseBox.addClass(StreamEater.class);
            fuseBox.addClass(RenderedImageWriterSignature.class);
            fuseBox.addClass(RandomAccessArraySignature.class);

            fuseBox.addObject(new ClockFaceRenderer(200, new Color(40, 40, 150), true));

            for (Fuse fuse : fuseBox) {
                trace.info(fuse);
            }
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }

    public FuseBox get() {
        return fuseBox;
    }
}
