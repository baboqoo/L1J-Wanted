package l1j.server.server.model.item.collection.favor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.common.bin.favorbook.AUBIBookInfoForClient;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.L1CollectionHandler;
import l1j.server.server.model.item.collection.L1CollectionLoader;
import l1j.server.server.model.item.collection.L1CollectionModel;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookCategoryObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookUserLoader;
import l1j.server.server.monitor.Logger.FavorType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.inventory.S_FavorBookActivatedSetBonusNoti;
import l1j.server.server.serverpackets.inventory.S_FavorBookEngraveNoti;
import l1j.server.server.serverpackets.inventory.S_FavorBookEngraveNoti.FavorEngraveType;
import l1j.server.server.serverpackets.inventory.S_FavorBookListNoti;

/**
 * 유저에게 할당된 성물 인벤토리
 * @author LinOffice
 */
public class L1FavorBookInventory {
	private final L1PcInstance owner;
	private final L1CollectionHandler collection;
	private final ConcurrentHashMap<L1FavorBookCategoryObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>> mapData;// key: category, value: key: slotid, value: obj
	private final ArrayList<L1FavorBookUserObject> listData;
	private final ConcurrentHashMap<Integer, ServerBasePacket> packets;// key: listId, value: packet

	/**
	 * 생성자
	 * @param owner
	 */
	public L1FavorBookInventory(L1PcInstance owner) {
		this.owner		= owner;
		this.collection	= owner.getInventory().getCollection();
		this.mapData	= new ConcurrentHashMap<>();
		this.listData	= new ArrayList<>();
		this.packets	= new ConcurrentHashMap<>();
		init();
	}
	
	/**
	 * 목록 패킷 반환
	 * @param listId
	 * @return ServerBasePacket
	 */
	public ServerBasePacket getListPacket(int listId){
		return packets.get(listId);
	}
	
	/**
	 * 등록되어 있는 모든 성물 정보를 조사한다.
	 * @return ArrayList<L1FavorBookUserObject>
	 */
	public ArrayList<L1FavorBookUserObject> getList(){
		return listData;
	}
	
	/**
	 * 전체 성물 데이터를 조사한다.
	 * @return map
	 */
	public ConcurrentHashMap<L1FavorBookCategoryObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>> getData(){
		return mapData;
	}
	
	/**
	 * 카테고리별 성물 목록을 조사한다.
	 * @param category
	 * @return map
	 */
	public ConcurrentHashMap<Integer, L1FavorBookUserObject> getCategoryMap(L1FavorBookCategoryObject category){
		return mapData.get(category);
	}
	
	/**
	 * 등록된 성물을 조사한다.
	 * @param category
	 * @param slotId
	 * @return L1FavorBookUserObject
	 */
	public L1FavorBookUserObject getUser(L1FavorBookCategoryObject category, int slotId){
		ConcurrentHashMap<Integer, L1FavorBookUserObject> categoryMap = getCategoryMap(category);
		if (categoryMap == null) {
			return null;
		}
		return categoryMap.get(slotId);
	}
	
	/**
	 * 성물 인벤토리에 새 데이터을 추가한다.
	 * @param favor
	 * @return L1FavorBookUserObject
	 */
	private L1FavorBookUserObject create(L1FavorBookObject favor){
		AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT stateT = favor.getSlotT().get_state_infos().getFirst();
		L1FavorBookUserObject user = new L1FavorBookUserObject(favor.getCategory(), favor.getSlotId(), stateT.get_craft_id(), stateT.get_awakening(), favor);
		ConcurrentHashMap<Integer, L1FavorBookUserObject> categoryMap = mapData.get(user.getCategory());
		if (categoryMap == null) {
			categoryMap = new ConcurrentHashMap<>();
			mapData.put(user.getCategory(), categoryMap);
		}
		categoryMap.put(user.getSlotId(), user);
		listData.add(user);
		return user;
	}
	
	/**
	 * 성물 인벤토리에 아이템을 적재한다.
	 * 등록된 성물에 대한 패킷을 보낸다.
	 * 성물 인벤토리 적재 로그를 남긴다.
	 * @param favor
	 * @param item
	 * @return boolean
	 */
	public boolean offer(L1FavorBookObject favor, L1ItemInstance item){
		// TODO 성물 인벤토리 등록(실제 유저 인벤토리에는 생성하지 않는다)
		if (!Config.COLLECTION.FAVOR_BOOK_ACTIVE) {
			return false;
		}
		L1FavorBookUserObject user = getUser(favor.getCategory(), favor.getSlotId());
		if (user == null) {
			user = create(favor);
		}
		
		// 봉인 아이템
		if (item.getBless() >= 128) {
			item.setBless(item.getBless() - 128);
		}
		item.setIdentified(true);
		user.setCurrentItem(item);
		
		// 다음 상태 정보
		AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT stateT = favor.getSlotT().get_next_state_info(user.getCategory().getCategory(), user.getCraftId(), item.getItem());
		if (stateT != null) {
			user.setCraftId(stateT.get_craft_id());
			user.setAwakening(stateT.get_awakening());
		}
		
		if (L1FavorBookUserLoader.getInstance().insert(owner, user)) {
			owner.sendPackets(new S_FavorBookEngraveNoti(FavorEngraveType.ADD, owner, user, false), true);
			LoggerInstance.getInstance().addFavorBook(FavorType.OFFER, owner, user);
			updateListPacket();// 목록 패킷 갱신
			return true;
		}
		return false;
	}
	
