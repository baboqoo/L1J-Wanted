package l1j.server.server.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IterableNodeList implements Iterable<Node> {
	private final NodeList _list;

	private class MyIterator implements Iterator<Node> {
		private int _idx = 0;

		@Override
		public boolean hasNext() {
			return _idx < _list.getLength();
		}

		@Override
		public Node next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return _list.item(_idx++);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public IterableNodeList(NodeList list) {
		_list = list;
	}

	@Override
	public Iterator<Node> iterator() {
		return new MyIterator();
	}

}

