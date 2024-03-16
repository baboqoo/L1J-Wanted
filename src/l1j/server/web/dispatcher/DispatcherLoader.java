package l1j.server.web.dispatcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.http.HttpModel;
import l1j.server.web.http.HttpResponseModel;

import org.apache.mina.util.ConcurrentHashSet;

/**
 * Appcenter Dispatcher Loader
 * 웹 요청의 응답 데이터 로더
 * @author LinOffice
 */
public class DispatcherLoader {
	private static Logger _log = Logger.getLogger(DispatcherLoader.class.getName());
	
	private static DispatcherLoader _instance;
	public static DispatcherLoader getInstance(){
		if (_instance == null) {
			_instance = new DispatcherLoader();
		}
		return _instance;
	}
	
	private static final ConcurrentHashMap<String, DispatcherModel> DATA	= new ConcurrentHashMap<>();
	private static final ConcurrentHashSet<String> PASS_URI					= new ConcurrentHashSet<>();
	private static final ConcurrentHashSet<String> AUTH_EXTENSOIN			= new ConcurrentHashSet<>();
	private static String BLOCK_URI_REGEX;
	
	public static DispatcherModel getDispatcher(String uri){
		return DATA.get(uri);
	}
	
	public static boolean isBlockUri(String uri){
		return uri.matches(BLOCK_URI_REGEX);
	}
	
	public static boolean isPassUri(String uri){
		return PASS_URI.contains(uri);
	}
	
	public static boolean isAuthExtension(String extension){
		return AUTH_EXTENSOIN.contains(extension);
	}
	
	private DispatcherLoader(){
		load();
	}
	
	private void load(){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		HttpModel res				= null;
		DispatcherModel dis			= null;
		try {
			con						= L1DatabaseFactory.getInstance().getConnection();
			pstm					= con.prepareStatement("SELECT * FROM app_page_info");
			rs						= pstm.executeQuery();
			while (rs.next()) {
				String uri			= rs.getString("uri");
				String path			= rs.getString("path");
				String className	= rs.getString("className");
				boolean Json		= Boolean.parseBoolean(rs.getString("Json"));
				Class<?> cls		= getClass(className, Json);
				if (cls == null) {
					System.out.println(String.format("[DispatcherLoader] Class Not Foud uri[%s] path[%s] classNmae[%s]", uri, path, className));
					continue;
				}
				res					= (HttpModel)cls.newInstance();
				String html			= null;
				// 페이지 존재
				if (!StringUtil.isNullOrEmpty(path) && res instanceof HttpResponseModel) {
					html			= ((HttpResponseModel)res).load_file_string(path);// html문서 로드
					if (StringUtil.isNullOrEmpty(html)) {
						System.out.println(String.format("[DispatcherLoader] page Not Foud uri[%s] path[%s] classNmae[%s]", uri, path, className));
						continue;
					}
				}
				int cnbType				= rs.getInt("cnbType");
				int cnbSubType			= rs.getInt("cnbSubType");
				boolean needIngame		= Boolean.parseBoolean(rs.getString("needIngame"));
				boolean needLauncher	= Boolean.parseBoolean(rs.getString("needLauncher"));
				boolean needLogin		= Boolean.parseBoolean(rs.getString("needLogin"));
				boolean needGm			= Boolean.parseBoolean(rs.getString("needGm"));
				boolean fileUpload		= Boolean.parseBoolean(rs.getString("fileUpload"));
				dis = new DispatcherModel(uri, path, html, res, String.valueOf(cnbType), String.valueOf(cnbSubType), needIngame, needLauncher, needLogin, needGm, Json, fileUpload);
				DATA.put(uri, dis);
			}
			SQLUtil.close(rs, pstm);
			
			pstm					= con.prepareStatement("SELECT uri FROM app_uri_block");
			rs						= pstm.executeQuery();
			StringBuilder sb		= new StringBuilder();
			while (rs.next()) {
				if (sb.length() > 0) {
					sb.append("|");
				}
				sb.append(".*").append(rs.getString("uri")).append(".*");
			}
			BLOCK_URI_REGEX = sb.toString();
			SQLUtil.close(rs, pstm);
			
			pstm					= con.prepareStatement("SELECT uri FROM app_uri_pass");
			rs						= pstm.executeQuery();
			while (rs.next()) {
				PASS_URI.add(rs.getString("uri"));
			}
			SQLUtil.close(rs, pstm);
			
			pstm					= con.prepareStatement("SELECT extension FROM app_auth_extension");
			rs						= pstm.executeQuery();
			while (rs.next()) {
				AUTH_EXTENSOIN.add(rs.getString("extension").toLowerCase());
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private Class<?> getClass(String response, boolean Json) throws ClassNotFoundException {
		if (Json) {
			return Class.forName(String.format("l1j.server.web.dispatcher.response.define.%s", response));
		}
		return Class.forName(String.format("l1j.server.web.dispatcher.response.%s", response));
	}
	
	public static void reload(){
		release();
		_instance = new DispatcherLoader();
	}
	
	public static void release(){
		DATA.clear();
		PASS_URI.clear();
		AUTH_EXTENSOIN.clear();
		_instance = null;
	}
}

