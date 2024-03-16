package l1j.server.server.model.Instance;

import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.datatables.MagicDollInfoTable.L1DollInfo;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.sprite.AcceleratorChecker;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.action.S_AttackPacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.action.S_MoveCharPacket;
import l1j.server.server.serverpackets.action.S_SocialAction;
import l1j.server.server.serverpackets.action.S_SocialAction.SOCIAL_ACTION_TYPE;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;

public class L1FakePcInstance extends L1PcInstance {
	public static final int DEFAULT_INTERVAL = 1000; 

	enum ACTION_TYPE {
		ACTION_IDLE,
		ACTION_WALK,
		ACTION_TELEPORT,
		ACTION_CHAT,
		ACTION_USE_ITEM,
		ACTION_ATTACK,
		ACTION_THINK,
		ACTION_EMOTION
	}

	//private static final String[] _chatList1 = { "님", "ㅋㅋㅋ", "ㅋ", "하이요", "?", "ㅇㅇ", "ㅎㅇ", "ㅇㄷ?", "ㄱㄱ", "ㅇㅋ", "잠시", "기달", "ㄱㄷ", "ㅈㅅ", "ㄴㄴ" }; 
	private static final String[] _chatList1 = { "sir", "hehehe", "haha", "yo", "?", "yessss", "hi", "ok?", "go ahead", "ok", "wait", "hold on", "come on", "good job", "no no" }; 

	private static final ACTION_TYPE[] _actionList = { 
		ACTION_TYPE.ACTION_IDLE, ACTION_TYPE.ACTION_WALK, ACTION_TYPE.ACTION_TELEPORT, ACTION_TYPE.ACTION_CHAT, 
		ACTION_TYPE.ACTION_USE_ITEM, ACTION_TYPE.ACTION_ATTACK, ACTION_TYPE.ACTION_THINK, ACTION_TYPE.ACTION_EMOTION };
	
	private static final int[] _actionRatio = {45, 35, 5, 0, 10, 2, 2, 1}; // 액션 확률 설정

	private ACTION_TYPE _actionType;
	private int	_actionCount;

	private int _spawnX;
	private int _spawnY;
	private int _spawnHeading;
	private int _spawnMapId;
	private int _actionInterval;
//	private int _lastDollTime;
	private boolean _isPolymorph = false;

	public void setPolymorph() {
		_isPolymorph = true;
	}

	private static final long serialVersionUID = 1L;

	public L1FakePcInstance() {
		super();
		_actionType = ACTION_TYPE.ACTION_IDLE;
		_actionCount = 0;
		_actionInterval = DEFAULT_INTERVAL;
		setFake();
	}

	public L1FakePcInstance(int x, int y, int heading, int mapId) {
		super();
		_actionType = ACTION_TYPE.ACTION_IDLE;
		_actionCount = 0;
		_actionInterval = DEFAULT_INTERVAL;
		setFake();

		_spawnX = x;
		_spawnY = y;
		_spawnHeading = heading;
		_spawnMapId = mapId;

	}

	public void SetSpawnInfo(int x, int y, int heading, int mapId) {
		_spawnX = x;
		_spawnY = y;
		_spawnHeading = heading;
		_spawnMapId = mapId;
	}
	
	class ActionTimer implements Runnable {
		boolean _active;

		public ActionTimer() {
			_active = true;
		}

		@Override
		public void run() {
			if (!_active)
				return;
			L1FakePcInstance.this.DoAction();
			if (_active)
				GeneralThreadPool.getInstance().schedule(this, _actionInterval);
		}

		public void cancel() {
			_active = false;
		}
	}
	
	private void OnIdle() {
		/*if ((int) (System.currentTimeMillis() / 1000) - _lastDollTime > 2000) {
			 UseDoll();
		}*/

		if (++_actionCount > 20 + CommonUtil.random(10)) {
			changeAction();
			_actionCount = 0;
		}
	}
	
