package l1j.server;

import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.serverpackets.message.S_SystemMessage;

enum SpecialEvent { BugRace, AllBuf, InfinityFight, DoNotChatEveryone, DoChatEveryone};

// 게임 내, 전체 이벤트에 대한 처리를 담당
public class SpecialEventHandler {

	private static volatile SpecialEventHandler uniqueInstance = null;

	private SpecialEventHandler() {}

	public static SpecialEventHandler getInstance() {
		if (uniqueInstance == null) {
			synchronized (SpecialEventHandler.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new SpecialEventHandler();
				}
			}
		}
		return uniqueInstance;
	}

	public void doGiveEventStaff() {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getNetConnection() != null) {
				if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(30105), 3) != L1Inventory.OK) continue;
				L1ItemInstance item = pc.getInventory().storeItem(30105, 3);// 크리스마스 선물상자
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("■ 전체선물 ■: \\aA[" + item.getLogNameRef() + "]이 도착했습니다.", true), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1238) + item.getLogNameRef()  + S_SystemMessage.getRefText(1239), true), true);
			}
		}
	}
	
	public void integratedBuff() {
		int[] allBuffSkill = L1SkillInfo.GM_BUFF_NORMAL_ARRAY;
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			}
			pc.send_effect(4856);
		}
//AUTO SRM: 		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aA알림: 게임마스터께서 '전체 버프'를 제공하였습니다."), true); // CHECKED OK
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(1240), true), true);
	}
	public void screenPullUp(L1PcInstance gm) {// 화면 안의 유져에게 풀버프
		int[] allBuffSkill = L1SkillInfo.GM_BUFF_DISPLAY_ARRAY;
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(gm, 30)) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			for (int i = 0; i < allBuffSkill.length ; i++) {
			    l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aA알림: 게임마스터 주위에 '버프'가 제공하었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1241), true), true);
		}
	}
	
	public void screenBlessing(L1PcInstance gm) {
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(gm, 30)) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_A))
				pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_A);
			if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_B))
				pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_B);
			if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_C))
				pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_C);
			if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_D))
				pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_D);
			l1skilluse.handleCommands(pc, L1SkillId.FEATHER_BUFF_A, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			pc.send_effect(4856);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aA알림: 게임마스터 주위에 '메티스의 축복'을 제공하였습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1242), true), true);
		}
	}
	
	public void screenRawHorse(L1PcInstance gm) {
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(gm, 30)) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.ANTA_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.ANTA_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.FAFU_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.FAFU_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.LIND_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.LIND_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.VALA_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.VALA_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.BIRTH_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.BIRTH_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.LIFE_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.LIFE_MAAN);
			l1skilluse.handleCommands(pc, L1SkillId.LIFE_MAAN, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			pc.send_effect(4856);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aA알림: 게임마스터 주위에 '생명의 마안'을 제공하였습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1243), true), true);
		}
	}
	
	public void screenBlack(L1PcInstance gm) {
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(gm, 30)) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.BUFF_BLACK_SAND)) {
				pc.getSkill().removeSkillEffect(L1SkillId.BUFF_BLACK_SAND);
			}
			l1skilluse.handleCommands(pc, L1SkillId.BUFF_BLACK_SAND, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			pc.send_effect(4856);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aA알림: 게임마스터 주위에 '흑사의 버프'을 제공하였습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1244), true), true);
		}
	}
	
	public void screenComa(L1PcInstance gm) {
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(gm, 30)) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.COMA_A)) {
				pc.getSkill().removeSkillEffect(L1SkillId.COMA_A);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.COMA_B)) {
				pc.getSkill().removeSkillEffect(L1SkillId.COMA_B);
			}
			l1skilluse.handleCommands(pc, L1SkillId.COMA_B, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			pc.send_effect(4856);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aA알림: 게임마스터 주위에 '코마 버프'을 제공하였습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1245), true), true);
		}
	}
	
	public void fullPullUp() {
		int[] allBuffSkill = L1SkillInfo.GM_BUFF_ALL_ARRAY;
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			}
			pc.send_effect(4856);
		}
//AUTO SRM: 		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aA알림: 게임마스터께서 '버프'를 제공하였습니다."), true); // CHECKED OK
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(1246), true), true);
	}
	
	public void fullBlessing() {
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_A)) {
				pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_A);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_B)) {
				pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_B);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_C)) {
				pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_C);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.FEATHER_BUFF_D)) {
				pc.getSkill().removeSkillEffect(L1SkillId.FEATHER_BUFF_D);
			}
			l1skilluse.handleCommands(pc, L1SkillId.FEATHER_BUFF_A, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			pc.send_effect(4856);
		}
//AUTO SRM: 		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aA알림: 게임마스터께서'메티스의 축복'을 제공하였습니다."), true); // CHECKED OK
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(1247), true), true);
	}
	
	public void fullRawHorse() {
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.ANTA_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.ANTA_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.FAFU_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.FAFU_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.LIND_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.LIND_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.VALA_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.VALA_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.BIRTH_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.BIRTH_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_MAAN);
			if (pc.getSkill().hasSkillEffect(L1SkillId.LIFE_MAAN))
				pc.getSkill().removeSkillEffect(L1SkillId.LIFE_MAAN);
			l1skilluse.handleCommands(pc, L1SkillId.LIFE_MAAN, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			pc.send_effect(4856);
		}
//AUTO SRM: 		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aA알림: 게임마스터께서 '생명의 마안'을 제공하였습니다."), true); // CHECKED OK
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(1248), true), true);
	}
	
	public void fullBlack() {
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.BUFF_BLACK_SAND)) {
				pc.getSkill().removeSkillEffect(L1SkillId.BUFF_BLACK_SAND);
			}
			l1skilluse.handleCommands(pc, L1SkillId.BUFF_BLACK_SAND, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			pc.send_effect(4856);
		}
//AUTO SRM: 		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aA알림: 게임마스터께서 '흑사의 버프'를 제공하였습니다."), true); // CHECKED OK
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(1249), true), true);
	}
	
	public void fullComa() {
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.isPrivateShop() || pc.getMapId() == 5166 || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.COMA_A)) {
				pc.getSkill().removeSkillEffect(L1SkillId.COMA_A);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.COMA_B)) {
				pc.getSkill().removeSkillEffect(L1SkillId.COMA_B);
			}
			l1skilluse.handleCommands(pc, L1SkillId.COMA_B, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
			pc.send_effect(4856);
		}
//AUTO SRM: 		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aA알림: 게임마스터께서 '코마 버프'를 제공하였습니다."), true); // CHECKED OK
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(1250), true), true);
	}

	public void doNotChatEveryone() {
		L1World.getInstance().setWorldChatElabled(false);
//AUTO SRM: 		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aG경고: \\aA월드채팅 비활성화 시작"), true); // CHECKED OK
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(1251), true), true);
	}

	public void doChatEveryone() {
		L1World.getInstance().setWorldChatElabled(true);
//AUTO SRM: 		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\aG경고: \\aA월드채팅 활성화 시작"), true); // CHECKED OK
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(S_SystemMessage.getRefText(1252), true), true);
	}
}


