package l1j.server.server.serverpackets.message;

import l1j.server.server.utils.BinaryOutputStream;

public class NotificationEventNpcInfoStream extends BinaryOutputStream {

	public NotificationEventNpcInfoStream(int npc_id, byte[] displaydesc, int rest_gauge_bonus_display) {
		super();
		write_npc_id(npc_id);
		write_displaydesc(displaydesc);
		if (rest_gauge_bonus_display > 0) {
			write_rest_gauge_icon_display(true);
			write_rest_gauge_bonus_display(rest_gauge_bonus_display);
		}
	}
	
	void write_npc_id(int npc_id) {
		writeC(0x08);// npc_id
		writeBit(npc_id);
	}
	
	void write_displaydesc(byte[] displaydesc) {
		writeC(0x12);// displaydesc
		writeBytesWithLength(displaydesc);
	}
	
	void write_rest_gauge_icon_display(boolean rest_gauge_icon_display) {
		writeC(0x18);// rest_gauge_icon_display
		writeB(rest_gauge_icon_display);
	}
	
	void write_rest_gauge_bonus_display(int rest_gauge_bonus_display) {
		writeC(0x20);// rest_gauge_bonus_display
	    writeBit(rest_gauge_bonus_display);
	}
}

