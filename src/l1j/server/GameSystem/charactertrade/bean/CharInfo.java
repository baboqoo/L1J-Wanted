package l1j.server.GameSystem.charactertrade.bean;

import l1j.server.common.data.Gender;
import l1j.server.server.utils.StringUtil;

public class CharInfo {
	public String 		name;
	public int			level;
	public int			type;
	public Gender		gender;
	public String		clanName;
	public int 			str;
	public int 			dex;
	public int 			con;
	public int 			wis;
	public int 			intel;
	public int 			cha;
	public int			elixir;
	public int			hp;
	public int			mp;
	public int			ac;
	public int			einstate;
	
	@Override
	public String toString(){
		return "[Level : " + level + "]                                                     "
				+ "[Class :" + toMoreClass() + "]                                                     "
				+ "[Pledge: " + clanName + "]                                                     "
				+ "[STR : " + str + "]                                                     "
				+ "[DEX : " + dex + "]                                                     "
				+ "[CON : " + con + "]                                                     "
				+ "[WIS : " + wis + "]                                                     "
				+ "[INT : " + intel + "]                                                     "
				+ "[CHA : " + cha + "]                                                     "
				+ "[Elixir : " + elixir + "]                                                     "
				+ "[HP : " + hp + "]                                                     "
				+ "[MP : " + mp + "]                                                     "
				+ "[AC : " + String.valueOf(ac) + "]                                                     "
				+ "[EinState : " + einstate + "]                                                     ";
	}
	
	private String toMoreClass(){
		//return String.format("%s(%s)", toClass(), gender == Gender.MALE ? "남" : "여");
		return String.format("%s (%s)", toClass(), gender == Gender.MALE ? "$28886" : "$28887");		
	}
	
	private String toClass(){
		switch(type){
		case 0:return "$27711"; //"군주" monarch;
		case 1:return "$27712"; //"기사" knight;
		case 2:return "$27713"; //"요정" elf;
		case 3:return "$27714"; //"법사" mage;
		case 4:return "$27715"; //"다크엘프" dark elf;
		case 5:return "$27717"; //"용기사" dragon knight;
		case 6:return "$27718"; //"환술사" illusionist;
		case 7:return "$27719"; //"전사" warrior;
		case 8:return "$31047"; //"검사" fencer;
		case 9:return "$34085"; //"창기사" lancer;
		default:return StringUtil.EmptyString;
		}
	}
}

