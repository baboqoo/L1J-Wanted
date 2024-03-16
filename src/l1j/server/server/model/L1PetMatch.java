package l1j.server.server.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.RepeatTask;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.CommonUtil;

public class L1PetMatch {
	public static final int STATUS_NONE = 0;
	public static final int STATUS_READY1 = 1;
	public static final int STATUS_READY2 = 2;
	public static final int STATUS_PLAYING = 3;

	public static final int MAX_PET_MATCH = 1;

	private static final short[] PET_MATCH_MAPID = { 5125, 5131, 5132, 5133, 5134 };

	private String[] _pc1Name = new String[MAX_PET_MATCH];
	private String[] _pc2Name = new String[MAX_PET_MATCH];
	private L1PetInstance[] _pet1 = new L1PetInstance[MAX_PET_MATCH];
	private L1PetInstance[] _pet2 = new L1PetInstance[MAX_PET_MATCH];

	private static L1PetMatch _instance;

	public static L1PetMatch getInstance() {
		if (_instance == null)
			_instance = new L1PetMatch();
		return _instance;
	}

	public int setPetMatchPc(int petMatchNo, L1PcInstance pc,
			L1PetInstance pet) {
		int status = getPetMatchStatus(petMatchNo);
		if (status == STATUS_NONE) {
			_pc1Name[petMatchNo] = pc.getName();
			_pet1[petMatchNo] = pet;
			return STATUS_READY1;
		} else if (status == STATUS_READY1) {
			_pc2Name[petMatchNo] = pc.getName();
			_pet2[petMatchNo] = pet;
			return STATUS_PLAYING;
		} else if (status == STATUS_READY2) {
			_pc1Name[petMatchNo] = pc.getName();
			_pet1[petMatchNo] = pet;
			return STATUS_PLAYING;
		}
		return STATUS_NONE;
	}

	private synchronized int getPetMatchStatus(int petMatchNo) {
		L1PcInstance pc1 = null;
		if (_pc1Name[petMatchNo] != null)
			pc1 = L1World.getInstance().getPlayer(_pc1Name[petMatchNo]);
		L1PcInstance pc2 = null;
		if (_pc2Name[petMatchNo] != null)
			pc2 = L1World.getInstance().getPlayer(_pc2Name[petMatchNo]);

		if (pc1 == null && pc2 == null)
			return STATUS_NONE;
		if (pc1 == null && pc2 != null) {
			if (pc2.getMapId() == PET_MATCH_MAPID[petMatchNo])
				return STATUS_READY2;
			else {
				_pc2Name[petMatchNo] = null;
				_pet2[petMatchNo] = null;
				return STATUS_NONE;
			}
		}
		if (pc1 != null && pc2 == null) {
			if (pc1.getMapId() == PET_MATCH_MAPID[petMatchNo])
				return STATUS_READY1;
			else {
				_pc1Name[petMatchNo] = null;
				_pet1[petMatchNo] = null;
				return STATUS_NONE;
			}
		}

	    // PC가 시합장에 2명 있는 경우
		if (pc1.getMapId() == PET_MATCH_MAPID[petMatchNo]
				&& pc2.getMapId() == PET_MATCH_MAPID[petMatchNo])
			return STATUS_PLAYING;

		// PC가 시합장에 1명 있는 경우
		if (pc1.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
			_pc2Name[petMatchNo] = null;
			_pet2[petMatchNo] = null;
			return STATUS_READY1;
		}
		if (pc2.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
			_pc1Name[petMatchNo] = null;
			_pet1[petMatchNo] = null;
			return STATUS_READY2;
		}
		return STATUS_NONE;
	}

	private int decidePetMatchNo() {
		// 상대가 대기중의 시합을 찾는다
		for (int i = 0; i < MAX_PET_MATCH; i++) {
			int status = getPetMatchStatus(i);
			if (status == STATUS_READY1 || status == STATUS_READY2)
				return i;
		}
		// 대기중의 시합이 없으면 비어 있는 시합을 찾는다
		for (int i = 0; i < MAX_PET_MATCH; i++) {
			int status = getPetMatchStatus(i);
			if (status == STATUS_NONE)
				return i;
		}
		return -1;
	}

