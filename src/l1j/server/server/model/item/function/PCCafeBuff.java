package l1j.server.server.model.item.function;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldLoader;
import l1j.server.server.Account;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.attendance.S_AttenDanceInfoNoti;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.gamegate.S_GameGatePCCafeCharge;
import l1j.server.server.serverpackets.gamegate.S_UserStartSundry;
import l1j.server.server.serverpackets.system.S_PCMasterFavorUpdateNoti;
import l1j.server.server.templates.L1Item;

public class PCCafeBuff extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public PCCafeBuff(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			buff((L1PcInstance) cha, this.getItem().getEtcValue());
		}
	}
	
	private void buff(L1PcInstance pc, int day) {
		try {
			if (pc.isPCCafe()) {
				pc.sendPackets(L1SystemMessage.PCCAFE_BUFF_RE_USE);
				return;
			}
			
			pc.setPCCafe(true);
			
//AUTO SRM: 			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format("[PC방 이용 시간] %d일 동안 PC방 혜택이 적용 됩니다.", day)), true); // CHECKED OK
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,String.format(S_SystemMessage.getRefText(1061) + "%d " + S_SystemMessage.getRefText(1088), day)), true);
			long deleteTime = System.currentTimeMillis() + (86400000 * (long) day) + 10000;
			Account account = pc.getAccount();
			if (account.getBuff_PCCafe() != null) {
				account.getBuff_PCCafe().setTime(deleteTime);
			} else {
				account.setBuff_PCCafe(new Timestamp(deleteTime));
			}
			account.updatePCCafeBuff();
			
			if (Config.ATTEND.ATTENDANCE_ACTIVE && Config.ATTEND.ATTENDANCE_PCROOM_USE) {
				pc.sendPackets(new S_AttenDanceInfoNoti(pc, account.getAttendance(), 0), true);
			}
			
			if (pc._isDragonFavor) {
				account.getEinhasad().setDragonFavor(pc, false);
			}
			if (!pc._isDragonFavorPCCafe) {
				account.getEinhasad().setDragonFavorPCCafe(pc, true);
			}

			pc.sendPackets(new S_RestExpInfoNoti(pc), true);
			pc.sendPackets(new S_ExpBoostingInfo(pc), true);
			
			// 불멸의 가호(버프) 로드
			FreeBuffShieldHandler handler	= pc.getConfig().get_free_buff_shield();
			if (handler == null) {
				handler = FreeBuffShieldLoader.getInstance().load(pc);// 로드 시 캐릭터 컨피그에 등록된다.
			}
			
			pc.sendPackets(new S_PCMasterFavorUpdateNoti(handler), true);
			pc.sendPackets(new S_UserStartSundry(true), true);
			pc.sendPackets(S_GameGatePCCafeCharge.START);
			pc.getInventory().removeItem(this, 1);
			
			handler.enable_golden_buff_info_map();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


