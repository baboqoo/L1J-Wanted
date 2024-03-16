package l1j.server.server;

import static l1j.server.server.Opcodes.*;
import l1j.server.Config;
import l1j.server.server.clientpackets.*;
import l1j.server.server.serverpackets.S_CharPass;

public class PacketHandler {
	private final GameClient _client;
	
	public void handlePacket(byte[] abyte0, int opcode) throws Exception {
		ClientBasePacket cbp = null;
		switch(opcode){
		case C_LOGIN:							cbp = new C_AuthLogin(abyte0, _client);break;
		case C_EXTENDED_PROTOBUF:				cbp = _client.getProto().getPacket(abyte0);break;
		case C_VOICE_CHAT:						cbp = new C_ReturnStatus(abyte0, _client);break;
		case C_PLEDGE_WATCH:					cbp = new C_ClanAttention(abyte0, _client);break;
		case C_SHUTDOWN:						cbp = new C_PledgeContent(abyte0, _client);break;
		case C_EXTENDED:						cbp = new C_Extended(abyte0, _client);break;
		case C_ATTACK:case C_FAR_ATTACK:		cbp = new C_Attack(abyte0, _client);break;
		case C_ATTACK_CONTINUE:					cbp = new C_AttackContinue(abyte0, _client);break;
		case C_SAVEIO:							cbp = new C_CharacterConfig(abyte0, _client);break;
		case C_OPEN:							cbp = new C_Door(abyte0, _client);break;
		case C_TITLE:							cbp = new C_Title(abyte0, _client);break;
		case C_EMBLEM:							cbp = new C_Emblem(abyte0, _client);break;
		case C_MATCH_MAKING:					cbp = new C_ClanMatching(abyte0, _client);break;
		case C_BOARD_DELETE:					cbp = new C_BoardDelete(abyte0, _client);break;
		case C_WHO_PLEDGE:						cbp = new C_WhoPledge(abyte0, _client);break;
		case C_CHANGE_DIRECTION:				cbp = new C_ChangeHeading(abyte0, _client);break;
		case C_HACTION:							cbp = haction(abyte0);break;
		case C_USE_SPELL:						cbp = new C_UseSkill(abyte0, _client);break;
		case C_UPLOAD_EMBLEM:					cbp = new C_EmblemUpload(abyte0, _client);break;
		case C_CANCEL_XCHG:						cbp = new C_TradeCancel(abyte0, _client);break;
		//case C_WARTIMELIST:						cbp = new C_ChangeWarTime(abyte0, _client);break;
		case C_BOOKMARK:						cbp = new C_AddBookmark(abyte0, _client);break;
		case C_MARRIAGE:						cbp = new C_Propose(abyte0, _client);break;
		case C_BOARD_LIST:						cbp = new C_BoardBack(abyte0, _client);break;
		case C_BOARD_READ:						cbp = new C_BoardRead(abyte0, _client);break;
		case C_ASK_XCHG:						cbp = new C_Trade(abyte0, _client);break;
		case C_DELETE_CHARACTER:				cbp = deleteCharacter(abyte0);break; 
		case C_ALIVE:							cbp = new C_KeepALIVE(abyte0, _client);break;
		case C_ANSWER:							cbp = new C_Attr(abyte0, _client);break;
		case C_SHOP_WAREHOUSE_RESULT:			cbp = new C_ShopAndWarehouse(abyte0, _client);break;
		case C_BUYABLE_SPELL:					cbp = new C_SkillBuy(abyte0, _client);break;
		case C_BUY_SPELL:						cbp = new C_SkillBuyOK(abyte0, _client);break;
		case C_DEPOSIT:							cbp = new C_Deposit(abyte0, _client);break;
		case C_WITHDRAW:						cbp = new C_Drawal(abyte0, _client);break;
		case C_ONOFF:							cbp = new C_OnOffSetting(abyte0, _client);break;
		case C_ADD_XCHG:						cbp = new C_TradeAddItem(abyte0, _client);break;
		case C_ADD_BUDDY:						cbp = new C_AddBuddy(abyte0, _client);break;
		case C_ACCEPT_XCHG:						cbp = new C_TradeOK(abyte0, _client);break;
		case C_CHECK_PK:						cbp = new C_CheckPK(abyte0, _client);break;
		case C_LOGOUT:							cbp = new C_ReturnToLogin(abyte0, _client);break;
		case C_TAX:								cbp = new C_TaxRate(abyte0, _client);break;
		case C_RESTART:							cbp = new C_Restart(abyte0, _client);break;
		case C_RESTART_COMPLETE:				cbp = new C_RestartComplete(abyte0, _client);break;
		case C_DEAD_RESTART:					cbp = new C_RestartAfterDie(abyte0, _client);break;
		case C_QUERY_BUDDY:						cbp = new C_Buddy(abyte0, _client);break;
		case C_DROP:							cbp = new C_DropItem(abyte0, _client);break;
		case C_LEAVE_PARTY:						cbp = new C_LeaveParty(abyte0, _client);break;
		case C_BAN_MEMBER:						cbp = new C_BanClan(abyte0, _client);break;
		case C_PLATE:							cbp = new C_Board(abyte0, _client);break;
		case C_DESTROY_ITEM:					cbp = new C_DeleteInventoryItem(abyte0, _client);break;
		case C_WHO_PARTY:						cbp = new C_Party(abyte0, _client);break;
		case C_GET:								cbp = new C_PickUpItem(abyte0, _client);break;
		case C_WHO:								cbp = new C_Who(abyte0, _client);break;
		case C_GIVE:							cbp = new C_GiveItem(abyte0, _client);break;
		case C_MOVE:							cbp = new C_MoveChar(abyte0, _client);break;
		case C_DELETE_BOOKMARK:					cbp = new C_DeleteBookmark(abyte0, _client);break;	
		case C_LEAVE_PLEDGE:					cbp = new C_LeaveClan(abyte0, _client);break;
		case C_DIALOG:							cbp = new C_NPCTalk(abyte0, _client);break;
		case C_BANISH_PARTY:					cbp = new C_BanParty(abyte0, _client);break;
		case C_REMOVE_BUDDY:					cbp = new C_DelBuddy(abyte0, _client);break;
		case C_WAR:								cbp = new C_War(abyte0, _client);break;
		case C_ENTER_WORLD:						cbp = enterWorld(abyte0, opcode);break;
		case C_QUERY_PERSONAL_SHOP:				cbp = new C_PrivateShopList(abyte0, _client);break;
		case C_READ_NEWS:						cbp = new C_ReadNews(abyte0, _client);break;
		case C_CREATE_CUSTOM_CHARACTER:			cbp = createCustomCharacter(abyte0);break;
		case C_ACTION:							cbp = new C_ExtraCommand(abyte0, _client);break;
		case C_BOARD_WRITE:						cbp = new C_BoardWrite(abyte0, _client);break;
		case C_USE_ITEM:						cbp = new C_ItemUSe(abyte0, _client);break;
		case C_INVITE_PARTY_TARGET:				cbp = new C_CreateParty(abyte0, _client);break;
		case C_ENTER_PORTAL:					cbp = new C_EnterPortal(abyte0, _client);break;
		case C_HYPERTEXT_INPUT_RESULT:			cbp = new C_Amount(abyte0, _client);break;
		case C_FIXABLE_ITEM:					cbp = new C_FixWeaponList(abyte0, _client);break;
		case C_FIX:								cbp = new C_SelectList(abyte0, _client);break;
		case C_EXIT_GHOST:						cbp = new C_ExitGhost(abyte0, _client);break;
		case C_SUMMON:							cbp = new C_CallPlayer(abyte0, _client);break;
		case C_THROW:							cbp = new C_FishClick(abyte0, _client);break;
		case C_SLAVE_CONTROL:					cbp = new C_SelectTarget(abyte0, _client);break;
		case C_RETURN_SUMMON:					cbp = new C_Teleport(abyte0, _client);break;
		case C_RANK_CONTROL:					cbp = new C_Rank(abyte0, _client);break;
		case C_CHAT_PARTY_CONTROL:				cbp = new C_ChatParty(abyte0, _client);break;
		case C_DUEL:							cbp = new C_Fight(abyte0, _client);break;
		case C_LEAVE_SHIP:						cbp = new C_LeaveShip(abyte0, _client);break;
		case C_ENTER_SHIP:						cbp = new C_EnterShip(abyte0, _client);break;
		case C_MAIL:							cbp = new C_MailBox(abyte0, _client);break;
		case C_WAREHOUSE_CONTROL:				cbp = new C_WarehousePassword(abyte0, _client);break;// 창고 비번
		case C_MERCENARYEMPLOY:					cbp = new C_SoldierBuy(abyte0, _client);break;
		case C_CHANNEL:							cbp = new C_Report(abyte0, _client);break;
		case C_QUERY_CASTLE_SECURITY:			cbp = new C_SecurityStatus(abyte0, _client);break;
		case C_CHANGE_CASTLE_SECURITY:			cbp = new C_SecurityStatusSet(abyte0, _client);break;
		case C_QUIT:							cbp = new C_Quit(abyte0, _client);break;
		case C_EXCHANGEABLE_SPELL:				cbp = new C_Horun(abyte0, _client);break;
		case C_EXCHANGE_SPELL:					cbp = new C_HorunOK(abyte0, _client);break;
		case C_JOIN_PLEDGE:						cbp = new C_JoinPledge(abyte0, _client);break;
		case C_READ_NOTICE:break;
		case C_CLIENT_READY:break;
		default:
			System.out.println(String.format("PacketHandler other opcode find [%d]", opcode));
			break;
		}
		if (cbp != null) {
			cbp.clear();
			cbp = null;
		}
	}
	
