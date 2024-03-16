package l1j.server.server.model.warehouse;

public class PackageWarehouseList extends WarehouseList {
	@Override
	protected PackageWarehouse createWarehouse(String name) {
		return new PackageWarehouse(name);
	}
}

