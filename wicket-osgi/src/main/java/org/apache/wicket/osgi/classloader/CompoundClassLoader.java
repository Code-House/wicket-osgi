package org.apache.wicket.osgi.classloader;

import java.util.List;

import org.apache.wicket.application.IClassResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompoundClassLoader extends ClassLoader {

    private final List<IClassResolver> resolvers;
    private Logger logger = LoggerFactory.getLogger(CompoundClassLoader.class);

    public CompoundClassLoader(List<IClassResolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        Class<?> clazz = round(new IClassResolverCallback<Class<?>>() {
            public Class<?> lookup(IClassResolver classResolver) throws Exception {
                return classResolver.getClassLoader().loadClass(name);
            }
        });
        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }

    protected <T> T round(IClassResolverCallback<T> callback) {
        for (IClassResolver resolver : resolvers) {
            try {
                return callback.lookup(resolver);
            } catch (Exception e) {
                logger.trace("Unable to execute callback {} using {}", callback, resolver);
            }
        }
        return null;
    }

    interface IClassResolverCallback<T> {
        T lookup(IClassResolver classResolver) throws Exception;
    }
}

