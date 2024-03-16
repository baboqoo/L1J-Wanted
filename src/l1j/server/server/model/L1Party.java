package l1j.server.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import l1j.server.Config;
import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.common.data.eDistributionType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.party.S_PartyMemberList;
import l1j.server.server.serverpackets.party.S_PartyMemberListChange;
import l1j.server.server.serverpackets.party.S_PartyMemberListChange.PartyMemberChangeType;
import l1j.server.server.serverpackets.party.S_PartyMemberStatus;
import l1j.server.server.serverpackets.party.S_PartySyncPeriodicInfo;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.StringUtil;

public class L1Party {
	private final ArrayList<L1PcInstance> _membersList = new ArrayList<L1PcInstance>();
	private L1PcInstance _leader;
	private eDistributionType _distribution;// 분배 타입
	
	public eDistributionType get_distribution() {
		return _distribution;
	}
	public void set_distribution(eDistributionType val) {
		_distribution = val;
	}

	private int _isBraveAvatar;
	public void setBraveAvatar(int val) {
		_isBraveAvatar = val;
	}
	public int getBraveAvatar() {
		return _isBraveAvatar;
	}

	private Timer _partySyncTimer;
	private PartySyncTask _partySyncTask;
	private S_PartySyncPeriodicInfo _partySyncPacket;
	private Object sync_lock = new Object();

	/**
	 * 파티 정보 동기화
	 */
	public void sendSync() {
		synchronized (sync_lock) {
			if (_partySyncPacket != null) {
				_partySyncPacket.clear();
				_partySyncPacket = null;
			}
			if (_membersList.isEmpty()) {
				return;
			}
			L1PcInstance[] members = getMembersArray();
			_partySyncPacket = new S_PartySyncPeriodicInfo(members);
			for (L1PcInstance member : members) {
				if (member == null) {
					continue;
				}
				member.sendPackets(_partySyncPacket);
			}
		}
	}

	/**
	 * 파티 정보 동기화 타이머
	 */
	private class PartySyncTask extends TimerTask {
		@Override
		public void run() {
			if (!_membersList.isEmpty()) {
				sendSync();
			} else {
				_partySyncTimer.cancel();
				_partySyncTimer	= null;
				_partySyncTask	= null;
			}
		}
	}
	
	static final long SYNC_INTERVAL = 5000L;
	
	void createSyncTimer(L1PcInstance pc){
		if (pc instanceof L1AiUserInstance) {
			return;
		}
		if (_partySyncTimer == null) {
			_partySyncTimer	= new Timer();
			_partySyncTask	= new PartySyncTask();
			_partySyncTimer.scheduleAtFixedRate(_partySyncTask, SYNC_INTERVAL, SYNC_INTERVAL);// 5초
		}
	}

	public void addMember(L1PcInstance pc) {
		if (pc == null) {
			throw new NullPointerException();
		}
		if (_membersList.size() == Config.ALT.MAX_PT || _membersList.contains(pc)) {
			return;
		}
		if (_membersList.isEmpty()) {
			setLeader(pc);
			createSyncTimer(pc);
		}
		
		_membersList.add(pc); // 멤버에 나 자신을 넣고..
		pc.setParty(this);
		
		if (pc instanceof L1AiUserInstance) {
			return;
		}
		pc.sendPackets(new S_HPMeter(pc), true); // 자기 자신에게 미니 hp바 생성.
		showAddPartyInfo(pc);
		if (!isLeader(pc)) {
			sendSync();
			update(pc);
		}
	}
	
	public void addIndunMember(L1PcInstance pc, IndunInfo info) {
		if (pc == null) {
			throw new NullPointerException();
		}
		if (_membersList.isEmpty()) {
			setLeader(pc);
			_distribution = info.distribution_type;
			createSyncTimer(pc);
		}
		_membersList.add(pc);
		pc.setParty(this);
		
		pc._partyMark = info.infoUserList.indexOf(info.getUserInfo(pc)) + 1;
		showAddPartyInfo(pc);
		if (!isLeader(pc)) {
			sendSync();
			update(pc);
		}
	}

