package org.apache.wicket.osgi.classloader;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.wicket.Application;
import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.util.collections.UrlExternalFormComparator;
import org.apache.wicket.util.lang.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompoundClassResolver implements IClassResolver {

    private static final Logger logger = LoggerFactory.getLogger(CompoundClassResolver.class);

    private final List<IClassResolver> resolvers = new CopyOnWriteArrayList<IClassResolver>();

    public Class<?> resolveClass(final String className) throws ClassNotFoundException {
        boolean debug = logger.isDebugEnabled();
        for (IClassResolver resolver : resolvers) {
            try {
                return resolver.resolveClass(className);
            } catch (ClassNotFoundException cnfx) {
                if (debug) {
                    logger.debug("ClassResolver '{}' cannot find class: '{}'", resolver.getClass().getName(), className);
                }
            }
        }

        throw new ClassNotFoundException(className);
    }

    public Iterator<URL> getResources(final String name) {
        Args.notNull(name, "name");

        Set<URL> urls = new TreeSet<URL>(new UrlExternalFormComparator());

        for (IClassResolver resolver : resolvers) {
            Iterator<URL> it = resolver.getResources(name);
            while (it.hasNext()) {
                URL url = it.next();
                urls.add(url);
            }
        }

        return urls.iterator();
    }

    public ClassLoader getClassLoader() {
        if (Application.get().usesDeploymentConfig()) {
            return resolvers.isEmpty() ? Thread.currentThread().getContextClassLoader() : resolvers.iterator().next().getClassLoader();
        }
        return new CompoundClassLoader(resolvers);
    }

    public void add(final IClassResolver resolver) {
        resolvers.add(resolver);
    }

    public void remove(final IClassResolver resolver) {
        resolvers.remove(resolver);
    }

}
