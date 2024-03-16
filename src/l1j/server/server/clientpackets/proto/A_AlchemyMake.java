package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1Doll;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.alchemy.S_AlchemyExtraInfo;
import l1j.server.server.serverpackets.alchemy.S_AlchemyDesign;
import l1j.server.server.serverpackets.alchemy.S_AlchemyMake;
import l1j.server.server.serverpackets.message.S_MessegeNoti;
import l1j.server.server.utils.CommonUtil;

public class A_AlchemyMake extends ProtoHandler {
	protected A_AlchemyMake(){}
	private A_AlchemyMake(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _alchemy_id;
	private int[] inputIds;
	private java.util.LinkedList<L1ItemInstance> inputs;
	private boolean isMainInput;
	private L1ItemInstance mainInput;
	private S_AlchemyMake.ResultCode resultCode;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		int total = (_total_length - 2) / 12;// 전체길이
		readP(1);// 08
		_alchemy_id = readC();
		
		// 사용되는 alchemy_id 검사
		if (!Config.ALCHEMY.ALCHEMY_USED_IDS.contains(_alchemy_id)) {
			return;
		}
		
		isMainInput	= Config.ALCHEMY.ALCHEMY_MAIN_INPUT_IDS.contains(_alchemy_id);
		inputIds	= new int[total];
		L1ItemInstance item = null;
		for (int i = 0; i < total; i++) {
			readP(3);// 12 길이 08
			int slot_no = readC();// 인덱스
			readP(1);// 10
			read4(read_size());// 데스크아이디
			readP(1);// 18
			int objid = read4(read_size());

			item = _pc.getInventory().getItem(objid);
			if (item == null) {
				return;
			}
			if (isMainInput && slot_no == 1) {
				mainInput = item;
			}
			inputIds[i] = item.getItemId();
			if (inputs == null) {
				inputs = new java.util.LinkedList<L1ItemInstance>();
			}
			inputs.add(item);
		}
		
		// TODO 합성 결과
		boolean result = CommonUtil.random(100) + 1 <= getFusionProb(total);
		item = result ? getSuccessOutput() : getFailureOutput();
		
		// 생성할 아이템 검증
		if (item == null) {
			System.out.println(String.format(
					"[A_AlchemyMake] OUTPUT_ITEM_EMPTY : ALCHEMY_ID(%d), RESULT(%b), CHAR_NAME(%s)", 
					_alchemy_id, result, _pc.getName()));
			return;
		}
		
		// 재료 소모 검증
		if (!consumeInputs()) {
			System.out.println(String.format(
					"[A_AlchemyMake] INPUTS_CONSUME_FAILURE : ALCHEMY_ID(%d), RESULT(%b), CHAR_NAME(%s)", 
					_alchemy_id, result, _pc.getName()));
			return;
		}
		
		// 성공 멘트 출력
		if (result && Config.ALCHEMY.ALCHEMY_SUCCESS_MENT_IDS.contains(_alchemy_id)) {
			GeneralThreadPool.getInstance().schedule(new DollFusionMent(item), 10000L);
		}
		
		// 잠재력 이전
		if (Config.ALCHEMY.ALCHEMY_POTENTIAL_TRANS_IDS.contains(_alchemy_id) && mainInput.getPotential() != null) {
			transPotential(item, mainInput);
		}
		
		// 축복받은 인형 획득시 확인 상태 생성
		if (item.getBless() == 0 && !item.isIdentified()) {
			item.setIdentified(true);
		}
		
		if (_pc.getInventory().checkAddItem(item, 1) != L1Inventory.OK) return;
		_pc.getInventory().storeItem(item);
		_pc.sendPackets(new S_AlchemyMake(resultCode, item), true);
		_pc.sendPackets(new S_AlchemyExtraInfo(_pc.getDoll()), true);
		_pc.sendPackets(S_AlchemyDesign.SAME_HASH);
	    LoggerInstance.getInstance().addAlchemy(_pc, _alchemy_id, result, item);
	}
	
	/**
	 * 슬롯에 등록한 재료 소모
	 */
	boolean consumeInputs() {
		if (inputs == null || inputs.isEmpty()) {
			return false;
		}
		// 상급 인형 교체 수수료
		if (_alchemy_id == 11 && !_pc.getInventory().consumeItem(L1ItemId.ADENA, 100000000)) {
			return false;
		}
		for (L1ItemInstance input : inputs) {
			_pc.getInventory().removeItem(input);
		}
		inputs.clear();
		inputs = null;
		return true;
	}
	
