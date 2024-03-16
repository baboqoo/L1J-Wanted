package l1j.server.common;

import java.util.StringTokenizer;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.StringUtil;

/**
 * desc-k.tbl 파일 로더
 * @author LinOffice
 */
public class DescKLoader {
	private static DescKLoader _instance;
	public static DescKLoader getInstance() {
		if (_instance == null) {
			_instance = new DescKLoader();
		}
		return _instance;
	}
	
	private static String[] DESCS;
	
	/**
	 * $ 기준으로 파싱한다.
	 * @param desc
	 * @return String
	 */
	public static String getDesc(String desc) {
		if (StringUtil.isNullOrEmpty(desc)) {
			return null;
		}
		try {
			StringTokenizer st = new StringTokenizer(desc, StringUtil.DollarString);
			StringBuilder sb = new StringBuilder();
			while (st.hasMoreElements()) {
				String str = st.nextToken();
				if (StringUtil.isNullOrEmpty(str)) {
					continue;
				}
				if (str.matches("\\+.*|\\-.*")) {
					sb.append(str.trim());
				} else {
					try {
						sb.append(getDesc(Integer.parseInt(str.trim())));
					} catch(Exception e) {
						sb.append(str.trim());
					}
				}
				if (str.contains(StringUtil.EmptyOneString)) {// 공백 존재시 그대로 채운다
					sb.append(StringUtil.EmptyOneString);
				}
			}
			return sb.toString();
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 순번에 해당하는 텍스트를 조사한다.
	 * @param id
	 * @return String
	 */
	public static String getDesc(int id) {
		try {
			return DESCS[id + 1];
		} catch(ArrayIndexOutOfBoundsException e) {
			//System.out.println(String.format("desc-k.tbl(파일 업데이트 필요) ArrayIndexOutOfBoundsException : %d", id));
			System.out.println(String.format("desc-k.tbl(file update required) ArrayIndexOutOfBoundsException : %d", id));
			return null;
		}
	}
	
	private DescKLoader() {
		load();
	}
	
	private void load() {
		String str = FileUtil.readAllText("./data/desc-k.tbl", CharsetUtil.MS_949);
		DESCS = str.split(StringUtil.LineString);
	}
	
	public static void reload() {
		DESCS = null;
		_instance.load();
	}
}

