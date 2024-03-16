package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.utils.StringUtil;

public class A_ClientOptionChangeNoti extends ProtoHandler {
	protected A_ClientOptionChangeNoti(){}
	private A_ClientOptionChangeNoti(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private SoundOption _sound_option;
	
	void paresPacket(){
		if (_total_length < 2) {
			return;
		}
		while(!isEnd()){
			switch(readC()){
			case 0x0a:
				readP(1);
				_sound_option = _client.get_sound_option();
				if (_sound_option == null) {
					_sound_option = new SoundOption();
					_client.set_sound_option(_sound_option);
				}
				while(!isEnd()){
					switch (readC()) {
					case 0x08:
						_sound_option._mute = readBool();
						break;
					case 0x10:
						_sound_option._background = readC();
						break;
					case 0x18:
						_sound_option._effect = readC();
						break;
					case 0x20:
						_sound_option._ambient = readC();
						break;
					case 0x28:
						_sound_option._voice = readC();
						break;
					case 0x30:
						_sound_option._system = readC();
						break;
					default:
						return;
					}
				}
				break;
			default:
				return;
			}
		}
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null) {
			return;
		}
		paresPacket();
	}
	
	public static class SoundOption {
		boolean _mute;
		int _background;
		int _effect;
		int _ambient;
		int _voice;
		int _system;
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("mute: ").append(_mute).append(StringUtil.LineString);
			sb.append("background: ").append(_background).append(StringUtil.LineString);
			sb.append("effect: ").append(_effect).append(StringUtil.LineString);
			sb.append("ambient: ").append(_ambient).append(StringUtil.LineString);
			sb.append("voice: ").append(_voice).append(StringUtil.LineString);
			sb.append("system: ").append(_system);
			return sb.toString();
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ClientOptionChangeNoti(data, client);
	}

}

