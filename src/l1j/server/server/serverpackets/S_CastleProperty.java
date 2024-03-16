package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.templates.L1Castle;

public class S_CastleProperty extends ServerBasePacket {
	private static final String S_CASTLE_PROPERTY = "[S] S_CastleProperty";
	private byte[] _byte = null;
	public static final int PROPERTY = 0x013e;

	public S_CastleProperty() {
		write_init();
		for (int castleID = 1; castleID <= 7; ++castleID) {
			write_castleInfoList(castleID);
		}
		writeH(0x00);
	}
	
	public S_CastleProperty(int castleID) {
		write_init();
		write_castleInfoList(castleID);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PROPERTY);
	}
	
	void write_castleInfoList(int castleID) {
		byte[] ownerPledge		= null;
		byte[] ownerLord		= null;
		int propertyTotal		= 0;
		L1Castle castle			= CastleTable.getInstance().getCastleTable(castleID);
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getCastleId() == castleID) {
				ownerPledge		= clan.getClanName().getBytes();
				ownerLord		= clan.getLeaderName().getBytes();
				break;
			}
		}
		if (castle != null) {
			propertyTotal = (int)(castle.getPublicMoney() << 1);// 현재 보유중인 세금
		}
		if (propertyTotal < 0) {
			propertyTotal = 0;
		}
		if (ownerPledge == null) {
			ownerPledge = L1CastleLocation.DEFAULT_CASTLE_OWNER_BYTES[castleID - 1];
		}
		int totalLength = 12 + ownerPledge.length + (getBitSize(propertyTotal) << 1);
		if (ownerLord != null) {
			totalLength += ownerLord.length;
		}
		
		writeRaw(0x0a);
		writeBit(totalLength); // 토탈길이
		write_castleID(castleID);
		write_ownerPledge(ownerPledge);
		write_ownerLord(ownerLord);
		write_siegePoint(0);
		write_occupyTime(0);
		write_propertyTotal(propertyTotal);
		write_propertyGet(propertyTotal);
	}
	
	void write_castleID(int castleID) {
		writeRaw(0x08);// castleID
		writeRaw(castleID);
	}
	
	void write_ownerPledge(byte[] ownerPledge) {
		writeRaw(0x12);// ownerPledge
		writeBytesWithLength(ownerPledge);
	}
	
	void write_ownerLord(byte[] ownerLord) {
		writeRaw(0x1a);// ownerLord
		writeBytesWithLength(ownerLord);
	}
	
	void write_siegePoint(int siegePoint) {
		writeRaw(0x20);// siegePoint
		writeBit(siegePoint);
	}
	
	void write_occupyTime(int occupyTime) {
		writeRaw(0x28);// occupyTime
		writeBit(occupyTime);
	}
	
	void write_propertyTotal(int propertyTotal) {
		writeRaw(0x30);// propertyTotal
		writeBit(propertyTotal);
	}
	
	void write_propertyGet(int propertyGet) {
		writeRaw(0x38);// propertyGet
		writeBit(propertyGet);
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
		return S_CASTLE_PROPERTY;
	}

}

