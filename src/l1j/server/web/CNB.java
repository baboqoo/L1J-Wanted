package l1j.server.web;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import l1j.server.server.utils.KeyValuePair;

import com.google.gson.Gson;

/**
 * 앱센터 메뉴 설정
 * @author LINOFFICE
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CNB {
	private static Logger _log = Logger.getLogger(CNB.class.getName());

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "CNBList")
	private static class CNBList implements Iterable<CNB> {
		@XmlElement(name = "CNB")
		private List<CNB> _list;

		public Iterator<CNB> iterator() {
			return _list.iterator();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class SUB {
		@XmlAttribute(name = "Id")
		private int id;

		@XmlAttribute(name = "Desc")
		private String desc;

		@XmlAttribute(name = "Href")
		private String href;

		@XmlAttribute(name = "HideIngame")
		private boolean hide_ingame;
		
		@XmlAttribute(name = "HideMobile")
		private boolean hide_mobile;
		
		@XmlAttribute(name = "HideLauncher")
		private boolean hide_launcher;

		@XmlAttribute(name = "ShowGm")
		private boolean show_gm;
		
		@XmlAttribute(name = "Download")
		private boolean download;

		public int getId() {
			return id;
		}

		public String getDesc() {
			return desc;
		}
		
		public String getHref() {
			return href;
		}
		
		public boolean isHideIngame() {
			return hide_ingame;
		}
		
		public boolean isHideMobile() {
			return hide_mobile;
		}
		
		public boolean isHideLauncher() {
			return hide_launcher;
		}
		
		public boolean isShowGm() {
			return show_gm;
		}
		
		public boolean isDownload() {
			return download;
		}
	}

	private static final String PATH = "./appcenter/cnb.xml";

	private static final LinkedHashMap<Integer, CNB> DATA_MAP	= new LinkedHashMap<Integer, CNB>();
	private static KeyValuePair<String, String> CNB_DEFAULT_PAIR;
	private static KeyValuePair<String, String> CNB_DEFAULT_GM_PAIR;
	private static KeyValuePair<String, String> CNB_MOBILE_PAIR;
	private static KeyValuePair<String, String> CNB_MOBILE_GM_PAIR;
	private static KeyValuePair<String, String> CNB_INGAME_PAIR;
	private static KeyValuePair<String, String> CNB_INGAME_GM_PAIR;
	private static KeyValuePair<String, String> CNB_LAUNCHER_PAIR;
	private static KeyValuePair<String, String> CNB_LAUNCHER_GM_PAIR;
	
	public static KeyValuePair<String, String> get_default() {
		return CNB_DEFAULT_PAIR;
	}
	public static KeyValuePair<String, String> get_default_gm() {
		return CNB_DEFAULT_GM_PAIR;
	}
	public static KeyValuePair<String, String> get_mobile() {
		return CNB_MOBILE_PAIR;
	}
	public static KeyValuePair<String, String> get_mobile_gm() {
		return CNB_MOBILE_GM_PAIR;
	}
	public static KeyValuePair<String, String> get_ingame() {
		return CNB_INGAME_PAIR;
	}
	public static KeyValuePair<String, String> get_ingame_gm() {
		return CNB_INGAME_GM_PAIR;
	}
	public static KeyValuePair<String, String> get_launcher() {
		return CNB_LAUNCHER_PAIR;
	}
	public static KeyValuePair<String, String> get_launcher_gm() {
		return CNB_LAUNCHER_GM_PAIR;
	}
	
	@XmlAttribute(name = "Id")
	private int id;
	
	@XmlAttribute(name = "Desc")
	private String desc;
	
	@XmlAttribute(name = "Href")
	private String href;
	
	@XmlAttribute(name = "HideIngame")
	private boolean hide_ingame;
	
	@XmlAttribute(name = "HideMobile")
	private boolean hide_mobile;
	
	@XmlAttribute(name = "HideLauncher")
	private boolean hide_launcher;
	
	@XmlAttribute(name = "ShowGm")
	private boolean show_gm;
	
	@XmlAttribute(name = "Download")
	private boolean download;

	public int getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}
	
	public String getHref() {
		return href;
	}
	
	public boolean isHideIngame() {
		return hide_ingame;
	}
	
	public boolean isHideMobile() {
		return hide_mobile;
	}
	
	public boolean isHideLauncher() {
		return hide_launcher;
	}
	
	public boolean isShowGm() {
		return show_gm;
	}
	
	public boolean isDownload() {
		return download;
	}

	@XmlElement(name = "SUB")
	private CopyOnWriteArrayList<CNB.SUB> _subs;

	public List<CNB.SUB> getSubs() {
		return _subs;
	}
	
	static void init() {
		if (DATA_MAP.isEmpty())
			throw new NullPointerException("CNB DATA NULL");
		
		LinkedHashMap<Integer, CNB> DEFAULT		= new LinkedHashMap<Integer, CNB>();
		LinkedHashMap<Integer, CNB> DEFAULT_GM	= new LinkedHashMap<Integer, CNB>();
		LinkedHashMap<Integer, CNB> MOBILE		= new LinkedHashMap<Integer, CNB>();
		LinkedHashMap<Integer, CNB> MOBILE_GM	= new LinkedHashMap<Integer, CNB>();
		LinkedHashMap<Integer, CNB> INGAME		= new LinkedHashMap<Integer, CNB>();
		LinkedHashMap<Integer, CNB> INGAME_GM	= new LinkedHashMap<Integer, CNB>();
		
		LinkedHashMap<Integer, CNB> LAUNCHER	= new LinkedHashMap<Integer, CNB>();
		LinkedHashMap<Integer, CNB> LAUNCHER_GM	= new LinkedHashMap<Integer, CNB>();
		for (CNB cnb : DATA_MAP.values()) {
			boolean is_hide_ingame		= cnb.isHideIngame();
			boolean is_hide_mobile		= cnb.isHideMobile();
			boolean is_hide_launcher	= cnb.isHideLauncher();
			if (is_hide_ingame && is_hide_mobile && is_hide_launcher) {
				// 인게임과 모바일과 런처에 숨긴다.
				if (cnb.isShowGm()) {
					DEFAULT_GM.put(cnb.getId(), cnb);
				} else {
					DEFAULT.put(cnb.getId(), cnb);
					DEFAULT_GM.put(cnb.getId(), cnb);
				}
			} else if (is_hide_ingame && is_hide_mobile) {
				// 인게임과 모바일에 숨긴다.
				if (cnb.isShowGm()) {
					DEFAULT_GM.put(cnb.getId(), cnb);
					LAUNCHER_GM.put(cnb.getId(), cnb);
				} else {
					DEFAULT.put(cnb.getId(), cnb);
					DEFAULT_GM.put(cnb.getId(), cnb);
					LAUNCHER.put(cnb.getId(), cnb);
					LAUNCHER_GM.put(cnb.getId(), cnb);
				}
			} else if (is_hide_ingame && is_hide_launcher) {
				// 인게임과 런처에 숨긴다.
				if (cnb.isShowGm()) {
					DEFAULT_GM.put(cnb.getId(), cnb);
					MOBILE_GM.put(cnb.getId(), cnb);
				} else {
					DEFAULT.put(cnb.getId(), cnb);
					DEFAULT_GM.put(cnb.getId(), cnb);
					MOBILE.put(cnb.getId(), cnb);
					MOBILE_GM.put(cnb.getId(), cnb);
				}
			} else if (is_hide_mobile && is_hide_launcher) {
				// 모바일과 런처에 숨긴다.
				if (cnb.isShowGm()) {
					DEFAULT_GM.put(cnb.getId(), cnb);
					INGAME_GM.put(cnb.getId(), cnb);
				} else {
					DEFAULT.put(cnb.getId(), cnb);
					DEFAULT_GM.put(cnb.getId(), cnb);
					INGAME.put(cnb.getId(), cnb);
					INGAME_GM.put(cnb.getId(), cnb);
				}
			} else if (is_hide_ingame) {
				// 인게임에서 숨긴다
				if (cnb.isShowGm()) {
					// 관리자만 보인다
					DEFAULT_GM.put(cnb.getId(), cnb);
					MOBILE_GM.put(cnb.getId(), cnb);
					LAUNCHER_GM.put(cnb.getId(), cnb);
				} else {
					// 모두 보인다
					DEFAULT.put(cnb.getId(), cnb);
					DEFAULT_GM.put(cnb.getId(), cnb);
					MOBILE.put(cnb.getId(), cnb);
					MOBILE_GM.put(cnb.getId(), cnb);
					LAUNCHER.put(cnb.getId(), cnb);
					LAUNCHER_GM.put(cnb.getId(), cnb);
				}
			} else if (is_hide_mobile) {
				// 모바일에서 숨긴다
				if (cnb.isShowGm()) {
					// 관리자만 보인다
					DEFAULT_GM.put(cnb.getId(), cnb);
					INGAME_GM.put(cnb.getId(), cnb);
					LAUNCHER_GM.put(cnb.getId(), cnb);
				} else {
					// 모두 보인다
					DEFAULT.put(cnb.getId(), cnb);
					DEFAULT_GM.put(cnb.getId(), cnb);
					INGAME.put(cnb.getId(), cnb);
					INGAME_GM.put(cnb.getId(), cnb);
					LAUNCHER.put(cnb.getId(), cnb);
					LAUNCHER_GM.put(cnb.getId(), cnb);
				}
			} else if (is_hide_launcher) {
				// 런처에서 숨긴다
				if (cnb.isShowGm()) {
					// 관리자만 보인다
					DEFAULT_GM.put(cnb.getId(), cnb);
					INGAME_GM.put(cnb.getId(), cnb);
					MOBILE_GM.put(cnb.getId(), cnb);
				} else {
					// 모두 보인다
					DEFAULT.put(cnb.getId(), cnb);
					DEFAULT_GM.put(cnb.getId(), cnb);
					INGAME.put(cnb.getId(), cnb);
					INGAME_GM.put(cnb.getId(), cnb);
					MOBILE.put(cnb.getId(), cnb);
					MOBILE_GM.put(cnb.getId(), cnb);
				}
			} else {
				// 모든 브라우저 허용
				if (cnb.isShowGm()) {
					// 관리자만 보인다
					DEFAULT_GM.put(cnb.getId(), cnb);
					MOBILE_GM.put(cnb.getId(), cnb);
					INGAME_GM.put(cnb.getId(), cnb);
					LAUNCHER_GM.put(cnb.getId(), cnb);
				} else {
					// 모두 보인다
					DEFAULT.put(cnb.getId(), cnb);
					DEFAULT_GM.put(cnb.getId(), cnb);
					MOBILE.put(cnb.getId(), cnb);
					MOBILE_GM.put(cnb.getId(), cnb);
					INGAME.put(cnb.getId(), cnb);
					INGAME_GM.put(cnb.getId(), cnb);
					LAUNCHER.put(cnb.getId(), cnb);
					LAUNCHER_GM.put(cnb.getId(), cnb);
				}
			}
		}
		CNB_DEFAULT_PAIR		= new KeyValuePair<String, String>("{CNB}", new Gson().toJson(DEFAULT));
		CNB_DEFAULT_GM_PAIR		= new KeyValuePair<String, String>("{CNB}", new Gson().toJson(DEFAULT_GM));
		CNB_MOBILE_PAIR			= new KeyValuePair<String, String>("{CNB}", new Gson().toJson(MOBILE));
		CNB_MOBILE_GM_PAIR		= new KeyValuePair<String, String>("{CNB}", new Gson().toJson(MOBILE_GM));
		CNB_INGAME_PAIR			= new KeyValuePair<String, String>("{CNB}", new Gson().toJson(INGAME));
		CNB_INGAME_GM_PAIR		= new KeyValuePair<String, String>("{CNB}", new Gson().toJson(INGAME_GM));
		CNB_LAUNCHER_PAIR		= new KeyValuePair<String, String>("{CNB}", new Gson().toJson(LAUNCHER));
		CNB_LAUNCHER_GM_PAIR	= new KeyValuePair<String, String>("{CNB}", new Gson().toJson(LAUNCHER_GM));
	}

	public static void load() {
		try {
			JAXBContext context = JAXBContext.newInstance(CNB.CNBList.class);
			Unmarshaller um = context.createUnmarshaller();

			File file = new File(PATH);
			CNB.CNBList list = (CNB.CNBList) um.unmarshal(file);

			for (CNB cnb : list) {
				DATA_MAP.put(cnb.getId(), cnb);
			}
			init();
		} catch (Exception e) {
			_log.log(Level.SEVERE, PATH + "Failed to load.", e);
			System.exit(0);
		}
	}
	
	public static void dispose() {
		DATA_MAP.clear();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID: ").append(id);
		sb.append(", DESC: ").append(desc);
		sb.append(", HREF: ").append(href);
		sb.append(", HIDE_INGAME: ").append(hide_ingame);
		sb.append(", HIDE_MOBILE: ").append(hide_mobile);
		sb.append(", HIDE_LAUNCHER: ").append(hide_launcher);
		sb.append(", SHOW_GM: ").append(show_gm);
		sb.append(", DOWNLOAD: ").append(download);
		return sb.toString();
	}
	
}
