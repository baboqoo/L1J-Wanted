package l1j.server.web.dispatcher.response.trade;

import java.util.concurrent.ConcurrentHashMap;

public enum TradeStatus {
	/*판매("0"),
	거래중("1"),
	거래완료("2"),
	거래취소("3");*/
	SELL("0"),
	IN_PROGRESS("1"),
	COMPLETED("2"),
	CANCELLED("3");	
	private String code;
	TradeStatus(String code) {
		this.code	= code;
	}
	public String getCode(){
		return code;
	}
	
	private static final ConcurrentHashMap<String, TradeStatus> CODE_DATA;
	private static final ConcurrentHashMap<String, TradeStatus> STATUS_DATA;
	static {
		CODE_DATA	= new ConcurrentHashMap<String, TradeStatus>();
		STATUS_DATA	= new ConcurrentHashMap<String, TradeStatus>();
		for (TradeStatus status : TradeStatus.values()) {
			CODE_DATA.put(status.code, status);
			STATUS_DATA.put(status.name(), status);
		}
	}
	
	public static TradeStatus getCode(String code){
		return CODE_DATA.get(code);
	}
	
	public static TradeStatus getStatus(String str){
		return STATUS_DATA.get(str);
	}
}

