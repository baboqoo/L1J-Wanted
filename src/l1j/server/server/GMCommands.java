package l1j.server.server;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.common.data.ChatType;
import l1j.server.Config;
import l1j.server.SpecialEventHandler;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestTable;
import l1j.server.GameSystem.charactertrade.CharacterTradeManager;
import l1j.server.GameSystem.eventpush.EventPushManager;
import l1j.server.LFCSystem.Loader.InstanceLoadManager;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.command.CommandArgs;
import l1j.server.server.command.L1Commands;
import l1j.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1ClanRanking;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.Chocco;
import l1j.server.server.serverpackets.S_CharacterFollowEffect;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_EventCountdown;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ProtoTest;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_DialogueMessage;
import l1j.server.server.serverpackets.message.S_NotificationStringKIndex;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.templates.L1Command;
import l1j.server.server.utils.StringUtil;
import l1j.server.server.serverpackets.spell.S_SkillIconGFX;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconNotiType;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconDurationShowType;
import l1j.server.server.serverpackets.playsupport.S_ForceFinishPlaySupport;
import l1j.server.server.serverpackets.playsupport.S_ForceFinishPlaySupport.eReason;
import l1j.server.server.serverpackets.message.S_ChatMessageNoti;
import l1j.server.server.serverpackets.message.S_NotificationMessage;
import l1j.server.server.serverpackets.message.S_NotificationMessageNoti;
import l1j.server.server.serverpackets.message.S_NotificationMessage.display_position;
import l1j.server.server.serverpackets.message.S_GlobalMessege;
import l1j.server.server.serverpackets.message.S_MessegeNoti;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_MsgAnnounce;
import l1j.server.server.utils.ColorUtil;
import l1j.server.LFCSystem.InstanceEnums.LFCMessages;
import l1j.server.LFCSystem.InstanceEnums.InstSpcMessages;
import l1j.server.server.model.L1Einhasad;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;

//import manager.Manager;  // MANAGER DISABLED

public class GMCommands {
	private static Logger _log = Logger.getLogger(GMCommands.class.getName());
	
	private static Map<Integer, String> _lastCommands = new HashMap<Integer, String>();

	private static class newInstance{
		public static final GMCommands INSTANCE = new GMCommands();
	}
	public static GMCommands getInstance() {
		return newInstance.INSTANCE;
	}
	private GMCommands(){}

	private String complementClassName(String className) {
		if (className.contains(StringUtil.PeriodString)) {
			return className;
		}
		return String.format("l1j.server.server.command.executor.%s", className);
	}

	private int executeDatabaseCommand(L1PcInstance pc, String name, String arg) {
		try {
			L1Command command = L1Commands.get(name);
			if (command == null) {
				return 0;
			}
			if (pc.getAccessLevel() < command.getLevel()) {
//AUTO SRM: 				pc.sendPackets(new S_ServerMessage(74, String.format("커멘드 %s", name)), true);// \f1%0은사용할 수없습니다. // CHECKED OK
				pc.sendPackets(new S_ServerMessage(74, String.format(S_SystemMessage.getRefText(1013) + " %s ", name)), true);
				return 0;
			}

			Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
			L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
			boolean reuslt = exe.execute(pc, name, arg);
			//Manager.getInstance().GmAppend(pc.getName(), name, arg); // MANAGER DISABLED
			/** 파일로그저장 **/
			LoggerInstance.getInstance().addCommand(String.format("%s: %s %s", pc.getName(), name, arg));
			return reuslt ? 1 : 2;
		} catch (Exception e) {
			e.printStackTrace();
			_log.log(Level.SEVERE, "error gm command", e);
		}
		return 0;
	}
	
