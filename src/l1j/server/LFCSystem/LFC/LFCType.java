package l1j.server.LFCSystem.LFC;

import java.util.ArrayList;

import l1j.server.LFCSystem.InstanceType;
import l1j.server.LFCSystem.LFC.Compensate.LFCCompensate;
import l1j.server.LFCSystem.Util.Rectangle;
import l1j.server.server.model.Instance.L1PcInstance;

public class LFCType extends InstanceType{
	private boolean 	_isPvp;
	private boolean 	_isUse;
	private int 		_buffSpawnSecond;
	private int			_needItemId;
	private int			_needItemCount;
	
	private int			_minParty;
	private int			_maxParty;
	private Rectangle _rtMap;
	private int 		_readySecond;

	public boolean isPvp(){
		return _isPvp;
	}
	public void setPvp(boolean b){
		_isPvp = b;
	}
	public void setPvp(int i){
		setPvp(i == 0);
	}
	
	public boolean isUse(){
		return _isUse;
	}
	public void setUse(boolean b){
		_isUse = b;
	}
	public void setUse(int i){
		setUse(i != 0);
	}
	
	public int getBuffSpawnSecond(){
		return _buffSpawnSecond;
	}
	public void setBuffSpawnSecond(int i){
		_buffSpawnSecond = i;
	}
	
	public int getNeedItemId(){
		return _needItemId;
	}
	public void setNeedItemId(int i){
		_needItemId = i;
	}
	
	public int getNeedItemCount(){
		return _needItemCount;
	}
	public void setNeedItemCount(int i){
		_needItemCount = i;
	}
	
	public int getMinParty(){
		return _minParty;
	}
	public void setMinParty(int i){
		_minParty = i;
	}
	
	public int getMaxParty(){
		return _maxParty;
	}
	public void setMaxParty(int i){
		_maxParty = i;
	}
	
	public void setMapRect(int left, int top, int right, int bottom){
		_rtMap = new Rectangle(left, top, right, bottom);
	}
	public void setMapRect(Rectangle rt){
		_rtMap = rt;
	}
	public Rectangle getMapRect(){
		return _rtMap;
	}
	
	private Rectangle _rtStartup;
	public void setStartupPosition(Rectangle rt){
		_rtStartup = rt;
	}
	public void setStartupPosition(int left, int top, int right, int bottom){
		_rtStartup = new Rectangle(left, top, right, bottom);
	}
	public Rectangle getStartupPosition(){
		return _rtStartup;
	}
	public int getRedXStartup(){
		return _rtStartup.getLeft();
	}
	public int getRedYStartup(){
		return _rtStartup.getTop();
	}
	public int getBlueXStartup(){
		return _rtStartup.getRight();
	}
	public int getBlueYStartup(){
		return _rtStartup.getBottom();
	}
	
	private int _playSecond;
	public void setPlaySecond(int i){
		_playSecond = i;
	}
	public int getPlaySecond(){
		return _playSecond;
	}
	
	public void setReadySecond(int i){
		_readySecond = i;
	}
	public int getReadySecond(){
		return _readySecond;
	}
	
	/** random compensate ratio. int type. **/
	private int _rndCompensateRatio;
	public void setRandomCompensateRatio(int i){
		_rndCompensateRatio = i;
	}
	public int getRandomCompensateRatio(){
		return _rndCompensateRatio;
	}
	
	private static final int PARTITION_WINNER 	= 1;
	private static final int PARTITION_LOSER 	= 2;
	private static final int PARTITION_RANDOM 	= 8;
	private ArrayList<LFCCompensate> _compensates;
	
	public void addCompensates(LFCCompensate compensate){
		_compensates.add(compensate);
	}
	
	private void compensate(L1PcInstance pc, int partition){
		LFCCompensate compen 	= null;
		int 			size	= _compensates.size();
		for (int i = 0; i < size; i++){
			compen = _compensates.get(i);
			if ((compen.getPartition() & partition) > 0)
				compen.compensate(pc);
		}
	}
	
	public void winnerCompensate(L1PcInstance pc){
		compensate(pc, PARTITION_WINNER);
	}
	
	public void loserCompensate(L1PcInstance pc){
		compensate(pc, PARTITION_LOSER);
	}
	
	public void randomCompensate(L1PcInstance pc){
		compensate(pc, PARTITION_RANDOM);
	}
	
	public LFCType(){
		_compensates = new ArrayList<LFCCompensate>();
	}
}

