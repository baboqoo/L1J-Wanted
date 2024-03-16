package l1j.server.server.model.Instance;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.common.data.ChatType;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.templates.L1Npc;

public class L1GuardianInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(L1GuardianInstance.class.getName());

	private L1GuardianInstance _npc = this;

	/**
	 * @param template
	 */
	public L1GuardianInstance(L1Npc template) {
		super(template);
		_restCallCount = new AtomicInteger(0);
	}

	@Override
	public void searchTarget() {
		L1PcInstance targetPlayer = null;

		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if (pc == null || pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm() || pc.isGhost()) {
				continue;
			}
			if (!pc.isInvisble() || isAgroInvis()) {// 인비지체크
				if (!pc.isElf()) {// 요정이아니면
					targetPlayer = pc;
					wideBroadcastPacket(new S_NpcChatPacket(this, "$804", ChatType.CHAT_SHOUT), true); 
					// 그대여. 목숨이 아까우면 빨리 이곳을 떠날지어다. 이곳은 그대같은 자가 더럽히지 못할 신성한 곳이다.
					talkChangeHead(targetPlayer);// 공격 대기
					break;
				}
			}
		}
		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
	}

	// 링크의 설정
	@Override
	public void setLink(L1Character cha) {
		if (cha != null && _hateList.isEmpty()) { // 타겟이 없는 경우만 추가
			_hateList.add(cha, 0);
			checkTarget();
		}
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		_actived = false;
		startAI();
	}

	//private int entitem = 20; //껍질, 판털, 아라크네의 거미줄
	//private int entitem2 = 20; //열매
	//private int entitem3 = 20; //줄기
	
	@Override
	public void onAction(L1PcInstance player) {
		if (this == null || player == null) {
			return;
		}
	/*	if (player.getType() == 2 && player.getCurrentWeapon() == 0 && player.isElf()) {
			L1Attack attack = new L1Attack(player, this);

			if (attack.calcHit()) {
				if (getNpcTemplate().get_npcId() == 70848) { // 엔트
					int chance = _random.nextInt(100) + 1;
					if (chance <= 5) {
						if (entitem2 > 1){
							player.getInventory().storeItem(40506, 1); //열매
							player.sendPackets(new S_ServerMessage(143, "$755", "$794"), true); // \f1%0이%1를 주었습니다.
							entitem2 -= 1;
						} else {
//AUTO SRM: 							player.sendPackets(new S_NpcChatPacket(this, "...열매... 없다... 나중에...", L1ChatConstruct.TYPE_NORMAL), true);
							player.sendPackets(new S_NpcChatPacket(this, S_SystemMessage.getRefText(6), L1ChatConstruct.TYPE_NORMAL), true);
						}
					} else if (chance <= 40 && chance > 5) {
						if (entitem3 > 1){
							player.getInventory().storeItem(40507, 2); //줄기
							player.sendPackets(new S_ServerMessage(143, "$755", "$763"), true); // \f1%0이%1를 주었습니다.
							entitem3 -= 1;
						} else {
//AUTO SRM: 							player.sendPackets(new S_NpcChatPacket(this, "너에게 줄... 줄기가... 없다... 나중에 다시... 와라", L1ChatConstruct.TYPE_NORMAL), true);
							player.sendPackets(new S_NpcChatPacket(this, S_SystemMessage.getRefText(7), L1ChatConstruct.TYPE_NORMAL), true);
						}
					} else if (chance <= 50 && chance > 40) {
						if (entitem > 1){
							player.getInventory().storeItem(40505, 1); //껍질
							player.sendPackets(new S_ServerMessage(143, "$755", "$770"), true); // \f1%0이%1를 주었습니다.
							entitem -= 1;
						} else {
							//지금은 줄수 없다.멘트
//AUTO SRM: 							player.sendPackets(new S_NpcChatPacket(this, "껍질... 나중에... 와라... 없어", L1ChatConstruct.TYPE_NORMAL), true);
							player.sendPackets(new S_NpcChatPacket(this, S_SystemMessage.getRefText(8), L1ChatConstruct.TYPE_NORMAL), true);
						}
					}
				}
				if (getNpcTemplate().get_npcId() == 70850) { // 빵
					int chance = _random.nextInt(100) + 1;
					if (chance <= 30) {
						if (entitem > 1){
							player.getInventory().storeItem(40519, 5);
							player.sendPackets(new S_ServerMessage(143, "$753", "$760" + " (" + 5 + ")"), true); // \f1%0이%1를 주었습니다.
							entitem -= 1;
						} else {
//AUTO SRM: 							player.sendPackets(new S_NpcChatPacket(this, "나의 갈기털... 더이상... 나중에... 다시...", L1ChatConstruct.TYPE_NORMAL), true);
							player.sendPackets(new S_NpcChatPacket(this, S_SystemMessage.getRefText(9), L1ChatConstruct.TYPE_NORMAL), true);
						}
					}
				}
				if (getNpcTemplate().get_npcId() == 70846) {
					int chance = _random.nextInt(100) + 1;
					if (chance <= 30) {
						if (entitem > 1){
							player.getInventory().storeItem(40503, 2); //아라크네의 거미줄
							player.sendPackets(new S_ServerMessage(143, "$752", "$769"), true); // \f1%0이%1를 주었습니다.
							entitem -= 1;
						} else {
//AUTO SRM: 							player.sendPackets(new S_NpcChatPacket(this, "아프다 이놈아 나중에 다시 와.", L1ChatConstruct.TYPE_NORMAL), true);
							player.sendPackets(new S_NpcChatPacket(this, S_SystemMessage.getRefText(10), L1ChatConstruct.TYPE_NORMAL), true);
						}
					}
				}
				attack.calcDamage();
				attack.calcStaffOfMana();
				attack.addPcPoisonAttack(player, this);
			}
			attack.action();
			attack.commit();
		} else*/ if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(player, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(player, this);
			}
			attack.action();
			attack.commit();
		}
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		if (player == null || this == null) {
			return;
		}
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().getNpcId());
		if (talking != null) {
			talkChangeHead(player);
			if (player.isGm())
				player.sendPackets(new S_SystemMessage("Dialog " + talking.getCaoticAction() + " | " + talking.getNormalAction()), true);											

			player.sendPackets(new S_NPCTalkReturn(talking, getId(), player.getAlignment() < -500 ? 2 : 1), true);
		}
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) { 
		if (this == null || attacker == null) {
			return;
		}
		if (attacker instanceof L1PcInstance && damage > 0) {
			L1PcInstance pc = (L1PcInstance) attacker;
			if (pc.getType() == 2 && pc.getCurrentWeapon() == 0) {
			} else {
				if (getCurrentHp() > 0 && !isDead()) {
					if (damage >= 0) {
						setHate(attacker, damage);
					}
					if (damage > 0) {
						removeSleepSkill();
					}
					onNpcAI();
					serchLink(pc, getNpcTemplate().getFamily());
					if (damage > 0) {
						pc.setPetTarget(this);
					}
					int newHp = getCurrentHp() - damage;
					if (newHp <= 0 && !isDead()) {
						setCurrentHp(0);
						setDead(true);
						setActionStatus(ActionCodes.ACTION_Die);
						_lastattacker = attacker;
						Death death = new Death();
						GeneralThreadPool.getInstance().execute(death);
					}
					if (newHp > 0) {
						setCurrentHp(newHp);
					}
				} else if (!isDead()) {
					setDead(true);
					setActionStatus(ActionCodes.ACTION_Die);
					_lastattacker = attacker;
					Death death = new Death();
					GeneralThreadPool.getInstance().execute(death);
				}
			}
		}
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();
		}
	}

	@Override
	public void setCurrentMp(int i) {
		super.setCurrentMp(i);
		if (getMaxMp() > getCurrentMp()) {
			startMpRegeneration();
		}
	}

	private L1Character _lastattacker;

	class Death implements Runnable {
		L1Character lastAttacker = _lastattacker;

		@Override
		public void run() {
			setDeathProcessing(true);
			setCurrentHp(0);
			setDead(true);
			setActionStatus(ActionCodes.ACTION_Die);
			int targetobjid = getId();
			getMap().setPassable(getLocation(), true);
			broadcastPacket(new S_DoActionGFX(targetobjid, ActionCodes.ACTION_Die), true);

			L1PcInstance player = null;
			if (lastAttacker instanceof L1PcInstance) {
				player = (L1PcInstance) lastAttacker;
			} else if (lastAttacker instanceof L1PetInstance) {
				player = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
			} else if (lastAttacker instanceof L1SummonInstance) {
				player = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
			}
			if (player != null) {
				ArrayList<L1Character> targetList = _hateList.toTargetArrayList();
				ArrayList<Integer> hateList = _hateList.toHateArrayList();
				player.getExpHandler().calcExp(L1GuardianInstance.this, targetList, hateList, getExp());

				ArrayList<L1Character> dropTargetList = _dropHateList.toTargetArrayList();
				ArrayList<Integer> dropHateList = _dropHateList.toHateArrayList();
				try {
					DropTable.getInstance().drop(_npc, dropTargetList, dropHateList, player);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
				// 업은 급소를 찌른 플레이어로 설정.애완동물 or사몬으로 넘어뜨렸을 경우도 들어간다.
				player.addKarma((int) (getKarma() * Config.RATE.RATE_KARMA));
				player.sendPackets(new S_Karma(player), true);
			}
			setDeathProcessing(false);

			setKarma(0);
			setAlignment(0);
			setExp(0);
			allTargetClear();

			startDeleteTimer();
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
	}

	public void doFinalAction(L1PcInstance player) {
	}

	private static final long REST_MILLISEC = 10000;

	private AtomicInteger _restCallCount;
	
	private void talkChangeHead(L1PcInstance pc){
		int pcx		= pc.getX();
		int pcy		= pc.getY();
		int npcx	= getX();
		int npcy	= getY();
		if (pcx == npcx && pcy < npcy) {
			getMoveState().setHeading(0);
		} else if (pcx > npcx && pcy < npcy) {
			getMoveState().setHeading(1);
		} else if (pcx > npcx && pcy == npcy) {
			getMoveState().setHeading(2);
		} else if (pcx > npcx && pcy > npcy) {
			getMoveState().setHeading(3);
		} else if (pcx == npcx && pcy > npcy) {
			getMoveState().setHeading(4);
		} else if (pcx < npcx && pcy > npcy) {
			getMoveState().setHeading(5);
		} else if (pcx < npcx && pcy == npcy) {
			getMoveState().setHeading(6);
		} else if (pcx < npcx && pcy < npcy) {
			getMoveState().setHeading(7);
		}
		broadcastPacket(new S_ChangeHeading(this), true);
		if (_restCallCount.getAndIncrement() == 0) {
			setRest(true);
		}
		GeneralThreadPool.getInstance().schedule(new RestMonitor(), REST_MILLISEC);
	}

	public class RestMonitor implements Runnable {
		@Override
		public void run() {
			if (_restCallCount.decrementAndGet() == 0) {
				setRest(false);
			}
		}
	}
}


