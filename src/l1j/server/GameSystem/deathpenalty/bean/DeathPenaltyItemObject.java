package l1j.server.GameSystem.deathpenalty.bean;

import java.sql.Timestamp;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.StringUtil;

/**
 * 아이템 패널티 클래스
 * @author LinOffice
 */
public class DeathPenaltyItemObject extends DeathPenaltyObject {
	private L1ItemInstance recovery_item;
	
	public DeathPenaltyItemObject(int char_id, Timestamp delete_time, L1ItemInstance recovery_item, int recovery_cost) {
		super(char_id, delete_time, recovery_cost);
		this.recovery_item	= recovery_item;
	}
	
	public L1ItemInstance get_recovery_item() {
		return recovery_item;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("char_id: ").append(char_id).append(StringUtil.LineString);
		sb.append("delete_time: ").append(delete_time.getTime()).append(StringUtil.LineString);
		sb.append("recovery_cost: ").append(recovery_cost).append(StringUtil.LineString);
		sb.append("db_id: ").append(recovery_item.getId()).append(StringUtil.LineString);
		sb.append("itemId: ").append(recovery_item.getItemId()).append(StringUtil.LineString);
		sb.append("itemName: ").append(recovery_item.getDescEn());
		return sb.toString();
	}
}

