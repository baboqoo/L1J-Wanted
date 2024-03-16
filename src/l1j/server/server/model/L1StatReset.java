package l1j.server.server.model;

public class L1StatReset {
	private int _nowLevel, _endLevel, _maxHp, _maxMp, _ac, _str, _con, _dex, _cha, _intel, _wis, _basestr, _basecon, _basedex, _basecha, _baseintel, _basewis;
	
	public int getNowLevel() {return _nowLevel;}
	public void setNowLevel(int i) {_nowLevel = i;}
	public void addNowLevel(){_nowLevel++;}
	
	public int getEndLevel() {return _endLevel;}
	public void setEndLevel(int i) {_endLevel = i;}
	
	public int getMaxHp() {return _maxHp;}
	public void setMaxHp(int i) {_maxHp = i;}
	
	public int getMaxMp() {return _maxMp;}
	public void setMaxMp(int i) {_maxMp = i;}
	
	public int getAC() {return _ac;}
	public void setAC(int i) {_ac = i;}
	
	public int getStr() {return _str;}
	public void setStr(int i) {_str = i;}
	public void addStr(){_str++;}
	
	public int getCon() {return _con;}
	public void setCon(int i) {_con = i;}
	public void addCon(){_con++;}
	
	public int getDex() {return _dex;}
	public void setDex(int i) {_dex = i;}
	public void addDex(){_dex++;}
	
	public int getCha() {return _cha;}
	public void setCha(int i) {_cha = i;}
	public void addCha(){_cha++;}
	
	public int getIntel() {return _intel;}
	public void setIntel(int i) {_intel = i;}
	public void addIntel(){_intel++;}
	
	public int getWis() {return _wis;}
	public void setWis(int i) {_wis = i;}
	public void addWis(){_wis++;}
	
	public int getBaseStr() {return _basestr;}
	public void setBaseStr(int i) {_basestr = i;}
	
	public int getBaseCon() {return _basecon;}
	public void setBaseCon(int i) {_basecon = i;}
	
	public int getBaseDex() {return _basedex;}
	public void setBaseDex(int i) {_basedex = i;}
	
	public int getBaseCha() {return _basecha;}
	public void setBaseCha(int i) {_basecha = i;}
	
	public int getBaseIntel() {return _baseintel;}
	public void setBaseIntel(int i) {_baseintel = i;}
	
	public int getBaseWis() {return _basewis;}
	public void setBaseWis(int i) {_basewis = i;}
	
	public int getBaseStatusAmount(){return _basestr + _basecon + _basedex + _basecha + _baseintel + _basewis;}
	public int getStatusAmount(){return _str + _con + _dex + _cha + _intel + _wis;}
}

