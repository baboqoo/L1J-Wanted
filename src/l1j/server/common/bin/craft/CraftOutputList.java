package l1j.server.common.bin.craft;

import java.util.LinkedList;

import l1j.server.Config;
import l1j.server.common.DescKLoader;
import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.utils.CommonUtil;

public class CraftOutputList implements ProtoMessage {
	public static final int CRAFT_ITEM_SUCCESS_MILLIONS = 1000000;
	
	public static CraftOutputList newInstance() {
		return new CraftOutputList();
	}
	
	public CraftOutputItemResult createOutputItem(int increase_prob, int db_prob) {
		int prob	= get_success_prob_by_million() + increase_prob;
		// TODO 성공 처리
		if (prob >= CRAFT_ITEM_SUCCESS_MILLIONS) {// 확률 계산
			return new CraftOutputItemResult(extractOutputItem(get_success(), prob), true);
		}
		
		// 디비에 등록된 확률 사용
		if (Config.CRAFT.CRAFT_DB_PROBABOLITY_MILLION_USE && db_prob > 0) {
			prob	= db_prob + increase_prob;
			
		}
		prob		= (int) ((double)prob * Config.CRAFT.CRAFT_PROBABOLITY_MILLION_RATE);
		if (CommonUtil.random(CRAFT_ITEM_SUCCESS_MILLIONS - 1) + 1 <= prob) {// 성공 확률 계산
			return new CraftOutputItemResult(extractOutputItem(get_success(), prob), true);
		}

		// TODO 실패 처리
		CraftOutputSFList fail = get_failure();
		if (fail.get_prob_count() <= 0 && fail.get_non_prob_count() <= 0) {
			return new CraftOutputItemResult(null, false);
		}
		return new CraftOutputItemResult(extractOutputItem(fail, prob), false);
	}

	private CraftOutputItem extractOutputItem(CraftOutputSFList sfList, int prob) {
		int probCount = sfList.get_prob_count();
		if (probCount > 0) {
			LinkedList<CraftOutputItem> outputItems = sfList.get_output_prob_items();
			return outputItems.get((probCount == 1 || CommonUtil.random(CRAFT_ITEM_SUCCESS_MILLIONS - 1) + 1 <= prob ? 0 : CommonUtil.random(outputItems.size() - 1) + 1));
		}
		return sfList.get_output_items().get(0);
	}

	private CraftOutputSFList _success;
	private CraftOutputSFList _failure;
	private int _success_prob_by_million;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftOutputList() {
	}

	public CraftOutputSFList get_success() {
		return _success;
	}

	public void set_success(CraftOutputSFList val) {
		_bit |= 0x1;
		_success = val;
	}
	