	private void OnWalk() {
		int heading = getMoveState().getHeading();
		int ori_Heading = heading;

		int headingFindCount = 0;
		boolean clockwise = CommonUtil.nextBoolean();

		_actionInterval = this.getAcceleratorChecker().getRightInterval(AcceleratorChecker.ACT_TYPE.MOVE);

		while (true) {
			if (getMap().isPassable(getX(), getY(), heading) == true) {
				int locx = getX();
				int locy = getY();

				switch (heading) {
				case 1:
					locx++;
					locy--;
					break;
				case 2:
					locx++;
					break;
				case 3:
					locx++;
					locy++;
					break;
				case 4:
					locy++;
					break;
				case 5:
					locx--;
					locy++;
					break;
				case 6:
					locx--;
					break;
				case 7:
					locx--;
					locy--;
					break;
				case 0:
					locy--;
					break;
				}
				
				if (getMap().isSafetyZone(locx, locy)) {
					getMap().setPassable(getLocation(), true);
					getLocation().set(locx, locy);
					getMap().setPassable(getLocation(), false);

					getMoveState().setHeading(heading);
					L1World.getInstance().onMoveObject(this);
					broadcastPacket(new S_MoveCharPacket(this), true);

					if (ori_Heading == heading && CommonUtil.random(3) == 0)
						getMoveState().setHeading(CommonUtil.random(8));

					break;
				}
			}

			if (++headingFindCount >= 8)
				break;

			if (clockwise) {
				++heading;
				if (heading > 7)
					heading = 0;
			} else {
				--heading;
				if (heading < 0)
					heading = 7;
			}
		}

		if (++_actionCount > 5 + CommonUtil.random(10)) {
			changeAction();
			_actionCount = 0;
		}
	}
	
	private void OnUseItem() {
		if (_actionCount == 0) {
			if (!getSkill().hasSkillEffect(STATUS_HASTE) && getMoveState().getMoveSpeed() == 0){
				broadcastPacket(new S_Effect(getId(), 191), true);
				broadcastPacket(new S_SkillHaste(getId(), 1, 0), true);
				getMoveState().setMoveSpeed(1);
				getSkill().setSkillEffect(STATUS_HASTE, 300 * 1000);
			}
		}

		if (++_actionCount > 5 + CommonUtil.random(5)) {
			changeAction();
			_actionCount = 0;
		}
	}

	private void OnTeleport() {
		if (_actionCount == 0)
			this.getTeleport().start(_spawnX, _spawnY, (short) _spawnMapId, _spawnHeading, true);

		if (++_actionCount > 20 + CommonUtil.random(5)) {
			changeAction();
			_actionCount = 0;
		}
	}

	private void OnChat() {
		if (_actionCount == 2) {
			String chatText = _chatList1[CommonUtil.random(_chatList1.length)];
			S_SystemMessage s_chatpacket = new S_SystemMessage(chatText);
			for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(this)) {
				L1ExcludingList spamList3 = SpamTable.getInstance().getExcludeTable(listner.getId());
				if (!spamList3.contains(0, this.getName()))
					listner.sendPackets(s_chatpacket, true);
			}
			//System.out.println("채팅왜안해");
			System.out.println("Why aren't you chatting?");
		}

