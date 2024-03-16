package l1j.server.web.dispatcher.response.customer;

import java.net.HttpURLConnection;
import java.net.URL;

import l1j.server.Config;
import l1j.server.server.utils.StringUtil;

public class TelegramTransfer {
	private static final int TIMEOUT_VALUE	= 10000;
	private static final String METHOD		= "POST";
	private static final String SEND_URL	= "https://api.telegram.org/bot" + Config.WEB.TELEGRAM_TOKEN + "/sendmessage?chat_id=" + Config.WEB.TELEGRAM_CHAT_ID + "&text=%s";
	
	public static boolean excute(String msg) {
		if (StringUtil.isNullOrEmpty(msg) || StringUtil.isNullOrEmpty(Config.WEB.TELEGRAM_TOKEN) || StringUtil.isNullOrEmpty(Config.WEB.TELEGRAM_CHAT_ID)) {
			return false;
		}
		HttpURLConnection con	= null;
		try {
			con	= (HttpURLConnection) new URL(String.format(SEND_URL, StringUtil.encodeUrl(msg))).openConnection();
			con.setConnectTimeout(TIMEOUT_VALUE);
			con.setReadTimeout(TIMEOUT_VALUE);
			con.setDefaultUseCaches(false);
			con.setRequestMethod(METHOD);
			con.setDoOutput(true);
			if (con.getResponseCode() == 200) {// 성공 코드
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
				con = null;
			}
		}
		return false;
	}
}

