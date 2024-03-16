package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class LogTable {

	private static LogTable _instance;

	// public static final int DROP_LOG = 0;
	// public static final int PICKUP_LOG = 1;

	public static LogTable getInstance() {
		if (_instance == null) {
			_instance = new LogTable();
		}
		return _instance;
	}

	public LogTable() {}

	//private static Timestamp start사냥T;
	private static Timestamp startHuntingTime;
	//private static Timestamp start상점T;
	private static Timestamp startShopTime;
	//public static boolean 사냥아덴;
	public static boolean huntingAden;	
	//public static boolean 상점아덴;
	public static boolean shopAden;	
	//public static ArrayList<adenLog> 사냥아덴리스트 = new ArrayList<adenLog>();
	public static ArrayList<adenLog> huntingAdenList = new ArrayList<adenLog>();	
	//public static ArrayList<adenLog> 상점아덴리스트 = new ArrayList<adenLog>();
	public static ArrayList<adenLog> shopAdenList = new ArrayList<adenLog>();
	
	//public static void 사냥아덴시작() {
	public static void startHuntingAden() {
		huntingAdenList.clear();
		huntingAden = true;
		startHuntingTime = new Timestamp(System.currentTimeMillis());
	}

	//public static void 사냥아덴(L1PcInstance pc, int count) {
	public static void huntingAden(L1PcInstance pc, int count) {
		if (huntingAden) {
			if (pc.getNetConnection() == null) {
				return;
			}
			adenLog log = null;
			for (adenLog aL : huntingAdenList) {
				if (aL.name.equalsIgnoreCase(pc.getName())) {
					log = aL;
					break;
				}
			}
			if (log != null) {
				log.count += count;
			} else {
				log = new adenLog();
				log.accounts = pc.getAccountName();
				log.name = pc.getName();
				log.count += count;
				huntingAdenList.add(log);
			}
		}
	}

	//public static void 사냥아덴종료() {
	public static void endHuntingAden() {		
		huntingAden = false;
		GeneralThreadPool.getInstance().schedule(new saveThread(huntingAdenList, startHuntingTime, false), 1);
	}

	//public static void 상점아덴시작() {
	public static void startShopAden() {		
		shopAdenList.clear();
		shopAden = true;
		startShopTime = new Timestamp(System.currentTimeMillis());
	}

	public static void shopAden(L1PcInstance pc, int count) {
		if (shopAden) {
			if (pc.getNetConnection() == null) {
				return;
			}
			adenLog log = null;
			for (adenLog aL : shopAdenList) {
				if (aL.name.equalsIgnoreCase(pc.getName())) {
					log = aL;
					break;
				}
			}
			if (log != null) {
				log.count += count;
			} else {
				log = new adenLog();
				log.accounts = pc.getAccountName();
				log.name = pc.getName();
				log.count += count;
				shopAdenList.add(log);
			}
		}
	}

	//public static void 상점아덴종료() {
	public static void endShopAden() {		
		shopAden = false;
		GeneralThreadPool.getInstance().schedule(new saveThread(shopAdenList, startShopTime, true), 1);
	}

	static class adenLog {
		public String accounts = StringUtil.EmptyString;
		public String name = StringUtil.EmptyString;
		public int count = 0;
	}

	static class saveThread implements Runnable {
		private ArrayList<adenLog> list = new ArrayList<adenLog>();
		private Timestamp start;
		private Timestamp end;
		private boolean shop = false;

		public saveThread(ArrayList<adenLog> _list, Timestamp _startTime, boolean ck) {
			list.addAll(_list);
			start = new Timestamp(_startTime.getTime());
			end = new Timestamp(System.currentTimeMillis());
			shop = ck;
		}

		@Override
		public void run() {
			try {
				if (list.size() <= 0) {
					return;
				}
				adenLog aL = list.remove(0);
				if (aL == null) {
					return;
				}
				if (aL.count <= 0) {
					GeneralThreadPool.getInstance().schedule(this, 100);
					return;
				}
				Connection con = null;
				PreparedStatement pstm = null;
				try {
					con = L1DatabaseFactory.getInstance().getConnection();
					if (shop) {
						pstm = con.prepareStatement("INSERT INTO log_adena_shop SET startTime=?, endTime=?, accounts=?, name=?, count=?");
					} else {
						pstm = con.prepareStatement("INSERT INTO log_adena_monster SET startTime=?, endTime=?, accounts=?, name=?, count=?");
					}
					pstm.setTimestamp(1, start);
					pstm.setTimestamp(2, end);
					pstm.setString(3, aL.accounts);
					pstm.setString(4, aL.name);
					pstm.setInt(5, aL.count);
					pstm.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					SQLUtil.close(pstm, con);
				}
				GeneralThreadPool.getInstance().schedule(this, 100);
			} catch (Exception e) {

			}
		}
	}

	
	/**
	 * 디비로 창고 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            Log type 0=개인넣기 1=개인찾기 2=요정넣기 3= 요정찾기 4=웹창고
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */
	public static boolean logWarehouse(L1PcInstance pc, L1ItemInstance item, int count, int type) {
		String sTemp = StringUtil.EmptyString;
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		try {
			/*switch (type) {
			case 0:
				sTemp = "개인넣기";
				break;
			case 1:
				sTemp = "개인찾기";
				break;
			case 2:
				sTemp = "요정넣기";
				break;
			case 3:
				sTemp = "요정찾기";
				break;
			case 4:
				sTemp = "웹창고";
				break;
			}*/
			
			switch (type) {
				case 0:
					sTemp = "Personal Deposit";
					break;
				case 1:
					sTemp = "Personal Withdraw";
					break;
				case 2:
					sTemp = "Elf Deposit";
					break;
				case 3:
					sTemp = "Elf Withdraw";
					break;
				case 4:
					sTemp = "Web Warehouse";
					break;
			}			

			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_warehouse SET type=?, account=?, char_id=?, char_name=?, item_id=?, item_name=?, item_enchantlvl=?, item_count=?, datetime=SYSDATE()");
			p.setString(1, sTemp);
			p.setString(2, pc.getAccountName());
			p.setInt(3, pc.getId());
			p.setString(4, pc.getName());
			p.setInt(5, item.getId());
			//p.setString(6, item.getDescKr());
			p.setString(6, item.getDescEn());
			p.setInt(7, item.getEnchantLevel());
			p.setInt(8, count);
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p, c);
		}
		return bool;
	}

	/**
	 * 디비로 클랜 창고 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            Log type 0=넣기 1=찾기
	 * @return
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */
	public static boolean logClanWarehouse(L1PcInstance pc, L1ItemInstance item,int count, int type) {
		String sTemp = StringUtil.EmptyString;
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		try {
			/*switch (type) {
			case 0:
				sTemp = "넣기";
				break;
			case 1:
				sTemp = "찾기";
				break;
			}*/
			
			switch (type) {
				case 0:
					sTemp = "Deposit";
					break;
				case 1:
					sTemp = "Withdraw";
					break;
			}

			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_cwarehouse SET type=?, clan_id=?, clan_name=?, account=?, char_id=?, char_name=?, item_id=?, item_name=?, item_enchantlvl=?, item_count=?, datetime=SYSDATE()");

			p.setString(1, sTemp);
			p.setInt(2, pc.getClanid());
			p.setString(3, pc.getClanName());
			p.setString(4, pc.getAccountName());
			p.setInt(5, pc.getId());
			p.setString(6, pc.getName());
			p.setInt(7, item.getId());
			//p.setString(8, item.getDescKr());
			p.setString(8, item.getDescEn());
			p.setInt(9, item.getEnchantLevel());
			p.setInt(10, count);
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p, c);
		}
		return bool;
	}

	/**
	 * 디비로 개인상점 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            0=구입 1=판매
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */
	public static boolean logShop(L1PcInstance pc, L1PcInstance targetPc, L1ItemInstance item, int price, int count, int type) {
		String sTemp = StringUtil.EmptyString;
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		int tPrice = price * count;
		try {
			/*switch (type) {
			case 0:
				sTemp = "개인상점 - 구매";
				break;
			case 1:
				sTemp = "개인상점 - 판매";
				break;
			}*/

			switch (type) {
				case 0:
					sTemp = "Personal Shop - Purchase";
					break;
				case 1:
					sTemp = "Personal Shop - Sale";
					break;
			}		

			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_private_shop SET time=SYSDATE(), type=?, shop_account=?, shop_id=?, shop_name=?, user_account=?, user_id=?, user_name=?, item_id=?, item_name=?, item_enchantlvl=?, price=?, item_count=?, total_price=?");
			p.setString(1, sTemp);
			p.setString(2, targetPc.getAccountName());// 상점 계정
			p.setInt(3, targetPc.getId());// 상점 아이디
			p.setString(4, targetPc.getName());// 상점 네임
			p.setString(5, pc.getAccountName());// 주체자 계정
			p.setInt(6, pc.getId());// 주체자 아이디
			p.setString(7, pc.getName());// 주체자 이름
			p.setInt(8, item.getId());
			//p.setString(9, item.getDescKr());
			p.setString(9, item.getDescEn());
			p.setInt(10, item.getEnchantLevel());
			p.setInt(11, price);
			p.setInt(12, count);
			p.setInt(13, tPrice);
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p, c);
		}
		return bool;
	}

	/**
	 * 디비로 상점 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            0=구입 1=판매
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */
	public static boolean logNpcShop(L1PcInstance pc, int npcid, L1ItemInstance item, int price, int count, int type) {
		String sTemp = StringUtil.EmptyString;
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		int tPrice = price * count;
		try {
			/*switch (type) {
			case 0:
				sTemp = "상점 - 구매";
				break;
			case 1:
				sTemp = "상점 - 판매";
				break;
			}*/

			switch (type) {
				case 0:
					sTemp = "Shop - Purchase";
					break;
				case 1:
					sTemp = "Shop - Sale";
					break;
			}			

			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_shop SET time=SYSDATE(), type=?, npc_id=?, user_account=?, user_id=?, user_name=?, item_id=?, item_name=?, item_enchantlvl=?, price=?, item_count=?, total_price=?");
			p.setString(1, sTemp);
			p.setInt(2, npcid);// 상점 계정
			p.setString(3, pc.getAccountName());// 주체자 계정
			p.setInt(4, pc.getId());// 주체자 아이디
			p.setString(5, pc.getName());// 주체자 이름
			p.setInt(6, item.getId());
			//p.setString(7, item.getDescKr());
			p.setString(7, item.getDescEn());
			p.setInt(8, item.getEnchantLevel());
			p.setInt(9, price);
			p.setInt(10, count);
			p.setInt(11, tPrice);
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p, c);
		}
		return bool;
	}
	
	/**
	 * 디비로 상점 로그를 기록(혈맹 상점)
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            0=구입 1=판매
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */
	public static boolean logNpcShopClan(L1PcInstance pc, int npcid, int itemObjId, String itemName, int enchant, int price, int count, int type) {
		String sTemp = StringUtil.EmptyString;
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		int tPrice = price * count;
		try {

			/*switch (type) {
			case 0:
				sTemp = "상점 - 구매";
				break;
			case 1:
				sTemp = "상점 - 판매";
				break;
			}*/

			switch (type) {
				case 0:
					sTemp = "Shop - Purchase";
					break;
				case 1:
					sTemp = "Shop - Sale";
					break;
			}
		
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_shop SET time=SYSDATE(), type=?, npc_id=?, user_account=?, user_id=?, user_name=?, item_id=?, item_name=?, item_enchantlvl=?, price=?, item_count=?, total_price=?");
			p.setString(1, sTemp);
			p.setInt(2, npcid);// 상점 계정
			p.setString(3, pc.getAccountName());// 주체자 계정
			p.setInt(4, pc.getId());// 주체자 아이디
			p.setString(5, pc.getName());// 주체자 이름
			p.setInt(6, itemObjId);
			p.setString(7, itemName);
			p.setInt(8, enchant);
			p.setInt(9, price);
			p.setInt(10, count);
			p.setInt(11, tPrice);
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p, c);
		}
		return bool;
	}

}
