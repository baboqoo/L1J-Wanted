package l1j.server.server.serverpackets.polymorph;

import l1j.server.common.data.ePolymorphAnonymityType;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PolymorphAnonymityNoti extends ServerBasePacket {
	private static final String S_POLYMORPH_ANONYMITY_NOTI = "[S] S_PolymorphAnonymityNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x019c;
	
	public S_PolymorphAnonymityNoti(L1PcInstance pc){
		write_init();
		write_object_id(pc.getId());
		write_anonymity_type(pc.getConfig().getAnonymityType());
		write_real_name(pc.getName());
		write_anonymity_name(pc.getConfig().getAnonymityName());
		write_char_class(pc.getType());
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_object_id(int object_id) {
		writeRaw(0x08);// object_id
		writeBit(object_id);
	}
	
	void write_anonymity_type(ePolymorphAnonymityType anonymity_type) {
		writeRaw(0x10);// anonymity_type
		writeRaw(anonymity_type.toInt());
	}
	
	void write_real_name(String real_name) {
		writeRaw(0x1a);// real_name
		writeStringWithLength(real_name);
	}
	
	void write_anonymity_name(String anonymity_name) {
		writeRaw(0x22);// anonymity_name
		writeStringWithLength(anonymity_name);
	}
	
	void write_char_class(int char_class) {
		writeRaw(0x28);// char_class
		writeRaw(char_class);
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
		return S_POLYMORPH_ANONYMITY_NOTI;
	}
}

