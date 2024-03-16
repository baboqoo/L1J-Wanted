package l1j.server.GameSystem.beginnerquest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestCollectItem;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestDropItem;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestKillNpc;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestTemp;
import l1j.server.common.bin.QuestCommonBinLoader;
import l1j.server.common.bin.quest.QuestT;
import l1j.server.common.data.OptionalRewardT;
import l1j.server.common.data.RewardListT;
import l1j.server.common.data.RewardT;
import l1j.server.common.data.RewardT.eType;
import l1j.server.server.command.CommandArgs;
import l1j.server.server.command.CommandInterface;
import l1j.server.server.command.CommandTree;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

import org.apache.mina.util.ConcurrentHashSet;

/**
 * 초급 퀘스트 테이블 로더
 * @author LinOffice
 */
public class BeginnerQuestTable implements CommandInterface {
	private static BeginnerQuestTable _instance;
	public static BeginnerQuestTable getInstance() {
		if (_instance == null) {
			_instance = new BeginnerQuestTable();
		}
		return _instance;
	}

	private static final ConcurrentHashMap<Integer, L1QuestTemp> TEMP_INFOS		= new ConcurrentHashMap<>();
	private static final ConcurrentHashSet<Integer> BLOCK_LIST					= new ConcurrentHashSet<>();// 사용안하는 퀘스트 리스트
	private static final ConcurrentHashSet<Integer> AUTO_COMPLETE_LIST			= new ConcurrentHashSet<>();// 자동 완성되는 퀘스트 리스트
	private static final ConcurrentHashMap<Integer, Integer> FAST_LEVEL_MAP		= new ConcurrentHashMap<>();// 레벨 지급 퀘스트 리스트
	
	/**
	 * 사용 금지 퀘스트인지 조사
	 * @param questId
	 * @return boolean
	 */
	public static boolean isQuestBlock(int questId){
		return BLOCK_LIST.contains(questId);
	}
	
	/**
	 * 자동 완성 퀘스트인지 조사
	 * @param questId
	 * @return boolean
	 */
	public static boolean isAutoCompleteQuest(int questId){
		return AUTO_COMPLETE_LIST.contains(questId);
	}
	
	/**
	 * 빠른 레벨업 퀘스트인지 조사
	 * @param questId
	 * @return boolean
	 */
	public static boolean isFastLevel(int questId){
		return FAST_LEVEL_MAP.containsKey(questId);
	}
	
	/**
	 * 빠른 레벨업 퀘스트의 달성 레벨 조사
	 * @param questId
	 * @return int
	 */
	public static int getFastLevel(int questId){
		return FAST_LEVEL_MAP.get(questId);
	}
	
	private CommandTree _commands;
	
	/**
	 * 생성자
	 */
	private BeginnerQuestTable() {
		loadQuestInfo();
		loadDetailInfo();
		_commands = createCommand();
	}

	public static void reload() {
		BLOCK_LIST.clear();
		AUTO_COMPLETE_LIST.clear();
		FAST_LEVEL_MAP.clear();
		TEMP_INFOS.clear();
		_instance = new BeginnerQuestTable();
	}
	
	/**
	 * 퀘스트 정보 로드
	 */
	void loadQuestInfo() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM beginner_quest ORDER BY quest_id ASC");
			rs		= pstm.executeQuery();
			L1QuestTemp info = null;
			while(rs.next()){
				int questId = rs.getInt("quest_id");
				QuestT commonInfo = QuestCommonBinLoader.getQuest(questId);
				if (commonInfo == null || commonInfo.get_Obsolete()) {
					continue;
				}
				info = new L1QuestTemp(
						questId,
						rs.getString("note"),
						Boolean.valueOf(rs.getString("use")),
						Boolean.valueOf(rs.getString("auto_complete")), 
						rs.getInt("fastLevel"),
						commonInfo);
				TEMP_INFOS.put(questId, info);
				if (!info.isUse()) {
					BLOCK_LIST.add(info.getQuestId());
				}
				if (info.isAutoComplete()) {
					AUTO_COMPLETE_LIST.add(info.getQuestId());
				}
				if (info.getFastLevel() > 0) {
					FAST_LEVEL_MAP.put(info.getQuestId(), info.getFastLevel());
				}
			}
			SQLUtil.close(rs, pstm);
			
