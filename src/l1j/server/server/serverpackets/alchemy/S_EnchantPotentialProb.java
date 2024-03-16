package l1j.server.server.serverpackets.alchemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.Config;
import l1j.server.common.bin.potential.CommonPotentialInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EnchantPotentialProb extends ServerBasePacket {
	private static final String S_ENCHANT_POTENTIAL_PROB = "[S] S_EnchantPotentialProb";
	private byte[] _byte = null;
    public static final int PROB	= 0x0a17;
    
    public S_EnchantPotentialProb(int dollGrade, int currentGrade, HashMap<Integer, ArrayList<CommonPotentialInfo.BonusInfoT>> prob_list){
    	write_init();
    	write_prob_list(dollGrade, currentGrade, prob_list);
        writeH(0x00);
    }
    
    void write_init() {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(PROB);
    }
    
    void write_prob_list(int dollGrade, int currentGrade, HashMap<Integer, ArrayList<CommonPotentialInfo.BonusInfoT>> prob_list) {
    	HashMap<Integer, Integer> probs = null;
    	switch (dollGrade) {
    	case 3:
    		probs = getRarityGradeProb(currentGrade);
    		break;
    	case 4:
    		probs = getHeroGradeProb(currentGrade);
    		break;
    	default:
    		probs = getLegendGradeProb(currentGrade);
    		break;
    	}
    	for (Map.Entry<Integer, ArrayList<CommonPotentialInfo.BonusInfoT>> entry : prob_list.entrySet()) {
        	ArrayList<CommonPotentialInfo.BonusInfoT> bonus_list = entry.getValue();
        	if (bonus_list == null) {
        		continue;
        	}
        	int prob = (int)((probs.get(entry.getKey()) * 10000000D) / bonus_list.size());// 단계별 확률
        	for (CommonPotentialInfo.BonusInfoT bonus : bonus_list) {
				int length = getBitSize(bonus.get_bonus_desc()) + getBitSize(prob) + 4;
				writeRaw(0x0a);
				writeBit(length);	// 길이
				
				writeRaw(0x08);// bonus name_id
				writeBit(bonus.get_bonus_desc());
	        
				writeRaw(0x10);// grade
				writeRaw(bonus.get_bonus_grade());
	        
				writeRaw(0x18);// prob
				writeBit(prob);
			}
        }
    	probs.clear();
        probs = null;
    }
    
    // 희귀
 	private static HashMap<Integer, Integer> getRarityGradeProb(int potentialGrade){
 		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
 		if (potentialGrade >= 3) {
 			result.put(3, 100);
 			return result;// 희귀 등급이상일때
 		}
 		if (potentialGrade >= 2) {// 고급 등급이상일때
 			result.put(2, Config.ALCHEMY.POTENTIAL_LEVEL3_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL3_STEP2_PROB);
 			result.put(3, Config.ALCHEMY.POTENTIAL_LEVEL3_STEP3_PROB);
 			return result;
 		} else {// 일반이하일때
 			result.put(1, Config.ALCHEMY.POTENTIAL_LEVEL3_STEP1_PROB);
 			result.put(2, Config.ALCHEMY.POTENTIAL_LEVEL3_STEP2_PROB);
 			result.put(3, Config.ALCHEMY.POTENTIAL_LEVEL3_STEP3_PROB);
 			return result;
 		}
 	}
 	
 	// 영웅
 	private static HashMap<Integer, Integer> getHeroGradeProb(int potentialGrade){
 		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
 		if (potentialGrade >= 4) {
 			result.put(4, 100);
 			return result;// 영웅등급이상일때
 		}
 		if (potentialGrade >= 3) {// 희귀등급이상일때
 			result.put(3, Config.ALCHEMY.POTENTIAL_LEVEL4_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP3_PROB);
 			result.put(4, Config.ALCHEMY.POTENTIAL_LEVEL4_STEP4_PROB);
 			return result;
 		} else if(potentialGrade >= 2) {// 고급등급이상일때
 			result.put(2, Config.ALCHEMY.POTENTIAL_LEVEL4_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP2_PROB);
 			result.put(3, Config.ALCHEMY.POTENTIAL_LEVEL4_STEP3_PROB);
 			result.put(4, Config.ALCHEMY.POTENTIAL_LEVEL4_STEP4_PROB);
 			return result;
 		} else {// 일반이하일때
 			result.put(1, Config.ALCHEMY.POTENTIAL_LEVEL4_STEP1_PROB);
 			result.put(2, Config.ALCHEMY.POTENTIAL_LEVEL4_STEP2_PROB);
 			result.put(3, Config.ALCHEMY.POTENTIAL_LEVEL4_STEP3_PROB);
 			result.put(4, Config.ALCHEMY.POTENTIAL_LEVEL4_STEP4_PROB);
 			return result;
 		}
 	}
 	
 	// 전설
 	private static HashMap<Integer, Integer> getLegendGradeProb(int potentialGrade){
 		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
 		if (potentialGrade >= 5) {
 			result.put(5, 100);
 			return result;// 전설등급이상일때
 		}
 		if (potentialGrade >= 4) {// 영웅등급이상일때
 			result.put(4, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP4_PROB);
 			result.put(5, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP5_PROB);
 			return result;
 		} else if (potentialGrade >= 3) {// 희귀등급이상일때
 			result.put(3, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB);
 			result.put(4, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP4_PROB);
 			result.put(5, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP5_PROB);
 			return result;
 		} else if (potentialGrade >= 2) {// 고급등급이상일때
 			result.put(2, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB);
 			result.put(3, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB);
 			result.put(4, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP4_PROB);
 			result.put(5, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP5_PROB);
 			return result;
 		} else {// 일반고급이하일때
 			result.put(1, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB);
 			result.put(2, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB);
 			result.put(3, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB);
 			result.put(4, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP4_PROB);
 			result.put(5, Config.ALCHEMY.POTENTIAL_LEVEL5_STEP5_PROB);
 			return result;
 		}
 	}

 	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
    
    @Override
	public String getType() {
		return S_ENCHANT_POTENTIAL_PROB;
	}
}