	/**
	 * 합성 성공 생성 아이템
	 * @return L1ItemInstance
	 */
	L1ItemInstance getSuccessOutput() {
		resultCode = S_AlchemyMake.ResultCode.RC_SUCCESS;
		// 성공시 생성할 아이템 리스트 설정
		if (_alchemy_id <= L1Doll.SUCCESS_ALCHEMY_DOLL_IDS.length) {
			// 하이퍼 발동(대성공)
			if (_alchemy_id <= 3 && inputIds.length == 4 && CommonUtil.random(100) + 1 <= Config.ALCHEMY.ALCHEMY_HYPER_PROB) {
				resultCode	= S_AlchemyMake.ResultCode.RC_HYPER_SUCCESS;
				inputIds	= L1Doll.SUCCESS_ALCHEMY_DOLL_IDS[_alchemy_id];
			} else {
				inputIds	= L1Doll.SUCCESS_ALCHEMY_DOLL_IDS[_alchemy_id - 1];
			}
		}
		
		if (_alchemy_id == 11) {// 교체
			int changeItemId = mainInput.getItemId();
			while (changeItemId == mainInput.getItemId()) {
				changeItemId = inputIds[CommonUtil.random(inputIds.length)];// 교체
			}
			return ItemTable.getInstance().createItem(changeItemId);
		}
		if (_alchemy_id == 12 || _alchemy_id == 15) {// 상급 합성 4단계(일반)[이벤트], 축복 드래곤 -> 주재료와 동일한 축복
			return ItemTable.getInstance().createItem(MagicDollInfoTable.getDollInfo(mainInput.getItemId()).getBlessItemId());
		}
		return ItemTable.getInstance().createItem(inputIds[CommonUtil.random(inputIds.length)]);
	}
	
	/**
	 * 합성 실패 생성 아이템
	 * @return L1ItemInstance
	 */
	L1ItemInstance getFailureOutput() {
		resultCode = S_AlchemyMake.ResultCode.RC_FAIL;
		if (_alchemy_id >= 5 && _alchemy_id <= 7) {// 특수 합성 실패시 100프로 나오도록
			inputIds = getSpecialFailOutputIds();
		}
		return isMainInput ? mainInput : ItemTable.getInstance().createItem(inputIds[CommonUtil.random(inputIds.length)]);
	}
	
	/**
	 * 특수 합성(3 ~ 5) 실패시 생성될 아아템 리스트
	 * @return output array
	 */
	int[] getSpecialFailOutputIds(){
		switch (_alchemy_id) {
		case 5:
			return L1Doll.NOMAL_LEVEL_2_IDS;
		case 6:
			return L1Doll.NOMAL_LEVEL_3_IDS;
		case 7:
			return L1Doll.NOMAL_LEVEL_4_IDS;
		default:
			return null;
		}
	}
	
	/**
	 * 단계별 합성 확률
	 * @param slot_size
	 * @return prob
	 */
	int getFusionProb(int slot_size){
		switch(_alchemy_id){
		case 1:
			return slot_size * Config.ALCHEMY.ALCHEMY_1_PROB;// 1단계
		case 2:
			return slot_size * Config.ALCHEMY.ALCHEMY_2_PROB;// 2단계
		case 3:
			return slot_size * Config.ALCHEMY.ALCHEMY_3_PROB;// 3단계
		case 4:
			return slot_size * Config.ALCHEMY.ALCHEMY_4_PROB;// 4단계
		case 11:
			return 100;// 교체 확률
		case 15:
			return slot_size * Config.ALCHEMY.ALCHEMY_15_PROB;// 축복 드래곤
		case 16:
			return slot_size * Config.ALCHEMY.ALCHEMY_16_PROB;// 5단계(축복 드래곤)
		default:
			return slot_size * Config.ALCHEMY.ALCHEMY_DEFAULT_PROB;
		}
	}
	
	/**
	 * 잠재력 이전
	 * @param createItem
	 * @param consumeItem
	 */
	void transPotential(L1ItemInstance createItem, L1ItemInstance consumeItem){
		createItem.setPotential(consumeItem.getPotential());
	}
	
	class DollFusionMent implements Runnable {
		private L1ItemInstance _item;
		public DollFusionMent(L1ItemInstance item) {
			_item	= item;
		}
		
		@Override
		public void run() {
			if (_pc == null || _item == null) {
				return;
			}
			S_MessegeNoti message = new S_MessegeNoti(4433, _item.getItem().getDesc(), _item.getItem().getItemNameId());// 누군가가 %s 합성에 성공하였습니다.
			if (_pc.getConfig().isGlobalMessege()) {
				_pc.sendPackets(message, false);
			}
			L1World.getInstance().broadcastPacket(_pc, message, true);
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_AlchemyMake(data, client);
	}

}

