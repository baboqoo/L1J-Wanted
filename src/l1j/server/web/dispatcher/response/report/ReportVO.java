package l1j.server.web.dispatcher.response.report;

import java.sql.Timestamp;

public class ReportVO {
	private int id;
	private String name;
	private String targetName;
	private ReportType type;
	private String log;
	private Timestamp date;
	private Object obj;
	private ReportObjectType obj_type;
	
	public ReportVO(int id, String name, String targetName, ReportType type, String log, Timestamp date, Object obj) {
		this.id			= id;
		this.name		= name;
		this.targetName	= targetName;
		this.type		= type;
		this.log		= log;
		this.date		= date;
		this.obj		= obj;
		this.obj_type	= ReportObjectType.fromObj(obj);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public ReportType getType() {
		return type;
	}
	public void setType(ReportType type) {
		this.type = type;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public Object getObj() {
		return obj;
	}
	public ReportObjectType getObjType() {
		return obj_type;
	}
}

