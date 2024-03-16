package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.RevengeTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_MessegeNoti;
import l1j.server.server.serverpackets.revenge.S_RevengeInfo;
import l1j.server.server.serverpackets.revenge.S_RevengeTaunt;
import l1j.server.server.serverpackets.revenge.eRevengeResult;
import l1j.server.server.utils.StringUtil;

public class A_RevengeTaunt extends ProtoHandler {
	protected A_RevengeTaunt(){}
	private A_RevengeTaunt(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _server_no;
	private String _user_name;
	
	void parse() {
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_server_no = read4(read_size());
				break;
			case 0x12:
				int name_length = readC();
				if (name_length > 0) {
					_user_name = readS(name_length);
				}
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidataion() {
		return _server_no >= 0 && !StringUtil.isNullOrEmpty(_user_name);
	}
	
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		if (!isValidataion()) {
			return;
		}
		RevengeTable revenge = RevengeTable.getInstance();
		if (revenge.getActionRemainCount(_pc, _user_name) < 1) {
			return;
		}
		if (!_pc.getInventory().consumeItem(L1ItemId.ADENA, Config.REVENGE.REVENGE_ACTION_COST)) {
			return;
		}
		revenge.startTaunt(_pc, _user_name);
		L1World.getInstance().broadcastPacketToAll(new S_MessegeNoti(7040, _pc.getName(), _user_name, 0), true);// %s (이)가, %s (을)를 제압하였습니다.
		_pc.sendPackets(new S_RevengeTaunt(eRevengeResult.SUCCESS), true);
		_pc.sendPackets(new S_RevengeInfo(_pc), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_RevengeTaunt(data, client);
	}

}

