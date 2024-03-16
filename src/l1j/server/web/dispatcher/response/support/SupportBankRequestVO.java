package l1j.server.web.dispatcher.response.support;

import java.sql.Timestamp;

public class SupportBankRequestVO {
	private int id;
	private String account_name;
	private String character_name;
	private Timestamp request_date;
	private String response;
	private Timestamp response_date;
	
	public SupportBankRequestVO(int id, 
			String account_name, String character_name, Timestamp request_date, 
			String response, Timestamp response_date) {
		this.id = id;
		this.account_name = account_name;
		this.character_name = character_name;
		this.request_date = request_date;
		this.response = response;
		this.response_date = response_date;
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

	public Timestamp getRequest_date() {
		return request_date;
	}

	public void setRequest_date(Timestamp request_date) {
		this.request_date = request_date;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Timestamp getResponse_date() {
		return response_date;
	}

	public void setResponse_date(Timestamp response_date) {
		this.response_date = response_date;
	}
}

