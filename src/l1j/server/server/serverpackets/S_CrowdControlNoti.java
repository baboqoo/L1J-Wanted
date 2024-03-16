package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CrowdControlNoti extends ServerBasePacket {
	private static final String S_CROWD_CONTROL_NOTI = "[S] S_CrowdControlNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x024f;

	public S_CrowdControlNoti(int target_id, boolean teleport_impossible, boolean block_teleport, boolean block_change_equip,
			boolean block_invisibillity, boolean block_attack, boolean block_use_spell, boolean block_use_item_without_potion) {
		write_init();
		write_target_id(target_id);
		write_teleport_impossible(teleport_impossible);	// 텔레포트 불가능
		write_block_teleport(block_teleport);			// 텔레포트 막기
		write_block_change_equip(block_change_equip);	// 아이템 탈/착 막기
		write_block_invisibillity(block_invisibillity);	// 인비지 사용 막기
		write_block_attack(block_attack);				// 공격 불가
		write_block_use_spell(block_use_spell);			// 스킬 사용 막기
		write_block_use_item_without_potion(block_use_item_without_potion);// 아이템 사용 막기 물약 제외
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_target_id(int target_id) {
		writeRaw(0x08);
		writeBit(target_id);
	}
	
	void write_teleport_impossible(boolean teleport_impossible) {
		writeRaw(0x10);
		writeB(teleport_impossible);
	}
	
	void write_block_teleport(boolean block_teleport) {
		writeRaw(0x18);
		writeB(block_teleport);
	}
	
	void write_block_change_equip(boolean block_change_equip) {
		writeRaw(0x20);
		writeB(block_change_equip);
	}
	
	void write_block_invisibillity(boolean block_invisibillity) {
		writeRaw(0x28);
		writeB(block_invisibillity);
	}
	
	void write_block_attack(boolean block_attack) {
		writeRaw(0x30);
		writeB(block_attack);
	}
	
	void write_block_use_spell(boolean block_use_spell) {
		writeRaw(0x38);
		writeB(block_use_spell);
	}
	
	void write_block_use_item_without_potion(boolean block_use_item_without_potion) {
		writeRaw(0x40);
		writeB(block_use_item_without_potion);
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
		return S_CROWD_CONTROL_NOTI;
	}
}

