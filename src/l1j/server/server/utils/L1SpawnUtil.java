package l1j.server.server.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.IndunSystem.orim.OrimController;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcInfoTable.NpcInfoData;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ArrowInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.templates.L1Npc;

public class L1SpawnUtil {
	private static Logger _log = Logger.getLogger(L1SpawnUtil.class.getName());

	public static void spawn(L1PcInstance pc, int npcId, int randomRange, int timeMillisToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance(). newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			if (randomRange == 0) {
				npc.getLocation().set(pc.getLocation());
				npc.getLocation().forward(pc.getMoveState().getHeading());
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(pc.getX() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(pc.getY() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if(npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation()))break;
				} while (tryCount < 50);
				if (tryCount >= 50) {
					npc.getLocation().set(pc.getLocation());
					npc.getLocation().forward(pc.getMoveState().getHeading());
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(pc.getMoveState().getHeading());
			
			spawnAction(npc, npcId);// 스폰 이미지 액션화

			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/** 엔피씨를 스폰한다
	 * @param x @param y @param map @param npcId @param randomRange
	 * @param timeMillisToDelete @param movemap (이동시킬 맵을 설정한다 - 안타레이드) 
	 * @return L1NpcInstance
	 */
	public static L1NpcInstance spawn1(int x, int y, short map, int Heading, int npcId, int randomRange, int timeMillisToDelete, int movemap) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, map);
				npc.getLocation().forward(Heading);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);
				
				if (tryCount >= 50) {
					npc.getLocation().set(x,y,map);
					npc.getLocation().forward(Heading);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(Heading);
			
			spawnAction(npc, npcId);// 스폰 이미지 액션화
			if (movemap > 0) {
				npc.setMoveMapId(movemap);// 이동할 맵 셋팅
			}

			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc); 

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);				
				timer.begin();
				if (npcId == 5000103 || npcId == 5000104) {
					npc.startExplosionTime(timeMillisToDelete);// 할파스의 권속
				}
			}
			return npc;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		}
	}

	/** 엔피씨를 스폰한다
	 * @param x @param y @param map @param npcId @param randomRange
	 * @param timeMillisToDelete @param movemap (이동시킬 맵을 설정한다 - 안타레이드) */
	public static L1NpcInstance spawn2(int x, int y, short map, int Heading, int npcId, int randomRange, int timeMillisToDelete, int movemap) {
		return spawn1(x, y, map, Heading, npcId, randomRange, timeMillisToDelete, movemap);
	}
	
	public static void spawn3(int x, int y, short MapId, int Heading, int npcId, int randomRange, boolean isUsePainwand, int timeMillisToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			if (randomRange == 0) {
				npc.getLocation().forward(Heading);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1L);
				} while (tryCount < 50);
				
				if (tryCount >= 50) {
					npc.getLocation().forward(Heading);
				}
			}
			
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(Heading);
			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
			if (timeMillisToDelete > 0) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void spawn5(int x, int y, short MapId, int Heading, int npcId, int randomRange) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			if (randomRange == 0) {
				npc.getLocation().forward(Heading);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().forward(Heading);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(Heading);
			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);

			if (npc.getMapId() == 9101) {
				if (npcId == 91240) {
					if (Heading == 4) {
						OrimController.getInstance().setShell1(npc);
					} else {
						OrimController.getInstance().setShell2(npc);
					}
				} else if (npcId != 91222 && npcId != 91233 && npcId != 91235 && npcId != 91243) {
					OrimController.getInstance().addMonList(npc);
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static L1NpcInstance spawn(int x, int y, short map, int heading, int npcId, int randomRange, int timeMillisToDelete, L1Clan clan) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, map);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);
				if (tryCount >= 50) {
					npc.getLocation().set(x, y, map);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(heading);
			if (npc instanceof L1MerchantInstance) {
				L1MerchantInstance mer = (L1MerchantInstance) npc;
				mer.setDesc(clan.getClanName());
				mer.setClanid(clan.getClanId());
				mer.setClanname(clan.getClanName());
				mer.setEmblemId(clan.getEmblemId());
			}
			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return npc;
	}
	
	public static void spawn(L1NpcInstance temp, int npcId, int randomRange) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(temp.getMapId());
			if (randomRange == 0) {
				npc.getLocation().set(temp.getLocation());
				npc.getLocation().forward(temp.getMoveState().getHeading());
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(temp.getX() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(temp.getY() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(temp.getLocation());
					npc.getLocation().forward(temp.getMoveState().getHeading());
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(temp.getMoveState().getHeading());
			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void spawn6(int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, int movemap) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, map);
				npc.getLocation().forward(5);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(x, y, map);
					npc.getLocation().forward(5);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(5);
			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	public static L1NpcInstance spawnCount(int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, int count) {
		L1NpcInstance npc = null;
		try {
			for (int i = 0; i < count; i++) {
				npc = NpcTable.getInstance().newNpcInstance(npcId);
				npc.setId(IdFactory.getInstance().nextId());
				npc.setMap(map);
				if (randomRange == 0) {
					npc.getLocation().set(x, y, map);
					npc.getLocation().forward(5);
				} else {
					int tryCount = 0;
					do {
						tryCount++;
						npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
						npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
						if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
							break;
						}
						Thread.sleep(1);
					} while (tryCount < 50);

					if (tryCount >= 50) {
						npc.getLocation().set(x, y, map);
						npc.getLocation().forward(5);
					}
				}

				npc.setHomeX(npc.getX());
				npc.setHomeY(npc.getY());
				npc.getMoveState().setHeading(6);

				if (npcId == 45545 || npcId == 45516 || npcId == 45529) {
					L1MonsterInstance mon = (L1MonsterInstance) npc;
					mon.setMovementDistance(15);
				}
				L1World world = L1World.getInstance();
				world.storeObject(npc);
				world.addVisibleObject(npc);
				npc.getLight().turnOnOffLight();
				npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
				if (0 < timeMillisToDelete) {
					L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
					timer.begin();
				}
				if (npc instanceof L1MonsterInstance) {
					npc.onNpcAI();
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return npc;
	}

	public static void spawn(L1NpcInstance npc1, int npcId, int randomRange, int timeMillisToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(npc1.getMapId());

			if (randomRange == 0) {
				npc.getLocation().set(npc1.getLocation());
				npc.getLocation().forward(npc1.getMoveState().getHeading());
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(npc1.getX() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(npc1.getY() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(npc1.getLocation());
					npc.getLocation().forward(npc1.getMoveState().getHeading());
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(npc1.getMoveState().getHeading());
			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);
			
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static L1NpcInstance spawn(int x, int y, short mapId, int heading, int npcId, int randomRange, int timeMillisToDelete) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance(). newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(mapId);
			if (randomRange == 0) {
				npc.setX(x);
				npc.setY(y);
				if (npcId == 7200015 || npcId == 7200021 || npcId == 7200027) {
				} else {
					npc.getLocation().forward(heading);
				}
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.setX(x);
					npc.setY(y);
					if (mapId >= 10000 && mapId <= 10999) {
					} else {
						npc.getLocation().forward(heading);
					}
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return npc;
	}
	
	public static void spawn(L1Spawn spawn, L1PcInstance pc, int npcId) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			boolean isMonster = npc instanceof L1MonsterInstance;
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			npc.getLocation().set(pc.getLocation());
			if (isMonster) {
				npc.getLocation().forward(pc.getMoveState().getHeading());
			}
			
			npc.setHomeX(pc.getX());
			npc.setHomeY(pc.getY());
			npc.getMoveState().setHeading(pc.getMoveState().getHeading());
			
			npc.setSpawn(spawn);
			npc.setRespawn(true);
			npc.setSpawnNumber(1); 
			
			if (isMonster) {
				L1SpawnUtil.spawnAction(npc, npcId);// 스폰 이미지 액션화
			}

			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);
			
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 화살을 스폰한다 */
	public static FastTable<L1ArrowInstance> ArrowSpawn() {
		Connection con					= null;
		PreparedStatement pstm			= null;
		ResultSet rs					= null;
		L1Npc l1npc						= null;
		L1ArrowInstance arrow			= null;
		FastTable<L1ArrowInstance> list	= new FastTable<L1ArrowInstance>();
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM spawnlist_arrow");
			rs		= pstm.executeQuery();
			while(rs.next()){
				l1npc = NpcTable.getInstance().getTemplate(rs.getInt("npc_id"));
				if (l1npc == null) {
					continue;
				}
				try {
					arrow = (L1ArrowInstance) NpcTable.getInstance().newNpcInstance(l1npc.getNpcId());
					arrow.setId(IdFactory.getInstance().nextId());
					arrow.setX(rs.getInt("locx"));
					arrow.setY(rs.getInt("locy"));
					arrow.setTarX(rs.getInt("tarx"));
					arrow.setTarY(rs.getInt("tary"));
					arrow.setMap((short) rs.getInt("mapid"));
					arrow.setLightSize(l1npc.getLightSize());
					arrow.getLight().turnOnOffLight();
					L1World world = L1World.getInstance();
					world.storeObject(arrow);
					world.addVisibleObject(arrow);
					int delay = rs.getInt("start_delay");
					if (delay == 0) {
						arrow.setAction(true);
					}
					list.add(arrow);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return list;
	}
	
	public static ArrayList<L1NpcInstance> worldWarSpawn(int type) {
		Connection con					= null;
		PreparedStatement pstm			= null;
		ResultSet rs					= null;
		L1Npc l1npc						= null;
		L1NpcInstance field				= null;
		ArrayList<L1NpcInstance> list	= new ArrayList<L1NpcInstance>();
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM spawnlist_worldwar");
			rs		= pstm.executeQuery();
			while(rs.next()){
				if (type != rs.getInt("type")) {
					continue;
				}
				l1npc = NpcTable.getInstance().getTemplate(rs.getInt("npc_id"));
				if (l1npc == null) {
					continue;
				}
				try {
					field = NpcTable.getInstance().newNpcInstance(rs.getInt("npc_id"));
					field.setId(IdFactory.getInstance().nextId());
					field.setX(rs.getInt("locx"));
					field.setY(rs.getInt("locy"));
					field.setMap((short) rs.getInt("mapid"));
					field.setHomeX(field.getX());
					field.setHomeY(field.getY());
					field.getMoveState().setHeading(rs.getInt("heading"));
					field.setLightSize(l1npc.getLightSize());
					field.getLight().turnOnOffLight();
					for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(field)) {
						field.onPerceive(_pc);
						_pc.send_effect(field.getId(), 12261);
					}
					L1World world = L1World.getInstance();
					world.storeObject(field);
					world.addVisibleObject(field);
					list.add(field);
					Thread.sleep(1);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
				l1npc = null;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return list;
	}
	
	public static void spawnAction(L1NpcInstance npc, int npcId){
		try {
			if (npcId == 707026) {// 에이션트 가디언
				npc.setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_ANCIENTGUARDIAN);
				npc.setActionStatus(6);
			} else if (npcId == 800144 || npcId == 5182) {// 왜곡의 제니스퀸, 에르자베
				npc.broadcastPacket(new S_EffectLocation(npc.getX(), npc.getY(), 14258), true);// 독이팩트
			} else if (npcId == 800153) {// 오만한 우그누스 등장 이팩트
				ugnusLightEffect(npc);
			}
			
			NpcInfoData info = npc.getInfo();
			if (info == null || info._spawnActionId == 0) {
				return;
			}
			npc.setActionStatus(info._spawnActionId);
			S_CharVisualUpdate visual = new S_CharVisualUpdate(npc, 0);
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc)) {
				npc.onPerceive(pc);
				pc.sendPackets(visual);
			}
			npc.setActionStatus(0);
			visual.clear();
			visual = null;
			npc.setParalysisTime(npc.getSprite().getActionSpeed(info._spawnActionId) + 100);// sprite delay
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void ugnusLightEffect(L1NpcInstance npc){
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				try {
					L1Location loc = null;
					for (int i=0; i<5; i++) {
						Thread.sleep(300L);
						loc = npc.getLocation().randomLocation(4, false);
						npc.broadcastPacket(new S_EffectLocation(loc.getX(), loc.getY(), 17081), true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

