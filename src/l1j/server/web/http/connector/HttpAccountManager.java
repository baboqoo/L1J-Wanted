package l1j.server.web.http.connector;

import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 로그인 세션 관리 핸들러
 * @author LinOffice
 */
public class HttpAccountManager {
	private static final ConcurrentHashMap<String, HttpLoginSession> SESSION = new ConcurrentHashMap<>();
	
	public static HttpLoginSession get(final String authToken){
		return SESSION.get(authToken);
	}
	
	public static void put(final HttpLoginSession session){
		if (session == null) {
			return;
		}
		final String authToken = session.getAuthToken();
		if (SESSION.containsKey(authToken)) {
			return;
		}
		SESSION.put(authToken, session);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				remove(authToken);
			}
		}, 600 * 1000);// 10분 뒤 세션 종료
	}
	
	public static void remove(final String authToken){
		SESSION.remove(authToken);
	}
	
	public static void clear(){
		SESSION.clear();
	}
	
	public static String checkHmac(final String hdd_id, final String mac_address, final String path) {
		try {
			String message = String.format("%s.%s@%s",
					hdd_id,
					mac_address,
					path
					);
			
			Mac sha256 = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(Config.LAUNCHER.CONNECTOR_SESSION_KEY.getBytes(CharsetUtil.UTF_8), "HmacSHA256");
			sha256.init(secret_key);
			return Base64.getEncoder().encodeToString(sha256.doFinal(message.getBytes(CharsetUtil.UTF_8)));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return StringUtil.EmptyString;
	}
	
	public static boolean isValidAccount(final String account) {
		if (account.length() < 5 || account.length() > 50) {
			return false;
		}
		char[] chars = account.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isLetterOrDigit(chars[i]) && (Character.compare(chars[i], '.') != 0) && (Character.compare(chars[i], '@') != 0)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isValidPassword(final String password) {
		if (password.length() < 6 || password.length() > 20) {
			return false;
		}
		boolean hasLetter	= false;
		boolean hasDigit	= false;

		char[] chars = password.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])) {
				hasLetter = true;
			} else if (Character.isDigit(chars[i])) {
				hasDigit = true;
			} else {
				return false;
			}
		}
		if (!hasLetter || !hasDigit) {
			return false;
		}
		return true;
	}
}