	public boolean handleCommands(L1PcInstance gm, String cmdLine) {
		try {
			StringTokenizer token = new StringTokenizer(cmdLine);
			// 최초의 공백까지가 커맨드, 그 이후는 공백을 단락으로 한 파라미터로서 취급한다
			String cmd = StringUtil.EmptyString;
			if (token.hasMoreTokens()) {
				cmd = token.nextToken();
			} else {
				cmd = cmdLine;
			}
			String param = StringUtil.EmptyString;
			while (token.hasMoreTokens()) {
				param = new StringBuilder(param).append(token.nextToken()).append(' ').toString();
			}
			param = param.trim();
	
			// 데이타베이스화 된 커멘드
			int db_command = executeDatabaseCommand(gm, cmd, param);
			if (db_command > 0) {
				if (!cmd.equalsIgnoreCase(StringUtil.PeriodString)) {
					_lastCommands.put(gm.getId(), cmdLine);
				}
				return false;
			}
	
			if (gm.getAccessLevel() < Config.ALT.GMCODE) {
//AUTO SRM: 				gm.sendPackets(new S_ServerMessage(74, String.format("커맨드 %s", cmd)), true); // CHECKED OK
				gm.sendPackets(new S_ServerMessage(74, String.format(S_SystemMessage.getRefText(1000) + " %s ", cmd)), true);
				return false;
			}
			//Manager.getInstance().GmAppend(gm.getName(), cmd, param);  // MANAGER DISABLED
			/** 파일로그저장 **/
			LoggerInstance.getInstance().addCommand(gm.getName() + ": " + cmd + StringUtil.EmptyOneString + param);	
	    	// GM에 개방하는 커맨드는 여기에 작성
			switch (cmd) {
			case "hottimepush":
				EventPushManager.getInstance().hot_time_push();
				break;
			case "dragonbless":
				StringTokenizer stringtokenizer2 = new StringTokenizer(param);
				String type2 = stringtokenizer2.nextToken();
				if (type2.equalsIgnoreCase("on")) { 
					gm.getAccount().getEinhasad().setDragonBless(gm, true);
					gm.sendPackets(new S_SystemMessage("gm dragonbless on", true), true);
				} else {
					gm.getAccount().getEinhasad().setDragonBless(gm, false);				
					gm.sendPackets(new S_SystemMessage("gm dragonbless off", true), true);
				}
				break;
			case "dragonfavor":
				StringTokenizer stringtokenizer3 = new StringTokenizer(param);
				String type3 = stringtokenizer3.nextToken();
				if (type3.equalsIgnoreCase("on")) {
					gm.getAccount().getEinhasad().setDragonFavor(gm, true);
					gm.sendPackets(new S_SystemMessage("gm dragonfavor on", true), true);
				} else {
					gm.getAccount().getEinhasad().setDragonFavor(gm, false);			
					gm.sendPackets(new S_SystemMessage("gm dragonfavor off", true), true);	
				}
				break;
			case "dragonfavorpc":
				StringTokenizer stringtokenizer5 = new StringTokenizer(param);
				String type5 = stringtokenizer5.nextToken();
				if (type5.equalsIgnoreCase("on")) {
					gm.getAccount().getEinhasad().setDragonFavorPCCafe(gm, true);
					gm.sendPackets(new S_SystemMessage("gm dragonfavor on", true), true);
				} else {
					gm.getAccount().getEinhasad().setDragonFavorPCCafe(gm, false);			
					gm.sendPackets(new S_SystemMessage("gm dragonfavor off", true), true);	
				}
				break;
			case "einhasadfavor":
				StringTokenizer stringtokenizer4 = new StringTokenizer(param);
				String type4 = stringtokenizer4.nextToken();
				int time = 0;
				if (type4.equalsIgnoreCase("on")) {
					time = 86400 * 3;
					gm.sendPackets(new S_SystemMessage("gm einhasadfavor on", true), true);					
				} else
					gm.sendPackets(new S_SystemMessage("gm einhasadfavor off", true), true);					
				gm.sendPackets(new S_SpellBuffNoti(gm, L1SkillId.EINHASAD_FAVOR, true, time), true);// 아이콘 출력
				gm.sendPackets(new S_RestExpInfoNoti(gm), true);
				gm.sendPackets(new S_ExpBoostingInfo(gm), true);					

				break;
			case "test":
				//gm.sendPackets(new S_SystemMessage(String.format("%s님의 %d번 퀘스트를 완료처리 하였습니다.", character_name, questid)), true);
				//"%s has completed quest number %d."
				//{0}%s has completed quest number {1}%d."				
				// si buscamos la cadena new S_SystemMessage(String.format( podremos encontrar los casos donde se hace uso de parametros y cambiarlo por un server message

				//gm.sendPackets(new S_SystemMessage(String.format("%s " + S_SystemMessage.getRefText(11) + " %d " + S_SystemMessage.getRefText(12), "Tungusca", 123), true), true);
				//gm.sendPackets(new S_ServerMessage(9664, String.valueOf("Tungusca"), String.valueOf(123)));
				//gm.sendPackets(new S_ChatMessageNoti(gm, ChatType.CHAT_WORLD, String.format("$123").getBytes(), null, 0)); // NO ADMITE REFS
				//gm.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(Integer.parseInt(param)), "tomatito", String.valueOf(45)), true);
				LFCMessages.REGIST_SUCCESS.sendSystemMsg(gm);
				break;
			case "test2":
				//gm.sendPackets(new S_ChatMessageNoti(gm, ChatType.CHAT_NORMAL, String.format("$123").getBytes(), null, 0)); // NO ADMITE REFS
				InstSpcMessages.INSTANCE_SPACE_FULL.sendSystemMsg(gm);

				break;
			//case "쇼"
			case "showcmd":				showCmd(gm, param, cmd);break;
			case "protocontinue":		protoContinueTest(gm, param, cmd);break;
			case "prototest":			protoTest(gm, param, cmd);break;
			case "mobaction":			actionMonster(gm, param, cmd);break;
			case "craftcheck":			craftCheck(gm, cmd);break;
			case "mapgfx":				mapGfxTranse(gm, param, cmd);break;
			case "mapeffect":			mapEffectTranse(gm, param, cmd);break;
			//case "deadlock":							GeneralThreadPool.getInstance().execute(new DeadLockDetecto(gm));break;	
			case "quest": 				BeginnerQuestTable.getInstance().execute(new CommandArgs().setOwner(gm).setParam(param));break;			
			//case "구슬":
			case "charactertrade":		CharacterTradeManager.commands(gm, param);break;
			//case "lfc상태":
			case "lfc":case "LFC":		InstanceLoadManager.commands(gm, param);break;
			//case "테스트":
			case "servertest":			serverTest(gm, param, cmd); break;
			//case "이팩트연속":
			case "chareffect":			charEffect(gm, param, cmd); break;
			//case "지속이팩트":
			case "durationeffect":		duration_effect(gm, param, cmd);break;
			//case "성장물약":
			case "grouthpotion":		grouthPotion(gm, param, cmd); break;
			//case "채팅패킷":
			case "charpacket":			charPacket(gm, param, cmd); break;
			//case "그림":
			case "picture":				picture(gm, param, cmd); break;
			//case "그림연속":
			case "pictureseq":			pictureSequence(gm, param, cmd); break;
			//case "스킬체크":
			case "skillcheck":			skillCheck(gm, param, cmd); break;
			//case "버프확인":
			case "buffcheck":			buffCheck(gm, param, cmd); break;
			case "buffcontinue":		buffContinue(gm, param, cmd);break;			
			//case "혈맹랭킹갱신":
			case "rankingupdate":
				L1ClanRanking.getInstance().gmcommand();
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("\\aA■ 혈맹레이드 랭킹이 갱신 되었습니다 ■"), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(949), true), true);
				break;
	
			//case "아이콘":
			case "icon":				icon(gm, param, cmd);break;
			//case "콘":
			case "icon1":				icon1(gm, param, cmd);break;
			//case "경험치물약지급":
			case "exppotiongift":		SpecialEventHandler.getInstance().doGiveEventStaff();break;
			//case "상점추방":
			case "shopkick":			ShopKick(gm, param, cmd);break;
			case "integratedbuff":		SpecialEventHandler.getInstance().integratedBuff();break;
			//case "포트변경":							changePort(gm, param);break;
			//case "길팅체크":
			case "movepingcheck":
				GameServerSetting.MOVE_PING_ERROR_CHECK = !GameServerSetting.MOVE_PING_ERROR_CHECK;
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("길팅체크 : " + GameServerSetting.MOVE_PING_ERROR_CHECK), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(950) + GameServerSetting.MOVE_PING_ERROR_CHECK, true), true);
			    break;
			    
			case StringUtil.PeriodString:
				if (!_lastCommands.containsKey(gm.getId())) {
//AUTO SRM: 					gm.sendPackets(new S_ServerMessage(74, String.format("커맨드 %s", cmd)), true);// \f1%0은사용할 수없습니다. // CHECKED OK
					gm.sendPackets(new S_ServerMessage(74, String.format(S_SystemMessage.getRefText(1000) + " %s ", cmd)), true);
					return false;
				}
				redo(gm, param, cmd);
				break;
			default:
