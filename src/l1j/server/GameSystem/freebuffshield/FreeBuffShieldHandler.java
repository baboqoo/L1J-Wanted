package l1j.server.GameSystem.freebuffshield;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.GameSystem.arca.L1Arca;
import l1j.server.common.bin.PCMasterCommonBinLoader;
import l1j.server.common.bin.pcmaster.PCMasterInfoForClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.system.DISABLE_FREE_BUFF_SHIELD;
import l1j.server.server.serverpackets.system.FREE_BUFF_SHIELD_INFO;
import l1j.server.server.serverpackets.system.FREE_BUFF_SHIELD_TYPE;
import l1j.server.server.serverpackets.system.S_FreeBuffShieldUpdateNoti;
import l1j.server.server.serverpackets.system.S_PCMasterGoldenBuffUpdateNoti;
import l1j.server.server.utils.CommonUtil;

/**
 * 가호(버프) 핸들러
 * L1CharacterConfig에 할당
 * @author LinOffice
 */
public class FreeBuffShieldHandler {
	protected L1PcInstance _owner;
	protected DISABLE_FREE_BUFF_SHIELD _disable_state;
	protected java.util.LinkedList<FREE_BUFF_SHIELD_INFO> _free_buff_shield_info;
	protected int _pccafe_reward_item_count;
	protected Timestamp _reset_time;
	protected PCMasterInfoForClient _pcmaster_bin;
	protected FavorLockedTimer _favor_locked_timer;
	protected java.util.LinkedList<GoldenBuffInfo> _golden_buff_infos;
	
	/**
	 * 새로 생성되는 생성자
	 * @param owner
	 */
	protected FreeBuffShieldHandler(L1PcInstance owner) {
		this(owner, 
				0,
				Config.FREEBUFF.PCCAFE_FAVOR_DAILY_COUNT,
				0,
				0,
				0,
				new Timestamp(System.currentTimeMillis()));
		
		java.util.LinkedList<PCMasterInfoForClient.BuffBonusT> bonus = _pcmaster_bin.get_buff_bonus();
		for (int i=0; i<bonus.size(); i++) {
			this.add_golden_buff_info(new GoldenBuffInfo(owner, i, GoldenBuffInfo.DEFAULT_TYPE, bonus.get(i)));
		}
		owner.sendPackets(new S_FreeBuffShieldUpdateNoti(this), true);
	}
	
	/**
	 * 디비 정보를 로드하여 생성되는 생성자
	 * @param owner
	 * @param rs
	 * @throws SQLException
	 */
	protected FreeBuffShieldHandler(L1PcInstance owner, ResultSet rs) throws SQLException {
		this(owner, 
				rs.getInt("favor_locked_time"),
				rs.getInt("pccafe_favor_remain_count"), 
				rs.getInt("free_favor_remain_count"), 
				rs.getInt("event_favor_remain_count"), 
				rs.getInt("pccafe_reward_item_count"), 
				rs.getTimestamp("reset_time"));
		
		if (CommonUtil.isDayResetTimeCheck(_reset_time)) {
			reset();
			return;
		}
		owner.sendPackets(new S_FreeBuffShieldUpdateNoti(this), true);
	}
	
	protected FreeBuffShieldHandler(L1PcInstance owner, 
			int favor_locked_time,
			int pccafe_favor_remain_count, 
			int free_favor_remain_count, 
			int event_favor_remain_count, 
			int pccafe_reward_item_count, 
			Timestamp reset_time) {
		this._owner						= owner;
		this._pccafe_reward_item_count	= pccafe_reward_item_count;
		this._reset_time				= reset_time;
		this._pcmaster_bin				= PCMasterCommonBinLoader.getData();
		
		initialize(pccafe_favor_remain_count, free_favor_remain_count, event_favor_remain_count);
		if (favor_locked_time > 0) {
			start_disable_penalty(favor_locked_time);
		}
	}
	
