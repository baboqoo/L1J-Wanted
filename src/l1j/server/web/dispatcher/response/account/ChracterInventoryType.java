package l1j.server.web.dispatcher.response.account;

public enum ChracterInventoryType {
	INVENTOY("inven"),
	NORMAL_WAREHOUSE("normal"),
	PACKAGE_WAREHOUSE("package")
	;
	private String type;
	ChracterInventoryType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	
	public static ChracterInventoryType getType(String str) {
		switch(str) {
		case "normal":return NORMAL_WAREHOUSE;
		case "package":return PACKAGE_WAREHOUSE;
		default:return INVENTOY;
		}
	}
}

