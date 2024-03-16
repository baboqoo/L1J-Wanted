package l1j.server.web.dispatcher.response.account;

import io.netty.handler.codec.http.cookie.Cookie;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.GameClient;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;
import l1j.server.web.http.connector.HttpLoginSession;

public class LoginFactory {
	public static final String COOKIE_AUTH_TOKEN	= "authToken";
	private static final String COOKIE_ACCOUNT		= "account";
	private static final String COOKIE_PASSWORD		= "password";
	private static final String COOKIE_REGEX		= "account|password|authToken";
	
	private static final ConcurrentHashMap<String, AccountVO> INGAME_FACTORY		= new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, AccountVO> INGAME_TOKEN_FACTORY	= new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, GameClient> CLIENT_FACTORY		= new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, AccountVO> WEB_FACTORY			= new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, AccountVO> LAUNCHER_FACTORY		= new ConcurrentHashMap<>();
	
	public static void put_web(AccountVO account) {
		WEB_FACTORY.put(account.getAuthToken(), account);
	}
	
	public static void remove_web(AccountVO account) {
		WEB_FACTORY.remove(account.getAuthToken());
	}
	
	public static AccountVO get_web(String authToken) {
		return WEB_FACTORY.get(authToken);
	}
	
	public static void put_launcher(AccountVO account) {
		LAUNCHER_FACTORY.put(account.getAuthToken(), account);
	}
	
	public static void remove_launcher(AccountVO account) {
		LAUNCHER_FACTORY.remove(account.getAuthToken());
	}
	
	public static AccountVO get_launcher(String authToken) {
		return LAUNCHER_FACTORY.get(authToken);
	}
	
	public static ConcurrentHashMap<String, AccountVO> get_web_accounts() {
		return WEB_FACTORY;
	}
	
	public static void put_ingame(AccountVO account, GameClient client){
		account.setIngame(true);
		INGAME_FACTORY.put(account.getName(), account);
		INGAME_TOKEN_FACTORY.put(account.getAuthToken(), account);
		CLIENT_FACTORY.put(account.getAuthToken(), client);
	}
	
	public static void remove_ingame(HttpLoginSession session) {
		CLIENT_FACTORY.remove(session.getAuthToken());
		INGAME_TOKEN_FACTORY.remove(session.getAuthToken());
		AccountVO account = INGAME_FACTORY.remove(session.getAccount());
		account.setIngame(false);
	}
	
	public static AccountVO get_ingame_from_name(String account_name) {
		return INGAME_FACTORY.get(account_name);
	}
	
	public static AccountVO get_ingame_from_token(String authToken) {
		return INGAME_TOKEN_FACTORY.get(authToken);
	}
	
	public static GameClient get_client_from_token(String authToken) {
		return CLIENT_FACTORY.get(authToken);
	}
	
	public static ConcurrentHashMap<String, AccountVO> get_ingame_accounts() {
		return INGAME_FACTORY;
	}
	
	/**
	 * 승인 토큰에 의한 로그인(웹)
	 * @param collection
	 * @return AccountVO
	 */
	public static AccountVO get_account_to_cookies_from_web(Collection<Cookie> collection) {
		if (collection == null) {
			return null;
		}
		AccountVO result = null;
		String name, value;
		for (Cookie cookie : collection) {
			name	= cookie.name();
			value	= cookie.value();
			if (StringUtil.isNullOrEmpty(value)) {
				continue;
			}
			if (name.equalsIgnoreCase(COOKIE_AUTH_TOKEN)) {
				result = get_web(value);
				if (result != null) {
					cookie.setValue("1");
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 승인 토큰에 의한 로그인(런처)
	 * @param collection
	 * @return AccountVO
	 */
	public static AccountVO get_account_to_cookies_from_launcher(Collection<Cookie> collection) {
		if (collection == null) {
			return null;
		}
		AccountVO result = null;
		String name, value;
		for (Cookie cookie : collection) {
			name	= cookie.name();
			value	= cookie.value();
			if (StringUtil.isNullOrEmpty(value)) {
				continue;
			}
			if (name.equalsIgnoreCase(COOKIE_AUTH_TOKEN)) {
				result = get_launcher(value);
				if (result != null) {
					cookie.setValue("1");
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 쿠키 또는 파라미터의 계정 정보에 의한 로그인(인게임)
	 * @param request
	 * @return AccountVO
	 */
	public static AccountVO get_account_to_cookies_or_parameter_from_ingame(HttpRequestModel request) {
		AccountVO result = get_account_to_parameter_from_ingame(request);
		if (result != null) {
			return result;
		}
		return get_account_to_cookies_from_ingame(request.get_cookies());
	}
	
	/**
	 * 파라미터에 의한 계정 정보 로그인(인게임)
	 * @param request
	 * @return AccountVO
	 */
	static AccountVO get_account_to_parameter_from_ingame(HttpRequestModel request) {
		String account	= request.read_parameters_at_once("account");
		if (StringUtil.isNullOrEmpty(account)) {
			return null;
		}
		String password	= request.read_parameters_at_once("password");
		if (StringUtil.isNullOrEmpty(password)) {
			return null;
		}
		AccountVO result = get_ingame_from_name(account);
		if (result == null) {
			return create_account_from_ingame(account, password);
		}
		if (!result.getPassword().equals(password)) {
			return null;
		}
		return result;
	}
	
	/**
	 * 쿠키에 의한 계정 정보 로그인(인게임)
	 * @param collection
	 * @return AccountVO
	 */
	static AccountVO get_account_to_cookies_from_ingame(Collection<Cookie> collection) {
		if (collection == null) {
			return null;
		}
		String account = null, password = null, authToken = null;
		String name, value;
		for (Cookie cookie : collection) {
			name	= cookie.name();
			value	= cookie.value();
			if (StringUtil.isNullOrEmpty(value)) {
				continue;
			}
			if (!name.matches(COOKIE_REGEX)) {
				continue;
			}
			switch (name) {
			case COOKIE_ACCOUNT:
				account = value;
				break;
			case COOKIE_PASSWORD:
				password = value;
				break;
			case COOKIE_AUTH_TOKEN:
				authToken = value;
				break;
			}
		}
		AccountVO result = null;
		if (!StringUtil.isNullOrEmpty(authToken)) {
			result = get_ingame_from_token(authToken);
			if (result != null) {
				return result;
			}
		}
		if (StringUtil.isNullOrEmpty(account) || StringUtil.isNullOrEmpty(password)) {
			return null;
		}
		result = get_ingame_from_name(account);
		if (result == null) {
			return create_account_from_ingame(account, password);
		}
		if (!result.getPassword().equals(password)) {
			return null;
		}
		return result;
	}
	
	/**
	 * 계정과 비밀번호 데이터로 계정 생성(인게임)
	 * @param account
	 * @param password
	 * @return AccountVO
	 */
	static AccountVO create_account_from_ingame(String account, String password) {
		GameClient client = HttpResponseModel.get_client_from_account_name(account, null);
		if (client == null) {
			return null;
		}
		AccountVO result = AccountDAO.getInstance().getAccount(client.getAccount(), client.getActiveChar());
		if (result == null) {
			return null;
		}
		if (!result.getPassword().equals(password)) {
			return null;
		}
		put_ingame(result, client);
		return result;
	}
	
	public static void clear(){
		WEB_FACTORY.clear();
		LAUNCHER_FACTORY.clear();
		INGAME_FACTORY.clear();
		INGAME_TOKEN_FACTORY.clear();
		CLIENT_FACTORY.clear();
	}
}

