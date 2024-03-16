package l1j.server.server.model.skill;

public class L1SkillId {
	// TODO SKILL ID START
	public static final int SKILLS_BEGIN					= 0;
	
	/** 1단계 일반마법 **/
	public static final int HEAL							= 0;
	public static final int LIGHT							= 1;
	public static final int SHIELD							= 2;
	public static final int ENERGY_BOLT						= 3;
	public static final int TELEPORT						= 4;
	public static final int ICE_DAGGER						= 5;
	public static final int WIND_CUTTER						= 6;
	public static final int HOLY_WEAPON						= 7;

	/** 2단계 일반마법 **/
	public static final int CURE_POISON						= 8;
	public static final int CHILL_TOUCH						= 9;
	public static final int CURSE_POISON					= 10;
	public static final int ENCHANT_WEAPON					= 11;
	public static final int DETECTION						= 12;
	public static final int DECREASE_WEIGHT					= 13;
	public static final int FIRE_ARROW						= 14;
	public static final int STALAC							= 15;

	/** 3단계 일반마법 **/
	public static final int LIGHTNING						= 16;
	public static final int TURN_UNDEAD						= 17;
	public static final int EXTRA_HEAL						= 18;
	public static final int CURSE_BLIND						= 19;
	public static final int BLESSED_ARMOR					= 20;
	public static final int FROZEN_CLOUD					= 21;
	public static final int WEAK_ELEMENTAL					= 22;

	/** 4단계 일반마법  **/
	public static final int FIREBALL						= 24;
	public static final int PHYSICAL_ENCHANT_DEX			= 25;
	public static final int WEAPON_BREAK					= 26;
	public static final int VAMPIRIC_TOUCH					= 27;
	public static final int SLOW							= 28;
	public static final int EARTH_JAIL						= 29;
	public static final int COUNTER_MAGIC					= 30;
	public static final int MEDITATION						= 31;

	/** 5단계 일반마법 **/
	public static final int CURSE_PARALYZE					= 32;
	public static final int CALL_LIGHTNING					= 33;
	public static final int GREATER_HEAL					= 34;
	public static final int TAMING_MONSTER					= 35;
	public static final int REMOVE_CURSE					= 36;
	public static final int CONE_OF_COLD					= 37;
	public static final int MANA_DRAIN						= 38;
	public static final int DARKNESS						= 39;

	/** 6단계 일반마법 **/
	public static final int CREATE_ZOMBIE					= 40;
	public static final int PHYSICAL_ENCHANT_STR			= 41;
	public static final int HASTE							= 42;
	public static final int CANCELLATION					= 43;
	public static final int ERUPTION						= 44;
	public static final int SUNBURST						= 45;
	public static final int WEAKNESS						= 46;
	public static final int BLESS_WEAPON					= 47;

	/** 7단계 일반마법 **/
	public static final int HEAL_ALL						= 48;
	public static final int FREEZING_ARMOR					= 49;
	public static final int SUMMON_MONSTER					= 50;
	public static final int HOLY_WALK						= 51;
	public static final int TORNADO							= 52;
	public static final int GREATER_HASTE					= 53;
	public static final int BERSERKERS						= 54;
	public static final int ENCHANT_ACCURACY				= 55;

	/** 8단계 일반마법 **/
	public static final int FULL_HEAL						= 56;
	public static final int FIRE_WALL						= 57;
	public static final int BLIZZARD						= 58;
	public static final int INVISIBILITY					= 59;
	public static final int RESURRECTION					= 60;
	public static final int EARTHQUAKE						= 61;
	public static final int LIFE_STREAM						= 62;
	public static final int SILENCE							= 63;

	/** 9단계 일반마법 **/
	public static final int LIGHTNING_STORM					= 64;
	public static final int FOG_OF_SLEEPING					= 65;
	public static final int SHAPE_CHANGE					= 66;
	public static final int IMMUNE_TO_HARM					= 67;
	public static final int MASS_TELEPORT					= 68;
	public static final int FIRE_STORM						= 69;
	public static final int FATAL_POTION					= 70;
	public static final int COUNTER_DETECTION				= 71;

	/** 10단계 일반마법 **/
	public static final int DEATH_HEAL						= 72;
	public static final int METEOR_STRIKE					= 73;
	public static final int GREATER_RESURRECTION			= 74;
	public static final int ICE_METEOR						= 75;
	public static final int DISINTEGRATE					= 76;
	public static final int ABSOLUTE_BARRIER				= 77;
	public static final int ICE_SPIKE						= 79;
	
	public static final int SHAPE_CHANGE_DOMINATION			= 84;
	public static final int SHAPE_CHANGE_100LEVEL			= 85;
	public static final int ICE_LANCE						= 790030;
	public static final int DISEASE							= 790031;
	public static final int GREATER_SLOW					= 100029;
	public static final int ADVANCE_SPIRIT_1				= 790079;
	public static final int DECAY_POTION					= 790080;

	/** 기사 마법 **/
	public static final int SHOCK_STUN						= 86;
	public static final int REDUCTION_ARMOR					= 87;
	public static final int BOUNCE_ATTACK					= 88;
	public static final int COUNTER_BARRIER					= 90;
	public static final int ABSOLUTE_BLADE					= 91;
	public static final int BLOW_ATTACK						= 93;

	/** 다엘 마법 **/
	public static final int BLIND_HIDING					= 96;
	public static final int ENCHANT_VENOM					= 97;
	public static final int SHADOW_ARMOR					= 98;
	public static final int BRING_STONE						= 99;
	public static final int MOVING_ACCELERATION				= 100;
	public static final int SHADOW_SLEEP					= 102;
	public static final int VENOM_RESIST					= 103;
	public static final int DOUBLE_BRAKE					= 104;
	public static final int UNCANNY_DODGE					= 105;
	public static final int SHADOW_FANG						= 106;
	public static final int DRESS_MIGHTY					= 108;
	public static final int DRESS_DEXTERITY					= 109;
	public static final int ARMOR_BREAK						= 111;
	
	/** 군주 마법 **/
	public static final int TRUE_TARGET						= 112;
	public static final int GLOWING_WEAPON					= 113;
	public static final int SHINING_SHIELD					= 114;
	public static final int BRAVE_MENTAL					= 116;
	public static final int GRACE							= 121;
	public static final int EMPIRE							= 122;

	/** 요정 마법 **/
	public static final int RESIST_MAGIC					= 128;
	public static final int BODY_TO_MIND					= 129;
	public static final int TELEPORT_TO_MATHER				= 130;
	public static final int TRIPLE_ARROW					= 131;
	public static final int ELEMENTAL_FALL_DOWN				= 132;
	public static final int ELVEN_GRAVITY					= 133;
	public static final int SOUL_BARRIER					= 134;	
	public static final int INFERNO							= 135;
	public static final int CLEAR_MIND						= 136;
	public static final int MAGIC_SHIELD					= 138;
	
