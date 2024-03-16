package l1j.server.server.serverpackets.system;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ShelterOwnerChange extends ServerBasePacket {
	private static final String S_SHELTER_OWNER_CHANGE = "[S] S_ShelterOwnerChange";
	private byte[] _byte = null;
	public static final int CHANGE	= 0x0a71;
	
	public S_ShelterOwnerChange(S_ShelterOwnerChange.eRES result, int need_change_time) {
		write_init();
		write_result(result);
		write_need_change_time(need_change_time);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHANGE);
	}
	
	void write_result(S_ShelterOwnerChange.eRES result) {
		writeRaw(0x08);
		writeRaw(result.value);
	}
	
	void write_need_change_time(int need_change_time) {
		writeRaw(0x10);
		writeBit(need_change_time);
	}
	
	public enum eRES{
		eOK(0),
		eNotOwner(1),			// 아지트를 소유하고 있지 않아 아이템을 사용할 수 없습니다.
		eNotShelterMap(2),		// 본인과 대상이 아지트 맵에 있어야 합니다.
		eNotChangeTime(3),		// 소유하고 일정 시간이 지난 후 양도가 가능합니다.
		eAlreadyChanging(4),	// 이미 다른 유저와 양도를 진행 중입니다.
		eNetworkBusy(5),		// 네트워크가 불안정합니다. 나중에 시도해 주세요.
		eNotTargetShelterMap(6),// 본인과 대상이 아지트 맵에 있어야 합니다.
		eTargetOwner(7),		// 대상이 이미 아지트를 보유하고 있습니다.
		eTargetNeedLevel(8),	// 대상의 레벨이 부족합니다.
		eNotOwnerShelterKey(9),	// 알 수 없는 에러가 발생하였습니다.
		eNotTarget(10),			// 대상을 찾을 수 없습니다.
		eFailShelterKeyItem(11),// 알 수 없는 에러가 발생하였습니다.
		;
		private int value;
		eRES(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eRES v){
			return value == v.value;
		}
		public static eRES fromInt(int i){
			switch(i){
			case 0:
				return eOK;
			case 1:
				return eNotOwner;
			case 2:
				return eNotShelterMap;
			case 3:
				return eNotChangeTime;
			case 4:
				return eAlreadyChanging;
			case 5:
				return eNetworkBusy;
			case 6:
				return eNotTargetShelterMap;
			case 7:
				return eTargetOwner;
			case 8:
				return eTargetNeedLevel;
			case 9:
				return eNotOwnerShelterKey;
			case 10:
				return eNotTarget;
			case 11:
				return eFailShelterKeyItem;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eRES, %d", i));
			}
		}
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_SHELTER_OWNER_CHANGE;
	}
}

