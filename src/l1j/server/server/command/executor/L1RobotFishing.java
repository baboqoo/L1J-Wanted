package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.RobotSystem.RobotAIThread;
import l1j.server.RobotSystem.RobotFishing;
import l1j.server.common.data.Gender;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.action.S_Fishing;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class L1RobotFishing implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1RobotFishing();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1RobotFishing() {}

	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			int count = Integer.parseInt(tok.nextToken());
			if (count <= 0) {
				//pc.sendPackets(new S_SystemMessage(String.format("%s [인원]", cmdName)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(74), cmdName), true);
				return false;
			}

			int cnt = 0;
			L1World world = L1World.getInstance();
			IdFactory idF = IdFactory.getInstance();
			while (count-- > 0) {
				String name = RobotAIThread.getName();
				if (name == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("더이상 생성할 이름이 존재하지않습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(643), true), true);
					return false;
				}

				L1PcInstance player = world.getPlayer(name);
				if (player != null) {
					continue;
				}

				L1PcInstance newPc = new L1PcInstance();
				newPc.setAccountName(StringUtil.EmptyString);
				newPc.setId(idF.nextId());
				newPc.setName(name);

				newPc.setHighLevel(5);
				newPc.setLevel(5);
				newPc.setExp(0);
				newPc.setAlignment(Config.SERVER.STANDBY_SERVER ? 0 : CommonUtil.random(Short.MAX_VALUE));
				newPc.setClanid(0);
				newPc.setClanName(StringUtil.EmptyString);
				newPc.setClanMemberNotes(StringUtil.EmptyString);
				newPc.setTitle(StringUtil.EmptyString);
				int typeCount = 0;
				for (L1PcInstance tempPc : world.getAllPlayers()) {
					if (tempPc.noPlayerCK && tempPc.getLevel() == 5) {
						typeCount++;
					}
				}
				RobotFishing rf = null;
				try {
					rf = RobotAIThread.getRobotFish().get(typeCount);
				} catch (Exception e) {
					continue;
				}
				if (rf == null) {
					continue;
				}
				
				newPc.setX(rf.x);
				newPc.setY(rf.y);
				newPc.setMap((short) rf.map);
				newPc.getMoveState().setHeading(rf.heading);
				int gender	= CommonUtil.random(1);
				int type	= CommonUtil.random(L1CharacterInfo.MALE_LIST.length);
				int sprite	= 0;

				switch (gender) {
				case 0:sprite = L1CharacterInfo.MALE_LIST[type];break;
				case 1:sprite = L1CharacterInfo.FEMALE_LIST[type];break;
				}

				newPc.noPlayerCK = true;
				newPc.setClassId(sprite);
				newPc.setSpriteId(sprite);
				newPc.setGender(Gender.fromInt(gender));
				newPc.setType(type);
				newPc.setFishing(true);
				newPc._fishingX = rf.fishX;
				newPc._fishingY = rf.fishY;
				newPc.broadcastPacket(new S_Fishing(newPc.getId(), ActionCodes.ACTION_Fishing,	rf.fishX, rf.fishY), true);

				world.storeObject(newPc);
				world.addVisibleObject(newPc);
				newPc.setNetConnection(null);

				cnt++;
				continue;
			}
			
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(String.format( "----------------------------------------------------\r\n%d명의 낚시터로봇이 배치 되었습니다.\r\n----------------------------------------------------", cnt)), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(String.format( "----------------------------------------------------\r\n" + " %d " + S_SystemMessage.getRefText(651), cnt), true), true);
			return true;
		} catch (Exception e) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s [인원]", cmdName)), true); // CHECKED OK
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(74), cmdName), true);
			return false;
		}
	}
	
}

