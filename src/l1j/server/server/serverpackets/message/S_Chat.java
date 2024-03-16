package l1j.server.server.serverpackets.message;

import l1j.server.Config;
import l1j.server.common.data.CHAT_RESULT;
import l1j.server.common.data.ChatType;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_Chat extends ServerBasePacket {
	private static final String S_CHAT = "[S] S_Chat";
	private byte[] _byte = null;
	public static final int CHAT	= 0x0203;
	
	public S_Chat(int transaction_id, ChatType type, byte[] message, byte[] target_user_name, byte[] link_message) {
		write_init();
		write_transaction_id(transaction_id);
		write_type(type);
		write_message(message);
		switch (type) {
		case CHAT_NORMAL:
			write_target_user_name(null);
			write_target_user_server_no(0);
			break;
		case CHAT_WHISPER:
			write_target_user_name(target_user_name);
			write_target_user_server_no(Config.VERSION.SERVER_NUMBER);
			break;
		default:
			break;
		}
		write_result(CHAT_RESULT.CHAT_RESULT_SUCCESS);
		if (link_message != null) {
			write_link_message(link_message);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHAT);
	}
	
	void write_transaction_id(int transaction_id) {
		writeRaw(0x08);// transaction_id
		writeBit(transaction_id);
	}
	
	void write_type(ChatType type) {
		writeRaw(0x10);// type
		writeRaw(type.toInt()); 
	}
	
	void write_message(byte[] message) {
		writeRaw(0x1a);// message
		writeBytesWithLength(message); 
	}
	
	void write_target_user_name(byte[] target_user_name) {
		writeRaw(0x22);// target_user_name
		writeBytesWithLength(target_user_name);
	}
	
	void write_target_user_server_no(int target_user_server_no) {
		writeRaw(0x28);// target server no
		writeBit(target_user_server_no);
	}
	
	void write_result(CHAT_RESULT result) {
		writeRaw(0x30);// result
		writeRaw(result.toInt());
	}
	
	void write_chat_ban_duration(int chat_ban_duration) {
		writeRaw(0x38);// chat_ban_duration
		writeBit(chat_ban_duration);
	}
	
	void write_link_message(byte[] link_message) {
		writeRaw(0x42);// link_message
		writeBytesWithLength(link_message); 
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_CHAT;
	}
}
