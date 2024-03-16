package l1j.server.web;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import l1j.server.Config;

public class WebDefaultPipeline implements WebChannelPipeline {
	private boolean		_active;
	private final int	_port;
	private SslContext	_sslContext;

	public WebDefaultPipeline(int port) {
		this._active			= true;
		this._port				= port;
		if (Config.WEB.WEB_SSL_ENABLE && port == 443) {
			this._sslContext	= SSLCertificator.getCertificate();
		}
	}
	
	@Override
	public void initialize(SocketChannel ch, ChannelPipeline pipe) {
		if (!_active) {
			ch.close();
			return;
		}
		if (_sslContext != null) {
			pipe.addLast(_sslContext.newHandler(ch.alloc()));
		}
		pipe.addLast(new HttpRequestDecoder());
		pipe.addLast(new HttpObjectAggregator(Config.WEB.MAX_CONTENT));
		pipe.addLast(new HttpResponseEncoder()); 
		pipe.addLast(new HttpContentCompressor());
		pipe.addLast(new WebClient());// 소켓 핸들러
	}
	
	@Override
	public int get_port() {
		return _port;
	}
	
	@Override
	public void close() {
		_active = false;
	}
}

