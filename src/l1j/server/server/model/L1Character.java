package l1j.server.server.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.datatables.MapTypeTable;
import l1j.server.server.model.L1ItemDelay.ItemDelayTimer;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1FollowerInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.poison.L1Poison;
import l1j.server.server.model.skill.L1PassiveSkillHandler;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.sprite.L1Sprite;
import l1j.server.server.model.sprite.SpriteLoader;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SpeedBonus;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcConStat;
import l1j.server.server.utils.CalcWisStat;
import l1j.server.server.utils.IntRange;

public class L1Character extends L1Object {
	private static final Random random			= new Random(System.nanoTime());
	private static final long serialVersionUID = 1L;
	// 캐릭터 기본
	private String _name, _koreanName, _title;
	private int _level, _boundaryLevel, _boundaryLevelIndex;
	private long _exp;
	private int _currentHp, _currentMp;
	private int _maxHp, _maxMp, _trueMaxHp, _trueMaxMp;
	private int _align, _karma;

	private L1Sprite _sprite;
	private int _spriteId; // ● 그래픽 ID
	private boolean _paralyzed;
	private boolean _sleeped;
	private boolean _poisonParalyzed;
	private L1Paralysis _paralysis;
	private L1Poison _poison;
	private boolean _isDead;
	protected L1Light light; // 캐릭터 주위 빛
	protected L1MoveState moveState; // 이동속도, 바라보는 방향
	protected L1Ability ability; // 능력치
	protected L1Resistance resistance; // 저항 (마방, 불, 물, 바람, 땅, 스턴, 동빙, 슬립, 석화)
	protected L1AC ac; // AC 방어
	protected L1SkillStatus skillStatus;
	
	public boolean _statusDistroyFear, _statusDistroyHorror;
	public boolean isFouSlayer, isTriple;
	public int true_target_clanid = -1, true_target_partyid = -1, true_target_level = 0;// 트루타켓
	private int immunelevel;
	private int armorBreakID;
    private L1PcInstance _eternityAttacker;
    private double _eternityDmg;
    public L1PcInstance _pressureAttacker;
	public int _pressureDmg;
	public boolean _strikerGaleShotHold;
	public boolean _isBurningShot;

	private int _addAttrKind;
	private int actionStatus;
	private int _kills, _deaths;

	private final Map<Integer, L1NpcInstance> _petlist				= new HashMap<Integer, L1NpcInstance>();
	private final Map<Integer, L1FollowerInstance> _followerlist	= new HashMap<Integer, L1FollowerInstance>();
	private final Map<Integer, ItemDelayTimer> _itemdelay			= new HashMap<Integer, ItemDelayTimer>();
	
	private L1DollInstance _doll;

	// ■■■■■■■■■■ L1PcInstance에 이동하는 프롭퍼티 ■■■■■■■■■■
	private final List<L1Object> _knownObjects						= new CopyOnWriteArrayList<L1Object>();
	private final List<L1PcInstance> _knownPlayer					= new CopyOnWriteArrayList<L1PcInstance>();

	public L1Character() {
		_level		= 1;
		ability		= new L1Ability(this);
		resistance	= new L1Resistance(this);
		ac			= new L1AC(this);
		moveState	= new L1MoveState();
		light		= new L1Light(this);
		skillStatus	= new L1SkillStatus(this);
	}
	
	public L1PassiveSkillHandler getPassiveSkill(){
    	return null;
    }
    public boolean isPassiveStatus(L1PassiveId passive){
    	return false;
    }