			pstm	= con.prepareStatement("SELECT * FROM beginner_quest_drop");
			rs		= pstm.executeQuery();
			L1QuestDropItem drop = null;
			while(rs.next()){
				int classId			= rs.getInt("classId");
				int mainQuestId		= rs.getInt("mainQuestId");
				ArrayList<L1Npc> npcList	= NpcTable.getInstance().getTemplateToClassIdList(classId);
				if (npcList == null || npcList.isEmpty()) {
					System.out.println(String.format("[BeginnerQuestTable] DROP_NPC_TEMPLATE_EMPTY : CLASS_ID(%d), QUEST_ID(%d)", classId, mainQuestId));
					continue;
				}
				int mainItemNameId	= rs.getInt("mainItemNameId");
				L1Item item = ItemTable.getInstance().findItemByNameId(mainItemNameId);
				if (item == null) {
					System.out.println(String.format("[BeginnerQuestTable] DROP_MAIN_ITEM_TEMPLATE_EMPTY : NAME_ID(%d), QUEST_ID(%d)", mainItemNameId, mainQuestId));
					continue;
				}
				
				int subQuestId		= rs.getInt("subQuestId");
				int subItemNameId	= rs.getInt("subItemNameId");
				if (subQuestId > 0 && subItemNameId > 0) {
					item = ItemTable.getInstance().findItemByNameId(subItemNameId);
					if (item == null) {
						System.out.println(String.format("[BeginnerQuestTable] DROP_SUB_ITEM_TEMPLATE_EMPTY : NAME_ID(%d), QUEST_ID(%d)", subItemNameId, subQuestId));
						continue;
					}
				}
				
				drop = new L1QuestDropItem(
						classId,
						mainQuestId,
						mainItemNameId, 
						subQuestId,
						subItemNameId);
				
				for (L1Npc npc : npcList) {
					npc.setQuestDropNpc(drop);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	/**
	 * 퀘스트 상세 정보
	 */
	void loadDetailInfo() {
		for (L1QuestTemp temp : TEMP_INFOS.values()) {
			java.util.LinkedList<QuestT.ObjectiveT> list = temp.getBin().get_ObjectiveList().get_Objective();
			for (QuestT.ObjectiveT obj : list) {
				switch (obj.get_Type()) {
				case KILL_NPC:
					ArrayList<L1Npc> npcList = NpcTable.getInstance().getTemplateToClassIdList(obj.get_AssetID());
					if (npcList == null || npcList.isEmpty()) {
						System.out.println(String.format("[BeginnerQuestTable] KILL_NPC_TEMPLATE_EMPTY : CLASS_ID(%d), QUEST_ID(%d)", obj.get_AssetID(), temp.getQuestId()));
						break;
					}
					L1QuestKillNpc killNpc = new L1QuestKillNpc(
							obj.get_AssetID(),
							temp.getQuestId(),
							obj.get_ID(),
							obj.get_RequiredQuantity());
					for (L1Npc npc : npcList) {
						npc.setQuestKillNpc(killNpc);
					}
					break;
				case COLLECT_ITEM:
					L1Item item = ItemTable.getInstance().findItemByNameId(obj.get_AssetID());
					if (item == null) {
						System.out.println(String.format("[BeginnerQuestTable] COLLECT_ITEM_TEMPLATE_EMPTY : NAME_ID(%d), QUEST_ID(%d)", obj.get_AssetID(), temp.getQuestId()));
						break;
					}
					L1QuestCollectItem collectItem = new L1QuestCollectItem(
							obj.get_AssetID(),
							temp.getQuestId(),
							obj.get_ID(),
							obj.get_RequiredQuantity());
					item.setQuestCollectItem(collectItem);
					break;
				default:
					break;
				}
			}
			
			RewardListT advanceList = temp.getBin().get_AdvanceRewardList();
			if (advanceList != null && advanceList.get_Reward() != null) {
				for (RewardT reward : advanceList.get_Reward()) {
					if (reward.get_Type() == eType.ITEM) {
						L1Item item = ItemTable.getInstance().findItemByNameId(reward.get_AssetID());
						if (item == null) {
							System.out.println(String.format("[BeginnerQuestTable] ADVANCE_ITEM_TEMPLATE_EMPTY : NAME_ID(%d), QUEST_ID(%d)", reward.get_AssetID(), temp.getQuestId()));
						}
					}
				}
			}
			
			RewardListT rewardList = temp.getBin().get_RewardList();
			if (rewardList != null && rewardList.get_Reward() != null) {
				for (RewardT reward : rewardList.get_Reward()) {
					if (reward.get_Type() == eType.ITEM) {
						L1Item item = ItemTable.getInstance().findItemByNameId(reward.get_AssetID());
						if (item == null) {
							System.out.println(String.format("[BeginnerQuestTable] REWARD_ITEM_TEMPLATE_EMPTY : NAME_ID(%d), QUEST_ID(%d)", reward.get_AssetID(), temp.getQuestId()));
						}
					}
				}
			}
			
			OptionalRewardT optionalList = temp.getBin().get_OptionalRewardList();
			if (optionalList != null && optionalList.get_Reward() != null) {
				for (RewardT reward : optionalList.get_Reward()) {
					if (reward.get_Type() == eType.ITEM) {
						L1Item item = ItemTable.getInstance().findItemByNameId(reward.get_AssetID());
						if (item == null) {
							System.out.println(String.format("[BeginnerQuestTable] OPTIONAL_ITEM_TEMPLATE_EMPTY : NAME_ID(%d), QUEST_ID(%d)", reward.get_AssetID(), temp.getQuestId()));
						}
					}
				}
			}
		}
	}
	
	CommandTree createCommand(){
		//return new CommandTree(".퀘스트", "초급 퀘스트 관련 명령어를 실행합니다.", null)
		return new CommandTree(".beginnerquest", "Executes beginner quest-related commands.", null)
		.add_command(createDatabaseCommand())
		.add_command(createUserCommand());
	}
	
	CommandTree createDatabaseCommand() {
		//return new CommandTree("디비", "디비 관련 명령을 수행합니다.", null)
		//.add_command(new CommandTree("정보", "데이터 정보를 출력 합니다.", null){
		return new CommandTree("db", "Executes database-related commands.", null)
		.add_command(new CommandTree("information", "Output data information.", null){			
			@Override
			protected void to_handle_command(CommandArgs args) throws Exception{
				L1PcInstance gm = args.getOwner();
				for (L1QuestTemp val : TEMP_INFOS.values()) {
					gm.sendPackets(new S_SystemMessage(val.toString()), true);
				}
			}
		})
		//.add_command(new CommandTree("리로드", "데이터를 리로드 합니다.", null){
		.add_command(new CommandTree("reload", "Reload data.", null){
			@Override
			protected void to_handle_command(CommandArgs args) throws Exception{
				try {
					reload();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	CommandTree createUserCommand() {
		//return new CommandTree("유저", "유저 관련 명령을 수행합니다.", null)
		//.add_command(new CommandTree("정보", "유저의 퀘스트 정보를 출력합니다.", new String[]{"케릭터이름"}){
		return new CommandTree("user", "Performs user-related commands.", null)
		.add_command(new CommandTree("information", "Outputs the user's quest information.", new String[]{"charactername"}){			
			@Override
			protected void to_handle_command(CommandArgs args) throws Exception{
				String character_name = args.nextString();
				L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
				L1PcInstance gm = args.getOwner();
				if (pc == null) {
					//args.notify(String.format("케릭터 이름 %s을(를) 찾을 수 없습니다.", character_name));
					args.notify(String.format("Character name %s not found.", character_name));
					return;					
				}
				ConcurrentHashMap<Integer, L1QuestProgress> list = pc.getQuest().getQuestProgressList();
				if (list == null || list.isEmpty()) {
					//args.notify(String.format("%s 캐릭터의 퀘스트 정보가 없습니다.", character_name));
					args.notify(String.format("There is no quest information for character %s.", character_name));
					return;
				}
				//gm.sendPackets(new S_SystemMessage(String.format("%s님의 퀘스트 정보", character_name)), true);
				gm.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(2), character_name), true);
				for (L1QuestProgress val : list.values()) {
					gm.sendPackets(new S_SystemMessage(val.toString()), true);
				}
			}
		})
		//.add_command(new CommandTree("완료", "유저의 퀘스트 정보를 완료합니다.", null)
		//	.add_command(new CommandTree("번호", "", new String[]{"케릭터이름", "퀘스트번호"}){
		.add_command(new CommandTree("complete", "Complete the user's quest information.", null)
			.add_command(new CommandTree("number", "", new String[]{"charactername", "questnumber"}){
				@Override
				protected void to_handle_command(CommandArgs args) throws Exception{
					String character_name = args.nextString();
					int questid = Integer.parseInt(args.nextString());
					L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
					L1PcInstance gm = args.getOwner();
					if (pc == null) {
						//args.notify(String.format("케릭터 이름 %s을(를) 찾을 수 없습니다.", character_name));
						args.notify(String.format("Character name %s not found.", character_name));
						return;					
					}
					L1QuestProgress progress = pc.getQuest().getQuestProgress(questid);
					if (progress == null) {
						//args.notify(String.format("%s 캐릭터의 퀘스트 정보가 없습니다.", character_name));
						args.notify(String.format("There is no quest information for character %s.", character_name));
						return;
					}
					pc.getQuest().questSkip(progress);
					//gm.sendPackets(new S_SystemMessage(String.format("%s님의 %d번 퀘스트를 완료처리 하였습니다.", character_name, questid)), true);
					gm.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(3), character_name, String.valueOf(questid)), true);
				}
			})
			//.add_command(new CommandTree("전체", "", new String[]{"케릭터이름"}){
			.add_command(new CommandTree("all", "", new String[]{"charactername"}){
				@Override
				protected void to_handle_command(CommandArgs args) throws Exception{
					String character_name = args.nextString();
					L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
					L1PcInstance gm = args.getOwner();
					if (pc == null) {
						//args.notify(String.format("케릭터 이름 %s을(를) 찾을 수 없습니다.", character_name));
						args.notify(String.format("Character name %s not found.", character_name));
						return;					
					}
					ConcurrentHashMap<Integer, L1QuestProgress> list = pc.getQuest().getQuestProgressList();
					if (list == null || list.isEmpty()) {
						//args.notify(String.format("%s 캐릭터의 퀘스트 정보가 없습니다.", character_name));
						args.notify(String.format("There is no quest information for character %s.", character_name));
						return;
					}
					for (L1QuestProgress val : list.values()) {
						pc.getQuest().questSkip(val);
					}
			 		//gm.sendPackets(new S_SystemMessage(String.format("%s님의 전체 퀘스트를 완료처리 하였습니다.", character_name)), true);
					gm.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(4), character_name), true);
				}
			})
		)
		//.add_command(new CommandTree("초기화", "유저의 퀘스트 정보를 초기화합니다.", new String[]{"케릭터이름"}){
		.add_command(new CommandTree("initialization", "Initializes the user's quest information.", new String[]{"charactername"}){
			@Override
			protected void to_handle_command(CommandArgs args) throws Exception{
				String character_name = args.nextString();
				L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
				L1PcInstance gm = args.getOwner();
				if (pc == null) {
					//args.notify(String.format("케릭터 이름 %s을(를) 찾을 수 없습니다.", character_name));
					args.notify(String.format("Character name %s not found.", character_name));
					return;					
				}
				ConcurrentHashMap<Integer, L1QuestProgress> list = pc.getQuest().getQuestProgressList();
				if (list == null || list.isEmpty()) {
					//args.notify(String.format("%s 캐릭터의 퀘스트 정보가 없습니다.", character_name));
					args.notify(String.format("There is no quest information for character %s.", character_name));
					return;
				}
				for (L1QuestProgress val : list.values()) {
					val.setStartTime(0);
					val.setFinishTime(0);
					for (int key : val.getObjectives().keySet()) {
						val.setQuantity(key, 0);
					}
				}
				//gm.sendPackets(new S_SystemMessage(String.format("%s님의 퀘스트 초기화 완료", character_name)), true);
				gm.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(5), character_name), true);
			}
		});
	}
	
	@Override
	public void execute(CommandArgs args) {
		_commands.execute(args, new StringBuilder(256).append(_commands.to_operation()));
	}
	
}

