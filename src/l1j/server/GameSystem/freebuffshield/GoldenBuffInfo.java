package l1j.server.GameSystem.freebuffshield;

import l1j.server.common.bin.PCMasterCommonBinLoader;
import l1j.server.common.bin.pcmaster.PCMasterInfoForClient;
import l1j.server.common.bin.pcmaster.PCMasterInfoForClient.BuffGroupT.BuffT.BuffEnumDescBonus;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.ablity.ItemAbilityFactory;
import l1j.server.server.serverpackets.system.S_PCMasterGoldenBuffEnableNoti;
import l1j.server.server.serverpackets.system.S_PCMasterGoldenBuffUpdateNoti;

/**
 * PC 플레이 마스터 금빛 버프 정보 오브젝트
 * @author LinOffice
 */
public class GoldenBuffInfo {
	public static final int DEFAULT_TYPE = 1;
	
	protected L1PcInstance owner;
	protected int index;
	protected int type;
	protected java.util.LinkedList<Integer> grade;
	protected int remain_time;
	protected boolean enable;
	protected PCMasterGoldenBuffTimer buff_timer;
	protected PCMasterInfoForClient.BuffBonusT bonusT;
	
	protected GoldenBuffInfo(L1PcInstance owner, int index, int type, PCMasterInfoForClient.BuffBonusT bonusT) {
		this(owner, index, type, FreeBuffShieldLoader.parseLoadeGrade(new byte[] { 0, 0, 0 }), 0, bonusT);
	}
	
	protected GoldenBuffInfo(L1PcInstance owner, int index, int type, java.util.LinkedList<Integer> grade, int remain_time, PCMasterInfoForClient.BuffBonusT bonusT) {
		this.owner			= owner;
		this.index			= index;
		this.type			= type;
		this.grade			= grade;
		this.remain_time	= remain_time;
		this.bonusT			= bonusT;
		if (this.remain_time > 0 && is_enable_map()) {
			setEnable(true);
		}
	}

	public int getIndex() {
		return index;
	}

	public int getType() {
		return type;
	}
	
	/**
	 * 옵션을 변경한다.
	 * @param val
	 */
	public void setType(int val) {
		boolean is_enable_map = is_enable_map();
		if (enable && is_enable_map) {
			setEnable(false);
		}
		this.type = val;
		if (this.remain_time > 0 && is_enable_map) {
			setEnable(true);
		}
	}

	public java.util.LinkedList<Integer> getGrade() {
		return grade;
	}
	
	/**
	 * 강화 단계를 변경한다.
	 * @param index
	 * @param val
	 */
	public void enforceGrade(int index, int val) {
		boolean is_enable_map = is_enable_map();
		if (enable && is_enable_map) {
			setEnable(false);// 기존 보너스 제거
		}
		grade.set(index, val);// 단계 변경
		if (this.remain_time > 0 && is_enable_map) {
			setEnable(true);// 새로운 보너스 부여
		}
	}

	public int getRemainTime() {
		return remain_time;
	}
	public void setRemainTime(int val) {
		this.remain_time = val;
	}
	
	public PCMasterInfoForClient.BuffBonusT getBuffBonusT() {
		return bonusT;
	}
	
	public boolean isEnable() {
		return enable;
	}
	
	/**
	 * 버프 활성화 설정
	 * @param val
	 */
	public void setEnable(boolean val) {
		enable = val;
		if (enable) {
			enableTimer();
		} else {
			disableTimer();
		}
		doBonus(val);
	}
	
	/**
	 * 입장한 맵에 의한 버프 변경 여부 처리
	 * @param flag
	 * @return boolean
	 */
	public boolean is_update_map(boolean flag) {
		if (remain_time <= 0 || enable == flag || enable == is_enable_map()) {
			return false;
		}
		setEnable(flag);
		return true;
	}
	
	/**
	 * 버프가 활성화 되는 맵인지 조사한다.
	 * @return boolean
	 */
	public boolean is_enable_map() {
		return bonusT.get_map_id().contains((int) owner.getMapId());
	}
	
	/**
	 * 버프 타이머 활성화
	 */
	protected void enableTimer() {
		if (buff_timer == null) {
			buff_timer = new PCMasterGoldenBuffTimer(this);
		}
		buff_timer._active = true;
		GeneralThreadPool.getInstance().schedule(buff_timer, 1000L);
	}
	
	/**
	 * 버프 타이머 비활성화
	 */
	protected void disableTimer() {
		if (buff_timer == null) {
			return;
		}
		buff_timer._active = false;
	}
	
	/**
	 * 버프 옵션 설정
	 * @param flag
	 */
	void doBonus(boolean flag) {
		if (owner == null) {
			return;
		}
		PCMasterInfoForClient.BuffBonusT.BuffCategoryT categoryT = bonusT.get_buff_category().get(type - 1);
		if (categoryT == null) {
			return;
		}
		
		int flag_val = flag ? 1 : -1;
		java.util.LinkedList<byte[]> desc = null;
		for (int i=0; i<this.grade.size(); i++) {
			int grade = this.grade.get(i);
			// 단계 활성화 검증
			if (grade == 0) {
				continue;
			}
			int group_id = categoryT.get_group().get(i);
			PCMasterInfoForClient.BuffGroupT groupT = PCMasterCommonBinLoader.getData().get_buff_group().get(group_id - 1);
			if (groupT == null) {
				System.out.println(String.format(
						"[GoldenBuffInfo] DO_ABILITY_GROUP_T_EMPTY : CHAR_NAME(%s), GROUP_ID(%d)", 
						owner.getName(), group_id));
				continue;
			}
			
			// 단계에 해당하는 버프
			PCMasterInfoForClient.BuffGroupT.BuffT buffT = groupT.get_buff().get(grade - 1);
			if (buffT == null) {
				System.out.println(String.format(
						"[GoldenBuffInfo] DO_ABILITY_BUFF_T_EMPTY : CHAR_NAME(%s), GROUP_ID(%d), GRADE(%d)", 
						owner.getName(), group_id, grade));
				continue;
			}
			
			byte[] desc_array	= buffT.get_desc();
			if (desc_array == null) {
				continue;
			}
			if (flag) {
				if (desc == null) {
					desc = new java.util.LinkedList<byte[]>();
				}
				desc.add(desc_array);
			}
			
			for (BuffEnumDescBonus buffEnumDescBonus : buffT.get_enum_desc_bonus_list()) {
				ItemAbilityFactory.doBonus(owner, buffEnumDescBonus.getFactory(), buffEnumDescBonus.getValue() * flag_val);
			}
		}
		
		if (flag) {
			owner.sendPackets(new S_PCMasterGoldenBuffEnableNoti(flag, remain_time, desc), true);
		} else {
			owner.sendPackets(S_PCMasterGoldenBuffEnableNoti.DISABLE);
		}
	}
	
	/**
	 * 버프 초기화
	 */
	protected void reset() {
		setEnable(false);
		for (int i=0; i<grade.size(); i++) {
			grade.set(i, 0);
		}
		owner.sendPackets(new S_PCMasterGoldenBuffUpdateNoti(
				owner.getConfig().get_free_buff_shield().get_golden_buff_infos(), 
				S_PCMasterGoldenBuffUpdateNoti.eUpdateReason.UPDATE), 
				true);
	}
	
	/**
	 * 메모리 해제
	 */
	protected void dispose() {
		grade.clear();
		grade = null;
		if (buff_timer != null) {
			buff_timer._active = false;
			buff_timer = null;
		}
	}
	
}

