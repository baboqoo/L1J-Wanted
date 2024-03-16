package l1j.server.GameSystem.huntingquest.user;

import javolution.util.FastMap;

/**
 * 유저에게 할당된 사냥터 도감 핸들러
 * @author LinOffice
 */
public class HuntingQuestUser {
	private final FastMap<Integer, HuntingQuestUserTemp> _temp = new FastMap<>();
	private final int _char_id;
	
	public HuntingQuestUser(int char_id){
		_char_id = char_id;
	}
	
	public int getCharId(){
		return _char_id;
	}
	
	public void add(int id, HuntingQuestUserTemp temp){
		_temp.put(id, temp);
	}
	
	public void remove(int id){
		if (!_temp.containsKey(id)) {
			return;
		}
		_temp.remove(id);
	}
	
	public void remove(HuntingQuestUserTemp temp){
		for (HuntingQuestUserTemp checktemp : _temp.values()) {
			if (checktemp == temp) {
				_temp.remove(checktemp.getId());
				break;
			}
		}
	}
	
	public HuntingQuestUserTemp getTemp(int id){
		return _temp.get(id);
	}
	
	public HuntingQuestUserTemp getTemp(int mapNumber, int locationDesc){
		HuntingQuestUserTemp sendTemp = null;
		for (HuntingQuestUserTemp temp : _temp.values()) {
			if (temp.getMapNumber() == mapNumber && temp.getLocationDesc() == locationDesc) {
				sendTemp = temp;
				break;
			}
		}
		return sendTemp;
	}
	
	public HuntingQuestUserTemp getTempInfo(int questId){
		HuntingQuestUserTemp sendTemp = null;
		for (HuntingQuestUserTemp temp : _temp.values()) {
			if (temp.getQuestId() == questId) {
				sendTemp = temp;
				break;
			}
		}
		return sendTemp;
	}
	
	public FastMap<Integer, HuntingQuestUserTemp> getInfo(){
		return _temp;
	}
	
	public void clear(){
		_temp.clear();
	}
	
	public void reset(){
		for (HuntingQuestUserTemp temp : _temp.values()) {
			temp.setKillCount(0);
			temp.setComplete(false);
		}
	}
	
}

