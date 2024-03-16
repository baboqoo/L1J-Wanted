package l1j.server.common;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.StringUtil;

/**
 * string-k.tbl 로더
 * @author LinOffice
 */
public class StringKLoader {
	private static StringKLoader _instance;
	public static StringKLoader getInstance() {
		if (_instance == null) {
			_instance = new StringKLoader();
		}
		return _instance;
	}
	
	private static String[] STRINGS;
	
	/**
	 * 순번에 해당하는 텍스트를 조사한다.
	 * @param id
	 * @return String
	 */
	public static String getString(int id) {
		try {
			return STRINGS[id + 1];// 첫번째 빈값이므로 +1
		} catch(ArrayIndexOutOfBoundsException e) {
			//System.out.println(String.format("string-k.tbl(파일 업데이트 필요) ArrayIndexOutOfBoundsException : %d", id));
			System.out.println(String.format("string-k.tbl(file update required) ArrayIndexOutOfBoundsException : %d", id));
			return StringUtil.EmptyString;
		}
	}
	
	private StringKLoader() {
		load();
	}
	
	private void load() {
		String str = FileUtil.readAllText("./data/string-k.tbl", CharsetUtil.MS_949);
		STRINGS = str.split(StringUtil.LineString);
	}
	
	public static void reload() {
		STRINGS = null;
		_instance.load();
	}
}

