package l1j.server.IndunSystem.valakasroom;

import java.util.ArrayList;
import java.util.Calendar;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class ValakasStart implements Runnable {
	private short _map;
	private int stage					= 1;
	private static final int FIRST_STEP	= 1;
	private static final int WAIT_RAID	= 2;
	private static final int VALAKAS	= 3;
	private static final int END		= 4;

	private L1NpcInstance death;
	private L1NpcInstance ifrit;
	private L1NpcInstance phoenix;
	private L1NpcInstance valakas;
	private L1NpcInstance leo1;
	private L1NpcInstance leo2;
	private L1NpcInstance leo3;

	private L1DoorInstance briddge1;
	private L1DoorInstance briddge2;
	private L1DoorInstance briddge3;

	private L1PcInstance owner;

	private boolean Running = true;

	public ArrayList<L1NpcInstance> BasicNpcList;
	private ArrayList<L1NpcInstance> NpcList;
	public ArrayList<L1NpcInstance> BossList;

	public ValakasStart(int id, L1PcInstance pc){
		_map	= (short)id;
		owner	= pc;
	}

	@Override
	public void run() {
		setting();
		NpcList = ValakasUtil.getInstance().fillSpawnTable(_map, 1, true);
		while(Running){
			try {

				if(NpcList != null){
					for(L1NpcInstance npc : NpcList){
						if(npc == null || npc.isDead())
							NpcList.remove(npc);
					}
				}

				if(leo1 != null && leo1.isDead() 
						&& ((briddge1.getOpenStatus() == ActionCodes.ACTION_Close))) {
					NpcList.remove(leo1);
					briddge1.open();
				}
				if(leo2 != null && leo2.isDead()
						&& ((briddge2.getOpenStatus() == ActionCodes.ACTION_Close))) {
					NpcList.remove(leo2);
					briddge2.open();
				}
				if(leo3 != null && leo3.isDead()
						&& ((briddge3.getOpenStatus() == ActionCodes.ACTION_Close))) {
					NpcList.remove(leo3);
					briddge3.open();
				}
				
				if (BossList != null && valakas != null && owner.isInValakasBoss) {
					stage = VALAKAS;
				}

				if(ifrit != null && ifrit.isDead()) {
					BossList.remove(ifrit);
					stage = END;
				} 
				if(phoenix != null && phoenix.isDead()) {
					BossList.remove(phoenix);
					stage = END;
				} 
				if(valakas != null && valakas.isDead()) {
					BossList.remove(valakas);
					stage = END;
				}


				switch(stage){
				case FIRST_STEP:
					Thread.sleep(2000); 
					//그 검의 힘을 이용하여 어서 이 곳을 빠져나가게.
					Broadcaster.broadcastPacket(death, new S_NpcChatPacket(death, "$18645"), true);
					Thread.sleep(3000); 
					//사용하면 저주의 영향 때문인지 과거 나와 발라카스의 전투가 자꾸 떠오르더군...
					Broadcaster.broadcastPacket(death, new S_NpcChatPacket(death, "$18646"), true);
					Thread.sleep(3000);
					S_NpcChatPacket s_chatpacket = new S_NpcChatPacket(death, "$18647");
					//어쩌면 자네도 그 전투를 볼 수 있겠군. 가게나..더 이상 버틸 수 없을 것 같네.
					Broadcaster.broadcastPacket(death, s_chatpacket, true);
					stage = WAIT_RAID;
					break;
				case WAIT_RAID:
					break;
				case VALAKAS:
					//발라카스:누가 나를 깨우는가?
					Broadcaster.broadcastPacket(valakas, new S_NpcChatPacket(valakas, "$18869"), true);
					Thread.sleep(2000);
					if(owner.getMapId() == _map){ 
						//데스나이트:발라카스! 드디어 너를 만나게 되는구나..
						owner.sendPackets(new S_NpcChatPacket(owner, "$18870"), true);
					}

					Thread.sleep(2000);
					//발라카스:나의 잠을 깨운 댓가는..나의 노예가 되어 평생 갚게 되리라..
					Broadcaster.broadcastPacket(valakas, new S_NpcChatPacket(valakas, "$18871"), true);
					Thread.sleep(2000);
					if(owner.getMapId() == _map){ 
						//데스나이트:그런 말은 내가 패배했을 때 해도 늦지 않는다.
						owner.sendPackets(new S_NpcChatPacket(owner, "$18872"), true);
					}
					Thread.sleep(2000);
					//발라카스:크크..자신감이 넘치는구나..
					Broadcaster.broadcastPacket(valakas, new S_NpcChatPacket(valakas, "$18873"), true);
					owner.isInValakasBoss = false;
					stage = WAIT_RAID;
					break;
				case END:
					Thread.sleep(2000);
					if(owner.getMapId() == _map){ 
						//pc.sendPackets(new S_ServerMessage(1480), true);  
						//시스템 메시지: 5초 후에 텔레포트 합니다.
//AUTO SRM: 						owner.sendPackets(new S_SystemMessage("잠시 후 마을로 이동됩니다."), true); // CHECKED OK
						owner.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(55), true), true);
					}
					Thread.sleep(3000);
					if(owner.getMapId() == _map){ 
						owner.getTeleport().start(33705, 32504, (short)4, 5, true);
						owner.getInventory().consumeItem(203003);
					}
					Running = false;
					break;
				default:
					break;
				}
				Thread.sleep(1000);
				checkPc();
			}catch(Exception e){
			}finally{
				try{
					Thread.sleep(1500);
				}catch(Exception e){}
			}
		}
		endRaid();
	}

	public void Start(){
		Calendar cal = Calendar.getInstance();
		int _hour = Calendar.HOUR;
		int _minute = Calendar.MINUTE;
		/** 0 오전 , 1 오후 * */
		String AMPM = cal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
		GeneralThreadPool.getInstance().schedule(this,2000);
		System.out.println(cal.get(_hour) + ":" + cal.get(_minute) + " " + AMPM + "   ■■■■■■ Fire Dragon's Nest Begins " +  _map+" ■■■■■■");
		cal = null;
	}

	private void setting(){
		for(L1NpcInstance npc : BasicNpcList){
			if(npc != null){
				//if(npc.getName().equalsIgnoreCase("데스나이트")){ 
				if(npc.getDesc().equalsIgnoreCase("$371")){ //Death Knight
					death = npc;
				}
				//if(npc.getName().equalsIgnoreCase("화룡의 레오")){ 
				if(npc.getDesc().equalsIgnoreCase("$18626")){ //Fire Dragon Leo
					leo1 = npc;
				}
				//if(npc.getName().equalsIgnoreCase("화룡의 붉은 레오")){
				if(npc.getDesc().equalsIgnoreCase("$18627")){ //Crimson Fire Dragon Leo
					leo2 = npc;
				}
				//if(npc.getName().equalsIgnoreCase("화룡의 불타는 레오")){ 
				if(npc.getDesc().equalsIgnoreCase("$18628")){ //Burning Fire Dragon Leo
					leo3 = npc;
				}
				if (npc instanceof L1DoorInstance) {
					L1DoorInstance door = (L1DoorInstance) npc;
					//if(npc.getName().equalsIgnoreCase("다리1")){
					if(npc.getKoreanName().equalsIgnoreCase("다리1")){ //leg 1
						briddge1 = door;
					}
					//if(npc.getName().equalsIgnoreCase("다리2")){
					if(npc.getKoreanName().equalsIgnoreCase("다리2")){ //leg 2
						briddge2 = door;
					}
					//if(npc.getName().equalsIgnoreCase("다리3")){
					if(npc.getKoreanName().equalsIgnoreCase("다리3")){ //legg 3
						briddge3 = door;
					}
				}
			}
		}
		
		for(L1NpcInstance npc : BossList){
			if(npc != null){
				//if(npc.getName().equalsIgnoreCase("이프리트")){ //Ifrit
				if(npc.getDesc().equalsIgnoreCase("$651") || npc.getDesc().equalsIgnoreCase("$1567")){ //Ifrit
					ifrit = npc;
				}
				//if(npc.getName().equalsIgnoreCase("피닉스")){
				if(npc.getDesc().equalsIgnoreCase("$1569") || npc.getDesc().equalsIgnoreCase("$14553")){ //Phoenix
					phoenix = npc;
				}
				//if(npc.getName().equalsIgnoreCase("발라카스")){
				if(npc.getDesc().equalsIgnoreCase("$1605")){ //Valakas
					valakas = npc;
				}
			}
		}
	}

	private void checkPc() {
		int check = L1World.getInstance().getMapPlayer(_map).size();
		if (check == 0) {
			if (owner != null) {
				owner = null;
			}
			Running = false;
		}
	}
	

	private void endRaid(){
		Calendar cal = Calendar.getInstance();
		int _hour = Calendar.HOUR;
		int _minute = Calendar.MINUTE;
		/** 0 오전 , 1 오후 * */
		String AMPM = cal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_map).values()) {
			if(ob == null)
				continue;
			if(ob instanceof L1ItemInstance){
				L1ItemInstance obj = (L1ItemInstance)ob;
				L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
				groundInventory.removeItem(obj);
			}else if(ob instanceof L1NpcInstance){
				L1NpcInstance npc = (L1NpcInstance)ob;
				npc.deleteMe();
			}
		}
		owner = null;
		death = null;
		ifrit = null;
		phoenix = null;
		valakas = null;
		leo1 = null;
		leo2 = null;
		leo3 = null;
		briddge1 = null;
		briddge2 = null;
		briddge3 = null;
		if (NpcList != null) NpcList.clear();
		if (BasicNpcList != null) BasicNpcList.clear();
		if (BossList != null) BossList.clear();
		ValakasRoomCreator.getInstance().removeStart(_map);
		System.out.println(cal.get(_hour) + ":" + cal.get(_minute) + " " + AMPM + "   ■■■■■■ Fire Dragon Nest ends " +  _map+" ■■■■■■");
		cal = null;
	}
}


