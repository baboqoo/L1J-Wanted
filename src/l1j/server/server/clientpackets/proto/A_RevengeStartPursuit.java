package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.controller.RevengePursuitController;
import l1j.server.server.datatables.RevengeTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.revenge.S_RevengeStartPursuit;
import l1j.server.server.serverpackets.revenge.S_RevengeInfo;
import l1j.server.server.serverpackets.revenge.eRevengeResult;
import l1j.server.server.utils.StringUtil;

public class A_RevengeStartPursuit extends ProtoHandler {
	protected A_RevengeStartPursuit(){}
	private A_RevengeStartPursuit(byte[] data, GameClient client) {
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
			return;
		}
		L1PcInstance target	= L1World.getInstance().getPlayer(_user_name);
		if (target == null) {
			_pc.sendPackets(new S_RevengeStartPursuit(_user_name, eRevengeResult.FAIL_USER), true);// 대상이 없을때
			return;
		}
		boolean pre = !StringUtil.isNullOrEmpty(_pc.getRevengeTarget());
		if (pre && _pc.getRevengeTarget().equals(target.getName())) {// 같은 대상을 이미 추적중일때
			_pc.sendPackets(new S_RevengeStartPursuit(_user_name, eRevengeResult.FAIL_ALREADY_PURSUITING), true);
			return;
		}
		RevengeTable revenge = RevengeTable.getInstance();
		if (revenge.getActionRemainCount(_pc, _user_name) < 1) {// 모두사용 햇을때
			_pc.sendPackets(new S_RevengeStartPursuit(_user_name, eRevengeResult.FAIL_COUNT), true);
			return;
		}
		if (!_pc.getInventory().consumeItem(L1ItemId.ADENA, Config.REVENGE.REVENGE_ACTION_COST)) {// 아데나가 없을때
			_pc.sendPackets(new S_RevengeStartPursuit(_user_name, eRevengeResult.FAIL_COST), true);
			return;
		}
		if (pre) {
			revenge.endTargetPursuit(_pc, _pc.getRevengeTarget());// 기존 추적이 있을때
		}
		_pc.setRevengeTarget(target.getName());
		revenge.startPursuit(_pc, target.getName());
		_pc.sendPackets(new S_RevengeStartPursuit(_user_name, eRevengeResult.SUCCESS), true);
		_pc.sendPackets(new S_RevengeInfo(_pc), true);
		GeneralThreadPool.getInstance().schedule(new RevengePursuitController(_pc), Config.REVENGE.REVENGE_PURSUIT_DURATION_SECOND * 1000L);// 10분
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_RevengeStartPursuit(data, client);
	}

}

