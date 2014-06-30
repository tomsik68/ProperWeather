package sk.tomsik68.pw.api.registry;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

import javax.naming.NameAlreadyBoundException;

import org.apache.commons.lang.Validate;

public class BaseRegistry<R> {
    private final Object lock = new Object();
    protected HashMap<String, R> elements = new HashMap<String, R>();

    public void load(File pluginFolder) throws IOException {

    }

    public void save(File pluginFolder) throws IOException {

    }

    public R get(String name) {
        Validate.notNull(name);
        R result;
        synchronized (lock) {
            if (!elements.containsKey(name))
                throw new NoSuchElementException("Element '" + name + "' doesn't exist!");
            result = elements.get(name);
        }
        return result;
    }

    public void register(String name, R element) throws NameAlreadyBoundException {
        Validate.notNull(name);
        Validate.notNull(element);
        synchronized (lock) {
            if (elements.containsKey(name))
                throw new NameAlreadyBoundException(getClass().getSimpleName() + " conflicts at '" + name + "'!");
            else
                elements.put(name, element);
        }
    }

    public boolean isRegistered(String name) {
        Validate.notNull(name);
        return elements.containsKey(name);
    }

    public Collection<String> getRegistered() {
        return Collections.unmodifiableCollection(elements.keySet());
    }
}
