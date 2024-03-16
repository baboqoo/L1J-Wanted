package l1j.server.server.model;

import java.util.HashMap;
import java.util.Map;

import l1j.server.server.templates.L1RevengeTemp;

public class L1Revenge {
	private final Map<String, L1RevengeTemp> _revenge = new HashMap<String, L1RevengeTemp>();
	
	private final int _charId;
	
	public L1Revenge(int char_id){
		_charId = char_id;
	}
	
	public int getCharId(){
		return _charId;
	}
	
	public void add(String targetName, L1RevengeTemp temp){
		_revenge.put(targetName, temp);
	}
	
	public void remove(String targetName){
		if (!_revenge.containsKey(targetName)) {
			return;
		}
		_revenge.remove(targetName);
	}
	
	public void remove(int targetId){
		for (L1RevengeTemp temp : _revenge.values()) {
			if (temp.getUserUid() == targetId) {
				_revenge.remove(temp.getUserName());
				break;
			}
		}
	}
	
	public L1RevengeTemp getRevenge(String targetName){
		if (!_revenge.containsKey(targetName)) {
			return null;
		}
		return _revenge.get(targetName);
	}
	
	public Map<String, L1RevengeTemp> getRevenges(){
		return _revenge;
	}
}

