package l1j.server.server.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		BufferedReader reader = null;
		HttpURLConnection con = null;
		try {
			URL urlObject = new URL(url);
			con = (HttpURLConnection)urlObject.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
			con.connect();
			if (con.getResponseCode() != 200) {
				System.out.println(String.format("ResponseCode %d %s", con.getResponseCode(), con.getResponseMessage()));
				return null;
			}
			
			InputStream is = con.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, CharsetUtil.UTF_8));
			String jsonText = readAll(reader);
			if (StringUtil.isNullOrEmpty(jsonText)) {
				return null;
			}
			return new JSONObject(jsonText);
		} catch(JSONException e) {
			//System.out.println("★JSON에서 오류가 발생됨★");
			System.out.println("--- An error occurred in JSON ---");
		} catch(UnknownHostException e) {
			//System.out.println("★호스트를 찾을 수 없습니다.★");
			System.out.println("--- Host not found ---");
		} catch(IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {	
				try {
					reader.close();
				} catch(Exception e) {}
			}
			if (con != null) {
				try {
					con.disconnect();
				} catch(Exception e) {}
			}
		}
		return null;
	}
}

