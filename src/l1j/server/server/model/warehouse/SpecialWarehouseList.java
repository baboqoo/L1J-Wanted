package l1j.server.server.model.warehouse;

// 특수창고
public class SpecialWarehouseList extends WarehouseList {
	@Override
	protected SpecialWarehouse createWarehouse(String name) {
		return new SpecialWarehouse(name);
	}
}

