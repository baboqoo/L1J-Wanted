package l1j.server.GameSystem.einhasadfaith;

import java.sql.Timestamp;
import java.util.HashMap;

import l1j.server.GameSystem.einhasadfaith.bean.EinhasadFaithInfo;
import l1j.server.common.bin.EinhasadPointFaithCommonBinLoader;
import l1j.server.common.bin.einhasadpoint.EinhasadPointFaithT;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.einhasadpoint.EinhasadFaithInfoStream;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadFaithBuffNoti;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadFaithDisableIndexNoti;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadFaithEnableIndex;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadFaithListNoti;

/**
 * 아인하사드의 신의 핸들러
 * L1PcInstance 할당
 * @author LinOffice
 */
public class EinhasadFaithHandler {
	private final L1PcInstance owner;
	private final java.util.LinkedList<EinhasadFaithInfo> infos;// indexId:0(그룹 정보), 1~4(인덱스 정보) 
	
	/**
	 * 생성자
	 * @param owner
	 */
	public EinhasadFaithHandler(L1PcInstance owner) {
		this.owner = owner;
		this.infos = new java.util.LinkedList<>();
		init();
	}
	
	/**
	 * 아인하스드의 신의 정보 리스트
	 * @return java.util.LinkedList<EinhasadFaithInfo>
	 */
	public java.util.LinkedList<EinhasadFaithInfo> getInfos() {
		return infos;
	}
	
	/**
	 * 아인하스드의 신의 정보 조사
	 * @param groupId
	 * @param indexId
	 * @return EinhasadFaithInfo
	 */
	public EinhasadFaithInfo getInfo(int groupId, int indexId) {
		for (EinhasadFaithInfo val : infos) {
			if (val.getGroupId() == groupId && val.getIndexId() == indexId) {
				return val;
			}
		}
		return null;
	}
	
	/**
	 * 아인하사드의 신의 버프 활성화
	 * @param groupVal
	 * @param indexVal
	 * @param groupT
	 * @param indexT
	 */
	public void enable(EinhasadFaithInfo groupVal, EinhasadFaithInfo indexVal, EinhasadPointFaithT.GroupListT.GroupT groupT, EinhasadPointFaithT.GroupListT.GroupT.IndexT indexT) {
		L1SkillUse skillUse = new L1SkillUse();
		skillUse.handleCommands(owner, indexT.get_spellId(), owner.getId(), 0, 0, indexT.get_duration(), L1SkillUseType.LOGIN);
		long currentTime = System.currentTimeMillis();
		if (indexVal.getExpiredTime() == null) {
			indexVal.setExpiredTime(new Timestamp(currentTime + (indexT.get_duration() * 1000L)));
		} else {
			indexVal.getExpiredTime().setTime(currentTime + (indexT.get_duration() * 1000L));
		}
		owner.sendPackets(new S_EinhasadFaithEnableIndex(
				EinhasadFaithInfoStream.FaithInfoType.Index, 
				indexVal.getGroupId(), 
				indexVal.getIndexId(), 
				indexVal.getExpiredTime()),
				true);
		owner.sendPackets(new S_EinhasadFaithBuffNoti(
				S_EinhasadFaithBuffNoti.eNotiType.NEW,
				indexVal),
				true);
		
		// 그룹 완성 검증
		L1SkillStatus skill = owner.getSkill();
		int enableCnt = 0;
		java.util.LinkedList<EinhasadPointFaithT.GroupListT.GroupT.IndexT> indexList = groupT.get_index();
		for (EinhasadPointFaithT.GroupListT.GroupT.IndexT index : indexList) {
			if (skill.hasSkillEffect(index.get_spellId())) {
				enableCnt++;
			}
		}
		if (indexList.size() == enableCnt) {
			// 그룹 버프 활성화
			skillUse.handleCommands(owner, groupT.get_spellId(), owner.getId(), 0, 0, 0, L1SkillUseType.LOGIN);
			if (groupVal.getExpiredTime() == null) {
				groupVal.setExpiredTime(new Timestamp(currentTime + (indexT.get_duration() * 1000L)));
			} else {
				groupVal.getExpiredTime().setTime(currentTime + (indexT.get_duration() * 1000L));
			}
			owner.sendPackets(new S_EinhasadFaithEnableIndex(
					EinhasadFaithInfoStream.FaithInfoType.Group, 
					groupVal.getGroupId(), 
					groupVal.getIndexId(), 
					groupVal.getExpiredTime()),
					true);
			owner.sendPackets(new S_EinhasadFaithBuffNoti(
					S_EinhasadFaithBuffNoti.eNotiType.NEW,
					groupVal),
					true);
		}
		skillUse = null;
		
		// 로그 기록(그룹은 기록하지 않는다)
		LoggerInstance.getInstance().addEinhasadFaith(owner, indexVal.getGroupId(), indexVal.getIndexId(), indexVal.getExpiredTime());
	}
	
