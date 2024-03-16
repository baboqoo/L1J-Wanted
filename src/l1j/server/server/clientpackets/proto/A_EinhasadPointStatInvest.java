package l1j.server.server.clientpackets.proto;

import l1j.server.common.bin.EinhasadPointCommonBinLoader;
import l1j.server.common.bin.einhasadpoint.EinhasadPointStatInfoT;
import l1j.server.common.data.eEinhasadStatType;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.CharacterEinhasadStatTable;
import l1j.server.server.model.L1Ability;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadStatInvest;
import l1j.server.server.templates.L1CharEinStat;

public class A_EinhasadPointStatInvest extends ProtoHandler {
	protected A_EinhasadPointStatInvest(){}
	private A_EinhasadPointStatInvest(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private EinhasadPointStatInfoT infoT;
	private static int FIRST_MAX_VALUE, SECOND_MAX_VALUE;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		CharacterEinhasadStatTable charStat	= CharacterEinhasadStatTable.getInstance();
		L1CharEinStat temp					= charStat.getEinSate(_pc);
		if (temp == null) {
			return;
		}
		
		L1Ability ability = _pc.getAbility();
		if (ability.get_total_ein_stat() >= _pc.getEinTotalStat()) {
			return;
		}
		
		infoT = EinhasadPointCommonBinLoader.getInfo();
		if (infoT == null) {
			System.out.println(String.format("[A_EinhasadPointStatInvest] BIN_INFO_EMPTY : CHAR_NAME(%s)", _pc.getName()));
			return;
		}
		if (FIRST_MAX_VALUE == 0) {
			FIRST_MAX_VALUE = infoT.get_StatMaxInfo().get(0).get_statMax();
		}
		if (SECOND_MAX_VALUE == 0) {
			SECOND_MAX_VALUE = infoT.get_StatMaxInfo().get(1).get_statMax();
		}
		
		readP(1);// 0x08
		eEinhasadStatType type = eEinhasadStatType.fromInt(readC());
		if (type == null) {
			return;
		}
		
		byte value;
		switch(type){
		case BLESS:
			value = (byte) (ability.getStatBless() + 1);
			break;
		case LUCKY:
			value = (byte) (ability.getStatLucky() + 1);
			break;
		case VITAL:
			value = (byte) (ability.getStatVital() + 1);
			break;
		case ITEM_SPELL_PROB:
			value = (byte) (ability.getStatItemSpellProb() + 1);
			break;
		case ABSOLUTE_REGEN:
			value = (byte) (ability.getStatAbsoluteRegen() + 1);
			break;
		case POTION:
			value = (byte) (ability.getStatPotion() + 1);
			break;
		default:
			return;
		}
		
		// 최대 수치 검증
		if ((eEinhasadStatType.GRADE_FIRST.contains(type) && value > FIRST_MAX_VALUE)
				|| (eEinhasadStatType.GRADE_SECOND.contains(type) && value > SECOND_MAX_VALUE)) {
			return;
		}
		
		switch(type){
		case BLESS:
			ability.addStatBless((byte)1);
			break;
		case LUCKY:
			ability.addStatLucky((byte)1);
			break;
		case VITAL:
			ability.addStatVital((byte)1);
			break;
		case ITEM_SPELL_PROB:
			ability.addStatItemSpellProb((byte)1);
			break;
		case ABSOLUTE_REGEN:
			ability.addStatAbsoluteRegen((byte)1);
			break;
		case POTION:
			ability.addStatPotion((byte)1);
			break;
		default:
			return;
		}
		
		charStat.updateTemp(temp, _pc, type);// 정보 갱신
		_pc.sendPackets(new S_EinhasadStatInvest(_pc, type), true);
		_pc.sendPackets(new S_RestExpInfoNoti(_pc), true);
		_pc.sendPackets(new S_ExpBoostingInfo(_pc), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EinhasadPointStatInvest(data, client);
	}

}

