package l1j.server.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.charactertrade.CharacterTradeManager;
import l1j.server.RobotSystem.RobotAIThread;
import l1j.server.common.data.ChatType;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.common.data.ePLEDGE_JOIN_REQ_TYPE;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ServerExplainTable;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Unknown2;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.message.S_ChatMessageNoti;
import l1j.server.server.serverpackets.pledge.S_PledgeWatch;
import l1j.server.server.serverpackets.S_OutputRawString;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
//import manager.Manager;  // MANAGER DISABLED

public class UserCommands {
	private static Logger _log = Logger.getLogger(UserCommands.class.getName());

	private static UserCommands _instance;
	public static UserCommands getInstance() {
		if(_instance == null)_instance = new UserCommands();
		return _instance;
	}
	private UserCommands() {
	}

	public void handleCommands(L1PcInstance pc, String cmdLine) {
		if(pc == null)return;
		StringTokenizer token = new StringTokenizer(cmdLine);
		String cmd = StringUtil.EmptyString;
		cmd = token.hasMoreTokens() ? token.nextToken() : cmdLine;
		
		String param = StringUtil.EmptyString;
		while(token.hasMoreTokens())
			param = new StringBuilder(param).append(token.nextToken()).append(' ').toString();
		param = param.trim();

		try {
			switch (cmd) {
			case "도움말": case "help":													showHelp(pc);break;
			case "텔렉풀기": case "텔렉": case "telreset": case StringUtil.PeriodString:	telrek(pc);break;
			case "좌표복구": case "locreset":											locReset(pc, cmd);break;
			case "보안설정": case "security":											changeQuiz(pc, param, cmd);break;
			case "보안해제": case "보안인증": case "validatesecurity":					 validateQuiz(pc, param, cmd);break;
			case "암호변경": case "비번변경": case "changepassword":					 changePassword(pc, param, cmd);break;
			case "드랍멘트": case "멘트": case "chat":									 ment(pc, param, cmd);break;
			case "무인상점": case "shop":											    privateShop(pc, cmd);break;
			case "캐릭명변경": case "케릭명변경": case "이름변경": case "changename":	  changeName(pc, param, cmd);break;
			case "고정신청": case "phone":												phone(pc, param, cmd);break;
			case "혈맹파티": case "bloodparty":											bloodParty(pc);break;
			case "혈마크": case "pledgemark": 										    clanMark(pc, param, cmd);break;
			case "인첸트연출": case "인챈트연출": case "enchant": 					 	 enchantGfx(pc, param, cmd);break;
			case "그랑카인": case "grankain":											grangKain(pc);break;
			case "수배": case "wanted":
				if (Config.ETC.WANTED_ACTIVE) {
					hunt(pc, param, cmd);
				} else {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("명령어 " + cmd + " 는 존재하지 않습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1159) + cmd  + S_SystemMessage.getRefText(1160), true), true);
				}
				break;
			case "수배확인":case "수배체크": case "wantedcheck":
				if (Config.ETC.WANTED_ACTIVE) {
					huntCheck(pc);
				} else {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("명령어 " + cmd + " 는 존재하지 않습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1159) + cmd  + S_SystemMessage.getRefText(1160), true), true);
				}
				break;
			case "무인가입": case "pledgeautojoin":
				if (pc.getClan() != null && pc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_KING) {
					autoPledgeJoin(pc);
				} else {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("명령어 " + cmd + " 는 존재하지 않습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1159) + cmd  + S_SystemMessage.getRefText(1160), true), true);
				}
				break;
			case "라이트":case "맵핵": case "light":				
				maphack(pc, param, cmd);break;			
			case "후원안내":case "sponsorinfo":
				if (!Config.SERVER.PROFIT_SERVER_ACTIVE) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("명령어 " + cmd + " 는 존재하지 않습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1159) + cmd  + S_SystemMessage.getRefText(1160), true), true);
				}
				ServerExplainTable.getInstance().explain(pc, 6);
				break;
			case "구슬": case "charactertrade":
			CharacterTradeManager.getInstance().release();
			CharacterTradeManager.getInstance().load();
			CharacterTradeManager.commands(pc, param);break;
			case "warp": 
				warp(pc, param, cmd);
				break;
			default:
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("명령어 " + cmd + " 는 존재하지 않습니다. "), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1159) + cmd  + S_SystemMessage.getRefText(1160), true), true);
				break;
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	private void showHelp(L1PcInstance pc) {	 
		if (!Config.SERVER.PROFIT_SERVER_ACTIVE) {
			ServerExplainTable.getInstance().explain(pc, pc.isCrown() ? 3 : 4);
		} else {
			ServerExplainTable.getInstance().explain(pc, pc.isCrown() ? 1 : 2);
		} 

	}
	
	private void enchantGfx(L1PcInstance pc, String param, String cmdName) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String onoff = tok.nextToken();
			if (onoff.equals("켬") || onoff.equals("on")) {
					pc.getConfig()._enchantGfx = true;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("인첸트 연출 on"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(396), true), true);
			} else if (onoff.equals("끔") || onoff.equals("off")) {
				pc.getConfig()._enchantGfx = false;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("인첸트 연출 off"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(397), true), true);
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(".인첸트연출 [켬 or 끔]"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(447), true), true);
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".인첸트연출 [켬 or 끔]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(447), true), true);
		}
	}

	private void ment(L1PcInstance pc, String param, String cmdName) {
		if (param.equalsIgnoreCase("끔") || param.equalsIgnoreCase("off")) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("아이템 획득 멘트 - OFF -"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1162), true), true);
			pc.getConfig().RootMent = false;
		} else if (param.equalsIgnoreCase("켬") || param.equalsIgnoreCase("on")) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("아이템 획득 멘트 - ON -"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1163), true), true);
			pc.getConfig().RootMent = true;
		} else {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".드랍멘트 [켬/끔]중 입력 (아이템 획득 멘트 설정)"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(447), true), true);
		}
	}
	
	private void grangKain(L1PcInstance pc) {
		if (Config.FATIGUE.FATIGUE_ACTIVE) {
			pc.sendPackets(new S_SystemMessage(pc.getFatigue().toString()), true);
		} else {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("그랑카인 시스템이 작동중이지 않습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1165), true), true);
		}
	}
	
	private void autoPledgeJoin(L1PcInstance pc) {
		try {
			// 실패조건
			if (pc.getClanid() == 0 || pc.getClanid() == Config.PLEDGE.BEGINNER_PLEDGE_ID) {// 신규혈 아이디
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("혈맹창설 상태가 아닙니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1166), true), true);
				return;	
			}
			if (pc.isDead() || pc.isFishing() || pc.isPrivateShop() || pc.isPinkName() || pc.isStun() || pc.isParalyzed() || pc.isSleeped()) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("현재상태에선 사용할 수 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1167), true), true);
				return;	
			}
			if (pc.getClan().getJoinType() != ePLEDGE_JOIN_REQ_TYPE.ePLEDGE_JOIN_REQ_TYPE_IMMEDIATLY) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("즉시가입 상태에서만 가능합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1168), true), true);
				return;	
			}
			
			// 기란 여관 앞
			if (pc.getX() >= 33426 && pc.getX() <= 33435 && pc.getY() >= 32795 && pc.getY() <= 32802 && pc.getMapId() == 4) {
				for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
					if (target.getId() != pc.getId() 
							&& target.getAccountName().toLowerCase().equals(pc.getAccountName().toLowerCase()) 
							&& target.isAutoClanjoin()) {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("이미 당신의 보조 캐릭터가 무인가입 상태입니다."), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1169), true), true);
						return;
					}
				}
				
				pc.getMoveState().setHeading(4);
				pc.broadcastPacket(new S_ChangeHeading(pc), true);
				
				pc.setAutoClanjoin(true);
				L1PolyMorph.doPolyAutoClanjoin(pc);
				//Manager.getInstance().LogLogOutAppend(pc.getName(), pc.getNetConnection().getHostname());  // MANAGER DISABLED
				GameClient client = pc.getNetConnection();
				pc.setNetConnection(null);
				try { 
					pc.save();
					pc.saveInventory();
				} catch(Exception e) {                    		
				}
				client.setActiveChar(null);
				client.setLoginAvailable();
				client.setCharReStart(true);
				client.sendPacket(S_Unknown2.RETART); // 리스버튼을 위한 구조변경 // Episode U
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("기란마을 여관 앞 공간에서만 사용할 수 있습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1170), true), true);
			}
		} catch (Exception e) {
			//System.out.println(String.format("'%s' 무인가입 처리 예외 발생", pc.getName()));
			System.out.println(String.format("'%s' unregistered registration processing exception occurred", pc.getName()));
		}		
	}
	
	private void phone(L1PcInstance pc, String param, String cmdName) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime() + 10 > curtime) {
				long sec = (pc.getQuizTime() + 10) - curtime;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(sec + "초 후에 사용할 수 있습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(100) + sec + " " + S_SystemMessage.getRefText(1306) , true), true);
				return;
			}
			StringTokenizer tok = new StringTokenizer(param);
			String phone = tok.nextToken();
			phone = phone.replaceAll(StringUtil.MinusString, StringUtil.EmptyString);
			if (phone.length() < 10) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("없는 번호입니다. 다시 입력해주세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1171), true), true);
				return;
			}
			if (phone.length() > 15) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("잘못된 번호입니다. 다시 입력해주세요.('-'는 입력불가)"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1172), true), true);
				return;
			}
			if (isDisitAlpha(phone) == false) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("숫자로만 입력하세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1173), true), true);
				return;
			}
			if (pc.getAccount().getPhone() != null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("이미 전화번호가 설정되어 있습니다.\n번호 변경시 메티스에게 편지로 연락처를 보내세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1174), true), true);
				return;
			}
			pc.getAccount().setPhone(phone);
			pc.getAccount().updatePhone();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(phone + " 설정 완료. 초기화 시 카카오톡 발송됩니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(phone  + S_SystemMessage.getRefText(1175), true), true);
			pc.getAC().addAc(-1);
			pc.addMaxHp(50);
			pc.sendPackets(new S_PacketBox(pc, S_PacketBox.ICON_SECURITY_SERVICES), true);
			pc.setQuizTime(curtime);
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".고정신청 연락처 형식으로 입력.(초기화 할때만 카카오톡 전송)"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1176), true), true);
		}
	}
	
	private void maphack(L1PcInstance pc, String param, String cmdName) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String onoff = tok.nextToken();
			if (onoff.equals("켬") || onoff.equals("on")) {
					pc.sendPackets(new S_Ability(3, true), true);
			} else if (onoff.equals("끔") || onoff.equals("off")) {
				pc.sendPackets(new S_Ability(3, false), true);
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".맵핵 [켬 or 끔]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(447), true), true);
		}
	}

	private void locReset(L1PcInstance pc, String cmdName) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime2() + 20 > curtime) {
				long time = (pc.getQuizTime2() + 20) - curtime;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(time + "초 후 사용할 수 있습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(560) + time + " " + S_SystemMessage.getRefText(1306), true), true);
				return;
			}
			Connection con = null;
			PreparedStatement pstm = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("UPDATE characters SET LocX=33432, LocY=32807, MapID=4 WHERE account_name=? AND MapID NOT IN (99,997,5166,39,34,701,2000)"); 
				// 운영자의방,감옥,배틀존대기실	 제외
				pstm.setString(1, pc.getAccountName());
				pstm.execute();
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("계정내 모든 캐릭터의 좌표가 기란마을로 이동되었습니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1178), true), true);

				pc.setQuizTime(curtime);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(pstm, con);
			}
		} catch (Exception e) {
		}
	}
	
	private void telrek(L1PcInstance pc) {
		long curtime = System.currentTimeMillis() / 1000;
		long last_local_tel_time = pc.getLastLocalTellTime() / 1000;
		if (last_local_tel_time + 10 > curtime) {
			long time = (last_local_tel_time + 10) - curtime;
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(time + "초 후 사용할 수 있습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(560) + time + " " + S_SystemMessage.getRefText(1306), true), true);
			return;
		}
		try {
			if (pc.getMapId() == 781 && pc.getLocation().getX() <= 32998 && pc.getLocation().getX() >= 32988 && pc.getLocation().getY() <= 32758 && pc.getLocation().getY() >= 32736) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("사용할 수 없는 장소입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1179), true), true);
				return;
			}
			if (pc.getMap().getBaseMapId() == 1936 || pc.getMap().getBaseMapId() == 2936 || pc.getMap().getBaseMapId() == 3000 || pc.getMap().getBaseMapId() == 3050 || (pc.getMap().getBaseMapId() >= 731 && pc.getMap().getBaseMapId() <= 736) || pc.getMap().getBaseMapId() == 751) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("사용할 수 없는 장소입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1179), true), true);
				return;
			}
			if (pc.isPinkName() || pc.isDead() || pc.isParalyzed() || pc.isSleeped() || pc.getMapId() == 800 || pc.getMapId() == 5302 || pc.getMapId() == 5153 || pc.getMapId() == 5490) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("사용할 수 없는 상태입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1180), true), true);
				return;
			}
			pc.getTeleport().start(pc.getX(), pc.getY(), pc.getMapId(), pc.getMoveState().getHeading(), false);
			pc.updateLastLocalTellTime();
		} catch (Exception exception35) {
		}
	}
	
	private void clanMark(L1PcInstance pc, String param, String cmdName) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String onoff = st.nextToken();
			
			if (pc.getNetConnection() != null && pc.getNetConnection().getInter() != null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("해당 맵에서는 사용이 불가능 합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(317), true), true);
				return;
			}
			
			if (onoff.equalsIgnoreCase("켬") || onoff.equalsIgnoreCase("on")) {
				pc.sendPackets(new S_PledgeWatch(pc, 2, true), true);
				pc.sendPackets(new S_PledgeWatch(pc, 0, true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("혈마크 표기를 시작 합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(318), true), true);
			} else if (onoff.equalsIgnoreCase("끔") || onoff.equalsIgnoreCase("off")) {
				pc.sendPackets(new S_PledgeWatch(pc, 2, false), true);
				pc.sendPackets(new S_PledgeWatch(pc, 1, false), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("혈마크 표기를 종료 합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(319), true), true);
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(".혈마크 [켬 / 끔]"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(447), true), true);
				return;
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".혈마크 [켬 / 끔]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(447), true), true);
		}
	}

	public void bloodParty(L1PcInstance pc) {
		if (pc.isDead()) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("죽은 상태에선 사용할 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1182), true), true);
			return;
		}
		if (pc.getClanid() != 0 && eBloodPledgeRankType.isAuthRankAtKnight(pc.getBloodPledgeRank())) {
			for (L1PcInstance member : pc.getClan().getOnlineClanMember()) {
				if (member.isPrivateShop() || member.isAutoClanjoin() || member.isInParty() || member.getName() == pc.getName()) {// 클랜이 같지않다면[X], 이미파티중이면[X], 상점중[X]
					continue;
				}
				member.setPartyID(pc.getId());// 파티아이디 설정
				member.sendPackets(new S_MessageYN(954, pc.getName()), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(member.getName() + " 님에게 파티를 신청했습니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(member.getName()  + S_SystemMessage.getRefText(1183), true), true);
			}
		} else {// 클랜이 없거나 군주 또는 수호기사 [X]
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("혈맹이 있으면서 군주, 부군주, 수호기사라면 사용가능."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1184), true), true);
		}
	}

	private void hunt(L1PcInstance pc, String param, String cmdName) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String char_name = st.nextToken();
			int price = Integer.parseInt(st.nextToken());

			L1PcInstance target = null;
			target = L1World.getInstance().getPlayer(char_name);
			if (target != null) {
				if (target.isGm()) {
					return;
				}
				if (target.getConfig().getHuntCount() == 1) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 수배 되어있습니다"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1185), true), true);
					return;
				}
				if (price != Config.ETC.WANTED_COST) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("단위 금액은 "+ Config.ETC.WANTED_COST +" 만 아데나입니다"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1186) + Config.ETC.WANTED_COST  + S_SystemMessage.getRefText(1187), true), true);
					return;
				}
				if (price > Config.ETC.WANTED_COST) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("최대 금액은 "+ Config.ETC.WANTED_COST +"만 아데나입니다"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1190) + Config.ETC.WANTED_COST  + S_SystemMessage.getRefText(1187), true), true);
					return;
				}
				if (!(pc.getInventory().checkItem(L1ItemId.ADENA, price))) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("아데나가 부족합니다"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1192), true), true);
					return;
				}
				if (target.getConfig().getHuntPrice() > Config.ETC.WANTED_COST) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("수배최대금액 "+ Config.ETC.WANTED_COST +"만 입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1193) + Config.ETC.WANTED_COST  + S_SystemMessage.getRefText(1187), true), true);
					return;
				}
				target.getConfig().setHuntCount(1);
				target.getConfig().setHuntPrice(target.getConfig().getHuntPrice() + price);
				target.save();
