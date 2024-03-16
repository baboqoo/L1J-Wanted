package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.common.bin.EinhasadPointCommonBinLoader;
import l1j.server.common.bin.einhasadpoint.EinhasadPointStatInfoT;
import l1j.server.server.GameClient;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadPointEnchantStat;
import l1j.server.server.utils.CommonUtil;

public class A_EinhasadPointEnchantStat extends ProtoHandler {
	protected A_EinhasadPointEnchantStat(){}
	private A_EinhasadPointEnchantStat(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		EinhasadPointStatInfoT infoT = EinhasadPointCommonBinLoader.getInfo();
		if (infoT == null) {
			System.out.println(String.format("[A_EinhasadPointEnchantStat] BIN_INFO_EMPTY : CHAR_NAME(%s)", _pc.getName()));
			return;
		}
		if (_pc.getEinTotalStat() >= infoT.get_totalStatMax()) {
			return;
		}
		int cost = infoT.get_EnchantCost().get(_pc.getEinTotalStat()).get_point();// 사용할 포인트 수치
		if (_pc.getEinPoint() < cost) {
			return;
		}
		// 포인트 소모
		_pc.addEinPoint(-cost);
		
		int cur_enchant_level	= _pc.getEinCurEnchantLevel();
		boolean isLastChance	= cur_enchant_level >= 9;
		EinhasadPointStatInfoT.EinhasadProb prob = infoT.get_einhasadProb().getFirst();
		boolean success = isLastChance || CommonUtil.random(0x3B9ACA00) + 1 <= prob.get_NormalLevels().get(cur_enchant_level).get_prob();
		
		// 성공
		if (success) {
			int bonus_value = 1;
			
			// 하이퍼 여부
			boolean isHyper = isLastChance || CommonUtil.random(100) + 1 <= Config.EIN.EINHASAD_POINT_CARD_HYPER_PROB;
			if (isHyper) {
				EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable probT = 
						isLastChance ? prob.get_probTable().getLast() : prob.get_probTable().getFirst();
				int bonusProb = CommonUtil.random(0x3B9ACA00) + 1;
				int flagProb = 0;
				for (EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb probs : probT.get_probList()) {
					flagProb += probs.get_prob();
					if (bonusProb <= flagProb) {
						bonus_value = probs.get_bonusPoint();
						break;
					}
				}
				if (bonus_value < 1) {
					bonus_value = 1;
				}
			}
			
			// 획득할 수치가 최대 수치를 벗어날경우 기본수치로 변경해준다.
			if (_pc.getEinTotalStat() + bonus_value > infoT.get_totalStatMax()) {
				bonus_value = 1;
			}
			
			if (isHyper) {
				// 보너스 카드 획득 A_EinhasadStatBonusCardOpen에서 처리된다.
				_pc.setEinBonusCardOpenValue(bonus_value);// 하이퍼로 획득한 보너스 수치
				_pc.sendPackets(new S_EinhasadPointEnchantStat(_pc, 1, bonus_value), true);
				return;
			}
			
			_pc.addEinTotalStat(bonus_value);
			_pc.setEinCurEnchantLevel(0);
			_pc.sendPackets(new S_EinhasadPointEnchantStat(_pc, 0, bonus_value), true);
			LoggerInstance.getInstance().addEinState(_pc, true, bonus_value, cost);
			return;
		}
		_pc.addEinCurEnchantLevel(1);// 실패시 누적
		_pc.sendPackets(new S_EinhasadPointEnchantStat(_pc, 0, 0), true);
		LoggerInstance.getInstance().addEinState(_pc, false, 0, cost);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EinhasadPointEnchantStat(data, client);
	}

}

