package l1j.server.web.dispatcher.response.coupon;

import java.sql.Timestamp;

public class CouponVO {
	private String number;// 쿠폰번호
	private CouponType type;// 쿠폰 종류
	private int value;// 쿠폰 수치
	private boolean status;// true: Used, false: Not used
	private String useAccount;// 사용한 계정
	private Timestamp createTime;// 생성일
	private Timestamp useTime;// 사용일
	
	public CouponVO(String number, CouponType type, int value, boolean status, String useAccount, Timestamp createTime, Timestamp useTime) {
		this.number		= number;
		this.type		= type;
		this.value		= value;
		this.status		= status;
		this.useAccount	= useAccount;
		this.createTime	= createTime;
		this.useTime	= useTime;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public CouponType getType() {
		return type;
	}
	public void setType(CouponType type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int val) {
		this.value = val;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getUseAccount() {
		return useAccount;
	}
	public void setUseAccount(String useAccount) {
		this.useAccount = useAccount;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUseTime() {
		return useTime;
	}
	public void setUseTime(Timestamp useTime) {
		this.useTime = useTime;
	}
}

