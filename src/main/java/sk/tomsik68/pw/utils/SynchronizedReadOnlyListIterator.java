package sk.tomsik68.pw.utils;

import java.util.Iterator;

final class SynchronizedReadOnlyListIterator<E> implements Iterator<E> {
	private final SynchronizedReadOnlyListWrapper<E> list;
	private int i;

	public SynchronizedReadOnlyListIterator(SynchronizedReadOnlyListWrapper<E> list) {
		this.list = list;
		i = 0;
	}

	@Override
	public boolean hasNext() {
		return i < list.size() - 1;
	}

	@Override
	public E next() {
		++i;
		return list.nth(i);
	}

	@Override
	public void remove() {
	}

}
