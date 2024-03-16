package l1j.server.server.utils;

import l1j.server.server.construct.L1BaseStatus;
import l1j.server.server.model.Instance.L1PcInstance;

public class CheckInitStat{

	/**
	 * 캐릭터의 최소 스탯을 검사
	 * @param pc
	 * @return true : 정상 or 운영자, false : 비정상
	 */
	public static boolean checkBaseStat(L1PcInstance pc){
		if (pc == null) {
			return false; // 만약 pc가 없다면
		}
		if (pc.isGm()) {
			return true; // pc가 운영자라면
		}
		int str		= pc.getAbility().getBaseStr();
		int dex		= pc.getAbility().getBaseDex();
		int cha		= pc.getAbility().getBaseCha();
		int con		= pc.getAbility().getBaseCon();
		int intel	= pc.getAbility().getBaseInt();
		int wis		= pc.getAbility().getBaseWis();
		L1BaseStatus base = L1BaseStatus.fromInt(pc.getType());
		if (str < base.get_base_str() || dex < base.get_base_dex() || con < base.get_base_con() 
				|| cha < base.get_base_cha() || intel < base.get_base_int() || wis < base.get_base_wis()) {
			return false; // 초기스탯보다 작다면
		}
		return true;
	}

	private CheckInitStat(){}
}

