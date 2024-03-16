package l1j.server.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;

import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.common.data.ePLEDGE_JOIN_REQ_TYPE;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeEnterNoticeNoti;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeAllyList;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeAllyListChange;
import l1j.server.server.templates.L1PledgeJoinningRequest;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.StringUtil;

public class L1Clan {
	static public class ClanMember {
		public String name;
		public eBloodPledgeRankType rank;
		public int level;
		public String notes;
		public int memberId;
		public int type;
		public boolean online;
		public int contribution, contributionWeek;
		public int join_date;
		public int logout_date;
		public L1PcInstance player;
		
		public ClanMember(String name, eBloodPledgeRankType rank, int level, String notes, int memberId, int type, boolean online, int contribution, int contributionWeek, int join_date, int logout_date, L1PcInstance player) {
			this.name				= name;
			this.rank				= rank;
			this.level				= level;
			this.notes				= notes;
			this.memberId			= memberId;
			this.type				= type;
			this.online				= online;
			this.contribution		= contribution;
			this.contributionWeek	= contributionWeek;
			this.join_date			= join_date;
			this.logout_date		= logout_date;
			this.player				= player;
		}
	}
	
	public static final int ALLIANCE_NORMAL				= 0;// 동맹혈맹
	public static final int ALLIANCE_MASTER				= 1;// 동맹주
	
	// 51 52 59 101 102 103 104 105 106 107 108 109 110
	public static final int RED_KNIGHT_TEAM_ID			= 13;
	public static final int[] INTER_TEAM_IDS			= { 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010 };// 인터서버 문장리스트(가끔씩 번호 바뀜)

	private static final IntRange CONTRIBUTION_RANGE = new IntRange(0, Config.PLEDGE.PLEDGE_CONTRIBUTION_MAX_COUNT);
	
	private int _clanId;
	private String _clanName;
	private int _leaderId;
	private String _leaderName;
	private int _castleId;
	private int _houseId;
	private String _introductionMessage;// 혈맹 소개글
	private String _enter_notice;// 혈맹 알림
	private S_BloodPledgeEnterNoticeNoti _enter_notice_pck;// 혈맹 알림  패킷(재사용)
	private FastMap<Integer, Integer> _alliance;
	private boolean _isEnableJoin;
	private ePLEDGE_JOIN_REQ_TYPE _joinType;
	private String _joinPassword;
	private ServerBasePacket _alliancePck;
	private Timestamp _clanBirthday;
	private int _maxuser;
	private int _emblemId;
	private int _emblemStatus;
	private int _contribution;
	private boolean _clanBuff;
	private boolean _clanNameChange;
	private int _teamId;// 인터서버 문장
	private LinkedList<L1PledgeJoinningRequest> _joinningList;// 승인가입 요청 대기자
	private LinkedList<String> _store_allow_list;// 창고 사용 목록
	private int _limit_level;// 제한 레벨
	private LinkedList<String> _limit_user_names;// 차단 목록
	
	private boolean _bot; 
	private int _botStyle; 
	private int _botLevel; 
	
	private long _master_change_req_time;// 혈맹 군주 위임 요청 시간
	
	public boolean isClanBuff(){
		return _clanBuff;
	}
	public void setClanBuff(boolean flag){
		_clanBuff = flag;
	}
	
	public boolean getclanNameChange(){
		return _clanNameChange;
	}
	public void setclanNameChange(boolean change){
		_clanNameChange = change;
	}

	public int getTeamId(){
		return _teamId;
	}
	public void setTeamId(int teamId){
		_teamId = teamId;
	}
	
	// 공헌도
	public int getContribution() {
		return _contribution;
	}
	public synchronized void setContribution(int value) {
		_contribution = CONTRIBUTION_RANGE.ensure(value);	
	}
	public synchronized void addContribution(int value) {
		_contribution = CONTRIBUTION_RANGE.ensure(_contribution + value);	
	}
	
	private Timestamp _clanDayDungeonTime;
	public Timestamp getClanDayDungeonTime(){
		return _clanDayDungeonTime;
	}
	public void setClanDayDungeonTime(Timestamp val){
		_clanDayDungeonTime = val;
	}
	
	private Timestamp _clanWeekDungeonTime;
	public Timestamp getClanWeekDungeonTime(){
		return _clanWeekDungeonTime;
	}
	public void setClanWeekDungeonTime(Timestamp val){
		_clanWeekDungeonTime = val;
	}
	
	private Timestamp _clanVowPotionTime;
	public Timestamp getClanVowPotionTime(){
		return _clanVowPotionTime;
	}
	public void setClanVowPotionTime(Timestamp val){
		_clanVowPotionTime = val;
	}
	
