package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.http.CookieSameSiteType;

public class WebserverConfigure {
	private static final Logger _log = Logger.getLogger(WebserverConfigure.class.getName());
	private static final String WEB_SERVER_CONFIG_FILE	= "./config/webserver.properties";
	
	public boolean WEB_SERVER_ENABLE;
	public int WEB_SERVER_PORT;
	public boolean WEB_SSL_ENABLE;
	public String WEB_SSL_CERTIFICATE_FILE_PATH;
	public String WEB_SSL_KEY_FILE_PATH;
	public boolean BROWSER_INGAME_ONLY;
	public String WEB_SERVER_NAME;
	public int WEB_GM_CODE;
	public int WEB_GM_AUTH_SECOND;
	public int MAX_CONTENT;
	public CookieSameSiteType COOKIE_SAME_SITE;
	public int LIMIT_URI_LENGTH;
	public boolean REALTIME_BLOCK;
	public boolean WEB_FILE_NO_CACHE;
	public boolean WEB_LOG_PRINT;
	public boolean SUGGEST_ENABLE;
	public int SEARCH_SUGGEST_TOTAL_COUNT;
	public int INGAME_LOGIN_AUTH_MAX_COUNT;
	
	public String CONNECTION_DOWNLOAD_URL_LOCAL;
	public String CONNECTION_DOWNLOAD_URL_REMOTE;
	
	public String INDEX_PAGE_MODAL_TITLE;
	public String INDEX_PAGE_MODAL_SRC;
	public String INDEX_PAGE_MODAL_COOKIE_NAME;
	public String INDEX_PAGE_MODAL_COOKIE_VALUE;
	public int INDEX_PAGE_MODAL_COOKIE_MAX_AGE;
	
	public int SUPPORT_PAY_REWARD_ITEM_ID;
	public int SUPPORT_PAY_REWARD_RATE;
	public int SUPPORT_PAY_REWARD_COUNT;
	public boolean SUPPORT_BANK_SEND_TYPE;
	public String SUPPORT_BANK;
	public String SUPPORT_BANK_NUMBER;
	public String SUPPORT_BANK_NAME;
	
	public boolean TELEGRAM_ACTIVE;
	public String TELEGRAM_ID;
	public boolean TELEGRAM_BOT_ACTIVE;
	public String TELEGRAM_TOKEN;
	public String TELEGRAM_CHAT_ID;
	
