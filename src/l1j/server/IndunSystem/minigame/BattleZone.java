package l1j.server.IndunSystem.minigame;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.Announcements;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.StringUtil;

public class BattleZone implements Runnable {
	private static final int BuffBall[] = { 101017, 101018, 101019, 101020, 101021, 101022 };

	private static BattleZone _instance;

	private boolean _battleZoneStarted = false;

	public boolean getBattleZoneStarted() {
		return _battleZoneStarted;
	}

	public void setBattleZoneStarted(boolean battleZone) {
		_battleZoneStarted = battleZone;
	}

	private boolean _entryStatus = false;

	public boolean getEntryStatus() {
		return _entryStatus;
	}

	public void setEntryStatus(boolean entryStatus) {
		_entryStatus = entryStatus;
	}

	private boolean _battleInProgress = false;
	
	public boolean getBattleInProgress() {
		return _battleInProgress;
	}

	public void setBattleInProgress(boolean battle) {
		_battleInProgress = battle;
	}
	
	private boolean _battleZoneEnded = false;

	public boolean getBattleZoneEnded() {
		return _battleZoneEnded;
	}

	public void setBattleZoneEnded(boolean battleEnd) {
		_battleZoneEnded = battleEnd;
	}

	private int enddueltime;

	private boolean Close = false;
	 
	protected ArrayList<L1PcInstance> battleZoneUsers = new ArrayList<L1PcInstance>();
	public void addBattleZoneUser(L1PcInstance pc) 	{
		battleZoneUsers.add(pc);
	}
	public void removeBattleZoneUser(L1PcInstance pc) 	{
		battleZoneUsers.remove(pc); 
	}
	public void clearBattleZoneUser() 					{ 
		battleZoneUsers.clear();	  
	}
	public boolean isBattleZoneUser(L1PcInstance pc) 	{ 
		return battleZoneUsers.contains(pc); 	
	} 
	public int getBattleZoneUserCount(){ 
		return battleZoneUsers.size();	
	}
	
	private boolean GmStart;
	public void setGmStart(boolean ck){
		GmStart = ck;
	}
	public boolean getGmStart(){
		return GmStart;
	}
	
