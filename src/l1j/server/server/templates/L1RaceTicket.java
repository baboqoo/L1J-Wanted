package l1j.server.server.templates;

public class L1RaceTicket extends L1Item {
	private static final long serialVersionUID = 1L;
	
	public L1RaceTicket() {
	}

	private boolean _stackable;
	public boolean isStackable() {
		return _stackable;
	}

	public void set_stackable(boolean stackable) {
		_stackable = stackable;
	}

}

