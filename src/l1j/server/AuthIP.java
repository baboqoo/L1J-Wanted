package l1j.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 허용 아이피 담당 클래스
 * @author LinOffice
 */
public class AuthIP {
	// API에 의한 조사(HttpResponse를 받기위해 요청을 태워야한다.)
	private static final String API_KEY				= Config.SERVER.IP_INFORMATION_API_KEY;
	private static final String INFORMATION_URL		= "http://apis.data.go.kr/B551505/whois/ip_address?serviceKey=%s&query=%s&answer=json";
	private static final String COUNTRY_CODE_URL	= "http://apis.data.go.kr/B551505/whois/ipas_country_code?serviceKey=%s&query=%s&answer=json";
	private static final int CONNECT_TIMEOUT_VALUE	= 5000;
	private static final String REQUEST_METHOD		= "GET";
	
	// data폴더의 파일에 의한 조사
	private static final String FILE_PATH			= "./data/korea_ip.csv";// 한국 아이피 대역(한국 인터넷 진흥원)
	private static final Set<String> AUTH_IP		= new TreeSet<String>();// 승인된 IP
	private static final String IP_SPLIT_STR		= "\\.";
	private static final String REG_STR				= "\\.\\d+\\.\\d+|";
	private static String KR_IP_REGEX;// 한국 IP 정규식
	
	private static AuthIP _instance;
	public static AuthIP getInstance(){
		if (_instance == null) {
			_instance = new AuthIP();
		}
		return _instance;
	}
	
	/**
	 * 승인 아이피 유효성 검사
	 * @param ip
	 * @return boolean
	 */
	public static boolean isWhiteIp(String ip){
		if (StringUtil.isNullOrEmpty(ip)) {
			return true;
		}
		if (ip.equals("0:0:0:0:0:0:0:1"))
		  ip = "127.0.0.1";		
		return AUTH_IP.contains(ip) || ip.matches(KR_IP_REGEX);
	}
	
	private AuthIP(){
		authLoad();
		fileLoad();
	}
	
