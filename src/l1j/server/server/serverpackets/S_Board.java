package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.controller.DollRaceController;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1BoardPost;
import l1j.server.server.utils.StringUtil;

public class S_Board extends ServerBasePacket {
	private static final String S_BOARD = "[S] S_Board";

	private static final int TOPIC_LIMIT = 8;
	
	private byte[] _byte = null;

	public S_Board(L1NpcInstance board) {
		if (board.getNpcId() == 4200015) // 서버 정보 게시판
			buildPacketNotice(board, 0);
		else if (board.getNpcId() == 4200020) // 운영자1
			buildPacketNotice1(board, 0);
		else if (board.getNpcId() == 4200021) // 운영자2
			buildPacketNotice2(board, 0);
		else if (board.getNpcId() == 4200022) // 운영자3
			buildPacketNotice3(board, 0);
		else if (board.getNpcId() == 500002) // 건의사항
			buildPacketFix(board, 0);
		else if (board.getNpcId() == 900006) // 드래곤키 알림게시판
			buildPacketKey(board, 0);
		else if (board.getNpcId() == 999999) // 버경게시판
			buildPacket1(board, 0);
		else if (board.getNpcId() == 500001) // 전체랭킹
			buildPacket2(board, 0);
		else if (board.getNpcId() == 4200013) // 버그게시판
			buildPacket3(board, 0);
		else
			buildPacket(board, 0); //자유 게시판
	}

	public S_Board(L1NpcInstance board, int number) {
		if (board.getNpcId() == 4200015) // 서버 정보 게시판
			buildPacketNotice(board, number);
		else if (board.getNpcId() == 4200020) // 운영자1
			buildPacketNotice1(board, number);
		else if (board.getNpcId() == 4200021) // 운영자2
			buildPacketNotice2(board, number);
		else if (board.getNpcId() == 4200022) // 운영자3
			buildPacketNotice3(board, number);
		else if (board.getNpcId() == 500002) // 건의사항
			buildPacketFix(board, number);
		else if (board.getNpcId() == 900006) // 드래곤키 알림게시판
			buildPacketKey(board, number);
		else if (board.getNpcId() == 999999) // 버경게시판
			buildPacket1(board, number);
		else if (board.getNpcId() == 500001) // 전체랭킹
			buildPacket2(board, number);
		else if (board.getNpcId() == 4200013) // 버그게시판
			buildPacket3(board, number);
		else
			buildPacket(board, number); //자유 게시판
	}
	
