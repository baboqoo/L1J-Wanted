/**
 * 시간의 균열 컨트롤러
*/

/**
 * 본섭 정보 파악
 * 현실시간 기준 2일에 한번씩 열린다
 * 시간의 균열중 무작위 한곳만 오픈
 * 오픈시간을 기점으로 3시간 카운트
 * 시간의 균열로 들어가게 되면 테베리스 사막으로 텔
 * 테레리스 사막에서 던전으로 이동도 가능 (단 보스 방은 불가능), 리스 : 아덴 , 귀환 : 테베리스 로 텔
 * 오픈후 2시간 30분후 부터  보스방 공략 가능(단 던전에서 나온 보스방 열쇠를 소지 한 자, 그리고 선착순 20명)
 * 3시간 이내 보스를 공략 하지 못했다면 전원 아덴 텔
 * 보스를 공략할시 24시간 자유 오픈
 * 프리섭이니 머 하루에 3~4번정두만 열리게 하면 되겠지 
 * 또한 3~4번이니 카운트도 약 1시간정도로만 지정.
*/
package l1j.server.server.controller;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.L1SpawnUtil;

public class CrockController implements Runnable{

	/** 시간의 균열 오픈 시각(ms) */
	private static long sTime = 0;

	/** 시간의 균열 임시 랜덤 값 */
	private static int rnd = 0;
	
	private static int rnd2 = 0;
	
	/** 시간의 균열 보스 횟수 */
	private static int dieCount = 0;

	/** 시간의 균열 보스 공략 판단 */
	private boolean boss = false;
	
	/** 시간의 균열 보스 공략 판단 */
	private boolean killBoss = false;

	/** 시간의 균열 이동 시간 판단 */
	private boolean move = false;

	/** 시간의 균열 열고 닫힌 거 판단 */
	private boolean isTimeCrock = false;

	/** 카운트 시간 : 2시간30분 */
	private static final long TIME = 9000000L;
	
	private boolean gmopen = false;
	/** 카운트 시간 : 24시간 */
	//private static final long DAY = 86400000L;

	/** 싱글톤 단일 객체 */
	private static CrockController instance;

	/** 시간의 균열 객체 아이디 */
	private static final int[] ID = { 200 };
	private static final int[] crockID = { 0, 1 }; //0 == 테베, 1 == 티칼
	
	
	// 시간의 균열 - 테베 선물 아이템 번호
	private static final int[][] ItemId = {
		//{ 506, 1}, { 507, 1}, { 508, 1}, { 509, 1}, { 22007, 1}, 

		{ 40074, 1}, { 40087, 1}, { 40076, 1}, 
		{ 140074, 1}, { 140087, 1}, { 240074, 1}, { 240087, 1}, 
		{ 40052, 2 }, { 40053, 2 }, 
		{ 40054, 2 }, { 40055, 2 },

		{ 40074, 1}, { 40087, 1}, { 40076, 1}, 
		{ 140074, 1}, { 140087, 1}, { 240074, 1}, { 240087, 1}, 
		{ 40052, 2 }, { 40053, 2 }, 
		{ 40054, 2 }, { 40055, 2 }
	};
	
	// 시간의 균열 선물 - 티칼 아이템 번호
	private static final int[][] ItemIdTikal = {
		//{ 22194, 1}, { 22195, 1}, { 22007, 1},

		{ 40074, 1}, { 40087, 1}, { 40076, 1}, 
		{ 140074, 1}, { 140087, 1}, { 240074, 1}, { 240087, 1}, 
		{ 40052, 2 }, { 40053, 2 }, 
		{ 40054, 2 }, { 40055, 2 },

		{ 40074, 1}, { 40087, 1}, { 40076, 1}, 
		{ 140074, 1}, { 140087, 1}, { 240074, 1}, { 240087, 1}, 
		{ 40052, 2 }, { 40053, 2 }, 
		{ 40054, 2 }, { 40055, 2 }
	}; // 시간의 균열 - 티칼용 주석

	/** 시간의 균열 좌표 */
	// 1.  화둥정상   2.  용뼈정상    3.  작은용뼈    4.  카오틱신전  5.  글루딘 무덤
	private static final int[][] loc = {
		{ 32873, 33257, 4 }, 
		{ 32873, 33257, 4 }, 
		{ 32873, 33257, 4 }, 
		{ 32873, 33257, 4 }, 
		{ 32873, 33257, 4 }, 
		{ 32873, 33257, 4 }, 
		{ 32873, 33257, 4 }, 
		{ 32873, 33257, 4 }
		};

