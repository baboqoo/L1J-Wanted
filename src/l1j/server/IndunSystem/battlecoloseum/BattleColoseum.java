package l1j.server.IndunSystem.battlecoloseum;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l1j.server.common.data.ChatType;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1TeleporterInstance;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.teaminfo.S_InfinityBattleBoardInfoNoti;
import l1j.server.server.serverpackets.teaminfo.S_InfinityBattleLeaveMapNoti;
import l1j.server.server.templates.L1Npc;

/**
 * 격전의 콜로세움 시스템 쓰레드
 * @author LinOffice
 */
public class BattleColoseum implements Runnable {
	
	private boolean A_TEAM, B_TEAM, C_TEAM, D_TEAM;
	private int A_TEAM_STEP, B_TEAM_STEP, C_TEAM_STEP, D_TEAM_STEP;
	private int A_TEAM_STEP_DELAY, B_TEAM_STEP_DELAY, C_TEAM_STEP_DELAY, D_TEAM_STEP_DELAY;
	private boolean A_TEAM_DOOR1, A_TEAM_DOOR2, B_TEAM_DOOR1, B_TEAM_DOOR2, C_TEAM_DOOR1, C_TEAM_DOOR2, D_TEAM_DOOR1, D_TEAM_DOOR2;
	private L1NpcInstance boss_A1, boss_A2, boss_B1, boss_B2, boss_C1, boss_C2, boss_D1, boss_D2, boss_LAST;
	
	private Map<Integer, L1NpcInstance[]> _monlist	= new HashMap<Integer, L1NpcInstance[]>();
	private List<L1PcInstance> pclist				= new ArrayList<L1PcInstance>();
	
	private int LimitTime = 1800;
	private static final int _mapId = 750;
	private static final int COMMON_GUAGE = getCommonGauge();// 기준이될 게이지
	
	private static BattleColoseum _instance;
	public static BattleColoseum getInstance() {
		if (_instance == null) {
			_instance = new BattleColoseum();
		}
		return _instance;
	}
	
	private static final int BEFORE_MINUTE = 5;

	private boolean running = true;

	private boolean _enter;
	public boolean getEnter(){
		return _enter;
	}
	public void setEnter(boolean enter){
		_enter = enter;
	}
	
	public void addPc(L1PcInstance pc){
		pclist.add(pc);
	}
	public void removePc(L1PcInstance pc){
		pclist.remove(pc);
	}
	public void clearPclist(){
		pclist.clear();
	}
	public boolean isPclist(L1PcInstance pc){
		return pclist.contains(pc);
	} 
	public int getPclistCount(){
		return pclist.size();
	}
	
	public int getTeamMemberCount(int team){
		int count = 0;
		for (L1PcInstance pc : pclist) {
			if (pc.getColoTeam() == team) {
				count++;
			}
		}
		return count;
	}
	
	private void sendServerMessage(int msg) {
		S_ServerMessage message = new S_ServerMessage(msg);
		for (L1PcInstance pc : pclist) {
			pc.sendPackets(message);
		}
		message.clear();
		message = null;
	}
	
	private void sendTeamServerMessage(int team, int msg) {
		S_ServerMessage message = new S_ServerMessage(msg);
		for (L1PcInstance pc : pclist) {
			if (pc.getColoTeam() == team) {
				pc.sendPackets(message);
			}
		}
		message.clear();
		message = null;
	}
	
