package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.AutoLoot;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class L1AutoLoot implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AutoLoot();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1AutoLoot() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			
			String type = st.nextToken();
			//if (type.equalsIgnoreCase("리로드")) {
				if (type.equalsIgnoreCase("reload")) {
				AutoLoot.getInstance().reload();
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("오토루팅 설정이 리로드 되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(199), true), true);
				return true;
			}
			
			//if (type.equalsIgnoreCase("검색")) {
			if (type.equalsIgnoreCase("search")) {
				Connection con = null;
				PreparedStatement pstm = null;
				ResultSet rs = null;

				String desc = st.nextToken();
				try {
					con = L1DatabaseFactory.getInstance().getConnection();
					String strQry;
					strQry = " SELECT e.item_id, e.desc_kr FROM etcitem e, autoloot l WHERE l.item_id = e.item_id AND e.desc_kr LIKE '%" + desc + "%' ";
					strQry += " UNION ALL "
							+ " SELECT w.item_id, w.desc_kr FROM weapon w, autoloot l WHERE l.item_id = w.item_id AND w.desc_kr LIKE '%" + desc + "%' ";
					strQry += " UNION ALL "
							+ " SELECT a.item_id, a.desc_kr FROM armor a, autoloot l WHERE l.item_id = a.item_id AND a.desc_kr LIKE '%" + desc + "%' ";
					pstm = con.prepareStatement(strQry);
					rs = pstm.executeQuery();
					while (rs.next()) {
						pc.sendPackets(new S_SystemMessage("[" + rs.getString("item_id") + "] " + rs.getString("desc_kr")), true);
					}
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					SQLUtil.close(rs, pstm, con);
				}
				return false;
			}
			
			String desc = st.nextToken();
			int itemid = 0;
			try {
				itemid = Integer.parseInt(desc);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByDescWithoutSpace(desc);
				if (itemid == 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("해당 아이템이 발견되지 않습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(200), true), true);
					return false;
				}
			}

			L1Item temp = ItemTable.getInstance().getTemplate(itemid);
			if (temp == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("해당 아이템이 발견되지 않습니다. "), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(200), true), true);
				return false;
			}
			
			//if (type.equalsIgnoreCase("추가")) {
			if (type.equalsIgnoreCase("add")) {
				if (AutoLoot.isAutoLoot(itemid)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 오토루팅 목록에 있습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(201), true), true);
					return false;
				}
				AutoLoot.getInstance().storeId(itemid);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("오토루팅 항목에 추가 했습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(202), true), true);
				return true;
			}
			//if (type.equalsIgnoreCase("삭제")) {
			if (type.equalsIgnoreCase("delete")) {
				if (!AutoLoot.isAutoLoot(itemid)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("오토루팅 항목에 해당 아이템이 없습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(203), true), true);
					return false;
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("오토루팅 항목에서 삭제 했습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(204), true), true);
				AutoLoot.getInstance().deleteId(itemid);
				return true;
			}
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 리로드"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(205), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 추가|삭제 itemid|name"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(206), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 검색 [name]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(207), true), true);
			return false;
		}
	}
}