	private void showAddPartyInfo(L1PcInstance add_pc) {
		if (add_pc instanceof L1AiUserInstance) {
			return;
		}
		S_PartyMemberList sPartyOld			= new S_PartyMemberList(add_pc);
		S_PartyMemberListChange sPartyNew	= new S_PartyMemberListChange(PartyMemberChangeType.NEW_USER, add_pc);
		for (L1PcInstance member : getMembersArray()) {
			if (member == null || member instanceof L1AiUserInstance) {
				continue;
			}
			if (add_pc.getId() == member.getId()) {
				add_pc.sendPackets(sPartyOld);// 가입자에게 뿌려지는거.
			} else {
				member.sendPackets(sPartyNew);// 타인에게 파티원 추가 알린다
			}
		}

		// 투명 멤버가 잇을경우
		S_PCObject s_obj_pck			= null;
		S_NPCObject s_doll				= null;
		S_PartyMemberStatus status_pck	= null;
		boolean is_invis				= add_pc.isInvisble();
		boolean isFloatingEye			= add_pc.isFloatingEye();// 괴물눈 고기와 불투명 물약
		int pledge_id					= add_pc.getClanid();
		L1DollInstance doll				= add_pc.getDoll();
		for (L1PcInstance member : L1World.getInstance().getVisiblePartyPlayer(add_pc, -1)) {// 화면 내 파티 멤버
			if (member == add_pc) {
				continue;
			}
			boolean isEqualsPledge		= pledge_id != 0 && pledge_id == member.getClanid();// 이미 같은 혈맹원
			if (isEqualsPledge) {// 같은 혈맹원은 오브젝트 처리를 하지 않는다.
				if (status_pck == null) {
					status_pck = new S_PartyMemberStatus(add_pc);
				}
				continue;
			}
			boolean isMemberInvis		= member.isInvisble();
			boolean isMemberFloatingEye	= member.isFloatingEye();// 괴물눈 고기와 눈멀기 물약
			// 본인이 투명
			if (is_invis) {
				if (isMemberFloatingEye) {
					if (doll != null) {
						if (s_doll == null) {
							s_doll = new S_NPCObject(doll);
						}
						member.sendPackets(s_doll);
					}
				} else {
					if (s_obj_pck == null) {
						s_obj_pck = new S_PCObject(add_pc);
					}
					member.sendPackets(s_obj_pck);
					if (doll != null) {
						if (s_doll == null) {
							s_doll = new S_NPCObject(doll);
						}
						member.sendPackets(s_doll);
					}
				}
				if (status_pck == null) {
					status_pck = new S_PartyMemberStatus(add_pc);
				}
				member.sendPackets(status_pck);
			}
			// 파티 멤버가 투명
			if (isMemberInvis) {
				if (isFloatingEye) {
					if (member.getDoll() != null) {
						add_pc.sendPackets(new S_NPCObject(member.getDoll()), true);
					}
				} else {
					add_pc.sendPackets(new S_PCObject(member), true);
					if (member.getDoll() != null) {
						add_pc.sendPackets(new S_NPCObject(member.getDoll()), true);
					}
				}
				add_pc.sendPackets(new S_PartyMemberStatus(member), true);
			}
		}
		if (s_obj_pck != null) {
			s_obj_pck.clear();
			s_obj_pck = null;
		}
		if (s_doll != null) {
			s_doll.clear();
			s_doll = null;
		}
		if (status_pck != null) {
			status_pck.clear();
			status_pck = null;
		}
		
		sPartyOld = null;
		sPartyNew = null;
	}
	
	private static final int[] PARTY_SKILLS = { 
		L1SkillId.CUBE_AVATAR, L1SkillId.CUBE_GOLEM, L1SkillId.CUBE_OGRE, L1SkillId.CUBE_LICH, L1SkillId.IMPACT, L1SkillId.GRACE
	};
	
	private void removeMember(L1PcInstance leave_pc) {
		if (!_membersList.contains(leave_pc)) {
			return;
		}
		
		leave_pc.setParty(null);
		leave_pc._partyMark = 0;
		deleteMiniHp(leave_pc);
		_membersList.remove(leave_pc);
		
		for (int skillId : PARTY_SKILLS) {
			if (!leave_pc.getSkill().hasSkillEffect(skillId)) {
				continue;
			}
			leave_pc.getSkill().removeSkillEffect(skillId);
			leave_pc.sendPackets(new S_SpellBuffNoti(leave_pc, skillId, false, -1), true);
		}
		
		// 투명 케릭을 감춘다
		L1DollInstance doll				= leave_pc.getDoll();
		S_RemoveObject s_remove			= null;
		S_RemoveObject s_remove_doll	= null;
		boolean is_invis				= leave_pc.isInvisble();
		boolean isFloatingEye			= leave_pc.isFloatingEye();
		int pledge_id					= leave_pc.getClanid();
		for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(leave_pc)) {// 화면 내 케릭터들
			if (member == leave_pc || !_membersList.contains(member)) {// 남은 파티 멤버가 아니라면 제외
				continue;
			}
			boolean isEqualsPledge		= pledge_id != 0 && member.getClanid() == pledge_id;// 이미 같은 혈맹원
			if (isEqualsPledge) {// 같은 혈맹원은 오브젝트 처리를 하지 않는다.
				continue;
			}
			boolean isMemberInvis		= member.isInvisble();// 파티 멤버가 투명 상태
			boolean isMemberFloatingEye	= member.isFloatingEye();// 괴물눈 고기와 불투명 물약
			// 본인이 투명
			if (is_invis) {
				if (isMemberFloatingEye) {
					if (doll != null) {
						if (s_remove_doll == null) {
							s_remove_doll = new S_RemoveObject(leave_pc.getDoll());
						}
						member.sendPackets(s_remove_doll);
					}
				} else {
					if (s_remove == null) {
						s_remove = new S_RemoveObject(leave_pc);
					}
					member.sendPackets(s_remove);
					if (doll != null) {
						if (s_remove_doll == null) {
							s_remove_doll = new S_RemoveObject(leave_pc.getDoll());
						}
						member.sendPackets(s_remove_doll);
					}
				}
			}
			// 파티 멤버가 투명
			if (isMemberInvis) {
				if (isFloatingEye) {
					if (member.getDoll() != null) {
						leave_pc.sendPackets(new S_RemoveObject(member.getDoll()), true);
					}
				} else {
					leave_pc.sendPackets(new S_RemoveObject(member), true);
					if (member.getDoll() != null) {
						leave_pc.sendPackets(new S_RemoveObject(member.getDoll()), true);
					}
				}
			}
		}
		
