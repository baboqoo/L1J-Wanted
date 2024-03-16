package l1j.server.LFCSystem;

public class InstanceType {
	protected int 		_id;
	protected String	_name;
	protected String	_playInstName;
	protected int		_possibleLevel;
	private short 		_baseMapId;
	
	public int getId(){
		return _id;
	}
	public void setId(int i){
		_id = i;
	}
	
	public String getName(){
		return _name;
	}
	public void setName(String s){
		_name = s;
	}
	
	public String getPlayInstName(){
		return _playInstName;
	}
	public void setPlayInstName(String s){
		_playInstName = s;
	}
	
	public int getPossibleLevel(){
		return _possibleLevel;
	}
	public void setPossibleLevel(int i){
		_possibleLevel = i;
	}
	
	public short getBaseMapId(){
		return _baseMapId;
	}
	public void setBaseMapId(short s){
		_baseMapId = s;
	}
}

