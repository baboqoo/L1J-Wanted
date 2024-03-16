package l1j.server.server.templates;

import java.sql.ResultSet;
import java.sql.SQLException;

public class L1CatalystCustom {
	private int _itemId;
	private int _input_itemId;
	private int _input_enchant;
	private int _output_itemId;
	private int _successProb;
	private int _rewardCount;
	private int _rewardEnchant;
	private boolean _broad;
	
	public L1CatalystCustom(ResultSet rs) throws SQLException {
		this(rs.getInt("itemId"), rs.getInt("input_itemId"), rs.getInt("input_enchant"), rs.getInt("output_itemId"), rs.getInt("successProb"), rs.getInt("rewardCount"), rs.getInt("rewardEnchant"), Boolean.parseBoolean(rs.getString("broad")));
	}

	public L1CatalystCustom(int itemId, int input_itemId, int input_enchant, int output_itemId, int successProb, int rewardCount, int rewardEnchant, boolean broad) {
		this._itemId		= itemId;
		this._input_itemId	= input_itemId;
		this._input_enchant	= input_enchant;
		this._output_itemId	= output_itemId;
		this._successProb	= successProb;
		this._rewardCount	= rewardCount;
		this._rewardEnchant	= rewardEnchant;
		this._broad			= broad;
	}
	
	public int getItemId() {
		return _itemId;
	}
	public int getInputItemId() {
		return _input_itemId;
	}
	public int getInputEnchant() {
		return _input_enchant;
	}
	public int getOutputItemId() {
		return _output_itemId;
	}
	public int getSuccessProb() {
		return _successProb;
	}
	public int getRewardCount() {
		return _rewardCount;
	}
	public int getRewardEnchant() {
		return _rewardEnchant;
	}
	public boolean isBroad() {
		return _broad;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("itemId: ").append(_itemId);
		sb.append("\r\ninput_itemId: ").append(_input_itemId);
		sb.append("\r\ninput_enchant: ").append(_input_enchant);
		sb.append("\r\noutput_itemId: ").append(_output_itemId);
		sb.append("\r\nsuccessProb: ").append(_successProb);
		sb.append("\r\nrewardCount: ").append(_rewardCount);
		sb.append("\r\nrewardEnchant: ").append(_rewardEnchant);
		sb.append("\r\nbroad: ").append(_broad);
		return sb.toString();
	}
	
}

