package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class S_EventCountdown extends ServerBasePacket {
	private byte[] _byte = null;
	public static final int COUNTDOWN	= 0x021c;

	public S_EventCountdown(int value, String desc, int type) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(COUNTDOWN);
		
		writeC(0x08);// remain time
		writeBit(value);
		
		writeC(0x10);// desc
		if (StringUtil.isNullOrEmpty(desc)) {
			writeC(0x00);
		} else {
			try {
				writeBytesWithLength(desc.getBytes(CharsetUtil.MS_949));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		writeC(0x18);// timer type -> 1:map timer, 2:content timer
		writeC(type);
		
		writeH(0x00);
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
		return "S_EventCountdown";
	}
}

