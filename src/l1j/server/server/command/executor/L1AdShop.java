package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.Gender;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameServerSetting;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.action.S_DoActionShop;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1AdShop implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AdShop();
	}

	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1AdShop() {}
	
	private static final String[] SHOP_MENTS = {
		//"아이템 팝니다.", "팝니다.", "최저가 판매합니다.", "구경하고 가세요.", "이것저것 팝니다.", "ㅍㅍㅍㅍ", "템 ㅍㅍㅍㅍ", "정리중"
		"I sell items.", "For sale.", "Selling at the lowest price.", "Feel free to browse.", "Selling various items.", "hehehe", "Items hahaha", "Cleaning up."
	};

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			String name = stringtokenizer.nextToken();

			if (CharacterTable.doesCharNameExist(name) || L1World.getInstance().getPlayer(name) != null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("이미 존재하는 캐릭터 이름입니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(171), true), true);
				return false;
			}
			
			int gender = CommonUtil.random(1);
			int type = CommonUtil.random(L1CharacterInfo.MALE_LIST.length);
			int AccountName = 1;

			createAdShop(pc.getAccountName(), name, gender, type, pc.getX(), pc.getY(), pc.getMoveState().getHeading(), pc.getMapId());

			Connection con = null;
			PreparedStatement pstm = null;

			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("INSERT INTO adShop SET account = ?, name = ?, sex = ?, type = ?, x = ?, y = ?, heading = ?, map_id = ?");
				pstm.setInt(1, AccountName);
				pstm.setString(2, name);
				pstm.setInt(3, gender);
				pstm.setInt(4, type);
				pstm.setInt(5, pc.getX());
				pstm.setInt(6, pc.getY());
				pstm.setInt(7, pc.getMoveState().getHeading());
				pstm.setInt(8, pc.getMapId());
				pstm.execute();				
			} catch(Exception e) {  
				e.printStackTrace();
			} finally {
				SQLUtil.close(pstm, con);
			}
			stringtokenizer = null;
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(".장사시작 [캐릭이름]로 입력해 주세요. "). toString()), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
	
	public static void createAdShop(String account, String name, int gender, int type, int x, int y, int heading, int mapId) {
		if (CharacterTable.doesCharNameExist(name) || L1World.getInstance().getPlayer(name) != null) {
			return;
		}

		L1PcInstance newPc = new L1PcInstance();
		newPc.setAccountName(account);
		newPc.setId(IdFactory.getInstance().nextId());
		newPc.setName(name);
		newPc.setHighLevel(1);
		newPc.setExp(0);
		newPc.addBaseMaxHp((short)2000);//14
		newPc.setCurrentHp(2000);//14
		newPc.setDead(false);
		newPc.setActionStatus(0);
		newPc.addBaseMaxMp((short)2);
		newPc.setCurrentMp(2);		
		newPc.getAbility().setBaseStr(16);
		newPc.getAbility().setStr(16);
		newPc.getAbility().setBaseCon(16);
		newPc.getAbility().setCon(16);
		newPc.getAbility().setBaseDex(11);
		newPc.getAbility().setDex(11);
		newPc.getAbility().setBaseCha(13);
		newPc.getAbility().setCha(13);
		newPc.getAbility().setBaseInt(12);
		newPc.getAbility().setInt(12);
		newPc.getAbility().setBaseWis(11);
		newPc.getAbility().setWis(11);
		
		int klass = gender == 0 ? L1CharacterInfo.MALE_LIST[type] : L1CharacterInfo.FEMALE_LIST[type];

		newPc.setCurrentWeapon(0);
		newPc.setClassId(klass);
		newPc.setSpriteId(klass);
		newPc.setGender(Gender.fromInt(gender));
		newPc.setType(type);
		newPc.getMoveState().setHeading(heading);
		newPc.setX(x);
		newPc.setY(y);
		newPc.setMap((short)mapId);
		newPc.setFood(GameServerSetting.MIN_FOOD_VALUE);
		newPc.setTitle(StringUtil.EmptyString);
		newPc.setClanName(StringUtil.EmptyString);
		newPc.setAccessLevel((short)0);
		newPc.setGm(false);
		newPc.setMonitor(false);
		newPc.setOnlineStatus(1);
		newPc.setBanned(false);
		newPc.refresh();
		newPc.getMoveState().setMoveSpeed(0);
		newPc.getMoveState().setBraveSpeed(0);
		newPc.setGmInvis(false);		
		L1World.getInstance().storeObject(newPc);
		L1World.getInstance().addVisibleObject(newPc);		
		newPc.setNetConnection(null);
		newPc.startObjectAutoUpdate();
		
		byte[] chat = ((String)CommonUtil.randomChoice(SHOP_MENTS)).getBytes();
		
		newPc.setShopChat(chat);
		newPc.setPrivateShop(true);
		newPc.broadcastPacketWithMe(new S_DoActionShop(newPc.getId(), ActionCodes.ACTION_Shop, chat), true);
	}

}


