package l1j.server.server.model.npc.action.id;

import l1j.server.server.ActionCodes;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.controller.action.War;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1CataInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.action.S_DoActionGFX;

public class CastleWarBombAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new CastleWarBombAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private CastleWarBombAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return castleTrapAction(pc, s, npcId, obj);
	}
	
	private String castleTrapAction(L1PcInstance pc, String s, int npcId, L1Object obj){
		if (s.equalsIgnoreCase("0-5") // 외성문 방향으로 발사!
				||  s.equalsIgnoreCase("0-6") // 내성문 방향으로 발사!
				||  s.equalsIgnoreCase("0-7") // 수호탑 방향으로 발사!
				||  s.equalsIgnoreCase("1-16") // 외성문 방향으로 침묵포탄 발사!
				||  s.equalsIgnoreCase("1-17") // 내성문 앞쪽으로 침묵포탄 발사!
				||  s.equalsIgnoreCase("1-18") // 내성문 좌측으로 침묵포탄 발사!
				||  s.equalsIgnoreCase("1-19") // 내성문 우측으로 침묵포탄 발사!
				||  s.equalsIgnoreCase("1-20") // 수호탑 방향으로 침묵포탄 발사!
				||  s.equalsIgnoreCase("0-9")){ // 외성문 방향으로 발사!
			int locx = 0, locy = 0, gfxid = 0, castleid = 0;
			if (s.equalsIgnoreCase("0-5") || s.equalsIgnoreCase("1-16")) { //외성문 방향으로 발사!, 외성문 방향으로 침묵포탄 발사!
				switch(npcId) {
				case 7000084:locx = 33114;locy = 32771;gfxid = 12193;castleid = 1;break;// 켄트성 공성측
				case 7000086:locx = 32795;locy = 32315;gfxid = 12197;castleid = 2;break;// 오크요새 공성측
				case 7000082:locx = 33632;locy = 32731;gfxid = 12197;castleid = 4;break;// 기란성 공성측
				}
			} else if (s.equalsIgnoreCase("0-6")) { //내성문 방향으로 발사!
				switch(npcId) {
				case 7000084:locx = 33155;locy = 32770;gfxid = 12193;castleid = 1;break;// 켄트성 공성측
				case 7000086:locx = 32796;locy = 32294;gfxid = 12197;castleid = 2;break;// 오크요새 공성측
				case 7000082:locx = 33633;locy = 32700;gfxid = 12197;castleid = 4;break;// 기란성 공성측
				}
			} else if (s.equalsIgnoreCase("1-17")) { //내성문 압쪽으로 침묵포탄 발사!
				switch(npcId) {
				case 7000084:locx = 33140;locy = 32776;gfxid = 12193;castleid = 1;break;// 켄트성 공성측
				case 7000086:locx = 32796;locy = 32300;gfxid = 12197;castleid = 2;break;// 오크요새 공성측
				case 7000082:locx = 33633;locy = 32711;gfxid = 12197;castleid = 4;break;// 기란성 공성측
				}
			} else if (s.equalsIgnoreCase("1-18")){ // 내성문 좌측으로 침묵포탄 발사!
				switch(npcId) {
				case 7000084:locx = 33140;locy = 32759;gfxid = 12193;castleid = 1;break;// 켄트성 공성측
				case 7000086:locx = 32789;locy = 32300;gfxid = 12197;castleid = 2;break;// 오크요새 공성측
				case 7000082:locx = 33626;locy = 32711;gfxid = 12197;castleid = 4;break;// 기란성 공성측
				}
			} else if (s.equalsIgnoreCase("1-19")){ // 내성문 우측으로 침묵포탄 발사!
				switch(npcId) {
				case 7000084:locx = 33140;locy = 32772;gfxid = 12193;castleid = 1;break;// 켄트성 공성측
				case 7000086:locx = 32805;locy = 32300;gfxid = 12197;castleid = 2;break;// 오크요새 공성측
				case 7000082:locx = 33641;locy = 32711;gfxid = 12197;castleid = 4;break;// 기란성 공성측
				}			
			} else if (s.equalsIgnoreCase("0-7") || s.equalsIgnoreCase("1-20")) { //수호탑 방향으로 발사!, 수호탑 방향으로 침묵포탄 발사!
				switch(npcId) {
				case 7000084:locx = 33165;locy = 32778;gfxid = 12193;castleid = 1;break;// 켄트성 공성측
				case 7000086:locx = 32798;locy = 32287;gfxid = 12197;castleid = 2;break;// 오크요새 공성측
				case 7000082:locx = 33632;locy = 32679;gfxid = 12197;castleid = 4;break;// 기란성 공성측
				}
			} else if (s.equalsIgnoreCase("0-9")) { //외성문 방향으로 발사!
				int pcCastleId = pc.getClanid() != 0 ? pc.getClan().getCastleId() : 0;
				switch(npcId) {
				case 7000085: // 켄트성 수성측
					if (castleDefenseClan(L1CastleLocation.KENT_CASTLE_ID) && pcCastleId != L1CastleLocation.KENT_CASTLE_ID) {
						pc.sendPackets(L1ServerMessage.sm3682);//투석기 사용: 실패(성을 수호하는 성혈 군주만 사용 가능)
						return null;
					}
					locx = 33107;locy = 32770;gfxid = 12197;castleid = 1;
					break;
				case 7000087: // 오크요새 수성측
					if (castleDefenseClan(L1CastleLocation.OT_CASTLE_ID) && pcCastleId != L1CastleLocation.OT_CASTLE_ID) {
						pc.sendPackets(L1ServerMessage.sm3682);//투석기 사용: 실패(성을 수호하는 성혈 군주만 사용 가능)
						return null;
					}
					locx = 32794;locy = 32320;gfxid = 12193;castleid = 2;
					break;
				case 7000083: // 기란성 수성측
					if (castleDefenseClan(L1CastleLocation.GIRAN_CASTLE_ID) && pcCastleId != L1CastleLocation.GIRAN_CASTLE_ID) {
						pc.sendPackets(L1ServerMessage.sm3682);//투석기 사용: 실패(성을 수호하는 성혈 군주만 사용 가능)
						return null;
					} 
					locx = 33631;locy = 32738;gfxid = 12193;castleid = 4;
					break;
				}					
			}

			boolean isNowWar = War.getInstance().isNowWar(castleid);
			if (!isNowWar) {
				pc.sendPackets(L1ServerMessage.sm3683);//투석기 사용: 실패(공성 시간에만 사용 가능)
				return null;
			}
			boolean inWar = false;
			for (L1War war : L1World.getInstance().getWarList()) {
				if (war.CheckClanInWar(pc.getClanName())) {
					inWar = true;
					break;
				}
			}
			if (!(pc.isCrown() && inWar && isNowWar)) {
				pc.sendPackets(L1ServerMessage.sm3681);//투석기 사용: 실패(전쟁을 선포한 군주만 사용 가능)
				return null; 
			}
			if (pc.getlastShellUseTime() + 10000L > System.currentTimeMillis()) {
				pc.sendPackets(L1ServerMessage.sm3680);//투석기 사용: 실패(재장전 시간 필요)
				return null;
			}
			if (obj != null && obj instanceof L1CataInstance) {
				if(!pc.getInventory().consumeItem(30124, 1)){
					pc.sendPackets(L1ServerMessage.sm337_BOMB);
					return null;
				}
				L1CataInstance npc = (L1CataInstance) obj;
				Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Attack), true);
				pc.sendPackets(new S_EffectLocation(locx, locy, gfxid), true);
				Broadcaster.wideBroadcastPacket(pc, new S_EffectLocation(locx, locy, gfxid), 100, true);
				if (s.equalsIgnoreCase("1-16") || s.equalsIgnoreCase("1-17") || s.equalsIgnoreCase("1-18") || s.equalsIgnoreCase("1-19") || s.equalsIgnoreCase("1-20")){
					silenceBomb(locx, locy);
				} else {
					damageBomb(locx, locy);
				}
				pc.updatelastShellUseTime();
			}
		}
		return null;
	}
	
	private boolean castleDefenseClan(int castleId) {
		boolean isExistDefenseClan = false;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (castleId == clan.getCastleId()) {
				isExistDefenseClan = true;
				break;
			}
		}
		return isExistDefenseClan;
	}
	
	private void silenceBomb(int locx, int locy) {
		L1PcInstance targetPc = null;
		L1NpcInstance targetNpc = null;
		L1EffectInstance effect = L1EffectSpawn.getInstance().spawnEffect(81154, 1000, locx, locy, (short) 4);
		for (L1Object object : L1World.getInstance().getVisibleObjects(effect, 3)) {
			if (object == null) {
				continue;
			}
			if (!(object instanceof L1Character)) {
				continue;
			}
			if (object.getId() == effect.getId()) {
				continue;
			}
			if (object instanceof L1PcInstance){
				targetPc = (L1PcInstance) object;
				targetPc.broadcastPacketWithMe(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage), true);
				targetPc.getSkill().setSkillEffect(L1SkillId.SILENCE, 15);
				targetPc.broadcastPacketWithMe(new S_PacketBox(S_PacketBox.POSION_ICON, targetPc, 6, 15), true);
				Broadcaster.broadcastPacket(targetPc, new S_Effect(targetPc.getId(), 10708), true);
			} else if (object instanceof L1SummonInstance || object instanceof L1PetInstance) {
				targetNpc = (L1NpcInstance) object;
				Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(targetNpc.getId(), ActionCodes.ACTION_Damage), true);
				targetNpc.getSkill().setSkillEffect(L1SkillId.SILENCE, 15);
				Broadcaster.broadcastPacket(targetNpc, new S_Effect(targetNpc.getId(), 10708), true);
			}
		}
	}
	
	private void damageBomb(int locx, int locy) {
		L1PcInstance targetPc = null;
		L1NpcInstance targetNpc = null;
		L1EffectInstance effect = L1EffectSpawn.getInstance().spawnEffect(81154, 1000, locx, locy, (short) 4);
		for (L1Object object : L1World.getInstance().getVisibleObjects(effect, 3)) {
			if (object == null) {
				continue;
			}
			if (!(object instanceof L1Character)) {
				continue;
			}
			if (object.getId() == effect.getId()) {
				continue;
			}
			if (object instanceof L1PcInstance) {
				targetPc = (L1PcInstance) object;
				targetPc.broadcastPacketWithMe(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage), true);
				targetPc.receiveDamage(targetPc, 100, 3);
			} else if (object instanceof L1SummonInstance || object instanceof L1PetInstance) {
				targetNpc = (L1NpcInstance) object;
				Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(targetNpc.getId(), ActionCodes.ACTION_Damage), true);
				targetNpc.receiveDamage(targetNpc, (int)100);
			}
		}
	}
}

