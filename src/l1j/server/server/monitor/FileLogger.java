package l1j.server.server.monitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyExpObject;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyItemObject;
import l1j.server.GameSystem.tjcoupon.TJCoupon;
import l1j.server.GameSystem.tjcoupon.bean.TJLogBean;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.templates.L1UserRanking;
import l1j.server.server.utils.StringUtil;

public class FileLogger implements Logger {
	private static String date = StringUtil.EmptyString;	
	
	private ArrayList<String> _chatlog;
	private ArrayList<String> _commandlog;
	private ArrayList<String> _connectionlog;
	private ArrayList<String> _enchantlog;
	private ArrayList<String> _craftlog;
	private ArrayList<String> _alchemylog;
	private ArrayList<String> _potentiallog;
	private ArrayList<String> _einStatelog;
	private ArrayList<String> _tradelog;
	private ArrayList<String> _warehouselog;
	private ArrayList<String> _itemactionlog;
	private ArrayList<String> _levellog;
	private ArrayList<String> _shoplog;
	private ArrayList<String> _ranklog;
	private ArrayList<String> _tjlog;
	private ArrayList<String> _deathpenaltylog;
	private ArrayList<String> _webserver;
	private ArrayList<String> _webaction;
	private ArrayList<String> _connector;
	private ArrayList<String> _favorBookLog;
	private ArrayList<String> _timeCollectionLog;
	private ArrayList<String> _smeltingMakeLog;
	private ArrayList<String> _smeltingSlotLog;
	private ArrayList<String> _einhasadFaithLog;
	private ArrayList<String> _pcmasterGoldenBuffEnforceLog;
	
	private ArrayList<TJLogBean> _enchantFailLog;
	
	public FileLogger() {
		_chatlog						= new ArrayList<String>();
		_commandlog						= new ArrayList<String>();
		_connectionlog					= new ArrayList<String>();
		_enchantlog						= new ArrayList<String>();
		_craftlog						= new ArrayList<String>();
		_alchemylog						= new ArrayList<String>();
		_potentiallog					= new ArrayList<String>();
		_einStatelog					= new ArrayList<String>();
		_tradelog						= new ArrayList<String>();
		_warehouselog					= new ArrayList<String>();
		_itemactionlog					= new ArrayList<String>();
		_levellog						= new ArrayList<String>();
		_shoplog						= new ArrayList<String>();
		_ranklog						= new ArrayList<String>();
		_tjlog							= new ArrayList<String>();
		_deathpenaltylog				= new ArrayList<String>();
		_webserver						= new ArrayList<String>();
		_webaction						= new ArrayList<String>();
		_connector						= new ArrayList<String>();
		_favorBookLog					= new ArrayList<String>();
		_timeCollectionLog				= new ArrayList<String>();
		_smeltingMakeLog				= new ArrayList<String>();
		_smeltingSlotLog				= new ArrayList<String>();
		_einhasadFaithLog				= new ArrayList<String>();
		_pcmasterGoldenBuffEnforceLog	= new ArrayList<String>();
		_enchantFailLog					= new ArrayList<TJLogBean>();
	}
	
	@Override
	public void addChat(ChatType type, L1PcInstance pc, String msg) {
		String log = StringUtil.EmptyString;
		switch (type) {
		case Clan:
			//log = String.format("%s\t혈맹(%s)\t[%s]\t%s\r\n", getLocalTime(), pc.getClanName(), pc.getName(), msg);
			log = String.format("%s\tClan(%s)\t[%s]\t%s\r\n", getLocalTime(), pc.getClanName(), pc.getName(), msg) ;
			break;
		case Global:
			//log = String.format("%s\t전체\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			log = String.format("%s\tGlobal\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			break;
		case Normal:
			//log = String.format("%s\t일반\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			log = String.format("%s\tGeneral\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			break;
		case Alliance:
			//log = String.format("%s\t동맹\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			log = String.format("%s\tAlliance\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			break;
		case Guardian:
			//log = String.format("%s\t수호\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			log = String.format("%s\tGuardian\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			break;
		case Party:
			//log = String.format("%s\t파티\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			log = String.format("%s\tParty\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			break;
		case Group:
			//log = String.format("%s\t그룹\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			log = String.format("%s\tGroup\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			break;
		case Shouting:
			//log = String.format("%s\t외침\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			log = String.format("%s\tShout\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			break;
		/*장사채팅 로그 기록 남기지 않게 변경
		case Trade:
			log = String.format("%s\t장사\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			break;
			 */
		}
		synchronized (_warehouselog) {
			_chatlog.add(log);
		}
	}
	
