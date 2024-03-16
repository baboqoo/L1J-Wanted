package xnetwork;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class Connection implements SelectorObject, CallbackErrorHandler {
	protected final SelectorThread _selector;
	private final SocketChannel _sc;
	private ByteBuffer _inBuffer;
	private ByteBuffer _outBuffer;
	private LinkedList<byte[]> _reserveOutBuffer;

	private static final int BUFFER_SIZE	= 64 * 1024; // 대략 BufferSize는 64k

	private final ConnectionHandler _handler;

	public SocketChannel getSocketChannel() {
		return _sc;
	}

	public String getHostAddress() {
		return _sc.socket().getInetAddress().getHostAddress();
	}

	public Connection(SocketChannel socketChannel, SelectorThread selector, ConnectionHandler handler)
			throws IOException {
		_selector			= selector;
		_sc					= socketChannel;
		_handler			= handler;
		_inBuffer			= ByteBuffer.allocateDirect(BUFFER_SIZE);
		_outBuffer			= ByteBuffer.allocateDirect(BUFFER_SIZE);
		_outBuffer.flip();
		_reserveOutBuffer	= new LinkedList<byte[]>();
		selector.registerChannelNow(_sc, 0, this);
	}

	public boolean isOpen() {
		return _sc.isOpen();
	}

	public void send(final byte[] buffer) {
		// keeps a reference to the packet. In production code this should copy
		// the contents of the buffer.
		_selector.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					if (!_sc.isOpen()) {
						return;
					}
					if (_outBuffer.hasRemaining()) {
						_reserveOutBuffer.add(buffer);
					} else {
						_outBuffer.clear();
						_outBuffer.put(buffer);
						_outBuffer.flip();

						handleWrite();
					}
				} catch (Exception ex) {
					close();
				}
			}
		});
	}

	public void handleRead() {
		try {
			int readBytes = _sc.read(_inBuffer);
			if (readBytes == -1) {
				close();
				return;
			}

			if (readBytes == 0) {
				return;
			}
			_inBuffer.flip();
			_handler.onRecv(this, _inBuffer);

			if (_inBuffer.hasRemaining()) {// 처리 안한거 있으면
				byte[] buffer = new byte[_inBuffer.remaining()];
				_inBuffer.get(buffer);
				_inBuffer.clear();
				_inBuffer.put(buffer);
			} else {
				_inBuffer.clear();
			}
			_selector.addChannelInterestNow(_sc, SelectionKey.OP_READ);

		} catch (Exception ex) {
			// Serious error. Close socket.
			close();
		}
	}

	public void handleWrite() {
		try {
			while (true) {
				_sc.write(_outBuffer);
				if (_outBuffer.hasRemaining()) {
					requestWrite();
				} else {
					if (_reserveOutBuffer.size() > 0) {
						_outBuffer.clear();

						do {
							_outBuffer.put(_reserveOutBuffer.remove());

							if (_reserveOutBuffer.size() < 1
									|| _outBuffer.remaining() < _reserveOutBuffer.getFirst().length) {
								break;
							}
						} while (true);

						_outBuffer.flip();
						continue;
					}

					_handler.onSend(this);
				}
				break;
			}
		} catch (Exception ex) {
			close();
		}
	}

	public void close() {
		_selector.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					if (!_sc.isOpen()) {
						return;
					}
					_sc.close();
					_handler.onDisconnect(Connection.this);
				} catch (Exception e) {
				}
			}
		});
	}

	public void resumeRecv() throws IOException {
		_selector.addChannelInterestLater(_sc, SelectionKey.OP_READ, this);
	}

	private void requestWrite() throws IOException {
		_selector.addChannelInterestNow(_sc, SelectionKey.OP_WRITE);
	}

	@Override
	public void handleError(Exception ex) {
		close();
	}
	
}
