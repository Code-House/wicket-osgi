package org.apache.wicket.osgi;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.wicket.application.IClassResolver;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OsgiClassResolver implements IClassResolver {
    private Logger logger = LoggerFactory.getLogger(OsgiClassResolver.class);
    private final BundleContext context;

    public OsgiClassResolver(BundleContext context) {
        this.context = context;
    }

    public Class<?> resolveClass(String classname) throws ClassNotFoundException {
        logger.debug("Resolve class {}", classname);
        return context.getBundle().loadClass(classname);
    }

    @SuppressWarnings("unchecked")
    public Iterator<URL> getResources(String name) {
        logger.debug("Resolve resource {}", name);
        try {
            return new EnumerationIterator<URL>(context.getBundle().getResources(name));
        } catch (IOException e) {
            logger.error("");
        }
        return null;
    }

    public ClassLoader getClassLoader() {
        return new BundleDelegatingClassLoader(context.getBundle());
    }

}

class EnumerationIterator<T> implements Iterator<T> {

    private final Enumeration<T> enumeration;

    public EnumerationIterator(Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }

    public boolean hasNext() {
        return enumeration != null && enumeration.hasMoreElements();
    }

    public T next() {
        return enumeration.nextElement();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
