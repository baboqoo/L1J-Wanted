package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class S_SPMR extends ServerBasePacket {
	private static final String S_SPMR = "[S] S_S_SPMR";
	private byte[] _byte = null;

	public S_SPMR(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_MAGIC_STATUS);
		// 위즈 댐 일부의 SP는 S_SkillBrave 송신시에 갱신되기 (위해)때문에 공제해 둔다
		if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_WISDOM_POTION)) {
			writeH(pc.getAbility().getSp() - pc.getAbility().getTrueSp() - 2); // 장비 증가한 SP
		} else {
			writeH(pc.getAbility().getSp() - pc.getAbility().getTrueSp()); // 장비 증가한 SP
		}
		writeH(pc.getResistance().getMr() - pc.getResistance().getBaseMr()); // 장비나 마법으로 증가한 MR
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	@Override
	public String getType() {
		return S_SPMR;
	}
}