	public static final int RETURN_TO_NATURE				= 144;
	public static final int PROTECTION_FROM_ELEMENTAL		= 146;
	public static final int EARTH_WEAPON					= 147;
	public static final int AQUA_SHOT						= 148;
	public static final int EGLE_EYE						= 149;
	public static final int FIRE_SHILD						= 150;
	public static final int QUAKE							= 151;
	public static final int ERASE_MAGIC						= 152;
	public static final int SUMMON_LESSER_ELEMENTAL			= 153;
	public static final int DANCING_BLADES					= 154;
	public static final int EYE_OF_STORM					= 155;
	public static final int EARTH_BIND						= 156;
	public static final int NATURES_TOUCH					= 157;
	public static final int EARTH_GUARDIAN					= 158;
	public static final int AQUA_PROTECTER					= 159;
	public static final int AREA_OF_SILENCE					= 160;
	public static final int SUMMON_GREATER_ELEMENTAL		= 161;
	public static final int BURNING_WEAPON					= 162;
	public static final int NATURES_BLESSING				= 163;
	public static final int CALL_OF_NATURE					= 164;
	public static final int STORM_SHOT						= 165;
	public static final int CYCLONE							= 166;
	public static final int IRON_SKIN						= 167;
	public static final int EXOTIC_VITALIZE					= 168;
	public static final int WATER_LIFE						= 169;
	public static final int ELEMENTAL_FIRE					= 170;
	public static final int STORM_WALK						= 171;
	public static final int POLLUTE_WATER					= 172;
	public static final int STRIKER_GALE					= 173;
	public static final int SOUL_OF_FLAME					= 174;
	public static final int FOCUS_WAVE						= 176;
	public static final int HURRICANE						= 177;
	public static final int SAND_STORM						= 178;
	
	public static final int ENTANGLE						= 10152;
	public static final int WIND_SHACKLE					= 167777;
	
	/** 용기사 마법 **/
	public static final int BURNING_SLASH					= 181;
	public static final int DESTROY							= 182;
	public static final int MAGMA_BREATH					= 183;
	public static final int SCALES_EARTH_DRAGON				= 184;
	public static final int BLOOD_LUST						= 185;
	public static final int FOU_SLAYER						= 186;
	public static final int MAGMA_ARROW						= 188;
	public static final int SCALES_WATER_DRAGON				= 189;
	public static final int THUNDER_GRAB					= 191;
	public static final int EYE_OF_DRAGON					= 193;
	public static final int SCALES_FIRE_DRAGON				= 194;	
	public static final int DESTROY_2						= 195;	
	public static final int SCALES_WIND_DRAGON				= 196;
	public static final int SHADOW_STEP						= 198;

	/** 환술사 마법 **/
	public static final int MIRROR_IMAGE					= 200;
	public static final int CONFUSION						= 201;
	public static final int SMASH							= 202;
	public static final int IllUSION_OGRE					= 203;
	public static final int CUBE_OGRE						= 204;
	public static final int CONCENTRATION					= 205;
	public static final int MIND_BREAK						= 206;
	public static final int BONE_BREAK						= 207;
	public static final int CUBE_GOLEM						= 209;
	public static final int PATIENCE						= 210;
	public static final int PHANTASM						= 211;
	public static final int IZE_BREAK						= 212;
	public static final int CUBE_LICH						= 214;
	public static final int INSIGHT							= 215;
	public static final int PANIC							= 216;
	public static final int REDUCE_WEIGHT					= 217;
	public static final int IllUSION_AVATAR					= 218;
	public static final int CUBE_AVATAR						= 219;
	public static final int IMPACT							= 221;	
	public static final int FOCUS_SPRITS					= 222;	

	/** 전사 마법 **/
	public static final int HOWL							= 224;
	public static final int POWER_GRIP						= 227;
	public static final int TOMAHAWK						= 228;
	public static final int DESPERADO						= 229;
	public static final int TITAN_RISING					= 230;	
	public static final int ASSASSIN						= 232;
	public static final int LUCIFER							= 233;
	public static final int PRIME							= 240;
	public static final int FORCE_STUN						= 241;
	public static final int ETERNITY						= 242;
	public static final int AVENGER							= 243;
	public static final int HALPHAS							= 244;
	public static final int POTENTIAL						= 245;
	
	// TODO SKILL ID END
	public static final int SKILLS_END						= 245;
	
	
	// TODO EXCEPTION SKILL ID
	public static final int JUDGEMENT						= 5000;
	public static final int PHANTOM							= 5001;
	public static final int PANTERA							= 5002;
	public static final int BLADE							= 5003;
	public static final int HELLFIRE						= 5004;
	public static final int ASURA							= 5016;
	public static final int TEMPEST							= 5026;
	public static final int TYRANT							= 5027;
	public static final int BLIND_HIDING_ASSASSIN			= 5028;
	public static final int SHADOW_STEP_CHASER				= 5029;
	public static final int ENSNARE							= 5035;
	public static final int OSIRIS							= 5036;
	public static final int BRAVE_UNION						= 5046;
	public static final int ALTERNATE 						= 5050;
	public static final int POS_WAVE 						= 5051;
	public static final int VANGUARD 						= 5052;
	public static final int RECOVERY 						= 5053;
	public static final int PRESSURE 						= 5054;
	public static final int CRUEL 							= 5055;
	
	public static final int BEHEMOTH 						= 5056;
	public static final int CHAIN_REACTION 					= 5058;
	
	public static final int CAL_CLAN_ADVANCE				= 5111;
	public static final int LIBERATION						= 5112;
	public static final int MASS_IMMUNE_TO_HARM 			= 5114;
	public static final int DIVINE_PROTECTION				= 5151;
	public static final int VISION_TELEPORT 				= 5152;
	public static final int SHOCK_ATTACK					= 5156;
	public static final int BURNING_SHOT					= 5157;
	
	public static final java.util.List<Integer> SKILLS_EXCEPTION_IDS = java.util.Arrays.asList(new Integer[]{
			JUDGEMENT, PHANTOM, PANTERA, BLADE, HELLFIRE, ASURA, TEMPEST, TYRANT, BLIND_HIDING_ASSASSIN,
			SHADOW_STEP_CHASER, ENSNARE, OSIRIS, ALTERNATE, POS_WAVE, VANGUARD, RECOVERY, PRESSURE, CRUEL, CAL_CLAN_ADVANCE,
			LIBERATION, MASS_IMMUNE_TO_HARM, DIVINE_PROTECTION, VISION_TELEPORT, SHOCK_ATTACK, BURNING_SHOT, 
			BRAVE_UNION, BEHEMOTH, CHAIN_REACTION
	});
	
	public static final int STATUS_ETERNITY					= 4342;
	public static final int STATUS_HOLD						= 4790;
	public static final int STATUS_STUN						= 4884;
	public static final int STATUS_FRAME					= 5006;
	public static final int STATUS_PHANTOM_NOMAL			= 5007;
	public static final int STATUS_PHANTOM_RIPER			= 5008;
	public static final int STATUS_PHANTOM_DEATH			= 5009;
	public static final int STATUS_PANTERA_SHOCK			= 5015;
	public static final int STATUS_SHOCK_ATTACK_TEL			= 5021;
	public static final int STATUS_TOMAHAWK_HUNT			= 5025;
	public static final int STATUS_TYRANT_EXCUTION			= 5045;
	public static final int STATUS_CONQUEROR				= 5047;
	
	public static final int STATUS_EXPOSE_WEAKNESS			= 5048;
	public static final int STATUS_FOU_SLAYER_FORCE_STUN	= 5049;
	public static final int STATUS_BEHEMOTH_DEBUFF			= 5057;
	public static final int STATUS_CHAIN_REACTION			= 5059;
	public static final int STATUS_CHAIN_REACTION_ACTIVE	= 5060;
	
