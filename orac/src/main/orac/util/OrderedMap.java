/*
 * Copyright (C) 2006-2008 Science and Technology Facilities Council.
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package orac.util;

import java.util.TreeMap;
import java.util.Vector;

/**
 * OrderedMap is a map, but retains the order items were placed into the
 * structure.
 *
 * Items fetched by name have the lookup speed of a map, with the slight
 * overhead of calling the map's methods indirectly. Items can be found
 * by name or by index.
 *
 * NB. OrderedMap does not check for duplication, what can happen is that
 * a duplicate key will be overridden by the first instance if searching
 * for its index, and the object will have been replaced by the duplicate
 * (most recent addition of).  Multiple indices will point to the
 * duplicated object.
 */
public class OrderedMap<S, O> {
    final private TreeMap<S, O> treeMap;
    final private Vector<S> vector;
    private int size;

    public OrderedMap() {
        treeMap = new TreeMap<S, O>();
        vector = new Vector<S>();
    }

    public synchronized void add(final S key, final O object) {
        treeMap.put(key, object);
        vector.add(key);
        size++;
    }

    public synchronized O remove(final int index) {
        final S key = vector.remove(index);
        final O object = treeMap.remove(key);

        if (object != null) {
            size--;
        }

        return object;
    }

    public synchronized O remove(final S key) {
        final O object = treeMap.remove(key);
        vector.remove(key);

        if (object != null) {
            size--;
        }

        return object;
    }

    public boolean containsKey(final S key) {
        final boolean containsKey = treeMap.containsKey(key);
        return containsKey;
    }

    public O find(final S key) {
        final O object = treeMap.get(key);
        return object;
    }

    public O find(int index) {
        final S key = vector.elementAt(index);
        O object = treeMap.get(key);
        return object;
    }

    public S getNameForIndex(int index) {
        return vector.elementAt(index);
    }

    public Vector<S> keys() {
        return vector;
    }

    public int getIndexForKey(S key) {
        return vector.indexOf(key);
    }

    public int size() {
        return size;
    }

    public void clear() {
        vector.clear();
        treeMap.clear();
        size = 0;
    }

    public synchronized void move(S key, int index) {
        int current = getIndexForKey(key);

        if (current != index && current > -1) {
            vector.remove(current);
            vector.insertElementAt(key, index);
        }
    }

    public synchronized void move(int currentIndex, int newIndex) {
        if (currentIndex != newIndex && currentIndex > -1
                && currentIndex < size()) {
            S object = vector.remove(currentIndex);
            newIndex = newIndex < size() ? newIndex : size() - 1;
            vector.insertElementAt(object, newIndex);
        }
    }
}
