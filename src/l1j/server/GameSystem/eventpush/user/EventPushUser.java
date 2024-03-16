package l1j.server.GameSystem.eventpush.user;

import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.GameSystem.eventpush.bean.EventPushObject;

public class EventPushUser {
	private final FastMap<Integer, EventPushObject> _temp = new FastMap<Integer, EventPushObject>(Config.PUSH.LIMIT_SIZE);
	
	public EventPushUser() {
	}
	
	public void add(int number, EventPushObject temp){
		if (_temp.size() >= Config.PUSH.LIMIT_SIZE) {
			_temp.remove(_temp.keySet().iterator().next());
		}
		_temp.put(number, temp);
	}
	
	public void remove(int number){
		if (!_temp.containsKey(number)) {
			return;
		}
		_temp.remove(number);
	}
	
	public void remove(EventPushObject temp){
		for (EventPushObject checktemp : _temp.values()) {
			if (checktemp == temp) {
				_temp.remove(checktemp.getEventPushId());
				break;
			}
		}
	}
	
	public EventPushObject getTemp(int number){
		return !_temp.containsKey(number) ? null : _temp.get(number);
	}
	
	public FastMap<Integer, EventPushObject> getInfo(){
		if (!_temp.isEmpty()) {
			long currentTime = System.currentTimeMillis();
			java.util.Iterator<Integer> itr = _temp.keySet().iterator();
			while (itr.hasNext()) {
				int key = itr.next();
				EventPushObject obj = _temp.get(key);
				if (obj != null && obj.getEnableDate().getTime() <= currentTime) {
					_temp.remove(key);
				}
			}
		}
		return _temp;
	}
}

