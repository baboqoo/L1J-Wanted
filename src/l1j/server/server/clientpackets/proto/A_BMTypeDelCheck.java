package l1j.server.server.clientpackets.proto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.inventory.S_BMTypeDelCheck;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class A_BMTypeDelCheck extends ProtoHandler {
	protected A_BMTypeDelCheck(){}
	private A_BMTypeDelCheck(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private String _char_name;

	@Override
	protected void doWork() throws Exception {
		readP(1);// 0x0a
		int length = readC();
		_char_name = readS(length);// charName
		if (StringUtil.isNullOrEmpty(_char_name)) {
			System.out.println(String.format("[A_BMTypeDelCheck] NULL_OR_EMPTY_NAME : ACCOUNT(%s)", _client.getAccountName()));
			return;
		}
		L1PcInstance pc = L1World.getInstance().getPlayer(_char_name);
		if (pc != null) {
			_client.sendPacket(S_BMTypeDelCheck.INVALID);
			return;
		}
		int bmFlag = getBMFlag();
		switch (bmFlag) {
		case 0:// 캐릭터 없음
			_client.sendPacket(S_BMTypeDelCheck.INVALID);
			System.out.println(String.format("[A_BMTypeDelCheck] NOT_FOUND_NAME : ACCOUNT(%s), CHAR_NAME(%s)", _client.getAccountName(), _char_name));
			break;
		case 1:// 가능
			_client.sendPacket(S_BMTypeDelCheck.SUCCESS);
			break;
		default:// BM 적용중
			_client.sendPacket(S_BMTypeDelCheck.FAILURE);
			break;
		}
	}
	
	/**
	 * BM성 아이템이 적용중인지 조사한다.
	 * @return flag
	 */
	int getBMFlag() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT 1 + (EinhasadGraceTime IS NOT NULL AND EinhasadGraceTime > NOW()) AS flag FROM characters WHERE char_name = ? LIMIT 1");
			pstm.setString(1, _char_name);
			rs = pstm.executeQuery();
			if (rs.next()) {
				return rs.getInt("flag");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_BMTypeDelCheck(data, client);
	}

}

