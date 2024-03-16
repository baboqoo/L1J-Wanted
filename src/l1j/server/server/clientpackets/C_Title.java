package l1j.server.server.clientpackets;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class C_Title extends ClientBasePacket {
	private static final String C_TITLE = "[C] C_Title";
	private static Logger _log = Logger.getLogger(C_Title.class.getName());

	private L1PcInstance pc;
	private L1PcInstance target;
	
	private String target_name;
	private String title;
	
	public C_Title(byte abyte0[], GameClient client) {
		super(abyte0);
		pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		target_name	= readS();
		title		= readS();
		if (target_name.isEmpty() || title.isEmpty() || title.length() > 16) {
			pc.sendPackets(L1ServerMessage.sm196);// 사용법: /호칭 [캐릭터명][원하는 호칭]
			return;
		}

		target = L1World.getInstance().getPlayer(target_name);
		if (target == null) {
			return;
		}
		
		// 관리자
		if (pc.isGm()) {
			changeTitle(target, title);
			return;
		}
		
		L1Clan pledge = pc.getClan();
		if (pledge != null) {// 혈맹에 소속중
			if (pc.getClan() == target.getClan()) {// 대상관 같은 혈맹
				if (eBloodPledgeRankType.isAuthRankAtKnight(pc.getBloodPledgeRank())) {// 호칭을 부여할 수 있는 계급
					// 자기 자신
					if (pc.getId() == target.getId()) {
						if (pc.getLevel() < 10) {
							pc.sendPackets(L1ServerMessage.sm197);// 호칭: 부여 불가(혈맹원이 10레벨 미만)
							return;
						}
						changeTitle(pc, title);
						send_noti_pledge_member(pledge);
						return;
					}
					
					if (target.getLevel() < 10) {
						pc.sendPackets(new S_ServerMessage(202, target_name), true);// \f1%0의 레벨이 10 미만이므로 호칭을 줄 수 없습니다.
						return;
					}
					
					changeTitle(target, title);
					send_noti_pledge_member(pledge);
					return;
				}
				// 혈맹에 가입한 유저가 스스로 호칭을 부여할 수 있는지 검증
				if (!Config.PLEDGE.PLEDGE_CHANGE_TITLE_BY_SELF) {
					pc.sendPackets(L1ServerMessage.sm198);// \f1혈맹원에게 호칭이 주어지는 것은 프린스와 프린세스 뿐입니다.
					return;
				}
				changeTitle(pc, title);
				return;
			}
			pc.sendPackets(new S_ServerMessage(201, pc.getClanName()), true);// \f1%0은 당신의 혈맹이 아닙니다.
			return;
		}
		
		// 혈맹이 없음
		if (pc != target) {// 다른 대상
			return;
		}
		
		// 호칭을 부여할 수 있는 레벨
		if (target.getLevel() < 40) {
			pc.sendPackets(L1ServerMessage.sm200);// 호칭: 부여 불가(40레벨 미만)
			return;
		}
		changeTitle(pc, title);
	}

	/**
	 * 호칭을 부여한다.
	 * @param pc
	 * @param title
	 */
	void changeTitle(L1PcInstance pc, String title) {
		pc.setTitle(title);
		pc.broadcastPacketWithMe(new S_CharTitle(pc.getId(), title), true);
		try {
			pc.save();// DB에 캐릭터 정보를 써 우
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 혈맹원들에게 호칭 변경을 알린다.
	 * @param pledge
	 */
	void send_noti_pledge_member(L1Clan pledge) {
		if (pledge == null) {
			return;
		}
		S_ServerMessage message = new S_ServerMessage(203, pc.getName(), target_name, title);// \f1%0이%1에 「%2라고 하는 호칭을 주었습니다.
		for (L1PcInstance member : pledge.getOnlineClanMember()) {
			member.sendPackets(message);
		}
		message.clear();
		message = null;
	}

	@Override
	public String getType() {
		return C_TITLE;
	}

}
