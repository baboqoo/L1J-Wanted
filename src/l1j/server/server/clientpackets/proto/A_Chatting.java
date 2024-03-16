package l1j.server.server.clientpackets.proto;

import java.util.Iterator;

import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.IndunSystem.minigame.L1Gambling;
import l1j.server.IndunSystem.minigame.L1Gambling3;
import l1j.server.common.data.ChatType;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GMCommands;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerSetting;
import l1j.server.server.UserCommands;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ChatLogTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoppelgangerInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.monitor.Logger;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_Chat;
import l1j.server.server.serverpackets.message.S_ChatMessageNoti;
import l1j.server.server.serverpackets.message.S_MsgAnnounce;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.ColorUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.define.GMDefine;
//import manager.Manager;  // MANAGER DISABLED

public class A_Chatting extends ProtoHandler {
	protected A_Chatting(){}
	private A_Chatting(byte[] data, GameClient client) {
		super(data, client);
	}
	//private static final byte[] LINK_MESSAGE_DEFAULT = "https://lineage.plaync.com/".getBytes();
	
	private L1World world;
	private int _transaction_id;
	private ChatType _type;
	private byte[] _message;
	private String _message_format;
	private byte[] _target_user_name;
	private int _target_user_server_no;
	private byte[] _link_message;
	
