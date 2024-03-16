package l1j.server.IndunSystem.treasureisland;

import java.util.ArrayList;
import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.common.bin.TreasureBoxCommonBinLoader;
import l1j.server.common.bin.treasureisland.TreasureIslandBox;
import l1j.server.server.GameServerSetting;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TreasureInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.treasure.S_LegendaryBoxSpawnPointNoti;
import l1j.server.server.serverpackets.treasure.S_LegendaryBoxSpawnPositionNoti;
import l1j.server.server.serverpackets.treasure.S_ReducedExcavationTimeNoti;
import l1j.server.server.serverpackets.treasure.S_LegendaryBoxHasExcavatedNoti;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 만월의 보물섬(인터서버)
 * @author LinOffice
 */
public class TreasureIsland {
	private static class newInstance {
		public static final TreasureIsland INSTANCE = new TreasureIsland();
	}
	public static TreasureIsland getInstance(){
		return newInstance.INSTANCE;
	}
	
	private boolean _active;
	private TreasureIslandTimer _timer;
	private int _currentGauge;
	private final int _maxGauge;
	private final long _playTime;
	protected final short _playMap;
	
	private TreasureIsland(){
		_currentGauge	= 0;
		_maxGauge		= 1500;
		_playTime		= Config.DUNGEON.TREASURE_ISLAND_DURATION * 60000L;
		_playMap		= L1TownLocation.GETBACK_MAP_TREASURE_ISLAND;
	}
	
	public boolean isActive(){
		return _active;
	}
	
