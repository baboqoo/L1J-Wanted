package l1j.server.server.model.npc;

import javolution.util.FastMap;

public enum L1NpcHtmlFactory {
	BUY(								"buy",									l1j.server.server.model.npc.action.html.BuyAction.getInstance()),
	SELL(								"sell",									l1j.server.server.model.npc.action.html.SellAction.getInstance()),
	NON_NPC_BUY(						"non_npc buy",							l1j.server.server.model.npc.action.html.NonNpcBuyAction.getInstance()),
	EXIT_GHOST(							"exitghost",							l1j.server.server.model.npc.action.html.ExitGhostAction.getInstance()),
	HISTORY(							"history",								l1j.server.server.model.npc.action.html.HistoryAction.getInstance()),
	DEPOSIT(							"deposit",								l1j.server.server.model.npc.action.html.DepositAction.getInstance()),
	DEPOSIT_ELVEN(						"deposit-elven",						l1j.server.server.model.npc.action.html.DepositElvenAction.getInstance()),
	DEPOSIT_PLEDGE(						"deposit-pledge",						l1j.server.server.model.npc.action.html.DepositPledgeAction.getInstance()),
	RETRIEVE(							"retrieve",								l1j.server.server.model.npc.action.html.RetrieveAction.getInstance()),
	RETRIEVE_ELVEN(						"retrieve-elven",						l1j.server.server.model.npc.action.html.RetrieveElvenAction.getInstance()),
	RETRIEVE_AIB(						"retrieve-aib",							l1j.server.server.model.npc.action.html.RetrieveAibAction.getInstance()),
	RETRIEVE_CHAR(						"retrieve-char",						l1j.server.server.model.npc.action.html.RetrieveCharAction.getInstance()),
	RETRIEVE_PLEDGE(					"retrieve-pledge",						l1j.server.server.model.npc.action.html.RetrievePledgeAction.getInstance()),
	GET(								"get",									l1j.server.server.model.npc.action.html.GetAction.getInstance()),
	FIX(								"fix",									l1j.server.server.model.npc.action.html.FixAction.getInstance()),
	OPENIGATE(							"openigate",							l1j.server.server.model.npc.action.html.OpenigateAction.getInstance()),
	CLOSEIGATE(							"closeigate",							l1j.server.server.model.npc.action.html.CloseigateAction.getInstance()),
	INEX(								"inex",									l1j.server.server.model.npc.action.html.InexAction.getInstance()),
	TAX(								"tax",									l1j.server.server.model.npc.action.html.TaxAction.getInstance()),
	WITHDRAWAL(							"withdrawal",							l1j.server.server.model.npc.action.html.WithdrawalAction.getInstance()),
	CDEPOSIT(							"cdeposit",								l1j.server.server.model.npc.action.html.CdepositAction.getInstance()),
	EMPLOY(								"employ",								l1j.server.server.model.npc.action.html.EmployAction.getInstance()),
	ARRANGE(							"arrange",								l1j.server.server.model.npc.action.html.ArrangeAction.getInstance()),
	CASTLEGATE(							"castlegate",							l1j.server.server.model.npc.action.html.CastlegateAction.getInstance()),
	DEMAND(								"demand",								l1j.server.server.model.npc.action.html.DemandAction.getInstance()),
	ALL_HEALE_GATE(						"allhealegate",							l1j.server.server.model.npc.action.html.AllhealegateAction.getInstance()),
	HEALE_GATE_GIRAN_OUTER_GATEF(		"healegate_giran outer gatef",			l1j.server.server.model.npc.action.html.HealegateGiranOuterGatefAction.getInstance()),
	HEALE_GATE_GIRAN_OUTER_GATE1(		"healegate_giran outer gatel",			l1j.server.server.model.npc.action.html.HealegateGiranOuterGatelAction.getInstance()),
	HEALE_GATE_GIRAN_INNER_GATEF(		"healegate_giran inner gatef",			l1j.server.server.model.npc.action.html.HealegateGiranInnerGatefAction.getInstance()),
	HEALE_GATE_GIRAN_INNER_GATE1(		"healegate_giran inner gatel",			l1j.server.server.model.npc.action.html.HealegateGiranInnerGate1Action.getInstance()),
	HEALE_GATE_GIRAN_INNER_GATER(		"healegate_giran inner gater",			l1j.server.server.model.npc.action.html.HealegateGiranInnerGaterAction.getInstance()),
	HEALE_GATE_GIRAN_CASTLE_HOUSE_DOOR(	"healigate_giran castle house door",	l1j.server.server.model.npc.action.html.HealigateGiranCastleHouseDoorAction.getInstance()),
	H_HEALE_GATE_IRON_DOOR_A(			"hhealegate_iron door a",				l1j.server.server.model.npc.action.html.HHealegateIronDoorAAction.getInstance()),
	H_HEALE_GATE_IRON_DOOR_B(			"hhealegate_iron door b",				l1j.server.server.model.npc.action.html.HHealegateIronDoorBAction.getInstance()),
	AUTO_REPAIR_ON(						"autorepairon",							l1j.server.server.model.npc.action.html.AutorepaironAction.getInstance()),
	AUTO_REPAIR_OFF(					"autorepairoff",						l1j.server.server.model.npc.action.html.AutorepairoffAction.getInstance()),
	ENCW(								"encw",									l1j.server.server.model.npc.action.html.EncwAction.getInstance()),
	ENCA(								"enca",									l1j.server.server.model.npc.action.html.EncaAction.getInstance()),
	WITHDRAW_NPC(						"withdrawnpc",							l1j.server.server.model.npc.action.html.WithdrawNpcAction.getInstance()),
	CHANGE_NAME(						"changename",							l1j.server.server.model.npc.action.html.ChangeNameAction.getInstance()),
	ATTACK_CHR(							"attackchr",							l1j.server.server.model.npc.action.html.AttackchrAction.getInstance()),
	MAP(								"map",									l1j.server.server.model.npc.action.html.MapAction.getInstance()),
	OPEN(								"open",									l1j.server.server.model.npc.action.html.OpenAction.getInstance()),
	CLOSE(								"close",								l1j.server.server.model.npc.action.html.OpenAction.getInstance()),
	EXPEL(								"expel",								l1j.server.server.model.npc.action.html.ExpelAction.getInstance()),
	PAYFEE(								"payfee",								l1j.server.server.model.npc.action.html.PayfeeAction.getInstance()),
	NAME(								"name",									l1j.server.server.model.npc.action.html.NameAction.getInstance()),
	REM(								"rem",									l1j.server.server.model.npc.action.html.RemAction.getInstance()),
	TEL0(								"tel0",									l1j.server.server.model.npc.action.html.TelAction.getInstance()),
	TEL1(								"tel1",									l1j.server.server.model.npc.action.html.TelAction.getInstance()),
	TEL2(								"tel2",									l1j.server.server.model.npc.action.html.TelAction.getInstance()),
	TEL3(								"tel3",									l1j.server.server.model.npc.action.html.TelAction.getInstance()),
	UPGRADE(							"upgrade",								l1j.server.server.model.npc.action.html.UpgradeAction.getInstance()),
	PK(									"pk",									l1j.server.server.model.npc.action.html.PkAction.getInstance()),
	ENT(								"ent",									l1j.server.server.model.npc.action.html.EntAction.getInstance()),
	PAR(								"par",									l1j.server.server.model.npc.action.html.ParAction.getInstance()),
	INFO(								"info",									l1j.server.server.model.npc.action.html.InfoAction.getInstance()),
	HASTE(								"haste",								l1j.server.server.model.npc.action.html.HasteAction.getInstance()),
	TEL_UNICORN_TEMPLE(					"tel_unicorn_temple",					l1j.server.server.model.npc.action.html.TelUnicornTempleAction.getInstance()),
	TEL_UNICORN_TEMPLE_BOOST(			"tel_unicorn_temple_boost",				l1j.server.server.model.npc.action.html.TelUnicornTempleBoostAction.getInstance()),
	SELECT(								"select",								l1j.server.server.model.npc.action.html.SelectAction.getInstance()),
	APPLY(								"apply",								l1j.server.server.model.npc.action.html.ApplyAction.getInstance()),
	SKELETON_NBMORPH(					"skeleton nbmorph",						l1j.server.server.model.npc.action.html.SkeletonNbmorphAction.getInstance()),
	LYCANTHROPE_NBMORPH(				"lycanthrope nbmorph",					l1j.server.server.model.npc.action.html.LycanthropeNbmorphAction.getInstance()),
	SHELOB_NBMORPH(						"shelob nbmorph",						l1j.server.server.model.npc.action.html.ShelobNbmorphAction.getInstance()),
	GHOUL_NBMORPH(						"ghoul nbmorph",						l1j.server.server.model.npc.action.html.GhoulNbmorphAction.getInstance()),
	GHAST_NBMORPH(						"ghast nbmorph",						l1j.server.server.model.npc.action.html.GhastNbmorphAction.getInstance()),
	ATUBA_ORC_NBMORPH(					"atuba orc nbmorph",					l1j.server.server.model.npc.action.html.AtubaOrcNbmorphAction.getInstance()),
	SKELETON_AXEMAM_NBMORPH(			"skeleton axeman nbmorph",				l1j.server.server.model.npc.action.html.SkeletonAxemanNbmorphAction.getInstance()),
	TROLL_NBMORPH(						"troll nbmorph",						l1j.server.server.model.npc.action.html.TrollNbmorphAction.getInstance()),
	CONTRACT1(							"contract1",							l1j.server.server.model.npc.action.html.Contract1Action.getInstance()),
	SET(								"set",									l1j.server.server.model.npc.action.html.SetAction.getInstance()),
	CLEAR(								"clear",								l1j.server.server.model.npc.action.html.ClearAction.getInstance()),
	EXP(								"exp",									l1j.server.server.model.npc.action.html.ExpAction.getInstance()),
	MATERIAL(							"material",								l1j.server.server.model.npc.action.html.MaterialAction.getInstance()),
	F_ANTHARAS(							"f_antharas",							l1j.server.server.model.npc.action.html.F_dragonRaidAction.getInstance()),
	F_FAFURION(							"f_fafurion",							l1j.server.server.model.npc.action.html.F_dragonRaidAction.getInstance()),
	F_LINDVIOR(							"f_lindvior",							l1j.server.server.model.npc.action.html.F_dragonRaidAction.getInstance()),
	F_VALAKAS(							"f_valakas",							l1j.server.server.model.npc.action.html.F_dragonRaidAction.getInstance()),
	MARKET_GIRAN(						"market-giran",							l1j.server.server.model.npc.action.html.MarketGiranAction.getInstance()),
	TELEPORT_MOBJTELE(					"teleport Mobjtele2",					l1j.server.server.model.npc.action.html.TeleportMobjteleAction.getInstance()),
	TELEPORT_MOBJTELE1(					"teleport Mobjtele1",					l1j.server.server.model.npc.action.html.TeleportMobjtele1Action.getInstance()),
	TELEPORT_GIRAN_TRADE_ZONE(			"teleport giran-trade-zone-giran",		l1j.server.server.model.npc.action.html.TeleportGiranTradeZoneGiranAction.getInstance()),
	ENTER_SELLER(						"EnterSeller",							l1j.server.server.model.npc.action.html.EnterSellerAction.getInstance());
	
	private String _html;
	private L1NpcHtmlAction _action;
	L1NpcHtmlFactory(String html, L1NpcHtmlAction action) {
		_html	= html;
		_action	= action;
	}
	
	private static final FastMap<String, L1NpcHtmlAction> _actions;
	static {
		L1NpcHtmlFactory[] actions	= L1NpcHtmlFactory.values();
		_actions					= new FastMap<String, L1NpcHtmlAction>(actions.length);
		for (L1NpcHtmlFactory action : actions) {
			_actions.put(action._html, action._action);
		}
	}
	
	public static L1NpcHtmlAction getAction(String html){
		return _actions.get(html);
	}
	
	public static void init(){}
}

