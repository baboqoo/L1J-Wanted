package l1j.server.server.clientpackets.proto;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeChangeMasterAskNoti;
import l1j.server.server.utils.StringUtil;

/**
 * 군주를 위임할 대상에게 요청한다.
 * @author LinOffice
 */
public class A_PledgeChangeMasterAsk extends ProtoHandler {// 군주 위임할 대상에게 요청
	protected A_PledgeChangeMasterAsk(){}
	private A_PledgeChangeMasterAsk(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private String _new_master_name;
	
	void parse() {
		if (_total_length <= 2) {
			return;
		}
		readP(1);// 0x0a
		int name_length	= readC();// size
		if (name_length <= 0) {
			return;
		}
		_new_master_name = readS(name_length);
	}
	
	boolean isValidation() {
		return !StringUtil.isNullOrEmpty(_new_master_name);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() 
				|| _pc.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING 
				|| _pc.getMap().getInter() != null) {
			return;
		}
		L1Clan pledge = _pc.getClan();
		if (pledge == null) {
			return;
		}
		long current_time = System.currentTimeMillis() / 1000;
		
		// 3분 제한 시간
		if (pledge.get_master_change_req_time() + 180 > current_time) {
			_pc.sendPackets(L1ServerMessage.sm4524);// 잠시 후에 다시 시도해 주세요.
			return;
		}
		
		parse();
		if (!isValidation()) {
			return;
		}
		
		ClanMember member = pledge.getClanMember(_new_master_name);
		// 혈맹원 및 접속 검증
		if (member == null || !member.online) {
			return;
		}
		// 인게임 검증
		L1PcInstance new_master = member.player;
		if (new_master == null || new_master.getNetConnection() == null) {
			return;
		}
		if (new_master.getLevel() < 30 || !new_master.isCrown() || new_master.getMap().getInter() != null) {
			return;
		}
		
		pledge.set_master_change_req_time(current_time);
		new_master.sendPackets(new S_BloodPledgeChangeMasterAskNoti(_pc.getName()), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeChangeMasterAsk(data, client);
	}

}

