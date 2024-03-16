package l1j.server.QuestSystem.Compensator;

import java.sql.ResultSet;

import l1j.server.QuestSystem.Compensator.Element.ExpCompensator;
import l1j.server.QuestSystem.Compensator.Element.ItemCompensator;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BytesOutputStream;

public class WeekQuestCompensator implements QuestCompensator{

	public static final String		_table		= "tb_weekquest_compensate";
	
	private int 			_buttonNo;			// button number.
	private int 			_ingredientItemId;	// 재료 아이템 id.
	private ExpCompensator 	_exp;				// 보상 경험치를 제공할 element.
	private ItemCompensator _item;				// 보상 아이템을 제공할 element.
	private String 			_lastRecord;		// 마지막 기록된 레코드
	private byte[]	 		_serialDatas;
	
	@Override
	public void set(ResultSet rs) throws Exception{
		int nTmp1	= 0;
		int nTmp2	= 0;
		int nTmp3	= 0;
		
		_lastRecord 		= "button_no";
		_buttonNo 			= rs.getInt(_lastRecord);
		
		_lastRecord			= "ingredient_itemId";
		_ingredientItemId	= rs.getInt(_lastRecord);
		
		_lastRecord			= "compen_exp";
		nTmp1 = rs.getInt(_lastRecord);
		
		_lastRecord			= "compen_exp_level";
		nTmp2	= rs.getInt(_lastRecord);
		_exp	= new ExpCompensator(nTmp1, nTmp2);
				
		_lastRecord			= "compen_itemId";
		nTmp1	= rs.getInt(_lastRecord);
		
		_lastRecord			= "compen_itemCount";
		nTmp2	= rs.getInt(_lastRecord);
				
		_lastRecord			= "compen_itemLevel";
		nTmp3	= rs.getInt(_lastRecord);
		_item = new ItemCompensator(nTmp1, nTmp2, nTmp3);
		
		serialize();
	}

	private static final int[] _exps = new int[]{ 721306, 1803265, 7213060 };
	private static final int[] _dds = new int[]{ 0, 4166, 15391 };
	public synchronized void serialize() throws Exception{
		if (_serialDatas != null)
			return;
		
		BytesOutputStream mbos = new BytesOutputStream();
		mbos.write(0x12);
		mbos.write(0x10);
		mbos.write(0x08);
		mbos.write(_buttonNo);
		mbos.write(0x10);
		//mbos.writeBit(_exp.getExp());
		mbos.writeBit(_exps[_buttonNo - 1]);
		mbos.write(0x18);
		mbos.writeBit(_dds[_buttonNo - 1]);
		mbos.write(0x22);
		mbos.write(0x06);
		mbos.write(0x08);
		// 군터의 인장 descid
		mbos.writeBit(17368);
		mbos.write(0x10);
		
		// 갯수,,, 난이도별로 1, 5, 10개
		mbos.write((_buttonNo / 3) + (_buttonNo % 3));
		_serialDatas = mbos.toArray();
		_serialDatas[1] = (byte)((_serialDatas.length - 2) & 0xff);
		mbos.close();
	}
	
	@Override
	public String getLastRecord() {
		return _lastRecord;
	}

	@Override
	public int getDifficulty() {
		return _buttonNo;
	}

	@Override
	public void compensate(L1PcInstance pc) {
		if (_ingredientItemId != 0){
			if (!pc.getInventory().checkItem(_ingredientItemId)){
				StringBuilder sb = new StringBuilder(100);
				L1Item item = ItemTable.getInstance().getTemplate(_ingredientItemId);
				if (item == null)
					return;
				
				//sb.append(item.getDesc()).append("을 가지고 있지 않습니다.");
				sb.append(item.getDesc()).append(S_SystemMessage.getRefText(1359));
				pc.sendPackets(new S_SystemMessage(sb.toString()), true);
				return;
			}
			pc.getInventory().consumeItem(_ingredientItemId, 1);
		}
		
		if (_exp != null)
			_exp.compensate(pc);
		if (_item != null)
			_item.compensate(pc);
		
	}
	
	public byte[] getSerialize(){
		return _serialDatas;
	}
}