	/**
	 * 아인하사드의 신의 버프 비활성화
	 * @param groupId
	 * @param indexId
	 */
	public void disable(int groupId, int indexId) {
		EinhasadFaithInfo info = getInfo(groupId, indexId);
		if (info == null) {
			return;
		}
		info.setExpiredTime(null);
		owner.sendPackets(new S_EinhasadFaithDisableIndexNoti(
				indexId == 0 ? EinhasadFaithInfoStream.FaithInfoType.Group : EinhasadFaithInfoStream.FaithInfoType.Index,
				groupId,
				indexId),
				true);
		
		if (indexId > 0) {
			owner.getSkill().removeSkillEffect(groupId == 1 ? L1SkillId.EINHASAD_FAITH_GROUP_1 : L1SkillId.EINHASAD_FAITH_GROUP_2);
		}
	}
	
	/**
	 * 객체 생성 초기화(로그인)
	 */
	void init() {
		HashMap<Integer, EinhasadFaithInfo> dbInfo	= EinhasadFaithLoader.getInstance().load(owner.getId());
		EinhasadPointFaithT.GroupListT groupListT	= EinhasadPointFaithCommonBinLoader.getGroupListT();
		L1SkillUse skillUse = null;
		long currentTime	= System.currentTimeMillis();
		int defailtDuration	= groupListT.get_Group().getFirst().get_index().getFirst().get_duration();
		
		// 그룹(인덱스 0)
		for (EinhasadPointFaithT.GroupListT.GroupT groupT : groupListT.get_Group()) {
			infos.add(new EinhasadFaithInfo(groupT.get_GroupId(), 0, groupT.get_spellId(), null));
		}
		
		// 인덱스(1 ~ 4)
		for (EinhasadPointFaithT.GroupListT.GroupT groupT : groupListT.get_Group()) {
			int enableCnt = 0;
			for (EinhasadPointFaithT.GroupListT.GroupT.IndexT indexT : groupT.get_index()) {
				EinhasadFaithInfo info = null;
				if (dbInfo != null && !dbInfo.isEmpty()) {
					info = dbInfo.get(indexT.get_indexId());
				}
				if (info == null) {
					info = new EinhasadFaithInfo(groupT.get_GroupId(), indexT.get_indexId(), indexT.get_spellId(), null);
				}
				// 만료 시간 검증
				if (info.getExpiredTime() != null && info.getExpiredTime().getTime() > currentTime) {
					enableCnt++;
				} else {
					info.setExpiredTime(null);
				}
				infos.add(info);
			}
			
			// 그룹 완성
			if (groupT.get_index().size() == enableCnt) {
				getInfo(groupT.get_GroupId(), 0).setExpiredTime(new Timestamp(currentTime + (defailtDuration * 1000L)));
			}
		}
		
		// 버프 활성화
		for (EinhasadFaithInfo val : infos) {
			if (val.getExpiredTime() == null || val.getExpiredTime().getTime() <= currentTime) {
				continue;
			}
			int duration = (int)((val.getExpiredTime().getTime() - currentTime) / 1000);
			if (duration <= 0) {
				continue;
			}
			if (skillUse == null) {
				skillUse = new L1SkillUse();
			}
			skillUse.handleCommands(owner, val.getSpellId(), owner.getId(), 0, 0, duration, L1SkillUseType.LOGIN);
		}
		skillUse = null;
		
		owner.sendPackets(new S_EinhasadFaithListNoti(infos), true);
		owner.sendPackets(new S_EinhasadFaithBuffNoti(
				S_EinhasadFaithBuffNoti.eNotiType.RESTART,
				infos),
				true);
	}
	
	/**
	 * 메모리 해제
	 */
	public void dispose() {
		infos.clear();
	}
	
}

