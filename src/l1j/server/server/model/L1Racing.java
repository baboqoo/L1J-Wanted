package l1j.server.server.model;

import java.util.ArrayList;

import l1j.server.common.data.ChatType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.Chocco;
import l1j.server.server.serverpackets.S_GameEnd;
import l1j.server.server.serverpackets.S_GameList;
import l1j.server.server.serverpackets.S_GameOver;
import l1j.server.server.serverpackets.S_GameRap;
import l1j.server.server.serverpackets.S_GameStart;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

@SuppressWarnings("unchecked")
public class L1Racing implements Runnable {
	public static final int STATUS_NONE = 0;
	public static final int STATUS_READY = 1;
	public static final int STATUS_PLAYING = 2;
	public static final int STATUS_CLEANUP = 3;

	public static final int EXECUTE_STATUS_NONE = 0;
	public static final int EXECUTE_STATUS_PREPARE = 1;
	public static final int EXECUTE_STATUS_READY = 2;
	public static final int EXECUTE_STATUS_PROGRESS = 3;
	public static final int EXECUTE_STATUS_FINALIZE = 4;

	private int _gameStatus = STATUS_NONE;
	private int _executeStatus = EXECUTE_STATUS_NONE;
	private int _count = 0;

	private static final short mapId = 5143;

	/*
	public final int 일반 = 0;
	public final int 순위00 = 1; // 이게.. 최초 시작할때
	public final int 순위01 = 2; // 0바퀴째 1번째 체크
	public final int 순위02 = 3; // 0바퀴째 2번째 체크
	public final int 순위03 = 4; // 0바퀴째 3번째 체크
	public final int 순위10 = 5; // 1바퀴째 결승점(1바퀴 완주시..)
	public final int 순위11 = 6; // 1바퀴째 1번째 체크
	public final int 순위12 = 7; // 1바퀴째 2번째 체크
	public final int 순위13 = 8; // 1바퀴째 3번째 체크
	public final int 순위20 = 9; // 2바퀴째 결승점
	public final int 순위21 = 10; // 2바퀴째 1번째 체크
	public final int 순위22 = 11; // 2바퀴째 2번째 체크
	public final int 순위23 = 12; // 2바퀴째 3번째 체크
	public final int 순위30 = 13; // 3바퀴째 결승점
	public final int 순위31 = 14; // 3바퀴째 1번째 체크
	public final int 순위32 = 15; // 3바퀴째 2번째 체크
	public final int 순위33 = 16; // 3바퀴째 3번째 체크
	public final int 순위99 = 17; // 이건.. 종료시? 완주일까 
	*/
	public final int rkNormal = 0;
	public final int rank00 = 1; // Initial start
	public final int rank01 = 2; // 0 laps, 1st checkpoint
	public final int rank02 = 3; // 0 laps, 2nd checkpoint
	public final int rank03 = 4; // 0 laps, 3rd checkpoint
	public final int rank10 = 5; // 1 lap finish (upon completing 1 lap)
	public final int rank11 = 6; // 1 lap, 1st checkpoint
	public final int rank12 = 7; // 1 lap, 2nd checkpoint
	public final int rank13 = 8; // 1 lap, 3rd checkpoint
	public final int rank20 = 9; // 2 lap finish
	public final int rank21 = 10; // 2 laps, 1st checkpoint
	public final int rank22 = 11; // 2 laps, 2nd checkpoint
	public final int rank23 = 12; // 2 laps, 3rd checkpoint
	public final int rank30 = 13; // 3 lap finish
	public final int rank31 = 14; // 3 laps, 1st checkpoint
	public final int rank32 = 15; // 3 laps, 2nd checkpoint
	public final int rank33 = 16; // 3 laps, 3rd checkpoint
	public final int rank99 = 17; // End or completed?
		
	private static L1Racing instance;

	public static L1Racing getInstance(){
		if (instance == null) {
			instance = new L1Racing();
		}
		return instance;
	}

