package l1j.server.server.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.StringUtil;

public class L1Buddy {
	private final LinkedHashMap<String, String> _buddys = new LinkedHashMap<String, String>();
	
	private final int _charId;

	public L1Buddy(int charId) {
		_charId = charId;
	}

	public int getCharId() {
		return _charId;
	}

	public boolean add(String name, String memo) {
		if (_buddys.containsKey(name)) {
			return false;
		}
		_buddys.put(name, memo);
		return true;
	}

	public boolean remove(String name) {
		String id = null;
		for (Map.Entry<String, String> buddy : _buddys.entrySet()) {
			if (name.equalsIgnoreCase(buddy.getKey())) {
				id = buddy.getKey();
				break;
			}
		}
		if (id == null) {
			return false;
		}
		_buddys.remove(id);
		return true;
	}
	
	public boolean updateMemo(String name, String memo) {
		if (_buddys.containsKey(name)) {
			_buddys.put(name, memo);
			return true;
		}
		return false;
	}
	
	public String getMemo(String name) {
		return _buddys.get(name);
	}

	public String getOnlineBuddyListString() {
		String result = new String(StringUtil.EmptyString);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (_buddys.containsKey(pc.getName())) {
				result += pc.getName() + StringUtil.EmptyOneString;
			}
		}
		return result;
	}

	public String getBuddyListString() {
		String result = new String(StringUtil.EmptyString);
		for (String name : _buddys.keySet()) {
			result += name + StringUtil.EmptyOneString;
		}
		return result;
	}
	
	public HashMap<String, String> getBuddy() {
		return _buddys;
	}

	public boolean containsName(String name) {
		return _buddys.containsKey(name);
	}

	public int size() {
		return _buddys.size();
	}
}

