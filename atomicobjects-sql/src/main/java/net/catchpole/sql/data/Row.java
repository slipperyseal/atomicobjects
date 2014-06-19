package net.catchpole.sql.data;

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

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 */
public class Row implements Iterable, Serializable {
    private boolean changed = false;
    private Object[] data;

    public Row(ResultSet resultSet, int width) throws SQLException {
        this.data = new Object[width];
        for (int x = 0; x < width; x++) {
            data[x] = resultSet.getObject(x + 1);
        }
    }

    public Row(int width) throws SQLException {
        data = new Object[width];
    }

    public Row(Object[] array) throws SQLException {
        this(array.length);
        System.arraycopy(array, 0, data, 0, array.length);
    }

    public int getWidth() {
        return data.length;
    }

    public Object get(int index) {
        return data[index];
    }

    public void set(int index, Object object) {
        Object original = data[index];
        if ((original == null && object != null) ||
                (original != null && object == null) ||
                (original != null && object != null && !original.equals(object))) {
            changed = true;
        }
        data[index] = object;
    }

    public void clearChanged() {
        this.changed = false;
    }

    public boolean hasChanged() {
        return changed;
    }

    public Iterator iterator() {
        return new RowIterator();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(80);
        boolean first = true;
        for (Object value : data) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
                sb.append(' ');
            }
            sb.append(value);
        }
        return sb.toString();
    }

    class RowIterator implements Iterator {
        private int pos;

        public boolean hasNext() {
            return pos < data.length;
        }

        public Object next() {
            return data[pos++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