//AUTO SRM: 				gm.sendPackets(new S_ServerMessage(74, String.format("[Command] 커멘드 %s 는 존재하지 않습니다.", cmd)), true); // CHECKED OK
				gm.sendPackets(new S_ServerMessage(74, String.format(S_SystemMessage.getRefText(1014) + "%s" + S_SystemMessage.getRefTextNS(1160), cmd)), true);
				break;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	/*private void showCastleWarTime(L1PcInstance pc, String param) {
		L1Castle[] castles = WarTimeController.getInstance().getCastleArray();
		Calendar[] startTime = WarTimeController.getInstance().getWarStartTimeArray();
		Calendar[] endTime = WarTimeController.getInstance().getWarEndTimeArray();
		SimpleDateFormat formatter = new SimpleDateFormat(StringUtil.DateFormatStringMinut);
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String name = tok.nextToken();
			for (int i = 0; i < castles.length; i++) {
				L1Castle castle = castles[i];
				if (castle.getName().startsWith(name)) {
					pc.sendPackets(new S_SystemMessage(String.format("%s: %s ~ %s", castle.getName(),
							formatter.format(startTime[i].getTime()), formatter.format(endTime[i].getTime()))));
				}
			}
			Calendar cal = Calendar.getInstance();
//AUTO SRM:             pc.sendPackets(new S_SystemMessage(String.format("서버시간: %s", formatter.format(cal.getTime()))));
            pc.sendPackets(new S_SystemMessage(String.format(S_SystemMessage.getRefText(4) + " %s ", formatter.format(cal.getTime())), true));
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".공성시간 (켄트,오크,윈다,기란,하이,드워,아덴,디아)"));
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(5), true));
		}
	}*/
	
	private void duration_effect(L1PcInstance gm, String param, String cmdName){
		try {
			StringTokenizer tok = new StringTokenizer(param);
			int value	= Integer.parseInt(tok.nextToken());
			String onoff	= tok.nextToken();
//AUTO SRM: 			gm.sendPackets(new S_CharacterFollowEffect(gm.getId(), onoff.equals("켬"), value), true); // CHECKED OK
			gm.sendPackets(new S_CharacterFollowEffect(gm.getId(), onoff.equals(S_SystemMessage.getRefText(1016)), value), true);
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("체크 : " + value + ", " + onoff), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(953) + value  + ", " + onoff, true), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".지속이팩트 [숫자] [켬/끔]"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(954), true), true);
		}
	}
	
	private void showCmd(L1PcInstance gm, String param, String cmdName){
		try {
			StringTokenizer tok = new StringTokenizer(param);
			
			int value1	= Integer.parseInt(tok.nextToken());
			int value2	= Integer.parseInt(tok.nextToken());
			//int value3	= Integer.parseInt(tok.nextToken());
			
			String str = tok.nextToken();
			
			/*int start	= Integer.parseInt(tok.nextToken());
			int end		= Integer.parseInt(tok.nextToken());
			int value	= Integer.parseInt(tok.nextToken());*/
			
			gm.sendPackets(new S_EventCountdown(value1, str, value2), true);
			
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("쇼: " + value1 + ", " + str + ", " + value2), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(955) + value1  + ", " + str  + ", " + value2, true), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".쇼"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(956), true), true);
		}
	}
	
	private void protoContinueTest(L1PcInstance gm, String param, String cmdName){
		try {
			StringTokenizer tok = new StringTokenizer(param);
			int start	= Integer.parseInt(tok.nextToken());
			int end		= Integer.parseInt(tok.nextToken());
			
			for (int value=start; value<=end; value++) {
				if (gm == null) {
					break;
				}
				gm.sendPackets(new S_ProtoTest(value), true);
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("프로토연속 code[" + value + "]"), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(957) + value  + "]", true), true);
				Thread.sleep(200L);
			}
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".프로토연속 [시작][종료]"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(182), true), true);
		}
	}
	
	private void protoTest(L1PcInstance gm, String param, String cmdName){
		try {
			StringTokenizer tok = new StringTokenizer(param);
			int value	= Integer.parseInt(tok.nextToken());
			gm.sendPackets(new S_ProtoTest(value), true);
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("프로토 code[" + GameServerSetting.TEST + "], 숫자[" + value + "]"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(959) + GameServerSetting.TEST  + S_SystemMessage.getRefText(960) + value  + "]", true), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".프로토 [숫자]"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(702), true), true);
		}
	}
	
	private void actionMonster(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			int action = Integer.parseInt(tok.nextToken());
			L1NpcInstance npc = null;
			for (L1Object obj : gm.getKnownObjects()) {
				if (obj instanceof L1NpcInstance) {
					npc = (L1NpcInstance)obj;
					Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), action), true);
				}
			}
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("몹액션 : " + action), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(962) + action, true), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".몹액션 [번호]"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(702), true), true);
		}
	}
	
	private void mapGfxTranse(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String name = tok.nextToken();
			String onoff = tok.nextToken();
			//gm.sendPackets(new S_SceneNoti(name, onoff.equals("켬"), onoff2.equals("켬")), true);
//AUTO SRM: 			gm.sendPackets(new S_SceneNoti(onoff.equals("켬"), name, System.currentTimeMillis() / 1000, gm.getX(), gm.getY()), true); // CHECKED OK
			gm.sendPackets(new S_SceneNoti(onoff.equals(S_SystemMessage.getRefText(1016)), name, System.currentTimeMillis() / 1000, gm.getX(), gm.getY()), true);
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("맵변화 : " + name + StringUtil.EmptyOneString + onoff), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(964) + name  + StringUtil.EmptyOneString  + onoff, true), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".맵변화 [타입]"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(965), true), true);
		}
	}
	
	private void mapEffectTranse(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			int name = Integer.parseInt(tok.nextToken());
			String onoff = tok.nextToken();
//AUTO SRM: 			gm.sendPackets(new S_SceneNoti(onoff.equals("켬"), System.currentTimeMillis() / 1000, name, gm.getX(), gm.getY()), true); // CHECKED OK
			gm.sendPackets(new S_SceneNoti(onoff.equals(S_SystemMessage.getRefText(1016)), System.currentTimeMillis() / 1000, name, gm.getX(), gm.getY()), true);
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("맵이팩 : " + name), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(966) + name, true), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".맵이팩 [네임]"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(969), true), true);
		}
	}
	
	private void craftCheck(L1PcInstance gm, String cmdName){
		GameServerSetting.CRAFT_CHECK = !GameServerSetting.CRAFT_CHECK;
//AUTO SRM: 		gm.sendPackets(new S_SystemMessage("제작체크 : " + GameServerSetting.CRAFT_CHECK), true); // CHECKED OK
		gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(970) + GameServerSetting.CRAFT_CHECK, true), true);
	}
	
	private void charEffect(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			//int 시작번호 = Integer.parseInt(stringtokenizer.nextToken());
			//int 끝번호 = Integer.parseInt(stringtokenizer.nextToken());
			int startNumber = Integer.parseInt(stringtokenizer.nextToken());
			int endNumber = Integer.parseInt(stringtokenizer.nextToken());			
			
			for(int k = startNumber; k < endNumber + 1; k++) {
				if(gm == null)break;
				gm.sendPackets(new S_Effect(gm.getId(), k), true);
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("이팩트 번호  " + k), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(971) + k, true), true);
				Thread.sleep(500);
            } 
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".이팩트연속 [시작번호] [끝번호] 으로 입력"), true);
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(182), true), true);
		}
	}
	
	private void serverTest(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			int number = Integer.parseInt(stringtokenizer.nextToken());
			GameServerSetting.TEST = number;
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(number + "번 테스트"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(number  + S_SystemMessage.getRefText(973), true), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".테스트 번호 으로 입력해 주세요."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(702), true), true);
		}
	}
	
	private void grouthPotion(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			//int 타입 = Integer.parseInt(stringtokenizer.nextToken());
			//int 작동 = Integer.parseInt(stringtokenizer.nextToken());
			int gType = Integer.parseInt(stringtokenizer.nextToken());
			int gWork = Integer.parseInt(stringtokenizer.nextToken());			

			gm.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, 3600, gType, gWork), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".성장물약 번호 [1:작동/2:중지] 으로 입력해 주세요."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(975), true), true);
		}
	}
	
	private void charPacket(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			//int 번호 = Integer.parseInt(stringtokenizer.nextToken());
			int number = Integer.parseInt(stringtokenizer.nextToken());
			gm.sendPackets(new S_NotificationStringKIndex(number, 2805, "ff 00 00", 20), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".채팅패킷 번호 으로 입력해 주세요."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(702), true), true);
		}
	}
	
	private void picture(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			//int 이미지 = Integer.parseInt(stringtokenizer.nextToken());
			int image = Integer.parseInt(stringtokenizer.nextToken());
			gm.sendPackets(new S_DialogueMessage(3515, image, 300), true);
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("그림번호  " + 이미지), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(977) + image, true), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".그림 번호 으로 입력해 주세요."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(702), true), true);
		}
	}
	
	private void pictureSequence(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			int start = Integer.parseInt(stringtokenizer.nextToken());
			int end = Integer.parseInt(stringtokenizer.nextToken());
			
			for (int k = start; k < end + 1; k++) {
				if(gm == null)break;
				gm.sendPackets(new S_DialogueMessage(k, 1, 300), true);
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("그림번호  " + k), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(977) + k, true), true);
				Thread.sleep(200);
            }
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".그림연속 [시작번호] [끝번호] 으로 입력"), true);
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(182), true), true);
		}
	}
	
	private void skillCheck(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			int start = Integer.parseInt(stringtokenizer.nextToken());
			int end = Integer.parseInt(stringtokenizer.nextToken());
			
			for (int k = start; k < end + 1; k++) {
				if (gm == null) {
					break;
				}
				gm.sendPackets(new S_AvailableSpellNoti(k, true), true);
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("스킬번호  " + k), true);// 실제 배우는 번호 // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(980) + k, true), true);
				Thread.sleep(200);
            } 
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".스킬체크[시작번호] [끝번호] 으로 입력"), true);
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(182), true), true);
		}
	}
	
	private void buffContinue(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			int start = Integer.parseInt(stringtokenizer.nextToken());
			int end = Integer.parseInt(stringtokenizer.nextToken());
			
			for(int k = start; k < end + 1; k++){
				if(gm == null)break;
				gm.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, k, true), true);
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("버프연속  " + k), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(982) + k, true), true);
				Thread.sleep(100);
            } 
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".버프연속 [시작번호] [끝번호] 으로 입력"), true);
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(182), true), true);
		}
	}
	
	private void buffCheck(L1PcInstance gm, String param, String cmdName) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(param);
			String type = stringtokenizer.nextToken();
            //if (type.equalsIgnoreCase("버프")) {
			if (type.equalsIgnoreCase("buff")) {
            	//int 번호 = Integer.parseInt(stringtokenizer.nextToken());
    			//int 시간 = Integer.parseInt(stringtokenizer.nextToken());
				int number = Integer.parseInt(stringtokenizer.nextToken());
				int time = Integer.parseInt(stringtokenizer.nextToken());				
    			if (number > 0 && time > 0){
    				gm.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, number, time), true);
