package l1j.server.web.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.gson.Gson;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.controller.LoginController;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.CNB;
import l1j.server.web.WebClient;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.AccountVO;
import l1j.server.web.dispatcher.response.account.CharacterVO;
import l1j.server.web.dispatcher.response.account.LoginFactory;

/**
 * HTTP 응답 클래스
 * @author LinOffice
 */
public abstract class HttpResponseModel implements HttpModel {
	protected static final String HTML_CONTENT_TYPE							= "text/html; charset=UTF-8";
	private static final FullHttpResponse REDIRECT_RESPONSE					= new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND, Unpooled.EMPTY_BUFFER);
	
	protected static final String USER_DATA_KEY								= "{USER_DATA}";
	protected static final String PAGE_CNB_TYPE_KEY							= "{PAGE_CNB_TYPE}";
	protected static final String PAGE_CNB_SUB_TYPE_KEY						= "{PAGE_CNB_SUB_TYPE}";
	
	protected static final KeyValuePair<String, String> SERVER_NAME_PAIR	= new KeyValuePair<String, String>("{SERVER_NAME}", Config.WEB.WEB_SERVER_NAME);
	protected static final KeyValuePair<String, String> USER_DATA_EMPTY		= new KeyValuePair<String, String>(USER_DATA_KEY, "undefined");
	protected static final KeyValuePair<String, String> SUGGEST_ENABLE_PAIR	= new KeyValuePair<String, String>("{SUGGEST_ENABLE}", String.valueOf(Config.WEB.SUGGEST_ENABLE));
	protected static final KeyValuePair<String, String> NOW_PAIR			= new KeyValuePair<String, String>("{NOW}", Long.toString(System.currentTimeMillis()));
	
	protected HttpRequestModel request;
	protected DispatcherModel dispatcher;
	
	protected AccountVO account;
	protected CharacterVO player;
	
	private GameClient ingame_client;
	private Account ingame_account;
	private L1PcInstance ingame_player;
	
	public HttpResponseModel() {}

	protected HttpResponseModel(HttpRequestModel request, DispatcherModel dispatcher) {
		this.request	= request;
		this.dispatcher	= dispatcher;
		if (dispatcher != null) {
			login();
		}
	}
	
	/**
	 * CNB 데이터
	 * @return CNB
	 */
	protected KeyValuePair<String, String> get_cnb_pair() {
		if (account != null && account.isGm()) {
			if (request.ingame) {
				return CNB.get_ingame_gm();
			}
			if (request.launcher) {
				return CNB.get_launcher_gm();
			}
			if (request.mobile) {
				return CNB.get_mobile_gm();
			}
			return CNB.get_default_gm();
		} else {
			if (request.ingame) {
				return CNB.get_ingame();
			}
			if (request.launcher) {
				return CNB.get_launcher();
			}
			if (request.mobile) {
				return CNB.get_mobile();
			}
			return CNB.get_default();
		}
	}
	
	/**
	 * 현재 시간(서버에서 관리)
	 * @return now time
	 */
	protected KeyValuePair<String, String> get_now_pair() {
		NOW_PAIR.value = Long.toString(System.currentTimeMillis());
		return NOW_PAIR;
	}
	
	/**
	 * 계정 정보
	 * @return user_data
	 */
	protected KeyValuePair<String, String> get_user_data_pair() {
		if (account == null) {
			return USER_DATA_EMPTY;
		}
		return new KeyValuePair<String, String>(USER_DATA_KEY, new Gson().toJson(account));
	}
	
	/**
	 * 로그인
	 */
	void login() {
		if (account == null) {
			if (request.ingame) {
				login_ingame();
			} else if (request.launcher) {
				account	= LoginFactory.get_account_to_cookies_from_launcher(request.get_cookies());
			} else {
				account	= LoginFactory.get_account_to_cookies_from_web(request.get_cookies());
			}
		}
		if (account != null) {
			player = account.getFirstChar();
		}
	}
	
	/**
	 * 인게임 로그인
	 */
	void login_ingame() {
		// 파라미터 또는 쿠키에 의한 로그인
		account		= LoginFactory.get_account_to_cookies_or_parameter_from_ingame(request);
		if (account != null) {
			setting_ingame_from_cookies_or_parameter();
		}
	}
	
	/**
	 * 쿠키 또는 파라미터에 의한 인게임 데이터 설정
	 */
	void setting_ingame_from_cookies_or_parameter() {
		ingame_client	= get_client_from_account_name(account.getName(), account.getAuthToken());
		if (ingame_client == null) {
			WebClient.print(request.get_ctx(), String.format("[HttpResponseModel] INGAME_CLIENT_NOT_FOUND_FROM_PARAMETER : ACCOUNT(%s)", account.getName()));
			return;
		}
		ingame_account	= ingame_client.getAccount();
		ingame_player	= ingame_client.getActiveChar();
		
		account.setNcoin(ingame_account.getNcoin());
		account.setNpoint(ingame_account.getNpoint());
		// 대표 케릭터 설정
		if (account.getFirstChar() == null || account.getFirstChar().getObjId() != ingame_player.getId()) {
			account.setFirstChar(ingame_player.getId());
		}
	}
	
	public AccountVO getAccount() {
		return account;
	}
	
	public CharacterVO getPlayer() {
		return player;
	}
	
	/**
	 * 인게임 클라이언트
	 * @return GameClient
	 */
	public GameClient getIngameClient() {
		if (account == null) {
			return null;
		}
		if (ingame_client == null) {
			ingame_client = get_client_from_account_name(account.getName(), account.getAuthToken());
		}
		if (ingame_client == null) {
			String message = account != null ? String.format("[HttpResponseModel] INGAME_CLIENT_NOT_FOUND : ACCOUNT_NAME(%s), CHARACTER_NAME(%s), ACCOUNT_IP(%s)", account.getName(), player.getName(), account.getIp()) 
					: "[HttpResponseModel] INGAME_CLIENT_NOT_FOUND";
			WebClient.print(request.get_ctx(), message);
		}
		return ingame_client;
	}
	
	/**
	 * 인게임 계정
	 * @return Account
	 */
	public Account getIngameAcoount() {
		if (account == null) {
			return null;
		}
		if (ingame_account == null) {
			ingame_account = get_account_from_account_name();
		}
		if (ingame_account == null) {
			String message = account != null ? String.format("[HttpResponseModel] INGAME_ACCOUNT_NOT_FOUND : ACCOUNT_NAME(%s), ACCOUNT_IP(%s)", account.getName(), account.getIp())
					: "[HttpResponseModel] INGAME_ACCOUNT_NOT_FOUND";
			WebClient.print(request.get_ctx(), message);
		}
		return ingame_account;
	}
	
	/**
	 * 인게임 플레이어
	 * @return L1PcInstance
	 */
	public L1PcInstance getIngamePlayer() {
		if (ingame_player == null) {
			GameClient client = getIngameClient();
			if (client != null) {
				ingame_player = client.getActiveChar();
			}
			if (ingame_player == null) {
				String message = player != null ? String.format("[HttpResponseModel] INGAME_PLAYER_NOT_FOUND : CHARACTER_NAME(%s), ACCOUNT_IP(%s)", player.getName(), account.getIp()) 
						: client != null ? String.format("[HttpResponseModel] INGAME_PLAYER_NOT_FOUND : CLIENT_IP(%s)", client.getIp())
						: "[HttpResponseModel] INGAME_PLAYER_NOT_FOUND";
				WebClient.print(request.get_ctx(), message);
				return null;
			}
		}
		
		// 대표 캐릭터 설정
		if (player == null || player.getObjId() != ingame_player.getId()) {
			account.setFirstChar(ingame_player.getId());
			player = account.getFirstChar();
		}
		return ingame_player;
	}
	
	/**
	 * 게임 클라이어언트 조사
	 * 접속중인 인게임 월드상의 플레이어로부터 조사한다.
	 * @param account_name
	 * @return GameClient
	 */
	public static GameClient get_client_from_account_name(String account_name, String authToken) {
		// 로그인 캐시 조사
		GameClient client = LoginController.getInstance().getClientByAccount(account_name);
		if (client != null) {
			return client;
		}
		// 승인 토큰 캐시 조사
		if (!StringUtil.isNullOrEmpty(authToken)) {
			client = LoginFactory.get_client_from_token(authToken);
			if (client != null) {
				return client;
			}
		}
		// 월드상 플레이어 캐시 조사
		for (L1PcInstance user : L1World.getInstance().getAllPlayers()) {
			if (user == null || user.getNetConnection() == null) {
				continue;
			}
			if (user.getNetConnection().getAccountName().equals(account_name)) {
				return user.getNetConnection();
			}
		}
		return null;
	}
	
	/**
	 * 계정 조사
	 * 접속중인 인게임 월드상의 플레이어로부터 조사한다.
	 * @param account_name
	 * @return Account
	 */
	Account get_account_from_account_name() {
		GameClient client = get_client_from_account_name(account.getName(), account.getAuthToken());
		if (client != null) {
			return client.getAccount();
		}
		for (L1PcInstance user : L1World.getInstance().getAllPlayers()) {
			if (user == null || user.getNetConnection() == null) {
				continue;
			}
			if (user.getNetConnection().getAccountName().equals(account.getName())) {
				return user.getNetConnection().getAccount();
			}
		}
		return null;
	}
	
	public DispatcherModel getDispatcher() {
		return dispatcher;
	}

	@Override
	public HttpResponse getResponse() throws Exception {
		return get_response();
	}

	public abstract HttpResponse get_response() throws Exception;
	
	public abstract HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception;

	public HttpResponse create_response(HttpResponseStatus status, String document) throws Exception {
		return create_response(status, document, false);
	}
	
	public HttpResponse create_response(HttpResponseStatus status, String document, boolean itemSearch) throws Exception {
		return create_response(status, document.getBytes(CharsetUtil.UTF_8));
	}

	/**
	 * 응답 생성
	 * @param status
	 * @param buff
	 * @return HttpResponse
	 */
	public HttpResponse create_response(HttpResponseStatus status, byte[] buff) {
		ByteBuf response_buff = Unpooled.wrappedBuffer(buff);
		FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), status, response_buff);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response_buff.readableBytes());
		return response;
	}
	
	/**
	 * html 읍답 생성
	 * @param status
	 * @param document
	 * @return HttpResponse
	 */
	public HttpResponse create_response_html(HttpResponseStatus status, String document) {
		ByteBuf response_buff = Unpooled.wrappedBuffer(document.getBytes(CharsetUtil.UTF_8));
		FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), status, response_buff);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response_buff.readableBytes());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, HTML_CONTENT_TYPE);
		return response;
	}
	
	/**
	 * 리다이렉트 응답 생성
	 * newUri로 이동시킨다.
	 * @param newUri
	 * @return HttpResponse
	 */
	public HttpResponse sendRedirect(String newUri) {
		REDIRECT_RESPONSE.headers().set(HttpHeaderNames.LOCATION, newUri);
		return REDIRECT_RESPONSE;
	}
	
	/**
	 * 빈 페이지 응답 생성
	 * @param status
	 * @return HttpResponse
	 */
	public HttpResponse create_empty(HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), status);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
		return response;
	}

	/**
	 * 404 페이지 응답 생성(페이지를 찾을 수 없습니다)
	 * @param message
	 * @return HttpResponse
	 * @throws Exception
	 */
	public HttpResponse notFound(String message) throws Exception {
		HttpResponse response = create_response(HttpResponseStatus.NOT_FOUND, message);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, HTML_CONTENT_TYPE);
		return response;
	}

	protected void append_log(String message) {
		WebClient.print(request.get_ctx(), message);
	}

	public String load_file_string(String path) {
		byte[] buff = load_file(path);
		if (buff == null) {
			return StringUtil.EmptyString;
		}
		return new String(buff, CharsetUtil.UTF_8);
	}

	public byte[] load_file(String path) {
		byte[] buff = null;
		InputStream is = null;
		try {
			File f = new File(path);
			if (!f.isFile()) {
				return null;
			}

			if (!f.exists()) {
				append_log(String.format("not found file. %s", path));
				return null;
			}
			buff = new byte[(int) f.length()];
			is = new FileInputStream(path);
			is.read(buff, 0, buff.length);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buff;
	}
}

