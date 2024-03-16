package l1j.server.server.clientpackets.proto;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.IndunSystem.indun.IndunType;
import l1j.server.common.bin.IndunCommonBinLoader;
import l1j.server.common.bin.indun.IndunInfoForClient;
import l1j.server.common.data.eArenaMapKind;
import l1j.server.common.data.eDistributionType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_IndunChangeRoomStatus;
import l1j.server.server.serverpackets.indun.S_IndunCreateRoom;
import l1j.server.server.serverpackets.indun.S_IndunRoomInfo;
import l1j.server.server.serverpackets.indun.S_IndunRoomInfo.ArenaInfomationResult;
import l1j.server.server.utils.StringUtil;

public class A_IndunChange extends ProtoHandler {
	protected A_IndunChange(){}
	private A_IndunChange(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _room_id;
	private byte[] _title;
	private int _min_level;
	private boolean _closed;
	private String _password = StringUtil.EmptyString;
	private eDistributionType _distribution_type;
	private int _max_player;
	private eArenaMapKind _map_kind;
	
	void parse() {
		if (_total_length < 2) {
			return;
		}
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_room_id 			= readBit();// 방 번호
				break;
			case 0x12:
				int titlelength		= readC();
				_title				= readByte(titlelength);// 방 이름
				break;
			case 0x18:
				_min_level			= readC();// 입장 레벨
				break;
			case 0x20:
				_closed				= readBool();// 비번설정유무
				break;
			case 0x2a:
				int passwordlength	= readC();
				_password			= readS(passwordlength);// 비밀번호
				break;
			case 0x30:
				_distribution_type	= eDistributionType.fromInt(readC());// 분배타입
				break;
			case 0x38:
				_max_player			= readC(); // 유저수
				break;
			case 0x40:
				_map_kind			= eArenaMapKind.fromInt(readBit()); // 방타입
				break;
			default:
				return;
			}
		}
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		
		IndunType indunType = IndunType.getIndunType(_map_kind.toInt());
		if (_title.length > 20) { // 방이름 길이 체크
			_pc.sendPackets(S_IndunCreateRoom.INDUN_NAME_FAIL);
			return;
		}
		IndunInfoForClient indun = IndunCommonBinLoader.getCommonInfo(_map_kind.toInt());
		if (indun == null) {
			System.out.println(String.format("[A_IndunChange] INDUN_BIN_EMPTY : KIND(%d)", _map_kind.toInt()));
			return;
		}
		if (indun.get_minLevel().getFirst() > _pc.getLevel()) {
			_pc.sendPackets(S_IndunCreateRoom.INDUN_CREATE_FAIL);
			return;
		}
		if (_map_kind == eArenaMapKind.Aurakia_Purification && _pc.getInventory().checkItem(420121, 1)) {// 정화의 파편 조각 소지
			_pc.sendPackets(L1ServerMessage.sm8598);
			return;
		}
		
		IndunInfo info			= IndunList.getIndunInfo(_room_id);
		info.title				= _title;
		info.map_kind			= _map_kind;
		info.distribution_type	= _distribution_type;
		info.min_level			= _min_level;
		info.max_player			= _max_player;
		info.is_closed			= _closed;
		info.password			= _password;
		info.indunType			= indunType;
		S_IndunChangeRoomStatus change	= new S_IndunChangeRoomStatus(info);
		S_IndunRoomInfo roominfo		= new S_IndunRoomInfo(ArenaInfomationResult.SUCCESS, info);
		for (L1PcInstance member : info.getMembers()) {
			member.sendPackets(change);
			member.sendPackets(roominfo);
		}
		change.clear();
		roominfo.clear();
		change = null;
		roominfo = null;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunChange(data, client);
	}

}

