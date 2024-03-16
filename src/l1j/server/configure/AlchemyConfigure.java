package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class AlchemyConfigure {
	private static final Logger _log = Logger.getLogger(AlchemyConfigure.class.getName());
	private static final String ALCHEMY_CONFIG_FILE	= "./config/alchemy.properties";
	
	public byte[] ALCHEMY_HASH;
	
	public List<Integer> ALCHEMY_USED_IDS				= new ArrayList<>();
	public List<Integer> ALCHEMY_SUCCESS_MENT_IDS		= new ArrayList<>();
	public List<Integer> ALCHEMY_MAIN_INPUT_IDS			= new ArrayList<>();
	public List<Integer> ALCHEMY_POTENTIAL_TRANS_IDS	= new ArrayList<>();
	
	public int ALCHEMY_1_PROB;
	public int ALCHEMY_2_PROB;
	public int ALCHEMY_3_PROB;
	public int ALCHEMY_4_PROB;
	public int ALCHEMY_15_PROB;
	public int ALCHEMY_16_PROB;
	public int ALCHEMY_DEFAULT_PROB;
	public int ALCHEMY_HYPER_PROB;
	
	public int POTENTIAL_LEVEL3_STEP1_PROB;
	public int POTENTIAL_LEVEL3_STEP2_PROB;
	public int POTENTIAL_LEVEL3_STEP3_PROB;
	
	public int POTENTIAL_LEVEL4_STEP1_PROB;
	public int POTENTIAL_LEVEL4_STEP2_PROB;
	public int POTENTIAL_LEVEL4_STEP3_PROB;
	public int POTENTIAL_LEVEL4_STEP4_PROB;
	
	public int POTENTIAL_LEVEL5_STEP1_PROB;
	public int POTENTIAL_LEVEL5_STEP2_PROB;
	public int POTENTIAL_LEVEL5_STEP3_PROB;
	public int POTENTIAL_LEVEL5_STEP4_PROB;
	public int POTENTIAL_LEVEL5_STEP5_PROB;
	
	public void load(){
		ALCHEMY_USED_IDS.clear();
		ALCHEMY_SUCCESS_MENT_IDS.clear();
		ALCHEMY_MAIN_INPUT_IDS.clear();
		ALCHEMY_POTENTIAL_TRANS_IDS.clear();
		try {
			Properties 	doll 				= new Properties();
			InputStream is					= new FileInputStream(new File(ALCHEMY_CONFIG_FILE));
			doll.load(is);
			is.close();
			
			String alchemyHash				= new String(doll.getProperty("ALCHEMY_HASH",
											"A6 98 14 E8 82 19 2A E1 CB D1 D6 44 F8 86 29 5D 34 F9 3E AA")
											.getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			if (StringUtil.isNullOrEmpty(alchemyHash)) {
				throw new Error("CONFIG_ALCHEMY_HASH_EMPTY");
			}
			String[] hashArray				= alchemyHash.split(StringUtil.EmptyOneString);
			ALCHEMY_HASH					= new byte[hashArray.length];
			for (int i=0; i<hashArray.length; i++) {
				ALCHEMY_HASH[i]				= (byte)(Integer.parseInt(hashArray[i].trim(), 16) & 0xFF);
			}
			
			String alchemyUsedIds			= new String(doll.getProperty("ALCHEMY_USED_IDS",
											"1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16")
											.getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			if (StringUtil.isNullOrEmpty(alchemyUsedIds)) {
				throw new Error("CONFIG_ALCHEMY_USED_IDS_EMPTY");
			}
			String[] usedArray				= alchemyUsedIds.split(StringUtil.CommaString);
			for (int i=0; i<usedArray.length; i++) {
				ALCHEMY_USED_IDS.add(Integer.parseInt(usedArray[i].trim()));
			}
			
			String alchemySuccessMentIds	= new String(doll.getProperty("ALCHEMY_SUCCESS_MENT_IDS",
											"4, 5, 6, 7, 8, 9, 10, 15, 16")
											.getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			if (!StringUtil.isNullOrEmpty(alchemySuccessMentIds)) {
				String[] mentArray			= alchemySuccessMentIds.split(StringUtil.CommaString);
				for (int i=0; i<mentArray.length; i++) {
					ALCHEMY_SUCCESS_MENT_IDS.add(Integer.parseInt(mentArray[i].trim()));
				}
			}
			
			String alchemyMainInputIds		= new String(doll.getProperty("ALCHEMY_MAIN_INPUT_IDS",
											"8, 9, 10, 11, 12, 13, 15, 16")
											.getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			if (!StringUtil.isNullOrEmpty(alchemyMainInputIds)) {
				String[] mainArray			= alchemyMainInputIds.split(StringUtil.CommaString);
				for (int i=0; i<mainArray.length; i++) {
					ALCHEMY_MAIN_INPUT_IDS.add(Integer.parseInt(mainArray[i].trim()));
				}
			}
			
			String alchemyPotentialTransIds	= new String(doll.getProperty("ALCHEMY_POTENTIAL_TRANS_IDS",
											"10, 11, 15, 16")
											.getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			if (!StringUtil.isNullOrEmpty(alchemyPotentialTransIds)) {
				String[] transArray			= alchemyPotentialTransIds.split(StringUtil.CommaString);
				for (int i=0; i<transArray.length; i++) {
					ALCHEMY_POTENTIAL_TRANS_IDS.add(Integer.parseInt(transArray[i].trim()));
				}
			}
			
			ALCHEMY_1_PROB					= Integer.parseInt(doll.getProperty("ALCHEMY_1_PROB", "10"));
			ALCHEMY_2_PROB					= Integer.parseInt(doll.getProperty("ALCHEMY_2_PROB", "10"));
			ALCHEMY_3_PROB					= Integer.parseInt(doll.getProperty("ALCHEMY_3_PROB", "10"));
			ALCHEMY_4_PROB					= Integer.parseInt(doll.getProperty("ALCHEMY_4_PROB", "10"));
			ALCHEMY_15_PROB					= Integer.parseInt(doll.getProperty("ALCHEMY_15_PROB", "10"));
			ALCHEMY_16_PROB					= Integer.parseInt(doll.getProperty("ALCHEMY_16_PROB", "10"));
			ALCHEMY_DEFAULT_PROB			= Integer.parseInt(doll.getProperty("ALCHEMY_DEFAULT_PROB", "10"));
			ALCHEMY_HYPER_PROB				= Integer.parseInt(doll.getProperty("ALCHEMY_HYPER_PROB", "1"));
			
			POTENTIAL_LEVEL3_STEP1_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL3_STEP1_PROB", "85"));
			POTENTIAL_LEVEL3_STEP2_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL3_STEP2_PROB", "10"));
			POTENTIAL_LEVEL3_STEP3_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL3_STEP3_PROB", "5"));
			
			POTENTIAL_LEVEL4_STEP1_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL4_STEP1_PROB", "70"));
			POTENTIAL_LEVEL4_STEP2_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL4_STEP2_PROB", "20"));
			POTENTIAL_LEVEL4_STEP3_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL4_STEP3_PROB", "7"));
			POTENTIAL_LEVEL4_STEP4_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL4_STEP4_PROB", "3"));
			
			POTENTIAL_LEVEL5_STEP1_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL5_STEP1_PROB", "63"));
			POTENTIAL_LEVEL5_STEP2_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL5_STEP2_PROB", "20"));
			POTENTIAL_LEVEL5_STEP3_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL5_STEP3_PROB", "10"));
			POTENTIAL_LEVEL5_STEP4_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL5_STEP4_PROB", "5"));
			POTENTIAL_LEVEL5_STEP5_PROB		= Integer.parseInt(doll.getProperty("POTENTIAL_LEVEL5_STEP5_PROB", "2"));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + ALCHEMY_CONFIG_FILE + " File.");
		}
	}
}

