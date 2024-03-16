package l1j.server.server.clientpackets.proto;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanJoinningTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.L1ClanJoin;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoinConfirm;
import l1j.server.server.utils.StringUtil;

public class A_PledgeJoinConfirm extends ProtoHandler {
	protected A_PledgeJoinConfirm(){}
	private A_PledgeJoinConfirm(byte[] data, GameClient client) {
		super(data, client);
	}
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1Clan clan = _pc.getClan();
		if (clan == null) {
			return;
		}
		if (!eBloodPledgeRankType.isAuthRankAtKnight(_pc.getBloodPledgeRank())) {
			_pc.sendPackets(S_BloodPledgeJoinConfirm.NO_PRIVILEGE);
			return;
		}
		
		readP(1);// 0x08
		ePLEDGE_JOIN_CONFIRM_REQ_TYPE req_type = ePLEDGE_JOIN_CONFIRM_REQ_TYPE.fromInt(readC());
		if (req_type == null) {
			return;
		}
		readP(1);// 0x12
		String user_name = readS(readC());
		if (StringUtil.isNullOrEmpty(user_name)) {
			return;
		}
		L1PcInstance user = L1World.getInstance().getPlayer(user_name);
		switch (req_type) {
		case ePLEDGE_JOIN_CONFIRM_REQ_TYPE_ACCEPT:// 승인
			// 인게임에 접속 중일 때 승인 처리된다.
			if (user == null) {
				_pc.sendPackets(S_BloodPledgeJoinConfirm.USER_OFFLINE);
				return;
			}
			
			// 이미 혈맹에 가입중
			if (user.getClanid() > 0) {
				_pc.sendPackets(S_BloodPledgeJoinConfirm.PLEDGE_ALREADY_EXIST);
				return;
			}
			
			// 인원 제한
			L1ClanJoin join = L1ClanJoin.getInstance();
			if (clan.getClanMemberList().size() >= join.getLimitMemberCount(_pc.getLevel(), true, _pc.getAbility().getTotalCha())) {
				_pc.sendPackets(S_BloodPledgeJoinConfirm.PLEDGE_IS_FULL);
				return;
			}
			
			// 혈맹 가입
			if (!join.join(_pc, user)) {
				_pc.sendPackets(S_BloodPledgeJoinConfirm.ERROR);
				return;
			}
			_pc.sendPackets(S_BloodPledgeJoinConfirm.OK);
			break;
		case ePLEDGE_JOIN_CONFIRM_REQ_TYPE_REJECT:// 거절
			// 이미 가입되어 있는 상태
			for (ClanMember member : clan.getClanMemberList()) {
				if (member.name.equalsIgnoreCase(user_name)) {
					_pc.sendPackets(S_BloodPledgeJoinConfirm.PLEDGE_ALREADY_EXIST);
					return;
				}
			}
			if (!ClanJoinningTable.getInstance().cancel(clan, user_name)) {
				_pc.sendPackets(S_BloodPledgeJoinConfirm.ERROR);
				return;
			}
			if (user != null) {
				user.sendPackets(new S_ServerMessage(96, _pc.getName()), true); // \f1%0은 당신의요청을거절했습니다.
			}
			_pc.sendPackets(S_BloodPledgeJoinConfirm.OK);
			break;
		}
	}
	
	public enum ePLEDGE_JOIN_CONFIRM_REQ_TYPE{
		ePLEDGE_JOIN_CONFIRM_REQ_TYPE_ACCEPT(1),// 승인
		ePLEDGE_JOIN_CONFIRM_REQ_TYPE_REJECT(2),// 거절
		;
		private int value;
		ePLEDGE_JOIN_CONFIRM_REQ_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePLEDGE_JOIN_CONFIRM_REQ_TYPE v){
			return value == v.value;
		}
		public static ePLEDGE_JOIN_CONFIRM_REQ_TYPE fromInt(int i){
			switch(i){
			case 1:
				return ePLEDGE_JOIN_CONFIRM_REQ_TYPE_ACCEPT;
			case 2:
				return ePLEDGE_JOIN_CONFIRM_REQ_TYPE_REJECT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ePLEDGE_JOIN_CONFIRM_REQ_TYPE, %d", i));
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeJoinConfirm(data, client);
	}

}

