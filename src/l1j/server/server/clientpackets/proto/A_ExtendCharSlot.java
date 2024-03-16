package l1j.server.server.clientpackets.proto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.S_CharSlotExtend;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class A_ExtendCharSlot extends ProtoHandler {
	protected A_ExtendCharSlot(){}
	private A_ExtendCharSlot(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_client == null) {
			return;
		}
		int slotCount = _client.getAccount().getCharSlot();
		if (slotCount >= Config.SERVER.CHARACTER_SLOT_MAX_COUNT) {
			return;
		}
		String use_item_char = checkItem(_client.getAccount().getName());
		if (StringUtil.isNullOrEmpty(use_item_char)) {
			_client.sendPacket(S_CharSlotExtend.ITEM_EMPTY);
			return;
		}
		_client.getAccount().updateCharSlot(slotCount + 1);
		S_CharSlotExtend extend = new S_CharSlotExtend(2, null, 8393);
		_client.sendPacket(extend);
		extend.clear();
		extend = null;
	}
	
	String checkItem(String accountName){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		PreparedStatement itemPstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_warehouse WHERE id = (SELECT C.* FROM (SELECT COALESCE(B.id, 0) FROM character_warehouse B WHERE B.item_id = '210083' AND B.account_name = ? LIMIT 1) AS C)");
			pstm.setString(1, accountName);
			if (pstm.executeUpdate() > 0) {
				return "창고";
			}
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("SELECT T.id, C.char_name FROM (SELECT id, char_id FROM character_items WHERE item_id = '210083' AND char_id IN (SELECT objid FROM characters WHERE account_name = ?) LIMIT 1) T, characters C WHERE T.char_id = C.objid");
			pstm.setString(1, accountName);
			rs = pstm.executeQuery();
			if (rs.next()) {
				int id			= rs.getInt("id");
				String charName	= rs.getString("char_name");
				itemPstm = con.prepareStatement("DELETE FROM character_items WHERE id = ?");
				itemPstm.setInt(1, id);
				if (itemPstm.executeUpdate() > 0) {
					return charName;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(itemPstm);
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ExtendCharSlot(data, client);
	}

}

