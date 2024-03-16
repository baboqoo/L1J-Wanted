package l1j.server.GameSystem.smelting;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.serverpackets.smelting.S_SynthesisSmeltingDesign;
import l1j.server.server.utils.FileUtil;

/**
 * 제련 시스템
 * @author LinOffice
 */
public class SmeltingLoader {
	private static Logger _log = Logger.getLogger(SmeltingLoader.class.getName());
	
	private static final ArrayList<S_SynthesisSmeltingDesign> SMELTING_DESIGN_PACKETS	= new ArrayList<>();
	
	/**
	 * 제련 정보 패킷 리스트
	 * @return ArrayList<S_Smelting>
	 */
	public static ArrayList<S_SynthesisSmeltingDesign> getSmeltingDesignList(){
    	return SMELTING_DESIGN_PACKETS;
    }
	
	private static SmeltingLoader _instance;
	public static SmeltingLoader getInstance(){
		if (_instance == null) {
			_instance = new SmeltingLoader();
		}
		return _instance;
	}
	
	private SmeltingLoader(){
		fileLoad();
	}
	
	void fileLoad(){
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
		    		byte[] buff = FileUtil.readFileBytes("./data/alchemy/smelting.dat");
		        	if (buff == null || buff.length == 0) {
		        		throw new IllegalArgumentException("WARNING ./data/alchemy/smelting.dat FILE NOT FOUND");
		        	}
		        	ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(buff.length);
		        	for (byte infoByte : buff) {
		        		queue.offer(binaryValue(infoByte));
		        	}
		        	SMELTING_DESIGN_PACKETS.addAll(S_SynthesisSmeltingDesign.getSmeltingPacketList(queue));        	
		    	} catch(Exception e) {
		    		_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		    	}
			}
		}, 2000L);
    }
	
	int binaryValue(byte infoByte){
    	String changeByte = Integer.toBinaryString(infoByte & 0xFF);
    	return Integer.parseInt(changeByte.toString(), 2);
    }
	
	public static void reload() {
		for (S_SynthesisSmeltingDesign pck : SMELTING_DESIGN_PACKETS) {
			pck.clear();
		}
		SMELTING_DESIGN_PACKETS.clear();
		getInstance().fileLoad();
	}
}

