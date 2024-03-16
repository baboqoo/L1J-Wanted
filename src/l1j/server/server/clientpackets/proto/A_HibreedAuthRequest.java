package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.inter.L1InterServerFactory;
import l1j.server.GameSystem.inter.L1InterServerModel;
import l1j.server.GameSystem.inter.L1LoginInterceptor;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.utils.StringUtil;

public class A_HibreedAuthRequest extends ProtoHandler {
	protected A_HibreedAuthRequest(){}
	private A_HibreedAuthRequest(byte[] data, GameClient client) {
		super(data, client);
		parse();
	}
	
	private int _reserverNumber;
	private String _token;
	
	boolean validation(){
		return _reserverNumber > 0 && !StringUtil.isNullOrEmpty(_token);
	}
	
	void parse(){
		if (_total_length <= 4) {
			return;
		}
		readP(1);// 0x08
		_reserverNumber	= readBit();
		
		readP(1);// 0x12
		int tokenLength = readC();
		if (tokenLength < 1) {
			return;
		}
		_token			= readS(tokenLength);
	}

	@Override
	protected void doWork() throws Exception {
		try {
			// 유효성 체크
			if (!validation()) {
				if (_client != null) {
					//System.out.println(String.format("[A_HibreedAuthRequest] 유효성 검사를 통과하지 못한 요청. IP(%s)", _client.getIp()));
					System.out.println(String.format("[A_HibreedAuthRequest] Request failed validation. IP(%s)", _client.getIp()));
					_client.close();
				}
				return;
			}
			
			// 클라이언트 체크
			if (_client == null) {
				//System.out.println(String.format("[A_HibreedAuthRequest] 클라이언트가 없는 유저가 요청을 시도하였습니다. TOKEN(%s)", _token));
				System.out.println(String.format("[A_HibreedAuthRequest] A user without a client attempted a request. TOKEN(%s)", _token));
				L1InterServerFactory.remove(_token);
				return;
			}
			
			// 인터서버 데이터를 가져온다.
			final L1InterServerModel model	= L1InterServerFactory.get(_token);
			if (model == null) {
				//System.out.println(String.format("[A_HibreedAuthRequest] 인터서버 데이터를 가져오지 못하였습니다. IP(%s), TOKEN(%s)", _client.getIp(), _token));
				System.out.println(String.format("[A_HibreedAuthRequest] Failed to get interserver data. IP(%s), TOKEN(%s)", _client.getIp(), _token));
				_client.close();
				return;
			}
			
			// 이동 전 캐릭터
			if (model.getOldPc() != null && model.getOldPc().getNetConnection() != null) {
				model.getOldPc().getNetConnection().close();
			}
			
			GeneralThreadPool.getInstance().schedule(new L1LoginInterceptor(_client, model), 500L);
		} catch (Exception e) {
			//System.out.println(String.format("[A_HibreedAuthRequest] 비정상적인 로그인 예외 발생 . IP(%s), TOKEN(%s) 로그인을 절단 하였습니다.", _client.getIp(), _token));
			System.out.println(String.format("[A_HibreedAuthRequest] Abnormal login exception occurred. IP(%s), TOKEN(%s) login was cut off.", _client.getIp(), _token));
			e.printStackTrace();
			_client.close();
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_HibreedAuthRequest(data, client);
	}

}

