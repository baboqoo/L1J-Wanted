package l1j.server.server.model;

import java.util.ArrayList;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.controller.action.War;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.serverpackets.S_PinkName;

public class L1PinkName {

	private L1PinkName() {
	}

	static class PinkNameTimer implements Runnable {
		private L1PcInstance _attacker = null;
		private long _nextPinkNameSendTime;

		public PinkNameTimer(L1PcInstance attacker) {
			_attacker = attacker;
			_nextPinkNameSendTime = GameTimeClock.getInstance().getGameTime().getSeconds() + _attacker.getPinkNameTime() + 1;
		}

		@Override
		public void run() {

			if (!_attacker.isPinkName()) {
				_attacker.setPinkName(true);
				S_PinkName pck = new S_PinkName(_attacker.getId(), _attacker.getPinkNameTime());
				if (!_attacker.isGmInvis()) {
					_attacker.broadcastPacket(pck);
				}
				_attacker.sendPackets(pck, true);
			}

			if (_attacker.isDead()) {
				_attacker.SetPinkNameTime(0);
			} else if (_attacker.DecrementPinkNameTime() > 0) {
				long currentTime = GameTimeClock.getInstance().getGameTime().getSeconds();

				if (_nextPinkNameSendTime < currentTime) {
					S_PinkName pck = new S_PinkName(_attacker.getId(), _attacker.getPinkNameTime());
					if (!_attacker.isGmInvis()) {
						_attacker.broadcastPacket(pck);
					}
					_attacker.sendPackets(pck, true);
					_nextPinkNameSendTime = currentTime + _attacker.getPinkNameTime() + 1;
				}

				GeneralThreadPool.getInstance().schedule(this, 1000);
				return;
			}

			stopPinkName(_attacker);
		}

		private void stopPinkName(L1PcInstance attacker) {
			attacker.setPinkName(false);
			attacker.broadcastPacketWithMe(new S_PinkName(attacker.getId(), 0), true);
		}
	}

	public static void onAction(L1PcInstance attacker) {
		if (attacker == null) {
			return;
		}
		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(attacker);
		if (castleId != 0) {
			isNowWar = War.getInstance().isNowWar(castleId);
		}
		if (attacker.getRegion() == L1RegionStatus.NORMAL && isNowWar == false) {
			on_pink(attacker);
		}
	}

	public static void onAction(L1PcInstance pc, L1Character cha) {
		if (pc == null || cha == null) {
			return;
		}
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance attacker = (L1PcInstance) cha;
		if (pc.getId() == attacker.getId()) {
			return;
		}
		if (attacker.getFightId() == pc.getId()) {
			return;
		}

		boolean is_siege_region = false;// 공성전 지역
		int castleId = L1CastleLocation.getCastleIdByArea(pc);
		if (castleId != 0) {
			is_siege_region = War.getInstance().isNowWar(castleId);
		}

		if (pc.getRegion() == L1RegionStatus.NORMAL && attacker.getRegion() == L1RegionStatus.NORMAL && is_siege_region == false) {
			if (attacker.SetPinkNameTime(20) == 0) {
				on_pink(attacker);
				on_visible_party_member(attacker);
			}
		}
	}

	public static void onHelp(L1Character target, L1Character helper) {
		if (target == null || helper == null) {
			return;
		}
		if (helper instanceof L1PcInstance == false) {
			return;
		}
		if (target instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance helperPc = (L1PcInstance) helper;
		L1PcInstance targetPc = (L1PcInstance) target;
		if (!targetPc.isPinkName()) {
			return;
		}
		if (helperPc.getId() == targetPc.getId()) {
			return;
		}
		boolean is_siege_region = false;
		int castleId = L1CastleLocation.getCastleIdByArea(helperPc);
		if (castleId != 0) {
			is_siege_region = War.getInstance().isNowWar(castleId);
		}

		if (targetPc.getRegion() == L1RegionStatus.NORMAL && helperPc.getRegion() == L1RegionStatus.NORMAL && is_siege_region == false) {
			if (helperPc.SetPinkNameTime(20) == 0) {
				on_pink(helperPc);
			}
		}
	}
	
	/**
	 * 화면내 파티 멤버 정당방위 적용
	 * @param attacker
	 */
	static void on_visible_party_member(L1PcInstance attacker) {
		ArrayList<L1PcInstance> members = L1World.getInstance().getVisiblePartyPlayer(attacker, -1);
		if (members == null || members.isEmpty()) {
			return;
		}
		for (L1PcInstance member : members) {
			if (member == null || member.getNetConnection() == null) {
				continue;
			}
			if (member.SetPinkNameTime(20) == 0) {
				on_pink(member);
			}
		}
	}
	
	/**
	 * 보라돌이 시작
	 * @param pc
	 */
	static void on_pink(L1PcInstance pc) {
		pc.setPinkName(true);
		S_PinkName pck = new S_PinkName(pc.getId(), 20);
		if (!pc.isGmInvis()) {
			pc.broadcastPacket(pck);
		}
		pc.sendPackets(pck, true);
		PinkNameTimer pink = new PinkNameTimer(pc);
		GeneralThreadPool.getInstance().execute(pink);
	}
}