		if (++_actionCount > 20 + CommonUtil.random(5)) {
			changeAction();
			_actionCount = 0;
		}
	}
	
	private void OnAttack() {
		if (_actionCount == 0)
			Broadcaster.broadcastPacket(this, new S_AttackPacket(this, 0, ActionCodes.ACTION_Attack, true), true);

		if (++_actionCount > 20 + CommonUtil.random(5)) {
			changeAction();
			_actionCount = 0;
		}
	}
	
	private void OnThink() {
		if (_actionCount == 0) {
			int chace = CommonUtil.random(100) + 1;
			if (chace >= 0 && chace < 25)
				broadcastPacket(new S_DoActionGFX(this.getId(), ActionCodes.ACTION_Think), true);
			else if (chace >= 25 && chace < 50)
				broadcastPacket(new S_DoActionGFX(this.getId(), ActionCodes.ACTION_Aggress), true);
			else if (chace >= 50 && chace < 75)
				broadcastPacket(new S_DoActionGFX(this.getId(), ActionCodes.ACTION_Salute), true);
			else
				broadcastPacket(new S_DoActionGFX(this.getId(), ActionCodes.ACTION_Cheer), true);
		}

		if (++_actionCount > 20 + CommonUtil.random(5)) {
			changeAction();
			_actionCount = 0;
		}
	}
	
	private void OnEmotion() {
		if (_actionCount == 0)
			broadcastPacket(new S_SocialAction(SOCIAL_ACTION_TYPE.SOCIAL_ACTION_TYPE_EMOTICON, CommonUtil.random(11) + 1, getId()), true);
		if (++_actionCount > 20 + CommonUtil.random(5)) {
			changeAction();
			_actionCount = 0;
		}
	}
	
	private void changeAction() {
		do {
			int ratio = CommonUtil.random(100);
			int totalRatio = 0;

			for (int i = 0; i < _actionRatio.length; ++i) {
				totalRatio += _actionRatio[i];

				if (totalRatio > ratio) {
					_actionType = _actionList[i];
					break;
				}
			}
		} while (_actionType == ACTION_TYPE.ACTION_TELEPORT && getX() == _spawnX && getY() == _spawnY && getMapId() == _spawnMapId);
		_actionInterval = DEFAULT_INTERVAL;
	}

	private void DoAction() {
		switch (_actionType) {
		case ACTION_IDLE:
			OnIdle();
			break;

		case ACTION_WALK:
			OnWalk();
			break;

		case ACTION_TELEPORT:
			OnTeleport();
			break;

		case ACTION_CHAT:
			OnChat();
			break;

		case ACTION_USE_ITEM:
			OnUseItem();
			break;
			
		case ACTION_ATTACK:
			OnAttack();
			break;
			
		case ACTION_THINK:
			OnThink();
			break;
			
		case ACTION_EMOTION:
			OnEmotion();
			break;
		}
	}

	ActionTimer _actionTimer = null;

	public void startAction() {
		if (_actionTimer == null) {
			UseDoll();
			if (_isPolymorph)
				Polymorph();

			_actionTimer = new ActionTimer();
			GeneralThreadPool.getInstance().execute(_actionTimer);
		}
	}
	
	private void stopAction() {
		if (_actionTimer != null) {
			_actionTimer.cancel();
			_actionTimer = null;
		}
	}
	
	void Polymorph() {
		int probability = CommonUtil.random(400);
		if (probability <= 15)
			L1PolyMorph.doPoly(this, 11329, 0, L1PolyMorph.MORPH_BY_LOGIN); //해골
		else if (probability <= 30)
			L1PolyMorph.doPoly(this, 11328, 0, L1PolyMorph.MORPH_BY_LOGIN); //오크
		else if (probability <= 45)
			L1PolyMorph.doPoly(this, 11367, 0, L1PolyMorph.MORPH_BY_LOGIN); //레서데몬
		else if (probability <= 55)
			L1PolyMorph.doPoly(this, 11339, 0, L1PolyMorph.MORPH_BY_LOGIN); //오크전사
		else if (probability <= 65)
			L1PolyMorph.doPoly(this, 11380, 0, L1PolyMorph.MORPH_BY_LOGIN); //다크나이트
		else if (probability <= 75)
			L1PolyMorph.doPoly(this, 11392, 0, L1PolyMorph.MORPH_BY_LOGIN); //아크나이트
		else if (probability <= 85)
			L1PolyMorph.doPoly(this, 11340, 0, L1PolyMorph.MORPH_BY_LOGIN); //늑인
		else if (probability <= 100)
			L1PolyMorph.doPoly(this, 11342, 0, L1PolyMorph.MORPH_BY_LOGIN); //오크궁수
	}

	void UseDoll() {
		if (getDoll() != null)
			return;
		L1ItemInstance item = getInventory().findItemId(CommonUtil.nextBoolean() ? 210070 : 41250);
		if(item == null)return;
		L1DollInfo info = MagicDollInfoTable.getDollInfo(item.getItemId());
		if(info == null)return;
		L1Npc template = NpcTable.getInstance().getTemplate(info.getDollNpcId());
		L1DollInstance doll = new L1DollInstance(template, this, info, item);
		broadcastPacket(new S_Effect(doll.getId(), 5935), true);

//		_lastDollTime = (int) (System.currentTimeMillis() / 1000);
	}

	@Override
	public void logout() {
		stopAction();
		super.logout();
	}

	public static L1FakePcInstance load(String charName) {
		L1FakePcInstance result = null;
		try {
			result = CharacterTable.getInstance().loadFakeCharacter(charName);
		} catch (Exception e) {
			//System.out.println("로봇에러 : " + e);
			System.out.println("Robot error: " + e);
		}
		return result;
	}
}