	void parse() throws Exception {
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_transaction_id = read4(read_size());
				break;
			case 0x10:
				_type = ChatType.fromInt(readC());// 채팅타입
				break;
			case 0x1a:
				int message_length = readC();
				if (message_length > 0) {
					_message = readByte(message_length);
				}
				break;
			case 0x2a:
				int target_user_name_length = readC();
				if (target_user_name_length > 0) {
					_target_user_name = readByte(target_user_name_length);
				}
				break;
			case 0x30:
				_target_user_server_no = read4(read_size());
				break;
			/*case 0x3a:
				int link_message_length = 0;
				if (isRead(1)) {
					link_message_length = readC();
				}
				if (link_message_length > 0) {
					_link_message = readByte(link_message_length);// 앱센터 주소로 온다(파워북 경로가 아니면 처리안됨)
					_link_message = LINK_MESSAGE_DEFAULT;
				}
				break;*/
			default:
				return;
			}
		}
	}
	
	boolean isValidation() {
		return _type != null && _message != null;
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		if (!isValidation()) {
			return;
		}
		world = L1World.getInstance();
		chat();
	}
	
	void chat() {
		if (GameServerSetting.SERVER_NOT_CHAT && !_pc.isGm() && !ChatType.isCannotPass(_type)) {
			_pc.sendPackets(L1SystemMessage.NOT_CHATTING_MESSAGE);
			return;
		}
		if (_pc.isSilence()) {
			return;
		}
		if (_pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {// 채팅 금지중
			_pc.sendPackets(L1ServerMessage.sm242);// 현재 채팅 금지중입니다.
			return;
		}
		if (_pc.getMapId() == 631 && !_pc.isGm()) {
			_pc.sendPackets(L1ServerMessage.sm912);// 채팅을 할 수 없습니다.
			return;
		}
		if (_pc.isDeathMatch() && !_pc.isGm() && !_pc.isGhost()) {
			_pc.sendPackets(L1ServerMessage.sm912);// 채팅을 할 수 없습니다.
			return;
		}
		if (!_pc.isGm() && _pc.getMapId() == 5153 && _type != ChatType.CHAT_NORMAL) {// 배틀존
			_pc.sendPackets(L1SystemMessage.BATTLE_ZONE_CHATTING);
			return;
		}
		if (Config.ALT.CHAT_ALLOWED_IN_PVP && _pc.isPinkName() && !ChatType.isCannotBattle(_type)) {
			_pc.sendPackets(L1SystemMessage.PINK_NAME_CHATTING);
			return;
		}
		String message = _message_format = new String(_message);
		switch(_type){
		case CHAT_NORMAL:// 일반 채팅
			normal(message);
			break;
		case CHAT_WHISPER:// 귓속말
			whisper(message);
			break;
		case CHAT_SHOUT:// 외치기
			shout(message);
			break;
		case CHAT_WORLD:// 전체 채팅
			world(message);
			break;
		case CHAT_PLEDGE:// 혈맹 채팅
			pledge(message);
			break;
		case CHAT_HUNT_PARTY:// 파티 채팅
			huntParty(message);
			break;
		case CHAT_TRADE:// 장사 채팅
			world(message);
			break;
		case CHAT_PLEDGE_PRINCE:// 군주
			pledgePrince(message);
			break;
		case CHAT_CHAT_PARTY:// 채팅 파티
			chatParty(message);
			break;
		case CHAT_PLEDGE_ALLIANCE:
			pledgeAlliance();
			break;
		case CHAT_PLEDGE_NOTICE:// 혈맹 채팅(간부)
			pledgeNotice(message);
			break;
		default:
			break;
		}
		if (!_pc.isGm()) {
			_pc.checkChatInterval();
		}
	}
	
	void normal(String message){
		if (_pc.isGhost() && !(_pc.isGm() || _pc.isMonitor())) {
			return;
		}
		if (message.startsWith(StringUtil.PeriodString)) {// 명령어
			String command = message.substring(1);
			if (_pc.getAccessLevel() == Config.ALT.GMCODE) {
				GMCommands.getInstance().handleCommands(_pc, command);
			} else {
				UserCommands.getInstance().handleCommands(_pc, command);
			}
			return;
		}
		if (message.startsWith(StringUtil.DollarString)) {
			String text = message.substring(1);
			_type = ChatType.CHAT_TRADE;
			world(text);
			return;
		}
		
		if (_pc.getGambling().isGambling() && isGamble(message)) {
			return;
		}
		if (_pc.getGambling().isGambling3() && isGamble3(message)) {
			return;
		}
		
		_pc.sendPackets(new S_Chat(_transaction_id, _type, _message, null, _link_message), true);
		S_ChatMessageNoti s_chat = new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no);
		for (L1PcInstance listner : world.getRecognizePlayer(_pc)) {
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
			if (!spamList.contains(0, _pc.getName())) {
				listner.sendPackets(s_chat);
			}
		}
		_pc.sendPackets(s_chat, true);
		dopelChatting(message);// 도펠겡어 채팅
		//Manager.getInstance().NomalchatAppend(_pc.getName(), message); // MANAGER DISABLED
    	ChatLogTable.getInstance().storeChat(_pc, null, message, _type);
    	LoggerInstance.getInstance().addChat(Logger.ChatType.Normal, _pc, message);
    	GMDefine.add_chat(_type, _pc, null, message);
	}
	
	void shout(String message){
		if (_pc.isGhost()) {
			return;
		}
		ChatLogTable.getInstance().storeChat(_pc, null, message, _type);
		LoggerInstance.getInstance().addChat(Logger.ChatType.Shouting, _pc, message);
		S_ChatMessageNoti shoutting = new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no);
		for (L1PcInstance listner : world.getRecognizePlayer(_pc)) {
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
			if (!spamList.contains(0, _pc.getName())) {
				listner.sendPackets(shoutting);
			}
		}
		_pc.sendPackets(shoutting, true);
		dopelChatting(message);// 도펠겡어 채팅
		GMDefine.add_chat(_type, _pc, null, message);
	}

	void world(String chatText) {
		if (_pc.isGm()) {
			if (_type == ChatType.CHAT_WORLD) {
				String message = String.format("[******] %s", chatText);
				world.broadcastServerMessage(message, true);
				world.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message), true);
				//Manager.getInstance().AllChatAppend(_pc.getName(), chatText); // MANAGER DISABLED
			} else if (_type == ChatType.CHAT_TRADE) {// 장사채팅
				world.broadcastPacketToAll(new S_MsgAnnounce(chatText, ColorUtil.getWhiteRgbBytes()), true);
			}
		} else if (_pc.getLevel() >= Config.ALT.GLOBAL_CHAT_LEVEL) {
			if (world.isWorldChatElabled()) {
				if (_pc.getFood() >= 12) {// 5%겟지?
					ChatLogTable.getInstance().storeChat(_pc, null, chatText, _type);
					if (_type == ChatType.CHAT_WORLD) {
						//Manager.getInstance().AllChatAppend(_pc.getName(), chatText); // MANAGER DISABLED
					} else if (_type == ChatType.CHAT_TRADE) {
						// manager.LogChatTradeAppend("[장사]", pc.getName(), chatText);
					}
					S_ChatMessageNoti chat = new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no);
					for (L1PcInstance listner : world.getAllPlayers()) {
						L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList.contains(0, _pc.getName())) {
							if (listner.getConfig().isShowTradeChat() && _type == ChatType.CHAT_TRADE) {
								listner.sendPackets(chat);
							} else if (listner.getConfig().isShowWorldChat() && _type == ChatType.CHAT_WORLD) {
								listner.sendPackets(chat);
							}
						}
					}
					chat.clear();
					chat = null;
				} else {
					_pc.sendPackets(L1ServerMessage.sm462);
				}
			} else {
				_pc.sendPackets(L1ServerMessage.sm510);
			}
		} else {
			_pc.sendPackets(new S_ServerMessage(195, String.valueOf(Config.ALT.GLOBAL_CHAT_LEVEL)), true);
		}
		LoggerInstance.getInstance().addChat(Logger.ChatType.Global, _pc, chatText);
		GMDefine.add_chat(_type, _pc, null, chatText);
	}
	
	void pledge(String message){
		if (_pc.getClanid() == 0) {
			return;
		}
		L1Clan clan = world.getClan(_pc.getClanName());
		if (clan == null) {
			return;
		}
		ChatLogTable.getInstance().storeChat(_pc, null, message, _type);
		//Manager.getInstance().ClanChatAppend(_pc.getClanName(), _pc.getName(), message); // MANAGER DISABLED
		LoggerInstance.getInstance().addChat(Logger.ChatType.Clan, _pc, message);
		S_ChatMessageNoti chat = new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no);
		for (L1PcInstance listner : clan.getOnlineClanMember()) {
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
			if (!spamList.contains(0, _pc.getName())) {
				listner.sendPackets(chat);
			}
		}
		chat.clear();
		chat = null;
		GMDefine.add_chat(_type, _pc, null, message);
	}
	
	void huntParty(String message){
		if (!_pc.isInParty()) {
			return;
		}
		//Manager.getInstance().PartyChatAppend(_pc.getName(), message); // MANAGER DISABLED
		ChatLogTable.getInstance().storeChat(_pc, null, message, _type);
		LoggerInstance.getInstance().addChat(Logger.ChatType.Party, _pc, message);
		S_ChatMessageNoti chat = new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no);
		for (L1PcInstance listner : _pc.getParty().getMembersArray()) {
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
			if (!spamList.contains(0, _pc.getName())) {
				listner.sendPackets(chat);
			}
		}
		chat.clear();
		chat = null;
		GMDefine.add_chat(_type, _pc, null, message);
	}
	
	void pledgePrince(String message){
		if (_pc.getClanid() == 0) {
			return;
		}
		L1Clan clan = world.getClan(_pc.getClanName());
		if (clan != null && (_pc.isCrown() && _pc.getId() == clan.getLeaderId())) {
			//Manager.getInstance().ClanChatAppend(_pc.getClanName(), _pc.getName(), message); // MANAGER DISABLED
			LoggerInstance.getInstance().addChat(Logger.ChatType.Clan, _pc, message);
			S_ChatMessageNoti chat = new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no);
			for (L1PcInstance listner : clan.getOnlineClanMember()) {
				L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
				if (!spamList.contains(0, _pc.getName())) {
					listner.sendPackets(chat);
				}
			}
			chat.clear();
			chat = null;
			GMDefine.add_chat(_type, _pc, null, message);
		}
	}
	
	void chatParty(String message){
		if (!_pc.isInChatParty()) {
			return;
		}
		//Manager.getInstance().PartyChatAppend(_pc.getName(), message); // MANAGER DISABLED
		ChatLogTable.getInstance().storeChat(_pc, null, message, _type);
		LoggerInstance.getInstance().addChat(Logger.ChatType.Group, _pc, message);
		S_ChatMessageNoti chat = new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no);
		for (L1PcInstance listner : _pc.getChatParty().getMembers()) {
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
			if (!spamList.contains(0, _pc.getName())) {
				listner.sendPackets(chat);
			}
		}
		chat.clear();
		chat = null;
		GMDefine.add_chat(_type, _pc, null, message);
	}
	
	void pledgeAlliance(){
		if (_pc.getClanid() == 0) {
			return;
		}
		L1Clan clan = world.getClan(_pc.getClanName());
		if (clan == null) {
			return;
		}
		FastMap<Integer, Integer> alianceList = clan.getAlliance();
		if (alianceList == null || alianceList.isEmpty()) {
			return;
		}
		Iterator<Integer> it = alianceList.keySet().iterator();
		S_ChatMessageNoti chat = new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no);
		for (L1PcInstance listner : clan.getOnlineClanMember()) {
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
			if (!spamList.contains(0, _pc.getName())) {
				listner.sendPackets(chat);
			}
		}
		while (it.hasNext()) {
			int alianceClanId = it.next();
			clan = world.getClan(alianceClanId);
			if (clan != null) {
				for (L1PcInstance listner : clan.getOnlineClanMember()) {
					L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
					if (!spamList.contains(0, _pc.getName())) {
						listner.sendPackets(chat);
					}
				}
			}
		}
		chat.clear();
		chat = null;
		GMDefine.add_chat(_type, _pc, null, _message_format);
	}
	
	void pledgeNotice(String message){
		if (_pc.getClanid() == 0) {
			return;
		}

		L1Clan clan = world.getClan(_pc.getClanName());
		eBloodPledgeRankType rank = _pc.getBloodPledgeRank();
		if (clan == null || !eBloodPledgeRankType.isAuthRankAtKnight(rank)) {
			return;
		}
		//Manager.getInstance().ClanChatAppend(_pc.getClanName(), _pc.getName(), message); // MANAGER DISABLED
		LoggerInstance.getInstance().addChat(Logger.ChatType.Clan, _pc, message);
		S_ChatMessageNoti chat = new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no);
		for (L1PcInstance listner : clan.getOnlineClanMember()) {
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
			if (!spamList.contains(0, _pc.getName())) {
				listner.sendPackets(chat);
			}
		}
		chat.clear();
		chat = null;
		GMDefine.add_chat(_type, _pc, null, message);
	}
	
	void whisper(String message) {
		if (_pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {// 채팅 금지중의 경우
			_pc.sendPackets(L1ServerMessage.sm242);// 현재 채팅 금지중입니다.
			return;
		}
		if (_pc.getLevel() < Config.ALT.WHISPER_CHAT_LEVEL) {
			_pc.sendPackets(new S_ServerMessage(404, String.valueOf(Config.ALT.WHISPER_CHAT_LEVEL)), true);
			return;
		}
		
		String targetName = new String(_target_user_name);
		
		// 영자상점 귓속말
		if (world.getShopNpc(targetName) != null) {
			_pc.sendPackets(new S_SystemMessage(String.format("-> (%s) %s", targetName, message)), true);
			return;
		}
		
		L1PcInstance whisperTo = world.getPlayer(targetName);// 받는 유저
		if (whisperTo == null) {// 월드에 없는 경우
			if (L1CharacterInfo.isGmName(targetName)) {// 운영자 부재시 답변 처리
				_pc.sendPackets(new S_SystemMessage(String.format("-> (%s) %s", targetName, Config.MESSAGE.GM_EMPTY_MESSAGE), true), true);
				return;
			}
			_pc.sendPackets(new S_ServerMessage(73, targetName), true);
			return;
		}
		// 자기 자신에 대한 귓속말
		if (whisperTo.equals(_pc)) {
			return;
		}

		// 차단되고 있는 경우
		L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(whisperTo.getId());
		if (spamList.contains(0, _pc.getName())) {
			_pc.sendPackets(new S_ServerMessage(117, whisperTo.getName()), true);
			return;
		}
		
		// 귓속말을 끈경우
		if (!whisperTo.getConfig().isCanWhisper()) {
			_pc.sendPackets(new S_ServerMessage(205, whisperTo.getName()), true);
			return;
		}
		
		// 유저가 운영자에게 귓속말 전달
		if (_pc.getAccessLevel() == 0 && whisperTo.isGm() && !StringUtil.isNullOrEmpty(Config.MESSAGE.GM_WISPER_MESSAGE)) {
			whisperTo.sendPackets(new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no), true);
			_pc.sendPackets(new S_SystemMessage(String.format("-> (%s) %s", whisperTo.getName(), Config.MESSAGE.GM_WISPER_MESSAGE), true), true);
			return;
		}
		_pc.sendPackets(new S_Chat(_transaction_id, _type, _message, _target_user_name, _link_message), true);
		whisperTo.sendPackets(new S_ChatMessageNoti(_pc, _type, _message, _link_message, _target_user_server_no), true);
		//Manager.getInstance().WisperChatAppend(_pc.getName(), whisperTo.getName(), message); // MANAGER DISABLED
		LoggerInstance.getInstance().addWhisper(_pc, whisperTo, message);
		GMDefine.add_chat(_type, _pc, whisperTo, message);
	}
	
	void dopelChatting(String message){
		L1DoppelgangerInstance dopel = null;
		switch(_type){
		case CHAT_NORMAL:
			for (L1Object obj : _pc.getKnownObjects()) {
				if (obj instanceof L1DoppelgangerInstance) {
					dopel = (L1DoppelgangerInstance) obj;
					if (dopel.getName().equals(_pc.getName())) {
						dopel.broadcastPacket(new S_NpcChatPacket(dopel, message, _type), true);
					}
				}
			}
			break;
		case CHAT_SHOUT:
			for (L1Object obj : _pc.getKnownObjects()) {
				if (obj instanceof L1DoppelgangerInstance) {
					dopel = (L1DoppelgangerInstance) obj;
					if (dopel.getName().equals(_pc.getName())) {
						for (L1PcInstance listner : L1World.getInstance().getVisiblePlayer(dopel, 50)) {
							listner.sendPackets(new S_NpcChatPacket(dopel, message, _type), true);
						}
					}
				}
			}
			break;
		default:break;
		}
	}
	
	private static final String[] GAMBLE_PARAM = { "홀", "짝", "1", "2", "3", "4", "5", "6" };
	private static final String[] GAMBLE_PARAM_EN = { "odd", "even", "1", "2", "3", "4", "5", "6" };
	
	boolean isGamble(String msg){
		L1Gambling gam = new L1Gambling();
		if (msg.startsWith(GAMBLE_PARAM[0]) || msg.startsWith(GAMBLE_PARAM_EN[0])) {
			gam.Gambling2(_pc, msg, 1);
			return true;
		}
		if (msg.startsWith(GAMBLE_PARAM[1]) || msg.startsWith(GAMBLE_PARAM_EN[1])) {
			gam.Gambling2(_pc, msg, 2);
			return true;
		}
		if (msg.startsWith(GAMBLE_PARAM[2]) || msg.startsWith(GAMBLE_PARAM_EN[2])) {
			gam.Gambling2(_pc, msg, 3);
			return true;
		}
		if (msg.startsWith(GAMBLE_PARAM[3]) || msg.startsWith(GAMBLE_PARAM_EN[3])) {
			gam.Gambling2(_pc, msg, 4);
			return true;
		}
		if (msg.startsWith(GAMBLE_PARAM[4]) || msg.startsWith(GAMBLE_PARAM_EN[4])) {
			gam.Gambling2(_pc, msg, 5);
			return true;
		}
		if (msg.startsWith(GAMBLE_PARAM[5]) || msg.startsWith(GAMBLE_PARAM_EN[5])) {
			gam.Gambling2(_pc, msg, 6);
			return true;
		}
		if (msg.startsWith(GAMBLE_PARAM[6]) || msg.startsWith(GAMBLE_PARAM_EN[6])) {
			gam.Gambling2(_pc, msg, 7);
			return true;
		}
		if (msg.startsWith(GAMBLE_PARAM[7]) || msg.startsWith(GAMBLE_PARAM_EN[7])) {
			gam.Gambling2(_pc, msg, 8);
			return true;
		}
		return false;
	}
	
	private static final String[] GAMBLE_MONSTER_PARAM = { "오크전사", "스파토이", "멧돼지", "슬라임", "해골", "늑대인간", "버그베어", "장로", "괴물눈" };
	private static final String[] GAMBLE_MONSTER_PARAM_EN = { "Orc Warrior", "Spartoi", "Boar", "Slime", "Skeleton", "Werewolf", "Bugbear", "Elder", "Floating Eye" } ;
	boolean isGamble3(String msg){
		L1Gambling3 gam1 = new L1Gambling3();
		if (msg.startsWith(GAMBLE_MONSTER_PARAM[0]) || msg.startsWith(GAMBLE_MONSTER_PARAM_EN[0])) {
			gam1.Gambling3(_pc, msg, 1);
			return true;
		}
		if (msg.startsWith(GAMBLE_MONSTER_PARAM[1]) || msg.startsWith(GAMBLE_MONSTER_PARAM_EN[1])) {
			gam1.Gambling3(_pc, msg, 2);
			return true;
		}
		if (msg.startsWith(GAMBLE_MONSTER_PARAM[2]) || msg.startsWith(GAMBLE_MONSTER_PARAM_EN[2])) {
			gam1.Gambling3(_pc, msg, 3);
			return true;
		}
		if (msg.startsWith(GAMBLE_MONSTER_PARAM[3]) || msg.startsWith(GAMBLE_MONSTER_PARAM_EN[3])) {
			gam1.Gambling3(_pc, msg, 4);
			return true;
		}
		if (msg.startsWith(GAMBLE_MONSTER_PARAM[4]) || msg.startsWith(GAMBLE_MONSTER_PARAM_EN[4])) {
			gam1.Gambling3(_pc, msg, 5);
			return true;
		}
		if (msg.startsWith(GAMBLE_MONSTER_PARAM[5]) || msg.startsWith(GAMBLE_MONSTER_PARAM_EN[5])) {
			gam1.Gambling3(_pc, msg, 6);
			return true;
		}
		if (msg.startsWith(GAMBLE_MONSTER_PARAM[6]) || msg.startsWith(GAMBLE_MONSTER_PARAM_EN[6])) {
			gam1.Gambling3(_pc, msg, 7);
			return true;
		}
		if (msg.startsWith(GAMBLE_MONSTER_PARAM[7]) || msg.startsWith(GAMBLE_MONSTER_PARAM_EN[7])) {
			gam1.Gambling3(_pc, msg, 8);
			return true;
		}
		if (msg.startsWith(GAMBLE_MONSTER_PARAM[8]) || msg.startsWith(GAMBLE_MONSTER_PARAM_EN[8])) {
			gam1.Gambling3(_pc, msg, 9);
			return true;
		}
		return false;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_Chatting(data, client);
	}

}

