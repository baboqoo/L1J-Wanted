package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.arca.L1Arca;
import l1j.server.GameSystem.arca.L1ArcaActivation;
import l1j.server.server.Account;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.SQLUtil;

public class S_ArcaActivationInfo extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_ARCA_ACTIVATION_INFO = "S_ArcaActivationInfo";
	public static final int INFO	= 0x01cd;// 계정 탐 정보창
	private static final String USER_LIST_QUERY = "SELECT objid, level, char_name, Type, gender FROM characters WHERE account_name=? ORDER BY `EXP` DESC";
	
	public S_ArcaActivationInfo(Account account, int cancelation_delay) {
		write_init();
		write_activate_user_list(account);
		write_max_activate_characters(Config.ALT.ARCA_MAX_ACTIVATE_CHARACTERS);
		write_buff_spell_id(0);
		write_cancelation_delay(cancelation_delay);
		writeH(0x00);		
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_activate_user_list(Account account) {
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		
		int user_level, user_class, user_gender, user_uid, activation_remain_count;
		byte[] user_name			= null;
		Timestamp end_time			= null;
		long activation_remain_time	= 0;
		long current_time			= System.currentTimeMillis();
		
		L1Arca arca					= account.getArca();
		L1ArcaActivation activation	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(USER_LIST_QUERY);
			pstm.setString(1, account.getName());
			rs		= pstm.executeQuery();
			while(rs.next()){
				activation_remain_count = 0;
				activation_remain_time	= 0L;
				end_time				= null;
				
				user_uid		= rs.getInt("objid");
				user_level		= rs.getInt("level");
				user_class		= rs.getInt("Type");
				user_gender		= rs.getInt("gender");
				user_name		= rs.getString("char_name").getBytes();
				
				activation				= arca.getActivations().get(user_uid);
				if (activation != null) {
					end_time				= activation.getEndTime();
					activation_remain_count	= activation.getRemain().isEmpty() ? 0 : activation.getRemain().size();
				}

				if (end_time != null && current_time < end_time.getTime()) {
					activation_remain_time = end_time.getTime() - current_time;
				}

				UserInfo os = new UserInfo();
				os.write_server_no(Config.VERSION.SERVER_NUMBER);
				os.write_user_uid(user_uid);
				os.write_activation_remain_time(activation_remain_time == 0 ? 0 : activation_remain_time / 1000);
				os.write_activation_remain_count(activation_remain_count);
				os.write_user_name(user_name);
				os.write_user_level(user_level);
				os.write_user_class(user_class);
				os.write_user_gender(user_gender);

				writeRaw(0x0a);
				writeBytesWithLength(os.getBytes());
				os.close();
				os = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	void write_max_activate_characters(int max_activate_characters) {
		writeRaw(0x10);// max_activate_characters
		writeRaw(max_activate_characters);
	}
	
	void write_buff_spell_id(int buff_spell_id) {
		writeRaw(0x18);// buff_spell_id
		writeRaw(buff_spell_id);
	}
	
	void write_cancelation_delay(int cancelation_delay) {
		writeRaw(0x20);// cancelation_delay
		writeBit(cancelation_delay);
	}
	
	public static class UserInfo extends BinaryOutputStream {
		public UserInfo() {
			super();
		}
		
		void write_server_no(int server_no) {
			writeC(0x08);
			writeBit(server_no);
		}
		
		void write_user_uid(int user_uid) {
			writeC(0x10);// user_uid
			writeBit(user_uid);
		}
		
		void write_activation_remain_time(long activation_remain_time) {
			writeC(0x18);// activation_remain_time
			writeBit(activation_remain_time);
		}
		
		void write_activation_remain_count(int activation_remain_count) {
			writeC(0x20);// activation_remain_count
			writeC(activation_remain_count);
		}
		
		void write_user_name(byte[] user_name) {
			writeC(0x2a);// user_name
			writeBytesWithLength(user_name);
		}
		
		void write_user_level(int user_level) {
			writeC(0x30);// user_level
			writeC(user_level);
		}
		
		void write_user_class(int user_class) {
			writeC(0x38);// user_class
			writeC(user_class);
		}
		
		void write_user_gender(int user_gender) {
			writeC(0x40);// user_gender
			writeC(user_gender);
		}
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_ARCA_ACTIVATION_INFO;
	}
}
