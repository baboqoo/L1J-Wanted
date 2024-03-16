package l1j.server.server.model.monitor;

import l1j.server.Config;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Alignment;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;

public class L1PcExpMonitor extends L1PcMonitor {
	private int _old_align;
	private long _old_exp;
	private L1RegionStatus _old_region;

	public L1PcExpMonitor(L1PcInstance owner) {
		super(owner);
	}

	@Override
	public void execTask() {
		if (_old_align != _owner.getAlignment()) {// 성향치 변화
			_old_align = _owner.getAlignment();
			changeAlignment();
		}
		if (_old_exp != _owner.getExp()) {// 경험치 변화
			_old_exp = _owner.getExp();
			_owner.onChangeExp();
		}
		L1RegionStatus region = _owner.getRegion();
    	if (_old_region != region) {// 지역 변화
    		_old_region = region;
    		changeRegion();
    	}
	}
	
	void changeAlignment() {
		_owner.broadcastPacketWithMe(new S_Alignment(_owner.getId(), _old_align), true);
		if (Config.ALT.BAPHOMET_SYSTEM_BONUS) {
			baphometSystemBonus();
		}
	}
	
	void changeRegion() {
		_owner.sendPackets(_old_region.getDeathPenaltyStatusPck());
		_owner.getSkill().expPotionCheck();// 경험치 물약 on off
	}
	
	void baphometSystemBonus() {
		int ACvalue = 0, MRvalue = 0, SPvalue = 0, ATvalue = 0, bapo = 0;
		L1CharacterConfig config = _owner.getConfig();
		int align = _owner.getAlignment();
		if (align >= 30000 && align <= 32768) {
			ACvalue = -6;
			MRvalue = 9;
			config.setOBapoLevel(config.getNBapoLevel());
			bapo = 2;
			config.setNBapoLevel(bapo);
		} else if (align >= 20000 && align <= 29999) {
			ACvalue = -4;
			MRvalue = 6;
			config.setOBapoLevel(config.getNBapoLevel());
			bapo = 1;
			config.setNBapoLevel(bapo);
		} else if (align >= 10000 && align <= 19999) {
			ACvalue = -2;
			MRvalue = 3;
			config.setOBapoLevel(config.getNBapoLevel());
			bapo = 0;
			config.setNBapoLevel(bapo);
		} else if (align >= -9999 && align <= 9999) {
			SPvalue = 0;
			ATvalue = 0;
			ACvalue = 0;
			MRvalue = 0;
			config.setOBapoLevel(config.getNBapoLevel());
			bapo = 7;
			config.setNBapoLevel(bapo);
		} else if (align <= -10000 && align >= -19999) {
			SPvalue = 1;
			ATvalue = 1;
			config.setOBapoLevel(config.getNBapoLevel());
			bapo = 3;
			config.setNBapoLevel(bapo);
		} else if (align <= -20000 && align >= -29999) {
			SPvalue = 2;
			ATvalue = 3;
			config.setOBapoLevel(config.getNBapoLevel());
			bapo = 4;
			config.setNBapoLevel(bapo);
		} else if (align <= -30000 && align >= -32768) {
			SPvalue = 3;
			ATvalue = 5;
			config.setOBapoLevel(config.getNBapoLevel());
			bapo = 5;
			config.setNBapoLevel(bapo);
		}
		
		if (config.getOBapoLevel() != config.getNBapoLevel()) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BAPO_BUFF, config.getOBapoLevel(), false), true);
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BAPO_BUFF, config.getNBapoLevel(), true), true);
			config.setOBapoLevel(config.getNBapoLevel());
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BAPO_BUFF, 6, _owner.getLevel() < Config.ALT.BAPHOMET_SYSTEM_LEVEL), true);
		}
		
		if (ACvalue != 0 && MRvalue != 0) {
			if (ACvalue != config.alignAC) {
				if (config.alignAC != 0) {
					_owner.getAC().addAc(config.alignAC * -1);
				}
				config.alignAC = ACvalue;
				_owner.getAC().addAc(ACvalue);
			}
			if (MRvalue != config.alignMR) {
				if (config.alignMR != 0) {
					_owner.getResistance().addMr(config.alignMR * -1);
				}
				config.alignMR = MRvalue;
				_owner.getResistance().addMr(MRvalue);
			}
			if (SPvalue != config.alignSP){
				if (config.alignSP != 0) {
					_owner.getAbility().addSp(config.alignSP * -1);
				}
				config.alignSP = SPvalue;
				_owner.getAbility().addSp(SPvalue);
			}
		/*	if (pc.LawfulSP != 0) {
				pc.addSp(pc.LawfulSP * 1);
				pc.LawfulSP = 0;
			}*/
		} else {
			if (config.alignAC != 0) {
				_owner.getAC().addAc(config.alignAC * -1);
				config.alignAC = 0;
			}
			if (config.alignMR != 0) {
				_owner.getResistance().addMr(config.alignMR * -1);
				config.alignMR = 0;
			}
			
			if (ATvalue != 0){
				if (config.alignAT != 0) {
					config.setBapodmg(config.alignAT * -1);
				}
				config.alignAT = ATvalue;
				config.setBapodmg(ATvalue);
				_owner.sendPackets(new S_OwnCharStatus(_owner), true);
			} else if (config.alignAT != 0) {
				config.setBapodmg(config.alignAT * -1);
				config.alignAT = 0;
				_owner.sendPackets(new S_OwnCharStatus(_owner), true);
			}
			
			if (SPvalue != 0) {
				if (config.alignSP != 0) {
					_owner.getAbility().addSp(config.alignSP * -1);
				}
				config.alignSP = SPvalue;
				_owner.getAbility().addSp(SPvalue);
			} else if (config.alignSP != 0) {
				_owner.getAbility().addSp(config.alignSP * -1);
				config.alignSP = 0;
			}
		}
	}
	
}