	private final ArrayList<L1PcInstance> _List[] = new ArrayList[18];

	{
		for (int i = 0; i < 18; i++) {
			_List[i] = new ArrayList<L1PcInstance>();
		}
	}

	/** 기본 생성자 */
	private L1Racing(){}

	/**
	 * Thread abstract Method 
	*/
	private int Rnd(int rnd){
		return (int)(Math.random() * rnd);
	}

	@Override
	public void run() {
		try{
			switch(_executeStatus)
			{
				case EXECUTE_STATUS_NONE:
				{
					if( getGameStatus() == STATUS_READY )
					{
						_executeStatus = EXECUTE_STATUS_PREPARE;

						//npcBroadcast("잠시후 펫 레이싱을 진행하겠습니다.");
						npcBroadcast(S_SystemMessage.getRefText(114));

						GeneralThreadPool.getInstance().schedule(this, 60000L);
					}
					else
					{
						GeneralThreadPool.getInstance().schedule(this, 1000L);
					}
				}
				break;

				case EXECUTE_STATUS_PREPARE:
				{
					removeRetiredMembers();

					if( readyPetRacing() )
					{
						_count = 5;

						_executeStatus = EXECUTE_STATUS_READY;					
					}
					else
					{
						_executeStatus = EXECUTE_STATUS_NONE;					
					}

					GeneralThreadPool.getInstance().schedule(this, 1000L);
				}
				break;

				case EXECUTE_STATUS_READY:
				{
					if ( countDown() )
					{
						removeRetiredMembers();
						startPetRacing();

						_count = 60 * 5;

						_executeStatus = EXECUTE_STATUS_PROGRESS;
					}

					GeneralThreadPool.getInstance().schedule(this, 1000L);
				}
				break;

				case EXECUTE_STATUS_PROGRESS:
				{
					
					if ( getGameStatus() == STATUS_CLEANUP )
					{
						if ( endCountDown() )
						{
							_executeStatus = EXECUTE_STATUS_FINALIZE;
							GeneralThreadPool.getInstance().schedule(this, 5 * 60 * 000L);
						}
						else
						{
							GeneralThreadPool.getInstance().schedule(this, 1000L);
						}
					}
					else
					{
						if ( --_count == 0 )
						{
							if ( _count % 10 == 0 )	// 얜 10초마다 한번씩
							{
								removeRetiredMembers();
							}

							endGame();

						}
						GeneralThreadPool.getInstance().schedule(this, 1000L);
					}
				}
				break;

				case EXECUTE_STATUS_FINALIZE:
				{
					_executeStatus = EXECUTE_STATUS_NONE;
					setGameStatus(STATUS_NONE);

					GeneralThreadPool.getInstance().schedule(this, 1000L);
				}
				break;
			}
		}catch(Exception e){
		}
	}

	public void removeRetiredMembers() {
		for (int i = 0; i < 18; i++)
		{
			for (L1PcInstance pc : toArray(i)) {
				if (pc.getMapId() != getMapId()) {
					removeMember(pc);
				}
			}
		}

		for (L1PcInstance pc : L1World.getInstance().getAllPlayers3())
		{
			if( pc.getMapId() == getMapId() && !isMember(pc))
			{
				pc.getTeleport().start(32616 + Rnd(4), 32774 + Rnd(4), (short)4, 5, true);
			}
		}
	}
	
	public void removeMember(L1PcInstance pc)
	{
		for (int i = 0; i < 18; i++)
		{
			if (_List[i].contains(pc))
			{
				_List[i].remove(pc);
				break;
			}
		}
	}
	
	
	public int getMembersCount()
	{
		int memberCount = 0;
		for (int i = 0; i < 18; i++)
		{
			memberCount += _List[i].size(); 
		}
		
		return memberCount;
	}