	public static final int STATUS_VANGUARD_ATTACK_MOVE		= 5103;
	public static final int STATUS_VANGUARD_ATTACK			= 5104;
	public static final int STATUS_PRESSURE_1				= 5105;
	public static final int STATUS_PRESSURE_2				= 5106;
	public static final int STATUS_PRESSURE_DEATH_RECAL		= 5109;
	public static final int STATUS_BERSERK					= 5116;
	public static final int STATUS_PRESSURE_RETURN			= 5117;
	public static final int STATUS_DISINTEGRATE_NEMESIS		= 5150;
	public static final int STATUS_PHANTOM_REQUIEM			= 5155;
	public static final int STATUS_EMPIRE_OVERLOAD			= 5159;
	

	/*
	 * Status
	 */
	public static final int STATUS_BEGIN					= 997;
	
	public static final int STATUS_STRIKER_GALE_SHOT		= 997;
	public static final int STATUS_TRIPLE_STUN_DELAY		= 998;
	public static final int STATUS_DRAGON_PEARL				= 999;// 드래곤 진주
	public static final int STATUS_BRAVE					= 1000;
	public static final int STATUS_HASTE					= 1001;
	public static final int STATUS_BLUE_POTION				= 1002;
	public static final int STATUS_UNDERWATER_BREATH		= 1003;
	public static final int STATUS_WISDOM_POTION			= 1004;
	public static final int STATUS_CHAT_PROHIBITED			= 1005;
	public static final int STATUS_POISON					= 1006;
	public static final int STATUS_POISON_SILENCE			= 1007;
	public static final int STATUS_POISON_PARALYZING		= 1008;// 커스-페럴라이즈: 마비 독(진행중)
	public static final int STATUS_POISON_PARALYZED			= 1009;// 커스-페럴라이즈: 마비 독(석화중)
	public static final int STATUS_CURSE_PARALYZING			= 1010;// 커스-페럴라이즈: 석화 상태(진행 중)
	public static final int STATUS_CURSE_PARALYZED			= 1011;// 커스-페럴라이즈: 석화 중
	public static final int STATUS_FLOATING_EYE				= 1012;
	public static final int STATUS_HOLY_WATER				= 1013;
	public static final int STATUS_HOLY_MITHRIL_POWDER		= 1014;
	public static final int STATUS_HOLY_WATER_OF_EVA		= 1015;
	public static final int STATUS_ELFBRAVE					= 1016;
	public static final int STATUS_CANCLE_END				= 1016;
	public static final int STATUS_CURSE_BARLOG				= 1017;
	public static final int STATUS_CURSE_YAHEE				= 1018;
	public static final int STATUS_BLUE_POTION2				= 1019;
	public static final int STATUS_TOMAHAWK					= 1020;
	
	public static final int STATUS_END						= 1020;
	
	public static final int ANTA_MAAN						= 1023;	// 지룡의 마안
	public static final int FAFU_MAAN						= 1024;	// 수룡의 마안
	public static final int LIND_MAAN						= 1025;	// 풍룡의 마안
	public static final int VALA_MAAN						= 1026;	// 화룡의 마안
	public static final int BIRTH_MAAN						= 1027;	// 탄생의 마안
	public static final int SHAPE_MAAN						= 1028;	// 형상의 마안
	public static final int LIFE_MAAN						= 1029;	// 생명의 마안
	
	public static final int BUFF_STR_ADD					= 1125;// 증강 물약: STR
	public static final int BUFF_DEX_ADD					= 1127;// 증강 물약: DEX
	public static final int BUFF_INT_ADD					= 1129;// 증강 물약: INT
	
	public static final int GMSTATUS_BEGIN					= 2000;
	public static final int GMSTATUS_INVISIBLE				= 2000;
	public static final int GMSTATUS_HPBAR					= 2001;
	public static final int GMSTATUS_SHOWTRAPS				= 2002;
	public static final int GMSTATUS_END					= 2002;
	
	/** 탐 **/
	public static final int TAM_FRUIT_1						= 2272;
	public static final int TAM_FRUIT_2						= 2389;
	public static final int TAM_FRUIT_3						= 2390;
	public static final int TAM_FRUIT_4						= 3320;
	public static final int TAM_FRUIT_5						= 3321;
	
	
	/*
	public static final int 강화버프_수방							= 2404;
	public static final int 강화버프_풍방							= 2405;
	public static final int 강화버프_지방							= 2406;
	public static final int 강화버프_화방							= 2407;
	public static final int 강화버프_정방							= 2408;
	public static final int 강화버프_완력							= 2409;
	public static final int 강화버프_민첩							= 2410;
	public static final int 강화버프_지식							= 2411;
	public static final int 강화버프_지혜							= 2412;
	public static final int 강화버프_수공							= 2413;
	public static final int 강화버프_풍공							= 2414;
	public static final int 강화버프_지공							= 2415;
	public static final int 강화버프_화공							= 2416;
	public static final int LIFE_FAVOR						= 2417;// 생명의 가호
	public static final int HERO_FAVOR						= 2418;// 영웅의 가호
	public static final int 강화버프_공격							= 2419;
	public static final int 강화버프_방어							= 2420;
	public static final int 강화버프_마법							= 2421;
	public static final int 강화버프_기술							= 2422;
	public static final int 강화버프_정령							= 2423;*/
	
	public static final int BUFF_STRENGTH 					= 2404;
	public static final int BUFF_AGILITY					= 2405;
	public static final int BUFF_CONSTITUTION 				= 2406;
	public static final int BUFF_INTELLIGENCE 				= 2407;
	public static final int BUFF_WISDOM 					= 2408;
	public static final int BUFF_POWER 						= 2409;
	public static final int BUFF_DEXTERITY 					= 2410;
	public static final int BUFF_KNOWLEDGE 					= 2411;
	public static final int BUFF_INSIGHT 					= 2412;
	public static final int BUFF_CRAFTSMANSHIP 				= 2413;
	public static final int BUFF_AGILITY2 					= 2414;
	public static final int BUFF_CONSTITUTION2 				= 2415;
	public static final int BUFF_INTELLIGENCE2 				= 2416;
	public static final int LIFE_FAVOR 						= 2417; // Favor of Life
	public static final int HERO_FAVOR 						= 2418; // Favor of Heroes
	public static final int BUFF_ATTACK 					= 2419;
	public static final int BUFF_DEFENSE 					= 2420;
	public static final int BUFF_MAGIC 						= 2421;
	public static final int BUFF_TECHNIQUE 					= 2422;
	public static final int BUFF_SPIRIT 					= 2423;
	
