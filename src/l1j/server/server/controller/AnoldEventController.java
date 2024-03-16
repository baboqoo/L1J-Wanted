package l1j.server.server.controller;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.warehouse.ClanWarehouse;
import l1j.server.server.model.warehouse.PrivateWarehouse;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 아놀드 이벤트 컨트롤러
 * @author LinOffice
 */
public class AnoldEventController extends Thread {
	private static AnoldEventController _instance;

	private boolean _AnoldEvent;
	public boolean getAnoldEvent() {
		return _AnoldEvent;
	}

	private static long sTime = 0;

	private String NowTime = StringUtil.EmptyString;

	private static final SimpleDateFormat s = new SimpleDateFormat("HH", Locale.KOREA);
	private static final SimpleDateFormat ss = new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);

	public void setAnoldEvent(boolean Anold) {
		_AnoldEvent = Anold;
	}

	public boolean isGmOpen;

	public static AnoldEventController getInstance() {
		if (_instance == null) {
			_instance = new AnoldEventController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(1000);
				/** 오픈 **/
				if (!isOpen6() && !isGmOpen) {
					continue;
				}
				L1World world = L1World.getInstance();
				if (world.getAllPlayers().size() <= 0) {
					continue;
				}
				isGmOpen = false;

				Config.ETC.ANOLD_EVENT_ACTIVE = true;
				int duration = Config.ETC.ANOLD_EVENT_TIME * 3600000;
				L1SpawnUtil.spawn2(33433, 32798, (short) 4, 5, 6, 0, duration, 0);// 텔레포터
				L1SpawnUtil.spawn2(33431, 32798, (short) 4, 5, 7, 0, duration, 0);// 텔레포터
				L1SpawnUtil.spawn2(33429, 32798, (short) 4, 5, 8, 0, duration, 0);// 텔레포터
//AUTO SRM: 				world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "운영자님께서 아놀드 이벤트를 오픈합니다."), true); // CHECKED OK
				world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(920)), true);
//AUTO SRM: 				world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "기란여관앞 엔피씨를 이용하시기 바랍니다."), true); // CHECKED OK
				world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(921)), true);
