package l1j.server.server.clientpackets.proto;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeChangeMasterAnswerNoti;
import l1j.server.server.utils.StringUtil;

/**
 * 군주 위임 요청에 대한 응답을 처리한다.
 * @author LinOffice
 */
public class A_PledgeChangeMasterAnswer extends ProtoHandler {
	protected A_PledgeChangeMasterAnswer(){}
	private A_PledgeChangeMasterAnswer(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private String _old_master_name;
	private boolean _yes;
	
	private L1PcInstance _old_master;
	
	void parse() {
		if (_total_length <= 4) {
			return;
		}
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x0a:
				int name_length	= readC();// size
				if (name_length > 0) {
					_old_master_name = readS(name_length);
				}
				break;
			case 0x10:
				_yes = readBool();
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation() {
		return !StringUtil.isNullOrEmpty(_old_master_name);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !_pc.isCrown() 
				|| _pc.getLevel() < 30
				|| _pc.getMap().getInter() != null) {
			return;
		}
		L1Clan pledge = _pc.getClan();
		if (pledge == null) {
			return;
		}
		long current_time = System.currentTimeMillis() / 1000;
		
		// 1분내 제한 시간
		if (pledge.get_master_change_req_time() + 60 <= current_time) {
			_pc.sendPackets(L1ServerMessage.sm4524);// 잠시 후에 다시 시도해 주세요.
			return;
		}
		
		parse();
		if (!isValidation()) {
			return;
		}
		
		ClanMember member = pledge.getClanMember(_old_master_name);
		// 혈맹원 및 접속 검증
		if (member == null || !member.online) {
			return;
		}
		// 인게임 검증
		_old_master = member.player;
		if (_old_master == null || _old_master.getNetConnection() == null) {
			return;
		}
		// 군주 검증
		if (_old_master.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING) {
			return;
		}
		// 인터서버 검증
		if (_old_master.getMap().getInter() != null) {
			return;
		}
		
		_old_master.sendPackets(new S_BloodPledgeChangeMasterAnswerNoti(_pc.getName(), _yes), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeChangeMasterAnswer(data, client);
	}

}

