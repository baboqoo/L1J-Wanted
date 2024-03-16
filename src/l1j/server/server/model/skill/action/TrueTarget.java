package l1j.server.server.model.skill.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.spell.S_TrueTarget;

public class TrueTarget extends L1SkillActionHandler {
	
	// 시전자가 시전한 트루타겟을 임시로 담을 공간.
	private static HashMap<Integer, L1Object> _truetarget_list = new HashMap<Integer, L1Object>();

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1PcInstance == false) {
			return 0;
		}
		L1PcInstance crown = (L1PcInstance) attacker;
		
		int pledgeId	= crown.getClanid();
		int partyId		= crown.getPartyID();
		if (pledgeId != 0) {
			cha.true_target_clanid = pledgeId;
		}
		if (partyId != 0) {
			cha.true_target_partyid = partyId;
		}
		cha.true_target_level = crown.getLevel();
		
		// 이전에 시전한 트루타겟 종료
		synchronized (_truetarget_list) {
			L1Object temp = _truetarget_list.remove(attacker.getId());
			if (temp != null && temp instanceof L1Character) {
				L1Character temp2 = (L1Character) temp;
				temp2.getSkill().removeSkillEffect(TRUE_TARGET);
			}
		}
		
		// 트루타겟 활성화.
		cha.getSkill().setSkillEffect(TRUE_TARGET, 16000);
		synchronized(_truetarget_list){
			_truetarget_list.put(attacker.getId(), (L1Object)cha);
		}
		
		// 트루타겟 이미지 출력(시전시 화면에 존재하는 유저에게만 출력한다)
		S_TrueTarget trueTarget = new S_TrueTarget(cha.getId(), true);
		crown.sendPackets(trueTarget);
		if (cha instanceof L1PcInstance) {
			L1PcInstance targetPc = (L1PcInstance) cha;
			if ((pledgeId != 0 && targetPc.getClanid() == pledgeId) 
					|| (partyId != 0 && targetPc.getPartyID() == partyId)) {
				targetPc.sendPackets(trueTarget);
			}
		}
		for (L1PcInstance each : L1World.getInstance().getRecognizePlayer(cha)) {
			if ((pledgeId != 0 && pledgeId == each.getClanid()) 
					|| (partyId != 0 && partyId == each.getPartyID())) {
				each.sendPackets(trueTarget);
			}
		}
		trueTarget.clear();
		trueTarget = null;
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		int pledgeId	= cha.true_target_clanid;
		int partyId		= cha.true_target_partyid;
		S_TrueTarget trueTarget = new S_TrueTarget(cha.getId(), false);
		if (cha instanceof L1PcInstance) {
			L1PcInstance targetPc = (L1PcInstance) cha;
			if ((pledgeId != 0 && targetPc.getClanid() == pledgeId) 
					|| (partyId != 0 && targetPc.getPartyID() == partyId)) {
				targetPc.sendPackets(trueTarget);
			}
		}
		for (L1PcInstance each : L1World.getInstance().getRecognizePlayer(cha)) {
			if ((pledgeId != 0 && pledgeId == each.getClanid()) 
					|| (partyId != 0 && partyId == each.getPartyID())) {
				each.sendPackets(trueTarget);
			}
		}
		trueTarget.clear();
		trueTarget = null;
		cha.true_target_clanid = cha.true_target_partyid = cha.true_target_level = 0;
		synchronized (_truetarget_list) {
			List<Integer> remove_list = new ArrayList<Integer>();
			for (Integer id : _truetarget_list.keySet()) {
				L1Object o = _truetarget_list.get(id);
				if (o.getId() != cha.getId()) {
					continue;
				}
				remove_list.add(id);
			}
			for (Integer id : remove_list) {
				_truetarget_list.remove(id);
			}
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new TrueTarget().setValue(_skillId, _skill);
	}

}

