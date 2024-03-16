package l1j.server.server.model.Instance;

import java.text.SimpleDateFormat;

import l1j.server.IndunSystem.clandungeon.ClanDungeonCreator;
import l1j.server.IndunSystem.clandungeon.ClanDungeonHandler;
import l1j.server.IndunSystem.clandungeon.ClanDungeonType;
import l1j.server.IndunSystem.dragonraid.DragonRaidCreator;
import l1j.server.IndunSystem.dragonraid.DragonRaidHandler;
import l1j.server.IndunSystem.dragonraid.DragonRaildType;
import l1j.server.IndunSystem.occupy.OccupyHandler;
import l1j.server.IndunSystem.occupy.OccupyManager;
import l1j.server.IndunSystem.occupy.OccupyType;
import l1j.server.IndunSystem.occupy.OccupyUtil;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.controller.CrockController;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1FieldObjectInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("MM-dd HH:mm", java.util.Locale.KOREA);
	
	public L1FieldObjectInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {  }

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int npcid = getNpcTemplate().getNpcId();
		switch(npcid){
		case 200:crockEnter(pc);break;// 시간의 균열
		case 7210011:telValakasRoom(pc);break;// 화룡의 안식처
/****************************************************************************************************************************
*************************************************** 점령전  *******************************************************************	
****************************************************************************************************************************/
		case 800811:occupyBossRoomEnter(pc, L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_HEINE_EVA));break;// 에바의 정원 이동 오브젝트
		case 800827:occupyBossRoomEnter(pc, L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_WINDAWOOD_AZUR));break;// 아주르의 정원 이동 오브젝트
/****************************************************************************************************************************
*************************************************** 혈맹 던전  *******************************************************************	
****************************************************************************************************************************/	
		case 20900:clanDungen(pc, ClanDungeonType.DAILY);break;
		case 20901:clanDungen(pc, ClanDungeonType.WEEKLY);break;
		case 20902:clanDungen(pc, ClanDungeonType.AREA);break;
		case 20950:pc.getDungoenTimer().enterDragon(this);break;
/****************************************************************************************************************************
 *************************************************** 안타라스  *******************************************************************	
 ****************************************************************************************************************************/
		case 900007:dragonRaidPortalEnter(pc, DragonRaildType.ANTARAS);break;// 드래곤 포탈[안타라스] => 1번방
		case 810851:pc.getTeleport().start(32671, 32672, pc.getMapId(), pc.getMoveState().getHeading(), true);break;// 1번방 => 안타라스 대기방
		case 900008:dragonRaidEnter(pc, DragonRaildType.ANTARAS);break;// [안타라스 대기방] => 안타라스 레어 맵
/****************************************************************************************************************************
*************************************************** 파푸리온  *******************************************************************	
****************************************************************************************************************************/	
		case 900036:dragonRaidPortalEnter(pc, DragonRaildType.FAFURION);break;// 드래곤 포탈[파푸리온] => 대기방
		case 900037:dragonRaidEnter(pc, DragonRaildType.FAFURION);break;// [파푸리온 대기방] => 파푸리온 레어 맵
/****************************************************************************************************************************
 *************************************************** 린드비오르  *******************************************************************	
 ****************************************************************************************************************************/
		case 900219:dragonRaidPortalEnter(pc, DragonRaildType.RINDVIOR);break;// 린드 레이드 포탈 -> 대기방
		case 5101:pc.getTeleport().start(32736, 32847, (short) getMoveMapId(), 5, true);break;// 린드 비오르 직계형 입구
		case 5102:dragonRaidEnter(pc, DragonRaildType.RINDVIOR);break;// 린드비오르 레어 입구
/****************************************************************************************************************************
*************************************************** 발라카스  *******************************************************************	
****************************************************************************************************************************/
		case 7220062:dragonRaidPortalEnter(pc, DragonRaildType.VALAKAS);break;// 발라포탈
		case 7220063:dragonRaidEnter(pc, DragonRaildType.VALAKAS);break;// 발라입구
