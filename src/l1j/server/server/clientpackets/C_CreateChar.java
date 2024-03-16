package l1j.server.server.clientpackets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.RobotSystem.RobotAIThread;
import l1j.server.common.data.Gender;
import l1j.server.server.BadNamesList;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerSetting;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.controller.FakeCharacterController;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Beginner;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharCreateStatus;
import l1j.server.server.serverpackets.S_NewCharPacket;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;
//import manager.ManagerInfoThread;  // MANAGER DISABLED

public class C_CreateChar extends ClientBasePacket {
	private static Logger _log = Logger.getLogger(C_CreateChar.class.getName());
	private static final String C_CREATE_CHAR = "[C] C_CreateChar";

	public C_CreateChar(byte[] abyte0, GameClient client) throws Exception {
		super(abyte0);
		if (client == null) {
			return;
		}
		// 영어는 한글자당 1의 길이, 한글은 한글자당 2의 길이
		String name = readS();
		if (StringUtil.isNullOrEmpty(name) || name.length() > 20) {
			System.out.println(String.format(
					"[C_CreateChar] Abnormal character creation was discovered and was forced to terminate.\r\n■ Packet attack NAME ■ :%s\r\n■ Packet attack IP ■ :%s",
					name, client.getIp()));
			client.kick();
			return;
		}
		for (int i = 0; i < name.length(); i++) {  
			if (StringUtil.INVALID_KO_CHARACTER.contains(name.charAt(i))) {
				client.sendPacket(S_CharCreateStatus.NAME_FAIL);
				return; 
			}
		}

		if (BadNamesList.getInstance().isBadName(name)) {
			client.sendPacket(S_CharCreateStatus.NAME_FAIL);
			return;
		}
		/** 로봇시스템 **/
		if (RobotAIThread.doesCharNameExist(name) || FakeCharacterController.doesFakeCharNameExist(name)) {
			_log.fine("account: " + client.getAccountName() + " already exists. creation failed.");
			client.sendPacket(S_CharCreateStatus.ALREADY_EXSISTS);
			return;
		}

		if (isInvalidName(name)) {
			client.sendPacket(S_CharCreateStatus.NAME_FAIL);
			return;
		}

		if (CharacterTable.doesCharNameExist(name) || L1World.getInstance().getPlayer(name) != null) {
			_log.fine("account: " + client.getAccountName() + " already exists. creation failed.");
			client.sendPacket(S_CharCreateStatus.ALREADY_EXSISTS);
			return;
		}

		if (client.getAccount().countCharacters() >= client.getAccount().getCharSlot()) {
			client.sendPacket(S_CharCreateStatus.AMOUNT_MAX);
			return;
		}

		L1PcInstance pc = new L1PcInstance();
		pc.setName(name);
		pc.setType(readC());
		pc.setGender(Gender.fromInt(readC()));
		L1Ability ability = pc.getAbility();
		ability.setBaseStr((byte) readC());
		ability.setBaseDex((byte) readC());
		ability.setBaseCon((byte) readC());
		ability.setBaseWis((byte) readC());
		ability.setBaseCha((byte) readC());
		ability.setBaseInt((byte) readC());

		int statusAmount = ability.getStatsAmount();
		if (ability.getBaseStr() > 20 || ability.getBaseDex() > 20 || ability.getBaseCon() > 20
				|| ability.getBaseWis() > 20 || ability.getBaseCha() > 20 || ability.getBaseInt() > 20
				|| statusAmount != 75) {
			_log.finest("Character have wrong value");
			client.sendPacket(S_CharCreateStatus.AMOUNT_MAX);
			return;
		}

		_log.fine("charname: " + pc.getName() + " classId: " + pc.getClassId());
		client.sendPacket(S_CharCreateStatus.OK);
		initNewChar(client, pc);
		client.getAccount().getArca().createActivation(pc.getId());
		
		Calendar cal = Calendar.getInstance();
		/** 0 오전 , 1 오후 * */
		String AMPM = cal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
		System.out.println(cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + " " + AMPM + "   ■ New Character: [" + pc.getName() + "] created ■");
		//ManagerInfoThread.CharCount += 1;  // MANAGER DISABLED
	}

