package l1j.server.server.serverpackets.object;

import l1j.server.server.model.Instance.L1SummonInstance;

public class S_SummonObject extends WorldPutObjectStream{
    private static final String S_SUMMON_OBJECT = "[S] S_SummonObject";
    private byte[] _byte = null;
    
    public S_SummonObject(L1SummonInstance cha) {
		buildPacket(cha, true);
	}

	public S_SummonObject(L1SummonInstance cha, boolean isCheckMaster) {
		buildPacket(cha, isCheckMaster);
	}

	private void buildPacket(L1SummonInstance cha, boolean isCheckMaster) {
		write_init();
    	write_point(cha.getX(), cha.getY());
    	write_objectnumber(cha.getId());
        write_objectsprite(cha.getSpriteId()); 
        write_action(cha.getActionStatus());
        write_direction(cha.getMoveState().getHeading());
        write_lightRadius(cha.getLightSize());
        write_objectcount(1);
        write_alignment(cha.getAlignment());
        write_desc(cha.getName());
        write_title(null);
        write_speeddata(cha.isHaste() ? 1 : 0);
        write_emotion(0);
        write_drunken(0);
        write_isghost(false);
        write_isparalyzed(cha.getParalysis() != null);
        write_isuser(false);
		write_isinvisible(false);
		write_ispoisoned(cha.getPoison() != null && cha.getPoison().getEffectId() == 1);
        write_emblemid(0);
		write_pledgename(null);
        write_mastername(isCheckMaster && cha.isExsistMaster() ? cha.getMaster().getName() : null);
        write_altitude(0);
        write_hitratio(-1);
        write_safelevel(cha.getLevel());
        write_weaponsprite(-1);
        write_couplestate(0);
        write_boundarylevelindex(0);
		write_manaratio(-1);
        write_homeserverno(0);
		write_dialog_radius(9);
        write_speed_value_flag(0);
		write_npc_class_id(cha.getNpcTemplate().getClassId());
		write_potential_grade(0);
        writeH(0x00);
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
		return S_SUMMON_OBJECT;
	}

}