	/** 요리 부분 **/
	public static final int COOKING_NOW						= 2999;
	public static final int COOKING_BEGIN					= 3000;
	/** 1차요리 효과 (노멀) */
	public static final int COOKING_1_0_N					= 3000;
	public static final int COOKING_1_1_N					= 3001;
	public static final int COOKING_1_2_N					= 3002;
	public static final int COOKING_1_3_N					= 3003;
	public static final int COOKING_1_4_N					= 3004;
	public static final int COOKING_1_5_N					= 3005;
	public static final int COOKING_1_6_N					= 3006;
	public static final int COOKING_1_7_N					= 3007;
	/** 2차요리 효과 (노멀) */
	public static final int COOKING_1_8_N					= 3008;
	public static final int COOKING_1_9_N					= 3009;
	public static final int COOKING_1_10_N					= 3010;
	public static final int COOKING_1_11_N					= 3011;
	public static final int COOKING_1_12_N					= 3012;
	public static final int COOKING_1_13_N					= 3013;
	public static final int COOKING_1_14_N					= 3014;
	public static final int COOKING_1_15_N					= 3015;
	/** 3차요리 효과 (노멀) */
	public static final int COOKING_1_16_N					= 3016;
	public static final int COOKING_1_17_N					= 3017;
	public static final int COOKING_1_18_N					= 3018;
	public static final int COOKING_1_19_N					= 3019;
	public static final int COOKING_1_20_N					= 3020;
	public static final int COOKING_1_21_N					= 3021;
	public static final int COOKING_1_22_N					= 3022;
	public static final int COOKING_1_23_N					= 3023;
	/** 1차요리 효과 (환상) */
	public static final int COOKING_1_0_S					= 3050;
	public static final int COOKING_1_1_S					= 3051;
	public static final int COOKING_1_2_S					= 3052;
	public static final int COOKING_1_3_S					= 3053;
	public static final int COOKING_1_4_S					= 3054;
	public static final int COOKING_1_5_S					= 3055;
	public static final int COOKING_1_6_S					= 3056;
	public static final int COOKING_1_7_S					= 3057;
	/** 2차요리 효과 (환상) */
	public static final int COOKING_1_8_S					= 3058;
	public static final int COOKING_1_9_S					= 3059;
	public static final int COOKING_1_10_S					= 3060;
	public static final int COOKING_1_11_S					= 3061;
	public static final int COOKING_1_12_S					= 3062;
	public static final int COOKING_1_13_S					= 3063;
	public static final int COOKING_1_14_S					= 3064;
	public static final int COOKING_1_15_S					= 3065;
	/** 3차요리 효과 (환상) */
	public static final int COOKING_1_16_S					= 3066;
	public static final int COOKING_1_17_S					= 3067;
	public static final int COOKING_1_18_S					= 3068;
	public static final int COOKING_1_19_S					= 3069;
	public static final int COOKING_1_20_S					= 3070;
	public static final int COOKING_1_21_S					= 3071;
	public static final int COOKING_1_22_S					= 3072;
	public static final int COOKING_1_23_S					= 3073;
    /** 요리 리뉴얼 **/
	public static final int COOK_STR						= 3074;
	public static final int COOK_DEX						= 3075;
	public static final int COOK_INT						= 3076;
	public static final int COOK_GROW						= 3077;
	/** 축복 요리 리뉴얼 **/
	public static final int BLESS_COOK_STR					= 3078;
	public static final int BLESS_COOK_DEX					= 3079;
	public static final int BLESS_COOK_INT					= 3080;
	public static final int BLESS_COOK_GROW					= 3081;
	/** 이벤트 요리 **/
	public static final int NARUTER_CANDY					= 3082;
	public static final int COOK_METIS_NORMAL				= 3083;
	public static final int COOK_METIS_GROW					= 3084;
	public static final int DOGAM_BUFF						= 1541;// 도감버프
	
	// 아덴 요리
	public static final int COOK_ADEN_SPECIAL_STEAK			= 3024;
	public static final int COOK_ADEN_SPECIAL_CANAPE		= 3025;
	public static final int COOK_ADEN_SPECIAL_SALAD			= 3026;
	public static final int COOK_ADEN_TOMATO_SOUP			= 3027;
	// 축복받은 아덴 요리
	public static final int BLESS_COOK_ADEN_SPECIAL_STEAK	= 3028;
	public static final int BLESS_COOK_ADEN_SPECIAL_CANAPE	= 3029;
	public static final int BLESS_COOK_ADEN_SPECIAL_SALAD	= 3030;
	public static final int BLESS_COOK_ADEN_TOMATO_SOUP		= 3031;
	
	public static final int COOKING_END						= 3084;	
	
	public static final int METIS_BLESS_SCROLL				= 3085;
	public static final int COMA_A							= 50006;
	public static final int COMA_B							= 50007;
	public static final int BUFF_ICE_STR					= 10524;
	public static final int BUFF_ICE_DEX					= 10525;
	public static final int BUFF_ICE_INT					= 10526;
	public static final int BUFF_JUGUN						= 10534;
	
	/** 전투 강화 주문서 **/
	public static final int STATUS_CASHSCROLL				= 6993;
	public static final int STATUS_CASHSCROLL2				= 6994;
	public static final int STATUS_CASHSCROLL3				= 6995;	
	public static final int NEW_CASHSCROLL1					= 7111;// 투사의 전투 강화 주문서
	public static final int NEW_CASHSCROLL2					= 7112;// 명궁의 전투 강화 주문서
	public static final int NEW_CASHSCROLL3					= 7113;// 현자의 전투 강화 주문서
	
	public static final int DRAGON_FAVOR_PCCAFE				= 1816;// 드래곤의 가호(PC)
	public static final int GRACE_OF_TOP					= 2655;// 정상의 가호
	
	public static final int BUFF_SPECIAL_GROW				= 3205;// 특별한 성장 버프
	public static final int BUFF_SPECIAL_DEFFENS			= 3206;// 특별한 방어 버프
	public static final int BUFF_SPECIAL_ATTACK				= 3207;// 특별한 공격 버프
	public static final int BUFF_MISOPIA_GROW				= 3208;// 미소피아의 성장 버프
	public static final int BUFF_MISOPIA_DEFFENS			= 3209;// 미소피아의 방어 버프
	public static final int BUFF_MISOPIA_ATTACK				= 3210;// 미소피아의 공격 버프
	
	public static final int HALPAS_JUJOO_MP_DAMAGE			= 3258;// 할파스의 저주: HP 회복 시 MP 감소
	public static final int HALPAS_JUJOO_MP_INCREASE		= 3259;// 할파스의 저주: 마법 사용 소모 MP가 증가
	public static final int DOMINATION_TELEPORT				= 3426;// 순간이동 지배 반지
	public static final int BUFF_GOLD_FEATHER				= 3926;// 고급버프
	public static final int BUFF_GOLD_FEATHER_GROW			= 3927;// 성장버프
	public static final int LEVELUP_BONUS_BUFF				= 4096;// 레벨업 보너스
	public static final int DRAGON_BLESS					= 4339;// 드래곤의 축복
	public static final int EINHASAD_FAVOR					= 4354;// 아인하사드의 가호
	public static final int EARTH_OF_BLESS					= 4431;// 축복의 땅
	public static final int FAITH_OF_HALPAH_PVP				= 4453;// 할파스의 신의 발동
	public static final int FAITH_OF_HALPAH					= 4454;// 할파스의 신의
	public static final int BLACK_MAAN						= 4455;// 흑룡의 마안
	public static final int ABSOLUTE_MAAN					= 4456;// 절대의 마안
	public static final int BUFF_ZAKEN						= 4535;// 자켄버프
	public static final int BUFF_ZAKEN_HALPAS				= 4536;// 자켄버프
	public static final int JUDGEMENT_DOLL					= 4539;// 저지먼트(잠재력)
	