	/**
	 * 가호 데이터 설정
	 * @param pccafe_favor_remain_count
	 * @param free_favor_remain_count
	 * @param event_favor_remain_count
	 */
	private void initialize(int pccafe_favor_remain_count, int free_favor_remain_count, int event_favor_remain_count) {
		for (FREE_BUFF_SHIELD_TYPE type : FREE_BUFF_SHIELD_TYPE.values()) {
			FREE_BUFF_SHIELD_INFO info = new FREE_BUFF_SHIELD_INFO();
			info.set_favor_type(type);
			switch (type) {
			case PC_CAFE_SHIELD:
				info.set_favor_remain_count(pccafe_favor_remain_count);
				break;
			case FREE_BUFF_SHIELD:
				info.set_favor_remain_count(free_favor_remain_count);
				break;
			case EVENT_BUFF_SHIELD:
				info.set_favor_remain_count(event_favor_remain_count);
				break;
			}
			add_free_buff_shield_info(info);
		}
		_owner.getConfig().set_free_buff_shield(this);// 플레이어 컨피그에 등록
	}
	
	public PCMasterInfoForClient get_pcmaster_bin() {
		return _pcmaster_bin;
	}
	
	public DISABLE_FREE_BUFF_SHIELD get_disable_state() {
		return _disable_state;
	}
	public void set_disable_state(DISABLE_FREE_BUFF_SHIELD val) {
		_disable_state = val;
		stop_favor_locked_timer();
		start_favor_locked_timer();
	}
	
	public java.util.LinkedList<FREE_BUFF_SHIELD_INFO> get_free_buff_shield_info() {
		return _free_buff_shield_info;
	}
	public FREE_BUFF_SHIELD_INFO get_free_buff_shield_info(FREE_BUFF_SHIELD_TYPE type) {
		for (FREE_BUFF_SHIELD_INFO val : _free_buff_shield_info) {
			if (val.get_favor_type() == type) {
				return val;
			}
		}
		return null;
	}
	public void add_free_buff_shield_info(FREE_BUFF_SHIELD_INFO val) {
		if (_free_buff_shield_info == null) {
			_free_buff_shield_info = new java.util.LinkedList<FREE_BUFF_SHIELD_INFO>();
		}
		_free_buff_shield_info.add(val);
	}
	public void remove_free_buff_shield_info(FREE_BUFF_SHIELD_INFO val) {
		_free_buff_shield_info.remove(val);
	}
	
	public int get_pccafe_reward_item_count() {
		return _pccafe_reward_item_count;
	}
	public void add_pccafe_reward_item_count(int val) {
		_pccafe_reward_item_count += val;
	}
	
	public java.util.LinkedList<GoldenBuffInfo> get_golden_buff_infos() {
		return _golden_buff_infos;
	}
	public GoldenBuffInfo get_golden_buff_info(int index) {
		return _golden_buff_infos.get(index);
	}
	public void set_golden_buff_infos(java.util.LinkedList<GoldenBuffInfo> val) {
		_golden_buff_infos = val;
	}
	public void add_golden_buff_info(GoldenBuffInfo val) {
		if (_golden_buff_infos == null) {
			_golden_buff_infos = new java.util.LinkedList<GoldenBuffInfo>();
		}
		_golden_buff_infos.add(val);
	}
	
	/**
	 * PK가호 사용 중지 상태 시작
	 */
	public void start_disable_penalty(int second) {
		DISABLE_FREE_BUFF_SHIELD disable = new DISABLE_FREE_BUFF_SHIELD();
		disable.set_favor_locked_time(second);
		set_disable_state(disable);
	}
	
	/**
	 * PK 패널티 타이머 시작
	 */
	protected void start_favor_locked_timer() {
		if (_disable_state != null && _disable_state.get_favor_locked_time() > 0) {
			_favor_locked_timer = new FavorLockedTimer(this, _disable_state);
			GeneralThreadPool.getInstance().schedule(_favor_locked_timer, 1000L);
		}
	}
	
	/**
	 * PK 패널티 타이머 중지
	 */
	protected void stop_favor_locked_timer() {
		if (_favor_locked_timer != null) {
			_favor_locked_timer._active = false;
			_favor_locked_timer = null;
		}
	}
	
	/**
	 * 픽시의 불멸의 가호 버프 적용 여부
	 * @param mapId
	 * @return boolean
	 */
	public boolean is_pccafe_death_penalty_shield(int mapId) {
		if (!_owner.isPCCafe()) {
			return false;
		}
		PCMasterInfoForClient.PCBonusMapT bonusMap = _pcmaster_bin.get_pc_bonus_map_infos().get(mapId);
		return bonusMap != null && bonusMap.is_death_penalty_shield();
	}
	
