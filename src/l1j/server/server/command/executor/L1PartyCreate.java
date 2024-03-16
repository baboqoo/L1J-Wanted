package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1PartyCreate implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1PartyCreate();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1PartyCreate() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String cmd = tok.nextToken();
			//if (cmd.equals("주변")) {
			if (cmd.equals("nearby")) {
				L1Party party = new L1Party();
				if (pc.getParty() == null)
					party.addMember(pc);
				else
					party = pc.getParty();
				int range = 3;// 현재주변3칸
				for (L1PcInstance Targetpc : L1World.getInstance().getVisiblePlayer(pc, range)) {
					if (pc.getName().equals(Targetpc.getName()))
						continue;
					if (Targetpc.getParty() != null)
						continue;
					// 파티있는유저제외
					if (Targetpc.isPrivateShop()||Targetpc.isAutoClanjoin())
						continue;
					// 무인제외
					party.addMember(Targetpc);
					//pc.sendPackets(new S_SystemMessage(Targetpc.getName() + "님을 내파티에 참가시켰습니다."), true); // CHECKED OK				
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(122), Targetpc.getName()), true);
				}
				//pc.sendPackets(new S_SystemMessage(range + "칸 안의 유저를 내파티에 참가시켰습니다."), true); // CHECKED OK
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(120), String.valueOf(range)), true);
				return true;
			}
			//if (cmd.equals("화면")) {
			if (cmd.equals("screen")) {
				L1Party party = new L1Party();
				if (pc.getParty() == null)
					party.addMember(pc);
				else
					party = pc.getParty();
				for (L1PcInstance Targetpc : L1World.getInstance().getVisiblePlayer(pc)) {
					if (pc.getName().equals(Targetpc.getName()))
						continue;
					if (Targetpc.getParty() != null)
						continue;
					if (Targetpc.isPrivateShop()||Targetpc.isAutoClanjoin())
						continue;
					party.addMember(Targetpc);
					//pc.sendPackets(new S_SystemMessage(Targetpc.getName() + "님을 내파티에 참가시켰습니다."), true); // CHECKED OK
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(122), Targetpc.getName()), true);
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("화면안의 유저를 내파티에 참가시켰습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(513), true), true);
				return true;
			}
			//if (cmd.equals("전체")) {
			if (cmd.equals("all")) {
				L1Party party = new L1Party();
				if (pc.getParty() == null)
					party.addMember(pc);
				else
					party = pc.getParty();
				int range = 3;// 현재주변3칸
				for (L1PcInstance Targetpc : L1World.getInstance().getAllPlayers()) {
					if (pc.getName().equals(Targetpc.getName()))
						continue;
					if (Targetpc.getParty() != null)
						continue;
					if (Targetpc.isPrivateShop()||Targetpc.isAutoClanjoin())
						continue;
					party.addMember(Targetpc);
					//pc.sendPackets(new S_SystemMessage(Targetpc.getName() + "님을 내파티에 참가시켰습니다."), true); // CHECKED OK
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(122), Targetpc.getName()), true);
				}
				//pc.sendPackets(new S_SystemMessage(range + "칸 안의 유저를 내파티에 참가시켰습니다."), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(120), String.valueOf(range)), true);
				return true;
			}
			//if (cmd.equals("참가")) {
			if (cmd.equals("join")) {
				String TargetpcName = tok.nextToken();
				L1PcInstance TargetPc = L1World.getInstance().getPlayer(TargetpcName);
				if (TargetPc.getParty() != null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(TargetPc.getName() + "님은 파티가 없습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(TargetPc.getName()  + S_SystemMessage.getRefText(514), true), true);
				} else {
					TargetPc.getParty().addMember(pc);
					//pc.sendPackets(new S_SystemMessage(TargetPc.getName() + "님의 파티에 참가했습니다."), true); // CHECKED OK
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(121), TargetPc.getName()), true);
				}
				return true;
			//} else if (cmd.equals("초대")) {
			} else if (cmd.equals("invite")) {
				String TargetpcName = tok.nextToken();
				L1PcInstance TargetPc = L1World.getInstance().getPlayer(TargetpcName);
				L1Party party = new L1Party();
				if (pc.getParty() == null) {
					party.addMember(pc);
				} else {
					party = pc.getParty();
				}
				if (TargetPc.getParty() != null) {
					TargetPc.getParty().kickMember(TargetPc);
				}
				party.addMember(TargetPc);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(TargetPc.getName() + "님을 내파티에 강제참가시켰습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(TargetPc.getName()  + S_SystemMessage.getRefText(516), true), true);
				return true;
			//} else if (cmd.equals("강제초대")) {
			} else if (cmd.equals("forceinvite")) {
				L1Party party = new L1Party();
				if (pc.getParty() == null)
					party.addMember(pc);
				else
					party = pc.getParty();
				for (L1PcInstance Targetpc : L1World.getInstance().getAllPlayers()) {
					if (pc.getName().equals(Targetpc.getName()))
						continue;
					if (Targetpc.isPrivateShop()||Targetpc.isAutoClanjoin())
						continue;
					if (Targetpc.getParty() != null)
						Targetpc.getParty().kickMember(Targetpc);
					party.addMember(Targetpc);
					//pc.sendPackets(new S_SystemMessage(Targetpc.getName() + "님을 내파티에 참가시켰습니다."), true); // CHECKED OK
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(122), Targetpc.getName()), true);
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("접속중인 유저를 내파티에 강제참가시켰습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(517), true), true);
				return true;
			//} else if (cmd.equals("파장")) {
			} else if (cmd.equals("removeinvitation")) {
				if (pc.getParty() == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("참가중인파티가없습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(518), true), true);
				} else {
					pc.getParty().passLeader(pc);
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("파장을 뺐었습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(519), true), true);
				}
				return true;
			}
			tok = null;
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [주변,화면,전체,참가 (유저이름)]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(520), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [초대 (유저이름),강제초대,파장]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(521), true), true);
			return false;
		}
	}
}


