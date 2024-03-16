package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;

public class A_CharBaseStatus extends ProtoHandler {
	private int _str;
	private int _int;
	private int _wis;
	private int _dex;
	private int _con;
	private int _cha;
	
	protected A_CharBaseStatus(){}
	private A_CharBaseStatus(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private void parse() {
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_str = readC();
				break;
			case 0x10:
				_int = readC();
				break;
			case 0x18:
				_wis = readC();
				break;
			case 0x20:
				_dex = readC();
				break;
			case 0x28:
				_con = readC();
				break;
			case 0x30:
				_cha = readC();
				break;
			default:
				return;
			}
		}
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		if (_pc.isGm()) {
			System.out.println(String.format("[A_CharBaseStatus] INFO : %s", toString()));
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CharBaseStatus(data, client);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("str : ").append(_str);
		sb.append(", int : ").append(_int);
		sb.append(", wis : ").append(_wis);
		sb.append(", dex : ").append(_dex);
		sb.append(", con : ").append(_con);
		sb.append(", cha : ").append(_cha);
		return sb.toString();
	}

}

