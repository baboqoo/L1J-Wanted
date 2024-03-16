package l1j.server.server.clientpackets.proto;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.IndunSystem.indun.IndunType;
import l1j.server.common.bin.IndunCommonBinLoader;
import l1j.server.common.bin.indun.IndunInfoForClient;
import l1j.server.common.data.eDistributionType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_IndunChangeRoomStatus;
import l1j.server.server.serverpackets.indun.S_IndunCreateRoom;
import l1j.server.server.serverpackets.indun.S_IndunCreateRoom.ArenaRoomCreateResult;
import l1j.server.server.serverpackets.indun.S_IndunEnterRoom;
import l1j.server.server.serverpackets.indun.S_IndunEnterRoom.ArenaEnterResult;
import l1j.server.server.serverpackets.indun.S_IndunEnterRoomNoti;
import l1j.server.server.serverpackets.indun.S_IndunRoomInfo;
import l1j.server.server.serverpackets.indun.S_IndunRoomInfo.ArenaInfomationResult;
import l1j.server.server.utils.StringUtil;

public class A_IndunEnter extends ProtoHandler {
	protected A_IndunEnter(){}
	private A_IndunEnter(byte[] data, GameClient client) {
		super(data, client);
	}
	
	//private static final byte[] AUTO_ROOM_NAME = "인던 한 판 하시죠~!".getBytes();
	private static final byte[] AUTO_ROOM_NAME = "Let's do one round of the dungeon!".getBytes();	

	private int _room_id;
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		_room_id = readBit();
		IndunInfo info = IndunList.getIndunInfo(_room_id);
		if (info == null) {// 방 존재여부 체크
			if (_pc.getConfig()._indunAutoMatching) {
				speedEnterSetting();
				return;
			}
			_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.FAIL_NOT_FOUND_ROOM, _room_id), true);
			return;
		}
		
		IndunInfoForClient indun = IndunCommonBinLoader.getCommonInfo(info.map_kind.toInt());
		if (indun == null) {
			System.out.println(String.format("[A_IndunEnter] INDUN_BIN_EMPTY : KIND(%d)", info.map_kind));
			return;
		}
		
		L1PcInventory inv = _pc.getInventory();
		L1ItemInstance keyItem = inv.findItemNameId(indun.get_keyItemId());
		if (keyItem == null) {
			keyItem = inv.findItemNameId(indun.get_bmkeyItemId());
		}
		if (keyItem == null) {
			keyItem = inv.findItemNameId(indun.get_eventKeyItemId());
		}
		if (keyItem == null) {
			_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.FAIL_NOT_ENOUGH_KEY, _room_id), true);
			return;
		}
		
		readP(1);
		int passwordlength	= readC();
		String password		= StringUtil.EmptyString;
		if (passwordlength > 0) {
			password = readS(passwordlength);// 비밀번호
		}
		
		// validation
		if (_pc.isPrivateShop() || _pc.isFishing() || _client.isInterServer()) {
			_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.FAIL_INVALID_SERVER, _room_id), true);
			return;
		}
		if (_pc.getAccount().getIndunCount() >= 5) {// 일일 사용횟수 체크
			_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.FAIL_ENTER_LIMIT, _room_id), true);
			return;
		}
		if (info.indunType == IndunType.AURAKIA && inv.checkItem(420121, 1)) {// 정화의 파편 조각 소지
			_pc.sendPackets(L1ServerMessage.sm8598);
			return;
		}
		if (info.getUserInfo(_pc) != null) { // 이미 참여한 상태
			_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.FAIL_ALREADY_ENTERED_PLAYER, _room_id), true);
			return;
		}
		if (IndunList.isIndunInfoPcCheck(_pc)) { // 다른방에 참여한 상태
			_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.FAIL_OTHER_ROOM_ENTERED, _room_id), true);
			return;
		}
		if (info.is_closed && !password.equalsIgnoreCase(info.password)) { // 비밀번호 체크
			_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.FAIL_INVALID_PASSWORD, _room_id), true);
			return;
		}
		if (info.infoUserList.size() >= info.max_player) { // 현재 방인원 체크
			_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.FAIL_FULL, _room_id), true);
			return;
		}
		if (info.min_level > _pc.getLevel()) { // 입장레벨 체크
			_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.FAIL_LEVEL, _room_id), true);
			return;
		}
		if (info.is_playing) { // 진행여부 체크
			_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.FAIL_TO_ALREADY_PLAY, _room_id), true);
			return;
		}
		
		info.setUser(_pc);
		_pc.getTeleport().start(32735, 32864, (short) 13000, _pc.getMoveState().getHeading(), true);// 대기실
		_pc.sendPackets(new S_IndunEnterRoom(ArenaEnterResult.SUCCESS, _room_id), true);
		S_IndunChangeRoomStatus change	= new S_IndunChangeRoomStatus(info);
		S_IndunEnterRoomNoti userinfo	= new S_IndunEnterRoomNoti(_pc, info);
		for (L1PcInstance member : info.getMembers()) {
			member.sendPackets(change);
			member.sendPackets(userinfo);
		}
		change.clear();
		userinfo.clear();
		change = null;
		userinfo = null;
	}
	
	// 빠른 입장
	void speedEnterSetting(){
		IndunInfo infoAuto	= new IndunInfo();
		infoAuto.room_id	= _room_id;
		infoAuto.title		= AUTO_ROOM_NAME;
		infoAuto.map_kind	= _pc.getConfig()._indunAutoMatchingMapKind;
		IndunType indunType = IndunType.getIndunType(infoAuto.map_kind.toInt());
		if (indunType == null) {
			return;
		}
		IndunInfoForClient indun = IndunCommonBinLoader.getCommonInfo(infoAuto.map_kind.toInt());
		if (indun == null) {
			return;
		}
		infoAuto.distribution_type	= eDistributionType.AUTO_DISTRIBUTION;
		infoAuto.min_level			= indun.get_minLevel().getFirst();
		infoAuto.fee				= indun.get_minAdena();
		infoAuto.min_player			= indun.get_minPlayer();
		infoAuto.max_player			= indun.get_maxPlayer();
		infoAuto.is_closed			= false;
		infoAuto.password			= StringUtil.EmptyString;
		infoAuto.chief_id			= _pc.getId();
		infoAuto.indunType			= indunType;
		infoAuto.setUser(_pc);
		IndunList.setIndunInfo(infoAuto.room_id, infoAuto);
		_pc.getConfig()._IndunReady	= true;
		_pc.getTeleport().start(32735, 32864, (short) 13000, _pc.getMoveState().getHeading(), true);// 대기실
		_pc.sendPackets(new S_IndunCreateRoom(ArenaRoomCreateResult.SUCCESS, infoAuto.room_id), true);
		_pc.sendPackets(new S_IndunChangeRoomStatus(infoAuto), true);
		_pc.sendPackets(new S_IndunRoomInfo(ArenaInfomationResult.SUCCESS, infoAuto), true);
		_pc.getConfig()._indunAutoMatching			= false;
		_pc.getConfig()._indunAutoMatchingMapKind	= null;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunEnter(data, client);
	}

}