	@Override
	public void addWhisper(L1PcInstance pcfrom, L1PcInstance pcto, String msg) {
		// 시간 귓말 케릭->케릭\t내용
		//String log = String.format("%s\t귓말\t[%s] -> [%s]\t%s\r\n", getLocalTime(), pcfrom.getName(), pcto.getName(), msg);
		String log = String.format("%s\tWhisper\t[%s] -> [%s]\t%s\r\n", getLocalTime(), pcfrom.getName(), pcto.getName() , msg);
		synchronized (_chatlog) {
			_chatlog.add(log);
		}
	}

	@Override
	public void addCommand(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);
		synchronized (_commandlog) {
			_commandlog.add(msg);
		}
	}
	
	@Override
	public void addConnection(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);
		synchronized (_connectionlog) {
			_connectionlog.add(msg);
		}
	}

	@Override
	public void addEnchant(L1PcInstance pc, L1ItemInstance item, boolean result) {
		String localTime = getLocalTime();
		String msg = String.format("%s\t[%s]\t%s\t%s\r\n", localTime, pc.getName(), (result ? "SUCCESS" : "FAILURE"), getFormatItemName(item, 1));
		synchronized (_enchantlog) {
			_enchantlog.add(msg);
			if (!result && Config.TJ.TJ_COUPON_ENABLE) {
				addEnchantFailLog(pc, item, localTime);
			}
		}
	}
	
	@Override
	public void addCraft(L1PcInstance pc, String message) {
		String log = String.format("%s\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), message);
		synchronized (_craftlog) {
			_craftlog.add(log);
		}
	}
	
	@Override
	public void addAlchemy(L1PcInstance pc, int number, boolean success, L1ItemInstance item) {
		//String log = String.format("%s\t[%s]\t%s\tALCHEMY: %d\t결과: %s\r\n", getLocalTime(), pc.getName(), (success ? "SUCCESS" : "FAILURE"), number, getFormatItemName(item, 1));
		String log = String.format("%s\t[%s]\t%s\tALCHEMY: %d\tResult: %s\r\n", getLocalTime(), pc.getName(), (success ? "SUCCESS" : "FAILURE"), number, getFormatItemName(item, 1));
		synchronized (_alchemylog) {
			_alchemylog.add(log);
		}
	}
	
	@Override
	public void addPotential(PotentialType type, L1PcInstance pc, int bonus_grade, int bonus_id, boolean isChange, L1ItemInstance item) {
		String log = StringUtil.EmptyString;
		switch (type) {
		case RIGHT:
			//log = String.format("%s\t[%s]\t[정상]\t%s\t잠재력번호: %d\t인형: %s\r\n", getLocalTime(), pc.getName(), (isChange ? "적용" : "취소"), bonus_id, getFormatItemName(item, 1));
			log = String.format("%s\t[%s]\t[Normal]\t%s\tPotential number: %d\tDoll: %s\r\n", getLocalTime(), pc.getName (), (isChange ? "Apply" : "Cancel"), bonus_id, getFormatItemName(item, 1));
			break;
		case DENALS:
			L1CharacterConfig config = pc.getConfig();
			//log = String.format("%s\t[%s]\t[부정]\t정상타겟: %d\t정상단계: %d\t정상번호: %d\t요청타겟: %d\t요청단계: %d\t요청번호: %d\r\n", 
			log = String.format("%s\t[%s]\t[Negative]\tNormal target: %d\tNormal level: %d\tNormal number: %d\tRequest target: %d\t Request level: %d\tRequest number: %d\r\n",
					getLocalTime(), pc.getName(), 
					config.get_potential_target_id(), config.get_potential_bonus_grade(), config.get_potential_bonus_id(), 
					item.getId(), bonus_grade, bonus_id);
			break;
		}
		synchronized (_potentiallog) {
			_potentiallog.add(log);
		}
	}
	
	@Override
	public void addEinState(L1PcInstance pc, boolean success, int state, int usePoint) {
		//String log = String.format("%s\t[%s]\t%s\t획득스탯: %d\t소모포인트: %d\r\n", getLocalTime(), pc.getName(), (success ? "SUCCESS" : "FAILURE"), state, usePoint);
		String log = String.format("%s\t[%s]\t%s\tStats acquired: %d\tPoints consumed: %d\r\n", getLocalTime(), pc.getName(), (success ? "SUCCESS" : "FAILURE"), state, usePoint);
		synchronized (_einStatelog) {
			_einStatelog.add(log);
		}
	}

	@Override
	public void addTrade(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count) {
		// 시간 성공 계정:케릭 [아이디]템이름(갯수) -> 계정:케릭
		// 거래 성공시 로그 기록 남기기 
		//String msg = String.format("%s\t%s\t[%s] --> [%s]\t%s\r\n", getLocalTime(), (success ? "OO완료OO" : "XX취소XX"), pcfrom.getName(), pcto.getName(), getFormatItemName(item, count));
		String msg = String.format("%s\t%s\t[%s] --> [%s]\t%s\r\n", getLocalTime(), (success ? "OO complete OO" : "XXCancelXX"), pcfrom.getName(), pcto.getName(), getFormatItemName(item, count));
		synchronized (_tradelog) {
			_tradelog.add(msg);
		}
	}
	
	@Override
	public void addPrivateShop(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count) {
		// 시간 성공 계정:케릭 [아이디]템이름(갯수) -> 계정:케릭
		// 거래 성공시 로그 기록 남기기 
		//String msg = String.format("%s\t%s\t[%s]\t%s\t[%s]\r\n", getLocalTime(), (success ? "시장구매" : "시장판매"), pcfrom.getName(), getFormatItemName(item, count), pcto.getName());
		String msg = String.format("%s\t%s\t[%s]\t%s\t[%s]\r\n", getLocalTime(), (success ? "Market Purchase" : "Market sales"), pcfrom.getName(), getFormatItemName(item, count), pcto.getName());
		synchronized (_tradelog) {
			_tradelog.add(msg);
		}
	}

	public void addWarehouse(WarehouseType type, boolean put, L1PcInstance pc, L1ItemInstance item, int count) {
		String msg = StringUtil.EmptyString;
		// 시간 타입 동작 계정:케릭명 [아이디]아이템(갯수)
		switch (type) {
		case Private:
			//msg = String.format("%s\t개인:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "IN" : "OUT"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			msg = String.format("%s\tPrivate:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "IN" : "OUT"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;
		case Clan:
			//msg = String.format("%s\t혈맹(%s):%s\t%s:[%s]\t%s\r\n", getLocalTime(), pc.getClanName(), (put ? "IN" : "OUT"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			msg = String.format("%s\tClan(%s):%s\t%s:[%s]\t%s\r\n", getLocalTime(), pc.getClanName(), (put ? "IN" : "OUT"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;
		case Package:
			//msg = String.format("%s\t패키지:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "IN" : "OUT"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			msg = String.format("%s\tPackage:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "IN" : "OUT"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));			
			break;
		case Elf:
			//msg = String.format("%s\t요정:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "IN" : "OUT"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			msg = String.format("%s\tElf:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "IN" : "OUT"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;
		default:break;
		}
		synchronized (_warehouselog) {
			_warehouselog.add(msg);
		}
	}
	
	public void addItemAction(ItemActionType type, L1PcInstance pc, L1ItemInstance item, int count) {
		String msg = StringUtil.EmptyString;
		// 시간 타입 계정:케릭명 [아이디]아이템(갯수)
		switch (type) {
		case Pickup:
			//msg = String.format("%s\t줍기\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, count));
			msg = String.format("%s\tPick up\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, count));
			break;
			/* 오토루팅 로그 기록 남기지 않게 변경 
		case AutoLoot:
			msg = String.format("%s\t오토루팅\t%s:[%s]\t%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
			break;
			 */
		case Drop:
			//msg = String.format("%s\t버림\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, count));
			msg = String.format("%s\tDrop\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, count));
			break;
		case Delete:
			//msg = String.format("%s\t삭제\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, count));
			msg = String.format("%s\tDelete\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, count));
			break;
		case del:
			//msg = String.format("%s\t증발\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, count));
			msg = String.format("%s\tEvaporation\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, count));
			break;	
		case del1:
			//msg = String.format("%s\t떨굼\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, count));
			msg = String.format("%s\tDel*\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, count));
			break;
		}
		synchronized (_itemactionlog) {
			_itemactionlog.add(msg);
		}
	}
	
	private void addEnchantFailLog(L1PcInstance pc, L1ItemInstance item, String localTime){
		_enchantFailLog.add(new TJLogBean(pc.getId(), item, localTime));
	}
	
	public void addLevel(L1PcInstance pc, int level) {
		//String msg = String.format("%s\t[%s]\tLEVELUP %d\r\n", getLocalTime(), pc.getName(), level);
		String msg = String.format("%s\t[%s]\tLEVELUP %d\r\n", getLocalTime(), pc.getName(), level);
		synchronized (_levellog) {
			_levellog.add(msg);
		}
	}
	
	@Override
	public void addShop(ShopLogType type, L1PcInstance pc, int npcId, L1ItemInstance item, int count, int totalPrice) {
		String msg = StringUtil.EmptyString;
		switch(type){
		case BUY:
			//msg = String.format("%s\t구매\t[%s]\tNPCID: %d\t%s\t총금액: %d\r\n", getLocalTime(), pc.getName(), npcId, getFormatItemName(item, count), totalPrice);
			msg = String.format("%s\tPurchase\t[%s]\tNPCID: %d\t%s\tTotal amount: %d\r\n", getLocalTime(), pc.getName(), npcId , getFormatItemName(item, count), totalPrice);
			break;
		case SELL:
			//msg = String.format("%s\t판매\t[%s]\tNPCID: %d\t%s\t총금액: %d\r\n", getLocalTime(), pc.getName(), npcId, getFormatItemName(item, count), totalPrice);
			msg = String.format("%s\tSale\t[%s]\tNPCID: %d\t%s\tTotal amount: %d\r\n", getLocalTime(), pc.getName(), npcId , getFormatItemName(item, count), totalPrice);
			break;
		case PLEDGE_BUY:
			//msg = String.format("%s\t구매\t[%s]\tNPCID: %d\t%s\t총금액: %d\r\n", getLocalTime(), pc.getName(), npcId, item == null ? "혈맹상점 즉시사용" : getFormatItemName(item, count), totalPrice);
			msg = String.format("%s\tClan Purchase\t[%s]\tNPCID: %d\t%s\tTotal amount: %d\r\n", getLocalTime(), pc.getName(), npcId , item == null ? "Use immediately at clan store" : getFormatItemName(item, count), totalPrice);
			break;
		}
		synchronized (_shoplog) {
			_shoplog.add(msg);
		}
	}
	
	@Override
	public void addRank(java.util.LinkedList<L1UserRanking> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		//sb.append(getLocalTime()).append("\t").append("순위 리스트").append("\r\n");
		sb.append(getLocalTime()).append("\t").append("Rank list").append("\r\n");
		L1UserRanking rank = null;
		for (int i=0; i<list.size(); i++) {
			if (i >= 30) {
				break;
			}
			rank = list.get(i);
			//sb.append(rank.getCurRank()).append("\t").append(getStar(i)).append("\t").append(rank.getName()).append("\t").append("이전순위: ").append(rank.getOldRank()).append("\r\n");
			sb.append(rank.getCurRank()).append("\t").append(getStar(i)).append("\t").append(rank.getName()).append("\t" ).append("Old rank: ").append(rank.getOldRank()).append("\r\n");
		}
		synchronized (_ranklog) {
			_ranklog.add(sb.toString());
		}
	}
	
	private static final String[] STARS = {
		"★★★★", "★★★", "★★", "★"
	};
	private String getStar(int i){
		if (i < 10) {
			return STARS[0];
		}
		if (i < 30) {
			return STARS[1];
		}
		if (i < 60) {
			return STARS[2];
		}
		return STARS[3];
	}
	
	@Override
	public void addTjCoupon(L1PcInstance pc, L1ItemInstance item) {
		String msg = String.format("%s\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), getFormatItemName(item, item.getCount()));
		synchronized (_tjlog) {
			_tjlog.add(msg);
		}
	}
	
	@Override
	public void addDeathPenaltyExp(DeathPenaltyType type, L1PcInstance pc, DeathPenaltyExpObject obj, int recovery_item_id) {
		String msg;
		switch (type) {
		case RECOVERY:
			//msg = String.format("%s\t[%s]\t[RECOVERY]\t사망레벨:%d\t경험치:%d\t비용:%d\t사용아이템:%d\r\n", 
			msg = String.format("%s\t[%s]\t[RECOVERY]\tDeath level:%d\tExperience:%d\tCost:%d\tItems used:%d\r\n ",
					getLocalTime(), pc.getName(), obj.get_death_level(), obj.get_exp_value(), obj.get_recovery_cost(), recovery_item_id);
			break;
		default:
			//msg = String.format("%s\t[%s]\t[LOST]\t사망레벨:%d\t경험치:%d\t비용:%d\r\n", 
			msg = String.format("%s\t[%s]\t[LOST]\tDeath level:%d\tExperience:%d\tCost:%d\r\n",
					getLocalTime(), pc.getName(), obj.get_death_level(), obj.get_exp_value(), obj.get_recovery_cost());
			break;
		}
		synchronized (_deathpenaltylog) {
			_deathpenaltylog.add(msg);
		}
	}
	
	@Override
	public void addDeathPenaltyItem(DeathPenaltyType type, L1PcInstance pc, DeathPenaltyItemObject obj) {
		//String msg = String.format("%s\t[%s]\t[%s]\t아이템:%s\t비용:%d\r\n", 
		String msg = String.format("%s\t[%s]\t[%s]\tItem:%s\tCost:%d\r\n",
				getLocalTime(), pc.getName(), type.name(), getFormatItemName(obj.get_recovery_item(), obj.get_recovery_item().getCount()), obj.get_recovery_cost());
		synchronized (_deathpenaltylog) {
			_deathpenaltylog.add(msg);
		}
	}
	
	@Override
	public void addWebserver(String log) {
		String msg = String.format("%s\t%s\r\n", getLocalTime(), log);
		synchronized (_webserver) {
			_webserver.add(msg);
		}
	}
	
	@Override
	public void addWebAction(WebActionType type, String account_name, String character_name, String log) {
		String msg = String.format("%s\t%s\t%s\t%s\t%s\r\n", getLocalTime(), type.name(), account_name, character_name, log);
		synchronized (_webaction) {
			_webaction.add(msg);
		}
	}
	
	@Override
	public void addConnector(ConnectType type, String account, String password, String result) {
		String msg;
		switch(type){
		case INFO:
			msg = String.format("%s\t%s\t%s\r\n", getLocalTime(), type, result);
			break;
		case PROCESS:
			msg = String.format("%s\t%s\t%s\t%s\r\n", getLocalTime(), type, account, result);
			break;
		default:
			msg = String.format("%s\t%s\t%s\t%s\t%s\r\n", getLocalTime(), type, account, password, result);
			break;
		}
		synchronized (_connector) {
			_connector.add(msg);
		}
	}
	
	@Override
	public void addFavorBook(FavorType type, L1PcInstance pc, L1FavorBookUserObject user) {
		L1ItemInstance item = user.getCurrentItem();
		if (item == null) {
			return;
		}
		//String msg = String.format("%s\t%s\t캐릭터:%s\t카테고리:%d\t슬롯번호:%d\t%s\r\n", getLocalTime(), type.name(), pc.getName(), user.getCategory().getCategory(), user.getSlotId(), getFormatItemName(item, item.getCount()));
		String msg = String.format("%s\t%s\tCharacter:%s\tCategory:%d\tSlot number:%d\t%s\r\n", getLocalTime(), type.name (), pc.getName(), user.getCategory().getCategory(), user.getSlotId(), getFormatItemName(item, item.getCount()));
		synchronized (_favorBookLog) {
			_favorBookLog.add(msg);
		}
	}
	
	@Override
	public void addTimeCollection(TimeCollectionType type, L1PcInstance pc, L1TimeCollectionUser user, L1ItemInstance item) {
		String msg;
		switch(type){
		case REGIST:
			//msg = String.format("%s\t%s\t캐릭터:%s\tID:%d\t%s\t완성:%s\t버프시간:%s\r\n", getLocalTime(), type.name(), pc.getName(), user.getSetId(), getFormatItemName(item, item.getCount()), String.valueOf(user.isRegistComplet()), user.getBuffTime() == null ? "null" : user.getBuffTime().toString());
			msg = String.format("%s\t%s\tCharacter:%s\tID:%d\t%s\tCompletion:%s\tBuff time:%s\r\n", getLocalTime() , type.name(), pc.getName(), user.getSetId(), getFormatItemName(item, item.getCount()), String.valueOf(user.isRegistComplet()), user.getBuffTime() == null ? "null" : user.getBuffTime().toString());
			break;
		default:
			//msg = String.format("%s\t%s\t캐릭터:%s\tID:%d\r\n", getLocalTime(), type.name(), pc.getName(), user.getSetId());
			msg = String.format("%s\t%s\tCharacter:%s\tID:%d\r\n", getLocalTime(), type.name(), pc.getName(), user.getSetId( ));
			break;
		}
		synchronized (_timeCollectionLog) {
			_timeCollectionLog.add(msg);
		}
	}
	
	@Override
	public void addSmeltingMake(SmeltingMakeType type, L1PcInstance pc, int alchemy_id, L1ItemInstance stone) {
		//String msg = String.format("%s\t%s\t캐릭터:%s\tALCHEMY_ID:%d\tOUTPUT:%s\r\n", getLocalTime(), type.name(), pc.getName(), alchemy_id, getFormatItemName(stone, stone.getCount()));
		String msg = String.format("%s\t%s\tCharacter:%s\tALCHEMY_ID:%d\tOUTPUT:%s\r\n", getLocalTime(), type.name(), pc.getName( ), alchemy_id, getFormatItemName(stone, stone.getCount()));
		synchronized (_smeltingMakeLog) {
			_smeltingMakeLog.add(msg);
		}
	}
	
	@Override
	public void addSmeltingSlot(SmeltingSlotType type, L1PcInstance pc, L1ItemInstance targetItem, L1ItemInstance stone) {
		//String msg = String.format("%s\t%s\t캐릭터:%s\tTARGET:%s\tSTONE:%s\r\n", getLocalTime(), type.name(), pc.getName(), getFormatItemName(targetItem, targetItem.getCount()), getFormatItemName(stone, stone.getCount()));
		String msg = String.format("%s\t%s\tCharacter:%s\tTARGET:%s\tSTONE:%s\r\n", getLocalTime(), type.name(), pc.getName( ), getFormatItemName(targetItem, targetItem.getCount()), getFormatItemName(stone, stone.getCount()));
		synchronized (_smeltingSlotLog) {
			_smeltingSlotLog.add(msg);
		}
	}
	
	@Override
	public void addEinhasadFaith(L1PcInstance pc, int groupId, int indexId, Timestamp expiredTime) {
		//String msg = String.format("%s\t캐릭터:%s\tGROUP_ID:%d\tINDEX_ID:%d\tEXPIRED_TIME:%s\r\n", getLocalTime(), pc.getName(), groupId, indexId, getFormatTime(expiredTime));
		String msg = String.format("%s\tCharacter:%s\tGROUP_ID:%d\tINDEX_ID:%d\tEXPIRED_TIME:%s\r\n", getLocalTime(), pc.getName(), groupId, indexId , getFormatTime(expiredTime));
		synchronized (_einhasadFaithLog) {
			_einhasadFaithLog.add(msg);
		}
	}
	
	@Override
	public void addPCMasterGoldenBuffEnforce(L1PcInstance pc, int index, int group, int bonus, int grade, int cost, int remainTime) {
		//String msg = String.format("%s\t캐릭터:%s\tINDEX:%d\tGROUP:%d\tBONUS:%d\tGRADE:%d\tCOST:%d\tREMAIN_TIME:%d\r\n", 
		String msg = String.format("%s\tCharacter:%s\tINDEX:%d\tGROUP:%d\tBONUS:%d\tGRADE:%d\tCOST:%d\tREMAIN_TIME:%d\r\n ",
				getLocalTime(), pc.getName(), index, group, bonus, grade, cost, remainTime);
		synchronized (_pcmasterGoldenBuffEnforceLog) {
			_pcmasterGoldenBuffEnforceLog.add(msg);
		}
	}
	
	public void addAll(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);
		synchronized (_chatlog) {
			_chatlog.add(msg);
		}
		synchronized (_commandlog) {
			_commandlog.add(msg);
		}
		synchronized (_connectionlog) {
			_connectionlog.add(msg);
		}
		synchronized (_enchantlog) {
			_enchantlog.add(msg);
		}
		synchronized (_craftlog) {
			_craftlog.add(msg);
		}
		synchronized (_alchemylog) {
			_alchemylog.add(msg);
		}
		synchronized (_potentiallog) {
			_potentiallog.add(msg);
		}
		synchronized (_einStatelog) {
			_einStatelog.add(msg);
		}
		synchronized (_tradelog) {
			_tradelog.add(msg);
		}
		synchronized (_warehouselog) {
			_warehouselog.add(msg);
		}
		synchronized (_itemactionlog) {
			_itemactionlog.add(msg);
		}
		synchronized (_levellog) {
			_levellog.add(msg);
		}
		synchronized (_shoplog) {
			_shoplog.add(msg);
		}
		synchronized (_ranklog) {
			_ranklog.add(msg);
		}
		synchronized (_tjlog) {
			_tjlog.add(msg);
		}
		synchronized (_deathpenaltylog) {
			_deathpenaltylog.add(msg);
		}
		synchronized (_webserver) {
			_webserver.add(msg);
		}
		synchronized (_webaction) {
			_webaction.add(msg);
		}
		synchronized (_connector) {
			_connector.add(msg);
		}
		synchronized (_favorBookLog) {
			_favorBookLog.add(msg);
		}
		synchronized (_timeCollectionLog) {
			_timeCollectionLog.add(msg);
		}
		synchronized (_smeltingMakeLog) {
			_smeltingMakeLog.add(msg);
		}
		synchronized (_smeltingSlotLog) {
			_smeltingSlotLog.add(msg);
		}
		synchronized (_einhasadFaithLog) {
			_einhasadFaithLog.add(msg);
		}
		synchronized (_pcmasterGoldenBuffEnforceLog) {
			_pcmasterGoldenBuffEnforceLog.add(msg);
		}
	}
	
	public void flush() throws IOException {
		synchronized (_chatlog) {
			if (!_chatlog.isEmpty()) {
				//writeLog(_chatlog, "채팅.txt");
				writeLog(_chatlog, "chat.txt");
				_chatlog.clear();
			}
		}
		synchronized (_commandlog) {
			if (!_commandlog.isEmpty()) {
				//writeLog(_commandlog, "명령어.txt");
				writeLog(_commandlog, "command.txt");
				_commandlog.clear();
			}
		}
		synchronized (_connectionlog) {
			if (!_connectionlog.isEmpty()) {
				//writeLog(_connectionlog, "로그인.txt");
				writeLog(_connectionlog, "login.txt");
				_connectionlog.clear();
			}
		}
		synchronized (_enchantlog) {
			if (!_enchantlog.isEmpty()) {
				//writeLog(_enchantlog, "인챈트.txt");
				writeLog(_enchantlog, "enchant.txt");
				_enchantlog.clear();
			}
		}
		synchronized (_craftlog) {
			if (!_craftlog.isEmpty()) {
				//writeLog(_craftlog, "아이템제작.txt");
				writeLog(_craftlog, "item_crafting.txt");
				_craftlog.clear();
			}
		}
		synchronized (_alchemylog) {
			if (!_alchemylog.isEmpty()) {
				//writeLog(_alchemylog, "인형합성.txt");
				writeLog(_alchemylog, "doll_synthesis.txt");
				_alchemylog.clear();
			}
		}
		synchronized (_potentiallog) {
			if (!_potentiallog.isEmpty()) {
				//writeLog(_potentiallog, "인형잠재력.txt");
				writeLog(_potentiallog, "doll_potential.txt");
				_potentiallog.clear();
			}
		}
		synchronized (_einStatelog) {
			if (!_einStatelog.isEmpty()) {
				//writeLog(_einStatelog, "아인하사드스탯.txt");
				writeLog(_einStatelog, "einhasad_stat.txt");
				_einStatelog.clear();
			}
		}
		synchronized (_tradelog) {
			if (!_tradelog.isEmpty()) {
				//writeLog(_tradelog, "교환.txt");
				writeLog(_tradelog, "trade.txt");
				_tradelog.clear();
			}
		}
		synchronized (_warehouselog) {
			if (!_warehouselog.isEmpty()) {
				//writeLog(_warehouselog, "창고.txt");
				writeLog(_warehouselog, "warehouse.txt");
				_warehouselog.clear();
			}
		}
		synchronized (_itemactionlog) {
			if (!_itemactionlog.isEmpty()) {
				//writeLog(_itemactionlog, "아이템로그.txt");
				writeLog(_itemactionlog, "item.txt");
				_itemactionlog.clear();
			}
		}
		synchronized (_levellog) {
			if (!_levellog.isEmpty()) {
				//writeLog(_levellog, "레벨업.txt");
				writeLog(_levellog, "level_up.txt");
				_levellog.clear();
			}
		}
		synchronized (_shoplog) {
			if (!_shoplog.isEmpty()) {
				//writeLog(_shoplog, "상점.txt");
				writeLog(_shoplog, "shop.txt");
				_shoplog.clear();
			}
		}
		synchronized (_ranklog) {
			if (!_ranklog.isEmpty()) {
				//writeLog(_ranklog, "랭킹.txt");
				writeLog(_ranklog, "ranking.txt");
				_ranklog.clear();
			}
		}
		synchronized (_tjlog) {
			if (!_tjlog.isEmpty()) {
				writeLog(_tjlog, "TJCOUPON.txt");
				_tjlog.clear();
			}
		}
		synchronized (_deathpenaltylog) {
			if (!_deathpenaltylog.isEmpty()) {
				//writeLog(_deathpenaltylog, "사망패널티.txt");
				writeLog(_deathpenaltylog, "death_penalty.txt");
				_deathpenaltylog.clear();
			}
		}
		synchronized (_webserver) {
			if (!_webserver.isEmpty()) {
				//writeLog(_webserver, "웹서버.txt");
				writeLog(_webserver, "web_server.txt");
				_webserver.clear();
			}
		}
		synchronized (_webaction) {
			if (!_webaction.isEmpty()) {
				//writeLog(_webaction, "웹액션.txt");
				writeLog(_webaction, "web_action.txt");
				_webaction.clear();
			}
		}
		synchronized (_connector) {
			if (!_connector.isEmpty()) {
				//writeLog(_connector, "접속기.txt");
				writeLog(_connector, "connector.txt");
				_connector.clear();
			}
		}
		synchronized (_favorBookLog) {
			if (!_favorBookLog.isEmpty()) {
				//writeLog(_favorBookLog, "성물.txt");
				writeLog(_favorBookLog, "relic.txt");
				_favorBookLog.clear();
			}
		}
		synchronized (_timeCollectionLog) {
			if (!_timeCollectionLog.isEmpty()) {
				//writeLog(_timeCollectionLog, "실렉티스.txt");
				writeLog(_timeCollectionLog, "time_collection.txt");
				_timeCollectionLog.clear();
			}
		}
		synchronized (_smeltingMakeLog) {
			if (!_smeltingMakeLog.isEmpty()) {
				//writeLog(_smeltingMakeLog, "제련석합성.txt");
				writeLog(_smeltingMakeLog, "smelting_stone_synthesis.txt");
				_smeltingMakeLog.clear();
			}
		}
		synchronized (_smeltingSlotLog) {
			if (!_smeltingSlotLog.isEmpty()) {
				//writeLog(_smeltingSlotLog, "제련석슬롯.txt");
				writeLog(_smeltingSlotLog, "smelting_stone_slot.txt");
				_smeltingSlotLog.clear();
			}
		}
		synchronized (_einhasadFaithLog) {
			if (!_einhasadFaithLog.isEmpty()) {
				//writeLog(_einhasadFaithLog, "아인하사드의신의.txt");
				writeLog(_einhasadFaithLog, "einhasad_faith.txt");
				_einhasadFaithLog.clear();
			}
		}
		synchronized (_pcmasterGoldenBuffEnforceLog) {
			if (!_pcmasterGoldenBuffEnforceLog.isEmpty()) {
				//writeLog(_pcmasterGoldenBuffEnforceLog, "금빛버프강화.txt");
				writeLog(_pcmasterGoldenBuffEnforceLog, "golden_buff_enforcement.txt");
				_pcmasterGoldenBuffEnforceLog.clear();
			}
		}
		synchronized (_enchantFailLog) {
			flushEnchantFail();
		}
	}
	
	private void flushEnchantFail(){
		if (!Config.TJ.TJ_COUPON_ENABLE || _enchantFailLog.isEmpty()) {
			return;
		}
		for (TJLogBean log : _enchantFailLog) {
			TJCoupon.createCoupon(log.getLogOwnerId(), log.getLogItem(), log.getLogTime());
		}
		_enchantFailLog.clear();
	}
	
	//** 날짜별로 폴더생성해서 로그저장하기 **//	
	private static String getDate(){
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh-ss", Locale.KOREA);
		return s.format(Calendar.getInstance().getTime());
	}
	
	public void writeLog(ArrayList<String> log, String filename) throws IOException {
		//** 날짜별로 폴더생성해서 로그저장하기 **//
		File f = null;
		String sTemp = getDate();
		StringTokenizer s = new StringTokenizer(sTemp, StringUtil.EmptyOneString);
		date = s.nextToken();
		f = new File(String.format("LogDB/%s", date));
		if (!f.exists()) {
			f.mkdir();
		}
		//** 날짜별로 폴더생성해서 로그저장하기  **//
		BufferedWriter w = new BufferedWriter(new FileWriter(String.format("LogDB/%s/%s", date, filename), true));
		PrintWriter pw = new PrintWriter(w, true);
		for (int i = 0, n = log.size(); i < n; i++) {
			pw.print(log.get(i));
		}
		pw.close();
		pw = null;
		w.close();
		w = null;
		sTemp = null;
		date = null;
	}

	private static final SimpleDateFormat formatter = new SimpleDateFormat(StringUtil.DateFormatStringSeconds);
	public String getLocalTime() {
		return formatter.format(new GregorianCalendar().getTime());
	}
	
	public String getFormatTime(Timestamp time) {
		return formatter.format(time);
	}
	
	public String getFormatItemName(L1ItemInstance item, int count) {
		if (item == null) {
			return StringUtil.EmptyString;
		}
		if (item.isMerge()) {
			//return String.format("[%d] %s (%d)", item.getId(), (item.getBless() == 0 ? "축복받은 " : item.getBless() == 2 ? "저주받은 " : StringUtil.EmptyString) + item.getDescKr(), count);
			return String.format("[%d] %s (%d)", item.getId(), (item.getBless() == 0 ? "Blessed " : item.getBless() == 2 ? "Cursed " : StringUtil.EmptyString) + item.getDescEn(), count);
		}
		if (item.getEnchantLevel() > 0) {
			//return String.format("[%d] +%d %s", item.getId(), item.getEnchantLevel(), (item.getBless() == 0 ? "축복받은 " : item.getBless() == 2 ? "저주받은 " : StringUtil.EmptyString) + item.getDescKr());
			return String.format("[%d] +%d %s", item.getId(), item.getEnchantLevel(), (item.getBless() == 0 ? "Blessed " : item.getBless() == 2 ? "Cursed " : StringUtil.EmptyString) + item.getDescEn());
		}
		if (item.getEnchantLevel() < 0) {
			//return String.format("[%d] -%d %s", item.getId(), item.getEnchantLevel(), (item.getBless() == 0 ? "축복받은 " : item.getBless() == 2 ? "저주받은 " : StringUtil.EmptyString) + item.getDescKr());
			return String.format("[%d] -%d %s", item.getId(), item.getEnchantLevel(), (item.getBless() == 0 ? "Blessed " : item.getBless() == 2 ? "Cursed " : StringUtil.EmptyString) + item.getDescEn());
		}
		//return String.format("[%d] %s", item.getId(), (item.getBless() == 0 ? "축복받은 " : item.getBless() == 2 ? "저주받은 " : StringUtil.EmptyString) + item.getDescKr());
		return String.format("[%d] %s", item.getId(), (item.getBless() == 0 ? "Blessed " : item.getBless() == 2 ? "Cursed " : StringUtil .EmptyString) + item.getDescEn());
	}
}

