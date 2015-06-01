package sk.tomsik68.pw.utils;

import java.util.Iterator;
import java.util.List;

final class SynchronizedReadOnlyListWrapper<E> implements Iterable<E> {
	private List<E> list;

	public SynchronizedReadOnlyListWrapper(List<E> actualList) {
		this.list = actualList;
	}

	@Override
	public Iterator<E> iterator() {
		return new SynchronizedReadOnlyListIterator<E>(this);
	}

	E nth(int n) {
		return list.get(n);
	}

	int size() {
		return list.size();
	}
}