	private static void initNewChar(GameClient client, L1PcInstance pc) throws IOException, Exception {
		pc.setId(IdFactory.getInstance().nextId());
		pc.setClassId(pc.getGender() == Gender.MALE ? L1CharacterInfo.MALE_LIST[pc.getType()] : L1CharacterInfo.FEMALE_LIST[pc.getType()]);

		short init_hp	= (short) CalcStat.startHp(pc.getType());
		short init_mp	= (short) CalcStat.startMp(pc.getType(), pc.getAbility().getBaseWis());
		int startPos	= CommonUtil.random(L1CharacterInfo.START_LOC_X.length);
		if (Config.ALT.NEW_CHAR_START_AREA) {
			pc.setX(L1CharacterInfo.START_LOC_X[0][startPos]);
			pc.setY(L1CharacterInfo.START_LOC_Y[0][startPos]);
			pc.setMap(L1CharacterInfo.MAPID_LIST[0]);
		} else {
			pc.setX(L1CharacterInfo.START_LOC_X[1][startPos]);
			pc.setY(L1CharacterInfo.START_LOC_Y[1][startPos]);
			pc.setMap(L1CharacterInfo.MAPID_LIST[1]);
		}
		
		pc.getMoveState().setHeading(0);
		pc.setAlignment(0);
		pc.addBaseMaxHp(init_hp);
		pc.setCurrentHp(init_hp);
		pc.addBaseMaxMp(init_mp);
		pc.setCurrentMp(init_mp);
		pc.resetBaseAc();
		pc.setTitle(StringUtil.EmptyString);
		pc.setClanid(0);
		pc.setFood(GameServerSetting.MAX_FOOD_VALUE); // 100%
		pc.setAccessLevel((short) 0);
		pc.setGm(false);
		pc.setMonitor(false);
		pc.setGmInvis(false);
		pc.setExp(0);// 경험치 0
		pc.setHighLevel(1);
		pc.setActionStatus(0);
		pc.setClanName(StringUtil.EmptyString);
		pc.setClanMemberNotes(StringUtil.EmptyString);
		pc.setBonusStats(0);
		pc.setElixirStats(0);
		pc.resetBaseMr();
		pc.setElfAttr(0);
		pc.setPKcount(0);
		pc.setExpRes(0);
		pc.setPartnerId(0);
		pc.setOnlineStatus(0);
		pc.setHomeTownId(0);
		pc.setContribution(0);
		pc.setBanned(false);
		pc.setKarma(0);
		pc.setReturnStat_exp(0);
		pc.setBookMarkCount(60);

		Calendar local_c = Calendar.getInstance();
		SimpleDateFormat local_sdf = new SimpleDateFormat("yyyyMMdd");
		local_c.setTimeInMillis(System.currentTimeMillis());
		pc.setBirthDay(Integer.parseInt(local_sdf.format(local_c.getTime())));
		if (pc.isWizard()) { // 마법사 에너지볼트 추가
			pc.sendPackets(S_AvailableSpellNoti.ENERGE_BOLT);
			L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.ENERGY_BOLT);
			pc.getSkill().spellActiveMastery(l1skills);
		} else if (pc.isElf()) { // 요정 텔리포트투마더 추가
			pc.sendPackets(S_AvailableSpellNoti.TELEPORT_MOTHER);
			L1Skills l1skills = SkillsTable.getTemplate(L1SkillId.TELEPORT_TO_MATHER);
			pc.getSkill().spellActiveMastery(l1skills);
		}
		Beginner.getInstance().GiveItem(pc);
		Beginner.getInstance().writeBookmark(pc);
		pc.setAccountName(client.getAccountName());
		pc.createFatigue(0, null);
		CharacterTable.getInstance().storeNewCharacter(pc);
		S_NewCharPacket s_newcharpacket = new S_NewCharPacket(pc);
		client.sendPacket(s_newcharpacket);
		pc.save();
		pc.refresh();
		client.getAccount().addCharList(pc);
		s_newcharpacket = null;
	}

	private static boolean isAlphaNumeric(String s) {
		if (s == null) {
			return false;
		}
		boolean flag = true;
		char ac[] = s.toCharArray();
		int i = 0;
		do {
			if (i >= ac.length) {
				break;
			}
			if (!Character.isLetterOrDigit(ac[i])) {
				flag = false;
				break;
			}
			i++;
		} while (true);
		return flag;
	}

	public static boolean isInvalidName(String name) {
		int numOfNameBytes = 0;
		try {
			numOfNameBytes = name.getBytes(CharsetUtil.EUC_KR_STR).length;
		} catch (UnsupportedEncodingException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return true;
		}
		if (!isAlphaNumeric(name)) {
			return true;
		}
		if (6 < (numOfNameBytes - name.length()) || 12 < numOfNameBytes || numOfNameBytes < 2) {
			return true;
		}
		if (BadNamesList.getInstance().isBadName(name)) {
			return true;
		}
		return false;
	}

	@Override
	public String getType() {
		return C_CREATE_CHAR;
	}
}