	public boolean isMember(L1PcInstance pc)
	{
		for (int i = 0; i < 18; i++)
		{
			if (_List[i].contains(pc))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void clearMembers()
	{
		for (int i = 0; i < 18; i++)
		{
			_List[i].clear();
		}
	}

	private boolean readyPetRacing() {
//		if( size(rkNormal) < 1 )
		if ( size(rkNormal) < 2 )
		{
			for (int i = 0; i < 18; i++)
			{
				for (L1PcInstance pc : toArray(i)) {
					if (pc.getMapId() == getMapId()){
					// 경기 최소 인원이 2명이 만족하지 않아 경기를 강제 종료 합니다. 1000 아데나를 돌려 드렸습니다.
						pc.sendPackets(new S_ServerMessage(1264), true);
						pc.getInventory().storeItem(L1ItemId.ADENA, 1000); // 1000 아데나 지급
						pc.getTeleport().start(32616 + Rnd(4), 32774 + Rnd(4), (short)4, 5, true);
					}
					removeMember(pc);

					setGameStatus(STATUS_NONE);
				}
			}

			return false;
		}

		setGameStatus(STATUS_PLAYING);

		int i = 0;
		for (L1PcInstance pc : toArray(rkNormal)) {
			L1PolyMorph.doPoly(pc, 5065, 1000,L1PolyMorph.MORPH_BY_NPC); //아기 진돗
			pc.sendPackets(new S_GameStart(pc), true);
			pc.sendPackets(new S_GameRap(pc, 1), true);
			pc.sendPackets(new S_GameList(pc, i++), true);
			pc.sendPackets(new Chocco(4), true);

			pc.sendPackets(new S_ServerMessage(1258), true);
		}
		
		return true;
	}

	private void startPetRacing(){
		openDoor();
	}
	
	private void openDoor()
	{
		L1DoorInstance door = null;
		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1DoorInstance) {
				door = (L1DoorInstance) object;

				if (door.getSpriteId() == 6677){
					door.open();
				}
			}
		}
	}
	
