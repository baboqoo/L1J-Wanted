package xnetwork;

import java.nio.ByteBuffer;

public interface ConnectionHandler {
	public void onRecv(Connection connection, ByteBuffer buffer);
	public void onSend(Connection connection);
	public void onDisconnect(Connection connection);
}