		if (s_remove != null) {
			s_remove.clear();
			s_remove = null;
		}
		if (s_remove_doll != null) {
			s_remove_doll.clear();
			s_remove_doll = null;
		}
	}

	private void deleteMiniHp(L1PcInstance pc) {
		if (pc instanceof L1AiUserInstance) {
			return;
		}
		S_PartyMemberListChange owner = new S_PartyMemberListChange(PartyMemberChangeType.OUT_USER, pc);
		pc.sendPackets(owner);
		
		for (L1PcInstance member : getMembersArray()) {
			if (member == null || member instanceof L1AiUserInstance) {
				continue;
			}
			member.sendPackets(owner);
			pc.sendPackets(new S_PartyMemberListChange(PartyMemberChangeType.OUT_USER, member), true);
		}
		owner = null;
	}

	public boolean isVacancy() {
		return _membersList.size() < Config.ALT.MAX_PT;
	}

	public int getVacancy() {
		return Config.ALT.MAX_PT - _membersList.size();
	}

	public boolean isMember(L1PcInstance pc) {
		return _membersList.contains(pc);
	}

	private void setLeader(L1PcInstance pc) {
		_leader = pc;
	}

	public L1PcInstance getLeader() {
		return _leader;
	}

	public boolean isLeader(L1PcInstance pc) {
		return pc.getId() == _leader.getId();
	}

	public boolean isAutoDistribution() {
		return _distribution == eDistributionType.AUTO_DISTRIBUTION;
	}

	public String getMembersNameList() {
		StringBuilder sb = new StringBuilder();
		for (L1PcInstance pc : getMembersArray()) {
			sb.append(pc.getName()).append(StringUtil.EmptyOneString);
		}
		return sb.toString();
	}

	/** 자신의 상태를 멤버들에게 보낸다. */
	public void update(L1PcInstance pc) {
		if (pc == null || pc instanceof L1AiUserInstance) {
			return;
		}
		sendPacketToMembers(new S_PartyMemberStatus(pc), true);
	}

	/** 파티 해산 */
	private void breakup() {
		for (L1PcInstance member : getMembersArray()) {
			removeMember(member);
			member.sendPackets(L1ServerMessage.sm418);
		}
	}

	/** 파티장 위임 */
	public void passLeader(L1PcInstance pc) {
		this.setLeader(pc);
		this.sendPacketToMembers(new S_PartyMemberListChange(PartyMemberChangeType.CHANGE_LEADER, pc), true);
		sendSync();
	}

	/** 파티 탈퇴 */
	public void leaveMember(L1PcInstance pc) {
		if (isLeader(pc) || getNumOfMembers() == 2) {
			breakup();
		} else {
			removeMember(pc);
		}
	}

	/** 파티 추방 */
	public void kickMember(L1PcInstance pc) {
		if (getNumOfMembers() == 2) {
			breakup();
		} else {
			removeMember(pc);
		}
	}

	/**
	 * 화면내 파티원 리스트 대상자는 제외
	 */
	public ArrayList<L1PcInstance> getMembersInScreen(L1PcInstance pc) {
		ArrayList<L1PcInstance> list = new ArrayList<L1PcInstance>();
		for (L1PcInstance member : getMembersArray()) {
			if (member != null && member.getId() != pc.getId() && pc.getMapId() == member.getMapId() && pc.getLocation().isInScreen(member.getLocation())) {
				list.add(member);
			}
		}
		return list;
	}

	/**
	 * 화면내 파티원 대상자포함 리스트
	 */
	public ArrayList<L1PcInstance> getMembersInScreenWithMe(L1PcInstance pc) {
		ArrayList<L1PcInstance> list = new ArrayList<L1PcInstance>();
		for (L1PcInstance member : getMembersArray()) {
			if (member != null && pc.getMapId() == member.getMapId() && pc.getLocation().isInScreen(member.getLocation())) {
				list.add(member);
			}
		}
		return list;
	}

	public L1PcInstance[] getMembersArray() {
		return _membersList.toArray(new L1PcInstance[_membersList.size()]);
	}

	public int getNumOfMembers() {
		return _membersList.size();
	}

	public List<L1PcInstance> getList() {// 하딘 파티 멤버 리스트
		return _membersList;
	}
	
	public void sendPacketToMembers(ServerBasePacket packet) {
		sendPacketToMembers(packet, false);
	}
	public void sendPacketToMembers(ServerBasePacket packet, boolean clear) {
		try {
			for (L1PcInstance member : getMembersArray()) {
				if (member == null) {
					continue;
				}
				member.sendPackets(packet);
			}
			if (clear) {
				packet.clear();
				packet = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

