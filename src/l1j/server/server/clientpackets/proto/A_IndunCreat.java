package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.IndunSystem.indun.IndunType;
import l1j.server.common.bin.IndunCommonBinLoader;
import l1j.server.common.bin.indun.IndunInfoForClient;
import l1j.server.common.data.eArenaMapKind;
import l1j.server.common.data.eDistributionType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.indun.S_IndunChangeRoomStatus;
import l1j.server.server.serverpackets.indun.S_IndunCreateRoom;
import l1j.server.server.serverpackets.indun.S_IndunCreateRoom.ArenaRoomCreateResult;
import l1j.server.server.serverpackets.indun.S_IndunRoomInfo;
import l1j.server.server.serverpackets.indun.S_IndunRoomInfo.ArenaInfomationResult;
import l1j.server.server.utils.StringUtil;

public class A_IndunCreat extends ProtoHandler {
	protected A_IndunCreat(){}
	private A_IndunCreat(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private byte[] _title;
	private int _min_level;
	private int _fee;
	private int _key_item_id;
	private eDistributionType _distribution_type;
	private boolean _closed;
	private int _max_player;
	private String _password = StringUtil.EmptyString;
	private eArenaMapKind _map_kind;
	private int _server_no;
	private int _bm_key_item_id;
	private int _event_key_item_id;
	
	void parse() {
		if (_total_length < 2) {
			return;
		}
		
		// IndunBuildInfo
		int build_tag = readC();
		if (build_tag != 0x0a) {
			return;
		}
		int length = readC();
		if (length < 1) {
			return;
		}
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x0a:
				int titlelength		= readC();
				_title				= readByte(titlelength); // 방 이름
				break;
			case 0x10:
				_min_level			= readC(); // 입장 레벨
				break;
			case 0x18:
				_fee				= readBit(); // 입장 아데나
				break;
			case 0x20:
				_key_item_id		= readBit();
				break;
			case 0x28:
				_distribution_type	= eDistributionType.fromInt(readC()); // 분배타입
				break;
			case 0x30:
				_closed				= readBool(); // 비번설정유무
				break;
			case 0x38:
				_max_player			= readC(); // 유저수
				break;
			case 0x42:
				int passwordlength	= readC();
				_password			= readS(passwordlength); // 비밀번호
				break;
			case 0x48:
				_map_kind			= eArenaMapKind.fromInt(readBit());
				break;
			case 0x50:
				_server_no			= readBit();
				break;
			case 0x58:
				_bm_key_item_id		= readBit();
				break;
			case 0x60:
				_event_key_item_id	= readBit();
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation() {
		if (_server_no > 0 && _server_no != Config.VERSION.SERVER_NUMBER) {
			return false;
		}
		return _title != null && _min_level > 0 && _fee > 0 && _distribution_type != null 
				&& _max_player > 0 && _map_kind != null;
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (_pc.isPrivateShop() || _pc.isFishing() || _client.isInterServer()) {
			_pc.sendPackets(S_IndunCreateRoom.INDUN_CREATE_FAIL);
			return;
		}
		if (_pc.getAccount().getIndunCount() >= 5 && !_pc.isGm()) { // 플레이 가능횟수 체크
			_pc.sendPackets(S_IndunCreateRoom.INDUN_PLAY_MAX);
			return;
		}
		parse();
		if (!isValidation()) {
			_pc.sendPackets(S_IndunCreateRoom.INDUN_CREATE_FAIL);
		}
		
		IndunType indunType	= IndunType.getIndunType(_map_kind.toInt());
		IndunInfoForClient indun = IndunCommonBinLoader.getCommonInfo(_map_kind.toInt());
		if (indun == null) {
			System.out.println(String.format("[A_IndunCreat] INDUN_BIN_EMPTY : KIND(%d)", _map_kind.toInt()));
			return;
		}
		if (indun.get_minLevel().getFirst() > _pc.getLevel()) {
			_pc.sendPackets(S_IndunCreateRoom.INDUN_CREATE_FAIL);
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
			_pc.sendPackets(S_IndunCreateRoom.INDUN_KEY_EMPTY);
			return;
		}
		if (_key_item_id > 0 && inv.findItemNameId(_key_item_id) == null) {
			_pc.sendPackets(S_IndunCreateRoom.INDUN_KEY_EMPTY);
			return;
		}
		if (_bm_key_item_id > 0 && inv.findItemNameId(_bm_key_item_id) == null) {
			_pc.sendPackets(S_IndunCreateRoom.INDUN_KEY_EMPTY);
			return;
		}
		if (_event_key_item_id > 0 && inv.findItemNameId(_event_key_item_id) == null) {
			_pc.sendPackets(S_IndunCreateRoom.INDUN_KEY_EMPTY);
			return;
		}
		if (_map_kind == eArenaMapKind.Aurakia_Purification && inv.checkItem(420121, 1)) {// 정화의 파편 조각 소지
			_pc.sendPackets(L1ServerMessage.sm8598);
			return;
		}
		if (_title.length > 20) { // 방이름 길이 체크
			_pc.sendPackets(S_IndunCreateRoom.INDUN_NAME_FAIL);
			return;
		}
		if (_fee > indun.get_maxAdena() || _fee < indun.get_minAdena()) { // 아데나 설정 체크
			_pc.sendPackets(S_IndunCreateRoom.INDUN_ADENA_FAIL);
			return;
		}
		if (IndunList.getIndunInfoList().size() >= IndunList.MAX_ROOM_COUNT) { // 최대 진행중인 맵체크
			_pc.sendPackets(S_IndunCreateRoom.INDUN_ROOM_MAX);
			return;
		}
		
		IndunInfo info			= new IndunInfo();
		info.room_id			= _pc.getConfig()._indunAutoMatching ? _pc.getConfig()._indunAutoMatchingNumber : IndunList.create_room_id();
		info.title				= _title;
		info.map_kind			= _map_kind;
		info.distribution_type	= _distribution_type;
		info.min_level			= _min_level;
		info.fee				= _fee;
		info.min_player			= indun.get_minPlayer();
		info.max_player			= _max_player;
		info.is_closed			= _closed;
		info.password			= _password;
		info.chief_id			= _pc.getId();
		info.indunType			= indunType;
		info.setUser(_pc);
		IndunList.setIndunInfo(info.room_id, info);
		_pc.getConfig()._IndunReady	= true;
		_pc.getTeleport().start(32735, 32864, (short) 13000, _pc.getMoveState().getHeading(), true);// 대기실
		_pc.sendPackets(new S_IndunCreateRoom(ArenaRoomCreateResult.SUCCESS, info.room_id), true);
		_pc.sendPackets(new S_IndunChangeRoomStatus(info), true);
		_pc.sendPackets(new S_IndunRoomInfo(ArenaInfomationResult.SUCCESS, info), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunCreat(data, client);
	}

}