//AUTO SRM:         			gm.sendPackets(new S_SystemMessage("버프번호 " + 번호), true); // CHECKED OK
        			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(984) + number, true), true);
    			} else {
//AUTO SRM:     				gm.sendPackets(new S_SystemMessage(".버프확인 [버프] 번호 시간 으로 입력해 주세요."), true); // CHECKED OK
    				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(985), true), true);
    			}
			//} else if (type.equalsIgnoreCase("요리")){
			} else if (type.equalsIgnoreCase("cooking")){
				int number = Integer.parseInt(stringtokenizer.nextToken());
    			int time = Integer.parseInt(stringtokenizer.nextToken());
    			if (number > 0 && time > 0){
    				gm.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, number, time), true);
//AUTO SRM:         			gm.sendPackets(new S_SystemMessage("요리번호 " + 번호), true); // CHECKED OK
        			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(986) + number, true), true);
    			} else {
//AUTO SRM:     				gm.sendPackets(new S_SystemMessage(".버프확인 [요리] 번호 시간 으로 입력해 주세요."), true); // CHECKED OK
    				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(987), true), true);
    			}
			//} else if (type.equalsIgnoreCase("아우라")){
			} else if (type.equalsIgnoreCase("aura")){
				int number = Integer.parseInt(stringtokenizer.nextToken());
				int time = Integer.parseInt(stringtokenizer.nextToken());
				if (number > 0 && time > 0){
					gm.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, number, time), true);
