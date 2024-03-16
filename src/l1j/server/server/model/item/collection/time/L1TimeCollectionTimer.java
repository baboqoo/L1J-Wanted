package l1j.server.server.model.item.collection.time;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionNotiType;

/**
 * 실렉티스 전시회 버프 타이머
 * @author LinOffice
 */
public class L1TimeCollectionTimer implements Runnable {
	private final L1PcInstance owner;
	private final L1TimeCollectionUser user;
	private boolean active;
	
	/**
	 * 기본 생성자
	 * @param owner
	 * @param ablity
	 */
	public L1TimeCollectionTimer(L1PcInstance owner, L1TimeCollectionUser user) {
		this.owner		= owner;
		this.user		= user;
		this.active		= true;
	}
	
	@Override
	public void run() {
		try {
			if (!active || owner == null || owner.getNetConnection() == null || owner.getTimeCollection() == null || user == null) {
				return;
			}
			// TODO 버프를 제거한다.
			owner.getTimeCollection().reset(user.getObj(), L1TimeCollectionNotiType.SOON_END);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 타이머를 중지한다.
	 */
	public void cancel(){
		this.active = false;
	}
}

