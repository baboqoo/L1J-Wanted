package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.utils.FormatterUtil;

public class A_ClientFunctionLog extends ProtoHandler {
	protected A_ClientFunctionLog(){}
	private A_ClientFunctionLog(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private A_ClientFunctionLog.FuncID _fid;
	
	private void parse(){
		if (_total_length < 2) {
			return;
		}
		while(!isEnd()){
			int tag = readC();
			switch(tag){
			case 0x08:
				_fid = A_ClientFunctionLog.FuncID.fromInt(readC());
				break;
			default:
				return;
			}
		}
	}
	
	private boolean validation(){
		return _fid != null;
	}

	@Override
	protected void doWork() throws Exception {
		if (_client == null) {
			return;
		}
		parse();
		if (!validation()) {
			return;
		}
		if (Config.SERVER.CLIENT_FUNCTION_LOG_PRINT) {
			System.out.println(String.format(
					"[CLIENT_FUCTION_LOG] [%s] %s : ACCOUNT(%s), IP(%s)", 
					FormatterUtil.get_formatter_time(),
					_fid.name(), 
					_client.getAccount() != null ? _client.getAccountName() : "UNDEFINED", 
					_client.getIp()));
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ClientFunctionLog(data, client);
	}

	public enum FuncID{
		LOGIN_PROCESS(1),
		OK_CHARACTER_PW(2),
		TO_WINDOW_MODE(3),
		OK_INGAME_PW(4),
		GET_MAP_NUM(5),
		;
		private int value;
		FuncID(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(FuncID v){
			return value == v.value;
		}
		public static FuncID fromInt(int i){
			switch(i){
			case 1:
				return LOGIN_PROCESS;
			case 2:
				return OK_CHARACTER_PW;
			case 3:
				return TO_WINDOW_MODE;
			case 4:
				return OK_INGAME_PW;
			case 5:
				return GET_MAP_NUM;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments FuncID, %d", i));
			}
		}
	}
}

