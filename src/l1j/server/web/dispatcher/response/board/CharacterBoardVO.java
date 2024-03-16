package l1j.server.web.dispatcher.response.board;

public class CharacterBoardVO {
	private String boardType;
	private Object board;
	private String boardUrl;
	// not used so... we can delete the board type
	public String getBoardType() {
		return boardType;
	}
	public void setBoardType(String boardType) {
		this.boardType = boardType;
	}
	public Object getBoard() {
		return board;
	}
	public void setBoard(Object board) {
		this.board = board;
	}
	public String getBoardUrl() {
		return boardUrl;
	}
	public void setBoardUrl(String boardUrl) {
		this.boardUrl = boardUrl;
	}
}

