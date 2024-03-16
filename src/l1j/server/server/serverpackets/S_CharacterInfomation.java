package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;
import l1j.server.server.templates.CharacterSkillInfo;

public class S_CharacterInfomation extends ServerBasePacket{
	private static final String S_CHARACTER_INFOMATION = "[S] S_CharacterInfomation";
	
	public static S_CharacterInfomation getSpellList(L1PcInstance pc, List<CharacterSkillInfo> skillsInfo, boolean describe){
		S_CharacterInfomation pck = new S_CharacterInfomation();
		pck.writeC(Opcodes.S_EXTENDED_PROTOBUF);
		pck.writeH(S_WareHouseItemListNoti.NOTI);
		
		if (skillsInfo == null || skillsInfo.isEmpty()) {
			pck.writeC(0x08);
			pck.writeC(0);
			pck.writeC(0x10);
			pck.writeC(0);
			pck.writeH(0x00);
			return pck;
		}
				
		pck.writeC(0x08);
		pck.writeBit(180);
		
		pck.writeC(0x10);
		pck.writeBit(skillsInfo.size());
		
		pck.writeC(0x18);
		pck.writeC(3);
		
		pck.writeC(0x20);
		pck.writeC(1);
		
		int cnt = 0;
		L1ItemInstance dummy = ItemTable.getInstance().createItem(40005);
		for (CharacterSkillInfo spell : skillsInfo) {
			if (spell == null) {
				continue;
			}
			
			dummy.getItem().setIconId(spell.getIcon());
			dummy.getItem().setDescKr(spell.getName());
			dummy.getItem().setDesc(spell.getName());
			
			byte[] detail = dummy.getItemInfo(pc);
			pck.writeC(0x32);
			pck.writeBit(detail.length + pck.getBitSize(detail.length) + pck.getBitSize(cnt) + 2);
			
			pck.writeC(0x08);
			pck.writeBit(cnt);
			
			pck.writeC(0x12);
			pck.writeBytesWithLength(detail);
			cnt++;
		}
		dummy = null;
		
		pck.writeC(0x38); 
		pck.writeB(true);
			
		pck.writeH(0x00);
		return pck;
	}
	
	public static S_CharacterInfomation getInvList(L1PcInstance pc, List<L1ItemInstance> items){
		S_CharacterInfomation pck = new S_CharacterInfomation();
		pck.writeC(Opcodes.S_EXTENDED_PROTOBUF);
		pck.writeH(S_WareHouseItemListNoti.NOTI);
		
		if (items == null) {
			pck.writeC(0x08);
			pck.writeC(0);
			pck.writeC(0x10);
			pck.writeC(0);
			pck.writeH(0x00);
			return pck;
		}
		
		pck.writeC(0x08);
		pck.writeBit(180);
		
		pck.writeC(0x10);
		pck.writeBit(items.size());
		
		pck.writeC(0x18);
		pck.writeC(3);
		
		pck.writeC(0x20);
		pck.writeC(1);

		int cnt = 0;
		for (L1ItemInstance item : items) {
			byte[] detail = item.getItemInfo(pc);
			pck.writeC(0x32);
			pck.writeBit(detail.length + pck.getBitSize(detail.length) + pck.getBitSize(cnt) + 2);
			
			pck.writeC(0x08);
			pck.writeBit(cnt);
			
			pck.writeC(0x12);
			pck.writeBytesWithLength(detail);
			cnt++;
		}
		
		pck.writeC(0x38);
		pck.writeB(true);
		
		pck.writeH(0x00);
		return pck;
	}
	
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_CHARACTER_INFOMATION;
	}
}

