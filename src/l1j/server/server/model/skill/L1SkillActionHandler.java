package l1j.server.server.model.skill;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Skills;

/**
 * 스킬 핸들러
 * @author LinOffice
 */
public abstract class L1SkillActionHandler extends L1SkillInfo {
	protected int _skillId = -1;
	protected L1Skills _skill;
	
	/**
	 * 스킬 능력치를 반영한다
	 * @param attacker
	 * @param cha
	 * @param time
	 * @param magic
	 * @return value
	 */
	public abstract int start(L1Character attacker, L1Character cha, int time, L1Magic magic);
	
	/**
	 * 스킬 능력치를 제거한다
	 * @param cha
	 */
	public abstract void stop(L1Character cha);
	
	/**
	 * 스킬 버프아이콘 출력
	 * @param pc
	 * @param time
	 */
	public abstract void icon(L1PcInstance pc, int time);
	
	/**
	 * 스킬 능력치를 UI에 반영한다
	 * 메세지를 출력한다
	 * @param cha
	 * @param flag
	 */
	public void wrap(L1PcInstance pc, boolean flag) {
		int msgID = flag ? _skill.getSysmsgIdHappen() : _skill.getSysmsgIdStop();
		if (msgID <= 0) {
			return;
		}
		pc.sendPackets(getMessage(msgID));
	}
	
	/**
	 * 메세지 패킷 재사용 처리
	 */
	private static final ConcurrentHashMap<Integer, S_ServerMessage> MESSAGES = new ConcurrentHashMap<>();
	protected S_ServerMessage getMessage(int id){
		S_ServerMessage message = MESSAGES.get(id);
		if (message == null) {
			message = new S_ServerMessage(id);
			MESSAGES.put(id, message);
		}
		return message;
	}
	
	/**
	 * 업무수행 Setter
	 * @param skillId
	 * @param skill
	 * @return L1SkillActionHandler
	 */
	protected L1SkillActionHandler setValue(int skillId, L1Skills skill){
		_skillId	= skillId;
		_skill		= skill;
		return this;
	}
	
	/**
	 * 업무 수행 인스턴스 생성
	 * @return L1SkillActionHandler
	 */
	public abstract L1SkillActionHandler copyInstance();
	
}