	private long createCharTime = 0;
	private long hactionTime = -1;
	
	public PacketHandler(GameClient client) {
		_client = client;
	}
	
	private boolean second_password_check(byte[] bytes){
		if (Config.SERVER.SECOND_PASSWORD_USE && !_client.getAccount().is_auth_second_password()) {
			if (_client.getAccount().get_second_password() == null) {
				_client.sendPacket(S_CharPass.CHAR_PWD_UI_CREATE);
				return false;
			}
			_client.getAccount().setWaitPacket(bytes);
			_client.sendPacket(S_CharPass.CHAR_PWD_UI_READY);
			return false;
		}
		return true;
	}
	
	private ClientBasePacket haction(byte[] abyte0) throws Exception {
		long now = System.currentTimeMillis();
		if (now < hactionTime) {
			return null;
		}
		hactionTime = now + 400;
		return new C_NPCAction(abyte0, _client); 
	}
	
	private ClientBasePacket enterWorld(byte[] abyte0, int opcode) throws Exception {
		if (!second_password_check(abyte0)) {
			return null;
		}
		if (_client.getRestartMillis() > 0) {
			long diff = System.currentTimeMillis() - _client.getRestartMillis();
			_client.setRestartMillis(0L);
			if (diff < Config.SERVER.RESTART_ENTER_WORLD_INTERVAL) {
				Runnable r = () -> {
					try {
						handlePacket(abyte0, opcode);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
				GeneralThreadPool.getInstance().schedule(r, Config.SERVER.RESTART_ENTER_WORLD_INTERVAL - diff);
				return null;
			}
		}
		return new C_LoginToServer(abyte0, _client);
	}
	
	private ClientBasePacket createCustomCharacter(byte[] abyte0) throws Exception {
		if (!second_password_check(abyte0)) {
			return null;
		}
		long systime = System.currentTimeMillis();
		if (createCharTime < systime) {
			createCharTime = systime + 2000;
			return new C_CreateChar(abyte0, _client);
		}
		return null;
	}
	
	private ClientBasePacket deleteCharacter(byte[] abyte0) throws Exception {
		if (!second_password_check(abyte0)) {
			return null;
		}
		if (_client.getActiveChar() == null) {
			return new C_DeleteChar(abyte0, _client);
		}
		return null;
	}
}
