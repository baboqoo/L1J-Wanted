package l1j.server.server.clientpackets.proto;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.utils.HexHelper;

/**
 * 클라이언트 프로토 패킷 수행 클래스
 * @author LinOffice
 */
public class ProtoPacketExecutor {
	private final GameClient client;
	private final ConcurrentHashMap<Integer, ProtoHandler> handlers;
	
	/**
	 * 프로토 패킷 업무 처리
	 * 코드별 업무처리 핸들러를 생성하여 수행한다.
	 * @param data
	 * @return ProtoHandler
	 * @throws Exception
	 */
	public ProtoHandler getPacket(byte[] data) throws Exception {
		int code = data[1] & 0xFF | data[2] << 8 & 0xFF00;
		ProtoHandler handler = handlers.get(code);
		if (handler == null) {
			if (Config.SERVER.PROTO_CLIENT_CODE_FIND) {
				print(code, data);
			}
			return null;
		}
		handler	= handler.copyInstance(data, client);// 업무를 수행할 인스턴스 생성
		handler.doWork();// 업무 처리
		return handler;
	}
	
	void print(int code, byte[] data){
		System.out.println(String.format(
				"[ProtoPacketExecutor] UNDEFINED_PROTO_CODE_FOUND : CODE(%d), LENGTH(%d)\n%s", 
				code, data.length, HexHelper.DataToPacket(data, data.length)));
	}
	
	/**
	 * 생성자
	 * 업무처리를 위한 객체를 생성하여 클라이언트에 할당한다.(비동기)
	 * @param client
	 */
	public ProtoPacketExecutor(GameClient client) {
		this.client		= client;
		this.handlers	= ProtoPacketLoader.createHandlers();
	}
	
	/**
	 * 메모리 해제
	 */
	public void dispose(){
		handlers.clear();
	}
}

