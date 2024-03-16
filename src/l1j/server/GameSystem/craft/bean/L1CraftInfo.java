package l1j.server.GameSystem.craft.bean;

import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.common.bin.craft.Craft;

public class L1CraftInfo {
	private int craftId;
	private String desc;
	private int outputNameId;
	private int probability_million;
	private ArrayList<Integer> preserveNameIds;
	private HashMap<Integer, Integer> successPreserveCount, failurePreserveCount;
	private boolean is_success_count_type;
	private Craft bin;
	
	public L1CraftInfo(int craftId,
			String desc,
			int outputNameId,
			int probability_million,
			ArrayList<Integer> preserveNameIds,
			HashMap<Integer, Integer> successPreserveCount,
			HashMap<Integer, Integer> failurePreserveCount,
			boolean is_success_count_type,
			Craft bin) {
		this.craftId					= craftId;
		this.desc						= desc;	
		this.outputNameId				= outputNameId;
		this.probability_million		= probability_million;
		this.preserveNameIds			= preserveNameIds;
		this.successPreserveCount		= successPreserveCount;
		this.failurePreserveCount		= failurePreserveCount;
		this.is_success_count_type		= is_success_count_type;
		this.bin						= bin;
	}
	
	public int getCraftId() {
		return craftId;
	}
	public String getDesc() {
		return desc;
	}
	public int getOutputNameId() {
		return outputNameId;
	}
	public int getProbabilityMillion() {
		return probability_million;
	}
	public ArrayList<Integer> getPreserveNameIds() {
		return preserveNameIds;
	}
	public HashMap<Integer, Integer> getSuccessPreserveCount() {
		return successPreserveCount;
	}
	public HashMap<Integer, Integer> getFailurePreserveCount() {
		return failurePreserveCount;
	}
	public boolean is_success_count_type() {
		return is_success_count_type;
	}
	public Craft getBin() {
		return bin;
	}

	public void dispose(){
		if (successPreserveCount != null) {
			successPreserveCount.clear();
			successPreserveCount = null;
		}
		if (failurePreserveCount != null) {
			failurePreserveCount.clear();
			failurePreserveCount = null;
		}
		if (preserveNameIds != null) {
			preserveNameIds.clear();
			preserveNameIds = null;
		}
	}
}

