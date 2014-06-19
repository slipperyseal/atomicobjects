package net.catchpole.model;

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

import net.catchpole.lang.Disposable;

import java.util.Iterator;

/**
 * The GraphItererator is used to traverse objects graphs.
 * <p/>
 * <p>It is Iterable in that it can return subsequent GraphIterators for objects related to the currently selected
 * object.
 * <p/>
 * <p>The currently selected Object is the one returned by the last call to next();
 */

public interface GraphIterator<T> extends Iterator<T>, Iterable<T>, Disposable {
    public boolean hasNext();

    public T next();

    public void remove();

    /**
     * Returns a GraphIterator for the contents of the node returned by the last call to <code>next()</code>
     */
    public GraphIterator iterator();

    /**
     * Sets a filter for subsequent calls to next().
     */
    //public void addFilter(Object filter) throws IllegalArgumentException;

    /**
     * Returns an attribute by specifiying a class or interface.
     * This method provides an open implementation to define custom attribute.  Using a class as the parameter
     * reduces imbiguity of the returned type and creates effective type safety.
     * eg.  RemainingObjects re = (RemainingObjects)gi.getAttribute(RemainingObjects.class);
     */
    //public Object getAttribute(Class type) throws IllegalArgumentException;

    /**
     * Indicates that this iterator will no longer be accessed, allowing internal references to be released.
     * This method should be called if an itorator does not run the length of the collection.
     * Fully iterating a collection should have the same logical effect.
     */
    public void dispose();
}
