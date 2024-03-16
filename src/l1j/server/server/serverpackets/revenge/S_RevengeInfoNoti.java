package l1j.server.server.serverpackets.revenge;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_RevengeInfoNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_REVENGE_INFO_NOTI = "[S] S_RevengeInfoNoti";
	public static final int INFO_NOTI	= 0x041d;
	
	public S_RevengeInfoNoti(String name) {
		L1PcInstance target		= L1World.getInstance().getPlayer(name);
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO_NOTI);

		RevengeInfoStream os = null;
		try {
			os = new RevengeInfoStream();
			int startTime = (int) (System.currentTimeMillis() / 1000);
			os.write_register_timestamp(startTime);
			os.write_unregister_duration(Config.REVENGE.REVENGE_DURATION_SECOND);
			os.write_action_type(S_RevengeInfo.eAction.TAUNT);
			os.write_action_result(S_RevengeInfo.eResult.NONE);
			os.write_action_timestamp(0);
			os.write_action_duration(0);
			os.write_action_remain_count(Config.REVENGE.REVENGE_TAUNT_MAX_COUNT);
			os.write_action_count(1);
			os.write_crimescene_server_no(Config.VERSION.SERVER_NUMBER);
			os.write_user_uid(target.getId());
			os.write_server_no(Config.VERSION.SERVER_NUMBER);
			os.write_game_class(target.getType());
			os.write_user_name(name);
			os.write_pledge_id(target.getClanid());
			os.write_pledge_name(target.getClanName());
			os.write_active(target != null);
			os.write_activate_duration(Config.REVENGE.REVENGE_DURATION_SECOND);
			os.write_anonymity_name(target != null && target.getConfig().getAnonymityType() != null);
			
			writeC(0x0a);
			writeBytesWithLength(os.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_REVENGE_INFO_NOTI;
	}
}
