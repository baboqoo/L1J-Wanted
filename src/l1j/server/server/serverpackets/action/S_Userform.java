package l1j.server.server.serverpackets.action;

import l1j.server.common.data.UserFormType;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_Userform extends ServerBasePacket {
	private static final String S_USER_FORM = "[S] S_Userform";
	private byte[] _byte = null;
	public static final int NOTI			= 0x0957;
	public static final int DELAY			= 0x0958;
	
	public static final S_Userform NONE		= new S_Userform(UserFormType.USER_FORM_0);
	public static final S_Userform LONG		= new S_Userform(UserFormType.USER_FORM_1);
	public static final S_Userform REFRESH	= new S_Userform();
	
	public S_Userform(UserFormType form_type) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
		writeC(0x08);// form_type
		writeC(form_type.toInt());
        writeH(0x00);
	}
	
	public S_Userform() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(DELAY);
		writeC(0x08);// delay
		writeBit(1000);
        writeH(0x00);
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
		return S_USER_FORM;
	}
}

