package l1j.server.GameSystem.fatigue;

import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.S_Fatigue;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.StringUtil;

/**
 * 그랑카인 시스템 관리 클래스(피로도)
 * L1Pcinstance에게 할당한다.
 * @author LinOffice
 */
public class L1Fatigue {
	private static final int MAX_POINT = Config.FATIGUE.FATIGUE_FIRST_VALUE + (Config.FATIGUE.FATIGUE_NEXT_VALUE * 6);
	
	private L1PcInstance owner;
	private int level;
	private int point;
	private Timestamp restTime;
	private L1FatigueTimer timer;
	
	/**
	 * 생성자
	 * @param owner
	 * @param point
	 * @param restTime
	 */
	public L1Fatigue(L1PcInstance owner, int point, Timestamp restTime) {
		this.owner		= owner;
		this.point		= point;
		this.restTime	= restTime;
	}

	public int getLevel() {
		return level;
	}
	public int getPoint() {
		return point;
	}
	public void addPoint(int value){
		point = IntRange.ensure(point + value, 0, MAX_POINT);
		calc();
	}
	public Timestamp getRestTime() {
		return restTime;
	}
	
	/**
	 * 패널티 수치
	 * @return double
	 */
	public double getPenalty(){
		switch(level){
		case 1:return 0.9D;
		case 2:return 0.8D;
		case 3:return 0.7D;
		case 4:return 0.6D;
		case 5:return 0.4D;
		case 6:return 0.1D;
		default:return 1.0D;
		}
	}
	
	/**
	 * 경험치 패널티 수치
	 * @return double
	 */
	public double getExpPenalty(){
		switch(level){
		case 1:return 0.7D;// 30%
		case 2:return 0.4D;// 60%
		case 3:
		case 4:
		case 5:
		case 6:return 0.1D;// 90%
		default:return 0.0D;
		}
    }
	
	/**
	 * 누적된 포인트에 대한 단계 계산
	 */
	void calc(){
		if (level == 0) {
			if (point >= Config.FATIGUE.FATIGUE_FIRST_VALUE) {
				level = 1;
				restTime = new Timestamp(System.currentTimeMillis() + (long)(3600000 * Config.FATIGUE.FATIGUE_DURATION));
				start();
			}
		} else {
			if (point == 0) {
				end();
				return;
			}
			if (level < 6 && point >= Config.FATIGUE.FATIGUE_FIRST_VALUE + (level * Config.FATIGUE.FATIGUE_NEXT_VALUE)) {
				level++;
				start();
			} else if (level > 1 && point < Config.FATIGUE.FATIGUE_FIRST_VALUE + ((level - 1) * Config.FATIGUE.FATIGUE_NEXT_VALUE)) {
				level--;
				start();
			}
		}
	}
	
	/**
	 * 패널티 부여
	 */
	private void start(){
		owner.sendPackets(S_Fatigue.LEVELS[level]);
		if (timer == null) {
			timer = new L1FatigueTimer(owner);
			GeneralThreadPool.getInstance().schedule(timer, restTime.getTime() - System.currentTimeMillis());
		}
	}
	
	/**
	 * 패널티 종료
	 */
	public void end(){
		owner.sendPackets(S_Fatigue.LEVELS[0]);
		dispose();
	}
	
	/**
	 * 로그인 시 패널티 정보를 설정한다.
	 */
	public void login(){
		if (restTime != null) {
			long restMillis	= restTime.getTime();
			if (restMillis <= System.currentTimeMillis()) {// 패널티 시간이 지남
				dispose();
			} else {
				if (owner.getAccount() != null) {
					Timestamp logoutTime = owner.getAccount().getLastQuit();
					if (logoutTime != null && point > 0) {// 로그아웃 시간에 대한 감소 처리
						int reduce	= (int)((restMillis - logoutTime.getTime()) / 60000);// 1분에 1포인트 감소
						if (reduce > 0) {
							point	= IntRange.ensure(point - reduce, 0, MAX_POINT);
						}
					}
				}
			}
		}
		
		if (restTime == null) {
			calc();
		} else {
			level = point >= Config.FATIGUE.FATIGUE_FIRST_VALUE ? ((point - Config.FATIGUE.FATIGUE_FIRST_VALUE) / Config.FATIGUE.FATIGUE_NEXT_VALUE) + 1 : 0;
			if (level > 0) {
				start();
			}
		}
	}
	
	/**
	 * 메모리 정리
	 */
	public void dispose(){
		level = point = 0;
		restTime = null;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	@Override
	public String toString() {
		if (level == 0) {
			//return String.format("그랑카인 '%d' 포인트 (경고: '%d' 포인트 누적시  1단계 부여)", point, Config.FATIGUE.FATIGUE_FIRST_VALUE); // CHECKED OK
			return String.format(S_SystemMessage.getRefText(288) + "%d" + S_SystemMessage.getRefText(375) + "%d" + S_SystemMessage.getRefTextNS(399), point, Config.FATIGUE.FATIGUE_FIRST_VALUE);
		}
		if (level == 6) {
			//return String.format("그랑카인 '6단계' 상태입니다.\n'%d' 포인트\n종료시간: '%s')", point, StringUtil.getFormatDate(restTime)); // CHECKED OK
			return String.format(S_SystemMessage.getRefText(402) + "%d" + S_SystemMessage.getRefText(439) + "%s" + "')", point, StringUtil.getFormatDate(restTime));
		}
		//return String.format("그랑카인 '%d단계' 상태입니다.\n'%d' 포인트 (경고: '%d' 포인트 누적시  '%d' 단계 부여)\n종료시간: '%s'", level, point,	 // CHECKED OK
		//	Config.FATIGUE.FATIGUE_FIRST_VALUE + (level * Config.FATIGUE.FATIGUE_NEXT_VALUE), level + 1, StringUtil.getFormatDate(restTime));
		return String.format(S_SystemMessage.getRefText(456)+ "%d" + S_SystemMessage.getRefText(457) + "%d" + S_SystemMessage.getRefText(461) + "%d" + S_SystemMessage.getRefText(462) + "%d" + S_SystemMessage.getRefText(511) + "%s" + "'", level, point,	
			Config.FATIGUE.FATIGUE_FIRST_VALUE + (level * Config.FATIGUE.FATIGUE_NEXT_VALUE), level + 1, StringUtil.getFormatDate(restTime));
	}
}

