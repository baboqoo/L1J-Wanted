package l1j.server.RobotSystem;

import static l1j.server.server.model.skill.L1SkillId.POLLUTE_WATER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameServerSetting;
import l1j.server.server.command.executor.L1Robot4;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Astar;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1HateList;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Node;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1GuardianInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1ScarecrowInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.model.sprite.AcceleratorChecker.ACT_TYPE;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.action.S_AttackPacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.action.S_MoveCharPacket;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.spell.S_SkillIconGFX;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class L1RobotAI {		

	private final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };	

	private int searchCount = 0;
	private int pickupCount = 0;
	private int teleportDelayCount = 2;
	private int moveCount = 0;
	private int moveDelayCount = 0;
	private int polyCount = 0;
	private int polyDelayCount = 0;
	private int skillDelayCount = 0;
	private int dieDelayCount = 0;
	private int postionCount = 0;
	private int curePostionCount = 0;
	private int cancellationCount = 0;	


	private int x = 0;
	private int y = 0;
	private L1Object targetObj = null;

	public int getCancellationCount() {
		return cancellationCount;
	}

	public void setCancellationCount(int cancellationCount) {
		this.cancellationCount = cancellationCount;
	}

	private L1PcInstance robot; // 로봇대상		
	private L1Character target;
	private L1Object dropItem;
	private L1Astar aStar;
	private L1Node tail;
	private int iCurrentPath;
	private int[][] iPath;

	private int gfxid;
	@SuppressWarnings("unused")
	private int weapon;
	private int interval;
	private double HASTE_RATE = 0.745;
	private double WAFFLE_RATE = 0.874;
	private double THIRDSPEED_RATE = 0.874;
	private String polyList = "6274, 6277, 6273, 6276";
	private L1Inventory groundInventory;
	private L1Object object;


	private long ai_start_time; // 인공지능 시작된 시간값
	@SuppressWarnings("unused")
	private long ai_time; // 인공지능 처리에 사용될 프레임참고 값

	// 텔레포트한 사냥터의 위치 임시 저장용.
	private RobotLocation location;
	private L1Skills l1skills;
	// 인공지능 상태 변수	
	public final int AI_STATUS_SETTING = 0;  // 초반 세팅처리
	public final int AI_STATUS_WALK = 1;     // 랜덤워킹 상태
	public final int AI_STATUS_ATTACK = 2;   // 공격 상태
	public final int AI_STATUS_DEAD = 3;     // 죽은 상태
	public final int AI_STATUS_CORPSE = 4;   // 시체 상태
	public final int AI_STATUS_SPAWN = 5;    // 스폰 상태
	public final int AI_STATUS_ESCAPE = 6;   // 도망 상태
	public final int AI_STATUS_PICKUP = 7;   // 아이템 줍기 상태
	public final int AI_STATUS_SHOP = 8;     // 상점으로이동처리

	// 마을에서 버프 스탭 확인용
	private int buff_step;

	public L1RobotAI(L1PcInstance pc) {
		robot = pc;
		attackList = new L1HateList();
		aStar = new L1Astar();
		iPath = new int[300][2];
		postionCount = CommonUtil.random(50, 200);				
	}

	private boolean active = false;    // 활성화

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int type = 1;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int statusType = 0;

	private int ai_Status = 1; // 인공지능 처리해야할 상태

	public int getAiStatus() {
		return ai_Status;
	}

	public void setAiStatus(int aiStatus) {
		this.ai_Status = aiStatus;
	}

	private L1HateList attackList;

	public L1HateList getAttackList() {
		return attackList;
	}

	public void setAttackList(L1HateList attackList) {
		this.attackList = attackList;
	}


	private int getFrame(int gfxmode) {
		int AttackSpeed = 0;

		switch (robot.getRobotAi().getType()) { 
		case 1:
			AttackSpeed = 600;
			break;
		case 2: 
			AttackSpeed = 0;
			break;

		}

		if (robot.getSpriteId() == robot.getClassId())
			AttackSpeed = 600;
		else {
			if (!robot.isElf())
				AttackSpeed = 0;
			else
				AttackSpeed = 200;
		}

		switch (gfxmode) {
		case 0: // 이동
			return 1000;
		case 1: // 공격
			return 800 + AttackSpeed;
		}
		return 1000;
	}


	public void toAI(long time) {
		try {
			if (!isAi(time))
				return;
			if (robot.getName().equals(GameServerSetting.ROBOT_NAME)) {
				for (L1Character cha : attackList.toTargetArrayList()) {
					System.out.println(cha.getName() + StringUtil.EmptyOneString + cha.isDead());
				}
				if (target != null)
					System.out.println(ai_Status + StringUtil.EmptyOneString + target.getName());
				else 
					System.out.println(ai_Status);	
			}
			if (robot.isDead()) {
				toAiDead();
				return;
			}

			if (type != 1 && ai_Status != AI_STATUS_SETTING && robot.getMap().isSafetyZone(robot.getLocation())) {
				attackList.clear();
				target = null;
				dropItem = null;				
				groundInventory = null;
				object = null;
				setAiStatus(AI_STATUS_SETTING);
			}

			switch (ai_Status) {
			case AI_STATUS_WALK:				
				toSearchTarget();
				if (attackList.toTargetArrayList().size() > 0)					
					setAiStatus(AI_STATUS_ATTACK);
				break;
			case AI_STATUS_PICKUP:
				pickup(robot);
				break;
			case AI_STATUS_ATTACK:
			case AI_STATUS_ESCAPE:
				if (attackList.toTargetArrayList().size() == 0)
					setAiStatus(AI_STATUS_WALK);				
				break;				
			default:
				break;
			}

			if (getAiStatus() != AI_STATUS_DEAD && robot.isDead())
				setAiStatus(AI_STATUS_DEAD);

			ai_start_time = time;


			// 물약 복용 처리.

			toHealingPostion(false);			

			// 속도 물약 복용 처리.
			if (cancellationCount <= 0) {
				toSpeedPostion();

				// 변신 처리
				if (type == 1) {
					int ran = CommonUtil.random(100);

					if (ran <= 5 && polyDelayCount <= 0) {
						if (polyCount <= 0 ) {				
							toPolyMorph();
							polyCount = CommonUtil.random(3500, 4500);							
						} 
						if (polyDelayCount <= 0)
							polyDelayCount = CommonUtil.random(3000, 4000);				
						else
							polyDelayCount--;
					} else
						polyDelayCount = CommonUtil.random(3000, 4000);				

					if (polyDelayCount > 0)
						polyDelayCount--;
					if (polyCount > 0)
						polyCount--;
				} else {
					if (robot.getSpriteId() == robot.getClassId())
						toPolyMorph();
				}

				cancellationCount = 0;
			} else {
				cancellationCount--;
				polyCount = 0;
				return;
			}

			switch (getAiStatus()) {
			case AI_STATUS_SETTING:				
				toAiSetting();			
				break;
			case AI_STATUS_WALK:
				toRandomWalk();
				break;
			case AI_STATUS_ATTACK:
				toAiAttack();
				break;
			case AI_STATUS_DEAD:
				toAiDead();
				break;
			case AI_STATUS_CORPSE:
				//toAiCorpse(time);
				break;
			case AI_STATUS_SPAWN:
				//toAiSpawn(time);
				break;
			case AI_STATUS_ESCAPE:
				//toAiEscape(time);
				break;
			case AI_STATUS_PICKUP:
				pickup(robot);
				break;
			case AI_STATUS_SHOP:
				toShopMove(time);
				break;
			default://여기
				ai_time = 1000;
				break;
			}	
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	//public void 허수아비처리(long time) {
	public void scareCrowProcessing (long time) {
		try {

			if (!isAi(time))
				return;

			if (robot.getName().equals(GameServerSetting.ROBOT_NAME)) {
				for (L1Character cha : attackList.toTargetArrayList()) {
					System.out.println(cha.getName() + StringUtil.EmptyOneString + cha.isDead());
				}
				if (target != null)
					System.out.println(ai_Status + StringUtil.EmptyOneString + target.getName());
				else 
					System.out.println(ai_Status);	
			}
			if (robot.isDead()) {
				toAiDead();
				return;
			}

			if (type != 1 && ai_Status != AI_STATUS_SETTING && robot.getMap().isSafetyZone(robot.getLocation())) {
				attackList.clear();
				target = null;
				dropItem = null;				
				groundInventory = null;
				object = null;
				setAiStatus(AI_STATUS_SETTING);
			}

			switch (ai_Status) {
			case AI_STATUS_WALK:				
				toSearchTarget();

				if (attackList.toTargetArrayList().size() > 0)				
					setAiStatus(AI_STATUS_ATTACK);
				break;
			case AI_STATUS_PICKUP:
				pickup(robot);
				break;
			case AI_STATUS_ATTACK:
			case AI_STATUS_ESCAPE:
				if (attackList.toTargetArrayList().size() == 0)
					setAiStatus(AI_STATUS_WALK);				
				break;				
			default:
				break;
			}

			if (getAiStatus() != AI_STATUS_DEAD && robot.isDead())
				setAiStatus(AI_STATUS_DEAD);
			ai_start_time = time;
			// 물약 복용 처리.

			toHealingPostion(false);			

			// 속도 물약 복용 처리.
			if (cancellationCount <= 0) {
			//	toSpeedPostion();

				// 변신 처리
				if (type == 1) {
					int ran = CommonUtil.random(100);

					if (ran <= 5 && polyDelayCount <= 0) {
						if (polyCount <= 0 ) {				
							toPolyMorph();
							polyCount = CommonUtil.random(3500, 4500);							
						} 
						if (polyDelayCount <= 0)
							polyDelayCount = CommonUtil.random(3000, 4000);				
						else
							polyDelayCount--;
					} else
						polyDelayCount = CommonUtil.random(3000, 4000);				

					if (polyDelayCount > 0)
						polyDelayCount--;
					if (polyCount > 0)
						polyCount--;
				} else {
					if (robot.getSpriteId() == robot.getClassId())
						toPolyMorph();
				}

				cancellationCount = 0;
			} else {
				cancellationCount--;
				polyCount = 0;
				return;
			}

			switch (getAiStatus()) {
			case AI_STATUS_SETTING:				
				toAiSetting();	
				break;
			case AI_STATUS_WALK:
				//toRandomWalk();
				si_move();
				break;
			case AI_STATUS_ATTACK:
//				toAiAttack();
				to_ai_attack();
				break;
			case AI_STATUS_DEAD:
				toAiDead();
				break; 

			case AI_STATUS_PICKUP:
				pickup(robot);
				break;
			case AI_STATUS_SHOP:
				toShopMove(time);
				break;
			default://여기
				ai_time = 1000;
				break;
			}		
		} catch (Exception e) {
			//e.printStackTrace();
		}

	}

	private void toShopMove(long time) {
		try {
			if (targetObj == null) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(robot, 20)) {
					if (obj instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) obj;
						if (npc.getNpcId() == 900088) {							
							x = npc.getX() - L1Robot4.random(1, 5);
							y = npc.getY() - L1Robot4.random(1, 5);
							targetObj = obj;
							break;
						}
					}
				}
			}

			if (targetObj == null) {				
				setAiStatus(AI_STATUS_WALK);
				return;
			}

			if (x != 0 && y != 0) {
				if (!isDistance(robot.getX(), robot.getY(), robot.getMapId(), x, y, targetObj.getMapId(), 1)) {
					ai_time = 500;
					toMoving(targetObj, x, y, 0, true);					
					return;					
				} else {
					targetObj = null;
					x = 0;
					y = 0;
				}
			}
			// 상태 변경.					
			setAiStatus(AI_STATUS_WALK);		
		} catch (Exception e) {

		}
	}

	public void checkTarget() {
		try {
			if (target == null || target.getMapId() != robot.getMapId() || target.isDead() || target.getCurrentHp() <= 0
					|| (target.isInvisble() && !attackList.containsKey(target))) {
				if (target != null)
					tagertClear();
				if (!attackList.isEmpty()) {
					target = attackList.getMaxHateCharacter();
					checkTarget();
				}
			}
		} catch (Exception e) {
		}
	}

	public void targetRemove(L1Character target) {
		attackList.remove(target);
		if (target != null && target.equals(target))
			target = null;
	}

	public void tagertClear() {
		if (target == null)
			return;
		attackList.remove(target);
		target = null;
	}

	public void setHate(L1Character cha, int hate) {
		if (cha != null && cha.getId() != robot.getId()) {
			if (!attackList.containsKey(cha))
				attackList.add(cha, hate);
			if (target == null)
				target = attackList.getMaxHateCharacter();
			
			checkTarget();
		}
	}

	/**
	 * 인공지능 활성화할 시간이 됫는지 확인해주는 함수.
	 * 
	 * @param time
	 * @return
	 */
	private boolean isAi(long time) {				
		long temp = time - ai_start_time;
		if (robot.isStop())
			return false;	

		gfxid = robot.getSpriteId();
		weapon = robot.getCurrentWeapon();		
		//interval = SprTable.getInstance().getAttackSpeed(gfxid, weapon + 1);//사냥용 로봇 (공속/이속) 0 0
		interval = robot.getSprite().getAttackSpeed(robot.getCurrentWeapon() + 1);

		
		// 윈드세클 걸린상태라면
		if (robot.equals(ACT_TYPE.ATTACK) && this.robot.getSkill().hasSkillEffect(L1SkillId.WIND_SHACKLE)) 
			interval *= 2;

		if (robot.isHaste())
			interval *= HASTE_RATE;
		if (robot.getMoveState().getBraveSpeed() == 4)
			interval *= HASTE_RATE;
		if (robot.isBrave())
			interval *= HASTE_RATE;
		if (robot.isElfBrave())
			interval *= WAFFLE_RATE;
		if (robot.isDrunken())
			interval *= THIRDSPEED_RATE;

		if (polyList.indexOf(String.valueOf(gfxid)) > -1) {			
			if (statusType == 1)
				interval += 50;
			else if (statusType == 2)
				interval += 120;
		} else {
			if (type != 1) {
				if (statusType == 1)
					interval -= 10;
				else if (statusType == 2)
					interval -= 0;
			}
		}	

		if (robot.isIllusionist()) {
			if (type != 1) {
				if (statusType == 1)
					interval -= 10;
				else if (statusType == 2)
					interval += 50;
			}
		}

		if (temp < interval)
			return false;

		if (temp >= interval) {
			if (robot.getName().equals(GameServerSetting.ROBOT_NAME))
				System.out.println(temp + StringUtil.EmptyOneString + interval);
			ai_start_time = time;
			return true;
		}
		return false;
	}

	/**
	 * 마을에서 기본적인 세팅처리할때 사용.
	 * 
	 * @param time
	 */
	private void toAiSetting() {
		statusType = 0;		

		ai_time = getFrame(0);

		// 마을로 귀환.
		if (!robot.getMap().isSafetyZone(robot.getLocation())) {
			// 딜레이.		
			int ran = L1Robot4.random(5, 15);
			ai_time = 1000 * ran;

			robot.getMap().setPassable(robot.getLocation(), true);

			int[] loc = Getback.GetBack_Location(robot);
			teleport(robot, loc[0], loc[1], (short) loc[2]);
			return;
		}

		// hp 회복처리.
		if (robot.getMaxHp() != robot.getCurrentHp()) {
			toHealingPostion(true);
			return;
		}

		// 로봇 변신 처리.		
		if (robot.getSpriteId() == robot.getClassId()) {
			Poly(robot);
			return;
		}

		if (skillDelayCount > 0) {
			skillDelayCount--;
			return;
		}

		location = RobotAIThread.getLocation();
		if (location != null) {
			int count = 0;
			while (true) {
				for(L1PcInstance player : L1World.getInstance().getAllPlayers()) {
					if (player.getRobotAi() != null && (short)location.map == player.getMapId())
						count++;
				}
				if (count < 3)
					break;
				location = RobotAIThread.getLocation();
				count = 0;
			}

			teleport(robot, location.x, location.y, (short) location.map);
		}
		setAiStatus(AI_STATUS_WALK);
		buff_step = 0;

	}

	private void toBuff() {
		try {
			if (skillDelayCount > 0) {
				skillDelayCount--;
				return;
			}

			// 버프시전.
			if (robot.isKnight()) {
				switch (buff_step++) {
				case 0:
					if (robot.getSkill().hasSkillEffect(L1SkillId.REDUCTION_ARMOR))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.REDUCTION_ARMOR, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.REDUCTION_ARMOR);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				default:
					buff_step = 0;
					break;
				}
			} else if (robot.isElf()) {
				switch (buff_step++) {
				case 0:
					if (robot.getSkill().hasSkillEffect(L1SkillId.STORM_SHOT))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.STORM_SHOT, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.STORM_SHOT);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				default:
					buff_step = 0;
					break;
				}
			} else if (robot.isWizard()) {
				switch (buff_step++) {
				case 0:
					if (robot.getSkill().hasSkillEffect(L1SkillId.ADVANCE_SPIRIT_1) || robot.getLevel() < 65)
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.ADVANCE_SPIRIT_1, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.ADVANCE_SPIRIT_1);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				case 1:
					if (robot.getSkill().hasSkillEffect(L1SkillId.PHYSICAL_ENCHANT_DEX))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.PHYSICAL_ENCHANT_DEX, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.PHYSICAL_ENCHANT_DEX);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				case 2:
					if (robot.getSkill().hasSkillEffect(L1SkillId.PHYSICAL_ENCHANT_STR))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.PHYSICAL_ENCHANT_STR, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.PHYSICAL_ENCHANT_STR);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;				
				case 3:
					if (robot.getSkill().hasSkillEffect(L1SkillId.BERSERKERS))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.BERSERKERS, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.BERSERKERS);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				case 4:
					if (robot.getSkill().hasSkillEffect(L1SkillId.HOLY_WALK))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.HOLY_WALK, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.HOLY_WALK);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				default:
					buff_step = 0;
					break;
				}
			} else if (robot.isDarkelf()) {
				switch (buff_step++) {
				case 0:
					if (robot.getSkill().hasSkillEffect(L1SkillId.ENCHANT_VENOM))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.ENCHANT_VENOM, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.ENCHANT_VENOM);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				case 1:
					if (robot.getSkill().hasSkillEffect(L1SkillId.SHADOW_ARMOR))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.SHADOW_ARMOR, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.SHADOW_ARMOR);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				case 2:
					if (robot.getSkill().hasSkillEffect(L1SkillId.DOUBLE_BRAKE))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.DOUBLE_BRAKE, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.DOUBLE_BRAKE);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				case 3:
					if (robot.getSkill().hasSkillEffect(L1SkillId.UNCANNY_DODGE))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.UNCANNY_DODGE, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.UNCANNY_DODGE);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				case 4:
					if (robot.getSkill().hasSkillEffect(L1SkillId.DRESS_DEXTERITY))
						return;
					Broadcaster.broadcastPacket(robot, new S_DoActionGFX(robot.getId(), 19), true);
					new L1SkillUse().handleCommands(robot, L1SkillId.DRESS_DEXTERITY, robot.getId(), robot.getX(), robot.getY(), 0, L1SkillUseType.GMBUFF);
					l1skills = SkillsTable.getTemplate(L1SkillId.DRESS_DEXTERITY);
					skillDelayCount = l1skills.getReuseDelay() / 1000 + 2;
					return;
				default:
					buff_step = 0;
					break;
				}
			} else if (robot.isDragonknight()) {
				//
			} else if (robot.isIllusionist()) {
				//
			} else if (robot.isCrown()) {

			}
			buff_step = 0;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 객체가 공격가능한 상태인지 확인해주는 함수.
	 * 
	 * @param o
	 * @param walk
	 * @return
	 */
	public boolean isAttack(L1Character cha, boolean walk) {
		try {
			if (cha == null)
				return false;
			if (cha.isBind())
				return false;
			if (cha.getMap().isSafetyZone(cha.getLocation()))
				return false;
			if (cha.isDead())
				return false;
			if (cha.isInvisble())
				return false;
			if (!isDistance(robot.getX(), robot.getY(), robot.getMapId(), cha.getX(), cha.getY(), cha.getMapId(), 12))
				return false;
			if (!robot.glanceCheck(15, cha.getX(), cha.getY(), cha instanceof L1DoorInstance))
				return false;

			return true;
		} catch (Exception e) {			
			//e.printStackTrace();
			return false;
		}
	}
	
	private void to_ai_attack() {
		try {
			if (isDistance(robot.getX(), robot.getY(), robot.getMapId(), target.getX(), target.getY(), target.getMapId(), robot.isElf() ? 8 : 1)) {
				ai_time = getFrame(1);					
				if (robot.glanceCheck(15, target.getX(), target.getY(), target instanceof L1DoorInstance))
				//	toAttack(target, 0, 0, robot.isElf(), 1, 0);
					target.onAction(robot);
				else
					toMoving(target, target.getX(), target.getY(), 0, true);
			} else {
				ai_time = getFrame(0);
				toMoving(target, target.getX(), target.getY(), 0, true);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void toAiAttack() {//사냥로봇 설정
		try {			
			if (target != null && target.isDead()) {
				if (robot.getName().equals(GameServerSetting.ROBOT_NAME))
					//System.out.println("로봇다이");
					System.out.println("Robot Die");
				attackList.remove(target);			
				target = null;	

				if (isPickup(robot)) {
					setAiStatus(AI_STATUS_PICKUP);
					attackList.clear();
				}
				return;
			}

			if (dropItem != null) {
				pickupCount++;

				if (pickupCount >= 5) {
					dropItem = null;
					groundInventory = null;
					object = null;
					setAiStatus(AI_STATUS_WALK);
					pickupCount = 0;
				}
				if (robot.getName().equals(GameServerSetting.ROBOT_NAME))
					//System.out.println("로봇드랍   " + ((L1ItemInstance)dropItem).getDescKr());
					System.out.println("Robot Drop: " + ((L1ItemInstance)dropItem).getDesc());
				return;
			}
			//워킹
			if (type != 1 && target != null && !(target instanceof L1PcInstance) && !(target instanceof L1GuardianInstance) && ((L1MonsterInstance)target).getHiddenStatus() >= 1) {
				attackList.remove(target);			
				target = null;
				return;
			}

			target = findDangerousObject();

			if (type != 1 && !isAttack(target, true)) {
				attackList.remove(target);			
				target = null;
			}

			// 객체를 찾지못했다면 랜덤워킹 변경.
			if (target == null && dropItem == null) {				
				ai_time = 0;
				setAiStatus(AI_STATUS_WALK);
				return;
			}
			// 객체 거리 확인
			if (type > 1 && isDistance(robot.getX(), robot.getY(), robot.getMapId(), target.getX(), target.getY(), target.getMapId(), robot.isElf() ? 8 : 1)) {
				// 사용자는 구분 처리.
				if (target instanceof L1PcInstance) { // PC랑 싸운다면.		
					//마법사
					if (robot.isWizard() && robot.getCurrentMp() >= 50 && isDistance(robot.getX(), robot.getY(), robot.getMapId(), target.getX(), target.getY(), target.getMapId(), robot.isWizard() ? 5 : 1)) {
						if (target.getSkill().hasSkillEffect(L1SkillId.SILENCE) || 
								target.getSkill().hasSkillEffect(L1SkillId.DECAY_POTION) || 
								target.getSkill().hasSkillEffect(L1SkillId.SHOCK_STUN) || 
								target.getSkill().hasSkillEffect(L1SkillId.FORCE_STUN) ||
								target.getSkill().hasSkillEffect(L1SkillId.PANTERA) ||
								target.getSkill().hasSkillEffect(L1SkillId.EMPIRE) ||
								target.getSkill().hasSkillEffect(L1SkillId.THUNDER_GRAB) || 
								target.getSkill().hasSkillEffect(L1SkillId.MIND_BREAK) || 
								target.getSkill().hasSkillEffect(L1SkillId.PANIC) || 
								target.getSkill().hasSkillEffect(L1SkillId.IllUSION_AVATAR) || 
								target.getSkill().hasSkillEffect(L1SkillId.STRIKER_GALE) || 
								target.getSkill().hasSkillEffect(L1SkillId.POLLUTE_WATER) || 
								target.getSkill().hasSkillEffect(L1SkillId.EARTH_BIND))
							return;
						else {
							toWizardMagic(target);
							return;
						}
						//기사
					} else if (robot.isKnight() && robot.getCurrentMp() >= 50 && isDistance(robot.getX(), robot.getY(), robot.getMapId(), target.getX(), target.getY(), target.getMapId(), robot.isKnight() ? 2 : 1)) {
						if (target.getSkill().hasSkillEffect(L1SkillId.SILENCE) || 
								target.getSkill().hasSkillEffect(L1SkillId.DECAY_POTION) || 
								target.getSkill().hasSkillEffect(L1SkillId.SHOCK_STUN) || 
								target.getSkill().hasSkillEffect(L1SkillId.FORCE_STUN) ||
								target.getSkill().hasSkillEffect(L1SkillId.PANTERA) ||
								target.getSkill().hasSkillEffect(L1SkillId.EMPIRE) ||
								target.getSkill().hasSkillEffect(L1SkillId.THUNDER_GRAB) || 
								target.getSkill().hasSkillEffect(L1SkillId.MIND_BREAK) || 
								target.getSkill().hasSkillEffect(L1SkillId.PANIC) || 
								target.getSkill().hasSkillEffect(L1SkillId.IllUSION_AVATAR) || 
								target.getSkill().hasSkillEffect(L1SkillId.STRIKER_GALE) || 
								target.getSkill().hasSkillEffect(L1SkillId.POLLUTE_WATER) || 
								target.getSkill().hasSkillEffect(L1SkillId.EARTH_BIND))
							return;
						else {
							toKnightMagic(target);
							return;
						}
						//용기사
					} else if (robot.isDragonknight() && robot.getCurrentMp() >= 50 && isDistance(robot.getX(), robot.getY(), robot.getMapId(), target.getX(), target.getY(), target.getMapId(), robot.isDragonknight() ? 2 : 1)) {
						if (target.getSkill().hasSkillEffect(L1SkillId.SILENCE) || 
								target.getSkill().hasSkillEffect(L1SkillId.DECAY_POTION) || 
								target.getSkill().hasSkillEffect(L1SkillId.SHOCK_STUN) || 
								target.getSkill().hasSkillEffect(L1SkillId.FORCE_STUN) ||
								target.getSkill().hasSkillEffect(L1SkillId.PANTERA) ||
								target.getSkill().hasSkillEffect(L1SkillId.EMPIRE) ||
								target.getSkill().hasSkillEffect(L1SkillId.THUNDER_GRAB) || 
								target.getSkill().hasSkillEffect(L1SkillId.MIND_BREAK) || 
								target.getSkill().hasSkillEffect(L1SkillId.PANIC) || 
								target.getSkill().hasSkillEffect(L1SkillId.IllUSION_AVATAR) || 
								target.getSkill().hasSkillEffect(L1SkillId.STRIKER_GALE) || 
								target.getSkill().hasSkillEffect(L1SkillId.POLLUTE_WATER) || 
								target.getSkill().hasSkillEffect(L1SkillId.EARTH_BIND))
							return;
						else {
							toDragonknightMagic(target);
							return;
						}
						//환술사
					} else if (robot.isIllusionist() && robot.getCurrentMp() >= 50 
							&& isDistance(robot.getX(), robot.getY(), robot.getMapId(), 
									target.getX(), target.getY(), target.getMapId(), robot.isIllusionist() ? 4 : 1)) {
						if (target.getSkill().hasSkillEffect(L1SkillId.SILENCE) || 
								target.getSkill().hasSkillEffect(L1SkillId.DECAY_POTION) || 
								target.getSkill().hasSkillEffect(L1SkillId.SHOCK_STUN) || 
								target.getSkill().hasSkillEffect(L1SkillId.FORCE_STUN) ||
								target.getSkill().hasSkillEffect(L1SkillId.PANTERA) ||
								target.getSkill().hasSkillEffect(L1SkillId.EMPIRE) ||
								target.getSkill().hasSkillEffect(L1SkillId.THUNDER_GRAB) || 
								target.getSkill().hasSkillEffect(L1SkillId.MIND_BREAK) || 
								target.getSkill().hasSkillEffect(L1SkillId.PANIC) || 
								target.getSkill().hasSkillEffect(L1SkillId.IllUSION_AVATAR) || 
								target.getSkill().hasSkillEffect(L1SkillId.STRIKER_GALE) || 
								target.getSkill().hasSkillEffect(L1SkillId.POLLUTE_WATER) || 
								target.getSkill().hasSkillEffect(L1SkillId.EARTH_BIND))
							return;
						else {
							toIllusionistMagic(target);
							return;
						}
						//요정
					} else if (robot.isElf() && robot.getCurrentMp() >= 50 && isDistance(robot.getX(), robot.getY(), robot.getMapId(), target.getX(), target.getY(), target.getMapId(), robot.isElf() ? 7 : 1)) {
						if (target.getSkill().hasSkillEffect(L1SkillId.SILENCE) || 
								target.getSkill().hasSkillEffect(L1SkillId.DECAY_POTION) || 
								target.getSkill().hasSkillEffect(L1SkillId.SHOCK_STUN) || 
								target.getSkill().hasSkillEffect(L1SkillId.FORCE_STUN) ||
								target.getSkill().hasSkillEffect(L1SkillId.PANTERA) ||
								target.getSkill().hasSkillEffect(L1SkillId.EMPIRE) ||
								target.getSkill().hasSkillEffect(L1SkillId.THUNDER_GRAB) || 
								target.getSkill().hasSkillEffect(L1SkillId.MIND_BREAK) || 
								target.getSkill().hasSkillEffect(L1SkillId.PANIC) || 
								target.getSkill().hasSkillEffect(L1SkillId.IllUSION_AVATAR) || 
								target.getSkill().hasSkillEffect(L1SkillId.STRIKER_GALE) || 
								target.getSkill().hasSkillEffect(L1SkillId.POLLUTE_WATER) || 
								target.getSkill().hasSkillEffect(L1SkillId.EARTH_BIND))
							return;
						else {
							toElfMagic(target);
							return;
						}
					}
				} 
			}

			if (isDistance(robot.getX(), robot.getY(), robot.getMapId(), target.getX(), target.getY(), target.getMapId(), robot.isElf() ? 8 : 1)) {
				ai_time = getFrame(1);						
				if (robot.glanceCheck(15, target.getX(), target.getY(), target instanceof L1DoorInstance))
					toAttack(target, 0, 0, robot.isElf(), 1, 0);
				else {
					toMoving(target, target.getX(), target.getY(), 0, true);			
					moveDelayCount++;

					if (moveDelayCount >= 40) {
						L1Location newLocation = robot.getLocation().randomLocation(200, true);
						int newX = newLocation.getX();
						int newY = newLocation.getY();
						short mapId = (short) newLocation.getMapId();
						teleport(robot, newX, newY, mapId);
						moveDelayCount = 0;
					}
				}
			} else {
				ai_time = getFrame(0);
				toMoving(target, target.getX(), target.getY(), 0, true);	
			}
		} catch (Exception e) {
			attackList.remove(target);
			target = null;
			//e.printStackTrace();
		}		
	}

	/**
	 * 각클레스가 마법을 시전하게 한다.
	 * 
	 * @param o
	 */
	private void toWizardMagic(L1Object o) {
		if (target instanceof L1MonsterInstance)
			return;

		if (robot.isWizard()) {
			int a = CommonUtil.random(5);
			switch (a) {
			case 1:
				new L1SkillUse().handleCommands(robot, L1SkillId.SUNBURST, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1);
/*				L1Skills skill = SkillsTable.getInstance().getTemplate(L1SkillId.SUNBURST);
				robot.setCurrentMp(robot.getCurrentMp()-skill.getMpConseume());*/
				break;
			case 2:
				new L1SkillUse().handleCommands(robot, L1SkillId.SILENCE, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1);
				break;
			case 3:
				new L1SkillUse().handleCommands(robot, L1SkillId.ERUPTION, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1);
				break;
			case 4:
				new L1SkillUse().handleCommands(robot, L1SkillId.FATAL_POTION, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1);
				break;
			case 5:
				new L1SkillUse().handleCommands(robot, L1SkillId.WEAPON_BREAK, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1);
				break;

			default:
			}
		}
	}

	private void toKnightMagic(L1Object o) {
		if (robot.isKnight()) {
			new L1SkillUse().handleCommands(robot, L1SkillId.SHOCK_STUN, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
			robot.setCurrentMp(1);
		}
	}

	private void toDragonknightMagic(L1Object o) {
		if (target instanceof L1MonsterInstance)
			return;

		int a = CommonUtil.random(3);
		switch (a) {
		case 1:
			new L1SkillUse().handleCommands(robot, L1SkillId.FOU_SLAYER, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
			robot.setCurrentMp(1);
			break;
		case 2:
			new L1SkillUse().handleCommands(robot, L1SkillId.THUNDER_GRAB, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
			robot.setCurrentMp(1);
			break;
		case 3:
			new L1SkillUse().handleCommands(robot, L1SkillId.FOU_SLAYER, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
			robot.setCurrentMp(1);
			break;
		default:
		}
	}

	private void toIllusionistMagic(L1Object o) {
		if (target instanceof L1MonsterInstance)
			return;

		if (robot.isIllusionist()) {
			int a = CommonUtil.random(4);
			switch (a) {
			case 1:
				new L1SkillUse().handleCommands(robot, L1SkillId.MIND_BREAK, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1);
				break;
			case 2:
				new L1SkillUse().handleCommands(robot, L1SkillId.MIND_BREAK, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1);
				break;
			case 3:
				new L1SkillUse().handleCommands(robot, L1SkillId.PANIC, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1);
				break;
			case 4:
				new L1SkillUse().handleCommands(robot, L1SkillId.IllUSION_AVATAR, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1);
				break;

			default:
			}
		}
	}

	private void toElfMagic(L1Object o) {
		if (robot.isElf()) {
			int a = CommonUtil.random(6);
			switch (a) {
			case 1:
				Broadcaster.broadcastPacket(robot, new S_Effect(robot.getId(), 4394), true);
				for (int i = 0; i < 3; ++i) {
					toAttack(o, 0, 0, robot.isElf(), 1, 0);
				}
				robot.setCurrentMp(1000);
				break;
			case 2:
				Broadcaster.broadcastPacket(robot, new S_Effect(robot.getId(), 4394), true);
				for (int i = 0; i < 3; ++i) {
					toAttack(o, 0, 0, robot.isElf(), 1, 0);
				}
				robot.setCurrentMp(1000);
				break;
			case 3:
				new L1SkillUse().handleCommands(robot, L1SkillId.STRIKER_GALE, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1000);
				break;
			case 4:
				new L1SkillUse().handleCommands(robot, L1SkillId.POLLUTE_WATER, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1000);
				break;
			case 5:
				Broadcaster.broadcastPacket(robot, new S_Effect(robot.getId(), 2178), true);
				robot.setCurrentMp(1);
				break;
			case 6:
				new L1SkillUse().handleCommands(robot, L1SkillId.EARTH_BIND, o.getId(), o.getX(), o.getY(), 0, L1SkillUseType.GMBUFF);
				robot.setCurrentMp(1000);
				break;
			default:
			}
		}
	}

	/**
	 * 공격 처리 함수.
	 * 
	 * @param o
	 * @param x
	 * @param y
	 * @param bow
	 * @param gfxMode
	 * @param alpha_dmg
	 */
	private void toAttack(L1Object o, int x, int y, boolean bow, int gfxMode, int alpha_dmg) {
		try {
			statusType = 2;		
			moveDelayCount = 0;
			searchCount = 0;
			if (target == null) {
				if (robot.getName().equals(GameServerSetting.ROBOT_NAME))
					//System.out.println("로봇클리어");
					System.out.println("Robot Clear");
				attackList.clear();
				setAiStatus(AI_STATUS_WALK);
				return;
			}


			if (robot.getSkill().hasSkillEffect(L1SkillId.MEDITATION))
				robot.getSkill().killSkillEffectTimer(L1SkillId.MEDITATION);

			robot.delInvis();	

			if (target != null) {
				target.onAction(robot);
				if (robot.getLevel() >= 50 && robot.getWeapon().getItem().getType() == 17) {
					if (robot.getWeapon().getItemId() == 504) {
						robot.sendPackets(new S_Effect(robot.getId(), 6983), true);
						Broadcaster.broadcastPacket(robot, new S_Effect(robot.getId(), 6983), true);
					} else {
						robot.sendPackets(new S_Effect(robot.getId(), 7049), true);
						Broadcaster.broadcastPacket(robot, new S_Effect(robot.getId(), 7049), true);
					}
				}
			} else {
				attackList.remove(target);
				target = null;
			}
		} catch (Exception e) {
			target = null;
			attackList.clear();
			//e.printStackTrace();
		}
	}

	/**
	 * 공격목록이 등록되어 있지 않으면 주변에 공격목록을 검색한다.
	 */
	private void toSearchTarget() {
		int distance = 20;
		int temp = 0;

		if (type != 1)
			searchCount++ ;
		if (robot.getName().equals(GameServerSetting.ROBOT_NAME)) {
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			//System.out.println("리서치 " + attackList.toTargetArrayList().size());
			System.out.println("Research " + attackList.toTargetArrayList().size());
		}
		
		
		if (type != 1 && searchCount >= 15) {
			if (robot.getName().equals(GameServerSetting.ROBOT_NAME)) {
				System.out.println("+++++++++++++++++++++++++++++++++++++++");
				System.out.println("searchCount : " + searchCount);
			}
			attackList.clear();
			target = null;			
			searchCount = 0;
			L1Location newLocation = robot.getLocation().randomLocation(200, true);
			int newX = newLocation.getX();
			int newY = newLocation.getY();
			short mapId = (short) newLocation.getMapId();
			teleport(robot, newX, newY, mapId);
			moveCount = 0;
			teleportDelayCount = 2;
			return;
		}
		switch (type) {
		case 1:
			for (L1Object obj : L1World.getInstance().getVisibleObjects(robot)) {
				if (obj instanceof L1ScarecrowInstance) {					
					L1ScarecrowInstance sca = (L1ScarecrowInstance)obj;
					temp = getDistance(robot.getX(), robot.getY(), sca.getX(), sca.getY());	

					attackList.add((L1Character)obj, 0);

					if (temp <= distance) {
						target = sca;
						distance = temp;
					}
				}
			}
			break;
		case 2:			
			checkTarget();
			if (target == null && dropItem == null) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(robot, 15)) {
					if (obj == null)
						continue;

					if (obj instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance)obj;
						if (pc.getRobotAi() != null && (robot.getClanid() != pc.getClanid()))
							attackList.add(pc, 0);
						else if (pc.getRobotAi() == null && !pc.isGm()) {
							attackList.clear();
							target = null;			
							searchCount = 0;
							L1Location newLocation = robot.getLocation().randomLocation(200, true);
							int newX = newLocation.getX();
							int newY = newLocation.getY();
							short mapId = (short) newLocation.getMapId();
							teleport(robot, newX, newY, mapId);
							moveCount = 0;
							teleportDelayCount = 2;
							return;
						}
						
					}

					if (obj instanceof L1MonsterInstance) {
						L1MonsterInstance mon = (L1MonsterInstance)obj;
						if (mon.isDead())
							continue;
						if (mon.getHiddenStatus() >= 1)
							continue;
						if (attackList.containsKey((L1Character)mon))
							continue;
						if (!robot.glanceCheck(15, mon.getX(), mon.getY(), target instanceof L1DoorInstance))
							continue;

						attackList.add((L1Character)mon, 0);

					}
					
					
				}

				if (target != null && attackList.toTargetArrayList().size() == 0) {
					if (!attackList.containsKey(target)) {
						if (robot.getName().equals(GameServerSetting.ROBOT_NAME))
							//System.out.println("로봇텔레포트.");
							System.out.println("Robot Teleport.");
						attackList.clear();
						target = null;			
						searchCount = 0;
						L1Location newLocation = robot.getLocation().randomLocation(200, true);
						int newX = newLocation.getX();
						int newY = newLocation.getY();
						short mapId = (short) newLocation.getMapId();
						teleport(robot, newX, newY, mapId);									
						moveCount = 0;
						teleportDelayCount = 2;
						return;									
					}
				}
			}			
			break;
		default:
			break;
		}

	}

	/**
	 * 공격목록에 등록된 객체중 위험한 객체를 우선검색해서 리턴. : 리턴된 객체를 타켓으로 공격함.
	 * 
	 * @return
	 */
	private L1Character findDangerousObject() {		
		L1Character o = null;
		try {
			// 사용자 우선 검색.
			for (int i = attackList.toTargetArrayList().size() - 1; i >= 0; i--) {
				L1Character oo = attackList.toTargetArrayList().get(i);
				if (oo instanceof L1PcInstance) {	
					if (((L1PcInstance)oo).isInvisble()) {
						attackList.remove(oo);			
						target = null;
						continue;
					}					
					if (robot.getMapId() != oo.getMapId()) {
						attackList.remove(oo);			
						target = null;
						continue;
					}
					if (!robot.glanceCheck(15, oo.getX(), oo.getY(), target instanceof L1DoorInstance)){
						attackList.remove(oo);			
						target = null;
						continue;
					}
					if (o == null)
						o = oo;
					else if (!o.isDead() && getDistance(robot.getX(), robot.getY(), oo.getX(), oo.getY()) < getDistance(robot.getX(), robot.getY(), o.getX(), o.getY()))
						o = oo;
				}
			}

			if (o != null)
				return o;

							/** 몬스터 검색 **/
			for (int i = attackList.toTargetArrayList().size() - 1; i >= 0; i--) {
				L1Character oo = attackList.toTargetArrayList().get(i);
				if (oo.isDead()) {
					attackList.remove(oo);			
					target = null;
					continue;
				}
				if (!robot.glanceCheck(15, oo.getX(), oo.getY(), target instanceof L1DoorInstance)){
					attackList.remove(oo);			
					target = null;
					continue;
				}
				if (o == null)
					o = oo;
				else if (!o.isDead() &&  getDistance(robot.getX(), robot.getY(), oo.getX(), oo.getY()) < getDistance(robot.getX(), robot.getY(), o.getX(), o.getY()))
					o = oo;
			}
			return o;
		} catch (Exception e) {
			attackList.clear();
			target = null;
			//e.printStackTrace();
			return o;
		}
	}

	/**
	 * 물약 복용처리 함수.
	 * 
	 * @param direct
	 */
	private void toHealingPostion(boolean direct) {
		if (robot.isDead())
			return;

		if (robot.getSkill().hasSkillEffect(L1SkillId.DECAY_POTION)) { // 디케이포션 상태
		} else {
			curePostionCount++;
			if (robot.getPoison() != null && curePostionCount >= 3) {
				Broadcaster.broadcastPacket(robot, new S_Effect(robot.getId(), 192), true);
				robot.curePoison();
				curePostionCount = 0;
			}
		}

		int p = robot.getCurrentHpPercent();
		if (direct || p <= 70) { 
			if (robot.getSkill().hasSkillEffect(L1SkillId.DECAY_POTION))
				return;
			Broadcaster.broadcastPacket(robot, new S_Effect(robot.getId(), 197), true);

			int healHp = 30;
			// 포르트워타중은 회복량1/2배
			if (robot.getSkill().hasSkillEffect(POLLUTE_WATER))
				healHp /= 2;	

			robot.setCurrentHp(robot.getCurrentHp() + healHp);
			postionCount--;
		}



		if (postionCount <= 0) {
			setAiStatus(AI_STATUS_SETTING);
			postionCount = CommonUtil.random(50, 200);
			attackList.clear();
			target = null;
		}

		if (p < 50) {
			setAiStatus(AI_STATUS_SETTING);
			setAiStatus(AI_STATUS_SHOP);
			postionCount = CommonUtil.random(50, 200);
			attackList.clear();
			target = null;			
		}
		if (p < 30) {
			setAiStatus(AI_STATUS_SETTING);
			postionCount = CommonUtil.random(50, 200);
			attackList.clear();
			target = null;			
		}
	}

	private void toPolyMorph() {
		if (robot.getLevel() < 52)
			return;

		// 로봇 변신 처리.		
		if (robot.getSpriteId() == robot.getClassId()) {
			Poly(robot);
			return;
		}
	}

	
	private void toSpeedPostion() {		
		if (robot.isDead())
			return;
		if (robot.getMoveState().getMoveSpeed() == 0) {
			// 촐기떠러졌을경우 복용하기.
			Broadcaster.broadcastPacket(robot, new S_Effect(robot.getId(), 191), true);
			Broadcaster.broadcastPacket(robot, new S_SkillHaste(robot.getId(), 1, 0), true);
			robot.getMoveState().setMoveSpeed(1);			
			//  robot.setSkillEffect(STATUS_HASTE, 300 * 1000);
			return;
		}
		if (!robot.isWizard() && !robot.isDarkelf() && robot.getMoveState().getBraveSpeed() == 0) {
			// 용기 떠러졌을경우 복용하기.
			Broadcaster.broadcastPacket(robot, new S_SkillBrave(robot.getId(), 1, 0), true);
			robot.getMoveState().setBraveSpeed(1);
			robot.getSkill().setSkillEffect(STATUS_BRAVE, 300 * 1000);
			Broadcaster.broadcastPacket(robot, new S_Effect(robot.getId(), 751), true);
			return;
		}
		if (robot.isWizard() && !robot.getSkill().hasSkillEffect(L1SkillId.STATUS_BLUE_POTION)) {
			// 파랭이떠러졌을경우 복용하기.
			robot.sendPackets(new S_SkillIconGFX(S_SkillIconGFX.MANA_ICON, 600), true);
			robot.sendPackets(new S_Effect(robot.getId(), 190), true);
			robot.getSkill().setSkillEffect(STATUS_BLUE_POTION, 600 * 1000);
			return;
		}
	}

	private void toAiDead() {
		ai_time = getFrame(0);

		if (dieDelayCount <= 5) {
			dieDelayCount++;
			return;
		}

		L1PolyMorph.undoPoly(robot);

		int[] loc = Getback.GetBack_Location(robot);
		robot.removeAllKnownObjects();		
		Broadcaster.broadcastPacket(robot, new S_RemoveObject(robot), true);
		robot.setCurrentHp(robot.getLevel());
		robot.setFood(GameServerSetting.MIN_FOOD_VALUE); // 죽었을때 겟지? 10%
		robot.setDead(false);
		robot.setActionStatus(0);		
		L1World.getInstance().moveVisibleObject(robot, loc[2], loc[0], loc[1]);
		robot.setX(loc[0]);
		robot.setY(loc[1]);
		robot.setMap((short) loc[2]);
		robot.broadcastPacket(new S_PCObject(robot), true);

		attackList.clear();
		target = null;
		dieDelayCount = 0;

		setAiStatus(AI_STATUS_SETTING);
	}

	public void search_target() {
		try {
			if (target == null) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(robot)) {
					if (obj instanceof L1ScarecrowInstance) {					
						L1ScarecrowInstance sca = (L1ScarecrowInstance)obj;
						attackList.add((L1Character)obj, 0);
						target = sca;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void si_move() {
		try {
			ai_time = getFrame(0);
			int dir = checkObject(robot.getX(), robot.getY(), robot.getMapId(), CommonUtil.random(20));
			if (dir != -1) {
				ai_time = getFrame(0);
				toRandomMoving(dir);					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void toRandomWalk() {
		if (type != 1) {
			toSearchTarget();
			toBuff();
		}

		if (type != 1 && attackList.toTargetArrayList().size() == 0) {
			if (teleportDelayCount > 0) {
				teleportDelayCount--;
				return;
			}

			L1Location newLocation = robot.getLocation().randomLocation(200, true);
			int newX = newLocation.getX();
			int newY = newLocation.getY();
			short mapId = (short) newLocation.getMapId();
			teleport(robot, newX, newY, mapId);
			moveCount = 0;
			teleportDelayCount = 2;
		}

		ai_time = getFrame(0);

		if (type != 1 && !robot.getMap().isSafetyZone(robot.getLocation())) {
			int dir = checkObject(robot.getX(), robot.getY(), robot.getMapId(), CommonUtil.random(20));
			if (dir != -1) {
				ai_time = getFrame(0);
				toRandomMoving(dir);					
			}
		}

		moveCount++;
		//System.out.println("로봇이름 : " + robot.getName() + " 상태 : " + getAiStatus() + " 타입 : " + type);
		//System.out.println("몹리스트 : " + attackList.toHateArrayList().size() + " 물약수 : " + postionCount + " 움직임 : " + moveCount);
		if (moveCount >= 5) {
			if (robot.getMap().isTeleportable()) {				
				L1Location newLocation = robot.getLocation().randomLocation(200, true);
				int newX = newLocation.getX();
				int newY = newLocation.getY();
				short mapId = (short) newLocation.getMapId();				
				teleport(robot, newX, newY, mapId);
				moveCount = 0;
			} else
				moveCount = 0;
		}
	}

	private void toRandomMoving(int dir) {		
		if (dir >= 0) {
			int nx = 0;
			int ny = 0;

			int heading = 0;
			nx = HEADING_TABLE_X[dir];
			ny = HEADING_TABLE_Y[dir];
			heading = dir;

			robot.getMoveState().setHeading(heading);
			robot.getMap().setPassable(robot.getLocation(), true);

			int nnx = robot.getX() + nx;
			int nny = robot.getY() + ny;			

			robot.setX(nnx);
			robot.setY(nny);	
			robot.getMap().setPassable(robot.getLocation(), false);

			Broadcaster.broadcastPacket(robot, new S_MoveCharPacket(robot), true);
		}
	}


	private void toMoving(L1Object o, int x, int y, int h, boolean astar) {		
		if (target != null && target.isDead()) {
			attackList.remove(target);
			target = null;
			return;
		}

		statusType = 1;		
		if (astar) {
			aStar.ResetPath();
			tail = aStar.FindPath(robot, x, y, robot.getMapId(), target);
			if (tail != null) {
				iCurrentPath = -1;
				while (tail != null) {
					if (tail.x == robot.getX() && tail.y == robot.getY())
						// 현재위치 라면 종료
						break;
					iPath[++iCurrentPath][0] = tail.x;
					iPath[iCurrentPath][1] = tail.y;
					tail = tail.prev;
				}
				toMoving(iPath[iCurrentPath][0], iPath[iCurrentPath][1], calcheading(robot.getX(), robot.getY(), iPath[iCurrentPath][0], iPath[iCurrentPath][1]));
			} else {}
		} else {		
			toMoving(x, y, h);
		}
	}




	public int targetReverseDirection(int tx, int ty) {
		int dir = robot.targetDirection(tx, ty);
		dir += 4;
		if (dir > 7)
			dir -= 8;
		return dir;
	}

	public void setDirectionMove(int dir) {
		if (dir >= 0) {
			int nx = 0;
			int ny = 0;

			int heading = 0;
			nx = HEADING_TABLE_X[dir];
			ny = HEADING_TABLE_Y[dir];
			heading = dir;

			robot.getMoveState().setHeading(heading);
			robot.getMap().setPassable(robot.getLocation(), true);

			int nnx = robot.getX() + nx;
			int nny = robot.getY() + ny;
			robot.setX(nnx);
			robot.setY(nny);	
			robot.getMap().setPassable(robot.getLocation(), false);

			Broadcaster.broadcastPacket(robot, new S_MoveCharPacket(robot), true);
		}
	}

	private void toMoving(final int x, final int y, final int h) {
		try {
			robot.getMap().setPassable(robot.getLocation(), true);
			robot.getLocation().set(x, y);
			robot.getMoveState().setHeading(h);	
			robot.getMap().setPassable(robot.getLocation(), false);
			Broadcaster.broadcastPacket(robot, new S_MoveCharPacket(robot), true);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	private boolean isDistance(int x, int y, int m, int tx, int ty, int tm, int loc) {
		int distance = getDistance(x, y, tx, ty);
		if (loc < distance)
			return false;
		if (m != tm)
			return false;
		return true;
	}

	private int getDistance(int x, int y, int tx, int ty) {
		long dx = tx - x;
		long dy = ty - y;
		return (int) Math.sqrt(dx * dx + dy * dy);
	}


	private int calcheading(int myx, int myy, int tx, int ty) {
		if (tx > myx && ty > myy)
			return 3;
		else if (tx < myx && ty < myy)
			return 7;
		else if (tx > myx && ty == myy)
			return 2;
		else if (tx < myx && ty == myy)
			return 6;
		else if (tx == myx && ty < myy)
			return 0;
		else if (tx == myx && ty > myy)
			return 4;
		else if (tx < myx && ty > myy)
			return 5;
		else
			return 1;
	}

	public int checkObject(int x, int y, short m, int d) {
		L1Map map = L1WorldMap.getInstance().getMap(m);
		switch (d) {
		case 1:
			if (map.isPassable(x, y, 1))
				return 1;
			else if (map.isPassable(x, y, 0))
				return 0;
			else if (map.isPassable(x, y, 2))
				return 2;
			break;
		case 2:
			if (map.isPassable(x, y, 2))
				return 2;
			else if (map.isPassable(x, y, 1))
				return 1;
			else if (map.isPassable(x, y, 3))
				return 3;
			break;
		case 3:
			if (map.isPassable(x, y, 3))
				return 3;
			else if (map.isPassable(x, y, 2))
				return 2;
			else if (map.isPassable(x, y, 4))
				return 4;
			break;
		case 4:
			if (map.isPassable(x, y, 4))
				return 4;
			else if (map.isPassable(x, y, 3))
				return 3;
			else if (map.isPassable(x, y, 5))
				return 5;
			break;
		case 5:
			if (map.isPassable(x, y, 5))
				return 5;
			else if (map.isPassable(x, y, 4))
				return 4;
			else if (map.isPassable(x, y, 6))
				return 6;
			break;
		case 6:
			if (map.isPassable(x, y, 6))
				return 6;
			else if (map.isPassable(x, y, 5))
				return 5;
			else if (map.isPassable(x, y, 7))
				return 7;
			break;
		case 7:
			if (map.isPassable(x, y, 7))
				return 7;
			else if (map.isPassable(x, y, 6))
				return 6;
			else if (map.isPassable(x, y, 0))
				return 0;
			break;
		case 0:
			if (map.isPassable(x, y, 0))
				return 0;
			else if (map.isPassable(x, y, 7))
				return 7;
			else if (map.isPassable(x, y, 1))
				return 1;
			break;
		default:
			break;
		}
		return -1;
	}
	
	private void Poly(L1PcInstance pc) {		
		int polyid = 0;
		int time = 1800;
		int k3 = CommonUtil.random(100);
		if (pc.getWeapon() != null) {
			// 타입별 분류
			switch (pc.getWeapon().getItem().getType()) {
			// 활
			case 4:
			case 13:
				if (k3 < 20) { 
					int[] polyList = { 13635 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 50) { 
					int[] polyList = { 15814, 13346 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 70) { 
					int[] polyList = { 11378, 13631 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 70) { 
					int[] polyList = { 11394, 15848};
					polyid = polyList[CommonUtil.random(polyList.length)];
				}
				L1PolyMorph.doPoly(pc, polyid, time, 1);
				break;
				// 크로우 이도류
			case 11:
			case 12:
				if (k3 < 20) { 
					int[] polyList = { 15868 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 50) { 
					int[] polyList = { 11381, 15866 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 70) { 
					int[] polyList = { 11381, 15847 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 70) { 
					int[] polyList = { 11366};
					polyid = polyList[CommonUtil.random(polyList.length)];
				}
				L1PolyMorph.doPoly(pc, polyid, time, 1);
				break;
				// 지팡이
			case 7:
			case 16:
				if (k3 < 20) { 
					int[] polyList = { 15550 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 50) { 
					int[] polyList = { 15548, 15545 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 70) { 
					int[] polyList = { 3888, 15849 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 70) { 
					int[] polyList = { 11391};
					polyid = polyList[CommonUtil.random(polyList.length)];
				}
				L1PolyMorph.doPoly(pc, polyid, time, 1);
				break;
				// 그외..
			default:
				if (k3 < 20) { 
					int[] polyList = { 13152 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 50) { 
					int[] polyList = { 13153, 12681 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 70) { 
					int[] polyList = { 12702, 15850 };
					polyid = polyList[CommonUtil.random(polyList.length)];
				} else if (k3 < 70) { 
					int[] polyList = { 11392};
					polyid = polyList[CommonUtil.random(polyList.length)];
				}			
				L1PolyMorph.doPoly(pc, polyid, time, 1);
				break;
			}
		} else {
			if (k3 < 20) { 
				int[] polyList = { 11333 };
				polyid = polyList[CommonUtil.random(polyList.length)];
			} else if (k3 < 50) { 
				int[] polyList = { 13380, 7967 };
				polyid = polyList[CommonUtil.random(polyList.length)];
			} else if (k3 < 70) { 
				int[] polyList = { 15833, 3905 };
				polyid = polyList[CommonUtil.random(polyList.length)];
			} else if (k3 < 70) { 
				int[] polyList = { 15564};
				polyid = polyList[CommonUtil.random(polyList.length)];
			}	
			L1PolyMorph.doPoly(pc, polyid, time, 1);
		}	
	}

	private void teleport(L1PcInstance pc, int x, int y, short mapid) {
		try {
			pc.getMap().setPassable(pc.getLocation(), true);
			pc.getTeleport().start(x, y, mapid, CommonUtil.random(0, 7), false);
			//teleportAfterDelayCount = 3;
			attackList.clear();
			target = null;
			dropItem = null;
			groundInventory = null;
			object = null;
		} catch (Exception e) { 
			//e.printStackTrace();
		}
	}

	private boolean isPickup(L1PcInstance pc) {	
		try {
			if (L1World.getInstance().getVisibleObjects(pc, 1).size() >= 7) {
				dropItem = null;
				groundInventory = null;
				object = null;
				return false;
			}

			for (L1Object obj : L1World.getInstance().getObject()) {
				if (obj == null)
					continue;
				if (obj.getMapId() != pc.getMapId())
					continue;

				if (pc.getLocation().getTileLineDistance(obj.getLocation()) <= 4) {

					if (obj instanceof L1ItemInstance) {
						groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());		
						object = groundInventory.getItem(obj.getId());
						if (object != null) {
							dropItem = object;
							groundInventory = null;
							object = null;
							return true;						
						}
					}
				}				
			}
			return false;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}

	private void pickup(L1PcInstance pc) {
		try {		
			boolean dropCheck = true;
			try {	
				for (L1ItemInstance item : robot.getInventory().getItems()) {
					if (item.isEquipped())
						continue;
					if (item.getItem().getItemId() == L1ItemId.ADENA)
						continue;
					if (item.getItem().getItemId() == L1ItemId.PIXIE_FEATHER)
						continue;
					robot.getInventory().removeItem(item);
				}

				pickupCount++;

				if (pickupCount >= 5) {
					dropItem = null;
					groundInventory = null;
					object = null;
					setAiStatus(AI_STATUS_WALK);
					pickupCount = 0;
				}

				if (robot.getName().equals(GameServerSetting.ROBOT_NAME))
					//System.out.println("로봇토글");
					System.out.println("Robot Toggle");
				if (dropItem != null && !isDistance(robot.getX(), robot.getY(), robot.getMapId(), dropItem.getX(), dropItem.getY(), dropItem.getMapId(), (robot.isElf() ? 8 : 1))) {

					toMoving(dropItem, dropItem.getX(), dropItem.getY(), 0, true);

					groundInventory = L1World.getInstance().getInventory(dropItem.getX(), dropItem.getY(), dropItem.getMapId());		
					object = groundInventory.getItem(dropItem.getId());
					if (object == null) {
						dropItem = null;
						groundInventory = null;
						object = null;
						setAiStatus(AI_STATUS_WALK);
					} 
					return;
				}	
			} catch (Exception e) {
				dropItem = null;
				groundInventory = null;
				object = null;
				return;
			}

			for (L1Object obj : L1World.getInstance().getObject()) {
				if (obj.getMapId() != pc.getMapId())
					continue;

				if (pc.getLocation().getTileLineDistance(obj.getLocation()) <= 2) {
					if (obj instanceof L1ItemInstance) {
						L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());		
						L1Object object = groundInventory.getItem(obj.getId());
						if (object != null) {
							L1ItemInstance item = (L1ItemInstance)object;			
							groundInventory.tradeItem(item.getId(), item.getCount(), pc.getInventory());					
							pc.getLight().turnOnOffLight();
							Broadcaster.broadcastPacket(pc, new S_AttackPacket(pc, obj.getId(), ActionCodes.ACTION_Pickup), true);
							dropItem = null;
							groundInventory = null;
							object = null;
							dropCheck = false;

							if (isPickup(robot))
								setAiStatus(AI_STATUS_PICKUP);
							else
								setAiStatus(AI_STATUS_WALK);
							break;
						}
					}
				}				
			}

			if (dropCheck) {
				dropItem = null;
				groundInventory = null;
				object = null;
			}
		} catch (Exception e) { 
			//e.printStackTrace();
			attackList.remove(target);
			target = null;
			dropItem = null;
			groundInventory = null;
			object = null;
			setAiStatus(AI_STATUS_WALK);
		}
	}
}

