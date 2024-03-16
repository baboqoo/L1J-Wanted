package l1j.server.web.dispatcher.response.support;

import java.sql.Timestamp;

import l1j.server.server.utils.StringUtil;

public class SupportVO {
	private int id;
	private String account_name;
	private String character_name;
	private int pay_amount;
	private Timestamp write_date;
	private SupportStatus status;
	
	public SupportVO(int id, String account_name, String character_name, int pay_amount, Timestamp write_date, SupportStatus status) {
		this.id				= id;
		this.account_name	= account_name;
		this.character_name	= character_name;
		this.pay_amount		= pay_amount;
		this.write_date		= write_date;
		this.status			= status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getCharacter_name() {
		return character_name;
	}
	public void setCharacter_name(String character_name) {
		this.character_name = character_name;
	}

	public int getPay_amount() {
		return pay_amount;
	}
	public void setPay_amount(int pay_amount) {
		this.pay_amount = pay_amount;
	}

	public Timestamp getWrite_date() {
		return write_date;
	}
	public void setWrite_date(Timestamp write_date) {
		this.write_date = write_date;
	}

	public SupportStatus getStatus() {
		return status;
	}
	public void setStatus(SupportStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID : ").append(id).append(StringUtil.LineString);
		sb.append("ACCOUNT : ").append(account_name).append(StringUtil.LineString);
		sb.append("CHARACTER : ").append(character_name).append(StringUtil.LineString);
		sb.append("PAY : ").append(pay_amount).append(StringUtil.LineString);
		sb.append("DATE : ").append(write_date).append(StringUtil.LineString);
		sb.append("STATUS : ").append(status.name()).append(StringUtil.LineString);
		return sb.toString();
	}
}

