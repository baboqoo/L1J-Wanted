package l1j.server.common.bin;

import java.io.IOException;

public interface ProtoMessage {
	public int getSerializedSize();
	public int getMemorizedSerializeSizedSize();
	public boolean isInitialized();// 바이트 완료 여부
	public long getInitializeBit();
	public void writeTo(ProtoOutputStream stream) throws IOException;// 바이트 쓰기 output
	public ProtoMessage readFrom(ProtoInputStream stream) throws IOException;// 바이트 읽기 input
	public void dispose();
}