	/**
	 * 성물 인벤토리에서 등록된 아이템을 꺼낸다.(제작재료)
	 * @param user
	 * @param favorItem
	 * @param pollCount(꺼낼 수량 : 현재 1개씩이지만 추후 여러개로 변경시 처리된다.)
	 */
	public void poll(L1FavorBookUserObject user, L1ItemInstance favorItem, int pollCount){
		int afterCount = favorItem.getCount() - pollCount;
		// 꺼낸후 수량이 남음
		if (afterCount > 0) {
			favorItem.setCount(afterCount);
			LoggerInstance.getInstance().addFavorBook(FavorType.POLL, owner, user);
			return;
		}
		// 등록된 성물의 옵션을 제거한다.
		L1CollectionModel model = L1CollectionLoader.getCollection(favorItem.getItemId());
        if (model != null) {
        	collection.collectionAblity(favorItem, model, false);
        }
        // 등록된 아이템을 꺼낸다.
        LoggerInstance.getInstance().addFavorBook(FavorType.POLL, owner, user);
        user.setCurrentItem(null);
	}
	
	/**
	 * 성물 인벤토리에서 등록된 성물을 제거한다.(기간제한)
	 * @param user
	 */
	public void delete(L1FavorBookUserObject user){
		L1ItemInstance favorItem = user.getCurrentItem();
		if (favorItem != null) {
			// 등록된 성물의 옵션을 제거한다.
			L1CollectionModel model = L1CollectionLoader.getCollection(favorItem.getItemId());
	        if (model != null) {
	        	collection.collectionAblity(favorItem, model, false);
	        }
		}
		if (L1FavorBookUserLoader.getInstance().delete(owner, user)) {
        	ConcurrentHashMap<Integer, L1FavorBookUserObject> map = getCategoryMap(user.getCategory());
        	if (map == null) {
        		return;
        	}
        	map.remove(user.getSlotId());
	        listData.remove(user);
	        LoggerInstance.getInstance().addFavorBook(FavorType.DELETE, owner, user);
	        updateListPacket();// 목록 패킷 갱신
        }
	}
	
	/**
	 * 인벤토리에 변화가 발생시 목록 패킷을 최신으로 갱신한다.
	 */
	private void updateListPacket(){
		clearListPacket();
		packets.put(0,	new S_FavorBookListNoti(owner, this, 0));
		packets.put(1,	new S_FavorBookListNoti(owner, this, 1));
		packets.put(2,	new S_FavorBookListNoti(owner, this, 2));
	}
	
	/**
	 * 목록 패킷을 제거한다.
	 */
	private void clearListPacket(){
		ServerBasePacket pck = packets.get(0);
		if (pck != null) {
			pck.clear();
			pck = null;
		}
		pck	= packets.get(1);
		if (pck != null) {
			pck.clear();
			pck = null;
		}
		pck	= packets.get(2);
		if (pck != null) {
			pck.clear();
			pck = null;
		}
		packets.clear();
	}
	
	/**
	 * 최초 생성시 default 처리
	 */
	private void init(){
		// TODO 로그인 처리
		ArrayList<L1FavorBookUserObject> userList = L1FavorBookUserLoader.getFavorUserList(owner.getId());// 등록되어 있는 성물 정보
		if (userList != null && !userList.isEmpty()) {
			for (L1FavorBookUserObject user : userList) {
				ConcurrentHashMap<Integer, L1FavorBookUserObject> categoryMap = mapData.get(user.getCategory());
				if (categoryMap == null) {
					categoryMap = new ConcurrentHashMap<>();
					mapData.put(user.getCategory(), categoryMap);
				}
				
				// 등록된 성물의 옵션을 부여한다.
				L1CollectionModel model = L1CollectionLoader.getCollection(user.getCurrentItem().getItemId());
				if (model != null) {
					collection.collectionAblity(user.getCurrentItem(), model, true);
				}
				
				categoryMap.put(user.getSlotId(), user);
				listData.add(user);
			}
		}
		updateListPacket();
		sendLoginPacket();
	}
	
	/**
	 * 로그인 출력 패킷
	 */
	private void sendLoginPacket(){
		if (!listData.isEmpty()) {
			for (L1FavorBookUserObject user : listData) {
				owner.sendPackets(new S_FavorBookEngraveNoti(FavorEngraveType.ADD, owner, user, true), true);// 등록되어 있는 인벤토리 정보
			}
		}
		owner.sendPackets(S_FavorBookActivatedSetBonusNoti.NOTI);
	}
	
	/**
	 * 메모리 정리
	 */
	public void dispose(){
		mapData.clear();
		listData.clear();
		clearListPacket();
	}
}

