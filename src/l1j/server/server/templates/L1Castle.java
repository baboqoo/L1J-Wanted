package l1j.server.server.templates;

import java.util.Calendar;

import l1j.server.server.construct.L1CastleType;

public class L1Castle {
	public L1Castle(int id, String name) {
		_id		= id;
		_name	= name;
		_castle	= L1CastleType.fromInt(id);
	}

	private int _id;
	public int getId() {
		return _id;
	}

	private String _name;
	public String getName() {
		return _name;
	}

	private Calendar _warTime;
	public Calendar getWarTime() {
		return _warTime;
	}
	public void setWarTime(Calendar i) {
		_warTime = i;
	}

	private int _taxRate;
	public int getTaxRate() {
		return _taxRate;
	}
	public void setTaxRate(int i) {
		_taxRate = i;
	}

	private int _publicMoney;
	public int getPublicMoney() {
		return _publicMoney;
	}
	public void setPublicMoney(int i) {
		_publicMoney = i;
	}

	private int _publicReadyMoney;
	public int getPublicReadyMoney() {
		return _publicReadyMoney;
	}
	public void setPublicReadyMoney(int i) {
		_publicReadyMoney = i;
	}

	private int _publicPlusMoney;
	public int get_publicPlusMoney() {
		return _publicPlusMoney;
	}
	public void set_publicPlusMoney(int i) {
		_publicPlusMoney = i;
	}

	private int _publicPlus_D_Money;
	public int get_publicPlus_D_Money() {
		return _publicPlus_D_Money;
	}
	public void set_publicPlus_D_Money(int i) {
		_publicPlus_D_Money = i;
	}

	private int _showMoney;
	public int getShowMoney() {
		return _showMoney;
	}
	public void setShowMoney(int i) {
		_showMoney = i;
	}

	private int _warBase;
	public int getWarBaseTime() {
		return _warBase;
	}
	public void setWarBaseTime(int i) {
		_warBase = i;
	}

	private int _security;
	public int getCastleSecurity() {
		return _security;
	}
	public void setCastleSecurity(int i) {
		_security = i;
	}
	
	private L1CastleType _castle;
	public L1CastleType getCastle(){
		return _castle;
	}
}

