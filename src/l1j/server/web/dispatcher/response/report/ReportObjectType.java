package l1j.server.web.dispatcher.response.report;

import l1j.server.web.dispatcher.response.board.BoardVO;
import l1j.server.web.dispatcher.response.content.ContentVO;
import l1j.server.web.dispatcher.response.pitch.PitchVO;
import l1j.server.web.dispatcher.response.trade.TradeVO;

public enum ReportObjectType {
	BOARD,
	TRADE,
	CONTENT,
	PTICH,
	;
	public static ReportObjectType fromObj(Object obj) {
		if (obj instanceof BoardVO) {
			return BOARD;
		}
		if (obj instanceof TradeVO) {
			return TRADE;
		}
		if (obj instanceof ContentVO) {
			return CONTENT;
		}
		if (obj instanceof PitchVO) {
			return PTICH;
		}
		throw new IllegalArgumentException(String.format("invalid arguments ReportObjectType"));
	}
}

