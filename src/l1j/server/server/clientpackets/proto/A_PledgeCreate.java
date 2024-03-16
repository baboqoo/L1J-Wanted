package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.common.data.ePLEDGE_JOIN_REQ_TYPE;
import l1j.server.server.GameClient;
import l1j.server.server.construct.L1BeginnerQuest;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ClanBlessBuffTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.pledge.S_BlessOfBloodPledgeUpdateNoti;
import l1j.server.server.serverpackets.pledge.S_Pledge;
import l1j.server.server.serverpackets.pledge.S_PledgeWatch;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeEmblem;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeInfo;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeMemberChanged;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeMemberChanged.MemberChangedReason;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeUserInfo;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;
//import manager.ManagerInfoThread;  // MANAGER DISABLED

public class A_PledgeCreate extends ProtoHandler {
	protected A_PledgeCreate(){}
	private A_PledgeCreate(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (!_pc.isCrown()) {
			_pc.sendPackets(L1ServerMessage.sm85);// \f1프린스와 프린세스만이 혈맹을 창설할 수 있습니다.
			return;
		}
		if (_pc.getClanid() != 0) {
			_pc.sendPackets(L1ServerMessage.sm86);// \f1 벌써 혈맹이 결성되고 있으므로 작성할 수 없습니다.
			return;
		}
		if (!_pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
			_pc.sendPackets(L1ServerMessage.sm189);// \f1%0이 부족합니다.
			return;
		}
		if (_pc.getLevel() < Config.PLEDGE.PLEDGE_CREATE_MIN_LEVEL) {
			//_pc.sendPackets(new S_SystemMessage(String.format("%d레벨 미만은 혈맹을 창설할수 없습니다.", Config.PLEDGE.PLEDGE_CREATE_MIN_LEVEL)), true);
			_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(41), String.valueOf(Config.PLEDGE.PLEDGE_CREATE_MIN_LEVEL)), true);
			return;
		}
		readP(1);
		int nameLength	= readC();// namelength
		String name	= readS(nameLength);
		if (StringUtil.isNullOrEmpty(name)) {
			return;
		}
		for (char ac : name.toCharArray()) {
			if (!Character.isLetterOrDigit(ac)) {
				//_pc.sendPackets(new S_SystemMessage(String.format("혈맹 창설 불가 : 잘못된 문자또는 기호가 포함됨. (%s)", ac)), true);
				_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(42), String.valueOf(ac)), true);
				return;
			}
		}
		for (int i=0; i<name.length(); i ++) {
			if (name.charAt(i) == ' ' || name.charAt(i) == 'ㅤ') {
				_pc.sendPackets(L1ServerMessage.sm53);
				return;
			}
		}
		int numOfNameBytes = name.getBytes(CharsetUtil.EUC_KR_STR).length;
		if (8 < (numOfNameBytes - name.length()) || 16 < numOfNameBytes) {
			_pc.sendPackets(L1ServerMessage.sm98);// \f1 혈맹 이름 최대 16자
			return;
		}
		
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getClanName().toLowerCase().equals(name.toLowerCase())) {
				_pc.sendPackets(L1ServerMessage.sm99);// \f1 같은 이름의 혈맹이 존재합니다.
				return;
			}
		}
		
		readP(1);// 10
		ePLEDGE_JOIN_REQ_TYPE join_type = ePLEDGE_JOIN_REQ_TYPE.fromInt(readC());// type	0:즉시, 1:승인, 2:암호
		String hashed_password = null;
		if (join_type == ePLEDGE_JOIN_REQ_TYPE.ePLEDGE_JOIN_REQ_TYPE_PASSWORD) {// 암호가입
			readP(1);// 1a
			int passwordlength = readC();// passwordlength
			StringBuilder sb = new StringBuilder(passwordlength);
			for (int i = 0; i < passwordlength; i++) {
				sb.append(String.format("%02X", readC()));
			}
			hashed_password = sb.toString();
		}
		readP(1);// 22
		int message_length			= readC();// memolength
		String introduction_message	= StringUtil.EmptyString;
		if (message_length > 0) {
			if (message_length > 40) {
				_pc.sendPackets(L1ServerMessage.sm7944);// 소개 내용이 너무 깁니다. (최대 40 bytes)
				return;
			}
			introduction_message = readS(message_length).replaceAll(StringUtil.MinusString, StringUtil.EmptyString);// 소개글
		}

		_pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
		L1Clan clan = ClanTable.getInstance().createClan(_pc, name, join_type, hashed_password, introduction_message);// 혈맹 창설
		//ManagerInfoThread.ClanMaker += 1;  // MANAGER DISABLED
		if (clan != null) {
			_pc.setClan(clan);
			_pc.setTitle(StringUtil.EmptyString);
			
			S_BloodPledgeUserInfo user_info		= new S_BloodPledgeUserInfo(clan.getClanName(), _pc.getBloodPledgeRank(), true);
			S_BloodPledgeInfo emlem_status		= new S_BloodPledgeInfo(_pc.getClan().getEmblemStatus() == 1);
			S_PledgeWatch attention	= new S_PledgeWatch(null);
			
			_pc.sendPackets(user_info, false);
			_pc.sendPackets(new S_BloodPledgeMemberChanged(MemberChangedReason.ADD_ME, _pc.getId()), true);// 혈맹원 정보
			_pc.sendPackets(new S_BloodPledgeMemberChanged(MemberChangedReason.ADD, _pc.getId()), true);// 혈맹원 정보
			_pc.broadcastPacketWithMe(new S_CharTitle(_pc.getId(), _pc.getTitle()), true);
			_pc.sendPackets(new S_Pledge(_pc, clan.getEmblemId(), _pc.getBloodPledgeRank()), true);
			_pc.broadcastPacketWithMe(new S_BloodPledgeEmblem(_pc.getId(), 0), true);
			_pc.sendPackets(emlem_status, false);
			_pc.sendPackets(new S_BloodPledgeInfo(_pc, 1, 0, clan.getClanMemberList()), true);
			_pc.sendPackets(attention, false);
			_pc.sendPackets(emlem_status, false);
			_pc.sendPackets(user_info, true);
			_pc.sendPackets(emlem_status, true);
			_pc.sendPackets(attention, true);
			_pc.sendPackets(new S_ServerMessage(84, name), true);// \f1%0 혈맹이 창설되었습니다.
			_pc.broadcastPacket(new S_PCObject(_pc), true);

			clan.setEinhasadBlessBuff(0);
			clan.setBuffFirst(ClanBlessBuffTable.getRandomBuff(clan));
			clan.setBuffSecond(ClanBlessBuffTable.getRandomBuff(clan));
			clan.setBuffThird(ClanBlessBuffTable.getRandomBuff(clan));
			_pc.sendPackets(new S_BlessOfBloodPledgeUpdateNoti(clan), true);
			ClanTable.getInstance().updateClan(clan);
		}
		if (_pc.getClanid() != 0 && _pc.getLevel() <= Config.QUEST.BEGINNER_QUEST_LIMIT_LEVEL){
			_pc.getQuest().questProcess(L1BeginnerQuest.PLEDGE_CREATE);
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeCreate(data, client);
	}

}