/****************************************************************************************************************************
*************************************************** 할파스  *******************************************************************	
****************************************************************************************************************************/
		case 900510:halpasPortalEnter(pc);break;// 할파스포탈
		case 900511:halpasEnter(pc);break;// 할파스 은신처
		default:break; 
		}
	}
	
	private void crockEnter(L1PcInstance pc){
		CrockController crock = CrockController.getInstance();
		if (!crock.isTimeCrock()) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("시간의 균열은 현재 닫혀있습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1021), true), true);
			return;
		}
		switch(crock.crocktype()){
		case 0:	pc.getTeleport().start(32639, 32876, (short) 780, 4, false);break;// 테베
		default:pc.getTeleport().start(32794, 32751, (short) 783, 4, false);break;// 티칼
		}
	}
	
	private void occupyBossRoomEnter(L1PcInstance pc, int[] loc){
		if (pc._occupyTeamType == null || pc.getNetConnection() == null) {
			return;
		}
		L1InterServer inter = pc.getNetConnection().getInter();
		if (inter == null || !pc.getInventory().checkItem(OccupyUtil.getBadgeId(pc._occupyTeamType))) {
			return;
		}
		OccupyHandler handler = OccupyManager.getInstance().getHandler(pc.getMapId() == L1TownLocation.MAP_WOLRDWAR_HEINE_TOWER ? OccupyType.HEINE : OccupyType.WINDAWOOD);
		if (handler == null || (handler.isBossStage() && !handler.isWinnerTeam(pc._occupyTeamType))) {// 진입 제한
			return;
		}
		pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);// 이동
	}
	
	private synchronized void clanDungen(L1PcInstance pc, ClanDungeonType type){
		if (pc.getClanid() == 0) {
			return;
		}
		ClanDungeonHandler handler = ClanDungeonCreator.getInstance().getRaid(getMoveMapId());
		if (handler == null) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("이미 종료된 포탈입니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1022), true), true);
			return;
		}
		if (type == ClanDungeonType.DAILY && pc.getInventory().checkItem(6014)) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("맹세의 징표 소지시 입장 불가"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1023), true), true);
			return;
		} else if ((type == ClanDungeonType.WEEKLY || type == ClanDungeonType.AREA) && pc.getInventory().checkItem(6015)) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("맹세의 징표(주간) 소지시 입장 불가"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1024), true), true);
			return;
		}
		if (L1World.getInstance().getMapPlayer(getMoveMapId()).size() >= 50) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("더이상 입장하실 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1025), true), true);
			return;
		}
		if (!handler.getClanName().equals(pc.getClanName())) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s혈맹의 혈맹원이 아닙니다.", handler.getClanName())), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(88), handler.getClanName()), true);
			return;
		}
		if (!handler.isRun()) {
			handler.setRun(true);
			handler.startRaid();
		}
		
		int enterX, enterY;
		switch(type){
		case DAILY:		enterX = 33538;enterY = 32701;break;
		case WEEKLY:	enterX = 33538;enterY = 32701;break;
		case AREA:		enterX = 32747;enterY = 32805;break;
		default:return;
		}
		
		L1Location loc = new L1Location(enterX, enterY, getMoveMapId()).randomLocation(10, true);
		pc.getTeleport().start(loc, 5, true);
		loc = null;
	}
	
	private synchronized void dragonRaidPortalEnter(L1PcInstance pc, DragonRaildType raidType) {
		if (pc.getSkill().hasSkillEffect(L1SkillId.DRAGONRAID_BUFF)) {
			pc.sendPackets(L1ServerMessage.sm1626);
			//pc.sendPackets(new S_SystemMessage(FORMAT.format(pc.getAccount().getDragonRaid()) + " 이후에 입장 가능합니다."), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(124), FORMAT.format(pc.getAccount().getDragonRaid())), true);

			return;
		}
		
		DragonRaidCreator manager = DragonRaidCreator.getInstance();
		DragonRaidHandler handler = manager.getRaid(getMoveMapId());
		if (handler == null) {
			pc.sendPackets(L1ServerMessage.sm7619);// 드래곤 포탈: 입장 불가
			return;
		}
		
		int userCount = handler.countLairUser();
		if (userCount > 0) {
			int checkCount = L1World.getInstance().getMapPlayer(getMoveMapId()).size();
			if (checkCount == 0) {
				handler.clearLairUser();
				handler.setWake(false);
			} else if (userCount >= 16) {
				pc.sendPackets(L1ServerMessage.sm7619);// 드래곤 포탈: 입장 불가
				return;
			}
		}
		
		if (handler.isWake()) {
			pc.sendPackets(L1ServerMessage.sm1537);// 드래곤이 깨서 진입 못한다
			return;
		}
		
		handler.addLairUser(pc);
		
		int enterX, enterY;
		switch(raidType){
		case ANTARAS:	enterX = 32731;enterY = 32929;break;
		case FAFURION:	enterX = 32731;enterY = 32929;break;
		case RINDVIOR:	enterX = 32731;enterY = 32929;break;
		case VALAKAS:	enterX = 32731;enterY = 32929;break;
		default:return;
		}
		pc.getTeleport().start(enterX, enterY, (short) getMoveMapId(), pc.getMoveState().getHeading(), true);
	}
	
	private void dragonRaidEnter(L1PcInstance pc, DragonRaildType raidType){
		DragonRaidHandler handler = DragonRaidCreator.getInstance().getRaid(getMoveMapId());
		if (handler == null) {
			pc.sendPackets(L1ServerMessage.sm7619);// 드래곤 포탈: 입장 불가
			return;
		}
		if (!handler.isRun()) {
			handler.setRun(true);
			handler.raidStart();
		}
		int enterX, enterY;
		switch(raidType){
		case ANTARAS:	enterX = 32796;enterY = 32664;break;
		case FAFURION:	enterX = 32988;enterY = 32842;break;
		case RINDVIOR:	enterX = 32855;enterY = 32881;break;
		case VALAKAS:	enterX = 32771;enterY = 32892;break;
		default:return;
		}
		pc.getTeleport().start(enterX, enterY, (short) getMoveMapId(), 5, true);
	}
	
	private synchronized void halpasPortalEnter(L1PcInstance pc) {
		if (pc.getSkill().hasSkillEffect(L1SkillId.DRAGONRAID_BUFF)) {
			pc.sendPackets(L1ServerMessage.sm7619);// 드래곤 포탈: 입장 불가
			//pc.sendPackets(new S_SystemMessage(FORMAT.format(pc.getAccount().getDragonRaid()) + " 이후에 입장 가능합니다."), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(124), FORMAT.format(pc.getAccount().getDragonRaid())), true);
			return;
		}
		DragonRaidCreator manager = DragonRaidCreator.getInstance();
		DragonRaidHandler handler = manager.getRaid(getMoveMapId());
		if (handler == null) {
			pc.sendPackets(L1ServerMessage.sm7619);// 드래곤 포탈: 입장 불가
			return;
		}
		if (handler.countLairUser() >= 16) {
			pc.sendPackets(L1ServerMessage.sm7619);// 드래곤 포탈: 입장 불가
			return;
		}
		
		handler.addLairUser(pc);
		L1Location loc = new L1Location(32736, 32905, getMoveMapId()).randomLocation(6, true);
		pc.getTeleport().start(loc, 5, true);
		loc = null;
	}
	
	private void halpasEnter(L1PcInstance pc){
		DragonRaidCreator manager = DragonRaidCreator.getInstance();
		DragonRaidHandler handler = manager.getRaid(getMoveMapId());
		if (handler == null) {
			pc.sendPackets(L1ServerMessage.sm7619);// 드래곤 포탈: 입장 불가
			return;
		}
		if (!handler.isRun()) {
			handler.setRun(true);
			handler.raidStart();
		}
		L1Location loc = new L1Location(32789, 32895, getMoveMapId()).randomLocation(6, true);
		if (pc.DragonPortalLoc == null) {
			pc.DragonPortalLoc = new int[3];
		}
		pc.DragonPortalLoc[0] = loc.getX();
		pc.DragonPortalLoc[1] = loc.getY();
		pc.DragonPortalLoc[2] = loc.getMapId();
		pc.sendPackets(S_MessageYN.RAID_ENTER_YN);// 드래곤 레이드: 드래곤 레어에 진입하시겠습니까?
		loc = null;
	}

	private void telValakasRoom(L1PcInstance pc) {
		pc.getTeleport().start(32833, 32757, (short)getMapId(), 5, false);
		pc.isInValakasBoss = true;
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		L1World world = L1World.getInstance();
		world.removeVisibleObject(this);
		world.removeObject(this);
		for (L1PcInstance pc : world.getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		removeAllKnownObjects();
	}
	
	private int _dragonEnterCount = 0;
	public int getDragonEnterCount(){
		return _dragonEnterCount;
	}
	public void setDragonEnterCount(int value){
		_dragonEnterCount = value;
	}
}


