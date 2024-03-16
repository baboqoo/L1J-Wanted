package l1j.server.server.model;

import l1j.server.IndunSystem.clandungeon.ClanDungeonCreator;
import l1j.server.IndunSystem.clandungeon.ClanDungeonHandler;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.controller.action.BossSpawn;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.CommonUtil;

public class L1NpcDeleteTimer implements Runnable {
	public L1NpcDeleteTimer(L1NpcInstance npc, int timeMillis) {
		_npc		= npc;
		_timeMillis	= timeMillis;
	}

	@Override
	public void run() {
		if (_npc == null) {
			return;
		}
		int npcId = _npc.getNpcId();
		if ((npcId == 7800012 || npcId == 7800013 || npcId == 7800014) && !_npc.isDead() && !_npc._destroyed) {// 화염의 상처
			fireBomb();
		} else if (npcId == 5000103 && !_npc.isDead() && !_npc._destroyed) {// 할파스의 권속
			halpasDebuff(true);
		} else if (npcId == 5000104 && !_npc.isDead() && !_npc._destroyed) {// 할파스의 권속
			halpasDebuff(false);
		} else if (npcId == 5136 && !_npc.isDead() && !_npc._destroyed && BossSpawn._erzabeRun) {// 에르자베
			BossSpawn._erzabeRun = false;
		} else if (npcId == 5135 && !_npc.isDead() && !_npc._destroyed && BossSpawn._sandwarmRun) {// 샌드웜
			BossSpawn._sandwarmRun = false;
		} else if ((npcId == 45752 || npcId == 45753) && _npc.getMapId() == 15404 && !_npc.isDead() && !_npc._destroyed) {// 발록 삭제될때
			barlogDoor();
		} else if (npcId == 800157 && _npc.getMapId() == 12862 && !_npc.isDead() && !_npc._destroyed) {// 리퍼 삭제될때
			reaperDoor();
		} else if (npcId == 46025 && _npc.getMapId() == 54 && BossSpawn._taros) {
			tarosMapGfx();
		} else if (npcId == 20900 || npcId == 20901 || npcId == 20902) {// 혈맹 던전
			clanDungenClose();
		} else if (npcId >= 800250 && npcId <= 800260) {// 지배의 탑 각성 보스
			((L1MonsterInstance)_npc).dominationWakeEnd();
		}
		
		// 폭발 몬스터
		if (npcId == 7800012 || npcId == 7800013 || npcId == 7800014 || npcId == 5000103 || npcId == 5000104) {
			_npc.receiveDamage(_npc, _npc.getMaxHp());
			return;
		}
		_npc.deleteMe();
	}

	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, _timeMillis);
	}
	
	void fireBomb(){
		_npc.broadcastPacket(new S_DoActionGFX(_npc.getId(), 8), true);
		_npc.broadcastPacket(new S_Effect(_npc.getId(), 4162), true);
    	for (L1Object obj : L1World.getInstance().getVisibleObjects(_npc, 2)) {
    		if (obj == _npc) {
    			continue;
    		}
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				if (pc.isGhost() || pc.isDead() || pc.isAbsol() || pc.isBind()) {
					continue;
				}
				pc.receiveDamage(_npc, 100);
			} else if (obj instanceof L1NpcInstance) {
				L1NpcInstance crystal = (L1NpcInstance) obj;
				if (crystal.getNpcId() == 780000 && !crystal.isDead() && !crystal._destroyed) {
					crystal.receiveDamage(_npc, 100);
				}
			}
		}
	}
	
	void halpasDebuff(boolean type){
		int skillId = type ? L1SkillId.HALPAS_JUJOO_MP_INCREASE : L1SkillId.HALPAS_JUJOO_MP_DAMAGE;
		_npc.broadcastPacket(new S_DoActionGFX(_npc.getId(), 19), true);
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_npc, 2)) {
			if (obj == _npc) {
    			continue;
    		}
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				if (pc == null || pc.isGhost() || pc.isDead() || pc.isAbsol() || pc.isBind()) {
					continue;
				}
				pc.receiveDamage(_npc, 500);
				if (CommonUtil.random(100) < 80) {
					L1BuffUtil.skillAction(pc, skillId);
					pc.sendPackets(new S_SpellBuffNoti(pc, skillId, true, 30), true);
					pc.send_effect(15896);
					pc.sendPackets(L1ServerMessage.sm5011);
				}
			}
		}
	}
	
	void barlogDoor(){
		for (L1DoorInstance door : L1World.getInstance().getAllDoor()) {
			if ((door.getDoorId() == 225 || door.getDoorId() == 226 || door.getDoorId() == 227) && door.getOpenStatus() == ActionCodes.ACTION_Close) {
				door.open();
			}
		}
	}
	
	void reaperDoor(){
		for (L1DoorInstance door : L1World.getInstance().getAllDoor()) {
			if (door.getDoorId() == 231 && door.getOpenStatus() == ActionCodes.ACTION_Open) {
			    door.close();
			}
	    }
	}
	
	void tarosMapGfx(){
		BossSpawn._taros = false;
		for (L1PcInstance pc : L1World.getInstance().getMapPlayer(54)) {
			pc.sendPackets(S_SceneNoti.MAP54_DISABLE_END);// 리셋
			pc.sendPackets(S_SceneNoti.KIKAM_FULL_END);// 불켜기
		}
	}
	
	void clanDungenClose(){
		int portalMap = ((L1FieldObjectInstance) _npc).getMoveMapId();
		if (L1World.getInstance().getMapPlayer(portalMap).isEmpty()) {// 인원 없을경우
			ClanDungeonHandler handler = ClanDungeonCreator.getInstance().getRaid(portalMap);
			if (handler == null) {
				return;
			}
			if (handler.isRun()) {
				handler.setRun(false);
			}
		}
	}

	private final L1NpcInstance _npc;
	private final int _timeMillis;
}

