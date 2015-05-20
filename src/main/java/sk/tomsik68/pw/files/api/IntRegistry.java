package sk.tomsik68.pw.files.api;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.naming.NameAlreadyBoundException;

import org.apache.commons.lang.Validate;

public abstract class IntRegistry<R> {
	private final Object lock = new Object();
	protected HashMap<Integer, R> elements = new HashMap<Integer, R>();

	public void load(File pluginFolder) throws IOException {
	}

	public void save(File pluginFolder) throws IOException {
	}

	public R get(int key) {

		synchronized (lock) {
			return elements.get(key);
		}
	}

	public void register(int key, R element) throws NameAlreadyBoundException {
		Validate.notNull(element);
		synchronized (lock) {
			if (elements.containsKey(key))
				throw new NameAlreadyBoundException(getClass().getSimpleName() + " conflicts at '" + key + "'!");
			else
				elements.put(key, element);
		}
	}

	public boolean isRegistered(int key) {
		return elements.containsKey(key);
	}

	public Collection<Integer> getRegistered() {
		return Collections.unmodifiableCollection(elements.keySet());
	}
}
