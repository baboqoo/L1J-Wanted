package l1j.server;

import l1j.server.configure.*;
import l1j.server.server.utils.IntRange;

public final class Config {
	public static ServerConfigure SERVER			= new ServerConfigure();
	public static RateConfigure RATE				= new RateConfigure();
	public static MessageConfigure MESSAGE			= new MessageConfigure();
	public static AltSettingConfigure ALT			= new AltSettingConfigure();
    public static SpeedConfigure SPEED				= new SpeedConfigure();
    public static CharacterConfigure CHA			= new CharacterConfigure();
    public static PledgeConfigure PLEDGE			= new PledgeConfigure();
	public static CraftConfigure CRAFT				= new CraftConfigure();
	public static CommonConfigure COMMON			= new CommonConfigure();
	public static RankingConfigure RANKING			= new RankingConfigure();
	public static FatigueConfigure FATIGUE			= new FatigueConfigure();
	public static DeathPenaltyConfigure PENALTY		= new DeathPenaltyConfigure();
	public static CompanionConfigure COMPANION		= new CompanionConfigure();
	public static DungeonConfigure DUNGEON			= new DungeonConfigure();
	public static EtcConfigure ETC					= new EtcConfigure();
	public static AttendanceConfigure ATTEND		= new AttendanceConfigure();
	public static QuestConfigure QUEST				= new QuestConfigure();
	public static EnchantConfigure ENCHANT			= new EnchantConfigure();
	public static SpellConfigure SPELL				= new SpellConfigure();
	public static AlchemyConfigure ALCHEMY			= new AlchemyConfigure();
	public static SmeltingConfigure SMELTING		= new SmeltingConfigure();
	public static EinhasadConfigure EIN				= new EinhasadConfigure();
	public static RevengeConfigure REVENGE			= new RevengeConfigure();
	public static VersionConfigure VERSION			= new VersionConfigure();
	public static InterServerConfigure INTER		= new InterServerConfigure();
	public static EventpushConfigure PUSH			= new EventpushConfigure();
	public static PlaySupportConfigure PSS			= new PlaySupportConfigure();
	public static OccupyConfigure OCCUPY			= new OccupyConfigure();
	public static TJCouponConfigure TJ				= new TJCouponConfigure();
	public static CollectionConfigure COLLECTION	= new CollectionConfigure();
	public static WebserverConfigure WEB			= new WebserverConfigure();
	public static LauncherConfigure LAUNCHER		= new LauncherConfigure();
	public static FreeBuffShieldConfigure FREEBUFF	= new FreeBuffShieldConfigure();
	
	public static void load() {
		SERVER.load();
		RATE.load();
		SPEED.load();
		ALT.load();
		CHA.load();
		DUNGEON.load();
		ETC.load();
		ATTEND.load();
		PLEDGE.load();
		CRAFT.load();
		COMMON.load();
		RANKING.load();
		FATIGUE.load();
		QUEST.load();
		ENCHANT.load();
		SPELL.load();
		ALCHEMY.load();
		SMELTING.load();
		PENALTY.load();
		COMPANION.load();
		EIN.load();
		REVENGE.load();
		VERSION.load();
		INTER.load();
		PUSH.load();
		MESSAGE.load();
		PSS.load();
		OCCUPY.load();
		TJ.load();
		COLLECTION.load();
		WEB.load();
		LAUNCHER.load();
		FREEBUFF.load();
		validate();
	}
	
	private static void validate() {
		if (!IntRange.includes(Config.ALT.ALT_ITEM_DELETION_RANGE, 0, 5))
			throw new IllegalStateException("ItemDeletionRange: The value is outside the settable range. ");
		if (!IntRange.includes(Config.ALT.ALT_ITEM_DELETION_TIME, 1, 35791))
			throw new IllegalStateException("ItemDeletionTime: The value is outside the settable range. ");
	}

	private Config() {}
}