	private int _clanVowPotionCount;
	public int getClanVowPotionCount(){
		return _clanVowPotionCount;
	}
	public void setClanVowPotionCount(int val){
		_clanVowPotionCount = val;
	}

	public String getAnnouncement() {
		return _announcement;
	}
	public void setAnnouncement(String val) {
		_announcement = val;
	}
	private String _announcement;
	
	public String getIntroductionMessage() {
		return _introductionMessage;
	}
	public void setIntroductionMessage(String val) {
		_introductionMessage = val;
	}
	
	public String getEnterNotice() {
		return _enter_notice;
	}
	public void setEnterNotice(String val) {
		_enter_notice = val;
		if (_enter_notice_pck != null) {
			_enter_notice_pck.clear();
		}
		if (!StringUtil.isNullOrEmpty(_enter_notice)) {
			_enter_notice_pck = new S_BloodPledgeEnterNoticeNoti(_enter_notice);
		}
	}
	
	public S_BloodPledgeEnterNoticeNoti getEnterNoticePck() {
		return _enter_notice_pck;
	}

	public int getEmblemId() {
		return _emblemId;
	}
	public void setEmblemId(int val) {
		_emblemId = val;
	}

	public int getEmblemStatus() {
		return _emblemStatus;
	}
	public void setEmblemStatus(int val) {
		_emblemStatus = val;
	}
	
	private ArrayList<ClanMember> clanMemberList = new ArrayList<ClanMember>();
	public ArrayList<ClanMember> getClanMemberList() {
		return clanMemberList;
	}
	
	public ClanMember getClanMember(int id) {
		for (int i=0; i<clanMemberList.size(); i++) {
			if (clanMemberList.get(i).memberId == id) {
				return clanMemberList.get(i);
			}
		}
		return null;
	}
	
	public ClanMember getClanMember(String name) {
		for (int i=0; i<clanMemberList.size(); i++) {
			if (clanMemberList.get(i).name.equals(name)) {
				return clanMemberList.get(i);
			}
		}
		return null;
	}

	public void addClanMember(String name, eBloodPledgeRankType rank, int level, String notes, int memberid, int type, int online, int contribution, int contributionWeek, int join_date, int logout_date, L1PcInstance pc) {
		clanMemberList.add(new ClanMember(name, rank, level, notes, memberid, type, online == 1, contribution, contributionWeek, join_date, logout_date, online == 1 ? pc : null));
	}
	
	public void removeClanMember(String name) {
		ClanMember[] list = clanMemberList.toArray(new ClanMember[clanMemberList.size()]);
		for (int i=0; i<list.length; i++) {
			if (list[i].name.equals(name)) {
				clanMemberList.remove(i);
				break;
			}
		}
	}
	
	public void setClanRank(String name, eBloodPledgeRankType rank){
		for (int i=0; i<clanMemberList.size(); i++) {
			if (clanMemberList.get(i).name.equals(name)) {
				clanMemberList.get(i).rank = rank;
				break;
			}
		}
	}
	
	public int getClanRankCount(eBloodPledgeRankType rank){
		int count=0;
		for (ClanMember member : clanMemberList) {
			if (member.rank == rank) {
				count++;
			}
		}
		return count;
	}

	public int getOnlineMaxUser() {
		return _maxuser;
	}
	public void setOnlineMaxUser(int val) {
		_maxuser = val;
	}
	
	public int getClanMemberContribution(String name){
		int contribution = 0;
		for (int i=0; i<clanMemberList.size(); i++) {
			if (clanMemberList.get(i).name.equals(name)) {
				contribution = clanMemberList.get(i).contribution;
				break;
			}
		}
		return contribution;
	}

	public void UpdataClanMember(String name, eBloodPledgeRankType rank) {
		for (int i=0; i<clanMemberList.size(); i++) {
			if (clanMemberList.get(i).name.equals(name)) {
				clanMemberList.get(i).rank = rank;
				break;
			}
		}
	}
	public void updateClanMemberOnline(L1PcInstance pc) {
		for (ClanMember clan : clanMemberList) {
			if (clan.memberId != pc.getId()) {
				continue;
			}
			clan.online = pc.getOnlineStatus() == 1;
			clan.player = clan.online ? pc : null;
			if (!clan.online) {
				clan.logout_date = (int)(System.currentTimeMillis() / 1000);
			}
			break;
		}
	}
	public String[] getAllMembersName() {							
		ArrayList<String> members = new ArrayList<String>();					
		ClanMember member;					
		for (int i = 0 ; i < clanMemberList.size() ; i++) {					
			member = clanMemberList.get(i);				
			if (!members.contains(member.name)) {
				members.add(member.name);
			}
		}
		return members.toArray(new String[members.size()]);					
	}

