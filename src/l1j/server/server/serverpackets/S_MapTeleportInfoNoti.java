package l1j.server.server.serverpackets;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.BinaryOutputStream;

public class S_MapTeleportInfoNoti extends ServerBasePacket {
    private static final String S_MAP_TELEPORT_INFO_NOTI = "[S] S_MapTeleportInfoNoti";
    private byte[] _byte = null;
    public static final int WINDOW		= 0x0243;
    
    public static final S_MapTeleportInfoNoti SIMPLE_TELEPORT_UI = new S_MapTeleportInfoNoti();
    
    private static final ConcurrentHashMap<Integer, S_MapTeleportInfoNoti> PCKS	= new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, byte[]> MAP_DATAS			= new ConcurrentHashMap<>();
    
    /**
     * 텔레포트 UI 패킷 재사용
     * @param objId
     * @param action
     * @param price
     * @param townId
     * @return S_TelePortUi
     */
    public static S_MapTeleportInfoNoti getTeleportPck(int objId, String[] action, int[] price, int townId){
    	S_MapTeleportInfoNoti pck = PCKS.get(objId);
    	return pck != null ? pck : putPck(objId, action, price, townId);
    }
    
    static S_MapTeleportInfoNoti putPck(int objId, String[] action, int[] price, int townId){
    	S_MapTeleportInfoNoti pck = new S_MapTeleportInfoNoti(objId, action, price, townId);
    	PCKS.put(objId, pck);
    	return pck;
    }

    public S_MapTeleportInfoNoti() {
    	write_init();
		write_npc_id(0);
		writeH(0x00);
    }
    
	public S_MapTeleportInfoNoti(int objid, String[] action, int[] price, int townId) {
		write_init();
		write_npc_id(objid);
		byte[] map_data	= MAP_DATAS.get(townId);
		writeByte(map_data != null ? map_data : put_map_datas(action, price, townId));
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(WINDOW);
	}
	
	void write_npc_id(int npc_id) {
		writeRaw(0x08);// npc_id
		writeBit(npc_id);
	}
	
	byte[] put_map_datas(String[] action_string, int[] count, int townId){
    	BinaryOutputStream os = null;
    	BinaryOutputStream detail = null;
    	try {
    		os = new BinaryOutputStream();
    		for (int i = 0; i < action_string.length; i++) {
    			detail = new BinaryOutputStream();
    			try {
    				int nameid = action_string[i].equalsIgnoreCase("T_pcbang") ? 25754 : 7;
    				int length = bitLengh(count[i]) + bitLengh(nameid) + 4;
    				
    				detail.writeC(0x0a);// action_string
    				detail.writeS2(action_string[i]);
    				
    				detail.writeC(0x12);// condition_items
    				detail.writeC(length);
    				
    				detail.writeC(0x08);// nameid
    				detail.writeBit(nameid);
    				
    				detail.writeC(0x10);// count
    				detail.writeBit(count[i]);
    				
    				detail.writeC(0x18);// deltype
    				detail.writeC(1);
    				
    				os.writeC(0x12);
    				os.writeBytesWithLength(detail.getBytes());
    			} catch (Exception e) {
    				e.printStackTrace();
    			} finally {
    				if (detail != null) {
    					try {
    						detail.close();
    						detail = null;
    					} catch (Exception e) {
    						e.printStackTrace();
    					}
    				}
    			}
    		}
    		MAP_DATAS.put(townId, os.getBytes());
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
        if (_byte == null) {
        	_byte = _bao.toByteArray();
        }
        return _byte;
    }

    @Override
    public String getType() {
        return S_MAP_TELEPORT_INFO_NOTI;
    }
}