//AUTO SRM: 					gm.sendPackets(new S_SystemMessage("아우라번호 " + 번호), true) // CHECKED OK;
					gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(988) + number, true), true);
				} else {
//AUTO SRM: 					gm.sendPackets(new S_SystemMessage(".버프확인 [아우라] 번호 시간 으로 입력해 주세요."), true); // CHECKED OK
					gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(989), true), true);
				}
			//} else if (type.equalsIgnoreCase("무제한")){
			} else if (type.equalsIgnoreCase("unlimited")){
				int number = Integer.parseInt(stringtokenizer.nextToken());
				if (number > 0){
					gm.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, number, true), true);
//AUTO SRM: 					gm.sendPackets(new S_SystemMessage("무제한번호 " + 번호), true); // CHECKED OK
					gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(990) + number, true), true);
				} else {
//AUTO SRM: 					gm.sendPackets(new S_SystemMessage(".버프확인 [무제한] 번호 으로 입력해 주세요."), true); // CHECKED OK
					gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(991), true), true);
				}
			//} else if (type.equalsIgnoreCase("독")){
			} else if (type.equalsIgnoreCase("poison")){
				int number = Integer.parseInt(stringtokenizer.nextToken());
				int time = Integer.parseInt(stringtokenizer.nextToken());
				if (number > 0 && time > 0){
					gm.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, number, time), true);
