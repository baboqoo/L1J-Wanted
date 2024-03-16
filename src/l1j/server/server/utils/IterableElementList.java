package l1j.server.server.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IterableElementList implements Iterable<Element> {
	IterableNodeList _list;

	private class MyIterator implements Iterator<Element> {
		private Iterator<Node> _itr;
		private Element _next = null;

		public MyIterator(Iterator<Node> itr) {
			_itr = itr;
			updateNextElement();
		}

		private void updateNextElement() {
			Node node = null;
			while (_itr.hasNext()) {
				node = _itr.next();
				if (node instanceof Element) {
					_next = (Element) node;
					return;
				}
			}
			_next = null;
		}

		@Override
		public boolean hasNext() {
			return _next != null;
		}

		@Override
		public Element next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Element result = _next;
			updateNextElement();
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public IterableElementList(NodeList list) {
		_list = new IterableNodeList(list);
	}

	@Override
	public Iterator<Element> iterator() {
		return new MyIterator(_list.iterator());
	}

}

