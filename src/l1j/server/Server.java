package l1j.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import l1j.server.server.GameServer;
import l1j.server.server.LoginServer;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.WebDefaultPipeline;
import l1j.server.web.WebServer;

/**
 * 서버를 기동한다.
 */
public class Server {
	/** 메세지 로그용.  */
	private static Logger _log = Logger.getLogger(Server.class.getName());

	/** 로그 설정 파일의 폴더.  */
	private static final String LOG_PROP = "./config/log.properties";

	public static Calendar StartTime;
	private boolean initialize;
	private static boolean isInitialize;	

	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint*/
	public static void main(String[] args) {
		// TODO main method
		try {
			// 서버 가동
			Server server = new Server();
			isInitialize = server.isInitialize();
			if (!isInitialize) {
				System.out.println("Server operation failed.");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 서버 메인.
	 *
	 * @param args
	 *            커멘드 라인 인수
	 * @throws Exception
	 */
	public Server() throws Exception {
		File logFolder = new File("log");
		logFolder.mkdir();
		
		StartTime = Calendar.getInstance();
		StartTime.setTimeInMillis(System.currentTimeMillis());
		
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(LOG_PROP));
			LogManager.getLogManager().readConfiguration(is);
			is.close();
		} catch (IOException e) {
			_log.log(Level.SEVERE, "Failed to Load " + LOG_PROP + " File.", e);
			System.exit(0);
		}
		initMessage();
		configSetting();
		databaseSetting();
		serverSetting();
		finallyMessage();
		initialize = true;
	}
	
	private void configSetting(){
		try {
			Config.load();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);    
			System.exit(0); 
		}
	}
	
	private void databaseSetting() throws SQLException{
		// L1DatabaseFactory 초기설정
		L1DatabaseFactory.setDatabaseSettings(Config.SERVER.DB_DRIVER, Config.SERVER.DB_URL, Config.SERVER.DB_LOGIN, Config.SERVER.DB_PASSWORD);
		L1DatabaseFactory.getInstance();
	}
	
	private void serverSetting() throws Exception {
		GameServer.getInstance().initialize();
		LoginServer.getInstance().initialize();
		if (Config.WEB.WEB_SERVER_ENABLE) {
			WebServer.getInstance().initialize(new WebDefaultPipeline(Config.WEB.WEB_SERVER_PORT));
		}
	}
	
