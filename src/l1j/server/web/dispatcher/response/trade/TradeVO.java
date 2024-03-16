package l1j.server.web.dispatcher.response.trade;

import java.sql.Timestamp;

public class TradeVO {
	private int id;// 번호
	private String title;// 제목
	private String content;// 내용
	private String bank;// 은행
	private String bankNumber;// 계좌번호
	private TradeStatus status;// 상태
	private String sellerName;// 판매자
	private String sellerCharacter;// 판매자 케릭터
	private String sellerPhone;// 판매자 연락처
	private String buyerName;// 구매자
	private String buyerCharacter;// 구매자 케릭터
	private String buyerPhone;// 구매자 연락처
	private Timestamp writeTime;// 작성시간
	private boolean send;// 인계
	private boolean receive;// 인수
	private Timestamp completeTime;// 완료시간
	private boolean sellerCancle;// 판매자 거래취소여부
	private boolean buyerCancle;// 구매자 거래취소여부
	private int rownum;
	private boolean top;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankNumber() {
		return bankNumber;
	}
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	public TradeStatus getStatus() {
		return status;
	}
	public void setStatus(TradeStatus status) {
		this.status = status;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerCharacter() {
		return sellerCharacter;
	}
	public void setSellerCharacter(String sellerCharacter) {
		this.sellerCharacter = sellerCharacter;
	}
	public String getSellerPhone() {
		return sellerPhone;
	}
	public void setSellerPhone(String sellerPhone) {
		this.sellerPhone = sellerPhone;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getBuyerCharacter() {
		return buyerCharacter;
	}
	public void setBuyerCharacter(String buyerCharacter) {
		this.buyerCharacter = buyerCharacter;
	}
	public String getBuyerPhone() {
		return buyerPhone;
	}
	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}
	public Timestamp getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	public boolean isSend() {
		return send;
	}
	public void setSend(boolean send) {
		this.send = send;
	}
	public boolean isReceive() {
		return receive;
	}
	public void setReceive(boolean receive) {
		this.receive = receive;
	}
	public Timestamp getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}
	public boolean isSellerCancle() {
		return sellerCancle;
	}
	public void setSellerCancle(boolean sellerCancle) {
		this.sellerCancle = sellerCancle;
	}
	public boolean isBuyerCancle() {
		return buyerCancle;
	}
	public void setBuyerCancle(boolean buyerCancle) {
		this.buyerCancle = buyerCancle;
	}
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public boolean isTop() {
		return top;
	}
	public void setTop(boolean top) {
		this.top = top;
	}
}

