package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.revenge.S_RevengePursuitInfo;
import l1j.server.server.serverpackets.revenge.eRevengeResult;
import l1j.server.server.utils.StringUtil;

public class A_RevengePursuitInfo extends ProtoHandler {
	protected A_RevengePursuitInfo(){}
	private A_RevengePursuitInfo(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _server_no;
	private String _user_name;
	
	void parse() {
		readP(2);// 0x0a, length
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_server_no = read4(read_size());
				break;
			case 0x12:
				int name_length = readC();
				_user_name = readS(name_length);
				break;
			default:
				return;
			}
		}
	}
	
	boolean is_validation() {
		return _server_no >= 0 && !StringUtil.isNullOrEmpty(_user_name);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		if (!is_validation()) {
			_pc.sendPackets(S_RevengePursuitInfo.FAIL_OTHER);
			return;
		}
		
		L1PcInstance target	= L1World.getInstance().getPlayer(_user_name);
		if (target == null || StringUtil.isNullOrEmpty(_pc.getRevengeTarget()) || !target.getName().equals(_pc.getRevengeTarget())) {
			_pc.sendPackets(S_RevengePursuitInfo.FAIL_USER);
			return;
		}
		_pc.sendPackets(new S_RevengePursuitInfo(_pc.getRevengeTarget(), eRevengeResult.SUCCESS), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_RevengePursuitInfo(data, client);
	}

}