	private void buildPacket1(L1NpcInstance board, int number) {	// 버경 출전 선수 상태
		writeC(Opcodes.S_HYPERTEXT);
		writeD(board.getId());
		writeS("maeno4");
		writeC(0);                        
		writeH(15);

		for( int i = 0; i < 5; ++i ) {
			writeS(DollRaceController.getInstance()._doll[i].getName()); //버그베어 이름
			writeS(DollRaceController.getInstance()._dollCondition[i]); //상태
			writeS(Double.toString(DollRaceController.getInstance()._winRate[i]) + "%"); //승률
		}
	}
	private void buildPacket2(L1NpcInstance board,int number) {
		int count = 0;
		String[][] db = null;
		int[] id = null;
		db = new String[9][3];
		id = new int[9];
		while (count < 9) {
			id[count] = count + 1;
			db[count][0] = StringUtil.EmptyString;// Ranking
			db[count][1] = StringUtil.EmptyString;
			count++;
		}
		//db[0][2] = "--------- 전체순위";
		/*
		db[0][2] = "--------- 전     사";
		db[1][2] = "--------- 군     주";
		db[2][2] = "--------- 기     사";
		db[3][2] = "--------- 요     정";
		db[4][2] = "--------- 법     사";
		db[5][2] = "--------- 다     엘";
		db[6][2] = "--------- 용 기 사";
		db[7][2] = "--------- 환 술 사";
		*/
		
		db[0][2] = "--------- Before The War";
		db[1][2] = "--------- Sovereign Nation";
		db[2][2] = "--------- Royal Guards";
		db[3][2] = "--------- Court Magician";
		db[4][2] = "--------- Law Enforcer";
		db[5][2] = "--------- Dal El";
		db[6][2] = "--------- Guardian Knight";
		db[7][2] = "--------- Enchanter";	

		writeC(Opcodes.S_BOARD_LIST);
		writeC(0);
		writeD(board.getId());
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0x7F); // ?
		writeH(9);
		writeH(300);
		for (int i = 0; i < 8; ++i) {
			writeD(id[i]);
			writeS(db[i][0]);
			writeS(db[i][1]);
			writeS(db[i][2]);
		}
	}
	private void buildPacket3(L1NpcInstance board,int number) {
		int count = 0;
		String[][] db = null;
		int[] id = null;
		db = new String[8][3];
		id = new int[8];
		while(count < 8) {
			id[count] = count + 1;
			//db[count][0] = "버그감시";
			db[count][0] = "Bug Monitoring";
			db[count][1] = StringUtil.EmptyString;
			count++;
		}
		/*
		db[0][2] = "1. 무기 랭킹";
		db[1][2] = "2. 방어구 랭킹";
		db[2][2] = "3. 아덴 랭킹";
		db[3][2] = "4. 레벨 랭킹";
		db[4][2] = "5. 신비깃털 랭킹";
		db[5][2] = "6. 창고아덴랭킹";
		db[6][2] = "7. HP랭킹";
		db[7][2] = "8. MP랭킹";
		*/

		db[0][2] = "1. Weapon Ranking";
		db[1][2] = "2. Armor Ranking";
		db[2][2] = "3. Aden Ranking";
		db[3][2] = "4. Level Ranking";
		db[4][2] = "5. Mystic Feather Ranking";
		db[5][2] = "6. Warehouse Adena Ranking";
		db[6][2] = "7. HP Ranking";
		db[7][2] = "8. MP Ranking";		

		writeC(Opcodes.S_BOARD_LIST);
		//writeC(0x00);//10월18일추가
		writeC(0);
		writeD(board.getId());
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0x7F); // ?
		writeH(8);
		writeH(300);
		for(int i = 0; i < 8; ++i){
			writeD(id[i]);
			writeS(db[i][0]);
			writeS(db[i][1]);
			writeS(db[i][2]);
		}
    }
	
	private void buildPacket(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.index(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		writeD(number == 0 ? 0x7FFFFFFF : number);
		writeC(topics.size());
		if(number == 0){
			writeC(0);
			writeH(300);
		}
		for(L1BoardPost topic : topics){
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	private void buildPacketNotice(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexGM(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		writeD(number == 0 ? 0x7FFFFFFF : number);
		writeC(topics.size());
		if(number == 0){
			writeC(0);
			writeH(300);
		}
		for(L1BoardPost topic : topics){
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	private void buildPacketNotice1(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexGM1(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		writeD(number == 0 ? 0x7FFFFFFF : number);
		writeC(topics.size());
		if(number == 0){
			writeC(0);
			writeH(300);
		}
		for(L1BoardPost topic : topics){
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	private void buildPacketNotice2(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexGM2(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		writeD(number == 0 ? 0x7FFFFFFF : number);
		writeC(topics.size());
		if(number == 0){
			writeC(0);
			writeH(300);
		}
		for(L1BoardPost topic : topics){
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	private void buildPacketNotice3(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexGM3(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		writeD(number == 0 ? 0x7FFFFFFF : number);
		writeC(topics.size());
		if(number == 0){
			writeC(0);
			writeH(300);
		}
		for(L1BoardPost topic : topics){
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	private void buildPacketFix(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexFix(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		writeD(number == 0 ? 0x7FFFFFFF : number);
		writeC(topics.size());
		if(number == 0){
			writeC(0);
			writeH(300);
		}
		for(L1BoardPost topic : topics){
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	
	private void buildPacketKey(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexKey(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		writeD(number == 0 ? 0x7FFFFFFF : number);
		writeC(topics.size());
		if(number == 0){
			writeC(0);
			writeH(300);
		}
		for(L1BoardPost topic : topics){
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	
	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = getBytes();
		return _byte;
	}

	@Override
	public String getType() {
		return S_BOARD;
	}
}


