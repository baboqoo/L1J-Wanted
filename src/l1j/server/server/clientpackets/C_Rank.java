package l1j.server.server.clientpackets;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.dungeontimer.L1DungeonTimer;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerUser;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Extended;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class C_Rank extends ClientBasePacket {
	private static final String C_RANK = "[C] C_Rank";
	private static Logger _log = Logger.getLogger(C_Rank.class.getName());
	
	private static final SimpleDateFormat RANK_ENABLE_DATE_FORMAT	= new SimpleDateFormat("M-d HH:mm");
	
	private static final int PLEDGE_RANK		= 1;
	private static final int ALLIANCE_LIST		= 2;
	private static final int ALLIANCE_REQUEST	= 3;
	private static final int ALLIANCE_LEAVE		= 4;
	private static final int SURVIVAL_CRY		= 5;
	private static final int WEAPON_COLOR		= 6;
	private static final int DUNGEON_TIME		= 8;
	private static final int ALLIANCE_KICK		= 9;
	private static final int ALLIANCE_DISTORY	= 10;
	
	private L1PcInstance pc;
	private L1Clan clan;
	private String clanname;

	public C_Rank(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		int type	= readC();
		clan		= pc.getClan();
		clanname	= pc.getClanName();

		switch (type) {
		case PLEDGE_RANK:// 계급
			pledgeRankSetting();
			break;
		case ALLIANCE_LIST:// 동맹 목록
			/*if(clan != null && !clan.getAlliance().isEmpty()){
				pc.sendPackets(new S_PacketBox(pc, S_PacketBox.ALLIANCE_LIST), true);
			}*/
			break;
		case ALLIANCE_REQUEST:// 동맹 신청
			allianceRequest();
			break;
		case ALLIANCE_LEAVE:// 동맹 탈퇴
			allianceLeave(false);
			break;
		case SURVIVAL_CRY: // 생존의 외침 (CTRL + E)
			survivalCry();
			break;
		case WEAPON_COLOR: // 무기 허세 떨기 Alt + 0(숫자)
			weaponColor();
			break;
		case DUNGEON_TIME: // 던전 남은 시간
			dungeonTimePrint();
			break;
		case ALLIANCE_KICK:// 동맹 추방(바로 해산)
		case ALLIANCE_DISTORY:// 동맹 해산(바로 해산)
			allianceLeave(true);
			break;
		default:
			break;
		}		
	}
	
	void allianceLeave(boolean distroy){
		if (!eBloodPledgeRankType.isAuthRankAtKnight(pc.getBloodPledgeRank())) {// 권한 검증
			pc.sendPackets(L1ServerMessage.sm1206);// 25레벨이상 혈맹 군주만 동맹신청을 할 수 있습니다. 또한 연합 군주는 동맹을 맺을 수 없습니다.
			return;
		}
		for (L1War war : L1World.getInstance().getWarList()) {
			if (war.CheckClanInWar(clanname)) {
				pc.sendPackets(L1ServerMessage.sm1203); // 전쟁중에는 동맹을 탈퇴할 수 없습니다.
				return;
			}
		}
		if (clan.getAlliance().isEmpty()) {
			pc.sendPackets(L1ServerMessage.sm1233); // 동맹이 없습니다.
			return;
		}
		if (distroy) {
			pc.getClan().destroyAlliance(pc);
		} else {
			pc.sendPackets(S_MessageYN.ALLIANCE_CANCEL_YN);// 정말로 동맹을 탈퇴하시겠습니까? (Y/N)
		}
	}
	
	void allianceRequest(){
		if (pc.getLevel() < 25 || !eBloodPledgeRankType.isAuthRankAtKnight(pc.getBloodPledgeRank())) {// 권한 검증
			pc.sendPackets(L1ServerMessage.sm1206);// 25레벨이상 혈맹 군주만 동맹신청을 할 수 있습니다. 또한 연합 군주는 동맹을 맺을 수 없습니다.
			return;
		}
		String allianceName = readS();
		if (allianceName.equals(clanname)) {// 자신의 혈맹
			return;
		}
		L1Clan allianceClan = L1World.getInstance().getClan(allianceName);
		if (allianceClan == null) {// 혈맹 미존재
			pc.sendPackets(L1SystemMessage.TARGET_CLAN_EMPTY);
			return;
		}
		L1PcInstance allianceLeader = (L1PcInstance)L1World.getInstance().findObject(allianceClan.getLeaderId());
		if (allianceLeader == null) {
			pc.sendPackets(new S_ServerMessage(218, allianceName), true); // \f1%0 혈맹의 군주는 현재 월드에 없습니다.
			return;
		}
		if (!allianceClan.getAlliance().isEmpty()) {
			pc.sendPackets(L1ServerMessage.sm1202);// 이미 동맹에 가입된 상태입니다.
			return;
		}
		if (!clan.getAlliance().isEmpty()) {
			pc.sendPackets(L1SystemMessage.CLAN_ALLIANCE_MAX);
			return;
		}
		for (L1War war : L1World.getInstance().getWarList()) {
			if (war.CheckClanInWar(clanname)) {
				pc.sendPackets(L1ServerMessage.sm1234); // 전쟁중에는 동맹에 가입할 수 없습니다.
				return;
			}
		} 
		if (allianceLeader.getLevel() > 24 && allianceLeader.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_KING) {
			allianceLeader.setTempID(pc.getId());
			allianceLeader.sendPackets(new S_MessageYN(223, pc.getName()), true);
		} else {
			pc.sendPackets(L1ServerMessage.sm1201);// 동맹에 가입할 수 없습니다.
		}
	}
	
	void weaponColor() {
		if (pc.getWeapon() == null) {
			pc.sendPackets(L1ServerMessage.sm1973);
			return;
		}
		int enchantLevel = pc.getWeapon().getEnchantLevel();
		if (enchantLevel < 0) {
			pc.sendPackets(L1ServerMessage.sm79);
			return;
		}
		int gfx3 = 8773;
		if (enchantLevel > 10) {
			gfx3 = 8686;
		} else if (enchantLevel > 8) {
			gfx3 = 8773;
		} else if (enchantLevel > 6) {
			gfx3 = 8685;
		}
		pc.send_effect(gfx3);
	}
	
	void survivalCry(){
		if (pc.getFood() < GameServerSetting.MAX_FOOD_VALUE) {
			pc.sendPackets(L1ServerMessage.sm3461);// 포만감이 부족하여 사용할 수 없습니다.
			return;
		}
		int addHp = 0, gfxId = 0;
		long systime	= System.currentTimeMillis();
		long nexttime	= pc.getSurvivalTime().getTime() + (10800000);
		if (pc.getLevel() >= 90) {
			nexttime -= (pc.getLevel() - 89) * (600000);
		}
		if (nexttime > systime) {
			long time = (nexttime - systime) / 1000;
			//pc.sendPackets(L1ServerMessage.sm1974);// 생존의 외침: 대기중
			//pc.sendPackets(new S_SystemMessage(String.format("생존의 외침: %d분 %d초 후 사용가능.", time / 60, time % 60)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(27), String.valueOf(time / 60), String.valueOf(time % 60)), true);
			return;
		}
		if (pc.getLevel() >= 90) {
			gfxId = 19294;
			addHp = 1200;
		} else if (pc.getLevel() >= 88) {
			gfxId = 19292;
			addHp = 1000;
		} else if (pc.getLevel() >= 80) {
			gfxId = 19290;
			addHp = 800;
		} else if (pc.getLevel() >= 70) {
			gfxId = 19288;
			addHp = 800;
		} else {
			gfxId = 19286;
			addHp = 800;
		}
		pc.send_effect(gfxId);
		if (pc.getSurvivalTime() != null) {
			pc.getSurvivalTime().setTime(systime);
		} else {
			pc.setSurvivalTime(new Timestamp(systime));
		}
		pc.setFood(0);
		pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, 0), true);
		pc.setCurrentHp(pc.getCurrentHp() + addHp);
		pc.sendPackets(new S_Extended(0, 10800 - (pc.getLevel() >= 90 ? (pc.getLevel() - 89) * 600 : 0)), true);
	}
	
	void dungeonTimePrint(){
		L1DungeonTimer handler = pc.getDungoenTimer();
		for (L1DungeonTimerUser timer : handler.getTimers().values()) {
			if (StringUtil.isNullOrEmpty(timer.getInfo().getDescId())) {
				continue;
			}
			int time = 0;
			time = handler.getTimerValue(timer.getInfo()) - timer.getRemainSecond();
			time = time > 0 ? time / 60 : 0;
			pc.sendPackets(new S_ServerMessage(2535, timer.getInfo().getDescId(), Integer.toString(time)), true);
		}
	}
	
	/**
	 * 혈맹 계급 변경
	 * @throws Exception
	 */
	void pledgeRankSetting() throws Exception{
		if (!eBloodPledgeRankType.isAuthRankAtKnight(pc.getBloodPledgeRank())) {// 계급을 변경할 수 있는 권한 검증
			pc.sendPackets(L1SystemMessage.CLAN_RANK_LOW_FAIL);
			return;
		}
		eBloodPledgeRankType req_rank	= eBloodPledgeRankType.fromInt(readC());
		
		// 자신의 계급과 같거나 높은 계급은 부여할 수 없다.
		if (pc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_PRINCE && eBloodPledgeRankType.isAuthRankAtPrince(req_rank)) {
			pc.sendPackets(L1SystemMessage.CLAN_RANK_HIGH_FAIL);
			return;
		}
		if (pc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_KNIGHT && eBloodPledgeRankType.isAuthRankAtKnight(req_rank)) {
			pc.sendPackets(L1SystemMessage.CLAN_RANK_HIGH_FAIL);
			return;
		}
		
		if (req_rank == eBloodPledgeRankType.RANK_NORMAL_PRINCE && clan.getClanRankCount(req_rank) >= Config.PLEDGE.RANK_NORMAL_PRINCE_COUNT) {
			pc.sendPackets(L1ServerMessage.sm7921);// 계급: 부군주는 1명까지 임명 가능
			return;
		}
		if (req_rank == eBloodPledgeRankType.RANK_NORMAL_KNIGHT && clan.getClanRankCount(req_rank) >= Config.PLEDGE.RANK_NORMAL_KNIGHT_COUNT) {
			pc.sendPackets(L1ServerMessage.sm2071);// 계급: 수호 기사는 5명까지 임명 가능
			return;
		}
		if (req_rank == eBloodPledgeRankType.RANK_NORMAL_ELITE_KNIGHT && clan.getClanRankCount(req_rank) >= Config.PLEDGE.RANK_NORMAL_ELITE_KNIGHT_COUNT) {
			pc.sendPackets(L1ServerMessage.sm3591);// 계급: 정예 계급은 15명까지 임명 가능
			return;
		}
		if (req_rank == eBloodPledgeRankType.RANK_NORMAL_JUNIOR_KNIGHT && clan.getClanRankCount(req_rank) >= Config.PLEDGE.RANK_NORMAL_JUNIOR_KNIGHT) {
			pc.sendPackets(L1ServerMessage.sm7922);// 계급: 일반 계급은 28명까지 임명 가능
			return;
		}
		String target_name	= readS();
		L1PcInstance targetPc = L1World.getInstance().getPlayer(target_name);
		if (targetPc == pc) {
			return;
		}
		if (targetPc != null) {
			if (pc.getClanid() != targetPc.getClanid()) {
				pc.sendPackets(L1ServerMessage.sm414);	
				return;
			}
			if (!isRankUpdateEnable(targetPc, req_rank)) {
				return;
			}
			rankChange(targetPc, req_rank);
			//targetPc.sendPackets(new S_SystemMessage(String.format("계급: %s(으)로 계급 임명함", req_rank.toDesc())), true);
			targetPc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(28), req_rank.toDesc()), true);
		} else {
			targetPc = CharacterTable.getInstance().restoreCharacter(target_name);
			if (targetPc == null || targetPc.getClanid() != pc.getClanid()) {
				pc.sendPackets(L1ServerMessage.sm414);	
				return;
			}
			if (!isRankUpdateEnable(targetPc, req_rank)) {
				return;
			}
			rankChange(targetPc, req_rank);
			targetPc = null;
		}
	}
	
	/**
	 * 혈맹 계급 부여 유효성 검증
	 * 가입 후 7일(168시간) 경과 시 정예 기사 이상 계급 부여 가능
	 * 계급 부여 이후 추가 계급 변경의 경우 3일(72시간) 뒤 변경 가능
	 * @param targetPc
	 * @param rank
	 * @return boolean
	 */
	boolean isRankUpdateEnable(L1PcInstance targetPc, eBloodPledgeRankType rank) {
		if (targetPc.getBloodPledgeRank() == rank) {// 요청 등급이 같다면 변경하지 않는다.
			return false;
		}
		if (!eBloodPledgeRankType.isAuthRankAtEliteKnight(rank)) {
			return true;
		}
		
		int rank_date		= targetPc.getPledgeRankDate();// 랭크 적용일
		int join_date		= targetPc.getPledgeJoinDate();// 혈맹 가입일
		boolean is_first	= rank_date == 0;
		int roll_hour		= is_first ? Config.PLEDGE.RANK_UPDATE_ROLL_DEFUALT_HOUR : Config.PLEDGE.RANK_UPDATE_ROLL_NEXT_HOUR;
		if (roll_hour <= 0) {
			return true;
		}
		
		int current_time	= (int) (System.currentTimeMillis() / 1000);
		int enable_time		= (is_first ? join_date : rank_date) + (roll_hour * 3600);// 변경 가능 일
		if (enable_time > current_time) {// 가능한 시간이 현재 시간보다 크다면 경과되지 않앗다.
			Calendar cal	= Calendar.getInstance();
			cal.add(Calendar.SECOND, enable_time - current_time);
			pc.sendPackets(new S_ServerMessage(3586, RANK_ENABLE_DATE_FORMAT.format(cal.getTime())), true);
			return false;
		}
		return true;
	}
	
	/**
	 * 혈맹 계급을 변경한다.
	 * @param targetPc
	 * @param rank
	 */
	void rankChange(L1PcInstance targetPc, eBloodPledgeRankType rank) {
		if (targetPc.isCrown() && targetPc.getId() == targetPc.getClan().getLeaderId()) {
			pc.sendPackets(L1SystemMessage.CLAN_RANK_HIGH_FAIL);
			return;
		}
		if (pc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_PRINCE && (targetPc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_KING || targetPc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_PRINCE)) {
			pc.sendPackets(L1SystemMessage.CLAN_RANK_HIGH_FAIL);
			return;
		}
		targetPc.setBloodPledgeRank(rank);
		targetPc.setPledgeRankDate((int)(System.currentTimeMillis() / 1000));
		try {
			targetPc.save(); // DB에 캐릭터 정보를 기입한다
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		clan.UpdataClanMember(targetPc.getName() ,targetPc.getBloodPledgeRank());
		pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank.toInt(), targetPc.getName()), true);// 군주에게 계급 알림
		//S_SystemMessage message = new S_SystemMessage(String.format("%s 님의 계급이 %s(으)로 변경되었습니다.", targetPc.getName(), rank.toDesc()));
		S_ServerMessage message = new S_ServerMessage(S_ServerMessage.getStringIdx(29), targetPc.getName(), rank.toDesc());
		for (L1PcInstance member : clan.getOnlineClanMember()) {
			member.sendPackets(message, true);
		}
		message.clear();
		message = null;
	}

	@Override
	public String getType() {
		return C_RANK;
	}
}


