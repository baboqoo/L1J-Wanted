package l1j.server.server.serverpackets.companion;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_CompanionBuffNoti extends ServerBasePacket {
	private static final String S_COMPANION_BUFF_NOTI = "[S] S_CompanionBuffNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x07d5;
	  
	public S_CompanionBuffNoti(int spell_id, int duration) {
		write_init();
		write_buff_list(spell_id, duration);
		writeH(0x00);
	}
	
	public S_CompanionBuffNoti(java.util.LinkedList<S_CompanionBuffNoti.Buff> buff_list) {
		write_init();
		write_buff_list(buff_list);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_buff_list(int spell_id, int duration) {
		writeC(0x0a);
		writeC(getBitSize(spell_id) + getBitSize(duration) + 2);
		
		writeC(0x08);
		writeBit(spell_id);
		
		writeC(0x10);
		writeBit(duration);
	}
	
	void write_buff_list(java.util.LinkedList<S_CompanionBuffNoti.Buff> buff_list) {
		for (S_CompanionBuffNoti.Buff buff : buff_list) {
			writeRaw(0x0a);
			writeBytesWithLength(buff.getBytes());
			buff.dispose();
		}
		buff_list.clear();
		buff_list = null;
	}
	
	public static class Buff extends BinaryOutputStream {
		public Buff(int spell_id, int duration) {
			super();
			write_spell_id(spell_id);
			write_duration(duration);
		}
		
		void write_spell_id(int spell_id) {
			writeC(0x08);
			writeBit(spell_id);
		}
		
		void write_duration(int duration) {
			writeC(0x10);
			writeBit(duration);
		}
		
		void dispose() {
			try {
				this.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public byte[] getContent() {
	    if (_byte == null) {
	    	_byte = getBytes();
	    }
	    return _byte;
	}
  
    @Override
	public String getType() {
		return S_COMPANION_BUFF_NOTI;
    }
}
