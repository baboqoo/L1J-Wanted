package l1j.server.web.dispatcher.response.goods;

import l1j.server.server.utils.StringUtil;

public enum GoodsFlag {
	NONE(			StringUtil.EmptyString, 																					StringUtil.EmptyString),
	/*DISCOUNT(		"<div class=\"flag\"><img src=\"/img/goods/discount.png\" width=\"54\" height=\"63\" alt=\"\"></div>",		"<div class=\"flag\"><img src=\"/img/goods/discount.png\" alt=\"\"></div>"),
	ESSENTIAL(		"<div class=\"flag\"><img src=\"/img/goods/essential.png\" width=\"54\" height=\"63\" alt=\"\"></div>",		"<div class=\"flag\"><img src=\"/img/goods/essential.png\" alt=\"\"></div>"),
	HOT(			"<div class=\"flag\"><img src=\"/img/goods/hot.png\" width=\"54\" height=\"63\" alt=\"\"></div>",			"<div class=\"flag\"><img src=\"/img/goods/hot.png\" alt=\"\"></div>"),
	LIMIT(			"<div class=\"flag\"><img src=\"/img/goods/limit.png\" width=\"54\" height=\"63\" alt=\"\"></div>",			"<div class=\"flag\"><img src=\"/img/goods/limit.png\" alt=\"\"></div>"),
	LIMIT_MONTH(	"<div class=\"flag\"><img src=\"/img/goods/limit_month.png\" width=\"54\" height=\"63\" alt=\"\"></div>",	"<div class=\"flag\"><img src=\"/img/goods/limit_month.png\" alt=\"\"></div>"),
	LIMIT_WEEK(		"<div class=\"flag\"><img src=\"/img/goods/limit_week.png\" width=\"54\" height=\"63\" alt=\"\"></div>",	"<div class=\"flag\"><img src=\"/img/goods/limit_week.png\" alt=\"\"></div>"),
	NEW(			"<div class=\"flag\"><img src=\"/img/goods/new.png\" width=\"54\" height=\"63\" alt=\"\"></div>",			"<div class=\"flag\"><img src=\"/img/goods/new.png\" alt=\"\"></div>"),
	REDKNIGHT(		"<div class=\"flag\"><img src=\"/img/goods/redknight.png\" width=\"54\" height=\"63\" alt=\"\"></div>",		"<div class=\"flag\"><img src=\"/img/goods/redknight.png\" alt=\"\"></div>"),*/

	DISCOUNT(		
		"<div class=\"flag fdiscount\"><img src=\"/img/goods/media_orange.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Discount</span></div>",
		"<div class=\"flag fdiscount\"><img src=\"/img/goods/media_orange_big.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Discount</span></div>"),
	ESSENTIAL(		
		"<div class=\"flag fessential\"><img src=\"/img/goods/media_blue.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Essential</span></div>",
		"<div class=\"flag fessential\"><img src=\"/img/goods/media_blue_big.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Essential</span></div>"),
	HOT(			
		"<div class=\"flag fhot\"><img src=\"/img/goods/media_red.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Hot!</span></div>",
		"<div class=\"flag fhot\"><img src=\"/img/goods/media_red_big.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Hot!</span></div>"),
	LIMIT(			
		"<div class=\"flag flimit\"><img src=\"/img/goods/media_brown.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Limited</span></div>",
		"<div class=\"flag flimit\"><img src=\"/img/goods/media_brown_big.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Limited</span></div>"),
	LIMIT_MONTH(	
		"<div class=\"flag fmonthlimit\"><img src=\"/img/goods/media_green.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Month Limit</span></div>",
		"<div class=\"flag fmonthlimit\"><img src=\"/img/goods/media_green_big.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Month Limit</span></div>"),
	LIMIT_WEEK(		
		"<div class=\"flag fweeklimit\"><img src=\"/img/goods/media_green2.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Week Limit</span></div>",
		"<div class=\"flag fweeklimit\"><img src=\"/img/goods/media_green2_big.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">Week Limit</span></div>"),
	NEW(			
		"<div class=\"flag fnew\"><img src=\"/img/goods/media_blue2.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">New</span></div>",
		"<div class=\"flag fnew\"><img src=\"/img/goods/media_blue2_big.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">New</span></div>"),
	REDKNIGHT(		
		"<div class=\"flag fredknight\"><img src=\"/img/goods/media_pink.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">RedKnight</span></div>",
		"<div class=\"flag fredknight\"><img src=\"/img/goods/media_pink_big.png\" alt=\"\"></div><div class=\"text_flag_div\"><span class=\"text_flag\">RedKnight</span></div>"),	

	;
	private String _tag_1;
	private String _tag_2;
	GoodsFlag(String tag_1, String tag_2) {
		_tag_1 = tag_1;
		_tag_2 = tag_2;
	}
	public String getTag_1() {
		return _tag_1;
	}
	public String getTag_2() {
		return _tag_2;
	}
	public static GoodsFlag fromString(String str) {
		switch (str) {
		case "DISCOUNT":
			return DISCOUNT;
		case "ESSENTIAL":
			return ESSENTIAL;
		case "HOT":
			return HOT;
		case "LIMIT":
			return LIMIT;
		case "LIMIT_MONTH":
			return LIMIT_MONTH;
		case "LIMIT_WEEK":
			return LIMIT_WEEK;
		case "NEW":
			return NEW;
		case "REDKNIGHT":
			return REDKNIGHT;
		default:
			return NONE;
		}
	}
}

