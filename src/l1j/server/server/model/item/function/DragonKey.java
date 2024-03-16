package l1j.server.server.model.item.function;

import l1j.server.IndunSystem.dragonraid.DragonRaidCreator;
import l1j.server.IndunSystem.dragonraid.DragonRaildType;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.L1SpawnUtil;

public class DragonKey extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public DragonKey(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getRegion() != L1RegionStatus.SAFETY || L1CastleLocation.getCastleIdByArea(pc) != 0 
					|| (pc.getMapId() >= 1005 && pc.getMapId() <= 1022) || (pc.getMapId() >= 1161 && pc.getMapId() <= 1166)
					|| (pc.getMapId() > 6000 && pc.getMapId() < 6999) || pc.getNetConnection().isInterServer()) {
				pc.sendPackets(L1ServerMessage.sm1892);// 사용불가 지역
				return;
			}
			switch(this.getItemId()){
			case 490012:dragonPortal(pc, DragonRaildType.ANTARAS);break;
			case 490013:dragonPortal(pc, DragonRaildType.FAFURION);break;
			case 490014:dragonPortal(pc, DragonRaildType.RINDVIOR);break;
			case 490015:dragonPortal(pc, DragonRaildType.VALAKAS);break;
			case 490016:dragonPortal(pc, DragonRaildType.HALPAS);break;
			case 6022:dragonArea(pc);break;
			}
		}
	}
	
	private void dragonPortal(L1PcInstance pc, DragonRaildType raidType){
		DragonRaidCreator manager = DragonRaidCreator.getInstance();
		if (raidType != DragonRaildType.HALPAS && manager.getRaidCount(raidType) >= 1) {
			pc.sendPackets(L1SystemMessage.DRAGON_RAID_MAX);
			return;
		}
		if (manager.getRaidCount(raidType) >= 6) {
			pc.sendPackets(L1SystemMessage.DRAGON_RAID_MAX);
			return;
		}
		if (manager.create(pc, raidType)) {
			L1World.getInstance().broadcastPacketToAll(L1ServerMessage.sm2921);// 어딘가에 드래곤포탈이 열렷습니다
			pc.getInventory().removeItem(this, 1);
		}
	}
	
	private void dragonArea(L1PcInstance pc){
		L1SpawnUtil.spawn2(pc.getX(), pc.getY(), pc.getMapId(), 5, 20950, 0, 180 * 1000, 140);// 드래곤 서식지 입구
		L1World.getInstance().broadcastPacketToAll(L1ServerMessage.sm2921);// 어딘가에 드래곤포탈이 열렷습니다
		pc.getInventory().removeItem(this, 1);
	}
	
}

