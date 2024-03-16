package l1j.server.server.templates;

import java.sql.ResultSet;
import java.sql.SQLException;

public class L1Command {
	private final String _name;
	private final int _level;
	private final String _executorClassName;

	public L1Command(ResultSet rs) throws SQLException {
		_name				= rs.getString("name");
		_level				= rs.getInt("access_level");
		_executorClassName	= rs.getString("class_name");
	}

	public String getName() {
		return _name;
	}

	public int getLevel() {
		return _level;
	}

	public String getExecutorClassName() {
		return _executorClassName;
	}
}

