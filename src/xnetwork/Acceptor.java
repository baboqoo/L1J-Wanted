package xnetwork;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.nio.channels.*;

final public class Acceptor implements SelectorObject {
	private ServerSocketChannel _ssc;
	private final SelectorThread _selector;
	private final int _listenPort;
	private final AcceptorHandler _handler;

	public Acceptor(int listenPort, SelectorThread ioThread, AcceptorHandler listener) {
		_selector	= ioThread;
		_listenPort	= listenPort;
		_handler	= listener;
	}

	public void startAccept() throws IOException {
		_ssc = ServerSocketChannel.open();
		InetSocketAddress isa = new InetSocketAddress(_listenPort);
		_ssc.socket().bind(isa, 100);

		_selector.registerChannelLater(_ssc, SelectionKey.OP_ACCEPT, this, new CallbackErrorHandler() {
			public void handleError(Exception ex) {
				_handler.onError(Acceptor.this, ex);
			}
		});
	}

	public String toString() {
		return "ListenPort: " + _listenPort;
	}

	public void handleAccept() {
		SocketChannel sc = null;
		try {
			sc = _ssc.accept();
			_selector.addChannelInterestNow(_ssc, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			_handler.onError(this, e);
		}
		if (sc != null) {
			try {
				sc.socket().setReceiveBufferSize(1024 << 1);
				sc.socket().setSendBufferSize(1024 << 1);
				_handler.onAccept(this, sc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		try {
			_selector.invokeAndWait(new Runnable() {
				public void run() {
					if (_ssc != null) {
						try {
							_ssc.close();
						} catch (IOException e) {
						}
					}
				}
			});
		} catch (InterruptedException e) {
		}
	}
}
