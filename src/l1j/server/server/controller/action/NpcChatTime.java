package l1j.server.server.controller.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.server.datatables.NpcChatTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1NpcChat;

/**
 * 엔피씨 채팅 컨트롤러
 * @author LinOffice
 */
public class NpcChatTime implements ControllerInterface {
	private static class newInstance {
		public static final NpcChatTime INSTANCE = new NpcChatTime();
	}
	public static NpcChatTime getInstance() {
		return newInstance.INSTANCE;
	}
	private NpcChatTime(){}

	@Override
	public void execute() {
		for (L1NpcChat npcChat : NpcChatTable.getInstance().getAllGameTime()) {
			if (isChatTime(npcChat.getGameTime())) {
				for (L1NpcInstance npc : L1World.getInstance().getAllNpcChat()) {
					if (npc.getNpcTemplate().getNpcId() != npcChat.getNpcId()) {
						continue;
					}
					npc.startChat(L1NpcInstance.CHAT_TIMING_GAME_TIME);
				}
			}
		}
	}
	
	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
	
	private boolean isChatTime(int chatTime) {
		int nowTime = Integer.valueOf(sdf.format(getRealTime().getTime()));
		return (nowTime == chatTime);
	}

	private static Calendar getRealTime() {
		return Calendar.getInstance(TimeZone.getTimeZone(Config.SERVER.TIME_ZONE));
	}

}

