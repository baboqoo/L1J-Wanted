package l1j.server.server.model.npc;

import l1j.server.server.utils.StringUtil;

public class L1NpcHtml {
	private final String _name;
	private final String _args[];
	private boolean _success;

	public static final L1NpcHtml HTML_CLOSE = new L1NpcHtml(StringUtil.EmptyString);

	public L1NpcHtml(String name) {
		this(name, new String[] {});
	}

	public L1NpcHtml(String name, String... args) {
		if (name == null || args == null)
			throw new NullPointerException();
		_name = name;
		_args = args;
	}

	public String getName() {
		return _name;
	}

	public String[] getArgs() {
		return _args;
	}
	
	public boolean isSuccess() {
		return _success;
	}

	public void setSuccess(boolean success) {
		this._success = success;
	}
}

