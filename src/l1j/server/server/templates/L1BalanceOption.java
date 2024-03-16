package l1j.server.server.templates;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.construct.L1CharacterInfo.L1Class;

public class L1BalanceOption {
	private int attackerType, targetType;
	private int physicalDmg, physicalHit, physicalReduc;
	private int magicDmg, magicHit, magicReduc;
	
	public L1BalanceOption(ResultSet rs) throws SQLException {
		this.attackerType	= L1Class.getType(rs.getString("attackerType"));
		this.targetType		= L1Class.getType(rs.getString("targetType"));
		this.physicalDmg	= rs.getInt("physicalDmg");
		this.physicalHit	= rs.getInt("physicalHit");
		this.physicalReduc	= rs.getInt("physicalReduction");
		this.magicDmg		= rs.getInt("magicDmg");
		this.magicHit		= rs.getInt("magicHit");
		this.magicReduc		= rs.getInt("magicReduction");
	}
	
	public int getAttackerType() {
		return attackerType;
	}
	public int getTargetType() {
		return targetType;
	}
	public int getPhysicalDmg() {
		return physicalDmg;
	}
	public int getPhysicalHit() {
		return physicalHit;
	}
	public int getPhysicalReduc() {
		return physicalReduc;
	}
	public int getMagicDmg() {
		return magicDmg;
	}
	public int getMagicHit() {
		return magicHit;
	}
	public int getMagicReduc() {
		return magicReduc;
	}
}

