package l1j.server.server.model.Instance;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1ScarecrowInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	
	ArrayList<L1PcInstance> targetList	= new ArrayList<L1PcInstance>();
	ArrayList<Integer> hateList			= new ArrayList<Integer>();

	public L1ScarecrowInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		if (player == null) {
			return;
		}
		L1Attack attack = new L1Attack(player, this);
		if (attack.calcHit()) {
			try {
				int npcId = getNpcId();
				
				if (player.getLevel() < 5 && !player.noPlayerCK) {// 레벨5까지만 로봇제외
					targetList.add(player);
					hateList.add(1);
					player.getExpHandler().calcExp(this, targetList, hateList, player.isInParty() || player.isDead() ? 0 : getExp());// 경험치지급
					
					if (Config.ETC.SCARECROW_ADENA && npcId >= 45002 && npcId <= 45003) {
						player.getInventory().storeItem(L1ItemId.ADENA, Config.ETC.SCARECROW_ADENA_VALUE);
					}
					targetList.clear();
					hateList.clear();
				}

				if (npcId >= 44997 && npcId <= 44999) {
					//player.sendPackets(new S_SystemMessage(String.format("물리대미지: [%d]", attack.calcDamage())), true);

					player.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(90), String.valueOf(attack.calcDamage())), true);
				}
				actionHeading();
			} catch (Exception e) {}
		}
		attack.action();
		attack = null;
	}
	
	public void actionHeading(){
		getMoveState().setHeading(getMoveState().getHeading() < 7 ? getMoveState().getHeading() + 1 : 0);
		broadcastPacket(new S_ChangeHeading(this), true);
	}

	@Override
	public void onTalkAction(L1PcInstance l1pcinstance) {}
	public void onFinalAction() {}
	public void doFinalAction() {}
}


