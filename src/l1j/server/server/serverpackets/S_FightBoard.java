package l1j.server.server.serverpackets;

import l1j.server.server.controller.DogFightController;
import l1j.server.server.utils.StringUtil;
import l1j.server.server.Opcodes;

public class S_FightBoard extends ServerBasePacket {

	private static final String S_RaceBoard = "[C] S_RaceBoard";

	public S_FightBoard(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		String str_text = "";
		String spacer = "                                                           ";

		for (int i = 0; i < 2; ++i) {
			str_text += DogFightController.getInstance()._dogfight[i].getName() + spacer; 				//투견 이름
			str_text += DogFightController.getInstance()._FightCondition[i] + spacer; 					//상태
			str_text += Double.toString(DogFightController.getInstance()._winRate[i]) + "%" + spacer; 	//승률
		}

		writeC(Opcodes.S_HYPERTEXT);
		writeD(0);
		writeS("deposit");
		writeH(0x02);
		writeH(0x02);
		writeS("$30044");
		writeS(str_text);
				
		/*
		writeC(Opcodes.S_HYPERTEXT);
		writeD(number);
		writeS("maeno4");
		writeC(0);                        
		writeH(15); // number of strings below
		
		for (int i = 0; i < 2; ++i) {
			writeS(DogFightController.getInstance()._dogfight[i].getName()); 				//투견 이름
			writeS(DogFightController.getInstance()._FightCondition[i]); 					//상태
			writeS(Double.toString(DogFightController.getInstance()._winRate[i]) + "%"); 	//승률
		}

		writeS(StringUtil.EmptyString);
		writeS(StringUtil.EmptyString);
		writeS(StringUtil.EmptyString);
		writeS(StringUtil.EmptyString);
		writeS(StringUtil.EmptyString);
		writeS(StringUtil.EmptyString);
		writeS(StringUtil.EmptyString);
		writeS(StringUtil.EmptyString);
		writeS(StringUtil.EmptyString);
		*/
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_RaceBoard;
	}
}


