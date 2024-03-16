package l1j.server.server.model.shop;

public class L1AssessedItem {
	private final int _targetId;
	private final int _assessedPrice;

	L1AssessedItem(int targetId, int assessedPrice) {
		_targetId = targetId;
		_assessedPrice = assessedPrice;
	}

	public int getTargetId() {
		return _targetId;
	}

	public int getAssessedPrice() {
		return _assessedPrice;
	}
}

