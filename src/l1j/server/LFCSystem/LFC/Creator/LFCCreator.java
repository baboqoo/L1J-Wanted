package l1j.server.LFCSystem.LFC.Creator;

import java.util.ArrayList;

import l1j.server.LFCSystem.InstanceSpace;
import l1j.server.LFCSystem.InstanceEnums.InstStatus;
import l1j.server.LFCSystem.InstanceEnums.LFCMessages;
import l1j.server.LFCSystem.LFC.LFCPlayFactory;
import l1j.server.LFCSystem.LFC.LFCType;
import l1j.server.LFCSystem.LFC.Template.LFCObject;
import l1j.server.LFCSystem.Loader.LFCTypeLoader;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1BoardPost;
import l1j.server.server.utils.CommonUtil;

public class LFCCreator implements Runnable{
		protected static final Object 	_lock 	= new Object();
		
		private static boolean isValidItem(L1PcInstance pc, LFCType type){
			if(type.getNeedItemId() == 0)
				return true;
			
			if (!pc.getInventory().checkItem(type.getNeedItemId(), type.getNeedItemCount())) {
				LFCMessages.REGIST_ERR_NOADENA.sendSystemMsg(pc);
				return false;
			}
			return true;
		}
		
		private static void consumeItem(L1PcInstance pc, LFCType type){
			if (type.getNeedItemId() != 0)
				pc.getInventory().consumeItem(type.getNeedItemId(),  type.getNeedItemCount());
		}
		
		protected static boolean isPossibleLfc(L1PcInstance user){
			if (user == null || user.getTradeID() != 0 || user.isPrivateShop() || user.isGhost() || user.isDead())
				return false;
			return true;
		}
		
		protected static boolean isPossibleTeam(ArrayList<L1PcInstance> team){
			for (L1PcInstance pc : team){
				if (!isPossibleLfc(pc))
					return false;
				
				if (pc.getParty() == null || pc.getParty().isLeader(pc))
					continue;
			
				if (pc.getInstStatus() != InstStatus.INST_USERSTATUS_LFCREADY)
					return false;
			}
			return true;
		}
		
		public static void setInstStatus(L1PcInstance pc, InstStatus status){
			synchronized(_lock){
				pc.setInstStatus(status);
			}
		}
		
		private static boolean isValidLevel(ArrayList<L1PcInstance> red, ArrayList<L1PcInstance> blue, LFCType type){
			boolean isLevel = true;
			for (L1PcInstance pc : red){
				if (pc.getLevel() < type.getPossibleLevel()){
					isLevel = false;
					break;
				}
			}
			if (!isLevel){
//				LFCMessages.REGIST_ERR_LEVEL.sendSystemMsgToList(red);
				LFCMessages.REGIST_ERR_LEVEL.sendSystemMsgToList(blue);
				return false;
			}
			for (L1PcInstance pc : blue){
				if (pc.getLevel() < type.getPossibleLevel()){
					isLevel = false;
					break;
				}
			}
			if (!isLevel){
//				LFCMessages.REGIST_ERR_LEVEL.sendSystemMsgToList(red);
				LFCMessages.REGIST_ERR_LEVEL.sendSystemMsgToList(blue);
				return false;
			}
			return true;
		}
		
		private static boolean isMems(ArrayList<L1PcInstance> arr, LFCType type){
			if (arr.size() < type.getMinParty()){
				LFCMessages.CREATE_ERR_PARTYMINSIZE.sendSystemMsgToList(arr);
				return false;
			} else if (arr.size() > type.getMaxParty()){
				LFCMessages.CREATE_ERR_PARTYMAXSIZE.sendSystemMsgToList(arr);
				return false;
			}
			return true;
		}
		
		public static void unregistLfc(L1PcInstance pc){
			 L1BoardPost.delLfc(pc.getName());		
		}
		
		public static void registLfc(L1PcInstance pc, int i){ //등록
			try {
				synchronized(_lock){
					if (pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE){
					    LFCMessages.REGIST_ERR_ININST.sendSystemMsg(pc);
					    unregistLfc(pc);
					    return;
					}
					LFCType type = LFCTypeLoader.getInstance().get(i);
					if (type == null){
						unregistLfc(pc);
						return;
					}
					
					if (!isValidItem(pc, type)){
						LFCMessages.REGIST_ERR_NOADENA.sendSystemMsg(pc);
					    unregistLfc(pc);
					    return;
					}
					consumeItem(pc, type);
					LFCMessages.REGIST_SUCCESS.sendSystemMsg(pc);
				}
			} catch(Exception e){
				   e.printStackTrace();
			}
		}
		