	public void load(){
		try {
			Properties 	settings 				= new Properties();
			InputStream is						= new FileInputStream(new File(WEB_SERVER_CONFIG_FILE));
			settings.load(is);
			is.close();
			
			WEB_SERVER_ENABLE					= Boolean.parseBoolean(settings.getProperty("WEB_SERVER_ENABLE", StringUtil.FalseString));
			WEB_SERVER_PORT						= Integer.parseInt(settings.getProperty("WEB_SERVER_PORT", "8080"));
			WEB_SSL_ENABLE						= Boolean.parseBoolean(settings.getProperty("WEB_SSL_ENABLE", StringUtil.FalseString));
			WEB_SSL_CERTIFICATE_FILE_PATH		= new String(settings.getProperty("WEB_SSL_CERTIFICATE_FILE_PATH", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			WEB_SSL_KEY_FILE_PATH				= new String(settings.getProperty("WEB_SSL_KEY_FILE_PATH", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			
			BROWSER_INGAME_ONLY					= Boolean.parseBoolean(settings.getProperty("BROWSER_INGAME_ONLY", StringUtil.FalseString));
			WEB_SERVER_NAME						= new String(settings.getProperty("WEB_SERVER_NAME", "LINOFFICE").getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			WEB_GM_CODE							= Integer.parseInt(settings.getProperty("WEB_GM_CODE", "1"));
			WEB_GM_AUTH_SECOND					= Integer.parseInt(settings.getProperty("WEB_GM_AUTH_SECOND", "3600"));
			MAX_CONTENT							= Integer.parseInt(settings.getProperty("MAX_CONTENT", "1048576"));
			String cookie_same_site				= new String(settings.getProperty("COOKIE_SAME_SITE", "STRICT").getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			COOKIE_SAME_SITE					= CookieSameSiteType.fromString(cookie_same_site);
			LIMIT_URI_LENGTH					= Integer.parseInt(settings.getProperty("LIMIT_URI_LENGTH", "1024"));
			REALTIME_BLOCK						= Boolean.parseBoolean(settings.getProperty("REALTIME_BLOCK", StringUtil.FalseString));
			WEB_FILE_NO_CACHE					= Boolean.parseBoolean(settings.getProperty("WEB_FILE_NO_CACHE", StringUtil.FalseString));
			WEB_LOG_PRINT						= Boolean.parseBoolean(settings.getProperty("WEB_LOG_PRINT", StringUtil.FalseString));
			
			SUGGEST_ENABLE						= Boolean.parseBoolean(settings.getProperty("SUGGEST_ENABLE", StringUtil.FalseString));
			SEARCH_SUGGEST_TOTAL_COUNT			= Integer.parseInt(settings.getProperty("SEARCH_SUGGEST_TOTAL_COUNT", "10"));
			INGAME_LOGIN_AUTH_MAX_COUNT			= Integer.parseInt(settings.getProperty("INGAME_LOGIN_AUTH_MAX_COUNT", "10"));
			
			CONNECTION_DOWNLOAD_URL_LOCAL		= new String(settings.getProperty("CONNECTION_DOWNLOAD_URL_LOCAL", "javascript:;").getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTION_DOWNLOAD_URL_REMOTE		= new String(settings.getProperty("CONNECTION_DOWNLOAD_URL_REMOTE", "javascript:;").getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			
			INDEX_PAGE_MODAL_TITLE				= new String(settings.getProperty("INDEX_PAGE_MODAL_TITLE", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			INDEX_PAGE_MODAL_SRC				= new String(settings.getProperty("INDEX_PAGE_MODAL_SRC", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			INDEX_PAGE_MODAL_COOKIE_NAME		= new String(settings.getProperty("INDEX_PAGE_MODAL_COOKIE_NAME", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			INDEX_PAGE_MODAL_COOKIE_VALUE		= new String(settings.getProperty("INDEX_PAGE_MODAL_COOKIE_VALUE", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			INDEX_PAGE_MODAL_COOKIE_MAX_AGE		= Integer.parseInt(settings.getProperty("INDEX_PAGE_MODAL_COOKIE_MAX_AGE", "0"));
			
			SUPPORT_PAY_REWARD_ITEM_ID			= Integer.parseInt(settings.getProperty("SUPPORT_PAY_REWARD_ITEM_ID", "0"));
			SUPPORT_PAY_REWARD_RATE				= Integer.parseInt(settings.getProperty("SUPPORT_PAY_REWARD_RATE", "10000"));
			SUPPORT_PAY_REWARD_COUNT			= Integer.parseInt(settings.getProperty("SUPPORT_PAY_REWARD_COUNT", "10"));
			SUPPORT_BANK_SEND_TYPE				= Boolean.parseBoolean(settings.getProperty("SUPPORT_BANK_SEND_TYPE", StringUtil.FalseString));
			SUPPORT_BANK						= new String(settings.getProperty("SUPPORT_BANK", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			SUPPORT_BANK_NUMBER					= new String(settings.getProperty("SUPPORT_BANK_NUMBER", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			SUPPORT_BANK_NAME					= new String(settings.getProperty("SUPPORT_BANK_NAME", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			
			TELEGRAM_ACTIVE						= Boolean.parseBoolean(settings.getProperty("TELEGRAM_ACTIVE", StringUtil.FalseString));
			TELEGRAM_ID							= new String(settings.getProperty("TELEGRAM_ID", "@LINOFFICE").getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			if (!StringUtil.isNullOrEmpty(TELEGRAM_ID) && !TELEGRAM_ID.startsWith("@")) {
				TELEGRAM_ID						= "@" + TELEGRAM_ID;
			}
			TELEGRAM_BOT_ACTIVE					= Boolean.parseBoolean(settings.getProperty("TELEGRAM_BOT_ACTIVE", StringUtil.FalseString));
			TELEGRAM_TOKEN						= settings.getProperty("TELEGRAM_TOKEN", StringUtil.EmptyString);
			TELEGRAM_CHAT_ID					= settings.getProperty("TELEGRAM_CHAT_ID", StringUtil.EmptyString);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + WEB_SERVER_CONFIG_FILE + " File.");
		}
	}
}

