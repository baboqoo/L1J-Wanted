package l1j.server.server.monitor;

public class LoggerInstance extends FileLogger implements l1j.server.server.controller.action.ControllerInterface {
	private static class newInstance {
		public static final LoggerInstance INSTANCE = new LoggerInstance();
	}
	public static LoggerInstance getInstance() {
		return newInstance.INSTANCE;
	}
	private LoggerInstance() {
	}
	
	@Override
	public void execute() {
		try {
			flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}
}
