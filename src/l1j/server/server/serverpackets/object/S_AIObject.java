package l1j.server.server.serverpackets.object;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class S_AIObject extends WorldPutObjectStream {
	private static final String S_AI_OBJECT = "[S] S_AIObject";
	private byte[] _byte = null;
	private static final byte[] MIDDLE_BYTES = getMiddleBytes();
	
	public S_AIObject(L1AiUserInstance pc) {
		write_init();
		write_point(pc.getX(), pc.getY());
		write_objectnumber(pc.getId());
		write_objectsprite(pc.isDead() ? pc.getTempCharGfxAtDead() : pc.getSpriteId());
		write_action(pc.isDead() ? pc.getActionStatus() : pc.getCurrentWeapon());
		write_direction(pc.getMoveState().getHeading());
		write_lightRadius(pc.getLight().getChaLightSize());
		write_objectcount(1);
		write_alignment(pc.getAlignment());
		write_desc(pc.getName());
		write_title(pc.getTitle());
		write_speeddata(pc.getMoveState().getMoveSpeed());
		write_emotion(pc.getMoveState().getBraveSpeed());
		write_drunken(pc.isDrunken() ? 8 : 0);
		write_isghost(pc.isGhost());
		write_isparalyzed(pc.getParalysis() != null);
		write_isuser(true);
		write_isinvisible(false);
		write_ispoisoned(pc.getPoison() != null);
		write_emblemid(pc.getClanid() > 0 ? pc.getClan().getEmblemId() : 0);
		write_pledgename(pc.getClanName());
		writeByte(MIDDLE_BYTES);
		write_boundarylevelindex(pc.getBoundaryLevelIndex());
		write_manaratio(-1);
		write_homeserverno(Config.VERSION.SERVER_NUMBER);
		write_user_game_class(pc.getType());
		write_forth_gear(pc.isFourthGear());
		write_speed_bonus(pc);
		write_is_apc();
		write_is_excavated(false);
		write_apc_pledge_icon(pc.getAiPledge().getApcPledgeIcon());
		write_above_head_mark_id(0);
		write_invisible_level(0);
		writeH(0x00);
	}
	
	private static byte[] getMiddleBytes(){
    	BinaryOutputStream os = null;
    	try {
    		os = new BinaryOutputStream();
    		os.writeH(0x01aa);// mastername
    		os.writeC(0);
    		
    		os.writeH(0x01b0);// altitude
    		os.writeC(0);
        	
    		os.writeH(0x01b8);// hitratio
    		os.writeByte(MINUS_BYTES);
        	
    		os.writeH(0x01c0);// safelevel
    		os.writeC(0);
            
    		os.writeH(0x01ca);// shoptitle
    		os.writeBit(0);
        	
    		os.writeH(0x01d0);// weaponsprite
    		os.writeByte(MINUS_BYTES);
            
    		os.writeH(0x01d8);// couplestate
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
			_byte = getBytes();
		}
		return _byte;
	}
	
	@Override
	public String getType() {
		return S_AI_OBJECT;
	}

}