	public synchronized boolean enterPetMatch(L1PcInstance pc, int amuletId) {
		int petMatchNo = decidePetMatchNo();
		if (petMatchNo == -1)
			return false;

		L1PetInstance pet = withdrawPet(pc, amuletId);
		pc.getTeleport().start(32799, 32868, PET_MATCH_MAPID[petMatchNo], 0, true);
		L1SkillUse skillUse = new L1SkillUse();
		skillUse.handleCommands(pc, L1SkillId.CANCELLATION, pet.getId(), pet.getX(), pet.getY(), 0, L1SkillUseType.LOGIN);
		L1PetMatchReadyTimer timer = new L1PetMatchReadyTimer(petMatchNo, pc, pet);
		timer.begin();
		return true;
	}

	private L1PetInstance withdrawPet(L1PcInstance pc, int amuletId) {
		return null;
	}

	public void startPetMatch(final int petMatchNo) {
		final int a = 3204 + CommonUtil.random(6);
		final int b = 3204 + CommonUtil.random(6);
		_pet1[petMatchNo].broadcastPacket(new S_Effect(_pet1[petMatchNo].getId(), a), true);
		_pet2[petMatchNo].broadcastPacket(new S_Effect(_pet2[petMatchNo].getId(), b), true);
		
		GeneralThreadPool.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if(a > b)
					_pet1[petMatchNo].useHastePotion(500);
				else if(b > a)
					_pet2[petMatchNo].useHastePotion(500);
			}
		}, 4000 );

		GeneralThreadPool.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				L1PcInstance pc1 = L1World.getInstance().getPlayer(_pc1Name[petMatchNo]);
				L1PcInstance pc2 = L1World.getInstance().getPlayer(_pc2Name[petMatchNo]);
				
				_pet1[petMatchNo].setTarget(_pet2[petMatchNo]);
				_pet2[petMatchNo].setTarget(_pet1[petMatchNo]);
				
				pc1.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_TIME, 300), true);
				pc2.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_TIME, 300), true);
				
				L1PetMatchTimer timer = new L1PetMatchTimer(_pet1[petMatchNo], _pet2[petMatchNo], petMatchNo);
				timer.begin();
			}
		}, 8000 );
	}

	public void endPetMatch(final int petMatchNo, final int winNo) {
	
		GeneralThreadPool.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				L1PcInstance pc1 = L1World.getInstance().getPlayer(_pc1Name[petMatchNo]);
				L1PcInstance pc2 = L1World.getInstance().getPlayer(_pc2Name[petMatchNo]);
				if (winNo == 1) {
					giveMedal(pc1, petMatchNo, true);
					giveMedal(pc2, petMatchNo, false);
				} else if (winNo == 2) {
					giveMedal(pc1, petMatchNo, false);
					giveMedal(pc2, petMatchNo, true);
				} else if (winNo == 3) { // 무승부
					giveMedal(pc1, petMatchNo, false);
					giveMedal(pc2, petMatchNo, false);
				}
				pc1.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_END), true);
				pc2.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_END), true);
				qiutPetMatch(petMatchNo);
			}	
		}, 4000);
	}

	
	private void giveMedal(L1PcInstance pc, int petMatchNo, boolean isWin) {
		if (pc == null)
			return;
		if (pc.getMapId() != PET_MATCH_MAPID[petMatchNo])
			return;
		if (isWin) {
			pc.sendPackets(new S_ServerMessage(1166, pc.getName()), true);  // %0%s펫 매치로 승리를 거두었습니다.
			L1ItemInstance item = ItemTable.getInstance().createItem(41309);
			int count = 3;
			if (item != null) {
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					item.setCount(count);
					pc.getInventory().storeItem(item);
					pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
				}
			}

			if(CommonUtil.nextInt() < 33){
				pc.getInventory().storeItem(3000025, 1);

				item = ItemTable.getInstance().createItem(3000025); 
				item.setCount(1);
				pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true); 
			}
		} else {
			L1ItemInstance item = ItemTable.getInstance().createItem(41309);
			int count = 1;
			if (item != null) {
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					item.setCount(count);
					pc.getInventory().storeItem(item);
					pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
				}
			}
		}
	}

	private void qiutPetMatch(int petMatchNo) {
		L1PcInstance pc1 = L1World.getInstance().getPlayer(_pc1Name[petMatchNo]);
		if (pc1 != null && pc1.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
			L1PetInstance pet = null;
			for (Object object : pc1.getPetList().values().toArray()) {
				if (object instanceof L1PetInstance) {
					pet = (L1PetInstance) object;
			//		pet.dropItem();
					pc1.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
			}
			pc1.getTeleport().start(32630, 32744, (short) 4, 4, true);
		}
		_pc1Name[petMatchNo] = null;
		_pet1[petMatchNo] = null;

		L1PcInstance pc2 = L1World.getInstance()
				.getPlayer(_pc2Name[petMatchNo]);
		if (pc2 != null && pc2.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
			L1PetInstance pet = null;
			for (Object object : pc2.getPetList().values().toArray()) {
				if (object instanceof L1PetInstance) {
					pet = (L1PetInstance) object;
			//		pet.dropItem();
					pc2.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
			}
			pc2.getTeleport().start(32630, 32744, (short) 4, 4, true);
		}
		_pc2Name[petMatchNo] = null;
		_pet2[petMatchNo] = null;
	}



public class L1PetMatchReadyTimer extends RepeatTask {
	private Logger _log = Logger.getLogger(L1PetMatchReadyTimer.class.getName());

	private final int _petMatchNo;
	private final L1PcInstance _pc;
	private final L1PetInstance _pet;

	public L1PetMatchReadyTimer(int petMatchNo, L1PcInstance pc, L1PetInstance pet) {
		super(1000);
		_petMatchNo = petMatchNo;
		_pc = pc;
		_pet = pet;
	}

	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, 3000);
	}

	@Override
	public void execute() {
		try {
			if (_pc == null || _pet == null) {
				cancel();
				return;
			}

			if (_pc.getTeleport().isTeleport())
				return;
			if (L1PetMatch.getInstance().setPetMatchPc(_petMatchNo, _pc, _pet) == L1PetMatch.STATUS_PLAYING)
				L1PetMatch.getInstance().startPetMatch(_petMatchNo);

			cancel();
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

}



public class L1PetMatchTimer extends RepeatTask {
	private Logger _log = Logger.getLogger(L1PetMatchTimer.class.getName());

	private final L1PetInstance _pet1;
	private final L1PetInstance _pet2;
	private final int _petMatchNo;
	private int _counter = 0;

	public L1PetMatchTimer(L1PetInstance pet1, L1PetInstance pet2, int petMatchNo) {
		super(3000);
		_pet1 = pet1;
		_pet2 = pet2;
		_petMatchNo = petMatchNo;
	}

	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, 0);
	}

	@Override
	public void execute() {
		try {
			_counter++;
			if (_pet1 == null || _pet2 == null) {
				cancel();
				return;
			}

			if (_pet1.isDead() || _pet2.isDead()) {
				int winner = 0;
				if (!_pet1.isDead() && _pet2.isDead()) {
					winner = 1;
					_pet1.broadcastPacket(new S_Effect(_pet1.getId(), 6354), true);
				} else if (_pet1.isDead() && !_pet2.isDead()) {
					winner = 2;
					_pet2.broadcastPacket(new S_Effect(_pet2.getId(), 6354), true);
				} else {
					winner = 3;
				}
				L1PetMatch.getInstance().endPetMatch(_petMatchNo, winner);
				cancel();
				return;
			}

			if (_counter == 100) { // 5분 지나도 끝나지 않는 경우는 무승부
				L1PetMatch.getInstance().endPetMatch(_petMatchNo, 3);
				cancel();
				return;
			}
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

}

}

