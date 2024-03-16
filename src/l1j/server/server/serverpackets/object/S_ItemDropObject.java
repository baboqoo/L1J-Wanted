package l1j.server.server.serverpackets.object;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class S_ItemDropObject extends WorldPutObjectStream {
    private static final String S_ITEM_DROP_OBJECT = "[S] S_ItemDropObject";
    private byte[] _byte = null;
    
    private static final byte[] MIDDLE_BYTES	= getMiddleBytes();
    private static final byte[] TAIL_BYTES		= getTailBytes();
    
    public S_ItemDropObject(L1ItemInstance item) {
    	write_init();
        write_point(item.getX(), item.getY());
        write_objectnumber(item.getId());
        write_objectsprite(item.getItem().getSpriteId());
        writeByte(MIDDLE_BYTES);
        write_lightRadius(item.isNowLighting() ? item.getItem().getLightRange() : 0);
        write_objectcount(item.getCount());
        write_alignment(0);
        write_desc(item.getViewName());
        writeByte(TAIL_BYTES);
        write_drop_info(0, 0);
		writeH(0x00);
    }
    
    static byte[] getMiddleBytes(){
    	BinaryOutputStream os = null;
    	try {
    		os = new BinaryOutputStream();
    		os.writeC(0x20);// action
    		os.writeC(0);
    		
    		os.writeC(0x28);// direction
    		os.writeC(0);
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
    
    static byte[] getTailBytes(){
    	BinaryOutputStream os = null;
    	try {
    		os = new BinaryOutputStream();
    		os.writeC(0x52);// title
    		os.writeC(0);
            
    		os.writeC(0x58);// speeddata
    		os.writeB(false);
            
    		os.writeC(0x60);// emotion
    		os.writeC(0);
            
    		os.writeC(0x68);// drunken
    		os.writeC(0);
            
    		os.writeC(0x70);// isghost
    		os.writeB(false);
            
    		os.writeC(0x78);// isparalyzed
    		os.writeB(false);
            
    		os.writeH(0x0180);// isuser
    		os.writeB(false);
            
    		os.writeH(0x0188);// isinvisible
    		os.writeB(false);
            
    		os.writeH(0x0190);// ispoisoned
    		os.writeB(false);
            
    		os.writeH(0x0198);// emblemid
    		os.writeC(0);
            
    		os.writeH(0x01a2);// pledgename
    		os.writeC(0);
            
    		os.writeH(0x01aa);// mastername
    		os.writeC(0);
            
    		os.writeH(0x01b0);// altitude
    		os.writeC(0);
            
    		os.writeH(0x01b8);// hitratio
    		os.writeByte(MINUS_BYTES);
            
    		os.writeH(0x01c0);// safelevel
    		os.writeC(0);
            
    		os.writeH(0x01d0);// weaponsprite
    		os.writeByte(MINUS_BYTES);
            
    		os.writeH(0x01d8);// couplestate
    		os.writeC(0);
            
    		os.writeH(0x01e0);// boundarylevelindex
    		os.writeC(0);
    		
    		os.writeH(0x01f0);// manaratio
    		os.writeByte(MINUS_BYTES);
    		
    		os.writeH(0x0280);// homeserverno
    		os.writeC(0);	
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
        return S_ITEM_DROP_OBJECT;
    }
}

