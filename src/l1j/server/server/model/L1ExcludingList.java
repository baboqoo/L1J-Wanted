package l1j.server.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class L1ExcludingList {
	private final int _charId;

	public final HashMap<Integer, String> _excludes = new HashMap<Integer, String>();

	private ArrayList<String> _nameList_Chat = new ArrayList<String>();
	private ArrayList<String> _nameList_Letter = new ArrayList<String>();

	public String[] getExcludeList(int type) {
		switch (type) {
		case 0:
			return _nameList_Chat.toArray(new String[_nameList_Chat.size()]);
		case 1:
			return _nameList_Letter.toArray(new String[_nameList_Letter.size()]);
		default:
			return null;
		}
	}

	public void add(int type, String name) {
		switch (type) {
		case 0:
			_nameList_Chat.add(name);
			break;
		case 1:
			_nameList_Letter.add(name);
			break;
		}
	}

	public void remove(int type, String name) {
		switch (type) {
		case 0:
			_nameList_Chat.remove(name);
			break;
		case 1:
			_nameList_Letter.remove(name);
			break;
		}
	}

	public boolean contains(int type, String name) {
		switch (type) {
		case 0:
			return _nameList_Chat.contains(name);
		case 1:
			return _nameList_Letter.contains(name);
		default:
			return false;
		}
	}

	public L1ExcludingList(int charId) {
		_charId = charId;
	}

	public int getCharId() {
		return _charId;
	}

	public boolean remove(int objId) {
		String result = _excludes.remove(objId);
		return (result != null ? true : false);
	}

	public boolean remove(String name) {
		int id = 0;
		for (Map.Entry<Integer, String> exclude : _excludes.entrySet()) {
			if (name.equalsIgnoreCase(exclude.getValue())) {
				id = exclude.getKey();
				break;
			}
		}
		if (id == 0) {
			return false;
		}
		_excludes.remove(id);
		return true;
	}

	public boolean contains(String name) {
		for (String each : _excludes.values()) {
			if (each.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public int size() {
		return _excludes.size();
	}
}

