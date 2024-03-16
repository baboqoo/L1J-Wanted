package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1BoardPost;

public class S_BoardRead extends ServerBasePacket {
	private static final String S_BoardRead = "[S] S_BoardRead";
	
	private byte[] _byte = null;

	public S_BoardRead(L1NpcInstance board, int number) {
		if (board.getNpcId() == 4200015) //서버공지
			buildPacketNotice(board, number);
		else if (board.getNpcId() == 4200020)//Gm1
			buildPacketNotice1(board, number);
		else if (board.getNpcId() == 4200021)//gm2
			buildPacketNotice2(board, number);
		else if (board.getNpcId() == 4200022)//gm3
			buildPacketNotice3(board, number);
		else if (board.getNpcId() == 900006)//드키
			buildPacketKey(board, number);
		else if (board.getNpcId() == 500002)//건의
			buildPacketFix(board, number);
		else
			buildPacket(board, number);//기본값
	}
	
	private void buildPacket(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findById(number);
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());
	}
	private void buildPacketNotice(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdGM(number);
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());
	}
	private void buildPacketNotice1(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdGM1(number);
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());
	}
	private void buildPacketNotice2(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdGM2(number);
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());
	}
	private void buildPacketNotice3(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdGM3(number);
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());
	}
	private void buildPacketFix(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdFix(number);
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());
	}

	private void buildPacketKey(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdKey(number);
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null)
			_byte = getBytes();
		return _byte;
	}

	@Override
	public String getType() {
		return S_BoardRead;
	}
}


