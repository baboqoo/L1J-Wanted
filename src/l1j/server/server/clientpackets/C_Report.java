package l1j.server.server.clientpackets;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.inter.L1InterServerFactory;
import l1j.server.IndunSystem.dragonraid.DragonRaidCreator;
import l1j.server.IndunSystem.dragonraid.DragonRaildType;
import l1j.server.IndunSystem.occupy.OccupyHandler;
import l1j.server.IndunSystem.occupy.OccupyManager;
import l1j.server.IndunSystem.occupy.OccupyType;
import l1j.server.LFCSystem.LFC.Creator.LFCCreator;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.TownTeleport;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ReportTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.S_CharacterCreate;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeInfo;
import l1j.server.server.templates.L1BoardPost;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class C_Report extends ClientBasePacket {
	private static final String C_REPORT = "[C] C_Report";

	public static final int REPORT					= 0x00;
	public static final int DRAGON_MENU				= 0x06;
	public static final int MINI_MAP_SEND			= 0x0b;
	public static final int WINDOW_ACTIVE			= 0x0d;
	public static final int PASSWORD_CREATE			= 0x0e;
	public static final int PASSWORD_CHANGE			= 0x10;
	public static final int PASSWORD_AUTH			= 0x11;
	public static final int HTTP					= 0x13;// 파워북
	public static final int CHARNAME_CHANGED		= 0x1a;
	public static final int CLAN_NAME_CHANGE		= 0x1b;
	public static final int CHARACTER_WAREHOUSE_UI	= 0x20;
	public static final int BOOKMARK_SAVE			= 0x22;
	public static final int OTP_AUTH				= 0x25;
	public static final int BOOKMARK_COLOR			= 0x27;
	public static final int BOOKMARK_LOADING_SAVE	= 0x28;
	public static final int CHAR_CREATE				= 0x2b;
	public static final int MONSTER_KILL_RESET		= 0x2c;
	public static final int EMBLEM					= 0x2e;// 문장주시
	public static final int TOWN_TELPORT			= 0x30;// 마을텔레포트
	public static final int FIND_MERCHANT			= 0x31;
	public static final int FAIRY					= 0x37;
	public static final int SHOP_OPEN_COUNT			= 0x39;
	public static final int RESTART_SCREEN			= 0xf3;
	public static final int SHOP_PRICE_SEARCH		= 0xff;
    
	private static final byte[] SECOND_PASSWORD_STR = { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39 };
	GameClient _client;
	L1PcInstance pc;
	public C_Report(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		_client	= client;
		pc		= client.getActiveChar();
		switch(readC()){
		case OTP_AUTH:
			break;
		case CHARACTER_WAREHOUSE_UI:
			readP(1);// 11: 맡기기, 12:찾기
			break;
		case CHARNAME_CHANGED:
			changeCharName();
			break;
		case WINDOW_ACTIVE:
			windowActive();
			break;
		case REPORT:
			report();
			break;
		case SHOP_PRICE_SEARCH:
			searchShopPrice();
			break;
		case FIND_MERCHANT:
			findMerchant();
			break;
		case SHOP_OPEN_COUNT:
			personalShopOpenCount();
			break;
		case PASSWORD_CREATE:
			createSecondPassword();
			break;
		case PASSWORD_CHANGE:
			changeSecondPassword();
		    break;
		case PASSWORD_AUTH:
			authSecondPassword();
			break;
		case CHAR_CREATE:
			createCharacter(abyte0);
			break;
		case BOOKMARK_COLOR:
			bookMarkColor();
			break;
		case BOOKMARK_SAVE:
			bookMarkSave();
			break;
		case BOOKMARK_LOADING_SAVE: //케플리샤의 기억저장 구슬 ->기억의구슬로 저장
			bookMarkItemSave();
			break;
		case RESTART_SCREEN:
			break;
		case MONSTER_KILL_RESET:
			resetMonsterKill();
			break;
		case EMBLEM:
			emblemStatus();
			break;
		case CLAN_NAME_CHANGE:
			changePledgeName();
			break;
		case TOWN_TELPORT:
			townTeleport();
			break;
		case HTTP:
			break;
		case DRAGON_MENU:
			dragonPortalMenu();
			break;
		case MINI_MAP_SEND:
			sendMiniMap();
			break;
		default:
			break;
		}
	}
	
	void changeCharName() {
		String sourceName		= readS();
		String destinationName	= readS();
		if (L1InterServerFactory.contains(sourceName) || _client.isInterServer()) {
			return;
		}
		S_ChangeCharName packet = S_ChangeCharName.doChangeCharName(_client, sourceName, destinationName);
		if (packet != null) {
			_client.sendPacket(packet);
		}
		if (!packet.is_result()) {
			return;
		}
		
		C_LoginToServer login = null;
		try {
			login = new C_LoginToServer(destinationName, _client);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (login != null) {
				login.clear();
				login = null;
			}
		}
	}
	
	void windowActive() {
		if (pc == null) {
			return;
		}
		int on = readC();
		pc.getConfig().window_active_time = on == 0 ? System.currentTimeMillis() + 5000 : -1;
	}
	
	void report() {
		L1PcInstance targetPc = (L1PcInstance) L1World.getInstance().findObject(readD());
		if (targetPc == null) {
			return;
		}
		ReportTable.getInstance().report(pc, targetPc);
	}
	
	void personalShopOpenCount() {
		if (pc.getAccount() == null) {
			return;
		}
		pc.sendPackets(new S_PacketBox(S_PacketBox.SHOP_OPEN_COUNT, pc.getAccount().getShopOpenCount()), true);
	}
	
	void createSecondPassword() {
		if (!Config.SERVER.SECOND_PASSWORD_USE) {
			return;
		}
		if (!StringUtil.isNullOrEmpty(_client.getAccount().get_second_password())) {
			//System.out.println("2차 비번 존재케릭 생성패킷 호출됨! -> " + _client.getAccountName());
			System.out.println("Secondary Password Existence Character Creation Packet Called! -> " + _client.getAccountName());
			_client.kick();
			return;
		}
		int passlength = readC();
		if (passlength < 6 || passlength > 8) {
			_client.sendPacket(new S_CharPass(S_CharPass._PasswordEntryIncorrect));
			return;
		}
		byte[] pw = readByte();
		String password = null;
		for (int i = 0; i < passlength; i++) {
			if (pw[i] > 9) {
				pw[i] = 0;
			}
			pw[i] = SECOND_PASSWORD_STR[pw[i]];
		}
		try {
			password = new String(pw, 0, passlength, CharsetUtil.EUC_KR_STR);
		} catch (Exception e) {
		}

		_client.getAccount().update_second_password(password);
		_client.sendPacket(new S_CharPass(S_CharPass._PasswordCreationCompletionWindow));
	}
	
	void changeSecondPassword() {
		if (StringUtil.isNullOrEmpty(_client.getAccount().get_second_password())) {
			//System.out.println("2차 비번 없는 계정 변경패킷 호출! " + _client.getAccountName());
			System.out.println("Secondary Password-Less Account Modification Packet Called! " + _client.getAccountName());
			_client.kick();
			return;
		}
		int passlength = readC();
		if (passlength < 6 || passlength > 8) {
			_client.sendPacket(new S_CharPass(S_CharPass._PasswordEntryIncorrect));
			return;
		}
		byte[] pw = readByte(passlength);
		readC();
		int npasslength = readC();
		if (npasslength < 6 || npasslength > 8) {
			_client.sendPacket(new S_CharPass(S_CharPass._PasswordEntryIncorrect));
			return;
		}
		byte[] npw			= readByte();
		String password		= null;
		String npassword	= null;
		for (int i = 0; i < passlength; i++) {
			if (pw[i] > 9) {
				pw[i] = 0;
			}
			pw[i] = SECOND_PASSWORD_STR[pw[i]];
		}
		for (int i = 0; i < npasslength; i++) {
			if (npw[i] > 9) {
				npw[i] = 0;
			}
			npw[i] = SECOND_PASSWORD_STR[npw[i]];
		}
		try {
			password	= new String(pw, 0, passlength, CharsetUtil.EUC_KR_STR);
			npassword	= new String(npw, 0, npasslength, CharsetUtil.EUC_KR_STR);
		} catch (Exception e) {
		}
		if (_client.getAccount().get_second_password().equals(password)) {
			_client.getAccount().update_second_password(npassword);
			_client.sendPacket(new S_CharPass(S_CharPass._PasswordChangeResponse, true));
		} else {
			_client.sendPacket(new S_CharPass(S_CharPass._PasswordChangeResponse, false));
		}
	}
	
	void authSecondPassword() {
		if (StringUtil.isNullOrEmpty(_client.getAccount().get_second_password())) {
			//System.out.println("2차 비번 없는 계정 승인 패킷 호출! -> " + _client.getAccountName());
			System.out.println("Secondary Password-Less Account Approval Packet Called! -> " + _client.getAccountName());
			_client.kick();
			return;
		}
		//int passlength = readD();
		int passlength = readC();
		if (passlength < 6 || passlength > 8) {
			_client.sendPacket(new S_CharPass(S_CharPass._PasswordEntryIncorrect));
			return;
		}
		byte[] pw = readByte();
		String password = null;
		for (int i = 0; i < passlength; i++) {
			if (pw[i] > 9) {
				pw[i] = 0;
			}
			pw[i] = SECOND_PASSWORD_STR[pw[i]];
		}
		try {
			password = new String(pw, 0, passlength, CharsetUtil.EUC_KR_STR);
		} catch (Exception e) {
		}
		if (_client.getAccount().get_second_password().equals(password)) {
			_client.getAccount().set_auth_second_password(true);
			if (_client.getAccount().getWaitPacket() != null) {
				_client.packetwaitgo(_client.getAccount().getWaitPacket());
			}
		} else {
			_client.sendPacket(new S_CharPass(S_CharPass._PasswordEntryIncorrect));
		}
	}
	
	void createCharacter(byte[] abyte0) {
		if (Config.SERVER.SECOND_PASSWORD_USE == true) {
			if (!_client.getAccount().is_auth_second_password()) {
				if (StringUtil.isNullOrEmpty(_client.getAccount().get_second_password())) {
					_client.sendPacket(new S_CharPass(S_CharPass._PasswordCreationWindow));
				} else {
					_client.getAccount().setWaitPacket(abyte0);
					_client.sendPacket(new S_CharPass(S_CharPass._PasswordEntryWindow));
				}
			} else {
				_client.sendPacket(new S_CharacterCreate());
			}
		} else {
			_client.sendPacket(new S_CharacterCreate());
		}
	}
	
	void bookMarkColor() {
		int sizeColor = readD();
		int Numid;
		String name;
	    Connection con = null;
		PreparedStatement pstm = null;
		try {
			if (sizeColor != 0) {
				con = L1DatabaseFactory.getInstance().getConnection();
			}
			for (int i = 0; i < sizeColor; i++) {
				Numid = readD();
				int id = 0;
				for (L1BookMark book : pc.getBookMarkArray()) {
					if (book.getNumId() == Numid) {
						id = book.getId();
					}
				}
				name = readS();
				name = name.replace("\\", "\\\\");
				pstm = con.prepareStatement(String.format("UPDATE character_teleport SET name='%s' WHERE id='%d'", name, id));
				pstm.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	void bookMarkSave() {
		readC();
		int num;
		int size = pc.getBookMarkSize();
		for (int i = 0; i < size; i++) {
			num = readC();
			pc.getBookMark().get(i).setTemp_id(num);
		}
		pc._speedbookmarks.clear();
		for (int i = 0; i < 5; i++) {
			num = readC();
			if (num == 255) {
				return;
			}
			if (pc.getBookMark().contains(num)) {
				pc.getBookMark().get(num).setSpeed_id(i);
			}
			pc._speedbookmarks.add(pc.getBookMark().get(num));
		}
	}
	
	void bookMarkItemSave() {
		int itemidd = readD(); // itemid
		int sizez = readH(); // size
	    // 사이즈만큼 readD(); 해서 그 id(현재 북마크id) 받아옴
		if (sizez <= 0) {
			pc.sendPackets(new S_ServerMessage(2963, StringUtil.EmptyString), true);	
			return;
		}
		L1ItemInstance item3 = pc.getInventory().getItem(itemidd);
		pc.getInventory().removeItem(item3, 1);
		if (item3.getItemId() != 700028) {
			return;
		}
		L1ItemInstance item4 = ItemTable.getInstance().createItem(700023);
		item4.setCount(1);
		item4.setIdentified(true);
		item4.setCreaterName(pc.getName());
		L1BookMark.ItemaddBookmarkByTeleport(pc, item4.getId());
		pc.getInventory().storeItem(item4);
		pc.sendPackets(L1ServerMessage.sm2920);// 기억 저장 구슬: 기억 장소 목록 저장 완료
	}
	
	void resetMonsterKill() {
		if (pc == null) {
			return;
		}
		pc.setMonsterKill(0);
	}
	
	void emblemStatus() {
		if (pc.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING) {
			return;
		}
		int emblemStatus = readC();
		L1Clan clan = pc.getClan();
		clan.setEmblemStatus(emblemStatus);
		ClanTable.getInstance().updateClan(clan);
		for (L1PcInstance member : clan.getOnlineClanMember()) {
			member.sendPackets(new S_BloodPledgeInfo(emblemStatus == 1), true);
		}
	}
	
	void changePledgeName() throws UnsupportedEncodingException {
		String oldClanName = readS();
		String newClanName = readS();
		if (StringUtil.isNullOrEmpty(newClanName)) {
			return;
		}
		for (char ac : newClanName.toCharArray()) {
			if (!Character.isLetterOrDigit(ac)) {
				return;
			}
		}
		for (int i = 0; i < newClanName.length(); i++) {
			if (newClanName.charAt(i) == ' ' || newClanName.charAt(i) == 'ㅤ') {
				return;
			}
		}
		int numOfNameBytes = newClanName.getBytes(CharsetUtil.EUC_KR_STR).length;
		
		if (8 < (numOfNameBytes - newClanName.length()) || 16 < numOfNameBytes) {
			return;
		}
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getClanName().toLowerCase().equals(newClanName.toLowerCase())) {
				return;
			}
		}
		L1Clan clan = L1World.getInstance().getClan(oldClanName);
		if (clan != null) {
			ClanTable.getInstance().changeClanName(oldClanName, newClanName);
			clan.setClanName(newClanName);
		}
	}
	
	void sendMiniMap() {
		String targetName = null;
		int mapid = 0, x = 0, y = 0, Mid = 0;
		try {
			targetName	= readS();
			mapid		= readH();
			x			= readH();
			y			= readH();
			Mid			= readH();
		} catch (Exception e) {
			return;
		}
		L1PcInstance target = L1World.getInstance().getPlayer(targetName);
		if (target == null) {
			pc.sendPackets(L1ServerMessage.sm1782);
		} else if (pc == target) {
			pc.sendPackets(L1ServerMessage.sm1785);
		} else {
			target.sendPackets(new S_ServerMessage(1784, pc.getName()), true);
			target.sendPackets(new S_PacketBox(S_PacketBox.MAP_LOC_SEND, pc.getName(), mapid, x, y, Mid), true);
			pc.sendPackets(new S_ServerMessage(1783, target.getName()), true);
		}
	}
	
	void townTeleport(){
		int group	= readH();
		int index	= readH();
		
		if (group == 7) {
			OccupyHandler handler = OccupyManager.getInstance().getHandler(OccupyType.HEINE);
			if (handler == null || (handler.isBossStage() && !handler.isWinnerTeam(pc._occupyTeamType))) {
				return;// 진입 제한
			}
		}
		
		TownTeleport town_teleport = TownTeleport.get_data(group);
		if (town_teleport == null) {
			return;
		}
		if (pc.getMapId() != town_teleport.get_map()) {
			return;
		}
		if (town_teleport.is_safetyZoneOnly() && pc.getRegion() != L1RegionStatus.SAFETY) {
			return;
		}
		
		TownTeleport.Territory territory = town_teleport.get_territory();
		if (pc.getX() < territory.get_points().get(0).get_x() || pc.getX() > territory.get_points().get(1).get_x()
				|| pc.getY() < territory.get_points().get(0).get_y() || pc.getY() > territory.get_points().get(1).get_y()) {
			return;
		}
		
		TownTeleport.TeleportTarget target = town_teleport.get_teleport_target().get(index);
		if (target == null) {
			return;
		}
		TownTeleport.Point point = target.get_point();
		int locx = point.get_x() + (int) (Math.random() * 2);
		int locy = point.get_y() + (int) (Math.random() * 2);
		pc.getTeleport().start(locx, locy, pc.getMapId(), pc.getMoveState().getHeading(), true);
		pc.sendPackets(new S_PacketBox(S_PacketBox.TOWN_TELEPORT, pc), true);
	}
	
	void searchShopPrice() {
		readP(1);
		int shopitemid = ItemTable.getInstance().findItemIdByDescWithoutSpace(readS());
		if (shopitemid == 0) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("아이템 이름을 검색할 수 없습니다. 검색어를 다시 입력해 주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(112), true), true);
			return;
		}
	}
	
	void findMerchant() {
		String paramName = readS();
		if (StringUtil.isNullOrEmpty(paramName)) {
			return;
		}
		try {
			if (paramName.startsWith("LFC-")) {// APPCENTER LFC response 
				appLfcAction(paramName);
				return;
			}
			if (paramName.startsWith("U_")) {
				ingame_init_auth(paramName);
				return;
			}
			shopSearch(paramName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void shopSearch(String name){
		if (pc.getMapId() != 800) {
			pc.sendPackets(S_PacketBox.SHOP_TELEPORT_FAIL);
			return;
		}
		L1PcInstance target = L1World.getInstance().getPlayer(name);
		if (target != null && target.getMapId() == 800 && target.isPrivateShop()) {
			int telX = target.getX() + CommonUtil.random(3) - 1;
			int telY = target.getY() + CommonUtil.random(3) - 1;
			short telMapId = target.getMapId();
			int telHead = pc.calcheading(telX, telY, target.getX(), target.getY());
			pc.getConfig().setFindMerchantId(target.getId());
			pc.getTeleport().start(telX, telY, telMapId, telHead, true);
			pc.sendPackets(S_PacketBox.SHOP_TELEPORT_SUCCESS);
		} else {
			L1NpcShopInstance npc = L1World.getInstance().getShopNpc(name);
			if (npc != null && npc.getMapId() == 800 && npc.getState() == 1) {
				int telX = npc.getX() + CommonUtil.random(3) - 1;
				int telY = npc.getY() + CommonUtil.random(3) - 1;
				short telMapId = npc.getMapId();
				int telHead = pc.calcheading(telX, telY, npc.getX(), npc.getY());
				pc.getConfig().setFindMerchantId(npc.getId());
				pc.getTeleport().start(telX, telY, telMapId, telHead, true);
				pc.sendPackets(S_PacketBox.SHOP_TELEPORT_SUCCESS);
			} else {
				pc.sendPackets(S_PacketBox.SHOP_TELEPORT_FAIL);
			}
		}
	}
	
	void appLfcAction(String name){
		String[] rep = name.split(StringUtil.MinusString);
		if (rep.length >= 2) {
			if (rep[1].equalsIgnoreCase("R")) {
				LFCCreator.registLfc(pc, Integer.parseInt(rep[2]));
			} else {
				LFCCreator.create(L1BoardPost.findByIdLfc(Integer.parseInt(rep[1])), pc);
			}
		}
	}
	
	void ingame_init_auth(String uid) {
		pc.ingame_init_auth_uid = uid;
		Runnable r = () -> {
			if (pc != null) {
				pc.ingame_init_auth_uid = null;
			}
		};
		GeneralThreadPool.getInstance().schedule(r, 3000L);// 3초후 파기
	}
	
	void dragonPortalMenu() {
		int itemId = readD();
		int Dragon_Type = readC();
		int Castle_Id = L1CastleLocation.getCastleIdByArea(pc);
		L1ItemInstance useItem = pc.getInventory().getItem(itemId);
		if (useItem == null) {
			return;
		}
		// 1892 : 이곳에서 드래곤 키를 사용할 수 없습니다. 1729 : 아직은 사용할 수 없습니다. 1413 : 현재 상태에서는 사용할수 없습니다.
		if (Castle_Id != 0) {
			pc.sendPackets(L1ServerMessage.sm1892);
			return;
		}
		if (pc.getMapId() >= 1005 && pc.getMapId() <= 1022 
			|| pc.getMapId() > 6000 && pc.getMapId() < 6999 
			|| pc.getMapId() == 1161) {
			pc.sendPackets(L1ServerMessage.sm1892);
			return;
		}
		DragonRaildType raidType = null;
		switch(Dragon_Type){
		case 0:raidType = DragonRaildType.ANTARAS;break;
		case 1:raidType = DragonRaildType.FAFURION;break;
		case 2:raidType = DragonRaildType.RINDVIOR;break;
		case 3:raidType = DragonRaildType.VALAKAS;break;
		}
		if (DragonRaidCreator.getInstance().getRaidCount(raidType) >= 6) {
			pc.sendPackets(L1SystemMessage.DRAGON_RAID_MAX);
			return;  
		}
		DragonRaidCreator.getInstance().create(pc, raidType);
		L1World.getInstance().broadcastPacketToAll(L1ServerMessage.sm2921);
		pc.getInventory().consumeItem(L1ItemId.DRAGON_KEY, 1);
		pc.sendPackets(L1ServerMessage.sm1729);
	}

	@Override
	public String getType() {
		return C_REPORT;
	}
}

