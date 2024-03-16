package l1j.server.server.model.item.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.RobotSystem.RobotAIThread;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.object.S_SummonObject;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class RobotScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public RobotScroll(L1Item item) {
		super(item);
	}
	
	private static final int[] _robot = { -2, -1, 0, 1, 2 };

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			switch(this.getItemId()){
			case 5564:scarecrow(pc);break;
			case 5565:fake(pc);break;
			}
		}
	}
	
	private void scarecrow(L1PcInstance pc){
		int type = 1, count = 1;
		try {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				if (type == 1) {
					pstm = con.prepareStatement("SELECT * FROM characters WHERE account_name = 'AIRobot' and level <= 99 order by rand()");
				}
				rs = pstm.executeQuery();
				while(rs.next()){
					L1PcInstance player = L1World.getInstance().getPlayer(rs.getString("char_name"));
					if (player != null) {
						continue;
					}
					if (count > 0) {
						L1PcInstance robot = L1PcInstance.load(rs.getString("char_name"));
						L1Map map = pc.getMap();
						int x = 0;
						int y = 0;
						if (type == 1) {
							while(true){
								x = _robot[CommonUtil.random(5)];
								y = _robot[CommonUtil.random(5)];
								robot.setX(pc.getX() + x);
								robot.setY(pc.getY() + y);
								robot.setMap(pc.getMapId());
								if(map.isPassable(robot.getX(), robot.getY()))break;
							}
						}
						robot.getMoveState().setHeading(CommonUtil.random(0, 7));
						robot.setOnlineStatus(1);
						robot.setNetConnection(null);
						robot.beginGameTimeCarrier();
						robot.sendVisualEffectAtLogin();
						robot.setDead(false);
						robot.setActionStatus(0);
						robot.noPlayerCK = true;
						for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
							if (summon.getMaster().getId() == robot.getId()) {
								summon.setMaster(robot);
								robot.addPet(summon);
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
									visiblePc.sendPackets(new S_SummonObject(summon), true);
								}
							}
						}
						War.getInstance().checkCastleWar(robot);
						robot.getAC().setAc(-(robot.getLevel() + 10));
						L1World.getInstance().storeObject(robot);
						L1World.getInstance().addVisibleObject(robot);
						if (robot.getResistance().getMr() <= 145) {
							int mr = 145 - robot.getResistance().getMr();
							robot.getResistance().addMr(mr);
						}
						robotItem(robot);
						for(L1ItemInstance item : robot.getInventory().getItems())robot.getInventory().removeItem(item);
						if (robot.isKnight()) {
							boolean isWeapon = false;
							for(L1ItemInstance item : robot.getInventory().getItems()){
								if(item.getItemId() == 300){ // 로보트의 한손검
									isWeapon = true;
									if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
								}
							}
							if(!isWeapon){
								L1ItemInstance item = ItemTable.getInstance().createItem(300);
								item.setEnchantLevel(7);
								robot.getInventory().storeItem(item);
								robot.getInventory().setEquipped(item, true);
							}
						} else if (robot.isElf()) {
							boolean isBow = false;
							for(L1ItemInstance item : robot.getInventory().getItems()){
								if(item.getItemId() == 305){ // 로보트의 활
									isBow = true;
									if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
								}
							}
							if(!isBow){
								L1ItemInstance item = ItemTable.getInstance().createItem(305);
								item.setEnchantLevel(7);
								robot.getInventory().storeItem(item);
								robot.getInventory().setEquipped(item, true);
							}
						} else if (robot.isWizard()) {
							boolean isWeapon = false;
							for(L1ItemInstance item : robot.getInventory().getItems()){
								if(item.getItemId() == 303){ // 로보트의 지팡이
									isWeapon = true;
									if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
								}
							}
							if(!isWeapon){
								L1ItemInstance item = ItemTable.getInstance().createItem(303);
								item.setEnchantLevel(7);
								robot.getInventory().storeItem(item);
								robot.getInventory().setEquipped(item, true);
							}
						} else if (robot.isDragonknight()) {
							boolean isWeapon = false;
							for(L1ItemInstance item : robot.getInventory().getItems()){
								if(item.getItemId() == 304){ // 로보트의 도끼
									isWeapon = true;
									if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
								}
							}
							if(!isWeapon){
								L1ItemInstance item = ItemTable.getInstance().createItem(304);
								item.setEnchantLevel(7);
								robot.getInventory().storeItem(item);
								robot.getInventory().setEquipped(item, true);
							}
						} else if (robot.isIllusionist()) {
							boolean isWeapon = false;
							for(L1ItemInstance item : robot.getInventory().getItems()){
								if(item.getItemId() == 306){ // 로보트의 키링크
									isWeapon = true;
									if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
								}
							}
							if(!isWeapon){
								L1ItemInstance item = ItemTable.getInstance().createItem(306);
								item.setEnchantLevel(7);
								robot.getInventory().storeItem(item);
								robot.getInventory().setEquipped(item, true);
							}
						} else if (robot.isDarkelf()) {
							boolean isWeapon = false;
							for(L1ItemInstance item : robot.getInventory().getItems()){
								if(item.getItemId() == 302){ // 로보트의 이도류
									isWeapon = true;
									if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
								}
							}
							if(!isWeapon){
								L1ItemInstance item = ItemTable.getInstance().createItem(302);
								item.setEnchantLevel(7);
								robot.getInventory().storeItem(item);
								robot.getInventory().setEquipped(item, true);
							}
						} else if (robot.isWarrior()) {
							boolean isWeapon = false;
							for (L1ItemInstance item : robot.getInventory().getItems()) {
								if (item.getItemId() == 304) { // 로보트의 도끼
									isWeapon = true;
									if (!item.isEquipped()) {
										robot.getInventory().setEquipped(item, true);
									}
								}
							}
							if (!isWeapon) {
								L1ItemInstance item = ItemTable.getInstance().createItem(304);
								item.setEnchantLevel(7);
								robot.getInventory().storeItem(item);
								robot.getInventory().setEquipped(item, true);
							}
						}
						if (type <= 2) {
							robot.getRobotAi().setType(type);
							RobotAIThread.append(robot, type);
							if (type == 1) {
								robot.getRobotAi().setAiStatus(robot.getRobotAi().AI_STATUS_WALK);
							}
						}
						count--;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fake(L1PcInstance pc){
		int type = 3, count = 1;
		try {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				if (type == 3) {
					pstm = con.prepareStatement("SELECT * FROM characters WHERE account_name = 'AIRobot' and level >= 10  order by rand()");
				}
				rs = pstm.executeQuery();
				while(rs.next()){
					L1PcInstance player = L1World.getInstance().getPlayer(rs.getString("char_name"));
					if (player != null) {
						continue;
					}
					if (count > 0) {
						L1PcInstance robot = L1PcInstance.load(rs.getString("char_name"));
						L1Map map = pc.getMap();
						int x = 0;
						int y = 0;
						if (type == 3) {
							while(true){
								x = _robot[CommonUtil.random(5)];
								y = _robot[CommonUtil.random(5)];
								robot.setX(pc.getX() + x);
								robot.setY(pc.getY() + y);
								robot.setMap(pc.getMapId());
								if (map.isPassable(robot.getX(), robot.getY())) {
									break;
								}
							}
						}
						robot.getMoveState().setHeading(CommonUtil.random(0, 7));
						robot.setOnlineStatus(1);
						robot.setNetConnection(null);
						robot.beginGameTimeCarrier();
						robot.sendVisualEffectAtLogin();
						robot.setDead(false);
						robot.setActionStatus(0);
						robot.noPlayerCK = true;
						for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
							if (summon.getMaster().getId() == robot.getId()) {
								summon.setMaster(robot);
								robot.addPet(summon);
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
									visiblePc.sendPackets(new S_SummonObject(summon), true);
								}
							}
						}
						War.getInstance().checkCastleWar(robot);
						robot.getAC().setAc(-(robot.getLevel() + 10));
						L1World.getInstance().storeObject(robot);
						L1World.getInstance().addVisibleObject(robot);
						if(robot.getResistance().getMr() <= 145){
							int mr = 145 - robot.getResistance().getMr();
							robot.getResistance().addMr(mr);
						}
						robotItem(robot);
						for(L1ItemInstance item : robot.getInventory().getItems())robot.getInventory().removeItem(item);
						if(robot.getLevel() >= 51){
							if(robot.isKnight())robot.getAbility().addAddedStr(robot.getLevel() - 50);
							else if(robot.isCrown())robot.getAbility().addAddedStr(robot.getLevel() - 50);
							else if(robot.isElf())robot.getAbility().addAddedDex(robot.getLevel() - 50);
							else if(robot.isWizard())robot.getAbility().addAddedInt(robot.getLevel() - 50);
							else if(robot.isDarkelf())robot.getAbility().addAddedStr(robot.getLevel() - 50);
							else if(robot.isIllusionist())robot.getAbility().addAddedInt(robot.getLevel() - 50);
							else if(robot.isDragonknight())robot.getAbility().addAddedStr(robot.getLevel() - 50);
							else if(robot.isWarrior())robot.getAbility().addAddedStr(robot.getLevel() - 50);
						}else{
							if(robot.isKnight()){
								boolean isWeapon = false;
								for(L1ItemInstance item : robot.getInventory().getItems()){
									if(item.getItemId() == 301){
										isWeapon = true;
										if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
									}
								}
								if(!isWeapon){
									L1ItemInstance item = ItemTable.getInstance().createItem(301);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							}else if(robot.isElf()){
								boolean isBow = false;
								for(L1ItemInstance item : robot.getInventory().getItems()){
									if(item.getItemId() == 305){
										isBow = true;
										if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
									}
								}
								if(!isBow){
									L1ItemInstance item = ItemTable.getInstance().createItem(305);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							}else if(robot.isWizard()){
								boolean isWeapon = false;
								for(L1ItemInstance item : robot.getInventory().getItems()){
									if(item.getItemId() == 303){
										isWeapon = true;
										if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
									}
								}
								if(!isWeapon){
									L1ItemInstance item = ItemTable.getInstance().createItem(303);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							}else if(robot.isDragonknight()){
								boolean isWeapon = false;
								for(L1ItemInstance item : robot.getInventory().getItems()){
									if(item.getItemId() == 304){
										isWeapon = true;
										if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
									}
								}
								if(!isWeapon){
									L1ItemInstance item = ItemTable.getInstance().createItem(304);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							}else if(robot.isIllusionist()){
								boolean isWeapon = false;
								for(L1ItemInstance item : robot.getInventory().getItems()){
									if(item.getItemId() == 306){
										isWeapon = true;
										if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
									}
								}
								if(!isWeapon){
									L1ItemInstance item = ItemTable.getInstance().createItem(306);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							}else if(robot.isWarrior()){
								boolean isWeapon = false;
								for(L1ItemInstance item : robot.getInventory().getItems()){
									if(item.getItemId() == 304){ // 로보트의 도끼
										isWeapon = true;
										if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
									}
								}
								if(!isWeapon){
									L1ItemInstance item = ItemTable.getInstance().createItem(304);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							}else if(robot.isDarkelf()){
								boolean isWeapon = false;
								for(L1ItemInstance item : robot.getInventory().getItems()){
									if(item.getItemId() == 302){
										isWeapon = true;
										if(!item.isEquipped())robot.getInventory().setEquipped(item, true);
									}
								}
								if(!isWeapon){
									L1ItemInstance item = ItemTable.getInstance().createItem(302);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							}
						}
						if(type == 3){
							if(CommonUtil.random(100) < 75){
								int rnd1 = CommonUtil.random(20, 60);
								robot.setTeleportTime(rnd1);
								int rnd2 = CommonUtil.random(5, 60);
								if(rnd1 == rnd2)rnd2++;
								robot.setSkillTime(rnd2);
							}
						}
						count--;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void robotItem(L1PcInstance pc) {
		pc.getInventory().loadItems();// DB로부터 아이템을 읽어들인다
	}
}

