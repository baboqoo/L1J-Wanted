package l1j.server.LFCSystem;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;

public abstract class InstanceObject implements Runnable{

	protected L1Map 			_copyMap;
	protected InstanceType 	_type;
	
	public InstanceObject(){
		_copyMap	= null;
	}
	
	public abstract void init();
	
	public abstract void run();
	
	public abstract void close();
	
	public abstract void closeForGM();
	
	public abstract void dispose();
	
	public abstract String getName();
	
	public abstract void notifySizeOver();
	
	public InstanceType getType(){
		return _type;
	}
	public void setType(InstanceType type){
		_type = type;
	}
	
	public int getBaseMapId(){
		if (_type == null)
			return -1;
		return _type.getBaseMapId();
	}
	public void setCopyMap(L1Map m){
		_copyMap = m;
	}
	public L1Map getCopyMap(){
		return _copyMap;
	}
	public int getCopyMapId(){
		if (_copyMap == null)
			return -1;
		return _copyMap.getId();
	}
	
	public int getMarkStatus(L1PcInstance pc){
		return 9;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(128);
		sb.append("baseMapId : ").append(getBaseMapId()).append("\n");
		sb.append("copyMapId : ").append(getCopyMapId()).append("\n");
		return sb.toString();
	}
}

