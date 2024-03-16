package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.serverpackets.S_ServerVersion;
import l1j.server.server.serverpackets.message.S_CommonNews;
import l1j.server.server.utils.DelayClose;
import l1j.server.server.utils.HexHelper;

public class A_ClientVersion extends ProtoHandler {
	protected A_ClientVersion(){}
	private A_ClientVersion(byte[] data, GameClient client) {
		super(data, client);
		parse();
	}
	
	private int _protocol_version;
	private int _active_code_page;
	private int _check_seed;
	private long _client_version;
	private int _client_instance_count;
	private byte[] _check_resource_seed;
	private boolean _is_streaming;
	private int _client_specific_value;
	
	boolean isValidation(){
		return _protocol_version > 0 && _active_code_page > 0 && _check_seed != 0 && _client_instance_count > 0;
	}
	
	void parse(){
		if (_total_length < 2) {
			return;
		}
		while(!isEnd()){
			int tag = readC();
			switch(tag) {
			case 0x08:
				_protocol_version			= read4(read_size());
				break;
			case 0x10:
				_active_code_page			= read4(read_size());
				break;
			case 0x18:
				_check_seed					= read4(read_size());
				break;
			case 0x20:
				_client_version				= readLong();
				break;
			case 0x28:
				_client_instance_count		= readC();
				break;
			case 0x32:
				int resourceLength			= readC();
				if (resourceLength > 0) {
					if (!isRead(resourceLength)) {
						break;
					}
					_check_resource_seed	= readByte(resourceLength);
				}
				break;
			case 0x38:
				_is_streaming				= readBool();
				break;
			case 0x40:
				_client_specific_value		= read4(read_size());
				break;
			default:
				return;
			}
		}
	}

	@Override
	protected void doWork() throws Exception {
		if (!isValidation()) {
			//System.out.println(String.format("[A_ClientVersion] 비정상적인 클라이언트 접속  : IP(%s)\r\nINFO ->\r\n%s", _client.getIp(), toString()));
			System.out.println(String.format("[A_ClientVersion] Abnormal client connection: IP(%s)\r\nINFO ->\r\n%s", _client.getIp(), toString()));
			_client.close();
			return;
		}
		boolean is_local = _client.getIp().equalsIgnoreCase(Config.SERVER.LOGIN_SERVER_ADDRESS) || _client.getIp().equalsIgnoreCase(Config.SERVER.LOCALHOST);
		if (_client_version != Config.VERSION.CLIENT_VERSION && !is_local) {
			_client.sendPacket(S_CommonNews.OTHER_CLIENT_VERSION);
			GeneralThreadPool.getInstance().schedule(new DelayClose(_client), 500L);
			return;
		}
		if (_client_instance_count > Config.SERVER.ALLOW_2PC_IP_COUNT) {// 클라이언트 접속 카운트 제한 IP기준
			_client.sendPacket(S_CommonNews.CLIENT_COUNT_MAX);
			GeneralThreadPool.getInstance().schedule(new DelayClose(_client), 500L);
			return;
		}
		if (_client_instance_count > Config.SERVER.ALLOW_2PC_HDD_COUNT) {// 클라이언트 접속 카운트 제한 HDD기준
			_client.sendPacket(S_CommonNews.CLIENT_COUNT_MAX);
			GeneralThreadPool.getInstance().schedule(new DelayClose(_client), 500L);
			return;
		}
		
		_client.setVersion(_client_version);
		S_ServerVersion version = new S_ServerVersion();
		_client.sendPacket(version);
		version.clear();
		version = null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("protocalVersion: ").append(_protocol_version);
		sb.append("\r\nactiveCodePage: ").append(_active_code_page);
		sb.append("\r\ncheckSeed: ").append(_check_seed);
		sb.append("\r\nclientVersion: ").append(_client_version);
		sb.append("\r\nclientCount: ").append(_client_instance_count);
		if (_check_resource_seed != null) {
			sb.append("\r\ncheckResourceSeed:\r\n").append(HexHelper.DataToPacket(_check_resource_seed, _check_resource_seed.length));
		}
		sb.append("\r\nisStreaming: ").append(_is_streaming);
		sb.append("\r\nclientSpecificValue: ").append(_client_specific_value);
		return sb.toString();
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ClientVersion(data, client);
	}

}

