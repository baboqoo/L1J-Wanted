package l1j.server.GameSystem.eventpush.bean;

import java.sql.Timestamp;

import l1j.server.server.templates.L1Item;

public class EventPushObject {
	private int _event_push_id;
	private String _subject;
	private String _text;
	private String _web_url;
	private int _item_id;
	private L1Item _item;
	private int _item_amount;
	private int _item_enchant;
	private boolean _used_immediately;
	private int _status;
	private Timestamp _enable_date;
	private int _image_id;
	
	public EventPushObject(int _event_push_id, String _subject, String _text, String _web_url, 
			int _item_id, L1Item _item, int _item_amount, int _item_enchant, boolean _used_immediately, 
			int _status, Timestamp _enable_date, int _image_id) {
		this._event_push_id		= _event_push_id;
		this._subject			= _subject;
		this._text				= _text;
		this._web_url			= _web_url;
		this._item_id			= _item_id;
		this._item				= _item;
		this._item_amount		= _item_amount;
		this._item_enchant		= _item_enchant;
		this._used_immediately	= _used_immediately;
		this._status			= _status;
		this._enable_date		= _enable_date;
		this._image_id			= _image_id;
	}
	
	public int getEventPushId() {
		return _event_push_id;
	}
	public String getSubject() {
		return _subject;
	}
	public String getText() {
		return _text;
	}
	public String getWebUrl() {
		return _web_url;
	}
	public int getItemId() {
		return _item_id;
	}
	public L1Item getItem() {
		return _item;
	}
	public int getItemAmount() {
		return _item_amount;
	}
	public int getItemEnchant() {
		return _item_enchant;
	}
	public boolean isUsedImmediately() {
		return _used_immediately;
	}
	public int getStatus() {
		return _status;
	}
	public void setStatus(int _status) {
		this._status = _status;
	}
	public Timestamp getEnableDate() {
		return _enable_date;
	}
	public int getImageId() {
		return _image_id;
	}
}

