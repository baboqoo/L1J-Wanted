package l1j.server.server.templates;

public class L1PledgeJoinningRequest {
	private int pledge_uid;
	private String pledge_name;
	private int user_uid;
	private String user_name;
	private String join_message;
	private int class_type;
	private int join_date;
	
	public L1PledgeJoinningRequest(int pledge_uid, String pledge_name, int user_uid, String user_name, String join_message, int class_type, int join_date) {
		this.pledge_uid		= pledge_uid;
		this.pledge_name	= pledge_name;
		this.user_uid		= user_uid;
		this.user_name		= user_name;
		this.join_message	= join_message;
		this.class_type		= class_type;
		this.join_date		= join_date;
	}

	public int getPledge_uid() {
		return pledge_uid;
	}

	public String getPledge_name() {
		return pledge_name;
	}

	public int getUser_uid() {
		return user_uid;
	}

	public String getUser_name() {
		return user_name;
	}

	public String getJoin_message() {
		return join_message;
	}

	public int getClass_type() {
		return class_type;
	}

	public int getJoin_date() {
		return join_date;
	}
	
}

