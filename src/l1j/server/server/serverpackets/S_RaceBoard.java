package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.controller.DollRaceController;

public class S_RaceBoard extends ServerBasePacket {
	private static final String S_RaceBoard = "[C] S_RaceBoard";
	private byte[] _byte = null;

	public S_RaceBoard(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		String str_text = "";
		String spacer = "                                                           ";

		for(int i = 0; i < 5; ++i){
			str_text += DollRaceController.getInstance()._doll[i].getName() + spacer; //버그베어 이름
			str_text += DollRaceController.getInstance()._dollCondition[i] + spacer; //상태
			str_text += Double.toString(DollRaceController.getInstance()._winRate[i]) + "%" + spacer; //승률
		}

		writeC(Opcodes.S_HYPERTEXT);
		writeD(0);
		writeS("deposit");
		writeH(0x02);
		writeH(0x02);
		writeS("$30044");
		writeS(str_text);

		/*writeC(Opcodes.S_HYPERTEXT);
		writeD(number);
		writeS("maeno4");
		writeC(0);                        
		writeH(15);
		for(int i = 0; i < 5; ++i){
			writeS(DollRaceController.getInstance()._doll[i].getName()); //버그베어 이름
			writeS(DollRaceController.getInstance()._dollCondition[i]); //상태
			writeS(Double.toString(DollRaceController.getInstance()._winRate[i]) + "%"); //승률
		}*/
	}

	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = getBytes();
		return _byte;
	}

	public String getType() {
		return S_RaceBoard;
	}
}