//AUTO SRM: 					gm.sendPackets(new S_SystemMessage("독번호 " + number), true); // CHECKED OK
					gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(992) + number, true), true);
				} else {
//AUTO SRM: 					gm.sendPackets(new S_SystemMessage(".버프확인 [독] 번호 시간 으로 입력해 주세요."), true); // CHECKED OK
					gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(993), true), true);
				}
			//} else if (type.equalsIgnoreCase("혈흔")){
			} else if (type.equalsIgnoreCase("dragonblood")){
				int number = Integer.parseInt(stringtokenizer.nextToken());
				int time = Integer.parseInt(stringtokenizer.nextToken());
				if (number > 0 && time > 0){
					gm.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, number, time), true);
//AUTO SRM: 					gm.sendPackets(new S_SystemMessage("혈흔번호 " + 번호), true); // CHECKED OK
					gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(994) + number, true), true);
				} else {
//AUTO SRM: 					gm.sendPackets(new S_SystemMessage(".버프확인 [혈흔] 번호 시간 으로 입력해 주세요."), true); // CHECKED OK
					gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(995), true), true);
				}
			//} else if (type.equalsIgnoreCase("진동")){
			} else if (type.equalsIgnoreCase("hadin")){
				int number = Integer.parseInt(stringtokenizer.nextToken());
				if (number > 0){
					gm.sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, number), true);
