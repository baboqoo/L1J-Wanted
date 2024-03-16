package l1j.server.server.serverpackets.companion;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PetMenuPacket extends ServerBasePacket {
	private byte[] _byte = null;

	public S_PetMenuPacket(L1NpcInstance npc, int exppercet) {
		buildpacket(npc, exppercet);
	}

	private void buildpacket(L1NpcInstance npc, int exppercet) {
		writeC(Opcodes.S_HYPERTEXT);
		if (npc instanceof L1SummonInstance) {
			L1SummonInstance summon = (L1SummonInstance) npc;
			writeD(summon.getId());
			writeS("moncom");
			writeC(0x00);
			writeH(6); // 건네주는 인수 캐릭터의 수의 모양
			switch (summon.getCurrentStatus()) {
			case 1:
				writeS("$469"); // 공격 태세
				break;
			case 2:
				writeS("$470"); // 방어 태세
				break;
			case 3:
				writeS("$471"); // 휴게
				break;
			case 5:
				writeS("$472"); // 경계
				break;
			default:
				writeS("$471"); // 휴게
				break;
			}
			writeS(Integer.toString(summon.getCurrentHp())); // 현재의 HP
			writeS(Integer.toString(summon.getMaxHp())); // 최대 HP
			writeS(Integer.toString(summon.getCurrentMp())); // 현재의 MP
			writeS(Integer.toString(summon.getMaxMp())); // 최대 MP
			writeS(Integer.toString(summon.getLevel())); // 레벨
			// writeS(summon.getNpcTemplate().get_nameid());
			// writeS(Integer.toString(0));
			// writeS(Integer.toString(790));
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}
}

