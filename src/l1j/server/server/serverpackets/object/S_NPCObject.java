package l1j.server.server.serverpackets.object;

import java.util.Arrays;
import java.util.List;

import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1DoppelgangerInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1SignboardInstance;
import l1j.server.server.model.Instance.L1TreasureInstance;
import l1j.server.server.utils.StringUtil;

public class S_NPCObject extends WorldPutObjectStream {
    private static final String S_NPC_OBJECT = "[S] S_NPCObject";
    private byte[] _byte = null;
    
    static final List<Integer> SWORD_SPRITE_IDS = Arrays.asList(new Integer[] {
    		727, 985, 986, 6632, 6634, 6636, 6638
    });
    static final List<Integer> BOW_SPRITE_IDS = Arrays.asList(new Integer[] {
    		816, 3860, 2284, 13346, 14170, 15053, 15659
    });
    
    public S_NPCObject(L1NpcInstance npc) {
    	int npcId = npc.getNpcId();
        int spriteId = npc.getSpriteId();
        L1Character master = npc.getMaster();
        boolean occupyPortal = npcId == 800817 || npcId == 800818;// 점령전 이동 포탈
        
        write_init();
        write_point(npc.getX(), npc.getY());
        write_objectnumber(npc.getId());
        write_objectsprite(spriteId);
        write_action(get_action(npc, spriteId));
		write_direction(npc instanceof L1DoorInstance ? (((L1DoorInstance) npc).getDirection() == 0 ? 2 : 6) : npc.getMoveState().getHeading());
        write_lightRadius(npc.getNpcTemplate().getLightSize());
        write_objectcount(1);
		write_alignment(npc.getAlignment());
		write_desc(get_desc(npc));
		write_title(get_title(npc, npcId));
        write_speeddata(npc.getMoveState().getMoveSpeed());
        write_emotion(npc.getMoveState().getBraveSpeed());
        write_drunken(0);
        write_isghost(false);
        write_isparalyzed(npc.getParalysis() != null);
        write_isuser(npc instanceof L1NpcShopInstance);
        write_isinvisible(npc.isInvisble() || (npc instanceof L1DollInstance && master != null && master.isInvisble()));
        write_ispoisoned(npc.getPoison() != null);
		write_emblemid(0);
        write_pledgename(null);
        write_mastername(master == null ? null : master.getName());
        write_altitude(0);
		write_hitratio(-1);
		write_safelevel(npc.getLevel());
        write_weaponsprite(-1);
		write_couplestate(0);
		write_boundarylevelindex(occupyPortal ? 15 : 0);
		write_manaratio(-1);
		write_homeserverno(0);
		write_dialog_radius(occupyPortal ? 11 : npc instanceof L1MerchantInstance ? 15 : 9);
		if (occupyPortal) {
			write_force_haction(StringUtil.ZeroString);
	    	write_is_portal(true);
		}
		if (!occupyPortal && !(npc instanceof L1MerchantInstance || npc instanceof L1NpcShopInstance)) {
			write_speed_value_flag(0);
			if (npc.getExplosionTime() > 0) {
				write_explosion_remain_time_ms(npc.getExplosionTime());
			}
			write_npc_class_id(npc.getNpcTemplate().getClassId());
			write_potential_grade(npc instanceof L1DollInstance ? get_potential_grade((L1DollInstance) npc) : 0);
			if (npc instanceof L1TreasureInstance) {
				write_category(184);
			    write_is_excavated(((L1TreasureInstance) npc).isExcavation());
			}
		}
		writeH(0x00);
    }
    
    /**
     * 엔피씨의 액션 상태를 조사한다.
     * @param npc
     * @param spriteId
     * @return action
     */
    int get_action(L1NpcInstance npc, int spriteId) {
    	if (npc instanceof L1DoorInstance) {
			return get_action_from_door((L1DoorInstance) npc);
		}
    	if (npc instanceof L1NpcShopInstance) {
			return ActionCodes.ACTION_Shop;
		}
    	if (npc.isDead()) {
			return npc.getActionStatus();
		}
    	if (npc instanceof L1DoppelgangerInstance && spriteId != 31) {
			return ((L1DoppelgangerInstance) npc).getCurrentWeapon();
		}
    	if (SWORD_SPRITE_IDS.contains(spriteId)) {
			return 4;
		}
    	if (spriteId == 51) {
			return 24;
		}
    	if (BOW_SPRITE_IDS.contains(spriteId)) {
			return 20;
		}
		return npc.getActionStatus();
    }
    
    /**
     * 도어의 액션 상태를 조사한다.
     * @param door
     * @return action
     */
    int get_action_from_door(L1DoorInstance door) {
    	int doorStatus = door.getActionStatus();
		if (door.isDead()) {
			return doorStatus;
		}
		int openStatus = door.getOpenStatus();
		if (openStatus == ActionCodes.ACTION_Open) {
			return openStatus;
		}
		if (door.getMaxHp() > 1 && doorStatus != 0) {
			return doorStatus;
		}
		return openStatus;
    }
    
    /**
     * 출력할 엔피씨의 명칭을 조사한다
     * @param npc
     * @return desc
     */
    String get_desc(L1NpcInstance npc) {
    	if (npc instanceof L1SignboardInstance) {
        	return null;
        }
    	if (!StringUtil.isNullOrEmpty(npc.getDesc())) {
        	return npc.getDesc();
        }
        return null;
    }
    
    /**
     * 출력할 호칭을 조사한다.
     * @param npc
     * @param npcId
     * @return title
     */
    String get_title(L1NpcInstance npc, int npcId) {
    	if (npc instanceof L1FieldObjectInstance) {
            L1NpcTalkData talkdata = NPCTalkDataTable.getInstance().getTemplate(npcId);
            return talkdata != null ? talkdata.getNormalAction() : null;
        }
    	if (npc instanceof L1SignboardInstance) {
        	return npc.getDesc();
        }
    	if (npc.getTitle() != null && !npc.getTitle().isEmpty()) {
        	return npc.getTitle();
        }
        return null;
    }
    
    /**
     * 인형의 잠재력 단계 조사
     * @param doll
     * @return potential_grade
     */
    int get_potential_grade(L1DollInstance doll){
    	return doll.getPotential() != null ? doll.getPotential().getInfo().get_bonus_grade() : 0;
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
        return S_NPC_OBJECT;
    }
}