	public String get_success_toString() {
		if (_success == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		CraftQuestFlag on_flag = _success.get_on_flag();
		if (on_flag != null) {
			sb.append("ON_FLAG: ").append("FIRST=").append(on_flag.get_flag1()).append(", SECOND=").append(on_flag.get_flag2());
		}
		
		CraftQuestFlag off_flag = _success.get_off_flag();
		if (off_flag != null) {
			sb.append("\r\nOFF_FLAG: ").append("FIRST=").append(off_flag.get_flag1()).append(", SECOND=").append(off_flag.get_flag2());
		}
		
		sb.append("\r\nPROB_COUNT: ").append(_success.get_prob_count());
		sb.append("\r\nNON_PROB_COUNT: ").append(_success.get_non_prob_count());
		
		java.util.LinkedList<CraftOutputItem> output_prob_items = _success.get_output_prob_items();
		if (output_prob_items != null && !output_prob_items.isEmpty()) {
			sb.append("\r\nOUTPUT_PROB_ITEMS ============>");
			for (CraftOutputItem val : output_prob_items) {
				sb.append("\r\nNAME_ID=").append(val.get_name_id());
				sb.append(", COUNT=").append(val.get_count());
				sb.append(", PROB=").append(val.get_slot());
				sb.append(", ENCHANT=").append(val.get_enchant());
				sb.append(", BLESS=").append(val.get_bless());
				if (val.has_elemental_type()) {
					sb.append(", ELEMENTAL_TYPE=").append(val.get_elemental_type());
				}
				if (val.has_elemental_level()) {
					sb.append(", ELEMENTAL_LEVEL=").append(val.get_elemental_level());
				}
				sb.append(", DESC=").append(val.get_desc());
				sb.append(", DESC_KR=").append(DescKLoader.getDesc(val.get_desc()));
				if (val.has_system_desc()) {
					sb.append(", SYSTEM_DESC=").append(val.get_system_desc());
				}
				if (val.has_broadcast_desc()) {
					sb.append(", BROAD_DESC=").append(val.get_broadcast_desc());
				}
				sb.append(", ICON=").append(val.get_iconId());
				if (val.has_url()) {
					sb.append(", URL=").append(val.get_url());
				}
				if (val.has_inherit_enchant_from()) {
					sb.append(", INHERIT_ENCHANT_FROM=").append(val.get_inherit_enchant_from());
				}
				if (val.has_inherit_elemental_enchant_from()) {
					sb.append(", INHERIT_ELEMENT_FROM=").append(val.get_inherit_elemental_enchant_from());
				}
				if (val.has_event_id()) {
					sb.append(", EVENT_ID=").append(val.get_event_id());
				}
				if (val.has_inherit_bless_from()) {
					sb.append(", INHERIT_BLESS_FROM=").append(val.get_inherit_bless_from());
				}
				if (val.has_attribute_bit_set()) {
					sb.append(", ATTRIBUTE_BIT_SET=").append(val.get_attribute_bit_set());
				}
				if (val.has_attribute_bit_set_ex()) {
					sb.append(", ATTRIBUTE_BIT_SET_EX=").append(val.get_attribute_bit_set_ex());
				}
			}
		}
		
		java.util.LinkedList<CraftOutputItem> output_items = _success.get_output_items();
		if (output_items != null && !output_items.isEmpty()) {
			sb.append("\r\nOUTPUT_ITEMS ============>");
			for (CraftOutputItem val : output_items) {
				sb.append("\r\nNAME_ID=").append(val.get_name_id());
				sb.append(", COUNT=").append(val.get_count());
				sb.append(", PROB=").append(val.get_slot());
				sb.append(", ENCHANT=").append(val.get_enchant());
				sb.append(", BLESS=").append(val.get_bless());
				if (val.has_elemental_type()) {
					sb.append(", ELEMENTAL_TYPE=").append(val.get_elemental_type());
				}
				if (val.has_elemental_level()) {
					sb.append(", ELEMENTAL_LEVEL=").append(val.get_elemental_level());
				}
				sb.append(", DESC=").append(val.get_desc());
				sb.append(", DESC_KR=").append(DescKLoader.getDesc(val.get_desc()));
				if (val.has_system_desc()) {
					sb.append(", SYSTEM_DESC=").append(val.get_system_desc());
				}
				if (val.has_broadcast_desc()) {
					sb.append(", BROAD_DESC=").append(val.get_broadcast_desc());
				}
				sb.append(", ICON=").append(val.get_iconId());
				if (val.has_url()) {
					sb.append(", URL=").append(val.get_url());
				}
				if (val.has_inherit_enchant_from()) {
					sb.append(", INHERIT_ENCHANT_FROM=").append(val.get_inherit_enchant_from());
				}
				if (val.has_inherit_elemental_enchant_from()) {
					sb.append(", INHERIT_ELEMENT_FROM=").append(val.get_inherit_elemental_enchant_from());
				}
				if (val.has_event_id()) {
					sb.append(", EVENT_ID=").append(val.get_event_id());
				}
				if (val.has_inherit_bless_from()) {
					sb.append(", INHERIT_BLESS_FROM=").append(val.get_inherit_bless_from());
				}
				if (val.has_attribute_bit_set()) {
					sb.append(", ATTRIBUTE_BIT_SET=").append(val.get_attribute_bit_set());
				}
				if (val.has_attribute_bit_set_ex()) {
					sb.append(", ATTRIBUTE_BIT_SET_EX=").append(val.get_attribute_bit_set_ex());
				}
			}
		}
		
		java.util.LinkedList<CraftEvent> events = _success.get_events();
		if (events != null && !events.isEmpty()) {
			sb.append("\r\nEVENTS START ============>");
			for (CraftEvent val : events) {
				sb.append("\r\nEVENT_ID=").append(val.get_event_id());
				sb.append(", PROB_COUNT=").append(val.get_prob_count());
				sb.append(", NON_PROB_COUNT=").append(val.get_non_prob_count());
				
				java.util.LinkedList<CraftOutputItem> output_prob_item_list = val.get_output_items();
				if (output_prob_item_list != null && !output_prob_item_list.isEmpty()) {
					sb.append("\r\n============= PROB ITEMS START ============>");
					for (CraftOutputItem out : output_prob_item_list) {
						sb.append("\r\nNAME_ID=").append(out.get_name_id());
						sb.append(", COUNT=").append(out.get_count());
						sb.append(", PROB=").append(out.get_slot());
						sb.append(", ENCHANT=").append(out.get_enchant());
						sb.append(", BLESS=").append(out.get_bless());
						if (out.has_elemental_type()) {
							sb.append(", ELEMENTAL_TYPE=").append(out.get_elemental_type());
						}
						if (out.has_elemental_level()) {
							sb.append(", ELEMENTAL_LEVEL=").append(out.get_elemental_level());
						}
						sb.append(", DESC=").append(out.get_desc());
						sb.append(", DESC_KR=").append(DescKLoader.getDesc(out.get_desc()));
						if (out.has_system_desc()) {
							sb.append(", SYSTEM_DESC=").append(out.get_system_desc());
						}
						if (out.has_broadcast_desc()) {
							sb.append(", BROAD_DESC=").append(out.get_broadcast_desc());
						}
						sb.append(", ICON=").append(out.get_iconId());
						if (out.has_url()) {
							sb.append(", URL=").append(out.get_url());
						}
						if (out.has_inherit_enchant_from()) {
							sb.append(", INHERIT_ENCHANT_FROM=").append(out.get_inherit_enchant_from());
						}
						if (out.has_inherit_elemental_enchant_from()) {
							sb.append(", INHERIT_ELEMENT_FROM=").append(out.get_inherit_elemental_enchant_from());
						}
						if (out.has_event_id()) {
							sb.append(", EVENT_ID=").append(out.get_event_id());
						}
						if (out.has_inherit_bless_from()) {
							sb.append(", INHERIT_BLESS_FROM=").append(out.get_inherit_bless_from());
						}
						if (out.has_attribute_bit_set()) {
							sb.append(", ATTRIBUTE_BIT_SET=").append(out.get_attribute_bit_set());
						}
						if (out.has_attribute_bit_set_ex()) {
							sb.append(", ATTRIBUTE_BIT_SET_EX=").append(out.get_attribute_bit_set_ex());
						}
					}
					sb.append("\r\n============= END =============");
				}
				
				java.util.LinkedList<CraftOutputItem> _output_items = val.get_output_items();
				if (_output_items != null && !_output_items.isEmpty()) {
					sb.append("\r\n============= ITEMS START ============>");
					for (CraftOutputItem out : _output_items) {
						sb.append("\r\nNAME_ID=").append(out.get_name_id());
						sb.append(", COUNT=").append(out.get_count());
						sb.append(", PROB=").append(out.get_slot());
						sb.append(", ENCHANT=").append(out.get_enchant());
						sb.append(", BLESS=").append(out.get_bless());
						if (out.has_elemental_type()) {
							sb.append(", ELEMENTAL_TYPE=").append(out.get_elemental_type());
						}
						if (out.has_elemental_level()) {
							sb.append(", ELEMENTAL_LEVEL=").append(out.get_elemental_level());
						}
						sb.append(", DESC=").append(out.get_desc());
						sb.append(", DESC_KR=").append(DescKLoader.getDesc(out.get_desc()));
						if (out.has_system_desc()) {
							sb.append(", SYSTEM_DESC=").append(out.get_system_desc());
						}
						if (out.has_broadcast_desc()) {
							sb.append(", BROAD_DESC=").append(out.get_broadcast_desc());
						}
						sb.append(", ICON=").append(out.get_iconId());
						if (out.has_url()) {
							sb.append(", URL=").append(out.get_url());
						}
						if (out.has_inherit_enchant_from()) {
							sb.append(", INHERIT_ENCHANT_FROM=").append(out.get_inherit_enchant_from());
						}
						if (out.has_inherit_elemental_enchant_from()) {
							sb.append(", INHERIT_ELEMENT_FROM=").append(out.get_inherit_elemental_enchant_from());
						}
						if (out.has_event_id()) {
							sb.append(", EVENT_ID=").append(out.get_event_id());
						}
						if (out.has_inherit_bless_from()) {
							sb.append(", INHERIT_BLESS_FROM=").append(out.get_inherit_bless_from());
						}
						if (out.has_attribute_bit_set()) {
							sb.append(", ATTRIBUTE_BIT_SET=").append(out.get_attribute_bit_set());
						}
						if (out.has_attribute_bit_set_ex()) {
							sb.append(", ATTRIBUTE_BIT_SET_EX=").append(out.get_attribute_bit_set_ex());
						}
					}
					sb.append("\r\n============= END =============");
				}
			}
			sb.append("\r\nEVENTS END =============");
		}
		sb.append("\r\nEVENT_COUNT: ").append(_success.get_event_count());
		return sb.toString();
	}

	public boolean has_success() {
		return (_bit & 0x1) == 0x1;
	}

	public CraftOutputSFList get_failure() {
		return _failure;
	}

	public void set_failure(CraftOutputSFList val) {
		_bit |= 0x2;
		_failure = val;
	}
	
	public String get_failure_toString() {
		if (_failure == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		CraftQuestFlag on_flag = _failure.get_on_flag();
		if (on_flag != null) {
			sb.append("ON_FLAG: ").append("FIRST=").append(on_flag.get_flag1()).append(", SECOND=").append(on_flag.get_flag2());
		}
		
		CraftQuestFlag off_flag = _failure.get_off_flag();
		if (off_flag != null) {
			sb.append("\r\nOFF_FLAG: ").append("FIRST=").append(off_flag.get_flag1()).append(", SECOND=").append(off_flag.get_flag2());
		}
		
		sb.append("\r\nPROB_COUNT: ").append(_failure.get_prob_count());
		sb.append("\r\nNON_PROB_COUNT: ").append(_failure.get_non_prob_count());
		
		java.util.LinkedList<CraftOutputItem> output_prob_items = _failure.get_output_prob_items();
		if (output_prob_items != null && !output_prob_items.isEmpty()) {
			sb.append("\r\nOUTPUT_PROB_ITEMS ============>");
			for (CraftOutputItem val : output_prob_items) {
				sb.append("\r\nNAME_ID=").append(val.get_name_id());
				sb.append(", COUNT=").append(val.get_count());
				sb.append(", PROB=").append(val.get_slot());
				sb.append(", ENCHANT=").append(val.get_enchant());
				sb.append(", BLESS=").append(val.get_bless());
				if (val.has_elemental_type()) {
					sb.append(", ELEMENTAL_TYPE=").append(val.get_elemental_type());
				}
				if (val.has_elemental_level()) {
					sb.append(", ELEMENTAL_LEVEL=").append(val.get_elemental_level());
				}
				sb.append(", DESC=").append(val.get_desc());
				sb.append(", DESC_KR=").append(DescKLoader.getDesc(val.get_desc()));
				if (val.has_system_desc()) {
					sb.append(", SYSTEM_DESC=").append(val.get_system_desc());
				}
				if (val.has_broadcast_desc()) {
					sb.append(", BROAD_DESC=").append(val.get_broadcast_desc());
				}
				sb.append(", ICON=").append(val.get_iconId());
				if (val.has_url()) {
					sb.append(", URL=").append(val.get_url());
				}
				if (val.has_inherit_enchant_from()) {
					sb.append(", INHERIT_ENCHANT_FROM=").append(val.get_inherit_enchant_from());
				}
				if (val.has_inherit_elemental_enchant_from()) {
					sb.append(", INHERIT_ELEMENT_FROM=").append(val.get_inherit_elemental_enchant_from());
				}
				if (val.has_event_id()) {
					sb.append(", EVENT_ID=").append(val.get_event_id());
				}
				if (val.has_inherit_bless_from()) {
					sb.append(", INHERIT_BLESS_FROM=").append(val.get_inherit_bless_from());
				}
				if (val.has_attribute_bit_set()) {
					sb.append(", ATTRIBUTE_BIT_SET=").append(val.get_attribute_bit_set());
				}
				if (val.has_attribute_bit_set_ex()) {
					sb.append(", ATTRIBUTE_BIT_SET_EX=").append(val.get_attribute_bit_set_ex());
				}
			}
		}
		
		java.util.LinkedList<CraftOutputItem> output_items = _failure.get_output_items();
		if (output_items != null && !output_items.isEmpty()) {
			sb.append("\r\nOUTPUT_ITEMS ============>");
			for (CraftOutputItem val : output_items) {
				sb.append("\r\nNAME_ID=").append(val.get_name_id());
				sb.append(", COUNT=").append(val.get_count());
				sb.append(", PROB=").append(val.get_slot());
				sb.append(", ENCHANT=").append(val.get_enchant());
				sb.append(", BLESS=").append(val.get_bless());
				if (val.has_elemental_type()) {
					sb.append(", ELEMENTAL_TYPE=").append(val.get_elemental_type());
				}
				if (val.has_elemental_level()) {
					sb.append(", ELEMENTAL_LEVEL=").append(val.get_elemental_level());
				}
				sb.append(", DESC=").append(val.get_desc());
				sb.append(", DESC_KR=").append(DescKLoader.getDesc(val.get_desc()));
				if (val.has_system_desc()) {
					sb.append(", SYSTEM_DESC=").append(val.get_system_desc());
				}
				if (val.has_broadcast_desc()) {
					sb.append(", BROAD_DESC=").append(val.get_broadcast_desc());
				}
				sb.append(", ICON=").append(val.get_iconId());
				if (val.has_url()) {
					sb.append(", URL=").append(val.get_url());
				}
				if (val.has_inherit_enchant_from()) {
					sb.append(", INHERIT_ENCHANT_FROM=").append(val.get_inherit_enchant_from());
				}
				if (val.has_inherit_elemental_enchant_from()) {
					sb.append(", INHERIT_ELEMENT_FROM=").append(val.get_inherit_elemental_enchant_from());
				}
				if (val.has_event_id()) {
					sb.append(", EVENT_ID=").append(val.get_event_id());
				}
				if (val.has_inherit_bless_from()) {
					sb.append(", INHERIT_BLESS_FROM=").append(val.get_inherit_bless_from());
				}
				if (val.has_attribute_bit_set()) {
					sb.append(", ATTRIBUTE_BIT_SET=").append(val.get_attribute_bit_set());
				}
				if (val.has_attribute_bit_set_ex()) {
					sb.append(", ATTRIBUTE_BIT_SET_EX=").append(val.get_attribute_bit_set_ex());
				}
			}
		}
		
		java.util.LinkedList<CraftEvent> events = _failure.get_events();
		if (events != null && !events.isEmpty()) {
			sb.append("\r\nEVENTS START ============>");
			for (CraftEvent val : events) {
				sb.append("\r\nEVENT_ID=").append(val.get_event_id());
				sb.append(", PROB_COUNT=").append(val.get_prob_count());
				sb.append(", NON_PROB_COUNT=").append(val.get_non_prob_count());
				
				java.util.LinkedList<CraftOutputItem> output_prob_item_list = val.get_output_items();
				if (output_prob_item_list != null && !output_prob_item_list.isEmpty()) {
					sb.append("\r\n============= PROB ITEMS START ============>");
					for (CraftOutputItem out : output_prob_item_list) {
						sb.append("\r\nNAME_ID=").append(out.get_name_id());
						sb.append(", COUNT=").append(out.get_count());
						sb.append(", PROB=").append(out.get_slot());
						sb.append(", ENCHANT=").append(out.get_enchant());
						sb.append(", BLESS=").append(out.get_bless());
						if (out.has_elemental_type()) {
							sb.append(", ELEMENTAL_TYPE=").append(out.get_elemental_type());
						}
						if (out.has_elemental_level()) {
							sb.append(", ELEMENTAL_LEVEL=").append(out.get_elemental_level());
						}
						sb.append(", DESC=").append(out.get_desc());
						sb.append(", DESC_KR=").append(DescKLoader.getDesc(out.get_desc()));
						if (out.has_system_desc()) {
							sb.append(", SYSTEM_DESC=").append(out.get_system_desc());
						}
						if (out.has_broadcast_desc()) {
							sb.append(", BROAD_DESC=").append(out.get_broadcast_desc());
						}
						sb.append(", ICON=").append(out.get_iconId());
						if (out.has_url()) {
							sb.append(", URL=").append(out.get_url());
						}
						if (out.has_inherit_enchant_from()) {
							sb.append(", INHERIT_ENCHANT_FROM=").append(out.get_inherit_enchant_from());
						}
						if (out.has_inherit_elemental_enchant_from()) {
							sb.append(", INHERIT_ELEMENT_FROM=").append(out.get_inherit_elemental_enchant_from());
						}
						if (out.has_event_id()) {
							sb.append(", EVENT_ID=").append(out.get_event_id());
						}
						if (out.has_inherit_bless_from()) {
							sb.append(", INHERIT_BLESS_FROM=").append(out.get_inherit_bless_from());
						}
						if (out.has_attribute_bit_set()) {
							sb.append(", ATTRIBUTE_BIT_SET=").append(out.get_attribute_bit_set());
						}
						if (out.has_attribute_bit_set_ex()) {
							sb.append(", ATTRIBUTE_BIT_SET_EX=").append(out.get_attribute_bit_set_ex());
						}
					}
					sb.append("\r\n============= END =============");
				}
				
				java.util.LinkedList<CraftOutputItem> _output_items = val.get_output_items();
				if (_output_items != null && !_output_items.isEmpty()) {
					sb.append("\r\n============= ITEMS START ============>");
					for (CraftOutputItem out : _output_items) {
						sb.append("\r\nNAME_ID=").append(out.get_name_id());
						sb.append(", COUNT=").append(out.get_count());
						sb.append(", PROB=").append(out.get_slot());
						sb.append(", ENCHANT=").append(out.get_enchant());
						sb.append(", BLESS=").append(out.get_bless());
						if (out.has_elemental_type()) {
							sb.append(", ELEMENTAL_TYPE=").append(out.get_elemental_type());
						}
						if (out.has_elemental_level()) {
							sb.append(", ELEMENTAL_LEVEL=").append(out.get_elemental_level());
						}
						sb.append(", DESC=").append(out.get_desc());
						sb.append(", DESC_KR=").append(DescKLoader.getDesc(out.get_desc()));
						if (out.has_system_desc()) {
							sb.append(", SYSTEM_DESC=").append(out.get_system_desc());
						}
						if (out.has_broadcast_desc()) {
							sb.append(", BROAD_DESC=").append(out.get_broadcast_desc());
						}
						sb.append(", ICON=").append(out.get_iconId());
						if (out.has_url()) {
							sb.append(", URL=").append(out.get_url());
						}
						if (out.has_inherit_enchant_from()) {
							sb.append(", INHERIT_ENCHANT_FROM=").append(out.get_inherit_enchant_from());
						}
						if (out.has_inherit_elemental_enchant_from()) {
							sb.append(", INHERIT_ELEMENT_FROM=").append(out.get_inherit_elemental_enchant_from());
						}
						if (out.has_event_id()) {
							sb.append(", EVENT_ID=").append(out.get_event_id());
						}
						if (out.has_inherit_bless_from()) {
							sb.append(", INHERIT_BLESS_FROM=").append(out.get_inherit_bless_from());
						}
						if (out.has_attribute_bit_set()) {
							sb.append(", ATTRIBUTE_BIT_SET=").append(out.get_attribute_bit_set());
						}
						if (out.has_attribute_bit_set_ex()) {
							sb.append(", ATTRIBUTE_BIT_SET_EX=").append(out.get_attribute_bit_set_ex());
						}
					}
					sb.append("\r\n============= END =============");
				}
			}
			sb.append("\r\nEVENTS END =============");
		}
		
		sb.append("\r\nEVENT_COUNT: ").append(_failure.get_event_count());
		return sb.toString();
	}

	public boolean has_failure() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_success_prob_by_million() {
		return _success_prob_by_million;
	}

	public void set_success_prob_by_million(int val) {
		_bit |= 0x4;
		_success_prob_by_million = val;
	}

	public boolean has_success_prob_by_million() {
		return (_bit & 0x4) == 0x4;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_success()){
			size += ProtoOutputStream.computeMessageSize(1, _success);
		}
		if (has_failure()){
			size += ProtoOutputStream.computeMessageSize(2, _failure);
		}
		if (has_success_prob_by_million()){
			size += ProtoOutputStream.computeInt32Size(3, _success_prob_by_million);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_success()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_failure()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_success_prob_by_million()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_success()){
			output.writeMessage(1, _success);
		}
		if (has_failure()){
			output.writeMessage(2, _failure);
		}
		if (has_success_prob_by_million()){
			output.wirteInt32(3, _success_prob_by_million);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x0000000A: {
				set_success((CraftOutputSFList) input.readMessage(CraftOutputSFList.newInstance()));
				break;
			}
			case 0x00000012: {
				set_failure((CraftOutputSFList) input.readMessage(CraftOutputSFList.newInstance()));
				break;
			}
			case 0x00000018: {
				set_success_prob_by_million(input.readInt32());
				break;
			}
			default: {
				System.out.println(String.format("[CraftOutputList] NEW_TAG : TAG(%d)", tag));
				return this;
			}
			}
		}
		return this;
	}

	@Override
	public void dispose() {
		if (has_success() && _success != null) {
			_success.dispose();
			_success = null;
		}
		if (has_failure() && _failure != null) {
			_failure.dispose();
			_failure = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

}

