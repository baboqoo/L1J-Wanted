package l1j.server.common.bin.craft;

public class CraftOutputItemResult {
	public CraftOutputItemResult(CraftOutputItem outputItem, boolean isSuccess) {
		this.outputItem	= outputItem;
		this.isSuccess	= isSuccess;
	}

	public CraftOutputItem outputItem;
	public boolean isSuccess;
}

