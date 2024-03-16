package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.freebuffshield.GoldenBuffInfo;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.common.bin.PCMasterCommonBinLoader;
import l1j.server.common.bin.pcmaster.PCMasterInfoForClient;
import l1j.server.server.GameClient;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.system.S_PCMasterGoldenBuffUpdateNoti;

public class A_PCMasterGoldenBuffEnforce extends ProtoHandler {
	protected A_PCMasterGoldenBuffEnforce(){}
	private A_PCMasterGoldenBuffEnforce(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _index;
	private int _group;
	private int _target_bonus;
	private int _target_grade;

	void parse() {
		readP(1);// 0x08
		_index = readC();
		readP(1);// 0x10
		_group = readC();
		readP(1);// 0x18
		_target_bonus = readC();
		readP(1);// 0x20
		_target_grade = readC();
	}
	
	boolean isValidation() {
		return _index >= 0 && _group >= 1 && _target_bonus >= 0 && _target_grade >= 1;
	}
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !_pc.isPCCafe()) {
			return;
		}
		FreeBuffShieldHandler handler = _pc.getConfig().get_free_buff_shield();
		if (handler == null) {
			System.out.println(String.format(
					"[A_PCMasterGoldenBuffEnforce] HANDLER_NOT_FOUND : CHAR_NAME(%s)", 
					_pc.getName()));
			return;
		}
		parse();
		if (!isValidation()) {
			System.out.println(String.format(
					"[A_PCMasterGoldenBuffEnforce] VALIDATION_FAIL : CHAR_NAME(%s), INDEX(%d), GROUP(%d), TARGET_BONUS(%d), TARGET_GRADE(%d)", 
					_pc.getName(), _index, _group, _target_bonus, _target_grade));
			return;
		}
		
		GoldenBuffInfo buffInfo = handler.get_golden_buff_info(_index);
		if (buffInfo == null) {
			System.out.println(String.format(
					"[A_PCMasterGoldenBuffEnforce] BUFF_INFO_EMPTY : CHAR_NAME(%s), INDEX(%d)", 
					_pc.getName(), _index));
			return;
		}
		
		PCMasterInfoForClient.BuffBonusT bonusT = buffInfo.getBuffBonusT();
		if (bonusT == null) {
			System.out.println(String.format(
					"[A_PCMasterGoldenBuffEnforce] BUFF_BONUS_T_EMPTY : CHAR_NAME(%s), INDEX(%d)", 
					_pc.getName(), _index));
			return;
		}
		
		int level = _pc.getLevel();
		if ((bonusT.get_max_level() > 0 && level > bonusT.get_max_level()) 
				|| (bonusT.get_min_level() > 0 && level < bonusT.get_min_level())) {
			return;
		}
		
		PCMasterInfoForClient.BuffBonusT.BuffCategoryT categoryT = bonusT.get_buff_category().get(_group - 1);
		if (categoryT == null) {
			System.out.println(String.format(
					"[A_PCMasterGoldenBuffEnforce] BUFF_CATEGORY_T_EMPTY : CHAR_NAME(%s), INDEX(%d), GROUP(%d)", 
					_pc.getName(), _index, _group));
			return;
		}
		
		int group_id = categoryT.get_group().get(_target_bonus);
		PCMasterInfoForClient.BuffGroupT groupT = PCMasterCommonBinLoader.getData().get_buff_group().get(group_id - 1);
		if (groupT == null) {
			System.out.println(String.format(
					"[A_PCMasterGoldenBuffEnforce] BUFF_GROUP_T_EMPTY : CHAR_NAME(%s), INDEX(%d), GROUP(%d), GROUP_ID(%d)", 
					_pc.getName(), _index, _group, group_id));
			return;
		}
		
		PCMasterInfoForClient.BuffGroupT.BuffT buffT = groupT.get_buff().get(_target_grade - 1);
		if (buffT == null) {
			System.out.println(String.format(
					"[A_PCMasterGoldenBuffEnforce] BUFF_T_EMPTY : CHAR_NAME(%s), INDEX(%d), GROUP(%d), GRADE(%d)", 
					_pc.getName(), _index, _group, _target_grade));
			return;
		}
		
		// 비용
		int cost = groupT.get_cost().get(_target_grade - 1);
		if (cost > 0 && !_pc.getInventory().consumeItem(L1ItemId.PIXIE_GOLD_FEATHER, cost)) {
			return;
		}
		
		int time = groupT.get_time().get(_target_grade - 1);// 1분단위
		buffInfo.setRemainTime(buffInfo.getRemainTime() + (time * 60));
		buffInfo.enforceGrade(_target_bonus, _target_grade);
		
		_pc.sendPackets(new S_PCMasterGoldenBuffUpdateNoti(
				handler.get_golden_buff_infos(), 
				S_PCMasterGoldenBuffUpdateNoti.eUpdateReason.ENFORCE_ACK), 
				true);
		
		LoggerInstance.getInstance().addPCMasterGoldenBuffEnforce(
				_pc, _index, _group, _target_bonus, _target_grade, cost, buffInfo.getRemainTime());
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PCMasterGoldenBuffEnforce(data, client);
	}

}

