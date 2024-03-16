package l1j.server.server.clientpackets;

import l1j.server.GameSystem.inn.InnHelper;
import l1j.server.GameSystem.inter.L1InterServerFactory;
import l1j.server.LFCSystem.InstanceEnums.InstStatus;
import l1j.server.LFCSystem.InstanceSpace;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DeadRestart;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_Weather;
import l1j.server.server.serverpackets.S_WorldPut;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;

public class C_RestartAfterDie extends ClientBasePacket {

	private static final String C_RESTART = "[C] C_Restart";

	public C_RestartAfterDie(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		
		if (pc == null) {
			return;
		}
		/* LFC instance space 안에서는 리스불가 */
		if (InstanceSpace.isInInstance(pc) && pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE) {
			return;
		}
		
		if (pc.getConfig().getDuelLine() != 0) {// 배틀존 종료
			pc.getConfig().setDuelLine(0);
		}
		if (!pc.isDead()) {
			return;
		}
		if (pc.isGhost()) {// 관람모드 종료
			pc.endGhost();
		}
		int[] loc = Getback.GetBack_Restart(pc);
		int[] innOutLoc = InnHelper.getOutLoc(pc.getMapId());
		if (pc.getHellTime() > 0) {
			loc[0] = 32701;
			loc[1] = 32777;
			loc[2] = 666;
		} else if (innOutLoc != null) {
			loc = innOutLoc;
	    } else if ((loc[2] >= L1TownLocation.MAP_ERJABE_CROWN && loc[2] <= L1TownLocation.MAP_ERJABE_FENCER || loc[2] == L1TownLocation.MAP_ERJABE_LANCER) 
	    		&& !pc.getInventory().checkItemOne(L1ItemId.DEATH_PENALTY_SHIELD_ITEMS)) {
			loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_WINDAWOOD);
		}
		
		pc.sendPackets(S_DeadRestart.SUCCESS);
		
		if (!pc._isArmorSetPoly) {
			int classId = pc.getClassId();
			pc.setSpriteId(classId);
			if (pc.isRankingPoly(pc.getSpriteId())) {
				pc.sendPackets(new S_Polymorph(pc.getId(), classId, pc.getCurrentWeapon()), true);
			}
			pc.removeShapeChange();
		}
		
		pc.removeAllKnownObjects();
		pc.broadcastPacket(new S_RemoveObject(pc), true);
		
		// 케릭터 터가 인던에서 죽은후 리스를 했을경우
		if (pc.getNetConnection().isInterServer() && pc.getNetConnection().getInter() == L1InterServer.INSTANCE_DUNGEON) {
			if (pc.isInParty()) {
				pc.getParty().leaveMember(pc);
			}
			pc.setLocation(33464, 32757, 4);
			L1InterServerFactory.regist(pc, 33464, 32757, (short)4, 5, L1InterServer.LEAVE);
			return;
		}
		
		pc.setCurrentHp(pc.getLevel());
		pc.setFood(GameServerSetting.MIN_FOOD_VALUE);
		pc.setDead(false);
		pc.setActionStatus(0);
		L1World world = L1World.getInstance();
		world.moveVisibleObject(pc, loc[2], loc[0], loc[1]);
		pc.setX(loc[0]);
		pc.setY(loc[1]);
		pc.setMap((short) loc[2]);
		pc.sendPackets(S_WorldPut.get(pc.getMap()));
		pc.broadcastPacketWithMe(new S_PCObject(pc), true);
		pc.sendPackets(new S_CharVisualUpdate(pc), true);
		pc.sendPackets(new S_Weather(world.getWeather()), true);
		pc.saveInventory();
		
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (L1ItemId.isInstanceDungeonItem(item.getItemId())) {
				pc.getInventory().removeItem(item);
			}
		}
	}

	@Override
	public String getType() {
		return C_RESTART;
	}
}
