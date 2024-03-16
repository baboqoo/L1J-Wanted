package l1j.server.server.serverpackets.eventpush;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import l1j.server.GameSystem.eventpush.bean.EventPushObject;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;

public class EventPushInfoStream extends BinaryOutputStream {

	public EventPushInfoStream(L1PcInstance pc, EventPushObject temp, int enable_date, int expire_date, L1ItemInstance dummy) {
		super();
		L1Item item = temp.getItem();
		dummy.setItem(item);
		dummy.setEnchantLevel(temp.getItemEnchant());
		dummy.updateItemAbility(pc);
		
		write_event_push_id(temp.getEventPushId());
		write_reset_num(60);
		write_remain_time(enable_date - expire_date);
		write_expire_date(expire_date);
		write_enable_date(enable_date);
		write_item_name_id(item.getItemNameId());
		write_item_amount(temp.getItemAmount());
		write_item_enchant(temp.getItemEnchant());
		write_bless_code(item.getBless());
		write_item_used_immediately(temp.isUsedImmediately());
		write_item_icon(item.getIconId());
		write_item_desc(item.getDesc());
		write_item_extra_desc(dummy.getStatusBytes(pc));
		write_subject(temp.getSubject());
		write_text(temp.getText());
		write_event_push_status(temp.getStatus());
		write_web_url(temp.getWebUrl());
		write_image_id(temp.getImageId());
	}
	
	void write_event_push_id(int event_push_id) {
		writeC(0x08);// event_push_id
		writeBit(event_push_id);
	}
	
	void write_reset_num(int reset_num) {
		writeC(0x10);// reset_num
		writeC(reset_num);
	}
	
	void write_remain_time(int remain_time) {
		writeC(0x18);// remain_time
		writeBit(remain_time);
	}
	
	void write_expire_date(int expire_date) {
		writeC(0x20);// expire_date
		writeBit(expire_date);
	}
	
	void write_enable_date(int enable_date) {
		writeC(0x28);// enable_date
		writeBit(enable_date);
	}
	
	void write_item_name_id(int item_name_id) {
		writeC(0x30);// item_name_id
		writeBit(item_name_id);
	}
	
	void write_item_amount(int item_amount) {
		writeC(0x38);// item_amount
		writeBit(item_amount);
	}
	
	void write_item_enchant(int item_enchant) {
		writeC(0x40);// item_enchant
		writeBit(item_enchant);
	}
	
	void write_bless_code(int bless_code) {
		writeC(0x48);// bless_code
		writeBit(bless_code);
	}
	
	void write_item_used_immediately(boolean item_used_immediately) {
		writeC(0x50);// item_used_immediately
		writeB(item_used_immediately);
	}
	
	void write_item_icon(int item_icon) {
		writeC(0x58);// item_icon
		writeBit(item_icon);
	}
	
	void write_item_desc(String item_desc) {
		writeC(0x62);// item_desc
		writeStringWithLength(item_desc);
	}
	
	void write_item_extra_desc(byte[] item_extra_desc) {
		writeC(0x6a);// item_extra_desc
		writeBytesWithLength(item_extra_desc);
	}
	
	void write_subject(String subject) {
		writeC(0x72);// subject
		ByteBuffer buffer = StandardCharsets.UTF_8.encode(subject);
		writeBit(buffer.limit());
		write(buffer.array(), 0, buffer.limit());
	}
	
	void write_text(String text) {
		writeC(0x7a);// text
		ByteBuffer buffer = StandardCharsets.UTF_8.encode(text);
		writeBit(buffer.limit());
		write(buffer.array(), 0, buffer.limit());
	}
	
	void write_event_push_status(int event_push_status) {
		writeH(0x0180);// event_push_status 0:신규, 1:읽음, 2:받음
		writeC(event_push_status);
	}
	
	void write_web_url(String web_url) {
		writeH(0x018a);// web_url
		writeStringWithLength(web_url);
	}
	
	void write_image_id(int image_id) {
		writeH(0x0190);// image_id
		writeC(image_id);
	}
	
}

