package l1j.server.server.clientpackets.proto;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_IndunInvite;

public class A_IndunInvite extends ProtoHandler {
	protected A_IndunInvite(){}
	private A_IndunInvite(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		int roomnumber		= readBit();// 방번호
		readP(1);
		readBit();
		readP(1);
		int targetlength	= readC();
		String targetname	= readS(targetlength);// 타겟 이름
		readP(1);
		int secret			= readC();
		IndunInfo info		= IndunList.getIndunInfo(roomnumber);
		L1PcInstance target	= L1World.getInstance().getPlayer(targetname);
		if (target == null || target.getNetConnection().isInterServer() 
				|| info.getUserInfo(target) != null || target.isNotEnablePc() 
				|| info.min_level > target.getLevel() || !target.getConfig().isIndunInviteOnOff()) {
			return;
		}
		target.sendPackets(new S_IndunInvite(roomnumber, _pc.getName(), target.getName(), secret == 1, true), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunInvite(data, client);
	}

}

