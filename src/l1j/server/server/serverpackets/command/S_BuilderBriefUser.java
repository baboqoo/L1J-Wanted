package l1j.server.server.serverpackets.command;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BuilderBriefUser extends ServerBasePacket {
	private static final String S_BUILDER_BRIEF_USER = "[S] S_BuilderBriefUser";
	private byte[] _byte = null;
	
	public S_BuilderBriefUser(int code, L1PcInstance target){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		
		writeC(0x0a);
		writeBytesWithLength(target.getName().getBytes());
		
		writeC(0x10);
		writeC(target.getType());
		
		writeC(0x18);
		writeBit(target.getLevel());
		
		writeC(0x20);
		writeBit(target.getMaxHp());
		
		writeC(0x28);
		writeBit(target.getMaxMp());
		
		writeC(0x30);
		writeBit(target.getResistance().getHitupFear());
		
		writeC(0x38);
		writeBit(target.getResistance().getHitupSkill());
		
		writeC(0x40);
		writeBit(target.getResistance().getHitupDragon());
		
		writeC(0x48);
		writeBit(target.getResistance().getHitupSpirit());
		
		writeC(0x50);
		writeBit(target.getResistance().getToleranceFear());
		
		writeC(0x58);
		writeBit(target.getResistance().getToleranceSkill());
		
		writeC(0x60);
		writeBit(target.getResistance().getToleranceDragon());
		
		writeC(0x68);
		writeBit(target.getResistance().getToleranceSpirit());
		
		writeC(0x70);
		writeBit(target.getResistance().getFire());
		
		writeC(0x78);
		writeBit(target.getResistance().getWater());
		
		writeH(0x0180);
		writeBit(target.getResistance().getWind());
		
		writeH(0x0188);
		writeBit(target.getResistance().getEarth());
		
		writeH(0x0190);
		writeBit(target.getAbility().getDg());
		
		writeH(0x0198);
		writeBit(target.getAbility().getEr());
		
		writeH(0x01a0);
		writeBit(target.getAbility().getMe());
		
		writeH(0x01a8);
		writeC(target.getAbility().getBaseStr());
		
		writeH(0x01b0);
		writeC(target.getAbility().getBaseInt());
		
		writeH(0x01b8);
		writeC(target.getAbility().getBaseDex());
		
		writeH(0x01c0);
		writeC(target.getAbility().getBaseWis());
		
		writeH(0x01c8);
		writeC(target.getAbility().getBaseCon());
		
		writeH(0x01d0);
		writeC(target.getAbility().getBaseCha());
		
		writeH(0x01d8);
		writeBit(target.getAbility().getTotalStr());
		
		writeH(0x01e0);
		writeBit(target.getAbility().getTotalInt());
		
		writeH(0x01e8);
		writeBit(target.getAbility().getTotalDex());
		
		writeH(0x01f0);
		writeBit(target.getAbility().getTotalWis());
		
		writeH(0x01f8);
		writeBit(target.getAbility().getTotalCon());
		
		writeH(0x0280);
		writeBit(target.getAbility().getTotalCha());
		
		writeH(0x028a);// equip items
		writeC(0x00);
		
		writeH(0x0290);// active skills
		writeC(0x00);
		
		writeH(0x0298);// passive skills
		writeC(0x00);
		
		writeH(0x02a2);// stat
		writeC(0x00);
		
		writeH(0x02aa);// baseStat
		writeC(0x00);
		
		writeH(0x02b2);// einStat
		writeC(0x00);
		
		writeH(0x02b8);
		writeC(target.getGender().toInt());
		
		for (L1ItemInstance item : target.getInventory().getItems()) {
			if (item == null) {
				continue;
			}
			writeH(0x02c2);// items
			writeBytesWithLength(item.getItemInfo(target));
		}
		
		writeH(0x02c8);// isFirst
		writeB(true);
		
		writeH(0x02d0);// isLast
		writeB(true);

		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_BUILDER_BRIEF_USER;
	}
}

