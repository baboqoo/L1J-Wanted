package l1j.server.GameSystem.charactertrade.bean;

import l1j.server.server.serverpackets.S_CharacterTrade;

public class CharacterTradeObject {
	private int _marbleId;
	private int _charId;
	private String _charname;
	private S_CharacterTrade _invPck;
	private S_CharacterTrade _spPck;
	private S_CharacterTrade _infoPck;
	
	public CharacterTradeObject(int marbleId, int charId, String charname){
		_marbleId	= marbleId;
		_charId		= charId;
		_charname	= charname;
	}
	
	public int getMarbleId(){
		return _marbleId;
	}
	
	public void setMarbleId(int marbleId){
		_marbleId = marbleId;
	}
	
	public int getCharId(){
		return _charId;
	}
	
	public void setCharId(int charId){
		_charId = charId;
	}
	
	public String getCharName(){
		return _charname;
	}
	
	public void setCharName(String charname){
		_charname = charname;
	}
	
	public S_CharacterTrade getInvenPck(){
		return _invPck;
	}
	
	public void setInvenPck(S_CharacterTrade invPck){
		_invPck = invPck;
	}
	
	public S_CharacterTrade getSpellPck(){
		return _spPck;
	}
	
	public void setSpellPck(S_CharacterTrade spPck){
		_spPck = spPck;
	}
	
	public S_CharacterTrade getCharPck(){
		return _infoPck;
	}
	
	public void setCharPck(S_CharacterTrade infoPck){
		_infoPck = infoPck;
	}
	
	public void dispose(){
		if (_invPck != null) {
			_invPck.clear();
		}
		if (_spPck != null) {
			_spPck.clear();
		}
		if (_infoPck != null) {
			_infoPck.clear();
		}
	}
}

