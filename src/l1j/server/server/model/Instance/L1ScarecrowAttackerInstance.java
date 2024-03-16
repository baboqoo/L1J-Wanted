package l1j.server.server.model.Instance;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.templates.L1Npc;

/**
 * 허수아비 공격 로봇 인스턴스
 * @author LinOffice
 */
public class L1ScarecrowAttackerInstance extends L1MonsterInstance {
	private static final long serialVersionUID	= 1L;
	
	/**
	 * 생성자
	 * 부모 상속 L1MonsterInstance
	 * @param template
	 */
	public L1ScarecrowAttackerInstance(L1Npc template) {
		super(template);
	}
	
	@Override
	public void searchTarget() {
		// TODO 몬스터가 인식할 타겟을 설정한다.
		L1ScarecrowInstance targetCrow	= null;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
			if (obj instanceof L1ScarecrowInstance) {
				L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
				if (crow.getHiddenStatus() != 0 || crow.isDead()) {
					continue;
				}
				int crowId = crow.getNpcTemplate().getNpcId();
				/** 허수아비 **/
				if (crowId == 9323 || crowId == 44997 || crowId == 44998 || crowId == 44999 || crowId == 45001 || crowId == 45002 || crowId == 45003 || crowId == 45004
						|| crowId == 202064 || crowId == 202098 || crowId == 7200023) {
					targetCrow = crow;
					break;
				}
			}
		}
		
		if (targetCrow != null) {
			_hateList.add(targetCrow, 0);
			_target = targetCrow;
		}
	}
	
}

