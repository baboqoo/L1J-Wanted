package l1j.server.LFCSystem;

import java.util.ArrayList;

import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class InstanceEnums {
	/** 유저의 instance space 상태를 나타낸다. **/
	public enum InstStatus{
		INST_USERSTATUS_NONE(1),	// instance space에 있지 않을 때
		INST_USERSTATUS_LFCREADY(2),
		INST_USERSTATUS_LFCINREADY(4),
		INST_USERSTATUS_LFC(8);		
		
		int _status;
		InstStatus(int i){
			_status = i;
		}
	}
	
	public enum InstSpcMessages{
		//INSTANCE_SPACE_FULL("인스턴스 던전이 꽉 찼습니다. 잠시 후 다시 시도해주세요.");
		INSTANCE_SPACE_FULL(S_SystemMessage.getRefText(1364));
		
		private String _msg;		
		InstSpcMessages(String msg){
			_msg = msg;
		}
		
		public String get(){
			return _msg;
		}
		public void sendSystemMsg(L1PcInstance pc){
			pc.sendPackets(new S_SystemMessage(get(), true));
		}
		public void sendSystemMsg(L1PcInstance pc, String msg){
			pc.sendPackets(new S_SystemMessage(new StringBuilder(get()).append(StringUtil.EmptyOneString).append(msg).toString(), true));			
		}
		public void sendGreenMsg(L1PcInstance pc){
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, get()));
		}
		public void sendGreenMsg(L1PcInstance pc, String msg){
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, new StringBuilder(get()).append(StringUtil.EmptyOneString).append(msg).toString()));
		}
	}
	
	/** 등록 관련 메시지 **/
	public enum LFCMessages{	
		/*REGIST_SUCCESS("등록이 완료되었습니다."),
		REGIST_ERR_ININST("인스턴스 상태에서는 사용할 수 없습니다."),
		REGIST_ERR_NOADENA("필요 아이템이 부족합니다."),
		REGIST_ERR_INTHEMILL("아직 준비중입니다."),
		REGIST_ERR_ADENA("배팅 금액이 잘못됐습니다."),
		REGIST_ERR_LEVEL("레벨 조건이 충족되지 않습니다. "),
		CREATE_ERR_TARGET_CANNOT("상대방(팀)은 현재 참여할 수 없는 상태입니다."),
		CREATE_ERR_CANNOT_INPARTYPLAY("파티중에는 개인전 신청이 불가능합니다."),
		CREATE_ERR_RVR("RVR 컨텐츠는 파티 컨텐츠입니다."),
		CREATE_ERR_PVP("PVP 컨텐츠는 논 파티 컨텐츠입니다."),
		CREATE_ERR_ONLYLEADER("파티장만 신청할 수 있습니다."),
		CREATE_ERR_PARTYMEMBER("참가 불가 상태인 파티원이 있습니다."),
		CREATE_ERR_PARTYMAXSIZE("최대 파티 인원을 넘었습니다."),
		CREATE_ERR_PARTYMINSIZE("최소 충족 파티인원을 채우지 못했습니다."),
		CREATE_NOTIFY_CANCEL_INPARTYPLAY("콜로세움 신청이 들어왔으나 파티중(개인전)/파티중이 아님(단체전)으로 인해 취소되었습니다."),
		CREATE_SUBSCRIBE("콜로세움 신청이 들어왔습니다. 15초 내에 수락하지 않으면 취소로 간주합니다."),
		CREATE_SUCCESS("콜로세움 신청이 완료되었습니다. 상대방이 15초 내에 수락하지 않으면 취소됩니다."),
		CREATE_CANCEL_OWNERUSER("상대방이 신청을 거절했습니다."),
		CREATE_CANCEL("경기가 취소되었습니다."),
		INGAME_CLOSE("경기가 종료되었습니다. 잠시 뒤 결과 판정 후 마을로 이동합니다."),
		INGAME_CLOSE_FORGM("GM에 의해 강제로 게임이 종료되었습니다."),
		INGAME_NOTIFY_WINNER("승리했습니다. 승리 보상 아이템이 곧 지급됩니다."),
		INGAME_NOTIFY_LOSER("당신은 LFC전에서 패배했습니다."),
		INGAME_NOTIFY_READY("[경기준비] "),
		INGAME_NOTIFY_START("Start!"),
		INGAME_NOTIFY_CLOSETIME("[종료임박] "),
		INGAME_NOTIFY_LOTTO("축하합니다. 랜덤보상에 당첨되었습니다.");*/
		REGIST_SUCCESS(S_SystemMessage.getRefText(1365)),
		REGIST_ERR_ININST(S_SystemMessage.getRefText(1366)),
		REGIST_ERR_NOADENA(S_SystemMessage.getRefText(1367)),
		REGIST_ERR_INTHEMILL(S_SystemMessage.getRefText(1368)),
		REGIST_ERR_ADENA(S_SystemMessage.getRefText(1369)),
		REGIST_ERR_LEVEL(S_SystemMessage.getRefText(1370)),
		CREATE_ERR_TARGET_CANNOT(S_SystemMessage.getRefText(1371)),
		CREATE_ERR_CANNOT_INPARTYPLAY(S_SystemMessage.getRefText(1362)),
		CREATE_ERR_RVR(S_SystemMessage.getRefText(1373)),
		CREATE_ERR_PVP(S_SystemMessage.getRefText(1374)),
		CREATE_ERR_ONLYLEADER(S_SystemMessage.getRefText(1375)),
		CREATE_ERR_PARTYMEMBER(S_SystemMessage.getRefText(1376)),
		CREATE_ERR_PARTYMAXSIZE(S_SystemMessage.getRefText(1377)),
		CREATE_ERR_PARTYMINSIZE(S_SystemMessage.getRefText(1378)),
		CREATE_NOTIFY_CANCEL_INPARTYPLAY(S_SystemMessage.getRefText(1379)),
		CREATE_SUBSCRIBE(S_SystemMessage.getRefText(1380)),
		CREATE_SUCCESS(S_SystemMessage.getRefText(1381)),
		CREATE_CANCEL_OWNERUSER(S_SystemMessage.getRefText(1382)),
		CREATE_CANCEL(S_SystemMessage.getRefText(1383)),
		INGAME_CLOSE(S_SystemMessage.getRefText(1384)),
		INGAME_CLOSE_FORGM(S_SystemMessage.getRefText(1385)),
		INGAME_NOTIFY_WINNER(S_SystemMessage.getRefText(1386)),
		INGAME_NOTIFY_LOSER(S_SystemMessage.getRefText(1387)),
		INGAME_NOTIFY_READY(S_SystemMessage.getRefText(1388)),
		INGAME_NOTIFY_START("Start!"),
		INGAME_NOTIFY_CLOSETIME(S_SystemMessage.getRefText(1389)),
		INGAME_NOTIFY_LOTTO(S_SystemMessage.getRefText(1390));
		private String _msg;		
		LFCMessages(String msg){
			_msg = msg;
		}
		
		public String get(){
			return _msg;
		}
		public void sendSystemMsg(L1PcInstance pc){
			pc.sendPackets(new S_SystemMessage(get(), true));
		}
		public void sendSystemMsg(L1PcInstance pc, String msg){
			pc.sendPackets(new S_SystemMessage(new StringBuilder(get()).append(StringUtil.EmptyOneString).append(msg).toString(), true));			
		}
		public void sendSystemMsgToList(ArrayList<L1PcInstance> pcs){
			sendList(pcs, new S_SystemMessage(get()));
		}
		public void sendSystemMsgToList(ArrayList<L1PcInstance> pcs, String msg){
			sendList(pcs, new S_SystemMessage(new StringBuilder(get()).append(StringUtil.EmptyOneString).append(msg).toString(), true));
		}
		public void sendGreenMsg(L1PcInstance pc){
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, get()));
		}
		public void sendGreenMsg(L1PcInstance pc, String msg){
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, new StringBuilder(get()).append(StringUtil.EmptyOneString).append(msg).toString()));
		}
		public void sendGreenMsgToList(ArrayList<L1PcInstance> pcs){
			sendList(pcs, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, get()));
		}
		public void sendGreenMsgToList(ArrayList<L1PcInstance> pcs, String msg){			
			sendList(pcs, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, new StringBuilder(get()).append(StringUtil.EmptyOneString).append(msg).toString()));
		}
		
		private void sendList(ArrayList<L1PcInstance> pcs, ServerBasePacket pck){
			int size 		= pcs.size();
			for (int i = 0; i < size; i++)
				pcs.get(i).sendPackets(pck, false);
			pck.clear();
		}
		
		public void sendSurvey(L1PcInstance pc){
			pc.sendPackets(new S_MessageYN(C_Attr.MSGCODE_LFC, C_Attr.YN_MESSAGE_CODE, get()));
		}
	}
}