	public static final int GRACE_CROWN_1ST					= 4654;
	public static final int GRACE_KNIGHT_1ST				= 4655;
	public static final int GRACE_DARKELF_1ST				= 4656;
	public static final int GRACE_WIZARD_1ST				= 4657;
	public static final int GRACE_ELF_1ST					= 4658;
	public static final int GRACE_DRAGONKNIGHT_1ST			= 4659;
	public static final int GRACE_ILLUSIONIST_1ST			= 4660;
	public static final int GRACE_WARRIOR_1ST				= 4661;
	public static final int GRACE_FENCER_1ST				= 4662;
	public static final int GRACE_LANCER_1ST				= 4663;
	
	public static final int PLEDGE_EXP_BUFF_I_NORMAL		= 4673;// 혈맹 성장 버프 I 일반
	public static final int PLEDGE_EXP_BUFF_II_NORMAL		= 4674;// 혈맹 성장 버프 II 일반
	public static final int PLEDGE_BATTLE_BUFF_I_NORMAL		= 4675;// 혈맹 전투 버프 I 일반
	public static final int PLEDGE_BATTLE_BUFF_II_NORMAL	= 4676;// 혈맹 전투 버프 II 일반
	
	public static final int PLEDGE_DEFENS_BUFF_I_NORMAL		= 5433;// 혈맹 방어 버프 I 일반
	public static final int PLEDGE_DEFENS_BUFF_II_NORMAL	= 5434;// 혈맹 방어 버프 II 일반
	
	public static final int PLEDGE_EXP_BUFF_I_KING			= 5598;// 혈맹 성장 버프 I 군주/부군주
	public static final int PLEDGE_EXP_BUFF_I_KNIGHT		= 5599;// 혈맹 성장 버프 I 수호
	public static final int PLEDGE_EXP_BUFF_I_ELITE			= 5600;// 혈맹 성장 버프 I 정예
	public static final int PLEDGE_EXP_BUFF_II_KING			= 5601;// 혈맹 성장 버프 II 군주/부군주
	public static final int PLEDGE_EXP_BUFF_II_KNIGHT		= 5602;// 혈맹 성장 버프 II 수호
	public static final int PLEDGE_EXP_BUFF_II_ELITE		= 5603;// 혈맹 성장 버프 II 정예
	
	public static final int PLEDGE_BATTLE_BUFF_I_ELITE		= 5604;// 혈맹 전투 버프 I 정예이상
	public static final int PLEDGE_BATTLE_BUFF_II_ELITE		= 5605;// 혈맹 전투 버프 II 정예이상
	
	public static final int PLEDGE_DEFENS_BUFF_I_ELITE		= 5606;// 혈맹 방어 버프 I 정예이상
	public static final int PLEDGE_DEFENS_BUFF_II_ELITE		= 5607;// 혈맹 방어 버프 II 정예이상
	
	public static final int PLEDGE_SIEGE_BUFF_KING			= 5608;// 혈맹 공성 버프(군주/부군주)
	public static final int PLEDGE_SIEGE_BUFF_KNIGHT		= 5609;// 혈맹 공성 버프(수호 기사)
	public static final int PLEDGE_SIEGE_BUFF_ELITE			= 5610;// 혈맹 공성 버프(정예 기사)
	
	public static final int TOWER_BUFF_N					= 4842;// 북쪽 탑의 축복
	public static final int TOWER_BUFF_E_BLESS				= 4845;// 동쪽 탑의 축복
	public static final int TOWER_BUFF_S					= 4847;// 남쪽 탑의 축복
	public static final int TOWER_BUFF_W					= 4848;// 서쪽 탑의 축복
	public static final int TOWER_BUFF_E_CURSE				= 4850;// 동쪽 탑의 저주
	
	public static final int KYULJUN_CASHSCROLL				= 4851;// 결전의 주문서
	public static final int ANONYMITY_POLY					= 4852;// 익명 변신
	public static final int BUFF_BLACK_SAND					= 4914;// 흑사의 버프
	public static final int ARROW_OF_AURAKIA				= 4915;// 아우라키아의 화살
	public static final int DRAGON_FAVOR					= 5014;// 드래곤의 가호
	public static final int BUFF_TARAS_HIGHT_MAGIC			= 5032;// 타라스의 상급 마법
	public static final int HP_CASHSCROLL					= 5168;// 체력 강화 주문서(hp+2000)
	
	public static final int GRACE_CROWN_2ST					= 5169;
	public static final int GRACE_KNIGHT_2ST				= 5170;
	public static final int GRACE_ELF_2ST					= 5171;
	public static final int GRACE_WIZARD_2ST				= 5172;
	public static final int GRACE_DARKELF_2ST				= 5173;
	public static final int GRACE_DRAGONKNIGHT_2ST			= 5174;
	public static final int GRACE_ILLUSIONIST_2ST			= 5175;
	public static final int GRACE_WARRIOR_2ST				= 5176;
	public static final int GRACE_FENCER_2ST				= 5177;
	public static final int GRACE_LANCER_2ST				= 5178;
	
	public static final int GRACE_CROWN_3ST					= 5179;
	public static final int GRACE_KNIGHT_3ST				= 5180;
	public static final int GRACE_ELF_3ST					= 5181;
	public static final int GRACE_WIZARD_3ST				= 5182;
	public static final int GRACE_DARKELF_3ST				= 5183;
	public static final int GRACE_DRAGONKNIGHT_3ST			= 5184;
	public static final int GRACE_ILLUSIONIST_3ST			= 5185;
	public static final int GRACE_WARRIOR_3ST				= 5186;
	public static final int GRACE_FENCER_3ST				= 5187;
	public static final int GRACE_LANCER_3ST				= 5188;
	
	public static final int DRAGON_EXP_POTION				= 5216;// 드래곤의 성장 물약
	public static final int MP_REDUCTION_POTION				= 5225;// 마나 절감 물약
	
	public static final int CLAN_BUFF_AIR					= 5250;
	public static final int CLAN_BUFF_OREN					= 5251;
	public static final int CLAN_BUFF_VALY					= 5252;
	public static final int CLAN_BUFF_FIRE					= 5253;
	public static final int CLAN_BUFF_BEGINNER				= 5254;
	public static final int CLAN_BUFF_GLUDIO				= 5255;
	public static final int CLAN_BUFF_EVA					= 5256;
	public static final int CLAN_BUFF_VALY_DUNGEON			= 5257;
	public static final int CLAN_BUFF_DOMINATION			= 5258;
	public static final int CLAN_BUFF_TOWER_1				= 5259;
	public static final int CLAN_BUFF_TOWER_2				= 5260;
	public static final int CLAN_BUFF_TOWER_3				= 5261;
	
	public static final int GROW_BUFF						= 5430;// 성장 버프
	public static final int GERAD_BUFF						= 5432;// 게라드 버프
	
	public static final int GERAD_LAW_BUFF					= 5436;// 게라드 버프(저레벨)
	public static final int SILVER_KNIGHT_GROW_BUFF			= 5437;// 은기사 성장 버프
	
