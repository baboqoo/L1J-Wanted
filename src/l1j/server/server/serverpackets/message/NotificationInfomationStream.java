package l1j.server.server.serverpackets.message;

import l1j.server.common.data.eNotiAnimationType;
import l1j.server.server.utils.BinaryOutputStream;

public class NotificationInfomationStream extends BinaryOutputStream {

	public NotificationInfomationStream() {
		super();
	}
	
	void write_notification_id(int notification_id) {
		writeC(0x08);// notification_id
		writeBit(notification_id);
	}
	
	void write_objectid(int objectid) {
		writeC(0x10);// objectid
		writeBit(objectid);
	}
	
	void write_hyperlink(byte[] hyperlink) {
		writeC(0x1a);// hyperlink
		writeBytesWithLength(hyperlink);
	}
	
	void write_displaydesc(byte[] displaydesc) {
		writeC(0x22);// displaydesc
		writeBytesWithLength(displaydesc);
	}
	
	void write_startdate(long startdate) {
		writeC(0x28);// startdate
		writeBit(startdate);
	}
	
	void write_enddate(long enddate) {
		writeC(0x30);// enddate
		writeBit(enddate);
	}
	
	void write_teleport(byte[] teleport) {
		writeC(0x3a);// teleport
		writeBytesWithLength(teleport);
	}
	
	void write_eventnpc(byte[] eventnpc) {
		writeC(0x42);// eventnpc
		writeBytesWithLength(eventnpc);
	}
	
	void write_rest_gauge_icon_display(boolean rest_gauge_icon_display) {
		writeC(0x48);// rest_gauge_icon_display
		writeB(rest_gauge_icon_display);
	}
	
	void write_rest_gauge_bonus_display(int rest_gauge_bonus_display) {
		writeC(0x50);// rest_gauge_bonus_display
		writeBit(rest_gauge_bonus_display);
	}
	
	void write_new(boolean is_new) {
		writeC(0x58);// is_new
		writeB(is_new);
	}
	
	void write_animation_type(eNotiAnimationType animation_type) {
		writeC(0x60);// animation_type
		writeC(animation_type.toInt());
	}
	
	void write_worlds(int worlds) {
		writeC(0x68);// worlds
		writeBit(worlds);
	}
	
	void write_NotiType(int NotiType) {
		writeC(0x70);// NotiType
		writeBit(NotiType);
	}
	
}

