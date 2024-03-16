package l1j.server.web.dispatcher.response.coupon;

public enum CouponType {
	VOUCHER(			"VOUCHER",		"100 Million Adena Voucher ",	" units"),
	NCOIN(		"NCOIN",	"NCOIN ",	" coin"),
	NPOINT(		"NPOINT",	"NPOINT ",	" point"),
	;
	private String _flag;
	private String _desc;
	private String _extension;
	CouponType(String flag, String desc, String extension) {
		_flag		= flag;
		_desc		= desc;
		_extension	= extension;
	}
	public String toFlag() {
		return _flag;
	}
	public String toDesc() {
		return _desc;
	}
	public String toExtension() {
		return _extension;
	}
	
	public static CouponType fromString(String val) {
		switch (val) {
		case "VOUCHER":
			return VOUCHER;
		case "NCOIN":
			return NCOIN;
		case "NPOINT":
			return NPOINT;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments CouponType, %s", val));
		}
	}
}

