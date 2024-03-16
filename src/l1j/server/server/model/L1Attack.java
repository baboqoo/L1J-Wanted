package l1j.server.server.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import l1j.server.Config;
import l1j.server.common.data.Material;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.L1Attr;
import l1j.server.server.construct.L1Grade;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.L1Undead;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.controller.action.GameTimeNight;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.BalanceTable;
import l1j.server.server.datatables.MapBalanceTable.BalanceType;
import l1j.server.server.datatables.MapBalanceTable.MapBalanceData;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1DoppelgangerInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1ScarecrowAttackerInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.potential.L1Potential;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.poison.L1PoisonType;
import l1j.server.server.model.poison.L1SilencePoison;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1PassiveSkillHandler.TitanType;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.sprite.L1Sprite;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.action.S_AttackCritical;
import l1j.server.server.serverpackets.action.S_AttackKeyringkPacket;
import l1j.server.server.serverpackets.action.S_AttackLancerPacket;
import l1j.server.server.serverpackets.action.S_AttackMissPacket;
import l1j.server.server.serverpackets.action.S_AttackPacket;
import l1j.server.server.serverpackets.action.S_AttackPacketForNpc;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.action.S_UseArrowSkill;
import l1j.server.server.serverpackets.action.S_UseAttackSkill;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcDexStat;
import l1j.server.server.utils.CalcIntelStat;
import l1j.server.server.utils.CalcStrStat;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.StringUtil;

public class L1Attack {
	private static final Random random = new Random(System.nanoTime());
	
	// 카오틱 성향치 대미지 증가 무기
	private static final List<Integer> CAOTIC_DAMAGE_WEAPONE	= Arrays.asList(new Integer[] {
			202002, 203002, 1136, 1137, 213, 217
	});
	
	// 파인 사이트 발동 무기 종류
	private static final List<L1ItemWeaponType> FINE_SITE_WEAPONE_TYPE	= Arrays.asList(new L1ItemWeaponType[] {
			L1ItemWeaponType.SWORD,
			L1ItemWeaponType.DAGGER,
			L1ItemWeaponType.TOHAND_SWORD,
			L1ItemWeaponType.BOW,
			L1ItemWeaponType.SINGLE_BOW
	});
	
	// 무형 화살 활
	private static final List<Integer> ARROW_FREE_BOW	= Arrays.asList(new Integer[] {
			190, 202011
	});
	
	// 공격 불가 엔피씨 번호
	private static final List<Integer> NOT_ENABLE_NPC_IDS = Arrays.asList(new Integer[] {
			7800000, 7800007, 7800064, 7800056, 7800300
    });
	
	// 드래곤 종류 엔피씨 번호
	private static final List<Integer> DRAGON_NPC_IDS = Arrays.asList(new Integer[] {
    		7311162, 900013, 900040, 5100, 7310039, 7310040, 7311063
    });
	
    private L1PcInstance _pc;
    private L1Character _target;
    private L1PcInstance _targetPc;
    private L1NpcInstance _npc;
    private L1NpcInstance _targetNpc;

    private int _targetId;
    private int _targetX, _targetY;
    private int _statusDamage;
    private int _statusCritical;
    private int _hitRate;
    private int _weaponDoubleChance;
    
    private int _calcType;
    private static final int PC_PC		= 1;
    private static final int PC_NPC		= 2;
    private static final int NPC_PC		= 3;
    private static final int NPC_NPC	= 4;

    public boolean _isHit, _isCritical, _petCombo, _doubleBurning;
    private int _damage, _drainMana, _drainHp;
    private L1WeaponSkill _weaponSkill;

    private L1ItemInstance weapon, _arrow, _sting;
    private L1ItemWeaponType _weaponType;
    private int _weaponId, _weaponAddDmg, _weaponSmall, _weaponLarge, _weaponBless = 1, _weaponEnchant, _weaponAttrLevel;
    private Material _weaponMaterial;
    private boolean _isLongWeapon, _isRange2Weapon, _isBow, _isGauntlet, _isKeyringk, _isStaff, _isEdoryu, _isClaw, _isBlunt, _isChainsword;
    
    private boolean _isLongAttackNpc;
    
    private int _leverage = 10; // 1/10배로 표현한다.
    public void setLeverage(int i) {
    	_leverage = i;
    }

    private int _attackType, _attckGrfxId, _attckActId;
    
    public void setActId(int actId) {
    	_attckActId = actId;
    }
    public int getActId() {
    	return _attckActId;
    }

    public void setGfxId(int gfxId) {
    	_attckGrfxId = gfxId;
    }
    public int getGfxId() {
    	return _attckGrfxId;
    }
    
    /**
     * 화살 설정
     */
    void setArrow() {
    	_pc.getInventory().searchArrow();
        _arrow = _pc.getInventory().getArrow();
        if (_arrow != null) {
            _weaponBless	= _arrow.getItem().getBless();
            _weaponMaterial	= _arrow.getItem().getMaterial();
        }
    }
    
    /**
     * 스팅 설정
     */
    void setSting() {
    	_pc.getInventory().searchSting();
        _sting = _pc.getInventory().getSting();
        if (_sting != null) {
            _weaponBless	= _sting.getItem().getBless();
            _weaponMaterial	= _sting.getItem().getMaterial();
        }
    }

    public L1Attack(L1Character attacker, L1Character target) {
    	if (target == null) {
    		return;
    	}
        if (attacker instanceof L1PcInstance) {
            _pc = (L1PcInstance) attacker;
            if (target instanceof L1PcInstance) {
                _targetPc	= (L1PcInstance) target;
                _calcType	= PC_PC;
            } else if (target instanceof L1NpcInstance) {
                _targetNpc	= (L1NpcInstance) target;
                _calcType	= PC_NPC;
            }
            
            // 무기 정보의 취득
            weapon	= _pc.getWeapon();
            if (weapon != null) {
                _weaponId			= weapon.getItem().getItemId();
                _weaponType			= weapon.getItem().getWeaponType();
                
                _isBow				= L1ItemWeaponType.isBowWeapon(_weaponType);
                _isGauntlet			= _weaponType == L1ItemWeaponType.GAUNTLET;
                _isKeyringk			= _weaponType == L1ItemWeaponType.KEYRINGK;
                _isStaff			= L1ItemWeaponType.isStaffWeapon(_weaponType);
                _isEdoryu			= _weaponType == L1ItemWeaponType.EDORYU;
                _isClaw				= _weaponType == L1ItemWeaponType.CLAW;
                _isBlunt			= L1ItemWeaponType.isBluntWeapon(_weaponType);
                _isChainsword		= _weaponType == L1ItemWeaponType.CHAINSWORD;
                
                _isLongWeapon		= L1ItemWeaponType.isLongWeapon(_weaponType);
                _isRange2Weapon		= L1ItemWeaponType.isRange2Weapon(_weaponType);
                
                // 무기의 추가명중과 추가대미지는 착용시 캐릭터에 부여된다.
                _weaponAddDmg		= weapon.getItem().getWeaponAddDamage();
                
                _weaponSmall		= weapon.getItem().getDmgSmall();
                _weaponLarge		= weapon.getItem().getDmgLarge();
                _weaponBless		= weapon.getItem().getBless();
                
                _weaponEnchant		= weapon.getEnchantLevel() - (!_isLongWeapon ? weapon.getDurability() : 0);
                _weaponMaterial		= weapon.getItem().getMaterial();
                if (_isBow) {
                	setArrow();
                } else if (_isGauntlet) {
                	setSting();
                }
                _weaponDoubleChance	= weapon.getItem().getDoubleDmgChance();
                _weaponAttrLevel	= weapon.getAttrEnchantLevel();
                _weaponSkill		= weapon.getWeaponSkill(target);
            }
			// 스테이터스에 의한 추가 대미지, 치명타 설정
			if (_isLongWeapon) {
				_statusDamage	= CalcDexStat.longDamage(_pc.getAbility().getTotalDex());
				_statusCritical	= CalcDexStat.longCritical(_pc.getAbility().getTotalDex()) + _pc.ability.getLongCritical();
			} else if (_isKeyringk) {
				_statusDamage	= CalcIntelStat.magicDamage(_pc.getAbility().getTotalInt());
				_statusCritical	= CalcIntelStat.magicCritical(_pc.getAbility().getTotalInt()) + _pc.ability.getMagicCritical();
			} else {
				_statusDamage	= CalcStrStat.shortDamage(_pc.getAbility().getTotalStr());
				_statusCritical	= CalcStrStat.shortCritical(_pc.getAbility().getTotalStr()) + _pc.ability.getShortCritical();
				if (_pc.isPassiveStatus(L1PassiveId.FINAL_BURN) && _pc.getCurrentHpPercent() <= 70) {
					_statusCritical += calcFinalBurn();
				}
			}
        } else if (attacker instanceof L1NpcInstance) {
            _npc = (L1NpcInstance) attacker;
            if (target instanceof L1PcInstance) {
                _targetPc	= (L1PcInstance) target;
                _calcType	= NPC_PC;
            } else if (target instanceof L1NpcInstance) {
                _targetNpc	= (L1NpcInstance) target;
                _calcType	= NPC_NPC;
            }
            
            _isLongAttackNpc	= (_npc.getNpcTemplate().getRanged() >= 10 || _npc.getDopelRanged() >= 10) && _npc.getLocation().getTileLineDistance(new Point(target.getX(), target.getY())) >= 2;
            _statusDamage		= _isLongAttackNpc ? _npc.ability.getLongDmgup() : _npc.ability.getShortDmgup();
        }
        _target		= target;
        _targetId	= target.getId();
        _targetX	= target.getX();
        _targetY	= target.getY();
    }
    
	/* ■■■■■■■■■■■■■■■■ 파인사이트 명중 판정 ■■■■■■■■■■■■■■■■ */
	boolean isFineSite(){
		if (FINE_SITE_WEAPONE_TYPE.contains(_weaponType)) {// 검 활 착용
			int chance = 18 + (_pc.getLevel() > 87 ? ((_pc.getLevel() + ~0x56) >> 1) << 1 : 0);// 기본 18% 87레벨 부터 2레벨당 2프로씩 상승
			if (random.nextInt(100) + 1 <= (chance > 30 ? 30 : chance)) {
				if (_calcType == PC_PC) {
					_targetPc.send_effect(20747);
				} else {
					_targetNpc.broadcastPacket(new S_Effect(_targetNpc.getId(), 20747), true);
				}
				return true;
			}
		}
		return false;
	}
	
    /* ■■■■■■■■■■■■■■■■ 명중 판정 ■■■■■■■■■■■■■■■■ */
    public boolean calcHit() {
        if (_calcType == PC_PC || _calcType == PC_NPC) {
            if (_pc == null || _target == null) {
            	return false;
            }
            if (_isBow && !ARROW_FREE_BOW.contains(_weaponId) && _arrow == null) {
            	_isHit = false;// 화살이 없는 경우는 미스
            } else if (_isGauntlet && _sting == null) {
            	_isHit = false;// 스팅이 없는 경우는 미스
            } else if (!_pc.glanceCheck(_pc.getAttackRange(), _targetX, _targetY, _target instanceof L1DoorInstance)
            		|| !_target.glanceCheck(_pc.getAttackRange(), _pc.getX(), _pc.getY(), false)) {
            	_isHit = false;// 공격자가 플레이어의 경우는 장애물 판정
            } else if (_weaponId == 247 || _weaponId == 248 || _weaponId == 249) {
            	_isHit = false;// 시련의 검B~C 공격 무효
            } else if (_pc.getMapId() == 631 || _pc.getMapId() == 514) {
            	_isHit = false;
            } else if (_calcType == PC_PC) {
				if (_targetPc.getTeleport().isTeleport()) {
					return _isHit = false;
				}
                _isHit = calcPcPcHit();
                if (_isHit == false) {
                	S_Effect effect = new S_Effect(_target.getId(), 13418);
                    _pc.sendPackets(effect, false);// 이펙트
                    _targetPc.sendPackets(effect, true);// 이펙트
                }
            } else if (_calcType == PC_NPC) {
                if (_target.getCurrentHp() <= 0) {
                	return _isHit = false;
                }
                if (_target instanceof L1ScarecrowAttackerInstance && (_pc.getRegion() == L1RegionStatus.SAFETY || _target.getRegion() == L1RegionStatus.SAFETY)) {
                	return _isHit = false;
                }
				_isHit = calcPcNpcHit();
                if (_isHit == false) {
                	_pc.sendPackets(new S_Effect(_targetNpc.getId(), 13418), true);// 미스 이펙트
                }
            }
        } else if (_calcType == NPC_PC) {
            _isHit = calcNpcPcHit();
            if (_isHit == false) {
            	_targetPc.sendPackets(new S_Effect(_target.getId(), 13418), true);// 이펙트
            }
            companionCombo();
        } else if (_calcType == NPC_NPC) {
            _isHit = calcNpcNpcHit();
            companionCombo();
        } else if (_targetNpc.getNpcTemplate().getNpcId() != 50087 && _targetNpc.getNpcTemplate().getSpriteId() == 7684 && !_pc.getSkill().hasSkillEffect(L1SkillId.PAP_FIVEPEARLBUFF)) {
            return _isHit = false;
        } else if (_targetNpc.getNpcTemplate().getSpriteId() == 7805 && !_pc.getSkill().hasSkillEffect(L1SkillId.PAP_MAGICALPEARLBUFF)) {
            return _isHit = false;
        }
        return _isHit;
    }

    // ●●●● 플레이어로부터 플레이어에의 명중 판정 ●●●●
    /*
     * PC에의 명중율 =(PC의 Lv＋클래스 보정＋STR 보정＋DEX 보정＋무기 보정＋DAI의 매수/2＋마법 보정)×0.68－10 이것으로 산출된 수치는 자신이 최대 명중(95%)을 주는 일을 할 수 있는 상대측 PC의 AC 거기로부터 상대측 PC의 AC가
     * 1좋아질 때마다 자명중율로부터 1당겨 간다 최소 명중율5% 최대 명중율95%
     */
    private boolean calcPcPcHit() {
    	int attack_range	= _pc.getAttackRange();
		L1Sprite sprite		= _targetPc.getSprite();
		int distance_x		= Math.abs(_pc.getX() - _targetPc.getX());
		int distance_y		= Math.abs(_pc.getY() - _targetPc.getY());
		int width			= 0;
		int height			= 0;
		// 타겟 sprite의 범위
		if (sprite != null) {
			width			= sprite.get_width();
			height			= sprite.get_height();
		}
		if (distance_x > width + attack_range || distance_y > height + attack_range) {
			return false;
		}
		
        boolean absol		= _targetPc.getSkill().hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER);
        if (_isKeyringk) {
        	return absol ? false : true;
        }
        boolean absolBlade	= absol && _pc.getSkill().hasSkillEffect(L1SkillId.ABSOLUTE_BLADE) && isAbsoluteBlade();// 앱솔루트 블레이트 성공여부
        if (absol && !absolBlade) {
        	return false;
        }
        if (_targetPc.getSkill().hasSkillEffect(L1SkillId.ICE_LANCE)) {
        	return false;
        }

        /** 배틀존 **/
        if (_pc.getMapId() == 5153 && _pc.getConfig().getDuelLine() == _targetPc.getConfig().getDuelLine()) {
        	return false;
        }
		
        _hitRate = calcPcHit() + getBalancePhysicalHit(_pc.getType(), _targetPc.getType());
        
        if (_targetPc.getLevel() > _pc.getLevel()) {
        	_hitRate -= _targetPc.getLevel() - _pc.getLevel();
        }
        
        if (_isStaff) {
        	_hitRate += 20;// 지팡이일때
        }
		
        boolean result = true;
	    if (!_isLongWeapon && !_isKeyringk) {// 근거리 무기
	    	int dg = _targetPc.ability.getDg();
	    	if (dg > 0 && calcTargetDg() && !isDodgeBreak() && (_pc.ability.getReflectEmasculate() <= 0 || random.nextInt(10) + 1 > _pc.ability.getReflectEmasculate())) {
	    		result = false;
	    	} else if (dg < 0) {// 마이너스일 경우
	    		_hitRate += dg * -1;
	    	}
	    }
	    if (_isLongWeapon) {// 원거리 무기
	    	int er = _targetPc.ability.getPlusEr();
	    	if (er > 0) {
		        if (calcTargetEr()) {
		        	result = false;
		        }
	    	} else if (er < 0) {// 마이너스일 경우
	    		_hitRate += er * -1;
	    	}
	    	if (_targetPc.getSkill().hasSkillEffect(L1SkillId.STRIKER_GALE)) {
	    		_hitRate += 20;// 게일 추가 확률
	    	}
		}
	    
	    /** 마안 **/
	    if (!_isKeyringk && !calcMaanBuff()) {
	    	result = false;
	    }
	    
	    _hitRate = IntRange.ensure(_hitRate, 5, 95);
	    
        int attackerDice = random.nextInt(20) + 1 + _hitRate + ~0x00000009;
        int defenderValue = (int) (_targetPc.getAC().getAc() * Config.CHA.AC_ER) * -1;
        int levelDmg = (_targetPc.getLevel() - _pc.getLevel()) << 1;
        if (levelDmg <= 0) {
        	levelDmg = 0;
        }

        defenderValue += levelDmg;

        /** DefenderDice 연산 **/
        int defenderDice = toPcDD(defenderValue);