	/**
	 * 캐릭터를 부활시킨다.
	 * @param hp 부활 후의 HP
	 */
	public void resurrect(int hp) {
		if (!isDead()) {
			return;
		}
		if (hp <= 0) {
			hp = 1;
		}
		setCurrentHp(hp);
		setDead(false);
		setActionStatus(0);
		L1PolyMorph.undoPoly(this);
		S_RemoveObject remove = new S_RemoveObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.sendPackets(remove);
			pc.removeKnownObject(this);
			onPerceive(pc);
		}
	}
	
	public boolean isHalfHp() {
		int max = getMaxHp();
		return max <= 0 ? false : (max >> 1) > _currentHp;
	}
	
	public int getCurrentHpPercent() {
		int max = getMaxHp();
		return max <= 0 ? 0 : 100 * _currentHp / max;
	}
	
	public int getCurrentMpPercent() {
		int max = getMaxMp();
		return max <= 0 ? 0 : 100 * _currentMp / max;
	}

	/**
	 * 캐릭터의 현재의 HP를 돌려준다.
	 * 
	 * @return 현재의 HP
	 */
	public int getCurrentHp() {
		return _currentHp;
	}
	public void setCurrentHp(int i) {
		if (i >= getMaxHp()) {
			i = getMaxHp();
		}
		if (i < 0) {
			i = 0;
		}
		_currentHp = i;
	}

	/**
	 * 캐릭터의 현재의 MP를 돌려준다.
	 * 
	 * @return 현재의 MP
	 */
	public int getCurrentMp() {
		return _currentMp;
	}
	public void setCurrentMp(int i) {
		if (i >= getMaxMp()) {
			i = getMaxMp();
		}
		if (i < 0) {
			i = 0;
		}
		_currentMp = i;
	}

	/**
	 * 캐릭터의 수면상태를 돌려준다.
	 * 
	 * @return 수면상태를 나타내는 값. 수면상태이면 true.
	 */
	public boolean isSleeped() {
		return _sleeped;
	}
	public void setSleeped(boolean val) {
		_sleeped = val;
	}

	/**
	 * 캐릭터의 마비 상태를 돌려준다.
	 * 
	 * @return 마비 상태를 나타내는 값. 마비 상태이면 true.
	 */
	public boolean isParalyzed() {
		return _paralyzed;
	}
	public void setParalyzed(boolean val) {
		_paralyzed = val;
	}
	
	public L1Paralysis getParalysis() {
		return _paralysis;
	}
	public void setParalaysis(L1Paralysis val) {
		_paralysis = val;
	}

	public void cureParalaysis() {
		if (_paralysis == null) {
			return;
		}
		_paralysis.cure();
	}
	/**
	 * 캐릭터의 가시 범위에 있는 플레이어에, 패킷을 송신한다.
	 * 
	 * @param packet
	 *            송신하는 패킷을 나타내는 ServerBasePacket 오브젝트.
	 */
	public void broadcastPacket(ServerBasePacket packet) {
		broadcastPacket(packet, false);
	}
	
	public void broadcastPacket(ServerBasePacket packet, boolean clear) {
		try {
			for (L1PcInstance each : L1World.getInstance().getVisiblePlayer(this)) {
				each.sendPackets(packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (clear) {
				if (packet != null) {
					packet.clear();
					packet = null;
				}
			}
		}
	}
	
	public void broadcastPacketWithMe(ServerBasePacket packet) {
		broadcastPacketWithMe(packet, false);
	}
	
	public void broadcastPacketWithMe(ServerBasePacket packet, boolean clear) {
	    try {
		    if (this instanceof L1PcInstance) {
		    	((L1PcInstance)this).sendPackets(packet);
		    }
		    for (L1PcInstance each : L1World.getInstance().getVisiblePlayer(this)) {
		    	each.sendPackets(packet);
		    }
	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (clear) {
				if (packet != null) {
					packet.clear();
					packet = null;
				}
			}
		}
	}
	
	public void broadcastPacket(ServerBasePacket packet, int range) {
		broadcastPacket(packet, range, false);
	}
	
	public void broadcastPacket(ServerBasePacket packet, int range, boolean clear) {
		try {
			for (L1PcInstance each : L1World.getInstance().getVisiblePlayer(this, range)) {
				each.sendPackets(packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (clear) {
				if (packet != null) {
					packet.clear();
					packet = null;
				}
			}
		}
	}
	
	public void broadcastPacket(ServerBasePacket packet, L1Character target) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet);
		}
	}

	public void broadcastPacket(ServerBasePacket packet, L1Character[] target) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet);
		}
	}
	/**
	 * 캐릭터의 가시 범위에 있는 플레이어에, 패킷을 송신한다. 다만 타겟의 화면내에는 송신하지 않는다.
	 * 
	 * @param packet
	 *            송신하는 패킷을 나타내는 ServerBasePacket 오브젝트.
	 */
	public void broadcastPacketExceptTargetSight(ServerBasePacket packet, L1Character target) {
		broadcastPacketExceptTargetSight(packet, target, false);
	}
	
	public void broadcastPacketExceptTargetSight(ServerBasePacket packet, L1Character target, boolean clear) {
		try {
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayerExceptTargetSight(this, target)) {
				if (pc.knownsObject(this)) {
					pc.sendPackets(packet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (clear) {
				if (packet != null) {
					packet.clear();
					packet = null;
				}
			}
		}
	}

	/**
	 * 캐릭터의 50 매스 이내에 있는 플레이어에, 패킷을 송신한다.
	 * 
	 * @param packet
	 *            송신하는 패킷을 나타내는 ServerBasePacket 오브젝트.
	 */
	public void wideBroadcastPacket(ServerBasePacket packet) {
		wideBroadcastPacket(packet, false);
	}
	
	public void wideBroadcastPacket(ServerBasePacket packet, boolean clear) {
		try {
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 30)) {
				pc.sendPackets(packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (clear) {
				if (packet != null) {
					packet.clear();
					packet = null;
				}
			}
		}
	}
	
	private static final int[][] HEADINGS = new int[][]{
		{7, 0, 1},
		{6, 1, 2},
		{5, 4, 3},
	};
	
	public int calcheading(int myx, int myy, int tx, int ty) {
		return HEADINGS[(ty > myy ? 2 : (ty < myy ? 0 : 1))][(tx > myx ? 2 : (tx < myx ? 0 : 1))];
	}
	
	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	/**
	 * 캐릭터의 정면의 좌표를 돌려준다.
	 * 
	 * @return 정면의 좌표
	 */
	public int[] getFrontLoc() {
		int heading	= moveState.getHeading();
		return new int[] { getX() + HEADING_TABLE_X[heading], getY() + HEADING_TABLE_Y[heading] };
	}

	/**
	 * 지정된 좌표에 대할 방향을 돌려준다.
	 * 
	 * @param tx
	 *            좌표의 X치
	 * @param ty
	 *            좌표의 Y치
	 * @return 지정된 좌표에 대할 방향
	 */
	public int targetDirection(int tx, int ty) {
		float dis_x	= Math.abs(getX() - tx);// X방향의 타겟까지의 거리
		float dis_y	= Math.abs(getY() - ty);// Y방향의 타겟까지의 거리
		float dis	= Math.max(dis_x, dis_y);// 타겟까지의 거리
		if (dis == 0) {
			return moveState.getHeading();
		}

		int avg_x	= (int) Math.floor((dis_x / dis) + 0.59f);// 상하 좌우가 조금 우선인 둥근
		int avg_y	= (int) Math.floor((dis_y / dis) + 0.59f);// 상하 좌우가 조금 우선인 둥근

		int dir_x = 0, dir_y = 0;

		if (getX() < tx) {
			dir_x = 1;
		}
		if (getX() > tx) {
			dir_x = -1;
		}

		if (getY() < ty) {
			dir_y = 1;
		}
		if (getY() > ty) {
			dir_y = -1;
		}

		if (avg_x == 0) {
			dir_x = 0;
		}
		if (avg_y == 0) {
			dir_y = 0;
		}

		if (dir_x == 1 && dir_y == -1) {
			return 1;// 상
		}
		if (dir_x == 1 && dir_y == 0) {
			return 2;// 우상
		}
		if (dir_x == 1 && dir_y == 1) {
			return 3;// 오른쪽
		}
		if (dir_x == 0 && dir_y == 1) {
			return 4;// 우하
		}
		if (dir_x == -1 && dir_y == 1) {
			return 5;// 하
		}
		if (dir_x == -1 && dir_y == 0) {
			return 6;// 좌하
		}
		if (dir_x == -1 && dir_y == -1) {
			return 7;// 왼쪽
		}
		if (dir_x == 0 && dir_y == -1) {
			return 0;// 좌상
		}
		return moveState.getHeading();
	}

	/**
	 * 지정된 좌표까지의 직선상에, 장애물이 존재여부 검증
	 * @param range 좌표까지의 거리
	 * @param tx 좌표의 X치
	 * @param ty 좌표의 Y치
	 * @return 장애물이 없으면 true, 있으면 false를 돌려준다.
	 */
	public boolean glanceCheck(int range, int tx, int ty, boolean isDoor) {
		L1Map map = getMap();
		int chx = getX();
		int chy = getY();
		int heading = targetDirection(tx, ty);
		if (range < 0) {
			range = 15;
		}
		if (targetAttackableCheck(range, chx, chy, tx, ty, map)) {// 타겟이 자신을 공격 가능할수 있는지 체크
			return true;
		}
		boolean pss = this instanceof L1PcInstance && ((L1PcInstance) this).getConfig().getPlaySupportType() > 1;
		for (int i = 0; i < range; i++) {
			if (chx == tx && chy == ty) {
				return true;
			}
			if (isDoor) {
				if (!map.isAttackableDoor(chx, chy, heading)) {
					return false;
				}
			} else {
				if (!map.isAttackable(chx, chy, heading, pss)) {
					return false;
				}
			}
			if (Math.abs(chx - tx) <= 1 && Math.abs(chy - ty) <= 1) {
				return true;
			}
			
			if (chx < tx) {
				chx++;
			} else if (chx > tx) {
				chx--;
			}
			if (chy < ty) {
				chy++;
			} else if (chy > ty) {
				chy--;
			}
		}
		return true;
	}
	
	boolean targetAttackableCheck(int range, int tx, int ty, int oriChx, int oriChy, L1Map map) {
		int chx = oriChx, chy = oriChy;
		int heading = targetDirection(tx, ty);
		for (int i = 0; i < range; i++) {
			if (chx == tx && chy == ty) {
				return true;
			}
			if (!map.isAttackable(chx, chy, heading, false)) {
				return false;
			}
			if (Math.abs(chx - tx) <= 1 && Math.abs(chy - ty) <= 1) {
				return true;
			}
			if (chx < tx) {
				chx++;
			} else if (chx > tx) {
				chx--;
			}
			if (chy < ty) {
				chy++;
			} else if (chy > ty) {
				chy--;
			}
		}
		return true;
	}

	/**
	 * 지정된 좌표에 공격 가능한가를 돌려준다.
	 * @param 좌표의 X치.
	 * @param 좌표의 Y치.
	 * @param range 공격 가능한 범위(타일수)
	 * @return 공격 가능하면 true, 불가능하면 false
	 */
	public boolean isAttackPosition(int x, int y, int range, boolean isDoor) {
		if (range >= 7) {// 원격 무기(7이상의 경우 기울기를 고려하면(자) 화면외에 나온다)
			if (getLocation().getTileDistance(new Point(x, y)) > range) {
				return false;
			}
		} else {
			if (getLocation().getTileLineDistance(new Point(x, y)) > range) {
				return false;
			}
		}
		return glanceCheck(range, x, y, isDoor);
	}
	
	/**
	 * 지정된 좌표에 공격 가능한가를 돌려준다.
	 * @param target
	 * @param range
	 * @param isDoor
	 * @return 공격 가능하면 true, 불가능하면 false
	 */
	public boolean isAttackPosition(L1Character target, int range, boolean isDoor) {
		if (target == null) {
			return false;
		}
		if (range >= 7) {// 원격 무기(7이상의 경우 기울기를 고려하면(자) 화면외에 나온다)
			if (getLocation().getTileDistance(target.getLocation()) > range) {
				return false;
			}
		} else {
			if (getLocation().getTileLineDistance(target.getLocation()) > range) {
				return false;
			}
		}
		return glanceCheck(range, target.getX(), target.getY(), isDoor);
	}
	
	/**
	 * 캐릭터의 이미지로부터 발사채의 이미지를 조사한다.
	 * @param isArrow
	 * @return sprite
	 */
	public int getArrowStingSprite(boolean isArrow) {
        switch (getSpriteId()) {
        case 7967:// 천상의 기사
			return 7972;
		case 11402:case 8900:// 75렙 변신
			return 8904;
		case 11406:case 8913:// 80렙 변신
			return 8916;
		case 13631:// 82렙 변신
			return 13656;
		case 13635:// 85렙 변신
			return 13658;
		case 17535:// 87렙 변신
			return 17539;
		case 21575:case 21579:case 21581:// 월드컵 변신
			return 21585;
		default:
			return isArrow ? 66 : 2989;
        }
    }

	/**
	 * 캐릭터의 목록을 돌려준다.
	 * 
	 * @return 캐릭터의 목록을 나타내는, L1Inventory 오브젝트.
	 */
	public L1Inventory getInventory() {
		return null;
	}
    
	/**
	 * 캐릭터에, Item delay 추가
	 * 
	 * @param delayId
	 *            아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디크로크이면 1.
	 * @param timer
	 *            지연 시간을 나타내는, L1ItemDelay.ItemDelayTimer 오브젝트.
	 */
	public void addItemDelay(int delayId, ItemDelayTimer timer) {
		_itemdelay.put(delayId, timer);
	}

	/**
	 * 캐릭터로부터, Item delay 삭제
	 * 
	 * @param delayId
	 *            아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디크로크이면 1.
	 */
	public void removeItemDelay(int delayId) {
		_itemdelay.remove(delayId);
	}

	/**
	 * 캐릭터에, Item delay 이 있을까
	 * 
	 * @param delayId
	 *            조사하는 아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디 클로크이면 1.
	 * @return 아이템 지연이 있으면 true, 없으면 false.
	 */
	public boolean hasItemDelay(int delayId) {
		return _itemdelay.containsKey(delayId);
	}

	/**
	 * 캐릭터의 item delay 시간을 나타내는, L1ItemDelay.ItemDelayTimer를 돌려준다.
	 * 
	 * @param delayId
	 *            조사하는 아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디 클로크이면 1.
	 * @return 아이템 지연 시간을 나타내는, L1ItemDelay.ItemDelayTimer.
	 */
	public ItemDelayTimer getItemDelayTimer(int delayId) {
		return _itemdelay.get(delayId);
	}

	/**
	 * 캐릭터에, pet, summon monster, tame monster, created zombie 를 추가한다.
	 * 
	 * @param npc
	 *            추가하는 Npc를 나타내는, L1NpcInstance 오브젝트.
	 */
	public void addPet(L1NpcInstance npc) {
		_petlist.put(npc.getId(), npc);
	}

	/**
	 * 캐릭터로부터, pet, summon monster, tame monster, created zombie 를 삭제한다.
	 * 
	 * @param npc
	 *            삭제하는 Npc를 나타내는, L1NpcInstance 오브젝트.
	 */
	public void removePet(L1NpcInstance npc) {
		_petlist.remove(npc.getId());
	}

	/**
	 * 캐릭터의 애완동물 리스트를 돌려준다.
	 * 
	 * @return 캐릭터의 애완동물 리스트를 나타내는, HashMap 오브젝트. 이 오브젝트의 Key는 오브젝트 ID, Value는
	 *         L1NpcInstance.
	 */
	public Map<Integer, L1NpcInstance> getPetList() {
		return _petlist;
	}

	/**
	 * 캐릭터에 doll을 추가한다.
	 * 
	 * @param doll 추가하는 doll를 나타내는, L1DollInstance 오브젝트.
	 */
	public void setDoll(L1DollInstance doll) {
		_doll = doll;
	}

	/**
	 * 캐릭터의 doll 돌려준다.
	 * 
	 * @return L1DollInstance.
	 */
	public L1DollInstance getDoll() {
		return _doll;
	}

	/**
	 * 캐릭터에 이벤트 NPC(캐릭터를 따라다니는)를 추가한다.
	 * 
	 * @param follower
	 *            추가하는 follower를 나타내는, L1FollowerInstance 오브젝트.
	 */
	public void addFollower(L1FollowerInstance follower) {
		_followerlist.put(follower.getId(), follower);
	}

	/**
	 * 캐릭터로부터 이벤트 NPC(캐릭터를 따라다니는)를 삭제한다.
	 * 
	 * @param follower
	 *            삭제하는 follower를 나타내는, L1FollowerInstance 오브젝트.
	 */
	public void removeFollower(L1FollowerInstance follower) {
		_followerlist.remove(follower.getId());
	}

	/**
	 * 캐릭터의 이벤트 NPC(캐릭터를 따라다니는) 리스트를 돌려준다.
	 * 
	 * @return 캐릭터의 종자 리스트를 나타내는, HashMap 오브젝트. 이 오브젝트의 Key는 오브젝트 ID, Value는
	 *         L1FollowerInstance.
	 */
	public Map<Integer, L1FollowerInstance> getFollowerList() {
		return _followerlist;
	}
	
	/**
	 * 캐릭터의 마비 독 상태
	 * @return boolean
	 */
	public boolean isPoisonParalyzed() {
		return _poisonParalyzed;
	}
	
	public void setPoisonParalyzed(boolean val) {
		_poisonParalyzed = val;
	}

	/**
	 * 캐릭터에, 독을 추가한다.
	 * 
	 * @param poison
	 *            독을 나타내는, L1Poison 오브젝트.
	 */
	public void setPoison(L1Poison poison) {
		if (poison != null) {
			curePoison();
		}
		_poison = poison;
	}

	/**
	 * 캐릭터의 독을 치료한다.
	 */
	public void curePoison() {
		if (_poison == null) {
			return;
		}
		_poison.cure();
	}

	/**
	 * 캐릭터의 독상태를 돌려준다.
	 * 
	 * @return 캐릭터의 독을 나타내는, L1Poison 오브젝트.
	 */
	public L1Poison getPoison() {
		return _poison;
	}

	/**
	 * 캐릭터에 독의 효과를 부가한다
	 * 
	 * @param effectId
	 * @see S_Poison#S_Poison(int, int)
	 */
	public void setPoisonEffect(int effectId) {
		broadcastPacket(new S_Poison(getId(), effectId), true);
	}

	/**
	 * 캐릭터가 존재하는 좌표가, 어느 존에 속하고 있을까를 돌려준다.
	 * 
	 * @return 좌표의 존을 나타내는 값. 세이프티 존이면 1, 컴배트 존이면―1, 노멀 존이면 0.
	 */
	public L1RegionStatus getRegion() {
		L1RegionStatus region = MapTypeTable.getRegion((int) getMapId());
		if (region != null) {
			return region;
		}
		if (getMap().isSafetyZone(getLocation())) {
			return L1RegionStatus.SAFETY;
		}
		if (getMap().isCombatZone(getLocation())) {
			return L1RegionStatus.COMBAT;
		}
		return L1RegionStatus.NORMAL;
	}

	public long getExp() {
		return _exp;
	}
	public void setExp(long exp) {
		_exp = exp;
	}

	/**
	 * 지정된 오브젝트를, 캐릭터가 인식하고 있을까를 돌려준다.
	 * 
	 * @param obj
	 *            조사하는 오브젝트.
	 * @return 오브젝트를 캐릭터가 인식하고 있으면 true, 하고 있지 않으면 false. 자기 자신에 대해서는 false를
	 *         돌려준다.
	 */
	public boolean knownsObject(L1Object obj) {
		return _knownObjects.contains(obj);
	}

	/**
	 * 캐릭터가 인식하고 있는 모든 오브젝트를 돌려준다.
	 * 
	 * @return 캐릭터가 인식하고 있는 오브젝트를 나타내는 List<L1Object>.
	 */
	public List<L1Object> getKnownObjects() {
		return _knownObjects;
	}

	/**
	 * 캐릭터가 인식하고 있는 모든 플레이어를 돌려준다.
	 * 
	 * @return 캐릭터가 인식하고 있는 오브젝트를 나타내는 List<L1PcInstance>
	 */
	public List<L1PcInstance> getKnownPlayers() {
		return _knownPlayer;
	}

	/**
	 * 캐릭터에, 새롭게 인식하는 오브젝트를 추가한다.
	 * 
	 * @param obj
	 *            새롭게 인식하는 오브젝트.
	 */
	public void addKnownObject(L1Object obj) {
		_knownObjects.add(obj);
		if (obj instanceof L1PcInstance) {
			_knownPlayer.add((L1PcInstance) obj);
		}
	}
	/**
	 * 캐릭터로부터, 인식하고 있는 오브젝트를 삭제한다.
	 * 
	 * @param obj
	 *            삭제하는 오브젝트.
	 */
	public void removeKnownObject(L1Object obj) {
		_knownObjects.remove(obj);
		_knownPlayer.remove(obj);
	}

	/**
	 * 캐릭터로부터, 모든 인식하고 있는 오브젝트를 삭제한다.
	 */
	public void removeAllKnownObjects() {
		_knownObjects.clear();
		_knownPlayer.clear();
	}
	
	public String getName() {
		  return _name;
	}
	public void setName(String val) {
		_name = val;
	}

	public String getKoreanName() {
		String _val = _koreanName;
		if ("".equals(_val))
			_val = _name;
		return _val;
	}
	public void setKoreanName(String val) {
		_koreanName = val;
	}

	public int getLevel() {
		return _level;
	}
	public synchronized void setLevel(long level) {
		_level = (int) level;
		if (this instanceof L1PcInstance) {
			setBoundaryLevel();
		}
	}
	
	public int getBoundaryLevel() {
		return _boundaryLevel;
	}
	public int getBoundaryLevelIndex() {
		return _boundaryLevelIndex;
	}
	public void setBoundaryLevel(){
		_boundaryLevel		= SpriteLoader.getBoundaryLevel(_level);// 현재 레벨 기준 바운더리 레벨
		_boundaryLevelIndex	= SpriteLoader.getBoundaryLevelToIndex(_level);// 바운더리 레벨에 대한 인덱스
	}

	public int getMaxHp(){
		return _maxHp + CalcConStat.increaseHpBonus(ability.getTotalCon());
	}

	public void addMaxHp(int i) {
		setMaxHp(_trueMaxHp + i);
		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
			if (pc.isInParty()) {
				pc.getParty().update(pc);
			}
		}
	}

	public void setMaxHp(int hp) {
		_trueMaxHp = hp;
		_maxHp = IntRange.ensure(_trueMaxHp, 1, 1000000);
		_currentHp = Math.min(_currentHp, _maxHp);
	}

	public int getMaxMp() {
		return _maxMp + CalcWisStat.increaseMpBonus(ability.getTotalWis());
	}

	public void setMaxMp(int mp) {
		_trueMaxMp = mp;
		 _maxMp = IntRange.ensure(_trueMaxMp, 0, 1000000);
		_currentMp = Math.min(_currentMp, _maxMp);
	}

	public void addMaxMp(int i) {
		setMaxMp(_trueMaxMp + i);
		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
		}
	}

	public void healHp(int val) {
		setCurrentHp(getCurrentHp() + val);
	}

	public int getAddAttrKind() {
		return _addAttrKind;
	}
	public void setAddAttrKind(int i) {
		_addAttrKind = i;
	}

	public boolean isDead() {
		return _isDead;
	}
	public void setDead(boolean flag) {
		_isDead = flag;
	}

	public String getTitle() {
		return _title;
	}
	public void setTitle(String s) {
		_title = s;
	}

	public int getAlignment() {
		return _align;
	}
	public void setAlignment(int i) {
		_align = IntRange.ensure(i, Short.MIN_VALUE, Short.MAX_VALUE);
	}
	public synchronized void addAlignment(int i) {
		_align += i;
		_align = IntRange.ensure(_align, Short.MIN_VALUE, Short.MAX_VALUE);
	}
	
	public L1Sprite getSprite() {
		return _sprite;
	}

	public int getSpriteId() {
		return _spriteId;
	}
	public void setSpriteId(int val) {
		_spriteId	= val;
		_sprite		= SpriteLoader.get_sprite(_spriteId);
	}
	
	private double _moveSpeedDelayRate, _attackSpeedDelayRate, _spellSpeedDelayRate;
	private byte[] _moveSpeedDelayByte, _attackSpeedDelayByte, _spellSpeedDelayByte;
	
	public double getMoveSpeedDelayRate() {
		return _moveSpeedDelayRate;
	}
	
	public double getAttackSpeedDelayRate() {
		return _attackSpeedDelayRate;
	}
	
	public double getSpellSpeedDelayRate() {
		return _spellSpeedDelayRate;
	}
	
	public byte[] getMoveSpeedDelayByte() {
		return _moveSpeedDelayByte;
	}
	
	public byte[] getAttackSpeedDelayByte() {
		return _attackSpeedDelayByte;
	}
	
	public byte[] getSpellSpeedDelayByte() {
		return _spellSpeedDelayByte;
	}
	
	public void addMoveSpeedDelayRate(double moveSpeedDelayRate) {
		_moveSpeedDelayRate += moveSpeedDelayRate;
		_moveSpeedDelayByte = S_SpeedBonus.getBonus(S_SpeedBonus.eKind.MOVE_SPEED, (int)_moveSpeedDelayRate);
		if (this instanceof L1PcInstance) {
			((L1PcInstance) this).broadcastPacketWithMe(new S_SpeedBonus(this.getId(), _moveSpeedDelayByte), true);
		}
	}
	
	public void addAttackSpeedDelayRate(double attackSpeedDelayRate) {
		_attackSpeedDelayRate += attackSpeedDelayRate;
		_attackSpeedDelayByte = S_SpeedBonus.getBonus(S_SpeedBonus.eKind.ATTACK_SPEED, (int)_attackSpeedDelayRate);
		if (this instanceof L1PcInstance) {
			((L1PcInstance) this).broadcastPacketWithMe(new S_SpeedBonus(this.getId(), _attackSpeedDelayByte), true);
		}
	}
	
	public void addSpellSpeedDelayRate(double spellSpeedDelayRate) {
		_spellSpeedDelayRate += spellSpeedDelayRate;
		_spellSpeedDelayByte = S_SpeedBonus.getBonus(S_SpeedBonus.eKind.SPELL_SPEED, (int)_spellSpeedDelayRate);
		if (this instanceof L1PcInstance) {
			((L1PcInstance) this).broadcastPacketWithMe(new S_SpeedBonus(this.getId(), _spellSpeedDelayByte), true);
		}
	}
	
	public void addSpeedDelayRate(double moveSpeedDelayRate, double attackSpeedDelayRate) {
		_moveSpeedDelayRate += moveSpeedDelayRate;
		_moveSpeedDelayByte = S_SpeedBonus.getBonus(S_SpeedBonus.eKind.MOVE_SPEED, (int)_moveSpeedDelayRate);
		_attackSpeedDelayRate += attackSpeedDelayRate;
		_attackSpeedDelayByte = S_SpeedBonus.getBonus(S_SpeedBonus.eKind.ATTACK_SPEED, (int)_attackSpeedDelayRate);
		if (this instanceof L1PcInstance) {
			((L1PcInstance) this).broadcastPacketWithMe(new S_SpeedBonus(this.getId(), _moveSpeedDelayByte, _attackSpeedDelayByte), true);
		}
	}
	
	public void addSpeedDelayRate(double moveSpeedDelayRate, double attackSpeedDelayRate, double spellSpeedDelayRate) {
		_moveSpeedDelayRate += moveSpeedDelayRate;
		_moveSpeedDelayByte = S_SpeedBonus.getBonus(S_SpeedBonus.eKind.MOVE_SPEED, (int)_moveSpeedDelayRate);
		_attackSpeedDelayRate += attackSpeedDelayRate;
		_attackSpeedDelayByte = S_SpeedBonus.getBonus(S_SpeedBonus.eKind.ATTACK_SPEED, (int)_attackSpeedDelayRate);
		_spellSpeedDelayRate += spellSpeedDelayRate;
		_spellSpeedDelayByte = S_SpeedBonus.getBonus(S_SpeedBonus.eKind.SPELL_SPEED, (int)_spellSpeedDelayRate);
		if (this instanceof L1PcInstance) {
			((L1PcInstance) this).broadcastPacketWithMe(new S_SpeedBonus(this.getId(), _moveSpeedDelayByte, _attackSpeedDelayByte, _spellSpeedDelayByte), true);
		}
	}
	
	public boolean isFastMovable() {
		return (skillStatus.hasSkillEffect(L1SkillId.HOLY_WALK)
				|| skillStatus.hasSkillEffect(L1SkillId.MOVING_ACCELERATION));
	}

	public boolean isBloodLust() {
		return skillStatus.hasSkillEffect(L1SkillId.BLOOD_LUST);
	}

	public boolean isBrave() {
		return (skillStatus.hasSkillEffect(L1SkillId.STATUS_BRAVE)
				|| skillStatus.hasSkillEffect(L1SkillId.DANCING_BLADES)
				|| skillStatus.hasSkillEffect(L1SkillId.SAND_STORM));
	}

	public boolean isDrunken() {
		return (skillStatus.hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL)
				|| moveState.getDrunken() == 8);
	}
	
	public boolean isFourthGear(){
		return moveState.isFourthGear();
	}

	public boolean isElfBrave() {
		return skillStatus.hasSkillEffect(L1SkillId.STATUS_ELFBRAVE);
	}
	
	public boolean isFocusWave() {
		return (skillStatus.hasSkillEffect(L1SkillId.FOCUS_WAVE));
	}
	
	public boolean isHurricane() {
		return (skillStatus.hasSkillEffect(L1SkillId.HURRICANE));
	}

	public boolean isFruit() {
		return skillStatus.hasSkillEffect(L1SkillId.STATUS_FRUIT);
	}

	public boolean isHaste() {
		return (skillStatus.hasSkillEffect(L1SkillId.STATUS_HASTE)
				|| skillStatus.hasSkillEffect(L1SkillId.HASTE)
				|| skillStatus.hasSkillEffect(L1SkillId.GREATER_HASTE)
				|| moveState.getMoveSpeed() == 1);
	}
	
	public boolean isSlow(){
		return skillStatus.hasSkillEffect(L1SkillId.SLOW)
				|| skillStatus.hasSkillEffect(L1SkillId.GREATER_SLOW)
				|| skillStatus.hasSkillEffect(L1SkillId.ENTANGLE);
	}
	
	public boolean isStop(){
		return isStun()
				|| isSleeped()
				|| isParalyzed()
			    || skillStatus.hasSkillEffect(L1SkillId.EARTH_BIND)
			    || skillStatus.hasSkillEffect(L1SkillId.ICE_LANCE)
			    || skillStatus.hasSkillEffect(L1SkillId.MOB_COCA)
			    || skillStatus.hasSkillEffect(L1SkillId.MOB_BASILL)
			    || skillStatus.hasSkillEffect(L1SkillId.ANTA_MESSAGE_6)
			    || skillStatus.hasSkillEffect(L1SkillId.ANTA_MESSAGE_7)
			    || skillStatus.hasSkillEffect(L1SkillId.ANTA_MESSAGE_8)
			    || skillStatus.hasSkillEffect(L1SkillId.ANTA_SHOCKSTUN)
			    || skillStatus.hasSkillEffect(L1SkillId.VALLAKAS_PREDICATE2)
			    || skillStatus.hasSkillEffect(L1SkillId.VALLAKAS_PREDICATE4)
			    || skillStatus.hasSkillEffect(L1SkillId.VALLAKAS_PREDICATE5);
	}
	
	public boolean isAbsol(){
		return skillStatus.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER);
	}
	
	public boolean isBind(){
		return skillStatus.hasSkillEffect(L1SkillId.EARTH_BIND)
				|| skillStatus.hasSkillEffect(L1SkillId.ICE_LANCE)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_COCA)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_BASILL);
	}
	
	public boolean isStun() {
		return skillStatus.hasSkillEffect(L1SkillId.SHOCK_STUN)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_STUN)
				|| skillStatus.hasSkillEffect(L1SkillId.FORCE_STUN)
				|| skillStatus.hasSkillEffect(L1SkillId.EMPIRE)
				|| skillStatus.hasSkillEffect(L1SkillId.BONE_BREAK)
				|| skillStatus.hasSkillEffect(L1SkillId.PANTERA)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_PANTERA_SHOCK)
				|| skillStatus.hasSkillEffect(L1SkillId.CRUEL)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_DISINTEGRATE_NEMESIS)
				|| skillStatus.hasSkillEffect(L1SkillId.TEMPEST)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_CONQUEROR)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_FOU_SLAYER_FORCE_STUN)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_SHOCKSTUN_18)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_SHOCKSTUN_19)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_SHOCKSTUN_30)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_RANGESTUN_18)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_RANGESTUN_19)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_RANGESTUN_20)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_RANGESTUN_30);
	}
	
	public boolean isHold(){
		return skillStatus.hasSkillEffect(L1SkillId.POWER_GRIP)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_HOLD)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_FREEZE)
				|| skillStatus.hasSkillEffect(L1SkillId.THUNDER_GRAB)
				|| (skillStatus.hasSkillEffect(L1SkillId.STRIKER_GALE) && _strikerGaleShotHold)
				|| skillStatus.hasSkillEffect(L1SkillId.PRESSURE) 
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_PRESSURE_DEATH_RECAL)
				|| isShadowStep();
	}
	
	public boolean isFreeze() {
		return skillStatus.hasSkillEffect(L1SkillId.STATUS_FREEZE)
				|| skillStatus.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)
				|| isBind();
	}
	
	public boolean isDesperado() {
		return skillStatus.hasSkillEffect(L1SkillId.DESPERADO);
	}
	
	public boolean isPhantom() {
		return skillStatus.hasSkillEffect(L1SkillId.STATUS_PHANTOM_NOMAL)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_PHANTOM_RIPER)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_PHANTOM_DEATH)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_PHANTOM_REQUIEM);
	}
	
	public boolean isPhantomNotMove(){
		return skillStatus.hasSkillEffect(L1SkillId.STATUS_PHANTOM_RIPER)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_PHANTOM_DEATH)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_PHANTOM_REQUIEM);
	}
	
	public boolean isPolluteWater() {
		return skillStatus.hasSkillEffect(L1SkillId.POLLUTE_WATER)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_POLLUTE_WATER)
				|| (skillStatus.hasSkillEffect(L1SkillId.STRIKER_GALE) && _strikerGaleShotHold);
	}
	
	public boolean isDeathPotion() {
		return skillStatus.hasSkillEffect(L1SkillId.PAP_DEATH_POTION)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_RANGE_DEATH_POTION);
	}
	
	public boolean isDeathHeal() {
		return skillStatus.hasSkillEffect(L1SkillId.DEATH_HEAL)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_DEATH_HEAL)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_RANGE_DEATH_HEAL)
				|| skillStatus.hasSkillEffect(L1SkillId.PAP_DEATH_HEAL);
	}
	
	public boolean isMoveDecrease(){
		return isShockAttack() 
				|| skillStatus.hasSkillEffect(L1SkillId.ENSNARE) 
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_TYRANT_EXCUTION);
	}
	
	public boolean isShockAttack(){
		return skillStatus.hasSkillEffect(L1SkillId.SHOCK_ATTACK) || isShockAttackTeleport();
	}
	
	public boolean isShockAttackTeleport(){
		return skillStatus.hasSkillEffect(L1SkillId.STATUS_SHOCK_ATTACK_TEL);
	}
	
	public boolean isOsiris() {
		return skillStatus.hasSkillEffect(L1SkillId.OSIRIS);
	}
	
	public boolean isEternity() {
		return skillStatus.hasSkillEffect(L1SkillId.STATUS_ETERNITY);
	}
	
	public boolean isShadowStep() {
		return skillStatus.hasSkillEffect(L1SkillId.SHADOW_STEP) || isShadowStepChaser();
	}
	
	public boolean isShadowStepChaser() {
		return skillStatus.hasSkillEffect(L1SkillId.SHADOW_STEP_CHASER);
	}
	
	public boolean isBehemoth() {
		return skillStatus.hasSkillEffect(L1SkillId.STATUS_BEHEMOTH_DEBUFF);
	}
	
	public boolean isNotTeleport(){
		return isDead() || isStop() || isParalyzed() || isSleeped() || isPressureDeathRecall()
				|| isDesperado() || isOsiris() || isPhantom() || isShockAttackTeleport()
				|| isEternity() || isShadowStepChaser() || _isBurningShot || isBehemoth();
	}
	
	public boolean isNotReturnFromSpell() {
		return isPressureDeathRecall()
				|| isDesperado() || isOsiris() || isPhantom() || isShockAttackTeleport()
				|| isEternity() || isShadowStepChaser() || isBehemoth();
	}

	public boolean isInvisble() {
		return (skillStatus.hasSkillEffect(L1SkillId.INVISIBILITY)
				|| isBlindHiding());
	}
	
	public boolean isBlindHiding(){
		return (skillStatus.hasSkillEffect(L1SkillId.BLIND_HIDING)
				|| skillStatus.hasSkillEffect(L1SkillId.MOB_BLIND_HIDING));
	}
	
	public boolean isSilence(){
		return skillStatus.hasSkillEffect(L1SkillId.SILENCE)
				|| skillStatus.hasSkillEffect(L1SkillId.AREA_OF_SILENCE)
				|| skillStatus.hasSkillEffect(L1SkillId.STATUS_POISON_SILENCE)
				|| skillStatus.hasSkillEffect(L1SkillId.CONFUSION);
	}
	
	public boolean isShapeChange(){
		return skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE)
				|| skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)
				|| skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL);
	}
	
	public boolean isBlind() {
		return skillStatus.hasSkillEffect(L1SkillId.CURSE_BLIND)
				|| skillStatus.hasSkillEffect(L1SkillId.DARKNESS)
				|| skillStatus.hasSkillEffect(L1SkillId.LINDBIOR_SPIRIT_EFFECT);
	}
	
	public boolean isFloatingEye() {
		return skillStatus.hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE) && isBlind();
	}
	
	public boolean isPotionPenalty(){
		return skillStatus.hasSkillEffect(L1SkillId.DECAY_POTION)
				|| (skillStatus.hasSkillEffect(L1SkillId.FATAL_POTION) && random.nextInt(100) + 1 <= Config.SPELL.PATAL_POTION_CHANCE);
	}
	
	public void removeSleepSkill(){
		if (skillStatus.hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
			skillStatus.removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
		}
        if (skillStatus.hasSkillEffect(L1SkillId.PHANTASM)) {
        	skillStatus.removeSkillEffect(L1SkillId.PHANTASM);
        }
        if (skillStatus.hasSkillEffect(L1SkillId.SHADOW_SLEEP)) {
        	skillStatus.removeSkillEffect(L1SkillId.SHADOW_SLEEP);
        }
	}
	
	public void removeSpeedSkill() {
        if (skillStatus.hasSkillEffect(L1SkillId.SLOW)) {
        	skillStatus.removeSkillEffect(L1SkillId.SLOW);
        } else if (skillStatus.hasSkillEffect(L1SkillId.GREATER_SLOW)) {
        	skillStatus.removeSkillEffect(L1SkillId.GREATER_SLOW);
        }
        if (skillStatus.hasSkillEffect(L1SkillId.HASTE)) {
        	skillStatus.removeSkillEffect(L1SkillId.HASTE);
        } else if (skillStatus.hasSkillEffect(L1SkillId.GREATER_HASTE)) {
        	skillStatus.removeSkillEffect(L1SkillId.GREATER_HASTE);
        } else if (skillStatus.hasSkillEffect(L1SkillId.STATUS_HASTE)) {
        	skillStatus.removeSkillEffect(L1SkillId.STATUS_HASTE);
        }
    }
	
	public void removeShapeChange(){
		if (skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
			skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE);
		} else if (skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)) {
			skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION);
		} else if (skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)) {
			skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL);
		}
	}

	public int getKarma() {
		return _karma;
	}
	public void setKarma(int val) {
		_karma = val;
	}
	
	public int getKills() {
		return _kills;
	}
	public void setKills(int val) {
		_kills = val;
	}
	 
	public int getDeaths() {
		return _deaths;
	}
	public void setDeaths(int val) {
		_deaths = val;
	}

	public L1Light getLight() {
		return light;
	}

	public L1Ability getAbility() {
		return ability;
	}

	public L1Resistance getResistance() {
		return resistance;
	}

	public L1AC getAC() {
		return ac;
	}
	
	public L1SkillStatus getSkill(){
		return skillStatus;
	}
	
	public L1MoveState getMoveState() {
		return moveState;
	}
	
	public int getActionStatus() {
		return actionStatus;
	}
	public void setActionStatus(int val) {
		actionStatus = val;
	}
	
	public int getImmuneLevel(){
		return immunelevel;
	}
	public void setImmuneLevel(int val){
		immunelevel = val;
	}
	
	public int getArmorBreakID(){
		return armorBreakID;
	}
	public void setArmorBreakID(int val){
		armorBreakID = val;
	}
	
    public L1PcInstance getEternityAttacker() {
    	return _eternityAttacker;
    }
    public void setEternityAttacker(L1PcInstance val) {
    	_eternityAttacker = val;
    }
	
    public double getEternityDmg() {
    	return _eternityDmg;
    }
    public void setEternityDmg(double val) {
    	_eternityDmg = val;
    }

    /**
     * 프레셔 상태시 대미지 누적
     * @param attacker
     * @param dmg
     */
	public void pressureCheck(L1Character attacker, double dmg){
    	if (skillStatus.hasSkillEffect(L1SkillId.PRESSURE) || skillStatus.hasSkillEffect(L1SkillId.STATUS_PRESSURE_DEATH_RECAL)) {
    		_pressureDmg += (int)(dmg * (attacker == _pressureAttacker ? 0.5D : 0.1D));// 시전자 50% 그외 10%
    	}
    }
	
	/**
	 * 프레셔 데스리콜 상태 검증
	 * 불가행동 패널티를 부여한다.
	 * @return boolean
	 */
	public boolean isPressureDeathRecall(){
		if (!skillStatus.hasSkillEffect(L1SkillId.STATUS_PRESSURE_DEATH_RECAL)) {
			return false;
		}
		if (this instanceof L1PcInstance && _pressureAttacker != null) {
			((L1PcInstance) this).receiveDamage(_pressureAttacker, _pressureDmg > 100 ? _pressureDmg : 100);
		}
		broadcastPacketWithMe(new S_Effect(getId(), 19349), true);
		broadcastPacketWithMe(new S_DoActionGFX(getId(), ActionCodes.ACTION_Damage), true);
		return true;
    }
	
	public boolean isStrikerGaleShotHold(){
		return _strikerGaleShotHold;
	}
	public void setStrikerGaleShotHold(boolean val){
		_strikerGaleShotHold = val;
	}

	/**
	 * @return the lastWarp
	 */
	private long lastWarp=0;
	public long getLastWarp() {
		return lastWarp;
	}

	/**
	 * @param lastWarp the lastWarp to set
	 */
	public void setLastWarp(long lastWarp) {
		this.lastWarp = lastWarp;
	}	
	
}

