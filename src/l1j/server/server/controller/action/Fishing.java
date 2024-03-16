package l1j.server.server.controller.action;

import java.util.Random;

import l1j.server.Config;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Einhasad;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.item.L1Fishing;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_FishingNoti;
import l1j.server.server.serverpackets.S_FishingNoti.FISHING_ROD;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

/**
 * 낚시 컨트롤러
 * @author LinOffice
 */
public class Fishing implements ControllerInterface {
	private static final Random random = new Random(System.nanoTime());
	
	private static class newInstance {
		public static final Fishing INSTANCE = new Fishing();
	}
	public static Fishing getInstance() {
		return newInstance.INSTANCE;
	}
	private Fishing(){}

	@Override
	public void execute() {
	}
	
	@Override
	public void execute(L1PcInstance pc) {
		try {
			fishing(pc);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 낚시 프로세스
	 * @param pc
	 */
	void fishing(L1PcInstance pc) {
		if (!pc.isFishing() || (pc.getMapId() != 4 && pc.getMapId() != 5490)) {
			return;
		}
		long currentTime	= System.currentTimeMillis();	
		if (currentTime > (pc.getFishingTime() + 1000)) {
			if (pc._fishingRod == null) {
				endFishing(pc);
				return;
			}
			L1Fishing fish		= L1Fishing.fromRod(pc._fishingRod.getItemId());
			if (fish == null || (fish.isBait() && !pc.getInventory().consumeItem(41295, 1))) {
				endFishing(pc);
				return;
			}
			if (pc._fishingRod.getChargeCount() <= 0) {
				pc.getInventory().removeItem(pc._fishingRod, 1);
				pc._fishingRod = pc.getInventory().storeItem(41293, 1);// 일반 낚싯대
				endFishing(pc);
				return;
			}
			
			if (pc._fishingRod.getItemId() == 41293) {
				pc.sendPackets(S_FishingNoti.FISH_NORMAL_RIL);
			} else {
				pc._fishingRod.setChargeCount(pc._fishingRod.getChargeCount() - 1);
				pc.getInventory().updateItem(pc._fishingRod, L1PcInventory.COL_CHARGE_COUNT);
				pc.sendPackets(new S_FishingNoti(fish.getInterval(), FISHING_ROD.FISHING_ROD_SPECIAL, pc._fishingRod.getChargeCount()), true);
			}
			pc.setFishingTime(currentTime + (fish.getInterval() * 1000));
			reward(pc, fish);
		}
	}
	
	/**
	 * 낚시를 종료시킨다.
	 * @param pc
	 */
	void endFishing(L1PcInstance pc) {
		pc.setFishingTime(0);
		pc.setFishing(false);
		pc._fishingRod = null;
		pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc), true);
		pc.sendPackets(L1ServerMessage.sm1163);// 낚시가 종료했습니다.
	}
	
	/**
	 * 낚시 성공에 대한 보상을 지급한다.
	 * @param pc
	 * @param fish
	 */
	void reward(L1PcInstance pc, L1Fishing fish){
		int prob		= 0;
		int rnd			= random.nextInt(1000000);
		int rewardCnt	= 0;
		for (L1Fishing.Reward each : fish.getRewards()) {
			if (each.getProb() == 0) {
				itemReward(pc, each);
				rewardCnt++;
				continue;
			}
			prob += each.getProb();
			if (rnd < prob) {
				itemReward(pc, each);
				rewardCnt++;
				break;
			}
		}
		if (rewardCnt == 0) {
			pc.sendPackets(L1ServerMessage.sm1136);// 낚시에 실패했습니다.
			return;
		}
		if (fish.getRewardExp() > 0) {
			expReward(pc, fish.getRewardExp());
		}
	}
	
	//static final String REWARD_MESSAGE = "누군가가 %s를(을) 낚아 올렸습니다!";
	static final String REWARD_MESSAGE = S_SystemMessage.getRefText(12) + "%s " + S_SystemMessage.getRefText(10);
	void itemReward(L1PcInstance pc, L1Fishing.Reward reward){
		if (pc.getInventory().getSize() > (L1PcInventory.MAX_SIZE - 2)) {
			pc.sendPackets(L1ServerMessage.sm263);
			return;
		}
		L1ItemInstance item = pc.getInventory().storeItem(reward.getItemId(), reward.getCount());
		if (item == null) {
			System.out.println("Fish Controller reward item empty -> itemId: " + reward.getItemId() + ", pcname: " + pc.getName());
			return;
		}
		//pc.sendPackets(new S_ServerMessage(1185, item.getDescKr()), true);// 낚시에 성공해%0%o를 낚시했습니다.
		pc.sendPackets(new S_ServerMessage(1185, item.getDesc()), true);// 낚시에 성공해%0%o를 낚시했습니다.
		if (reward.isBroad()) {
			//broadMent(String.format(REWARD_MESSAGE, item.getDescKr()));
			broadMent(String.format(REWARD_MESSAGE, item.getDesc()));
		}
		if (reward.getEffectId() > 0) {
			pc.send_effect_self(reward.getEffectId());
		}
	}
	
