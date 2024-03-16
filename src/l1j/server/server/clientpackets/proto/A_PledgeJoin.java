package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.common.data.ePLEDGE_JOIN_REQ_TYPE;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanJoinningTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanJoin;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoin;
import l1j.server.server.utils.CharsetUtil;

public class A_PledgeJoin extends ProtoHandler {
	protected A_PledgeJoin(){}
	private A_PledgeJoin(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (_pc.getClanid() != 0) {
			_pc.sendPackets(S_BloodPledgeJoin.ALREADY_JOINED);// 이미 혈맹에 가입한 상태 입니다.
			return;
		}
		try {
			readP(1);// 0x0a
			int length = readC();
			String pledge_name = new String(readByte(length), 0, length, CharsetUtil.MS_949);
			L1Clan clan = L1World.getInstance().getClan(pledge_name);
			if (clan == null) {
				_pc.sendPackets(S_BloodPledgeJoin.PRINCE_NEED_OTHER_METHOD);
				return;
			}
			if (clan.get_limit_level() > _pc.getLevel()) {
				_pc.sendPackets(S_BloodPledgeJoin.JOIN_LIMIT_LEVEL);
				return;
			}
			if (clan.is_limit_user_names(_pc.getName())) {
				_pc.sendPackets(S_BloodPledgeJoin.JOIN_LIMIT_USER);
				return;
			}
			
			if (clan.getClanId() == Config.PLEDGE.BEGINNER_PLEDGE_ID && _pc.getLevel() > Config.PLEDGE.BEGINNER_PLEDGE_LEAVE_LEVEL) {
				//_pc.sendPackets(new S_SystemMessage(String.format("신규보호혈맹은 %d레벨 이하만 가입할 수 있습니다.", Config.PLEDGE.BEGINNER_PLEDGE_LEAVE_LEVEL)), true);
				_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(43), String.valueOf(Config.PLEDGE.BEGINNER_PLEDGE_LEAVE_LEVEL)), true);
				_pc.sendPackets(S_BloodPledgeJoin.PLEDGE_NOT_OPENED);
				return;
			}
			if (!clan.isEnableJoin()) {// 혈맹원 모집 셋팅
				_pc.sendPackets(S_BloodPledgeJoin.PLEDGE_NOT_OPENED);
				return;
			}
			if (clan.getClanMemberList().size() >= Config.PLEDGE.PLEDGE_LIMIT_MEMBER_COUNT) {// 혈맹원 50명 제한
				_pc.sendPackets(S_BloodPledgeJoin.PLEDGE_IS_FULL);
				return;
			}
			readP(1);// 0x10
			ePLEDGE_JOIN_REQ_TYPE join_type = ePLEDGE_JOIN_REQ_TYPE.fromInt(readC());
			switch(join_type){
			case ePLEDGE_JOIN_REQ_TYPE_CONFIRMATION:// 승인가입
				readP(1);// 0x1a
				int join_message_length = readC();
				String join_message = null;
				if (join_message_length > 0) {
					join_message = readS(join_message_length);
				}
				ClanJoinningTable.getInstance().add(clan, _pc, join_message);
				_pc.sendPackets(S_BloodPledgeJoin.CONFIRMING);
				break;
			case ePLEDGE_JOIN_REQ_TYPE_PASSWORD:// 암호가입
				readP(3);// pwd total length, 0x22
				int size = readC();
				StringBuilder sb = new StringBuilder(size);// hashed_password
				for (int i = 0; i < size; i++) {
					sb.append(String.format("%02X", readC()));
				}
				if (clan.getJoinPassword() == null || !clan.getJoinPassword().equalsIgnoreCase(sb.toString())) {
					_pc.sendPackets(S_BloodPledgeJoin.INVALID_PASSWORD);
					return;
				}
				_pc.sendPackets(L1ClanJoin.getInstance().join(clan, _pc));
				break;
			case ePLEDGE_JOIN_REQ_TYPE_IMMEDIATLY:// 즉시가입
				_pc.sendPackets(L1ClanJoin.getInstance().join(clan, _pc));
				break;
			default:
				_pc.sendPackets(S_BloodPledgeJoin.NO_PLEDGE_MEMBER_IN_WORLD);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeJoin(data, client);
	}

}