	private void closeDoor()
	{
		L1DoorInstance door = null;
		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1DoorInstance) {
				door = (L1DoorInstance) object;

				if (door.getSpriteId() == 6677){
					door.close();
				}
			}
		}
	}

	private void broadcast(String msg)
	{
		for (int i = 0; i < 18; i++)
		{
			for (L1PcInstance pc : toArray(i)) {
				if (pc.getMapId() == getMapId()) {
					pc.sendPackets(new S_SystemMessage(msg, true), true);
				}
			}
		}
	}
	
	private void npcBroadcast(String msg)
	{
		for (L1Object obj : L1World.getInstance().getObjectArray()) {
			if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance)obj;
				if (npc.getNpcTemplate().getNpcId() == 300000) {
					//npc.broadcastPacket(new S_NpcChatPacket(npc, "잠시후 펫 레이싱을 진행하겠습니다.", ChatType.CHAT_SHOUT), true);					
					npc.broadcastPacket(new S_NpcChatPacket(npc, S_SystemMessage.getRefText(114), ChatType.CHAT_SHOUT), true);
				}			
			}
		}
	}

	private boolean countDown()
	{
		--_count;

		if ( _count == 0 )
		{
			return true;
		}
	
		return false;
	}
	
	private void kickAllPlayers()
	{
		for (int i = 0; i < 18; i++)
		{
			for (L1PcInstance pc : toArray(i)) {
				if (pc.getMapId() == getMapId()) {
					pc.sendPackets(new S_GameEnd(pc), true);
					pc.getTeleport().start(32624, 32813, (short) 4, 5, true);
				}
			}
		}

		clearMembers();
	}

	private boolean endCountDown()
	{
		if ( _count == 0 )
		{
			kickAllPlayers();
			closeDoor();

			return true;
		}

		//broadcast( _count + "초후 밖으로 이동합니다." );
		broadcast( _count + S_SystemMessage.getRefText(107) );		
		
		--_count;
		
		return false;
		
	}

	public void endGame()
	{
		if ( getGameStatus() != STATUS_PLAYING )
		{
			return;
		}

		for (L1PcInstance c : L1World.getInstance().getAllPlayers()) {
			if (c.getMapId() == getMapId()) {
				c.sendPackets(new S_GameOver(c), true);
			}
		}

		_count = 10;

		setGameStatus(STATUS_CLEANUP);
	}

	public void setGameStatus(int status)
	{
		_gameStatus = status;
	}
	
	public int getGameStatus()
	{
		return _gameStatus;
	}
	/**
	 * 각 저장소 길이를 리턴
	 * @param	(int)	index	ArrayList[] 배열의 인덱스
	 * @return	(int)	ArrayList 인덱스로 접근된 저장소의 길이
	*/
	public int size(int index){
		return _List[index].size();
	}
	/**
	 * 각 저장소 객체 배열 리턴
	 * @param	(int)	index		ArrayList[] 배열의 인덱스
	 * @return	(L1PcInstance[])	L1PcInstance[] 배열
	*/
	public L1PcInstance[] toArray(int index){
		return (L1PcInstance[]) _List[index].toArray(new L1PcInstance[size(index)]);
	}
	/**
	 * 각 저장소 객체 리턴
	 * @param	(int)	index		ArrayList[] 배열의 인덱스
	 * @param	(int)	i			인덱스
	 * @return	(L1PcInstance)	L1PcInstance 배열
	*/
	public L1PcInstance toArray(int index, int i){
		return (L1PcInstance) _List[index].get(i);
	}
	/**
	 * 각 저장소 리턴
	 * @param	(int)	index		ArrayList[] 배열의 인덱스
	 * @return	(ArrayList)			ArrayList
	*/
	public ArrayList<L1PcInstance> arrayList(int index){
		return _List[index];
	}
	/**
	 * 객체 추가
	 * @param	(int)			index	배열인덱스
	 * @param	(L1PcInstance)	c		객체
	*/
	public void add(int index, L1PcInstance c){
		if (!_List[index].contains(c)) {
			_List[index].add(c);

			if (index == rkNormal )
			{
				c.sendPackets(new S_ServerMessage(1253, Integer.toString(_List[index].size())), true);

//				if( _List[index].size() > 0)
				if ( _List[index].size() > 1)
				{
					if (getGameStatus() == STATUS_NONE){
						setGameStatus(STATUS_READY);
					}

					for (L1PcInstance player : toArray(rkNormal)) {
						// 입장하시겠습니까? (Y/N)
						if(player.getMap().getId() != getMapId()) player.sendPackets(new S_MessageYN(1256, StringUtil.EmptyString), true);
					}
				}
			}
		}
		else if (index == rkNormal)
		{
			c.sendPackets(new S_ServerMessage(1254), true);
		}
	}
	/**
	 * 객체 삭제 
	 * @param	(int)			index	배열인덱스
	 * @param	(L1PcInstance)	c		객체
	*/
	public void remove(int index, L1PcInstance c){
		if (_List[index].contains(c)) _List[index].remove(c);
	}
	/**
	 * 객체가 현재 펫레이싱 중인지 체크
	 * @param	(int)			index	배열인덱스
	 * @param	(L1PcInstance)	c		객체
	 * @return	(boolean)	있다면 true, 없다면 false
	*/
	public  boolean contains(int index, L1PcInstance c){
		return _List[index].contains(c);
	}

	/**
	 * 저장 초기화
	 * @param	(int)			index	배열인덱스
	*/
	public void clear(int index){
		_List[index].clear();
	}
	/**
	 * 저장 초기화
	*/
	public void clear(){
		for (int i = 0; i < _List.length; i++) {
			_List[i].clear();
		}
	}

	/**
	 * 참가인원을 다시한번 검색 처음맴버에서 현재없다면 삭제
	 */

	public short getMapId() {
		return mapId;
	}
}
