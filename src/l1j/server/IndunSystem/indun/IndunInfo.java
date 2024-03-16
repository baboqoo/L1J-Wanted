package l1j.server.IndunSystem.indun;

import java.util.ArrayList;

import l1j.server.common.data.eArenaMapKind;
import l1j.server.common.data.eDistributionType;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class IndunInfo {
	public ArrayList<UserInfo> infoUserList = new ArrayList<UserInfo>();
	
	public static class UserInfo {
		public int _objectid;
		public String _userName;
		public L1PcInstance _userPc;
		
		public UserInfo(int object, String userName, L1PcInstance pc){
			_objectid	= object;
			_userName	= userName;
			_userPc		= pc;
		}
	}
	
	public UserInfo getUserInfo(L1PcInstance pc) {// 해당 방에 유저존제여부
		for (UserInfo user : infoUserList) {
			if (user._objectid == pc.getId()) {
				return user;
			}
		}
		return null;
	}
	
	public void setUser(L1PcInstance pc) {// 해당 방에 유저를 넣거나 제거
		UserInfo user = getUserInfo(pc);
		if (!infoUserList.contains(user)) {
			infoUserList.add(new UserInfo(pc.getId(), pc.getName(), pc));
		} else {
			infoUserList.remove(user);
		}
	}
	
	public L1PcInstance getInfoCheckUser(int objectId) {// 해당방에 존재하는 유저가 접속중인지 체크
		L1PcInstance pc;
		for (UserInfo user : infoUserList) {
			pc = L1World.getInstance().getPlayer(user._userName);
			if (pc != null && pc.getId() == objectId) {
				return pc;
			}
		}
		return null;
	}
	
	public ArrayList<L1PcInstance> getMembers() {// 해당방에 있는 모든 유저 리스트
		ArrayList<L1PcInstance> members = new ArrayList<L1PcInstance>();
		L1PcInstance pc;
		for (UserInfo user : infoUserList) {
			pc = L1World.getInstance().getPlayer(user._userName);
			if (pc != null) {
				members.add(pc);
			}
		}
		return members;
	}
	
	public void clearUserList(){
		infoUserList.clear();
	}
	
	public IndunReadyCounter readyCounter;
	
	public byte[]				title;
	public int 					room_id;
	public eArenaMapKind		map_kind;
	public eDistributionType	distribution_type;
	public int					min_level;
	public int					fee;
	public int					min_player;
	public int					max_player;
	public boolean 				is_closed;
	public String				password;
	public int					chief_id;
	public boolean 				is_playing;
	public boolean				is_locked;
	public IndunType			indunType;
}
