package l1j.server.server.serverpackets.inter;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InterGuardianTowerWarOccupyTeam extends ServerBasePacket {
	private static final String S_INTER_GUARDIAN_TOWER_WAR_OCCUPY_TEAM	= "[S] S_InterGuardianTowerWarOccupyTeam";
	private byte[] _byte = null;
	public static final int TEAM_NOTI = 0x09f6;
	public static final int TEAM_TIME = 0x0939;

	public S_InterGuardianTowerWarOccupyTeam(int teamId, long attackBonusTime, long deffenceBonusTime){
		write_init();
		write_teamId(teamId);
		write_attackBonusTime(attackBonusTime);
		write_deffenceBonusTime(deffenceBonusTime);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(TEAM_NOTI);
	}
	
	void write_teamId(int teamId) {
		writeRaw(0x08);// teamId
		writeBit(teamId);
	}
	
	void write_attackBonusTime(long attackBonusTime) {
		writeRaw(0x10);// attackBonusTime
		writeBit(attackBonusTime);
	}
	
	void write_deffenceBonusTime(long deffenceBonusTime) {
		writeRaw(0x18);// deffenceBonusTime
		writeBit(deffenceBonusTime);
	}
	
	public S_InterGuardianTowerWarOccupyTeam(long time){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(TEAM_TIME);
		writeRaw(0x08);
		writeBit(time);
        writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_INTER_GUARDIAN_TOWER_WAR_OCCUPY_TEAM;
	}
}

