package l1j.server.server.model;

import java.util.ArrayList;
import java.util.List;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class L1ChatParty {

	private final List<L1PcInstance> _membersList = new ArrayList<L1PcInstance>();

	private L1PcInstance _leader = null;

	public void addMember(L1PcInstance pc) {
		if (pc == null) {
			throw new NullPointerException();
		}
		if (_membersList.size() == Config.ALT.MAX_CHAT_PT && !_leader.isGm() || _membersList.contains(pc)) {
			return;
		}
		if (_membersList.isEmpty()) {
			setLeader(pc);
		}

		_membersList.add(pc);
		pc.setChatParty(this);
	}

	private void removeMember(L1PcInstance pc) {
		if (!_membersList.contains(pc)) {
			return;
		}

		_membersList.remove(pc);
		pc.setChatParty(null);
	}

	public boolean isVacancy() {
		return _membersList.size() < Config.ALT.MAX_CHAT_PT;
	}

	public int getVacancy() {
		return Config.ALT.MAX_CHAT_PT - _membersList.size();
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

	public String getMembersNameList() {
		String _result = new String(StringUtil.EmptyString);
		for (L1PcInstance pc : _membersList) {
			_result = _result + pc.getName() + StringUtil.EmptyOneString;
		}
		return _result;
	}

	private void breakup() {
		for (L1PcInstance member : getMembers()) {
			removeMember(member);
			member.sendPackets(new S_ServerMessage(418), true);
		}
	}

	public void leaveMember(L1PcInstance pc) {
		if (isLeader(pc)) {
			breakup();
		} else {
			if (getNumOfMembers() == 2) {
				removeMember(pc);
				L1PcInstance leader = getLeader();
				removeMember(leader);

				sendLeftMessage(pc, pc);
				sendLeftMessage(leader, pc);
			} else {
				removeMember(pc);
				for (L1PcInstance member : getMembers()) {
					sendLeftMessage(member, pc);
				}
				sendLeftMessage(pc, pc);
			}
		}
	}

	public void kickMember(L1PcInstance pc) {
		if (getNumOfMembers() == 2) {
			removeMember(pc);
			L1PcInstance leader = getLeader();
			removeMember(leader);
		} else {
			removeMember(pc);
		}
		
		pc.sendPackets(new S_ServerMessage(419), true);
	}

	public L1PcInstance[] getMembers() {
		return _membersList.toArray(new L1PcInstance[_membersList.size()]);
	}

	public int getNumOfMembers() {
		return _membersList.size();
	}

	private void sendLeftMessage(L1PcInstance sendTo, L1PcInstance left) {
		sendTo.sendPackets(new S_ServerMessage(420, left.getName()), true);
	}

}

