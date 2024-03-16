package l1j.server.web.dispatcher.response.editor;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

public class Emblem {
	private static Emblem _instance;
	public static Emblem getInstance(){
		if (_instance == null) {
			_instance = new Emblem();
		}
		return _instance;
	}
	private static final Set<String> LIST = new TreeSet<String>();
	public static Set<String> getList(){
		return LIST;
	}
	
	private Emblem() {
		load();
	}
	
	private void load() {
		File file			= new File("./appcenter/download/clanEmble");
		if (file.isDirectory()) {
	        File[] Files = file.listFiles();
	        for (int i=0; i<Files.length; i++) {
	        	LIST.add(Files[i].getName());
	        }
		}
	}
	
	public static void release() {
		LIST.clear();
		_instance = null;
	}
}

