package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BlessOfBloodPledgeUpdateNoti extends ServerBasePacket {
    private static final String S_BLESS_OF_BLOOD_PLEDGE_UPDATE_NOTI = "[S] S_BlessOfBloodPledgeUpdateNoti";
    private byte[] _byte = null;
    public static final int NOTI = 0x03fb;

	public S_BlessOfBloodPledgeUpdateNoti(L1Clan clan) {
		write_init();
		if (clan.getBuffFirst() != 0) {
			write_world(clan.getBuffFirst(), 
					clan.getEinhasadBlessBuff() == 0 ? WorldState.Idle 
					: clan.getEinhasadBlessBuff() == clan.getBuffFirst() ? WorldState.Selected 
					: WorldState.Unselected);
		}
		if (clan.getBuffSecond() != 0) {
			write_world(clan.getBuffSecond(),
					clan.getEinhasadBlessBuff() == 0 ? WorldState.Idle 
					: clan.getEinhasadBlessBuff() == clan.getBuffSecond() ? WorldState.Selected 
					: WorldState.Unselected);
		}
		if (clan.getBuffThird() != 0) {
			write_world(clan.getBuffThird(),
					clan.getEinhasadBlessBuff() == 0 ? WorldState.Idle 
					: clan.getEinhasadBlessBuff() == clan.getBuffThird() ? WorldState.Selected 
					: WorldState.Unselected);
		}
		write_can_shuffle(true);// 전체변경 활성화
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_world(int world_id, WorldState state) {
		writeC(0x0a);// world_candidate
		writeC(0x05);// 사이즈
		
		writeC(0x08);// world_id
		writeBit(world_id);
		
		writeC(0x10);// world_state
		writeC(state.value);
	}
	
	void write_can_shuffle(boolean can_shuffle) {
		writeC(0x10);// can_shuffle
		writeB(can_shuffle);
	}
	
	public enum WorldState{
		Idle(1),
		Selected(2),
		Unselected(3),
		;
		private int value;
		WorldState(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(WorldState v){
			return value == v.value;
		}
		public static WorldState fromInt(int i){
			switch(i){
			case 1:
				return Idle;
			case 2:
				return Selected;
			case 3:
				return Unselected;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eState, %d", i));
			}
		}
	}
    
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

    @Override
    public String getType() {
        return S_BLESS_OF_BLOOD_PLEDGE_UPDATE_NOTI;
    }
}

