package l1j.server.server.model.item.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;

/**
 * Inventory Collection System Handler
 * @author LinOffice
 */
public class L1CollectionHandler {
	private final L1PcInventory _inventory;
	private final ConcurrentHashMap<Integer, Boolean> _collection;
	
	private boolean _sasinGraceStatus;
	public boolean isSasinGraceStatus(){
		return _sasinGraceStatus;
	}
	
	public L1CollectionHandler(L1PcInventory inventory) {
		_inventory	= inventory;
		_collection	= new ConcurrentHashMap<Integer, Boolean>();
		init();
	}
	
	/**
	 * 컬렉션 상태
	 * @param type
	 * @return boolean
	 */
	public boolean getStatus(int type){
		return !_collection.containsKey(type) ? false : _collection.get(type);
	}
	
	/**
	 * 컬렉션 능력치 설정
	 * @param item
	 * @param model
	 * @param boolean
	 */
	public void collectionAblity(L1ItemInstance item, L1CollectionModel model, boolean push){
		if (_inventory.checkItem(model.getItemId())) {
			return;// 같은아이템 보유시 제외
		}
		boolean status = getStatus(model.getType());
		if (push == status) {
			return;// 이미 같은 상태인경우 중단
		}
		if (model.getSameItems() != null && _inventory.checkItemOne(model.getSameItems())) {
			return;// 같은 타입의 컬렉션 아이템 보유
		}
		_collection.put(model.getType(), !status);// 상태 변경
		if (push) {
			_inventory.setItemAblity(item);
		} else {
			_inventory.removeItemAblity(item);
		}
		int name_id = item.getItem().getItemNameId();
		if (name_id == L1ItemId.SASIN_GRACE) {// 사신의 가호
			_sasinGraceStatus = push;
		} else if (name_id >= 31739 && name_id <= 31747) {// 안샤르의 가호
			_inventory.getOwner().getSkill().doGraceOfAnshar(name_id, push);
		}
	}
	
	/**
	 * 컬렉션 상태 default
	 */
	private void init(){
		for (int type : L1CollectionLoader.getTypes()) {
			_collection.put(type, false);
		}
	}
	
	/**
	 * 컬렉션 상태 reload
	 */
	protected void reload(){
		List<L1ItemInstance> items	= _inventory.getItems();
		ArrayList<L1FavorBookUserObject> favors = _inventory.getOwner().getFavorBook().getList();
		
		L1CollectionModel model		= null;
		for (L1ItemInstance item : items) {
			model = L1CollectionLoader.getCollection(item.getItemId());
			if (model == null || !getStatus(model.getType())) {
				continue;
			}
	    	_collection.put(model.getType(), false);// 상태 변경
	    	_inventory.removeItemAblity(item);
		}
		
		L1ItemInstance favorItem = null;
		for (L1FavorBookUserObject favor : favors) {
			favorItem = favor.getCurrentItem();
			if (favorItem == null) {
				continue;
			}
			model = L1CollectionLoader.getCollection(favorItem.getItemId());
			if (model == null || !getStatus(model.getType())) {
				continue;
			}
	    	_collection.put(model.getType(), false);// 상태 변경
	    	_inventory.removeItemAblity(favorItem);
	    	if (favorItem.getItemId() == L1ItemId.SASIN_GRACE) {
	    		_sasinGraceStatus = false;
			}
		}
		
		init();
		
		for (L1ItemInstance item : items) {
			model = L1CollectionLoader.getCollection(item.getItemId());
			if (model == null || getStatus(model.getType())) {
				continue;
			}
	    	_collection.put(model.getType(), true);// 상태 변경
	    	_inventory.setItemAblity(item);
		}
		
		for (L1FavorBookUserObject favor : favors) {
			favorItem = favor.getCurrentItem();
			if (favorItem == null) {
				continue;
			}
			model = L1CollectionLoader.getCollection(favorItem.getItemId());
			if (model == null || getStatus(model.getType())) {
				continue;
			}
	    	_collection.put(model.getType(), true);// 상태 변경
	    	_inventory.setItemAblity(favorItem);
	    	if (favorItem.getItemId() == L1ItemId.SASIN_GRACE) {
	    		_sasinGraceStatus = true;
			}
		}
	}
}