	private void sendGreenMessage(String msg) {
		S_PacketBox message = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg);
		for (L1PcInstance pc : pclist) {
			pc.sendPackets(message);
		}
		message.clear();
		message = null;
	}
	
	private void sendTeamGreenMessage(int team, String msg) {
		S_PacketBox message = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg);
		for (L1PcInstance pc : pclist) {
			if (pc.getColoTeam() == team) {
				pc.sendPackets(message);
			}
		}
		message.clear();
		message = null;
	}
	
	private void broadcastNpc(String msg) {
		for (L1TeleporterInstance tel_npc : L1World.getInstance().getAllTeleporter()) {
			if (tel_npc.getNpcId() == 150035) {
				tel_npc.broadcastPacket(new S_NpcChatPacket(tel_npc, msg, ChatType.CHAT_NORMAL), true);
			}
		}
	}
	
	private void countDown() throws InterruptedException {
		/*
		"The Colosseum showdown match will begin in 4 minutes."
		"The Colosseum showdown match will begin in 3 minutes."
		"The Colosseum showdown match will begin in 2 minutes."
		"The Colosseum showdown match will begin in 1 minute."
		분: server 106
		ref + 1 + minutos + ref
		*/
		for (int loop = 0; loop < BEFORE_MINUTE * 60 - 10; loop++) {
			if (loop == 60) {
				//broadcastNpc("4분 뒤에 격전의 콜로세움 경기를 시작합니다."); // CHECKED OK
				broadcastNpc(S_SystemMessage.getRefText(1059) + "4 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(1055));
				sendServerMessage(7535); // 4분 뒤에 격전의 콜로세움 경기를 시작합니다.
				sendGreenMessage(S_SystemMessage.getRefText(1059) + "4 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(1055));
			} else if (loop == 120) {
				//broadcastNpc("3분 뒤에 격전의 콜로세움 경기를 시작합니다."); // CHECKED OK
				broadcastNpc(S_SystemMessage.getRefText(1059) + "3 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(1055));
				sendServerMessage(7536); // 3분 뒤에 격전의 콜로세움 경기를 시작합니다.
				sendGreenMessage(S_SystemMessage.getRefText(1059) + "3 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(1055));
			} else if (loop == 180) {
				//broadcastNpc("2분 뒤에 격전의 콜로세움 경기를 시작합니다."); // CHECKED OK
				broadcastNpc(S_SystemMessage.getRefText(1059) + "2 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(1055));
				sendServerMessage(7537); // 2분 뒤에 격전의 콜로세움 경기를 시작합니다.
				sendGreenMessage(S_SystemMessage.getRefText(1059) + "2 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(1055));
			} else if (loop == 240) {
				//broadcastNpc("1분 뒤에 격전의 콜로세움 경기를 시작합니다."); // CHECKED OK
				broadcastNpc(S_SystemMessage.getRefText(1059) + "1 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(1055));
				sendServerMessage(7538); // 1분 뒤에 격전의 콜로세움 경기를 시작합니다. // CHECKED OK
				sendGreenMessage(S_SystemMessage.getRefText(1059) + "1 " + S_SystemMessage.getRefText(106) + S_SystemMessage.getRefText(1055));
			}
			Thread.sleep(1000);
		}
		// "The Colosseum showdown match will begin in 10 seconds."
		// 초: server 719
		sendServerMessage(7494); // 10초 뒤에 격전의 콜로세움 경기를 시작합니다.
		//sendGreenMessage("10초 뒤에 격전의 콜로세움 경기를 시작합니다."); // CHECKED OK
		sendGreenMessage(S_SystemMessage.getRefText(1059) + "10 " + S_SystemMessage.getRefText(719) + S_SystemMessage.getRefText(1055));

		// "The Colosseum showdown match has started."
		Thread.sleep(10000);
		broadcastNpc(S_SystemMessage.getRefText(1026));
		System.out.println("■■■■■■■■■■ Colosseum Showdown Start  ■■■■■■■■■■");
	}

	@Override
	public void run() {
		try {
			spawn_door();	//문스폰
			setEnter(true); //입장가능
			countDown();	//5분딜레이
			A_TEAM = true;
			B_TEAM = true;
			C_TEAM = true;
			D_TEAM = true;
			TimeCheck();
			setEnter(false);//입장불가
			while(running){
				try {
					check_A_TEAM();
					check_B_TEAM();
					check_C_TEAM();
					check_D_TEAM();
					checkStep();
					if (boss_LAST != null && boss_LAST.isDead()) {
						boss_LAST = null;
						Thread.sleep(30000);
						endColoseum();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					try{
						checkPc();
						sendTeamChart();
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void TimeCheck(){
		if (LimitTime > 0) {
			LimitTime--;
		}
		// "In 5 minutes, the Colosseum will end." --> "In" + 5 + "minutes, the Colosseum will end." | "" + 5 + "분 뒤 콜로세움이 종료됩니다.""
		// "In 30 seconds, the Colosseum will end."
		if (LimitTime == 300) {
			//sendGreenMessage("5분 뒤 콜로세움이 종료됩니다."); // CHECKED OK
			sendGreenMessage(S_SystemMessage.getRefText(1001) + "5 " + S_SystemMessage.getRefText(943));
		} else if (LimitTime == 240) {
			//sendGreenMessage("4분 뒤 콜로세움이 종료됩니다."); // CHECKED OK
			sendGreenMessage(S_SystemMessage.getRefText(1001) + "4 " + S_SystemMessage.getRefText(943));
		} else if (LimitTime == 180) {
			//sendGreenMessage("3분 뒤 콜로세움이 종료됩니다."); // CHECKED OK
			sendGreenMessage(S_SystemMessage.getRefText(1001) + "3 " + S_SystemMessage.getRefText(943));
		} else if (LimitTime == 120) {
			//sendGreenMessage("2분 뒤 콜로세움이 종료됩니다."); // CHECKED OK
			sendGreenMessage(S_SystemMessage.getRefText(1001) + "2 " + S_SystemMessage.getRefText(943));
		} else if (LimitTime == 60) {
			//sendGreenMessage("1분 뒤 콜로세움이 종료됩니다."); // CHECKED OK
			sendGreenMessage(S_SystemMessage.getRefText(1001) + "1 " + S_SystemMessage.getRefText(943));
		} else if (LimitTime == 30) {
			//sendGreenMessage("30초 후에 콜로세움이 종료됩니다."); // CHECKED OK
			sendGreenMessage(S_SystemMessage.getRefText(1001) + "30 " + S_SystemMessage.getRefText(944));
		}
		if (LimitTime <= 0) {
			endColoseum();
		}
	}
	
	private void RETURN_TEL(){
		int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
		for (L1PcInstance pc : pclist) {
			if (pc != null && pc.getMapId() == _mapId)
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
		}
		loc = null;
		running = false;
	}
	
	private void sendTeamChart(){
		double AteamPercent = 0, BteamPercent = 0, CteamPercent = 0, DteamPercent = 0;
		L1NpcInstance[] list	= null;
		L1NpcInstance npc		= null;
		for (int i=1; i<5; i++) {
			list = _monlist.get(i);
			if (list == null) {
				continue;
			}
			for (int j=0; j<list.length; j++) {
				npc = list[j];
				if (npc == null || npc.isDead()) {
					if (i == 1) {
						AteamPercent += npc.getMaxHp();
					} else if (i == 1) {
						BteamPercent += npc.getMaxHp();
					} else if (i == 1) {
						CteamPercent += npc.getMaxHp();
					} else if (i == 1) {
						DteamPercent += npc.getMaxHp();
					}
				} else {
					if (i == 1) {
						AteamPercent += npc.getMaxHp() - npc.getCurrentHp();
					} else if (i == 1) {
						BteamPercent += npc.getMaxHp() - npc.getCurrentHp();
					} else if (i == 1) {
						CteamPercent += npc.getMaxHp() - npc.getCurrentHp();
					} else if (i == 1) {
						DteamPercent += npc.getMaxHp() - npc.getCurrentHp();
					}
				}
			}
		}
		AteamPercent = (AteamPercent / COMMON_GUAGE) * 100;
		BteamPercent = (BteamPercent / COMMON_GUAGE) * 100;
		CteamPercent = (CteamPercent / COMMON_GUAGE) * 100;
		DteamPercent = (DteamPercent / COMMON_GUAGE) * 100;
		
		S_InfinityBattleBoardInfoNoti chart = new S_InfinityBattleBoardInfoNoti((int)AteamPercent, (int)BteamPercent, (int)CteamPercent, (int)DteamPercent);
		L1PcInstance pc = null;
		for (int i = 0; i < pclist.size(); i++) {
			pc = pclist.get(i);
			if (pc == null || pc.getMapId() != _mapId) {
				continue;
			}
			pc.sendPackets(chart);
		}
		chart.clear();
		chart = null;
	}
	
	private void check_A_TEAM(){
		if(!A_TEAM)return;
		if(A_TEAM_STEP == 0){
			AteamSpwanMonster();
			A_TEAM_STEP++;
		}else{
			boolean dieCheck = false;
			if(_monlist.get(1) != null){
				for(L1NpcInstance npc : _monlist.get(1)){
					if(npc != null && !npc.isDead()){
						dieCheck = true;
						break;
					}
				}
			}
			if(!dieCheck){
				if(A_TEAM_STEP >= 1 && A_TEAM_STEP <= 8 || A_TEAM_STEP >= 10 && A_TEAM_STEP <= 18 || A_TEAM_STEP == 20){
					if(A_TEAM_STEP == 10 || A_TEAM_STEP == 20){
						if(A_TEAM_STEP_DELAY >= 120){ //2분간 쉰다
							AteamSpwanMonster();
							A_TEAM_STEP++;
							A_TEAM_STEP_DELAY = 0;
						}else
							A_TEAM_STEP_DELAY++;
						//"In 10 seconds, Round 2 will begin."
						//"In 2 minutes, the final round will start."
						//if(A_TEAM_STEP == 10 && A_TEAM_STEP_DELAY == 110)sendTeamGreenMessage(1, "10초 뒤에 2라운드 경기를 시작합니다.");
						//if(A_TEAM_STEP == 20 && A_TEAM_STEP_DELAY == 1)sendTeamGreenMessage(1, "2분 뒤에 최종 라운드가 시작합니다.");
						if(A_TEAM_STEP == 10 && A_TEAM_STEP_DELAY == 110)sendTeamGreenMessage(1, S_SystemMessage.getRefText(945));
						if(A_TEAM_STEP == 20 && A_TEAM_STEP_DELAY == 1)sendTeamGreenMessage(1, S_SystemMessage.getRefText(946));
					}else{
						AteamSpwanMonster();
						A_TEAM_STEP++;
					}
				}
				if(A_TEAM_STEP == 9 || A_TEAM_STEP == 19){
					if(A_TEAM_STEP_DELAY >= 60){ //1분간 쉰다
						if((A_TEAM_STEP == 9 && boss_A1 == null) || (A_TEAM_STEP == 19 && boss_A2 == null))
							AteamSpwanMonster();
						if((A_TEAM_STEP == 9 && boss_A1 != null && boss_A1.isDead()) || (A_TEAM_STEP == 19 && boss_A2 != null && boss_A2.isDead())){
							if(boss_A1 != null)boss_A1 = null;
							if(boss_A2 != null)boss_A2 = null;
							A_TEAM_STEP++;
							A_TEAM_STEP_DELAY = 0;
						}
					}else
						A_TEAM_STEP_DELAY++;
				}
			}
		}
	}
	private void check_B_TEAM(){
		if(!B_TEAM)return;
		if(B_TEAM_STEP == 0){
			BteamSpwanMonster();
			B_TEAM_STEP++;
		}else{
			boolean dieCheck = false;
			if(_monlist.get(2) != null){
				for(L1NpcInstance npc : _monlist.get(2)){
					if(npc != null && !npc.isDead()){
						dieCheck = true;
						break;
					}
				}
			}
			if(!dieCheck){
				if(B_TEAM_STEP >= 1 && B_TEAM_STEP <= 8 || B_TEAM_STEP >= 10 && B_TEAM_STEP <= 18 || B_TEAM_STEP == 20){
					if(B_TEAM_STEP == 10 || B_TEAM_STEP == 20){
						if(B_TEAM_STEP_DELAY >= 120){ //2분간 쉰다
							BteamSpwanMonster();
							B_TEAM_STEP++;
							B_TEAM_STEP_DELAY = 0;
						}else
							B_TEAM_STEP_DELAY++;
						//if(B_TEAM_STEP == 10 && B_TEAM_STEP_DELAY == 110)sendTeamGreenMessage(2, "10초 뒤에 2라운드 경기를 시작합니다.");
						//if(B_TEAM_STEP == 20 && B_TEAM_STEP_DELAY == 1)sendTeamGreenMessage(2, "2분 뒤에 최종 라운드가 시작합니다.");
						if(B_TEAM_STEP == 10 && B_TEAM_STEP_DELAY == 110)sendTeamGreenMessage(2, S_SystemMessage.getRefText(945));
						if(B_TEAM_STEP == 20 && B_TEAM_STEP_DELAY == 1)sendTeamGreenMessage(2, S_SystemMessage.getRefText(946));
					}else{
						BteamSpwanMonster();
						B_TEAM_STEP++;
					}
				}
				if(B_TEAM_STEP == 9 || B_TEAM_STEP == 19){
					if(B_TEAM_STEP_DELAY >= 60){ //1분간 쉰다
						if((B_TEAM_STEP == 9 && boss_B1 == null) || (B_TEAM_STEP == 19 && boss_B2 == null))
							BteamSpwanMonster();
						if((B_TEAM_STEP == 9 && boss_B1 != null && boss_B1.isDead()) || (B_TEAM_STEP == 19 && boss_B2 != null && boss_B2.isDead())){
							if(boss_B1 != null)boss_B1 = null;
							if(boss_B2 != null)boss_B2 = null;
							B_TEAM_STEP++;
							B_TEAM_STEP_DELAY = 0;
						}
					}else
						B_TEAM_STEP_DELAY++;
				}
			}
		}
	}
	private void check_C_TEAM(){
		if(!C_TEAM)return;
		if(C_TEAM_STEP == 0){
			CteamSpwanMonster();
			C_TEAM_STEP++;
		}else{
			boolean dieCheck = false;
			if(_monlist.get(3) != null){
				for(L1NpcInstance npc : _monlist.get(3)){
					if(npc != null && !npc.isDead()){
						dieCheck = true;
						break;
					}
				}
			}
			if(!dieCheck){
				if(C_TEAM_STEP >= 1 && C_TEAM_STEP <= 8 || C_TEAM_STEP >= 10 && C_TEAM_STEP <= 18 || C_TEAM_STEP == 20){
					if(C_TEAM_STEP == 10 || C_TEAM_STEP == 20){
						if(C_TEAM_STEP_DELAY >= 120){ //2분간 쉰다
							CteamSpwanMonster();
							C_TEAM_STEP++;
							C_TEAM_STEP_DELAY = 0;
						}else
							C_TEAM_STEP_DELAY++;
						//if(C_TEAM_STEP == 10 && C_TEAM_STEP_DELAY == 110)sendTeamGreenMessage(3, "10초 뒤에 2라운드 경기를 시작합니다.");
						//if(C_TEAM_STEP == 20 && C_TEAM_STEP_DELAY == 1)sendTeamGreenMessage(3, "2분 뒤에 최종 라운드가 시작합니다.");
						if(C_TEAM_STEP == 10 && C_TEAM_STEP_DELAY == 110)sendTeamGreenMessage(3, S_SystemMessage.getRefText(945));
						if(C_TEAM_STEP == 20 && C_TEAM_STEP_DELAY == 1)sendTeamGreenMessage(3, S_SystemMessage.getRefText(946));
					}else{
						CteamSpwanMonster();
						C_TEAM_STEP++;
					}
				}
				if(C_TEAM_STEP == 9 || C_TEAM_STEP == 19){
					if(C_TEAM_STEP_DELAY >= 60){ //1분간 쉰다
						if((C_TEAM_STEP == 9 && boss_C1 == null) || (C_TEAM_STEP == 19 && boss_C2 == null))
							CteamSpwanMonster();
						if((C_TEAM_STEP == 9 && boss_C1 != null && boss_C1.isDead()) || (C_TEAM_STEP == 19 && boss_C2 != null && boss_C2.isDead())){
							if(boss_C1 != null)boss_C1 = null;
							if(boss_C2 != null)boss_C2 = null;
							C_TEAM_STEP++;
							C_TEAM_STEP_DELAY = 0;
						}
					}else
						C_TEAM_STEP_DELAY++;
				}
			}
		}
	}
	private void check_D_TEAM(){
		if(!D_TEAM)return;
		if(D_TEAM_STEP == 0){
			DteamSpwanMonster();
			D_TEAM_STEP++;
		}else{
			boolean dieCheck = false;
			if(_monlist.get(4) != null){
				for(L1NpcInstance npc : _monlist.get(4)){
					if(npc != null && !npc.isDead()){
						dieCheck = true;
						break;
					}
				}
			}
			if(!dieCheck){
				if(D_TEAM_STEP >= 1 && D_TEAM_STEP <= 8 || D_TEAM_STEP >= 10 && D_TEAM_STEP <= 18 || D_TEAM_STEP == 20){
					if(D_TEAM_STEP == 10 || D_TEAM_STEP == 20){
						if(D_TEAM_STEP_DELAY >= 120){ //2분간 쉰다
							DteamSpwanMonster();
							D_TEAM_STEP++;
							D_TEAM_STEP_DELAY = 0;
						}else
							D_TEAM_STEP_DELAY++;
						if(D_TEAM_STEP == 10 && D_TEAM_STEP_DELAY == 110)sendTeamGreenMessage(4, S_SystemMessage.getRefText(945));
						if(D_TEAM_STEP == 20 && D_TEAM_STEP_DELAY == 1)sendTeamGreenMessage(4, S_SystemMessage.getRefText(946));
					}else{
						DteamSpwanMonster();
						D_TEAM_STEP++;
					}
				}
				if(D_TEAM_STEP == 9 || D_TEAM_STEP == 19){
					if(D_TEAM_STEP_DELAY >= 60){ //1분간 쉰다
						if((D_TEAM_STEP == 9 && boss_D1 == null) || (D_TEAM_STEP == 19 && boss_D2 == null))
							DteamSpwanMonster();
						if((D_TEAM_STEP == 9 && boss_D1 != null && boss_D1.isDead()) || (D_TEAM_STEP == 19 && boss_D2 != null && boss_D2.isDead())){
							if(boss_D1 != null)boss_D1 = null;
							if(boss_D2 != null)boss_D2 = null;
							D_TEAM_STEP++;
							D_TEAM_STEP_DELAY = 0;
						}
					}else
						D_TEAM_STEP_DELAY++;
				}
			}
		}
	}
	
	int firstDoor = 0, secondDoor = 0;
	private void checkStep(){
		if(A_TEAM_STEP == 10 && A_TEAM && firstDoor < 3 && !A_TEAM_DOOR1){
			firstDoor++;
			openDoor(9001);
			//sendTeamGreenMessage(1, "1라운드가 종료되었습니다. 다음 지역으로 이동해야 합니다.");
			//sendTeamGreenMessage(1, "1라운드 보스 공략 성공! 다음 지역으로 이동하여 전투를 준비하십시오");
			sendTeamGreenMessage(1, S_SystemMessage.getRefText(924));
			sendTeamGreenMessage(1, S_SystemMessage.getRefText(925));
			A_TEAM_DOOR1 = true;
		}
		if(B_TEAM_STEP == 10 && B_TEAM && firstDoor < 3 && !B_TEAM_DOOR1){
			firstDoor++;
			openDoor(9002);
			//sendTeamGreenMessage(2, "1라운드가 종료되었습니다. 다음 지역으로 이동해야 합니다.");
			//sendTeamGreenMessage(2, "1라운드 보스 공략 성공! 다음 지역으로 이동하여 전투를 준비하십시오");
			sendTeamGreenMessage(2, S_SystemMessage.getRefText(924));
			sendTeamGreenMessage(2, S_SystemMessage.getRefText(925));
			B_TEAM_DOOR1 = true;
		}
		if(C_TEAM_STEP == 10 && C_TEAM && firstDoor < 3 && !C_TEAM_DOOR1){
			firstDoor++;
			openDoor(9003);
			//sendTeamGreenMessage(3, "1라운드가 종료되었습니다. 다음 지역으로 이동해야 합니다.");
			//sendTeamGreenMessage(3, "1라운드 보스 공략 성공! 다음 지역으로 이동하여 전투를 준비하십시오");
			sendTeamGreenMessage(3, S_SystemMessage.getRefText(924));
			sendTeamGreenMessage(3, S_SystemMessage.getRefText(925));
			C_TEAM_DOOR1 = true;
		}
		if(D_TEAM_STEP == 10 && D_TEAM && firstDoor < 3 && !D_TEAM_DOOR1){
			firstDoor++;
			openDoor(9004);
			//sendTeamGreenMessage(4, "1라운드가 종료되었습니다. 다음 지역으로 이동해야 합니다.");
			//sendTeamGreenMessage(4, "1라운드 보스 공략 성공! 다음 지역으로 이동하여 전투를 준비하십시오");
			sendTeamGreenMessage(4, S_SystemMessage.getRefText(924));
			sendTeamGreenMessage(4, S_SystemMessage.getRefText(925));
			D_TEAM_DOOR1 = true;
		}
		
		if(A_TEAM_STEP < 10 && firstDoor >= 3 && A_TEAM){
			A_TEAM = false;
			sendTeamGreenMessage(1, S_SystemMessage.getRefText(926));
		}
		if(B_TEAM_STEP < 10 && firstDoor >= 3 && B_TEAM){
			B_TEAM = false;
			sendTeamGreenMessage(2, S_SystemMessage.getRefText(926));
		}
		if(C_TEAM_STEP < 10 && firstDoor >= 3 && C_TEAM){
			C_TEAM = false;
			sendTeamGreenMessage(3, S_SystemMessage.getRefText(926));
		}
		if(D_TEAM_STEP < 10 && firstDoor >= 3 && D_TEAM){
			D_TEAM = false;
			sendTeamGreenMessage(4, S_SystemMessage.getRefText(926));
		}
		
		if(A_TEAM_STEP == 20 && A_TEAM && secondDoor < 1 && !A_TEAM_DOOR2){
			secondDoor++;
			openDoor(9005);
			sendTeamGreenMessage(1, S_SystemMessage.getRefText(909));
			sendTeamGreenMessage(1, S_SystemMessage.getRefText(910));
			A_TEAM_DOOR2 = true;
		}
		if(B_TEAM_STEP == 20 && B_TEAM && secondDoor < 1 && !B_TEAM_DOOR2){
			secondDoor++;
			openDoor(9006);
			sendTeamGreenMessage(2, S_SystemMessage.getRefText(909));
			sendTeamGreenMessage(2, S_SystemMessage.getRefText(910));
			B_TEAM_DOOR2 = true;
		}
		if(C_TEAM_STEP == 20 && C_TEAM && secondDoor < 1 && !C_TEAM_DOOR2){
			secondDoor++;
			openDoor(9007);
			sendTeamGreenMessage(3, S_SystemMessage.getRefText(909));
			sendTeamGreenMessage(3, S_SystemMessage.getRefText(910));
			C_TEAM_DOOR2 = true;
		}
		if(D_TEAM_STEP == 20 && D_TEAM && secondDoor < 1 && !D_TEAM_DOOR2){
			secondDoor++;
			openDoor(9008);
			sendTeamGreenMessage(4, S_SystemMessage.getRefText(909));
			sendTeamGreenMessage(4, S_SystemMessage.getRefText(910));
			D_TEAM_DOOR2 = true;
		}
		
		if(A_TEAM_STEP < 20 && secondDoor >= 1 && A_TEAM){
			A_TEAM = false;
			sendTeamGreenMessage(1, S_SystemMessage.getRefText(729));
		}
		if(B_TEAM_STEP < 20 && secondDoor >= 1 && B_TEAM){
			B_TEAM = false;
			sendTeamGreenMessage(2, S_SystemMessage.getRefText(729));
		}
		if(C_TEAM_STEP < 20 && secondDoor >= 1 && C_TEAM){
			C_TEAM = false;
			sendTeamGreenMessage(3, S_SystemMessage.getRefText(729));
		}
		if(D_TEAM_STEP < 20 && secondDoor >= 1 && D_TEAM){
			D_TEAM = false;
			sendTeamGreenMessage(4, S_SystemMessage.getRefText(729));
		}
	}
	
	/** 몬스터 리스트 **/
	private static final int [][] monster = { 
			{30153, 10}, {30154, 10}, {30155, 10}, {30156, 10}, {30157, 10}, {30158, 10}, {30159, 10}, {30167, 4}, {30168, 10}, {30150, 1},// 1단계
			{30160, 10}, {30161, 10}, {30162, 10}, {30163, 10}, {30164, 10}, {30165, 10}, {30166, 4}, {30173, 10}, {30174, 4}, {30151, 1},// 2단계
			{30152, 1}// 최종보스
	};
	final int _radomrange = 10;
	private void AteamSpwanMonster(){
		if(A_TEAM_STEP <= 9)		spawn(1, 32777, 33378, (short)_mapId, 5, monster[A_TEAM_STEP][0], _radomrange, monster[A_TEAM_STEP][1]);
		else if(A_TEAM_STEP <= 19)	spawn(1, 32726, 33380, (short)_mapId, 5, monster[A_TEAM_STEP][0], _radomrange, monster[A_TEAM_STEP][1]);
		else						spawn(1, 32679, 33378, (short)_mapId, 5, monster[A_TEAM_STEP][0], _radomrange, monster[A_TEAM_STEP][1]);
		
		if(A_TEAM_STEP == 0)		sendTeamServerMessage(1, 7495); // 1라운드 시작~!
		else if(A_TEAM_STEP == 8)	sendTeamServerMessage(1, 7496); // 몬스터 공략 완료! 곧 1라운드 보스가 투입될 예정입니다.
		else if(A_TEAM_STEP == 10)	sendTeamServerMessage(1, 7499); // 2라운드 시작~!
		else if(A_TEAM_STEP == 18)	sendTeamServerMessage(1, 7500); // 몬스터 공략 완료! 곧 2라운드 보스가 투입될 예정입니다.
		else if(A_TEAM_STEP == 20){
			sendTeamServerMessage(1, 7503); // 최종전 개시! 제한 시간은 10분입니다.
			if(LimitTime > 600)LimitTime = 600;
		}
			
	}
	private void BteamSpwanMonster(){
		if(B_TEAM_STEP < 9)			spawn(2, 32678, 33480, (short)_mapId, 5, monster[B_TEAM_STEP][0], _radomrange, monster[B_TEAM_STEP][1]);
		else if(B_TEAM_STEP < 19)	spawn(2, 32679, 33431, (short)_mapId, 5, monster[B_TEAM_STEP][0], _radomrange, monster[B_TEAM_STEP][1]);
		else						spawn(2, 32679, 33378, (short)_mapId, 5, monster[B_TEAM_STEP][0], _radomrange, monster[B_TEAM_STEP][1]);
		
		if(B_TEAM_STEP == 0)		sendTeamServerMessage(2, 7495); // 1라운드 시작~!
		else if(B_TEAM_STEP == 8)	sendTeamServerMessage(2, 7496); // 몬스터 공략 완료! 곧 1라운드 보스가 투입될 예정입니다.
		else if(B_TEAM_STEP == 10)	sendTeamServerMessage(2, 7499); // 2라운드 시작~!
		else if(B_TEAM_STEP == 18)	sendTeamServerMessage(2, 7500); // 몬스터 공략 완료! 곧 2라운드 보스가 투입될 예정입니다.
		else if(B_TEAM_STEP == 20){
			sendTeamServerMessage(2, 7503); // 최종전 개시! 제한 시간은 10분입니다.
			if(LimitTime > 600)LimitTime = 600;
		}
	}
	private void CteamSpwanMonster(){
		if(C_TEAM_STEP < 9)			spawn(3, 32580, 33380, (short)_mapId, 5, monster[C_TEAM_STEP][0], _radomrange, monster[C_TEAM_STEP][1]);
		else if(C_TEAM_STEP < 19)	spawn(3, 32628, 33380, (short)_mapId, 5, monster[C_TEAM_STEP][0], _radomrange, monster[C_TEAM_STEP][1]);
		else						spawn(3, 32679, 33378, (short)_mapId, 5, monster[C_TEAM_STEP][0], _radomrange, monster[C_TEAM_STEP][1]);
		
		if(C_TEAM_STEP == 0)		sendTeamServerMessage(3, 7495); // 1라운드 시작~!
		else if(C_TEAM_STEP == 8)	sendTeamServerMessage(3, 7496); // 몬스터 공략 완료! 곧 1라운드 보스가 투입될 예정입니다.
		else if(C_TEAM_STEP == 10)	sendTeamServerMessage(3, 7499); // 2라운드 시작~!
		else if(C_TEAM_STEP == 18)	sendTeamServerMessage(3, 7500); // 몬스터 공략 완료! 곧 2라운드 보스가 투입될 예정입니다.
		else if(C_TEAM_STEP == 20){
			sendTeamServerMessage(3, 7503); // 최종전 개시! 제한 시간은 10분입니다.
			if(LimitTime > 600)LimitTime = 600;
		}
	}
	private void DteamSpwanMonster(){
		if(D_TEAM_STEP < 9)			spawn(4, 32680, 33273, (short)_mapId, 5, monster[D_TEAM_STEP][0], _radomrange, monster[D_TEAM_STEP][1]);
		else if(D_TEAM_STEP < 19)	spawn(4, 32680, 33329, (short)_mapId, 5, monster[D_TEAM_STEP][0], _radomrange, monster[D_TEAM_STEP][1]);
		else						spawn(4, 32679, 33378, (short)_mapId, 5, monster[D_TEAM_STEP][0], _radomrange, monster[D_TEAM_STEP][1]);
		
		if(D_TEAM_STEP == 0)		sendTeamServerMessage(4, 7495); // 1라운드 시작~!
		else if(D_TEAM_STEP == 8)	sendTeamServerMessage(4, 7496); // 몬스터 공략 완료! 곧 1라운드 보스가 투입될 예정입니다.
		else if(D_TEAM_STEP == 10)	sendTeamServerMessage(4, 7499); // 2라운드 시작~!
		else if(D_TEAM_STEP == 18)	sendTeamServerMessage(4, 7500); // 몬스터 공략 완료! 곧 2라운드 보스가 투입될 예정입니다.
		else if(D_TEAM_STEP == 20){
			sendTeamServerMessage(4, 7503); // 최종전 개시! 제한 시간은 10분입니다.
			if(LimitTime > 600)LimitTime = 600;
		}
	}
	
	private static int getCommonGauge(){
		int totalValue = 0;
		L1Npc npc = null;
		for(int[] steps : monster){
			int npcId = steps[0];
			int count = steps[1];
			npc = NpcTable.getInstance().getTemplate(npcId);
			if(npc == null)continue;
			totalValue += (npc.getHp() * count);
		}
		return totalValue;
	}
	
	private void spawn(int type, int x, int y, short MapId, int Heading, int npcId, int randomRange, int count) {
		L1NpcInstance[] list = new L1NpcInstance [count];
		for(int i=0; i<list.length; i++){
			try {
				L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
				npc.setId(IdFactory.getInstance().nextId());
				npc.setMap(MapId);
				if(randomRange == 0){
					npc.getLocation().set(x, y, MapId);
					npc.getLocation().forward(Heading);
				}else{
					int tryCount = 0;
					do{
						tryCount++;
						npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
						npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
						if(npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation()))
							break;
						Thread.sleep(1);
					}while(tryCount < 50);
					if(tryCount >= 50)
						npc.getLocation().forward(Heading);
				}
				npc.setHomeX(npc.getX());
				npc.setHomeY(npc.getY());
				npc.getMoveState().setHeading(Heading);

				L1World world = L1World.getInstance();
				world.storeObject(npc);
				world.addVisibleObject(npc);
				
				npc.getLight().turnOnOffLight();
				npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
				if(type == 1 && npcId == 30150)boss_A1 = npc;
				if(type == 1 && npcId == 30151)boss_A2 = npc;
				if(type == 2 && npcId == 30150)boss_B1 = npc;
				if(type == 2 && npcId == 30151)boss_B2 = npc;
				if(type == 3 && npcId == 30150)boss_C1 = npc;
				if(type == 3 && npcId == 30151)boss_C2 = npc;
				if(type == 4 && npcId == 30150)boss_D1 = npc;
				if(type == 4 && npcId == 30151)boss_D2 = npc;
				if(npcId == 30152)boss_LAST = npc;
				list[i] = npc;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		_monlist.put(type, list);
	}
	
	private void openDoor(int doorId) {
		L1DoorInstance door = null;
		for(L1Object obj : L1World.getInstance().getVisibleObjects(_mapId).values()){
			if(obj instanceof L1DoorInstance){
				door = (L1DoorInstance) obj;
				if(door.getDoorId() == doorId){
					door.setCurrentHp(0);
					door.setDead(true);
					door.isPassibleDoor(true);
					door.setActionStatus(ActionCodes.ACTION_Open);
					door.getMap().setPassable(door.getLocation(), true);
					Broadcaster.broadcastPacket(door, new S_DoActionGFX(door.getId(), ActionCodes.ACTION_Open), true);
					door.deleteMe();
				}
			}
		}
	}
	
	private void doorSpawn(int x, int y, short map, int doorid, boolean xy) {
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(81158);
			if (l1npc != null) {
				String s = l1npc.getImpl();
				Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
				Object parameters[] = { l1npc };
				L1DoorInstance door = (L1DoorInstance) constructor.newInstance(parameters);
				door = (L1DoorInstance) constructor.newInstance(parameters);
				door.setId(IdFactory.getInstance().nextId());

				door.setDoorId(doorid);
				door.setSpriteId(xy ? 18727 : 18728);
				door.setX(x);
				door.setY(y);
				door.setMap((short) map);
				door.setHomeX(door.getX());
				door.setHomeY(door.getY());
				door.setDirection(xy ? 0 : 1);
				door.setLeftEdgeLocation(xy ? door.getX() - 1 : door.getY() - 7);
				door.setRightEdgeLocation(xy ? door.getX() + 7 : door.getY() + 1);
				door.setMaxHp(0);
				door.setCurrentHp(0);
				door.setKeeperId(doorid);

				L1World world = L1World.getInstance();
				for(L1PcInstance pc : world.getVisiblePlayer(door)){
					door.onPerceive(pc);
					S_DoActionGFX gfx = new S_DoActionGFX(door.getId(), ActionCodes.ACTION_Close);
					pc.sendPackets(gfx, true);
				}
				
				door.isPassibleDoor(false);
				
				world.storeObject(door);
				world.addVisibleObject(door);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void spawn_door(){
		doorSpawn(32742, 33383, (short) _mapId, 9001, false); //1시
		doorSpawn(32675, 33448, (short) _mapId, 9002, true); //4시
		doorSpawn(32611, 33383, (short) _mapId, 9003, false); //7시
		doorSpawn(32677, 33311, (short) _mapId, 9004, true); //10시
		
		doorSpawn(32706, 33382, (short) _mapId, 9005, false); //1시
		doorSpawn(32676, 33412, (short) _mapId, 9006, true); //4시
		doorSpawn(32644, 33382, (short) _mapId, 9007, false); //7시
		doorSpawn(32676, 33349, (short) _mapId, 9008, true); //10시
	}

	private void checkPc() {
		if (pclist == null || pclist.isEmpty()) {
			endColoseum();
			return;
		}
		for (int i = 0; i < pclist.size(); i++) {
			if (pclist.get(i) == null) {
				continue;
			}
			if (pclist.get(i).getMapId() == _mapId) {
				continue;
			}
			pclist.get(i).setColoTeam(0);
			pclist.get(i).sendPackets(S_InfinityBattleLeaveMapNoti.LEAVE);
			if (isPclist(pclist.get(i))) {
				removePc(pclist.get(i));
			}
		}
		if (getTeamMemberCount(1) <= 0) {
			A_TEAM = false;
		}
		if (getTeamMemberCount(2) <= 0) {
			B_TEAM = false;
		}
		if (getTeamMemberCount(3) <= 0) {
			C_TEAM = false;
		}
		if (getTeamMemberCount(4) <= 0) {
			D_TEAM = false;
		}
	}
	
	public void startColoseum() {
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}
	
	private void endColoseum() {
		if (running) {
			running = false;
		}
		RETURN_TEL();
		L1World world = L1World.getInstance();
		Collection<L1Object> cklist = world.getVisibleObjects(_mapId).values();
		for (L1Object obj : cklist) {
			if (obj == null) {
				continue;
			}
			if (obj instanceof L1DollInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
				continue;
			}
			if (obj instanceof L1ItemInstance) {
				L1ItemInstance item = (L1ItemInstance)obj;
				L1Inventory groundInventory = world.getInventory(item.getX(), item.getY(), item.getMapId());
				groundInventory.removeItem(item);
			} else if (obj instanceof L1DoorInstance) {
				L1DoorInstance door = (L1DoorInstance)obj;
				door.isPassibleDoor(true);
				door.getMap().setPassable(door.getLocation(), true);
				door.deleteMe();
			} else if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance)obj;
				npc.deleteMe();
			}
		}
		if (pclist != null) {
			pclist.clear();
			pclist = null;
		}
		if (_monlist != null) {
			_monlist.clear();
			_monlist = null;
		}
		System.out.println("■■■■■■■■■■ Battle for Colosseum ends ■■■■■■■■■■");
	}

}

