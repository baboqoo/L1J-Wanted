package l1j.server.IndunSystem.clandungeon;

public class ClanDungeonObject {
	public int _type, _stage, _npcId, _count;
	public boolean _boss;
	
	public ClanDungeonObject(int type, int stage, int npcId, int count, boolean boss){
		_type	= type;
		_stage	= stage;
		_npcId	= npcId;
		_count	= count;
		_boss	= boss;
	}
}

