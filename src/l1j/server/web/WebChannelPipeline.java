package l1j.server.web;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public interface WebChannelPipeline {
	public void initialize(SocketChannel ch, ChannelPipeline pipe);
	public int get_port();
	public void close();
}

