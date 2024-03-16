package l1j.server.server.serverpackets;

import l1j.server.server.BadNamesList;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1CharName;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class S_ChangeCharName extends ServerBasePacket {
	public static final int CHARACTER_NAME_CHANGE_UI		= 0x1d;
	public static final int CHARACTER_NAME_CHANGE_RESULT	= 0x1e;
	
	private boolean result;
	public boolean is_result() {
		return result;
	}
	
	public static S_ChangeCharName getChangedSuccess(){
		return CHAR_NAME_CHANGE_SUCCESS;
	}
	
	public static S_ChangeCharName getChangedFailure(){
		return CHAR_NAME_CHANGE_FAILURE;
	}
	
	public static final S_ChangeCharName CHAR_NAME_CHANGE_START		= getChangedStart();
	public static final S_ChangeCharName CHAR_NAME_CHANGE_SUCCESS	= getChangedResult(true);
	public static final S_ChangeCharName CHAR_NAME_CHANGE_FAILURE	= getChangedResult(false);
	
	public static S_ChangeCharName getChangedResult(boolean isSuccess){
		S_ChangeCharName s = new S_ChangeCharName();
		s.writeC(Opcodes.S_VOICE_CHAT);
		s.writeC(CHARACTER_NAME_CHANGE_RESULT);
		s.writeH(isSuccess ? 0x02 : 0x06);
		s.writeH(0x00);
		s.result = isSuccess;
		return s;
	}
	
	public static S_ChangeCharName getChangedStart() {
		S_ChangeCharName s = new S_ChangeCharName();
		s.writeC(Opcodes.S_VOICE_CHAT);
		s.writeC(CHARACTER_NAME_CHANGE_UI);
		s.writeD(0x00);
		return s;
	}
	
	public static S_ChangeCharName getChangedStart(String sourceName){
		S_ChangeCharName s = new S_ChangeCharName();
		s.writeC(Opcodes.S_VOICE_CHAT);
		s.writeC(CHARACTER_NAME_CHANGE_UI);
		s.writeS(sourceName);
		return s;
	}
	
	public static S_ChangeCharName doChangeCharName(GameClient client, String sourceName, String destinationName){
		try{
			L1PcInstance pc = client.getActiveChar();
			if (pc == null) {
				pc = L1PcInstance.load(sourceName);
			}
			
			byte[] buff = destinationName.getBytes(CharsetUtil.MS_949);
			if (buff.length <= 0) {
				return getChangedFailure();
			}
			if (CharacterTable.getInstance().isContainNameList(destinationName)) {
				return getChangedFailure();
			}
			if (BadNamesList.getInstance().isBadName(destinationName)) {
				return getChangedFailure();
			}
			for (int i = 0; i < destinationName.length(); i++) {
				if (StringUtil.INVALID_KO_CHARACTER.contains(destinationName.charAt(i))) {
					return getChangedFailure();
				}
			}
			
			for (int i = 0; i < destinationName.length(); i++) {
				if (!Character.isLetterOrDigit(destinationName.charAt(i))) {
					return getChangedFailure();
				}
			}
			
			if (!isAlphaNumeric(destinationName)) {
				return getChangedFailure();
			}
			// 변경
			CharacterTable.getInstance().CharacterNameChange("UPDATE characters SET char_name='" + destinationName + "' WHERE char_name='" + sourceName + "'");
			pc.save();// 저장
			//System.out.println(String.format("[%s] %s에서 %s로 캐릭명 변경", client.getIp(), sourceName, destinationName));
			System.out.println(String.format("[%s] Character name changed from %s to %s", client.getIp(), sourceName, destinationName));
			
			L1CharName cn = new L1CharName();
			cn.setName(destinationName);
			cn.setId(pc.getId());
			CharacterTable.getInstance().putContainNameList(destinationName, cn);
			
			BuddyTable.getInstance().removeBuddy(pc.getId(), sourceName);
			LetterTable.getInstance().removeLetter(pc);
			return getChangedSuccess();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return getChangedFailure();
	}
	
	public static boolean isAlphaNumeric(String s) {
		char[] acArray = s.toCharArray();
		for (char ac : acArray) {
			if (((ac >= 'A') && (ac <= 'z')) || ((ac >= 'a') && (ac <= 'z'))) {
				return true;
			}
			if ((ac >= '0') && (ac <= '9')) {
				return true;
			}
			if ((ac >= 44032) && (ac <= 55203)) {
				return true;
			}
		}
		return false;
	}

	public S_ChangeCharName(){}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

}