	/** 보스방 선착순 20명을 담기 위한 리스트 */
	private static final ArrayList<L1PcInstance> sList = new ArrayList<L1PcInstance>();

	/** 시각 데이터 포맷 */
	private static final SimpleDateFormat ss = new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);

	public static final int EXECUTE_STATUS_NONE = 0;
	public static final int EXECUTE_STATUS_PREPARE = 1;
	public static final int EXECUTE_STATUS_READY = 2;
	public static final int EXECUTE_STATUS_STANDBY = 3;
	public static final int EXECUTE_STATUS_PROGRESS = 4;
	public static final int EXECUTE_STATUS_FINALIZE = 5;

	private int _executeStatus = EXECUTE_STATUS_NONE;

	/**
	 * CrockController 객체 리턴
	 * @return	(CrockController)	단일객체
	*/
	public static CrockController getInstance(){
		if(instance == null) instance = new CrockController();
		return instance;
	}
	/**
	 * 기본 생성자 - 싱글톤구현으로 private
	*/
	private CrockController(){
	}

	/**
	 * Super class abstract method
	*/
	@Override
	public void run() {
		try {
			switch (_executeStatus) {
			case EXECUTE_STATUS_NONE:
				if (!isOpen() || L1World.getInstance().getAllPlayers().size() <= 0) {
					GeneralThreadPool.getInstance().schedule(this, 1000L); // 1초
				} else {
					setTimeCrock(true);
//AUTO SRM: 					L1World.getInstance().broadcastServerMessage("\\aD시간의 균열이 잠시 후에 열립니다.", true); // CHECKED OK
					L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(931), true);
					_executeStatus = EXECUTE_STATUS_PREPARE;
					GeneralThreadPool.getInstance().schedule(this, 15000L); // 15초
//AUTO SRM: 					L1World.getInstance().broadcastServerMessage("\\aD균열의 위치는 사막 오아시스 우측입니다.", true); // CHECKED OK
					L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(932), true);
					L1SpawnUtil.spawn2(32873, 33257, (short) 4, 5, 200, 0, 86400 * 1000, 0);
					L1SpawnUtil.spawn2(32780, 32832, (short) 782, 5, 400016, 0, 86400 * 1000, 0);
					L1SpawnUtil.spawn2(32793, 32832, (short) 782, 5, 400017, 0, 86400 * 1000, 0);
				
					L1SpawnUtil.spawn2(32751, 32859, (short) 784, 5, 800019, 0, 86400 * 1000, 0);//왼쪽
					L1SpawnUtil.spawn2(32751, 32867, (short) 784, 5, 800018, 0, 86400 * 1000, 0);//오른쪽
				}
				break;

			case EXECUTE_STATUS_PREPARE:
				openCrock();

				_executeStatus = EXECUTE_STATUS_READY;
				GeneralThreadPool.getInstance().schedule(this, 15000L);
				// GeneralThreadPool.getInstance().schedule(this, 1000L);
				break;

			case EXECUTE_STATUS_READY:
				//L1NpcInstance crock = L1World.getInstance().findNpc(npcId());
				//ready(crock);
				_executeStatus = EXECUTE_STATUS_STANDBY;
				GeneralThreadPool.getInstance().schedule(this, TIME); // 2시간
				// GeneralThreadPool.getInstance().schedule(this, 60000); // 2시간
				break;

			case EXECUTE_STATUS_STANDBY:
				dieCount = 0;
				setBoss(true); // 보스 공략 시작
				_executeStatus = EXECUTE_STATUS_PROGRESS;
				GeneralThreadPool.getInstance().schedule(this, 1800000L); // 30분
				break;

			case EXECUTE_STATUS_PROGRESS:
				setBoss(false);
				if (!isTeleport()) {
					setKillBoss(true);
					/** 보스 공략이 성공했다면 12시간 후 텔 */
					if (crocktype() == 0) {// 테베
//AUTO SRM: 						L1World.getInstance().broadcastServerMessage("오시리스의 힘이 회복될 때까지 시간의 균열이 유지됩니다", true); // CHECKED OK
						L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(933), true);
					} else {// 티칼
//AUTO SRM: 						L1World.getInstance().broadcastServerMessage("쿠쿨칸의 힘이 회복될 때까지 시간의 균열이 유지됩니다.", true); // CHECKED OK
						L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(934), true);
					}
					TelePort();
					GeneralThreadPool.getInstance().schedule(this, 1000);
					_executeStatus = EXECUTE_STATUS_FINALIZE;
					break;
				}
				break;
			case EXECUTE_STATUS_FINALIZE:
				setKillBoss(false);
