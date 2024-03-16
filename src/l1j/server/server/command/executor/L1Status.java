package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Alignment;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Status implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Status();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Status() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String char_name = st.nextToken();
			String param = st.nextToken();
			int value = Integer.parseInt(st.nextToken());

			L1PcInstance target = L1World.getInstance().getPlayer(char_name);

			// \f1%0은게임을 하고 있지 않습니다.
			if (target == null) {
				pc.sendPackets(new S_ServerMessage(73, char_name), true); 
				return false;
			}

			// -- not use DB --
			//if (param.equalsIgnoreCase("방어")) {
			if (param.equalsIgnoreCase("ac")) {
				target.getAC().addAc((byte) (value - target.getAC().getAc()));
			//} else if (param.equalsIgnoreCase("마방")) {
			} else if (param.equalsIgnoreCase("mr")) {
				target.getResistance().addMr((short) (value - target.getResistance().getMr()));
			//} else if (param.equalsIgnoreCase("공성")) {
			} else if (param.equalsIgnoreCase("hit")) {				
				target.getAbility().addShortHitup((short) (value - target.getAbility().getShortHitup()));
			//} else if (param.equalsIgnoreCase("대미지")) {
			} else if (param.equalsIgnoreCase("dmg")) {				
				target.getAbility().addShortDmgup((short) (value - target.getAbility().getShortDmgup()));
				// -- use DB --
			} else {
				//if (param.equalsIgnoreCase("피")) {
				if (param.equalsIgnoreCase("hp")) {
					target.addBaseMaxHp((short) (value - target.getBaseMaxHp()));
					target.setCurrentHp(target.getMaxHp());
				//} else if (param.equalsIgnoreCase("엠피")) {
				} else if (param.equalsIgnoreCase("mp")) {
					target.addBaseMaxMp((short) (value - target.getBaseMaxMp()));
					target.setCurrentMp(target.getMaxMp());
				//} else if (param.equalsIgnoreCase("성향")) {
				} else if (param.equalsIgnoreCase("align")) {
					target.setAlignment(value);
					target.broadcastPacketWithMe(new S_Alignment(target.getId(), target.getAlignment()), true);
				//} else if (param.equalsIgnoreCase("우호도")) {
				} else if (param.equalsIgnoreCase("karma")) {
					target.setKarma(value);
				//} else if (param.equalsIgnoreCase("지엠")) {
				} else if (param.equalsIgnoreCase("access")) {
					if (value == Config.ALT.GMCODE) {
						target.setAccessLevel((short) value);
//AUTO SRM: 						target.sendPackets(new S_SystemMessage("RESTART 하면 GM권한이 생깁니다."), true); // CHECKED OK
						target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(687), true), true);
					} else {
//AUTO SRM: 						target.sendPackets(new S_SystemMessage("GM번호가 일치하지 않습니다."), true); // CHECKED OK
						target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(688), true), true);
					}
				//} else if (param.equalsIgnoreCase("힘")) {
				} else if (param.equalsIgnoreCase("str")) {
					target.getAbility().setStr((byte) value);
				//} else if (param.equalsIgnoreCase("콘")) {
				} else if (param.equalsIgnoreCase("con")) {
					target.getAbility().setCon((byte) value);
				//} else if (param.equalsIgnoreCase("덱스")) {
				} else if (param.equalsIgnoreCase("dex")) {					
					target.getAbility().setDex((byte) value);
				//} else if (param.equalsIgnoreCase("인트")) {
				} else if (param.equalsIgnoreCase("int")) {					
					target.getAbility().setInt((byte) value);
				//} else if (param.equalsIgnoreCase("위즈")) {
				} else if (param.equalsIgnoreCase("wis")) {					
					target.getAbility().setWis((byte) value);
				//} else if (param.equalsIgnoreCase("카리")) {
				} else if (param.equalsIgnoreCase("cha")) {
					target.getAbility().setCha((byte) value);
				} else {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("스테이터스 " + param + " (은)는 불명합니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(689) + param  + S_SystemMessage.getRefText(690), true), true);
					return false;
				}
				target.save(); // DB에 캐릭터 정보를 기입한다
			}
			target.sendPackets(new S_OwnCharStatus(target), true);
			//pc.sendPackets(new S_SystemMessage(target.getName() + "의 " + param + "(을)를 " + value + "로 변경했습니다. "), true); // CHECKED OK
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(106), target.getName(), param, String.valueOf(value)), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명] [스텟] [변경치] 입력."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(693), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("피 엠피 성향 우호도 지엠 방어 마방 공성 대미지 힘 콘 덱스 인트 위즈 카리"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(694), true), true);
			return false;
		}
	}
}


