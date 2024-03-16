package l1j.server.server.controller.action;

import l1j.server.common.data.ChatType;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ChatMessageNoti;
import l1j.server.server.utils.CommonUtil;

/**
 * 자동 형맹 가입 멘트 컨트롤러
 * @author LinOffice
 */
public class AutoClanJoinMent implements ControllerInterface {
	private static class newInstance {
		public static final AutoClanJoinMent INSTANCE = new AutoClanJoinMent();
	}
	public static AutoClanJoinMent getInstance(){
		return newInstance.INSTANCE;
	}
	private AutoClanJoinMent(){}

	@Override
	public void execute() {
	}

	@Override
	public void execute(L1PcInstance pc) {
		autoClanJoinMent(pc);
	}
	
	private void autoClanJoinMent(L1PcInstance pc) {
		if (!pc.isAutoClanjoin()) {
			return;
		}
		try {
			if (++pc._isAutoClanMentCount >= 60) {
				pc._isAutoClanMentCount = 0;
				if (CommonUtil.random(5) == 1 && L1World.getInstance().isWorldChatElabled()) {
					//S_ChatMessageNoti chat = new S_ChatMessageNoti(pc, ChatType.CHAT_WORLD, String.format("%s혈맹에서 혈원 모집! \\f3/혈맹가입 %s", pc.getClanName(), pc.getClanName()).getBytes(), null, 0);
					S_ChatMessageNoti chat = new S_ChatMessageNoti(pc, ChatType.CHAT_WORLD, String.format("%s pledge is recruiting members! \f3/Apply for pledge %s", pc.getClanName(), pc.getClanName()).getBytes(), null, 0);
					for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
						L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList.contains(0, pc.getName())) {
							listner.sendPackets(chat);// 전체 채팅
						}
					}
					chat.clear();
					chat = null;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}

