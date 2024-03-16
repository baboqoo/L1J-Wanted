package l1j.server.server.serverpackets.pledge;

import java.io.IOException;
import java.util.LinkedList;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1PledgeJoinningRequest;
import l1j.server.server.utils.BinaryOutputStream;

public class S_BloodPledgeJoinningList extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_JOINNING_LIST = "[S] S_BloodPledgeJoinningList";
	private byte[] _byte = null;
	public static final int JOINING_LIST	= 0x0145;
	
	public S_BloodPledgeJoinningList(L1PcInstance pc, ePLEDGE_JOINING_LIST_TYPE type) {
		write_init();
		write_req_type(type);
		switch (type) {
		case ePLEDGE_JOINING_LIST_TYPE_USER:
			joinningRequestUser(pc);
			break;
		case ePLEDGE_JOINING_LIST_TYPE_PLEDGE:
			joinningRequestPledge(pc.getClan());
			break;
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(JOINING_LIST);
	}
	
	void write_req_type(ePLEDGE_JOINING_LIST_TYPE req_type) {
		writeRaw(0x08);// req_type
		writeRaw(req_type.toInt());
	}
	
	void joinningRequestUser(L1PcInstance pc) {
		for (L1Clan clans : L1World.getInstance().getAllClans()) {
			if (clans != null && !clans.isBot()) {
				LinkedList<L1PledgeJoinningRequest> joinningList = clans.getJoinningList();
				if (joinningList == null || joinningList.isEmpty()) {
					continue;
				}
				for (L1PledgeJoinningRequest request : joinningList) {
					if (request.getUser_uid() != pc.getId()) {
						continue;
					}
					writeRaw(0x12);// data
					writeBytesWithLength(getData(request));
					break;
				}
			}
		}
	}
	
	void joinningRequestPledge(L1Clan clan) {
		LinkedList<L1PledgeJoinningRequest> joinningList = clan.getJoinningList();
		if (joinningList == null || joinningList.isEmpty()) {
			return;
		}
		for (L1PledgeJoinningRequest request : joinningList) {
			writeRaw(0x12);// data
			writeBytesWithLength(getData(request));
		}
	}
	
	byte[] getData(L1PledgeJoinningRequest request) {
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			
			os.writeC(0x08);// pledge_uid
			os.writeBit(request.getPledge_uid());
			
			os.writeC(0x12);// pledge_name
			os.writeBytesWithLength(request.getPledge_name().getBytes());
			
			os.writeC(0x18);// user_uid
			os.writeBit(request.getUser_uid());
			
			os.writeC(0x22);// user_name
			os.writeBytesWithLength(request.getUser_name().getBytes());
			
			os.writeC(0x2A);// join_msg
			os.writeStringWithLength(request.getJoin_message());

			os.writeC(0x30);// online
			os.writeB(L1World.getInstance().getPlayer(request.getUser_name()) != null);// 접속중
			
			os.writeC(0x38);// char_class
			os.writeC(request.getClass_type());
			
			os.writeC(0x40);
			os.writeBit(request.getJoin_date());
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
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
		return S_BLOODPLEDGE_JOINNING_LIST;
	}
}