//AUTO SRM: 				world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "아놀드 이벤트는 " + Config.ETC.ANOLD_EVENT_TIME + "시간동안 유지됩니다."), true); // CHECKED OK
				world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(922) + Config.ETC.ANOLD_EVENT_TIME  + S_SystemMessage.getRefText(923)), true);

				setAnoldEvent(true);
				Thread.sleep(Config.ETC.ANOLD_EVENT_TIME * 3600000); // 60분정도
				Config.ETC.ANOLD_EVENT_ACTIVE = false;
				/** 종료 **/
				End();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void delenpc(int npcid) {
		L1NpcInstance npc = null;
		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1NpcInstance) {
				npc = (L1NpcInstance) object;
				if (npc.getNpcTemplate().getNpcId() == npcid) {
					npc.deleteMe();
					npc = null;
				}
			}
		}
	}

	/**
	 * 오픈 시각을 가져온다
	 *
	 * @return (Strind) 오픈 시각(MM-dd HH:mm)
	 */
	public String AnoldOpen() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(sTime);
		return ss.format(c.getTime());
	}

	/**
	 * 맵이 열려있는지 확인
	 *
	 * @return (boolean) 열려있다면 true 닫혀있다면 false
	 */
	private boolean isOpen6() {
		NowTime = getTime();
		if ((Integer.parseInt(NowTime)) == 0) {
			return true;
		}
		return false;
	}

	// 악마왕영토 소스 참조하여 추가함.
	public boolean isOpen7() {
		NowTime = getTime();
		if ((Integer.parseInt(NowTime)) >= 2 && (Integer.parseInt(NowTime)) <= 8) {
			return true;
		}
		return false;
	}

	/**
	 * 실제 현재시각을 가져온다
	 *
	 * @return (String) 현재 시각(HH:mm)
	 */
	private String getTime() {
		return s.format(Calendar.getInstance().getTime());
	}

	private static final int delItemlist[] = { 307, 308, 309, 310, 311, 312, 313, 314, 21095, 30146, 30147, 30150 };

	public synchronized static void deleteItem() {
		try {
			if (delItemlist.length <= 0) {
				return;
			}

			for (L1PcInstance tempPc : L1World.getInstance().getAllPlayers()) {
				if (tempPc == null) {
					continue;
				}
				for (int i = 0; i < delItemlist.length; i++) {
					L1ItemInstance[] item = tempPc.getInventory().findItemsId(delItemlist[i]);
					if (item != null && item.length > 0) {
						for (int o = 0; o < item.length; o++) {
							tempPc.getInventory().removeItem(item[o]);
						}
					}
					try {
						PrivateWarehouse pw = WarehouseManager.getInstance().getPrivateWarehouse(tempPc.getAccountName());
						L1ItemInstance[] item2 = pw.findItemsId(delItemlist[i]);
						if (item2 != null && item2.length > 0) {
							for (int o = 0; o < item2.length; o++) {
								pw.removeItem(item2[o]);
							}
						}
					} catch (Exception e) {
					}
					try {
						if (tempPc.getClanid() > 0) {
							ClanWarehouse cw = WarehouseManager.getInstance().getClanWarehouse(tempPc.getClanName());
							L1ItemInstance[] item3 = cw.findItemsId(delItemlist[i]);
							if (item3 != null && item3.length > 0) {
								for (int o = 0; o < item3.length; o++) {
									cw.removeItem(item3[o]);
								}
							}
						}
					} catch (Exception e) {
					}
					try {
						if (tempPc.getPetList().size() > 0) {
							for (L1NpcInstance npc : tempPc.getPetList().values()) {
								L1ItemInstance[] pitem = npc.getInventory().findItemsId(delItemlist[i]);
								if (pitem != null && pitem.length > 0) {
									for (int o = 0; o < pitem.length; o++) {
										npc.getInventory().removeItem(pitem[o]);
									}
								}
							}
						}
					} catch (Exception e) {
					}
				}
			}
			try {
				for (L1Object obj : L1World.getInstance().getAllItem()) {
					if (!(obj instanceof L1ItemInstance)) {
						continue;
					}
					L1ItemInstance temp_item = (L1ItemInstance) obj;
					if (temp_item.getItemOwner() == null) {
						if (temp_item.getX() == 0 && temp_item.getY() == 0) {
							continue;
						}
					}
					for (int ii = 0; ii < delItemlist.length; ii++) {
						if (delItemlist[ii] == temp_item.getItemId()) {
							L1Inventory groundInventory = L1World.getInstance().getInventory(temp_item.getX(), temp_item.getY(), temp_item.getMapId());
							groundInventory.removeItem(temp_item);
							break;
						}
					}
				}
			} catch (Exception e) {
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < delItemlist.length; i++) {
				sb.append(+delItemlist[i]);
				if (i < delItemlist.length - 1) {
					sb.append(StringUtil.CommaString);
				}
			}
			try {
				Delete(sb.toString());
			} catch (Exception e) { }
		} catch (Exception e) {
		}
	}

	private static void Delete(String id_name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_warehouse WHERE item_id IN (" + id_name + ")");
			pstm.executeUpdate();
			SQLUtil.close(pstm);
			pstm = con.prepareStatement("DELETE FROM clan_warehouse WHERE item_id IN (" + id_name + ")");
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/** 종료 **/
	public void End() {
		Config.ETC.ANOLD_EVENT_ACTIVE = false;
		delenpc(6);
		delenpc(7);
		delenpc(8);
		deleteItem();
//AUTO SRM: 		L1World.getInstance().broadcastServerMessage("아놀드 이벤트가 종료되었습니다.", true); // CHECKED OK
		L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(919), true);
		setAnoldEvent(false);
	}

}


