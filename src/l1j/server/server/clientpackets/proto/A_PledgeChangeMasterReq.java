package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeChangeMasterAck;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeChangeMasterNoti;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeUserInfo;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.world.BloodPledgeDAO;

/**
 * 대상이 위임 승낙시 요청한 군주에 오는 패킷 
 * 군주 교체를 처리한다.
 * @author LinOffice
 */
public class A_PledgeChangeMasterReq extends ProtoHandler {
	protected A_PledgeChangeMasterReq(){}
	private A_PledgeChangeMasterReq(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private String _new_master_name;
	private L1PcInstance _new_master;
	
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
					_new_master_name = readS(name_length);
				}
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation() {
		return !StringUtil.isNullOrEmpty(_new_master_name);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (_pc.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING) {
			_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_NOT_MASTER);
			return;
		}
		if (_pc.getMap().getInter() != null) {
			_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_MAINSERVER);
			return;
		}
		
		L1Clan pledge = _pc.getClan();
		if (pledge == null) {
			_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_NOT_PLEDGE);
			return;
		}
		// 혈맹 아지트 혹은 성 보유 검증
		if (pledge.getHouseId() != 0) {
			_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_NOT_OWN_AGIT);
			return;
		}
		if (pledge.getCastleId() != 0) {
			_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_NOT_OWN_CASTLE);
			return;
		}
		// 전쟁 여부 검증
		for (L1War war : L1World.getInstance().getWarList()) {
			if (war.CheckClanInWar(pledge.getClanName())) {
				_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_NOT_IN_WAR);
				return;
			}
		}
		
		parse();
		if (!isValidation()) {
			return;
		}
		ClanMember new_king = pledge.getClanMember(_new_master_name);
		ClanMember old_king = pledge.getClanMember(_pc.getName());
		// 혈맹원 및 접속 검증
		if (new_king == null || old_king == null) {
			_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_NOT_SAME_PLEDGE_MEMBER);
			return;
		}
		if (!new_king.online || !old_king.online) {
			_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_NOT_ONLINE);
			return;
		}
		// 인게임 검증
		_new_master = new_king.player;
		if (_new_master.getLevel() < 30) {
			_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_NOT_LEVEL);
			return;
		}
		if (!_new_master.isCrown()) {
			_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_NOT_PRINCE);
			return;
		}
		if (_new_master.getMap().getInter() != null) {
			_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_MAINSERVER);
			return;
		}
		
		// 유효검 검증 완료후 교체 진행
		int cur_date = (int) (System.currentTimeMillis() / 1000);
		old_king.rank = eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT;
		new_king.rank = eBloodPledgeRankType.RANK_NORMAL_KING;
		_pc.setBloodPledgeRank(old_king.rank);
		_pc.setPledgeRankDate(cur_date);
		_new_master.setBloodPledgeRank(new_king.rank);
		_new_master.setPledgeRankDate(cur_date);
		
		pledge.setLeaderId(_new_master.getId());
		pledge.setLeaderName(_new_master.getName());
		
		if (pledge.is_store_allow(_new_master.getName())) {
			pledge.del_store_allow_list(_new_master.getName());
		}
		
		if (!ClanTable.getInstance().updateMasterChange(pledge)) {
			System.out.println(String.format(
					"[A_PledgeChangeMasterReq] CHANGE_DB_UPDATE_FAILURE : PLEDGE_NAME(%s), NEW_MASTER(%s), OLD_MASTER(%s)", 
					pledge.getClanName(), _new_master.getName(), _pc.getName()));
			return;
		}
		_pc.save();
		_new_master.save();
		
		S_BloodPledgeChangeMasterNoti noti = new S_BloodPledgeChangeMasterNoti(_pc.getName(), _new_master_name);
		// 기존 군주 처리
		_pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, _pc.getBloodPledgeRank().toInt(), _pc.getName()), true);
		_pc.sendPackets(new S_BloodPledgeUserInfo(pledge.getClanName(), _pc.getBloodPledgeRank(), false), true);
		_pc.sendPackets(noti, false);
		_pc.sendPackets(S_BloodPledgeChangeMasterAck.eRESULT_OK);
		
		// 새로운 군주 처리
		_new_master.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, _new_master.getBloodPledgeRank().toInt(), _new_master.getName()), true);
		_new_master.sendPackets(new S_BloodPledgeUserInfo(pledge.getClanName(), _new_master.getBloodPledgeRank(), true), true);
		_new_master.sendPackets(noti, true);
		
		if (Config.WEB.WEB_SERVER_ENABLE) {
			BloodPledgeDAO.reload();
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeChangeMasterReq(data, client);
	}

}

