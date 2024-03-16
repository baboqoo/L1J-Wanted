package l1j.server.web.dispatcher.response.engine;

import java.sql.Timestamp;

public class EngineLogVO {
	private String account;
	private String engine;
	private Timestamp time;
	
	public EngineLogVO(String account, String engine, Timestamp time) {
		this.account	= account;
		this.engine		= engine;
		this.time		= time;
	}
	
	public String getAccount() {
		return account;
	}
	public String getEngine() {
		return engine;
	}
	public Timestamp getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("[time]").append(time.toString())
				.append(", [account]").append(account)
				.append(", [engine]").append(engine)
				.toString();
	}
}

