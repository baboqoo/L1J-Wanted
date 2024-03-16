package l1j.server.server.serverpackets.command;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.StringUtil;

public class S_WhoUserNoti extends ServerBasePacket {
	private static final String S_WHO_USER_NOTI = "[S] S_WhoUserNoti";
	public static final int WHO_USER = 0x0078;
	
	int get_map_desc(int mapid) {
		switch(mapid){
		case 777:case 778:case 779:// 버땅
			return 0x01a3;
		case 19:case 20:case 21:// 요정 숲 던전
			return 0x02b3;
		case 807:case 808:case 809:case 810:case 811:case 812:case 813:// 글루딘 던전
			return 0x03c3;
		case 30:case 31:case 32:case 33:case 35:case 36:case 37:// 용던
			return 0x02b5;
		default:
			return 0;
		}
	}
	
	int get_count(int map_desc) {
		int count = 0;
		switch(map_desc){
		case 0x01a3:// 버땅
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null) {
					break;
				}
				if (pc.getMapId() >= 777 && pc.getMapId() <= 779) {
					count++;
				}
			}
			return count;
		case 0x02b3:// 요정 숲 던전
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null) {
					break;
				}
				if (pc.getMapId() >= 19 && pc.getMapId() <= 21) {
					count++;
				}
			}
			return count;
		case 0x03c3:// 글루딘 던전
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null) {
					break;
				}
				if (pc.getMapId() >= 807 && pc.getMapId() <= 813) {
					count++;
				}
			}
			return count;
		case 0x02b5:// 용던
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null) {
					break;
				}
				if (pc.getMapId() >= 30 && pc.getMapId() <= 37) {
					count++;
				}
			}
			return count;
		default:
			return 0;
		}
	}
	
	public S_WhoUserNoti(int mapid) {// 변경된 패킷 맵별로 인원수를 체크한다.
		write_init();
		int map_desc = get_map_desc(mapid);
		if (map_desc != 0) {
			int count = get_count(map_desc);
			writeC(0x1a);// hunting_map_info
			writeC(2 + bitLengh(map_desc) + bitLengh(count));
			
			writeC(0x08);// map_desc
			writeBit(map_desc);
			
			writeC(0x10);// count
			writeBit(count);
		}
		
		writeH(0x00);// 버림수
	}
	
	public S_WhoUserNoti(L1PcInstance target, boolean isGm) {
		write_init();
		
		int length = 0;
		byte[] target_name_byte = target.getName().getBytes();
		length += target_name_byte.length + 2;
		
		byte[] align_str_byte = (target.getAlignment() > 500 ? "Lawful" : target.getAlignment() < 0 ? "Chaotic" : "Natural").getBytes();
		length += align_str_byte.length + 2;
		
		length += getBitSize(Config.VERSION.SERVER_NUMBER) + 1;
		
		byte[] title_byte = null;
		if (StringUtil.isNullOrEmpty(target.getTitle())) {
			title_byte = target.getTitle().getBytes();
			length += title_byte.length + 2;
		} else {
			length += 2;
		}
		
		byte[] pledge_byte = null;
		if (target.getClanid() > 0) {
			pledge_byte = target.getClanName().getBytes();
			length += pledge_byte.length + 2;
		} else {
			length += 2;
		}
		
		byte[] account_byte = null;
		if (isGm) {
			account_byte = getAccountBytes(target);
			length += account_byte.length + 2;
		}
		
		writeC(0x0a);// whouserinfo
		writeC(length);
		
		writeC(0x0a);// user name
		writeBytesWithLength(target_name_byte);
		
		writeC(0x12);// align str
		writeBytesWithLength(align_str_byte);
		
		writeC(0x18);// server no
		writeBit(Config.VERSION.SERVER_NUMBER);
		
		writeC(0x22);// title
		if (title_byte == null) {
			writeC(0);
		} else {
			writeBytesWithLength(title_byte);
		}
		
		writeC(0x2a);// pledge
		if (pledge_byte == null) {
			writeC(0);
		} else {
			writeBytesWithLength(pledge_byte);
		}
		
		if (isGm) {
			writeC(0x32);// account info
			writeBytesWithLength(account_byte);
		}
		
		writeH(0x00);// 버림수
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(WHO_USER);
	}
	
	byte[] getAccountBytes(L1PcInstance target) {
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			os.writeC(0x0a);// account name
			os.writeBytesWithLength(target.getAccountName().getBytes());
			
			os.writeC(0x10);// world number
			os.writeBit(target.getMapId());
			
			os.writeC(0x18);// location
			os.writeLongLocationReverse(target.getX(), target.getY());
			
			if (target.getNetConnection() != null) {
				os.writeC(0x22);// ip
				os.writeBytesWithLength(target.getNetConnection().getIp().getBytes());
				
				os.writeC(0x28);// ip kind
				os.writeBit(target.getNetConnection().getIpBigEndian());
			} else {
				os.writeC(0x22);// ip
				os.writeBytesWithLength("0.0.0.0".getBytes());
				
				os.writeC(0x28);// ip kind
				os.writeC(0);
			}
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_WHO_USER_NOTI;
	}
}

