package l1j.server.server.serverpackets.object;

import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class S_CompanionObject extends WorldPutObjectStream{
    private static final String S_COMPANION_OBJECT = "[S] S_CompanionObject";
    private byte[] _byte = null;
    private static final byte[] MIDDLE_BYTES = getMiddleBytes();

    public S_CompanionObject(L1PetInstance pet) {
    	write_init();
    	write_point(pet.getX(), pet.getY());
    	write_objectnumber(pet.getId());
    	write_objectsprite(pet.getSpriteId());
    	write_action(pet.getActionStatus());
    	write_direction(pet.getMoveState().getHeading());
    	write_lightRadius(pet.getLightSize());
    	write_objectcount(1);
    	write_alignment(pet.getAlignment());
    	write_desc(pet.getName());
    	write_title(null);
    	write_speeddata(pet.isHaste() ? 1 : 0);
    	writeByte(MIDDLE_BYTES);
    	write_isparalyzed(pet.getParalysis() != null);
    	write_isuser(false);
    	write_isinvisible(false);
    	write_ispoisoned(pet.getPoison() != null && pet.getPoison().getEffectId() == 1);
    	write_emblemid(0);
    	write_pledgename(null);
    	write_mastername(pet.getMaster() != null ? pet.getMaster().getName() : null);
    	write_altitude(0);
    	write_hitratio(-1);
    	write_safelevel(pet.getLevel());
    	write_weaponsprite(-1);
    	write_couplestate(0);
    	write_boundarylevelindex(0);
    	write_manaratio(-1);
    	write_homeserverno(0);
    	write_speed_value_flag(0);
    	write_npc_class_id(pet.getNpcTemplate().getClassId());
        write_companion(pet);
        writeH(0x00);
    }
    
    private static byte[] getMiddleBytes(){
    	BinaryOutputStream os = null;
    	try {
    		os = new BinaryOutputStream();
    		os.writeC(0x60);// emotion
    		os.writeC(0);
    		
    		os.writeC(0x68);// drunken
    		os.writeC(0);
    		
    		os.writeC(0x70);// isghost
    		os.writeB(false);
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
		return S_COMPANION_OBJECT;
	}

}