//AUTO SRM: 				L1World.getInstance().broadcastServerMessage("시간의 균열: 균열이 잠시 후 닫힘", true); // CHECKED OK
				L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(935), true);
				TelePort();
				clear();
				_executeStatus = EXECUTE_STATUS_NONE;
				GeneralThreadPool.getInstance().schedule(this, 1000L);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setTimeCrock(boolean status) {
		isTimeCrock = status;
	}

	public boolean isTimeCrock() {
		return isTimeCrock;
	}

	/**
	 * 전원 아덴 마을로 텔
	*/
	private void TelePort(){
		for(L1PcInstance c : L1World.getInstance().getAllPlayers()){
			if(c.getMap().getId() == 780 || c.getMap().getId() == 781 || c.getMap().getId() == 782
			|| c.getMap().getId() == 783 || c.getMap().getId() == 784){ // 시간의 균열 - 티칼용 주석
				c.getTeleport().start(33970, 33246, (short) 4, 4, true);
			}
		}
	}

	public String OpenTime(){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(sTime);
		return ss.format(c.getTime());
	}
	
	/**
	 * 시간의 균열이 현재 열려있는지 판단
	 * @return	(boolean)	열려있다면 true 닫혀있다면 false
	*/
	private boolean isOpen(){
		Calendar cal = Calendar.getInstance();
		int _hour = Calendar.HOUR;
		int _minute = Calendar.MINUTE;
		/** 0 오전 , 1 오후 * */
		String AMPM = "PM";
		if (cal.get(Calendar.AM_PM) == 0) {
			AMPM = "AM";
		}
		if ((AMPM.equals("PM") && cal.get(_hour) == 6 && cal.get(_minute) == 59) || isGmOpen()) {
	    	 return true;
	      } 
		return false;
	}

	/**
	 * 시간의 균열 이동 상태
	 * @return	(boolean)	move	이동 여부
	*/
	public boolean isMove(){
		return move;
	}

	/**
	 * 시간의 균열 이동 상태 셋팅
	 * @param	(boolean)	move	이동 여부
	*/
	private void setMove(boolean move){
		this.move = move;
	}

	/**
	 * 시간의 균열 보스공략 시간상태
	 * @return	(boolean)	boss	공략 여부
	*/
	public boolean isBoss(){
		return boss;
	}

	public boolean isKillBoss() {
		return killBoss;
	}
	/**
	 * 시간의 균열 보스공략 시간 알림
	 * @param	(boolean)	boss	공략 여부
	*/
	private void setBoss(boolean boss){
		this.boss = boss;
	}

	private void setKillBoss(boolean killBoss){
		this.killBoss = killBoss;
	}
	/**
	 * 선착순 20명 등록
	*/
	public boolean add(L1PcInstance c){
		synchronized(this)
		{
			/** 등록되어 있지 않고 */
			if(!sList.contains(c)){
				/** 선착순 20명 이하라면 */
				if(sList.size() < 20)
				{
					sList.add(c);
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * 선착순 리스트 사이즈 반납
	 * @return	(int)	sList 의 사이즈
	*/
	public int size(){
		return sList.size();
	}

	/**
	 * 클리어(초기화) : 시스템이 한바퀴 끝날때 재 셋팅을 위해 쓰인다.
	*/
	private void clear(){
		sList.clear();
		dieCount = 0;
		setBoss(false);
		setMove(false);
		setGmOpen(false);
	
		L1FieldObjectInstance Portal = null; 
		for (L1Object object : L1World.getInstance().getObject()) { 
			if (object instanceof L1FieldObjectInstance) { 
				Portal = (L1FieldObjectInstance) object;
				if (Portal.getNpcTemplate().getNpcId() == 200) { 
					Portal.deleteMe(); 
					Portal = null;
				}
			}
		}
		setTimeCrock(false);
	}

	/**
	 * 선착순 20명에게 아이템 지급 - 테베
	*/
	public void send() {
		for (L1PcInstance c : sList) {
			if (c == null)continue;
			int[] Item = Item();
			
			L1ItemInstance item = c.getInventory().storeItem(Item[0], Item[1]);
//AUTO SRM: 			c.sendPackets(new S_SystemMessage("테베 오시리스 제단의 성스러운 아이템을 획득하였습니다."), true); // CHECKED OK
			c.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(929), true), true);
			for (L1PcInstance partymember : c.getParty().getMembersArray()) {
				if (partymember != null && !c.isDead()) {
//AUTO SRM: 					partymember.sendPackets(new S_ServerMessage(813, "테베 오시리스 제단", item.getLogNameRef(), c.getName()), true); // CHECKED OK
					partymember.sendPackets(new S_ServerMessage(813, S_SystemMessage.getRefText(936), item.getLogNameRef(), c.getName()), true);
				}
			}
		}
	}
	
	/**
	 * 선착순 20명에게 아이템 지급 - 티칼
	*/
	public void sendTikal() {
		for (L1PcInstance c : sList) {
			if (c == null)continue;
			int[] ItemTikal = ItemTikal();
			L1ItemInstance item = c.getInventory().storeItem(ItemTikal[0], ItemTikal[1]);
//AUTO SRM: 			c.sendPackets(new S_SystemMessage("티칼 제단의 성스러운 아이템을 획득하였습니다."), true); // CHECKED OK
			c.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(930), true), true);
			for (L1PcInstance partymember : c.getParty().getMembersArray()) {
				if (partymember != null && !c.isDead()) {
//AUTO SRM: 				partymember.sendPackets(new S_ServerMessage(813, "티칼 제단", item.getLogNameRef(), c.getName()), true); // CHECKED OK
				partymember.sendPackets(new S_ServerMessage(813, S_SystemMessage.getRefText(937), item.getLogNameRef(), c.getName()), true);
			}
			}
		}
	} // 시간의 균열 - 티칼용 주석

	/**
	 * 시간의 균열중 하나의 랜덤의 아이디를 반납
	 * @return	(int)	npcId	엔피씨 아이디
	*/
	private void openCrock() {
		rnd = (int) (Math.random() * ID.length);
		rnd2 = (int)(Math.random() * crockID.length);
//		System.out.println("c opcode : " + rnd2);
	}
	
	public int crocktype() {
		return rnd2;
	}

	/**
	 * 지정된 npcId 에 대한 loc 을 반납
	 * @return	(int[])	loc		좌표 배열
	*/
	public int[] loc(){
		return loc[rnd];
	}

	/**
	 * 시간의 균열 보스공략 확인
	 * @return	(boolean)	2보스다 죽었다면 false 1보스 이하 죽였다면 true
	*/
	private boolean isTeleport(){
		boolean sTemp = true;
		switch(dieCount()){
			case 2: sTemp = false; break;
			default: sTemp = true; break;
		}
		return sTemp;
	}

	/**
	 * 시간의 균열 테베 보스 다이 반납
	 * @return	(int)	dieCount	보스 다이 횟수
	*/
	public int dieCount(){
		return dieCount;
	}

	/**
	 * 시간의 균열 테베 보스 다이 설정
	 * @param	(int)	dieCount	보스 다이 횟수
	*/
	public void dieCount(int dieCount){
		CrockController.dieCount = dieCount;
	}

	/**
	 * 아이템 지급 아이디 랜덤 반납 - 테베
	 * @return	(int[]) Itemid	지급받을 아이템아이디, 갯수
	*/
	private int[] Item(){
		return ItemId[(int)(Math.random() * ItemId.length)];
	}
	
	/**
	 * 아이템 지급 아이디 랜덤 반납 - 티칼
	 * @return	(int[]) Itemid	지급받을 아이템아이디, 갯수
	*/
	private int[] ItemTikal(){
		return ItemIdTikal[(int)(Math.random() * ItemIdTikal.length)];
	} // 시간의 균열 - 티칼용 주석
	/**
	 * Gm에 의한 테베오픈
	*/
	public boolean isGmOpen(){
		return gmopen;
	}

	public void setGmOpen(boolean gmopen1){
		gmopen = gmopen1;
	}


	public void BossSpawn(int mob, int x, int y, short map1, int time) {
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(mob);
			if (l1npc != null) {
				try {
					String s = l1npc.getImpl();
					@SuppressWarnings("rawtypes")
					Constructor constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
					Object aobj[] = { l1npc };
					L1NpcInstance npc = (L1NpcInstance) constructor.newInstance(aobj);
					npc.setId(IdFactory.getInstance().nextId());
					npc.setMap(map1);
					npc.setX(x);
					npc.setY(y);
					npc.setHomeX(x);
					npc.setHomeY(y);
					npc.getMoveState().setHeading(5);
					
					L1World.getInstance().storeObject(npc);
					L1World.getInstance().addVisibleObject(npc);
					L1Object object = L1World.getInstance().findObject(npc.getId());
					L1NpcInstance newnpc = (L1NpcInstance) object;
					newnpc.onNpcAI();
					newnpc.getLight().turnOnOffLight();
					newnpc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
					L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, time);
					timer.begin();
				} catch (Exception e) {
					// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		} catch (Exception exception) {
		}
	}
}

