package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1NpcShop;
import l1j.server.server.utils.SQLUtil;

public class NpcCashShopSpawnTable {
	private static Logger _log = Logger.getLogger(NpcCashShopSpawnTable.class.getName());

	private static NpcCashShopSpawnTable _instance;

	private ArrayList<L1NpcShop> npcShoplist = new ArrayList<L1NpcShop>();

	public static NpcCashShopSpawnTable getInstance() {
		if (_instance == null) {
			_instance = new NpcCashShopSpawnTable();
		}
		return _instance;
	}

	private NpcCashShopSpawnTable() {
		load();
	}

	public static void reload() {
		NpcCashShopSpawnTable oldInstance = _instance;
		_instance = new NpcCashShopSpawnTable();
		oldInstance.npcShoplist.clear();
	}
	
	public void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_npc_cash_shop");
			rs = pstm.executeQuery();
			do {
				if (!rs.next()) {
					break;
				}
				L1NpcShop shop = new L1NpcShop();

				shop.setNpcId(rs.getInt("npc_id"));
				shop.setName(rs.getString("name"));
				shop.setX(rs.getInt("locx"));
				shop.setY(rs.getInt("locy"));
				shop.setMapId(rs.getShort("mapid"));
				shop.setHeading(rs.getInt("heading"));
				shop.setTitle(rs.getString("title"));
				shop.setShopChat(rs.getString("shop_chat"));

				npcShoplist.add(shop);
				shop = null;
			} while (true);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (SecurityException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (IllegalArgumentException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public ArrayList<L1NpcShop> getList() {
		return npcShoplist;
	}

	public void Start() {
		try {
			ArrayList<L1NpcShop> list = getList();
			for (int i = 0; i < list.size(); i++) {
				L1NpcShop shop = list.get(i);

				L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(shop.getNpcId());
				npc.setId(IdFactory.getInstance().nextId());
				npc.setMap(shop.getMapId());

				npc.getLocation().set(shop.getX(), shop.getY(), shop.getMapId());
				npc.getLocation().forward(5);

				npc.setHomeX(npc.getX());
				npc.setHomeY(npc.getY());
				npc.getMoveState().setHeading(shop.getHeading());

				npc.setName(shop.getName());
				npc.setTitle(shop.getTitle());

				// L1NpcCashShopInstance obj = (L1NpcCashShopInstance)npc;

				// obj.setShopName(shop.getShopName());

				L1World.getInstance().storeObject(npc);
				L1World.getInstance().addVisibleObject(npc);

				npc.getLight().turnOnOffLight();

				// obj.setState(1);
				// Broadcaster.broadcastPacket(npc, new S_DoActionShop(npc.getId(), ActionCodes.ACTION_Shop, shop.getShopName().getBytes()), true);
			}
			list.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

