package l1j.server.server.model;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.Instance.L1NpcInstance;

public class L1MobGroupInfo {

	private final List<L1NpcInstance> _membersList = new ArrayList<L1NpcInstance>();

	private L1NpcInstance _leader;

	public L1MobGroupInfo() {
	}

	public void setLeader(L1NpcInstance npc) {
		_leader = npc;
	}

	public L1NpcInstance getLeader() {
		return _leader;
	}

	public boolean isLeader(L1NpcInstance npc) {
		return npc.getId() == _leader.getId();
	}

	private L1Spawn _spawn;

	public void setSpawn(L1Spawn spawn) {
		_spawn = spawn;
	}

	public L1Spawn getSpawn() {
		return _spawn;
	}

	public void addMember(L1NpcInstance npc) {
		if (npc == null)
			throw new NullPointerException();

		if (_membersList.isEmpty()) {
			setLeader(npc);
			if (npc.isReSpawn()) {
				setSpawn(npc.getSpawn());
			}
		}

		if (!_membersList.contains(npc)) {
			_membersList.add(npc);
		}
		npc.setMobGroupInfo(this);
		npc.setMobGroupId(_leader.getId());
	}

	public synchronized int removeMember(L1NpcInstance npc) {
		if (npc == null)
			throw new NullPointerException();

		if (_membersList.contains(npc)) {
			_membersList.remove(npc);
		}
		npc.setMobGroupInfo(null);

		if (isLeader(npc)) {
			if (isRemoveGroup() && _membersList.size() != 0) {
				for (L1NpcInstance minion : _membersList) {
					minion.setMobGroupInfo(null);
					minion.setSpawn(null);
					minion.setRespawn(false);
				}
				return 0;
			}
			if (_membersList.size() != 0) {
				setLeader(_membersList.get(0));
			}
		}
		return _membersList.size();
	}
	
	public L1NpcInstance[] getMember() {
		return _membersList.toArray(new L1NpcInstance[_membersList.size()]);
	}

	public int getNumOfMembers() {
		return _membersList.size();
	}
	
	private boolean _isRemoveGroup;

	public boolean isRemoveGroup() {
		return _isRemoveGroup;
	}

	public void setRemoveGroup(boolean flag) {
		_isRemoveGroup = flag;
	}
}

