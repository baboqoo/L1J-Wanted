package l1j.server.server.clientpackets;

import java.util.Collection;

import l1j.server.GameSystem.deathpenalty.DeathPenaltyTable;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimeChargedItem;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerUser;
import l1j.server.GameSystem.dungeontimer.loader.L1DungeonTimerLoader;
import l1j.server.LFCSystem.InstanceEnums.InstStatus;
import l1j.server.LFCSystem.LFC.Creator.LFCCreator;
import l1j.server.common.data.eDistributionType;
import l1j.server.server.GameClient;
import l1j.server.server.GameServer;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.L1Status;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.BossSpawnTable;
import l1j.server.server.datatables.BossSpawnTable.BossTemp;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.datatables.ClanAttentionTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanJoin;
import l1j.server.server.model.L1DeathMatch;
import l1j.server.server.model.L1HauntedHouse;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Racing;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Resurrection;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.system.S_ShelterOwnerChange;
import l1j.server.server.serverpackets.trade.S_Trade;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class C_Attr extends ClientBasePacket {
	private static final String C_ATTR = "[C] C_Attr";
	
	public static final int YN_MESSAGE_CODE					= 5902;
	public static final int MSGCODE_BOSS					= 1;
	public static final int MSGCODE_LFC						= 2;
	public static final int MSGCODE_SERVER_RESET			= 13;
	public static final int MSGCODE_DUNGEON_TIMER_RESET		= 14;
	public static final int MSGCODE_INGAME_LOGIN_AUTH		= 15;
	
	public static final int MSGCODE_NO						= 0;
	public static final int MSGCODE_YES						= 1;
	
	private L1PcInstance pc;
	
	public C_Attr(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		int i = readH();
		int attrcode;
		int msgIdx = 0;
		if (i == 479) {
			attrcode = i;
		} else {
			msgIdx		= readD();
			attrcode	= readH();
		}
		
		if (attrcode == YN_MESSAGE_CODE) {// YN 메세지 코드
			if (msgIdx == MSGCODE_BOSS) {// 보스 레이드 Y_N
	            bossTeleport(readC());
	            return;
	        }
			if (msgIdx == MSGCODE_LFC) {// 앱센터 LFC
				lfcRegist(readC());
				return;
			}
			if (msgIdx == MSGCODE_SERVER_RESET) {// 서버 초기화
				serverReset(readC());
	            return;
	        }
			if (msgIdx == MSGCODE_DUNGEON_TIMER_RESET) {// 던전시간 초기화
				dungeonTimerRest(readC());
				return;
			}
			if (msgIdx == MSGCODE_INGAME_LOGIN_AUTH) {// 인게임 로그인 승인
				webserverIngameLoginAuth(readC());
				return;
			}
		}
		
		switch (attrcode) {
		case 479: // 어느 능력치를 향상시킵니까? (str, dex, int, con, wis, cha)
			status(readC());
			break;
		case 180:// 셰이프체인지로 변신하기
			readP(1);
			poly(readS());
			break;
		case 3348:// 혈맹문장주시: %0 혈맹의 문장 주시를 승낙 하시겠습니까?
			pledgeAttention(readC());
			break;
		case 97:// %0가 혈맹에 가입했지만은 있습니다. 승낙합니까? (Y/N)
			pledgeJoinConfirm(readH());
			break;
		case 217:// %0혈맹의%1가 당신의 혈맹과의 전쟁을 바라고 있습니다. 전쟁에 응합니까? (Y/N)
		case 221:// %0혈맹이 항복을 바라고 있습니다. 받아들입니까? (Y/N)
		case 222:// %0혈맹이 전쟁의 종결을 바라고 있습니다. 종결합니까? (Y/N)
			pledgeWarResponse(readC(), attrcode);
			break;
		case 223:// 동맹 승낙
			pledgeAllianceResponse();
			break;
		case 1210:// 동맹 탈퇴
			pledgeAllianceDestroy();
			break;
		case 252: // %0%s가 당신과 아이템의 거래를 바라고 있습니다. 거래합니까? (Y/N)
			tradeStartResponse(readC());
			break;
		case 321:// 부활하고 싶습니까? (Y/N)
			resurrection(readC(), false);
			break;
		case 322:// 부활하고 싶습니까? (Y/N)
			resurrection(readC(), true);
			break;
		case 325:// 동물의 이름을 결정해 주세요：
			setCompanionName();
			break;
		case 512: // 가의 이름은?
			setHouseName();
			break;
		case 622:
			buddyConfirm(readC());
			break;
		case 630:
			fightConfirm(readC());
			break;
		case 653:// 이혼을 하면(자) 링은 사라져 버립니다. 이혼을 바랍니까? (Y/N)
			divorceConfirm(readC());
			break;
		case 654:// %0%s당신과 결혼 하고 싶어하고 있습니다. %0과 결혼합니까? (Y/N)
			marryConfirm(readC());
			break;
		case 951:// 채팅 파티 초대를 허가합니까? (Y/N)
			chatParyResponse(readC());
			break;
		case 953:// 파티 초대를 허가합니까? (Y/N)
		case 954:// 자동 분배 파티를 허가합니까? (Y/N)
			partyConfirm(readC(), attrcode == 953 ? eDistributionType.GET_PRIORITY : eDistributionType.AUTO_DISTRIBUTION);
			break;
		case 1256:// 경기장에 입장하시겠습니까? (Y/N)
			racingGameEnter(readC());
			break;
		case 1268:// 데스매치에 입장하시겠습니까? (Y/N)
			deathMatchEnter(readC());
			break;
		case 2923:// 드래곤 레이드
			dragonPortalEnter(readC());
			break;
		case 3401:
			halpasPortalEnter(readC());
			break;
		case 9134:// 아지트 양도
			shelterOwnerChangeRequest();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 스탯 변경
	 * @param code
	 * @throws Exception
	 */
	void status(int code) throws Exception {
		if (code == MSGCODE_YES) {
			final int bonusAblity = pc.getBonusStats();
			if (pc.isTwoLogin() || !(pc.getLevel() - 50 > bonusAblity)) {// 스텟 버그
				return;
			}
			if (pc.getOnlineStatus() != 1) {
				pc.denals_disconnect(String.format("[C_Attr] NOT_ONLINE_DISCONNECT : NAME(%s)", pc.getName()));
				return;
			}
			
			if (statUp(readS())) {
				pc.setBonusStats(bonusAblity + 1);
				pc.sendPackets(new S_OwnCharStatus2(pc), true);
				pc.sendPackets(new S_CharVisualUpdate(pc), true); // 케릭정보 업뎃
				pc.save(); // DB에 캐릭터 정보 저장
			} else {
				pc.sendPackets(L1ServerMessage.sm481);
			}
			pc.CheckStatus();
			if (pc.getLevel() >= 51 && pc.getLevel() - 50 > pc.getBonusStats()) {
				if ((pc.getAbility().getStr() + pc.getAbility().getDex() + pc.getAbility().getCon() + pc.getAbility().getInt() + pc.getAbility().getWis() + pc.getAbility().getCha()) < 150) {
					int upstat = (pc.getLevel() - 50) - (pc.getBonusStats());
					String up = Integer.toString(upstat);
					pc.sendPackets(new S_MessageYN(479, up), true);
				}
			}
		}
	}
	
	/**
	 * 스탯 변경
	 * @param type
	 * @return boolean
	 */
	boolean statUp(String type){
		int maxCount = pc.getLevel() >= 100 ? 60 : pc.getLevel() >= 90 ? 55 : 50;
		if (type.toLowerCase().equals(L1Status.STR.getName().toLowerCase()) && pc.getAbility().getStr() < maxCount) {
			pc.getAbility().addStr((byte) 1);
			return true;
		}
		if (type.toLowerCase().equals(L1Status.DEX.getName().toLowerCase()) && pc.getAbility().getDex() < maxCount) {
			pc.getAbility().addDex((byte) 1);
			return true;
		}
		if (type.toLowerCase().equals(L1Status.CON.getName().toLowerCase()) && pc.getAbility().getCon() < maxCount) {
			pc.getAbility().addCon((byte) 1);
			return true;
		}
		if (type.toLowerCase().equals(L1Status.INT.getName().toLowerCase()) && pc.getAbility().getInt() < maxCount) {
			pc.getAbility().addInt((byte) 1);
			return true;
		}
		if (type.toLowerCase().equals(L1Status.WIS.getName().toLowerCase()) && pc.getAbility().getWis() < maxCount) {
			pc.getAbility().addWis((byte) 1);
			return true;
		}
		if (type.toLowerCase().equals(L1Status.CHA.getName().toLowerCase()) && pc.getAbility().getCha() < maxCount) {
			pc.getAbility().addCha((byte) 1);
			return true;
		}
		return false;
	}
	
	/**
	 * 교환 승인
	 * @param code
	 */
	void tradeStartResponse(int code) {
		if (pc.getTradeID() == 0 || pc.getTradeReady()) {
			return;
		}
		L1Object trading_partner = L1World.getInstance().findObject(pc.getTradeID());
		if (trading_partner != null) {
			if (trading_partner instanceof L1PcInstance) {
				L1PcInstance target = (L1PcInstance) trading_partner;
				if (target.getTradeReady()) {
					return;
				}
				if (code == MSGCODE_NO) { // No
					target.sendPackets(new S_ServerMessage(253, pc.getName()), true); 
					// %0%d는 당신과의 거래에 응하지 않았습니다.
					pc.setTradeID(0);
					target.setTradeID(0);
					pc.setTradeReady(false);
					target.setTradeReady(false);
				} else if (code == MSGCODE_YES) { // Yes
					if (pc.getLocation().getTileLineDistance(new Point(target.getX(), target.getY())) > 5) {
						pc.sendPackets(L1SystemMessage.TRADE_TARGET_RANGE_FAIL);
						pc.setTradeID(0);
						target.setTradeID(0);
						pc.setTradeReady(false);
						target.setTradeReady(false);
						return;
					}
					pc.sendPackets(new S_Trade(target.getName()), true);
					target.sendPackets(new S_Trade(pc.getName()), true);
					pc.setTradeReady(true);
					target.setTradeReady(true);
				}
			}
		} else {
			// 미니게임
			if (code == MSGCODE_NO) {// No
				pc.setTradeID(0);
			} else if (code == MSGCODE_YES) {// Yes
				if (pc.getX() == 33507 && pc.getY() == 32851 && pc.getMapId() == 4) {
					L1Npc npc	= NpcTable.getInstance().getTemplate(400064);
					pc.sendPackets(new S_Trade(npc.getDesc()), true);
				} else if (pc.getX() == 33515 && pc.getY() == 32851 && pc.getMapId() == 4) {
					L1Npc npc3	= NpcTable.getInstance().getTemplate(300027);
					pc.sendPackets(new S_Trade(npc3.getDesc()), true);
				}
			}
		}
	}
	
	/**
	 * 문장 주시
	 * @param code
	 */
	void pledgeAttention(int code) {
		L1PcInstance enemyClan_leader = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
		pc.setTempID(0);
		if (enemyClan_leader == null || enemyClan_leader.getClan() == null) {
			return;
		}
		L1Clan enemyClan = enemyClan_leader.getClan();
		if (enemyClan == null) {
			return;
		}
		if (enemyClan.getLeaderId() != enemyClan_leader.getId()) {
			return;
		}
		String enemyClan_name = enemyClan.getClanName();
		if (pc.getClanid() == 0) {
			return;
		}
		if (code == MSGCODE_NO){
			if (enemyClan_leader != null) {
				enemyClan_leader.sendPackets(L1SystemMessage.TARGET_CLAN_CROWN_NO_AGREE);
			}
		} else if (code == MSGCODE_YES) {
			ClanAttentionTable attention = ClanAttentionTable.getInstance();
			attention.deleteEmblemAttention(pc.getClanName(), enemyClan_name);
			attention.deleteEmblemAttention(enemyClan_name, pc.getClanName());
			attention.writeEmblemAttention(pc.getClanName(), enemyClan_name);
			attention.writeEmblemAttention(enemyClan_name, pc.getClanName());
		}
	}
	
	/**
	 * 혈맹 가입 신청 응답
	 * @param code
	 */
	void pledgeJoinConfirm(int code) {
		L1PcInstance joinPc = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
		pc.setTempID(0);
		if (joinPc != null) {
			if (code == MSGCODE_NO) {
				joinPc.sendPackets(new S_ServerMessage(96, pc.getName()), true); // \f1%0은 당신의요청을거절했습니다.
			} else if (code == MSGCODE_YES) {
				L1ClanJoin.getInstance().join(pc, joinPc);
			}
		}
	}
	
	/**
	 * 혈전 요청 응답
	 * @param code
	 * @param attrcode
	 */
	void pledgeWarResponse(int code, int attrcode) {
		L1PcInstance enemyLeader = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());// 전쟁 신청한 pc
		pc.setTempID(0);
		if (enemyLeader == null) {
			return;
		}
		String clanName = pc.getClanName();
		String enemyClanName = enemyLeader.getClanName();// 전쟁 신청한 혈맹
		if (code == MSGCODE_NO) { // No
			if (attrcode == 217) {
				enemyLeader.sendPackets(new S_ServerMessage(236, clanName), true); // %0혈맹이 당신의 혈맹과의 전쟁을 거절했습니다.
			} else if (attrcode == 221 || attrcode == 222) {
				enemyLeader.sendPackets(new S_ServerMessage(237, clanName), true); // %0혈맹이 당신의 제안을 거절했습니다.
			}
		} else if (code == MSGCODE_YES) { // Yes
			if (attrcode == 217) {
				L1War war = new L1War();
				war.handleCommands(2, enemyClanName, clanName); // 모의전 개시
			} else if (attrcode == 221 || attrcode == 222) {
				// 전쟁 리스트를 취득
				for (L1War war : L1World.getInstance().getWarList()) {
					if (war.CheckClanInWar(clanName)) { // 자크란이 가고 있는 전쟁을 발견
						if (attrcode == 221) {
							war.SurrenderWar(enemyClanName, clanName); // 항복
						} else if (attrcode == 222) {
							war.CeaseWar(enemyClanName, clanName); // 종결
						}
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 동맹 신청 응답
	 */
	void pledgeAllianceResponse() {
		L1PcInstance alianceLeader = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());// 동맹 신청한 pc
		pc.setTempID(0);
		if (alianceLeader == null) {
			return;
		}
		int c = readC();// yn code
		if (c == MSGCODE_NO) {
			alianceLeader.sendPackets(L1ServerMessage.sm1198);// 동맹: 상대가 신청을 거절함
		} else if (c == MSGCODE_YES) {
			pc.getClan().createAlliance(pc, alianceLeader);
		}
	}
	
	/**
	 * 동맹 파기
	 */
	void pledgeAllianceDestroy() {
		if (readC() == MSGCODE_YES) {
			pc.getClan().destroyAlliance(pc);
		}
	}
	
	/**
	 * 친구 추가 응답
	 * @param code
	 */
	void buddyConfirm(int code) {
		BuddyTable buddyTable = BuddyTable.getInstance();
		L1Buddy buddyList = buddyTable.getBuddyTable(pc.getId());
		L1PcInstance target2 = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
		pc.setTempID(0);
		String name2 = pc.getName();
		if (code == MSGCODE_NO) { // No
			if (target2 != null) {// 있다면
				//target2.sendPackets(new S_SystemMessage(String.format("%s님이 친구 요청을 거절하였습니다.", pc.getName())), true);	
				target2.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(13), pc.getName()), true);
			}
			return;
		} else if (code == MSGCODE_YES) { // Yes
			if (target2 != null) {
				buddyList.add(name2, StringUtil.EmptyString);
				buddyTable.addBuddy(target2.getId(), name2, StringUtil.EmptyString);
				//target2.sendPackets(new S_SystemMessage(String.format("%s님이 친구 등록 되었습니다.", pc.getName())), true);
				target2.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(14), pc.getName()), true);
				//pc.sendPackets(new S_SystemMessage(String.format("%s님에게 친구 등록이 되었습니다.", target2.getName())), true);
				target2.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(15), target2.getName()), true);
			}
		} else {
			pc.sendPackets(L1SystemMessage.TARGET_NAME_CHECK_FAIL);
		}
	}
	
	/**
	 * 파티 요청 응답
	 * @param code
	 * @param distribution
	 */
	void partyConfirm(int code, eDistributionType distribution) {
		L1PcInstance target = (L1PcInstance) L1World.getInstance().findObject(pc.getPartyID());
		if (target != null) {
			if (code == MSGCODE_NO) { // No
				target.sendPackets(new S_ServerMessage(423, pc.getName()), true); // %0가 초대를 거부했습니다.
				pc.setPartyID(0);
			} else if (code == MSGCODE_YES) { // Yes
				/** 배틀존 **/
				if (target.getMapId() == 5153 || pc.getMapId() == 5153 
						|| pc.getMap().getBaseMapId() == 730 || pc.getMap().getBaseMapId() == 731
						|| target.getMap().getBaseMapId() == 730 || target.getMap().getBaseMapId() == 731) {
					target.sendPackets(new S_ServerMessage(423, pc.getName()), true); // %0가 초대를 거부했습니다.
					return;
				}

				if (target.isInParty()) { // 초대주가 파티중
					if (target.getParty().isVacancy() || target.isGm()) {// 파티에 빈 곳이 있다
						target.getParty().addMember(pc);
					} else {// 파티에 빈 곳이 없다
						target.sendPackets(L1ServerMessage.sm417); // 더 이상 파티 멤버를 받아들일 수 없습니다.
					}
				} else { // 초대주가 파티중이 아니다
					L1Party party = new L1Party();
					party.set_distribution(distribution);
					party.addMember(target);
					party.addMember(pc);
					//target.sendPackets(new S_ServerMessage(424, pc.getName()), true); // %0가 파티에 들어갔습니다.
				}
				pc.setPartyID(0);
			}
		}
	}
	
	/**
	 * 채팅 파티 응답
	 * @param code
	 */
	void chatParyResponse(int code) {
		L1PcInstance chatPc = (L1PcInstance) L1World.getInstance().findObject(pc.getPartyID());
		if (chatPc != null) {
			if (code == MSGCODE_NO) { // No
				chatPc.sendPackets(new S_ServerMessage(423, pc.getName()), true); // %0가 초대를 거부했습니다.
				pc.setPartyID(0);
			} else if (code == MSGCODE_YES) { // Yes
				if (chatPc.isInChatParty()) {
					if (chatPc.getChatParty().isVacancy() || chatPc.isGm()) {
						chatPc.getChatParty().addMember(pc);
					} else {
						chatPc.sendPackets(L1ServerMessage.sm417); // 더 이상 파티 멤버를 받아들일 수 없습니다.
					}
				} else {
					L1ChatParty chatParty = new L1ChatParty();
					chatParty.addMember(chatPc);
					chatParty.addMember(pc);
					chatPc.sendPackets(new S_ServerMessage(424, pc.getName()), true); // %0가 파티에 들어갔습니다.
				}
			}
		}
	}
	
	/**
	 * 결투 응답
	 * @param code
	 */
	void fightConfirm(int code) {
		L1PcInstance fightPc = (L1PcInstance) L1World.getInstance().findObject(pc.getFightId());
		if (code == MSGCODE_NO) {
			pc.setFightId(0);
			fightPc.setFightId(0);
			fightPc.sendPackets(new S_ServerMessage(631, pc.getName()), true);
		} else if (code == MSGCODE_YES) {
			fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, fightPc.getFightId(), fightPc.getId()), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, pc.getFightId(), pc.getId()), true);
		}
	}
	
	/**
	 * 펫 이름 변경
	 */
	void setCompanionName() {
		L1PetInstance companion = (L1PetInstance) L1World.getInstance().findObject(pc.getTempID());
		for (L1NpcInstance sum : pc.getPetList().values()) {
            if (sum instanceof L1NpcInstance) {
            	companion = (L1PetInstance)sum;
                break;
            }  
		}
		if (companion == null) {
			pc.sendPackets(L1ServerMessage.sm1301);
			return;
		}
		readP(1);
		companion.changeName(readS());
	}
	
	/**
	 * 아지트 이름 변경
	 */
	void setHouseName() {
		readP(1);
		String name = readS();
		int houseId = pc.getTempID();
		pc.setTempID(0);
		if (name.length() <= 16) {
			L1House house = HouseTable.getInstance().getHouseTable(houseId);
			house.setHouseName(name);
			HouseTable.getInstance().updateHouse(house); // DB에 기입해
		} else {
			pc.sendPackets(L1ServerMessage.sm513); // 가의 이름이 너무 깁니다.
		}
	}
	
	/**
	 * 영광의 아지트 양도 요청
	 */
	void shelterOwnerChangeRequest() {
		if (readC() != MSGCODE_YES) {
			return;
		}
		String name = readS();
		if (StringUtil.isNullOrEmpty(name)) {
			return;
		}
		if (name.length() > 16) {
			pc.sendPackets(L1ServerMessage.sm513);// 가의 이름이 너무 깁니다.
			return;
		}
		L1PcInstance target = L1World.getInstance().getPlayer(name);
		if (target == null) {
			pc.sendPackets(new S_ShelterOwnerChange(S_ShelterOwnerChange.eRES.eNotTarget, 0), true);// 대상을 찾을 수 없습니다.
			return;
		}

		// A_ShelterOwnerChange 에서 처리된다.
		pc.sendPackets(new S_MessageYN(9135, name), true);// %s에게 아지트를 양도하는 것이 맞는지 다시 한 번 확인해주세요.\n아지트 양도가 완료될 경우 소유권이 이전됩니다.
	}
	
	/**
	 * 부활 승인
	 * @param code
	 * @param bless
	 */
	void resurrection(int code, boolean bless) {
		L1PcInstance scroll_pc = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
		pc.setTempID(0);
		if ((pc.getMapId() >= L1TownLocation.MAP_ERJABE_CROWN && pc.getMapId() <= L1TownLocation.MAP_ERJABE_FENCER || pc.getMapId() == L1TownLocation.MAP_ERJABE_LANCER) && !pc.getInventory().checkItemOne(L1ItemId.DEATH_PENALTY_SHIELD_ITEMS)) {
			pc.sendPackets(L1ServerMessage.sm8712);// 불멸의 가호(이벤트 가호) 류를 소지하고 있지 않아 부활할 수 없습니다.
			return;
		}
		if (scroll_pc != null && code == MSGCODE_YES) { // 부활 스크롤
			pc.send_effect('\346');
			pc.resurrect(bless ? pc.getMaxHp() : pc.getMaxHp() >> 1);
			pc.startMpRegenerationByItem64Second();
			pc.broadcastPacketWithMe(new S_Resurrection(pc, scroll_pc, 0), true);
			pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc), true);
			
			if (bless) {
				// 축복받은 부활 or 그레이트 리절렉션의 경우 하락된 경험치를 소량 보상한다.
				if (pc.getExpRes() == 1 && pc.isGres() && pc.isGresValid()) {
					DeathPenaltyTable.getInstance().recoveryExpSpell(pc);
					pc.setExpRes(0);
					pc.setGres(false);
				}
			}
		}
	}
	
	/**
	 * 변신
	 * @param param
	 */
	void poly(String param){
		if (param.startsWith(L1PolyMorph.MAPLE_STR)) {
			param = param.replace(L1PolyMorph.MAPLE_STR, StringUtil.EmptyString).trim();
		}
		boolean isRank = pc.getConfig().is_rank_poly();
		if (param.startsWith(L1PolyMorph.RANKING_STR) && !isRank) {
			return;
		}
		
		if (param.equalsIgnoreCase(L1PolyMorph.RANKING_CLASS_STR)) {
			if (!isRank) {
				return;
			}
			param = L1PolyMorph.RANKING_CLASS_ARRAY[pc.getType()][pc.getGender().toInt()];
		} else if (param.equalsIgnoreCase(L1PolyMorph.BASIC_CLASS_STR)) {
			param = L1PolyMorph.BASIC_CLASS_ARRAY[pc.getType()][pc.getGender().toInt()];
		}
		
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(param);
		if (poly != null || param.equals(StringUtil.EmptyString)) {
			if (param.equals(StringUtil.EmptyString)) {
				if (pc.getSpriteId() == 6034 || pc.getSpriteId() == 6035) {
				} else {
					pc.removeShapeChange();
				}
			} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm() || GameServerSetting.POLY_LEVEL_EVENT) {
			    L1PolyMorph.handleCommands(pc, param);
				pc.cancelAbsoluteBarrier();
				if (pc.isGm()) {
					//pc.sendPackets(new S_SystemMessage(String.format("\\aH변신이름  >> %s 변신번호  >> %d", param, pc.getSpriteId())), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(16), param, String.valueOf(pc.getSpriteId())), true);
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm181); // \f1 그러한 monster에게는 변신할 수 없습니다.
			}
		}
	}
	
	/**
	 * 결혼 응답
	 * @param code
	 * @throws Exception
	 */
	void marryConfirm(int code) throws Exception {
		L1PcInstance partner = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
		pc.setTempID(0);
		if (partner != null) {
			if (code == MSGCODE_NO) {// No
				partner.sendPackets(new S_ServerMessage(656, pc.getName()), true); // %0%s는 당신과의 결혼을 거절했습니다.
			} else if (code == MSGCODE_YES) { // Yes
				pc.setPartnerId(partner.getId());
				pc.save();
				pc.sendPackets(L1ServerMessage.sm790); // 모두의 축복 중(안)에서, 두 명의 결혼을 했습니다.
				pc.sendPackets(new S_ServerMessage(655, partner.getName()), true); // 축하합니다! %0과 결혼했습니다.

				partner.setPartnerId(pc.getId());
				partner.save();
				partner.sendPackets(L1ServerMessage.sm790); // 모두의 축복 중(안)에서, 두 명의 결혼을 했습니다.
				partner.sendPackets(new S_ServerMessage(655, pc.getName()), true); // 축하합니다! %0과 결혼했습니다.
			}
		}
	}
	
	/**
	 * 이혼 응답
	 * @param code
	 * @throws Exception
	 */
	void divorceConfirm(int code) throws Exception {
		if (code == MSGCODE_YES) { // Yes
			pc.getInventory().consumeItem(40901);
			pc.getInventory().consumeItem(40902);
			pc.getInventory().consumeItem(40903);
			pc.getInventory().consumeItem(40904);
			pc.getInventory().consumeItem(40905);
			pc.getInventory().consumeItem(40906);
			pc.getInventory().consumeItem(40907);
			pc.getInventory().consumeItem(40908);
			pc.setPartnerId(0);
			pc.save();
		}
	}
	
	/**
	 * 보스 지역 텔레포트
	 * @param code
	 */
	void bossTeleport(int code){
        if (code == MSGCODE_NO) {
        	pc.getConfig().setBossYN(0);
        } else if (code == MSGCODE_YES) {
        	if (pc.getNetConnection().isInterServer() || !pc.getMap().isEscapable()) {
        		pc.sendPackets(L1ServerMessage.sm1892); // 사용불가 지역
        		pc.getConfig().setBossYN(0);
        		return;
        	}
        	BossTemp boss = BossSpawnTable.getBossInfo(pc.getConfig().getBossYN());
        	pc.getConfig().setBossYN(0);
        	if (boss == null) {
        		pc.sendPackets(L1SystemMessage.WRITE_TIME_FAIL);
        		return;
        	}
        	L1Location loc = new L1Location(boss.spawnLoc[0], boss.spawnLoc[1], boss.spawnLoc[2]).randomLocation(0, 5, true);
    		pc.getTeleport().start(loc, pc.getMoveState().getHeading(), true);
        }
	}
	
	/**
	 * 드래곤 레이드 포탈 입장
	 * @param code
	 */
	void dragonPortalEnter(int code) {
		if (code == MSGCODE_YES) {
			if (pc.DragonPortalLoc[0] != 0) {
				Collection<L1PcInstance> templist = L1World.getInstance().getAllPlayers();
				L1PcInstance[] list = templist.toArray(new L1PcInstance[templist.size()]);
				int count = 0;
				for (L1PcInstance player : list) {
					if (player == null) {
						continue;
					}
					if (player.getMapId() == pc.DragonPortalLoc[2]) {
						count += 1;
					}
				}
				if (count >= 16) {
					pc.sendPackets(L1ServerMessage.sm1536);// 인원이 가득차서 더 이상 입장할 수 없습니다.
					return;
				}
				pc.getTeleport().start(pc.DragonPortalLoc[0], pc.DragonPortalLoc[1], (short) pc.DragonPortalLoc[2], 5, true);
			}
		}
		pc.DragonPortalLoc[0] = pc.DragonPortalLoc[1] = pc.DragonPortalLoc[2] = 0;
	}
	
	/**
	 * 할파스 레이드 포탈 입장
	 * @param code
	 */
	void halpasPortalEnter(int code) {
		if (code == MSGCODE_YES) {
			if (pc.DragonPortalLoc[0] != 0) {
				Collection<L1PcInstance> templist = L1World.getInstance().getAllPlayers();
				L1PcInstance[] list = templist.toArray(new L1PcInstance[templist.size()]);
				int count = 0;
				for (L1PcInstance player : list) {
					if (player == null) {
						continue;
					}
					if (player.getMapId() == pc.DragonPortalLoc[2]) {
						count += 1;
					}
				}
				if (count >= 16) {
					pc.sendPackets(L1ServerMessage.sm7620);//할파스의 은신처: 입장 불가
					return;
				}
				pc.getTeleport().start(pc.DragonPortalLoc[0], pc.DragonPortalLoc[1], (short) pc.DragonPortalLoc[2], 5, true);
			}
		}
		pc.DragonPortalLoc[0] = pc.DragonPortalLoc[1] = pc.DragonPortalLoc[2] = 0;
	}
	
	/**
	 * 펫 레이싱 입장
	 * @param code
	 */
	void racingGameEnter(int code) {
		switch (code) {
		case MSGCODE_NO: // no
			if (L1Racing.getInstance().contains(0, pc) && pc.getMapId() != 5143) {
				L1Racing.getInstance().remove(0, pc);
			} else if (L1HauntedHouse.getInstance().isMember(pc) && pc.getMapId() != 5140) {
				L1HauntedHouse.getInstance().removeMember(pc);
			} else {
				break;
			}
			// 천원 다시 돌려주기
			// pc.getInventory().storeItem(L1ItemId.ADENA, 1000); // 1000 아데나 지급
			break;
		case MSGCODE_YES: // Yes
			if (L1Racing.getInstance().contains(0, pc) && pc.getMapId() != 5143) { // 멤버라면
				if (L1Racing.getInstance().getGameStatus() == L1Racing.STATUS_NONE || L1Racing.getInstance().getGameStatus() == L1Racing.STATUS_READY) {
					int locx = 32767 + CommonUtil.random(2);
					int locy = 32848 + CommonUtil.random(2);
					L1SkillUse l1skilluse = new L1SkillUse(true);
					l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.LOGIN);
					l1skilluse = null;
					pc.getTeleport().start(locx, locy, (short) 5143, 5, true);
				}
			} else if (L1HauntedHouse.getInstance().isMember(pc) && pc.getMapId() != 5140) {
				if (L1HauntedHouse.getInstance().getHauntedHouseStatus() == L1HauntedHouse.STATUS_NONE
						|| L1HauntedHouse.getInstance().getHauntedHouseStatus() == L1HauntedHouse.STATUS_READY) {
					if (pc.isInParty()) {
						pc.getParty().leaveMember(pc);
					}
					L1SkillUse l1skilluse = new L1SkillUse(true);
					l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.LOGIN);
					l1skilluse = null;
					L1PolyMorph.doPoly(pc, 6284, 600, L1PolyMorph.MORPH_BY_LOGIN);
					pc.getTeleport().start(32722, 32830, (short) 5140, 2, true);
				}
			}
			break;
		}
	}
	
	/**
	 * 데스 매치 입장
	 * @param code
	 */
	void deathMatchEnter(int code) {
		switch (code) {
		case MSGCODE_NO: // no
			// 천원 다시 돌려주기
			L1DeathMatch.getInstance().removeMember(pc);
			// pc.getInventory().storeItem(L1ItemId.ADENA, 1000); // 1000 아데나 지급
			break;
		case MSGCODE_YES: // Yes
			if (pc.isInParty()) {// 파티중
				pc.getParty().leaveMember(pc);
			}
			L1DeathMatch.getInstance().addPlayer();
			pc.getTeleport().start(32658, 32899, (short) 5153, 2, true);
			break;
		}
	}
	
	/**
	 * LFC등록
	 * @param code
	 */
	void lfcRegist(int code){
		if (code == MSGCODE_NO) {
			LFCCreator.setInstStatus(pc, InstStatus.INST_USERSTATUS_NONE);
		} else if (code == MSGCODE_YES) {
			if (pc.getInstStatus() == InstStatus.INST_USERSTATUS_LFCREADY) {
				LFCCreator.setInstStatus(pc, InstStatus.INST_USERSTATUS_LFCINREADY);
			}
		}
	}
	
	/**
	 * 서버 초기화
	 * @param code
	 */
	void serverReset(int code){
		if (!pc.isGm() || code != MSGCODE_YES) {
			return;
		}
		GameServer.getInstance().serverReset();
	}
	
	/**
	 * 시간제 던전 시간 초기화
	 * @param code
	 */
	void dungeonTimerRest(int code) {
		if (code == MSGCODE_YES) {
			L1DungeonTimeChargedItem obj	= L1DungeonTimerLoader.getDungeonTimerItem(pc.resetTimerItemId);
			if (obj == null)
				return;
			if (obj.getTimerId() == 0) 
				return;			
			L1DungeonTimerUser timer = pc.getDungoenTimer().getTimers().get(obj.getTimerId());
			timer.reset();
			//pc.sendPackets(new S_SystemMessage(String.format("[%s] 던전 이용시간이 초기화 되었습니다.", timer.getInfo().getDescId())), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(17), timer.getInfo().getDescId()), true);
			pc.getInventory().consumeItem(pc.resetTimerItemId, 1);
		}
		pc.resetTimerItemId = 0;
	}
	
	/**
	 * 앱센터 인게임 로그인 승인
	 * @param code
	 */
	void webserverIngameLoginAuth(int code) {
		if (!pc.ingame_login_auth_delay) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("입력 가능한 시간이 초과 되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(77), true), true);
			return;
		}
		if (code == MSGCODE_YES) {// 승인
			pc.ingame_login_auth = true;
		}
		pc.ingame_login_auth_delay = false;
	}

	@Override
	public String getType() {
		return C_ATTR;
	}
}

