package l1j.server.server.model.warehouse;

public class WarehouseManager {	
	private static WarehouseManager uniqueInstance	= null;
	private PrivateWarehouseList plist				= new PrivateWarehouseList();
	private ElfWarehouseList elist					= new ElfWarehouseList();
	private ClanWarehouseList clist					= new ClanWarehouseList();
	private PresentWarehouseList prlist				= new PresentWarehouseList();
	private PackageWarehouseList packlist			= new PackageWarehouseList();
	private SpecialWarehouseList speciallist		= new SpecialWarehouseList(); 
	
	public static WarehouseManager getInstance() {
		if (uniqueInstance == null) {
			synchronized(ElfWarehouseList.class){
				if (uniqueInstance == null) {
					uniqueInstance = new WarehouseManager();
				}
			}
		}
		return uniqueInstance;
	}
	
	public PrivateWarehouse getPrivateWarehouse(String name) {
		return (PrivateWarehouse) plist.findWarehouse(name);
	}	
	public ElfWarehouse getElfWarehouse(String name) {
		return (ElfWarehouse) elist.findWarehouse(name);
	}
	public PackageWarehouse getPackageWarehouse(String name) {
		return (PackageWarehouse) packlist.findWarehouse(name);
	}
	public SpecialWarehouse getSpecialWarehouse(String name) {
		return (SpecialWarehouse) speciallist.findWarehouse(name);
	}		
	public ClanWarehouse getClanWarehouse(String name) {
		return (ClanWarehouse) clist.findWarehouse(name);
	}	
	public PresentWarehouse getPresentWarehouse(String name) {
		return (PresentWarehouse) prlist.findWarehouse(name);
	}
	
	public void delPrivateWarehouse(String name) {
		plist.delWarehouse(name);
	}	
	public void delElfWarehouse(String name) {
		elist.delWarehouse(name);
	}	
	public void delClanWarehouse(String name) {
		clist.delWarehouse(name);
	}	
	public void delPresentWarehouse(String name) {
		prlist.delWarehouse(name);
	}
	public void delPackageWarehouse(String name) {
		packlist.delWarehouse(name);
	}
	public void delSpecialWarehouse(String name) {
		speciallist.delWarehouse(name);
	}
}

