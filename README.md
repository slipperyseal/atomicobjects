Atomic Objects
===========

Automagical Object Transformations

```
This is an old project. Its really just here for prosperity.

The main flaw with this project is that it assumed magically well crafted and well interfaced classes
could interact with each other. But who could write such magical classes? Not me, that's for sure.

So... this was the idea..
```

Atomic is a collection of Java modules that provide self-managed integration of data sources, component libraries and user interfaces.  These modules can be used individually by developers or in combination as an application.

The modules are automatic in that they require little to no configuration.  Each module maintains meta data about its resources.  The database module reflects all table and relational information from a database.  The Java component module extracts annotations from its set of component classes.  These describe how, when and why components can be constructed and methods involked.  XML and service layer modules can expose, or even attempt to align input models based on requests.  The GUI module can be used to traverse the overall data model.  This GUI uses OpenGL to create a performant scene graph using both 2D and 3D techniques for visually representing the abstract model.

* Fuse - Java class components - net.catchpole.fuse
* Visualization - A user interface and 2/3D visualization that utilizes OpenGL. - net.catchpole.scene
* Web - A connector for browser based data access. - net.catchpole.web
* Web Services - XML bindings with connectors for SOAP and REST. - net.catchpole.dom
* File - Allows the system to access files and directories for data import or on disk managment. - net.catchpole.io
* Disq - Exposes system managed data as a virtual file system server (such as NFS, Samba, FTP etc) - net.catchpole.disq
* SQL - Database persistance layer that uses database meta data and inferences to self-configure. - net.catchpole.sql

The Fuse component model combines several techniques:
* Runtime Interface implementation
* Inversion of control
* Metadata annotations
* Binding using dependancy graph search
* A standard abstract Model for graph traversal.

Runtime Interface implementation can be thought of as Inversion of Inversion of Control.  This allows the POJOs to make calls against Interfaces which are implemented by the container.  The advantages of this are:

* Reduces unnecessary object configuration.
* Reduced boiler plate (only call what you need)
* Late as possible binding and resolution
* Clean distinction of the concrete functional code and external dependencies.  This encourages the component mindset and interface design.

The project also aims to supply a library of quality modules. These can be used independently of the container:

* Runtime Interface implementation
* Abstract persistence including Object, File and Reflective Database persistence.
* XML DOM to Model mapping.
* Bean reflection to Model mapping.
* OpenGL based Model Visualization
* Automatic Web Interface.
* Color ANSI Console
* IO Stream Utilities including "Stream Sources" interfaces.
* [trace] API (advanced logging)
* Runtime Annotations for describing programatic patterns for class methods.
* Special purpose Collections API.
* Imaging utilities.
* Resource mapping into class paths, file systems and custom URL implementations.
* Clusterable communications and archiving protocol.
* Efficient HTTP Multipart (File Upload) implementation using Stream Sources.
* Various high quality utility classes such as stream, reflection, binary, math, string and build time tools.
* Single Page Overview Doclet.

This project uses the following Open Source libraries:
* Janino embedded compiler 
* JOGL OpenGL
* Jetty embedded servlet container
* HSQLDB embedded database
* JUnit

