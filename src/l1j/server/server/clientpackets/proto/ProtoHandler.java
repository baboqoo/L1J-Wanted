package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 프로토 패킷 업무 처리 추상클래스
 * ClientBasePacket 상속
 * 코드별로 업무를 분리하여 구현하십시오.
 * @author LinOffice
 */
public abstract class ProtoHandler extends ClientBasePacket {
	protected GameClient _client;
	protected L1PcInstance _pc;
	protected int _total_length;
	protected int _limit;
	
	/**
	 * 클라이언트에 최초 할당할 생성자
	 */
	protected ProtoHandler(){}
	
	/**
	 * 프로토 클래스 부모 생성자
	 * stream 부모 클래스(ClientBasePacket)에 데이터 bind(0번 옵코드, 1~2번 구분 코드, 3~4번 패킷 사용 길이 : 배열의 5번째부터 읽는다.)
	 * @param data	- 배열의 길이가 5이상은 읽을 패킷 존재
	 * @param client
	 */
	protected ProtoHandler(byte[] data, GameClient client) {
		super(data, 5);
		_client			= client;
		_pc				= client.getActiveChar();
		if (data.length > 5) {
			_total_length	= data[3] & 0xFF | data[4] << 8 & 0xFF00;
			_limit			= _total_length + 5;
		}
	}
	
	/**
	 * 패킷의 마지막 여부
	 * @return boolean
	 */
	protected boolean isEnd() {
		return getOffset() >= _limit;
	}
	
	/**
	 * 업무 처리
	 * @throws Exception
	 */
	protected abstract void doWork() throws Exception;
	
	/**
	 * 업무를 수행할 인스턴스 생성
	 * @param data
	 * @param client
	 * @return ProtoHandler
	 */
	protected abstract ProtoHandler copyInstance(byte[] data, GameClient client);
	
}