//AUTO SRM: 				L1World.getInstance().broadcastServerMessage("\\aD[" + target.getName() + "]의 목에 현상금이 걸렸습니다.", true); // CHECKED OK
				L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefTextNS(1194) + target.getName()  + S_SystemMessage.getRefText(1237), true);
//AUTO SRM: 				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aD[ 수배자 ]:  [" + target.getName() + "  ]"), true); // CHECKED OK
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(1195) + "[" + target.getName()  + "]", true), true);
				
				if (price >= Config.ETC.WANTED_COST) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("수배로 인한 근/원거리 대미지+3, 공성+3, SP+3 적용"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1196), true), true);
					pc.getConfig().initBeWanted();
					int[] beWanted = { 3, 3, 3, 3, 3, 3 };
					pc.getConfig().setBeWanted(beWanted);
					pc.getConfig().addBeWanted();
				}
				
				pc.getInventory().consumeItem(L1ItemId.ADENA, price);
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("접속중이지 않습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1197), true), true);
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".수배 [캐릭터명] [금액]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(395), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("====== 근/원거리 대미지+3, 공성+3, 리덕+3, SP+3 적용 ====="), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1199), true), true);
		}
	}
	
	private void huntCheck(L1PcInstance pc) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime() + 10 > curtime) {
				long sec = (pc.getQuizTime() + 10) - curtime;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(sec + "초 후에 사용할 수 있습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(100) + sec + " " + S_SystemMessage.getRefText(1306) + ".", true), true);
				return;
			}
			for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
				if (target.getConfig().getHuntCount() != 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("\\aD[" + target.getName() + "]님이 수배중입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage("\\aD[" + target.getName()  + S_SystemMessage.getRefText(1200), true), true);
				}
			}		
			pc.setQuizTime(curtime);
		} catch (Exception e) {
		}
	}

	private static boolean isDisitAlpha(String str) {
		boolean check = true;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)) // 숫자가 아니라면
					&& !Character.isUpperCase(str.charAt(i)) // 대문자가 아니라면
					&& !Character.isLowerCase(str.charAt(i))) { // 소문자가 아니라면
				check = false;
				break;
			}
		}
		return check;
	}

	private boolean isValidQuiz(L1PcInstance pc, String quiz) {
		if (pc.getAccount().getQuiz() == null || pc.getAccount().getQuiz().equals(quiz)) {
			return true;
		}
		return false;
	}

	private void changeQuiz(L1PcInstance pc, String param, String cmdName) {
		if (pc.getLastQuizChangeTime() + 600 * 1000 > System.currentTimeMillis()) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("퀴즈를 변경하신지 10분이 지나지 않았습니다. 잠시후 다시 변경하세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1201), true), true);
			return;
		}
		boolean firstQuiz = false;
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String oldquiz = StringUtil.EmptyString;
			if (isValidQuiz(pc, oldquiz)) {
				firstQuiz = true;
			} else {
				oldquiz = tok.nextToken();
			}
			String newquiz = tok.nextToken();

			if (newquiz.length() < 4) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("4자 ~ 12자 사이의 영어나 숫자로 입력하세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1202), true), true);
				return;
			}
			if (newquiz.length() > 12) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("4자 ~ 12자 사이의 영어나 숫자로 입력하세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1202), true), true);
				return;
			}
			if (isDisitAlpha(newquiz) == false) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("숫자와 영어로만 입력하세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1203), true), true);
				return;
			}
			pc.getAccount().setQuiz(newquiz);
			chkquiz(pc, pc.getAccount().getQuiz());
		} catch (Exception e) {
			if (firstQuiz) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(".보안설정 원하는보안암호 <-- 형식으로 입력하세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1204), true), true);
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("당신의 계정은 이미 보안이 설정되어 있습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1205), true), true);
			}
		}
	}

	private void chkquiz(L1PcInstance pc, String newQuiz) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET quiz = ? WHERE login=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, newQuiz);
			pstm.setString(2, pc.getAccountName());
			pstm.execute();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aD보안 설정이 정상적으로 완료되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1206), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aD보안 암호: " + newQuiz + " (분실시 계정의 암호변경 불가능)"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1207) + newQuiz  + S_SystemMessage.getRefText(1208), true), true);
			pc.updateLastQuizChangeTime();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	private void validateQuiz(L1PcInstance pc, String param, String cmdName) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			if (isValidQuiz(pc, StringUtil.EmptyString)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("먼저 보안설정이 필요합니다. 명령어 [.보안설정]"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1209), true), true);
				return;
			}
			String quiz = tok.nextToken();
			if (!isValidQuiz(pc, quiz)) {
				Accountsquiz(pc, quiz);
				return;
			}
			pc.setQuizValidated();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("보안이 잠시 해제되었습니다. 잠시 암호변경이 가능합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1210), true), true);
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".보안해제 보안설정된암호 <-- 형식으로 입력."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1211), true), true);
		}
	}

	private void Accountsquiz(L1PcInstance pc, String quiz) {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT quiz FROM accounts WHERE login='" + pc.getAccountName() + "'");
			rs = statement.executeQuery();
			String oldQuiz = StringUtil.EmptyString;
			if (rs.next()) {
				oldQuiz = rs.getString(1);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("보안설정된 암호와 일치하지 않습니다. *힌트: " + oldQuiz.length() + " 글자."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1212) + oldQuiz.length()  + S_SystemMessage.getRefText(1213), true), true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, statement, con);
		}
	}

	private void changePassword(L1PcInstance pc, String param, String cmdName) {
		try {
			if (pc.getLastPasswordChangeTime() + 600 * 1000 > System.currentTimeMillis()) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("암호를 변경하신지 10분이 지나지 않았습니다. 잠시후 다시 변경하세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1214), true), true);
				return;
			}
			StringTokenizer tok = new StringTokenizer(param);
			String newpasswd = tok.nextToken();
			if (isValidQuiz(pc, StringUtil.EmptyString)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("보안설정 후에 암호변경이 가능합니다. 명령어 [.보안설정]"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1215), true), true);
				return;
			}
			if (!pc.isQuizValidated()) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("보안해제 후에 암호변경이 가능합니다. 명령어 [.보안해제]"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1216), true), true);
				return;
			}
			if (newpasswd.length() < 6) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("6자 ~ 16자 사이의 영어나 숫자로 입력하세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1217), true), true);
				return;
			}
			if (newpasswd.length() > 16) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("6자 ~ 16자 사이의 영어나 숫자로 입력하세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1217), true), true);
				return;
			}
			if (isDisitAlpha(newpasswd) == false) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("영어와 숫자로만 입력하세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1203), true), true);
				return;
			}
			toChangePassword(pc, newpasswd);
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".암호변경 변경할암호 <-- 형식으로 입력하세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1219), true), true);
		}
	}

	private void toChangePassword(L1PcInstance pc, String passwd) {
		if (StringUtil.isNullOrEmpty(pc.getAccountName())) {
			return;
		}
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET password=? WHERE login LIKE '" + pc.getAccountName() + "'");
			pstm.setString(1, passwd);
			if (pstm.executeUpdate() > 0) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("당신의 계정 암호가 (" + passwd + ") 로 변경되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1220) + passwd  + S_SystemMessage.getRefText(1221), true), true);
				pc.updateLastPasswordChangeTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	// 패스워드 맞는지 여부 리턴
	public static boolean isPasswordTrue(String Password, String oldPassword) {
		String _rtnPwd = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT password(?) AS pwd");
			pstm.setString(1, oldPassword);
			rs = pstm.executeQuery();
			if (rs.next()) {
				_rtnPwd = rs.getString("pwd");
			}
			if (_rtnPwd.equals(Password)) {// 동일하다면
				result = true;
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}

	private void changeName(L1PcInstance pc, String name, String cmdName) {
		if (BadNamesList.getInstance().isBadName(name)) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("생성 금지된 캐릭명입니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1222), true), true);
			return;
		}
		if (CharacterTable.doesCharNameExist(name)) { // 케릭터
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("동일한 이름이 존재 합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1223), true), true);
			return;
		}
		if (pc.getClanid() != 0) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("혈맹을 잠시 탈퇴한 후 변경할 수 있습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1224), true), true);
			return;
		}
		if (pc.isCrown()) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("군주는 운영자와 상담 후에만 변경할 수 있습니다"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1225), true), true);
			return;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("채금 상태에는 변경할 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1226), true), true);
			return;
		}
		try {
			if (pc.getLevel() >= 60) {
				for (int i = 0; i < name.length(); i++) {
					if (name.charAt(i) == 'ㄱ' || name.charAt(i) == 'ㄲ' || name.charAt(i) == 'ㄴ' || name.charAt(i) == 'ㄷ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㄸ' || name.charAt(i) == 'ㄹ' || name.charAt(i) == 'ㅁ' || name.charAt(i) == 'ㅂ' || // 한문자(char)단위로 비교
							name.charAt(i) == 'ㅃ' || name.charAt(i) == 'ㅅ' || name.charAt(i) == 'ㅆ' || name.charAt(i) == 'ㅇ' || // 한문자(char)단위로 비교
							name.charAt(i) == 'ㅈ' || name.charAt(i) == 'ㅉ' || name.charAt(i) == 'ㅊ' || name.charAt(i) == 'ㅋ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅌ' || name.charAt(i) == 'ㅍ' || name.charAt(i) == 'ㅎ' || name.charAt(i) == 'ㅛ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅕ' || name.charAt(i) == 'ㅑ' || name.charAt(i) == 'ㅐ' || name.charAt(i) == 'ㅔ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅗ' || name.charAt(i) == 'ㅓ' || name.charAt(i) == 'ㅏ' || name.charAt(i) == 'ㅣ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅠ' || name.charAt(i) == 'ㅜ' || name.charAt(i) == 'ㅡ' || name.charAt(i) == 'ㅒ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅖ' || name.charAt(i) == 'ㅢ' || name.charAt(i) == 'ㅟ' || name.charAt(i) == 'ㅝ' || // 한문자(char)단위로 비교.
							name.charAt(i) == 'ㅞ' || name.charAt(i) == 'ㅙ' || name.charAt(i) == 'ㅚ' || name.charAt(i) == 'ㅘ' || // 한문자(char)단위로 비교.
							name.charAt(i) == '씹' || name.charAt(i) == '좃' || name.charAt(i) == '좆' || name.charAt(i) == 'ㅤ') {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("캐릭명이 올바르지 않습니다."), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1227), true), true);
						return;
					}
				}
				for (int i = 0; i < name.length(); i++) {
					if (!Character.isLetterOrDigit(name.charAt(i))) {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("캐릭명이 올바르지 않습니다."), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1227), true), true);
						return;
					}
				}
				int numOfNameBytes = 0;
				numOfNameBytes = name.getBytes(CharsetUtil.EUC_KR_STR).length;
				if (numOfNameBytes == 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(".이름변경 바꿀캐릭명 <--형식으로 입력"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1228), true), true);
					return;
				}
				if (numOfNameBytes < 2 || numOfNameBytes > 12) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("한글 1자 ~ 6자 사이로 입력하세요."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1229), true), true);
					return;
				}

				if (BadNamesList.getInstance().isBadName(name)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("생성 금지된 캐릭명입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1222), true), true);
					return;
				}
				if (RobotAIThread.doesCharNameExist(name)) { // 로봇 
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("동일한 이름이 존재 합니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1223), true), true);
					return;
				}

				if (pc.getInventory().checkItem(408990, 1)) { // 인벤 아이템 체크 
					Connection con = null;
					PreparedStatement pstm = null;
					try {
						con = L1DatabaseFactory.getInstance().getConnection();
						pstm = con.prepareStatement("UPDATE characters SET char_name =? WHERE char_name = ?");
						pstm.setString(1, name); // 변경 
						pstm.setString(2, pc.getName());
						pstm.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						SQLUtil.close(pstm, con);
					}

					pc.save(); // 저장
					/****** 여긴 파일로 캐릭명변경 내용 작성 부분 *******/

					/****** LogDB 라는 폴더를 미리 생성 해두세요 *******/
					Calendar rightNow = Calendar.getInstance();
					int year = rightNow.get(Calendar.YEAR);
					int month = rightNow.get(Calendar.MONTH) + 1;
					int date = rightNow.get(Calendar.DATE);
					int hour = rightNow.get(Calendar.HOUR);
					int min = rightNow.get(Calendar.MINUTE);
					String stryyyy = StringUtil.EmptyString;
					String strmmmm = StringUtil.EmptyString;
					String strDate = StringUtil.EmptyString;
					String strhour = StringUtil.EmptyString;
					String strmin = StringUtil.EmptyString;
					stryyyy = Integer.toString(year);
					strmmmm = Integer.toString(month);
					strDate = Integer.toString(date);
					strhour = Integer.toString(hour);
					strmin = Integer.toString(min);
					String str = StringUtil.EmptyString;
					str = new String("[" + stryyyy + StringUtil.MinusString + strmmmm + StringUtil.MinusString + strDate + StringUtil.EmptyOneString + strhour + StringUtil.ColonString + strmin + "]  " + pc.getName() + "  --->  " + name);
					//StringBuffer FileName = new StringBuffer("LogDB/캐릭명변경.txt");
					StringBuffer FileName = new StringBuffer("LogDB/change_character_name.txt");
					PrintWriter out = null;
					try {
						out = new PrintWriter(new FileWriter(FileName.toString(), true));
						out.println(str);
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					str = StringUtil.EmptyString;// 초기화
					pc.getInventory().consumeItem(408990, 1); // 주문서 삭제 
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("재접속하시면 새로운 이름으로 변경됩니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1230), true), true);
					buddys(pc); // 친구 삭제
					deleteLetter(pc); // deleteLetter
					Thread.sleep(500);
					pc.sendPackets(S_Disconnect.DISCONNECT);
				} else {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이름 변경 주문서가 부족합니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1231), true), true);
				}
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("60레벨 이상만 가능합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1232), true), true);
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".이름변경 바꿀캐릭명 으로 입력해 주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(157), true), true);
		}
	}

	private void buddys(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_buddys WHERE buddy_name=?");
			pstm.setString(1, pc.getName());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	private void deleteLetter(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM letter WHERE receiver=?");
			pstm.setString(1, pc.getName());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	private void privateShop(L1PcInstance pc, String cmdName) {
		try {
			if (!pc.isPrivateShop()) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("알림:개인상점 상태에서 사용이 가능합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1234), true), true);
				return;
			}
			if (pc.getMapId() != 800) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("개인상점은 시장에서만  열수 있습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1235), true), true);
				return;
			}
			for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
				if (target.getId() != pc.getId() && target.getAccountName().toLowerCase().equals(pc.getAccountName().toLowerCase()) && target.isPrivateShop()) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("경고:이미 당신의 보조 캐릭터가 무인상점 상태입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1236), true), true);
					return;
				}
			}
			//Manager.getInstance().LogLogOutAppend(pc.getName(), pc.getNetConnection().getHostname());  // MANAGER DISABLED
			GameClient client = pc.getNetConnection();
			pc.setNetConnection(null);
			try {
				pc.save();
				pc.saveInventory();
			} catch (Exception e) {
			}
			client.setActiveChar(null);
			client.setLoginAvailable();
			client.setCharReStart(true);
			client.sendPacket(new S_Unknown2(1)); // 리스버튼을 위한 구조변경 // Episode U
			//client.close(); // 리마스터 무인상점 접속끓어지게
		} catch (Exception e) {
		}
	}

	private static final S_SystemMessage NoWarpArea = new S_SystemMessage(
			"You cannot use the command WARP in this area.", true);
	private static final S_SystemMessage NoWarpState = new S_SystemMessage(
			"You cannot use the command WARP in your current state.", true);
	private static final S_SystemMessage NoWarp = new S_SystemMessage(
			"The WARP command is disabled.", true);
	private static final S_SystemMessage WarpLimit = new S_SystemMessage(
			".warp 1-7 only.", true);
	private static final S_SystemMessage WarpHelp = new S_SystemMessage(
			//".warp 1-Pandora, 2-SKT, 3-Giran, 4-Werldern, 5-Oren, 6-Orc Town, 7-Silent Cavern, 8-Gludio, 9-Silveria, 10-Behimous", true);	
			".warp 1-Pandora, 2-SKT, 3-Giran, 4-Werldern, 5-Oren, 6-Orc Town, 7-Heine, 8-Gludio, 9-Aden, 10-Woodbeck", true);	

	
	
	
	
	private void warp(L1PcInstance player, String param, String cmdName) {
		/*if (param.equalsIgnoreCase("끔") || param.equalsIgnoreCase("off")) {
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1162), true), true);
			pc.getConfig().RootMent = false;
		} else if (param.equalsIgnoreCase("켬") || param.equalsIgnoreCase("on")) {
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1163), true), true);
			pc.getConfig().RootMent = true;
		} else {
			pc.sendPackets(new S_SystemMessage(cmdName + S_SystemMessage.getRefText(447), true), true);
		}*/

		//pc.sendPackets(new S_SystemMessage("WARP command is not activated.", true), true);	


		if (!Config.ALT.WARP) {
			player.sendPackets(NoWarp, true);
			return;
		}

		if (!player.getLocation().getMap().isEscapable()) {
			player.sendPackets(NoWarpArea, true);
			return;
		}

		if (player.isPrivateShop() 
				|| player.getSkill().hasSkillEffect(L1SkillId.EARTH_BIND) 
				|| player.getSkill().hasSkillEffect(L1SkillId.SHOCK_STUN)
				//|| player.getSkill().hasSkillEffect(L1SkillId.MASS_SHOCK_STUN) 
				|| player.getSkill().hasSkillEffect(L1SkillId.BONE_BREAK) 
				|| player.getSkill().hasSkillEffect(L1SkillId.CONFUSION) 
				|| player.isParalyzed() || player.isPinkName()
				|| player.isSleeped() || player.isDead()
				|| player.getMapId() == 99) {
			player.sendPackets(NoWarpState, true);
			return;
		}

		try {
			StringTokenizer st = new StringTokenizer(param);
			int i = Integer.parseInt(st.nextToken());

			if (i >= 1 && i <= 10) {
				if (System.currentTimeMillis() - player.getLastWarp() < 3000) {
					return;
				} else {
					player.setLastWarp(System.currentTimeMillis());
				}
				
				if (player.isPrivateShop() 
						|| player.getSkill().hasSkillEffect(L1SkillId.EARTH_BIND) 
						|| player.getSkill().hasSkillEffect(L1SkillId.SHOCK_STUN)
						//|| player.getSkill().hasSkillEffect(L1SkillId.MASS_SHOCK_STUN) 
						|| player.getSkill().hasSkillEffect(L1SkillId.BONE_BREAK) 
						|| player.getSkill().hasSkillEffect(L1SkillId.CONFUSION) 
						|| player.isParalyzed() || player.isPinkName()
						|| player.isSleeped() || player.isDead()
						|| player.getMapId() == 99) {
					player.sendPackets(NoWarpState, true);
					return;
				}
				WarpDelay warpDelay = null;
				switch (i) {
				case 1: // Pandora
					warpDelay = new WarpDelay(player, 32640, 32951, (short) 0, 5, true);
					break;
				case 2: // SKT
					warpDelay = new WarpDelay(player, 33073, 33391, (short) 4, 5, true);
					break;
				case 3: // Giran
					warpDelay = new WarpDelay(player, 33437, 32799, (short) 4, 5, true);
					break;
				case 4: // Weldern
					warpDelay = new WarpDelay(player, 33700, 32500, (short) 4, 5, true);
					break;
				case 5: // Oren
					warpDelay = new WarpDelay(player, 34059, 32279, (short) 4, 5, true);
					break;
				case 6: // Orc Town
					warpDelay = new WarpDelay(player, 32741, 32454, (short) 4, 5, true);
					break;
				case 7: // Heine // Silent Cave
					//warpDelay = new WarpDelay(player, 32857, 32898, (short) 304, 5, true);
					warpDelay = new WarpDelay(player, 33609, 33257, (short) 4, 5, true);
					break;
				case 8: // Gludio
					warpDelay = new WarpDelay(player, 32607, 32791, (short) 4, 5, true);
					break;
				case 9: // Aden // Silveria
					//warpDelay = new WarpDelay(player, 32841, 32856, (short) 1000, 5, true);
					warpDelay = new WarpDelay(player, 33934, 33353, (short) 4, 5, true);
					break;
				case 10: // Woodbeck // Behimous
					//warpDelay = new WarpDelay(player, 32779, 32887, (short) 1001, 5, true);
					warpDelay = new WarpDelay(player, 32632, 33188, (short) 4, 5, true);
				}
				if (warpDelay != null) {
					GeneralThreadPool.getInstance().schedule(warpDelay, 3000);
				}
			} else {
				player.sendPackets(WarpLimit, true);
			}
		} catch (Exception exception) {
			player.sendPackets(WarpHelp, true);
		}
	}

	private class WarpDelay implements Runnable {

		L1PcInstance player;
		int x;
		int y;
		short mapid;
		int heading;
		boolean effectable;
		
		public WarpDelay(L1PcInstance player, int x, int y, short mapid, int heading, boolean effectable) {
			this.player = player;
			this.x = x;
			this.y = y;
			this.mapid = mapid;
			this.heading = heading;
			this.effectable = effectable;
		}
		@Override
		public void run() {
			try {
				//L1Teleport.teleport(player, x, y, mapid, heading, effectable);
				player.getTeleport().start(x, y, mapid, heading, true);
				player.setLastWarp(System.currentTimeMillis());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				//_log.error("",e);
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		
	}
	
	
}

