package net.catchpole.collection;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A filter for a list which sends messages to zero or more ListTrackers when elements are added or removed.
 */
public class TrackedList<E> implements List<E> {
    private final List<E> list;
    private final ArrayList<ListTracker<E>> trackers = new ArrayList<ListTracker<E>>();

    public TrackedList(List<E> list) {
        this.list = list;
    }

    public void addListTracker(ListTracker<E> listTracker) {
        this.trackers.add(listTracker);
    }

    public void removeListTracker(ListTracker<E> listTracker) {
        this.trackers.remove(listTracker);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Iterator<E> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T> T[] toArray(T[] ts) {
        return list.toArray(ts);
    }

    public boolean add(E e) {
        boolean contains = list.add(e);
        if (trackers.size() != 0) {
            for (ListTracker<E> tracker : trackers) {
                tracker.elementAdded(e, list.indexOf(e));
            }
        }
        return contains;
    }

    public boolean remove(Object e) {
        int indexOf = list.indexOf(e);
        boolean contains = list.remove(e);
        if (trackers.size() != 0) {
            for (ListTracker<E> tracker : trackers) {
                tracker.elementRemoved((E) e, indexOf);
            }
        }
        return contains;
    }

    public boolean containsAll(Collection<?> objects) {
        return list.containsAll(objects);
    }

    public boolean addAll(Collection<? extends E> es) {
        return list.addAll(es);
    }

    public boolean addAll(int i, Collection<? extends E> es) {
        return list.addAll(i, es);
    }

    public boolean removeAll(Collection<?> objects) {
        return list.removeAll(objects);
    }

    public boolean retainAll(Collection<?> objects) {
        return list.removeAll(objects);
    }

    public void clear() {
        list.clear();
    }

    public E get(int i) {
        return list.get(i);
    }

    public E set(int i, E e) {
        return list.set(i, e);
    }

    public void add(int i, E e) {
        list.add(i, e);
    }

    public E remove(int i) {
        return list.remove(i);
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    public ListIterator<E> listIterator(int i) {
        return list.listIterator();
    }

    public List<E> subList(int i, int i1) {
        return list.subList(i, i1);
    }
}
