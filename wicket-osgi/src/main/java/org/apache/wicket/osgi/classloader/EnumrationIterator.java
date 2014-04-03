package org.apache.wicket.osgi.classloader;

import java.util.Enumeration;
import java.util.Iterator;

public class EnumrationIterator<T> implements Iterator<T> {

    private final Enumeration<T> enumeration;

    public EnumrationIterator(Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }

    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    public T next() {
        return enumeration.nextElement();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
