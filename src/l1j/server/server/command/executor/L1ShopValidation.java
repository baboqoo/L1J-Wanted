package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1ShopValidation implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ShopValidation();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ShopValidation() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			ArrayList<Integer> itemids = new ArrayList<Integer>();
			Connection con			= null;
			PreparedStatement pstm	= null;
			ResultSet rs			= null;
			Iterator<Integer> it;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement("SELECT item_id FROM shop");
				rs		= pstm.executeQuery();
				while (rs.next()) {
					if (!itemids.contains(Integer.valueOf(rs.getInt("item_id")))) {
						itemids.add(Integer.valueOf(rs.getInt("item_id")));
					}
				}
				for (it = itemids.iterator(); it.hasNext();) {
					int itemid = ((Integer) it.next()).intValue();
					//int 구매최저가 = 최소값(itemid);
					//int 판매최고가 = 최대값(itemid);
					//if ((구매최저가 != 0) && (구매최저가 < 판매최고가)) {
					int minPurchasePrice = getMinimumValue(itemid);
					int maxSalePrice = getMaximumValue(itemid);					
					if ((minPurchasePrice != 0) && (minPurchasePrice < maxSalePrice)) {					
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("검출됨! [템 " + itemid + " : [구매값 " + 구매최저가 + "] [매입값 " + 판매최고가 + "]"), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(671) + itemid  + S_SystemMessage.getRefText(672) + minPurchasePrice  + S_SystemMessage.getRefText(673) + maxSalePrice  + "]", true), true);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
				itemids.clear();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//private int 최소값(int itemid) {
	private int getMinimumValue(int itemid) {
	    try {
	        Connection con = null;
	        PreparedStatement pstm = null;
	        ResultSet rs = null;
	        try {
	            con = L1DatabaseFactory.getInstance().getConnection();
	            pstm = con.prepareStatement("SELECT * FROM shop WHERE item_id = ? AND selling_price NOT IN (-1) ORDER BY selling_price ASC LIMIT 1");
	            pstm.setInt(1, itemid);
	            rs = pstm.executeQuery();
	            if (rs.next()) {
	                int temp = 0;
	                if (rs.getInt("pack_count") > 1)
	                    temp = rs.getInt("selling_price") / rs.getInt("pack_count");
	                else
	                    temp = rs.getInt("selling_price");
	                int i = temp;
	                return i;
	            }
	        } catch (SQLException e){
	            e.printStackTrace();
	        } finally {
	            SQLUtil.close(rs, pstm, con);
	        }
	        return 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 
	    return 0;
	}

	//private int 최대값(int itemid) {
	private int getMaximumValue(int itemid) {
	    try {
	        Connection con = null;
	        PreparedStatement pstm = null;
	        ResultSet rs = null;
	        try {
	            con = L1DatabaseFactory.getInstance().getConnection();
	            pstm = con.prepareStatement("SELECT purchasing_price FROM shop WHERE item_id = ? ORDER BY purchasing_price DESC LIMIT 1");
	            pstm.setInt(1, itemid);
	            rs = pstm.executeQuery();
	            if (rs.next()) {
	                int i = rs.getInt("purchasing_price");
	                return i;
	            }
	        } catch (SQLException e){
	            e.printStackTrace();
	        } finally {
	            SQLUtil.close(rs, pstm, con);
	        }
	        return -1;
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 
	    return -1;
	}
}