        /** 히트 최종 연산 **/
        if (isHitRateCal(attackerDice, defenderDice, _hitRate + ~0x00000008, _hitRate + 10)) {
        	result = false;
        }
        if (result) {
        	result = _hitRate >= random.nextInt(100) + 1;
        }
        if (!result && _pc.isPassiveStatus(L1PassiveId.FINE_SITE) && isFineSite()) {// 파인 사이트
        	_hitRate = 100;
        	return true;
        }
        return result;
    }

    // ●●●● 플레이어로부터 NPC 에의 명중 판정 ●●●●
    private boolean calcPcNpcHit() {
    	int attack_range	= _pc.getAttackRange();
		L1Sprite sprite		= _targetNpc.getSprite();
		int distance_x		= Math.abs(_pc.getX() - _targetNpc.getX());
		int distance_y		= Math.abs(_pc.getY() - _targetNpc.getY());
		int width			= 0;
		int height			= 0;
		// 타겟 sprite의 범위
		if (sprite != null) {
			width			= sprite.get_width();
			height			= sprite.get_height();
		}
		if (distance_x > width + attack_range || distance_y > height + attack_range) {
			return false;
		}
        
        int npcId			= _targetNpc.getNpcTemplate().getNpcId();
        int targetSprite	= _targetNpc.getNpcTemplate().getSpriteId();
        if (npcId >= 45912 && npcId <= 45915 && !_pc.getSkill().hasSkillEffect(L1SkillId.STATUS_HOLY_WATER)) {
        	return false;
        }
        if (npcId == 45916 && !_pc.getSkill().hasSkillEffect(L1SkillId.STATUS_HOLY_MITHRIL_POWDER)) {
        	return false;
        }
        if (npcId == 45941 && !_pc.getSkill().hasSkillEffect(L1SkillId.STATUS_HOLY_WATER_OF_EVA)) {
        	return false;
        }
        if (Config.ALT.KARMA_BUFF_ENABLE){
            if (L1MonsterInstance.KARMA_BALROG_BOSS_IDS.contains(npcId) && !_pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_BARLOG)) {
            	return false;
            }
            if (L1MonsterInstance.KARMA_YAHEE_BOSS_IDS.contains(npcId) && !_pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_YAHEE)) {
            	return false;
            }
        }
        if ((npcId >= 5000103 && npcId <= 5000104) && !_pc.getSkill().hasSkillEffect(L1SkillId.DETHNIGHT_BUNNO)) {
        	return false;
        }
        if (NOT_ENABLE_NPC_IDS.contains(npcId)) {
        	return false;// 인던
        }
        if (npcId >= 46068 && npcId <= 46091 && _pc.getSpriteId() == 6035) {
        	return false;
        }
        if (npcId >= 46092 && npcId <= 46106 && _pc.getSpriteId() == 6034) {
        	return false;
        }
        if (npcId != 50087 && targetSprite == 7684 && !_pc.getSkill().hasSkillEffect(L1SkillId.PAP_FIVEPEARLBUFF)) {
        	return false;// 오색진주
        }
        if (targetSprite == 7805 && !_pc.getSkill().hasSkillEffect(L1SkillId.PAP_MAGICALPEARLBUFF)) {
        	return false;// 신비진주
        }
        
        if (_isKeyringk) {
        	return true;
        }
        
        _hitRate = _pc.getLevel() + calcPcHit() + getBalancePhysicalHit(_pc.getType(), -1);
        
        // DG수치에 따른 근거리 회피
		if (_targetNpc._statusDistroyFear && !_isLongWeapon && !_isKeyringk) {// 근거리 무기
			_hitRate += 20;
		}
		// ER수치에 따른 원거리 회피
		if (_targetNpc.getSkill().hasSkillEffect(L1SkillId.STRIKER_GALE) && _isLongWeapon) {// 원거리 무기
			_hitRate += 20;
		}
		
        if (_targetNpc.getAC().getAc() < 0) {
            int acRate		= (int)(_targetNpc.getAC().getAc() * Config.ETC.MONSTER_AC_DEFEND) * -1;
            int decrease	= (int)(acRate / 2.5D);
            _hitRate -= decrease;
        }

        if (_pc.getLevel() < _targetNpc.getLevel()) {
        	_hitRate -= _targetNpc.getLevel() - _pc.getLevel();
        }
        if (_pc.getMap().isBeginZone()) {// 초보존
        	_hitRate += 40;
        }
        
        _hitRate = IntRange.ensure(_hitRate, 5, 95);
        
        /** 몬스터가 카운터 배리어 시전할시 **/
        if ((_targetNpc.getSkill().hasSkillEffect(L1SkillId.MOB_COUNTER_BARRIER) || npcId == 7220084) 
        		&& !_isLongWeapon && !_isKeyringk && actionMonterCounterBarrier(L1SkillId.MOB_COUNTER_BARRIER)) {
        	_hitRate = 0;
        	return false;
		}
        if (_targetNpc.getSkill().hasSkillEffect(L1SkillId.MOB_COUNTER_BARRIER_BETERANG) 
				&& !_isLongWeapon && !_isKeyringk && actionMonterCounterBarrier(L1SkillId.MOB_COUNTER_BARRIER_BETERANG)) {
			_hitRate = 0;
			return false;
		}
        
        boolean result = _hitRate >= random.nextInt(100) + 1;// 공격 성공 여부
        // 공격 실패시
        if (!result) {
        	if (_pc.isPassiveStatus(L1PassiveId.MEISTER_ACCURACY)) {// 마에스터 어큐러시 일경우 npc 공격성공 100%
        		_hitRate = 100;
        		return true;
        	}
            if (_pc.isPassiveStatus(L1PassiveId.FINE_SITE) && isFineSite()) {// 파인 사이트 공격성공 100%
            	_hitRate = 100;
            	return true;
            }
        }
        return result;
    }

    // ●●●● NPC 로부터 플레이어에의 명중 판정 ●●●●
    private boolean calcNpcPcHit() {
        if (_targetPc.getSkill().hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) {
        	return false;
        }
        _hitRate += (int) (_npc.getLevel() * 1.2D) + _npc.ability.getShortHitup();
        if (_targetPc.getMap().isBeginZone()) {// 초보존
        	_hitRate += ~0x00000027;// -40
        }
        if (_isLongAttackNpc) {
        	int er = _targetPc.ability.getPlusEr();
			if (er > 0) {
				if (calcTargetEr()) {
					return false;
				}
			} else if (er < 0) {
				_hitRate += er * -1;//마이너스일 경우
			}
			
			if (_targetPc.getSkill().hasSkillEffect(L1SkillId.STRIKER_GALE)) {
				_hitRate += 20;// 게일 추가 확률
			}
        } else {
        	int dg = _targetPc.ability.getDg();
		    if (dg > 0 && calcTargetDg()) {
		    	return false;
		    }
		    if (dg < 0) {
		    	_hitRate += dg * -1;//마이너스일 경우
		    }
        }
		
		/** 마안 **/
		if (!calcMaanBuff()) {
			return false;
		}
		_hitRate += getBalancePhysicalHit(-1, _targetPc.getType());
        
        int attackerDice = random.nextInt(20) + 1 + _hitRate + ~0x00000000;
        int defenderValue = (int) (_targetPc.getAC().getAc() * Config.CHA.AC_ER) * -1;
        /** DefenderDice 연산 **/
        int defenderDice = toPcDD(defenderValue);
        /** 히트 최종 연산 **/
        if (isHitRateCal(attackerDice, defenderDice, _hitRate, _hitRate + 19)) {
        	return false;
        }
        return _hitRate >= random.nextInt(100) + 1;
    }

    // ●●●● NPC 로부터 NPC 에의 명중 판정 ●●●●
    private boolean calcNpcNpcHit() {
        int target_ac		= 10 - _targetNpc.getAC().getAc();
        int attacker_lvl	= _npc.getNpcTemplate().getLevel();
        int targetNpcId		= _targetNpc.getNpcId();
        _hitRate = target_ac != 0 ? (100 / target_ac * attacker_lvl) : 100; 
        // 피공격자 AC = 공격자 Lv  // 의 때 명중율 100%
        _hitRate += _npc.ability.getShortHitup();
        _hitRate += getBalancePhysicalHit(-1, -1);

        if (_hitRate < attacker_lvl) {
        	_hitRate = attacker_lvl; // 최저 명중율=Lｖ％
        }
        _hitRate = IntRange.ensure(_hitRate, 5, 95);
        if (targetNpcId == 202086 || targetNpcId == 202087) {
        	_hitRate = 0;
        }
        return _hitRate >= random.nextInt(100) + 1;
    }

    /* ■■■■■■■■■■■■■■■ 대미지 산출 ■■■■■■■■■■■■■■■ */
    public int calcDamage(){
        try {
            switch (_calcType) {
            case PC_PC:
                _damage = calcPcPcDamage();
                if (_targetPc.isWarrior()) {
                	if (_isKeyringk) {
                    	if (_targetPc.isPassiveStatus(L1PassiveId.TITAN_MAGIC) && _targetPc.getPassiveSkill().isTitan(_pc, TitanType.MAGIC, _calcType)) {// 타이탄 매직
                        	return _damage = 0;
                        }
                    } else if (_isLongWeapon){
                    	if (_targetPc.isPassiveStatus(L1PassiveId.TITAN_BLICK) && _targetPc.getPassiveSkill().isTitan(_pc, TitanType.BLICK, _calcType)) {// 타이탄 블릿
                        	return _damage = 0;
                        }
                    } else {
                    	if (_targetPc.isPassiveStatus(L1PassiveId.TITAN_LOCK) && _targetPc.getPassiveSkill().isTitan(_pc, TitanType.LOCK, _calcType)) {// 타이탄 락
                        	return _damage = 0;
                        }
                    }
                }
                break;
            case PC_NPC:
                _damage = calcPcNpcDamage();
                break;
            case NPC_PC:
                _damage = calcNpcPcDamage();
                if (_targetPc.isWarrior()) {
                	if (_isLongAttackNpc) {
                		if (_targetPc.isPassiveStatus(L1PassiveId.TITAN_BLICK) && _targetPc.getPassiveSkill().isTitan(_npc, TitanType.BLICK, _calcType)) {// 타이탄 블릿
                        	return _damage = 0;
                        }
                	} else {
                		if (_targetPc.isPassiveStatus(L1PassiveId.TITAN_LOCK) && _targetPc.getPassiveSkill().isTitan(_npc, TitanType.LOCK, _calcType)) {// 타이탄 락
                        	return _damage = 0;
                        }
                	}
                }
                break;
            case NPC_NPC:
                _damage = calcNpcNpcDamage();
                break;
            default:break;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return _damage;
    }
   
    // ●●●● 플레이어로부터 플레이어에의 대미지 산출 ●●●●
    public int calcPcPcDamage() {
        if ((_pc.getRegion() == L1RegionStatus.SAFETY && _targetPc.getRegion() == L1RegionStatus.NORMAL) 
        		|| (_pc.getRegion() == L1RegionStatus.SAFETY && _targetPc.getRegion() == L1RegionStatus.COMBAT)) {
            _isHit = false;// 세이프티존에서 노멀/컴뱃존 공격 불가
        }
        int weaponMaxDamage = _weaponSmall;
        
        int weaponDamage = 0;
        if (_weaponType != null) {
        	weaponDamage = random.nextInt(weaponMaxDamage) + 1 + _weaponAddDmg;
        }
        
        // 크로우 무기 최대 대미지
        if (_isClaw && random.nextInt(100) + 1 <= _weaponDoubleChance) {
            weaponDamage = weaponMaxDamage + _weaponAddDmg;
            _isCritical = true;
        }

        // 소울 오브 프레임 무기 최대 대미지
        if (_pc.getSkill().hasSkillEffect(L1SkillId.SOUL_OF_FLAME) && !_isLongWeapon) {
            weaponDamage = weaponMaxDamage + _weaponAddDmg;
        }
        
        // 치명타
        if (!_isCritical && _weaponType != null) {
        	weaponDamage = calcCritical(weaponDamage, weaponMaxDamage);
        }

        int weaponTotalDamage = 0;
        if (weapon != null) {
    		switch(weapon.getItem().getItemGrade()){
        	case LEGEND:weaponTotalDamage = weaponDamage + (_weaponEnchant >= 6 ? _weaponEnchant * 3 : _weaponEnchant << 1);break;// 전설무기 2배
        	case MYTH:	weaponTotalDamage = weaponDamage + (_weaponEnchant * 5);break;// 신화무기 5배
        	default:	weaponTotalDamage = weaponDamage + _weaponEnchant;break;
        	}
    		if (_weaponEnchant >= 10) {
    			weaponTotalDamage += _weaponEnchant + ~0x00000008;// -9
    		}
        }
        
        if ((_isEdoryu || _isClaw) && _pc.isDarkelf() && isDoubleBrake()) {
        	weaponTotalDamage <<= 1;// 2배
        }

        double dmg = weaponTotalDamage + _statusDamage + (!_isLongWeapon ? _pc.ability.getShortDmgup() : _pc.ability.getLongDmgup()) + _pc.ability.getPVPDamage();
        int pvp_damage_percent = _pc.ability.getPVPDamagePercent();
        if (pvp_damage_percent > 0) {
        	dmg += (dmg * 0.01) * pvp_damage_percent;
        }
        
        if (_isRange2Weapon && _pc._isLancerForm) {
        	dmg = calcLancerFormDamage(_targetPc, dmg);// 창기사 원거리폼
        }
        
        if (_isKeyringk) {// 키링크
        	dmg = calcKeyringkDamage(weaponTotalDamage);
			if ((_targetPc.getSkill().hasSkillEffect(L1SkillId.FAFU_MAAN) || _targetPc.getSkill().hasSkillEffect(L1SkillId.BIRTH_MAAN)
	    			|| _targetPc.getSkill().hasSkillEffect(L1SkillId.SHAPE_MAAN) || _targetPc.getSkill().hasSkillEffect(L1SkillId.LIFE_MAAN)
	    			|| _targetPc.getSkill().hasSkillEffect(L1SkillId.ABSOLUTE_MAAN)) && random.nextInt(100) + 1 <= 10) {
		    	dmg = (int)dmg >> 1;// 대미지 반감
			}
		} else if (_isBow) {// 활
            if (_arrow != null) {// 화살 장착일 경우
                int add_dmg = _arrow.getItem().getDmgSmall() + _arrow.getItem().getBowDmgRate();// 화살 대미지
                if (add_dmg == 0) {
                	add_dmg = 1;
                }
                dmg = dmg + add_dmg;
            } else if (ARROW_FREE_BOW.contains(_weaponId)) {
            	dmg = dmg + 2;// 무형 화살 대미지
            }
        } else if (_isGauntlet) {// 건틀렛
            int add_dmg = _sting.getItem().getDmgSmall() + _sting.getItem().getBowDmgRate();// 스팅 대미지
            if (add_dmg == 0) {
            	add_dmg = 1;
            }
            dmg = dmg + add_dmg;
        }
        dmg = calcBuffDamage(dmg);
        
        /** 아머브레이크 */
        if (_targetPc.getSkill().hasSkillEffect(L1SkillId.ARMOR_BREAK) && !_isLongWeapon && !_isKeyringk) {// 아머브레이크
        	if (_targetPc.getArmorBreakID() == _pc.getId()) {
        		dmg += (int)dmg >> 1;// 1.5배
        	} else {
        		dmg *= 1.2D;
        	}
        } else if (_targetPc.getSkill().hasSkillEffect(L1SkillId.MOB_ARMOR_BRAKE) && !_isLongWeapon && !_isKeyringk) {// 몬스터 아머브레이크
            dmg *= 1.20D;
        }
        
        //if(_pc.hasSkillEffect(L1SkillId.DESTROY_2) && _pc.getWeapon().getItem().getType() == 18)ArmorDestory();

        dmg += calcBurningSlash(_targetPc);// 전사 스킬 PC - PC
        dmg += calcCrash(_targetPc);// 크래쉬 : 공격자에 레벨에 50% 정도를 데미지에 반영한다.
        if (isRage(_targetPc)) {// 레이지
        	dmg += (int)dmg >> 1;// 1.5배
        }
        if (_isChainsword) {// 체인소드 약점 노출
        	expose_weakness_from_chainsword();
        }
		dmg += calcWeaponSpellDmg(_pc, _targetPc);// 마법무기
		dmg += calcCaoticDmg();//	카오수치에 따른 대미지
		dmg += calcEnchantDmg();//	인첸트에 따른 추타
		dmg += getArmorChanceDamage();// 검은빛 귀걸이 추가 데미지 처리
		dmg += calcDollDamage(_targetPc);// 마법인형에 의한 추가 대미지
        
        // 랭커 변신 PVP추가 대미지
		int polyGfx = _pc.getSpriteId();
		if (_pc.isRankingPoly(polyGfx) || PolyTable.PVP_DAMAGE_POLY_ADD_LIST.contains(polyGfx)) {
			dmg += 2;
		}
		// 70레벨부터 추가타격 + 1
        dmg += Math.max(0, _pc.getLevel() + ~0x00000045);
        
        // 혈맹 버프 pvp
        if (_pc.getSkill().hasSkillEffect(L1SkillId.CLAN_BUFF3)) {
        	dmg += 1;
        }
        if (_targetPc.getSkill().hasSkillEffect(L1SkillId.CLAN_BUFF4)) {
        	dmg -= 1;
        }
        if (_pc.getSkill().hasSkillEffect(L1SkillId.BUFF_JUGUN) && eBloodPledgeRankType.isAuthRankAtEliteKnight(_pc.getBloodPledgeRank())) {
        	dmg += 5;
        }
		
		// 대미지 리덕션
        int targetReduc	= _targetPc.ability.getDamageReduction() + toPcBuffReduction() + calcInfinitiArmor();
		int pvpReduc	= _targetPc.ability.getPVPDamageReduction();
		
		// 케릭터별 PVP대미지 감소 리뉴얼
		if ((_targetPc.isElf() || _targetPc.isWizard()) && _targetPc.getLevel() >= 60) {
			pvpReduc += (_targetPc.getLevel() + ~0x0000003B) >> 2;
		} else if (_targetPc.isKnight() && _targetPc.getLevel() >= 60) {
			pvpReduc += (_targetPc.getLevel() + ~0x0000003B) >> 1;
		} else if ((_targetPc.isCrown() || _targetPc.isDarkelf() || _targetPc.isIllusionist()) && _targetPc.getLevel() >= 60) {
			pvpReduc += (_targetPc.getLevel() + ~0x0000003B) / 3;
		} else if ((_targetPc.isDragonknight() || _targetPc.isWarrior()) && _targetPc.getLevel() >= 60) {
			pvpReduc += _targetPc.getLevel() <= 92 ? (_targetPc.getLevel() + ~0x0000003B) / 3 : ((92 + ~0x0000003B) / 3) + (_targetPc.getLevel() + ~0x0000005B);
		}
		
		if (_targetPc.ability.getAbnormalStatusDamageReduction() > 0 
				&& (_targetPc.isStun() || _targetPc.isHold() || _targetPc.isPressureDeathRecall()
				|| _targetPc.isDesperado() || _targetPc.isOsiris() || _targetPc.isPhantom() || _targetPc.isShockAttackTeleport()
				|| _targetPc.isEternity() || _targetPc.isShadowStepChaser() || _targetPc.isBehemoth())) {
			targetReduc += _targetPc.ability.getAbnormalStatusDamageReduction();
		}
		if (_targetPc.ability.getAbnormalStatusPVPDamageReduction() > 0 
				&& (_targetPc.isStun() || _targetPc.isHold() || _targetPc.isPressureDeathRecall()
				|| _targetPc.isDesperado() || _targetPc.isOsiris() || _targetPc.isPhantom() || _targetPc.isShockAttackTeleport()
				|| _targetPc.isEternity() || _targetPc.isShadowStepChaser() || _targetPc.isBehemoth())) {
			pvpReduc += _targetPc.ability.getAbnormalStatusPVPDamageReduction();
		}
		targetReduc +=  pvpReduc;
		
		targetReduc -= _pc.ability.getDamageReductionEgnor() + _pc.ability.getPVPDamageReductionEgnor();// 대미지 리덕션 무시
        if (targetReduc < 0) {
        	targetReduc = 0;
        }
		
		dmg -= targetReduc;
		if (_targetPc.ability.getPVPDamageReductionPercent() > 0 && dmg > 0) {
			dmg -= (int)(((double)dmg * 0.01D) * _targetPc.ability.getPVPDamageReductionPercent());// PVP대미지 감소 퍼센트
		}

        // 대상 속성인첸트에따른 대미지연산
		dmg += calcAttrDmg();

        // 대상 Buff에 따른 대미지 연산
        dmg = toPcBuffDamage(dmg);
        
        if (_targetPc.getSkill().hasSkillEffect(L1SkillId.IMMUNE_TO_HARM) && _targetPc._isImmunToHarmSaint) {
        	int saint = 0;
        	if (_targetPc.getLevel() >= 80) {
        		saint += (int) _targetPc.getLevel() >> 1;
        		if (saint > 5) {
        			saint = 5;
        		}
        		if (_targetPc.getLevel() >= 90) {
        			saint += _targetPc.getLevel() + ~0x00000059;// -90
        		}
        		if (saint > 10) {
        			saint = 10;
        		}
        	}
        	if (saint > 0) {
        		dmg -= saint;
        	}
        }
        if (_targetPc.getSkill().hasSkillEffect(L1SkillId.LUCIFER) && _targetPc.isPassiveStatus(L1PassiveId.LUCIFER_DESTINY)) {
        	int lucifer = (int) _targetPc.getLevel() >> 1;
        	if (lucifer > 10) {
        		lucifer = 10;
        	}
        	if (lucifer > 0) {
        		dmg -= lucifer;
        	}
        }

        // 트루타켓
        if (_targetPc != null && _targetPc.getSkill().hasSkillEffect(L1SkillId.TRUE_TARGET) && _pc != null 
        		&& (_targetPc.true_target_clanid == _pc.getClanid() || _targetPc.true_target_partyid == _pc.getPartyID())) {
        	dmg += calcTrueTarget(dmg);
		}
		
        L1ArmorSkill.armorSkillAction(_targetPc);// 갑옷 스킬
        doNaturesTouch();

        dmg += getBalancePhysicalDmg(_pc.getType(), _targetPc.getType());
        
        dmg = getArmorChanceReduction(dmg);// 아이템장착 확률적 대미지 감소

        // 캐릭터 간 대미지 외부화 처리
        switch(_pc.getType()){
        case 0:dmg += Config.CHA.PRINCE_ADD_DAMAGEPC;break;
        case 1:dmg += Config.CHA.KNIGHT_ADD_DAMAGEPC;break;
        case 2:dmg += Config.CHA.ELF_ADD_DAMAGEPC;break;
        case 3:dmg += Config.CHA.WIZARD_ADD_DAMAGEPC;break;
        case 4:dmg += Config.CHA.DARKELF_ADD_DAMAGEPC;break;
        case 5:dmg += Config.CHA.DRAGONKNIGHT_ADD_DAMAGEPC;break;
        case 6:dmg += Config.CHA.ILLUSIONIST_ADD_DAMAGEPC;break;
        case 7:dmg += Config.CHA.WARRIOR_ADD_DAMAGEPC;break;
        case 8:dmg += Config.CHA.FENCER_ADD_DAMAGEPC;break;
        case 9:dmg += Config.CHA.LANCER_ADD_DAMAGEPC;break;
        default:break;
        }
        
		if (_pc.isTriple) {
			dmg = dmg * Config.SPELL.TRIPLE_DMG;
			if (_pc.isPassiveStatus(L1PassiveId.TRIPLE_ARROW_BOOST)) {
				dmg += 16;
			}
		}
		if (_pc.isFouSlayer) {
			dmg = dmg * Config.SPELL.FOW_SLAYER_DMG;
		}
		
		if (_weaponType == null) {
			dmg = random.nextInt(2) + 1;// 맨손
		}
		
		// 신규레벨 보호
		int castle_id = L1CastleLocation.getCastleIdByArea(_pc);
		if (Config.ALT.BEGINNER_SAFE_LEVEL > 0) {
	        if (castle_id == 0 && (_targetPc.getLevel() < Config.ALT.BEGINNER_SAFE_LEVEL || _pc.getLevel() < Config.ALT.BEGINNER_SAFE_LEVEL)) {
	        	dmg = (int)dmg >> 1;
	        	_pc.sendPackets(L1SystemMessage.BEGIN_LEVEL_HALF_DAMAGE);
	        	_targetPc.sendPackets(L1SystemMessage.BEGIN_LEVEL_HALF_DAMAGE);
	        }
		}
		
		// 신규혈맹 공격안되게
        if (castle_id == 0 && (_pc.getClanid() == Config.PLEDGE.BEGINNER_PLEDGE_ID || _targetPc.getClanid() == Config.PLEDGE.BEGINNER_PLEDGE_ID)) {
            if (Config.PLEDGE.BEGINNER_PLEDGE_PVP_TYPE) {
                _isHit = false;
                _pc.sendPackets(L1SystemMessage.BEGIN_CLAN_CANNOT_ATTACK);
                _targetPc.sendPackets(L1SystemMessage.BEGIN_CLAN_CANNOT_ATTACK);
            } else {
            	dmg = (int)dmg >> 1;
                _pc.sendPackets(L1SystemMessage.BEGIN_CLAN_HALF_DAMAGE);
                _targetPc.sendPackets(L1SystemMessage.BEGIN_CLAN_HALF_DAMAGE);
            }
        }
        
        // 신규보호 지역
        if (Config.ALT.BEGINNER_MAP_LIST.contains(_pc.getMapId()) || Config.ALT.BEGINNER_MAP_LIST.contains(_targetPc.getMapId())) {
            _isHit = false;
            _pc.sendPackets(L1SystemMessage.BEGIN_AREA_CANNOT_ATTACK);
            _targetPc.sendPackets(L1SystemMessage.BEGIN_AREA_CANNOT_ATTACK);
        }
        
        // 키링크 카매로 막아지도록
        if (_isKeyringk && _targetPc.getSkill().hasSkillEffect(L1SkillId.COUNTER_MAGIC)) {
    		_targetPc.getSkill().removeSkillEffect(L1SkillId.COUNTER_MAGIC);
    		_targetPc.sendPackets(S_PacketBox.COUNTER_MAGIC_OFF);
    		_targetPc.send_effect(10702);
			dmg = 0;
        }
        
        if (GameServerSetting.SAFETY_MODE) {
			dmg = 0;
			_pc.sendPackets(L1SystemMessage.SAFTY_MODE_MSG);
		}

        if (dmg <= 0) {
        	_isHit = false;
        }
        return (int) dmg;
    }

    // ●●●● 플레이어로부터 NPC 에의 대미지 산출 ●●●●
    int calcPcNpcDamage() {
        if (_targetNpc == null || _pc == null) {
            _isHit = false;
            _drainHp = 0;
            return 0;
        }
        
        int weaponMaxDamage	= (_targetNpc.getNpcTemplate().isBig() && _weaponLarge > 0) ? _weaponLarge : _weaponSmall;
        int weaponDamage	= _weaponType == null ? 0 : random.nextInt(weaponMaxDamage) + 1 + _weaponAddDmg;
        
        // 크로우 무기 최대 대미지
        if (_isClaw && random.nextInt(100) + 1 <= _weaponDoubleChance) {
            weaponDamage = weaponMaxDamage + _weaponAddDmg;
            _isCritical = true;
        }

        // 소울 오브 프레임 무기 최대 대미지
        if (!_isLongWeapon && _pc.getSkill().hasSkillEffect(L1SkillId.SOUL_OF_FLAME)) {
            weaponDamage = weaponMaxDamage + _weaponAddDmg;
        }
        
        // 치명타
        if (!_isCritical && _weaponType != null) {
        	weaponDamage = calcCritical(weaponDamage, weaponMaxDamage);
        }

        int weaponTotalDamage = 0;
        if (weapon != null) {
        	switch(weapon.getItem().getItemGrade()){
        	case LEGEND:weaponTotalDamage = weaponDamage + (_weaponEnchant >= 6 ? _weaponEnchant * 3 : _weaponEnchant << 1);break;
        	case MYTH:	weaponTotalDamage = weaponDamage + (_weaponEnchant * 5);break;
        	default:	weaponTotalDamage = weaponDamage + _weaponEnchant;break;
        	}
        	if (_weaponEnchant >= 10) {
        		weaponTotalDamage += _weaponEnchant + ~0x00000008;// -9
        	}
        }
     
        if ((_isEdoryu || _isClaw) && _pc.isDarkelf() && isDoubleBrake()) {
        	weaponTotalDamage <<= 1;// 2배
        }

        double dmg = weaponTotalDamage + _statusDamage + (!_isLongWeapon ? _pc.ability.getShortDmgup() : _pc.ability.getLongDmgup());
        
        if (_isRange2Weapon && _pc._isLancerForm) {
        	dmg = calcLancerFormDamage(_targetNpc, dmg);
        }
        
        if (_isKeyringk) {
        	dmg = calcKeyringkDamage(weaponTotalDamage);
        } else if (_isBow) {// 활
            if (_arrow != null) {// 화살장착일때
                int add_dmg = (_targetNpc.getNpcTemplate().isBig() ? _arrow.getItem().getDmgLarge() : _arrow.getItem().getDmgSmall()) + _arrow.getItem().getBowDmgRate();
                if (add_dmg == 0) {
                	add_dmg = 1;
                }
                
                Material material = _arrow.getItem().getMaterial();// 재질
                L1Undead undead = _targetNpc.getNpcTemplate().getUndead();
                if (Material.isUndeadMaterial(material) && L1Undead.isHolyUndead(undead)) {
                	add_dmg += random.nextInt(20) + 5;
                }
				add_dmg += addAttrArrowDamage(_targetNpc) + 1;//속성 화살         
                if (_targetNpc.getNpcTemplate().isHard()) {
                	add_dmg >>= 1;// 근거리 무기에 손상을주는 몹일경우 활은 손상을 안입는대신 데미지 반으로 줄인다.
                }
                dmg += add_dmg;
            } else if (ARROW_FREE_BOW.contains(_weaponId)) {
            	dmg = dmg + 2;// 무형 화살
            }
        } else if (_isGauntlet) {// 건틀렛
            int add_dmg = (_targetNpc.getNpcTemplate().isBig() ? _sting.getItem().getDmgLarge() : _sting.getItem().getDmgSmall()) + _sting.getItem().getBowDmgRate();
            if (add_dmg == 0) {
            	add_dmg = 1;
            }
            
            // 언데드 몬스터
            Material material = _arrow.getItem().getMaterial();// 재질
            L1Undead undead = _targetNpc.getNpcTemplate().getUndead();
            if (Material.isUndeadMaterial(material) && L1Undead.isHolyUndead(undead)) {
            	add_dmg += random.nextInt(20) + 5;
            }
            dmg += add_dmg;
        }
        
        MapBalanceData mapBalance = _targetNpc.getMap().getBalance();
        if (mapBalance != null) {
        	dmg *= mapBalance.getReductionValue(BalanceType.ATTACK);
        }
        
        int dmgModiPc2Npc = _pc.getMap().getDmgModiPc2Npc();
        if (dmgModiPc2Npc > 0) {
        	dmg *= dmgModiPc2Npc * 0.01;
        }
        
		dmg = calcBuffDamage(dmg);// 버프 대미지
		
		// 아머브레이크
        if (_targetNpc.getSkill().hasSkillEffect(L1SkillId.ARMOR_BREAK) && !_targetNpc.getNpcTemplate().isBossMonster() && !_isLongWeapon && !_isKeyringk) {
        	if (_targetNpc.getArmorBreakID() == _pc.getId()) {
        		dmg += (int)dmg >> 1;// 1.5배
        	} else {
        		dmg *= 1.2D;
        	}
        }
		
        dmg += calcMaterialOrBlessDamage();// 재질에의한 대미지 보너스
        dmg += calcAttrDmg();// 속성 대미지
        dmg += calcBurningSlash(_targetNpc);// 전사 스킬 PC - NPC
        dmg += calcCrash(_targetPc);// 크래쉬 : 공격자에 레벨에 50% 정도를 데미지에 반영한다.
        if (isRage(_targetNpc)) {//	레이지
        	dmg += (int)dmg >> 1;// 1.5배
        }
        if (_isChainsword) {// 체인소드 약점 노출
        	expose_weakness_from_chainsword();
        }
		dmg += calcWeaponSpellDmg(_pc, _targetNpc);// 마법무기
		dmg += calcCaoticDmg();// 카오수치에 따른 대미지
		dmg += calcEnchantDmg();// 인첸트에 따른 추타
		dmg += getArmorChanceDamage();// 검은빛 귀걸이 추가 데미지 처리
        dmg += calcDollDamage(_targetNpc);// 마법인형에 의한 추가 대미지
        dmg += getBalancePhysicalDmg(_pc.getType(), -1);
        
        // 그랑카인 시스템 공격력 감소
        if (Config.FATIGUE.FATIGUE_ACTIVE && _pc != null && _pc.getFatigue() != null && _pc.getFatigue().getLevel() > 0) {
        	dmg *= _pc.getFatigue().getPenalty();
        }
        
		int reduction = _targetNpc.ability.getDamageReduction() - _pc.ability.getDamageReductionEgnor();
		if (reduction > 0) {
			dmg -= reduction;
		}
        
        // 몬스터 AC구간 대미지 감소
		int targetAc = _targetNpc.getAC().getAc();
        if (targetAc < 0) {
        	if (targetAc <= -150) {
        		dmg *= Config.ETC.MONSTER_AC_TEN;
        	} else if (targetAc <= -140) {
        		dmg *= Config.ETC.MONSTER_AC_NINE;
        	} else if (targetAc <= -130) {
        		dmg *= Config.ETC.MONSTER_AC_EIGHT;
        	} else if (targetAc <= -120) {
        		dmg *= Config.ETC.MONSTER_AC_SEVEN;
        	} else if (targetAc <= -110) {
        		dmg *= Config.ETC.MONSTER_AC_SIX;
        	} else if (targetAc <= -100) {
        		dmg *= Config.ETC.MONSTER_AC_FIVE;
        	} else if (targetAc <= -80) {
        		dmg *= Config.ETC.MONSTER_AC_FOUR;
        	} else if (targetAc <= -60) {
        		dmg *= Config.ETC.MONSTER_AC_THREE;
        	} else if (targetAc <= -40) {
        		dmg *= Config.ETC.MONSTER_AC_TWO;
        	} else if (targetAc <= -10) {
        		dmg *= Config.ETC.MONSTER_AC_ONE;
        	}
        }
        
        boolean isPet		= _targetNpc instanceof L1PetInstance;
        boolean isSummon	= _targetNpc instanceof L1SummonInstance;
        if (isPet || isSummon) {
        	boolean isNowWar = false;
            int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
            if (castleId > 0) {
            	isNowWar = War.getInstance().isNowWar(castleId);
            }
        	if (isPet) {
        		L1PetInstance pet = (L1PetInstance)_targetNpc;
                if (pet.get_pvp_melee_defense() != 0) {
                	dmg -= pet.get_pvp_melee_defense();
                }
                L1PinkName.onAction(_pc);
                L1PetInstance myPet = _pc.getPet();
                if (myPet != null && myPet.getTarget() == null && myPet != pet) {
                	myPet.setTarget(pet);
                }
                if (!isNowWar) {
                	dmg = (int)dmg >> 3;
                }
        	}
            if (!isNowWar && isSummon && ((L1SummonInstance)_targetNpc).isExsistMaster()) {
            	dmg = (int)dmg >> 3;
            }
        }
		
		if (_pc.isTriple) {
			dmg = dmg * Config.SPELL.TRIPLE_DMG;
			if (_pc.isPassiveStatus(L1PassiveId.TRIPLE_ARROW_BOOST)) {
				dmg += 16;
			}
		}
		if (_pc.isFouSlayer) {
			dmg = dmg * Config.SPELL.FOW_SLAYER_DMG;
		}
		
		if (_weaponType == null) {
			dmg = random.nextInt(2) + 1;// 맨손
		}
		if (_targetNpc.getSkill().hasSkillEffect(L1SkillId.ZEROS_REDUCTION)) {
			dmg *= 0.8D;
		}
        if (_targetNpc.getSkill().hasSkillEffect(L1SkillId.ICE_LANCE)) {
        	dmg = 0;
        }
        if (_targetNpc.getSkill().hasSkillEffect(L1SkillId.EARTH_BIND)) {
        	dmg = 0;
        }
		
		// 버프없을시 공격안됨	
        int npcId = _targetNpc.getNpcTemplate().getNpcId();
        if ((npcId >= 5000103 && npcId <= 5000104) && (!_pc.getSkill().hasSkillEffect(L1SkillId.DETHNIGHT_BUNNO))) {
        	dmg = 0;
        }
        if (NOT_ENABLE_NPC_IDS.contains(npcId)) {
        	dmg = 0;// 인던
        }
        
        if (dmg <= 0) {
        	_isHit = false;
        }
        return (int) dmg;
    }

    // ●●●● NPC 로부터 플레이어에의 대미지 산출 ●●●●
    int calcNpcPcDamage() {
        if (_npc == null || _targetPc == null) {
        	return 0;
        }

		int lvl = _npc.getLevel();
		double dmg = 0D;
		if (_isLongAttackNpc) {// 원거리 몬스터
			int dex	= _npc.getAbility().getTotalDex();
			if (lvl >= 95) {
				dmg = (random.nextInt(lvl) + dex) * Config.ETC.MONSTER_DAMAGE_NINE;
			} else if (lvl >= 90) {
				dmg = (random.nextInt(lvl) + dex) * Config.ETC.MONSTER_DAMAGE_EIGHT;
			} else if (lvl >= 85) {
				dmg = (random.nextInt(lvl) + dex) * Config.ETC.MONSTER_DAMAGE_SEVEN;
			} else if (lvl >= 80) {
				dmg = (random.nextInt(lvl) + dex) * Config.ETC.MONSTER_DAMAGE_SIX;
			} else if (lvl >= 75) {
				dmg = (random.nextInt(lvl) + dex) * Config.ETC.MONSTER_DAMAGE_FIVE;
			} else if (lvl >= 70) {
				dmg = (random.nextInt(lvl) + dex) * Config.ETC.MONSTER_DAMAGE_FOUR;
			} else if (lvl >= 60) {
				dmg = (random.nextInt(lvl) + dex) * Config.ETC.MONSTER_DAMAGE_THREE;
			} else if (lvl >= 40) {
				dmg = (random.nextInt(lvl) + dex) * Config.ETC.MONSTER_DAMAGE_TWO;
			} else if (lvl >= 20) {
				dmg = (random.nextInt(lvl) + dex) * Config.ETC.MONSTER_DAMAGE_ONE;
			} else {
				dmg = (random.nextInt(lvl) + dex) * Config.ETC.MONSTER_DAMAGE_ONE + 5;
			}
		} else {
			int str	= _npc.getAbility().getTotalStr();
			if (lvl >= 95) {
				dmg = (random.nextInt(lvl) + str) * Config.ETC.MONSTER_DAMAGE_NINE;
			} else if (lvl >= 90) {
				dmg = (random.nextInt(lvl) + str) * Config.ETC.MONSTER_DAMAGE_EIGHT;
			} else if (lvl >= 85) {
				dmg = (random.nextInt(lvl) + str) * Config.ETC.MONSTER_DAMAGE_SEVEN;
			} else if (lvl >= 80) {
				dmg = (random.nextInt(lvl) + str) * Config.ETC.MONSTER_DAMAGE_SIX;
			} else if (lvl >= 75) {
				dmg = (random.nextInt(lvl) + str) * Config.ETC.MONSTER_DAMAGE_FIVE;
			} else if (lvl >= 70) {
				dmg = (random.nextInt(lvl) + str) * Config.ETC.MONSTER_DAMAGE_FOUR;
			} else if (lvl >= 60) {
				dmg = (random.nextInt(lvl) + str) * Config.ETC.MONSTER_DAMAGE_THREE;
			} else if (lvl >= 40) {
				dmg = (random.nextInt(lvl) + str) * Config.ETC.MONSTER_DAMAGE_TWO;
			} else if (lvl >= 20) {
				dmg = (random.nextInt(lvl) + str) * Config.ETC.MONSTER_DAMAGE_ONE;
			} else {
				dmg = (random.nextInt(lvl) + str) * Config.ETC.MONSTER_DAMAGE_ONE + 5;
			}
		}
        
        dmg += _statusDamage;
        
        if (isUndeadDamage()) {
        	dmg += (int)dmg >> 2;
        }
        
        if (_npc.getSkill().hasSkillEffect(L1SkillId.MOB_BLIND_HIDING)) {
        	_npc.getSkill().removeSkillEffect(L1SkillId.MOB_BLIND_HIDING);
        	_targetPc.send_effect(14547);
        	dmg = (int)dmg << 1;
		}
        
        // 전체 몬스터 대미지
        dmg = dmg * _leverage / Config.ETC.MONSTER_DMG;

        // 특정 맵 몬스터 대미지
        MapBalanceData mapBalance = _npc.getMap().getBalance();
        if (mapBalance != null) {
        	dmg *= mapBalance.getDamageValue(BalanceType.ATTACK);
        }
        
        if (_npc.isWeaponBreaked()) {
        	dmg = (int)dmg >> 1;// NPC가 웨폰브레이크중.
        }
        if (_npc._statusDistroyHorror) {
        	dmg += ~0x00000000;// -1
        }
        
        dmg += getBalancePhysicalDmg(-1, _targetPc.getType());
        
        int dmgModiNpc2Pc = _npc.getMap().getDmgModiNpc2Pc();
        if (dmgModiNpc2Pc > 0) {
        	dmg *= dmgModiNpc2Pc * 0.01;
        }

        // 대미지 리덕션
        int targetReduc = _targetPc.ability.getDamageReduction() + toPcBuffReduction() + calcInfinitiArmor() + calcDraniumReduction() - _npc.ability.getDamageReductionEgnor();
        if (_targetPc.ability.getAbnormalStatusDamageReduction() > 0 
				&& (_targetPc.isStun() || _targetPc.isHold() || _targetPc.isPressureDeathRecall()
				|| _targetPc.isDesperado() || _targetPc.isOsiris() || _targetPc.isPhantom() || _targetPc.isShockAttackTeleport()
				|| _targetPc.isEternity() || _targetPc.isShadowStepChaser() || _targetPc.isBehemoth())) {
			targetReduc += _targetPc.ability.getAbnormalStatusDamageReduction();
		}
        
        // 그랑카인 시스템 리덕션 감소
        if (Config.FATIGUE.FATIGUE_ACTIVE && _targetPc != null && _targetPc.getFatigue() != null && _targetPc.getFatigue().getLevel() > 0) {
        	targetReduc *= _targetPc.getFatigue().getPenalty();
        }
        
		if (targetReduc < 0) {
			targetReduc = 0;
		}
        dmg -= targetReduc;
        
        // 드래곤 피혜 감소
    	if (DRAGON_NPC_IDS.contains(_npc.getNpcId()) && _targetPc.getDoll() != null && _targetPc.getDoll().getDollNpcId() == 757) {
    		dmg *= 0.9D;;
    	}
    	
        // 대상 Buff에 따른 대미지 연산
        dmg = toPcBuffDamage(dmg);
        if ((_targetPc.getSkill().hasSkillEffect(L1SkillId.ARMOR_BREAK) || _targetPc.getSkill().hasSkillEffect(L1SkillId.MOB_ARMOR_BRAKE)) 
        		&& !_isLongAttackNpc) {
        	dmg *= 1.20D;
        }
		dmg = getArmorChanceReduction(dmg);// 아이템장착 대미지 감소
		L1ArmorSkill.armorSkillAction(_targetPc);// 갑옷 스킬
		doNaturesTouch();
        
        if (_npc.isTriple) {
        	dmg = dmg * Config.SPELL.TRIPLE_DMG;
        }
        if (_npc.isFouSlayer) {
        	dmg = dmg * Config.SPELL.FOW_SLAYER_DMG;
        }
               
        if (_npc instanceof L1PetInstance) {
            if (_targetPc.getRegion() == L1RegionStatus.SAFETY) {
            	_isHit = false;
            } else {
                L1PetInstance pet = (L1PetInstance)_npc;
                if (pet.isCombo()) {
                    dmg += pet.getComboCount() * 10.0D * pet.get_comboDmgMulti() * 0.01D;
                    if (pet.getComboCount() >= 3) {
                        pet.setComboCount(0);
                        _petCombo = true;
                        _targetPc.broadcastPacketWithMe(new S_Effect(_targetPc.getId(), 17326), true);
                    }
                }
                dmg += companionAttrDamage(pet);
                int pvp_dmg_ratio = ((L1PetInstance)_npc).get_pvp_dmg_ratio();
                if (pvp_dmg_ratio != 0) {
                	dmg += (dmg * 0.01) * pvp_dmg_ratio;
                }
                if (pet.get_bloodSuckHit() > 0 && pet.get_bloodSuckHeal() > 0 && random.nextInt(100) + 1 <= pet.get_bloodSuckHit()) {
                	_drainHp = random.nextInt(pet.get_bloodSuckHeal()) + 1;
                }
            }
        }
        
        boolean isPet		= _npc instanceof L1PetInstance;
        boolean isSummon	= _npc instanceof L1SummonInstance;
        if (isPet || isSummon) {
        	boolean isNowWar = false;
            int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
            if (castleId > 0) {
            	isNowWar = War.getInstance().isNowWar(castleId);
            }
            if (!isNowWar) {
                if (isPet) {
                	dmg = (int)dmg >> 3;
                }
                if (isSummon && ((L1SummonInstance)_npc).isExsistMaster()) {
                	dmg = (int)dmg >> 3;
                }
            }
        }

        addNpcPoisonAttack(_npc, _targetPc);

        if ((isPet || isSummon) && _targetPc.getRegion() == L1RegionStatus.SAFETY) {
        	_isHit = false;
        }
        if (dmg <= 0) {
        	_isHit = false;
        }
        return (int) dmg;
    }

    // ●●●● NPC 로부터 NPC 에의 대미지 산출 ●●●●
    int calcNpcNpcDamage() {
        if (_targetNpc == null || _npc == null) {
        	return 0;
        }
        int lvl = _npc.getLevel();
        double dmg = 0;
        if (_npc instanceof L1PetInstance) {
        	L1PetInstance pet = (L1PetInstance)_npc;
            dmg = random.nextInt(pet.getLevel());
        } else if (_npc instanceof L1SummonInstance) {
        	dmg = random.nextInt(lvl) + _npc.getAbility().getTotalStr() + 5;
        } else {
        	dmg = random.nextInt(lvl) + (_npc.getAbility().getTotalStr() >> 1) + 1;
        }
        dmg += _statusDamage;
        if (isUndeadDamage()) {
        	dmg += (int)dmg >> 2;
        }
        dmg = dmg * _leverage / 10;
        dmg += getBalancePhysicalDmg(-1, -1);
        dmg -= _targetNpc.ability.getDamageReduction();
        if (_npc.isWeaponBreaked()) {
        	dmg -= (int)dmg >> 1; // NPC가 웨폰브레이크중.
        }
        
        if (_npc.getSkill().hasSkillEffect(L1SkillId.MOB_BLIND_HIDING)) {
        	_npc.getSkill().removeSkillEffect(L1SkillId.MOB_BLIND_HIDING);
        	_targetNpc.broadcastPacket(new S_Effect(_targetNpc.getId(), 14547), true);
        	dmg += dmg;
		}

        addNpcPoisonAttack(_npc, _targetNpc);
        
        if (_npc instanceof L1PetInstance) {
            L1PetInstance pet = (L1PetInstance)_npc;
            if (pet.isCombo()) {
                dmg += pet.getComboCount() * 10.0D * pet.get_comboDmgMulti() * 0.01D;
                if (pet.getComboCount() >= 3) {
                    pet.setComboCount(0);
                    _petCombo = true;
                    _targetNpc.broadcastPacket(new S_Effect(_targetNpc.getId(), 17326), true);
                }
            }
            dmg += companionAttrDamage(pet);
            if (pet.get_bloodSuckHit() > 0 && pet.get_bloodSuckHeal() > 0 && random.nextInt(100) + 1 <= pet.get_bloodSuckHit()) {
            	_drainHp = random.nextInt(pet.get_bloodSuckHeal()) + 1;
            }
            if (_targetNpc instanceof L1PetInstance) {
            	L1PinkName.onAction(pet.getMaster());
            }
        }

        if (_targetNpc.getSkill().hasSkillEffect(L1SkillId.ICE_LANCE)) {
        	dmg = 0;
        }
        if (_targetNpc.getSkill().hasSkillEffect(L1SkillId.EARTH_BIND)) {
        	dmg = 0;
        }
        if (dmg <= 0) {
        	_isHit = false;
        }
        return (int) dmg;
    }
    
    int getBalancePhysicalHit(int attackerType, int targetType){
    	return BalanceTable.getPhysicalHit(attackerType, targetType);
    }
    
    int getBalancePhysicalDmg(int attackerType, int targetType){
    	return BalanceTable.getPhysicalDmg(attackerType, targetType);
    }
    
    // ●●●● 더블 브레이크  ●●●●
    boolean isDoubleBrake(){
    	if (_isEdoryu && random.nextInt(100) + 1 <= _weaponDoubleChance - weapon.getDurability()) {// 이도류 더블대미지
    		_isCritical = true;
    		return true;
    	}
    	if (_pc.getSkill().hasSkillEffect(L1SkillId.DOUBLE_BRAKE)) {
    		int level			= _pc.getLevel();
    		int doubleChance	= 25;// 기본 25%
    		if (level >= 92) {// 90레벨 이상부터 2레벨 마다 1%씩 증가
    			doubleChance	+= (level + ~0x00000059) >> 1;
        		if (doubleChance > 30) {// 최대 30%
        			doubleChance = 30;
        		}
    		}
			// 더블브레이크 데스티니
			if (_pc.isPassiveStatus(L1PassiveId.DOUBLE_BREAK_DESTINY) && level >= 80) {
				doubleChance += level + ~0x0000004E;// -79, 80레벨 이상일때 1레벨당 1퍼씩 증가
			}
            if (random.nextInt(100) + 1 <= doubleChance) {
            	_doubleBurning = true;
                return true;
            }
		}
    	return false;
    }
    
    /**
	 * 체인소드 약점 노출
	 * @param pc
	 */
	void expose_weakness_from_chainsword() {// 체인소드 약점노출
		L1SkillStatus skill_status = _pc.getSkill();
		if (skill_status.hasSkillEffect(L1SkillId.EXPOSE_WEAKNESS)) {
			L1Character expose_weakness_target = skill_status.get_expose_weakness_target();
			if (expose_weakness_target == null 
					|| (expose_weakness_target != _target)) {
				// 대상이 변경되면 기존 타겟의 디버프가 해제된다.
				disable_expose_weakness_target(skill_status, expose_weakness_target);
			}
			return;
		}
		if (!_target.getSkill().hasSkillEffect(L1SkillId.STATUS_EXPOSE_WEAKNESS) 
				&& random.nextInt(100) + 1 <= Config.SPELL.EXPOSE_WEAKNESS_PROB) {
			enable_expose_weakness_target(skill_status);
		}
	}
	
	/**
	 * 약점 노출 활성화
	 * @param skill_status
	 */
	void enable_expose_weakness_target(L1SkillStatus skill_status) {
		long duration = 8000L;
		skill_status.set_expose_weakness_target(_target);
		skill_status.setSkillEffect(L1SkillId.EXPOSE_WEAKNESS, duration);
		_pc.sendPackets(S_PacketBox.EXPOSE_WEAKNESS_NORMAL);
		
		// 대상 디버프 부여
		_target.getSkill().setSkillEffect(L1SkillId.STATUS_EXPOSE_WEAKNESS, duration);
		_target.ac.addAc(5);
		_target.ability.addDg(-10);
		_target.resistance.addToleranceDragon(-10);
		if (_calcType == PC_PC) {
			_pc.send_effect_self(_targetPc.getId(), 21932);// 약점 노출 이팩트 공격자에게만 출력된다.
		}
	}
	
	/**
	 * 약점 노출 비활성화
	 * @param skill_status
	 * @param expose_weakness_target
	 */
	void disable_expose_weakness_target(L1SkillStatus skill_status, L1Character expose_weakness_target) {
		if (expose_weakness_target != null && expose_weakness_target.getSkill().hasSkillEffect(L1SkillId.STATUS_EXPOSE_WEAKNESS)) {
			expose_weakness_target.getSkill().removeSkillEffect(L1SkillId.STATUS_EXPOSE_WEAKNESS);// 대상 디버프 해제
		}
		skill_status.removeSkillEffect(L1SkillId.EXPOSE_WEAKNESS);
		_pc.sendPackets(S_PacketBox.EXPOSE_WEAKNESS_DISABLE);
	}
    
    /**
     * 트루타겟 대상 대미지 증가량
     * @param dmg
     * @return damage
     */
    double calcTrueTarget(double dmg) {
    	if (_target.true_target_level >= 90) {
    		return dmg * 0.06D;
		}
    	if (_target.true_target_level >= 75) {
			return dmg * 0.05D;
		}
    	if (_target.true_target_level >= 60) {
			return dmg * 0.04D;
		}
    	if (_target.true_target_level >= 45) {
			return dmg * 0.03D;
		}
    	if (_target.true_target_level >= 30) {
			return dmg * 0.02D;
		}
    	if (_target.true_target_level >= 15) {
			return dmg * 0.01D;
		}
    	return 0D;
    }
    
    // ●●●● 파이널 번  ●●●●
    int calcFinalBurn() {
    	int level		= _pc.getLevel();
    	int finalBurn	= 5;// 기본 5%
		if (level >= 92) {// 90레벨 이상부터 2레벨당 2%씩 증가
			finalBurn += ((level + ~0x00000059) >> 1) << 1;
			if (finalBurn > 15) {// 최대 15%
				finalBurn = 15;
			}
		}
		return finalBurn;
    }
    
    // ●●●● 플레이어의 치명타 대미지  ●●●●
    int calcCritical(int weaponDamage, int weaponMaxDamage){
    	if (random.nextInt(100) + 1 <= _statusCritical) {
			_isCritical = true;
			return weaponDamage = weaponMaxDamage + _weaponAddDmg;
		}
    	if (L1ArmorSkill.valakasArmor(_pc, _weaponType, false)) {
			return weaponDamage = weaponMaxDamage + _weaponAddDmg;
    	}
    	return weaponDamage;
    }
    
    // ●●●● 창기사 원거리 폼 대미지  ●●●●
    double calcLancerFormDamage(L1Character cha, double dmg){
    	int range = (int) _pc.getLocation().getLineDistance(cha.getLocation());
    	if (range <= 2) {
    		return dmg;
    	}
		double rangeDmg = dmg * (range * 0.06D);// 1거리당 대미지 6프로씩 차감
		if (_pc.isPassiveStatus(L1PassiveId.INCREASE_RANGE)) {
			// 10% 증가, 90레벨부터 2레벨당 2%씩 상승, 최대 20%
			int level = _pc.getLevel();
			double rate = 0.1D + (level >= 90 ? (((level + ~0x00000059) >> 1) << 1) * 0.01D : 0.0D);
			if (rate > 0.2D) {
				rate = 0.2D;
			}
			rangeDmg -= rangeDmg * rate;
		}
		dmg -= rangeDmg;
    	return dmg;
    }

	// ●●●● 플레이어의 대미지 강화 마법 ●●●●
	double calcBuffDamage(double dmg) {
		if ((_pc.isPassiveStatus(L1PassiveId.BURNING_SPRITS)
				|| _pc.getSkill().hasSkillEffect(L1SkillId.BRAVE_MENTAL)
				|| _pc.getSkill().hasSkillEffect(L1SkillId.QUAKE)
				|| _pc.getSkill().hasSkillEffect(L1SkillId.ELEMENTAL_FIRE))
				&& !_isLongWeapon && !_isKeyringk) {
			if ((random.nextInt(100) + 1) <= 33) {
				double tempDmg = dmg;
				if (_pc.getSkill().hasSkillEffect(L1SkillId.BURNING_WEAPON)) {
					tempDmg += ~0x00000005;// -6
				}
				if (_pc.getSkill().hasSkillEffect(L1SkillId.BERSERKERS)) {
					tempDmg += ~0x00000001;// -2
				}
				double diffDmg = dmg - tempDmg;
				dmg = tempDmg * 1.5D + diffDmg;
				if (_pc.isPassiveStatus(L1PassiveId.BURNING_SPRITS) && _doubleBurning) {
					_pc.send_effect(_calcType == PC_PC ? _targetPc.getId() : _targetNpc.getId(), 6532);
					_doubleBurning = false;
				} else {
					_pc.send_effect(_calcType == PC_PC ? _targetPc.getId() : _targetNpc.getId(), 7727);
				}
			}
		} 
		if (_pc.getSkill().hasSkillEffect(L1SkillId.BLOW_ATTACK) && !_isLongWeapon) {// 블로우 어택
			int chance = _pc.getLevel() <= 75 ? 10 : 10 + (_pc.getLevel() + ~0x0000004A);// 기본 10
			if ((random.nextInt(100) + 1) <= chance) {
				double tempDmg = dmg;
				if (_pc.getSkill().hasSkillEffect(L1SkillId.BERSERKERS)) {
					tempDmg += ~0x00000001;// -2
				}
				double diffDmg = dmg - tempDmg;
				dmg = tempDmg * 1.5D + diffDmg;
				_pc.send_effect(_calcType == PC_PC ? _targetPc.getId() : _targetNpc.getId(), 17223);
			}
		} 
		if (_pc.getSkill().hasSkillEffect(L1SkillId.CYCLONE) && _isBow) {
        	int chance = _pc.getLevel() < 84 ? 6 : 6 + (int)((_pc.getLevel() + ~0x00000053) >> 1);// 기본 6
        	if ((random.nextInt(100) + 1) <= (chance > 20 ? 20 : chance)) {
                dmg += (int) dmg >> 1;// 1.5배
                _pc.send_effect(_calcType == PC_PC ? _targetPc.getId() : _targetNpc.getId(), 17557);
        	}
        }
		if (_pc._isBurningShot && _isBow) {
			dmg *= 1.3D;// 30% 상승
		}
		if (_pc.isPassiveStatus(L1PassiveId.RAMPAGE) && (_target.isStun() || _target.isHold())) {
			int chance = 30 + (_pc.getLevel() + ~0x0000004F);
			if ((random.nextInt(100) + 1) <= (chance > 48 ? 48 : chance)) {
                dmg += (int) dmg >> 1;// 1.5배
                _pc.broadcastPacketWithMe(new S_AttackCritical(_pc, _targetId, 24), true);
        	}
		}
		if (_pc.isPassiveStatus(L1PassiveId.DEADLY_STRIKE) && (random.nextInt(100) + 1) <= 5) {
			dmg += dmg + ((int)dmg >> 1);// 2.5배
			_pc.send_effect(_calcType == PC_PC ? _targetPc.getId() : _targetNpc.getId(), 19367);
		}
		return dmg;
	}
	
	boolean isDodgeBreak(){
		if (!_pc.isLancer()) {
			return false;
		}
		if (_pc.isPassiveStatus(L1PassiveId.DODGE_BREAK)) {
			int chance = 15 + (_pc.getLevel() > 80 ? (_pc.getLevel() + ~0x0000004F) : 0);
			if (chance > 30) {
				chance = 30;
			}
			if (random.nextInt(100) + 1 < chance) {
				_pc.send_effect(19384);
				_hitRate = 100;
				return true;
			}
		}
		return false;
	}
	
	boolean calcTargetDg(){
    	return (random.nextInt(120) + 1 <= _targetPc.ability.getDg());
    }
    
    boolean calcTargetEr(){
    	return (random.nextInt(120) + 1 <= _targetPc.ability.getPlusEr());
    }
    
    boolean calcMaanBuff(){
    	if ((_targetPc.getSkill().hasSkillEffect(L1SkillId.ANTA_MAAN) || _targetPc.getSkill().hasSkillEffect(L1SkillId.BIRTH_MAAN) 
    		|| _targetPc.getSkill().hasSkillEffect(L1SkillId.SHAPE_MAAN) || _targetPc.getSkill().hasSkillEffect(L1SkillId.LIFE_MAAN)
    		|| _targetPc.getSkill().hasSkillEffect(L1SkillId.ABSOLUTE_MAAN)) && random.nextInt(120) + 1 <= 10) {
    		return false;
    	}
    	return true;
    }
    
    double calcKeyringkDamage(int weaponTotalDamage){
    	double dmg = weaponTotalDamage + _statusDamage + (random.nextInt(_pc.getAbility().getTotalInt()) + 1) + (random.nextInt(_pc.getAbility().getSp()) + 1) + Config.SPELL.KEYRINK_DMG;
    	return calcMrDefense(_target.getResistance().getEffectedMrBySkill(), dmg);
    }
    
    int calcBurningSlash(L1Character cha){
    	if (_pc.getSkill().hasSkillEffect(L1SkillId.BURNING_SLASH) && !_isLongWeapon) {
    		_pc.send_effect((cha instanceof L1PcInstance) ? _targetPc.getId() : _targetNpc.getId(), 6591);
    		_pc.getSkill().removeSkillEffect(L1SkillId.BURNING_SLASH);
    		return 7;
        }
    	return 0;
    }
    
    int calcCrash(L1Character cha){
    	if (_pc.isPassiveStatus(L1PassiveId.CRASH)) {// 크래쉬
            int chance = random.nextInt(10) + 1;
            if (2 >= chance) {// 20%
            	int crashdmg = _pc.getLevel() >> 1; // 크래쉬 : 레벨 나누기 2의 데미지
                int plusdmg = crashdmg;
                if (_pc.isPassiveStatus(L1PassiveId.FURY)) {// 퓨리 : 크래쉬에서 나온 데미지에 2배
                    chance = random.nextInt(100) + 1;
                    if (Config.SPELL.FURY >= chance) { // 퓨리 확률
                    	plusdmg = (int) crashdmg * 3;// 3배 대미지
                        // 성공시 이팩 2개 나가는거
                    	if (cha instanceof L1PcInstance) {
                    		_targetPc.send_effect(12489);
                    	} else {
                    		_targetNpc.broadcastPacket(new S_Effect(_targetNpc.getId(), 12489), true);
                    	}
                    }
                }
                // 크래쉬는 크래쉬 이팩트 그대로 처리.
                if (cha instanceof L1PcInstance) {
                	_targetPc.send_effect(12487);
                } else {
                	_targetNpc.broadcastPacket(new S_Effect(_targetNpc.getId(), 12487), true);
                }
                return plusdmg;
            }
        }
    	return 0;
    }
    
    boolean isRage(L1Character cha){
    	if (_pc.isPassiveStatus(L1PassiveId.RAGE) && Config.SPELL.RAGE_PROB >= random.nextInt(100) + 1) {
            if (cha instanceof L1PcInstance) {
            	_targetPc.send_effect(18517);
            } else {
            	_targetNpc.broadcastPacket(new S_Effect(_targetNpc.getId(), 18517), true);
            }
            return true;
        }
    	return false;
    }
    
    boolean isAbsoluteBlade(){
    	int chance = _pc.getLevel() + ~0x0000004E;// -79
    	if (chance >= 10) {
    		chance = 10;
    	}
    	if (chance >= random.nextInt(100) + 1) {
    		_targetPc.getSkill().removeSkillEffect(L1SkillId.ABSOLUTE_BARRIER);
    		_targetPc.send_effect(14539);
    		return true;
    	}
    	return false;
    }
    
    int calcCaoticDmg(){
    	if (!CAOTIC_DAMAGE_WEAPONE.contains(_weaponId)) {
    		return 0;
    	}
		int align = _pc.getAlignment();
        if (align < -30000) {
        	return 5;
        }
        if (align < -15000) {
        	return 3;
        }
        if (align < 0) {
        	return 1;
        }
    	return 0;
    }
    
    int calcEnchantDmg(){
    	if (_weaponType != null && _weaponEnchant > 6) {
    		return _weaponEnchant + ~0x00000005;// -6
    	}
    	return 0;
    }
    
    double getArmorChanceDamage(){
    	return L1ArmorSkill.getChanceDamage(_pc);
    }
    
    double getArmorChanceReduction(double dmg){
        return L1ArmorSkill.getChanceReduction(_targetPc, dmg);
    }
    
    int calcDollDamage(L1Character cha){
    	L1DollInstance doll = _pc.getDoll();
    	if (doll == null) {
    		return 0;
    	}
    	int dmg = 0;
		if (!_isLongWeapon) {
			dmg += doll.getShortDamageChanceByDoll();
		}
		dmg += doll.attackMagicDamage(_pc, cha instanceof L1PcInstance ? _targetPc : _targetNpc);
		
		L1Potential potential = doll.getPotential();
		if (potential == null) {
			return dmg;
		}
		if (potential.getHpStill() > 0) {
			actionDollPotentialDrainHP(potential.getHpStill(), potential.getStillChance());
		} else if (potential.getMpStill() > 0) {
			actionDollPotentialDrainMP(potential.getMpStill(), potential.getStillChance());
		} else if (potential.getSkilId() != -1) {
			actionDollPotentialSpell(potential.getSkilId(), potential.getSkillChance(), _target);
		}
    	return dmg;
    }
    
    int calcInfinitiArmor(){
    	if (_targetPc.isPassiveStatus(L1PassiveId.INFINITI_ARMOR) && _targetPc.getLevel() >= 45) {
    		int infinitiarmor = 5;// 기본 5
    		if (_targetPc.getLevel() > 86) {
    			infinitiarmor += ((_targetPc.getLevel() + ~0x00000055) >> 1);// 2레벨당 1
    		}
        	if (infinitiarmor > 15) {
        		infinitiarmor = 15;
        	}
        	return infinitiarmor;
        }
    	return 0;
    }
    
    void doNaturesTouch(){
    	if (_targetPc.getSkill().hasSkillEffect(L1SkillId.NATURES_TOUCH) && random.nextInt(100) + 1 <= 5) {
    		int gethp = 10 + random.nextInt(10); // 회복률 = 기본50회복+랜덤(1~30) //원래 랜덤수치 30임
    		if (_targetPc.getSkill().hasSkillEffect(L1SkillId.POLLUTE_WATER) || _targetPc.getSkill().hasSkillEffect(L1SkillId.MOB_POLLUTE_WATER)) {
    			gethp >>= 1; // 플루트워터경우절반 //원래 랜덤수치 30임
    		}
    		if (_targetPc.getSkill().hasSkillEffect(L1SkillId.WATER_LIFE)) {
    			gethp <<= 1; // 워터라이프경우두배 //원래 랜덤수치 30임
    		}
    		_targetPc.setCurrentHp(_targetPc.getCurrentHp() + gethp);
    		_targetPc.send_effect(18930);
        }
    }

    /**
     * 타겟의 속성 방어에 따른 속성 대미지 계산
     * @return dmg
     */
    double calcAttrDmg() {
        double attrDmg = 0;
        switch (_weaponAttrLevel) {
        case 1:case 2:case 3:case 4:case 5:
        	attrDmg += (_weaponAttrLevel + ~0x00000000) + 1;
        	attrDmg -= attrDmg * _target.getResistance().getFire() / 100;
            break;
        case 6:case 7:case 8:case 9:case 10:
        	attrDmg += (_weaponAttrLevel + ~0x00000005) + 1;
        	attrDmg -= attrDmg * _target.getResistance().getWater() / 100;
            break;
        case 11:case 12:case 13:case 14:case 15:
        	attrDmg += (_weaponAttrLevel + ~0x0000000A) + 1;
        	attrDmg -= attrDmg * _target.getResistance().getWind() / 100;
            break;
        case 16:case 17:case 18:case 19:case 20:
        	attrDmg += (_weaponAttrLevel + ~0x0000000F) + 1;
        	attrDmg -= attrDmg * _target.getResistance().getEarth() / 100;
            break;
        default:
        	attrDmg = 0;
        	break;
        }
        if (_target instanceof L1NpcInstance) {
        	attrDmg += calcNpcWeakAttrDmg();
        }
        return attrDmg;
    }

    /**
     * NPC 취약 속성에 따른 속성 대미지 계산
     * @return dmg
     */
    int calcNpcWeakAttrDmg() {
		int attrDmg = 0;
		switch (_targetNpc.getNpcTemplate().getWeakAttr()) {
		case EARTH: // 땅 취약 몬스터
			if (_pc.getSkill().hasSkillEffect(L1SkillId.BUFF_CONSTITUTION2)) {
				attrDmg += 2;
			}
			if (_weaponAttrLevel >= 15 && _weaponAttrLevel <= 20) {
				attrDmg += 1 + (_weaponAttrLevel + ~0x0000000E);
			}
			return attrDmg;
		case WATER: // 물 취약 몬스터
			if (_pc.getSkill().hasSkillEffect(L1SkillId.BUFF_CRAFTSMANSHIP)) {
				attrDmg += 2;
			}
			if (_weaponAttrLevel >= 6 && _weaponAttrLevel <= 10) {
				attrDmg += 1 + (_weaponAttrLevel + ~0x00000005);
			}
			return attrDmg;
		case FIRE: // 불 취약 몬스터
			if (_pc.getSkill().hasSkillEffect(L1SkillId.BUFF_INTELLIGENCE2)) {
				attrDmg += 2;
			}
			if (_weaponAttrLevel >= 1 && _weaponAttrLevel <= 5) {
				attrDmg += 1 + (_weaponAttrLevel + ~0x00000000);
			}
			return attrDmg;
		case WIND: // 바람 취약 몬스터
			if (_pc.getSkill().hasSkillEffect(L1SkillId.BUFF_AGILITY2)) {
				attrDmg += 2;
			}
			if (_weaponAttrLevel >= 11 && _weaponAttrLevel <= 15) {
				attrDmg += 1 + (_weaponAttrLevel + ~0x0000000A);
			}
			return attrDmg;
		default:
			return 0;
		}
	}

    // ●●●● 무기의 재질과 축복에 의한 추가 대미지 산출 ●●●●
    int calcMaterialOrBlessDamage() {
        int damage = 0;
        L1Undead undead = _targetNpc.getNpcTemplate().getUndead(); //1:언데드, 2:데몬, 3:언데드보스, 4:드라니움
        if (Material.isUndeadMaterial(_weaponMaterial) && L1Undead.isHolyUndead(undead)) {// 은·미스릴·오리하르콘
            damage += random.nextInt(20) + 5;
        } else if (_weaponMaterial == Material.DRANIUM && undead == L1Undead.DRANIUM) {// 드라니움 재질
        	damage += random.nextInt(20) + 5;
        }
        
        if ((_weaponBless == 0 || _weaponBless == 128) && L1Undead.isBlessUndead(undead)) {// 축복 무기
            damage += random.nextInt(5) + 1;
        }
        if (weapon != null && !_isLongWeapon && _pc.getSkill().hasSkillEffect(L1SkillId.HOLY_WEAPON) && L1Undead.isHolyUndead(undead)) {
            damage += 1;
        }
        return damage;
    }
    
    int calcDraniumReduction(){
    	L1ItemInstance item = _targetPc.getInventory().getEquippedArmor();// 착용중인 갑옷
    	if (item == null) {
    		return 0;
    	}
    	if (item.getItem().getMaterial() != Material.DRANIUM) {
    		return 0;
    	}
    	if (_npc.getNpcTemplate().getUndead() == L1Undead.DRANIUM) {
    		return 5;// 1:언데드, 2:데몬, 3:언데드보스, 4:드라니움
    	}
    	return 0;
    }

    // ●●●● NPC의 언데드의 야간 공격력의 변화 ●●●●
    boolean isUndeadDamage() {
    	L1Undead undead = _npc.getNpcTemplate().getUndead();
        return GameTimeNight.isNight() && (undead == L1Undead.UNDEAD || undead == L1Undead.UNDEAD_BOSS || _npc.getMapId() == 70);
    }

    // ●●●● NPC의 독공격을 부가 ●●●●
    void addNpcPoisonAttack(L1Character attacker, L1Character target) {
    	L1PoisonType poison = _npc.getNpcTemplate().getPoisonAtk();
    	if (poison == L1PoisonType.NONE) {
    		return;
    	}
        if (10 >= random.nextInt(100) + 1) { // 10%의 확률로 독공격
        	switch(poison){
        	case DAMAGE:// 통상독
        		L1DamagePoison.doInfection(attacker, target, 3000, 5);
        		break;
        	case SILENCE:// 침묵독
        		L1SilencePoison.doInfection(target);
        		break;
        	case PARALYSIS:// 마비독
        		L1ParalysisPoison.doInfection(target, 20000, 4000);
        		break;
        	default:
        		break;
        	}
        }
    }

    public void getDollDrainHP(L1PcInstance pc, L1Character target) {
        int pcInt = pc.getAbility().getTotalInt();
        _drainHp = (random.nextInt(5) + pcInt + _weaponEnchant) / 3;
        if (_drainHp > 0 && target.getCurrentHp() > 0) {
            if (_drainHp > target.getCurrentHp()) {
            	_drainHp = target.getCurrentHp();
            }
            short newHp = (short) (target.getCurrentHp() - _drainHp);
            target.setCurrentHp(newHp);
            newHp = (short) (_pc.getCurrentHp() + _drainHp);
            pc.setCurrentHp(newHp);
        }
    }

    public static double calcMrDefense(int MagicResistance, double dmg) {
		double cc = 0.0D;
		if (MagicResistance <= 19) {
			cc = 0.05D;
		} else if (MagicResistance <= 29) {
			cc = 0.07D;
		} else if (MagicResistance <= 39) {
			cc = 0.1D;
		} else if (MagicResistance <= 49) {
			cc = 0.12D;
		} else if (MagicResistance <= 59) {
			cc = 0.17D;
		} else if (MagicResistance <= 69) {
			cc = 0.20D;
		} else if (MagicResistance <= 79) {
			cc = 0.22D;
		} else if (MagicResistance <= 89) {
			cc = 0.25D;
		} else if (MagicResistance <= 99) {
			cc = 0.27D;
		} else if (MagicResistance <= 110) {
			cc = 0.31D;
		} else if (MagicResistance <= 120) {
			cc = 0.32D;
		} else if (MagicResistance <= 130) {
			cc = 0.34D;
		} else if (MagicResistance <= 140) {
			cc = 0.36D;
		} else if (MagicResistance <= 150) {
			cc = 0.38D;
		} else if (MagicResistance <= 160) {
			cc = 0.40D;
		} else if (MagicResistance <= 170) {
			cc = 0.42D;
		} else if (MagicResistance <= 180) {
			cc = 0.44D;
		} else if (MagicResistance <= 190) {
			cc = 0.46D;
		} else if (MagicResistance <= 200) {
			cc = 0.48D;
		} else if (MagicResistance <= 220) {
			cc = 0.49D;
		} else {
			cc = 0.51D;
		}
		dmg -= dmg * cc;
		if (dmg < 0) {
			dmg = 0;
		}
		return dmg;
	}

    // ■■■■ PC의 독공격을 부가 ■■■■
    public void addPcPoisonAttack(L1Character attacker, L1Character target) {
        if ((_weaponId == 13 || _weaponId == 44 // 핑거 오브 데스, 고대의 다크엘프 검
        		|| (_weaponId != 0 && _pc.getSkill().hasSkillEffect(L1SkillId.ENCHANT_VENOM))) 
        		&& random.nextInt(100) + 1 <= 10) {
            L1DamagePoison.doInfection(attacker, target, 3000, 30);
        }
    }

    /* ■■■■■■■■■■■■■■ 공격 모션 송신 ■■■■■■■■■■■■■■ */
    public void action() {
        try {
            if (_calcType == PC_PC || _calcType == PC_NPC) {
                if (_isCritical) {
                    criticalPc();
                    _isCritical = false;
                } else {
                	actionPc();
                }
            } else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
            	actionNpc();
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    // ●●●● 플레이어의 공격 모션 송신 ●●●●
    void actionPc() {
    	int heading = _pc.targetDirection(_targetX, _targetY);
        if (_pc.getMoveState().getHeading() != heading) {
        	_pc.getMoveState().setHeading(heading); // 방향세트
        }
        if (_pc._isLancerForm && _isRange2Weapon) {
        	_pc.broadcastPacketWithMe(new S_AttackLancerPacket(_pc, _targetId, _weaponType.getSprite(), _targetX, _targetY, _isHit), true);
        } else if (_isKeyringk) {
        	_pc.broadcastPacketWithMe(new S_AttackKeyringkPacket(_pc, _targetId, _weaponType.getSprite(), _targetX, _targetY, _isHit), true);
        	if (_isHit) {
        		if (_calcType == PC_PC) {
        			_targetPc.send_effect(21122);
        		} else {
        			_targetNpc.broadcastPacket(new S_Effect(_targetId, 21122), true);
        		}
        	}
        } else if (_isBow) {
        	if (_arrow == null) {
        		if (ARROW_FREE_BOW.contains(_weaponId)) {// 무형 화살
                    _pc.broadcastPacketWithMe(new S_UseArrowSkill(_pc, _targetId, 2349, _targetX, _targetY, _isHit), true);
                } else {
                	return;
                }
        	} else {
        		if (!_pc.noPlayerCK && !(_pc instanceof L1AiUserInstance) && _arrow.getCount() == 1) {
                	_pc.getInventory().setArrow(null);
                }
                _pc.getInventory().removeItem(_arrow, 1);
    			_pc.broadcastPacketWithMe(new S_UseArrowSkill(_pc, _targetId, _pc.getArrowStingSprite(true), _targetX, _targetY, _isHit), true);
        	}
        } else if (_isGauntlet && _sting != null) {
            _pc.getInventory().removeItem(_sting, 1);
            _pc.broadcastPacketWithMe(new S_UseArrowSkill(_pc, _targetId, _pc.getArrowStingSprite(false), _targetX, _targetY, _isHit), true);
        } else {
            if (_isHit) {
            	_pc.broadcastPacketWithMe(new S_AttackPacket(_pc, _targetId, ActionCodes.ACTION_Attack, _attackType), true);
            } else {
                if (_targetId > 0) {
                    _pc.broadcastPacketWithMe(new S_AttackMissPacket(_pc, _targetId), true);
                } else {
                    _pc.broadcastPacketWithMe(new S_AttackPacket(_pc, 0, ActionCodes.ACTION_Attack), true);
                }
            }
        }
        if (_isHit) {
        	_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc, true);
        }
    }

    // ●●●● NPC의 공격 모션 송신 ●●●●
    void actionNpc() {
        int _npcObjectId	= _npc.getId();
        int bowSpriteId		= 0; 
        int doppelRanged	= 0;
        int heading			= _npc.targetDirection(_targetX, _targetY);
        if (_npc.getMoveState().getHeading() != heading) {// 방향세트
        	_npc.getMoveState().setHeading(heading);
        }

        // 타겟과의 거리가 2이상 있으면 원거리 공격
        boolean isDoppel	= _npc instanceof L1DoppelgangerInstance;
        boolean isLongRange = _isLongAttackNpc;
        bowSpriteId			= _npc.getNpcTemplate().getBowSpriteId();
        doppelRanged		= _npc.getDopelRanged();

        int actId = getActId() > 0 ? getActId() : ActionCodes.ACTION_Attack;
        /** 펫 콤보 액션**/
		if (_npc instanceof L1PetInstance && ((L1PetInstance)_npc).isCombo() && _petCombo) {
			actId = 18;
		}
		
        if (isDoppel && isLongRange && doppelRanged == 12) {
        	_npc.broadcastPacket(new S_UseArrowSkill(_npc, _targetId, 66, _targetX, _targetY, _isHit), true);
        } else if (isDoppel && doppelRanged == 4 && ((L1DoppelgangerInstance) _npc).getClassId() == 6) {
        	_npc.broadcastPacket(new S_AttackKeyringkPacket(_npc, _targetId, 21081, _targetX, _targetY, _isHit), true);
        	if (_isHit) {
        		if (_calcType == NPC_PC) {
        			_targetPc.send_effect(21122);
        		} else {
        			_targetNpc.broadcastPacket(new S_Effect(_targetId, 21122), true);
        		}
        	}
        } else if (isLongRange && bowSpriteId > 0) {
        	_npc.broadcastPacket(new S_UseArrowSkill(_npc, _targetId, bowSpriteId, _targetX, _targetY, _isHit), true);
        } else {
            if (_isHit) {
            	if (isDoppel && doppelRanged == 1 && ((L1DoppelgangerInstance) _npc).getClassId() == 4) {
            		if (random.nextInt(10) + 1 <= 3) {
            			_npc.broadcastPacket(new S_Effect(_npc.getId(), 3398), true);// 더블 이팩
            		}
        			if (random.nextInt(10) + 1 <= 1) {
        				_npc.broadcastPacket(new S_Effect(_targetId, 6532), true);// 버닝 이팩트
        			}
            	}
                if (getGfxId() > 0) {
                	_npc.broadcastPacket(new S_UseAttackSkill(_target, _npcObjectId, getGfxId(), _targetX, _targetY, actId), true);
                } else {
                	_npc.broadcastPacket(new S_AttackPacketForNpc(_target, _npcObjectId, actId), true);
                }
                _target.broadcastPacketExceptTargetSight(new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _npc, true);
            } else {
                if (getGfxId() > 0) {
                	_npc.broadcastPacket(new S_UseAttackSkill(_target, _npcObjectId, getGfxId(), _targetX, _targetY, actId, 0), true);
                } else {
                	_npc.broadcastPacket(new S_AttackMissPacket(_npc, _targetId, actId), true);
                }
            }
        }
    }
    
    // ●●●● 플레이어의 크리티컬 공격 모션 송신 ●●●●
    void criticalPc() {
    	int heading = _pc.targetDirection(_targetX, _targetY);
        if (_pc.getMoveState().getHeading() != heading) {
        	_pc.getMoveState().setHeading(heading); // 방향세트
        }
        int criticalId = _weaponType.getCritical();
        if (_isKeyringk) {
        	_pc.broadcastPacketWithMe(new S_AttackKeyringkPacket(_pc, _targetId, criticalId, _targetX, _targetY, _isHit), true);
        	if (_isHit) {
        		if (_calcType == PC_PC) {
        			_targetPc.send_effect(21124);
        		} else {
        			_targetNpc.broadcastPacket(new S_Effect(_targetId, 21124), true);
        		}
        	}
        } else if (_isBow || (_isGauntlet && _sting != null) || (_pc._isLancerForm && _isRange2Weapon)) {
            _pc.broadcastPacketWithMe(new S_AttackCritical(_pc, _targetId, _targetX, _targetY, criticalId, _isHit), true);
        } else {
            if (_pc.isWarrior() && _isBlunt) {
            	criticalId = 13415;
            }
            _pc.broadcastPacketWithMe(new S_AttackCritical(_pc, _targetId, criticalId), true);
        }
    }
    
    boolean isMeisterAccuracy(){
    	if (!_pc.isWizard() || _target.isBind() || _target.isAbsol()) {
    		return false;
    	}
    	if (!random.nextBoolean()) {// 50% 확률
    		return false;
    	}
    	_damage += (int) calcWeaponSpellDmg(_pc, _target);
    	return true;
    }

    /* ■■■■■■■■■■■■■■■ 계산 결과 반영 ■■■■■■■■■■■■■■■ */
    public void commit() {
        if (_isHit) {
            try {
            	if (_calcType == PC_PC || _calcType == NPC_PC) {
            		commitPc();
            	} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
                	commitNpc();
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
        } else {
        	if ((_calcType == PC_PC || _calcType == PC_NPC) && _pc.isPassiveStatus(L1PassiveId.MEISTER_ACCURACY) && isMeisterAccuracy()) {
    			try {
                    if (_calcType == PC_PC) {
                    	commitPc();
                    } else if (_calcType == PC_NPC) {
                    	commitNpc();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        	}
        }

        // 대미지치 및 명중율 확인용 메세지
        if (!Config.ALT.ALT_ATKMSG) {
        	return;
        }
        if ((_calcType == PC_PC || _calcType == PC_NPC) && !_pc.isGm()) {
        	return;
        }
        if ((_calcType == PC_PC || _calcType == NPC_PC) && !_targetPc.isGm()) {
        	return;
        }
        
        String msg0 = StringUtil.EmptyString;
        String msg1 = StringUtil.EmptyString;
        String msg2 = StringUtil.EmptyString;
        String msg3 = StringUtil.EmptyString;
        if (_calcType == PC_PC || _calcType == PC_NPC) {// 어텍커가 PC의 경우
            msg0 = _pc.getName();
        } else if (_calcType == NPC_PC) {// 어텍커가 NPC의 경우
            msg0 = _npc.getName();
        }
        if (_calcType == NPC_PC || _calcType == PC_PC) { // 타겟이 PC의 경우
            msg3 = _targetPc.getName();
            msg1 = "HP:" + _targetPc.getCurrentHp() + " / HR:" + _hitRate;
        } else if (_calcType == PC_NPC) { // 타겟이 NPC의 경우
            msg3 = _targetNpc.getName();
            msg1 = "HP:" + _targetNpc.getCurrentHp() + " / HR:" + _hitRate;
        }
        msg2 = "DMG:" + _damage;

        if (_calcType == PC_PC || _calcType == PC_NPC) {// 어텍커가 PC의 경우
            _pc.sendPackets(new S_SystemMessage("\\fR[" + msg0 + "->" + msg3 + "] " + msg2 + " / " + msg1, true), true);
        }
        if (_calcType == NPC_PC || _calcType == PC_PC) {// 타겟이 PC의 경우
            _targetPc.sendPackets(new S_SystemMessage("\\fY[" + msg0 + "->" + msg3 + "] " + msg2 + " / " + msg1, true), true);
        }
    }

    // ●●●● 플레이어에 계산 결과를 반영 ●●●●
    void commitPc() {
        if (_calcType == PC_PC) {
            if (_targetPc.isBind()) {
                _damage = _drainMana = _drainHp = 0;
            }
            if (_drainMana > 0 && _targetPc.getCurrentMp() > 0) {
                if (_drainMana > _targetPc.getCurrentMp()) {
                	_drainMana = _targetPc.getCurrentMp();
                }
                short newMp = (short) (_targetPc.getCurrentMp() - _drainMana);
                _targetPc.setCurrentMp(newMp);
                newMp = (short) (_pc.getCurrentMp() + _drainMana);
                _pc.setCurrentMp(newMp);
            }
            
            /** 플레임 **/
            if (_pc.isPassiveStatus(L1PassiveId.FRAME)) {
            	doFram();
            }

            /** 피흡수 **/
            if (_drainHp > 0 && _targetPc.getCurrentHp() > 1) {
                if (_drainHp >= _targetPc.getCurrentHp()) {
                	_drainHp = _targetPc.getCurrentHp() + ~0x00000000;
                }
                short newHp = (short) (_targetPc.getCurrentHp() - _drainHp);
                _targetPc.setCurrentHp(newHp);// 피격자 hp 감소
                newHp = (short) (_pc.getCurrentHp() + _drainHp);
                _pc.setCurrentHp(newHp);// 공격자 hp 추가
            }
            
            if (_damage > 0 && _targetPc.isCrown() && _targetPc.getSkill().hasSkillEffect(L1SkillId.BRAVE_UNION)) {
            	_targetPc.receiveDamageFromBraveUnion(_pc, _damage);
			} else {
				_targetPc.receiveDamage(_pc, _damage);
			}
        } else if (_calcType == NPC_PC) {
            if (_targetPc.isBind()) {
                _damage = 0;
            }
            _targetPc.receiveDamage(_npc, _damage);
        }
    }

    // ●●●● NPC에 계산 결과를 반영 ●●●●
    void commitNpc() {
        if (_calcType == PC_NPC) {
            if (_targetNpc.isBind()) {
                _damage = _drainMana = _drainHp = 0;
            }
            
            if (_targetNpc instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) _targetNpc;
				if (mon.kirtasCounterBarrier || mon.titanCounterBarrier) {
					_pc.receiveDamage(_pc, _damage);
					return;
				}
				if (mon.kirtasPoisonBarrier || mon.titanPoisonBarrier) {
					if (15 >= random.nextInt(100) + 1) {// 15%의 확률로 독공격
						L1DamagePoison.doInfection(_targetNpc, _pc, 3000, 100 + random.nextInt(50));
					}
				} else if (mon.kirtasAbsolute || mon._vallacasFly) {
					_damage = _drainMana = _drainHp = 0;
				} else if (mon.getNpcTemplate().getNpcId() >= 800550 && mon.getNpcTemplate().getNpcId() <= 800555) {
					if ((mon.getMapId() >= 15482 && mon.getMapId() <= 15484) && (_pc.getClanid() == 0 || _pc.getClan().getCastleId() != 0)) {
						_damage = _drainMana = _drainHp = 0;
					}
				}
			}
            
            if (_drainMana > 0) {
                int drainValue = _targetNpc.drainMana(_drainMana);
                int newMp = _pc.getCurrentMp() + drainValue;
                _pc.setCurrentMp(newMp);

                if (drainValue > 0) {
                    int newMp2 = _targetNpc.getCurrentMp() - drainValue;
                    _targetNpc.setCurrentMp(newMp2);
                }
            }
            
            if (_pc.isPassiveStatus(L1PassiveId.FRAME)) {
            	doFram();// 플레임
            }
            
            // 피흡수
            if (_drainHp > 0) {
                int newHp = _pc.getCurrentHp() + _drainHp;
                _pc.setCurrentHp(newHp);
            }

            damageNpcWeaponDurability(); // 무기를 손상시킨다.

            _targetNpc.receiveDamage(_pc, _damage);
        } else if (_calcType == NPC_NPC) {
            if (_targetNpc.isBind()) {
                _damage = 0;
            }
            _targetNpc.receiveDamage(_npc, _damage);
        }
    }
    
    /* ■■■■■■■■■■■■■■■ 플레임 ■■■■■■■■■■■■■■■ */
    void doFram(){
    	if (_target.getSkill().hasSkillEffect(L1SkillId.STATUS_FRAME)) {
    		return;
    	}
        if (weapon != null && random.nextInt(100) + 1 <= 5) {
        	if (_calcType == PC_PC) {
        		_targetPc.sendPackets(S_SpellBuffNoti.FRAME_ON);
        	}
        	int dmg = random.nextInt(_weaponSmall) + _weaponAddDmg + _weaponEnchant + _pc.getAbility().getTotalStr();
        	new L1FrameDmg(_pc, _target, dmg);
        }
    }
    
    // ■■■■ 상대의 근거리 공격에 대해서 유효한가를 판별 ■■■■
    public boolean isShortDistance() {
        boolean isShortDistance = true;
        if (_calcType == PC_PC) {
            if (_isLongWeapon || _isKeyringk || _pc.getSkill().hasSkillEffect(L1SkillId.ARMOR_BREAK)) {
            	isShortDistance = false;
            }
        } else if (_calcType == NPC_PC) {
            if (_npc == null) {
            	return false;
            }
            int bowSpriteId = _npc.getNpcTemplate().getBowSpriteId();
            int doppelRanged = _npc.getDopelRanged();
            // 거리가 2이상, 공격자의 활의 액션 ID가 있는 경우는 원공격
            if (_isLongAttackNpc && bowSpriteId > 0) {
            	return false;
            }
            if (_isLongAttackNpc && doppelRanged >= 12) {
            	return false;
            }
        }
        return isShortDistance;
    }

    /* ■■■■■■■■■■■■■■■ 카운터 배리어 몬스터 ■■■■■■■■■■■■■■■ */
    boolean actionMonterCounterBarrier(int skillId){
	    if (random.nextInt(10) <= 2) {
	    	int sprite = (skillId == L1SkillId.MOB_COUNTER_BARRIER_BETERANG) ? 17220 : 10710;
	    	if (_pc.is_reflect_emasculate(null)) {
	    		_targetNpc.broadcastPacket(new S_Effect(_targetNpc.getId(), sprite), true); 
	    		return false;
	    	}
    		int heading = _pc.targetDirection(_targetX, _targetY);
    		if (_pc.getMoveState().getHeading() != heading) {
    			_pc.getMoveState().setHeading(heading); // 방향세트
    		}
	        _pc.broadcastPacketWithMe(new S_AttackMissPacket(_pc, _targetId), true);
	        _pc.broadcastPacketWithMe(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage), true);
	        _targetNpc.broadcastPacket(new S_Effect(_targetNpc.getId(), sprite), true); 
	        _pc.receiveDamage(_targetNpc, 100);
	        return true;
	    }
	    return false;
    }

    // ■■■■ 카운터 배리어시의 공격 모션 송신 ■■■■
    public void actionCounterBarrier() {
    	actionCounting(10710);
    }
    // ■■■■ 카운터 배리어:베테랑 시의 공격 모션 송신 ■■■■
 	public void actionCounterBarrierBeterang() {
 		actionCounting(17220);
 	}
 	// ■■■■ 카운터 배리어:마스터 시의 공격 모션 송신 ■■■■
  	public void actionCounterBarrierMaster() {
  		actionCounting(20475);
  	}
    
 	// ■■■■ 인페르노시의 공격 모션 송신 ■■■■
 	public void actionInferno() {
 		actionCounting(0);
 	}
    
    // ■■■■ 할파스의 공격 모션 송신 ■■■■
    public void actionHalphas() {
    	actionCounting(18410);
    }
    
    // ■■■■ 컨쿼러의 공격 모션 송신 ■■■■
    public void actionConcqueror() {
    	actionCounting(21808);
    }
    
    void actionCounting(int sprite_id){
 		if (_calcType == PC_PC) {
            if (_pc == null) {
            	return;
            }
            int heading = _pc.targetDirection(_targetX, _targetY);
            if (_pc.getMoveState().getHeading() != heading) {
            	_pc.getMoveState().setHeading(heading); // 방향세트
            }
            _pc.broadcastPacketWithMe(new S_AttackMissPacket(_pc, _targetId), true);
            _pc.broadcastPacketWithMe(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage), true);
            if (sprite_id > 0) {
            	_pc.send_effect(_targetId, sprite_id);
            }
        } else if (_calcType == NPC_PC) {
            if (_npc == null || _target == null) {
            	return;
            }
            int heading = _npc.targetDirection(_targetX, _targetY);
            if (_npc.getMoveState().getHeading() != heading) {
            	_npc.getMoveState().setHeading(heading); // 방향세트
            }
            int actId = getActId() > 0 ? getActId() : ActionCodes.ACTION_Attack;
            if (getGfxId() > 0) {
            	_npc.broadcastPacket(new S_UseAttackSkill(_target, _npc.getId(), getGfxId(), _targetX, _targetY, actId, 0), true);
            } else {
            	_npc.broadcastPacket(new S_AttackMissPacket(_npc, _targetId, actId), true);
            }
            _npc.broadcastPacket(new S_DoActionGFX(_npc.getId(), ActionCodes.ACTION_Damage), true);
            if (sprite_id > 0) {
            	_npc.broadcastPacket(new S_Effect(_targetId, sprite_id), true);
            }
        }
 	}

    // ■■■■ 카운터 배리어의 대미지를 반영 ■■■■
    public void commitCounterBarrier() {
        int damage = calcCounterBarrierDamage();
        if (damage == 0) {
        	return;
        }
        if (_calcType == PC_PC) {
        	_pc.receiveCountingDamage(_targetPc, damage);
        } else if (_calcType == NPC_PC) {
        	_npc.receiveCountingDamage(_targetPc, damage);
        }
    }
    
    // ■■■■ 할파스의 대미지를 반영 ■■■■
    public void commitHalphas() {
        int damage = calcHalphasDamage();
        if (damage == 0) {
        	return;
        }
        if (_calcType == PC_PC) {
        	_pc.receiveCountingDamage(_targetPc, damage);
        } else if (_calcType == NPC_PC) {
        	_npc.receiveCountingDamage(_targetPc, damage);
        }
    }
    
    // ■■■■ 컨쿼러의 대미지를 반영 ■■■■
    public void commitConcqueror() {
        int damage = calcConcquerorDamage();
        if (damage == 0) {
        	return;
        }
        if (_calcType == PC_PC) {
        	_pc.receiveCountingDamage(_targetPc, damage);
        } else if (_calcType == NPC_PC) {
        	_npc.receiveCountingDamage(_targetPc, damage);
        }
    }

    // ●●●● 카운터 배리어의 대미지를 산출 ●●●●
    int calcCounterBarrierDamage() {
        L1ItemInstance weapon = _targetPc.getWeapon();
        if (weapon != null && weapon.getItem().getType() == L1ItemWeaponType.TOHAND_SWORD.getId()) {
        	return Math.round((weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgRate()) << 1);
        }
        return 0;
    }
    
    // ●●●● 할파스의 대미지를 산출 ●●●●
    int calcHalphasDamage() {
        L1ItemInstance weapon = _targetPc.getWeapon();
        if (weapon != null && weapon.getItem().getType() == L1ItemWeaponType.CHAINSWORD.getId()) {
        	return Math.round((weapon.getItem().getDmgSmall() + weapon.getEnchantLevel() + weapon.getItem().getDmgRate()) * 3);
        }
        return 0;
    }
    
    // ●●●● 인페르노의 대미지를 산출 ●●●●
 	int calcInfernoDamage() {
 		int rnd = random.nextInt(4) + 1;
 		L1ItemInstance weapon = _targetPc.getWeapon();
 		switch(rnd){
 		case 1:_targetPc.send_effect(17561);break;
 		case 2:_targetPc.send_effect(17563);break;
 		case 3:_targetPc.send_effect(17565);break;
 		default:_targetPc.send_effect(17567);break;
 		}
 		return weapon == null ? 10 : (int) Math.round((weapon.getItem().getDmgSmall() + weapon.getEnchantLevel() + weapon.getItem().getDmgRate()) * rnd);
 	}
 	
 	// ■■■■ 인페르노의 대미지를 반영 ■■■■
 	public void commitInferno() {
 		int damage = calcInfernoDamage();
 		if (damage == 0) {
 			return;
 		}
 		switch(_calcType){
 		case PC_PC:
 			_pc.receiveCountingDamage(_targetPc, damage);
 			break;
 		case NPC_PC:
 			_npc.receiveCountingDamage(_targetPc, damage);
 			break;
 		case PC_NPC:
 			_pc.receiveCountingDamage(_targetPc, damage);
 			break;
 		}
 	}
 	
 	/* ■■■■ 인페르노의 대미지 경감 ■■■■ */
 	public int clacInfernoDamage() {
 		switch (_calcType) {
 		case PC_PC:
 			return _damage = calcPcPcDamage() >> 1;
 		case PC_NPC:
 			return _damage = calcPcNpcDamage() >> 1;
 		case NPC_PC:
 			return _damage = calcNpcPcDamage() >> 1;
 		case NPC_NPC:
 			return _damage = calcNpcNpcDamage() >> 1;
 		default:
 			return _damage;
 		}
 	}
 	
 	// ●●●● 컨쿼러의 대미지를 산출 ●●●●
    int calcConcquerorDamage() {
    	if (_targetPc == null) {
    		return 0;
    	}
        L1ItemInstance weapon = _targetPc.getWeapon();
        if (weapon == null) {
        	return 0;
        }
    	if (L1ItemWeaponType.isSwordWeapon(weapon.getItem().getWeaponType())) {
    		doConcquerorStun();
    	}
    	return Math.round((weapon.getItem().getDmgSmall() + weapon.getEnchantLevel() + weapon.getItem().getDmgRate()) << 1);
    }
    
    /**
     * 컨쿼러 카운터 발동 시 일정 확률로 스턴 발동
     */
    void doConcquerorStun() {
    	if (random.nextInt(100) + 1 > Config.SPELL.CONQUEROR_STUN_PROB) {
    		return;
    	}
    	final int duration = 2000;
    	if (_calcType == PC_PC) {
    		L1EffectSpawn.getInstance().spawnEffect(81065, duration, _pc.getX(), _pc.getY(), _pc.getMapId());
        	_pc.getSkill().setSkillEffect(L1SkillId.STATUS_CONQUEROR, duration);
        	_pc.sendPackets(S_Paralysis.STURN_ON);
        	_pc.sendPackets(new S_SpellBuffNoti(_pc, L1SkillId.STATUS_CONQUEROR, true, 2), true);
        } else if (_calcType == NPC_PC) {
        	L1EffectSpawn.getInstance().spawnEffect(81065, duration, _npc.getX(), _npc.getY(), _npc.getMapId());
        	if (_npc.getNpcTemplate().isBossMonster()) {
        		return;
        	}
        	_npc.getSkill().setSkillEffect(L1SkillId.STATUS_CONQUEROR, duration);
        	_npc.setParalyzed(true);
        }
    }

    /*
     * 무기를 손상시킨다. 대NPC의 경우, 손상 확률은5%로 한다. 축복 무기는2%로 한다.
     */
    void damageNpcWeaponDurability() {
        /*
         * 손상하지 않는 NPC, 맨손, 손상하지 않는 무기 사용, SOF중의 경우 아무것도 하지 않는다.
         */
        if (_pc.getRobotAi() != null 
        		|| _calcType != PC_NPC 
        		|| !_targetNpc.getNpcTemplate().isHard()
        		|| _weaponType == null 
        		|| weapon.getItem().getCanbeDmg() == 0
        		|| _pc.getSkill().hasSkillEffect(L1SkillId.SOUL_OF_FLAME) 
        		|| _pc.isPassiveStatus(L1PassiveId.DAMASCUS)
        		|| _pc.isPassiveStatus(L1PassiveId.SOLID_NOTE)) {
        	return;
        }
        int chance = _weaponBless == 0 || _weaponBless == 128 ? 2 : 5;// 축복 2, 일반 5
        if (random.nextInt(100) + 1 < chance) {
        	_pc.sendPackets(new S_ServerMessage(268, weapon.getLogNameRef()), true);// \f1당신의%0가 손상했습니다.
            _pc.getInventory().receiveDamage(weapon);
        }
    }

    /** 속성화살 **/
    int addAttrArrowDamage(L1NpcInstance npc) {
    	L1Attr weakAttr	= npc.getNpcTemplate().getWeakAttr();
        if (weakAttr == L1Attr.NONE) {
        	return 0;
        }
        int itemId		= _arrow.getItem().getItemId();
        if (itemId == 820014 && weakAttr == L1Attr.WATER) {
        	return 3;// 수령의 블랙 미스릴 화살
        }
        if (itemId == 820015 && weakAttr == L1Attr.WIND) {
        	return 3;// 풍령의 블랙 미스릴 화살
        }
        if (itemId == 820016 && weakAttr == L1Attr.EARTH) {
        	return 3;// 지령의 블랙 미스릴 화살
        }
        if (itemId == 820017 && weakAttr == L1Attr.FIRE) {
        	return 3;// 화령의 블랙 미스릴 화살
        }
		if (itemId == 31174 || itemId == 130040) {// 대정령의 전투 화살
			if ((_weaponAttrLevel >= 1 && _weaponAttrLevel <= 5) && weakAttr == L1Attr.FIRE) {
				return 3;// 불 속성
			}
			if ((_weaponAttrLevel >= 6 && _weaponAttrLevel <= 10) && weakAttr == L1Attr.WATER) {
				return 3;// 물 속성
			}
			if ((_weaponAttrLevel >= 11 && _weaponAttrLevel <= 15) && weakAttr == L1Attr.WIND) {
				return 3;// 바람 속성
			}
			if ((_weaponAttrLevel >= 16 && _weaponAttrLevel <= 20) && weakAttr == L1Attr.EARTH) {
				return 3;// 땅 속성
			}
		}
        return 0;
    }
    
    /** 스탯 + 무기에 따른 공격 성공률 **/
	int calcPcHit() {
		int value = 0;
		if (!_isLongWeapon) {
			value += _pc.ability.getShortHitup() + (_weaponEnchant >> 1);
			value += CalcStrStat.shortHitup(_pc.getAbility().getTotalStr());
		} else {
			value += _pc.ability.getLongHitup() + (_weaponEnchant >> 1);
			value += CalcDexStat.longHitup(_pc.getAbility().getTotalDex());
			if (_isBow && _arrow != null) {
				value += _arrow.getItem().getBowHitRate();// 화살 명중
			} else if (_isGauntlet && _sting != null) {
				value += _sting.getItem().getBowHitRate();// 스팅 명중
			}
		}
		if (weapon != null && weapon.getItem().getItemGrade().equals(L1Grade.MYTH)) {
			value += _weaponEnchant + (_weaponEnchant >> 1);
		}
		return value;
	}

    /** Hit 최종 연산 **/
    boolean isHitRateCal(int AD, int DD, int fumble, int critical) {
        if (AD <= fumble) {
            _hitRate = 0;
            return true;
        }
        if (AD >= critical) {
        	_hitRate = 100;
        } else {
            if (AD > DD) {
            	_hitRate = 100;
            } else if (AD <= DD) {
                _hitRate = 0;
                return true;
            }
        }
        return false;
    }

    /** 타겟PC DD 연산 **/
    int toPcDD(int dv) {
        return _targetPc.ac.getAc() >= 0 ? 10 - _targetPc.ac.getAc() : 10 + (dv <= 0 ? 1 : random.nextInt(dv) + 1);
    }
    
    int toPcBuffReduction(){
    	int reduc = 0;
        // 전사스킬 : 아머가드 - 캐릭의 AC/10의 데미지감소 효과를 얻는다.
        if (_targetPc.isPassiveStatus(L1PassiveId.ARMOR_GUARD)) {
        	reduc += _targetPc.ac.getAc() / 10;
        }
        
        if (_targetPc.getSkill().hasSkillEffect(L1SkillId.REDUCTION_ARMOR)) {
            int targetLevel = _targetPc.getLevel();
            if (targetLevel < 50) {
            	targetLevel = 50;
            }
            reduc += (targetLevel - 50) / 5 + 1;
        }
        if (_targetPc.getSkill().hasSkillEffect(L1SkillId.EARTH_GUARDIAN)) {
        	int add = (_targetPc.getLevel() > 80 ? (_targetPc.getLevel() + ~0x0000004F) >> 2 : 0);
        	reduc += 2 + add;
        }
		if (_targetPc.isPassiveStatus(L1PassiveId.DRAGON_SKIN)) {
			reduc += _targetPc.getLevel() >= 80 ? 5 + ((_targetPc.getLevel() + ~0x0000004F) >> 1) : 5;
		}
        if (_targetPc.isPassiveStatus(L1PassiveId.MAJESTY)) {
        	reduc += _targetPc.getLevel() >= 80 ? 2 + ((_targetPc.getLevel() + ~0x0000004F) >> 1) : 2;
        }
        if (_targetPc.getSkill().hasSkillEffect(L1SkillId.PATIENCE)) {
        	reduc += _targetPc.getLevel() >= 80 ? 2 + ((_targetPc.getLevel() + ~0x0000004F) >> 2) : 2;
        }
        if (_targetPc.isPassiveStatus(L1PassiveId.VENGEANCE)) {
        	int chance = 2, result = 20;;
        	boolean cosumItem = false;
        	if (_targetPc.isHalfHp() && _targetPc.getInventory().checkItem(L1ItemId.GEMSTONE, 5)) {
        		chance += 1;
        		result += 10;
        		cosumItem = true;
        	}
        	if ((int)(Math.random() * 10) + 1 < chance) {
        		if (cosumItem) {
        			_targetPc.getInventory().consumeItem(L1ItemId.GEMSTONE, 5);
        		}
        		reduc += result;
        		_targetPc.send_effect(19695);
        	}
        }
        if (_targetPc.isPassiveStatus(L1PassiveId.ARTERIAL_CIRCLE) && (_targetPc.isStun() || _targetPc.isHold() || _targetPc.isDesperado() || _targetPc.isOsiris() || _targetPc.isPhantom() || _targetPc.isEternity()) && random.nextInt(100)+ 1 < Config.SPELL.ARTERIALCIRCLE_PROB) {
        	reduc += 5;
        	_targetPc.send_effect(20118);
        }
        if (_targetPc.isPassiveStatus(L1PassiveId.GLORY_EARTH) && random.nextInt(100) + 1 <= 15) {
			reduc += 30;
	    	_targetPc.send_effect(19318);
		}
    	return reduc;
    }

    /** 대상 Buff에 따른 대미지 연산 **/
    double toPcBuffDamage(double dmg) {
    	if (_targetPc.ability.getDamageReductionPercent() > 0) {
    		dmg -= (dmg * 0.01D) * _targetPc.ability.getDamageReductionPercent();// 대미지 감소 퍼센트
    	}
    	if (_targetPc.getSkill().hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)) {
    		dmg -= _calcType == PC_PC && _pc.ability.getEmunEgnor() > 0 ? (dmg * (_targetPc.immunToHarmValue * (1D - ((double) _pc.ability.getEmunEgnor() * 0.01D)))) : (dmg * _targetPc.immunToHarmValue);
		}
		
		if (_targetPc.getSkill().hasSkillEffect(L1SkillId.LUCIFER)) {
			dmg -= (dmg * 0.1D);
		}
		if (_targetPc.isIllusionist() && _targetPc.isPassiveStatus(L1PassiveId.MOEBIUS)) {
			if ((_calcType == PC_PC && _isLongWeapon) || (_calcType == NPC_PC && _isLongAttackNpc)) {
				int percent = 9 + (_targetPc.getLevel() > 87 ? (int)((_targetPc.getLevel() + ~0x00000056) >> 1) : 0);
				if (percent > 15) {
					percent = 15;
				}
				dmg -= (dmg / 100) * percent;
			}
		}
		if (_targetPc.isWarrior() && _targetPc.isPassiveStatus(L1PassiveId.TITAN_BEAST)
				&& ((_calcType == PC_PC && !_isLongWeapon && !_isKeyringk) || (_calcType == NPC_PC && _npc.getNpcTemplate().getRanged() <= 4 && _npc.getNpcTemplate().getBowSpriteId() == 0)) 
				&& _targetPc.getPassiveSkill().isTitanBeastTime()) {// 타이탄 비스트 대미지감소
			dmg = (int)dmg >> 1;
        }
		if (_targetPc.getSkill().hasSkillEffect(L1SkillId.STATUS_TOMAHAWK_HUNT) && _calcType == PC_PC && _targetPc._tomahawkHuntPc == _pc && random.nextInt(10) <= 8) {
			_drainHp = random.nextInt(Config.SPELL.TOMAHAWK_HUNT_HP_RANDOM_VALUE) + Config.SPELL.TOMAHAWK_HUNT_HP_FIX_VALUE;// 토마호크:헌터 피 흡수
			_pc.send_effect_self(20600);
		}
        if (_targetPc.getSkill().hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)
        		|| _targetPc.getSkill().hasSkillEffect(L1SkillId.ICE_LANCE)
        		|| _targetPc.getSkill().hasSkillEffect(L1SkillId.EARTH_BIND)) {
        	return 0D;
        }
        return dmg;
    }
    
    /** 펫 콤보 **/
    void companionCombo() {
        if (_npc instanceof L1PetInstance) {
            L1PetInstance pet = (L1PetInstance)_npc;
            if (pet.isCombo()) {
                if (_isHit) {
                    if (pet.getComboTarget() == _target.getId()) {
                    	pet.setComboCount(pet.getComboCount() + 1);
                    } else {
                        pet.setComboTarget(_target.getId());
                        pet.setComboCount(1);
                    }
                } 
            } else {
            	pet.setComboCount(0);
            }
        }
    }
    
    /** 펫 속성 **/
    int companionAttrDamage(L1PetInstance pet) {
        switch (pet.getPetInfo().getClassT().get_element()) {
        case LIGHT:
            if (_targetNpc != null) {
            	L1Undead undead = _targetNpc.getNpcTemplate().getUndead();
                if (undead != L1Undead.UNDEAD && undead != L1Undead.UNDEAD_BOSS) {
                	return pet.get_lightElementalDmg(); 
                }
            }
            return 0;
        case FIRE:
        	int fireElementalDmg = pet.get_fireElementalDmg();
        	return fireElementalDmg -= fireElementalDmg * _target.getResistance().getFire() / 100;
        case WATER:
        	int waterElementalDmg = pet.get_waterElementalDmg();
        	return waterElementalDmg -= waterElementalDmg * _target.getResistance().getWater() / 100;
        case AIR:
        	int airElementalDmg = pet.get_airElementalDmg();
        	return airElementalDmg -= airElementalDmg * _target.getResistance().getWind() / 100;
        case EARTH:
        	int earthElementalDmg = pet.get_earthElementalDmg();
        	return earthElementalDmg -= earthElementalDmg * _target.getResistance().getEarth() / 100;
        default:
        	return 0;
        }
    }
    
    void actionDollPotentialSpell(int skillId, int chance, L1Character target) {
    	switch(skillId){
    	case L1SkillId.SOUL_OF_FLAME:
    		if (!_pc.getSkill().hasSkillEffect(skillId) && random.nextInt(100) + 1 <= chance) {
    			_pc.sendPackets(new S_Effect(_pc.getId(), 19264), true);
        		_pc.getSkill().setSkillEffect(skillId, 8000);
    		}
    		break;
    	case L1SkillId.JUDGEMENT_DOLL:
    		if (_calcType == PC_PC && !target.getSkill().hasSkillEffect(skillId) && random.nextInt(100) + 1 <= chance) {
    			S_Effect effect = new S_Effect(_targetPc.getId(), 18490);
    			if (_pc.isInParty()) {
    				for (L1PcInstance member : L1World.getInstance().getVisiblePartyPlayer(_pc, -1)) {
    					if (member == _pc) {
    						continue;
    					}
    					member.sendPackets(effect, false);
    				}
    			}
    			_pc.sendPackets(effect, true);
    			_targetPc.getSkill().setSkillEffect(skillId, 8000);
    			_targetPc._statusJudgementDoll = _pc.getType();// 공격자의 타입 세팅
    			switch(_targetPc._statusJudgementDoll){
    			case 2:// 요정
    			case 4:// 다크엘프
    				_targetPc.getResistance().addToleranceSpirit(-15);
    				break;
    			case 5:// 용기사
    			case 6:// 환술사
    				_targetPc.getResistance().addToleranceDragon(-15);
    				break;
    			case 7:// 전사
    			case 8:// 검사
    			case 9:// 창기사
    				_targetPc.getResistance().addToleranceFear(-15);
    				break;
    			case 3:// 법사
    				_targetPc.getResistance().addMr(-15);
    				break;
    			default:
    				_targetPc.getResistance().addToleranceSkill(-15);
    				break;
    			}
    		}
    		break;
    	case L1SkillId.DECAY_POTION:
    		if (_calcType == PC_PC && !target.getSkill().hasSkillEffect(skillId) && random.nextInt(100) + 1 <= chance) {
    			S_Effect effect = new S_Effect(_targetPc.getId(), 2232);
    			if (_pc.isInParty()) {
    				for (L1PcInstance member : L1World.getInstance().getVisiblePartyPlayer(_pc, -1)) {
    					if (member == _pc) {
    						continue;
    					}
    					member.sendPackets(effect, false);
    				}
    			}
    			_pc.sendPackets(effect, true);
    			_targetPc.getSkill().setSkillEffect(skillId, 4000);
        	}
    		break;
    	}
    }
    
    /**
     * 마법인형 잠재력 HP흡수
     * @param value
     */
    void actionDollPotentialDrainHP(int value, int chance) {
    	if (random.nextInt(100) + 1 <= chance) {
    		_drainHp = random.nextInt(value >> 1) + (value >> 1);
    	}
    }
    
    /**
     * 마법인형 잠재력 MP흡수
     * @param value
     */
    void actionDollPotentialDrainMP(int value, int chance) {
    	if (random.nextInt(100) + 1 <= chance) {
    		_drainMana = random.nextInt(value) + 1;
    	}
    }
    
    /**
     * HP or MP흡수 무기
     * @param pc
     * @param target
     */
    void doWeaponHpMpDrain(L1PcInstance pc, L1Character target){
    	if (_weaponSkill == null) {
    		return;
    	}
    	// HP흡수
    	if (_weaponSkill.isHpStill()) {
    		doWeaponDrainHp(pc, target);
    	}
    	// MP흡수
    	if (_weaponSkill.isMpStill()) {
    		doWeaponDrainMp(pc, target);
    	}
    }
    
    /**
     * HP를 흡수한다.
     * @param pc
     * @param target
     */
    void doWeaponDrainHp(L1PcInstance pc, L1Character target){
    	int prob = _weaponSkill.getHpStillProbability();
    	int enchantChance = _weaponSkill.getEnchantProbability() > 0 ? _weaponEnchant : 0;
    	if (prob == 0 || random.nextInt(100) + 1 <= prob + enchantChance) {
    		if (_weaponSkill.getHpStillValue() > 0) {
    			_drainHp = random.nextInt(_weaponSkill.getHpStillValue()) + 1;
    		}
        	if (_weaponSkill.getEnchantDamage() > 0 && _weaponEnchant > 6) {
        		_drainHp += _weaponEnchant + ~0x00000005;// -6
        	}
        	if (_weaponSkill.getIntDamage() > 0) {
        		int intel = pc.getAbility().getTotalInt();
        		_drainHp += random.nextInt(intel >> 1) + (intel);
        	}
        	if (_weaponSkill.getStillEffectId() != 0) {
        		sendWeaponSpellEffect(pc, target);
        	}
    	}
    }
    
    /**
     * MP를 흡수한다.
     * @param pc
     * @param target
     */
    void doWeaponDrainMp(L1PcInstance pc, L1Character target){
    	int prob = _weaponSkill.getMpStillProbability();
    	int enchantChance = _weaponSkill.getEnchantProbability() > 0 ? _weaponEnchant : 0;
    	if (prob == 0 || random.nextInt(100) + 1 <= prob + enchantChance) {
    		if (_weaponSkill.getMpStillValue() > 0) {
        		_drainMana = random.nextInt(_weaponSkill.getMpStillValue()) + 1;
    		}
        	if (_weaponSkill.getEnchantDamage() > 0 && _weaponEnchant > 6) {
        		_drainMana += _weaponEnchant + ~0x00000005;// -6
        	}
        	if (_weaponSkill.getIntDamage() > 0) {
        		int intel = pc.getAbility().getTotalInt();
        		_drainMana += random.nextInt(intel / 6) + (intel / 3);
        	}
        	if (_drainMana > 12) {
				_drainMana = 12;
			}
        	if (_weaponSkill.getStillEffectId() != 0) {
        		sendWeaponSpellEffect(pc, target);
        	}
    	}
    }
    
    /**
     * 흡수 이팩트 출력
     * @param pc
     * @param target
     */
    void sendWeaponSpellEffect(L1PcInstance pc, L1Character target){
    	int chaId = _weaponSkill.getEffectTarget() == 0 ? target.getId() : pc.getId();
		if (!_weaponSkill.isArrowType()) {
			pc.send_effect(chaId, _weaponSkill.getStillEffectId());
		} else {
			pc.broadcastPacketWithMe(new S_UseAttackSkill(pc, target.getId(), _weaponSkill.getStillEffectId(), target.getX(), target.getY(), ActionCodes.ACTION_Attack, false), true);
		}
    }
    
    /**
     * 마법 무기
     * @param attcker
     * @param target
     * @param oriDmg
     * @return double(대미지)
     */
    double calcWeaponSpellDmg(L1PcInstance attcker, L1Character target) {
    	if (weapon == null) {
    		return 0D;
    	}
    	doWeaponHpMpDrain(attcker, target);
    	switch (_weaponId) {
        case 200172:case 200173:case 200174:
        	return L1WeaponSkill.pumpkin_curs(attcker, target);
        case 2:case 200002:
        	return L1WeaponSkill.dagger_of_ill_luck(attcker, target, weapon);
        case 1120:
        	return L1WeaponSkill.mind_break(attcker, target, 6553, _weaponEnchant);
        default:
        	return getWeaponSpellDamage(attcker, target);
        }
    }
    
	int getRandomProb(){
		return random.nextInt(1000);
	}
	
	int getActionProb(L1PcInstance attcker, int prob){
		return (prob * 10) + (attcker.getAbility().getItemSpellProbWeapon() / 10);
	}
    
	/**
	 * 마법 무기를 발동시킨다.
	 * @param attcker
	 * @param target
	 * @return 대미지
	 */
	double getWeaponSpellDamage(L1PcInstance attcker, L1Character target) {
		if (attcker == null || target == null || _weaponSkill == null) {
			return 0;
		}
		int enchantLimit		= _weaponSkill.getEnchantLimit();
		if (enchantLimit != 0 && enchantLimit > _weaponEnchant) {
			return 0;
		}
		int prob				= _weaponSkill.getProbability();// 발동 확률
		if (prob == 0) {
			return 0;
		}
		int enchant_probability	= _weaponSkill.getEnchantProbability();// 인첸트 발동 확률
		if (enchant_probability != 0) {
			prob += _weaponEnchant * enchant_probability;
		}
		if (getActionProb(attcker, prob) < getRandomProb()) {
			return 0;
		}

		int skillId = _weaponSkill.getSkillId();
		if (skillId != -1) {
			if (skillId == L1SkillId.SILENCE && target instanceof L1NpcInstance && ((L1NpcInstance) target).getNpcTemplate().isBossMonster()) {
				return 0;// 보스 제외
			}
			L1Skills skill = SkillsTable.getTemplate(skillId);
			if (skill != null && !target.isFreeze()) {
				L1BuffUtil.skillAction(attcker, target, skillId, _weaponSkill.getSkillTime());
			}
		}

		int effectId = _weaponSkill.getEffectId();
		if (effectId != 0) {
			int effectTarget = _weaponSkill.getEffectTarget();
			if (effectTarget == 2) {
				S_EffectLocation packet = new S_EffectLocation(target.getX(), target.getY(), effectId, attcker.getMoveState().getHeading());
				attcker.broadcastPacketWithMe(packet, true);
			} else {
				int chaId = effectTarget == 0 ? target.getId() : attcker.getId();
				if (!_weaponSkill.isArrowType()) {
					attcker.send_effect(chaId, effectId);
				} else {
					attcker.broadcastPacketWithMe(new S_UseAttackSkill(attcker, chaId, effectId, target.getX(), target.getY(), ActionCodes.ACTION_Attack, false), true);
				}
			}
		}

		double damage		= _weaponSkill.getFixDamage();// 기본 대미지
		int randomDamage	= _weaponSkill.getRandomDamage();// 랜덤 대미지
		if (randomDamage != 0) {
			damage += random.nextInt(randomDamage);
		}
		int enchantDamage	= _weaponSkill.getEnchantDamage();// 인첸트 대미지
		if (enchantDamage != 0) {
			damage += _weaponEnchant * enchantDamage;
		}
		int intDamage		= _weaponSkill.getIntDamage();// 인트 대미지
		if (intDamage != 0) {
			damage += attcker.getAbility().getTotalInt() * intDamage;
		}
		int spellDamage		= _weaponSkill.getSpellDamage();// 스펠 대미지
		if (spellDamage != 0) {
			damage += attcker.getAbility().getSp() * spellDamage;
		}

		int area = _weaponSkill.getArea();// 범위 공격
		if (area > 0 || area == -1) {
			sendAreaDamage(attcker, target, area, damage, _weaponSkill.getAttr());
		}
		return calcDamageReduction(target, damage, _weaponSkill.getAttr());
	}
	
	/**
	 * 범위 대미지
	 * @param attcker
	 * @param target
	 * @param area
	 * @param damage
	 * @param attr
	 */
	void sendAreaDamage(L1PcInstance attcker, L1Character target, int area, double damage, L1Attr attr){
		L1PcInstance targetPc	= null;
		L1NpcInstance targetNpc	= null;
		boolean targetOneType	= target instanceof L1PcInstance || target instanceof L1SummonInstance || target instanceof L1PetInstance;
		for (L1Object object : L1World.getInstance().getVisibleObjects(target, area)) {
			if (object == null || object instanceof L1Character == false || object.getId() == attcker.getId() || object.getId() == target.getId()) {
				continue;
			}
			// 위치 여부
			if (object instanceof L1PcInstance && ((L1PcInstance) object).getRegion() == L1RegionStatus.SAFETY) {
				continue;
			}
			// 몬스터 공격시 몬스터에게만 대미지를 준다.
			if (target instanceof L1MonsterInstance && object instanceof L1MonsterInstance == false) {
				continue;
			}
			// PC타입일때 PC류에게만 대미지를 준다.
			if (targetOneType && !(object instanceof L1PcInstance || object instanceof L1SummonInstance || object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
				continue;
			}
			double send_damage = calcDamageReduction((L1Character) object, damage, attr);
			if (send_damage <= 0) {
				continue;
			}
			// 대미지 전달
			if (object instanceof L1PcInstance) {
				targetPc = (L1PcInstance) object;
				targetPc.broadcastPacketWithMe(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage), true);
				targetPc.receiveDamage(attcker, (int)send_damage);
			} else if (object instanceof L1SummonInstance || object instanceof L1PetInstance || object instanceof L1MonsterInstance) {
				targetNpc = (L1NpcInstance) object;
				targetNpc.broadcastPacket(new S_DoActionGFX(targetNpc .getId(), ActionCodes.ACTION_Damage), true);
				targetNpc.receiveDamage(attcker, (int)send_damage);
			}
		}
	}
	
	double calcDamageReduction(L1Character cha, double damage, L1Attr attr) {
		if (cha.isFreeze()) {
			return 0;
		}
		if (damage < 0) {
			damage = 0;
		}
		int resist = 0;
		switch (attr) {
		case EARTH:
			resist = cha.getResistance().getEarth();
			break;
		case FIRE:
			resist = cha.getResistance().getFire();
			break;
		case WATER:
			resist = cha.getResistance().getWater();
			break;
		case WIND:
			resist = cha.getResistance().getWind();
			break;
		default:
			break;
		}
		if (resist <= 0) {
			return damage;
		}
		int resistFloor = (int) (0.32D * Math.abs(resist));
		resistFloor *= resist >= 0 ? 1 : -1;
		double attrDeffence = resistFloor / 32.0;
		return (1.0D - attrDeffence) * damage;
	}

}

