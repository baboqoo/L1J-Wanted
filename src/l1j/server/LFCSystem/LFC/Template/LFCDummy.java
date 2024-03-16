package l1j.server.LFCSystem.LFC.Template;

public class LFCDummy extends LFCObject{
	public static LFCDummy createInstance(){
		return new LFCDummy();
	}
	
	@Override
	public void init(){
	}
	
	@Override
	public void run() {
		System.out.println("why dummy run???");
	}
	
	@Override
	public void close(){
	}
	
	@Override
	public String getName(){
		return "LFCDummy";
	}
}

