package l1j.server.server.model.item.function;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1Fishing;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_FishingNoti;
import l1j.server.server.serverpackets.S_FishingNoti.FISHING_ROD;
import l1j.server.server.serverpackets.action.S_Fishing;
import l1j.server.server.templates.L1Item;
import l1j.server.server.types.Point;

public class FishingRod extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public FishingRod(L1Item item) {
		super(item);
	}
	
	private int fishX, fishY;

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) cha;
		fishX		= packet.readH();
		fishY		= packet.readH();
		if (Config.ETC.FISH_LOC_TOWN) {
			town(pc);
		} else {
			fishingZone(pc);
		}
	}
	
	L1Fishing validate(L1PcInstance pc){
		if (pc.getInventory().getWeightPercent() > 82) { // 중량 오버
			pc.sendPackets(L1ServerMessage.sm1518); // 낚시중단 무게 게이지 부족
			return null;
		}
		if (pc.getInventory().getSize() >= L1PcInventory.MAX_SIZE) {
			pc.sendPackets(L1ServerMessage.sm263);
			return null;
		}
		L1Fishing fish		= L1Fishing.fromRod(this.getItemId());
		if (fish.getMaxChargeCount() > 0 && this.getChargeCount() <= 0) {
			return null;
		}
		return fish;
	}

	/** 마을 **/
	void town(L1PcInstance pc) {
		if (pc.getMapId() != 4 || pc.getLocation().getTileDistance(new Point(fishX, fishY)) > 13) {
			pc.sendPackets(L1ServerMessage.sm1138);// 여기에 낚싯대를 던질 수 없습니다.
			return;
		}
		L1Fishing fish		= validate(pc);
		if (fish == null) {
			return;
		}
		/** 좌표 기준점 **/
		int x = 33413, y = 32809;
		if ((fishX >= x - 5 && fishX <= x + 5) && (fishY >= y - 8 && fishY <= y + 8)) {
			action(pc, fish);
		} else {
			pc.sendPackets(L1ServerMessage.sm1138);// 여기에 낚싯대를 던질 수 없습니다.
		}
	}
	
	static final int[] GAB_X = { 0, 5, 5, 5, 0, -5, -5, -5};
	static final int[] GAB_Y = { -5, -5, -5, 5, 5, 5, 0, -5};

	/** 낚시터 **/
	void fishingZone(L1PcInstance pc) {
		if (pc.getMapId() != 5490 || fishX <= 32704 || fishX >= 32831 || fishY <= 32768 || fishY >= 32895) {
			pc.sendPackets(L1ServerMessage.sm1138);// 여기에 낚싯대를 던질 수 없습니다.
			return;
		}
		L1Fishing fish		= validate(pc);
		if (fish == null) {
			return;
		}
		int heading = pc.getMoveState().getHeading(); // ● 방향: (0.좌상)(1.상)(2.우상)(3.오른쪽)(4.우하)(5.하)(6.좌하)(7.좌)
		int gab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(pc.getX() + GAB_X[heading], pc.getY() + GAB_Y[heading]);
		int fishGab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(fishX, fishY);
		if (gab == 28 && fishGab == 28) {
			action(pc, fish);
		} else {
			pc.sendPackets(L1ServerMessage.sm1138);// 여기에 낚싯대를 던질 수 없습니다.
		}
	}
	
	void action(L1PcInstance pc, L1Fishing fish){
		if (fish.isBait() && !pc.getInventory().checkItem(41295, 1)) {
			pc.sendPackets(L1ServerMessage.sm1137);// 낚시를 하기 위해서는 먹이가 필요합니다.
			return;
		}
		pc._fishingRod	= this;
		pc._fishingX	= fishX;
		pc._fishingY	= fishY;
		pc.broadcastPacketWithMe(new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX, fishY), true);
		pc.setFishing(true);
		pc.setFishingTime(System.currentTimeMillis() + (fish.getInterval() * 1000));
		pc.sendPackets(new S_FishingNoti(fish.getInterval(), fish.getRilId() > 0 ? FISHING_ROD.FISHING_ROD_SPECIAL : FISHING_ROD.FISHING_ROD_NORMAL, this.getChargeCount()), true);
	}
}

