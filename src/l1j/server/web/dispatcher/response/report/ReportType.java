package l1j.server.web.dispatcher.response.report;

public enum ReportType {
	/*LEWDNESS(				1,	"음란성"),
	ABUSIVE(				2,	"욕설"),
	ADVERTISE(				3,	"일반광고"),
	ILLEGALITY_PROGRAM(		4,	"불법프로그램"),
	OVER_CONTENT(			5,	"도배"),
	PERSONAL_INFORMATION(	6,	"개인정보"),
	SPEAK_ILL(				7,	"타인비방"),
	ETC(					0,	"기타");*/
	LEWDNESS(				1,	"Sexual"),
	ABUSIVE(				2,	"Abuse"),
	ADVERTISE(				3,	"Advertisement"),
	ILLEGALITY_PROGRAM(		4,	"Illegal Program"),
	OVER_CONTENT(			5,	"Content"),
	PERSONAL_INFORMATION(	6,	"Privacy"),
	SPEAK_ILL(				7,	"Lies"),
	ETC(					0,	"Other");	
	private int code;
	private String desc;
	ReportType(int code, String desc) {
		this.code	= code;
		this.desc	= desc;
	}
	public int get_code() {
		return code;
	}
	public String get_desc() {
		return desc;
	}
	
	public static ReportType fromInt(int val) {
		switch (val) {
		case 0:
			return ETC;
		case 1:
			return LEWDNESS;
		case 2:
			return ABUSIVE;
		case 3:
			return ADVERTISE;
		case 4:
			return ILLEGALITY_PROGRAM;
		case 5:
			return OVER_CONTENT;
		case 6:
			return PERSONAL_INFORMATION;
		case 7:
			return SPEAK_ILL;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments ReportType, %d", val));
		}
	}
	
	public static ReportType fromString(String val) {
		switch (val) {
		/*case "기타":
			return ETC;
		case "음란성":
			return LEWDNESS;
		case "욕설":
			return ABUSIVE;
		case "일반광고":
			return ADVERTISE;
		case "불법프로그램":
			return ILLEGALITY_PROGRAM;
		case "도배":
			return OVER_CONTENT;
		case "개인정보":
			return PERSONAL_INFORMATION;
		case "타인비방":
			return SPEAK_ILL;*/
		case "Other":
			return ETC;
		case "Sexual":
			return LEWDNESS;
		case "Abuse":
			return ABUSIVE;
		case "Advertisement":
			return ADVERTISE;
		case "Illegal Program":
			return ILLEGALITY_PROGRAM;
		case "Content":
			return OVER_CONTENT;
		case "Privacy":
			return PERSONAL_INFORMATION;
		case "Lies":
			return SPEAK_ILL;			
		default:
			throw new IllegalArgumentException(String.format("invalid arguments ReportType, %s", val));
		}
	}
}

