package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Calendar;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_War;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Castle;

public class L1War {
	private String _param1 = null;
	private String _param2 = null;
	private final ArrayList<String> _attackClanList = new ArrayList<String>();
	private String _defenceClanName = null;
	private int _warType = 0;
	private L1Castle _castle = null;
	private long _warEndTime;

	private boolean _isWarTimerDelete = false;

	public L1War() {
	}

	class CastleWarTimer implements Runnable {
		public CastleWarTimer() {
		}

		@Override
		public void run() {
			try {
				for(;;){
					try {
						Thread.sleep(1000);
						if (_warEndTime < System.currentTimeMillis()) {
							break;
						}
					} catch (Exception exception) {
						break;
					}
					if (_isWarTimerDelete) {
						return;
					}
				}
				CeaseCastleWar();
				delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class SimWarTimer implements Runnable {
		public SimWarTimer() {
		}

		@Override
		public void run() {
			try {
				for(int loop = 0; loop < 240; loop++){
					try {
						Thread.sleep(60000);
					} catch (Exception exception) {
						break;
					}
					if (_isWarTimerDelete) {
						return;
					}
				}
				CeaseWar(_param1, _param2);
				delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void handleCommands(int war_type, String attack_clan_name, String defence_clan_name) {
		SetWarType(war_type);
		DeclareWar(attack_clan_name, defence_clan_name);

		_param1 = attack_clan_name;
		_param2 = defence_clan_name;
		InitAttackClan();
		AddAttackClan(attack_clan_name);
		SetDefenceClanName(defence_clan_name);

		if (war_type == 1) {
			GetCastleId();
			_castle = GetCastle();
			if (_castle != null) {
				_warEndTime = LongType_setTime(_castle.getWarTime().getTimeInMillis(), Config.PLEDGE.WAR_TIME_UNIT, Config.PLEDGE.WAR_TIME);
				// Calendar cal = (Calendar) _castle.getWarTime().clone();
				// cal.add(Config.ALT_WAR_TIME_UNIT, Config.ALT_WAR_TIME);
				// _warEndTime = cal;
			}

			CastleWarTimer castle_war_timer = new CastleWarTimer();
			GeneralThreadPool.getInstance().execute(castle_war_timer);
		} else if (war_type == 2) {
			SimWarTimer sim_war_timer = new SimWarTimer();
			GeneralThreadPool.getInstance().execute(sim_war_timer);
		}
		L1World.getInstance().addWar(this);
	}

	private long LongType_setTime(long o, int type, long time) {
		if (Calendar.DATE == type) {
			return o + (60000 * 60 * 24 * time);
		}
		if (Calendar.HOUR_OF_DAY == type) {
			return o + (60000 * 60 * time);
		}
		if (Calendar.MINUTE == type) {
			return o + (60000 * time);
		}
		return 0;
	}

	public void CastleWarThreadExit() {
		_isWarTimerDelete = true;
		delete();
	}

	private void RequestCastleWar(int type, String clan1_name, String clan2_name) {
		if (clan1_name == null || clan2_name == null) {
			return;
		}
		L1Clan clan1 = L1World.getInstance().getClan(clan1_name);
		if (clan1 != null) {
			L1PcInstance clan1_member[] = clan1.getOnlineClanMember();
			for (int cnt = 0; cnt < clan1_member.length; cnt++) {
				clan1_member[cnt].sendPackets(new S_War(type, clan1_name, clan2_name), true);
			}
		}

		int attack_clan_num = GetAttackClanListSize();

		if (type == 1 || type == 2 || type == 3) {
			L1Clan clan2 = L1World.getInstance().getClan(clan2_name);
			if (clan2 != null) {
				L1PcInstance clan2_member[] = clan2.getOnlineClanMember();
				for (int cnt = 0; cnt < clan2_member.length; cnt++) {
					if (type == 1) {
						clan2_member[cnt].sendPackets(new S_War(type,clan1_name, clan2_name), true);
					} else if (type == 2) {
						clan2_member[cnt].sendPackets(new S_War(type,clan1_name, clan2_name), true);
						if (attack_clan_num == 1) {
							clan2_member[cnt].sendPackets(new S_War(4,clan2_name, clan1_name), true);
						} else {
							clan2_member[cnt].sendPackets(new S_ServerMessage(228, clan1_name, clan2_name), true);
							RemoveAttackClan(clan1_name);
						}
					} else if (type == 3) {
						clan2_member[cnt].sendPackets(new S_War(type,clan1_name, clan2_name), true);
						if (attack_clan_num == 1) {
							clan2_member[cnt].sendPackets(new S_War(4,clan2_name, clan1_name), true);
						} else {
							clan2_member[cnt].sendPackets(new S_ServerMessage(227, clan1_name, clan2_name), true);
							RemoveAttackClan(clan1_name);
						}
					}
				}
			}
		}

		if ((type == 2 || type == 3) && attack_clan_num == 1) {
			_isWarTimerDelete = true;
			delete();
		}
	}

	private void RequestSimWar(int type, String clan1_name, String clan2_name) {
		if (clan1_name == null || clan2_name == null) {
			return;
		}
		L1Clan clan1 = L1World.getInstance().getClan(clan1_name);
		if (clan1 != null) {
			L1PcInstance clan1_member[] = clan1.getOnlineClanMember();
			for (int cnt = 0; cnt < clan1_member.length; cnt++) {
				clan1_member[cnt].sendPackets(new S_War(type, clan1_name, clan2_name), true);
			}
		}

		if (type == 1 || type == 2 || type == 3) {
			L1Clan clan2 = L1World.getInstance().getClan(clan2_name);
			if (clan2 != null) {
				L1PcInstance clan2_member[] = clan2.getOnlineClanMember();
				for (int cnt = 0; cnt < clan2_member.length; cnt++) {
					if (type == 1) {
						clan2_member[cnt].sendPackets(new S_War(type, clan1_name, clan2_name), true);
					} else if (type == 2 || type == 3) {
						clan2_member[cnt].sendPackets(new S_War(type, clan1_name, clan2_name), true);
						clan2_member[cnt].sendPackets(new S_War(4, clan2_name, clan1_name), true);
					}
				}
			}
		}

		if (type == 2 || type == 3) {
			_isWarTimerDelete = true;
			delete();
		}
	}

	public void WinCastleWar(String clan_name) {
		String defence_clan_name = GetDefenceClanName();
		L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(231, clan_name, defence_clan_name), true);

		L1Clan defence_clan = L1World.getInstance().getClan(defence_clan_name);
		if (defence_clan != null) {
			L1PcInstance defence_clan_member[] = defence_clan.getOnlineClanMember();
			for (int i = 0; i < defence_clan_member.length; i++) {
				for (String clanName : GetAttackClanList()) {
					defence_clan_member[i].sendPackets(new S_War(3, defence_clan_name, clanName), true);
				}
			}
		}

		String clanList[] = GetAttackClanList();
		L1Clan clan = null;
		L1PcInstance clan_member[] = null;
		for (int j = 0; j < clanList.length; j++) {
			if (clanList[j] != null) {
				L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(227, defence_clan_name, clanList[j]), true);
				clan = L1World.getInstance().getClan(clanList[j]);
				if (clan != null) {
					clan_member = clan.getOnlineClanMember();
					for (int k = 0; k < clan_member.length; k++) {
						clan_member[k].sendPackets(new S_War(3, clanList[j],defence_clan_name), true);
					}
				}
			}
		}

		_isWarTimerDelete = true;
		delete();
	}

	public void CeaseCastleWar() {
		String defence_clan_name = GetDefenceClanName();
		String clanList[] = GetAttackClanList();
		if (defence_clan_name != null) {
			L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(231, defence_clan_name, clanList[0]), true);
		}
		L1Clan defence_clan = L1World.getInstance().getClan(defence_clan_name);
		if (defence_clan != null) {
			L1PcInstance defence_clan_member[] = defence_clan.getOnlineClanMember();
			for (int i = 0; i < defence_clan_member.length; i++) {
				defence_clan_member[i].sendPackets(new S_War(4, defence_clan_name, clanList[0]), true);
			}
		}
		L1Clan clan = null;
		L1PcInstance clan_member[] = null;
		for (int j = 0; j < clanList.length; j++) {
			if (clanList[j] != null) {
				L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(227, defence_clan_name, clanList[j]), true);
				clan = L1World.getInstance().getClan(clanList[j]);
				if (clan != null) {
					clan_member = clan.getOnlineClanMember();
					for (int k = 0; k < clan_member.length; k++) {
						if (clan_member[k] != null) {
							clan_member[k].sendPackets(new S_War(3, clanList[j], defence_clan_name), true);
						}
					}
				}
			}
		}

		_isWarTimerDelete = true;
		delete();
	}

	public void DeclareWar(String clan1_name, String clan2_name) {
		if (GetWarType() == 1) {
			RequestCastleWar(1, clan1_name, clan2_name);
		} else {
			RequestSimWar(1, clan1_name, clan2_name);
		}
	}

	public void SurrenderWar(String clan1_name, String clan2_name) {
		if (GetWarType() == 1) {
			RequestCastleWar(2, clan1_name, clan2_name);
		} else {
			RequestSimWar(2, clan1_name, clan2_name);
		}
	}

	public void CeaseWar(String clan1_name, String clan2_name) {
		if (GetWarType() == 1) {
			RequestCastleWar(3, clan1_name, clan2_name);
		} else {
			RequestSimWar(3, clan1_name, clan2_name);
		}
	}

	public void WinWar(String clan1_name, String clan2_name) {
		if (GetWarType() == 1) {
			RequestCastleWar(4, clan1_name, clan2_name);
		} else {
			RequestSimWar(4, clan1_name, clan2_name);
		}
	}

	public boolean CheckClanInWar(String clan_name) {
		if (GetDefenceClanName().toLowerCase().equals(clan_name.toLowerCase())) {
			return true;
		}
		return CheckAttackClan(clan_name);
	}

	public boolean CheckClanInSameWar(String player_clan_name, String target_clan_name) {
		boolean player_clan_flag;
		boolean target_clan_flag;

		if (GetDefenceClanName().toLowerCase().equals(player_clan_name.toLowerCase())) {
			player_clan_flag = true;
		} else {
			player_clan_flag = CheckAttackClan(player_clan_name);
		}
		if (GetDefenceClanName().toLowerCase().equals(target_clan_name.toLowerCase())) {
			target_clan_flag = true;
		} else {
			target_clan_flag = CheckAttackClan(target_clan_name);
		}
		if (player_clan_flag == true && target_clan_flag == true) {
			return true;
		}
		return false;
	}

	public String GetEnemyClanName(String player_clan_name) {
		if (GetDefenceClanName().toLowerCase().equals(player_clan_name.toLowerCase())) {
			String clanList[] = GetAttackClanList();
			for (int cnt = 0; cnt < clanList.length; cnt++) {
				if (clanList[cnt] != null) {
					return clanList[cnt];
				}
			}
		}
		return GetDefenceClanName();
	}

	public void delete() {
		L1World.getInstance().removeWar(this);
	}

	public int GetWarType() {
		return _warType;
	}

	public void SetWarType(int war_type) {
		_warType = war_type;
	}

	public String GetDefenceClanName() {
		return _defenceClanName;
	}

	public void SetDefenceClanName(String defence_clan_name) {
		_defenceClanName = defence_clan_name;
	}

	public void InitAttackClan() {
		_attackClanList.clear();
	}

	public void AddAttackClan(String attack_clan_name) {
		if (!_attackClanList.contains(attack_clan_name)) {
			_attackClanList.add(attack_clan_name);
		}
	}

	public void RemoveAttackClan(String attack_clan_name) {
		if (_attackClanList.contains(attack_clan_name)) {
			_attackClanList.remove(attack_clan_name);
		}
	}

	public boolean CheckAttackClan(String attack_clan_name) {
		return _attackClanList.contains(attack_clan_name);
	}

	public String[] GetAttackClanList() {
		return _attackClanList.toArray(new String[_attackClanList.size()]);
	}

	public int GetAttackClanListSize() {
		return _attackClanList.size();
	}

	public int GetCastleId() {
		if (GetWarType() == 1) {
			L1Clan clan = L1World.getInstance().getClan(GetDefenceClanName());
			if (clan != null) {
				return clan.getCastleId();
			}
		}
		return 0;
	}

	public L1Castle GetCastle() {
		if (GetWarType() == 1) {
			L1Clan clan = L1World.getInstance().getClan(GetDefenceClanName());
			if (clan != null) {
				int castle_id = clan.getCastleId();
				return CastleTable.getInstance().getCastleTable(castle_id);
			}
		}
		return null;
	}
}