	public L1PcInstance[] toArrayBattleZoneUser() {
		return battleZoneUsers.toArray(new L1PcInstance[battleZoneUsers.size()]);
	}
	public static BattleZone getInstance() {
		if (_instance == null)
			_instance = new BattleZone();
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				try {
					if (_battleZoneEnded){
						Thread.sleep(1000 * 60 * Config.DUNGEON.BATTLE_ZONE_STAY_MINUT); //대기시간
						_battleZoneEnded = false;
					} else {
					    check(); // 듀얼 가능시간을 체크
					    if (_battleInProgress)
							checkUser();
					    Thread.sleep(1000);
					}
				} catch (Exception e) {}
			}
		} catch (Exception e1) {
		}
	}

	private void checkUser() {
		L1PcInstance[] pc = toArrayBattleZoneUser();
		for (int i = 0; i < pc.length; i++) {
			if (pc[i] == null)
				continue;
			if (pc[i].getMapId() == 5153)
				continue;
			else {
				if (isBattleZoneUser(pc[i]))
				removeBattleZoneUser(pc[i]);
				pc[i].getConfig().setDuelLine(0);
			}
		}
	}

	// 시간체크
	public void check() {
		//게임시간을 받아온다.
		try{
			long servertime = RealTimeClock.getInstance().getRealTime().getSeconds();
			//현재시간
			long nowdueltime = servertime % 86400;
			int count1 = 0, count2 = 0, winLine = 4;
			
			if (_battleZoneStarted == false){
				if (GmStart){
					_battleZoneStarted = _entryStatus = true;
					
//AUTO SRM: 					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[공지] 배틀존이 열렸습니다."), true); // CHECKED OK
					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(38)), true);
//AUTO SRM: 					L1World.getInstance().broadcastServerMessage("\\aH............ 3분후 배틀존이 진행됩니다. ............", true); // CHECKED OK
					L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(37), true);
					
					try {
						Thread.sleep(1000 * 120);
					} catch (Exception e) {
					}
					//Announcements.getInstance().announceToAll("배틀존 입장 마감 1분 남았습니다.");
					Announcements.getInstance().announceToAll(S_SystemMessage.getRefTextNS(85));
					
					try {
						Thread.sleep(1000 * 50);
					} catch (Exception e) {
					}
					//Announcements.getInstance().announceToAll("배틀존 입장 마감 10초 남았습니다.");
					Announcements.getInstance().announceToAll(S_SystemMessage.getRefTextNS(86));
					
					try {
						Thread.sleep(1000 * 10);
					} catch (Exception e) {
					}
					
					_entryStatus = false;
					
					//Announcements.getInstance().announceToAll("배틀존 입장을 마감하였습니다.");
					Announcements.getInstance().announceToAll(S_SystemMessage.getRefTextNS(87));
					try {
						Thread.sleep(1000 * 5);
					} catch (Exception e) {
					}
					
					L1PcInstance[] c = toArrayBattleZoneUser();
					for (int i = 0; i < c.length; i++) {
						if (c[i].getMapId() == 5153){
							if (!c[i].isDead()){
								createMiniHp(c[i]);	// 피바생성	
								c[i].sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 2), true); // 진동
							}
//AUTO SRM: 							c[i].sendPackets(new S_SystemMessage("배틀존이 시작되엇습니다."), true); // CHECKED OK
							c[i].sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(34), true), true);
						}
					}
					L1DoorInstance door = null;
					for (L1Object object : L1World.getInstance().getObject()) {
						if (object instanceof L1DoorInstance) {
							door = (L1DoorInstance) object;
							if (door.getDoorId() >= 5000 && door.getDoorId() <= 5015)
								door.open(); //경계 해제
						}
					}					
					
					//끝나는 시간지정
					enddueltime = (int) nowdueltime + 600; //10분후종료종료시간 정하는곳
					_battleInProgress = true;
				}
			} else {

				//종료시간이거나 강제종료라면
				if (nowdueltime >= enddueltime || Close == true){
					L1PcInstance[] c1 = toArrayBattleZoneUser();
					for (int i = 0; i < c1.length; i++) {
						if (c1[i].getMapId() == 5153){
							if (!c1[i].isDead()){
								if (c1[i].getConfig().getDuelLine() == 1)
									count1 += 1;
								else
									count2 += 1;
							}
						}
					}
					//우승체크
					String ment = null;
					if (count1 > count2){
						//1번라인 우승
						winLine = 1;
						//ment = "배틀존 '블루' 라인의 승리입니다.";
						ment = S_SystemMessage.getRefTextNS(88);
					} else if (count1 < count2){
						//2번라인 우승
						winLine = 2;
						//ment = "배틀존 '레드' 라인의 승리입니다.";
						ment = S_SystemMessage.getRefTextNS(81);
					} else {
						winLine = 3;
						//ment = "배틀존 '블루' 라인과 '레드' 라인이 비겼습니다.";
						ment = S_SystemMessage.getRefTextNS(71);
					}

					L1PcInstance[] c2 = toArrayBattleZoneUser();
					for (int i = 0; i < c2.length; i++) {  
						if (c2[i] == null) continue;
						if (c2[i].getConfig().getDuelLine() != 0){
							c2[i].sendPackets(new S_SystemMessage(ment, true), true);
							//이긴 라인에게 아이템지급
							if (c2[i].getConfig().getDuelLine() == winLine){
						    	String[] itemIds = null;
							        try{
							 			int idx = Config.DUNGEON.BATTLE_ZONE_REWARD_ITEMID.indexOf(StringUtil.CommaString);
							 			// ,로 있을경우
							 			if (idx > -1)
							 				itemIds = Config.DUNGEON.BATTLE_ZONE_REWARD_ITEMID.split(StringUtil.CommaString);
							 			else {
							 				itemIds = new String[1];
							 				itemIds[0] = Config.DUNGEON.BATTLE_ZONE_REWARD_ITEMID;
							 			}
							 		} catch(Exception e){}
							 		// 지급할 아이템 갯수
							 		String[] counts = null;
							 		try{
							 			int idx = Config.DUNGEON.BATTLE_ZONE_REWARD_COUNT.indexOf(StringUtil.CommaString);
							 			// ,로 있을경우
							 			if (idx > -1)
							 				counts = Config.DUNGEON.BATTLE_ZONE_REWARD_COUNT.split(StringUtil.CommaString);
							 			else {
							 				counts = new String[1];
							 				counts[0] = Config.DUNGEON.BATTLE_ZONE_REWARD_COUNT;
							 			}
							 		}catch(Exception e){}
							 		// 아이템 아이디나 카운트가 없을경우
							 		if (itemIds == null || counts == null)
							 			return;
							 		for (int j = 0; j < itemIds.length; j++) {
							 			int itemId = 0;
							 			int count = 0;
							 			itemId = Integer.parseInt(itemIds[j]);
							 			count = Integer.parseInt(counts[j]);
							 			if (itemId <= 0 || count <= 0)
							 				continue;
										if (c2[i].getInventory().checkAddItem(ItemTable.getInstance().getTemplate(itemId), count) != L1Inventory.OK) return;
							 			L1ItemInstance item = c2[i].getInventory().storeItem(itemId, count);
							 			if (item != null)
											//c2[i].sendPackets(new S_SystemMessage(item.getItem().getDesc() + " (" + count + ")을 얻었습니다."), true);
											c2[i].sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(117), item.getItem().getDesc(), String.valueOf(count)), true);
							 		}
//AUTO SRM: 							        c2[i].sendPackets(new S_SystemMessage("\\fU* 승리팀에게 아이템이 지급되었습니다 *"), true); // CHECKED OK
							        c2[i].sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(36), true), true);
							    }
						
							deleteMiniHp(c2[i]); //피바삭제
							c2[i].getConfig().setDuelLine(0); //편나누기 리셋
							//배틀존이라면
							if (c2[i].getMapId() == 5153){
								if (!c2[i].isDead()){
									int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
									c2[i].getTeleport().start(loc[0], loc[1], (short) loc[2], c2[i].getMoveState().getHeading(), true);
								}
							}
						}
					}
					L1DoorInstance door = null;
				    for (L1Object object : L1World.getInstance().getObject()) {
					    if (object instanceof L1DoorInstance) {
						    door = (L1DoorInstance) object;
						    if (door.getDoorId() >= 5000 && door.getDoorId() <= 5015)
							    GeneralThreadPool.getInstance().schedule(door.new DoorTimer(), 1 * 1000); //경계 스폰
					    }
				    }
					ment = null;
					//Announcements.getInstance().announceToAll("\\fW* 배틀존이 종료되었습니다 *");
					Announcements.getInstance().announceToAll(S_SystemMessage.getRefTextNS(72));
					
					_battleZoneEnded = true;
					_battleInProgress = _battleZoneStarted = Close = false;
					battleZoneUsers.clear();
				} else {
					//버프구슬 스폰
					if (nowdueltime == enddueltime - 540 
							|| nowdueltime == enddueltime - 420
							|| nowdueltime == enddueltime - 300
							|| nowdueltime == enddueltime - 180){
						L1PcInstance[] c = toArrayBattleZoneUser();
						for (int i = 0; i < c.length; i++) {
							if (c[i].getMapId() == 5153){
								if (!c[i].isDead())
									c[i].sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 6), true); // 이팩트
							}
						}
						for (int i = 0; i < 10; i++) {
							L1SpawnUtil.spawn2(32638, 32898, (short) 5153, 5, BuffBall[CommonUtil.random(BuffBall.length)], 20, 30 * 1000, 0);
						}
					}
					//대미지 불 스폰
					if (nowdueltime == enddueltime - 480
							|| nowdueltime == enddueltime - 360
							|| nowdueltime == enddueltime - 240
							|| nowdueltime == enddueltime - 200
							|| nowdueltime == enddueltime - 120
							|| nowdueltime == enddueltime - 60){
						L1PcInstance[] c = toArrayBattleZoneUser();
						for (int i = 0; i < c.length; i++) {
							if (c[i].getMapId() == 5153){
								if (!c[i].isDead())
									c[i].sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 7), true); // 이팩트
							}
						}
						for (int i = 0; i < 4; i++) {
							L1SpawnUtil.spawn2(32638, 32898, (short) 5153, 5, 100587, 20, 30 * 1000, 0);
						}
					}
					//입장이 마감되었다면
					if (!_entryStatus){
						int count3 = 0;
						int count4 = 0;
						L1PcInstance[] c3 = toArrayBattleZoneUser();
						for (int i = 0; i < c3.length; i++) {
							if (c3[i] == null)
								continue;
							//배틀존이라면
							if (c3[i].getMapId() == 5153){
								if (!c3[i].isDead()){//죽지않은 유저 체크
									if (c3[i].getConfig().getDuelLine() == 1)
										count3 += 1;
									else if (c3[i].getConfig().getDuelLine() == 2)
										count4 += 1;
									else
									removeBattleZoneUser(c3[i]);
								}
							}
						}

						// 남은유저가 0명일때 강제종료실행
						if (count3 == 0 || count4 == 0) {
							Close = true;
						}
					}

				}

			}
		} catch(Exception e){}
	}
	
	private void createMiniHp(L1PcInstance pc) {
		// 배틀시, 서로 HP를 표시시킨다
		for (L1PcInstance member : BattleZone.getInstance().toArrayBattleZoneUser()) {
			// 같은라인에게 hp표시
			if (member != null) {
				if (pc.getConfig().getDuelLine() == member.getConfig().getDuelLine()) {
					member.sendPackets(new S_HPMeter(pc), true);
					pc.sendPackets(new S_HPMeter(member), true);
				}
			}
		}
	}
	
	private void deleteMiniHp(L1PcInstance pc) {
		// 배틀종료시, HP바를 삭제한다.
		for (L1PcInstance member : pc.getKnownPlayers()){
			//같은라인에게 hp표시
			if (member != null){
				if (pc.getConfig().getDuelLine() == member.getConfig().getDuelLine()){
					pc.sendPackets(new S_HPMeter(member.getId(), 0xff, 0xff), true);
					member.sendPackets(new S_HPMeter(pc.getId(), 0xff, 0xff), true);
				}
			}
		}
	}

}