	private void initMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append("──────────────────────────────────────────────────────────────────\n");
		sb.append("                   L O A D I N G    S E R V E R\n");
		sb.append("──────────────────────────────────────────────────────────────────");
		System.out.println(sb.toString());
	}
	
	private void finallyMessage(){
		StringBuilder sb = new StringBuilder();
		
		/*
		sb.append("▶ 경 험 치 :     ").append(Config.RATE.RATE_XP).append(" 배\n");
		sb.append("▶ 성 향 치 :     ").append(Config.RATE.RATE_ALIGNMENT).append(" 배\n");
		sb.append("▶ 우 호 도 :     ").append(Config.RATE.RATE_KARMA).append(" 배\n");
		sb.append("▶ 아 이 템 :     ").append(Config.RATE.RATE_DROP_ITEMS).append(" 배 \n");
		sb.append("▶ 아 데 나 :     ").append(Config.RATE.RATE_DROP_ADENA).append(" 배\n");
		sb.append("▶ 채 팅 레 벨 :  ").append(Config.ALT.GLOBAL_CHAT_LEVEL).append(" 레벨\n");
		sb.append("▶ 최 대 인 원 :  ").append(Config.SERVER.MAX_ONLINE_USERS).append(" 명\n");
		sb.append("▶ 피 케 이 :     ").append(Config.ALT.ALT_NONPVP ? "가능" : "불가능").append("\n");
		sb.append("▶ 서 버 번 호 :  ").append(Config.VERSION.SERVER_NUMBER).append(" 번\n");
		sb.append("▶ 린 빈 버 전 :  ").append(Config.VERSION.CLIENT_VERSION_TO_STRING).append("\n");
		sb.append("▶ IP 승 인 :     ").append(Config.SERVER.IP_PROTECT ? "사용" : "미사용").append("\n");
		sb.append("▶ 로 긴 서 버 :  ").append(Config.SERVER.LOGIN_SERVER_ADDRESS).append(StringUtil.ColonString).append(Config.SERVER.LOGIN_SERVER_PORT).append("\n");
		sb.append("▶ 인 터 서 버 :  ");
		*/

		sb.append("> Exp Rate        : ").append(Config.RATE.RATE_XP).append(" %\n");
		sb.append("> Lawfull Rate    : ").append(Config.RATE.RATE_ALIGNMENT).append(" %p\n");
		sb.append("> Karma Rate      : ").append(Config.RATE.RATE_KARMA).append(" %p\n");
		sb.append("> Drop Rate       : ").append(Config.RATE.RATE_DROP_ITEMS).append(" %\n");
		sb.append("> Adena Rate      : ").append(Config.RATE.RATE_DROP_ADENA).append(" %\n");
		sb.append("> Chat Level      : ").append(Config.ALT.GLOBAL_CHAT_LEVEL).append("\n");
		sb.append("> Max Users       : ").append(Config.SERVER.MAX_ONLINE_USERS).append("\n");
		sb.append("> P K             : ").append(Config.ALT.ALT_NONPVP ? "Enabled" : "Disabled").append("\n");
		sb.append("> Server Version  : ").append(Config.VERSION.SERVER_NUMBER).append("\n");
		sb.append("> Linbin version  : ").append(Config.VERSION.CLIENT_VERSION_TO_STRING).append("\n");
		sb.append("> IP Protect      : ").append(Config.SERVER.IP_PROTECT ? "Used" : "Not used").append("\n");
		sb.append("> Server Address  : ").append(Config.SERVER.LOGIN_SERVER_ADDRESS).append(StringUtil.ColonString).append(Config.SERVER.LOGIN_SERVER_PORT).append("\n");
		sb.append("> Inter server    : ");	
		if (Config.INTER.INTER_SERVER_ACTIVE) {
			if (Config.INTER.INTER_SERVER_INNER_SETTING) {
				sb.append(Config.SERVER.LOGIN_SERVER_ADDRESS).append(StringUtil.ColonString).append(Config.SERVER.LOGIN_SERVER_PORT);
			} else {
				sb.append(Config.INTER.INTER_SERVER_OUTER_IP).append(StringUtil.ColonString).append(Config.INTER.INTER_SERVER_OUTER_PORT);
			}
			sb.append("\n");
		} else {
			sb.append("not used\n");
		}
		if (Config.WEB.WEB_SERVER_ENABLE) {
			sb.append("──────────────────────────────────────────────────────────────────\n");
			sb.append("> Web Server      : ").append(Config.SERVER.LOGIN_SERVER_ADDRESS).append(StringUtil.ColonString).append(Config.WEB.WEB_SERVER_PORT);
			if (Config.WEB.WEB_SSL_ENABLE && Config.WEB.WEB_SERVER_PORT == 443) {
				sb.append(" (SSL)");
			}
			sb.append("\n");
			sb.append("> Browser         : ").append(Config.WEB.BROWSER_INGAME_ONLY ? "INGAME\n" : "ALL\n");
		}
		if (Config.SERVER.CONNECT_DEVELOP_LOCK) {
			sb.append("──────────────────────────────────────────────────────────────────\n");
			sb.append("> Dev mode        : ON\n");
		}
		sb.append("──────────────────────────────────────────────────────────────────\n");
		sb.append("> Server Running  : ON\n");
		sb.append("──────────────────────────────────────────────────────────────────");
		System.out.println(sb.toString());
	}
	
	public boolean isInitialize(){
		return initialize;
	}
}

