package l1j.server.GameSystem.tjcoupon.user;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.GameSystem.tjcoupon.bean.TJCouponBean;

public class TJCouponUser {
	private final ConcurrentHashMap<Integer, TJCouponBean> _map;
	
	/**
	 * 생성자
	 */
	public TJCouponUser() {
		_map	= new ConcurrentHashMap<>(Config.TJ.TJ_COUPON_LOG_SIZE);
	}
	
	/**
	 * 캐릭터의 복구 정보들을 반환
	 * @return FastTable
	 */
	public ArrayList<TJCouponBean> getCoupons(){
		return new ArrayList<>(_map.values());
	}
	
	/**
	 * 캐릭터의 복구 정보 반환(특정)
	 * @param objId
	 * @return TjCouponBean
	 */
	public TJCouponBean getCoupon(int objId){
		return _map.get(objId);
	}
	
	/**
	 * 캐릭터의 복구정보 저장
	 * @param bean
	 */
	public void put(TJCouponBean bean){
		if (_map.containsKey(bean.getObjId())) {
			return;
		}
		if (_map.size() >= Config.TJ.TJ_COUPON_LOG_SIZE) {
			_map.remove(_map.keySet().iterator().next());
		}
		_map.put(bean.getObjId(), bean);
	}
	
	/**
	 * 캐릭터의 복구정보 제거
	 * @param bean
	 */
	public void remove(TJCouponBean bean){
		remove(bean.getObjId());
	}
	
	public void remove(int objId){
		if (!_map.containsKey(objId)) {
			return;
		}
		_map.remove(objId);
	}
}

