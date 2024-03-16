package l1j.server.server.serverpackets.object;

import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class S_EffectObject extends WorldPutObjectStream {
    private static final String S_EFFECT_OBJECT = "[S] S_EffectObject";
    private byte[] _byte = null;
    private static final byte[] TAIL_BYTES = getTailBytes();
    
    public S_EffectObject(L1EffectInstance effect) {
        write_init();
        write_point(effect.getX(), effect.getY());
        write_objectnumber(effect.getId());
        write_objectsprite(effect.getSpriteId());
        writeByte(TAIL_BYTES);
		writeH(0x00);
    }
    
    private static byte[] getTailBytes(){
    	BinaryOutputStream os = null;
    	try {
    		os = new BinaryOutputStream();
    		os.writeC(0x20);// action
    		os.writeC(0);
    		
    		os.writeC(0x28);// direction
    		os.writeC(0);
    		
    		os.writeC(0x30);// lightRadius
    		os.writeC(0);
    		
    		os.writeC(0x38);// objectcount
    		os.writeC(1);
    		
    		os.writeC(0x40);// alignment
    		os.writeC(0);
    		
    		os.writeC(0x4a);// desc
    		os.writeC(0);
    		
    		os.writeC(0x52);// title
    		os.writeC(0);
            
    		os.writeC(0x58);// speeddata
    		os.writeC(0);
            
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
    		os.writeBit(-1);
            
    		os.writeH(0x01c0);// safelevel
    		os.writeC(0);
            
    		os.writeH(0x01d0);// weaponsprite
    		os.writeBit(-1);
            
    		os.writeH(0x01d8);// couplestate
    		os.writeC(0);
            
    		os.writeH(0x01e0);// boundarylevelindex
    		os.writeC(0);
    		
    		os.writeH(0x01f0);// manaratio
    		os.writeBit(-1);
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
        return S_EFFECT_OBJECT;
    }
}