	void expReward(L1PcInstance pc, int exp){
		if (pc.getExp() + exp >= L1ExpPlayer.LIMIT_EXP) {
			return;
		}
		if (Config.SERVER.STANDBY_SERVER) {
			pc.sendPackets(L1SystemMessage.STANBY_EXP_EMPTY_MSG);
			return;
		}
		
		double blessEXP = 0D, addEXP = 0D;
		L1Einhasad ein = pc.getAccount().getEinhasad();
		int einhasadValue = ein.getPoint();
		int einhasadDefaultRation = Config.EIN.REST_EXP_DEFAULT_RATION;
		if (einhasadValue >= einhasadDefaultRation) {
			blessEXP	= 1.0D;
			addEXP		= 2.0D;
            if (pc.getSkill().hasSkillEffect(L1SkillId.EMERALD_YES)) {
            	blessEXP += 0.54;
            } else if (pc.getSkill().hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
            	blessEXP += (double)(L1ExpPlayer.getDragonPupleExp(pc.getLevel()) * 0.01D);
	        } else if (pc.getSkill().hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
	        	blessEXP += (double)(L1ExpPlayer.getTopazExp() * 0.01D);
	        }
            
		    int einDecrease					= (int)((exp * Config.EIN.REST_EXP_DECREASE_RATE) * blessEXP);
			int rest_exp_reduce_efficiency	= pc.get_rest_exp_reduce_efficiency();
			if (rest_exp_reduce_efficiency > 0) {
				int defense		= (int)((einDecrease / Config.EIN.REST_EXP_REDUCE_EFFICIENCY_PERCENT) * (rest_exp_reduce_efficiency >= 100 ? 99 : rest_exp_reduce_efficiency));
				einDecrease		-= defense;
			}
		    if (einDecrease <= 0) {
		    	einDecrease		= einhasadDefaultRation >> 2;// 4분의1고정
			}
		    ein.addPoint(-einDecrease, pc);// 아인하사드 소모
		    if (ein.getPoint() < einhasadDefaultRation && pc.getSkill().hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
		    	pc.getSkill().removeSkillEffect(L1SkillId.DRAGON_PUPLE);
		    }
		    
		    if (pc.getEinPoint() < Config.EIN.EINHASAD_POINT_LIMIT_CHARGE_VALUE) {
		    	int addEinPoint = (int)((einDecrease * 0.001D) * Config.EIN.EINHASAD_POINT_RATE);
		    	if (addEinPoint > 0) {
		    		pc.addEinPoint(addEinPoint);
		    	}
		    } else {
		    	pc.sendPackets(L1ServerMessage.sm7552);// 아인하사드 포인트를 더 이상  얻을 수 없습니다.
		    }
		    
		    pc.sendPackets(new S_RestExpInfoNoti(pc), true);
		    pc.sendPackets(new S_ExpBoostingInfo(pc), true);
        } else if (pc.isPCCafe() || pc.getSkill().hasSkillEffect(L1SkillId.EINHASAD_FAVOR)) {
        	addEXP = 1.0D;
        }
		
        long add_exp = (long)(exp * Config.RATE.RATE_XP * ExpTable.getPenalty(pc.getLevel()) * (1 + blessEXP + addEXP));
        if (add_exp < 0) {
        	System.out.println(String.format("[Fishing] MINUS_EXP_CHECK : VALUE(%d), CHAR_NAME(%s)", add_exp, pc.getName()));
			return;
        }
        if (add_exp == 0) {
        	add_exp = 1;
        }
        
	    /** 폭렙 방지 **/
	    if (pc.getLevel() >= 1 && (add_exp + pc.getExp() > ExpTable.getExpByLevel(pc.getLevel() + 1))) {
			add_exp = ExpTable.getExpByLevel(pc.getLevel() + 1) - pc.getExp();
		}
	    if (pc.getClan() != null && (!Config.PLEDGE.PLEDGE_CONTRIBUTION_EINHASAD || einhasadValue >= einhasadDefaultRation)) {// 혈맹 공헌도
			int contribution = (int)((add_exp * 0.01) * Config.PLEDGE.PLEDGE_CONTRIBUTION_RATE);
			if (contribution > 0) {
				pc.addClanWeekContribution(contribution);
			}
		}
	    pc.addExp(add_exp);
	}
	
	void broadMent(String msg){
		S_PacketBox greenMsg = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg);
		for (L1PcInstance player : L1World.getInstance().getMapPlayer(5490)) {// 낚시터
			player.sendPackets(greenMsg);
		}
		greenMsg.clear();
		greenMsg = null;
	}
}