	/**
	 * 가호를 사용할 데이터 조사
	 * 우선 순위 : PC방 -> 이벤트 -> 무료
	 * @return FREE_BUFF_SHIELD_INFO
	 */
	public FREE_BUFF_SHIELD_INFO getUseFavor() {
		// PK패널티 상태
		if (_disable_state != null) {
			return null;
		}
		for (FREE_BUFF_SHIELD_INFO val : _free_buff_shield_info) {
			if (val.get_favor_type() == FREE_BUFF_SHIELD_TYPE.PC_CAFE_SHIELD && !_owner.isPCCafe()) {
				continue;
			}
			if (val.get_favor_remain_count() > 0) {
				return val;
			}
		}
		return null;
	}
	
	/**
	 * 금빛 버프 앱 조사
	 * 비활성화 검사 후 활성화 여부를 검사해야한다.
	 */
	public void enable_golden_buff_info_map() {
		if (_golden_buff_infos == null) {
			return;
		}
		boolean update = false;
		// 버프 비활성화
		for (GoldenBuffInfo info : _golden_buff_infos) {
			update |= info.is_update_map(false);
		}
		// 버프 활성화
		for (GoldenBuffInfo info : _golden_buff_infos) {
			update |= info.is_update_map(true);
		}
		if (update) {
			_owner.sendPackets(new S_PCMasterGoldenBuffUpdateNoti(_golden_buff_infos, S_PCMasterGoldenBuffUpdateNoti.eUpdateReason.UPDATE));
		}
	}
	
	/**
	 * PC방 종료에 의한 처리
	 */
	public void end_pccafe() {
		if (_golden_buff_infos == null || _golden_buff_infos.isEmpty()) {
			return;
		}
		for (GoldenBuffInfo golden : _golden_buff_infos) {
			if (golden.isEnable()) {
				golden.setEnable(false);
				_owner.sendPackets(new S_PCMasterGoldenBuffUpdateNoti(_golden_buff_infos, S_PCMasterGoldenBuffUpdateNoti.eUpdateReason.UPDATE));
			}
		}
	}
	
	/**
	 * 가호 지급 및 기존 소지 가호 개수 초기화
	 */
	public void reset() {
		long current_time = System.currentTimeMillis();
		if (_reset_time == null) {
			_reset_time = new Timestamp(current_time);
		} else {
			_reset_time.setTime(current_time);
		}
		for (FREE_BUFF_SHIELD_INFO val : _free_buff_shield_info) {
			val.set_favor_remain_count(0);
			switch (val.get_favor_type()) {
			case PC_CAFE_SHIELD:
				val.set_favor_remain_count(Config.FREEBUFF.PCCAFE_FAVOR_DAILY_COUNT);
				_pccafe_reward_item_count = 0;
				break;
			case FREE_BUFF_SHIELD:
				L1Arca arca = _owner.getAccount().getArca();
				if (arca != null) {
					int active_arca_count = arca.getActiveCount(current_time);
					if (active_arca_count >= 3) {
						val.set_favor_remain_count(Config.FREEBUFF.FREE_FAVOR_DAILY_ARCA_3_COUNT);
					} else if (active_arca_count >= 1) {
						val.set_favor_remain_count(Config.FREEBUFF.FREE_FAVOR_DAILY_ARCA_1_COUNT);
					}
				}
				break;
			case EVENT_BUFF_SHIELD:
				val.set_favor_remain_count(Config.FREEBUFF.EVENT_FAVOR_DAILY_COUNT);
				break;
			}
		}
		_owner.sendPackets(new S_FreeBuffShieldUpdateNoti(this), true);
	}
	
	/**
	 * 메모리 해제
	 */
	public void dispose() {
		stop_favor_locked_timer();
		if (_disable_state != null) {
			_disable_state.dispose();
			_disable_state = null;
		}
		if (_free_buff_shield_info != null) {
			for (FREE_BUFF_SHIELD_INFO val : _free_buff_shield_info) {
				val.dispose();
			}
			_free_buff_shield_info.clear();
			_free_buff_shield_info = null;
		}
		if (_golden_buff_infos != null) {
			for (GoldenBuffInfo buff : _golden_buff_infos) {
				buff.dispose();
			}
			_golden_buff_infos.clear();
			_golden_buff_infos = null;
		}
	}
	
}

