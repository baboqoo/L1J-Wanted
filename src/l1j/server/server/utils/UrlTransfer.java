package l1j.server.server.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 외부 통신 클래스
 * @author LinOffice
 */
public class UrlTransfer {
	private static final String METHOD		= "POST";// 전송 방식
	private static final String CHAR_SET	= CharsetUtil.UTF_8_STR;// 인코딩
	
	/**
	 * 파라미터 생성
	 * @param paramMap
	 * @return byte array
	 */
	public static byte[] createParameterBytes(Map<String,Object> paramMap){
		try {
	        StringBuilder sb = new StringBuilder();
	        for (Map.Entry<String,Object> param : paramMap.entrySet()) {
	            if (sb.length() != 0) {
	            	sb.append(StringUtil.AndString);// 구분
	            }
	            sb.append(URLEncoder.encode(param.getKey(), CHAR_SET));// key
	            sb.append(StringUtil.EqualsString);
	            sb.append(URLEncoder.encode(String.valueOf(param.getValue()), CHAR_SET));// value
	        }
	        return sb.toString().getBytes(CHAR_SET);
		} catch(IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	/**
	 * 지정된 URL로 파리미터 전송
	 * @param sendUrl
	 * @param sendBytes
	 */
	public static void trans(String sendUrl, byte[] sendBytes){
		try {
			URL url					= new URL(sendUrl);// URL 설정
			HttpURLConnection con	= (HttpURLConnection) url.openConnection();// 접속 
			con.setDoOutput(true);// 서버로 쓰기 모드 지정 
			con.setDoInput(true);// 서버에서 읽기 모드 지정 
			con.setInstanceFollowRedirects(false);
			con.setRequestMethod(METHOD);// 전송 방식
			con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			con.setRequestProperty("charset", CHAR_SET);
			con.setRequestProperty("Content-Length", String.valueOf(sendBytes.length));
			con.setUseCaches(false);
			con.getOutputStream().write(sendBytes);// 데이터 세팅
			con.getInputStream();// 전송
			con.disconnect();// 종료
			url = null;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

