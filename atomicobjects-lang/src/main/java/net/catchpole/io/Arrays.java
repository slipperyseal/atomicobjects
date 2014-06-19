package net.catchpole.io;

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

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public final class Arrays {
    private Arrays() {
    }

    public static void spool(InputStream is, OutputStream os) throws IOException {
        spool(is, os, 4096);
    }

    public static void spool(InputStream is, OutputStream os, int spoolSize) throws IOException {
        spool(is, os, new byte[spoolSize]);
    }

    public static void spool(InputStream is, OutputStream os, byte[] spoolBuffer) throws IOException {
        int l;
        while ((l = is.read(spoolBuffer, 0, spoolBuffer.length)) != -1) {
            os.write(spoolBuffer, 0, l);
        }
    }

    public static byte[] streamToByteArray(InputStream is) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        spool(is, baos);
        return baos.toByteArray();
    }

    public static byte[] compressGzip(byte[] data) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
        final GZIPOutputStream gzos = new GZIPOutputStream(baos);
        gzos.write(data);
        gzos.finish();
        return baos.toByteArray();
    }

    public static <T> T[] resizeArray(T[] source, int newLen) {
        return resizeArray(source, 0, newLen);
    }

    public static <T> T[] resizeArray(T[] source, int start, int newLen) {
        final int sourceLen = source.length - start;
        final T[] result = (T[]) Array.newInstance(source.getClass().getComponentType(), newLen);
        System.arraycopy(source, start, result, 0, (sourceLen < newLen ? sourceLen : newLen));
        return result;
    }

    public static byte[] resizeArray(byte[] source, int start, int newLen) {
        final int sourceLen = source.length - start;
        byte[] result = new byte[newLen];
        System.arraycopy(source, start, result, 0, (sourceLen < newLen ? sourceLen : newLen));
        return result;
    }

    public static int[] resizeArray(int[] source, int start, int newLen) {
        final int sourceLen = source.length - start;
        final int[] result = new int[newLen];
        System.arraycopy(source, start, result, 0, (sourceLen < newLen ? sourceLen : newLen));
        return result;
    }

    public static long[] resizeArray(long[] source, int start, int newLen) {
        final int sourceLen = source.length - start;
        final long[] result = new long[newLen];
        System.arraycopy(source, start, result, 0, (sourceLen < newLen ? sourceLen : newLen));
        return result;
    }

    public static String arrayToString(Object[] oa) {
        final StringBuilder sb = new StringBuilder((oa.length + 1) * 32);
        for (int x = 0; x < oa.length; x++) {
            if (x != 0) {
                sb.append(';');
            }
            sb.append(oa);
        }
        return sb.toString();
    }

    public static void reverse(List list) {
        int l = list.size() >> 1;
        for (int x = 0; x < l; x++) {
            Object objectX = list.get(x);
            int y = list.size() - 1 - x;
            list.set(x, list.get(y));
            list.set(y, objectX);
        }
    }

    public static List asList(Enumeration e) {
        final List list = new ArrayList(32);
        while (e.hasMoreElements()) {
            list.add(e.nextElement());
        }
        return list;
    }

    public static List asList(Iterator i) {
        final ArrayList list = new ArrayList(32);
        while (i.hasNext()) {
            list.add(i.next());
        }
        return list;
    }

    public static int indexOf(Object[] array, Object object) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == object) {
                return i;
            }
        }
        return -1;
    }

    public static int maxIndex(float[] values) {
        float max = 0;
        for (float value : values) {
            if (value > max) {
                max = value;
            }
        }
        for (int x = 0; x < values.length; x++) {
            if (values[x] == max) {
                return x;
            }
        }
        return 0;
    }

    public static boolean isBit(long[] bits, int index) {
        return isBit(bits[index >> 6], index & 63);
    }

    public static void setBit(long[] bits, int index, boolean value) {
        bits[index >> 6] = setBit(bits[index >> 6], index & 63, value);
    }

    public static boolean isBit(long bits, int index) {
        return ((1 << index) & bits) != 0;
    }

    public static long setBit(long bits, int index, boolean value) {
        if (value) {
            bits |= (1 << index);
        } else {
            bits &= ~(1 << index);
        }
        return bits;
    }

    /**
     * Merges elements in a list equate to the same value.
     * This is used to reduce the number of object references where objects have equality.
     * This method has reasonably constant performance as the size of the list increases and uses hashing
     * rather than repeated search.
     */
    public static void mergeReferences(List list) {
        final Map map = new HashMap(list.size());

        // put all values into map - equal values will merge references
        for (Object entry : list) {
            map.put(entry, entry);
        }
        // replace all values with possible singular references
        int len = list.size();
        for (int x = 0; x < len; x++) {
            list.set(x, map.get(list.get(x)));
        }
    }

    public static boolean arrayEquals(byte[] a, byte[] b, int len) {
        for (int x = 0; x < len; x++) {
            if (a[x] != b[x]) {
                return false;
            }
        }
        return true;
    }

    public static void readFully(InputStream is, byte[] buf) throws IOException {
        int offset = 0;
        int len = buf.length;
        while (len > 0) {
            int numread = is.read(buf, offset, len);
            if (numread < 0)
                throw new EOFException();
            len -= numread;
            offset += numread;
        }
    }

    public static int readBlocking(InputStream is, byte[] buf, int offset) throws IOException {
        int position = offset;
        int len = buf.length - position;
        while (len > 0) {
            int numread = is.read(buf, position, len);
            if (numread < 0) {
                // EOF - return current read if present else -1
                return position == offset ? -1 : position;
            }
            len -= numread;
            position += numread;
        }
        return position;
    }

    public static int indexOf(byte[] buffer, int len, byte[] value) {
        System.out.println("undexof len=" + len);
        final byte first = value[0];
        int stop = len-value.length;
        for (int x=0;x<stop;x++) {
            System.out.println("buffer[" + x + "] " + ((char)buffer[x]) + " == " + first + " = " + (buffer[x] == first));

            if (buffer[x] == first) {
                if (arrayEquals(buffer, x, value)) {
                    System.out.println("equals: " + x);
                    return x;
                }
            }
        }
        return -1;
    }

    public static void copy(byte[] buffer, int from, int to, int len) {
        for (int x=0;x<len;x++) {
            buffer[to++] = buffer[from++];
        }
    }

    public static boolean arrayEquals(byte[] buffer, int offset, byte[] value) {
        for (int x=0;x<value.length;x++) {
            if (buffer[offset++] != value[x]) {
                return false;
            }
        }
        return true;
    }

    public static boolean spoolUntilBoundary(InputStream is, OutputStream os, byte[] boundary) throws IOException {
        short[] rotor  = new short[boundary.length];
        java.util.Arrays.fill(rotor, (short)-1);

        int lastByte = boundary[boundary.length-1] & 0xff;
        for (;;) {
            for (int x=0;x<rotor.length;x++) {
                int next = x+1 == rotor.length ? 0 : x+1;
                int value = is.read();
                if (value == -1) {
                    rotorWrite(rotor, os, next);
                    return false;
                } else {
                    if ((rotor[x] = (short)(value & 0xff)) == lastByte && rotorSearch(rotor, boundary, next)) {
                        return true;
                    }
                    int v = rotor[next];
                    if (v != -1) {
                        os.write(v);
                    }
                }
            }
        }
    }

    private static boolean rotorSearch(short[] rotor, byte[] boundary, int start) {
        int b=0;
        for (int x=start;x<rotor.length;x++) {
            if (rotor[x] != boundary[b++]) {
                return false;
            }
        }
        for (int x=0;x<start;x++) {
            if (rotor[x] != boundary[b++]) {
                return false;
            }
        }
        return true;
    }

    private static void rotorWrite(short[] rotor, OutputStream os, int start) throws IOException {
        for (int x=start;x<rotor.length;x++) {
            int value = rotor[x];
            if (value != -1) {
                os.write(value);
            }
        }
        start--;
        for (int x=0;x<start;x++) {
            int value = rotor[x];
            if (value != -1) {
                os.write(value);
            }
        }
    }
}
