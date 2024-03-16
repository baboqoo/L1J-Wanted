package l1j.server.GameSystem.tjcoupon.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;

public class TJCouponBean {
	private int objId;
	private int charId;
	private int itemId;
	private int count;
	private int enchantLevel;
	private int attrLevel;
	private int bless;
	private Timestamp lostTime;
	private L1ItemInstance item;
	
	public TJCouponBean(ResultSet rs)throws SQLException {
		this(rs.getInt("objId"), rs.getInt("charId"), rs.getInt("itemId"), rs.getInt("count"), rs.getInt("enchantLevel"),
				rs.getInt("attrLevel"), rs.getInt("bless"), rs.getTimestamp("lostTime"));
	}
	
	public TJCouponBean(int objId, int charId, int itemId, int count, int enchantLevel, int attrLevel, int bless, Timestamp lostTime) {
		this.objId			= objId;
		this.charId			= charId;
		this.itemId			= itemId;
		this.count			= count;
		this.enchantLevel	= enchantLevel;
		this.attrLevel		= attrLevel;
		this.bless			= bless;
		this.lostTime		= lostTime;
		this.item			= ItemTable.getInstance().createItem(itemId, objId);
		if (this.item != null) {
			this.item.setIdentified(true);
			this.item.setCount(this.count);
			this.item.setEnchantLevel(this.enchantLevel);
			this.item.setAttrEnchantLevel(this.attrLevel);
			this.item.setBless(this.bless);
		}
	}

	public int getObjId() {
		return objId;
	}

	public void setObjId(int objId) {
		this.objId = objId;
	}

	public int getCharId() {
		return charId;
	}

	public void setCharId(int charId) {
		this.charId = charId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getEnchantLevel() {
		return enchantLevel;
	}

	public void setEnchantLevel(int enchantLevel) {
		this.enchantLevel = enchantLevel;
	}

	public int getAttrLevel() {
		return attrLevel;
	}

	public void setAttrLevel(int attrLevel) {
		this.attrLevel = attrLevel;
	}

	public int getBless() {
		return bless;
	}

	public void setBless(int bless) {
		this.bless = bless;
	}

	public Timestamp getLostTime() {
		return lostTime;
	}

	public void setLostTime(Timestamp lostTime) {
		this.lostTime = lostTime;
	}

	public L1ItemInstance getItem() {
		return item;
	}

	public void setItem(L1ItemInstance item) {
		this.item = item;
	}
}