//AUTO SRM: 					gm.sendPackets(new S_SystemMessage("진동번호 " + 번호), true); // CHECKED OK
					gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(996) + number, true), true);
				} else {
//AUTO SRM: 					gm.sendPackets(new S_SystemMessage(".버프확인 [진동] 번호 으로 입력해 주세요."), true); // CHECKED OK
					gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(997), true), true);
				}
			} else {
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage(".버프확인 [버프/요리/아우라/무제한/독/혈흔/진동] 으로 입력해 주세요."), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(998), true), true);
			}
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".버프확인 [버프/요리/아우라/무제한/독/진동] 으로 입력해 주세요."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(998), true), true);
		}
	}
	
	private void redo(L1PcInstance pc, String arg, String cmdName) {
		try {
			String lastCmd = _lastCommands.get(pc.getId());
			if (arg.isEmpty()) {
				//pc.sendPackets(new S_SystemMessage(String.format("커맨드 %s 을(를) 재실행합니다.", lastCmd)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(12), lastCmd), true);
				handleCommands(pc, lastCmd);
			} else {
				StringTokenizer token = new StringTokenizer(lastCmd);
				String cmd = token.nextToken() + StringUtil.EmptyOneString + arg;
				//pc.sendPackets(new S_SystemMessage(String.format("커맨드 %s 을(를) 재실행합니다.", cmd)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(12), cmd), true);
				handleCommands(pc, cmd);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".재실행 커맨드에러"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(168), true), true);
		}
	}
	
	private void ShopKick(L1PcInstance gm, String param, String cmdName) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(param);
			if (target != null) {
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append("님을 추방 했습니다.").toString()), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(S_SystemMessage.getRefText(1003)).toString(), true), true);
				GameServer.disconnectChar(target);
			} else
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("그러한 이름의 캐릭터는 월드내에는 존재하지 않습니다."), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(156), true), true);
		} catch (Exception e) {
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(".상점추방 캐릭명"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(157), true), true);
		}
	}
	
	private void icon(L1PcInstance pc, String param, String cmdName) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			int iconId = Integer.parseInt(st.nextToken(), 10);
			pc.sendPackets(new Chocco(3), true);
			pc.sendPackets(new Chocco(2), true);
			pc.sendPackets(new Chocco(4), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_LIST, iconId), true);
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".아이콘 [actid]를 입력 해주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(165), true), true);
		}
	}
	
	private void icon1(L1PcInstance pc, String param, String cmdName) {
        try {
            StringTokenizer st = new StringTokenizer(param);
            String type = st.nextToken();
            //if (type.equalsIgnoreCase("지속")) {
			if (type.equalsIgnoreCase("continuous")) {
                try {
                    String select = st.nextToken();
                    int i = Integer.parseInt(st.nextToken());
                    int j = 0;
                    try {
                        j = Integer.parseInt(st.nextToken());
                    } catch (Exception exception) {
                        j = i;
                    }
                    //if (select.equalsIgnoreCase("표현")) {
					if (select.equalsIgnoreCase("add")) {
                        for (int k = i; k < j + 1; k++) {
//AUTO SRM:                             pc.sendPackets(new S_SystemMessage("\\aA아이콘 지속출력 번호 : [\\aG" + k + "\\aA]"), true); // CHECKED OK
                            pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1006) + k  + "\\aA]", true), true);
                            pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, k, true), true);
                        }
                    //} else if (select.equalsIgnoreCase("삭제")) {
					} else if (select.equalsIgnoreCase("delete")) {
                        for (int k = i; k < j + 1; k++) {
//AUTO SRM:                             pc.sendPackets(new S_SystemMessage("\\aA아이콘 삭제 번호 : [\\aG" + k + "\\aA]"), true); // CHECKED OK
                            pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1007) + k  + "\\aA]", true), true);
                            pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, k, false), true);
                        }
                    } else {
//AUTO SRM:                         pc.sendPackets(new S_SystemMessage("\\aI.콘 [지속] [표현 or 삭제] [i 또는 i~j]"), true); // CHECKED OK
                        pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1008), true), true);
                    }
                } catch (Exception exception) {
//AUTO SRM:                     pc.sendPackets(new S_SystemMessage("\\aI.콘 [지속] [표현 or 삭제] [i 또는 i~j]"), true); // CHECKED OK
                    pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1008), true), true);
                }
            //} else if (type.equalsIgnoreCase("연속")) {
			} else if (type.equalsIgnoreCase("consecutive")) {
                try {
                    int i = Integer.parseInt(st.nextToken());
                    int j = 0;
                    try {
                        j = Integer.parseInt(st.nextToken());
                    } catch (Exception exception) {
                        j = i;
                    }
                    for (int k = i; k < j + 1; k++) {
//AUTO SRM:                         pc.sendPackets(new S_SystemMessage("\\aA아이콘 연속출력 번호:[\\aG" + k + "\\aA]"), true); // CHECKED OK
                        pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1009) + k  + "\\aA]", true), true);
                        pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, k, true), true);
                        Thread.sleep(500);
                        pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, k, false), true);
                    }
                } catch (Exception exception) {
//AUTO SRM:                     pc.sendPackets(new S_SystemMessage("\\aL.콘 [연속] [i 또는 i~j]"), true); // CHECKED OK
                    pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1010), true), true);
                }
            } else {
//AUTO SRM:                 pc.sendPackets(new S_SystemMessage("\\aI.콘 [지속] [표현 or 삭제] [i 또는 i~j]"), true); // CHECKED OK
                pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1008), true), true);
//AUTO SRM:                 pc.sendPackets(new S_SystemMessage("\\aL.콘 [연속] [i 또는 i~j]"), true); // CHECKED OK
                pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1010), true), true);
            }
        } catch (Exception exception) {
//AUTO SRM:             pc.sendPackets(new S_SystemMessage("\\aI.아이콘 [지속] [표현 or 삭제] [i 또는 i~j]"), true) // CHECKED OK;
            pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1008), true), true);
//AUTO SRM:             pc.sendPackets(new S_SystemMessage("\\aL.아이콘 [연속] [i 또는 i~j]"), true); // CHECKED OK
            pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(1010), true), true);
        }
    }
	
//	private void changePort(L1PcInstance gm, String param) {
//	try {
//		 StringTokenizer st = new StringTokenizer(param); 
//		 int port = Integer.parseInt(st.nextToken(), 10); 
//		 Server.changeport(port);
//		 gm.sendPackets(new S_SystemMessage(port+" 번호로 포트가 변경 되었습니다."), true); 
//	} catch (Exception e) {
//		gm.sendPackets(new S_SystemMessage(".포트변경 [Port]"), true);
//	}
//}
	
}


