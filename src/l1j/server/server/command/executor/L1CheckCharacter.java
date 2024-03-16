package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1CheckCharacter implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1CheckCharacter();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1CheckCharacter() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		Connection c = null;
		PreparedStatement p = null;
		PreparedStatement p1 = null;
		ResultSet r = null;
		ResultSet r1 = null;
		try {				
			StringTokenizer st = new StringTokenizer(arg);
			String charname	= st.nextToken();
			String type		= st.nextToken();

			c = L1DatabaseFactory.getInstance().getConnection();
			
			String itemname;
			int searchCount = 0;
			//if (type.equalsIgnoreCase("인벤")){	
			if (type.equalsIgnoreCase("inventory")){	
				try {

					// 캐릭 오브젝트 ID 검색 1=objid 2=charname
					p = c.prepareStatement("SELECT objid, char_name FROM characters WHERE char_name = '" + charname + "'");
					r = p.executeQuery();
					if(r.next()){
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("\\fW** 검사: "+type+" 캐릭: " + charname + " **"), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(289) + type + S_SystemMessage.getRefText(290) + charname  + " **", true), true);
						L1PcInstance target = L1World.getInstance().getPlayer(charname);			
						if (target != null) target.saveInventory();						
						// 캐릭 아이템 검색 1-itemid 2-인챈 3-착용 4-수량 5-이름 6-축복 7-속성
						p1 = c.prepareStatement("SELECT item_id,enchantlvl,is_equipped,count,item_name,bless,attr_enchantlvl " +
								"FROM character_items WHERE char_id = '" + r.getInt(1) + "' ORDER BY 3 DESC,2 DESC, 1 ASC");
						r1 = p1.executeQuery();				
						while(r1.next()){
							itemname = getInvenItemMsg(r1.getInt(1),r1.getInt(2),r1.getInt(3),r1.getInt(4),r1.getString(5),r1.getInt(6),r1.getInt(7));
							pc.sendPackets(new S_SystemMessage("\\fU"+ ++searchCount +". " + itemname), true);
							itemname = StringUtil.EmptyString;
						}
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("\\fW** 총 "+searchCount+"건의 아이템이 검색 되었습니다 **"), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(291) + searchCount + S_SystemMessage.getRefText(292), true), true);
					}					
				} catch (Exception e) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("\\fW** [" + charname + "] 캐릭 검색 오류 **"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage("\\fW** [" + charname  + S_SystemMessage.getRefText(293), true), true);
				}
			//} else if (type.equalsIgnoreCase("창고")){
			} else if (type.equalsIgnoreCase("storage")){
				try {
					p = c.prepareStatement("SELECT account_name, char_name FROM characters WHERE char_name = '" + charname + "'");
					r = p.executeQuery();
					while (r.next()){
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("\\fW** 검사: "+type+" 캐릭: " + charname + "(" + r.getString(1) + ") **"), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(289) + type + S_SystemMessage.getRefText(290) + charname  + "(" + r.getString(1)  + ") **", true), true);
						//캐릭 창고 검색 1-itemid 2-인챈 3-수량 4-이름 5-축복 6-속성
						p1 = c.prepareStatement("SELECT item_id,enchantlvl,count,item_name,bless,attr_enchantlvl FROM character_warehouse " +
								"WHERE account_name = '" + r.getString(1) + "' ORDER BY 2 DESC, 1 ASC");
						r1 = p1.executeQuery();
						while (r1.next()){
							itemname = getInvenItemMsg(r1.getInt(1),r1.getInt(2),0,r1.getInt(3),r1.getString(4),r1.getInt(5),r1.getInt(6));
							pc.sendPackets(new S_SystemMessage("\\fU"+ ++searchCount +". " + itemname), true);
							itemname = StringUtil.EmptyString;
						}
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("\\fW** 총 "+searchCount+"건의 아이템이 검색 되었습니다 **"), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(291) + searchCount + S_SystemMessage.getRefText(292), true), true);
					}
				} catch (Exception e) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("\\fW** [" + charname + "] 캐릭 검색 오류 **"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage("\\fW** [" + charname  + S_SystemMessage.getRefText(293), true), true);
				}
			//} else if (type.equalsIgnoreCase("요정창고")){				
			} else if (type.equalsIgnoreCase("elfstorage")){
				try {
					p = c.prepareStatement("SELECT account_name, char_name FROM characters WHERE char_name = '" + charname + "'");
					r = p.executeQuery();
					while (r.next()){
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("\\fW** 검사: "+type+" 캐릭: " + charname + "(" + r.getString(1) + ") **"), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(289) + type + S_SystemMessage.getRefText(290) + charname  + "(" + r.getString(1)  + ") **", true), true);
						//캐릭 요정창고 검색 1-itemid 2-인챈 3-수량 4-이름 5-축복 6-속성
						p1 = c.prepareStatement("SELECT item_id,enchantlvl,count,item_name,bless,attr_enchantlvl FROM character_elf_warehouse " +
								"WHERE account_name = '" + r.getString(1) + "' ORDER BY 2 DESC, 1 ASC");
						r1 = p1.executeQuery();
						while (r1.next()){
							itemname = getInvenItemMsg(r1.getInt(1),r1.getInt(2),0,r1.getInt(3),r1.getString(4),r1.getInt(5),r1.getInt(6));
							pc.sendPackets(new S_SystemMessage("\\fU"+ ++searchCount +". " + itemname), true);
							itemname = StringUtil.EmptyString;
						}
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("\\fW** 총 "+searchCount+"건의 아이템이 검색 되었습니다 **"), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(291) + searchCount + S_SystemMessage.getRefText(292), true), true);
					}
				} catch (Exception e) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("\\fW** [" + charname + "] 캐릭 검색 오류 **"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage("\\fW** [" + charname  + S_SystemMessage.getRefText(293), true), true);
				}
			//} else if (type.equalsIgnoreCase("장비")){	
			} else if (type.equalsIgnoreCase("equipment")){	
				try {
					// 캐릭 오브젝트 ID 검색 1=objid 2=charname
					p = c.prepareStatement("SELECT objid, char_name FROM characters WHERE char_name = '" + charname + "'");
					r = p.executeQuery();
					while(r.next()){
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("\\fW** 검사: "+type+" 캐릭: " + charname + " **"), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(289) + type + S_SystemMessage.getRefText(290) + charname  + " **", true), true);
						L1PcInstance target = L1World.getInstance().getPlayer(charname);			
						if (target != null) target.saveInventory();						
						// 캐릭 아이템 검색 1-itemid 2-인챈 3-착용 4-수량 5-이름 6-축복 7-속성
						p1 = c.prepareStatement("SELECT item_id,enchantlvl,is_equipped,count,item_name,bless,attr_enchantlvl " +
								"FROM character_items WHERE char_id = '" + r.getInt(1) + "' ORDER BY 3 DESC,2 DESC, 1 ASC");
						r1 = p1.executeQuery();				
						while(r1.next()){
							L1ItemInstance item = ItemTable.getInstance().createItem(r1.getInt(1));							
							if (item.getItem().getItemType() == L1ItemType.WEAPON || item.getItem().getItemType() == L1ItemType.ARMOR) { // 무기 방어구만 표시
								itemname = getInvenItem(r1.getInt(1),r1.getInt(2),r1.getInt(3),r1.getInt(4),r1.getString(5),r1.getInt(6),r1.getInt(7));
								pc.sendPackets(new S_SystemMessage("\\fU"+ ++searchCount +". " + itemname), true);
								itemname = StringUtil.EmptyString;
							}
						}
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("\\fW** 총 "+searchCount+"건의 아이템이 검색 되었습니다 **"), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(291) + searchCount + S_SystemMessage.getRefText(292), true), true);
					}					
				} catch (Exception e) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("\\fW** [" + charname + "] 캐릭 검색 오류 **"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage("\\fW** [" + charname  + S_SystemMessage.getRefText(293), true), true);
				}
			//} else if (type.equalsIgnoreCase("계정")) {
			} else if (type.equalsIgnoreCase("account")) {
				try {
//					L1PcInstance player = L1World.getInstance().getPlayer(charname);
//					
//					if (player == null) {
//						pc.sendPackets(new S_SystemMessage("그러한 이름의 캐릭터는 월드내에는 존재하지 않습니다."), true);
//						return;
//					}
					
					CharacterTable.getInstance().CharacterAccountCheck(pc, charname);
				} catch (Exception e) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("\\fW** [" + charname + "] 계정 검색 오류 **"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage("\\fW** [" + charname  + S_SystemMessage.getRefText(294), true), true);
				}
			}	
			return true;
		} catch (Exception e) {
		//	_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".검사 [캐릭명] [인벤,창고,요정창고,장비,계정]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(295), true), true);
		} finally {
			SQLUtil.close(r1);SQLUtil.close(p1);
			SQLUtil.close(r);SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return false;
	}
	private String getInvenItemMsg(int itemid, int enchant, int equip, int count, String itemname, int bless, int attr){
		StringBuilder name = new StringBuilder();
		// +9 축복받은 실프의 흑왕도 (착용)		
		// 인챈
		if (enchant > 0) {
			name.append(StringUtil.PlusString + enchant + StringUtil.EmptyOneString);
		} else if (enchant == 0) {
			name.append(StringUtil.EmptyString);
		} else if (enchant < 0) {
			name.append(String.valueOf(enchant) + StringUtil.EmptyOneString);
		}
		// 축복
		switch (bless) {
		//case 0:name.append("축복받은 ");break;
		//case 1:name.append(StringUtil.EmptyString);break;		
		//case 2:name.append("저주받은 ");break;
		case 0:name.append("Blessed ");break;
		case 1:name.append(StringUtil.EmptyString);break;
		case 2:name.append("Cursed ");break;		
		default: break;
		}
		// 속성
		switch(attr){
		/*case 1: name.append("화령:1단"); break;
		case 2: name.append("화령:2단"); break;
		case 3: name.append("화령:3단"); break;
		case 4: name.append("화령:4단"); break;
		case 5: name.append("화령:5단"); break;
		case 6: name.append("수령:1단"); break;
		case 7: name.append("수령:2단"); break;
		case 8: name.append("수령:3단"); break;
		case 9: name.append("수령:4단"); break;
		case 10: name.append("수령:5단"); break;
		case 11: name.append("풍령:1단"); break;
		case 12: name.append("풍령:2단"); break;
		case 13: name.append("풍령:3단"); break;
		case 14: name.append("풍령:4단"); break;
		case 15: name.append("풍령:5단"); break;
		case 16: name.append("지령:1단"); break;
		case 17: name.append("지령:2단"); break;
		case 18: name.append("지령:3단"); break;
		case 19: name.append("지령:4단"); break;
		case 20: name.append("지령:5단"); break;
		default: break;*/
		case 1: name.append("Fire Spirit: 1st Stage"); break;
		case 2: name.append("Fire Spirit: 2nd Stage"); break;
		case 3: name.append("Fire Spirit: 3rd Stage"); break;
		case 4: name.append("Fire Spirit: 4th Stage"); break;
		case 5: name.append("Fire Spirit: 5th Stage"); break;
		case 6: name.append("Water Spirit: 1st Stage"); break;
		case 7: name.append("Water Spirit: 2nd Stage"); break;
		case 8: name.append("Water Spirit: 3rd Stage"); break;
		case 9: name.append("Water Spirit: 4th Stage"); break;
		case 10: name.append("Water Spirit: 5th Stage"); break;
		case 11: name.append("Wind Spirit: 1st Stage"); break;
		case 12: name.append("Wind Spirit: 2nd Stage"); break;
		case 13: name.append("Wind Spirit: 3rd Stage"); break;
		case 14: name.append("Wind Spirit: 4th Stage"); break;
		case 15: name.append("Wind Spirit: 5th Stage"); break;
		case 16: name.append("Earth Spirit: 1st Stage"); break;
		case 17: name.append("Earth Spirit: 2nd Stage"); break;
		case 18: name.append("Earth Spirit: 3rd Stage"); break;
		case 19: name.append("Earth Spirit: 4th Stage"); break;
		case 20: name.append("Earth Spirit: 5th Stage"); break;		
		default: break;
		}
		// 이름
		name.append(itemname + StringUtil.EmptyOneString);
		// 착용여부
		if (equip == 1){
			//name.append("(착용)");
			name.append("(equipped)");
		}
		// 카운트
		if (count > 1){
			name.append("(" + count + ")");
		}
		return name.toString();
	}
	
	private String getInvenItem(int itemid, int enchant, int equip, int count, String itemname, int bless, int attr){		
		StringBuilder name = new StringBuilder();
		// +9 축복받은 실프의 흑왕도 (착용)		
		// 인챈
		if (enchant > 0) {
			name.append(StringUtil.PlusString + enchant + StringUtil.EmptyOneString);
		} else if (enchant == 0) {
			name.append(StringUtil.EmptyString);
		} else if (enchant < 0) {
			name.append(String.valueOf(enchant) + StringUtil.EmptyOneString);
		}
		// 축복
		switch (bless) {
		//case 0:name.append("축복받은 ");break;
		//case 1:name.append(StringUtil.EmptyString);break;		
		//case 2:name.append("저주받은 ");break;
		case 0:name.append("Blessed ");break;
		case 1:name.append(StringUtil.EmptyString);break;
		case 2:name.append("Cursed ");break;		
		default: break;
		}
		// 속성
		switch(attr){
		case 1: name.append("$6115 "); break;
		case 2: name.append("$6116 "); break;
		case 3: name.append("$6117 "); break;
		case 4: name.append("$6118 "); break;
		case 5: name.append("$6119 "); break;
		case 6: name.append("$6120 "); break;
		case 7: name.append("$6121 "); break;
		case 8: name.append("$6122 "); break;
		case 9: name.append("$6123 "); break;
		case 10: name.append("$6124 "); break;
		case 11: name.append("$6125 "); break;
		case 12: name.append("$6126 "); break;
		default: break;
		}
		// 이름
		name.append(itemname + StringUtil.EmptyOneString);
		// 착용여부
		if (equip == 1){
			//name.append("(착용)");
			name.append("(equipped)");
		}
		switch (bless) {
		//case 129:name.append("[봉인]");break;
		case 129:name.append("[sealed]");break;
		default: break;
		}
		// 카운트
		if (count > 1){
			name.append("(" + count + ")");
		}
		return name.toString();
	}
}


