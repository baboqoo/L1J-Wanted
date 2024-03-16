package l1j.server.server.model.item.collection;

public class L1CollectionModel {
	public static final int TYPE_RUN				= 1;
	public static final int TYPE_HP_GRACE			= 2;
	public static final int TYPE_DEFEND_GRACE		= 3;
	public static final int TYPE_TOLERANCE_GRACE	= 4;
	public static final int TYPE_HITUP_GRACE		= 5;
	public static final int TYPE_GROW_GRACE			= 6;
	public static final int TYPE_WAR_GRACE			= 7;
	public static final int TYPE_DOMINATION_SOUL	= 8;
	public static final int TYPE_BLACK_TIGER		= 9;
	public static final int TYPE_ADEN_FAST_GRACE	= 10;
	public static final int TYPE_OMAN_GRACE			= 11;
	public static final int TYPE_BRAVE_GRACE		= 12;
	public static final int TYPE_GUARD_GRACE		= 13;
	
	private int _type;
	private int _itemId;
	private int[] _sameItems;
	
	public L1CollectionModel(int _type, int _itemId, int[] _sameItems) {
		this._type		= _type;
		this._itemId	= _itemId;
		this._sameItems	= _sameItems;
	}
	
	public int getType() {
		return _type;
	}
	public int getItemId() {
		return _itemId;
	}
	public int[] getSameItems(){
		return _sameItems;
	}
}