![atomic](https://storage.googleapis.com/kyoto.catchpole.net/atomic-spin.jpg "atomic")

# Fuses

A Fuse will accept a parameter Criteria and produce a result, itself defined as a Criteria.

        public interface Fuse {
            public Criteria getInputCriteria();

            public Criteria getOutputCriteria();

            public Object[] involk(Object[] params) throws Exception;
        }

It could be a method call on a object or constructing and Object. It could be pulling data from a database. It could be something quite complex, or something quite simple.

To make a Fuse, simply annotate a Class and define which methods or constructors provide Input, Output or Transformations.

        @Fuse
        public class RandomAccessArray implements RandomAccessStream {
            private byte[] bytes;
            private int length;
            private int pos;

            @Input
            public RandomAccessArray(byte[] bytes) {
                this.bytes = bytes;
                this.length = bytes.length;
            }

            ...

        @Fuse
        public class RenderedImageWriter {
            private final static String defaultFormatName = "PNG";
            ...
            @Transform
            public byte[] write(RenderedImage image) throws IOException {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                write(image, baos);
                return baos.toByteArray();
            }
            ...
        }

If you want to annotate classes which aren't yours or are final, or wish to create alternate annotations, simply annotate a "signature" class and define the actual class to use.

        @Fuse(classname = "java.lang.String")
        public abstract class StringSignature {
            @Output
            public abstract byte[] getBytes();
        }

Here we register a set of Fuses with the FuseBox...

        Table table = airlineDatabase.getDatabase().getTable("schedule");
        fuseBox.addFuse(new TableFuse(table, table.getColumn("call_sign"), table.getColumn("arrive_time")));
        fuseBox.addClass(ByteArrayInputStreamSignature.class);
        fuseBox.addClass(StringSignature.class);
        fuseBox.addClass(Stringer.class);
        fuseBox.addClass(StreamEater.class);
        fuseBox.addClass(RenderedImageWriter.class);
        fuseBox.addClass(RandomAccessArray.class);
        fuseBox.addObject(new ClockFaceRenderer(200, new Color(40,40,150), true));
    
A Plan is a sequence of Fuses to call to find a result based on a request Criteria.  Graph traversal is used to search all the available Fuses.  A Criteria is a set of Classes.  They can optionally be named.

In our test case, we have a Database Table fuse connected to the Schedule table of the Airline database.

The Input Criteria is:

            name="call_sign"
            type=java.lang.String

The Output Criteria is:

            type=net.catchpole.io.RandomAccessStream

A RandomAccessStream is similar to a RandomAccessFile. So, based on this FuseBox, making this request will result in this as indicated by the log trace...

        PlanTest.find Plan 4 fuses
        PlanTest.find     fuse:  TableFuse Column SCHEDULE/CALL_SIGN null:PUBLIC FK: FLEET 8 > Column SCHEDULE/ARRIVE_TIME null:PUBLIC 6
        PlanTest.find     fuse:  TransformFuse ClockFaceRenderer 200 ff282896 true
        PlanTest.find     fuse:  NewInstanceMethodFuse byte[]=RenderedImageWriter.write(RenderedImage)
        PlanTest.find     fuse:  ConstructorFuse public net.catchpole.io.RandomAccessArray(byte[])

Fuses have been found which can be called in sequence to convert the input Criteria into the output Criteria.  The arrival_time is selected from the Schedule table where call_sign = VH123.  The result is a Timestamp object.  As Timestamp extends java.util.Date, this can be transformed by the ClockFaceRenderer into a BufferedImage.  This BufferedImage can then be converted into a PNG using RenderedImageWriter.write().  This method returns a byte array.  A RandomAccessArray (which implements RandomAccessStream) can be constructed from that byte array.

This output of the Plan Iterator be seen below..

        Prospect.getSelectPreparedStatement select AIRLINE,FLIGHT,CALL_SIGN,DEPART_AIRPORT,ARRIVE_AIRPORT,DEPART_TIME,
                                   ARRIVE_TIME,STATUS from SCHEDULE where CALL_SIGN=? order by AIRLINE,FLIGHT

        PlanTest.find     iterator: 2007-01-01 14:00:00.0
        PlanTest.find     iterator: BufferedImage@6f878144: type = 2 DirectColorModel: rmask=ff0000 gmask=ff00 bmask=ff amask=ff000000
                                   IntegerInterleavedRaster: width = 200 height = 200 #Bands = 4 xOff = 0 yOff = 0 dataOffset[0] 0
        PlanTest.find     iterator: [B@31ad98ef
        PlanTest.find     iterator: RandomAccessArray 4606 bytes

        PlanTest.test RESULT > RandomAccessArray 4606 bytes class net.catchpole.io.RandomAccessArray

