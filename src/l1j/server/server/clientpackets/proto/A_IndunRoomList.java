package l1j.server.server.clientpackets.proto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.indun.S_IndunRoomList;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class A_IndunRoomList extends ProtoHandler {
	protected A_IndunRoomList(){}
	private A_IndunRoomList(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !Config.INTER.INTER_SERVER_ACTIVE || _client.isInterServer()) {
			return;
		}
		if (CommonUtil.isDayResetTimeCheck(_pc.getAccount().getIndunCheckTime())) {
			if (_pc.getAccount().getIndunCheckTime() != null) {
				_pc.getAccount().getIndunCheckTime().setTime(System.currentTimeMillis());
			} else {
				_pc.getAccount().setIndunCheckTime(new Timestamp(System.currentTimeMillis()));
			}
			_pc.getAccount().setIndunCount(0);
			resetIndun();
		}
		_pc.getConfig()._indunAutoMatching			= false;
		_pc.getConfig()._indunAutoMatchingMapKind	= null;
		_pc.sendPackets(new S_IndunRoomList(), true);
	}
	
	// 인던 이용횟수 초기화
	void resetIndun() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET IndunCheckTime=NOW(), IndunCount=0 WHERE login=?");
			pstm.setString(1, _pc.getAccount().getName());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunRoomList(data, client);
	}

}