	private void authLoad(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT ip FROM auth_ip");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				AUTH_IP.add(rs.getString("ip"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private void fileLoad() {
		Set<String> kr_ips			= new TreeSet<String>();
		File file					= null;
		FileReader filereader		= null;
		BufferedReader bufReader	= null;
		try {
			file		= new File(FILE_PATH);
			filereader	= new FileReader(file);
			bufReader	= new BufferedReader(filereader);
			StringBuilder sb = new StringBuilder();
			String line = StringUtil.EmptyString;
			while ((line = bufReader.readLine()) != null) {// 한 라인
				String IpFrom		= line.split(StringUtil.CommaString)[0];// 시작 IP
				String IpTo			= line.split(StringUtil.CommaString)[1];// 끝 IP

				// IP 첫번째 자리
				String IpFrom1Digit	= IpFrom.split(IP_SPLIT_STR)[0];

				// IP 두번째 자리
				String IpFrom2Digit	= IpFrom.split(IP_SPLIT_STR)[1];
				String IpTo2Digit	= IpTo.split(IP_SPLIT_STR)[1];

				// IP 두번째 자리 Range 처리 후 전체 IP 정규식 생성
				for (int i = Integer.parseInt(IpFrom2Digit); i <= Integer.parseInt(IpTo2Digit); i++) {
					kr_ips.add(sb.append(IpFrom1Digit).append(IP_SPLIT_STR).append(Integer.toString(i)).append(REG_STR).toString());
					sb.setLength(0);
				}
			}
			
			// local IP 추가
			kr_ips.add("127\\.0\\.\\d+\\.\\d+|");
			// 공유기 IP 추가
			kr_ips.add("192\\.168\\.\\d+\\.\\d+");
			
			// 한개의 정규식으로 만든다.
			for (Iterator<String> i = kr_ips.iterator(); i.hasNext();) {
				sb.append(i.next());
			}
			KR_IP_REGEX = sb.toString();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufReader != null) {
					bufReader.close();
				}
				if (filereader != null) {
					filereader.close();
				}
				if (kr_ips != null) {
					kr_ips.clear();
					kr_ips = null;
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean insertAuth(String ip){
		if (StringUtil.isNullOrEmpty(ip)) {
			return false;
		}
		if (ip.split(IP_SPLIT_STR).length != 4) {
			return false;
		}
		if (AUTH_IP.contains(ip)) {
			return false;
		}
		Connection con			= null;
		PreparedStatement pstm	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("INSERT INTO auth_ip SET ip=?");
			pstm.setString(1, ip);
			if (pstm.executeUpdate() > 0) {
				AUTH_IP.add(ip);
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean deleteAuth(String ip){
		if (StringUtil.isNullOrEmpty(ip)) {
			return false;
		}
		if (ip.split(IP_SPLIT_STR).length != 4) {
			return false;
		}
		if (!AUTH_IP.contains(ip)) {
			return false;
		}
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM auth_ip WHERE ip=?");
			pstm.setString(1, ip);
			if (pstm.executeUpdate() > 0) {
				AUTH_IP.remove(ip);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	/**
	 * HttpRequest를 요청하여 HttpResponse를 받는다.
	 * 성공코드 200
	 * 응답받은 데이터를 JSONObject로 파싱한다.
	 * @param spec 요청
	 * @return JSONObject
	 */
	JSONObject get_response_json(String spec) {
		URL url					= null;
		HttpURLConnection conn	= null;
		InputStreamReader ir	= null;
        BufferedReader br		= null;
        try {
            url = new URL(spec);

            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT_VALUE);
            conn.setReadTimeout(CONNECT_TIMEOUT_VALUE);
            conn.setRequestMethod(REQUEST_METHOD);

            if (conn.getResponseCode() == 200) {
            	ir	= new InputStreamReader(conn.getInputStream());
            	br	= new BufferedReader(ir);
                StringBuilder sb = new StringBuilder();
                String line = StringUtil.EmptyString;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return new JSONObject(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
            	if (ir != null) {
            		ir.close();
            		ir = null;
            	}
            	if (conn != null) {
            		conn.disconnect();
            		conn = null;
            	}
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
        return null;
	}
	
	/**
	 * 아이피의 상세 정보를 조사한다.
	 * @param address
	 * @return String
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String get_information(String address) {
		JSONObject responseJson = get_response_json(String.format(INFORMATION_URL, API_KEY, address));
		if (responseJson == null) {
			return null;
		}
		Map<String, Object> map		= (Map) responseJson.toMap().get("response");
		Map<String, Object> result	= (Map)map.get("result");
		Map<String, Object> whois	= (Map)map.get("whois");
		Object result_code			= result.get("result_code");
		if (result_code == null || !((String) result_code).equals("10000")) {
			return null;
		}
		Object query_type			= whois.get("queryType");
		Object registry				= whois.get("registry");
        Object country_code			= whois.get("countryCode");
        StringBuilder sb			= new StringBuilder();
        return sb.append("query_type : ").append(query_type)
        		.append(", registry : ").append(registry)
        		.append(", country_code : ").append(country_code)
        		.toString();
	}
	
	/**
	 * 아이피의 국가 코드를 조사한다.
	 * 실패시 null을 반환한다.
	 * KR : 한국
	 * none : localhost, 공유기 주소
	 * @param address
	 * @return String KR(한국), none(localhost, 공유기 주소)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String get_country_code(String address) {
		JSONObject responseJson = get_response_json(String.format(COUNTRY_CODE_URL, API_KEY, address));
		if (responseJson == null) {
			return null;
		}
		Map<String, Object> map		= (Map) responseJson.toMap().get("response");
		Map<String, Object> result	= (Map)map.get("result");
		Map<String, Object> whois	= (Map)map.get("whois");
		Object result_code			= result.get("result_code");
		if (result_code == null || !((String) result_code).equals("10000")) {
			return null;
		}
        Object country_code			= whois.get("countryCode");
        return (String) country_code;
    }
	
	public static void reload(){
		release();
		_instance = new AuthIP();
	}
	
	private static void release(){
		AUTH_IP.clear();
		_instance = null;
	}
}

