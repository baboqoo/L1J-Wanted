package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;

public class S_ShowCmd extends ServerBasePacket{
	//TODO 프로토 서브코드 출력
	public static S_ShowCmd getProto8(int n){
		S_ShowCmd s = new S_ShowCmd(n);
//		s.writeC(0x08);
//		s.writeC(0x00); //0일때 1일때 나오는게 다르다
//		s.writeC(0x10);
//		s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	//TODO 프로토 서브코드 출력
	public static S_ShowCmd getProtoA(int n){
		S_ShowCmd s = new S_ShowCmd(n);
		s.writeC(0x0A);
		s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	
	private S_ShowCmd(int i){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(i);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}

