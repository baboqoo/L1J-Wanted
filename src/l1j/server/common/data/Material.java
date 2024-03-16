package l1j.server.common.data;

import java.util.Arrays;
import java.util.List;

public enum Material{
	NONE(			0,	"-", "-"),
	LIQUID(			1,	"액체", "Liquid"),
	WAX(			2,	"밀랍", "Wax"),
	VEGGY(			3,	"식물성", "Vegetable"),
	FLESH(			4,	"동물성", "Animal"),
	PAPER(			5,	"종이", "Paper"),
	CLOTH(			6,	"천", "Cloth"),
	LEATHER(		7,	"가죽", "Leather"), 
	WOOD(			8,	"나무", "Wood"),
	BONE(			9,	"뼈", "Bone"),
	DRAGON_HIDE(	10,	"용비늘", "Dragon Hide"),
	IRON(			11,	"철", "Iron"),
	METAL(			12,	"금속", "Metal"),
	COPPER(			13,	"구리", "Copper"),
	SILVER(			14,	"은", "Silver"),
	GOLD(			15,	"금", "Gold"),
	PLATINUM(		16,	"백금", "Platinum"),
	MITHRIL(		17,	"미스릴", "Mithril"),
	PLASTIC(		18,	"블랙미스릴", "Black Mithril"),
	GLASS(			19,	"유리", "Glass"),
	GEMSTONE(		20,	"보석", "Gemstone"),
	MINERAL(		21,	"광석", "Mineral"),
	ORIHARUKON(		22,	"오리하루콘", "Oriharukon"),
	DRANIUM(		23,	"드라니움", "Dranium"),
	;
	private int value;
	private String name;
	private String name_en;
	Material(int val, String val2, String val3){
		value = val;
		name = val2;
		name_en = val3;
	}
	public int toInt(){
		return value;
	}
	public String toName() {
		return name;
	}
	public String toNameEn() {
		return name_en;
	}
	public boolean equals(Material v){
		return value == v.value;
	}
	public static Material fromInt(int i){
		switch(i){
		case 0:
			return NONE;
		case 1:
			return LIQUID;
		case 2:
			return WAX;
		case 3:
			return VEGGY;
		case 4:
			return FLESH;
		case 5:
			return PAPER;
		case 6:
			return CLOTH;
		case 7:
			return LEATHER;
		case 8:
			return WOOD;
		case 9:
			return BONE;
		case 10:
			return DRAGON_HIDE;
		case 11:
			return IRON;
		case 12:
			return METAL;
		case 13:
			return COPPER;
		case 14:
			return SILVER;
		case 15:
			return GOLD;
		case 16:
			return PLATINUM;
		case 17:
			return MITHRIL;
		case 18:
			return PLASTIC;
		case 19:
			return GLASS;
		case 20:
			return GEMSTONE;
		case 21:
			return MINERAL;
		case 22:
			return ORIHARUKON;
		case 23:
			return DRANIUM;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments Material, %d", i));
		}
	}
	
	public static Material fromString(String str) {
		switch (str) {
		case "NONE(-)":
			return NONE;
		case "LIQUID(액체)":
			return LIQUID;
		case "WAX(밀랍)":
			return WAX;
		case "VEGGY(식물성)":
			return VEGGY;
		case "FLESH(동물성)":
			return FLESH;
		case "PAPER(종이)":
			return PAPER;
		case "CLOTH(천)":
			return CLOTH;
		case "LEATHER(가죽)":
			return LEATHER;
		case "WOOD(나무)":
			return WOOD;
		case "BONE(뼈)":
			return BONE;
		case "DRAGON_HIDE(용비늘)":
			return DRAGON_HIDE;
		case "IRON(철)":
			return IRON;
		case "METAL(금속)":
			return METAL;
		case "COPPER(구리)":
			return COPPER;
		case "SILVER(은)":
			return SILVER;
		case "GOLD(금)":
			return GOLD;
		case "PLATINUM(백금)":
			return PLATINUM;
		case "MITHRIL(미스릴)":
			return MITHRIL;
		case "PLASTIC(블랙미스릴)":
			return PLASTIC;
		case "GLASS(유리)":
			return GLASS;
		case "GEMSTONE(보석)":
			return GEMSTONE;
		case "MINERAL(광석)":
			return MINERAL;
		case "ORIHARUKON(오리하루콘)":
			return ORIHARUKON;
		case "DRANIUM(드라니움)":
			return DRANIUM;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments Material, %s", str));
		}
	}
	
	private static final List<Material> UNDEAD_MATERIALS = Arrays.asList(
		new Material[] {
			SILVER, MITHRIL, ORIHARUKON
		}
	);
	private static final List<Material> NOT_SAFE_ENCHANT_MATERIALS = Arrays.asList(
		new Material[] {
			BONE, PLASTIC, DRANIUM
		}
	);
	
	public static boolean isUndeadMaterial(Material material){
		return UNDEAD_MATERIALS.contains(material);
	}
	
	public static boolean isNotSafeEnchantMaterial(Material material){
		return NOT_SAFE_ENCHANT_MATERIALS.contains(material);
	}
}

