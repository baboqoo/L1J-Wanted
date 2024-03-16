package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class VersionConfigure {
	private static final Logger _log = Logger.getLogger(VersionConfigure.class.getName());
	private static final String VERSION_CONFIG_FILE	= "./config/version.properties";
	
	public byte[] FIRST_KEY;
	public long FIRST_KEY_SEED;
	public long CLIENT_VERSION;
	public String CLIENT_VERSION_TO_STRING;
	public int MSDL_VERSION;
	public String MSDL_VERSION_TO_STRING;
	public long LIBCOCOS_VERSION;
	public String LIBCOCOS_VERSION_TO_STRING;
	public long AUTHSERVER_VERSION;
	public long GLOBAL_CACHE_VERSION;
	public long TAMSERVER_VERSION;
	public long ARCASERVER_VERSION;
	public long HIBREED_INTERSERVER_VERSION;
	public long ARENACOSERVER_VERSION;
	public long CLIENT_SETTING_SWITCH;
	public int SERVER_NUMBER;
	public int SERVER_TYPE;
	public long BROKER_SERVER_VERSION;
	public long AI_AGENT_DLL_VERSION;
	
	public void load(){
		try {
			Properties 	settings 				= new Properties();
			InputStream is						= new FileInputStream(new File(VERSION_CONFIG_FILE));
			settings.load(is);
			is.close();
			
			String keyText						= new String(settings.getProperty("FIRST_KEY", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			String[] array = keyText.replaceAll(StringUtil.LineString, StringUtil.EmptyString).split(StringUtil.CommaString);
			FIRST_KEY = new byte[array.length + 2];// 패킷 길이 2개 추가
			for (int i=0; i<FIRST_KEY.length; i++) {
				if (i == 0) {
					FIRST_KEY[i] = (byte) ((array.length + 1) & 0xff);// 패킷 길이
				} else if (i == 1) {
					FIRST_KEY[i] = (byte) 0x00;
				} else {
					FIRST_KEY[i] = (byte) (Integer.parseInt(array[i - 2].trim(), 16) & 0xff);
				}
			}
			long seed = FIRST_KEY[3] & 0xff;
			seed |= ((FIRST_KEY[4] << 8) & 0xff00);
			seed |= ((FIRST_KEY[5] << 16) & 0xff0000);
			seed |= ((FIRST_KEY[6] << 24) & 0xff000000);
			FIRST_KEY_SEED						= seed;
			
			SERVER_NUMBER						= Integer.parseInt(settings.getProperty("SERVER_NUMBER", "2"));
			CLIENT_VERSION						= Long.parseLong(settings.getProperty("CLIENT_VERSION", "2008311701"));
			LIBCOCOS_VERSION					= Long.parseLong(settings.getProperty("LIBCOCOS_VERSION", "2303081701"));
			MSDL_VERSION						= Integer.parseInt(settings.getProperty("MSDL_VERSION", "1006"));
			
			String version = Long.toString(CLIENT_VERSION);
			StringBuilder sb = new StringBuilder();
			sb.append(version.substring(0, 2));// 년도
			sb.append(StringUtil.CommaString).append(version.substring(2, 4));// 월
			sb.append(StringUtil.CommaString).append(version.substring(4, 6));// 일
			sb.append(StringUtil.CommaString).append(version.substring(6));// 버전
			CLIENT_VERSION_TO_STRING			= sb.toString();

			String cocosversion = Long.toString(LIBCOCOS_VERSION);
			sb.setLength(0);
			sb.append(cocosversion.substring(0, 2));// 년도
			sb.append(StringUtil.CommaString).append(cocosversion.substring(2, 4));// 월
			sb.append(StringUtil.CommaString).append(cocosversion.substring(4, 6));// 일
			sb.append(StringUtil.CommaString).append(cocosversion.substring(6));// 버전
			LIBCOCOS_VERSION_TO_STRING			= sb.toString();

			String libversion = Integer.toString(MSDL_VERSION);
			sb.setLength(0);
			sb.append(libversion.substring(0, 1));// 년도
			sb.append(".").append(libversion.substring(1, 2));// 월
			sb.append(".").append(libversion.substring(2, 3));// 일
			sb.append(".").append(libversion.substring(3));// 버전
			MSDL_VERSION_TO_STRING			= sb.toString();

			AUTHSERVER_VERSION					= Long.parseLong(settings.getProperty("AUTHSERVER_VERSION", "2015090301"));
			GLOBAL_CACHE_VERSION				= Long.parseLong(settings.getProperty("GLOBAL_CACHE_VERSION", "2008101001"));
			TAMSERVER_VERSION					= Long.parseLong(settings.getProperty("TAMSERVER_VERSION", "2008101004"));
			ARCASERVER_VERSION					= Long.parseLong(settings.getProperty("ARCASERVER_VERSION", "2008101003"));
			HIBREED_INTERSERVER_VERSION			= Long.parseLong(settings.getProperty("HIBREED_INTERSERVER_VERSION", "2008101005"));
			ARENACOSERVER_VERSION 				= Long.parseLong(settings.getProperty("ARENACOSERVER_VERSION", "2008101002"));
			CLIENT_SETTING_SWITCH 				= Long.parseLong(settings.getProperty("CLIENT_SETTING_SWITCH", "889191819"));
			SERVER_TYPE							= Integer.parseInt(settings.getProperty("SERVER_TYPE", "0"));
			BROKER_SERVER_VERSION				= Long.parseLong(settings.getProperty("BROKER_SERVER_VERSION", StringUtil.ZeroString));
			AI_AGENT_DLL_VERSION				= Long.parseLong(settings.getProperty("AI_AGENT_DLL_VERSION", StringUtil.ZeroString));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + VERSION_CONFIG_FILE + " File.");
		}
	}

}

