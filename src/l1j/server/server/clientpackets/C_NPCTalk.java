package l1j.server.server.clientpackets;

import l1j.server.GameSystem.inn.InnHelper;
import l1j.server.IndunSystem.minigame.L1Gambling;
import l1j.server.server.GameClient;
import l1j.server.server.construct.L1TeleportInfo;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.serverpackets.S_AuctionBoard;
import l1j.server.server.serverpackets.S_Inn;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.S_MapTeleportInfoNoti;
import l1j.server.server.utils.StringUtil;

public class C_NPCTalk extends ClientBasePacket implements L1TeleportInfo {
	private static final String C_NPC_TALK = "[C] C_NPCTalk";
	
	public C_NPCTalk(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		int objid = readD();
		L1Object obj = L1World.getInstance().findObject(objid);
		if (obj == null || obj instanceof L1MonsterInstance) {
			return;
		}
		L1NpcInstance npc	= (L1NpcInstance) obj;
		int npcid			= npc.getNpcTemplate().getNpcId();
		S_MapTeleportInfoNoti teleportPacket = getTeleportWindowPck(npcid, objid);
		if (teleportPacket != null) {
			pc.sendPackets(teleportPacket);
			return;
		}
		
		if (InnHelper.isHelper(npcid)) {// 여관
			L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(npcid);
			String htmlid = "inn";
			if (talking != null)
				htmlid = talking.getNormalAction();

			pc.sendPackets(new S_Inn(objid, pc.getAlignment() >= 0 ? htmlid : htmlid + "1", npc.getDesc()), true);
			return;
		}
		
		if (npcid == 400064) {// 미니게임
			gambling(pc);
		} else if (npcid == 81161) {// 경매 계시판
			pc.sendPackets(new S_AuctionBoard(npc, pc), true);
		} else if (npc.getNpcTemplate().isNotification() && !pc.knownsObject(npc)) {// 이벤트 알람 엔피씨
			npc.onPerceive(pc);
		}
		
		L1NpcAction action = NpcActionTable.getInstance().get(pc, obj);
		if (action != null) {
			L1NpcHtml html = action.execute(StringUtil.EmptyString, pc, obj, new byte[0]);
			if (html != null) {
				if (pc.isGm())
					pc.sendPackets(new S_SystemMessage("Dialog " + html), true);													
				pc.sendPackets(new S_NPCTalkReturn(obj.getId(), html), true);
			}
			return;
		}
        if (!pc.isGhostCanTalk()) {
        	return;
        }
		obj.onTalkAction(pc);
	}
	
	S_MapTeleportInfoNoti getTeleportWindowPck(int npcid, int objid) {
		switch (npcid) {
		case 50015:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_TALL_ISLAND, L1TownLocation.TOWNID_TALKING_ISLAND);// 말하는 섬 (루카스)
		case 50024:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_GLUDIO, L1TownLocation.TOWNID_GLUDIO);// 글루딘 마을 (아스터)
		case 50082:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_ORC, L1TownLocation.TOWNID_ORCISH_FOREST);// 화전민 마을 (지프란)
		case 5091:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_ELF, L1TownLocation.TOWNID_ELVEN_FOREST);// 요정숲 마을 (엘루나)
		case 50054:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_WOODBEC, L1TownLocation.TOWNID_WINDAWOOD);// 우드벡 마을 (트레이)
		case 50056:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_SILVEER_KNIGHT, L1TownLocation.TOWNID_SILVER_KNIGHT);// 은기사 마을 (메트)
		case 50020:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_KENT, L1TownLocation.TOWNID_KENT);// 켄트성 마을 (스텐리)
		case 50036:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_GIRAN, L1TownLocation.TOWNID_GIRAN);// 기란 마을 (윌마)
		case 50066:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_HEINE, L1TownLocation.TOWNID_HEINE);// 하이네 마을 (리올)
		case 50039:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_WERLDERN, L1TownLocation.TOWNID_WERLDAN);// 웰던 마을 (레슬리)
		case 50051:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_OREN, L1TownLocation.TOWNID_OREN);// 오렌 마을 (키리우스)
		case 50044:case 50046:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_ADEN, L1TownLocation.TOWNID_ADEN);// 아덴 마을 (엘레리스)
		case 50079:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_SCAVE, L1TownLocation.TOWNID_SILENT_CAVERN);// 침묵의 동굴 마을 (다니엘)
		case 3000005:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_BEHEMOTH, L1TownLocation.TOWNID_BEHEMOTH);// 베히모스 (데카비아)
		case 3100005:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_SILVERIA, L1TownLocation.TOWNID_SILVERIA);// 실베리아 (샤리엘)
		case 70106:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_LUUN, L1TownLocation.TOWNID_LUUN);// 루운성 마을 (세민)
		case 181210:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, TELEPORT_ACTION, T_SKY_GARDEN, L1TownLocation.TOWNID_SKYGARDEN);// 수상한 하늘정원 (피아트)
		case 202055:
			return S_MapTeleportInfoNoti.getTeleportPck(objid, CLAUDIA_ACTION, T_CLAUDIA, L1TownLocation.TOWNID_CLAUDIA);// 클라우디아 (소피)
		default:
			return null;
		}
	}
	
	void gambling(L1PcInstance pc) {
		L1Gambling gam = new L1Gambling();
		gam.dealerTrade(pc);
		gam = null;
	}

	@Override
	public String getType() {
		return C_NPC_TALK;
	}
}