	/**
	 * 보물섬 입장
	 * @param pc
	 */
	public void enter(L1PcInstance pc){
		L1PolyMorph.doPolyTreasure(pc);// 고고학자 변신
		if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.TREASURE_SHOVEL), 1) != L1Inventory.OK) return;
		L1ItemInstance item = ItemTable.getInstance().createItem(L1ItemId.TREASURE_SHOVEL);// 보물 탐지 삽
		item.setCount(1);
		item.setIdentified(true);		
		pc.getInventory().storeItem(item);
		pc.sendPackets(new S_ServerMessage(143, "$36722", item.getItem().getDesc()), true); // $36722 = Treasure Island
		pc.sendPackets(new S_LegendaryBoxSpawnPointNoti(_currentGauge, _maxGauge, false), true);// 게이지
		pc.sendPackets(pc.getInventory().checkItem(L1ItemId.TREASURE_COMPASS) ? S_ReducedExcavationTimeNoti.MULTIPLE : S_ReducedExcavationTimeNoti.DEFAULT);// 발굴 속도 설정
	}
	
	/**
	 * 보물섬 탈출
	 * @param pc
	 */
	public void leave(L1PcInstance pc){
		L1PolyMorph.undoPolySimple(pc);// 고고학자 변신 해제
		pc.getInventory().consumeItem(L1ItemId.TREASURE_SHOVEL);// 보물 탐지 삽 제거
	}
	
	/**
	 * 보물 발굴
	 * @param pc
	 * @param treasure
	 */
	public void excavationTreasure(L1PcInstance pc, L1TreasureInstance treasure){
		TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT boxT = TreasureBoxCommonBinLoader.getBox(treasure.getNpcTemplate().getDescKr());
		if (boxT == null) {
			return;
		}
		ArrayList<L1PcInstance> mapPcList = L1World.getInstance().getMapPlayer(_playMap);
		if (boxT.get_grade() == TreasureIslandBox.TreasureBoxGrade.Legendary) {// 에바의 보물
			S_LegendaryBoxHasExcavatedNoti evaDigMessage = new S_LegendaryBoxHasExcavatedNoti(pc.getX(), pc.getY(), pc.getName());// 에바 발굴 메세지
			for (L1PcInstance player : mapPcList) {
				if (player == null) {
					continue;
				}
				player.sendPackets(evaDigMessage);
			}
			evaDigMessage.clear();
			evaDigMessage = null;
		}
		_currentGauge += boxT.get_excavateTime();
		S_LegendaryBoxSpawnPointNoti gauge = new S_LegendaryBoxSpawnPointNoti(_currentGauge > _maxGauge ? _maxGauge : _currentGauge, _maxGauge, false);// 게이지
		S_LegendaryBoxSpawnPositionNoti legendaryBoxSpawnPosition = null;
		if (_currentGauge >= _maxGauge) {
			_currentGauge = 0;
			legendaryBoxSpawnPosition = getLegendaryBoxSpawnPosition();
		}
		for (L1PcInstance player : mapPcList) {
			if (player == null) {
				continue;
			}
			player.sendPackets(gauge);
			if (legendaryBoxSpawnPosition != null) {
				player.sendPackets(legendaryBoxSpawnPosition);
			}
		}
		TreasureIslandUtil.excavateReward(pc, TreasureBoxCommonBinLoader.getReward(boxT.get_grade()));
		if (!TreasureIslandUtil.EXCAVATION_USERS.contains(pc)) {
			TreasureIslandUtil.EXCAVATION_USERS.add(pc);
		}
		gauge.clear();
		gauge = null;
		mapPcList.clear();
		mapPcList = null;
		if (legendaryBoxSpawnPosition != null) {
			legendaryBoxSpawnPosition.clear();
			legendaryBoxSpawnPosition = null;
		}
	}
	
	/**
	 * 에바의 보물 스폰
	 * @return S_LegendaryBoxSpawnPositionNoti
	 */
	private S_LegendaryBoxSpawnPositionNoti getLegendaryBoxSpawnPosition(){
		int[] evaLoc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_TREASURE_ISLAND);
		L1NpcInstance evaTreasure = L1SpawnUtil.spawn(evaLoc[0], evaLoc[1], _playMap, 0, 18305, 400, 1800);// 에바의 보물 스폰
		return new S_LegendaryBoxSpawnPositionNoti(evaTreasure.getX(), evaTreasure.getY());
	}
	
	private static final String COMMAND_START	= "start";
	private static final String COMMAND_END		= "stop";
	private static final String COMMAND_RELOAD	= "reload";
	
	public void command(L1PcInstance pc, String param){
		try {
			StringTokenizer tok	= new StringTokenizer(param);
			String onoff		= tok.nextToken();
			switch(onoff){
			case COMMAND_START:	
				commandStart(pc);break;
			case COMMAND_END:	
				commandEnd(pc);break;
			case COMMAND_RELOAD:
				commandReload(pc);break;
			default:pc.sendPackets(TreasureIslandUtil.COMMAND_MESSAGE);break;
			}
		} catch (Exception e) {
			pc.sendPackets(TreasureIslandUtil.COMMAND_MESSAGE);
		}
	}
	
	private void commandStart(L1PcInstance pc){
		if (_active) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("만월의 보물섬이 이미 가동중입니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(50), true), true);
			return;
		}
		start();
	}
	
	private void commandEnd(L1PcInstance pc){
		if (!_active) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("만월의 보물섬이 이미 종료되어 있습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(51), true), true);
			return;
		}
		end();
	}
	
	private void commandReload(L1PcInstance pc){
		if (_active) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("만월의 보물섬이 가동중에는 리로드할 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(52), true), true);
			return;
		}
		TreasureBoxCommonBinLoader.reload();
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("보물정보가 리로드 되었습니다."), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(53), true), true);
	}
	
	public void start(){
		L1World.getInstance().broadcastPacketToAll(TreasureIslandUtil.TREASURE_OPEN);// 하이네 마을에 달의 기사 질리언이 보물섬으로 향하는 통로를 개방했습니다.
		_currentGauge	= 0;
		_active			= GameServerSetting.TREASURE_ISLAND = true;
		_timer			= new TreasureIslandTimer(_playMap);
		GeneralThreadPool.getInstance().schedule(_timer, _playTime);// 30분간 진행
		System.out.println(String.format("■■■■■■■■■■ Full Moon Treasure Island begins ■■■■■■■■■■ mapId - %d", _playMap));
	}
	
	public void end(){
		_currentGauge	= 0;
		_active			= GameServerSetting.TREASURE_ISLAND = false;
		if (_timer != null) {// 타이머 제거
			_timer.cancel();
			_timer		= null;	
		}
		TreasureIslandUtil.EXCAVATION_USERS.clear();
		ArrayList<L1PcInstance> mapPcList = L1World.getInstance().getMapPlayer(_playMap);
		for (L1PcInstance pc : mapPcList) {
			if (pc == null) {
				continue;
			}
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_HEINE);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], 5, true, true);
		}
		mapPcList.clear();
		mapPcList = null;
		System.out.println(String.format("■■■■■■■■■■ Full Moon's Treasure Island ends ■■■■■■■■■■ mapId - %d", _playMap));
	}
}


