package l1j.server.GameSystem.einhasadfaith.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import l1j.server.server.utils.StringUtil;

/**
 * 아인하사드의 신의 오브젝트
 * @author LinOffice
 */
public class EinhasadFaithInfo {
	private int groupId;
	private int indexId;
	private int spellId;
	private Timestamp expiredTime;
	
	public EinhasadFaithInfo(ResultSet rs) throws SQLException {
		this(rs.getInt("groupId"), rs.getInt("indexId"), rs.getInt("spellId"), rs.getTimestamp("expiredTime"));
	}
	
	public EinhasadFaithInfo(int groupId, int indexId, int spellId, Timestamp expiredTime) {
		this.groupId		= groupId;
		this.indexId		= indexId;
		this.spellId		= spellId;
		this.expiredTime	= expiredTime;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getIndexId() {
		return indexId;
	}

	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}
	
	public int getSpellId() {
		return spellId;
	}
	
	public void setSpellId(int spellId) {
		this.spellId = spellId;
	}

	public Timestamp getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Timestamp expiredTime) {
		this.expiredTime = expiredTime;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("groupId: ").append(groupId).append(StringUtil.LineString);
		sb.append("indexId: ").append(indexId).append(StringUtil.LineString);
		sb.append("spellId: ").append(spellId).append(StringUtil.LineString);
		sb.append("expiredTime: ").append(expiredTime);
		return sb.toString();
	}
	
}

