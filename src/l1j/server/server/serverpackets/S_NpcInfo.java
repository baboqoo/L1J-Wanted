package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;

public class S_NpcInfo extends ServerBasePacket {
	private static final String S_NPC_INFO = "[S] S_NpcInfo";
	private byte[] _byte = null;
	public static final int INFO = 0x01CE;
	
	public S_NpcInfo(L1NpcInstance npc) {
		write_init();
		write_npcId(npc.getId());
		write_npcHp(npc.getCurrentHp());
		write_npcMaxHp(npc.getMaxHp());
		write_npcMp(npc.getCurrentMp());
		write_npcMaxMp(npc.getMaxMp());
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_npcId(int npcId) {
		writeRaw(0x08);// npcId
		writeBit(npcId);
	}
	
	void write_npcHp(int npcHp) {
		writeRaw(0x10);// npcHp
		writeBit(npcHp);
	}
	
	void write_npcMaxHp(int npcMaxHp) {
		writeRaw(0x18);// npcMaxHp
		writeBit(npcMaxHp);
	}
	
	void write_npcMp(int npcMp) {
		writeRaw(0x20);// npcMp
		writeBit(npcMp);
	}
	
	void write_npcMaxMp(int npcMaxMp) {
		writeRaw(0x28);// npcMaxMp
		writeBit(npcMaxMp);
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
		return S_NPC_INFO;
	}
}

