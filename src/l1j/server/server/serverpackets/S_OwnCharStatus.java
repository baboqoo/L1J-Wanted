package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.utils.IntRange;

public class S_OwnCharStatus extends ServerBasePacket {
	private static final String S_OWN_CHAR_STATUS = "[S] S_OwnCharStatus";
	private byte[] _byte = null;
	private static final IntRange LEVEL_RANGE = new IntRange(1, ExpTable.MAX_LEVEL);

	public S_OwnCharStatus(L1PcInstance pc) {
		long time = GameTimeClock.getInstance().getGameTime().getSeconds();
		time = time - (time % 300);
		writeC(Opcodes.S_STATUS);
		writeD(pc.getId());
		writeC(LEVEL_RANGE.ensure(pc.getLevel()));
		writeD(pc.getExp());
		writeH(pc.getAbility().getTotalStr());
		writeH(pc.getAbility().getTotalInt());
		writeH(pc.getAbility().getTotalWis());
		writeH(pc.getAbility().getTotalDex());
		writeH(pc.getAbility().getTotalCon());
		writeH(pc.getAbility().getTotalCha());
		writeH(pc.getCurrentHp());
		writeH(pc.getMaxHp());
		writeH(pc.getCurrentMp());
		writeH(pc.getMaxMp());
		writeD((int) time);
		writeC(pc.getFood());
		writeC(pc.getInventory().getWeightPercent());
		writeH(pc.getAlignment());
		writeH(pc.getResistance().getFire());
		writeH(pc.getResistance().getWater());
		writeH(pc.getResistance().getWind());
		writeH(pc.getResistance().getEarth());
		writeD(pc.getMonsterKill());
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}
	@Override
	public String getType() {
		return S_OWN_CHAR_STATUS;
	}
}
