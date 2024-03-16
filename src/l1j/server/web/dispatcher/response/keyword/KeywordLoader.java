package l1j.server.web.dispatcher.response.keyword;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.FormatterUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 핫이슈
 * @author LinOffice
 */
public class KeywordLoader {
	private static class newInstance {
		public static final KeywordLoader INSTANCE = new KeywordLoader();
	}
	public static KeywordLoader getInstance() {
		return newInstance.INSTANCE;
	}
	
	private static final String JS_FILE_PATH = "./appcenter/js/popularkeyword.js";
	
	private static final ArrayList<KeywordVO> OLD_LIST = new ArrayList<KeywordVO>();
	private static final ArrayList<KeywordVO> CUR_LIST = new ArrayList<KeywordVO>();
	
	private static final String[][] KEYWORD = new String[10][3];// 송출 키워드
	
	private static final String[] STATUS = {"up", "down", "same"};
	
	public static String[][] getKeyword(){
		return KEYWORD;
	}
	
	public static void putKeyword(String keyword) {
		if (StringUtil.isNullOrEmpty(keyword)) {
			return;
		}
		if (keyword.length() > 15) {
			return;
		}
		KeywordVO vo = null;
		for (KeywordVO key : CUR_LIST) {
			if (key.getKeyword().equalsIgnoreCase(keyword)) {
				vo = key;
				break;
			}
		}
		if (vo == null) {
			vo = new KeywordVO(keyword, 0, 0, 0);
			CUR_LIST.add(vo);
		}
		vo.addView();
	}
	
	public static void reload() {
		if (CUR_LIST.isEmpty()) {
			return;
		}
		init();
		OLD_LIST.clear();
		OLD_LIST.addAll(CUR_LIST);
		
		for (KeywordVO cur : CUR_LIST) {
			cur.setOldRank(cur.getCurRank());
			int rank = OLD_LIST.size() + 1;
			for (KeywordVO old : OLD_LIST) {
				if (cur.getView() >= old.getView()) {
					rank--;
				}
			}
			cur.setCurRank(rank);
		}
		
		for (int i=0; i<KEYWORD.length; i++) {
			int sameCnt = 0;
			for (KeywordVO cur : CUR_LIST) {
				if (i + sameCnt + 1 > KEYWORD.length) {
					break;
				}
				if (cur.getCurRank() == i + 1) {
					if (!KEYWORD[i + sameCnt][0].equals(StringUtil.MinusString)) {
						sameCnt++;
					}
					if (i + sameCnt + 1 > KEYWORD.length) {
						break;
					}
					KEYWORD[i + sameCnt][0] = cur.getKeyword();
					if (cur.getOldRank() != 0 && cur.getCurRank() != cur.getOldRank()) {
						KEYWORD[i + sameCnt][1] = cur.getCurRank() > cur.getOldRank() ? STATUS[1] : STATUS[0];
						KEYWORD[i + sameCnt][2] = String.valueOf(Math.abs(cur.getCurRank() - cur.getOldRank()));
					} else {
						KEYWORD[i + sameCnt][1] = STATUS[2];
						KEYWORD[i + sameCnt][2] = StringUtil.ZeroString;
					}
				}
			}
		}
		write_file();
	}
	
	private KeywordLoader() {
		init();
		write_file();
	}
	
	private static void init() {
		// 기본 초기화
		for (String[] key : KEYWORD) {
			key[0] = StringUtil.MinusString;
			key[1] = STATUS[2];
			key[2] = StringUtil.ZeroString;
		}
	}
	
	/**
	 * popularkeyword.js파일을 새로 만든다.
	 */
	static void create_file() {
		try {
			int nLast = JS_FILE_PATH.lastIndexOf(StringUtil.DirectorySeparatorChar);
			String str_dir = JS_FILE_PATH.substring(0, nLast);
			String str_file = JS_FILE_PATH.substring(nLast + 1, JS_FILE_PATH.length());
			
			File dir_folder = new File(str_dir);
			if (!dir_folder.exists()) {
				dir_folder.mkdir();
			}
			File f = new File(dir_folder, str_file);
			f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * popularkeyword.js파일을 새로 쓴다.
	 */
	static void write_file() {
		String contents = get_contents();
		File f = null;
		BufferedWriter buffWrite = null;
		OutputStreamWriter outputStream = null;
		FileOutputStream fileStream = null;
		try {
			f = new File(JS_FILE_PATH);
			if (!f.exists()) {
				create_file();
			}
			fileStream		= new FileOutputStream(f);
			outputStream	= new OutputStreamWriter(fileStream, CharsetUtil.UTF_8);
			buffWrite		= new BufferedWriter(outputStream);
			buffWrite.write(contents, 0, contents.length());
			buffWrite.flush();
			buffWrite.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileStream != null) {
					fileStream.close();
					fileStream = null;
				}
				if (outputStream != null) {
					outputStream.close();
					outputStream = null;
				}
				if (buffWrite != null) {
					buffWrite.close();
					buffWrite = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * popularkeyword.js파일에 넣을 내용
	 * @return String
	 */
	static String get_contents() {
		StringBuilder sb = new StringBuilder();
		sb.append("const popularKeyword = {\r\n	last_modify: '").append(FormatterUtil.get_formatter_time());
		sb.append("',\r\n	list: [\r\n");
		for (int i=0; i<KEYWORD.length; i++) {
			if (i > 0) {
				sb.append("\r\n,");
			}
			sb.append("		['").append(KEYWORD[i][0]).append("'").append(StringUtil.CommaString).append("'").append(KEYWORD[i][1]).append("'").append(StringUtil.CommaString).append(KEYWORD[i][2]).append("]");
		}
		sb.append("\r\n	]\r\n};");
		return sb.toString();
	}
	
	public static void release() {
		OLD_LIST.clear();
		CUR_LIST.clear();
	}
}