	public static final int SASIN_GRACE						= 5459;// 사신의 가호
	public static final int BUFF_LEVEL100_CONGRATS			= 5464;// 100레벨 축하 버프
	public static final int BLESS_OF_METIS					= 5465;// 메티스의 축복
	public static final int BUFF_VISUAL_OF_CAPTAIN			= 5495;// 선장의 시야
	public static final int BUFF_PUFFER_FISH				= 5496;// 먹음직스러운 복어
	
	
	// 아인하사드의 신의 버프
	public static final int EINHASAD_FAITH_GROUP_1			= 5513;
	public static final int EINHASAD_FAITH_GROUP_2			= 5525;
	public static final int EINHASAD_FAITH_INDEX_1			= 5499;
	public static final int EINHASAD_FAITH_INDEX_2			= 5514;
	public static final int EINHASAD_FAITH_INDEX_3			= 5515;
	public static final int EINHASAD_FAITH_INDEX_4			= 5516;
	
	public static final int BUFF_PCCAFE_EXP					= 5524;// PC방 전용 EXP +10
	
	public static final int EINHASAD_FAITH_INDEX_5			= 5526;
	public static final int EINHASAD_FAITH_INDEX_6			= 5527;
	public static final int EINHASAD_FAITH_INDEX_7			= 5528;
	public static final int EINHASAD_FAITH_INDEX_8			= 5529;
	
	public static final int BUFF_LUNAR_NEW_YERR				= 5556;// 설날 버프
	
	public static final int STATUS_ART_MAAN					= 858585;// 예술가의 마안
	public static final int ADEN_FAST_GRACE					= 858586;// 아덴의 신속 가호
	
	/** 경험치 물약 **/
	public static final int EXP_POTION						= 200881;// 성장의 물약
	public static final int EXP_POTION1						= 200882;// 빛나는성장의 물약
	public static final int EXP_POTION2						= 200883;// 진데스나이트의 성장 물약
	public static final int EXP_POTION3						= 200884;// 드래곤의 성장 물약
	public static final int EXP_POTION4						= 200885;// 영웅의 성장 물약
	public static final int EXP_POTION5						= 200886;// 21주년 성장 물약
	public static final int EXP_POTION6						= 200887;// 21주년 전설 성장 물약
	public static final int EXP_POTION7						= 200888;// 아인하사드의 성장 물약
	public static final int EXP_POTION8						= 200889;// 향상된 빛나는 성장 물약
	
	public static final int FEATHER_BUFF_A					= 22000;// 운세버프(매우좋음)
	public static final int FEATHER_BUFF_B					= 22001;// 운세버프(좋음)
	public static final int FEATHER_BUFF_C					= 22002;// 운세버프(보통)
	public static final int FEATHER_BUFF_D					= 22003;// 운세버프(나쁨)
	
	
	/** 랭킹 버프 **/
	public static final int RANKING_BUFF_1					= 3533;// 3533 ~ 3535
	public static final int RANKING_BUFF_2					= 3530;// 3530 ~ 3532
	public static final int RANKING_BUFF_3					= 3527;// 3527 ~ 3529
	public static final int RANKING_BUFF_4_10				= 3524;// 3524 ~ 3526
	public static final int RANKING_BUFF_11_20				= 3523;
	public static final int RANKING_BUFF_21_30				= 5522;
	public static final int RANKING_BUFF_31_60				= 5523;
	public static final int RANKING_BUFF_61_100				= 5533;
	
	
	/** 체인소드 약점 노출 **/
	public static final int EXPOSE_WEAKNESS					= 7000111;// 약점 노출
	
	/** 브레이브 아바타 **/
	public static final int STATUS_AURA						= 7100;
	
	/** 혈맹 버프 **/
	public static final int CLAN_BUFF1						= 505;
	public static final int CLAN_BUFF2						= 506;
	public static final int CLAN_BUFF3						= 507;
	public static final int CLAN_BUFF4						= 508;
	
	public static final int CLAN_BLESS						= 990005;// 혈맹의 축복
	
	public static final int ATTENDANCE_DELAY				= 301011;// 출석체크 딜레이
	public static final int ARCA_CANCEL_DELAY				= 301012;// 탐 취소 딜레이
	
	/** 중첩 불가 **/
	public static final int ANTI_FINAL_BURN					= 5000111;// 파번 중복 사용 불가
	public static final int ANTI_METEOR						= 5000222;// 미티어 중복 사용 불가
	public static final int ANTI_DISINTEGRATE				= 5000333;// 디스 중복 사용 불가	
	public static final int NO_DIS							= 5000334;// 디스 중첩 대미지 불가
	
	public static final int STATUS_FREEZE					= 10071;
	public static final int STATUS_IGNITION					= 20075;
	public static final int STATUS_QUAKE					= 20076;
	public static final int STATUS_SHOCK					= 20077;
	public static final int STATUS_BALANCE					= 20078;
	public static final int STATUS_FRUIT					= 20079;
	public static final int STATUS_OVERLAP					= 20080;
	public static final int STATUS_DESHOCK					= 20083;
	public static final int STATUS_CUBE						= 20084;
	public static final int STATUS_PET_FOOD					= 55555;
	public static final int STATUS_WITCH_POTION				= 7790;
	public static final int COMBO_BUFF						= 80006;// 콤보
	public static final int DRAGON_PUPLE					= 80008;
	public static final int DRAGON_TOPAZ					= 80009;
	public static final int REPORT_DELAY					= 80010;
	public static final int ABYSS_LIGHTNING_TIME			= 80018;
	public static final int STATUS_SAFTY_MODE				= 77777;
	public static final int SET_BUFF						= 90008;// 셋 버프
	public static final int DELAY							= 9999;// 광역스턴딜레이
	public static final int EMERALD_YES						= 22018;// 드래곤 에메랄드
	public static final int EMERALD_NO						= 22019;// 드래곤 에메랄드
	public static final int ICE_ERUPTION					= 22058;// 극한의 무기 아이스 이럽션
	
