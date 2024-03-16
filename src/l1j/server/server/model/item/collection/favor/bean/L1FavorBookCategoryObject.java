package l1j.server.server.model.item.collection.favor.bean;

import java.sql.Timestamp;

import l1j.server.common.bin.favorbook.AUBIBookInfoForClient;
import l1j.server.server.utils.StringUtil;

public class L1FavorBookCategoryObject {
	private int category;
	private String desc;
	private Timestamp startDate;
	private Timestamp endDate;
	private String startDateToString;
	private String endDateToString;
	private AUBIBookInfoForClient.BookT.CategoryT categoryT;
	
	public L1FavorBookCategoryObject(AUBIBookInfoForClient.BookT.CategoryT categoryT) {
		this.category				= categoryT.get_id();
		this.desc					= categoryT.get_desc();
		if (!StringUtil.isNullOrEmpty(categoryT.get_start_date())) {
			if (categoryT.get_start_date().length() == 16) {
				this.startDate			= Timestamp.valueOf(categoryT.get_start_date() + ":00");
				this.startDateToString	= categoryT.get_start_date() + ":00";
			} else {
				this.startDate			= Timestamp.valueOf(categoryT.get_start_date());
				this.startDateToString	= categoryT.get_start_date();
			}
		}
		if (!StringUtil.isNullOrEmpty(categoryT.get_end_date())) {
			if (categoryT.get_end_date().length() == 16) {
				this.endDate			= Timestamp.valueOf(categoryT.get_end_date() + ":00");
				this.endDateToString	= categoryT.get_end_date() + ":00";
			} else {
				this.endDate			= Timestamp.valueOf(categoryT.get_end_date());
				this.endDateToString	= categoryT.get_end_date();
			}
		}
		this.categoryT = categoryT;
	}
	
	public int getCategory() {
		return category;
	}
	public String getDesc() {
		return desc;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public String getStartDateToString() {
		return startDateToString;
	}
	public String getEndDateToString() {
		return endDateToString;
	}
	public AUBIBookInfoForClient.BookT.CategoryT getCategoryT() {
		return categoryT;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("category : ").append(category).append(StringUtil.LineString);
		sb.append("desc : ").append(desc).append(StringUtil.LineString);
		if (startDate != null) {
			sb.append("startDate : ").append(startDateToString).append(StringUtil.LineString);
		}
		if (endDate != null) {
			sb.append("endDate : ").append(endDateToString).append(StringUtil.LineString);
		}
		return sb.toString();
	}
}