	public Timestamp getClanBirthDay() {
		return _clanBirthday;
	}
	public void setClanBirthDay(Timestamp val){
		_clanBirthday = val;
	}
	
	public int getClanId() {
		return _clanId;
	}
	public void setClanId(int val) {
		_clanId = val;
	}

	public String getClanName() {
		return _clanName;
	}
	public void setClanName(String val) {
		_clanName = val;
	}

	public int getLeaderId() {
		return _leaderId;
	}
	public void setLeaderId(int val) {
		_leaderId = val;
	}

	public String getLeaderName() {
		return _leaderName;
	}
	public void setLeaderName(String val) {
		_leaderName = val;
	}

	public int getCastleId() {
		return _castleId;
	}
	public void setCastleId(int val) {
		_castleId = val;
	}

	public int getHouseId() {
		return _houseId;
	}
	public void setHouseId(int val) {
		_houseId = val;
	}

	public FastMap<Integer, Integer> getAlliance() {
		return _alliance;
	}
	public void setAlliance(FastMap<Integer, Integer> val) {
		_alliance = val;
	}
	public boolean isAlliance(int clanId) {
		if (_alliance == null || _alliance.isEmpty()) {
			return false;
		}
		return _alliance.containsKey(clanId);
	}
	
	public ServerBasePacket getAlliancePck(){
		if (_alliancePck == null && _alliance != null && _alliance.size() > 0) {
			_alliancePck = new S_BloodPledgeAllyList(this);// 동맹 패킷 세팅
		}
		return _alliancePck;
	}
	
	// 동맹 정보 초기화
	public void disposeAlliance(){
		if (_alliancePck != null) {
			_alliancePck.close();
			_alliancePck = null;
		}
		_alliance.clear();
	}
	
	// 동맹 생성
	public void createAlliance(L1PcInstance leader, L1PcInstance allianceLeader){
		L1Clan allianceClan = allianceLeader.getClan();
		
		// reset
		disposeAlliance();
		allianceClan.disposeAlliance();
		
		// aliance regist
		_alliance.put(allianceClan.getClanId(), L1Clan.ALLIANCE_NORMAL);
		allianceClan._alliance.put(getClanId(), L1Clan.ALLIANCE_MASTER);
		
		// database update
		ClanTable clanTable = ClanTable.getInstance();
		clanTable.updateClan(this);
		clanTable.updateClan(allianceClan);
		
		// packet create and send
		leader.sendPackets(getAlliancePck());
		allianceLeader.sendPackets(allianceClan.getAlliancePck());
		
		// complete msg
		leader.sendPackets(new S_ServerMessage(1200, allianceClan.getClanName()), true);//동맹: {0} 혈맹과 동맹을 결성함
		allianceLeader.sendPackets(new S_ServerMessage(1200, getClanName()), true);//동맹: {0} 혈맹과 동맹을 결성함
	}
	
	// 동맹 해산
	public void destroyAlliance(L1PcInstance leader){
		L1World world				= L1World.getInstance();
		ClanTable clanTable 		= ClanTable.getInstance();
		int allianceClanId			= getAlliance().keySet().iterator().next();
		L1Clan allianceClan			= world.getClan(allianceClanId);
		if (allianceClan == null) {
			System.out.println("destroyAliance method - targetClan empty requestClanId : " + getClanId() + ", targetClanId : " + allianceClanId);
			return;
		}
		L1PcInstance allianceLeader	= world.getPlayer(allianceClan.getLeaderName());
		
		// reset
		disposeAlliance();
		allianceClan.disposeAlliance();
		
		// database update
		clanTable.updateClan(this);
		clanTable.updateClan(allianceClan);
		
		// msg send
		S_ServerMessage leaveMsg = new S_ServerMessage(1204, getClanName());// 동맹: %s 혈맹이 동맹을 해제함
		if (allianceLeader != null) {// 동맹 군주가 접속중일경우 패킷 전송
			allianceLeader.sendPackets(leaveMsg);
			allianceLeader.sendPackets(S_BloodPledgeAllyListChange.DISMISS);// ui reset
		}
		leader.sendPackets(leaveMsg, true);
		leader.sendPackets(S_BloodPledgeAllyListChange.DISMISS);// ui reset
	}

