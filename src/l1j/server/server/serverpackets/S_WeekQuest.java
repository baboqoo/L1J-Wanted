package l1j.server.server.serverpackets;

import java.util.ArrayList;

import l1j.server.QuestSystem.Compensator.WeekQuestCompensator;
import l1j.server.QuestSystem.Loader.MonsterBookCompensateLoader;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_WeekQuest extends ServerBasePacket{
	private byte[] _byte = null;
	public static final int LIST = 0x032A;
	public static final int UPDATE = 0x032D;
	public static final int COMPLETE = 0x032E;
	
	private static final byte[] WQLIST_ANONYMOUSE = new byte[]{
		0x1A, 0x0B, 0x08, 0x01, 0x10, (byte) 0x84, (byte) 0xA0, (byte) 0xB8, 0x03, 0x18, (byte) 0xC0, (byte) 0x87, 0x01, 0x1A, 0x0B, 0x08, 0x01, 0x10, (byte) 0xB5, (byte) 0xBF, (byte) 0xF0, 0x06, 0x18, (byte) 0xD8, (byte) 0x87, 0x01,
	};
	
	public S_WeekQuest(){}
	
	/** pc 인스턴스의 주간 퀘스트 리스트를 전송한다. **/
	public void writeWQList(L1PcInstance pc){
		MonsterBookCompensateLoader compensator = MonsterBookCompensateLoader.getInstance();
		ArrayList<WeekQuestCompensator> list = compensator.getWeekCompensators();
		try {
			byte[] comp1 = list.get(0).getSerialize();
			byte[] comp2 = list.get(1).getSerialize();
			byte[] comp3 = list.get(2).getSerialize();
			byte[] progress = pc.getQuest().getWeekQuest().getSerialize();

			int size = 0;
			size += WQLIST_ANONYMOUSE.length;
			size += comp1.length + comp2.length + comp3.length;
			size += progress.length;
			size += 2;
			
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeH(LIST);
			writeC(0x0A);
			writeC(size);
			writeC(0x01);
			writeC(0x0A);
			writeC(0x55);
			writeByte(comp1);
			writeByte(comp2);
			writeByte(comp3);
			writeByte(WQLIST_ANONYMOUSE);
			writeByte(progress);
			writeH(0x00);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/** 현재 진행 중인 퀘스트의 갱신을 통지한다.
	 * difficulty 	: 0, 1, 2 난이도,
	 * section 		: 0, 1, 2 섹션
	 * count : 잡은 마리 수(max/max 되면 자동으로 clear 딱지를 붙임)
	 *  **/
	public void writeWQUpdate(int difficulty, int section, int count){
		writeSignature(UPDATE);
		writeByte(new byte[]{
				0x08, (byte)(difficulty & 0xff), 
				0x10, (byte)(section & 0xff), 
				0x18, (byte)(count & 0xff), 
				0x00, 0x00
			}
		);
	}
	
	/** 라인 클리어 관련 패킷을 보낸다. **/
	/** 
	 * difficulty 	: 난이도
	 * status		: 상태값 (1=활성화만 시킴, 3=클리어메시지(이벤트알림에)를 보내고 보상버튼 활성화, 5=라인 클리어를 표시하고 보상버튼 비활성화)
	 * (4=라인 비활성화... 주간 퀘 갱신때 사용하면 될듯)
	 *  **/
	public void writeWQLineClear(int difficulty, int status){
		writeSignature(COMPLETE);
		writeByte(new byte[]{
				0x08, (byte)(difficulty & 0xff),
				0x10, (byte)(status & 0xff),
				0x00, 0x00
			}
		);
	}
	
	private void writeSignature(int type){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
	}
	
	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = getBytes();
		return _byte;
	}

	@Override
	public String getType() {
		return "S_WeekQuest";
	}
}
