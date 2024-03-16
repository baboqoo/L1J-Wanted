package l1j.server.server.templates;

public class L1DogFightTicket extends L1Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1DogFightTicket() {
	}

	private boolean _merge;

	@Override
	public boolean isMerge() {
		return _merge;
	}

	public void setMerge(boolean val) {
		_merge = val;
	}

}

