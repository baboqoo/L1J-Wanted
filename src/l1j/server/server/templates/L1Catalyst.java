package l1j.server.server.templates;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.common.bin.CatalystTableCommonBinLoader;
import l1j.server.common.bin.catalyst.CatalystTableT;
import l1j.server.server.utils.StringUtil;

public class L1Catalyst {
	private int _name_id;
	private int _input;
	private int _successProb;
	private boolean _broad;
	private CatalystTableT.CatalystRewardInfoT _bin;
	
	public L1Catalyst(ResultSet rs) throws SQLException {
		this(rs.getInt("nameId"), rs.getInt("input"), rs.getInt("successProb"), Boolean.parseBoolean(rs.getString("broad")));
	}
	
	public L1Catalyst(int name_id, int input, int successProb, boolean broad) {
		this._name_id		= name_id;
		this._input			= input;
		this._successProb	= successProb;
		this._broad			= broad;
		this._bin			= CatalystTableCommonBinLoader.getCatalyst(this._name_id, this._input);
		if (this._bin == null) {
			System.out.println(String.format("[L1Catalyst] BIN_EMPTY : NAME_ID(%d), INPUT_ID(%d)", this._name_id, this._input));
		}
	}

	public int get_name_id() {
		return _name_id;
	}

	public int get_input() {
		return _input;
	}

	public int get_successProb() {
		return _successProb;
	}

	public boolean is_broad() {
		return _broad;
	}
	
	public CatalystTableT.CatalystRewardInfoT getBin() {
		return _bin;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name_id: ").append(_name_id).append(StringUtil.LineString);
		sb.append("input: ").append(_input).append(StringUtil.LineString);
		sb.append("successProb: ").append(_successProb).append(StringUtil.LineString);
		sb.append("broad: ").append(_broad);
		return sb.toString();
	}
	
}

