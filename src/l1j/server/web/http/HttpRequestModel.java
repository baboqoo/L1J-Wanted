package l1j.server.web.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.WebClient;
import l1j.server.web.dispatcher.DispatcherLoader;

/**
 * HTTP 요청 클래스
 * @author LinOffice
 */
public class HttpRequestModel implements HttpRequest {
	private static final int INIT_COOKIE			= 1;
	private static final int INIT_PARAMS			= INIT_COOKIE << 1;
	private static final int INIT_MULTI_PART		= INIT_PARAMS << 1;
	private static final int INIT_POST_DATA			= INIT_MULTI_PART << 1;
	
	private static final String USER_AGENT_INGAME	= "nc ingame";
	private static final String USER_AGENT_LAUNCHER	= "nc launcher";
	private static final String USER_AGENT_MOBILE	= ".*(LG|SAMSUNG|Samsung|iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*";
	private static final String USER_AGENT_REGEX	= ".*(nc launcher|Trident|MSIE|Edge|Chrome|Safari|Firefox|Opera|LG|Samsung|iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*";
	
	private static final HttpDataFactory FACTORY	= new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);// 데이터 처리량 16KB

	protected HttpRequest request;
	protected ChannelHandlerContext ctx;
	
	private int bit;
	
	protected Collection<Cookie> cookies;
	protected Map<String, List<String>> parameters;
	protected Map<String, String> postDatas;
	protected String requestUri;
	
	protected boolean multipart;
	protected byte[] body;
	
	protected String userAgent;
	protected boolean ingame;
	protected boolean launcher;
	protected boolean mobile;
	
	protected boolean is_bad_request;
	protected boolean is_bad_user_agent; 
	
	public HttpRequestModel(HttpRequest request, ChannelHandlerContext ctx){
		this.request		= request;
		this.ctx			= ctx;
		this.bit			= 0;
		this.cookies		= null;
		this.parameters		= null;
		this.requestUri		= StringUtil.EmptyString;
		this.userAgent		= headers().get(HttpHeaderNames.USER_AGENT);
		if (!is_user_agent_validation()) {
			return;
		}
		this.ingame		= this.userAgent.indexOf(USER_AGENT_INGAME) != -1;
		this.launcher	= this.userAgent.indexOf(USER_AGENT_LAUNCHER) != -1;
		this.mobile		= this.userAgent.matches(USER_AGENT_MOBILE);
	}
	
	/**
	 * 정상적인 브라우저에서 요청이 이루어졌는지 검증한다.
	 * @return boolean
	 */
	protected boolean is_user_agent_validation() {
		if (StringUtil.isNullOrEmpty(userAgent) || !userAgent.matches(USER_AGENT_REGEX)) {
			is_bad_user_agent = true;
			return false;
		}
		return true;
	}
	
	@Override
	public HttpHeaders headers() {
		return request.headers();
	}
	
	@Override
	public HttpRequest setProtocolVersion(HttpVersion version) {
		request.setProtocolVersion(version);
		return this;
	}
	
	@Override
	public HttpVersion getProtocolVersion() {
		return protocolVersion();
	}
	
	@Override
	public HttpVersion protocolVersion() {
		return request.protocolVersion();
	}
	
	@Override
	public HttpRequest setMethod(HttpMethod method) {
		request.setMethod(method);
		return this;
	}
	
	@Override
	public HttpRequest setUri(String uri) {
		request.setUri(uri);
		return this;
	}
	
	@Override
	public DecoderResult getDecoderResult() {
		return request.decoderResult();
	}
	
	@Override
	public void setDecoderResult(DecoderResult result) {
		request.setDecoderResult(result);
	}
	
	@Override
	public DecoderResult decoderResult() {
		return request.decoderResult();
	}

	@Override
	public HttpMethod getMethod() {
		return method();
	}

	@Override
	public HttpMethod method() {
		return request.method();
	}
	
	@Override
	public String getUri() {
		return uri();
	}
	
	@Override
	public String uri() {
		return request.uri();
	}
	
	/**
	 * 부정 요청 검증
	 * @return boolean
	 */
	public boolean is_bad_request() {
		return is_bad_request;
	}
	
	/**
	 * 부정 브라우저 환경 요청 정보
	 * @return boolean
	 */
	public boolean is_bad_user_agent() {
		return is_bad_user_agent;
	}
	
	/**
	 * User-Agent
	 * @return String
	 */
	public String get_user_agent() {
		return userAgent;
	}
	
	/**
	 * 인게임 요청 검증
	 * @return boolean
	 */
	public boolean isIngame() {
		return ingame;
	}
	
	/**
	 * 런처 요청 검증
	 * @return boolean
	 */
	public boolean isLauncher() {
		return launcher;
	}
	
	/**
	 * 모바일 요청 검증
	 * @return boolean
	 */
	public boolean isMobile() {
		return mobile;
	}

	public ChannelHandlerContext get_ctx(){
		return ctx;
	}
	public SocketAddress get_remote_address() {
		return ctx.channel().remoteAddress();
	}
	
	public String get_remote_address_string(){
		return ((InetSocketAddress)get_remote_address()).getAddress().getHostAddress();
	}
	
	public int get_remote_address_port(){
		InetSocketAddress address = (InetSocketAddress)get_remote_address();
		return address.getPort();
	}

	public SocketAddress get_local_address() {
		return ctx.channel().localAddress();
	}

	public String get_request_uri() {
		if ((bit & INIT_PARAMS) == 0) {
			parse_parameters();
		}
		return requestUri;
	}

	public List<String> read_parameters(String key){
		Map<String, List<String>> parameters = get_parameters();
		return parameters == null ? new ArrayList<>(0) : get_parameters().get(key);
	}
	
	public String read_parameters_at_once(String key){
		List<String> list = read_parameters(key);
		return list == null || list.size() <= 0 ? StringUtil.EmptyString : list.get(0);
	}

	public Map<String, List<String>> get_parameters() {
		if ((bit & INIT_PARAMS) == 0) {
			parse_parameters();
		}
		return parameters;
	}
	
	private void parse_parameters(){
		bit |= INIT_PARAMS;
		String uri = uri();
		if (is_bad_user_agent || DispatcherLoader.isBlockUri(uri)) {// 부정 요청
			bad_request(uri);
			return;
		}
		QueryStringDecoder decoder = new QueryStringDecoder(uri);
		requestUri = decoder.path();
		Map<String, List<String>> params = decoder.parameters();
		if (method() == HttpMethod.POST) {
			parameters = new HashMap<>();
			parameters.putAll(params);	
		} else {
			parameters = params;
		}
	}
	
	public Collection<Cookie> get_cookies() {
		if ((bit & INIT_COOKIE) == 0) {
			bit |= INIT_COOKIE;
			String cookie_header = headers().get(HttpHeaderNames.COOKIE);
			if (StringUtil.isNullOrEmpty(cookie_header)) {
				cookies = Collections.emptySet();
			} else {
				cookies = ServerCookieDecoder.LAX.decode(cookie_header);
			}
		}
		return cookies;
	}
	
	public boolean is_multipart() {
		if ((bit & INIT_MULTI_PART) == 0) {
			multipart = false;
			bit |= INIT_MULTI_PART;
			if (method() == HttpMethod.POST) {
				multipart = HttpPostRequestDecoder.isMultipart(request);
			}
		}
		return multipart;
	}
	
	public void set_body(byte[] body){
		this.body = body;
	}
	
	public byte[] get_body() {
		return body;
	}
	
	public String read_post(String key) {
		return get_post_datas().get(key);
	}

	public Map<String, String> get_post_datas(){
		if ((bit & INIT_POST_DATA) == 0) {
			parse_post_datas();
		}
		return postDatas;
	}
	
	private void parse_post_datas(){
		bit |= INIT_POST_DATA;
		postDatas = new HashMap<String, String>();
		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(FACTORY, request);
		try {
			for (InterfaceHttpData data : decoder.getBodyHttpDatas()) {
				HttpDataType data_type = data.getHttpDataType();
				if (HttpDataType.Attribute == data_type) {
					try {
						Attribute attribute = (Attribute)data;
						postDatas.put(attribute.getName(), attribute.getValue());
					} catch (Exception e) {
						e.printStackTrace();
						WebClient.print(ctx, String.format("Throws BODY Attribute - %s", data.getHttpDataType().name()));
					}
				} else {
					WebClient.print(ctx, String.format("invalid BODY data - %s : %s", data_type.name(), data));
				}
			}
		} finally {
			try {
				decoder.destroy();
			} catch (Exception e) {}
		}
	}
	
	public boolean is_keep_alive(){
		return HttpUtil.isKeepAlive(request);
	}
	
	private void bad_request(String uri){
		requestUri		= uri;
		is_bad_request	= true;
	}
	
	public String connectionFlow() {
		return new StringBuilder(64)
				.append("[")
				.append(get_remote_address().toString())
				.append("] -> [")
				.append(get_local_address().toString())
				.append("]")
				.toString();
	}
	
	
	private String toString;
	
	@Override
	public String toString(){
		if (StringUtil.isNullOrEmpty(toString)) {
			HttpHeaders headers = request.headers();
			toString = new StringBuilder(1024)
					.append("[NettyHttpRequest]").append(connectionFlow()).append(StringUtil.LineString)
					.append(" -uri: ").append(uri()).append(StringUtil.LineString)
					.append(" -requestUri: ").append(get_request_uri()).append(StringUtil.LineString)
					.append(" -method: ").append(method()).append(StringUtil.LineString)
					.append(" -headers: ").append(StringUtil.LineString).append(StringUtil.join(StringUtil.LineString, "   ", headers, headers.size())).append(StringUtil.LineString)
					.append(" -cookies : ").append(StringUtil.LineString).append(StringUtil.join(StringUtil.LineString, get_cookies())).append(StringUtil.LineString)
					.append(" -parameters: ").append(StringUtil.LineString).append(StringUtil.join(StringUtil.LineString, "   ", get_parameters())).append(StringUtil.LineString)
					.append(" -body: ").append(StringUtil.LineString).append(StringUtil.join(StringUtil.LineString, "   ", get_post_datas())).append(StringUtil.LineString)
					.toString();
		}
		return toString;
	}
}

