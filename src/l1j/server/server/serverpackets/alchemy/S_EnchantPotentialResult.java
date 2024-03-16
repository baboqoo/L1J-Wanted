package l1j.server.server.serverpackets.alchemy;

import java.util.HashMap;

import l1j.server.common.bin.PotentialCommonBinLoader;
import l1j.server.common.bin.potential.CommonPotentialInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_EnchantPotentialResult extends ServerBasePacket {
	private static final String S_ENCHANT_POTENTIAL_RESULT = "[S] S_EnchantPotentialResult";
	private byte[] _byte = null;
    public static final int RESULT	= 0x094b;
    
    static final HashMap<Integer, byte[]> BONUS_BYTES	= new HashMap<Integer, byte[]>();
    static final byte[] GREATE_SUCCESS_BONUS_LIST_BYTES		= get_greate_bonuslist_bytes();
    
    public S_EnchantPotentialResult(int target_id, int bonus_grade, int bonus_id, int curbonus_desc){
    	write_init();
        write_target_id(target_id);
        write_bonus_grade(bonus_grade);	
        writeByte(bonus_grade == 6 ? GREATE_SUCCESS_BONUS_LIST_BYTES : get_select_bonuslist_bytes(bonus_id));
        if (curbonus_desc > 0) {
        	write_curbonus_desc(curbonus_desc);
        }
        writeH(0x00);
    }
    
    void write_init() {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(RESULT);
    }
    
    void write_target_id(int target_id) {
    	writeRaw(0x08);// target_id
        writeBit(target_id);
    }
    
    void write_bonus_grade(int bonus_grade) {
    	writeRaw(0x10);// bonus_grade 6:대성공
        writeRaw(bonus_grade);
    }
    
    byte[] get_select_bonuslist_bytes(int bonusId){
    	byte[] bytes = BONUS_BYTES.get(bonusId);
    	if (bytes == null) {
    		bytes = create_select_bonuslist_bytes(bonusId);
    		BONUS_BYTES.put(bonusId, bytes);
    	}
    	return bytes;
    }
    
    byte[] create_select_bonuslist_bytes(int bonusId){
    	BinaryOutputStream os				= null;
    	PotentialBonusTStream bonusStream	= null;
		try {
			CommonPotentialInfo.BonusInfoT bonusInfoT = PotentialCommonBinLoader.getBonusInfo(bonusId);
			bonusStream = new PotentialBonusTStream(bonusInfoT);
			
			os = new BinaryOutputStream();
			os.writeC(0x1a);
			os.writeBytesWithLength(bonusStream.getBytes());
			return os.getBytes();
		} catch(Exception e) {
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
			if (bonusStream != null) {
				try {
					bonusStream.close();
					bonusStream = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
    }
    
    /**
     * 대성공 시 출력되는 패킷
     * @return byte[]
     */
    static byte[] get_greate_bonuslist_bytes(){
    	BinaryOutputStream os				= null;
    	PotentialBonusTStream bonusStream	= null;
    	try {
    		os			= new BinaryOutputStream();
    		bonusStream	= new PotentialBonusTStream();
    		for (int i=0; i<10; i++) {
    			int bonusId = 132 + i;
    			CommonPotentialInfo.BonusInfoT bonusInfoT = PotentialCommonBinLoader.getBonusInfo(bonusId);
    			if (bonusInfoT == null) {
    				System.out.println(String.format("[S_EnchantPotentialRestartNoti] CREATE_POTENTIAL_BIN_INFO_EMPTY : BONUS_ID(%d)", bonusId));
    				continue;
    			}
    			if (bonusStream != null) {
    				bonusStream.reset();
    			}
    			bonusStream.write_bonus_grade(6);
    			bonusStream.write_bonus_id(bonusId);
    			bonusStream.write_bonus_desc(bonusInfoT.get_bonus_desc());
    			
				os.writeC(0x1a);
				os.writeBytesWithLength(bonusStream.getBytes());
    		}
    		return os.getBytes();
    	} catch(Exception e) {
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
    		if (bonusStream != null) {
				try {
					bonusStream.close();
					bonusStream = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
    	}
    	return null;
    }
    
    void write_curbonus_desc(int curbonus_desc) {
    	writeRaw(0x20);// curbonus_desc
    	writeBit(curbonus_desc);
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
		return S_ENCHANT_POTENTIAL_RESULT;
	}
}
