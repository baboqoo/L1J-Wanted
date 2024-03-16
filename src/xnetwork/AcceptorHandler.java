package xnetwork;

import java.nio.channels.SocketChannel;

public interface AcceptorHandler {
	public void onAccept(Acceptor acceptor, SocketChannel sc);
	public void onError(Acceptor acceptor, Exception ex);
}