	public static final int MOB_CANCELLATION				= 22056;// 오만보스 광역 캔슬
	public static final int MOB_SLOW_1						= 30001;// 슬로우 1번모션
	public static final int MOB_SLOW_18						= 30000;// 슬로우 18번모션
	public static final int MOB_CURSEPARALYZ_18				= 30007;// 커스 18번모션
	public static final int MOB_CURSEPARALYZ_19				= 30002;// 커스 19번모션
	public static final int MOB_COCA						= 30003;// 코카트리스 얼리기공격
	public static final int MOB_BASILL						= 30004;// 바실리스크 얼리기에볼
	public static final int MOB_SHOCKSTUN_18				= 666001;// 쇼크스턴 18번모션
	public static final int MOB_SHOCKSTUN_19				= 666000;// 쇼크스턴 19번모션
	public static final int MOB_SHOCKSTUN_30				= 30081;// 쇼크스턴 30번모션
	public static final int MOB_RANGESTUN_18				= 30006;// 범위스턴 18번모션
	public static final int MOB_RANGESTUN_19				= 30005;// 범위스턴 19번모션
	public static final int MOB_RANGESTUN_20				= 30010;// 범위스턴 20번모션
	public static final int MOB_RANGESTUN_30				= 22055;// 범위스턴 30번 모션
	public static final int MOB_RANGE_FREEZE_18				= 170027;// 범위홀드
	public static final int MOB_DISEASE_1					= 30079;// 디지즈 1번모션
	public static final int MOB_DISEASE_30					= 30008;// 디지즈 30번모션
	public static final int MOB_WEAKNESS_1					= 30009;// 위크니스 1번모션
	public static final int MOB_WEAKNESS_19					= 130009;// 위크니스 19번모션
	public static final int MOB_WINDSHACKLE_1				= 30084;// 윈드셰클 1번모션
	public static final int MOB_ABSOLUTE_BLADE				= 929292;// 오만 앱솔루트 블레이드
	public static final int MOB_IMMUNE_BLADE				= 929293;// 이뮨 깨기
	public static final int MOB_DESPERADO					= 22061;// 데스페라도
	public static final int MOB_ARMOR_BRAKE					= 71067;// 아머브레이크
	public static final int MOB_COUNTER_BARRIER				= 171030;// 카운터 배리어
	public static final int MOB_COUNTER_BARRIER_BETERANG	= 71030;// 카운터 배리어 베테랑
	public static final int MOB_ERASE_MAGIC					= 71076;// 이래이즈 매직
	public static final int MOB_POLLUTE_WATER				= 150013;// 폴루트 워터
	public static final int MOB_ICE_LANCE					= 150015;// 몹 얼리기
	public static final int MOB_DEATH_HEAL					= 1573;// 데스힐
	public static final int MOB_RANGE_DEATH_HEAL			= 15573;// 광역 데스힐
	public static final int MOB_RANGE_DEATH_POTION			= 15574;// 광역 데스 포션
	public static final int MOB_MOVING						= 50000;// 몬스터 이동
	public static final int MOB_TRIPLE_ARROW				= 50001;// 몹 트리플	
	public static final int MOB_FOU_SLAYER					= 50002;// 몹 포우슬레이어	
	public static final int MOB_ICE_UNSUK_18				= 430008;// 아이스 운석 18번 모션
	public static final int MOB_ICE_UNSUK_19				= 440008;// 아이스 운석 19번 모션
	public static final int MOB_BIG_LIGHTING				= 150021;// 거대 번개 스킬
	public static final int MOB_BLIND_HIDING				= 150097;// 몬스터 블라인드 하이딩
	public static final int MOB_RUN_TELEPORT				= 170028;// 몬스터 도망 텔레포트
	public static final int MOB_RECALL						= 10057;// 소환
	public static final int MOB_PANTERA						= 180104;// 몬스터 판테라
	
	public static final int JIBAE_IRIS_TELEPORT				= 71052;// 아이리스 넉백
	public static final int IRIS_TELEPORT					= 71054;// 아이리스 넉백
	public static final int LICH_TELEPORT					= 430003;// 리치 넉백
	public static final int HAPEEQUEEN_TONADO				= 150030;// 하피 퀸 토네이도
	public static final int BALLOG_MONSTER					= 150012;// 발록 몹소환	
	public static final int BALLOG_FIRE						= 150010;// 안식처 스킬
	public static final int MAIDEN_AGRO						= 25004;// 메이든 어그로 스킬
	public static final int DARK_WATCH_METEOR				= 11050;// 어둠 감시관 미티어
	

	/** 드래곤 리뉴얼 관련 스킬 **/
	public static final int DRAGONRAID_BUFF					= 55005;// 레이드 클리어시 입장불가 버프
	
	public static final int BUFF_CRAY						= 50011;// 크레이버프
	public static final int BUFF_SAEL						= 10499;// 사엘버프
	public static final int BUFF_GUNTER						= 120384;// 군터버프
	public static final int BUFF_BALRACAS					= 50013;// 발라데스나이트버프
	
	public static final int RIND_ANSIG						= 10026;// 안식
	public static final int ANTA_ANSIG						= 10027;// 안식
	public static final int VALA_ANSIG						= 10028;// 안식
	public static final int FAFU_ANSIG						= 10029;// 안식
	
	public static final int VALA_HALPAS						= 100028;// 할파스 권속 소환
	public static final int DETHNIGHT_BUNNO					= 22062;// 할파스 권속 공격 버프

	public static final int ANTA_MESSAGE_1					= 22020;// 안타[용언1 / 캔슬 + 마비 -> 오브 모크! 케 네시]
	public static final int ANTA_MESSAGE_2					= 22021;// 안타[용언2 / 블레스+독/ 오브 모크! 켄 로우]
	public static final int ANTA_MESSAGE_3					= 22022;// 안타[용언3 / 왼오펀치+고함/ 오브 모크! 티기르]
	public static final int ANTA_MESSAGE_4					= 22023;// 안타[용언4 / 펀치+블레스/ 오브 모크! 켄 티기르]
	public static final int ANTA_MESSAGE_5					= 22024;// 안타[용언5 / 고함+블레스/ 오브 모크! 루오타]
	public static final int ANTA_MESSAGE_6					= 22025;// 안타[용언6 / 스턴+점프/ 오브 모크! 뮤즈삼]
	public static final int ANTA_MESSAGE_7					= 22026;// 안타[용언7 / 스턴+발작/ 오브 모크! 너츠삼]
	public static final int ANTA_MESSAGE_8					= 22027;// 안타[용언8 / 스턴+발+점/ 오브 모크! 티프삼]
	public static final int ANTA_MESSAGE_9					= 22028;// 안타[용언9 / 웨폰+블레스/ 오브 모크! 리라프]
	public static final int ANTA_MESSAGE_10					= 22029;// 안타[용언10 / 웨폰+마비/ 오브 모크! 세이 라라프]
	public static final int ANTA_CANCELLATION				= 22030;// 안타[범위캔슬 / 6칸 / 8명]
	public static final int ANTA_SHOCKSTUN					= 22031;// 안타[범위스턴 / 4칸]
	public static final int ANTA_WEAPON_BREAK				= 22032;// 안타[범위웨폰 / 4칸]
	public static final int PREDICATEDELAY					= 22033;// 용언스킬딜레이
	public static final int ANTA_UNSUCK						= 22034;// 안타[범위 운석]
	
	public static final int PAP_FIVEPEARLBUFF				= 22035;// 파푸[오색진주 파괴 버프]
	public static final int PAP_MAGICALPEARLBUFF			= 22036;// 파푸[신비한오색진주 파괴버프]
	public static final int PAP_DEATH_POTION				= 22037;// 파푸[데스 포션 버프]
	public static final int PAP_DEATH_HEAL					= 22038;// 파푸[데스 힐 버프]
	public static final int PAP_REDUCE_HEAL					= 22039;// 파푸[리듀스 힐 버프]
	public static final int PAP_PREDICATE1					= 22041;// 파푸[용언1:리오타! 피로이 나! [오색 진주 / 신비한 오색 진주 / 토르나 소환]
	public static final int PAP_PREDICATE3					= 22043;// 파푸[용언3:리오타! 라나 오이므! [데스포션]
	public static final int PAP_PREDICATE5					= 22045;// 파푸[용언5:리오타! 네나 우누스! [리듀스 힐 + 머리 공격 + 아이스 브레스]
	public static final int PAP_PREDICATE6					= 22046;// 파푸[용언6:리오타! 테나 웨인라크! [데스 힐 + 꼬리 공격 + 아이스 브레스]
	public static final int PAP_PREDICATE7					= 22047;// 파푸[용언7:리오타! 라나 폰폰! [캔슬레이션 + 오른속 2번 ] [범위 X]
	public static final int PAP_PREDICATE8					= 22048;// 파푸[용언8:리오타! 레포 폰폰! [웨폰브레이크 + 왼손 2번 ] [범위 X]
	public static final int PAP_PREDICATE9					= 22049;// 파푸[용언9:리오타! 테나 론디르 ! [꼬리 2연타 + 아이스 브레스][범위 X]
	public static final int PAP_PREDICATE11					= 22051;// 파푸[용언11:리오타! 오니즈 웨인라크! [매스 캔슬레이션 + 데스 힐 + 아이스 미티어 + 아이스 이럽션] [범위 O]
	public static final int PAP_PREDICATE12					= 22052;// 파푸[용언12:리오타! 오니즈 쿠스온 웨인라크! [매스 캔슬레이션 + 데스힐 + 아이스 미티어 + 발작] [범위 0]

