package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.RobotSystem.RobotAIThread;
import l1j.server.common.data.Gender;
import l1j.server.server.GameServerSetting;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.object.S_SummonObject;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1Robot4 implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Robot4.class.getName());
	private static final int[] loc = { -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8 };

	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Robot4();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Robot4() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String type = st.nextToken();

			if (type.equalsIgnoreCase("1")) {
				toAppendBot(pc, st);
				return true;
			}

			if (type.equalsIgnoreCase("2")) {
				toBotStart(pc, st);
				return true;
			}

			if (type.equalsIgnoreCase("3")) {
				toBotEnd(pc, st);
				return true;
			}
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".인공지능 1:캐릭생성 2:캐릭시작 3:캐릭종료"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(642), true), true);
			return false;
		}
	}

	/**
	 * 로봇 생성처리 함수.
	 * 
	 * @param pc
	 * @param st
	 */
	private void toAppendBot(L1PcInstance pc, StringTokenizer st) {
		try {
			int count = Integer.valueOf(st.nextToken());
			// 캐릭터 생성 디비 등록.
			while (count-- > 0) {
				int init_hp = 1500;
				int init_mp = 200;
				String name = RobotAIThread.getName();

				if (name == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("더이상 생성할 이름이 존재하지않습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(643), true), true);
					return;
				}

				L1PcInstance robot = new L1PcInstance();
				
				
				robot.setAccountName("AIRobot"); // 계정명
				robot.setId(IdFactory.getInstance().nextId()); // 고유 ID
				robot.setName(name); // 캐릭터명

				int ra = CommonUtil.random(100);
				if (ra < 50) {
					robot.setLevel(1); // 레벨
					robot.setHighLevel(1); // 최고레벨
					robot.setExp(0); // 경험치
					robot.addBaseMaxHp((short) 200); // 최대 HP
					robot.setCurrentHp(init_hp); // 현재 HP
					robot.addBaseMaxMp((short) 50); // 최대 MP
				} else {
					robot.setLevel(55); // 레벨
					robot.setHighLevel(55); // 최고레벨
					robot.setExp(596787342 + CommonUtil.random(50000)); // 경험치
					robot.addBaseMaxHp((short) 700); // 최대 HP
					robot.setCurrentHp(init_hp); // 현재 HP
					robot.addBaseMaxMp((short) 200); // 최대 MP
				}

				robot.setCurrentMp(init_mp); // 현재 MP
				robot.getAbility().setBaseStr(18); // 스텟 STR
				robot.getAbility().setBaseDex(18); // 스텟 DEX
				robot.getAbility().setBaseCon(18); // 스텟 CON
				robot.getAbility().setBaseWis(18); // 스텟 WIS
				robot.getAbility().setBaseCha(18); // 스텟 CHA
				robot.getAbility().setBaseInt(18); // 스텟 INT

				int ran = CommonUtil.random(100);
				if (ran >= 0 && ran < 20) {
					robot.setType(0);
				} else if (ran >= 20 && ran < 40) {
					robot.setType(1);

				} else if (ran >= 40 && ran < 50) {
					robot.setType(2);

				} else if (ran >= 50 && ran < 60) {
					robot.setType(3);

				} else if (ran >= 60 && ran < 70) {
					robot.setType(4);

				} else if (ran >= 70 && ran < 80) {
					robot.setType(5);
				} else if (ran >= 80 && ran < 90) {
					robot.setType(6);
				} else if (ran >= 90 && ran < 100) {
					robot.setType(7);
				} else {
					robot.setType(CommonUtil.random(7));
				}

				robot.setGender(Gender.fromInt(CommonUtil.nextBoolean() ? 0 : 1)); // 남

				// 클래스 이미지
				robot.setClassId(robot.getGender() == Gender.MALE ? L1CharacterInfo.MALE_LIST[robot.getType()] : L1CharacterInfo.FEMALE_LIST[robot.getType()]);
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
				robot.setX(loc[0]);
				robot.setY(loc[1]);
				robot.setMap((short)loc[2]);
				robot.getMoveState().setHeading(0);
				robot.setFood(GameServerSetting.MAX_FOOD_VALUE); // 17%
				robot.setTitle(StringUtil.EmptyString);
				robot.setClanid(0);
				robot.setClanName(StringUtil.EmptyString);
				robot.setClanMemberNotes(StringUtil.EmptyString);
				robot.resetBaseAc();
				robot.setGm(false);
				robot.setMonitor(false);
				robot.setGmInvis(false);
				robot.setAccessLevel((short) 0);
				robot.setBonusStats(0);
				robot.resetBaseMr();
				robot.setBanned(false);
				int rnd11 = CommonUtil.random(5);
				int rnd111 = CommonUtil.random(5);
				robot.setKills(rnd11);
				robot.setDeaths(rnd111);
				CharacterTable.getInstance().storeNewCharacter(robot);
				robot.refresh();
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aG시스템:\\aA 인공지능 캐릭들 생성완료"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(644), true), true);
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".인공지능  1  [생성할갯수]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(645), true), true);
		}
	}

	/**
	 * 로봇 인공지능 활성화.
	 * 
	 * @param pc
	 * @param st
	 */
	private void toBotStart(L1PcInstance pc, StringTokenizer st) {
		try {
			int type = Integer.valueOf(st.nextToken());
			int count = Integer.valueOf(st.nextToken());

			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				if (type == 1) {
					pstm = con.prepareStatement("SELECT * FROM characters WHERE account_name = 'AIRobot' and level <= 55 order by rand()");
				} else if (type == 2) {
					pstm = con.prepareStatement("SELECT * FROM characters WHERE account_name = 'AIRobot' and level >= 55  order by rand()");
				} else if (type == 3) {
					pstm = con.prepareStatement("SELECT * FROM characters WHERE account_name = 'AIRobot' order by rand()");
				}
				rs = pstm.executeQuery();
				while (rs.next()) {
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
							while (true) {
								x = loc[CommonUtil.random(17)];
								y = loc[CommonUtil.random(17)];
								robot.setX(pc.getX() + x);
								robot.setY(pc.getY() + y);
								robot.setMap(pc.getMapId());
								if (map.isPassable(robot.getX(), robot.getY())) {
									break;
								}
							}
						} else if (type == 2) {
							while (true) {
								x = loc[CommonUtil.random(17)];
								y = loc[CommonUtil.random(17)];
								robot.setX(pc.getX() + x);
								robot.setY(pc.getY() + y);
								robot.setMap(pc.getMapId());
								if (map.isPassable(robot.getX(), robot.getY())) {
									break;
								}
							}
						} else if (type == 3) {
							while (true) {
								x = loc[CommonUtil.random(17)];
								y = loc[CommonUtil.random(17)];
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

						if (robot.getResistance().getMr() <= 145) {
							int mr = 145 - robot.getResistance().getMr();
							robot.getResistance().addMr(mr);
						}

						items(robot);

						for (L1ItemInstance item : robot.getInventory().getItems()) {
							robot.getInventory().removeItem(item);
						}

						if (robot.getLevel() >= 51) {
							if (robot.isKnight() || robot.isFencer() || robot.isLancer()) {
								robot.getAbility().addAddedStr(robot.getLevel() - 50);
							} else if (robot.isCrown()) {
								robot.getAbility().addAddedStr(robot.getLevel() - 50);
							} else if (robot.isElf()) {
								robot.getAbility().addAddedDex(robot.getLevel() - 50);
							} else if (robot.isWizard()) {
								robot.getAbility().addAddedInt(robot.getLevel() - 50);
							} else if (robot.isDarkelf()) {
								robot.getAbility().addAddedStr(robot.getLevel() - 50);
							} else if (robot.isIllusionist()) {
								robot.getAbility().addAddedInt(robot.getLevel() - 50);
							} else if (robot.isDragonknight()) {
								robot.getAbility().addAddedStr(robot.getLevel() - 50);
							} else if (robot.isWarrior()) {
    							robot.getAbility().addAddedStr(robot.getLevel() - 50);
							}
						}
						if (type == 3) {
							if (robot.isKnight() || robot.isFencer()) {
								robot.setCurrentWeapon(50);
							} else if (robot.isCrown()) {
								robot.setCurrentWeapon(4);
							} else if (robot.isElf()) {
								robot.setCurrentWeapon(20);
							} else if (robot.isWizard()) {
								robot.setCurrentWeapon(40);
							} else if (robot.isDarkelf()) {
								robot.setCurrentWeapon(54);
							} else if (robot.isIllusionist()) {
								robot.setCurrentWeapon(40);
							} else if (robot.isDragonknight()) {
								robot.setCurrentWeapon(50);
							} else if (robot.isWarrior()) {
								robot.setCurrentWeapon(50);
							} else if (robot.isLancer()) {
								robot.setCurrentWeapon(24);
							}
						} else {
							if (robot.isKnight() || robot.isFencer()) {
								boolean isWeapon = false;

								for (L1ItemInstance item : robot.getInventory().getItems()) {
									if (item.getItemId() == 300) { //로보트의 한손검
										isWeapon = true;
										if (!item.isEquipped()) {
											robot.getInventory().setEquipped(item, true);
										}
									}
								}
								if (!isWeapon) {
									L1ItemInstance item = ItemTable.getInstance().createItem(300);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							} else if (robot.isElf()) {
								boolean isBow = false;

								for (L1ItemInstance item : robot.getInventory().getItems()) {
									if (item.getItemId() == 305) { //로보트의 활
										isBow = true;
										if (!item.isEquipped()) {
											robot.getInventory().setEquipped(item, true);
										}
									}
								}

								if (!isBow) {
									L1ItemInstance item = ItemTable.getInstance().createItem(305);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							} else if (robot.isWizard()) {
								boolean isWeapon = false;

								for (L1ItemInstance item : robot.getInventory().getItems()) {
									if (item.getItemId() == 303) { //로보트의 지팡이
										isWeapon = true;
										if (!item.isEquipped()) {
											robot.getInventory().setEquipped(item, true);
										}
									}
								}

								if (!isWeapon) {
									L1ItemInstance item = ItemTable.getInstance().createItem(303);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							} else if (robot.isDragonknight()) {
								boolean isWeapon = false;

								for (L1ItemInstance item : robot.getInventory().getItems()) {
									if (item.getItemId() == 301) { //로보트의 양손검
										isWeapon = true;
										if (!item.isEquipped()) {
											robot.getInventory().setEquipped(item, true);
										}
									}
								}

								if (!isWeapon) {
									L1ItemInstance item = ItemTable.getInstance().createItem(301);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							} else if (robot.isIllusionist()) {
								boolean isWeapon = false;

								for (L1ItemInstance item : robot.getInventory().getItems()) {
									if (item.getItemId() == 306) { //로보트의 키링크
										isWeapon = true;
										if (!item.isEquipped()) {
											robot.getInventory().setEquipped(item, true);
										}
									}
								}

								if (!isWeapon) {
									L1ItemInstance item = ItemTable.getInstance().createItem(306);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							} else if (robot.isDarkelf()) {
								boolean isWeapon = false;
								for (L1ItemInstance item : robot.getInventory().getItems()) {
									if (item.getItemId() == 302) { //로보트의 이도류
										isWeapon = true;
										if (!item.isEquipped()) {
											robot.getInventory().setEquipped(item, true);
										}
									}
								}
								if (!isWeapon) {
									L1ItemInstance item = ItemTable.getInstance().createItem(302);
									item.setEnchantLevel(7);
									robot.getInventory().storeItem(item);
									robot.getInventory().setEquipped(item, true);
								}
							} else if (robot.isWarrior()) {
								boolean isWeapon = false;
								for (L1ItemInstance item : robot.getInventory().getItems()) {
									if (item.getItemId() == 304) { //로보트의 도끼
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
							} else if (robot.isLancer()) {
								boolean isWeapon = false;
								for (L1ItemInstance item : robot.getInventory().getItems()) {
									if (item.getItemId() == 105) { //로보트의 창
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
							
						}

						if (type == 3) {
							if (CommonUtil.random(100) < 75) {
								int rnd1 = CommonUtil.random(20, 60);
								robot.setTeleportTime(rnd1);

								int rnd2 = CommonUtil.random(5, 60);

								if (rnd1 == rnd2) {
									rnd2++;
								}
								robot.setSkillTime(rnd2);
							}
						}
						if (type <= 2) {
							robot.getRobotAi().setType(type);
							RobotAIThread.append(robot, type);

							if (type == 1) {
								robot.getRobotAi().setAiStatus(robot.getRobotAi().AI_STATUS_WALK);
							} else if (type == 2) {
								robot.getRobotAi().setAiStatus(robot.getRobotAi().AI_STATUS_SETTING);
							}
						}

						count--;
					}
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs, pstm, con);
			}

		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".ai (2) [1.허수아비] [2.사냥] [3.마을]  [갯수]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(646), true), true);
		}
	}

	/**
	 * 로봇 인공지능 비활성화.
	 * 
	 * @param pc
	 * @param st
	 */
	private void toBotEnd(L1PcInstance pc, StringTokenizer st) {
		try {
			int type = Integer.parseInt(st.nextToken());
			int count = 0;
			for (Object obj : L1World.getInstance().getAllPlayers()) {
				if (obj instanceof L1PcInstance) {
					L1PcInstance player = (L1PcInstance) obj;
					if (!player.isPrivateShop()) {
						if (player.getRobotAi() != null && player.getRobotAi().type == type) {
							if (type == 1) {
								count++;
							}
							RobotAIThread.remove(player, type);
							player.setRobotAi(null);
							player.logout();
							if (type == 1 && count >= 10) {
//AUTO SRM: 								pc.sendPackets(new S_SystemMessage("허수아비 인공지능 " + count + " 캐릭 종료."), true); // CHECKED OK
								pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(647) + count  + S_SystemMessage.getRefText(648), true), true);
								count = 0;
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".인공지능 3 [1허수,2사냥,3마을]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(649), true), true);
		}
	}

	private void items(L1PcInstance pc) {
		// DB로부터 캐릭터와 아이템을 읽어들인다
		pc.getInventory().loadItems();
	}

	static public int random(int lbound, int ubound) {
		return (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
	}
}

