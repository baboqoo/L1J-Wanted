package l1j.server.IndunSystem.fantasyisland.action;

import l1j.server.IndunSystem.fantasyisland.FantasyIslandUtil;
import l1j.server.IndunSystem.fantasyisland.FantasylslandHandler;
import l1j.server.IndunSystem.fantasyisland.FantasylslandType;
import l1j.server.common.data.ChatType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.utils.CommonUtil;

/**
 * 유니콘 사원(부스트)
 * @author LinOffice
 */
public class Boost extends FantasylslandHandler {
	public Boost(L1PcInstance pc, short mapId, FantasylslandType raidType){
		super(pc, mapId, raidType);
	}
	
	@Override
	protected void createSpwan() throws Exception{
		if (_pc != null) {
			_pc.sendPackets(FantasyIslandUtil.UNICORN_FIRST_GREEN_MSG[0]);//.........저...도....세요.........
		}
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		if (_pc != null) {
			_pc.sendPackets(FantasyIslandUtil.UNICORN_FIRST_GREEN_MSG[1]);//.......용사..님 저를.... 도..와주세요....
		}
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		if (_pc != null) {
			_pc.sendPackets(FantasyIslandUtil.UNICORN_FIRST_GREEN_MSG[2]);//...제가.. 보이시나요?? 제발 도와주세요!!!!!
		}
		Thread.sleep(1000);
		if (!isValidataion()) {
			return;
		}
		BasicNpcList = _util.spawn(_map, 50, true);// 유니콘 스폰
		setting();
		NpcList = _util.spawn(_map, 51, true);// 봉인술사, 경비병 스폰
		Thread.sleep(1000);
		if (!isValidataion()) {
			return;
		}
		for (L1NpcInstance npc : NpcList) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 7200004) {
				npc.broadcastPacket(new S_NpcChatPacket(npc, "$17719", ChatType.CHAT_NORMAL), true);// 타락한 경비병 누구냐 어떻게 왓지?
			}
		}
	}

	@Override
	protected void waitStep() throws Exception {
		if (NpcList.isEmpty()) {
			stage = FIRST_STEP;
			Thread.sleep(2000);
			if (!isValidataion()) {
				return;
			}
			unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17691", ChatType.CHAT_NORMAL), true);// 도와주러 와 주셔서 감사합니다.
			Thread.sleep(1000);
			if (!isValidataion()) {
				return;
			}
			unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17692", ChatType.CHAT_NORMAL), true);// 이계의 존재가 곧 돌아올겁니다.
			Thread.sleep(1000);
			if (!isValidataion()) {
				return;
			}
			unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17693", ChatType.CHAT_NORMAL), true);// 그전에 제가 봉인을 풀 수 있도록 시간을 벌어주세요.
			Thread.sleep(1000);
			if (!isValidataion()) {
				return;
			}
			unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17947", ChatType.CHAT_NORMAL), true);
			if (_pc != null) {
				_pc.sendPackets(FantasyIslandUtil.ROUND_READY_GREEN_MSG[2]);// 마법 막대를 이용해 적을 처치해주세요.
				if (_pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(810011), 1) != L1Inventory.OK) return;
				_pc.getInventory().storeItem(810011, 1);
				_pc.sendPackets(FantasyIslandUtil.WAND_GET_MSG);// 강력한 공격마법 지원
			}
			Thread.sleep(3000);
			if (!isValidataion()) {
				return;
			}
			if (_pc != null) {
				_pc.sendPackets(FantasyIslandUtil.ROUND_READY_GREEN_MSG[0]);// 적들이 몰려오고 있습니다.
				_pc.sendPackets(S_Sound.FANTASY_START_SOUND);
				_pc.sendPackets(S_PacketBox.WAKE);// 진동
				_pc.sendPackets(FantasyIslandUtil.ROUND_GFX[0]);
			}
		}
	}

	@Override
	protected void firstStep() throws Exception {
		stage = SECOND_STEP;
		_util.spawnDelay(_map, 52, 700);
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		_util.spawnDelay(_map, 53, 700);
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		_util.spawnDelay(_map, 54, 700);
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		_util.spawnDelay(_map, 55, 700);
	}

	@Override
	protected void secondStep() throws Exception {
		stage = THIRD_STEP;
		Thread.sleep(4000);
		if (!isValidataion()) {
			return;
		}
		if (_pc != null) {
			_pc.sendPackets(FantasyIslandUtil.ROUND_GFX[1]);
			_pc.sendPackets(FantasyIslandUtil.ROUND_READY_GREEN_MSG[1]);// 적들이 더 몰려옵니다. 준비해 주세요
			if (_pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(810006), 1) != L1Inventory.OK) return;
			_pc.getInventory().storeItem(810006, 1);
			_pc.sendPackets(FantasyIslandUtil.WAND_GET_MSG);// 강력한 공격마법 지원
		}
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		_util.spawnDelay(_map, 56, 500);				
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		_util.spawnDelay(_map, 57, 500);
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
	}

	@Override
	protected void thirdStep() throws Exception {
		stage = LAST_STEP;
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		if (_pc != null) {
			_pc.sendPackets(FantasyIslandUtil.ROUND_GFX[2]);
			_pc.sendPackets(FantasyIslandUtil.ROUND_READY_GREEN_MSG[1]);// 적들이 더 몰려옵니다. 준비해 주세요
			if (_pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(810011), 1) != L1Inventory.OK) return;
			_pc.getInventory().storeItem(810011, 1);
			_pc.sendPackets(FantasyIslandUtil.WAND_GET_MSG);// 강력한 공격마법 지원
		}
		unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17706", ChatType.CHAT_NORMAL), true);// 얼마남지 않았습니다
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		_util.spawnDelay(_map, 58, 300);
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		_util.spawnDelay(_map, 59, 300);
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		_util.spawnDelay(_map, 60, 300);
		Thread.sleep(10000);
		if (!isValidataion()) {
			return;
		}
		if (_pc != null) {
			_pc.sendPackets(S_SceneNoti.CT_BOSS_ENABLE);
		}
		int chance = CommonUtil.random(10) + 1;
		if (chance >= 0 && chance <= 5) {
			if (_pc != null) {
				_pc.sendPackets(FantasyIslandUtil.BOSS_SPAWN_GRREEN_MSG[0]);// 유니콘을 빼앗아가려고? 그렇게 놔둘 순 없지!!
				_pc.sendPackets(S_SceneNoti.CT_NIGHTMARE_ENABLE);
			}
			Thread.sleep(2000);
			if (!isValidataion()) {
				return;
			}
			boss = _util.bossSpawn(_map, 100);// 구미호
			if (_pc != null) {
				_pc.sendPackets(S_SceneNoti.CT_NIGHTMARE_DISABLE);
			}
		}else if(chance > 5 && chance <= 8) {
			if (_pc != null) {
				_pc.sendPackets(FantasyIslandUtil.BOSS_SPAWN_GRREEN_MSG[1]);// 유니콘을 빼앗아가려고? 그렇게 놔둘 순 없지!!
				_pc.sendPackets(S_SceneNoti.CT_LASTBOSS2_ENABLE);
			}
			Thread.sleep(2000);
			if (!isValidataion()) {
				return;
			}
			boss = _util.bossSpawn(_map, 101);// 아비쉬
			if (_pc != null) {
				_pc.sendPackets(S_SceneNoti.CT_LASTBOSS2_DISABLE);
			}
		}else{
			if (_pc != null) {
				_pc.sendPackets(FantasyIslandUtil.BOSS_SPAWN_GRREEN_MSG[2]);// 유니콘을 빼앗아가려고? 그렇게 놔둘 순 없지!!
				_pc.sendPackets(S_SceneNoti.CT_LASTBOSS3_ENABLE);
			}
			Thread.sleep(2000);
			if (!isValidataion()) {
				return;
			}
			boss = _util.bossSpawn(_map, 102);// 아즈모단
			if (_pc != null) {
				_pc.sendPackets(S_SceneNoti.CT_LASTBOSS3_DISABLE);
			}
		}
	}

	@Override
	protected void lastStep() throws Exception {
		if (boss.isDead() || boss == null) {
			stage = END;
			if (_pc != null) {
				_pc.sendPackets(S_SceneNoti.CT_BOSS_DISABLE);
				_pc.sendPackets(S_SceneNoti.CT_FOG_ENABLE);
			}
			Thread.sleep(2000);
			if (!isValidataion()) {
				return;
			}
			unicorn.broadcastPacket(new S_Effect(unicorn.getId(), 1911), true);
			Thread.sleep(1000);
			if (!isValidataion()) {
				return;
			}
			unicorn.broadcastPacket(new S_Polymorph(unicorn.getId(), 12493, 0), true);
			if (_pc != null) {
				_pc.sendPackets(FantasyIslandUtil.RAID_COMPLETE_GREEN_MSG[0]);// 감사합니다!
			}
			unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17708", ChatType.CHAT_NORMAL), true);
			Thread.sleep(2000);
			if (!isValidataion()) {
				return;
			}
			if (_pc != null) {
				_pc.sendPackets(FantasyIslandUtil.RAID_COMPLETE_GREEN_MSG[1]);// 당분간 그것은 돌아올 수 없을 것입니다.
			}
			unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17709", ChatType.CHAT_NORMAL), true);
			Thread.sleep(2000);
			if (!isValidataion()) {
				return;
			}
			if (_pc != null) {
				_pc.sendPackets(FantasyIslandUtil.RAID_COMPLETE_GREEN_MSG[2]);// 어서 몽환의 섬으로 돌아가 봐야겠군요.
			}
			unicorn.broadcastPacket(new S_NpcChatPacket(unicorn, "$17710", ChatType.CHAT_NORMAL), true);
			Thread.sleep(2000);
			if (!isValidataion()) {
				return;
			}
			unicorn.broadcastPacket(new S_Effect(unicorn.getId(), 169), true);
			unicorn.deleteMe();
		}
	}

	@Override
	protected void end() throws Exception {
		Thread.sleep(2000);
		if (!isValidataion()) {
			return;
		}
		if (_pc != null && _pc.getMapId() == _map) {
			_pc.sendPackets(L1ServerMessage.sm1259);// 잠시 후 마을로 이동됩니다.
		}
		Thread.sleep(5000);
		if (!isValidataion()) {
			return;
		}
		if (_pc != null && _pc.getMapId() == _map) {
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
			_pc.getTeleport().start(loc[0], loc[1], (short) loc[2], _pc.getMoveState().getHeading(), true);
		    _pc.getInventory().consumeItem(810011);
		    _pc.getInventory().consumeItem(810007);
		}
		_pc = null;
	}

	@Override
	public void raidStart() {
		running = true;
		GeneralThreadPool.getInstance().schedule(this, 2000);
		System.out.println("■■■■■■■■■■ Unicorn Temple (BOOST) Start ■■■■■■■■■■ MAP - " + _map);
	}

	@Override
	protected void raidClose() {
		stage = CLOSE;
		System.out.println("■■■■■■■■■■ Unicorn Temple (BOOST) Ends ■■■■■■■■■■ MAP - " + _map);
		dispose();
	}

}