	public static final int ANTA_BUFF						= 22015;// 안타라스 혈흔
	public static final int FAFU_BUFF						= 22016;// 파푸리온 혈흔
	public static final int RIND_BUFF						= 22060;
	public static final int VALL_BUFF						= 2220177;// 발라 혈흔

	public static final int LINDBIOR_SPIRIT_EFFECT			= 50009;// 어두워지기
	public static final int RINDVIOR_WIND_SHACKLE			= 7001;// 윈드세클
	public static final int RINDVIOR_PREDICATE_CANCELLATION	= 7002;// 리콜 소환
	public static final int RINDVIOR_TORNADO_FORE			= 7003;// 회오리 4개 전체 마법
	public static final int RINDVIOR_WEAPON					= 7004;// 웨폰
	public static final int RINDVIOR_BOW					= 7005;
	// 7006 라이트닝 스톰
	public static final int RINDVIOR_WIND_SHACKLE_1			= 7007;
	public static final int RINDVIOR_WEAPON_2				= 7008;
	public static final int RINDVIOR_STORM					= 7009;
	public static final int RINDVIOR_CANCELLATION			= 7010;
	// 7011 브레스
	// 7012 라이트닝 스톰 SILENCE
	public static final int RINDVIOR_SILENCE				= 7013;
	public static final int RINDVIOR_SUMMON_MONSTER			= 7018;
	public static final int RINDVIOR_PREDICATE				= 7019;
	public static final int RINDVIOR_SUMMON_MONSTER_CLOUD	= 7023;

	public static final int VALLAKAS_PREDICATE1				= 30033;// 제르큐오 삼 케로누 디스트로이+캔슬+이뮨깨기
	public static final int VALLAKAS_PREDICATE2				= 30034;// 제르큐오 케로누 켈 쥬펜 디스트로이+스턴+이뮨깨기
	public static final int VALLAKAS_PREDICATE3				= 30035;// 제르큐오 베르하 디스트로이+이뮨깨기
	public static final int VALLAKAS_PREDICATE4				= 30036;// 제르큐오 삼 쥬펜 킬리카야 디스트로이+스턴
	public static final int VALLAKAS_PREDICATE5				= 30037;// 제르큐오 삼 킬리카야 스턴
	
	public static final int HALPAS_FIRE_BRESS				= 180089;
	public static final int HALPAS_POISON_BRESS				= 180090;
	public static final int HALPAS_ICE_BRESS				= 180091;
	public static final int HALPAS_WIND_BRESS				= 180092;
	public static final int HALPAS_FLY_TELEPORT				= 180093;
	public static final int HALPAS_IMMUNE_BLADE				= 180094;
	public static final int HALPAS_RECALL					= 180095;
	public static final int HALPAS_ZAKEN_CALL				= 180106;
	
	public static final int PHOENIX_CANCELLATION			= 7024;
	public static final int PHOENIX_SUMMON_MONSTER			= 7025;
	public static final int EFRETE_SUMMON_MONSTER			= 7028;
	public static final int BLACKELDER_DEATH_POTION			= 7030;
	public static final int BLACKELDER_DEATH_HEAL			= 7031;
	public static final int BLACKELDER						= 7037;// [ 라이트닝 / 리치 오로라 / 검은 마법 ]
	public static final int ZEROS_REDUCTION					= 50050;// 제로스 리덕션
	public static final int DRAKE_WIND_SHACKLE				= 7035;
	public static final int DRAKE_MASSTELEPORT				= 7036;
	
	public static final int ANTARAS_ERUPTION				= 7300;
	public static final int FAFURION_ERUPTION				= 7301;
	public static final int VALLAKAS_ERUPTION_1				= 7302;
	public static final int VALLAKAS_ERUPTION_2				= 7303;
	
	// 광역 독범위
	public static final int STATE_POISON					= 20016;// 광역 독 1번 모션
	public static final int STATE_POISON1					= 40048;// 광역 독 18번 모션
	public static final int STATE_POISON2					= 120016;// 광역 독 19번 모션

	// 사막 보스 [ 광역 마법 ]	
	public static final int DESERT_SKILL1					= 7041;// 광역 커스 패럴라이즈
	public static final int DESERT_SKILL2					= 7042;// 광역 어스 바인드
	public static final int DESERT_SKILL3					= 7043;// 광역 마나 드레인
	public static final int DESERT_SKILL4					= 7044;// 광역 독
	public static final int DESERT_SKILL5					= 7045;// 광역 위크니스
	public static final int DESERT_SKILL6					= 7046;// 광역 다크니스
	public static final int DESERT_SKILL7					= 7047;// 광역 포그 오브 슬리핑
	public static final int DESERT_SKILL8					= 7060;// 광역 디케이
	public static final int DESERT_SKILL9					= 7061;// 광역 디지즈
	public static final int DESERT_SKILL10					= 7062;// 광역 커스/디지즈/다크니스/위크니스
	public static final int DESERT_SKILL11					= 7048;// 에르자베 토네이도
	public static final int DESERT_SKILL12					= 7049;// 에르자베 서먼 몬스터
	public static final int DESERT_SKILL13					= 7050;// 에르자베 토네이도 스폰
	
	// 간수장 타로스
	public static final int TAROS_SKILL1					= 15575;// 타로스 광역 데스힐/구름
	public static final int TAROS_SKILL2					= 15576;// 타로스 광역 데스포션/사일런스
	
	// 펫 스킬 스킬번호 고정
	public static final int PET_BUFF_GROW					= 3919;// 성장의 나뭇잎: EXP 획득 30% 증가
	public static final int PET_BUFF_EIN					= 3920;// 아인의 나뭇잎: EXP 획득 50% 증가
	public static final int PET_BUFF_SKY					= 3921;// 천상의 나뭇잎: EXP 획득 100% 증가
	public static final int PET_BUFF_YEGABAM				= 3922;// 핏볼의 예거밤: 투지 포인트 획득 30% 증가
	public static final int PET_BUFF_BLOOD					= 4001;// 광견의 혈흔: 대인 공격력 상승 / 방어력 감소
	
	public static final int PET_ATTR_FIRE					= 1000001;// 펫 불속성 공격
	public static final int PET_ATTR_WATER					= 1000002;// 펫 물속성 공격
	public static final int PET_ATTR_WIND					= 1000003;// 펫 바람속성 공격
	public static final int PET_ATTR_EARTH					= 1000004;// 펫 땅속성 공격

	public static final int STATUS_PSS						= 9851;
}
