package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class S_EnchantRanking extends ServerBasePacket {
	private static final String S_EnchantRanking = "[C] S_EnchantRanking";

 	private byte[] _byte = null;
    static String[] name;
    static String[] name1;
    static String[] castlename;
    static String[] clanname;
    static String[] leadername;
    static int[] enchantlvl;
    static int[] aden;
    static int[] armor;
    static int[] level;
    static int[] Ac;
    static int[] priaden;
    static int[] castleid;
    static int[] hascastle;
    static int[] taxrate;
    static int[] castleaden;
    static int[] MaxHp;
    static int[] MaxMp;
    
 	public S_EnchantRanking(L1PcInstance pc, int number) {
        name = new String[10];
        name1 = new String[10];
        enchantlvl = new int [10];
        aden = new int [10];
        armor = new int [10];
        level = new int [10];
        Ac = new int [10];
        priaden = new int [10];
        castlename = new String [10];
        clanname = new String [10];
        leadername = new String [10];
        castleid = new int [10];
        hascastle = new int [10];
        taxrate = new int [10];
        castleaden = new int [10];
        MaxHp = new int [10];
        MaxMp = new int [10];
  	    buildPacket(pc, number);
 	}

    private void buildPacket(L1PcInstance pc, int number) {
 		String date = time();
 		String title = null;
	    writeC(Opcodes.S_BOARD_READ);
	    writeD(number);
	    //writeS("운영자");
		writeS("Operator");
	    switch(number) {
  	    /*case 1:title = "인첸 랭킹";break;
  	    case 2:title = "방어구 랭킹";break;
  	    case 3:title = "아덴 랭킹";break;
  	    case 4:title = "레벨 랭킹";break;
  	    case 5:title = "신비깃털 랭킹";break;
  	    case 6:title = "창고아덴랭킹";break;
  	    case 7:title = "HP 랭킹";break;
  	    case 8:title = "MP 랭킹";break;*/
		  case 1:title = "Enchan Ranking";break;
		  case 2:title = "Armor Ranking";break;
		  case 3:title = "Aden Ranking";break;
		  case 4:title = "Level Ranking";break;
		  case 5:title = "Mystery Feather Ranking";break;
		  case 6:title = "Warehouse Arden Ranking";break;
		  case 7:title = "HP Ranking";break;
		  case 8:title = "MP Ranking";break;		
	    }
	    writeS(title);
	    writeS(date);

		Rank(pc, number);
        /*if (number == 1) { //추가부분입니다
	        writeS("\n\r" + "  1위 "+ StringUtil.PlusString + enchantlvl[0] + StringUtil.EmptyOneString + name[0] + "\n\r" + "  소유자 : " + name1[0] +"\n\r" +
	  	          "  2위 " + StringUtil.PlusString + enchantlvl[1] + StringUtil.EmptyOneString + name[1] +"\n\r" + "  소유자 : " + name1[1] + "\n\r" +
	  	          "  3위 " + StringUtil.PlusString + enchantlvl[2]  + StringUtil.EmptyOneString + name[2]+ "\n\r" + "  소유자 : " + name1[2] + "\n\r" +
	  	          "  4위 " + StringUtil.PlusString + enchantlvl[3] + StringUtil.EmptyOneString + name[3] + "\n\r" + "  소유자 : " + name1[3] + "\n\r" +
	  	          "  5위 " + StringUtil.PlusString + enchantlvl[4] + StringUtil.EmptyOneString + name[4] + "\n\r" + "  소유자 : " + name1[4] + "\n\r" +
	  	          "  6위 " + StringUtil.PlusString + enchantlvl[5] + StringUtil.EmptyOneString + name[5] + "\n\r" + "  소유자 : " + name1[5] + "\n\r" +
	  	          "  7위 " + StringUtil.PlusString + enchantlvl[6] + StringUtil.EmptyOneString + name[6] + "\n\r" + "  소유자 : " + name1[6] +"\n\r" +
	  	          "  8위 " + StringUtil.PlusString + enchantlvl[7] + StringUtil.EmptyOneString + name[7] + "\n\r" + "  소유자 : " + name1[7] +"\n\r" +
	  	          "  9위 " + StringUtil.PlusString + enchantlvl[8] + StringUtil.EmptyOneString + name[8] + "\n\r" + "  소유자 : " + name1[8] +"\n\r"+
	  	          " 10위 " + StringUtil.PlusString + enchantlvl[9] + StringUtil.EmptyOneString + name[9] + "\n\r" + "  소유자 : " + name1[9] +"\n\r" +
	  	                      "      ");
        } else if (number == 2) { //추가부분입니다
	        writeS("\n\r" + "  1위 "+ StringUtil.PlusString + armor[0] + StringUtil.EmptyOneString + name[0] + "\n\r" + "  소유자 : " + name1[0] +"\n\r" +
	  	          "  2위 " + StringUtil.PlusString + armor[1] + StringUtil.EmptyOneString + name[1] +"\n\r" + "  소유자 : " + name1[1] + "\n\r" +
	  	          "  3위 " + StringUtil.PlusString + armor[2]  + StringUtil.EmptyOneString + name[2]+ "\n\r" + "  소유자 : " + name1[2] + "\n\r" +
	  	          "  4위 " + StringUtil.PlusString + armor[3] + StringUtil.EmptyOneString + name[3] + "\n\r" + "  소유자 : " + name1[3] + "\n\r" +
	  	          "  5위 " + StringUtil.PlusString + armor[4] + StringUtil.EmptyOneString + name[4] + "\n\r" + "  소유자 : " + name1[4] + "\n\r" +
	  	          "  6위 " + StringUtil.PlusString + armor[5] + StringUtil.EmptyOneString + name[5] + "\n\r" + "  소유자 : " + name1[5] + "\n\r" +
	  	          "  7위 " + StringUtil.PlusString + armor[6] + StringUtil.EmptyOneString + name[6] + "\n\r" + "  소유자 : " + name1[6] +"\n\r" +
	  	          "  8위 " + StringUtil.PlusString + armor[7] + StringUtil.EmptyOneString + name[7] + "\n\r" + "  소유자 : " + name1[7] +"\n\r" +
	  	          "  9위 " + StringUtil.PlusString + armor[8] + StringUtil.EmptyOneString + name[8] + "\n\r" + "  소유자 : " + name1[8] +"\n\r"+
	  	          " 10위 " + StringUtil.PlusString + armor[9] + StringUtil.EmptyOneString + name[9] + "\n\r" + "  소유자 : " + name1[9] +"\n\r" +
	  	                      "      ");
	    } else if (number == 3) { //추가부분입니다
	        writeS("\n\r" + "  1위 "+ "$ " + aden[0] + " 아데나\n\r" + "  소유자 : " + name[0] + "\n\r" +
	  	          "  2위 " + "$ " + aden[1] + " 아데나\n\r" + "  소유자 : " + name[1] + "\n\r" +
	  	          "  3위 " + "$ " + aden[2] + " 아데나\n\r" + "  소유자 : " + name[2] + "\n\r" +
	  	          "  4위 " + "$ " + aden[3] + " 아데나\n\r" + "  소유자 : " + name[3] + "\n\r" +
	  	          "  5위 " + "$ " + aden[4] + " 아데나\n\r" + "  소유자 : " + name[4] + "\n\r" +
	  	          "  6위 " + "$ " + aden[5] + " 아데나\n\r" + "  소유자 : " + name[5] + "\n\r" +
	  	          "  7위 " + "$ " + aden[6] + " 아데나\n\r" + "  소유자 : " + name[6] + "\n\r" +
	  	          "  8위 " + "$ " + aden[7] + " 아데나\n\r" + "  소유자 : " + name[7] + "\n\r" +
	  	          "  9위 " + "$ " + aden[8] + " 아데나\n\r" + "  소유자 : " + name[8] + "\n\r" +
	  	          " 10위 " + "$ " + aden[9] + " 아데나\n\r" + "  소유자 : " + name[9] + "\n\r" +
	  	                      "      ");
	    } else if (number == 4) { //추가부분입니다
	        writeS("\n\r" + "  1위 " + name[0] + " \n\r" + "  현재레벨 : " + level[0] + "\n\r" +
	  	          "  2위 " + name[1] + " \n\r" + "  현재레벨 : " + level[1] + "\n\r" +
	  	          "  3위 " + name[2] + " \n\r" + "  현재레벨 : " + level[2] + "\n\r" +
	  	          "  4위 " + name[3] + " \n\r" + "  현재레벨 : " + level[3] + "\n\r" +
	  	          "  5위 " + name[4] + " \n\r" + "  현재레벨 : " + level[4] + "\n\r" +
	  	          "  6위 " + name[5] + " \n\r" + "  현재레벨 : " + level[5] + "\n\r" +
	  	          "  7위 " + name[6] + " \n\r" + "  현재레벨 : " + level[6] + "\n\r" +
	  	          "  8위 " + name[7] + " \n\r" + "  현재레벨 : " + level[7] + "\n\r" +
	  	          "  9위 " + name[8] + " \n\r" + "  현재레벨 : " + level[8] + "\n\r" +
	  	          " 10위 " + name[9] + " \n\r" + "  현재레벨 : " + level[9] + "\n\r" +
	                      "      ");
	    } else if (number == 5) { //추가부분입니다
	        writeS("\n\r" + "  1위 "+ priaden[0] + "개의 깃털\n\r" + "  소유자 : " + name[0] + "\n\r" +
	  	          "  2위 " + priaden[1] + "개의 깃털\n\r" + "  소유자 : " + name[1] + "\n\r" +
	  	          "  3위 " + priaden[2] + "개의 깃털\n\r" + "  소유자 : " + name[2] + "\n\r" +
	  	          "  4위 " + priaden[3] + "개의 깃털\n\r" + "  소유자 : " + name[3] + "\n\r" +
	  	          "  5위 " + priaden[4] + "개의 깃털\n\r" + "  소유자 : " + name[4] + "\n\r" +
	  	          "  6위 " + priaden[5] + "개의 깃털\n\r" + "  소유자 : " + name[5] + "\n\r" +
	  	          "  7위 " + priaden[6] + "개의 깃털\n\r" + "  소유자 : " + name[6] + "\n\r" +
	  	          "  8위 " + priaden[7] + "개의 깃털\n\r" + "  소유자 : " + name[7] + "\n\r" +
	  	          "  9위 " + priaden[8] + "개의 깃털\n\r" + "  소유자 : " + name[8] + "\n\r" +
	  	          " 10위 " + priaden[9] + "개의 깃털\n\r" + "  소유자 : " + name[9] + "\n\r" +
	  	                      "      ");
	    } else if (number == 6) { //추가부분입니다
	        writeS("\n\r" + "  1위 $ : "+ priaden[0] + " 아데나\n\r" + "  계정명 : " + name[0] + "\n\r" +
	  	          "  2위 $ : " + priaden[1] + " 아데나\n\r" + "  계정명 : " + name[1] + "\n\r" +
	  	          "  3위 $ : " + priaden[2] + " 아데나\n\r" + "  계정명 : " + name[2] + "\n\r" +
	  	          "  4위 $ : " + priaden[3] + " 아데나\n\r" + "  계정명 : " + name[3] + "\n\r" +
	  	          "  5위 $ : " + priaden[4] + " 아데나\n\r" + "  계정명 : " + name[4] + "\n\r" +
	  	          "  6위 $ : " + priaden[5] + " 아데나\n\r" + "  계정명 : " + name[5] + "\n\r" +
	  	          "  7위 $ : " + priaden[6] + " 아데나\n\r" + "  계정명 : " + name[6] + "\n\r" +
	  	          "  8위 $ : " + priaden[7] + " 아데나\n\r" + "  계정명 : " + name[7] + "\n\r" +
	  	          "  9위 $ : " + priaden[8] + " 아데나\n\r" + "  계정명 : " + name[8] + "\n\r" +
	  	          " 10위 $ : " + priaden[9] + " 아데나\n\r" + "  계정명 : " + name[9] + "\n\r" +
	  	                      "      ");
	    } else if (number == 7) { //추가부분입니다
		     writeS("\n\r" + "  1위. " + name[0]+StringUtil.EmptyOneString + MaxHp[0] + "\n\r" +
		       "  2위. " + name[1]+StringUtil.EmptyOneString + MaxHp[1] + "\n\r" +
		       "  3위. " + name[2]+StringUtil.EmptyOneString + MaxHp[2] + "\n\r" +
		       "  4위. " + name[3]+StringUtil.EmptyOneString + MaxHp[3] + "\n\r" +
		       "  5위. " + name[4]+StringUtil.EmptyOneString + MaxHp[4] + "\n\r" +
		       "  6위. " + name[5]+StringUtil.EmptyOneString + MaxHp[5] + "\n\r" +
		       "  7위. " + name[6]+StringUtil.EmptyOneString + MaxHp[6] + "\n\r" +
		       "  8위. " + name[7]+StringUtil.EmptyOneString + MaxHp[7] + "\n\r" +
		       "  9위. " + name[8]+StringUtil.EmptyOneString + MaxHp[8] + "\n\r" +
		       " 10위. " + name[9]+StringUtil.EmptyOneString + MaxHp[9] + "\n\r" +
		     "      ");
		} else if (number == 8) { //추가부분입니다
		     writeS("\n\r" + "  1위. " + name[0]+StringUtil.EmptyOneString + MaxMp[0] + "\n\r" +
		       "  2위. " + name[1]+StringUtil.EmptyOneString + MaxMp[1] + "\n\r" +
		       "  3위. " + name[2]+StringUtil.EmptyOneString + MaxMp[2] + "\n\r" +
		       "  4위. " + name[3]+StringUtil.EmptyOneString + MaxMp[3] + "\n\r" +
		       "  5위. " + name[4]+StringUtil.EmptyOneString + MaxMp[4] + "\n\r" +
		       "  6위. " + name[5]+StringUtil.EmptyOneString + MaxMp[5] + "\n\r" +
		       "  7위. " + name[6]+StringUtil.EmptyOneString + MaxMp[6] + "\n\r" +
		       "  8위. " + name[7]+StringUtil.EmptyOneString + MaxMp[7] + "\n\r" +
		       "  9위. " + name[8]+StringUtil.EmptyOneString + MaxMp[8] + "\n\r" +
		       " 10위. " + name[9]+StringUtil.EmptyOneString + MaxMp[9] + "\n\r" +
		     "      ");
        }*/
		if (number == 1) { //This is an additional part
			writeS("\n\r" + " 1st place "+ StringUtil.PlusString + enchantlvl[0] + StringUtil.EmptyOneString + name[0] + "\n\r" + " Owner: " + name1[0] +" \n\r" +
			" 2nd place " + StringUtil.PlusString + enchantlvl[1] + StringUtil.EmptyOneString + name[1] +"\n\r" + " Owner: " + name1[1] + "\n\r" +
			" 3rd place " + StringUtil.PlusString + enchantlvl[2] + StringUtil.EmptyOneString + name[2]+ "\n\r" + " Owner: " + name1[2] + "\n\r" +
			" 4th place " + StringUtil.PlusString + enchantlvl[3] + StringUtil.EmptyOneString + name[3] + "\n\r" + " Owner: " + name1[3] + "\n\r" +
			" 5th place " + StringUtil.PlusString + enchantlvl[4] + StringUtil.EmptyOneString + name[4] + "\n\r" + " Owner: " + name1[4] + "\n\r" +
			" 6th place " + StringUtil.PlusString + enchantlvl[5] + StringUtil.EmptyOneString + name[5] + "\n\r" + " Owner: " + name1[5] + "\n\r" +
			" 7th " + StringUtil.PlusString + enchantlvl[6] + StringUtil.EmptyOneString + name[6] + "\n\r" + " Owner: " + name1[6] +"\n\r" +
			" 8th place " + StringUtil.PlusString + enchantlvl[7] + StringUtil.EmptyOneString + name[7] + "\n\r" + " Owner: " + name1[7] +"\n\r" +
			" 9th place " + StringUtil.PlusString + enchantlvl[8] + StringUtil.EmptyOneString + name[8] + "\n\r" + " Owner: " + name1[8] +"\n\r"+
			" 10th " + StringUtil.PlusString + enchantlvl[9] + StringUtil.EmptyOneString + name[9] + "\n\r" + " Owner: " + name1[9] +"\n\r" +
			"      ");
		 } else if (number == 2) { //This is an additional part
			writeS("\n\r" + " 1st place "+ StringUtil.PlusString + armor[0] + StringUtil.EmptyOneString + name[0] + "\n\r" + " Owner: " + name1[0] +" \n\r" +
			" 2nd place " + StringUtil.PlusString + armor[1] + StringUtil.EmptyOneString + name[1] +"\n\r" + " Owner: " + name1[1] + "\n\r" +
			" 3rd place " + StringUtil.PlusString + armor[2] + StringUtil.EmptyOneString + name[2]+ "\n\r" + " Owner: " + name1[2] + "\n\r" +
			" 4th place " + StringUtil.PlusString + armor[3] + StringUtil.EmptyOneString + name[3] + "\n\r" + " Owner: " + name1[3] + "\n\r" +
			" 5th place " + StringUtil.PlusString + armor[4] + StringUtil.EmptyOneString + name[4] + "\n\r" + " Owner: " + name1[4] + "\n\r" +
			" 6th place " + StringUtil.PlusString + armor[5] + StringUtil.EmptyOneString + name[5] + "\n\r" + " Owner: " + name1[5] + "\n\r" +
			" 7th place " + StringUtil.PlusString + armor[6] + StringUtil.EmptyOneString + name[6] + "\n\r" + " Owner: " + name1[6] +"\n\r" +
			" 8th place " + StringUtil.PlusString + armor[7] + StringUtil.EmptyOneString + name[7] + "\n\r" + " Owner: " + name1[7] +"\n\r" +
			" 9th place " + StringUtil.PlusString + armor[8] + StringUtil.EmptyOneString + name[8] + "\n\r" + " Owner: " + name1[8] +"\n\r"+
			" 10th " + StringUtil.PlusString + armor[9] + StringUtil.EmptyOneString + name[9] + "\n\r" + " Owner: " + name1[9] +"\n\r" +
			"      ");
		} else if (number == 3) { //This is an additional part
			writeS("\n\r" + " 1st place "+ "$ " + aden[0] + " Aden\n\r" + " Owner: " + name[0] + "\n\r" +
			" 2nd place " + "$ " + aden[1] + " Adena\n\r" + " Owner: " + name[1] + "\n\r" +
			" 3rd place " + "$ " + aden[2] + " Adena\n\r" + " Owner: " + name[2] + "\n\r" +
			" 4th place " + "$ " + aden[3] + " Adena\n\r" + " Owner: " + name[3] + "\n\r" +
			" 5th place " + "$ " + aden[4] + " Adena\n\r" + " Owner: " + name[4] + "\n\r" +
			" 6th place " + "$ " + aden[5] + " Adena\n\r" + " Owner: " + name[5] + "\n\r" +
			" 7th place " + "$ " + aden[6] + " Adena\n\r" + " Owner: " + name[6] + "\n\r" +
			" 8th place " + "$ " + aden[7] + " Adena\n\r" + " Owner: " + name[7] + "\n\r" +
			" 9th place " + "$ " + aden[8] + " Adena\n\r" + " Owner: " + name[8] + "\n\r" +
			" 10th place " + "$ " + aden[9] + " Adena\n\r" + " Owner: " + name[9] + "\n\r" +
			"      ");
		} else if (number == 4) { //This is an additional part
			writeS("\n\r" + " 1st place " + name[0] + " \n\r" + " Current level: " + level[0] + "\n\r" +
			" 2nd place " + name[1] + " \n\r" + " Current level: " + level[1] + "\n\r" +
			" 3rd place " + name[2] + " \n\r" + " Current level: " + level[2] + "\n\r" +
			" 4th place " + name[3] + " \n\r" + " Current level: " + level[3] + "\n\r" +
			" 5th place " + name[4] + " \n\r" + " Current level: " + level[4] + "\n\r" +
			" 6th place " + name[5] + " \n\r" + " Current level: " + level[5] + "\n\r" +
			" 7th place " + name[6] + " \n\r" + " Current level: " + level[6] + "\n\r" +
			" 8th place " + name[7] + " \n\r" + " Current level: " + level[7] + "\n\r" +
			" 9th place " + name[8] + " \n\r" + " Current level: " + level[8] + "\n\r" +
			" 10th place " + name[9] + " \n\r" + " Current level: " + level[9] + "\n\r" +
			"      ");
		} else if (number == 5) { //This is an additional part
			writeS("\n\r" + " 1st place "+ priaden[0] + "Dog Feather\n\r" + " Owner: " + name[0] + "\n\r" +
			" 2nd place " + priaden[1] + "Dog Feather\n\r" + " Owner: " + name[1] + "\n\r" +
			" 3rd place " + priaden[2] + "Dog Feather\n\r" + " Owner: " + name[2] + "\n\r" +
			" 4th place " + priaden[3] + "Dog Feather\n\r" + " Owner: " + name[3] + "\n\r" +
			" 5th place " + priaden[4] + "Dog Feather\n\r" + " Owner: " + name[4] + "\n\r" +
			" 6th place " + priaden[5] + "Dog Feather\n\r" + " Owner: " + name[5] + "\n\r" +
			" 7th place " + priaden[6] + "Dog Feather\n\r" + " Owner: " + name[6] + "\n\r" +
			" 8th place " + priaden[7] + "Dog Feather\n\r" + " Owner: " + name[7] + "\n\r" +
			" 9th place " + priaden[8] + "Dog Feather\n\r" + " Owner: " + name[8] + "\n\r" +
			" 10th place " + priaden[9] + "Dog Feather\n\r" + " Owner: " + name[9] + "\n\r" +
			"      ");
		} else if (number == 6) { //This is an additional part
			writeS("\n\r" + " 1st place $ : "+ priaden[0] + " Adena\n\r" + " Account name: " + name[0] + "\n\r" +
			" 2nd place $ : " + priaden[1] + " Adena\n\r" + " Account name: " + name[1] + "\n\r" +
			" 3rd place $ : " + priaden[2] + " Adena\n\r" + " Account name: " + name[2] + "\n\r" +
			" 4th place $ : " + priaden[3] + " Adena\n\r" + " Account name: " + name[3] + "\n\r" +
			" 5th place $ : " + priaden[4] + " Adena\n\r" + " Account name: " + name[4] + "\n\r" +
			" 6th place $ : " + priaden[5] + " Adena\n\r" + " Account name: " + name[5] + "\n\r" +
			" 7th place $ : " + priaden[6] + " Adena\n\r" + " Account name: " + name[6] + "\n\r" +
			" 8th place $ : " + priaden[7] + " Adena\n\r" + " Account name: " + name[7] + "\n\r" +
			" 9th place $ : " + priaden[8] + " Adena\n\r" + " Account name: " + name[8] + "\n\r" +
			" 10th place $ : " + priaden[9] + " Adena\n\r" + " Account name: " + name[9] + "\n\r" +
			"      ");
		} else if (number == 7) { //This is an additional part
			writeS("\n\r" + " 1st place. " + name[0]+StringUtil.EmptyOneString + MaxHp[0] + "\n\r" +
			" 2nd place. " + name[1]+StringUtil.EmptyOneString + MaxHp[1] + "\n\r" +
			" 3rd place. " + name[2]+StringUtil.EmptyOneString + MaxHp[2] + "\n\r" +
			" 4th place. " + name[3]+StringUtil.EmptyOneString + MaxHp[3] + "\n\r" +
			" 5th place. " + name[4]+StringUtil.EmptyOneString + MaxHp[4] + "\n\r" +
			" 6th place. " + name[5]+StringUtil.EmptyOneString + MaxHp[5] + "\n\r" +
			" 7th place. " + name[6]+StringUtil.EmptyOneString + MaxHp[6] + "\n\r" +
			" 8th place. " + name[7]+StringUtil.EmptyOneString + MaxHp[7] + "\n\r" +
			" 9th place. " + name[8]+StringUtil.EmptyOneString + MaxHp[8] + "\n\r" +
			" 10th place. " + name[9]+StringUtil.EmptyOneString + MaxHp[9] + "\n\r" +
			"      ");
		} else if (number == 8) { //This is an additional part
			writeS("\n\r" + " 1st place. " + name[0]+StringUtil.EmptyOneString + MaxMp[0] + "\n\r" +
			" 2nd place. " + name[1]+StringUtil.EmptyOneString + MaxMp[1] + "\n\r" +
			" 3rd place. " + name[2]+StringUtil.EmptyOneString + MaxMp[2] + "\n\r" +
			" 4th place. " + name[3]+StringUtil.EmptyOneString + MaxMp[3] + "\n\r" +
			" 5th place. " + name[4]+StringUtil.EmptyOneString + MaxMp[4] + "\n\r" +
			" 6th place. " + name[5]+StringUtil.EmptyOneString + MaxMp[5] + "\n\r" +
			" 7th place. " + name[6]+StringUtil.EmptyOneString + MaxMp[6] + "\n\r" +
			" 8th place. " + name[7]+StringUtil.EmptyOneString + MaxMp[7] + "\n\r" +
			" 9th place. " + name[8]+StringUtil.EmptyOneString + MaxMp[8] + "\n\r" +
			" 10th place. " + name[9]+StringUtil.EmptyOneString + MaxMp[9] + "\n\r" +
			"      ");
		}
    }

	private int Rank(L1PcInstance pc, int number) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int i = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			switch(number) {
		    case 1: //추가부분입니다
		        pstm = con.prepareStatement(
		        		"SELECT enchantlvl, weapon.name, characters.char_name  "
		        		+ "FROM character_items, weapon, characters "
		        		+ "WHERE character_items.item_id in(SELECT item_id FROM weapon) "
		        		+ "And character_items.char_id in(SELECT objid FROM characters where AccessLevel = 0) "
		        		+ "And character_items.item_id=weapon.item_id "
		        		+ "And character_items.char_id=characters.objid "
		        		+ "And count = 1 order by character_items.enchantlvl desc limit 10");
		        break;		
		    case 2: //추가부분입니다
		        pstm = con.prepareStatement(
		        		"SELECT enchantlvl, armor.name, characters.char_name  "
		        		+ "FROM character_items, armor, characters "
		        		+ "WHERE character_items.item_id in(SELECT item_id FROM armor) "
		        		+ "And character_items.char_id in(SELECT objid FROM characters where AccessLevel = 0) "
		        		+ "And character_items.item_id=armor.item_id "
		        		+ "And character_items.char_id=characters.objid "
		        		+ "And count = 1 order by character_items.enchantlvl desc limit 10");
		        break;	
		    case 3:
		    	pstm = con.prepareStatement(
		    			"SELECT count, characters.char_name "
		    			+ "FROM character_items, characters "
		    			+ "WHERE item_id in(SELECT item_id FROM etcitem) "
		    			+ "And char_id in(SELECT objid FROM characters where AccessLevel = 0) "
		    			+ "And character_items.char_id=characters.objid "
		    			+ "And item_id = 40308 order by count desc limit 10");
		        break;
		    case 4: //추가부분입니다
		    	if(pc.isGm()){
		    	pstm = con.prepareStatement(
		    			"SELECT level, char_name FROM characters WHERE AccessLevel = 0 order by level desc limit 10");
		    	} else {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("해당 게시판은 운영자만 사용가능합니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1156), true), true);
				}
		    	break;		
		    case 5:
		    	pstm = con.prepareStatement(
		    			"SELECT count, characters.char_name "
		    			+ "FROM character_items, characters "
		    			+ "WHERE item_id in(SELECT item_id FROM etcitem) "
		    			+ "And char_id in(SELECT objid FROM characters where AccessLevel = 0) "
		    			+ "And character_items.char_id=characters.objid "
		    			+ "And item_id = 41159 order by count desc limit 10");
		        break;
		    case 6:  
		    	if(pc.isGm()){
		    	pstm = con.prepareStatement(
		    			"SELECT count, accounts.login "
		    			+ "FROM character_warehouse, accounts "
		    			+ "WHERE  login in(SELECT login FROM accounts where access_level = 0) "
		    			+ "And character_warehouse.account_name =accounts.login "
		    			+ "And item_id = 40308 order by count desc limit 10");
		    	} else {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("해당 게시판은 운영자만 사용가능합니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1156), true), true);
				}
		    	break;
		    case 7: //추가부분입니다
		        pstm = con.prepareStatement("SELECT MaxHp, char_name FROM characters WHERE AccessLevel = 0 order by MaxHp desc limit 10");
		        break;
		    case 8: //추가부분입니다
		    	pstm = con.prepareStatement("SELECT MaxMp, char_name FROM characters WHERE AccessLevel = 0 order by MaxMp desc limit 10");
		    	break;
		    default:
		    	pstm = con.prepareStatement("SELECT char_name FROM characters WHERE AccessLevel = 0 order by Exp desc limit 10");
		    	break;
			}

			rs = pstm.executeQuery();
			if (number == 1) { // 추가부분입니다
				while (rs.next()) {
					enchantlvl[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					name1[i] = rs.getString(3);
					i++;
				}
			} else if (number == 2) { // 추가부분입니다
				while (rs.next()) {
					armor[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					name1[i] = rs.getString(3);
					i++;
				}
			} else if (number == 3) { // 추가부분입니다
				while (rs.next()) {
					aden[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 4) { // 추가부분입니다
				while (rs.next()) {
					level[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 5) { // 추가부분입니다
				while (rs.next()) {
					priaden[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 6) { // 추가부분입니다
				while (rs.next()) {
					priaden[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 7) { //추가부분입니다
			    while(rs.next()){
			    	MaxHp[i] = rs.getInt(1);
			    	name[i] = rs.getString(2);
			    	i++;
			    }
			} else if (number == 8) { //추가부분입니다
			    while(rs.next()){
			    	MaxMp[i] = rs.getInt(1);
			    	name[i] = rs.getString(2);
			    	i++;
			    }

			} else {
				while (rs.next()) {
					name[i] = rs.getString(1);
					i++;
				}
				// 레코드가 없거나 5보다 작을때
				while (i < 10) {
					//name[i] = "없음.";
					name[i] = "None.";
					i++;
				}
			}
		} catch (SQLException e) {
		//	_log.log(Level.SEVERE, "S_EnchantRanking[]Error", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return i;
	}

	private static String time() {
		TimeZone tz = TimeZone.getTimeZone(Config.SERVER.TIME_ZONE);
		Calendar cal = Calendar.getInstance(tz);
		int year = cal.get(Calendar.YEAR) - 2000;
		String year2;
		if (year < 10)
			year2 = StringUtil.ZeroString + year;
		else
			year2 = Integer.toString(year);
		
		int Month = cal.get(Calendar.MONTH) + 1;
		String Month2 = null;
		if (Month < 10)
			Month2 = StringUtil.ZeroString + Month;
		else
			Month2 = Integer.toString(Month);
		
		int date = cal.get(Calendar.DATE);
		String date2 = null;
		if (date < 10)
			date2 = StringUtil.ZeroString + date;
		else
			date2 = Integer.toString(date);
		return year2 + StringUtil.SlushString + Month2 + StringUtil.SlushString + date2;
	}

	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = getBytes();
		return _byte;
	}

	public String getType() {
		return S_EnchantRanking;
	}

}