	// 온라인중의 혈원수
	public int getOnlineMemberCount() {
		int count = 0;
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (L1World.getInstance().getPlayer(clanMemberList.get(i).name) != null) {
				count++;
			}
		}
		return count;
	}

	// 온라인중 혈원 인스턴스 리스트
	public L1PcInstance[] getOnlineClanMember() {
		ArrayList<L1PcInstance> onlineMembers = new ArrayList<L1PcInstance>();
		L1PcInstance pc = null;
		for (int i = 0; i < clanMemberList.size(); i++) {
			pc = L1World.getInstance().getPlayer(clanMemberList.get(i).name);
			if (pc != null && !onlineMembers.contains(pc)) {
				onlineMembers.add(pc);
			}
		}
		return onlineMembers.toArray(new L1PcInstance[onlineMembers.size()]);
	}
	
	// 전체 혈원 네임 리스트
	public String getAllMembersFP() {
		String result	= StringUtil.EmptyString;
		String rank		= StringUtil.EmptyString;
		for (int i = 0; i < clanMemberList.size(); i++) {
			result = result + clanMemberList.get(i).name + rank + StringUtil.EmptyOneString;
		}
		return result;
	}

	// 온라인중의 혈원 네임 리스트
	public String getOnlineMembersFP() {
		String result	= StringUtil.EmptyString;
		String rank		= StringUtil.EmptyString;
		L1PcInstance pc = null;
		for (int i = 0; i < clanMemberList.size(); i++) {
			pc = L1World.getInstance().getPlayer(clanMemberList.get(i).name);
			if (pc != null) {
				result = result + clanMemberList.get(i).name + rank + StringUtil.EmptyOneString;
			}
		}
		return result;
	}
	
	/**혈맹자동가입*/
	public boolean isBot() {
		return _bot;
	}
	public void setBot(boolean val) {
		_bot = val;
	}

	public int getBotStyle() {
		return _botStyle;
	}
	public void setBotStyle(int val) {
		_botStyle = val;
	}

	public int getBotLevel() {
		return _botLevel;
	}
	public void setBotLevel(int val) {
		_botLevel = val;
	}
	
	/**
	 * 접속중인 권한등급 혈맹원
	 * @return L1PcInstance
	 */
	public L1PcInstance getOnlineAuthMember() {
		L1PcInstance king = null, prince = null, knight = null;
		for (ClanMember member : clanMemberList) {
			if (member == null || !member.online || member.player == null) {
				continue;
			}
			eBloodPledgeRankType rank = member.player.getBloodPledgeRank();
			if (!eBloodPledgeRankType.isAuthRankAtKnight(rank)) {
				continue;
			}
			if (rank == eBloodPledgeRankType.RANK_NORMAL_KING) {
				king	= member.player;
			} else if (rank == eBloodPledgeRankType.RANK_NORMAL_PRINCE) {
				prince	= member.player;
			} else {
				knight	= member.player;
			}
		}
		return king != null ? king : prince != null ? prince : knight != null ? knight : null;// 군주 -> 부군주 -> 수호기사
	}

	public boolean isEnableJoin() {
		return _isEnableJoin;
	}
	public void setEnableJoin(boolean val) {
		_isEnableJoin = val;
	}

	public ePLEDGE_JOIN_REQ_TYPE getJoinType() {
		return _joinType;
	}
	public void setJoinType(ePLEDGE_JOIN_REQ_TYPE val) {
		_joinType = val;
	}
	
	public String getJoinPassword() {
		return _joinPassword;
	}
	public void setJoinPassword(String val) {
		_joinPassword = val;
	}
	
	/** 혈맹버프 포인트 **/	
	private int _bless, _blessCount, _attack, _defence, _pvpAttack, _pvpDefence;
	public int[] getBuffTime = new int[] {
			_attack, _defence, _pvpAttack, _pvpDefence
	};

	public int[] getBuffTime() {
		return getBuffTime;
	}
	public void setBuffTime(int i, int j) {
		getBuffTime[i] = IntRange.ensure(j, 0, 172800);
	}
	public void setBuffTime(int a, int b, int c, int d) {
		getBuffTime = new int[] { a, b, c, d };
	}

	public int getBlessCount() {
		return _blessCount;
	}
	public void setBlessCount(int val) {
		_blessCount = IntRange.ensure(val, 0, 400000000);
	}
	public void addBlessCount(int val) {
		_blessCount = IntRange.ensure(_blessCount + val, 0, 400000000);
	}

	public int getBless() {
		return _bless;
	}
	public void setBless(int val) {
		_bless = val;
	}
	
	private int _buffFirst, _buffSecond, _buffThird;
	private int _einhasadBlessBuff;

	public int getBuffFirst() {
		return _buffFirst;
	}
	public void setBuffFirst(int val) {
		_buffFirst = val;
	}

	public int getBuffSecond() {
		return _buffSecond;
	}
	public void setBuffSecond(int val) {
		_buffSecond = val;
	}

	public int getBuffThird() {
		return _buffThird;
	}
	public void setBuffThird(int val) {
		_buffThird = val;
	}
	
	public int getEinhasadBlessBuff() {
		return _einhasadBlessBuff;
	}
	public void setEinhasadBlessBuff(int val) {
		_einhasadBlessBuff = val;
	}
	
	public LinkedList<L1PledgeJoinningRequest> getJoinningList() {
		return _joinningList;
	}
	public void setJoinningList(LinkedList<L1PledgeJoinningRequest> val) {
		_joinningList = val;
	}
	public boolean isJoinningList(int user_uid) {
		if (_joinningList == null || _joinningList.isEmpty()) {
			return false;
		}
		for (L1PledgeJoinningRequest request : _joinningList) {
			if (request.getUser_uid() == user_uid) {// 이미 요청 중
				return true;
			}
		}
		return false;
	}
	public void addJoinningList(L1PledgeJoinningRequest val) {
		if (_joinningList == null) {
			_joinningList = new LinkedList<L1PledgeJoinningRequest>();
		}
		_joinningList.add(val);
	}
	public void removeJoinningList(int user_uid) {
		if (_joinningList == null) {
			return;
		}
		L1PledgeJoinningRequest del = null;
		for (L1PledgeJoinningRequest request : _joinningList) {
			if (request.getUser_uid() == user_uid) {
				del = request;
				break;
			}
		}
		if (del != null) {
			_joinningList.remove(del);
			if (_joinningList.isEmpty()) {
				_joinningList = null;
			}
		}
	}
	public void removeJoinningList(String user_name) {
		if (_joinningList == null) {
			return;
		}
		L1PledgeJoinningRequest del = null;
		for (L1PledgeJoinningRequest request : _joinningList) {
			if (request.getUser_name().equalsIgnoreCase(user_name)) {
				del = request;
				break;
			}
		}
		if (del != null) {
			_joinningList.remove(del);
			if (_joinningList.isEmpty()) {
				_joinningList = null;
			}
		}
	}
	
	public void clearJonningList() {
		if (_joinningList == null) {
			return;
		}
		_joinningList.clear();
		_joinningList = null;
	}
	
	public LinkedList<String> get_store_allow_list() {
		return _store_allow_list;
	}
	public boolean add_store_allow_list(String val) {
		if (_store_allow_list == null) {
			_store_allow_list = new LinkedList<String>();
		}
		if (_store_allow_list.size() >= Config.PLEDGE.PLEDGE_STORE_ALLOW_MAX_COUNT) {
			return false;
		}
		_store_allow_list.add(val);
		return true;
	}
	public void del_store_allow_list(String val) {
		_store_allow_list.remove(val);
	}
	public boolean is_store_allow(String val) {
		return _store_allow_list != null && _store_allow_list.contains(val);
	}
	
	public int get_limit_level() {
		return _limit_level;
	}
	public void set_limit_level(int val) {
		_limit_level = val;
	}
	
	public LinkedList<String> get_limit_user_names() {
		return _limit_user_names;
	}
	public boolean add_limit_user_names(String val) {
		if (_limit_user_names == null) {
			_limit_user_names = new LinkedList<String>();
		}
		if (_limit_user_names.size() >= Config.PLEDGE.PLEDGE_JOIN_LIMIT_MAX_COUNT) {
			return false;
		}
		_limit_user_names.add(val);
		return true;
	}
	public void del_limit_user_names(String val) {
		_limit_user_names.remove(val);
	}
	public boolean is_limit_user_names(String val) {
		return _limit_user_names != null && _limit_user_names.contains(val);
	}
	
	public long get_master_change_req_time() {
		return _master_change_req_time;
	}
	public void set_master_change_req_time(long val) {
		_master_change_req_time = val;
	}
	
	public void dispose() {
		if (_enter_notice_pck != null) {
			_enter_notice_pck.clear();
			_enter_notice_pck = null;
		}
		if (_alliance != null) {
			_alliance.clear();
			_alliance = null;
		}
		if (_alliancePck != null) {
			_alliancePck.clear();
			_alliancePck = null;
		}
		if (_joinningList != null) {
			_joinningList.clear();
			_joinningList = null;
		}
		if (_store_allow_list != null) {
			_store_allow_list.clear();
			_store_allow_list = null;
		}
		if (_limit_user_names != null) {
			_limit_user_names.clear();
			_limit_user_names = null;
		}
	}
}