		public static void create(L1BoardPost post, L1PcInstance partUser){
			LFCType type	= null;
			try {
				if (post == null){
//					System.out.println("invalid LFC doc.");
					return;
				}
				type = LFCTypeLoader.getInstance().get(Integer.parseInt(post.getContent()));
			} catch(Exception e){
				/** 잘못된 글이 발생하면 오류를 출력하고 지운다. **/
				e.printStackTrace();
				L1BoardPost.delLfc(post.getId());
				return;
			}
			
			if (type == null)
				return;
			
			String postingName = post.getName();
			//if (partUser.getName().equalsIgnoreCase(postingName))
			if (partUser.getKoreanName().equalsIgnoreCase(postingName))
				return;
			
			if (!type.isUse())
				return;
			
			L1PcInstance ownerUser = L1World.getInstance().getPlayer(postingName);
			if (!isPossibleLfc(ownerUser)){
				LFCMessages.CREATE_ERR_TARGET_CANNOT.sendSystemMsg(partUser);
				return;
			}
			
			ArrayList<L1PcInstance> red; 
			ArrayList<L1PcInstance> blue;
			if (partUser.isInParty()){
				if (partUser.getParty().isLeader(partUser)){
					LFCMessages.CREATE_ERR_ONLYLEADER.sendSystemMsg(partUser);
					return;
				}
				blue 	= (ArrayList<L1PcInstance>)partUser.getParty().getList();
			} else {
				blue	= new ArrayList<L1PcInstance>();
				blue.add(partUser);
			}
			if (!isMems(blue, type))
				return;
			
			if (ownerUser.isInParty()){
				red		= (ArrayList<L1PcInstance>)ownerUser.getParty().getList();
			} else {
				red		= new ArrayList<L1PcInstance>();
				red.add(ownerUser);
			}
			if (!isMems(red, type)){
				LFCMessages.CREATE_ERR_TARGET_CANNOT.sendSystemMsg(partUser);
				return;
			}
			if (!isValidItem(partUser, type))
				return;
			if (!isValidLevel(red, blue, type))
				return;
			
			synchronized(_lock){
				for (L1PcInstance pc : red){
					if (!isPossibleLfc(pc) || pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE){
						LFCMessages.CREATE_ERR_TARGET_CANNOT.sendSystemMsg(partUser);
						return;				
					}
				}
				for (L1PcInstance pc : blue){
					if (!isPossibleLfc(pc) || pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE){
						LFCMessages.CREATE_ERR_PARTYMEMBER.sendSystemMsg(partUser);
						return;								
					}
				}
				for (L1PcInstance pc : red)
					pc.setInstStatus(InstStatus.INST_USERSTATUS_LFCREADY);
				for (L1PcInstance pc : blue)
					pc.setInstStatus(InstStatus.INST_USERSTATUS_LFCREADY);
			}
			GeneralThreadPool.getInstance().execute(new LFCCreator(post, red, blue, type));
		}
		
		private L1BoardPost				_post;
		private ArrayList<L1PcInstance> _red;
		private ArrayList<L1PcInstance> _blue;
		private LFCType 				_type;
		private LFCCreator(L1BoardPost post, ArrayList<L1PcInstance> red, ArrayList<L1PcInstance> blue, LFCType type){
			_post 	= post;
			_red 	= red;
			_blue 	= blue;
			_type 	= type;
		}
			
		@Override
		public void run() {
			L1PcInstance redLeader;
			L1PcInstance blueLeader;
			if (_red.get(0).isInParty())
				redLeader = _red.get(0).getParty().getLeader();
			else
				redLeader = _red.get(0);
			if (_blue.get(0).isInParty())
				blueLeader = _blue.get(0).getParty().getLeader();
			else
				blueLeader = _blue.get(0);
			LFCMessages.CREATE_SUBSCRIBE.sendSurvey(redLeader);
			LFCMessages.CREATE_SUCCESS.sendSystemMsg(blueLeader);
			try {
				for (int i = 0; i < 15; i++){
					Thread.sleep(1000);
					if (redLeader.getInstStatus() == InstStatus.INST_USERSTATUS_NONE)
						break;
					else if (redLeader.getInstStatus() == InstStatus.INST_USERSTATUS_LFCINREADY){
						if (blueLeader.getInstStatus() != InstStatus.INST_USERSTATUS_LFCREADY)
							break;
						else if (!isPossibleTeam(_red))
							break;
						else if (!isPossibleTeam(_blue))
							break;
						else if (!isPossibleLfc(redLeader))
							break;
						else if (!isPossibleLfc(blueLeader))
							break;
						else if (!isValidItem(blueLeader, _type))
							break;
						
						consumeItem(blueLeader, _type);
						L1BoardPost.delLfc(_post.getId());
						for (L1PcInstance pc : _red)
							setInstStatus(pc, InstStatus.INST_USERSTATUS_LFCINREADY);
						for (L1PcInstance pc : _blue)
							setInstStatus(pc, InstStatus.INST_USERSTATUS_LFCINREADY);
						LFCObject obj = LFCPlayFactory.create(_type.getPlayInstName());
						
						// 매크로 방지.
						if (CommonUtil.nextBoolean()){
							obj.setRedTeam(_red);
							obj.setBlueTeam(_blue);
						} else {
							obj.setRedTeam(_blue);
							obj.setBlueTeam(_red);
						}
						obj.setType(_type);
						InstanceSpace.getInstance().startInstance(obj);
						return;
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			for (L1PcInstance pc : _blue){
				if (pc != null){
					setInstStatus(pc, InstStatus.INST_USERSTATUS_NONE);
					LFCMessages.CREATE_CANCEL.sendSystemMsg(pc);
				}
			}
			for (L1PcInstance pc : _red){
				if (pc != null){
					setInstStatus(pc, InstStatus.INST_USERSTATUS_NONE);
					LFCMessages.CREATE_CANCEL.sendSystemMsg(pc);
				}			
			}
		}
}

